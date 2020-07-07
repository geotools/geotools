/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2011, Open Source Geospatial Foundation (OSGeo)
 *    (C) 2001-2007 TOPP - www.openplans.org.
 *
 *    This library is free software; you can redistribute it and/or
 *    modify it under the terms of the GNU Lesser General Public
 *    License as published by the Free Software Foundation;
 *    version 2.1 of the License.
 *
 *    This library is distributed in the hope that it will be useful,
 *    but WITHOUT ANY WARRANTY; without even the implied warranty of
 *    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 *    Lesser General Public License for more details.
 */
package org.geotools.process.vector;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.geotools.data.collection.ListFeatureCollection;
import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.data.simple.SimpleFeatureIterator;
import org.geotools.feature.simple.SimpleFeatureBuilder;
import org.geotools.feature.simple.SimpleFeatureTypeBuilder;
import org.geotools.geometry.jts.JTS;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.process.ProcessException;
import org.geotools.process.factory.DescribeParameter;
import org.geotools.process.factory.DescribeProcess;
import org.geotools.process.factory.DescribeResult;
import org.geotools.referencing.CRS;
import org.json.simple.JSONArray;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.referencing.FactoryException;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.opengis.referencing.operation.MathTransform;
import org.opengis.referencing.operation.TransformException;
import org.opengis.util.ProgressListener;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Envelope;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.Point;
import com.vividsolutions.jts.geom.Polygon;
import com.vividsolutions.jts.geom.impl.PackedCoordinateSequenceFactory;

import java.util.logging.Logger;

/**
 * A Rendering Transformation process which aggregates features into a set of visually
 * non-conflicting point features. The created points have attributes which provide the total number
 * of points aggregated, as well as the number of unique point locations.
 *
 * <p>This is sometimes called "point clustering". The term stacking is used instead, since
 * clustering has multiple meanings in geospatial processing - it is also used to mean identifying
 * groups defined by point proximity.
 *
 * <p>The stacking is defined by specifying a grid to aggregate to. The grid cell size is specified
 * in pixels relative to the requested output image size. This makes it more intuitive to pick an
 * appropriate grid size, and ensures that the aggregation works at all zoom levels.
 *
 * <p>The output is a FeatureCollection containing the following attributes:
 *
 * <ul>
 *   <li><code>geom</code> - the point representing the cluster
 *   <li><code>count</code> - the total number of points in the cluster
 *   <li><code>countUnique</code> - the number of unique point locations in the cluster
 * </ul>
 *
 * Note that as required by the Rendering Transformation API, the output has the CRS of the input
 * data.
 *
 * @author mdavis
 * @author Cosmin Cioranu (cncioranu)
 */
@DescribeProcess(
    title = "Point Stacker",
    description = "Aggregates a collection of points over a grid into one point per grid cell.")
public class PointStackerProcess implements VectorProcess {
  /** The logger for the rendering module. */
  private static final Logger LOGGER =
      org.geotools.util.logging.Logging.getLogger("org.geotools.process.vector");

  public enum PreserveLocation {
    /** Preserves the original point location in case there is a single point in the cell */
    Single,
    /**
     * Preserves the original point location in case there are multiple points, but all with the
     * same coordinates in the cell
     */
    Superimposed,
    /**
     * Default value, averages the point locations with the cell center to try and avoid conflicts
     * among the symbolizers for the
     */
    Never
  };

  public enum ClusterType {
    /** Grid X/Y, center point is centered */
    GridCenter,
    /** Grid X/Y, center point weighted */
    GridWeight,
    /** Grid X/Y, center point is weighted at maximum (cellSize/2) */
    GridWeightHalf,
    /** Grid X/Y, center point is Nearest to the center */
    GridNearest,
    /**
     * Natural Clustering, center point is weighted Natural clustering. It is using a greedy
     * approach in order to be fast. Due to it's implementation, cluster might be broken down in
     * pieced and regrouped.
     */
    Natural
  };

  public enum ComputeBBoxType {
    /** Compute output BBox and and area as original source */
    Original,
    /** compute output BBox and area as map */
    Map,
  };

  public static final String ATTR_GEOM = "geom";
  public static final String ATTR_COUNT = "count";
  public static final String ATTR_COUNT_UNIQUE = "countUnique";
  public static final String ATTR_NORM_COUNT = "normCount";
  public static final String ATTR_NORM_COUNT_UNIQUE = "normCountUnique";

  /** area, internally it is a Polygon */
  public static final String ATTR_BOUNDING_AREA = "computedArea";

  /** bounding box */
  public static final String ATTR_BOUNDING_BOX = "computedBBox";

  public static final String ATTR_CLUSTERED_VALUES = "clusteredAttributes";

  // TODO: add ability to pick index point selection strategy
  // TODO: add ability to set attribute name containing value to be aggregated
  // TODO: add ability to specify aggregation method (COUNT, SUM, AVG)
  // TODO: ultimately could allow aggregating multiple input attributes, with
  // different methods for each
  // TODO: allow including attributes from input data (eg for use with points
  // that are not aggregated)
  // TODO: expand query window to avoid edge effects?

  // no process state is defined, since RenderingTransformation processes must
  // be stateless

  @DescribeResult(name = "result", description = "Aggregated feature collection")
  public SimpleFeatureCollection execute(

      // process data
      @DescribeParameter(name = "data", description = "Input feature collection")
          SimpleFeatureCollection data,

      // process parameters
      @DescribeParameter(
              name = "cellSize",
              description =
                  "In grid systetems, this is the size of grid (in pixesls) in natural clustering this is the diameter of cell (in pixels)")
          Integer cellSize,
      @DescribeParameter(
              name = "weightClusterPosition",
              description =
                  "Weight cluster position based on points added. This flag is deprecated. The equivalent is clusterType=GridWeight",
              defaultValue = "false")
          Boolean argWeightClusterPosition,
      @DescribeParameter(
              name = "normalize",
              description = "Indicates whether to add fields normalized to the range 0-1.",
              defaultValue = "false")
          Boolean argNormalize,
      @DescribeParameter(
              name = "preserveLocation",
              description =
                  "Indicates wheter to preserve the original location of points for single/superimposed points",
              defaultValue = "Never",
              min = 0)
          PreserveLocation preserveLocation,
      @DescribeParameter(
              name = "collectClusterAttributeName",
              description = "Return the specfied attribute for each feature in the cluster",
              defaultValue = "")
          String returnClusteredAttribute,
      @DescribeParameter(
              name = "clusterType",
              description = "Specify the clusterization method",
              defaultValue = "GridCenter")
          ClusterType clusterType,
      @DescribeParameter(
              name = "computeBBox",
              description =
                  "Compute BBox of the cluster and return it in data set as geom structure",
              defaultValue = "false")
          Boolean argComputeBBox,
      @DescribeParameter(
              name = "computeBBoxType",
              description = "Compute BBox Type",
              defaultValue = "Original")
          ComputeBBoxType argComputeBBoxType,

      // output image parameters
      @DescribeParameter(name = "outputBBOX", description = "Bounding box for target image extent")
          ReferencedEnvelope outputEnv,
      @DescribeParameter(
              name = "outputWidth",
              description = "Target image width in pixels",
              minValue = 1)
          Integer outputWidth,
      @DescribeParameter(
              name = "outputHeight",
              description = "Target image height in pixels",
              minValue = 1)
          Integer outputHeight,
      ProgressListener monitor)
      throws ProcessException, TransformException {

    CoordinateReferenceSystem srcCRS = data.getSchema().getCoordinateReferenceSystem();
    CoordinateReferenceSystem dstCRS = outputEnv.getCoordinateReferenceSystem();
    MathTransform crsTransform = null;
    MathTransform invTransform = null;
    try {
      crsTransform = CRS.findMathTransform(srcCRS, dstCRS);
      invTransform = crsTransform.inverse();
    } catch (FactoryException e) {
      throw new ProcessException(e);
    }

    boolean normalize = false;
    if (argNormalize != null) {
      normalize = argNormalize;
    }

    boolean weightClusterPosition = false;
    if (argWeightClusterPosition != null) {
      weightClusterPosition = argWeightClusterPosition;
    }

    if (weightClusterPosition) {
      clusterType = ClusterType.GridWeight;
    }

    boolean computeBBox = false;
    if (argComputeBBox != null) {
      computeBBox = argComputeBBox;
    }

    // TODO: allow output CRS to be different to data CRS
    // assume same CRS for now...
    double cellSizeSrc = cellSize * outputEnv.getWidth() / outputWidth;

    Collection<StackedPoint> stackedPts =
        stackPoints(
            data,
            crsTransform,
            cellSizeSrc,
            outputEnv.getMinX(),
            outputEnv.getMinY(),
            clusterType,
            returnClusteredAttribute);

    SimpleFeatureType schema = createType(srcCRS, normalize);
    ListFeatureCollection result = new ListFeatureCollection(schema);
    SimpleFeatureBuilder fb = new SimpleFeatureBuilder(schema);

    GeometryFactory factory = new GeometryFactory(new PackedCoordinateSequenceFactory());
    double[] srcPt = new double[2];
    double[] dstPt = new double[2];

    // Find maxima of the point stacks if needed.
    int maxCount = 0;
    int maxCountUnique = 0;
    if (normalize) {
      for (StackedPoint sp : stackedPts) {
        if (maxCount < sp.getCount()) maxCount = sp.getCount();
        if (maxCountUnique < sp.getCount()) maxCountUnique = sp.getCountUnique();
      }
    }
    // get all stecked points and create features.
    for (StackedPoint sp : stackedPts) {
      // create feature for stacked point
      Coordinate pt = getStackedPointLocation(preserveLocation, sp);

      // transform back to src CRS, since RT rendering expects the output
      // to be in the same CRS
      srcPt[0] = pt.x;
      srcPt[1] = pt.y;
      invTransform.transform(srcPt, 0, dstPt, 0, 1);
      Coordinate psrc = new Coordinate(dstPt[0], dstPt[1]);

      Geometry point = factory.createPoint(psrc);
      fb.add(point);
      fb.add(sp.getCount());
      fb.add(sp.getCountUnique());
      if (computeBBox) {
        // adding bounding box of the points staked, as geometry
        // envelope transformation
        // Envelope boundingBox = sp.getBoundingBox(invTransform);
        /*
        srcPt[0] = boundingBox.getMinX();
        srcPt[1] = boundingBox.getMinY();
        srcPt2[0] = boundingBox.getMaxX();
        srcPt2[1] = boundingBox.getMaxY();

        invTransform.transform(srcPt, 0, dstPt, 0, 1);
        invTransform.transform(srcPt2, 0, dstPt2, 0, 1);
        Envelope boundingBoxTransformed = new Envelope(dstPt[0], dstPt[1], dstPt2[0], dstPt2[1]);
        */
        if (argComputeBBoxType == ComputeBBoxType.Original) {
          fb.add(sp.getPoligon(invTransform));
          fb.add(sp.getBoundingBoxList(invTransform));
        } else if (argComputeBBoxType == ComputeBBoxType.Map) {
          fb.add(sp.getPoligon());
          fb.add(sp.getBoundingBoxList(null));
        } else {
          fb.add(null);
          fb.add(null);
        }
        // adding bounding box of the points staked, as string
        // fb.add(boundingBoxTransformed.toString()); //not used
      } else {
        // we need to maintain the order of the fields.
        fb.add(null);
        fb.add(null);
      }
      if (normalize) {
        fb.add(((double) sp.getCount()) / maxCount);
        fb.add(((double) sp.getCountUnique()) / maxCountUnique);
      }
      fb.add(sp.getClusteredAttributeValues());
      result.add(fb.buildFeature(null));
    }
    return result;
  }

  /**
   * Extract the geometry depending on the location preservation flag
   *
   * @param preserveLocation
   * @param sp
   * @return
   */
  private Coordinate getStackedPointLocation(PreserveLocation preserveLocation, StackedPoint sp) {
    Coordinate pt = null;
    if (PreserveLocation.Single == preserveLocation) {
      if (sp.getCount() == 1) {
        pt = sp.getOriginalLocation();
      }
    } else if (PreserveLocation.Superimposed == preserveLocation) {
      if (sp.getCountUnique() == 1) {
        pt = sp.getOriginalLocation();
      }
    }
    if (pt == null) {
      pt = sp.getLocation();
    }
    return pt;
  }

  /**
   * Computes the stacked points for the given data collection. All geometry types are handled - for
   * non-point geometries, the centroid is used.
   *
   * @param data
   * @param cellSize
   * @param minX
   * @param minY
   * @return
   * @throws TransformException
   */
  private Collection<StackedPoint> stackPoints(
      SimpleFeatureCollection data,
      MathTransform crsTransform,
      double cellSize,
      double minX,
      double minY,
      ClusterType clusterType,
      String returnClusteredAttribute)
      throws TransformException {
    SimpleFeatureIterator featureIt = data.features();

    Map<Coordinate, StackedPoint> stackedPts = new HashMap<Coordinate, StackedPoint>();

    double[] srcPt = new double[2];
    double[] dstPt = new double[2];

    Coordinate indexPt = new Coordinate();
    try {
      while (featureIt.hasNext()) {
        SimpleFeature feature = featureIt.next();
        // get the point location from the geometry
        Geometry geom = (Geometry) feature.getDefaultGeometry();
        Coordinate p = getRepresentativePoint(geom);

        // reproject data point to output CRS, if required
        srcPt[0] = p.x;
        srcPt[1] = p.y;
        crsTransform.transform(srcPt, 0, dstPt, 0, 1);
        Coordinate pout = new Coordinate(dstPt[0], dstPt[1]);

        indexPt.x = pout.x;
        indexPt.y = pout.y;

        StackedPoint stkPt = null;
        if (clusterType == ClusterType.Natural) {
          // clustering is natural
          // searching for best cluster using center point
          // in case of Natural Clustering the cellSize is the
          // diameter
          double r = cellSize / 2;
          StackedPoint sTemp = null;
          double distanceTemp = Double.MAX_VALUE;
          boolean found = false;
          for (Map.Entry<Coordinate, StackedPoint> entry : stackedPts.entrySet()) {
            Coordinate lc = entry.getKey();
            // Coordinate lc=entry.getValue().getLocation(); is
            // correct but it needs a second pas?!
            double d = lc.distance(pout);
            if (d <= r) {
              // an already staked point exists so return it.
              // foound best match.
              // due to it's greedy fashion iterate though all
              // clusters.
              if (d < distanceTemp) {
                sTemp = entry.getValue();
                distanceTemp = d;
                found = true;
              }
              // break;
            }
          }
          if (found) {
            stkPt = sTemp;
          }
          if (stkPt == null) {
            // no staked point found, so create new cluster
            double centreX = indexPt.x * cellSize;
            double centreY = indexPt.y * cellSize;
            stkPt = new StackedPoint(indexPt, new Coordinate(centreX, centreY), clusterType, r);
            stackedPts.put(stkPt.getKey(), stkPt);
          }

        } else {
          // if clustering is grid based
          gridIndex(indexPt, cellSize);
          stkPt = stackedPts.get(indexPt);
          if (stkPt == null) {
            double centreX = indexPt.x * cellSize + cellSize / 2;
            double centreY = indexPt.y * cellSize + cellSize / 2;

            stkPt =
                new StackedPoint(indexPt, new Coordinate(centreX, centreY), clusterType, cellSize);
            stackedPts.put(stkPt.getKey(), stkPt);
          }
        }
        stkPt.add(pout);
        if (returnClusteredAttribute != "") {
          stkPt.addClusteredAttribute(feature.getAttribute(returnClusteredAttribute));
        }
      }

    } finally {
      featureIt.close();
    }
    return stackedPts.values();
  }

  /**
   * Gets a point to represent the Geometry. If the Geometry is a point, this is returned.
   * Otherwise, the centroid is used.
   *
   * @param g the geometry to find a point for
   * @return a point representing the Geometry
   */
  private static Coordinate getRepresentativePoint(Geometry g) {
    if (g.getNumPoints() == 1) return g.getCoordinate();
    return g.getCentroid().getCoordinate();
  }

  /**
   * Computes the grid index for a point for the grid determined by the cellsize.
   *
   * @param griddedPt the point to grid, and also holds the output value
   * @param cellSize the grid cell size
   */
  private void gridIndex(Coordinate griddedPt, double cellSize) {

    // TODO: is there any situation where this could result in too much loss
    // of precision?
    /**
     * The grid is based at the origin of the entire data space, not just the query window. This
     * makes gridding stable during panning.
     *
     * <p>This should not lose too much precision for any reasonable coordinate system and map size.
     * The worst case is a CRS with small ordinate values, and a large cell size. The worst case
     * tested is a map in degrees, zoomed out to show about twice the globe - works fine.
     */
    // Use longs to avoid possible overflow issues (e.g. for a very small
    // cell size)
    long ix = (long) ((griddedPt.x) / cellSize);
    long iy = (long) ((griddedPt.y) / cellSize);

    griddedPt.x = ix;
    griddedPt.y = iy;
  }

  private SimpleFeatureType createType(CoordinateReferenceSystem crs, boolean stretch) {
    SimpleFeatureTypeBuilder tb = new SimpleFeatureTypeBuilder();
    tb.add(ATTR_GEOM, Point.class, crs);
    tb.add(ATTR_COUNT, Integer.class);
    tb.add(ATTR_COUNT_UNIQUE, Integer.class);
    tb.add(ATTR_BOUNDING_AREA, Geometry.class);
    tb.add(ATTR_BOUNDING_BOX, List.class);

    if (stretch) {
      tb.add(ATTR_NORM_COUNT, Double.class);
      tb.add(ATTR_NORM_COUNT_UNIQUE, Double.class);
    }
    tb.add(ATTR_CLUSTERED_VALUES, JSONArray.class);
    tb.setName("stackedPoint");
    SimpleFeatureType sfType = tb.buildFeatureType();
    return sfType;
  }

  private static class StackedPoint {
    /** First Element */
    private Coordinate first = null;
    /** Differential Coordinate X */
    private double dx = 0;
    /** Differential Coordinate Y */
    private double dy = 0;

    /** Cell Size */
    private double cellSize = 0;

    /** Internal Key */
    private Coordinate key;
    /** Default Value, but it will be overridden at initialization (constructor) */
    ClusterType clusterType = ClusterType.Natural;

    private Coordinate centerPt;

    private Coordinate location = null;

    private int count = 0;

    private Set<Coordinate> uniquePts;
    private ArrayList<Coordinate> allPts;

    /** Bounding box of the clustered points */
    private Envelope boundingBox = null;

    private JSONArray idsClustered = null;

    /**
     * Creates a new stacked point grid cell. The center point of the cell is supplied so that it
     * may be used as or influence the location of the final display point
     *
     * @param key a key for the grid cell (using integer ordinates to avoid precision issues)
     * @param centerPt the center point of the grid cell
     */
    public StackedPoint(
        Coordinate key, Coordinate centerPt, ClusterType clusterType, double cellSize) {
      this.key = new Coordinate(key);
      this.centerPt = centerPt;
      this.cellSize = cellSize;
      this.clusterType = clusterType;
    }

    public Coordinate getKey() {
      return key;
    }

    public Coordinate[] getClusterCoordinates() {
      return uniquePts.toArray(new Coordinate[uniquePts.size()]);
    }

    /**
     * Return Polygon from the data, original
     *
     * @return
     */
    public Geometry getPoligon() {
      GeometryFactory factory = new GeometryFactory(new PackedCoordinateSequenceFactory());
      allPts.add(allPts.get(0));
      Coordinate[] list = allPts.toArray(new Coordinate[] {});
      if (list.length < 4) {
        // cannot build a valid polygon so return the bounding box
        return JTS.toGeometry(this.getBoundingBox());
      }
      Polygon polygon = factory.createPolygon(list);
      return polygon.convexHull();
    }

    public Geometry getPoligon(MathTransform invTransform) {
      GeometryFactory factory = new GeometryFactory(new PackedCoordinateSequenceFactory());
      allPts.add(allPts.get(0));
      Coordinate[] list = allPts.toArray(new Coordinate[] {});
      if (list.length < 4) {
        // the polygon is not valid, so return the bounding box
        return JTS.toGeometry(this.getBoundingBox(invTransform));
      }
      double[] srcPt = new double[2];
      double[] dstPt = new double[2];
      try {
        for (int i = 0; i < list.length; i++) {
          srcPt[0] = list[i].x;
          srcPt[1] = list[i].y;
          invTransform.transform(srcPt, 0, dstPt, 0, 1);
          list[i] = new Coordinate(dstPt[0], dstPt[1]);
        }
        Polygon polygon = factory.createPolygon(list);
        return polygon.convexHull();
      } catch (TransformException e) {
        // return the bounding box
        return null;
        // return JTS.toGeometry(this.getBoundingBox(invTransform));
      }
    }

    /**
     * return the original bounding box
     *
     * @return
     */
    public Envelope getBoundingBox() {
      return boundingBox;
    }

    public List<Double> getBoundingBoxList(MathTransform invTransform) {
      Envelope x = null;
      if (invTransform == null) {
        x = this.getBoundingBox();
      } else {
        x = this.getBoundingBox(invTransform);
      }
      List<Double> output = new ArrayList<Double>();
      // double[] output=new double[4];
      output.add(x.getMinX());
      output.add(x.getMinY());
      output.add(x.getMaxX());
      output.add(x.getMaxY());
      return output;
    }
    /**
     * Return the bounding box in the system source env
     *
     * @param invTransform
     * @return
     */
    public Envelope getBoundingBox(MathTransform invTransform) {
      double[] srcPt = new double[2];
      double[] dstPt = new double[2];
      double[] srcPt2 = new double[2];
      double[] dstPt2 = new double[2];
      srcPt[0] = boundingBox.getMinX();
      srcPt[1] = boundingBox.getMinY();
      srcPt2[0] = boundingBox.getMaxX();
      srcPt2[1] = boundingBox.getMaxY();
      try {
        invTransform.transform(srcPt, 0, dstPt, 0, 1);
        invTransform.transform(srcPt2, 0, dstPt2, 0, 1);
        return new Envelope(dstPt[0], dstPt2[0], dstPt[1], dstPt2[1]);
      } catch (TransformException e) {
        // TODO Auto-generated catch block
        return null;
      }
    }

    public Coordinate getLocation() {
      return location;
    }

    public int getCount() {
      return count;
    }

    public int getCountUnique() {
      if (uniquePts == null) return 1;
      return uniquePts.size();
    }

    /** get the clustered attributes of the points that have been clustered */
    public JSONArray getClusteredAttributeValues() {
      return this.idsClustered;
    }

    /**
     * @todo change GeometryFactory
     * @param pt
     * @param clusterType
     */
    public void add(Coordinate pt) {
      if (first == null) {
        first = pt;
      }
      // GeometryFactory factory = new GeometryFactory(new
      // PackedCoordinateSequenceFactory());
      count++;
      /**
       * Only create set if this is the second point seen (and assume the first pt is in location)
       */
      if (uniquePts == null) {
        uniquePts = new HashSet<Coordinate>();
      }
      if (allPts == null) {
        allPts = new ArrayList<Coordinate>();
      }
      uniquePts.add(pt);
      allPts.add(pt);

      if (clusterType == ClusterType.GridWeight) {
        pickWeightedLocation(pt);
      } else if (clusterType == ClusterType.GridWeightHalf) {
        pickBoundedWeightedLocation(pt);
      } else if (clusterType == ClusterType.GridNearest) {
        pickNearestLocation(pt);
      } else if (clusterType == ClusterType.GridCenter) {
        pickCenterLocation(pt);
      } else if (clusterType == ClusterType.Natural) {
        pickBoundedWeightedLocation(pt);
      }

      if (boundingBox == null) {
        boundingBox = new Envelope();
      }
      boundingBox.expandToInclude(pt);
    }

    @SuppressWarnings("unchecked")
    public void addClusteredAttribute(Object id) {
      /** Add in cluster */
      if (idsClustered == null) {
        idsClustered = new JSONArray();
      }
      idsClustered.add(id.toString());
    }

    /**
     * The original location of the points, in case they are all superimposed (or there is a single
     * point), otherwise null
     *
     * @return
     */
    public Coordinate getOriginalLocation() {
      if (uniquePts != null && uniquePts.size() == 1) {
        return uniquePts.iterator().next();
      } else {
        return null;
      }
    }

    /**
     * Picks the location as the point which is nearest to the center of the cell. In addition, the
     * nearest location is averaged with the cell center. This gives the best chance of avoiding
     * conflicts.
     *
     * @param pt
     */
    private void pickNearestLocation(Coordinate pt) {
      // strategy - pick most central point
      if (location == null) {
        location = average(centerPt, pt);
        return;
      }
      if (pt.distance(centerPt) < location.distance(centerPt)) {
        location = average(centerPt, pt);
      }
    }

    /**
     * Calculate the weighted position of the cluster based on cluster points. On grid systems this
     * will not produce good results, best use GridNearest of the {@link ClusterType}
     *
     * @param pt
     */
    private void pickWeightedLocation(Coordinate pt) {
      if (location == null) {
        location = pt;
        this.dx = pt.x;
        this.dy = pt.y;
        return;
      }
      // add to weighting system
      this.dx += pt.x;
      this.dy += pt.y;
      location = new Coordinate(this.dx / this.count, this.dy / this.count);
      // location = average(location, pt);
    }

    /**
     * Computed the weight position by allowing a freedom of maximum r/2 from the center position.
     * This would avoid symbolization issues
     *
     * @param pt
     */
    private void pickBoundedWeightedLocation(Coordinate pt) {
      if (location == null) {
        location = pt;
        this.dx = pt.x;
        this.dy = pt.y;
        return;
      }

      double dx = this.dx + pt.x;
      double dy = this.dy + pt.y;
      if (new Coordinate(dx, dy).distance(first) > cellSize / 2) {
        this.dx += first.x;
        this.dy += first.y;
      } else {
        this.dx += pt.x;
        this.dy += pt.y;
      }
      location = new Coordinate(this.dx / this.count, this.dy / this.count);
      // location = average(location, pt);
    }

    /**
     * Picks the location as the centre point of the cell. This does not give a good visualization -
     * the gridding is very obvious
     *
     * @param pt
     */
    private void pickCenterLocation(Coordinate pt) {
      // strategy - pick first point
      if (location == null) {
        location = new Coordinate(pt);
        return;
      }
      location = centerPt;
    }

    /**
     * Picks the first location encountered as the cell location. This is sub-optimal, since if the
     * first point is near the cell boundary it is likely to collide with neighboring points.
     *
     * @param pt
     */
    private void pickFirstLocation(Coordinate pt) {
      // strategy - pick first point
      if (location == null) {
        location = new Coordinate(pt);
      }
    }

    private static Coordinate average(Coordinate p1, Coordinate p2) {
      double x = (p1.x + p2.x) / 2;
      double y = (p1.y + p2.y) / 2;
      return new Coordinate(x, y);
    }
  }
}
