/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2007-2008, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.gce.imagemosaic;

import java.awt.Rectangle;
import java.awt.image.ColorModel;
import java.awt.image.RenderedImage;
import java.awt.image.SampleModel;
import java.io.File;
import java.io.IOException;
import java.lang.ref.SoftReference;
import java.net.URL;
import java.util.Collection;
import java.util.Collections;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.media.jai.ImageLayout;

import org.geotools.coverage.grid.GridCoverage2D;
import org.geotools.coverage.grid.GridCoverageFactory;
import org.geotools.coverage.grid.GridEnvelope2D;
import org.geotools.coverage.grid.io.DecimationPolicy;
import org.geotools.coverage.grid.io.OverviewPolicy;
import org.geotools.data.DataSourceException;
import org.geotools.data.DataUtilities;
import org.geotools.data.Query;
import org.geotools.factory.Hints;
import org.geotools.gce.imagemosaic.OverviewsController.OverviewLevel;
import org.geotools.gce.imagemosaic.catalog.GranuleCatalog;
import org.geotools.gce.imagemosaic.catalog.GranuleCatalogVisitor;
import org.geotools.geometry.GeneralEnvelope;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.referencing.CRS;
import org.geotools.util.Utilities;
import org.opengis.coverage.grid.GridCoverage;
import org.opengis.geometry.BoundingBox;
import org.opengis.parameter.GeneralParameterValue;
import org.opengis.referencing.FactoryException;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.opengis.referencing.datum.PixelInCell;
import org.opengis.referencing.operation.MathTransform;
import org.opengis.referencing.operation.MathTransform2D;
import org.opengis.referencing.operation.TransformException;
class RasterManager {
    
	/** Logger. */
	private final static Logger LOGGER = org.geotools.util.logging.Logging.getLogger(RasterManager.class);
	
	/**
	 * This class is responsible for putting together all the 2D spatial information needed for a certain raster.
	 * 
	 * <p>
	 * Notice that when this structure will be extended to work in ND this will become much more complex or as an 
	 * alternative a sibling TemporalDomainManager will be created.
	 * 
	 * @author Simone Giannecchini, GeoSolutions SAS
	 *
	 */
	static class SpatialDomainManager{

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
		 

		public SpatialDomainManager(final GeneralEnvelope envelope,
				final GridEnvelope2D coverageGridrange,
				final CoordinateReferenceSystem crs,
				final MathTransform coverageGridToWorld2D,
				final OverviewsController overviewsController) throws TransformException, FactoryException {
		    this.coverageEnvelope = envelope.clone();
		    this.coverageRasterArea =coverageGridrange.clone();
		    this.coverageCRS = crs;
		    this.coverageGridToWorld2D = (MathTransform2D) coverageGridToWorld2D;
		    this.coverageFullResolution = new double[2];
		    final OverviewLevel highestLevel= overviewsController.resolutionsLevels.get(0);
		    coverageFullResolution[0] = highestLevel.resolutionX;
		    coverageFullResolution[1] = highestLevel.resolutionY;
		    
		    prepareCoverageSpatialElements();
		}
			
			
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
            coverageGeographicBBox = Utils.getWGS84ReferencedEnvelope(coverageEnvelope);
            coverageGeographicCRS2D = coverageGeographicBBox==null?coverageGeographicBBox.getCoordinateReferenceSystem():null;

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
	
	/** Default {@link ColorModel}.*/
	ColorModel defaultCM;
	
	/** Default {@link SampleModel}.*/
	SampleModel defaultSM;
	
	/** The coverage factory producing a {@link GridCoverage} from an image */
	private GridCoverageFactory coverageFactory;

	/** The name of the input coverage 
	 * TODO consider URI
	 */
	private String coverageIdentifier;

	
	/** The hints to be used to produce this coverage */
	private Hints hints;
	OverviewsController overviewsController;
	OverviewPolicy overviewPolicy;
	DecimationPolicy decimationPolicy;
	ImageMosaicReader parent;
	private PathType pathType;
	boolean expandMe;
	boolean heterogeneousGranules;
	SpatialDomainManager spatialDomainManager;

	/** {@link SoftReference} to the index holding the tiles' envelopes. */
	final GranuleCatalog granuleCatalog;

	String timeAttribute;
	
	String elevationAttribute;
	
	String runtimeAttribute;

	ImageLayout defaultImageLayout;

	public RasterManager(final ImageMosaicReader reader) throws DataSourceException {
		
		Utilities.ensureNonNull("ImageMosaicReader", reader);
		
		this.parent=reader;
		this.expandMe=parent.expandMe;
		this.heterogeneousGranules = parent.heterogeneousGranules;
        
        //take ownership of the index
        granuleCatalog = parent.catalog;
        parent.catalog = null;
		
        timeAttribute=parent.timeAttribute;
        elevationAttribute=parent.elevationAttribute;
        runtimeAttribute=parent.runtimeAttribute;
        coverageIdentifier=reader.getName();
        hints = reader.getHints();
        this.coverageIdentifier =reader.getName();
        this.coverageFactory = reader.getGridCoverageFactory();
        this.pathType=parent.pathType;
        
        //resolution values
        
        //instantiating controller for subsampling and overviews
        overviewsController=new OverviewsController(
        		reader.getHighestRes(),
        		reader.getNumberOfOvervies(),
        		reader.getOverviewsResolution());
        try {
			spatialDomainManager= new SpatialDomainManager(
					reader.getOriginalEnvelope(),
					(GridEnvelope2D)reader.getOriginalGridRange(),
					reader.getCrs(),
					reader.getOriginalGridToWorld(PixelInCell.CELL_CENTER),
					overviewsController);
		} catch (TransformException e) {
			throw new DataSourceException(e);
		} catch (FactoryException e) {
			throw new DataSourceException(e);
		}
        extractOverviewPolicy();
        extractDecimationPolicy();
        
        // load defaultSM and defaultCM by using the sample_image if it was provided
        loadSampleImage();        
		
	}

 	/**
	 * This code tries to load the sample image from which we can extract SM and CM to use when answering to requests
	 * that falls within a hole in the mosaic.
	 */
	private void loadSampleImage() {
	    if (this.parent.sourceURL == null) {
	        //TODO: I need to define the sampleImage somehow for the ImageMosaicDescriptor case
	        return;
	    }
		
			final URL baseURL=this.parent.sourceURL;
			final File baseFile= DataUtilities.urlToFile(baseURL);
			// in case we do not manage to convert the source URL we leave right awaycd sr
			if (baseFile==null){
				if(LOGGER.isLoggable(Level.FINE))
					LOGGER.fine("Unable to find sample image for path "+baseURL);
				return;
			}
			final File sampleImageFile= new File(baseFile.getParent() + "/sample_image");			
			final RenderedImage sampleImage = Utils.loadSampleImage(sampleImageFile);
			if(sampleImage!=null){
				
				// load SM and CM
				defaultCM= sampleImage.getColorModel();
				defaultSM= sampleImage.getSampleModel();
				
				// default ImageLayout
				defaultImageLayout= new ImageLayout().setColorModel(defaultCM).setSampleModel(defaultSM);
			}
			else
				if(LOGGER.isLoggable(Level.WARNING))
					LOGGER.warning("Unable to find sample image for path "+baseURL);
	}

	/**
	 * This method is responsible for checking the overview policy as defined by
	 * the provided {@link Hints}.
	 * 
	 * @return the overview policy which can be one of
	 *         {@link OverviewPolicy#IGNORE},
	 *         {@link OverviewPolicy#NEAREST},
	 *         {@link OverviewPolicy#SPEED}, {@link OverviewPolicy#QUALITY}.
	 *         Default is {@link OverviewPolicy#NEAREST}.
	 */
	private OverviewPolicy extractOverviewPolicy() {
		
		// check if a policy was provided using hints (check even the
		// deprecated one)
		if (this.hints != null)
			if (this.hints.containsKey(Hints.OVERVIEW_POLICY))
				overviewPolicy = (OverviewPolicy) this.hints.get(Hints.OVERVIEW_POLICY);
	
		// use default if not provided. Default is nearest
		if (overviewPolicy == null) {
			overviewPolicy = OverviewPolicy.getDefaultPolicy();
		}
		assert overviewPolicy != null;
		return overviewPolicy;
	}
	
	/**
         * This method is responsible for checking the decimation policy as defined by
         * the provided {@link Hints}.
         * 
         * @return the decimation policy which can be one of
         *         {@link DecimationPolicy#ALLOW},
         *         {@link DecimationPolicy#DISALLOW}.
         *         Default is {@link DecimationPolicy#ALLOW}.
         */
	private DecimationPolicy extractDecimationPolicy() {
            if (this.hints != null)
                if (this.hints.containsKey(Hints.DECIMATION_POLICY))
                    decimationPolicy = (DecimationPolicy) this.hints.get(Hints.DECIMATION_POLICY);
    
            // use default if not provided. Default is allow
            if (decimationPolicy == null) {
                decimationPolicy = DecimationPolicy.getDefaultPolicy();
            }
            assert decimationPolicy != null;
            return decimationPolicy;

        }

	public Collection<GridCoverage2D> read(final GeneralParameterValue[] params) throws IOException {

		// create a request
		final RasterLayerRequest request= new RasterLayerRequest(params,this);
		if (request.isEmpty()){
			if(LOGGER.isLoggable(Level.FINE))
				LOGGER.log(Level.FINE,"Request is empty: "+ request.toString());
			return Collections.emptyList();		
		}
		
		// create a response for the provided request
		final RasterLayerResponse response= new RasterLayerResponse(request,this);
		
		// execute the request
		final GridCoverage2D elem = response.createResponse();
		if (elem != null){
			return Collections.singletonList(elem);
		}
		return Collections.emptyList();
		
		
	}
	
	public void dispose() {
	    try{
	        if(granuleCatalog!=null)
	            this.granuleCatalog.dispose();
	    } catch (Exception e) {
                if(LOGGER.isLoggable(Level.FINE))
                    LOGGER.log(Level.FINE,e.getLocalizedMessage(),e);
            } 
	}

	/**
	 * Retrieves the list of features that intersect the provided envelope
	 * loading them inside an index in memory where needed.
	 * 
	 * @param envelope
	 *            Envelope for selecting features that intersect.
	 * @return A list of features.
	 * @throws IOException
	 *             In case loading the needed features failes.
	 */
	Collection<GranuleDescriptor> getGranules(final BoundingBox envelope)throws IOException {
		final Collection<GranuleDescriptor> granules = granuleCatalog.getGranules(envelope);
		if (granules != null)
			return granules;
		else
			return Collections.emptyList();
	}
	
	Collection<GranuleDescriptor> getGranules(final Query q)throws IOException {
		final Collection<GranuleDescriptor> granules = granuleCatalog.getGranules(q);
		if (granules != null)
			return granules;
		else
			return Collections.emptyList();
	}
	
	void getGranules(final Query q,final GranuleCatalogVisitor visitor)throws IOException {
		granuleCatalog.getGranules(q,visitor);

	}

	/**
	 * Retrieves the list of features that intersect the provided envelope
	 * loading them inside an index in memory where needed.
	 * 
	 * @param envelope
	 *            Envelope for selecting features that intersect.
	 * @return A list of features.
	 * @throws IOException
	 *             In case loading the needed features fails.
	 */
	void getGranules(final BoundingBox envelope,final GranuleCatalogVisitor visitor)throws IOException {
		granuleCatalog.getGranules(envelope,visitor);
	}

	public PathType getPathType() {
		return pathType;
	}


	public String getCoverageIdentifier() {
		return coverageIdentifier;
	}

	
	public Hints getHints() {
		return hints;
	}

	public GridCoverageFactory getCoverageFactory() {
		return coverageFactory;
	}

}
