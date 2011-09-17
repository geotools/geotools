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
package org.geotools.gce;

import java.awt.Rectangle;
import java.io.IOException;
import java.util.Collection;
import java.util.Collections;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.imageio.ImageTypeSpecifier;
import javax.media.jai.ImageLayout;

import org.geotools.coverage.grid.GridCoverage2D;
import org.geotools.coverage.grid.GridCoverageFactory;
import org.geotools.coverage.grid.io.OverviewPolicy;
import org.geotools.data.DataSourceException;
import org.geotools.factory.Hints;
import org.geotools.gce.geotiff.GeoTiffReader;
import org.geotools.gce.geotiff.GeoTiffUtils;
import org.geotools.geometry.GeneralEnvelope;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.referencing.CRS;
import org.geotools.util.Utilities;
import org.opengis.parameter.GeneralParameterValue;
import org.opengis.referencing.FactoryException;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.opengis.referencing.datum.PixelInCell;
import org.opengis.referencing.operation.MathTransform;
import org.opengis.referencing.operation.MathTransform2D;
import org.opengis.referencing.operation.TransformException;

public class RasterManager {

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

        public SpatialDomainManager(
                CoordinateReferenceSystem coverageCRS,
                GeneralEnvelope coverageEnvelope, 
                MathTransform2D coverageGridToWorld2D, 
                Rectangle coverageRasterArea) throws TransformException, FactoryException {
            this.coverageCRS = coverageCRS;
            this.coverageEnvelope = coverageEnvelope;
            this.coverageGridToWorld2D = coverageGridToWorld2D;
            this.coverageRasterArea = coverageRasterArea;
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
        GeneralEnvelope coverageEnvelope;


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
                    coverageEnvelope.getCoordinateReferenceSystem(), GeoTiffUtils.WGS84,
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
    }

    /**
     * The name of the input coverage TODO consider URI
     */
    String coverageIdentifier;

    /** The hints to be used to produce this coverage */
    Hints hints;

    OverviewsController overviewsController;

    OverviewPolicy overviewPolicy;

    GeoTiffReader parent;

    SpatialDomainManager spatialDomainManager;

    ImageTypeSpecifier baseImageType;

    ImageLayout defaultImageLayout;

    RasterDescriptor rasterDescriptor;

    /** Logger. */
    private final static Logger LOGGER = org.geotools.util.logging.Logging.getLogger(RasterManager.class);
    public RasterManager(
            String identifier,
            Hints hints,
            CoordinateReferenceSystem crs,
            GeneralEnvelope envelope,
            MathTransform2D gridToWorld,
            Rectangle originalRasterRange,
            double [][] resolutions,
            ImageTypeSpecifier baseImageType,
            GeoTiffReader reader) throws DataSourceException {
        Utilities.ensureNonNull("GeoTiffReader", reader);        
        initialize(
                identifier,
                hints,
                crs,
                envelope,
                gridToWorld,
                originalRasterRange,
                resolutions,
                baseImageType,
                reader);

        
    }
    
    private void initialize(
            String identifier, 
            Hints hints, 
            CoordinateReferenceSystem crs,
            GeneralEnvelope envelope, 
            MathTransform2D gridToWorld, 
            Rectangle originalRasterRange,
            double[][] resolutions, 
            ImageTypeSpecifier baseImageType, 
            GeoTiffReader reader) throws DataSourceException {
        Utilities.ensureNonNull("GeoTiffReader", reader);
        this.parent = reader;
        this.coverageIdentifier = identifier;
        hints = reader.getHints();
        // get the default levels policy from the hints
        extractOverviewPolicy();

        // base image type
        this.baseImageType = baseImageType;
        
        // default ImageLayout
        defaultImageLayout= new ImageLayout().setColorModel( baseImageType.getColorModel()).setSampleModel(baseImageType.getSampleModel());


        //instantiating controller for subsampling and levels
        overviewsController=new OverviewsController(resolutions);
        try {
            spatialDomainManager = new SpatialDomainManager(
                    crs,
                    envelope,
                    gridToWorld,
                    originalRasterRange);
        } catch (TransformException e) {
            throw new DataSourceException(e);
        } catch (FactoryException e) {
            throw new DataSourceException(e);
        }
        // rasterDescriptor creation
        rasterDescriptor = new RasterDescriptor(this);
        
    }

    public RasterManager(final GeoTiffReader reader) throws DataSourceException {
        Utilities.ensureNonNull("GeoTiffReader", reader);
        
        // extract levels
        double [][]resolutions = new double[reader.getNumberOfOverviews()+1][2];
        double[][] overviewResolutions=reader.getOverviewsResolution();
        resolutions[0]=reader.getHighestRes();
        for(int i=1; i<resolutions.length;i++)
            resolutions[i]=overviewResolutions[i-1];
        initialize(
                reader.getName(),
                reader.getHints(),
                reader.getCrs(),
                reader.getOriginalEnvelope(),
                (MathTransform2D)reader.getOriginalGridToWorld(PixelInCell.CELL_CENTER), 
                (Rectangle) reader.getOriginalGridRange(), 
                resolutions,
                reader.baseImageType, 
                reader);
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

    public String getCoverageIdentifier() {
        return coverageIdentifier;
    }

    public Hints getHints() {
        return hints;
    }

    public CoordinateReferenceSystem getCoverageCRS() {
        return spatialDomainManager.coverageCRS;
    }

    public GeneralEnvelope getCoverageEnvelope() {
        return spatialDomainManager.coverageEnvelope;
    }

    public MathTransform2D getRaster2Model() {
        return spatialDomainManager.coverageGridToWorld2D;
    }

    public Rectangle getCoverageGridrange() {
        return spatialDomainManager.coverageRasterArea;
    }

    public GridCoverageFactory getGridCoverageFactory() {
        return null;
    }

}
