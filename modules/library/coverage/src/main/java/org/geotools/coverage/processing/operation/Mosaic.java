/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 * 
 *    (C) 2014, Open Source Geospatial Foundation (OSGeo)
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

import java.awt.RenderingHints;
import java.awt.image.IndexColorModel;
import java.awt.image.MultiPixelPackedSampleModel;
import java.awt.image.RenderedImage;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
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

import org.geotools.coverage.grid.GridCoverage2D;
import org.geotools.coverage.grid.GridEnvelope2D;
import org.geotools.coverage.grid.GridGeometry2D;
import org.geotools.coverage.grid.ViewType;
import org.geotools.coverage.processing.CoverageProcessingException;
import org.geotools.coverage.processing.OperationJAI;
import org.geotools.factory.GeoTools;
import org.geotools.factory.Hints;
import org.geotools.geometry.Envelope2D;
import org.geotools.image.ImageWorker;
import org.geotools.metadata.iso.citation.Citations;
import org.geotools.parameter.DefaultParameterDescriptor;
import org.geotools.parameter.ImagingParameterDescriptors;
import org.geotools.parameter.ImagingParameters;
import org.geotools.referencing.CRS;
import org.geotools.referencing.operation.transform.ConcatenatedTransform;
import org.geotools.resources.coverage.CoverageUtilities;
import org.geotools.resources.i18n.ErrorKeys;
import org.geotools.resources.i18n.Errors;
import org.geotools.resources.image.ImageUtilities;
import org.geotools.util.Utilities;
import org.jaitools.imageutils.ImageLayout2;
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
import org.opengis.util.InternationalString;

/**
 * This operation does a mosaic of multiple {@link GridCoverage2D}s. The {@link GridCoverage2D}s can have different resolutions; the operation will
 * resample them to the same one. The policies for choosing the output resolution are:
 * <ul>
 * <li>resolution of the FIRST coverage (Default)</li>
 * <li>FINE resolution</li>
 * <li>COARSE resolution</li>
 * <li>resolution of an EXTERNAL GridGeometry</li>
 * </ul>
 * 
 * Note that the operation requires that all the {@link GridCoverage2D}s are in the same CRS, else an exception is thrown.
 * 
 * The input parameters of the operation are:
 * <ul>
 * <li>a Collection of the {@link GridCoverage2D} to mosaic</li>
 * <li>an optional {@link GridGeometry} object for setting the final resolution and BoundingBox</li>
 * <li>an optional {@link String} indicating the policy to use for choosing the resolution</li>
 * <li>an optional {@link double[]} indicating the nodata values to set for the background. Note that the only the first value will be used</li>
 * </ul>
 * 
 * @author Nicola Lagomarsini GesoSolutions S.A.S.
 * 
 */
public class Mosaic extends OperationJAI {

    /** Name for the COVERAGE_INDEX parameter */
    public static final String POLICY = "policy";

    /** Name for the GG2D parameter */
    public static final String GEOMETRY = "geometry";

    /** Name for the Sources parameter */
    public static final String SOURCES_NAME = "Sources";

    /** Name for the Output No Data parameter */
    public static final String OUTNODATA_NAME = "outputNoData";

    /**
     * The parameter descriptor for the Sources.
     */
    public static final ParameterDescriptor SOURCES = new DefaultParameterDescriptor(Citations.JAI,
            SOURCES_NAME, Collection.class, // Value class (mandatory)
            null, // Array of valid values
            null, // Default value
            null, // Minimal value
            null, // Maximal value
            null, // Unit of measure
            true);

    /**
     * The parameter descriptor for the GridGeometry to use.
     */
    public static final ParameterDescriptor<GridGeometry> GG = new DefaultParameterDescriptor<GridGeometry>(
            Citations.JAI, GEOMETRY, GridGeometry.class, // Value class (mandatory)
            null, // Array of valid values
            null, // Default value
            null, // Minimal value
            null, // Maximal value
            null, // Unit of measure
            false);

    /**
     * The parameter descriptor for the GridGeometry choosing policy.
     */
    public static final ParameterDescriptor<String> GEOMETRY_POLICY = new DefaultParameterDescriptor<String>(
            Citations.JAI, POLICY, String.class, // Value class (mandatory)
            null, // Array of valid values
            null, // Default value
            null, // Minimal value
            null, // Maximal value
            null, // Unit of measure
            false);

    /**
     * The parameter descriptor for the Transformation Choice.
     */
    public static final ParameterDescriptor<double[]> OUTPUT_NODATA = new DefaultParameterDescriptor<double[]>(
            Citations.JAI, OUTNODATA_NAME, double[].class, // Value class (mandatory)
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
        REPLACED_DESCRIPTORS = Collections.unmodifiableSet(replacedDescriptors);
    }

    /**
     * Enum used for choosing the output {@link GridGeometry2D} to use and then resampling all the {@link GridCoverage2D} to its resolution.
     * 
     * @author Nicola Lagomarsini GesoSolutions S.A.S.
     * 
     */
    public enum GridGeometryPolicy {
        FIRST("first") {
            @Override
            public ResampledRasters resampleGridGeometry(GridCoverage2D[] sources,
                    GridGeometry2D external, ParameterValueGroup parameters) {
                // Index associated to the first coverage
                int index = PRIMARY_SOURCE_INDEX;
                // Selection of the first GridGeometry2D object to use
                GridGeometry2D finalGG = extractFinalGridGeometry(sources, index);
                // GridCoverage resampling
                return resampleCoverages(sources, finalGG, parameters);
            }
        },
        FINE("fine") {
            @Override
            public ResampledRasters resampleGridGeometry(GridCoverage2D[] sources,
                    GridGeometry2D external, ParameterValueGroup parameters) {

                // Number of the sources to use
                int numSources = sources.length;
                // Selection of the first GridGeometry
                GridGeometry2D grid = sources[0].getGridGeometry();
                Envelope2D env = grid.getEnvelope2D();
                GridEnvelope2D gridEnv = grid.getGridRange2D();

                // Method for searching the index at the highest resolution. Suppose that the coverages contains the same
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
                return resampleCoverages(sources, finalGG, parameters);
            }
        },
        COARSE("coarse") {
            @Override
            public ResampledRasters resampleGridGeometry(GridCoverage2D[] sources,
                    GridGeometry2D external, ParameterValueGroup parameters) {
                // Number of the sources to use
                int numSources = sources.length;
                // Selection of the first GridGeometry
                GridGeometry2D grid = sources[0].getGridGeometry();
                Envelope2D env = grid.getEnvelope2D();
                GridEnvelope2D gridEnv = grid.getGridRange2D();

                // Method for searching the index at the lowest resolution. Suppose that the coverages contains the same
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
                return resampleCoverages(sources, finalGG, parameters);
            }
        },
        EXTERNAL("external") {
            @Override
            public ResampledRasters resampleGridGeometry(GridCoverage2D[] sources,
                    GridGeometry2D external, ParameterValueGroup parameters) {
                // Check if the external GridGeometry is present
                if (external == null) {
                    throw new CoverageProcessingException("No input GridGeometry found");
                }
                // Coverage resampling
                return resampleCoverages(sources, external, parameters);
            }
        };

        /** Name associated to the {@link GridGeometryPolicy} object */
        private String name;

        private GridGeometryPolicy(String name) {
            this.name = name;
        }

        /**
         * Method for resampling the input {@link GridCoverage2D} objects. The output of the method is an object containing the resampled
         * {@link RenderedImage}s and the final {@link GridGeometry2D} object to use.
         * 
         * @param sources
         * @param external
         * @param parameters
         * @return
         */
        public abstract ResampledRasters resampleGridGeometry(GridCoverage2D[] sources,
                GridGeometry2D external, ParameterValueGroup parameters);

        /**
         * Static method to use for choosing the {@link GridGeometryPolicy} object associated to the input string.
         * 
         * @param policyString
         * @return
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
         * Private method for resampling the {@link GridCoverage2D}s to the same resolution imposed by the {@link GridGeometry2D} object.
         * 
         * @param sources
         * @param external
         * @param parameters
         * @return
         */
        private static ResampledRasters resampleCoverages(GridCoverage2D[] sources,
                GridGeometry2D external, ParameterValueGroup parameters) {
            // Number of the sources to use
            int numSources = sources.length;

            // Creation of an array of the RenderedImages to use
            RenderedImage[] rasters = new RenderedImage[numSources];

            // Selection of the GridToWorld transformation associated to the External GG2D
            MathTransform g2w = external.getGridToCRS2D(PixelOrientation.UPPER_LEFT);
            // Initial null value for NoData
            Double nodata = null;

            // Check if the output nodata value is set as parameter
            Object outputNodata = parameters.parameter(OUTNODATA_NAME).getValue();
            if (outputNodata != null && outputNodata instanceof double[]) {
                nodata = ((double[]) outputNodata)[0];
            }

            // Cycle around the various sources
            for (int i = 0; i < numSources; i++) {
                // For each source, create a new GridGeometry which at the same resolution of the imposed one
                GridCoverage2D coverage = sources[i];
                GridGeometry2D inputGG = coverage.getGridGeometry();

                // Check if the transform from one gridGeometry to the other is an Identity transformation
                MathTransform g2wS = inputGG.getGridToCRS2D(PixelOrientation.UPPER_LEFT);
                MathTransform w2gD = external.getCRSToGrid2D(PixelOrientation.UPPER_LEFT);
                // Creation of a Concatenated transformation in order to check if the final transformation from
                // source space to the final space is an identity.
                MathTransform concatenated = ConcatenatedTransform.create(g2wS, w2gD);

                // No operation must be done if the transformation is an Identity
                if (concatenated != null && concatenated.isIdentity()) {
                    rasters[i] = coverage.getRenderedImage();
                } else {
                    // New GridGeometry
                    GridGeometry2D newGG = new GridGeometry2D(PixelInCell.CELL_CORNER, g2w,
                            inputGG.getEnvelope(), GeoTools.getDefaultHints());
                    double fillValue = 0;
                    // Selection of the nodata value
                    if (nodata == null) {
                        fillValue = CoverageUtilities.getBackgroundValues(coverage)[0];
                    } else {
                        fillValue = nodata;
                    }

                    // Resample to the new resolution
                    rasters[i] = GridCoverage2DRIA.create(coverage, newGG, fillValue);
                }
            }

            // Create the final object containing the final GridGeometry and the resampled RenderedImages
            ResampledRasters rr = new ResampledRasters();
            rr.setFinalGeometry(external);
            rr.setRasters(rasters);
            return rr;
        }

        /**
         * This method creates a new {@link GridGeometry2D} object based on that of the {@link GridCoverage2D} defined by the index.
         * 
         * @param sources
         * @param index
         * @return
         */
        private static GridGeometry2D extractFinalGridGeometry(GridCoverage2D[] sources, int index) {
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
            GridGeometry2D finalGG = new GridGeometry2D(PixelInCell.CELL_CORNER, g2w, bbox,
                    GeoTools.getDefaultHints());
            return finalGG;
        }
    }

    public Mosaic() {
        super(new MosaicDescriptor(), new ImagingParameterDescriptors(
                getOperationDescriptor("Mosaic"), REPLACED_DESCRIPTORS));
    }

    public Coverage doOperation(final ParameterValueGroup parameters, final Hints hints)
            throws CoverageProcessingException {
        /*
         * Extracts the source grid coverages now as a List. The sources will be set in the ParameterBlockJAI (as RenderedImages) later.
         */
        final Collection<GridCoverage2D> sourceCollection = new ArrayList<GridCoverage2D>();
        // The ViewType is used in post processing
        ViewType primarySourceType = extractSources(parameters, sourceCollection);
        // Selection of the source number
        int numSources = sourceCollection.size();
        GridCoverage2D[] sources = new GridCoverage2D[numSources];
        // Creation of an array of GridCoverage2D from the input collection
        sourceCollection.toArray(sources);
        // Selection of the CRS of the first coverage in order to check that the CRS is the same for all the GridCoverages
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
        GridCoverage2D coverage = deriveGridCoverage(sources, params);
        if (primarySourceType != null) {
            coverage = coverage.view(primarySourceType);
        }
        return coverage;
    }

    /**
     * Extraction of the sources from the parameter called SOURCES. The sources are stored inside a List. The output of the method is an ViewType to
     * use in post processing.
     * 
     * @param parameters
     * @param sources
     * @return
     * @throws ParameterNotFoundException
     * @throws InvalidParameterValueException
     */
    private ViewType extractSources(final ParameterValueGroup parameters,
            final Collection<GridCoverage2D> sources) throws ParameterNotFoundException,
            InvalidParameterValueException {
        Utilities.ensureNonNull("parameters", parameters);
        Utilities.ensureNonNull("sources", sources);

        // Extraction of the sources from the parameters
        Object srcCoverages = parameters.parameter("sources").getValue();

        if (!(srcCoverages instanceof Collection) || ((Collection) srcCoverages).isEmpty()
                || !(((Collection) srcCoverages).iterator().next() instanceof GridCoverage2D)) {
            throw new InvalidParameterValueException(Errors.format(ErrorKeys.ILLEGAL_ARGUMENT_$1,
                    "sources"), "sources", srcCoverages);
        }
        // Collection of the sources to use
        Collection<GridCoverage2D> sourceCoverages = (Collection<GridCoverage2D>) srcCoverages;
        // ViewType object
        ViewType type = null;
        // Check if the operation must be computed on GeoPhysical values
        final boolean computeOnGeophysicsValues = computeOnGeophysicsValues(parameters);
        // Counter for the coverages
        int i = 0;
        // Cycle on all the Sources
        for (GridCoverage2D source : sourceCoverages) {
            if (source != null) {
                // Add the view type to the coverage
                if (computeOnGeophysicsValues) {
                    final GridCoverage2D old = source;
                    source = source.view(ViewType.GEOPHYSICS);
                    if (i == PRIMARY_SOURCE_INDEX) {
                        type = (old == source) ? ViewType.GEOPHYSICS : ViewType.PACKED;
                    }
                }
                // Store the i-th source
                sources.add(source);
            }
            // Counter update
            i++;
        }
        return type;
    }

    /**
     * Prepares the parameters to store in the {@link ParameterBlockJAI} object and resample the input {@link GridCoverage2D}.
     * 
     * @param parameters
     * @param sources
     * @param hints
     * @return
     */
    private Params prepareParameters(final ParameterValueGroup parameters,
            GridCoverage2D[] sources, Hints hints) {
        final ImagingParameters copy = (ImagingParameters) descriptor.createValue();
        final ParameterBlockJAI block = (ParameterBlockJAI) copy.parameters;

        // org.geotools.parameter.Parameters.copy(parameters, copy);

        // Object indicating the policy to use for resampling all the GridCoverages to the same GridGeometry
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
        // Resample to the defined GridGeometry
        ResampledRasters rr = policy.resampleGridGeometry(sources, gg, parameters);
        // Get the resampled RenderedImages
        RenderedImage[] rasters = rr.getRasters();

        // Setting of the final GridGeometry
        GridGeometry2D finalGeometry = rr.getFinalGeometry();
        if (finalGeometry == null) {
            throw new CoverageProcessingException("No final GridGeometry found");
        }

        // Setting the source rasters for the mosaic
        int numSources = rasters.length;
        for (int i = 0; i < numSources; i++) {
            block.setSource(rasters[i], i);
        }

        // Setting the nodata values for the areas not covered by any GridCoverage.
        double nodata = 0;
        // Check if the output nodata value is present
        Object outputNodata = parameters.parameter(OUTNODATA_NAME).getValue();
        if (outputNodata != null && outputNodata instanceof double[]) {
            nodata = ((double[]) outputNodata)[0];
        } else {
            nodata = CoverageUtilities.getBackgroundValues(sources[PRIMARY_SOURCE_INDEX])[0];
        }
        // Setting of the output nodata
        block.setParameter("backgroundValues", new double[] { nodata });

        // Setting of the Threshold to use
        double threshold = CoverageUtilities.getMosaicThreshold(rasters[PRIMARY_SOURCE_INDEX]
                .getSampleModel().getDataType());
        // Setting of the Threshold object to use for the mosaic
        block.setParameter("sourceThreshold", new double[][] { { threshold } });

        // Setting of the ROI associated to each GridCoverage
        // We need to add its roi in order to avoid problems with the mosaics sources overlapping
        ROI[] rois = new ROI[numSources];
        // Cycle on each coverage in order to add the associated ROI
        for (int i = 0; i < numSources; i++) {
            rois[i] = new ROIShape(PlanarImage.wrapRenderedImage(rasters[i]).getBounds());
        }
        block.setParameter("sourceROI", rois);

        // Setting of the Mosaic type as Overlay
        block.setParameter("mosaicType", MosaicDescriptor.MOSAIC_TYPE_OVERLAY);

        // Setting of the optional Alpha channels
        PlanarImage[] alpha = new PlanarImage[numSources];
        boolean alphaChannel = true;

        for (int i = 0; i < numSources; i++) {
            RenderedImage img = rasters[i];
            // ImageWorker to use for elaborating each raster
            ImageWorker w = new ImageWorker(img);
            // I have to force going to ComponentColorModel in
            // case the image is indexed.
            if (img.getSampleModel() instanceof MultiPixelPackedSampleModel
                    || img.getColorModel() instanceof IndexColorModel) {
                w.forceComponentColorModel();
                img = w.getRenderedImage();
            }
            boolean hasAlpha = img.getColorModel().hasAlpha();
            if (hasAlpha) {
                alphaChannel &= hasAlpha;
                alpha[i] = w.retainLastBand().getPlanarImage();
            }
        }
        // If at least one image contains Alpha channel, it is used for the mosaic
        if (alphaChannel) {
            block.setParameter("sourceAlpha", alpha);
        }

        // Creation of the finel Parameters
        return new Params(block, hints, finalGeometry);
    }

    /**
     * Applies a JAI operation to the coverages. This method is invoked by {@link #doOperation}. This implementation performs the following steps:
     * 
     * <ul>
     * <li>Applied the JAI operation using {@link #createRenderedImage}.</li>
     * <li>Wraps the result in a {@link GridCoverage2D} object.</li>
     * </ul>
     * 
     * @param sources The source coverages.
     * @param parameters Parameters, rendering hints and coordinate reference system to use.
     * @return The result as a grid coverage.
     * 
     * @see #doOperation
     * @see JAI#createNS
     */
    private GridCoverage2D deriveGridCoverage(final GridCoverage2D[] sources,
            final Params parameters) {
        GridCoverage2D primarySource = sources[PRIMARY_SOURCE_INDEX];

        /*
         * Set the rendering hints image layout. Only the following properties will be set:
         * 
         * - Width - Height
         */
        RenderingHints hints = ImageUtilities.getRenderingHints(parameters.getSource());
        // Addition of the Hints
        if (parameters.hints != null) {
            if (hints != null) {
                hints.add(parameters.hints); // May overwrite the image layout we have just set.
            } else {
                hints = parameters.hints;
            }
        }
        // Layout associated to the input RenderingHints
        ImageLayout layout = (hints != null) ? (ImageLayout) hints.get(JAI.KEY_IMAGE_LAYOUT) : null;

        // Check on the ImageLayout
        if (layout != null) {
            // Unset the previous dimension parameters
            layout.unsetValid(ImageLayout.MIN_X_MASK);
            layout.unsetValid(ImageLayout.MIN_Y_MASK);
            layout.unsetValid(ImageLayout.WIDTH_MASK);
            layout.unsetValid(ImageLayout.HEIGHT_MASK);
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
        hints.put(JAI.KEY_IMAGE_LAYOUT, layout);

        /*
         * Performs the operation using JAI and construct the new grid coverage. Uses the coordinate system from the main source coverage in order to
         * preserve the extra dimensions (if any). The first two dimensions should be equal to the coordinate system set in the 'parameters' block.
         */
        final InternationalString name = deriveName(sources, -1, null);
        final CoordinateReferenceSystem crs = primarySource.getCoordinateReferenceSystem();
        final MathTransform toCRS = parameters.finalGeometry.getGridToCRS();
        final RenderedImage data = createRenderedImage(parameters.parameters, hints);
        final Map<String, ?> properties = getProperties(data, crs, name, toCRS, sources, null);
        return getFactory(parameters.hints).create(name, // The grid coverage name
                data, // The underlying data
                crs, // The coordinate system (may not be 2D).
                toCRS, // The grid transform (may not be 2D).
                null, // The sample dimensions
                sources, // The source grid coverages.
                properties); // Properties
    }

    /**
     * A block of parameters for a {@link GridCoverage2D} processed by the {@link Mosaic} operation.
     * 
     * @author Nicola Lagomarsini
     */
    protected static final class Params {

        /**
         * The parameters to be given to the {@link JAI#createNS} method.
         */
        public final ParameterBlockJAI parameters;

        /**
         * The {@link GridGeometry2D} object to use for the final {@link GridCoverage2D}.
         */
        public final GridGeometry2D finalGeometry;

        /**
         * The rendering hints to be given to the {@link JAI#createNS} method. The {@link JAI} instance to use for the {@code createNS} call will be
         * fetch from the {@link Hints#JAI_INSTANCE} key.
         */
        public final Hints hints;

        /**
         * Constructs a new instance of this class with the specified values.
         */
        Params(final ParameterBlockJAI parameters, final Hints hints,
                final GridGeometry2D finalGeometry) {
            this.parameters = parameters;
            this.hints = hints;
            this.finalGeometry = finalGeometry;
        }

        /**
         * Returns the first source image, or {@code null} if none.
         */
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
     * A container class used for passing the resampled {@link RenderedImage}s and the final {@link GridGeometry2D} created by the
     * {@link GridGeometryPolicy}.resampleGridGeometry() method.
     * 
     * @author Nicola Lagomarsini
     */
    private static final class ResampledRasters {
        /**
         * @return The array of the resampled RenderedImages
         */
        public RenderedImage[] getRasters() {
            return rasters;
        }

        /**
         * Set the array of the resampled RenderedImages
         * 
         * @param rasters
         */
        public void setRasters(RenderedImage[] rasters) {
            this.rasters = rasters;
        }

        /**
         * @return The {@link GridGeometry2D} object to use for the mosaic
         */
        public GridGeometry2D getFinalGeometry() {
            return finalGeometry;
        }

        /**
         * Set the {@link GridGeometry2D} object to use for the mosaic
         * 
         * @param finalGeometry
         */
        public void setFinalGeometry(GridGeometry2D finalGeometry) {
            this.finalGeometry = finalGeometry;
        }

        /**
         * The array of the resampled RenderedImages
         */
        private RenderedImage[] rasters;

        /**
         * The {@link GridGeometry2D} object to use for the mosaic
         */
        private GridGeometry2D finalGeometry;
    }
}
