/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2014-2015, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.coverage.processing.operation;

import it.geosolutions.jaiext.JAIExt;
import it.geosolutions.jaiext.range.NoDataContainer;
import it.geosolutions.jaiext.range.Range;
import it.geosolutions.jaiext.range.RangeFactory;
import it.geosolutions.jaiext.utilities.ImageLayout2;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.geom.Rectangle2D;
import java.awt.image.IndexColorModel;
import java.awt.image.RenderedImage;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import javax.media.jai.ImageLayout;
import javax.media.jai.JAI;
import javax.media.jai.ParameterBlockJAI;
import javax.media.jai.PlanarImage;
import javax.media.jai.ROI;
import javax.media.jai.ROIShape;
import javax.media.jai.operator.MosaicDescriptor;
import javax.media.jai.operator.MosaicType;
import org.geotools.coverage.GridSampleDimension;
import org.geotools.coverage.grid.GridCoverage2D;
import org.geotools.coverage.grid.GridEnvelope2D;
import org.geotools.coverage.grid.GridGeometry2D;
import org.geotools.coverage.grid.InvalidGridGeometryException;
import org.geotools.coverage.processing.CoverageProcessingException;
import org.geotools.coverage.processing.OperationJAI;
import org.geotools.coverage.util.CoverageUtilities;
import org.geotools.geometry.Envelope2D;
import org.geotools.geometry.GeneralEnvelope;
import org.geotools.image.ImageWorker;
import org.geotools.image.util.ImageUtilities;
import org.geotools.metadata.i18n.ErrorKeys;
import org.geotools.metadata.i18n.Errors;
import org.geotools.metadata.iso.citation.Citations;
import org.geotools.parameter.DefaultParameterDescriptor;
import org.geotools.parameter.ImagingParameterDescriptors;
import org.geotools.parameter.ImagingParameters;
import org.geotools.referencing.CRS;
import org.geotools.referencing.operation.transform.ConcatenatedTransform;
import org.geotools.util.Utilities;
import org.geotools.util.factory.GeoTools;
import org.geotools.util.factory.Hints;
import org.opengis.coverage.Coverage;
import org.opengis.coverage.grid.GridGeometry;
import org.opengis.metadata.spatial.PixelOrientation;
import org.opengis.parameter.InvalidParameterValueException;
import org.opengis.parameter.ParameterDescriptor;
import org.opengis.parameter.ParameterNotFoundException;
import org.opengis.parameter.ParameterValueGroup;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.opengis.referencing.datum.PixelInCell;
import org.opengis.referencing.operation.MathTransform;
import org.opengis.referencing.operation.NoninvertibleTransformException;
import org.opengis.referencing.operation.TransformException;
import org.opengis.util.InternationalString;

/**
 * This operation does a mosaic of multiple {@link GridCoverage2D}s. The {@link GridCoverage2D}s can
 * have different resolutions; the operation will resample them to the same one. The policies for
 * choosing the output resolution are:
 *
 * <ul>
 *   <li>resolution of the FIRST coverage (Default)
 *   <li>FINE resolution
 *   <li>COARSE resolution
 *   <li>resolution of an EXTERNAL GridGeometry
 * </ul>
 *
 * Note that the operation requires that all the {@link GridCoverage2D}s are in the same CRS, else
 * an exception is thrown.
 *
 * <p>The input parameters of the operation are:
 *
 * <ul>
 *   <li>a Collection of the {@link GridCoverage2D} to mosaic
 *   <li>an optional {@link GridGeometry} object for setting the final resolution and BoundingBox
 *   <li>an optional {@link String} indicating the policy to use for choosing the resolution
 *   <li>an optional {@code double[]} indicating the nodata values to set for the background. Note
 *       that the only the first value will be used
 * </ul>
 *
 * @author Nicola Lagomarsini GesoSolutions S.A.S.
 */
public class Mosaic extends OperationJAI {

    private static final int THRESHOLD_PARAM = 3;

    private static final int BACKGROUND_PARAM = 4;

    private static final int NODATA_RANGE_PARAM = 5;

    private static final int MOSAIC_TYPE_PARAM = 0;

    private static final int ALPHA_PARAM = 1;

    private static final int ROI_PARAM = 2;

    /** Name for the COVERAGE_INDEX parameter */
    public static final String POLICY = "policy";

    /** Name for the GG2D parameter */
    public static final String GEOMETRY = "geometry";

    /** Name for the Sources parameter */
    public static final String SOURCES_NAME = "Sources";

    /** Name for the Output No Data parameter */
    public static final String OUTNODATA_NAME = "outputNoData";

    /** Name for the input Alpha bands */
    public static final String ALPHA_NAME = "alphas";

    /** The parameter descriptor for the Sources. */
    public static final ParameterDescriptor SOURCES =
            new DefaultParameterDescriptor(
                    Citations.JAI,
                    SOURCES_NAME,
                    Collection.class, // Value class (mandatory)
                    null, // Array of valid values
                    null, // Default value
                    null, // Minimal value
                    null, // Maximal value
                    null, // Unit of measure
                    true);

    /** The parameter descriptor for the GridGeometry to use. */
    public static final ParameterDescriptor<GridGeometry> GG =
            new DefaultParameterDescriptor<GridGeometry>(
                    Citations.JAI,
                    GEOMETRY,
                    GridGeometry.class, // Value class (mandatory)
                    null, // Array of valid values
                    null, // Default value
                    null, // Minimal value
                    null, // Maximal value
                    null, // Unit of measure
                    false);

    /** The parameter descriptor for the GridGeometry choosing policy. */
    public static final ParameterDescriptor<String> GEOMETRY_POLICY =
            new DefaultParameterDescriptor<String>(
                    Citations.JAI,
                    POLICY,
                    String.class, // Value class (mandatory)
                    null, // Array of valid values
                    null, // Default value
                    null, // Minimal value
                    null, // Maximal value
                    null, // Unit of measure
                    false);

    /** The parameter descriptor for the Transformation Choice. */
    public static final ParameterDescriptor<double[]> OUTPUT_NODATA =
            new DefaultParameterDescriptor<double[]>(
                    Citations.JAI,
                    OUTNODATA_NAME,
                    double[].class, // Value class (mandatory)
                    null, // Array of valid values
                    null, // Default value
                    null, // Minimal value
                    null, // Maximal value
                    null, // Unit of measure
                    false);

    /** The parameter descriptor for the Alpha band. */
    public static final ParameterDescriptor<Collection> ALPHA =
            new DefaultParameterDescriptor<Collection>(
                    Citations.JAI,
                    ALPHA_NAME,
                    Collection.class, // Value class (mandatory)
                    null, // Array of valid values
                    null, // Default value
                    null, // Minimal value
                    null, // Maximal value
                    null, // Unit of measure
                    false);

    private static Set<ParameterDescriptor> REPLACED_DESCRIPTORS;

    // Replace the old parameter descriptor group with a new one with the old parameters and the new
    // ones defined above.
    static {
        final Set<ParameterDescriptor> replacedDescriptors = new HashSet<ParameterDescriptor>();
        replacedDescriptors.add(SOURCES);
        replacedDescriptors.add(GG);
        replacedDescriptors.add(GEOMETRY_POLICY);
        replacedDescriptors.add(OUTPUT_NODATA);
        replacedDescriptors.add(ALPHA);
        REPLACED_DESCRIPTORS = Collections.unmodifiableSet(replacedDescriptors);
    }

    /**
     * Enum used for choosing the output {@link GridGeometry2D} to use and then resampling all the
     * {@link GridCoverage2D} to its resolution.
     *
     * @author Nicola Lagomarsini GesoSolutions S.A.S.
     */
    public enum GridGeometryPolicy {
        FIRST("first") {
            @Override
            public ResampledRasters resampleGridGeometry(
                    GridCoverage2D[] sources,
                    GridCoverage2D[] alphas,
                    GridGeometry2D external,
                    ParameterValueGroup parameters,
                    Hints hints) {
                // Index associated to the first coverage
                int index = PRIMARY_SOURCE_INDEX;
                // Selection of the first GridGeometry2D object to use
                GridGeometry2D finalGG = extractFinalGridGeometry(sources, index);
                // GridCoverage resampling
                return resampleCoverages(sources, alphas, finalGG, parameters, hints);
            }
        },
        FINE("fine") {
            @Override
            public ResampledRasters resampleGridGeometry(
                    GridCoverage2D[] sources,
                    GridCoverage2D[] alphas,
                    GridGeometry2D external,
                    ParameterValueGroup parameters,
                    Hints hints) {

                // Number of the sources to use
                int numSources = sources.length;
                // Selection of the first GridGeometry
                GridGeometry2D grid = sources[0].getGridGeometry();
                Envelope2D env = grid.getEnvelope2D();
                GridEnvelope2D gridEnv = grid.getGridRange2D();

                // Method for searching the index at the highest resolution. Suppose that the
                // coverages contains the same
                // resolution on both axis
                double res = env.width / gridEnv.width;

                // Coverage index
                int index = PRIMARY_SOURCE_INDEX;
                // Search for the minimum value of the resolution
                for (int i = 1; i < numSources; i++) {
                    GridGeometry2D gridI = sources[i].getGridGeometry();
                    Envelope2D envI = gridI.getEnvelope2D();
                    GridEnvelope2D gridEnvI = gridI.getGridRange2D();
                    double resValue = envI.width / gridEnvI.width;
                    // Search the index associated to the better resolution
                    if (resValue < res) {
                        res = resValue;
                        index = i;
                    }
                }
                // Calculation of the final GridGeometry to use
                GridGeometry2D finalGG = extractFinalGridGeometry(sources, index);
                // Coverage resampling
                return resampleCoverages(sources, alphas, finalGG, parameters, hints);
            }
        },
        COARSE("coarse") {
            @Override
            public ResampledRasters resampleGridGeometry(
                    GridCoverage2D[] sources,
                    GridCoverage2D[] alphas,
                    GridGeometry2D external,
                    ParameterValueGroup parameters,
                    Hints hints) {
                // Number of the sources to use
                int numSources = sources.length;
                // Selection of the first GridGeometry
                GridGeometry2D grid = sources[0].getGridGeometry();
                Envelope2D env = grid.getEnvelope2D();
                GridEnvelope2D gridEnv = grid.getGridRange2D();

                // Method for searching the index at the lowest resolution. Suppose that the
                // coverages contains the same
                // resolution on both axis
                double res = env.width / gridEnv.width;

                // Coverage index
                int index = PRIMARY_SOURCE_INDEX;
                // Search for the minimum value of the resolution
                for (int i = 1; i < numSources; i++) {
                    GridGeometry2D gridI = sources[i].getGridGeometry();
                    Envelope2D envI = gridI.getEnvelope2D();
                    GridEnvelope2D gridEnvI = gridI.getGridRange2D();
                    double resValue = envI.width / gridEnvI.width;
                    // Search the index associated to the worst resolution
                    if (resValue > res) {
                        res = resValue;
                        index = i;
                    }
                }
                // Calculation of the final GridGeometry to use
                GridGeometry2D finalGG = extractFinalGridGeometry(sources, index);
                // Coverage resampling
                return resampleCoverages(sources, alphas, finalGG, parameters, hints);
            }
        },
        EXTERNAL("external") {
            @Override
            public ResampledRasters resampleGridGeometry(
                    GridCoverage2D[] sources,
                    GridCoverage2D[] alphas,
                    GridGeometry2D external,
                    ParameterValueGroup parameters,
                    Hints hints) {
                // Check if the external GridGeometry is present
                if (external == null) {
                    throw new CoverageProcessingException("No input GridGeometry found");
                }
                // Coverage resampling
                return resampleCoverages(sources, alphas, external, parameters, hints);
            }
        };

        /** Name associated to the {@link GridGeometryPolicy} object */
        private String name;

        private GridGeometryPolicy(String name) {
            this.name = name;
        }

        /**
         * Method for resampling the input {@link GridCoverage2D} objects. The output of the method
         * is an object containing the resampled {@link RenderedImage}s and the final {@link
         * GridGeometry2D} object to use.
         */
        public abstract ResampledRasters resampleGridGeometry(
                GridCoverage2D[] sources,
                GridCoverage2D[] alphas,
                GridGeometry2D external,
                ParameterValueGroup parameters,
                Hints hints);

        /**
         * Static method to use for choosing the {@link GridGeometryPolicy} object associated to the
         * input string.
         */
        public static GridGeometryPolicy getPolicyFromString(String policyString) {
            if (policyString.equalsIgnoreCase(FIRST.name)) {
                return FIRST;
            } else if (policyString.equalsIgnoreCase(FINE.name)) {
                return FINE;
            } else if (policyString.equalsIgnoreCase(COARSE.name)) {
                return COARSE;
            } else if (policyString.equalsIgnoreCase(EXTERNAL.name)) {
                return EXTERNAL;
            }
            return null;
        }

        /**
         * Private method for resampling the {@link GridCoverage2D}s to the same resolution imposed
         * by the {@link GridGeometry2D} object.
         */
        private static ResampledRasters resampleCoverages(
                GridCoverage2D[] sources,
                GridCoverage2D[] alphas,
                GridGeometry2D external,
                ParameterValueGroup parameters,
                Hints hints) {
            // Number of the sources to use
            int numSources = sources.length;

            // Creation of an array of the RenderedImages to use
            RenderedImage[] rasters = new RenderedImage[numSources];
            // Creation of an array of background values
            double[] backgrounds = new double[numSources];
            // Creation of an array of rois
            ROI[] rois = new ROI[numSources];
            // Creation of an array of NoData
            boolean hasNoDataProp = false;

            // Selection of the GridToWorld transformation associated to the External GG2D
            MathTransform g2w = external.getGridToCRS2D(PixelOrientation.UPPER_LEFT);
            // Initial null value for NoData
            double[] nodata = null;

            // Check if the output nodata value is set as parameter
            Object outputNodata = parameters.parameter(OUTNODATA_NAME).getValue();
            if (outputNodata != null && outputNodata instanceof double[]) {
                nodata = ((double[]) outputNodata);
            }
            // Checking if the external alpha bands are defined
            boolean hasAlpha = alphas != null && alphas.length > 0;
            PlanarImage[] alphaArray = new PlanarImage[numSources];

            // Cycle around the various sources
            for (int i = 0; i < numSources; i++) {
                // For each source, create a new GridGeometry which at the same resolution of the
                // imposed one
                GridCoverage2D coverage = sources[i];
                GridGeometry2D inputGG = coverage.getGridGeometry();

                // Check if the transform from one gridGeometry to the other is an Identity
                // transformation
                MathTransform g2wS = inputGG.getGridToCRS2D(PixelOrientation.UPPER_LEFT);
                MathTransform w2gD = external.getCRSToGrid2D(PixelOrientation.UPPER_LEFT);
                // Creation of a Concatenated transformation in order to check if the final
                // transformation from
                // source space to the final space is an identity.
                MathTransform concatenated = ConcatenatedTransform.create(g2wS, w2gD);

                // No operation must be done if the transformation is an Identity
                if (concatenated != null && concatenated.isIdentity()) {
                    RenderedImage renderedImage = coverage.getRenderedImage();
                    rasters[i] = renderedImage;
                    // Get ROI from the coverage
                    rois[i] = CoverageUtilities.getROIProperty(coverage);
                    // Add the alpha band
                    if (hasAlpha && alphas[i] != null) {
                        checkAlpha(coverage, alphas[i]);
                        alphaArray[i] = PlanarImage.wrapRenderedImage(alphas[i].getRenderedImage());
                        // Mask alpha band with ROI in order to see ROI during Mosaic operation
                        if (rois[i] != null) {
                            ImageWorker w = new ImageWorker(alphaArray[i]);
                            // Expand ROI Image to alpha size
                            ImageWorker w1 = new ImageWorker(rois[i].getAsImage());
                            ImageLayout layout = new ImageLayout();
                            layout.setMinX(alphaArray[i].getMinX());
                            layout.setMinY(alphaArray[i].getMinY());
                            layout.setWidth(alphaArray[i].getWidth());
                            layout.setHeight(alphaArray[i].getHeight());
                            w1.setRenderingHint(JAI.KEY_IMAGE_LAYOUT, layout);
                            w1.translate(0f, 0f, null);
                            // Mask Alpha
                            w.mask(w1.getRenderedImage(), false, 0);
                        }
                    }
                    double fillValue = CoverageUtilities.getBackgroundValues(coverage)[0];
                    backgrounds[i] = fillValue;
                    // Get NoData as property if present
                    NoDataContainer noDataProperty = CoverageUtilities.getNoDataProperty(coverage);
                    hasNoDataProp |= noDataProperty != null;
                } else {
                    // New GridGeometry
                    GridGeometry2D newGG =
                            new GridGeometry2D(
                                    PixelInCell.CELL_CORNER,
                                    g2w,
                                    inputGG.getEnvelope(),
                                    GeoTools.getDefaultHints());
                    try {
                        // Transformation of the input envelope in the Raster Space
                        GeneralEnvelope transformed =
                                CRS.transform(g2w.inverse(), inputGG.getEnvelope());
                        // Rounding of the bounds
                        Rectangle rect = transformed.toRectangle2D().getBounds();
                        // Creation of a new GridEnvelope to set for the new GridGeometry
                        GridEnvelope2D gEnv2 = new GridEnvelope2D(rect);
                        // Creation of the new GridGeometry
                        newGG = new GridGeometry2D(gEnv2, inputGG.getEnvelope());
                    } catch (InvalidGridGeometryException e) {
                        throw new CoverageProcessingException(e);
                    } catch (NoninvertibleTransformException e) {
                        throw new CoverageProcessingException(e);
                    } catch (TransformException e) {
                        throw new CoverageProcessingException(e);
                    }
                    // Initialization of the nodata value
                    double[] fillValue = null;
                    // Selection of the nodata value
                    if (nodata == null) {
                        fillValue = CoverageUtilities.getBackgroundValues(coverage);
                    } else {
                        fillValue = nodata;
                    }

                    // Resample to the new resolution
                    rasters[i] =
                            GridCoverage2DRIA.create(
                                    coverage,
                                    newGG,
                                    fillValue,
                                    hints,
                                    CoverageUtilities.getROIProperty(coverage));
                    // Resample also the alpha band
                    if (hasAlpha && alphas[i] != null) {
                        checkAlpha(coverage, alphas[i]);
                        RenderedImage al =
                                GridCoverage2DRIA.create(
                                        alphas[i],
                                        newGG,
                                        new double[1],
                                        hints,
                                        CoverageUtilities.getROIProperty(coverage));
                        alphaArray[i] = PlanarImage.wrapRenderedImage(al);
                    }
                    backgrounds[i] = fillValue[0];
                    // Resample to the new resolution
                    GridCoverage2DRIA.GridCoverage2DRIAPropertyGenerator propertyGenerator =
                            new GridCoverage2DRIA.GridCoverage2DRIAPropertyGenerator();
                    Object property = propertyGenerator.getProperty("roi", rasters[i]);
                    ROI roi = (property != null && property instanceof ROI) ? (ROI) property : null;
                    rois[i] = roi;
                    // Get NoData as property if present
                    NoDataContainer noDataProperty = CoverageUtilities.getNoDataProperty(coverage);
                    hasNoDataProp |= noDataProperty != null;
                }
            }

            // Create the final object containing the final GridGeometry and the resampled
            // RenderedImages
            ResampledRasters rr = new ResampledRasters();
            rr.setFinalGeometry(external);
            rr.setRasters(rasters);
            rr.setAlphas(alphaArray);
            rr.setBackgrounds(backgrounds);
            rr.setRois(rois);
            rr.setHasNoData(hasNoDataProp);
            return rr;
        }

        /** Method for checking if Alpha Coverage and Image Coverage have the same dimensions */
        private static void checkAlpha(GridCoverage2D coverage, GridCoverage2D alpha) {
            // Check GridGeometries
            if (!coverage.getGridGeometry().equals(alpha.getGridGeometry())) {
                throw new CoverageProcessingException(
                        "Alpha Coverage and Source Coverage does not have the same dimensions");
            }
        }

        /**
         * This method creates a new {@link GridGeometry2D} object based on that of the {@link
         * GridCoverage2D} defined by the index.
         */
        private static GridGeometry2D extractFinalGridGeometry(
                GridCoverage2D[] sources, int index) {
            // Select the GridGeometry of the first coverage
            GridGeometry2D gg = sources[index].getGridGeometry();
            MathTransform g2w = gg.getGridToCRS2D(PixelOrientation.UPPER_LEFT);
            // Initial Bounding box
            Envelope2D bbox = gg.getEnvelope2D();
            // Number of the sources to use
            int numSources = sources.length;
            // Cycle on all the GridCoverages in order to create the final Bounding box
            for (int i = 0; i < numSources; i++) {
                bbox.include(sources[i].getEnvelope2D());
            }

            // Creation of a final GridGeometry containing the final Bounding Box
            GridGeometry2D finalGG =
                    new GridGeometry2D(
                            PixelInCell.CELL_CORNER, g2w, bbox, GeoTools.getDefaultHints());
            return finalGG;
        }
    }

    public Mosaic() {
        super(
                getOperationDescriptor("Mosaic"),
                new ImagingParameterDescriptors(
                        getOperationDescriptor("Mosaic"), REPLACED_DESCRIPTORS));
    }

    public Coverage doOperation(final ParameterValueGroup parameters, final Hints hints)
            throws CoverageProcessingException {
        /*
         * Extracts the source grid coverages now as a List. The sources will be set in the ParameterBlockJAI (as RenderedImages) later.
         */
        final Collection<GridCoverage2D> sourceCollection = new ArrayList<GridCoverage2D>();
        extractSources(parameters, sourceCollection, null);

        // Selection of the source number
        int numSources = sourceCollection.size();
        GridCoverage2D[] sources = new GridCoverage2D[numSources];
        // Creation of an array of GridCoverage2D from the input collection
        sourceCollection.toArray(sources);
        // Selection of the CRS of the first coverage in order to check that the CRS is the same for
        // all the GridCoverages
        GridCoverage2D firstCoverage = sources[PRIMARY_SOURCE_INDEX];
        CoordinateReferenceSystem crs = firstCoverage.getCoordinateReferenceSystem();

        for (int i = 0; i < sources.length; i++) {
            final GridCoverage2D source = sources[i];
            CoordinateReferenceSystem crsSource = source.getCoordinateReferenceSystem();
            if (!CRS.equalsIgnoreMetadata(crs, crsSource)) {
                throw new CoverageProcessingException("Input Coverages have different CRS");
            }
        }
        // Preparation of the input parameters and resampling of the source images
        final Params params = prepareParameters(parameters, sources, hints);

        /*
         * Applies the operation. This delegates the work to the chain of 'deriveXXX' methods.
         */
        return deriveGridCoverage(sources, params);
    }

    /**
     * Prepares the parameters to store in the {@link ParameterBlockJAI} object and resample the
     * input {@link GridCoverage2D}.
     */
    private Params prepareParameters(
            final ParameterValueGroup parameters, GridCoverage2D[] sources, Hints hints) {
        final ImagingParameters copy = (ImagingParameters) descriptor.createValue();
        final ParameterBlockJAI block = (ParameterBlockJAI) copy.parameters;

        // Object indicating the policy to use for resampling all the GridCoverages to the same
        // GridGeometry
        GridGeometryPolicy policy = null;

        // Check if the External GridGeometry is present
        Object externalGG = parameters.parameter(GEOMETRY).getValue();
        GridGeometry2D gg = null;
        if (externalGG != null && externalGG instanceof GridGeometry2D) {
            gg = (GridGeometry2D) externalGG;
            policy = GridGeometryPolicy.EXTERNAL;
        } else {
            // Check if the GridGeometry selection policy is present
            Object ggPolicy = parameters.parameter(POLICY).getValue();
            if (ggPolicy != null && ggPolicy instanceof String) {
                policy = GridGeometryPolicy.getPolicyFromString((String) ggPolicy);
            }
        }
        // No policy defined, the first GridCoverage is used.
        if (policy == null) {
            policy = GridGeometryPolicy.FIRST;
        }

        // Getting Alpha bands if present
        Object alphaBandList = parameters.parameter(ALPHA_NAME).getValue();
        GridCoverage2D[] alphaCovs = null;
        if (alphaBandList != null && alphaBandList instanceof Collection) {
            Collection<GridCoverage2D> alphas = (Collection<GridCoverage2D>) alphaBandList;
            alphaCovs = new GridCoverage2D[alphas.size()];
            alphas.toArray(alphaCovs);
        }

        // Resample to the defined GridGeometry
        ResampledRasters rr =
                policy.resampleGridGeometry(sources, alphaCovs, gg, parameters, hints);
        // Get the resampled RenderedImages
        RenderedImage[] rasters = rr.getRasters();
        // Get returnedROIs
        ROI[] newRois = rr.getRois();

        // Setting of the final GridGeometry
        GridGeometry2D finalGeometry = rr.getFinalGeometry();
        if (finalGeometry == null) {
            throw new CoverageProcessingException("No final GridGeometry found");
        }

        int numSources = rasters.length;
        // Setting the source rasters for the mosaic
        if (Boolean.TRUE.equals(hints.get(JAI.KEY_REPLACE_INDEX_COLOR_MODEL))) {
            // the mosaic operation will blow up in this case, internally the Raster accessors
            // are not getting configured to do expansion as needed. Work around it.
            for (int i = 0; i < numSources; i++) {
                RenderedImage source = rasters[i];
                if (source.getColorModel() instanceof IndexColorModel) {
                    source = new ImageWorker(source).forceComponentColorModel().getRenderedImage();
                }
                block.setSource(source, i);
            }

            hints = new Hints(hints);
            hints.add(new RenderingHints(JAI.KEY_REPLACE_INDEX_COLOR_MODEL, Boolean.FALSE));
            hints.add(new RenderingHints(JAI.KEY_TRANSFORM_ON_COLORMAP, Boolean.TRUE));
        } else {
            for (int i = 0; i < numSources; i++) {
                block.setSource(rasters[i], i);
            }
        }
        // Setting the nodata values for the areas not covered by any GridCoverage.
        double[] nodata = null;
        // Check if the output nodata value is present
        Object outputNodata = parameters.parameter(OUTNODATA_NAME).getValue();
        if (outputNodata != null && outputNodata instanceof double[]) {
            nodata = ((double[]) outputNodata);
        } else {
            nodata = CoverageUtilities.getBackgroundValues(sources[PRIMARY_SOURCE_INDEX]);
        }
        // Setting of the output nodata
        block.set(nodata, BACKGROUND_PARAM);
        // Setting of the ROI associated to each GridCoverage
        // We need to add its roi in order to avoid problems with the mosaics sources overlapping
        ROI[] rois = new ROI[numSources];
        // Cycle on each coverage in order to add the associated ROI
        for (int i = 0; i < numSources; i++) {
            if (newRois != null && newRois[i] != null) {
                rois[i] = newRois[i];
            } else {
                rois[i] = new ROIShape(PlanarImage.wrapRenderedImage(rasters[i]).getBounds());
            }
        }
        block.set(rois, ROI_PARAM);
        // If at least one image contains Alpha channel, it is used for the mosaic
        if (rr.getAlphas() != null) {
            block.set(rr.getAlphas(), ALPHA_PARAM);
        }

        // Setting of the Threshold to use
        double threshold =
                CoverageUtilities.getMosaicThreshold(
                        rasters[PRIMARY_SOURCE_INDEX].getSampleModel().getDataType());
        // Setting of the Threshold object to use for the mosaic
        block.set(new double[][] {{threshold}}, THRESHOLD_PARAM);

        // Setting of the Mosaic type as Overlay
        block.set(MosaicDescriptor.MOSAIC_TYPE_OVERLAY, MOSAIC_TYPE_PARAM);

        // Check if it is a JAI-Ext operation
        if (JAIExt.isJAIExtOperation("Mosaic")) {
            // Get the nodata values
            double[] nodatas = rr.getBackgrounds();
            if (nodatas != null && rr.hasNoData()) {
                Range[] ranges = new Range[numSources];
                for (int i = 0; i < numSources; i++) {
                    double value = nodatas[i];
                    ranges[i] = RangeFactory.create(value, value);
                }
                block.set(ranges, NODATA_RANGE_PARAM);
            }
        }

        // Creation of the finel Parameters
        Params params = new Params(block, hints, finalGeometry);
        params.rr = rr;
        return params;
    }

    /**
     * Applies a JAI operation to the coverages. This method is invoked by {@link #doOperation}.
     * This implementation performs the following steps:
     *
     * <ul>
     *   <li>Applied the JAI operation using {@link #createRenderedImage}.
     *   <li>Wraps the result in a {@link GridCoverage2D} object.
     * </ul>
     *
     * @param sources The source coverages.
     * @param parameters Parameters, rendering hints and coordinate reference system to use.
     * @return The result as a grid coverage.
     * @see #doOperation
     * @see JAI#createNS
     */
    private GridCoverage2D deriveGridCoverage(
            final GridCoverage2D[] sources, final Params parameters) {
        GridCoverage2D primarySource = sources[PRIMARY_SOURCE_INDEX];

        /*
         * Set the rendering hints image layout. Only the following properties will be set:
         *
         * - Width - Height
         */
        RenderingHints hintsStart = ImageUtilities.getRenderingHints(parameters.getSource());
        RenderingHints hints = null;
        // Addition of the Hints
        if (parameters.hints != null) {
            if (hintsStart != null) {
                hints = new Hints(hintsStart);
                hints.add(parameters.hints); // May overwrite the image layout we have just set.
            } else {
                hints = new Hints(parameters.hints);
            }
        }

        // Layout associated to the input RenderingHints
        ImageLayout layoutOld =
                (hints != null) ? (ImageLayout) hints.get(JAI.KEY_IMAGE_LAYOUT) : null;
        ImageLayout layout = null;
        // Check on the ImageLayout
        if (layoutOld != null) {
            layout = (ImageLayout) layoutOld.clone();
            // Unset the previous dimension parameters
            layout.unsetValid(ImageLayout.MIN_X_MASK);
            layout.unsetValid(ImageLayout.MIN_Y_MASK);
            layout.unsetValid(ImageLayout.WIDTH_MASK);
            layout.unsetValid(ImageLayout.HEIGHT_MASK);
            // there might be color expansion
            layout.unsetValid(ImageLayout.COLOR_MODEL_MASK);
            layout.unsetValid(ImageLayout.SAMPLE_MODEL_MASK);
        } else {
            // Create a new one
            layout = new ImageLayout2();
        }

        // Get the GridRange associated to the final GridGeometry to use
        GridEnvelope2D gridRange = parameters.finalGeometry.getGridRange2D();

        // Then set the parameters associated to the final GridGeometry used
        layout.setMinX(gridRange.x);
        layout.setMinY(gridRange.y);
        layout.setWidth(gridRange.width);
        layout.setHeight(gridRange.height);

        // Set the new layout for the rendering hints
        if (hints == null) {
            hints = new RenderingHints(JAI.KEY_IMAGE_LAYOUT, layout);
        } else {
            hints.put(JAI.KEY_IMAGE_LAYOUT, layout);
        }

        /*
         * Performs the operation using JAI and construct the new grid coverage. Uses the coordinate system from the main source coverage in order to
         * preserve the extra dimensions (if any). The first two dimensions should be equal to the coordinate system set in the 'parameters' block.
         */
        final InternationalString name = deriveName(sources, -1, null);
        final CoordinateReferenceSystem crs = primarySource.getCoordinateReferenceSystem();
        final MathTransform toCRS = parameters.finalGeometry.getGridToCRS();
        final RenderedImage data = createRenderedImage(parameters.parameters, hints);
        final Map<String, ?> properties =
                getProperties(data, crs, name, toCRS, sources, parameters);
        return getFactory(parameters.hints)
                .create(
                        name, // The grid coverage name
                        data, // The underlying data
                        crs, // The coordinate system (may not be 2D).
                        toCRS, // The grid transform (may not be 2D).
                        getOutputSampleDimensions(
                                primarySource.getSampleDimensions(), data), // The sample dimensions
                        sources, // The source grid coverages.
                        properties); // Properties
    }

    /**
     * We override this one to get some extra behavior that ImageWorker has (ROI, paletted images
     * management)
     */
    protected RenderedImage createRenderedImage(
            final ParameterBlockJAI parameters, final RenderingHints hints) {
        parameters.getSources();
        RenderedImage[] images =
                (RenderedImage[])
                        parameters
                                .getSources()
                                .toArray(new RenderedImage[parameters.getSources().size()]);
        MosaicType type = getParameter(parameters, 0);
        PlanarImage[] alphas = getParameter(parameters, ALPHA_PARAM);
        ROI[] rois = getParameter(parameters, ROI_PARAM);
        double[][] thresholds = getParameter(parameters, THRESHOLD_PARAM);
        Range[] noData = getParameter(parameters, NODATA_RANGE_PARAM);
        double[] backgrounds = getParameter(parameters, BACKGROUND_PARAM);
        ImageWorker iw = new ImageWorker();
        iw.setRenderingHints(hints);
        iw.setBackground(backgrounds);
        iw.mosaic(images, type, alphas, rois, thresholds, noData);
        return iw.getRenderedImage();
    }

    private <T> T getParameter(ParameterBlockJAI pb, int index) {
        if (pb.getNumParameters() > index) {
            return (T) pb.getObjectParameter(index);
        } else {
            return null;
        }
    }

    private GridSampleDimension[] getOutputSampleDimensions(
            GridSampleDimension[] sampleDimensions, RenderedImage data) {
        // if there was no color model expansion, we are fine
        int outputNumBands = data.getSampleModel().getNumBands();
        if (outputNumBands == sampleDimensions.length) {
            return sampleDimensions;
        }
        GridSampleDimension[] newSampleDimensions = new GridSampleDimension[outputNumBands];
        //        GridSampleDimension reference = sampleDimensions[0];
        //        ColorInterpretation[] interpretations = new ColorInterpretation[outputNumBands];
        //        switch (outputNumBands) {
        //        case 1:
        //            interpretations[0] = ColorInterpretation.GRAY_INDEX;
        //            break;
        //        case 2:
        //            interpretations[0] = ColorInterpretation.GRAY_INDEX;
        //            interpretations[1] = ColorInterpretation.ALPHA_BAND;
        //            break;
        //
        //        case 3:
        //            interpretations[0] = ColorInterpretation.RED_BAND;
        //            interpretations[1] = ColorInterpretation.GREEN_BAND;
        //            interpretations[2] = ColorInterpretation.BLUE_BAND;
        //            break;
        //
        //        case 4:
        //            interpretations[0] = ColorInterpretation.RED_BAND;
        //            interpretations[1] = ColorInterpretation.GREEN_BAND;
        //            interpretations[2] = ColorInterpretation.BLUE_BAND;
        //            interpretations[3] = ColorInterpretation.ALPHA_BAND;
        //            break;
        //
        //        default:
        //            for (int i = 0; i < outputNumBands; i++) {
        //                interpretations[i] =  ColorInterpretation.UNDEFINED;
        //            }
        //            break;
        //        }
        for (int i = 0; i < newSampleDimensions.length; i++) {
            newSampleDimensions[i] = new GridSampleDimension("Band" + i);
        }

        return newSampleDimensions;
    }

    protected Map<String, ?> getProperties(
            RenderedImage data,
            CoordinateReferenceSystem crs,
            InternationalString name,
            MathTransform gridToCRS,
            GridCoverage2D[] sources,
            Params parameters) {
        Map properties;
        if (sources[0].getProperties() == null) {
            properties = new HashMap<>();
        } else {
            properties = new HashMap<>(sources[0].getProperties());
        }

        // Get the ROI and NoData property from the parameterBlock
        ParameterBlockJAI jai = parameters.parameters;
        int numSources = jai.getNumSources();
        // ROI
        // Object roiParam = jai.getObjectParameter(2);
        ResampledRasters rr = parameters.rr;
        if (rr != null && rr.getRois() != null) {
            ROI[] rois = rr.getRois();
            ROI finalROI = null;
            for (int i = 0; i < numSources; i++) {
                if (finalROI == null) {
                    finalROI = rois[i];
                } else if (rois[i] == null) {
                    // no ROI, the image is full
                    RenderedImage ri = sources[i].getRenderedImage();
                    finalROI =
                            finalROI.add(
                                    new ROIShape(
                                            new Rectangle2D.Double(
                                                    ri.getMinX(),
                                                    ri.getMinY(),
                                                    ri.getWidth(),
                                                    ri.getHeight())));
                } else {
                    finalROI = finalROI.add(rois[i]);
                }
            }
            CoverageUtilities.setROIProperty(properties, finalROI);
        }
        // NoData
        Object nodataParam = jai.getObjectParameter(4);
        if (nodataParam != null && rr != null && rr.hasNoData()) {
            CoverageUtilities.setNoDataProperty(properties, nodataParam);
        }

        return properties;
    }

    protected void extractSources(
            final ParameterValueGroup parameters,
            final Collection<GridCoverage2D> sources,
            final String[] sourceNames)
            throws ParameterNotFoundException, InvalidParameterValueException {
        Utilities.ensureNonNull("parameters", parameters);
        Utilities.ensureNonNull("sources", sources);

        // Extraction of the sources from the parameters
        Object srcCoverages = parameters.parameter("Sources").getValue();

        if (!(srcCoverages instanceof Collection)
                || ((Collection) srcCoverages).isEmpty()
                || !(((Collection) srcCoverages).iterator().next() instanceof GridCoverage2D)) {
            throw new InvalidParameterValueException(
                    Errors.format(ErrorKeys.ILLEGAL_ARGUMENT_$1, "sources"),
                    "sources",
                    srcCoverages);
        }
        // Collection of the sources to use
        Collection<GridCoverage2D> sourceCoverages = (Collection<GridCoverage2D>) srcCoverages;
        sources.addAll(sourceCoverages);
    }

    /**
     * A block of parameters for a {@link GridCoverage2D} processed by the {@link Mosaic} operation.
     *
     * @author Nicola Lagomarsini
     */
    protected static final class Params {

        public ResampledRasters rr;

        /** The parameters to be given to the {@link JAI#createNS} method. */
        public final ParameterBlockJAI parameters;

        /** The {@link GridGeometry2D} object to use for the final {@link GridCoverage2D}. */
        public final GridGeometry2D finalGeometry;

        /**
         * The rendering hints to be given to the {@link JAI#createNS} method. The {@link JAI}
         * instance to use for the {@code createNS} call will be fetch from the {@link
         * Hints#JAI_INSTANCE} key.
         */
        public final Hints hints;

        /** Constructs a new instance of this class with the specified values. */
        Params(
                final ParameterBlockJAI parameters,
                final Hints hints,
                final GridGeometry2D finalGeometry) {
            this.parameters = parameters;
            this.hints = hints;
            this.finalGeometry = finalGeometry;
        }

        /** Returns the first source image, or {@code null} if none. */
        final RenderedImage getSource() {
            final int n = parameters.getNumSources();
            for (int i = 0; i < n; i++) {
                final Object source = parameters.getSource(i);
                if (source instanceof RenderedImage) {
                    return (RenderedImage) source;
                }
            }
            return null;
        }
    }

    /**
     * A container class used for passing the resampled {@link RenderedImage}s and the final {@link
     * GridGeometry2D} created by the {@link GridGeometryPolicy}.resampleGridGeometry() method.
     *
     * @author Nicola Lagomarsini
     */
    private static final class ResampledRasters {
        /** @return The array of the resampled RenderedImages */
        public RenderedImage[] getRasters() {
            return rasters;
        }

        /** Set the array of the resampled RenderedImages */
        public void setRasters(RenderedImage[] rasters) {
            this.rasters = rasters;
        }

        /** @return The {@link GridGeometry2D} object to use for the mosaic */
        public GridGeometry2D getFinalGeometry() {
            return finalGeometry;
        }

        /** Set the {@link GridGeometry2D} object to use for the mosaic */
        public void setFinalGeometry(GridGeometry2D finalGeometry) {
            this.finalGeometry = finalGeometry;
        }

        /** @return The {@link PlanarImage} array to use for the mosaic */
        public PlanarImage[] getAlphas() {
            return alphas;
        }

        /** Sets the array of the external alpha bands */
        public void setAlphas(PlanarImage[] alphas) {
            this.alphas = alphas;
        }

        /** @return The nodata to use for the mosaic */
        public double[] getBackgrounds() {
            return nodata;
        }

        /** Set the nodata values for each mosaic element */
        public void setBackgrounds(double[] nodata) {
            this.nodata = nodata;
        }

        /** @return The roi to use for the mosaic */
        public ROI[] getRois() {
            return rois;
        }

        /** Set the roi values for each mosaic element */
        public void setRois(ROI[] rois) {
            this.rois = rois;
        }

        /** @return boolean indicating that at least one coverage contains nodata */
        public boolean hasNoData() {
            return hasNoData;
        }

        /** Set the hasNoData boolean parameter */
        public void setHasNoData(boolean hasNoData) {
            this.hasNoData = hasNoData;
        }

        /** The array of the resampled RenderedImages */
        private RenderedImage[] rasters;

        /** The {@link GridGeometry2D} object to use for the mosaic */
        private GridGeometry2D finalGeometry;

        /** The {@link PlanarImage} array used for representing external alpha bands */
        private PlanarImage[] alphas;

        /** Array of the final NoData values */
        private double[] nodata;

        /** Array of the final roi values */
        private ROI[] rois;

        /** Boolean indicating if input nodata values are present */
        private boolean hasNoData;
    }
}
