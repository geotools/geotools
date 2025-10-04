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

import java.awt.geom.AffineTransform;
import java.awt.geom.NoninvertibleTransformException;
import java.awt.image.DataBuffer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import org.eclipse.imagen.ROI;
import org.eclipse.imagen.media.range.Range;
import org.eclipse.imagen.media.range.RangeDouble;
import org.eclipse.imagen.media.range.RangeFactory;
import org.eclipse.imagen.media.rlookup.RangeLookupTable;
import org.eclipse.imagen.media.vectorbin.ROIGeometry;
import org.geotools.api.coverage.SampleDimensionType;
import org.geotools.api.feature.simple.SimpleFeatureType;
import org.geotools.api.geometry.Bounds;
import org.geotools.api.geometry.MismatchedDimensionException;
import org.geotools.api.geometry.Position;
import org.geotools.api.metadata.spatial.PixelOrientation;
import org.geotools.api.parameter.ParameterValueGroup;
import org.geotools.api.referencing.operation.MathTransform;
import org.geotools.api.referencing.operation.TransformException;
import org.geotools.coverage.Category;
import org.geotools.coverage.GridSampleDimension;
import org.geotools.coverage.TypeMap;
import org.geotools.coverage.grid.GridCoverage2D;
import org.geotools.coverage.grid.GridEnvelope2D;
import org.geotools.coverage.grid.GridGeometry2D;
import org.geotools.coverage.processing.CoverageProcessor;
import org.geotools.feature.simple.SimpleFeatureTypeBuilder;
import org.geotools.geometry.GeneralBounds;
import org.geotools.geometry.jts.JTS;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.process.ProcessException;
import org.geotools.referencing.operation.matrix.XAffineTransform;
import org.geotools.referencing.operation.transform.AffineTransform2D;
import org.geotools.referencing.operation.transform.ProjectiveTransform;
import org.geotools.util.ClassChanger;
import org.geotools.util.NumberRange;
import org.geotools.util.Utilities;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.Point;
import org.locationtech.jts.geom.util.AffineTransformation;
import org.locationtech.jts.simplify.DouglasPeuckerSimplifier;

/**
 * A set of utilities methods for the Grid Coverage package. Those methods are not really rigorous; many of them should
 * be seen as temporary implementations.
 *
 * @author Simone Giannecchini, GeoSolutions
 */
public class CoverageUtilities {

    private static final CoverageProcessor PROCESSOR = CoverageProcessor.getInstance();

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
     * Utility method for transforming a geometry ROI into the raster space, using the provided affine transformation.
     *
     * @param roi a {@link Geometry} in model space.
     * @param mt2d an {@link AffineTransform} that maps from raster to model space. This is already referred to the
     *     pixel corner.
     * @return a {@link ROI} suitable for using with ImageN.
     * @throws ProcessException in case there are problems with ivnerting the provided {@link AffineTransform}. Very
     *     unlikely to happen.
     */
    public static ROI prepareROI(Geometry roi, AffineTransform mt2d) throws ProcessException {
        // transform the geometry to raster space so that we can use it as a ROI source
        Geometry rasterSpaceGeometry;
        try {
            rasterSpaceGeometry = JTS.transform(roi, new AffineTransform2D(mt2d.createInverse()));
        } catch (MismatchedDimensionException | NoninvertibleTransformException | TransformException e) {
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
     * Creates a {@link SimpleFeatureType} that exposes a coverage as a collections of feature points, mapping the
     * centre of each pixel as a point plus all the bands as attributes.
     *
     * <p>The FID is the long that combines x+y*width.
     *
     * @param gc2d the {@link GridCoverage2D} to wrap.
     * @param geometryClass the class for the geometry.
     * @return a {@link SimpleFeatureType} or <code>null</code> in case we are unable to wrap the coverage
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

    /**
     * Static create method. This just relieves the tedium of having to call create RangeFactory create method.
     *
     * @param <T> value type
     * @param minValue the lower bound; passing null for this parameter means an open lower bound; for Float or Double
     *     types, the relevant NEGATIVE_INFINITY value can also be used.
     * @param minIncluded true if the lower bound is to be included in the range; false to exclude the lower bound;
     *     overridden to be false if the lower bound is open
     * @param maxValue the upper bound; passing null for this parameter means an open upper bound; for Float or Double
     *     types, the relevant NEGATIVE_INFINITY value can also be used.
     * @param maxIncluded true if the upper bound is to be included in the range; false to exclude the upper bound;
     *     overridden to be false if the upper bound is open
     * @return the new instance
     */
    public static <T extends Number> Range createRange(
            T minValue, boolean minIncluded, T maxValue, boolean maxIncluded) {
        Class<?> minType = minValue != null ? minValue.getClass() : null;
        Class<?> maxType = minValue != null ? maxValue.getClass() : null;

        // step 1: determine type
        if (minType != null && maxType != null && !minType.equals(maxType)) {
            throw new IllegalArgumentException("minValue and maxValue must be of the same type");
        }
        if (minType == null && maxType != null) {
            minType = maxType;
        } else if (maxType == null && minType != null) {
            maxType = minType;
        }
        if (minValue == null && maxValue == null) {
            throw new IllegalArgumentException(
                    "RangeType could not be determined as both minValue and maxValue were null");
        }

        // step 2: create range using factory
        if (minType.isAssignableFrom(Double.class)) {
            return RangeFactory.create(
                    minValue != null ? minValue.doubleValue() : Double.NEGATIVE_INFINITY,
                    minIncluded,
                    maxValue != null ? maxValue.doubleValue() : Double.POSITIVE_INFINITY,
                    maxIncluded);
        } else if (minType.isAssignableFrom(Float.class)) {
            return RangeFactory.create(
                    minValue != null ? minValue.floatValue() : Float.NEGATIVE_INFINITY,
                    minIncluded,
                    maxValue != null ? maxValue.floatValue() : Float.POSITIVE_INFINITY,
                    maxIncluded);
        } else if (minType.isAssignableFrom(Long.class)) {
            return RangeFactory.create(
                    minValue != null ? minValue.longValue() : Long.MIN_VALUE,
                    minIncluded,
                    maxValue != null ? maxValue.longValue() : Long.MAX_VALUE,
                    maxIncluded);
        } else if (minType.isAssignableFrom(Integer.class)) {
            return RangeFactory.create(
                    minValue != null ? minValue.intValue() : Integer.MIN_VALUE,
                    minIncluded,
                    maxValue != null ? maxValue.intValue() : Integer.MAX_VALUE,
                    maxIncluded);
        } else if (minType.isAssignableFrom(Short.class)) {
            return RangeFactory.create(
                    minValue != null ? minValue.shortValue() : Short.MIN_VALUE,
                    minIncluded,
                    maxValue != null ? maxValue.shortValue() : Short.MAX_VALUE,
                    maxIncluded);
        } else if (minType.isAssignableFrom(Byte.class)) {
            return RangeFactory.create(
                    minValue != null ? minValue.byteValue() : Byte.MIN_VALUE,
                    minIncluded,
                    maxValue != null ? maxValue.byteValue() : Byte.MAX_VALUE,
                    maxIncluded);
        } else {
            throw new UnsupportedOperationException("Class " + minType + " not supported by RangeFactory");
        }
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
            final List<Range> classificationRanges, final int[] outputPixelValues, final Number noDataValue) {
        return getRangeLookupTable(classificationRanges, outputPixelValues, noDataValue, noDataValue.getClass());
    }

    @SuppressWarnings("unchecked")
    public static RangeLookupTable getRangeLookupTable(
            List<Range> classificationRanges,
            final int[] outputPixelValues,
            final Number noDataValue,
            final Class<? extends Number> clazz) {
        final RangeLookupTable.Builder rltBuilder = new RangeLookupTable.Builder();
        final int size = classificationRanges.size();
        final boolean useCustomOutputPixelValues = outputPixelValues != null && outputPixelValues.length == size;

        Class<? extends Number> widestClass = noDataValue.getClass();

        for (int i = 0; i < size; i++) {
            final Range range = classificationRanges.get(i);
            final Class<? extends Number> rangeClass = range.getDataType().getClassValue();

            if (widestClass != rangeClass) {
                widestClass = ClassChanger.getWidestClass(widestClass, rangeClass);
            }

            final int reference = useCustomOutputPixelValues ? outputPixelValues[i] : i + 1;

            rltBuilder.add(range, convert(reference, noDataValue.getClass()));
        }

        // Add the largest range that contains the no data value
        rltBuilder.add(
                createRange(getClassMinimum(widestClass), true, getClassMaximum(widestClass), true), noDataValue);

        return rltBuilder.build();
    }

    /**
     * Setup lookup table from classification ranges.
     *
     * @param classificationRanges
     * @param outputPixelValues
     * @param noDataValue
     * @param transferType
     * @return lookup table
     */
    @SuppressWarnings("unchecked")
    public static RangeLookupTable getRangeLookupTable(
            List<Range> classificationRanges,
            final int[] outputPixelValues,
            final Number noDataValue,
            final int transferType) {
        final RangeLookupTable.Builder rltBuilder = new RangeLookupTable.Builder();
        final int size = classificationRanges.size();
        final boolean useCustomOutputPixelValues = outputPixelValues != null && outputPixelValues.length == size;

        Class<? extends Number> noDataClass = Range.DataType.classFromType(transferType);

        Class<? extends Number> widestClass = noDataClass;
        for (int i = 0; i < size; i++) {
            final Range range = classificationRanges.get(i);
            final Class<? extends Number> rangeClass = range.getDataType().getClassValue();

            if (widestClass != rangeClass) {
                widestClass = ClassChanger.getWidestClass(widestClass, rangeClass);
            }
            int rangeType = Range.DataType.dataTypeFromClass(rangeClass);

            final int reference = useCustomOutputPixelValues ? outputPixelValues[i] : i + 1;
            Range rangeJaiext = RangeFactory.convert(
                    RangeFactory.create(
                            range.getMin().doubleValue(),
                            range.isMinIncluded(),
                            range.getMax().doubleValue(),
                            range.isMaxIncluded()),
                    rangeType);
            rltBuilder.add(rangeJaiext, convert(reference, noDataClass));
        }

        // Add the largest range that contains the no data value
        int rangeType = Range.DataType.dataTypeFromClass(widestClass);
        Range rangeJaiext = RangeFactory.convert(
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

        throw new UnsupportedOperationException("Class " + numberClass + " can't be used in a value Range");
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

        throw new UnsupportedOperationException("Class " + numberClass + " can't be used in a value Range");
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
            throw new UnsupportedOperationException("Class " + type + " can't be used in a value Range");
        }
    }

    /**
     * Get the parameters of the region covered by the {@link GridCoverage2D coverage}.
     *
     * @param gridCoverage the coverage.
     * @return the {@link HashMap map} of parameters. ( {@link #NORTH} and the other static vars can be used to retrieve
     *     them.
     */
    public static HashMap<String, Double> getRegionParamsFromGridCoverage(GridCoverage2D gridCoverage) {
        HashMap<String, Double> envelopeParams = new HashMap<>();

        Bounds envelope = gridCoverage.getEnvelope();

        Position lowerCorner = envelope.getLowerCorner();
        double[] westSouth = lowerCorner.getCoordinate();
        Position upperCorner = envelope.getUpperCorner();
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

    /**
     * Retrieves the no data ranges for a given grid coverage.
     *
     * @param coverage the grid coverage to extract nodata ranges from
     * @return a list of no data ranges, or null if no nodata ranges are found
     */
    public static List<RangeDouble> getNoDataAsList(GridCoverage2D coverage) {
        List<RangeDouble> noDataValueRangeList = null;
        GridSampleDimension sampleDimension = coverage.getSampleDimension(0);
        List<Category> categories = sampleDimension.getCategories();

        if (categories != null) {
            for (Category category : categories) {
                String catName = category.getName().toString();
                if (catName.equalsIgnoreCase("no data")) {
                    NumberRange range = category.getRange();
                    double min = range.getMinimum();
                    double max = category.getRange().getMaximum();
                    if (!Double.isNaN(min) && !Double.isNaN(max)) {
                        // we have to filter those out
                        RangeDouble novalueRange = RangeFactory.create(min, true, max, true);
                        noDataValueRangeList = new ArrayList<>();
                        noDataValueRangeList.add(novalueRange);
                    }
                    break;
                }
            }
        }
        return noDataValueRangeList;
    }

    /**
     * Crops a given grid coverage to the specified geometry envelope using a coverage crop operation.
     *
     * @param coverage The source grid coverage to be cropped
     * @param geometryEnvelope The envelope defining the crop region
     * @return A new GridCoverage2D cropped to the specified envelope
     */
    public static GridCoverage2D crop(GridCoverage2D coverage, ReferencedEnvelope geometryEnvelope) {
        ParameterValueGroup param = PROCESSOR.getOperation("CoverageCrop").getParameters();
        param.parameter("Source").setValue(coverage);
        param.parameter("Envelope").setValue(new GeneralBounds(geometryEnvelope));
        return (GridCoverage2D) PROCESSOR.doOperation(param);
    }

    /**
     * Converts a given geometry to a simplified Region of Interest (ROI) in raster space for a given grid coverage.
     *
     * @param coverage The grid coverage defining the raster transformation context
     * @param geometry The original geometry to be transformed and simplified
     * @return A simplified ROI geometry suitable for raster processing
     * @throws TransformException If the geometry transformation fails
     */
    public static ROI getSimplifiedRoiGeometry(GridCoverage2D coverage, Geometry geometry) throws TransformException {
        // double checked with the tasmania simple test data, this transformation
        // actually lines up the polygons where they are supposed to be in raster space
        final AffineTransform dataG2WCorrected = new AffineTransform(
                (AffineTransform) coverage.getGridGeometry().getGridToCRS2D(PixelOrientation.UPPER_LEFT));
        final MathTransform w2gTransform;
        try {
            w2gTransform = ProjectiveTransform.create(dataG2WCorrected.createInverse());
        } catch (NoninvertibleTransformException e) {
            throw new IllegalArgumentException(e.getLocalizedMessage());
        }
        // transform the geometry to raster space so that we can use it as a ROI source
        Geometry rasterSpaceGeometry = JTS.transform(geometry, w2gTransform);
        // System.out.println(rasterSpaceGeometry);
        // System.out.println(rasterSpaceGeometry.getEnvelopeInternal());

        // simplify the geometry so that it's as precise as the coverage, excess coordinates
        // just make it slower to determine the point in polygon relationship
        Geometry simplifiedGeometry = DouglasPeuckerSimplifier.simplify(rasterSpaceGeometry, 1);
        // System.out.println(simplifiedGeometry.getEnvelopeInternal());

        // compensate for the range lookup poking the corner of the cells instead
        // of their center, this makes for odd results if the polygon is just slightly
        // misaligned with the coverage
        AffineTransformation at = new AffineTransformation();

        at.setToTranslation(-0.5, -0.5);
        simplifiedGeometry.apply(at);

        // build a shape using a fast point in polygon wrapper
        return new ROIGeometry(simplifiedGeometry, false);
    }
}
