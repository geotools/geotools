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
package org.geotools.imageio.hdf4;

import it.geosolutions.imageio.ndplugin.BaseImageMetadata;
import it.geosolutions.imageio.plugins.hdf4.HDF4Utilities;
import it.geosolutions.imageio.plugins.hdf4.aps.HDF4APSImageMetadata;
import it.geosolutions.imageio.plugins.hdf4.aps.HDF4APSStreamMetadata;
import it.geosolutions.imageio.plugins.hdf4.terascan.HDF4TeraScanImageMetadata;
import it.geosolutions.imageio.plugins.hdf4.terascan.HDF4TeraScanProperties;
import it.geosolutions.imageio.plugins.hdf4.terascan.HDF4TeraScanStreamMetadata;

import java.awt.Rectangle;
import java.awt.geom.AffineTransform;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.imageio.metadata.IIOMetadata;

import org.geotools.coverage.grid.GridEnvelope2D;
import org.geotools.coverage.grid.io.AbstractGridFormat;
import org.geotools.coverage.io.hdf4.CRSUtilities;
import org.geotools.coverage.io.util.Utilities;
import org.geotools.geometry.GeneralEnvelope;
import org.geotools.imageio.SpatioTemporalImageReader;
import org.geotools.imageio.hdf4.HDF4SpatioTemporalImageReaderSpi.HDF4_TYPE;
import org.geotools.imageio.metadata.Band;
import org.geotools.imageio.metadata.BoundedBy;
import org.geotools.imageio.metadata.CoordinateReferenceSystem;
import org.geotools.imageio.metadata.Identification;
import org.geotools.imageio.metadata.RectifiedGrid;
import org.geotools.imageio.metadata.SpatioTemporalMetadata;
import org.geotools.imageio.metadata.SpatioTemporalMetadataFormat;
import org.geotools.imageio.metadata.TemporalCRS;
import org.geotools.imageio.metadata.AbstractCoordinateReferenceSystem.Datum;
import org.geotools.metadata.iso.spatial.PixelTranslation;
import org.geotools.referencing.CRS;
import org.geotools.referencing.crs.DefaultGeographicCRS;
import org.geotools.referencing.operation.builder.GridToEnvelopeMapper;
import org.geotools.temporal.object.DefaultInstant;
import org.geotools.temporal.object.DefaultPeriod;
import org.geotools.temporal.object.DefaultPosition;
import org.opengis.referencing.FactoryException;
import org.opengis.referencing.NoSuchAuthorityCodeException;
import org.opengis.referencing.crs.GeographicCRS;
import org.opengis.referencing.datum.PixelInCell;
import org.opengis.temporal.Instant;
import org.opengis.temporal.TemporalObject;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;

/**
 * Class involved in SpatioTemporal Metadata settings.
 * 
 * @author Alessio Fabiani, GeoSolutions
 * @author Daniele Romagnoli, GeoSolutions
 *
 * @source $URL: http://svn.osgeo.org/geotools/branches/2.7.x/build/maven/javadoc/../../../modules/unsupported/coverage-experiment/hdf4/src/main/java/org/geotools/imageio/hdf4/HDF4SpatioTemporalMetadata.java $
 */
public class HDF4SpatioTemporalMetadata extends SpatioTemporalMetadata {
	
    protected final static Logger LOGGER = Logger.getLogger(HDF4SpatioTemporalMetadata.class.toString());

    public HDF4SpatioTemporalMetadata(HDF4SpatioTemporalImageReader reader,int imageIndex) {
        super(reader, imageIndex);
        if (hdf4_type == HDF4_TYPE.UNDEFINED)
        	hdf4_type = reader.getHDF4Type();
        
    }

    private org.opengis.referencing.crs.CoordinateReferenceSystem nativeCrs;
    
    private static org.opengis.referencing.crs.GeographicCRS WGS84_CRS;
    
    private GeneralEnvelope envelope;
    
    private Rectangle gridRange;
    
    private AffineTransform transform;
    
    private HDF4_TYPE hdf4_type = HDF4_TYPE.UNDEFINED; 
    
    private Map<String, String> attributesMap = null;
    
    private Instant startInstant;
    
    private Instant endInstant;
    
    static {
        try {
            WGS84_CRS = (GeographicCRS) CRS.decode("EPSG:4326",true);
        } catch (NoSuchAuthorityCodeException e) {
            if (LOGGER.isLoggable(Level.WARNING))
                LOGGER.warning("Unable to setup WGS84 CRS" + e.getLocalizedMessage());
        } catch (FactoryException e) {
            if (LOGGER.isLoggable(Level.WARNING))
                LOGGER.warning("Unable to setup WGS84 CRS" + e.getLocalizedMessage());
        }
    }
    
    /**
     * Get the attributes Map from the underlying ImageMetadata.
     * 
     * @param reader
     */
    private Map<String, String> getAttributesMap(SpatioTemporalImageReader reader) {
        if (attributesMap == null) {
            buildAttributesMap(reader);
        }
        return attributesMap;

    }

    /**
     * Initialize a Map of attributes available from the underlying
     * metadata.
     * 
     * @param reader
     */
    private void buildAttributesMap(SpatioTemporalImageReader reader) {
    	init(reader);
        attributesMap = new HashMap<String, String>();
        IIOMetadata metadata; 
        IIOMetadata streamMetadata;
        final int imageIndex = getImageIndex();
        try {
            metadata = reader.getImageMetadata(imageIndex);
            streamMetadata = reader.getStreamMetadata();
            switch (hdf4_type){
            case TeraScan:
            	setAttributesFromTerascanMetadata(metadata,streamMetadata);
                break;
            case APS:
            	setAttributesFromAPSMetadata(metadata, streamMetadata);
                break;
            }
        } catch (IOException e) {
            throw new IllegalArgumentException("Unable parsing metadata");
        }
    }

    /**
     * Build an attributes map from APS specific metadata, needed to setup spatiotemporalmetadata
     * @param metadata
     * @param streamMetadata
     */
    private void setAttributesFromAPSMetadata(IIOMetadata metadata, IIOMetadata streamMetadata) {
    	if (metadata instanceof HDF4APSImageMetadata) {
            Node root = metadata.getAsTree(HDF4APSImageMetadata.nativeMetadataFormatName);
            if (root != null) {
                Node mainNode = root.getFirstChild();
                if (mainNode != null) {
                    final NamedNodeMap attributes = mainNode.getAttributes();
                    if (attributes != null) {
                        final int numAttributes = attributes.getLength();
                        for (int i = 0; i < numAttributes; i++) {
                            final Node node = attributes.item(i);
                            if (node != null) {
                                attributesMap.put(node.getNodeName(), node.getNodeValue());
                            }
                        }
                    }
                }
            }
        }
        if (streamMetadata instanceof HDF4APSStreamMetadata) {
        	Node root = streamMetadata.getAsTree(HDF4APSStreamMetadata.nativeMetadataFormatName);
        	if (root != null) {
	        	NamedNodeMap attributes = null;
	        	Node attributesNode = root.getFirstChild();
	        	if (attributesNode != null){
	                Node mainNode = attributesNode.getFirstChild();
	                if (mainNode != null){
	                	if(mainNode.getNodeName().equalsIgnoreCase(HDF4APSStreamMetadata.STD_NODE)){
		                	
		                	// //
		                	// Time Attributes
		                	// //
		                	Node timeNode = mainNode.getFirstChild().getNextSibling();
		                    attributes = timeNode.getAttributes();
		                    if (attributes != null) {
		                        final int numAttributes = attributes.getLength();
		                        for (int i = 0; i < numAttributes; i++) {
		                            final Node node = attributes.item(i);
		                            if (node != null) {
		                                attributesMap.put(node.getNodeName(), node.getNodeValue());
		                            }
		                        }
		                    }
		                }
	                	attributes = null;
		                mainNode = mainNode.getNextSibling();
		                if (mainNode != null && mainNode.getNodeName().equalsIgnoreCase(HDF4APSStreamMetadata.PFA_NODE)){
		                	
		                	// //
		                	// Navigation Attributes
		                	// //
		                	Node navigationNode = mainNode.getFirstChild().getNextSibling();
		                	if (navigationNode != null){
			                    attributes = navigationNode.getAttributes();
			                    if (attributes != null) {
			                        final int numAttributes = attributes.getLength();
			                        for (int i = 0; i < numAttributes; i++) {
			                            final Node node = attributes.item(i);
			                            if (node != null) {
			                                attributesMap.put(node.getNodeName(), node.getNodeValue());
			                            }
			                        }
			                    }
			                }
		                }
	        		}
	                
	                Node projectionNode = attributesNode.getNextSibling().getFirstChild();
	                if (projectionNode != null && projectionNode.getNodeName().equalsIgnoreCase(HDF4APSStreamMetadata.PROJECTION_NODE)){
	                	
	                	// //
	                	// Projection Attributes
	                	// //
	                	attributes = projectionNode.getAttributes();
	                    if (attributes != null) {
	                        final int numAttributes = attributes.getLength();
	                        for (int i = 0; i < numAttributes; i++) {
	                            final Node node = attributes.item(i);
	                            if (node != null) {
	                                attributesMap.put(node.getNodeName(), node.getNodeValue());
	                            }
	                        }
	                    }
	                }
	            }
	        }
        }
	}

    /**
     * Build an attributes map from Terascan specific metadata, needed to setup spatiotemporalmetadata
     * @param metadata
     * @param streamMetadata
     */
	private void setAttributesFromTerascanMetadata(IIOMetadata metadata, IIOMetadata streamMetadata) {
    	if (metadata instanceof HDF4TeraScanImageMetadata) {
            Node root = metadata.getAsTree(HDF4TeraScanImageMetadata.nativeMetadataFormatName);
            if (root != null) {
                Node mainNode = root.getFirstChild();
                if (mainNode != null) {
                    final NamedNodeMap attributes = mainNode.getAttributes();
                    if (attributes != null) {
                        final int numAttributes = attributes.getLength();
                        for (int i = 0; i < numAttributes; i++) {
                            final Node node = attributes.item(i);
                            if (node != null) {
                                attributesMap.put(node.getNodeName(), node.getNodeValue());
                            }
                        }
                    }
                }
            }
        }
        if (streamMetadata instanceof HDF4TeraScanStreamMetadata) {
        	Node root = streamMetadata.getAsTree(HDF4TeraScanStreamMetadata.nativeMetadataFormatName);
            if (root != null) {
                Node mainNode = root.getFirstChild();
                if (mainNode != null) {
                    NamedNodeMap attributes = mainNode.getAttributes();
                    if (attributes != null) {
                        final int numAttributes = attributes.getLength();
                        for (int i = 0; i < numAttributes; i++) {
                            final Node node = attributes.item(i);
                            if (node != null) {
                                attributesMap.put(node.getNodeName(), node.getNodeValue());
                            }
                        }
                    }
                    
                    mainNode = mainNode.getNextSibling();
                    if (mainNode != null) {
                        attributes = mainNode.getAttributes();
                        if (attributes != null) {
                            final int numAttributes = attributes.getLength();
                            for (int i = 0; i < numAttributes; i++) {
                                final Node node = attributes.item(i);
                                if (node != null) {
                                    attributesMap.put(node.getNodeName(), node.getNodeValue());
                                }
                            }
                        }
                    }
                }
            }
        }
		
	}

	protected void setCoordinateReferenceSystemElement(SpatioTemporalImageReader reader) {
        getAttributesMap(reader);
        init(reader);
        CoordinateReferenceSystem crs = getCRS(SpatioTemporalMetadataFormat.PROJECTED);
        crs.setBaseCRS(new Identification("WGS 84", null, null,"EPSG:4326"));
        
        switch (hdf4_type) {
        case TeraScan:
           setTerascanCRS(crs);
           break;
        case APS:
        	setAPSCRS(crs);
            break;
        }

        // //
        //
        // Setting Temporal CRS
        //
        // //
        setHasTemporalCRS(true);
        final TemporalCRS tCRS = getTemporalCRS();
        tCRS.setDatum(new Identification("ISO8601", null, null, null));
        
        String startTime = null;
    	String endTime = null;
    	Date startDate = null;
		Date endDate = null;
		switch (hdf4_type){
    	case APS:
    		startTime = attributesMap.get("timeStart");
    		endTime = attributesMap.get("timeEnd");
    		startDate = HDF4Utilities.getDateTime(startTime, HDF4Utilities.APS_DATETIME_FORMAT);
    		endDate = HDF4Utilities.getDateTime(endTime, HDF4Utilities.APS_DATETIME_FORMAT);
    		break;
    	case TeraScan:
    		final String startDates = attributesMap.get("data_start_date");
    		final String startTimes = attributesMap.get("data_start_time");
    		final String endDates = attributesMap.get("data_end_date");
    		final String endTimes = attributesMap.get("data_end_time");
    		startTime = new StringBuilder(startDates).append(" ").append(startTimes).toString();
    		endTime = new StringBuilder(endDates).append(" ").append(endTimes).toString();
    		startDate = HDF4Utilities.getDateTime(startTime, HDF4Utilities.TERASCAN_DATETIME_FORMAT);
    		endDate = HDF4Utilities.getDateTime(endTime, HDF4Utilities.TERASCAN_DATETIME_FORMAT);
    		break;
    	}
		if (startDate != null)
			startInstant = new DefaultInstant(new DefaultPosition(startDate));
		if (endDate != null)	
			endInstant = new DefaultInstant(new DefaultPosition(endDate));

		final String timeOrigin = startInstant.getPosition().getDateTime().toString();
		tCRS.addAxis(new Identification("TIME"), "future", "hours since "+ timeOrigin, null);
        tCRS.addOrigin(timeOrigin);
    }

	private void setAPSCRS(CoordinateReferenceSystem crs) {
		
		// //
		//
		// Actually, all the available APS datasets have been produced 
		// using a Mercator_1SP projection.
		// Extend this section when more projections wiil be used   
		//
		// //
		final String projectionCode = attributesMap.get(HDF4APSStreamMetadata.PROJECTION);
		if (Double.parseDouble(projectionCode)!=5.0)
			throw new IllegalArgumentException("Actually, only Mercator 1SP projection is supported");
		
		String projectionNameS = "Mercator_1SP";
	    // /////////////////////////////////////////////////////////////
	    //
	    // Mercator 1SP
	    //
	    // /////////////////////////////////////////////////////////////
	    crs.setDefinedByConversion(new Identification(projectionNameS), null, null, null);
		crs.setIdentification(new Identification(projectionNameS));
        
        final String datum = attributesMap.get(HDF4APSStreamMetadata.DATUM);
        final String centralMeridianS = attributesMap.get(HDF4APSStreamMetadata.LONGITUDE_OF_CENTRAL_MERIDIAN);
        final String latitudeOfTrueScaleS = attributesMap.get(HDF4APSStreamMetadata.LATITUDE_OF_TRUE_SCALE);
        final String falseEastingS = attributesMap.get(HDF4APSStreamMetadata.FALSE_EASTINGS);
        final String falseNorthingS = attributesMap.get(HDF4APSStreamMetadata.FALSE_NORTHINGS);
        if (Utilities.ensureValidString(latitudeOfTrueScaleS, centralMeridianS, falseEastingS, falseNorthingS)) {
        	final double centralMeridian = Double.parseDouble(centralMeridianS)/1000000d;
			final double latitudeOfTrueScale = Double.parseDouble(latitudeOfTrueScaleS)/1000000d;
			final double falseNorthing = Double.parseDouble(falseNorthingS);
			final double falseEasting = Double.parseDouble(falseEastingS);
        
	        crs.addParameterValue(new Identification("central_meridian"),Double.toString(centralMeridian));
	        crs.addParameterValue(new Identification("latitude_of_origin"), Double.toString(latitudeOfTrueScale));
	        crs.addParameterValue(new Identification("false_northing"), falseNorthingS);
	        crs.addParameterValue(new Identification("false_easting"), falseEastingS);
	        crs.addParameterValue(new Identification("scale_factor"), "1");
	        
	        if (Double.parseDouble(datum)==12.0){
	            crs.setDatum(Datum.GEODETIC_DATUM, new Identification("WGS_1984","World Geodetic System 1984", null, "EPSG:6326"));
	            crs.addPrimeMeridian("0.0", new Identification("Greenwich", null, null,"EPSG:8901"));
	            crs.addEllipsoid("6378137.0", null, "298.257223563", "meter",new Identification("WGS 84", null, null, "EPSG:7030"));
	        }
			nativeCrs = CRSUtilities.getMercator1SPProjectedCRS(centralMeridian,latitudeOfTrueScale,
					falseEasting,falseNorthing,1,WGS84_CRS,null);
	        
	        crs.addAxis(new Identification("Easting"), "East", "metre", null);
	        crs.addAxis(new Identification("Northing"), "North", "metre", null);
        }
	}

	private void setTerascanCRS(CoordinateReferenceSystem crs) {
		// /////////////////////////////////////////////////////////////
        //
        // Mercator 2SP
        //
        // /////////////////////////////////////////////////////////////
       final String centralMeridianS = attributesMap.get(HDF4TeraScanProperties.ProjAttribs.PROJECT_ORIGIN_LONGITUDE);
       final String natOriginLatS = attributesMap.get(HDF4TeraScanProperties.ProjAttribs.PROJECT_ORIGIN_LATITUDE);
       final String standardParallelS = attributesMap.get(HDF4TeraScanProperties.ProjAttribs.STANDARD_PARALLEL_1);
       final String equatorialRadiusS = attributesMap.get(HDF4TeraScanProperties.ProjAttribs.EQUATORIAL_RADIUS);
       final String flatteningS = attributesMap.get(HDF4TeraScanProperties.ProjAttribs.EARTH_FLATTENING);
       
       if (Utilities.ensureValidString(standardParallelS, centralMeridianS, equatorialRadiusS, natOriginLatS, flatteningS)) {
			final double standardParallel = Double.parseDouble(standardParallelS);
			final double centralMeridian = Double.parseDouble(centralMeridianS);
			final double equatorialRadius = 1000 * Double.parseDouble(equatorialRadiusS);
			final double natOriginLat = Double.parseDouble(natOriginLatS);
			final double inverseFlattening = 1 / Double.parseDouble(flatteningS);

			GeographicCRS sourceCRS = CRSUtilities.getBaseCRS(equatorialRadius, inverseFlattening,true);
			nativeCrs = CRSUtilities.getMercator2SPProjectedCRS(standardParallel, centralMeridian, natOriginLat,
							sourceCRS, null);
//		   if (nativeCrs!=null)
//			   projectionNameS = nativeCrs.getName().toString();
//		   else 
			String projectionNameS = "Mercator_2SP";
		    crs.setDefinedByConversion(new Identification(projectionNameS), null, null, null);
		    crs.setIdentification(new Identification(projectionNameS));
			
           crs.addParameterValue(new Identification("standard_parallel_1"), standardParallelS);
           crs.addParameterValue(new Identification("central_meridian"), centralMeridianS);
           crs.addParameterValue(new Identification("latitude_of_origin"), natOriginLatS);
           crs.addParameterValue(new Identification("false_northing"), "0.0");
           crs.addParameterValue(new Identification("false_easting"), "0.0");
           
           // //
           // Datum and Ellipsoid
           // //
           crs.setDatum(Datum.GEODETIC_DATUM, new Identification("WGS_1984","World Geodetic System 1984", null, "EPSG:6326"));
           crs.addPrimeMeridian("0.0", new Identification("Greenwich", null, null,"EPSG:8901"));
           crs.addEllipsoid(Double.toString(Double.parseDouble(equatorialRadiusS)*1000), null, Double.toString(inverseFlattening), "meter",new Identification("WGS 84", null, null, "EPSG:7030"));

           crs.addAxis(new Identification("Easting"), "East", "metre", null);
           crs.addAxis(new Identification("Northing"), "North", "metre", null);
       }
		
	}

	@Override
    protected void setRectifiedGridElement(SpatioTemporalImageReader reader) {
    	init(reader);
        final RectifiedGrid rg = getRectifiedGrid();
        getAttributesMap(reader);
        final int imageIndex = getImageIndex();
        int width;
        int height;
        String startX,startY,deltaX,deltaY,axisX,axisY;
        startX=startY=deltaX=deltaY=axisX=axisY = null;
        AffineTransform tempTransform = null;
		try {
			width = reader.getWidth(imageIndex);
			height = reader.getHeight(imageIndex);
            rg.setLow(new int[] { 0, 0 });
            rg.setHigh(new int[] { width, height });
            switch (hdf4_type){
            case APS:
            	if (transform == null){
            		 final GridToEnvelopeMapper geMapper = new GridToEnvelopeMapper();
                     geMapper.setEnvelope(envelope);
                     geMapper.setGridRange(new GridEnvelope2D(gridRange));
                     geMapper.setPixelAnchor(PixelInCell.CELL_CORNER);
                     transform = (AffineTransform) geMapper.createTransform();
            	}
            	tempTransform = (AffineTransform) transform.clone();
            	break;
            case TeraScan:
            	if (transform == null){
            		final String projToImageTransformation = attributesMap.get(HDF4TeraScanProperties.ProjAttribs.PROJECT_TO_IMAGE_AFFINE);
            		transform = CRSUtilities.createAffineTransform(projToImageTransformation);
            	}
            	if (transform != null){
            		tempTransform = (AffineTransform) transform.clone();
        			final double tr = -PixelTranslation.getPixelTranslation(PixelInCell.CELL_CORNER);
        			tempTransform.translate(tr, tr);
        			
            	}
            	break;
            }
            startX = Double.toString(tempTransform.getTranslateX());
			startY = Double.toString(tempTransform.getTranslateY());
			deltaX = Double.toString(tempTransform.getScaleX());
			deltaY = Double.toString(tempTransform.getScaleY());
			axisX = "Easting";
			axisY = "Northing";
            
			rg.setCoordinates(new double[] {Double.parseDouble(startX), Double.parseDouble(startY)});
            rg.addOffsetVector(new double[] {Double.parseDouble(deltaX), 0d});
            rg.addOffsetVector(new double[] {0d, Double.parseDouble(deltaY)});

            rg.addAxisName(axisX);
            rg.addAxisName(axisY);
		} catch (IOException e) {

		}

    }

    @Override
    protected void setBandsElement(SpatioTemporalImageReader reader) {
        init(reader);
        final HDF4SpatioTemporalImageReader hdf4Reader = ((HDF4SpatioTemporalImageReader) reader);
        final Band band = addBand();
        final int imageIndex = getImageIndex();
		String imageMetadataFormat=null;
		String unitAttributeName=null;
		
		// //
		//
		// Setting format specific fields and metadata format names
		//
		// //
		switch (hdf4_type){
			case TeraScan:
				imageMetadataFormat=HDF4TeraScanImageMetadata.nativeMetadataFormatName;
	    		unitAttributeName=HDF4TeraScanProperties.DatasetAttribs.UNITS;
	    		break;
			case APS:
				imageMetadataFormat=HDF4APSImageMetadata.nativeMetadataFormatName;
	    		unitAttributeName="productUnits";
	    		break;
		}
		
    	try {	
    		// //
    		//
    		// Setting band element metadata
    		//
    		// //
    		final IIOMetadata metadata = hdf4Reader.getImageMetadata(imageIndex,imageMetadataFormat);
            if (metadata instanceof BaseImageMetadata) {
                final BaseImageMetadata commonMetadata = (BaseImageMetadata) metadata;
                setBandFromCommonMetadata(band, commonMetadata);
                Node node = commonMetadata.getAsTree(imageMetadataFormat);
                node = node.getFirstChild();
                if (node != null) {
                	
                	//Handling units
                    final NamedNodeMap attributesMap = node.getAttributes();
                    if (attributesMap != null) {
                    	final Node units = attributesMap.getNamedItem(unitAttributeName);
                        if (units != null) {
                            String unit = units.getNodeValue();
                            if (unit != null) {
                                band.setUoM(unit);
                            }
                        }
                    }
                }
            }

        } catch (IOException e) {
            if (LOGGER.isLoggable(Level.WARNING))
                LOGGER.warning("Unable to set band metadata");
        }
	}

	private void init(SpatioTemporalImageReader reader) {
    	if ((hdf4_type == null || hdf4_type == HDF4_TYPE.UNDEFINED) && reader != null){
        	hdf4_type = ((HDF4SpatioTemporalImageReader) reader).getHDF4Type();
        	if (hdf4_type == null || hdf4_type == HDF4_TYPE.UNDEFINED)
        		throw new IllegalArgumentException ("Unsupported HDF4 Type");
    	}
    	
	}

	@Override
    protected void setBoundedByElement(SpatioTemporalImageReader reader) {
		init(reader);
		getAttributesMap(reader);
        BoundedBy bb = getBoundedBy();
        if (bb == null)
            throw new IllegalArgumentException("Provided BoundedBy element is null");
        
        final int imageIndex = getImageIndex();
        int width;
        int height;
		try {
			width = reader.getWidth(imageIndex);
			height = reader.getHeight(imageIndex);
			gridRange = new Rectangle(0,0,width,height);
		} catch (IOException e) {
			throw new IllegalArgumentException("Unable to set boundedBy"+e.getLocalizedMessage(), e);
		}
		
        // ////////////////////////////////////////////////////////////////////
        //
        // Setting Envelope
        // 
        // ////////////////////////////////////////////////////////////////////
        double[] lc = null;
        double[] uc = null;
        
        switch (hdf4_type){
        case TeraScan:
	        if (nativeCrs != null){
	        	if (transform == null)
	        		transform = CRSUtilities.createAffineTransform(attributesMap.get(HDF4TeraScanProperties.ProjAttribs.PROJECT_TO_IMAGE_AFFINE));
	        	envelope = CRSUtilities.buildEnvelope(transform, gridRange);
	        }
	        lc = envelope.getLowerCorner().getCoordinate();
	        uc = envelope.getUpperCorner().getCoordinate();
	        
	        break;
        case APS:
	        if (nativeCrs != null){
	        	envelope = CRSUtilities.buildEnvelope(WGS84_CRS, nativeCrs, attributesMap.get(HDF4APSStreamMetadata.MAP_LOWER_LEFT)
	        			, attributesMap.get(HDF4APSStreamMetadata.MAP_UPPER_RIGHT));
	        }
	        lc = envelope.getLowerCorner().getCoordinate();
	        uc = envelope.getUpperCorner().getCoordinate();
	        break;
        }
        
        bb.setLowerCorner(lc);
        bb.setUpperCorner(uc);
        
        if (isHasTemporalCRS()) {
            // //
            //
            // Setting temporal Extent
            //
            // //
        	final TemporalObject timeExtent;
        	if (startInstant != null){
        		if (endInstant != null)
        			timeExtent = new DefaultPeriod(startInstant, endInstant);
        		else
        			timeExtent = startInstant;
        		setTimeExtentNode(bb, timeExtent);
        	}
        	else 
        		throw new IllegalArgumentException ("Unavailable time extent");
        	
       		
        }
    }
    
}
