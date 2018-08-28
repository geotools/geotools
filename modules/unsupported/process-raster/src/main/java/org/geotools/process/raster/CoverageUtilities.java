/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2011-2015, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.process.raster;

import it.geosolutions.jaiext.range.RangeFactory;
import it.geosolutions.jaiext.vectorbin.ROIGeometry;
import java.awt.geom.AffineTransform;
import java.awt.geom.NoninvertibleTransformException;
import java.awt.image.DataBuffer;
import java.util.HashMap;
import java.util.List;
import javax.media.jai.ROI;
import org.geotools.coverage.GridSampleDimension;
import org.geotools.coverage.TypeMap;
import org.geotools.coverage.grid.GridCoverage2D;
import org.geotools.coverage.grid.GridEnvelope2D;
import org.geotools.coverage.grid.GridGeometry2D;
import org.geotools.feature.simple.SimpleFeatureTypeBuilder;
import org.geotools.geometry.jts.JTS;
import org.geotools.process.ProcessException;
import org.geotools.referencing.operation.matrix.XAffineTransform;
import org.geotools.referencing.operation.transform.AffineTransform2D;
import org.geotools.resources.ClassChanger;
import org.geotools.util.Utilities;
import org.jaitools.media.jai.rangelookup.RangeLookupTable;
import org.jaitools.numeric.Range;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.Point;
import org.locationtech.jts.simplify.DouglasPeuckerSimplifier;
import org.opengis.coverage.SampleDimensionType;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.geometry.DirectPosition;
import org.opengis.geometry.Envelope;
import org.opengis.geometry.MismatchedDimensionException;
import org.opengis.referencing.operation.TransformException;

/**
 * A set of utilities methods for the Grid Coverage package. Those methods are not really rigorous;
 * must of them should be seen as temporary implementations.
 *
 * @author Simone Giannecchini, GeoSolutions
 * @source $URL$
 */
public class CoverageUtilities {

    public static final String NORTH = "NORTH";

    public static final String SOUTH = "SOUTH";

    public static final String WEST = "WEST";

    public static final String EAST = "EAST";

    public static final String XRES = "XRES";

    public static final String YRES = "YRES";

    public static final String ROWS = "ROWS";

    public static final String COLS = "COLS";

    public static final String MINY = "MINY";

    public static final String MINX = "MINX";

    /** Do not allows instantiation of this class. */
    private CoverageUtilities() {}

    /**
     * Utility method for transforming a geometry ROI into the raster space, using the provided
     * affine transformation.
     *
     * @param roi a {@link Geometry} in model space.
     * @param mt2d an {@link AffineTransform} that maps from raster to model space. This is already
     *     referred to the pixel corner.
     * @return a {@link ROI} suitable for using with JAI.
     * @throws ProcessException in case there are problems with ivnerting the provided {@link
     *     AffineTransform}. Very unlikely to happen.
     */
    public static ROI prepareROI(Geometry roi, AffineTransform mt2d) throws ProcessException {
        // transform the geometry to raster space so that we can use it as a ROI source
        Geometry rasterSpaceGeometry;
        try {
            rasterSpaceGeometry = JTS.transform(roi, new AffineTransform2D(mt2d.createInverse()));
        } catch (MismatchedDimensionException e) {
            throw new ProcessException(e);
        } catch (TransformException e) {
            throw new ProcessException(e);
        } catch (NoninvertibleTransformException e) {
            throw new ProcessException(e);
        }
        // System.out.println(rasterSpaceGeometry);
        // System.out.println(rasterSpaceGeometry.getEnvelopeInternal());

        // simplify the geometry so that it's as precise as the coverage, excess coordinates
        // just make it slower to determine the point in polygon relationship
        Geometry simplifiedGeometry = DouglasPeuckerSimplifier.simplify(rasterSpaceGeometry, 1);

        // build a shape using a fast point in polygon wrapper
        return new ROIGeometry(simplifiedGeometry);
    }

    /**
     * Creates a {@link SimpleFeatureType} that exposes a coverage as a collections of feature
     * points, mapping the centre of each pixel as a point plus all the bands as attributes.
     *
     * <p>The FID is the long that combines x+y*width.
     *
     * @param gc2d the {@link GridCoverage2D} to wrap.
     * @param geometryClass the class for the geometry.
     * @return a {@link SimpleFeatureType} or <code>null</code> in case we are unable to wrap the
     *     coverage
     */
    public static SimpleFeatureType createFeatureType(
            final GridCoverage2D gc2d, final Class<? extends Geometry> geometryClass) {

        // checks
        Utilities.ensureNonNull("gc2d", gc2d);

        // building a feature type for this coverage
        final SimpleFeatureTypeBuilder ftBuilder = new SimpleFeatureTypeBuilder();
        ftBuilder.setName(gc2d.getName().toString());
        ftBuilder.setNamespaceURI("http://www.geotools.org/");

        // CRS
        ftBuilder.setCRS(gc2d.getCoordinateReferenceSystem2D());
        //		ftBuilder.setCRS(DefaultEngineeringCRS.GENERIC_2D);

        // TYPE is as follows the_geom | band
        ftBuilder.setDefaultGeometry("the_geom");
        ftBuilder.add("the_geom", geometryClass);
        if (!geometryClass.equals(Point.class)) {
            ftBuilder.add("value", Double.class);
        } else {

            // get sample type on bands
            final GridSampleDimension[] sampleDimensions = gc2d.getSampleDimensions();
            for (GridSampleDimension sd : sampleDimensions) {
                final SampleDimensionType sdType = sd.getSampleDimensionType();
                final int dataBuffType = TypeMap.getDataBufferType(sdType);

                // TODO I think this should be a public utility inside the FeatureUtilities class
                @SuppressWarnings("rawtypes")
                final Class bandClass;
                switch (dataBuffType) {
                    case DataBuffer.TYPE_BYTE:
                        bandClass = Byte.class;
                        break;
                    case DataBuffer.TYPE_DOUBLE:
                        bandClass = Double.class;
                        break;
                    case DataBuffer.TYPE_FLOAT:
                        bandClass = Float.class;
                        break;
                    case DataBuffer.TYPE_INT:
                        bandClass = Integer.class;
                        break;
                    case DataBuffer.TYPE_SHORT:
                    case DataBuffer.TYPE_USHORT:
                        bandClass = Short.class;
                        break;
                    case DataBuffer.TYPE_UNDEFINED:
                    default:
                        return null;
                }
                ftBuilder.add(sd.getDescription().toString(), bandClass);
            }
        }
        return ftBuilder.buildFeatureType();
    }

    public static RangeLookupTable getRangeLookupTable(
            final List<Range> classificationRanges, final Number noDataValue) {

        return getRangeLookupTable(classificationRanges, noDataValue, noDataValue.getClass());
    }

    public static RangeLookupTable getRangeLookupTable(
            final List<Range> classificationRanges, final Number noDataValue, final Class clazz) {
        return getRangeLookupTable(classificationRanges, null, noDataValue, noDataValue.getClass());
    }

    public static RangeLookupTable getRangeLookupTable(
            final List<Range> classificationRanges,
            final int[] outputPixelValues,
            final Number noDataValue) {
        return getRangeLookupTable(
                classificationRanges, outputPixelValues, noDataValue, noDataValue.getClass());
    }

    public static RangeLookupTable getRangeLookupTable(
            List<Range> classificationRanges,
            final int[] outputPixelValues,
            final Number noDataValue,
            final Class<? extends Number> clazz) {
        final RangeLookupTable.Builder rltBuilder = new RangeLookupTable.Builder();
        final int size = classificationRanges.size();
        final boolean useCustomOutputPixelValues =
                outputPixelValues != null && outputPixelValues.length == size;

        Class<? extends Number> widestClass = noDataValue.getClass();

        for (int i = 0; i < size; i++) {
            final Range range = classificationRanges.get(i);
            final Class<? extends Number> rangeClass = range.getMin().getClass();

            if (widestClass != rangeClass) {
                widestClass = ClassChanger.getWidestClass(widestClass, rangeClass);
            }

            final int reference = useCustomOutputPixelValues ? outputPixelValues[i] : i + 1;

            rltBuilder.add(range, convert(reference, noDataValue.getClass()));
        }

        // Add the largest range that contains the no data value
        rltBuilder.add(
                new Range(getClassMinimum(widestClass), true, getClassMaximum(widestClass), true),
                noDataValue);

        return rltBuilder.build();
    }

    public static it.geosolutions.jaiext.rlookup.RangeLookupTable getRangeLookupTableJAIEXT(
            List<Range> classificationRanges,
            final int[] outputPixelValues,
            final Number noDataValue,
            final int transferType) {
        final it.geosolutions.jaiext.rlookup.RangeLookupTable.Builder rltBuilder =
                new it.geosolutions.jaiext.rlookup.RangeLookupTable.Builder();
        final int size = classificationRanges.size();
        final boolean useCustomOutputPixelValues =
                outputPixelValues != null && outputPixelValues.length == size;

        Class<? extends Number> noDataClass =
                it.geosolutions.jaiext.range.Range.DataType.classFromType(transferType);

        Class<? extends Number> widestClass = noDataClass;
        for (int i = 0; i < size; i++) {
            final Range range = classificationRanges.get(i);
            final Class<? extends Number> rangeClass = range.getMin().getClass();

            if (widestClass != rangeClass) {
                widestClass = ClassChanger.getWidestClass(widestClass, rangeClass);
            }
            int rangeType =
                    it.geosolutions.jaiext.range.Range.DataType.dataTypeFromClass(rangeClass);

            final int reference = useCustomOutputPixelValues ? outputPixelValues[i] : i + 1;
            it.geosolutions.jaiext.range.Range rangeJaiext =
                    RangeFactory.convert(
                            RangeFactory.create(
                                    range.getMin().doubleValue(),
                                    range.isMinIncluded(),
                                    range.getMax().doubleValue(),
                                    range.isMaxIncluded()),
                            rangeType);
            rltBuilder.add(rangeJaiext, convert(reference, noDataClass));
        }

        // Add the largest range that contains the no data value
        int rangeType = it.geosolutions.jaiext.range.Range.DataType.dataTypeFromClass(widestClass);
        it.geosolutions.jaiext.range.Range rangeJaiext =
                RangeFactory.convert(
                        RangeFactory.create(
                                getClassMinimum(widestClass).doubleValue(),
                                getClassMaximum(widestClass).doubleValue()),
                        rangeType);
        rltBuilder.add(rangeJaiext, noDataValue);

        return rltBuilder.build();
    }

    private static Number getClassMinimum(Class<? extends Number> numberClass) {
        if (numberClass == null) {
            return null;
        } else if (Double.class.equals(numberClass)) {
            return Double.MIN_VALUE;
        } else if (Float.class.equals(numberClass)) {
            return Float.MIN_VALUE;
        } else if (Long.class.equals(numberClass)) {
            return Long.MIN_VALUE;
        } else if (Integer.class.equals(numberClass)) {
            return Integer.MIN_VALUE;
        } else if (Short.class.equals(numberClass)) {
            return Short.MIN_VALUE;
        } else if (Byte.class.equals(numberClass)) {
            return Byte.MIN_VALUE;
        }

        throw new UnsupportedOperationException(
                "Class " + numberClass + " can't be used in a value Range");
    }

    private static Number getClassMaximum(Class<? extends Number> numberClass) {
        if (numberClass == null) {
            return null;
        } else if (Double.class.equals(numberClass)) {
            return Double.MAX_VALUE;
        } else if (Float.class.equals(numberClass)) {
            return Float.MAX_VALUE;
        } else if (Long.class.equals(numberClass)) {
            return Long.MAX_VALUE;
        } else if (Integer.class.equals(numberClass)) {
            return Integer.MAX_VALUE;
        } else if (Short.class.equals(numberClass)) {
            return Short.MAX_VALUE;
        } else if (Byte.class.equals(numberClass)) {
            return Byte.MAX_VALUE;
        }

        throw new UnsupportedOperationException(
                "Class " + numberClass + " can't be used in a value Range");
    }

    public static Number convert(Number val, Class<? extends Number> type) {
        if (val == null) {
            return null;
        } else if (Double.class.equals(type)) {
            if (val instanceof Double) {
                return val;
            }
            return Double.valueOf(val.doubleValue());
        } else if (Float.class.equals(type)) {
            if (val instanceof Float) {
                return val;
            }
            return Float.valueOf(val.floatValue());
        } else if (Integer.class.equals(type)) {
            if (val instanceof Integer) {
                return val;
            }
            return Integer.valueOf(val.intValue());
        } else if (Byte.class.equals(type)) {
            if (val instanceof Byte) {
                return val;
            }
            return Byte.valueOf(val.byteValue());
        } else if (Short.class.equals(type)) {
            if (val instanceof Short) {
                return val;
            }
            return Short.valueOf(val.shortValue());
        } else {
            throw new UnsupportedOperationException(
                    "Class " + type + " can't be used in a value Range");
        }
    }

    /**
     * Get the parameters of the region covered by the {@link GridCoverage2D coverage}.
     *
     * @param gridCoverage the coverage.
     * @return the {@link HashMap map} of parameters. ( {@link #NORTH} and the other static vars can
     *     be used to retrieve them.
     */
    public static HashMap<String, Double> getRegionParamsFromGridCoverage(
            GridCoverage2D gridCoverage) {
        HashMap<String, Double> envelopeParams = new HashMap<String, Double>();

        Envelope envelope = gridCoverage.getEnvelope();

        DirectPosition lowerCorner = envelope.getLowerCorner();
        double[] westSouth = lowerCorner.getCoordinate();
        DirectPosition upperCorner = envelope.getUpperCorner();
        double[] eastNorth = upperCorner.getCoordinate();

        GridGeometry2D gridGeometry = gridCoverage.getGridGeometry();
        GridEnvelope2D gridRange = gridGeometry.getGridRange2D();
        int height = gridRange.height;
        int width = gridRange.width;
        int minX = gridRange.x;
        int minY = gridRange.y;

        AffineTransform gridToCRS = (AffineTransform) gridGeometry.getGridToCRS();
        double xRes = XAffineTransform.getScaleX0(gridToCRS);
        double yRes = XAffineTransform.getScaleY0(gridToCRS);

        envelopeParams.put(NORTH, eastNorth[1]);
        envelopeParams.put(SOUTH, westSouth[1]);
        envelopeParams.put(WEST, westSouth[0]);
        envelopeParams.put(EAST, eastNorth[0]);
        envelopeParams.put(XRES, xRes);
        envelopeParams.put(YRES, yRes);
        envelopeParams.put(ROWS, (double) height);
        envelopeParams.put(COLS, (double) width);
        envelopeParams.put(MINY, (double) minY);
        envelopeParams.put(MINX, (double) minX);

        return envelopeParams;
    }
}
