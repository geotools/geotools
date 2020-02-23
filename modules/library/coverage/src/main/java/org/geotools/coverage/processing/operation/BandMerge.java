/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2015, Open Source Geospatial Foundation (OSGeo)
 *    (C) 2014 TOPP - www.openplans.org.
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
import it.geosolutions.jaiext.vectorbin.ROIGeometry;
import java.awt.RenderingHints;
import java.awt.geom.AffineTransform;
import java.awt.geom.NoninvertibleTransformException;
import java.awt.image.RenderedImage;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.media.jai.ImageLayout;
import javax.media.jai.JAI;
import javax.media.jai.ParameterBlockJAI;
import javax.media.jai.PlanarImage;
import javax.media.jai.ROI;
import org.geotools.coverage.GridSampleDimension;
import org.geotools.coverage.grid.GridCoverage2D;
import org.geotools.coverage.grid.GridEnvelope2D;
import org.geotools.coverage.grid.GridGeometry2D;
import org.geotools.coverage.processing.CoverageProcessingException;
import org.geotools.coverage.processing.OperationJAI;
import org.geotools.coverage.util.CoverageUtilities;
import org.geotools.geometry.Envelope2D;
import org.geotools.geometry.jts.JTS;
import org.geotools.image.util.ImageUtilities;
import org.geotools.metadata.i18n.ErrorKeys;
import org.geotools.metadata.i18n.Errors;
import org.geotools.metadata.iso.citation.Citations;
import org.geotools.parameter.DefaultParameterDescriptor;
import org.geotools.parameter.ImagingParameterDescriptors;
import org.geotools.parameter.ImagingParameters;
import org.geotools.referencing.operation.transform.AffineTransform2D;
import org.geotools.util.Utilities;
import org.geotools.util.factory.Hints;
import org.geotools.util.logging.Logging;
import org.locationtech.jts.geom.Geometry;
import org.opengis.coverage.Coverage;
import org.opengis.geometry.MismatchedDimensionException;
import org.opengis.metadata.spatial.PixelOrientation;
import org.opengis.parameter.InvalidParameterValueException;
import org.opengis.parameter.ParameterDescriptor;
import org.opengis.parameter.ParameterNotFoundException;
import org.opengis.parameter.ParameterValueGroup;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.opengis.referencing.datum.PixelInCell;
import org.opengis.referencing.operation.MathTransform;
import org.opengis.referencing.operation.MathTransform2D;
import org.opengis.referencing.operation.TransformException;
import org.opengis.util.InternationalString;

/**
 * {@link OperationJAI} subclass used for executing the "Merge" of multiple coverages into a single
 * coverage with multiple bands. This operation can be used also for merging coverages which are not
 * aligned and with different resolutions. The user should only set: *
 *
 * <ul>
 *   <li>the Coverages (Note that they must be in the same CRS).
 *   <li>the optional Geometry to use as ROI.
 *   <li>The optional policy for choosing the Grid To World transformation(FIRST for that of the
 *       first coverage, LAST for the last one, INDEX for that of a coverage defined by the "index"
 *       parameter).
 *   <li>the index parameter for choosing the main Coverage.
 * </ul>
 *
 * @author Nicola Lagomarsini, GeoSolutions S.A.S.
 */
public class BandMerge extends OperationJAI {

    /** Name for the TRANSFORM_CHOICE parameter */
    public static final String TRANSFORM_CHOICE = "transform_choice";

    /** Name for the COVERAGE_INDEX parameter */
    public static final String COVERAGE_INDEX = "coverage_idx";

    /** Name for the GEOMETRY parameter */
    public static final String GEOMETRY = "geometry";

    /** The parameter descriptor for the Sources. */
    public static final ParameterDescriptor SOURCES =
            new DefaultParameterDescriptor(
                    Citations.JAI,
                    "Sources",
                    Collection.class, // Value class (mandatory)
                    null, // Array of valid values
                    null, // Default value
                    null, // Minimal value
                    null, // Maximal value
                    null, // Unit of measure
                    true);

    /** The parameter descriptor for the Transformation Choice. */
    public static final ParameterDescriptor TRANSFORM_CHOICE_PARAM =
            new DefaultParameterDescriptor(
                    Citations.JAI,
                    TRANSFORM_CHOICE,
                    String.class, // Value class (mandatory)
                    null, // Array of valid values
                    null, // Default value
                    null, // Minimal value
                    null, // Maximal value
                    null, // Unit of measure
                    false);

    /**
     * The parameter descriptor for the Source index to use for selecting the Affine Transformation
     * to use.
     */
    public static final ParameterDescriptor INDEX =
            new DefaultParameterDescriptor(
                    Citations.JAI,
                    COVERAGE_INDEX,
                    Integer.class, // Value class (mandatory)
                    null, // Array of valid values
                    0, // Default value
                    0, // Minimal value
                    null, // Maximal value
                    null, // Unit of measure
                    false);

    /** The parameter descriptor for the Transformation Choice. */
    public static final ParameterDescriptor GEOMETRY_PARAM =
            new DefaultParameterDescriptor(
                    Citations.JAI,
                    GEOMETRY,
                    Geometry.class, // Value class (mandatory)
                    null, // Array of valid values
                    null, // Default value
                    null, // Minimal value
                    null, // Maximal value
                    null, // Unit of measure
                    false);

    private static final Logger LOGGER = Logging.getLogger(BandMerge.class);

    private static Set<ParameterDescriptor> REPLACED_DESCRIPTORS;

    // Replace the old parameter descriptor group with a new one with the old parameters and the new
    // ones defined above.
    static {
        final Set<ParameterDescriptor> replacedDescriptors = new HashSet<ParameterDescriptor>();
        replacedDescriptors.add(SOURCES);
        replacedDescriptors.add(INDEX);
        replacedDescriptors.add(TRANSFORM_CHOICE_PARAM);
        replacedDescriptors.add(GEOMETRY_PARAM);
        REPLACED_DESCRIPTORS = Collections.unmodifiableSet(replacedDescriptors);
    }

    /**
     * Enum used for selecting an Affine Transformation to use for backward mapping the final
     * coverage pixel to the Model Space. The method "getTransformationList" returns a List of the
     * AffineTransformations to use for backward mapping the destination pixels into each source
     * coverage pixel.
     *
     * @author Nicola Lagomarsini, GeoSolutions S.A.S.
     */
    public enum TransformList {
        FIRST("first") {
            @Override
            public AffineTransform getGridToCRS2D(List<GridGeometry2D> list, int index) {
                if (list.isEmpty()) {
                    throw new IllegalArgumentException("No Affine Transformation found");
                }
                return getAffineTransform(list.get(0), true);
            }

            @Override
            public AffineTransform getCRStoGrid2D(List<GridGeometry2D> list, int index) {
                if (list.isEmpty()) {
                    throw new IllegalArgumentException("No Affine Transformation found");
                }
                return getAffineTransform(list.get(0), false);
            }
        },
        LAST("last") {
            @Override
            public AffineTransform getGridToCRS2D(List<GridGeometry2D> list, int index) {
                if (list.isEmpty()) {
                    throw new IllegalArgumentException("No Affine Transformation found");
                }
                return getAffineTransform(list.get(list.size() - 1), true);
            }

            @Override
            public AffineTransform getCRStoGrid2D(List<GridGeometry2D> list, int index) {
                if (list.isEmpty()) {
                    throw new IllegalArgumentException("No Affine Transformation found");
                }
                return getAffineTransform(list.get(list.size() - 1), false);
            }
        },
        INDEX("index") {
            @Override
            public AffineTransform getGridToCRS2D(List<GridGeometry2D> list, int index) {
                if (index < 0) {
                    // Take the first available
                    return FIRST.getGridToCRS2D(list, index);
                } else if (index >= list.size()) {
                    // Take the last available
                    return LAST.getGridToCRS2D(list, index);
                }
                return getAffineTransform(list.get(index), true);
            }

            @Override
            public AffineTransform getCRStoGrid2D(List<GridGeometry2D> list, int index) {
                if (index < 0) {
                    // Take the first available
                    return FIRST.getCRStoGrid2D(list, index);
                } else if (index >= list.size()) {
                    // Take the last available
                    return LAST.getCRStoGrid2D(list, index);
                }
                return getAffineTransform(list.get(index), false);
            }
        };

        /**
         * Returns a List of AffineTransformations objects to use for backward mapping the
         * destination image pixels into each source image
         */
        public List<AffineTransform> getTransformationList(List<GridGeometry2D> list, int index) {
            // Creation of a List of Transformations
            List<AffineTransform> transforms = new ArrayList<AffineTransform>();
            // Get the g2w transform to use for the remapping
            AffineTransform g2w = getGridToCRS2D(list, index);
            // Get all the other w2g transforms to concatenate for the remapping
            for (GridGeometry2D gg2D : list) {
                AffineTransform tr = new AffineTransform(getAffineTransform(gg2D, false));
                tr.concatenate(g2w);
                transforms.add(tr);
            }
            // Returns the transformations list
            return transforms;
        }

        /** Name associated to the {@link TransformList} object */
        private String name;

        // private constructor, avoiding instantiation
        private TransformList(String name) {
            this.name = name;
        }

        /**
         * Returns the Grid To World transformation from the following GridGeometry list. The result
         * depends on the implementation
         */
        public abstract AffineTransform getGridToCRS2D(List<GridGeometry2D> list, int index);

        /**
         * Returns the World To Grid transformation from the following GridGeometry list. The result
         * depends on the implementation
         */
        public abstract AffineTransform getCRStoGrid2D(List<GridGeometry2D> list, int index);

        /** Static method for choosing the TransformList Object associated to the input string */
        public static TransformList getTransformList(String choice) {
            if (choice.equalsIgnoreCase(LAST.name)) {
                return LAST;
            } else if (choice.equalsIgnoreCase(INDEX.name)) {
                return INDEX;
            }
            return FIRST;
        }

        /** Static method for taking the AffineTransform from the List */
        private static AffineTransform getAffineTransform(GridGeometry2D gg2D, boolean grid2crs) {

            MathTransform2D tr = null;
            if (grid2crs) {
                tr = gg2D.getGridToCRS2D(PixelOrientation.UPPER_LEFT);
            } else {
                tr = gg2D.getCRSToGrid2D(PixelOrientation.UPPER_LEFT);
            }
            if (tr instanceof AffineTransform2D) {
                return (AffineTransform2D) tr;
            } else {
                throw new IllegalArgumentException(tr.toString() + " is not an AffineTransform");
            }
        }
    }

    public BandMerge() {
        super(
                getOperationDescriptor("BandMerge"),
                new ImagingParameterDescriptors(
                        getOperationDescriptor("BandMerge"), REPLACED_DESCRIPTORS));
    }

    @Override
    public Coverage doOperation(ParameterValueGroup parameters, Hints hints)
            throws CoverageProcessingException {
        /*
         * Extracts the source grid coverages now as a List. The sources will be set in the ParameterBlockJAI (as RenderedImages) later.
         */
        final Collection<GridCoverage2D> sourceCollection = new ArrayList<GridCoverage2D>();
        extractSources(parameters, sourceCollection);
        // Selection of the first coverage
        GridCoverage2D coverage = sourceCollection.iterator().next();
        // CRS to use. The first CRS is used
        final CoordinateReferenceSystem crs = coverage.getCoordinateReferenceSystem2D();
        // Global bounding Box to use
        Envelope2D globalBbox = new Envelope2D();
        // Transformation choice string parameter
        String transChoice = (String) parameters.parameter(TRANSFORM_CHOICE).getValue();
        // The TransformList object is initilaized to FIRST in order to take the first element.
        TransformList choice = TransformList.FIRST;
        // Setting of the correct TransformList object to use if the parameter is present
        if (transChoice != null && !transChoice.isEmpty()) {
            choice = TransformList.getTransformList(transChoice);
        }

        // Source number
        int size = sourceCollection.size();

        // GridGeometry List for all the sources
        List<GridGeometry2D> gg2D = new ArrayList<GridGeometry2D>(size);

        // Loop through the
        for (GridCoverage2D source : sourceCollection) {
            if (source == null) {
                size--;
                continue;
            }
            // Expand the BBOX for containing all the input Coverages
            globalBbox.include(source.getEnvelope2D());
            // Add the gridGeometry associated to the coverage
            gg2D.add(source.getGridGeometry());
        }

        // Creation of the list of the transformations to use.
        List<AffineTransform> tr = choice.getTransformationList(gg2D, getIndex(parameters));
        // Selection of the GridToWorld transformation to use for the final coverage
        AffineTransform2D gridToCRS =
                new AffineTransform2D(choice.getGridToCRS2D(gg2D, getIndex(parameters)));
        // Selection of the WorldToGrid transformation to use for the final coverage
        AffineTransform2D crsToGrid =
                new AffineTransform2D(choice.getCRStoGrid2D(gg2D, getIndex(parameters)));
        // Storing the input sources into and array
        GridCoverage2D[] sources = new GridCoverage2D[size];
        sourceCollection.toArray(sources);
        // Creation of the ParameterBlockJAI object to pass to JAI.
        ParameterBlockJAI block;
        try {
            block = prepareParameters(parameters, sources, tr, crsToGrid);
        } catch (MismatchedDimensionException e) {
            throw new CoverageProcessingException(e);
        } catch (ParameterNotFoundException e) {
            throw new CoverageProcessingException(e);
        } catch (TransformException e) {
            throw new CoverageProcessingException(e);
        }
        /*
         * Applies the operation.
         */
        return deriveGridCoverage(
                sources, new BandMergeParams(crs, gridToCRS, globalBbox, block, hints));
    }

    /**
     * Method for searching the index of the coverage associated to the Main g2w transformation If
     * nothing is found the first coverage is taken(index = 0)
     */
    private int getIndex(ParameterValueGroup parameters) {
        // Get the index parameter from the parameter group
        Object idx = parameters.parameter(COVERAGE_INDEX).getValue();
        if (idx != null && idx instanceof Integer) {
            return ((Integer) idx).intValue();
        }
        // If nothing is found, then the index for the first coverage is returned
        return 0;
    }

    /**
     * Extraction of the sources from the parameter called SOURCES. The sources are stored inside a
     * List.
     */
    protected void extractSources(
            final ParameterValueGroup parameters, final Collection<GridCoverage2D> sources)
            throws ParameterNotFoundException, InvalidParameterValueException {
        Utilities.ensureNonNull("parameters", parameters);
        Utilities.ensureNonNull("sources", sources);

        // Extraction of the sources from the parameters
        Object srcCoverages = parameters.parameter("sources").getValue();

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
        // Cycle on all the Sources
        for (GridCoverage2D source : sourceCoverages) {
            // Store the i-th source
            sources.add(source);
        }
    }

    /**
     * Applies the BandMerge operation to a grid coverage. The following steps are performed:
     *
     * <ul>
     *   <li>Gets the {@linkplain GridSampleDimension sample dimensions} for the target images by
     *       invoking the {@link #deriveSampleDimension deriveSampleDimension(...)} method.
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
            final GridCoverage2D[] sources, final BandMergeParams parameters) {
        GridCoverage2D primarySource = sources[PRIMARY_SOURCE_INDEX];
        /*
         * Gets the target SampleDimensions. If they are identical to the SampleDimensions of one of the source GridCoverage2D, then this
         * GridCoverage2D will be used at the primary source. It will affect the target GridCoverage2D's name and the visible band. Then, a new color
         * model will be constructed from the new SampleDimensions, taking in account the visible band.
         */
        final GridSampleDimension[][] list = new GridSampleDimension[sources.length][];
        for (int i = 0; i < list.length; i++) {
            if (sources[i] == null) {
                continue;
            }
            list[i] = sources[i].getSampleDimensions();
        }
        // Newly ordered GridSampleDimensions
        final GridSampleDimension[] sampleDims = deriveSampleDimension(list, null);
        int primarySourceIndex = PRIMARY_SOURCE_INDEX;
        /*
         * Set the rendering hints image layout. Only the following properties will be set:
         *
         * - Color model
         */
        RenderingHints hints = ImageUtilities.getRenderingHints(parameters.getSource());
        ImageLayout layout = (hints != null) ? (ImageLayout) hints.get(JAI.KEY_IMAGE_LAYOUT) : null;

        // Selection of the Bounding Box to use if present
        Envelope2D bbox = parameters.bbox;

        if (layout != null) {
            // If BBOX is present the it is added to the layout
            if (bbox != null) {
                updateLayout(parameters, layout, bbox);
            }

            if (hints == null) {
                hints = new RenderingHints(JAI.KEY_IMAGE_LAYOUT, layout);
            } else {
                hints.put(JAI.KEY_IMAGE_LAYOUT, layout);
            }
        } else if (bbox != null) {
            // New Layout Creation
            layout = new ImageLayout2();
            // Setting of the layout associated to the Final BoundingBox
            updateLayout(parameters, layout, bbox);

            if (hints == null) {
                hints = new RenderingHints(JAI.KEY_IMAGE_LAYOUT, layout);
            } else {
                hints.put(JAI.KEY_IMAGE_LAYOUT, layout);
            }
        }
        // Setting of the Hints to use
        if (parameters.hints != null) {
            if (hints != null) {
                hints.add(parameters.hints); // May overwrite the image layout we have just set.
            } else {
                hints = parameters.hints;
            }
        }

        /*
         * Performs the operation using JAI and construct the new grid coverage. Uses the coordinate system from the main source coverage in order to
         * preserve the extra dimensions (if any). The first two dimensions should be equal to the coordinate system set in the 'parameters' block.
         */
        final InternationalString name = deriveName(sources, primarySourceIndex, null);
        final CoordinateReferenceSystem crs = primarySource.getCoordinateReferenceSystem();
        final MathTransform toCRS = parameters.gridToCRS;
        final RenderedImage data = createRenderedImage(parameters.parameters, hints);
        final Map properties = getProperties(data, crs, name, toCRS, sources, parameters);
        // The gridToCRS refers to the corner. Make sure to create a GridGeometry accordingly
        GridGeometry2D gridGeometry2D =
                new GridGeometry2D(
                        new GridEnvelope2D(PlanarImage.wrapRenderedImage(data).getBounds()),
                        PixelInCell.CELL_CORNER,
                        toCRS,
                        crs,
                        parameters.hints);
        return getFactory(parameters.hints)
                .create(
                        name, // The grid coverage name
                        data, // The underlying data
                        gridGeometry2D,
                        sampleDims, // The sample dimensions
                        sources, // The source grid coverages.
                        properties); // Properties
    }

    /** This method is used for setting the final image layout. */
    private void updateLayout(
            final BandMergeParams parameters, ImageLayout layout, Envelope2D bbox) {
        // Creation of a GridGeoemtry with the selected BBOX and the defined World2Grid
        // transformation
        GridGeometry2D gg2D =
                new GridGeometry2D(PixelInCell.CELL_CORNER, parameters.gridToCRS, bbox, null);

        // Selection of the GridEnvelope from the GridGeometry for using it as the new Layout
        GridEnvelope2D gridRange2D = gg2D.getGridRange2D();
        layout.setMinX(gridRange2D.x);
        layout.setMinY(gridRange2D.y);
        layout.setWidth(gridRange2D.width);
        layout.setHeight(gridRange2D.height);
    }

    protected Map getProperties(
            RenderedImage data,
            CoordinateReferenceSystem crs,
            InternationalString name,
            MathTransform toCRS,
            GridCoverage2D[] sources,
            BandMergeParams parameters) {
        // Merge the coverage properties
        Map properties = new HashMap();

        for (GridCoverage2D cov : sources) {
            if (cov != null && cov.getProperties() != null) {
                properties.putAll(cov.getProperties());
            }
        }

        // Setting ROI and NoData if present
        if (JAIExt.isJAIExtOperation("BandMerge")) {
            ParameterBlockJAI pb = parameters.parameters;
            CoverageUtilities.setROIProperty(properties, (ROI) pb.getObjectParameter(3));
            CoverageUtilities.setNoDataProperty(properties, pb.getObjectParameter(1));
        }

        return properties;
    }

    protected GridSampleDimension[] deriveSampleDimension(
            GridSampleDimension[][] list, Parameters parameters) {
        // Total number of sample dimensions
        int numDim = 0;
        // Cycle on the input list in order to calculate the number of sample dimensions
        for (GridSampleDimension[] array : list) {
            numDim += array.length;
        }

        if (numDim == 0) {
            return null;
        }
        // Creation of a new GridSampleDimension array
        List<GridSampleDimension> sampleDims = new ArrayList<GridSampleDimension>(numDim);

        for (GridSampleDimension[] array : list) {
            for (GridSampleDimension sample : array) {
                sampleDims.add(sample);
            }
        }
        // Array containing the final Coverage SampleDimensions
        GridSampleDimension[] dims = new GridSampleDimension[numDim];

        return sampleDims.toArray(dims);
    }

    /**
     * This method prepares the {@link ParameterBlockJAI} to pass to JAI in order to execute the
     * {@link BandMerge} operation.
     */
    private ParameterBlockJAI prepareParameters(
            final ParameterValueGroup parameters,
            GridCoverage2D[] sources,
            List<AffineTransform> tr,
            AffineTransform2D crsToGRID)
            throws MismatchedDimensionException, ParameterNotFoundException, TransformException {
        final ImagingParameters copy = (ImagingParameters) descriptor.createValue();
        final ParameterBlockJAI block = (ParameterBlockJAI) copy.parameters;

        Range[] nodata = new Range[sources.length];
        // Image dataType
        int dataType = sources[0].getRenderedImage().getSampleModel().getDataType();

        // No Data check
        for (int i = 0; i < sources.length; i++) {
            GridCoverage2D cov = sources[i];
            // Setting of the Source
            block.setSource(cov.getRenderedImage(), i);
            // Check on the image datatype
            int dataTypeCov = cov.getRenderedImage().getSampleModel().getDataType();
            if (dataType != dataTypeCov) {
                throw new IllegalArgumentException("Input Coverages must have the same data type");
            }

            // Creation of the NoData range associated
            nodata[i] = createNoDataRange(cov, dataType);
        }

        if (JAIExt.isJAIExtOperation("BandMerge")) {
            // Setting NoData
            block.setParameter("noData", nodata);

            // Setting Transformations
            block.setParameter("transformations", tr);

            // Setting ROI
            ROI roi = null;
            if (parameters.parameter(GEOMETRY).getValue() != null) {
                // Creation of a ROI geometry object from the Geometry
                roi =
                        new ROIGeometry(
                                JTS.transform(
                                        (Geometry) parameters.parameter(GEOMETRY).getValue(),
                                        crsToGRID));
            }
            // Check if the coverages contains a ROI property
            for (int i = 0; i < sources.length; i++) {
                GridCoverage2D cov = sources[i];
                ROI covROI = CoverageUtilities.getROIProperty(cov);
                if (covROI != null) {
                    ROI newROI = null;
                    // Check if it must be transformed
                    if (tr != null) {
                        try {
                            AffineTransform trans = tr.get(i).createInverse();
                            newROI = covROI.transform(trans);
                        } catch (NoninvertibleTransformException e) {
                            LOGGER.log(Level.SEVERE, e.getMessage(), e);
                        }
                    } else {
                        newROI = covROI;
                    }

                    if (roi == null) {
                        roi = newROI;
                    } else {
                        roi = roi.intersect(newROI);
                    }
                }
            }

            // Addition of the ROI to the ParameterBlock
            if (roi != null) {
                block.setParameter("roi", roi);
            }

            // Setting the destination No Data Value as the NoData of the principal coverage
            // selected
            block.setParameter(
                    "destinationNoData", nodata[getIndex(parameters)].getMin().doubleValue());
        }

        return block;
    }

    /** Method for creating the nodata range associated to each coverage */
    private Range createNoDataRange(GridCoverage2D cov, int dataType) {
        // Extract NoData property from gridCoverage
        NoDataContainer container = CoverageUtilities.getNoDataProperty(cov);
        if (container != null) {
            return container.getAsRange();
        }
        // No property set, use the input NoData Range
        double[] nodatas = CoverageUtilities.getBackgroundValues(cov);
        if (nodatas != null && nodatas.length > 0) {
            Range noData =
                    RangeFactory.convert(RangeFactory.create(nodatas[0], nodatas[0]), dataType);
            return noData;
        }
        return null;
    }

    /**
     * Container class used for passing various parameters to the deriveGridCoverage method. The
     * structure is similar to that of the {@link Parameters} class.
     *
     * @author Nicola Lagomarsini, GeoSolutions S.A.S.
     */
    static class BandMergeParams {
        /**
         * The two dimensional coordinate reference system for all sources and the destination
         * {@link GridCoverage2D}.
         */
        public final CoordinateReferenceSystem crs;

        /** The "grid to coordinate reference system" transform chosen for all the Coverages. */
        public final AffineTransform2D gridToCRS;

        /** The parameters to be given to the {@link JAI#createNS} method. */
        public final ParameterBlockJAI parameters;

        /**
         * The rendering hints to be given to the {@link JAI#createNS} method. The {@link JAI}
         * instance to use for the {@code createNS} call will be fetch from the {@link
         * Hints#JAI_INSTANCE} key.
         */
        public final Hints hints;

        /** The Bounding box of the Final Coverage */
        public Envelope2D bbox;

        /** Constructs a new instance with the specified values. */
        BandMergeParams(
                final CoordinateReferenceSystem crs,
                final AffineTransform2D gridToCRS,
                final Envelope2D bbox,
                final ParameterBlockJAI parameters,
                final Hints hints) {
            this.crs = crs;
            this.gridToCRS = gridToCRS;
            this.bbox = bbox;
            this.parameters = parameters;
            this.hints = hints;
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
}
