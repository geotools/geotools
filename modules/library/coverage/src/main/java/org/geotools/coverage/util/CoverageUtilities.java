/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2001 - 2016, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.coverage.util;

import it.geosolutions.jaiext.range.NoDataContainer;
import it.geosolutions.jaiext.range.Range;
import java.awt.Rectangle;
import java.awt.geom.AffineTransform;
import java.awt.image.DataBuffer;
import java.awt.image.RenderedImage;
import java.io.BufferedInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.logging.Level;
import javax.imageio.ImageReadParam;
import javax.media.jai.PropertySource;
import javax.media.jai.ROI;
import org.geotools.coverage.Category;
import org.geotools.coverage.GridSampleDimension;
import org.geotools.coverage.grid.GridCoverage2D;
import org.geotools.coverage.grid.GridGeometry2D;
import org.geotools.geometry.Envelope2D;
import org.geotools.geometry.PixelTranslation;
import org.geotools.metadata.i18n.ErrorKeys;
import org.geotools.metadata.i18n.Errors;
import org.geotools.metadata.i18n.Vocabulary;
import org.geotools.metadata.i18n.VocabularyKeys;
import org.geotools.referencing.CRS;
import org.geotools.referencing.operation.matrix.XAffineTransform;
import org.geotools.referencing.operation.transform.AffineTransform2D;
import org.geotools.referencing.util.CRSUtilities;
import org.geotools.util.Utilities;
import org.opengis.coverage.Coverage;
import org.opengis.coverage.grid.GridCoverage;
import org.opengis.geometry.MismatchedDimensionException;
import org.opengis.referencing.FactoryException;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.opengis.referencing.datum.PixelInCell;
import org.opengis.referencing.operation.MathTransform;
import org.opengis.referencing.operation.TransformException;
import org.opengis.util.InternationalString;

/**
 * A set of utilities methods for the Grid Coverage package. Those methods are not really rigorous;
 * must of them should be seen as temporary implementations.
 *
 * @since 2.4
 * @version $Id$
 * @author Martin Desruisseaux (IRD)
 * @author Simone Giannecchini, GeoSolutions
 */
public final class CoverageUtilities {

    /** Public name for standard No Data category. */
    public static final InternationalString NODATA =
            Vocabulary.formatInternational(VocabularyKeys.NODATA);

    /** Axes transposition for swapping Lat and Lon axes. */
    public static final AffineTransform AXES_SWAP = new AffineTransform2D(0, 1, 1, 0, 0, 0);

    /** Identity affine transformation. */
    public static final AffineTransform IDENTITY_TRANSFORM =
            new AffineTransform2D(AffineTransform.getRotateInstance(0));

    /**
     * {@link AffineTransform} that can be used to go from an image datum placed at the center of
     * pixels to one that is placed at ULC.
     */
    public static final AffineTransform CENTER_TO_CORNER =
            AffineTransform.getTranslateInstance(
                    PixelTranslation.getPixelTranslation(PixelInCell.CELL_CORNER),
                    PixelTranslation.getPixelTranslation(PixelInCell.CELL_CORNER));
    /**
     * {@link AffineTransform} that can be used to go from an image datum placed at the ULC corner
     * of pixels to one that is placed at center.
     */
    public static final AffineTransform CORNER_TO_CENTER =
            AffineTransform.getTranslateInstance(
                    -PixelTranslation.getPixelTranslation(PixelInCell.CELL_CORNER),
                    -PixelTranslation.getPixelTranslation(PixelInCell.CELL_CORNER));

    public static final double AFFINE_IDENTITY_EPS = 1E-6;

    /** Do not allows instantiation of this class. */
    private CoverageUtilities() {}

    /**
     * Returns a two-dimensional CRS for the given coverage. This method performs a <cite>best
     * effort</cite>; the returned CRS is not garanteed to be the most appropriate one.
     *
     * @param coverage The coverage for which to obtains a two-dimensional CRS.
     * @return The two-dimensional CRS.
     * @throws TransformException if the CRS can't be reduced to two dimensions.
     */
    public static CoordinateReferenceSystem getCRS2D(final Coverage coverage)
            throws TransformException {
        if (coverage instanceof GridCoverage2D) {
            return ((GridCoverage2D) coverage).getCoordinateReferenceSystem2D();
        }
        if (coverage instanceof GridCoverage) {
            final GridGeometry2D geometry =
                    GridGeometry2D.wrap(((GridCoverage) coverage).getGridGeometry());
            if (geometry.isDefined(GridGeometry2D.CRS_BITMASK)) {
                return geometry.getCoordinateReferenceSystem2D();
            } else
                try {
                    return geometry.reduce(coverage.getCoordinateReferenceSystem());
                } catch (FactoryException exception) {
                    // Ignore; we will fallback on the code below.
                }
        }
        return CRSUtilities.getCRS2D(coverage.getCoordinateReferenceSystem());
    }

    /**
     * Returns a two-dimensional horizontal CRS for the given coverage. This method performs a
     * <cite>best effort</cite>; the returned CRS is not garanteed to succed.
     *
     * @param coverage The coverage for which to obtains a two-dimensional horizontal CRS.
     * @return The two-dimensional horizontal CRS.
     * @throws TransformException if the CRS can't be reduced to two dimensions.
     */
    public static CoordinateReferenceSystem getHorizontalCRS(final Coverage coverage)
            throws TransformException {
        CoordinateReferenceSystem returnedCRS = null;
        if (coverage instanceof GridCoverage2D) {
            returnedCRS = ((GridCoverage2D) coverage).getCoordinateReferenceSystem2D();
        }
        if (coverage instanceof GridCoverage) {
            final GridGeometry2D geometry =
                    GridGeometry2D.wrap(((GridCoverage) coverage).getGridGeometry());
            if (geometry.isDefined(GridGeometry2D.CRS_BITMASK)) {
                returnedCRS = geometry.getCoordinateReferenceSystem2D();
            } else
                try {
                    returnedCRS = geometry.reduce(coverage.getCoordinateReferenceSystem());
                } catch (FactoryException exception) {
                    // Ignore; we will fallback on the code below.
                }
        }
        if (returnedCRS == null)
            returnedCRS = CRS.getHorizontalCRS(coverage.getCoordinateReferenceSystem());
        if (returnedCRS == null)
            throw new TransformException(
                    Errors.format(ErrorKeys.CANT_REDUCE_TO_TWO_DIMENSIONS_$1, returnedCRS));
        return returnedCRS;
    }

    /**
     * Returns a two-dimensional envelope for the given coverage. This method performs a <cite>best
     * effort</cite>; the returned envelope is not garanteed to be the most appropriate one.
     *
     * @param coverage The coverage for which to obtains a two-dimensional envelope.
     * @return The two-dimensional envelope.
     * @throws MismatchedDimensionException if the envelope can't be reduced to two dimensions.
     */
    public static Envelope2D getEnvelope2D(final Coverage coverage)
            throws MismatchedDimensionException {
        if (coverage instanceof GridCoverage2D) {
            return ((GridCoverage2D) coverage).getEnvelope2D();
        }
        if (coverage instanceof GridCoverage) {
            final GridGeometry2D geometry =
                    GridGeometry2D.wrap(((GridCoverage) coverage).getGridGeometry());
            if (geometry.isDefined(GridGeometry2D.ENVELOPE_BITMASK)) {
                return geometry.getEnvelope2D();
            } else {
                return geometry.reduce(coverage.getEnvelope());
            }
        }
        // Following may thrown MismatchedDimensionException.
        return new Envelope2D(coverage.getEnvelope());
    }

    /**
     * Utility method for extracting NoData property from input {@link GridCoverage2D}.
     *
     * @return A {@link NoDataContainer} object containing input NoData definition
     */
    public static NoDataContainer getNoDataProperty(GridCoverage2D coverage) {
        // Searching for NoData property
        final Object noData = coverage.getProperty(NoDataContainer.GC_NODATA);
        if (noData != null) {
            // Returning a new instance of NoDataContainer
            if (noData instanceof NoDataContainer) {
                return (NoDataContainer) noData;
            } else if (noData instanceof Double) {
                return new NoDataContainer((Double) noData);
            }
        }
        return null;
    }

    /**
     * Utility method for extracting ROI property from input {@link GridCoverage2D}.
     *
     * @return A {@link ROI} object
     */
    public static ROI getROIProperty(GridCoverage2D coverage) {
        // Searching for the ROI
        final Object roi = coverage.getProperty("GC_ROI");
        // Returning it if present
        if (roi != null && roi instanceof ROI) {
            return (ROI) roi;
        }
        return null;
    }

    /**
     * Utility method for setting NoData to the input {@link Map}
     *
     * @param properties {@link Map} where the nodata will be set
     * @param noData May be a {@link Range}, {@code double[]}, {@code double} or {@link
     *     NoDataContainer}
     */
    public static void setNoDataProperty(Map<String, Object> properties, Object noData) {
        // If no nodata or no properties are defined, nothing is done
        if (noData == null || properties == null) {
            return;
        }
        // Creation of a new NoDataContainer instance and setting it inside the properties
        if (noData instanceof Range) {
            properties.put(NoDataContainer.GC_NODATA, new NoDataContainer((Range) noData));
        } else if (noData instanceof Double) {
            properties.put(NoDataContainer.GC_NODATA, new NoDataContainer((Double) noData));
        } else if (noData instanceof double[]) {
            properties.put(NoDataContainer.GC_NODATA, new NoDataContainer((double[]) noData));
        } else if (noData instanceof NoDataContainer) {
            properties.put(
                    NoDataContainer.GC_NODATA, new NoDataContainer((NoDataContainer) noData));
        }
    }

    /**
     * Utility method for setting ROI to the input {@link Map}
     *
     * @param properties {@link Map} where the ROI will be set
     * @param roi {@link ROI} instance to set
     */
    public static void setROIProperty(Map<String, Object> properties, ROI roi) {
        // If no properties is defined, nothing is done
        if (properties == null) {
            return;
        }
        // If No ROI is defined we remove ROI property from the property map
        if (roi == null) {
            properties.remove("GC_ROI");
            return;
        }
        // Otherwise ROI is set
        properties.put("GC_ROI", roi);
    }

    /**
     * Retrieves a best guess for the sample value to use for background, inspecting the categories
     * of the provided {@link GridCoverage2D}.
     *
     * @param coverage to use for guessing background values.
     * @return an array of double values to use as a background.
     */
    public static double[] getBackgroundValues(GridCoverage2D coverage) {

        // minimal checks
        if (coverage == null) {
            throw new NullPointerException(
                    Errors.format(ErrorKeys.NULL_PARAMETER_$2, "coverage", "GridCoverage2D"));
        }

        // try to get the GC_NODATA double value from the coverage property
        final Object noData = coverage.getProperty(NoDataContainer.GC_NODATA);
        if (noData != null && noData instanceof NoDataContainer) {
            return ((NoDataContainer) noData).getAsArray();
            // new double[]{((Double)noData).doubleValue()};
        }

        ////
        //
        // Try to gather no data values from the sample dimensions
        // and, if not available, we try to suggest them from the sample
        // dimension type
        //
        ////
        final GridSampleDimension[] sampleDimensions = coverage.getSampleDimensions();
        final double[] background = new double[sampleDimensions.length];

        boolean found = false;
        final int dataType = coverage.getRenderedImage().getSampleModel().getDataType();
        for (int i = 0; i < background.length; i++) {
            // try to use the no data category if preset
            final List<Category> categories = sampleDimensions[i].getCategories();
            if (categories != null && categories.size() > 0) {
                for (Category category : categories) {
                    if (category.getName().equals(NODATA)) {
                        background[i] = category.getRange().getMinimum();
                        found = true;
                        break;
                    }
                }
            }

            if (!found) {
                // we don't have a proper no data value, let's try to suggest something
                // meaningful fro mthe data type for this coverage
                background[i] = suggestNoDataValue(dataType).doubleValue();
            }

            //          SG 25112012, removed this automagic behavior
            //            final NumberRange<?> range =
            // sampleDimensions[i].getBackground().getRange();
            //            final double min = range.getMinimum();
            //            final double max = range.getMaximum();
            //            if (range.isMinIncluded()) {
            //                background[i] = min;
            //            } else if (range.isMaxIncluded()) {
            //                background[i] = max;
            //            } else {
            //                background[i] = 0.5 * (min + max);
            //            }
        }
        return background;
    }

    /**
     * Returns {@code true} if the specified grid coverage or any of its source uses the following
     * image.
     */
    public static boolean uses(final GridCoverage coverage, final RenderedImage image) {
        if (coverage != null) {
            if (coverage.getRenderedImage() == image) {
                return true;
            }
            final Collection<GridCoverage> sources = coverage.getSources();
            if (sources != null) {
                for (final GridCoverage source : sources) {
                    if (uses(source, image)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    /**
     * Returns the visible band in the specified {@link RenderedImage} or {@link PropertySource}.
     * This method fetch the {@code "GC_VisibleBand"} property. If this property is undefined, then
     * the visible band default to the first one.
     *
     * @param image The image for which to fetch the visible band, or {@code null}.
     * @return The visible band.
     */
    public static int getVisibleBand(final Object image) {
        Object candidate = null;
        if (image instanceof RenderedImage) {
            candidate = ((RenderedImage) image).getProperty("GC_VisibleBand");
        } else if (image instanceof PropertySource) {
            candidate = ((PropertySource) image).getProperty("GC_VisibleBand");
        }
        if (candidate instanceof Integer) {
            return ((Integer) candidate).intValue();
        }
        return 0;
    }

    /**
     * Checks if the transformation is a pure scale/translate instance (using the provided tolerance
     * factor)
     *
     * @param transform The {@link MathTransform} to check.
     * @param EPS The tolerance factor.
     * @return {@code true} if the provided transformation is a simple scale and translate, {@code
     *     false} otherwise.
     */
    public static boolean isScaleTranslate(final MathTransform transform, final double EPS) {
        if (!(transform instanceof AffineTransform)) {
            return false;
        }
        final AffineTransform at = (AffineTransform) transform;
        final double rotation = Math.abs(XAffineTransform.getRotation(at));
        return rotation < EPS; // This is enough for returning 'false' if 'scale' is NaN.
    }

    /**
     * Computes the resolutions for the provided "grid to world" transformation The returned
     * resolution array is of length of 2.
     *
     * @param gridToCRS The grid to world transformation.
     */
    public static double[] getResolution(final AffineTransform gridToCRS) {
        double[] requestedRes = null;
        if (gridToCRS != null) {
            requestedRes = new double[2];
            requestedRes[0] = XAffineTransform.getScaleX0(gridToCRS);
            requestedRes[1] = XAffineTransform.getScaleY0(gridToCRS);
        }
        return requestedRes;
    }

    /**
     * Tries to estimate if the supplied affine transform is either a scale and translate transform
     * or if it contains a rotations which is an integer multiple of PI/2.
     *
     * @param gridToCRS an instance of {@link AffineTransform} to check against.
     * @param EPS tolerance value for comparisons.
     * @return {@code true} if this transform is "simple", {@code false} otherwise.
     */
    public static boolean isSimpleGridToWorldTransform(
            final AffineTransform gridToCRS, double EPS) {
        final double rotation = XAffineTransform.getRotation(gridToCRS);
        // Checks if there is a valid rotation value (it could be 0). If the result is an integer,
        // then there is no rotation and skew or there is a rotation multiple of PI/2. Note that
        // there is no need to check explicitly for NaN rotation angle since such value will be
        // propagated as NaN by every math functions used here, and (NaN < EPS) returns false.
        final double quadrantRotation = Math.abs(rotation / (Math.PI / 2));
        return Math.abs(quadrantRotation - Math.floor(quadrantRotation)) < EPS;
    }

    /**
     * Checks that the provided {@code dimensions} when intersected with the source region used by
     * the provided {@link ImageReadParam} instance does not result in an empty {@link Rectangle}.
     * Finally, in case the region intersection is not empty, set it as new source region for the
     * provided {@link ImageReadParam}.
     *
     * <p>Input parameters cannot be null.
     *
     * @param readParameters an instance of {@link ImageReadParam} for which we want to check the
     *     source region element.
     * @param dimensions an instance of {@link Rectangle} to use for the check.
     * @return {@code true} if the intersection is not empty, {@code false} otherwise.
     */
    public static boolean checkEmptySourceRegion(
            final ImageReadParam readParameters, final Rectangle dimensions) {
        Utilities.ensureNonNull("readDimension", dimensions);
        Utilities.ensureNonNull("readP", readParameters);
        final Rectangle sourceRegion = readParameters.getSourceRegion();
        Rectangle.intersect(sourceRegion, dimensions, sourceRegion);
        if (sourceRegion.isEmpty()) {
            return true;
        }
        readParameters.setSourceRegion(sourceRegion);
        return false;
    }

    /**
     * Returns a suitable threshold depending on the {@link DataBuffer} type.
     *
     * <p>Remember that the threshold works with >=.
     *
     * @param dataType to create a low threshold for.
     * @return a minimum threshold value suitable for this data type.
     */
    public static double getMosaicThreshold(int dataType) {
        switch (dataType) {
            case DataBuffer.TYPE_BYTE:
            case DataBuffer.TYPE_USHORT:
                // this may cause problems and truncations when the native mosaic
                // operations is enabled
                return 0.0;
            case DataBuffer.TYPE_INT:
                return Integer.MIN_VALUE;
            case DataBuffer.TYPE_SHORT:
                return Short.MIN_VALUE;
            case DataBuffer.TYPE_DOUBLE:
                return -Double.MAX_VALUE;
            case DataBuffer.TYPE_FLOAT:
                return -Float.MAX_VALUE;
        }
        return 0;
    }

    /**
     * Returns a suitable no data value depending on the {@link DataBuffer} type.
     *
     * @param dataType to create a low threshold for.
     * @return a no data value suitable for this data type.
     */
    public static Number suggestNoDataValue(int dataType) {
        switch (dataType) {
            case DataBuffer.TYPE_BYTE:
                return Byte.valueOf((byte) 0);
            case DataBuffer.TYPE_USHORT:
                return Short.valueOf((short) 0);
            case DataBuffer.TYPE_INT:
                return Integer.valueOf(Integer.MIN_VALUE);
            case DataBuffer.TYPE_SHORT:
                return Short.valueOf(Short.MIN_VALUE);
            case DataBuffer.TYPE_DOUBLE:
                return Double.valueOf(Double.NaN);
            case DataBuffer.TYPE_FLOAT:
                return Float.valueOf(Float.NaN);
            default:
                throw new IllegalAccessError(
                        Errors.format(ErrorKeys.ILLEGAL_ARGUMENT_$2, "dataType", dataType));
        }
    }

    /** Unified Code for Units of Measure (UCUM) */
    public static class UCUM {

        /** An UCUM Unit instance simply made of name and symbol. */
        public static class UCUMUnit {

            private String name;

            private String symbol;

            public UCUMUnit(String name, String symbol) {
                this.name = name;
                this.symbol = symbol;
            }

            public String getName() {
                return name;
            }

            public String getSymbol() {
                return symbol;
            }
        }

        /**
         * Commonly used UCUM units. In case this set will grow too much, we may consider importing
         * some UCUM specialized library.
         */
        public static final UCUMUnit TIME_UNITS = new UCUMUnit("second", "s");

        public static final UCUMUnit ELEVATION_UNITS = new UCUMUnit("meter", "m");
    }

    /** Extract Properties from a specified URL */
    public static Properties loadPropertiesFromURL(URL propsURL) {
        Utilities.ensureNonNull("propsURL", propsURL);
        final Properties properties = new Properties();
        try (InputStream stream = new BufferedInputStream(propsURL.openStream())) {
            properties.load(stream);
        } catch (FileNotFoundException e) {
            if (FeatureUtilities.LOGGER.isLoggable(Level.SEVERE))
                FeatureUtilities.LOGGER.log(Level.SEVERE, e.getLocalizedMessage(), e);
            return null;
        } catch (IOException e) {
            if (FeatureUtilities.LOGGER.isLoggable(Level.SEVERE))
                FeatureUtilities.LOGGER.log(Level.SEVERE, e.getLocalizedMessage(), e);
            return null;
        }
        return properties;
    }
}
