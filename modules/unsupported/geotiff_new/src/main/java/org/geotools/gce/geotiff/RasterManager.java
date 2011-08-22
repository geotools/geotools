/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2005-2008, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.gce.geotiff;

import java.awt.Rectangle;
import java.awt.image.ColorModel;
import java.awt.image.SampleModel;
import java.io.IOException;
import java.net.URL;
import java.util.Collection;
import java.util.Collections;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.imageio.ImageTypeSpecifier;
import javax.media.jai.ImageLayout;

import org.geotools.coverage.grid.GridCoverage2D;
import org.geotools.coverage.grid.GridCoverageFactory;
import org.geotools.coverage.grid.GridEnvelope2D;
import org.geotools.coverage.grid.io.OverviewPolicy;
import org.geotools.data.DataSourceException;
import org.geotools.data.DataUtilities;
import org.geotools.factory.Hints;
import org.geotools.gce.geotiff.OverviewsController.OverviewLevel;
import org.geotools.gce.geotiff.RasterLayerResponse.RasterProducer;
import org.geotools.geometry.GeneralEnvelope;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.referencing.CRS;
import org.geotools.util.Utilities;
import org.opengis.coverage.grid.GridCoverage;
import org.opengis.coverage.grid.GridEnvelope;
import org.opengis.parameter.GeneralParameterValue;
import org.opengis.referencing.FactoryException;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.opengis.referencing.datum.PixelInCell;
import org.opengis.referencing.operation.MathTransform;
import org.opengis.referencing.operation.MathTransform2D;
import org.opengis.referencing.operation.TransformException;

class RasterManager {

    /**
     * This class is responsible for putting together all the 2D spatial
     * information needed for a certain raster.
     * 
     * <p>
     * Notice that when this structure will be extended to work in ND this will
     * become much more complex or as an alternative a sibling
     * TemporalDomainManager will be created.
     * 
     * @author Simone Giannecchini, GeoSolutions SAS
     * 
     */
    class SpatialDomainManager {

        public SpatialDomainManager() throws TransformException, FactoryException {
            setBaseParameters();
            prepareCoverageSpatialElements();
        }

        /** The base envelope 2D */
        ReferencedEnvelope coverageBBox;

        /** The CRS for the coverage */
        CoordinateReferenceSystem coverageCRS;

        /** The CRS related to the base envelope 2D */
        CoordinateReferenceSystem coverageCRS2D;

        // ////////////////////////////////////////////////////////////////////////
        //
        // Base coverage properties
        //
        // ////////////////////////////////////////////////////////////////////////
        /** The base envelope read from file */
        GeneralEnvelope coverageEnvelope = null;

        double[] coverageFullResolution;

        /** WGS84 envelope 2D for this coverage */
        ReferencedEnvelope coverageGeographicBBox;

        CoordinateReferenceSystem coverageGeographicCRS2D;

        MathTransform2D coverageGridToWorld2D;

        /** The base grid range for the coverage */
        Rectangle coverageRasterArea;

        /**
         * Initialize the 2D properties (CRS and Envelope) of this coverage
         * 
         * @throws TransformException
         * 
         * @throws FactoryException
         * @throws TransformException
         * @throws FactoryException
         */
        private void prepareCoverageSpatialElements() throws TransformException, FactoryException {
            //
            // basic initialization
            //
            coverageGeographicBBox = new ReferencedEnvelope(CRS.transform(CRS.findMathTransform(
                    coverageEnvelope.getCoordinateReferenceSystem(), Utils.WGS84,
                    true), coverageEnvelope));
            coverageGeographicCRS2D = coverageGeographicBBox!=null?coverageGeographicBBox.getCoordinateReferenceSystem():null;

            //
            // Get the original envelope 2d and its spatial reference system
            //
            coverageCRS2D = CRS.getHorizontalCRS(coverageCRS);
            assert coverageCRS2D.getCoordinateSystem().getDimension() == 2;
            if (coverageCRS.getCoordinateSystem().getDimension() != 2) {
                final MathTransform transform = CRS.findMathTransform(coverageCRS,
                        (CoordinateReferenceSystem) coverageCRS2D);
                final GeneralEnvelope bbox = CRS.transform(transform, coverageEnvelope);
                bbox.setCoordinateReferenceSystem(coverageCRS2D);
                coverageBBox = new ReferencedEnvelope(bbox);
            } else {
                // it is already a bbox
                coverageBBox = new ReferencedEnvelope(coverageEnvelope);
            }

        }

        /**
         * Set the main parameters of this coverage request, getting basic
         * information from the reader.
         */
        private void setBaseParameters() {
            this.coverageEnvelope = RasterManager.this.getCoverageEnvelope().clone();
            this.coverageRasterArea = ((GridEnvelope2D) RasterManager.this.getCoverageGridrange()).clone();
            this.coverageCRS = RasterManager.this.getCoverageCRS();
            this.coverageGridToWorld2D = (MathTransform2D) RasterManager.this.getRaster2Model();
            this.coverageFullResolution = new double[2];
            final OverviewLevel highestLevel = RasterManager.this.overviewsController.resolutionsLevels.get(0);
            coverageFullResolution[0] = highestLevel.resolutionX;
            coverageFullResolution[1] = highestLevel.resolutionY;
        }

    }

    /** The CRS of the input coverage */
    private CoordinateReferenceSystem coverageCRS;

    /** The base envelope related to the input coverage */
    private GeneralEnvelope coverageEnvelope;

    /** The coverage factory producing a {@link GridCoverage} from an image */
    GridCoverageFactory coverageFactory;

    /**
     * The name of the input coverage TODO consider URI
     */
    String coverageIdentifier;

    /** The hints to be used to produce this coverage */
    Hints hints;

     URL inputURL;

    // ////////////////////////////////////////////////////////////////////////
    //
    // Information obtained by the coverageRequest instance
    //
    // ////////////////////////////////////////////////////////////////////////
    /** The coverage grid to world transformation */
    MathTransform raster2Model;

    OverviewsController overviewsController;

    private GridEnvelope coverageGridrange;

    OverviewPolicy overviewPolicy;

    GeoTiffReader parent;

    SpatialDomainManager spatialDomainManager;

    ImageTypeSpecifier baseImageType;

    /** Default {@link ColorModel}.*/
    ColorModel defaultCM;

    /** Default {@link SampleModel}.*/
    SampleModel defaultSM;

    ImageLayout defaultImageLayout;

    RasterProducer rasterProducer;

    GranuleDescriptor granuleDescriptor;

    /** Logger. */
    private final static Logger LOGGER = org.geotools.util.logging.Logging.getLogger(RasterManager.class);

    public RasterManager(final GeoTiffReader reader) throws DataSourceException {

        Utilities.ensureNonNull("GeoTiffReader", reader);
        this.parent = reader;
        inputURL = reader.sourceURL;
        coverageIdentifier = reader.getName();
        hints = reader.getHints();
        coverageFactory = reader.getGridCoverageFactory();
        // get the overviews policy
        extractOverviewPolicy();

        // base image type
        baseImageType = reader.baseImageType;
        // load SM and CM
        defaultCM= baseImageType.getColorModel();
        defaultSM= baseImageType.getSampleModel();
        
        // default ImageLayout
        defaultImageLayout= new ImageLayout().setColorModel(defaultCM).setSampleModel(defaultSM);

        coverageEnvelope = reader.getOriginalEnvelope();
        coverageGridrange = reader.getOriginalGridRange();
        coverageCRS = reader.getCrs();
        raster2Model = reader.getOriginalGridToWorld(PixelInCell.CELL_CENTER);

        //instantiating controller for subsampling and overviews
        overviewsController=new OverviewsController(
                        reader.getHighestRes(),
                        reader.getNumberOfOverviews(),
                        reader.getOverviewsResolution());
        try {
            spatialDomainManager = new SpatialDomainManager();
        } catch (TransformException e) {
            throw new DataSourceException(e);
        } catch (FactoryException e) {
            throw new DataSourceException(e);
        }
        // granuleDescriptor creation
        granuleDescriptor = new GranuleDescriptor(this,DataUtilities.urlToFile(this.inputURL));
    }

    /**
     * This method is responsible for checking the overview policy as defined by
     * the provided {@link Hints}.
     * 
     * @return the overview policy which can be one of
     *         {@link Hints#VALUE_OVERVIEW_POLICY_IGNORE},
     *         {@link Hints#VALUE_OVERVIEW_POLICY_NEAREST},
     *         {@link Hints#VALUE_OVERVIEW_POLICY_SPEED},
     *         {@link Hints#VALUE_OVERVIEW_POLICY_QUALITY}. Default is
     *         {@link Hints#VALUE_OVERVIEW_POLICY_NEAREST}.
     */
    private OverviewPolicy extractOverviewPolicy() {

        // check if a policy was provided using hints (check even the
        // deprecated one)
        if (this.hints != null) {
            if (this.hints.containsKey(Hints.OVERVIEW_POLICY)) {
                overviewPolicy = (OverviewPolicy) this.hints.get(Hints.OVERVIEW_POLICY);
            }
        }

        // use default if not provided. Default is nearest
        if (overviewPolicy == null) {
			overviewPolicy = OverviewPolicy.getDefaultPolicy();
        }
        assert overviewPolicy != null;
        return overviewPolicy;
    }

    public Collection<GridCoverage2D> read(final GeneralParameterValue[] params) throws IOException {

        // create a request
        final RasterLayerRequest request = new RasterLayerRequest(params, this);
        if (request.isEmpty()) {
            if (LOGGER.isLoggable(Level.FINE))
                LOGGER.log(Level.FINE, "Request is empty: " + request.toString());
            return Collections.emptyList();
        }

        // create a response for the provided request
        final RasterLayerResponse response = new RasterLayerResponse(request, this);

        // execute the request
        final GridCoverage2D elem = response.createResponse();
        if (elem != null) {
            return Collections.singletonList(elem);
        }
        return Collections.emptyList();

    }

    public void dispose() {

    }
    public URL getInputURL() {
        return inputURL;
    }

    public String getCoverageIdentifier() {
        return coverageIdentifier;
    }

    public Hints getHints() {
        return hints;
    }

    public CoordinateReferenceSystem getCoverageCRS() {
        return coverageCRS;
    }

    public GeneralEnvelope getCoverageEnvelope() {
        return coverageEnvelope;
    }

    public GridCoverageFactory getCoverageFactory() {
        return coverageFactory;
    }

    public MathTransform getRaster2Model() {
        return raster2Model;
    }

    public GridEnvelope getCoverageGridrange() {
        return coverageGridrange;
    }

}
