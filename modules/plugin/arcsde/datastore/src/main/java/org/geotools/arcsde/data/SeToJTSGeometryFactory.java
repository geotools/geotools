/**
 * 
 */
package org.geotools.arcsde.data;

import org.geotools.geometry.jts.LiteCoordinateSequenceFactory;

import com.esri.sde.sdk.client.SeRow;
import com.esri.sde.sdk.client.SeShape;
import com.esri.sde.sdk.geom.GeometryFactory;
import com.vividsolutions.jts.geom.CoordinateSequence;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.LinearRing;
import com.vividsolutions.jts.geom.MultiPolygon;
import com.vividsolutions.jts.geom.Polygon;

/**
 * This is an experimental implementation of {@link com.esri.sde.sdk.geom.GeometryFactory} that
 * creates JTS geometries directly by calling {@link SeRow#getGeometry(GeometryFactory, int)},
 * instead of fetching an {@link SeShape} through {@link SeRow#getShape(int)} and then converting it
 * to a JTS geometry. This is work in progress and _experimental_, though.
 * 
 * @source $URL:
 *         http://svn.osgeo.org/geotools/trunk/modules/plugin/arcsde/datastore/src/main/java/org
 *         /geotools/arcsde/data/SeToJTSGeometryFactory.java $
 */
public class SeToJTSGeometryFactory implements GeometryFactory {

    protected static com.vividsolutions.jts.geom.GeometryFactory gf = new com.vividsolutions.jts.geom.GeometryFactory(
            new LiteCoordinateSequenceFactory());

    private SeToJTSGeometryFactory delegate;

    public void init(final int type, final int numParts, final int numPoints) {
        if (type == SeShape.TYPE_POLYGON) {
            delegate = new PolygonFactory();
        } else if (type == SeShape.TYPE_MULTI_POLYGON) {
            delegate = new MultiPolygonFactory();
        } else {
            throw new IllegalArgumentException("Unhandled geometry type: " + type);
        }
        delegate.init(numParts, numPoints);
    }

    protected void init(int numParts, int numPoints) {
        // do-nothing, override as needed
    }

    public void envelope(double minx, double miny, double maxx, double maxy) {
        // System.out.println("envelope: " + minx + "," + miny + "," + maxx + "," + maxy);
    }

    public Geometry getGeometry() {
        return delegate.getGeometry();
    }

    public void newPart(final int numSubParts) {
        delegate.newPart(numSubParts);
    }

    public void newSubPart(final int numPoints) {
        delegate.newSubPart(numPoints);
    }

    public void newPoint(final double x, final double y) {
        delegate.newPoint(x, y);
    }

    public void newPoint(double x, double y, double m) {
        newPoint(x, y);
    }

    public void newPoint(double x, double y, double m, double z) {
        newPoint(x, y);
    }

    public void partOffsets(int[] partOffsets) {
        // System.out.println(Arrays.toString(partOffsets));
    }

    /**
     * 
     * 
     */
    private static final class PolygonFactory extends SeToJTSGeometryFactory {

        private LinearRing[] subparts;

        private CoordinateSequence currCoordSeq;

        private int subPartNo;

        private int currPartNumPoints;

        private int currPointNo;

        @Override
        protected void init(int numParts, int numPoints) {
            subPartNo = -1;
            currCoordSeq = null;
        }

        @Override
        public Polygon getGeometry() {
            LinearRing shell = subparts[0];
            LinearRing[] holes = null;
            if (subparts.length > 1) {
                holes = new LinearRing[subparts.length - 1];
                System.arraycopy(subparts, 0, holes, 0, holes.length);
            }
            Polygon poly = gf.createPolygon(shell, holes);
            return poly;
        }

        @Override
        public void newPart(final int numSubParts) {
            subparts = new LinearRing[numSubParts];
            subPartNo = -1;
        }

        @Override
        public void newSubPart(final int numPoints) {
            this.subPartNo++;
            this.currPartNumPoints = numPoints;
            this.currPointNo = 0;
            final int dimension = 2;
            this.currCoordSeq = gf.getCoordinateSequenceFactory().create(numPoints, dimension);
        }

        @Override
        public void newPoint(final double x, final double y) {
            this.currCoordSeq.setOrdinate(this.currPointNo, 0, x);
            this.currCoordSeq.setOrdinate(this.currPointNo, 1, y);
            currPointNo++;
            if (currPointNo == this.currPartNumPoints) {
                this.subparts[this.subPartNo] = gf.createLinearRing(this.currCoordSeq);
            }
        }
    }

    /**
     * 
     * 
     */
    private static final class MultiPolygonFactory extends SeToJTSGeometryFactory {

        private SeToJTSGeometryFactory.PolygonFactory polygonFactory;

        private Polygon[] parts;

        private int partNo;

        @Override
        protected void init(int numParts, int numPoints) {
            this.parts = new Polygon[numParts];
            polygonFactory = new PolygonFactory();
            this.partNo = -1;
        }

        @Override
        public Geometry getGeometry() {
            if (parts.length > 0) {
                parts[parts.length - 1] = polygonFactory.getGeometry();
            }
            MultiPolygon mp = gf.createMultiPolygon(parts);
            return mp;
        }

        @Override
        public void newPart(final int numSubParts) {
            if (this.partNo > -1) {
                Polygon poly = polygonFactory.getGeometry();
                this.parts[partNo] = poly;
            }
            this.partNo++;
            polygonFactory.init(1, -1);
            polygonFactory.newPart(numSubParts);
        }

        @Override
        public void newSubPart(final int numPoints) {
            polygonFactory.newSubPart(numPoints);
        }

        @Override
        public void newPoint(final double x, final double y) {
            polygonFactory.newPoint(x, y);
        }
    }
}
