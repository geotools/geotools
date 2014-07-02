/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2007-2013, Open Source Geospatial Foundation (OSGeo)
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

import java.awt.Color;
import java.awt.Dimension;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.media.jai.Interpolation;

import org.geotools.coverage.grid.GridGeometry2D;
import org.geotools.coverage.grid.io.AbstractGridFormat;
import org.geotools.coverage.grid.io.DecimationPolicy;
import org.geotools.coverage.grid.io.OverviewPolicy;
import org.geotools.data.DataSourceException;
import org.geotools.factory.Hints;
import org.geotools.gce.imagemosaic.SpatialRequestHelper.CoverageProperties;
import org.opengis.filter.Filter;
import org.opengis.metadata.Identifier;
import org.opengis.parameter.GeneralParameterDescriptor;
import org.opengis.parameter.GeneralParameterValue;
import org.opengis.parameter.ParameterDescriptor;
import org.opengis.parameter.ParameterValue;
import org.opengis.parameter.ParameterValueGroup;
import org.opengis.referencing.ReferenceIdentifier;

/**
 * A class to handle coverage requests to a reader for a single 2D layer..
 * 
 * @author Daniele Romagnoli, GeoSolutions
 * @author Simone Giannecchini, GeoSolutions
 */
@SuppressWarnings("rawtypes")
class RasterLayerRequest {
	/** Logger. */
    private final static Logger LOGGER = org.geotools.util.logging.Logging.getLogger(RasterLayerRequest.class);

    private ReadType readType = AbstractGridFormat.USE_JAI_IMAGEREAD.getDefaultValue()?ReadType.JAI_IMAGEREAD:ReadType.DIRECT_READ;

    SpatialRequestHelper spatialRequestHelper;
    
    /** The desired decimation Policy for this request */
    private DecimationPolicy decimationPolicy;
    
    /** The desired overview Policy for this request */
    private OverviewPolicy overviewPolicy;
    
    /** The Interpolation required to serve this request */
    private Interpolation interpolation;

    private FootprintBehavior footprintBehavior = FootprintBehavior.None;
    
    private int defaultArtifactsFilterThreshold = Integer.MIN_VALUE;;
    
    private double artifactsFilterPTileThreshold;
    
    private boolean heterogeneousGranules = false; 

    RasterManager rasterManager;
    
	private Color inputTransparentColor=AbstractGridFormat.INPUT_TRANSPARENT_COLOR.getDefaultValue();;

	private boolean blend=ImageMosaicFormat.FADING.getDefaultValue();
	
	/** Specifies the behavior for the merging of the final mosaic.*/
	private MergeBehavior mergeBehavior=MergeBehavior.getDefault();

	private Color outputTransparentColor=ImageMosaicFormat.OUTPUT_TRANSPARENT_COLOR.getDefaultValue();;

	/**
	 * Max number of tiles that this plugin will load.
	 * 
	 * If this number is exceeded, i.e. we request an area which is too large
	 * instead of getting stuck with opening thousands of files I give you back a
	 * fake coverage. 
	 */
	private int maximumNumberOfGranules=ImageMosaicFormat.MAX_ALLOWED_TILES.getDefaultValue().intValue();

	private double[] backgroundValues;

	private Dimension tileDimensions;

	private boolean multithreadingAllowed;

	private List<?> requestedTimes;

	private List<?> elevation;
	
	private Filter filter;

    private boolean accurateResolution;
	
        private final Map<String,List> requestedAdditionalDomains = new HashMap<String,List>();

	/** Sort clause on shapefile attributes.*/
	private String sortClause;

	public List<?> getElevation() {
		return elevation;
	}

	public String getSortClause() {
		return sortClause;
	}

	public void setSortClause(String sortClause) {
		this.sortClause = sortClause;
	}

	public Filter getFilter() {
		return filter;
	}
	
	public List<?> getRequestedTimes() {
		return requestedTimes;
	}

	public boolean isMultithreadingAllowed() {
		return multithreadingAllowed;
	}

	public DecimationPolicy getDecimationPolicy() {
	        return decimationPolicy;
	}
	
    public boolean isHeterogeneousGranules() {
        return heterogeneousGranules;
    }

    public void setHeterogeneousGranules(final boolean heterogeneousGranules) {
        this.heterogeneousGranules = heterogeneousGranules;
    }

    RasterManager getRasterManager() {
        return rasterManager;
    }
    
    public Map<String, List> getRequestedAdditionalDomains() {
        return new HashMap<String, List>(requestedAdditionalDomains);
    }

	 
	/**
     * Build a new {@code CoverageRequest} given a set of input parameters.
     * 
     * @param params
     *                The {@code GeneralParameterValue}s to initialize this
     *                request
     * @param baseGridCoverage2DReader 
	 * @throws DataSourceException 
     */
    public RasterLayerRequest(final GeneralParameterValue[] params, final RasterManager rasterManager) throws DataSourceException {

        // //
        //
        // Setting default parameters
        //
        // //
        this.rasterManager = rasterManager;
        this.heterogeneousGranules = rasterManager.heterogeneousGranules;
        CoverageProperties coverageProperties = new CoverageProperties();
        coverageProperties.setBBox(rasterManager.spatialDomainManager.coverageBBox);
        coverageProperties.setRasterArea(rasterManager.spatialDomainManager.coverageRasterArea);
        coverageProperties.setFullResolution(rasterManager.spatialDomainManager.coverageFullResolution);
        coverageProperties.setGridToWorld2D(rasterManager.spatialDomainManager.coverageGridToWorld2D);
        coverageProperties.setCrs2D(rasterManager.spatialDomainManager.coverageCRS2D);
        coverageProperties.setGeographicBBox(rasterManager.spatialDomainManager.coverageGeographicBBox);
        coverageProperties.setGeographicCRS2D(rasterManager.spatialDomainManager.coverageGeographicCRS2D);
        this.spatialRequestHelper = new SpatialRequestHelper(coverageProperties);
        setDefaultParameterValues();

        // //
        //
        // Parsing parameter that can be used to control this request
        //
        // //
        if (params != null) {
            for (GeneralParameterValue gParam : params) {
                if (gParam instanceof ParameterValue<?>) {
                    final ParameterValue<?> param = (ParameterValue<?>) gParam;
                    final ReferenceIdentifier name = param.getDescriptor().getName();
                    extractParameter(param, name);
                }
            }
        }

        // //
        //
        // Set specific imageIO parameters: type of read operation,
        // imageReadParams
        //
        // //
        checkReadType();
        spatialRequestHelper.setAccurateResolution(accurateResolution);
        spatialRequestHelper.compute();
    }

    private void setDefaultParameterValues() {
        
        // get the read parameters for this format plus the ones for the basic format and set them to the default
    	final ParameterValueGroup readParams = this.rasterManager.parentReader.getFormat().getReadParameters();
    	if (readParams == null) {
    		if(LOGGER.isLoggable(Level.FINER))
    			LOGGER.finer("No default values for the read parameters!");
    		return;
    	}
    	final List<GeneralParameterDescriptor> parametersDescriptors = readParams.getDescriptor().descriptors();
    	for(GeneralParameterDescriptor descriptor:parametersDescriptors){
    		
    		// we canc get the default vale only with the ParameterDescriptor class
    		if(!(descriptor instanceof ParameterDescriptor))
    			continue;
    		
    		// get name and default value
    		final ParameterDescriptor desc=(ParameterDescriptor) descriptor;
	    	final ReferenceIdentifier name = desc.getName();
	    	final Object value= desc.getDefaultValue();
	    	
	        // //
	        //
	        // Requested GridGeometry2D parameter
	        //
	        // //
	        if (descriptor.getName().equals(AbstractGridFormat.READ_GRIDGEOMETRY2D.getName())) {
	        	if (value == null)
	        		continue;
	            final GridGeometry2D gg = (GridGeometry2D)value;
	            
	            spatialRequestHelper.setRequestedGridGeometry(gg);
	            continue;
	        }
	
	        // //
	        //
	        // Use JAI ImageRead parameter
	        //
	        // //
	        if (name.equals(AbstractGridFormat.USE_JAI_IMAGEREAD.getName())) {
	        	if (value == null)
	        		continue;
	            readType = ((Boolean)value)? ReadType.JAI_IMAGEREAD: ReadType.DIRECT_READ;
	            continue;
	        }
	
	
	        // //
	        //
	        // Overview Policy parameter
	        //
	        // //
	        if (name.equals(AbstractGridFormat.OVERVIEW_POLICY.getName())) {
	            if (value == null)
	                continue;
	            overviewPolicy = (OverviewPolicy) value;
	            continue;
	        }
	        
	        // //
                //
                // Decimation Policy parameter
                //
                // //
                if (name.equals(AbstractGridFormat.DECIMATION_POLICY.getName())) {
                    if (value == null)
                            continue;
                    decimationPolicy = (DecimationPolicy) value;
                    continue;
                }
	
                // //
                //
                // Interpolation parameter
                //
                // //
                if (name.equals(ImageMosaicFormat.INTERPOLATION.getName())) {
                    if (value == null) {
                            continue;
                    }
                    interpolation = (Interpolation) value;
                    continue;
                }
                
                
	        if (name.equals(AbstractGridFormat.INPUT_TRANSPARENT_COLOR.getName())) {
	        	if(value==null)
	        		continue;
				inputTransparentColor = (Color) value;
				// paranoiac check on the provided transparent color
				inputTransparentColor = new Color(
						inputTransparentColor.getRed(),
						inputTransparentColor.getGreen(), 
						inputTransparentColor.getBlue());
				continue;
	
			} 
	
			if (name.equals(ImageMosaicFormat.FADING.getName())) {
	        	if(value==null)
	        		continue;
				blend = ((Boolean) value).booleanValue();
				continue;
	
			} 
			if (name.equals(
					ImageMosaicFormat.OUTPUT_TRANSPARENT_COLOR.getName())) {
	        	if(value==null)
	        		continue;
				outputTransparentColor = (Color) value;
				// paranoiac check on the provided transparent color
				outputTransparentColor = new Color(
						outputTransparentColor.getRed(),
						outputTransparentColor.getGreen(), 
						outputTransparentColor.getBlue());
				continue;
	
			}
			
			if (name.equals(
					ImageMosaicFormat.BACKGROUND_VALUES.getName())) {
	        	if(value==null)
	        		continue;
				backgroundValues = (double[]) value;
				continue;
	
			}		
		
			if (name.equals(ImageMosaicFormat.MAX_ALLOWED_TILES.getName())) {
	        	if(value==null)
	        		continue;
				maximumNumberOfGranules=(Integer)value;
				continue;
			}	 
			
			if (name.equals(ImageMosaicFormat.DEFAULT_ARTIFACTS_FILTER_THRESHOLD.getName())) {
	                        if (value==null)
	                                continue;
	                        defaultArtifactsFilterThreshold =(Integer)value;
	                        continue;
	                }     
			
			if (name.equals(ImageMosaicFormat.ARTIFACTS_FILTER_PTILE_THRESHOLD.getName())) {
                            if (value==null)
                                    continue;
                            artifactsFilterPTileThreshold =(Double)value;
                            continue;
                    }     
			
			
			if (name.equals(ImageMosaicFormat.ALLOW_MULTITHREADING.getName())) {
	        	if(value==null)
	        		continue;
				multithreadingAllowed = ((Boolean) value).booleanValue();
				continue;
			}	 	
			
            if (name.equals(ImageMosaicFormat.FOOTPRINT_BEHAVIOR.getName())) {
                if (value == null)
                    continue;
                footprintBehavior = FootprintBehavior.valueOf((String) value);
                continue;
            }
			
	       
	        // //
	        //
	        // Suggested tile size parameter. It must be specified with
	        // the syntax: "TileWidth,TileHeight" (without quotes where TileWidth
	        // and TileHeight are integer values)
	        //
	        // //
	        if (name.equals(AbstractGridFormat.SUGGESTED_TILE_SIZE.getName())) {
	            final String suggestedTileSize = (String) value;
	
	            // Preliminary checks on parameter value
	            if ((suggestedTileSize != null)
	                    && (suggestedTileSize.trim().length() > 0)) {
	
	                if (suggestedTileSize
	                        .contains(AbstractGridFormat.TILE_SIZE_SEPARATOR)) {
	                    final String[] tilesSize = suggestedTileSize
	                            .split(AbstractGridFormat.TILE_SIZE_SEPARATOR);
	                    if (tilesSize.length == 2) {
	                        try {
	                            // Getting suggested tile size
	                            final int tileWidth = Integer.valueOf(tilesSize[0] .trim());
	                            final int tileHeight = Integer.valueOf(tilesSize[1].trim());
	                            tileDimensions= new Dimension(tileWidth,tileHeight);
	                        } catch (NumberFormatException nfe) {
	                            if (LOGGER.isLoggable(Level.WARNING)) {
	                                LOGGER.log(Level.WARNING, "Unable to parse "
	                                        + "suggested tile size parameter");
	                            }
	                        }
	                    }
	                }
	            }
	        }
	        
            if (name.equals(ImageMosaicFormat.ACCURATE_RESOLUTION.getName())) {
                if (value == null)
                    continue;
                accurateResolution = ((Boolean) value).booleanValue();
                return;
            }
    	}
		
	}

	/**
     * Set proper fields from the specified input parameter.
     * 
     * @param param
     *                the input {@code ParamaterValue} object
     * @param name
     *                the name of the parameter
     */
	private void extractParameter(ParameterValue<?> param, Identifier name) {

        // //
        //
        // Requested GridGeometry2D parameter
        //
        // //
        if (name.equals(AbstractGridFormat.READ_GRIDGEOMETRY2D.getName())) {
        	final Object value = param.getValue();
        	if (value == null)
        		return;
            final GridGeometry2D gg = (GridGeometry2D) value;
            spatialRequestHelper.setRequestedGridGeometry(gg);
            return;
        }

        // //
        //
        // Use JAI ImageRead parameter
        //
        // //
        if (name.equals(AbstractGridFormat.USE_JAI_IMAGEREAD.getName())) {
        	final Object value = param.getValue();
        	if(value==null)
        		return;
            readType = param.booleanValue() ? ReadType.JAI_IMAGEREAD: ReadType.DIRECT_READ;
            return;
        }

        // //
        //
        // Sort clause
        //
        // //
        if (name.equals(ImageMosaicFormat.SORT_BY.getName())) {
            final Object value = param.getValue();
            if(value==null)
                return;
            sortClause = param.stringValue();
            return;
        }

        // //
        //
        // Merge Behavior
        //
        // //
        if (name.equals(ImageMosaicFormat.MERGE_BEHAVIOR.getName())) {
            final Object value = param.getValue();
            if(value==null)
                return;
            mergeBehavior = MergeBehavior.valueOf(param.stringValue().toUpperCase());
            return;
        }

        // //
        //
        // Overview Policy parameter
        //
        // //
        if (name.equals(AbstractGridFormat.OVERVIEW_POLICY.getName())) {
            final Object value = param.getValue();
            if (value == null)
                return;
            overviewPolicy = (OverviewPolicy) value;
            return;
        }
        
        // //
        //
        // Decimation Policy parameter
        //
        // //
        if (name.equals(AbstractGridFormat.DECIMATION_POLICY.getName())) {
                final Object value = param.getValue();
                if(value==null)
                        return;
            decimationPolicy = (DecimationPolicy) value;
            return;
        }
        
        // //
        //
        // Interpolation parameter
        //
        // //
        if (name.equals(ImageMosaicFormat.INTERPOLATION.getName())) {
                final Object value = param.getValue();
                if(value==null)
                        return;
            interpolation = (Interpolation) value;
            return;
        }

        if (name.equals(AbstractGridFormat.INPUT_TRANSPARENT_COLOR.getName())) {
        	final Object value = param.getValue();
        	if(value==null)
        		return;
			inputTransparentColor = (Color) value;
			// paranoiac check on the provided transparent color
			inputTransparentColor = new Color(
					inputTransparentColor.getRed(),
					inputTransparentColor.getGreen(), 
					inputTransparentColor.getBlue());
			return;

		} 

		if (name.equals(ImageMosaicFormat.FADING.getName())) {
        	final Object value = param.getValue();
        	if(value==null)
        		return;
			blend = ((Boolean) value).booleanValue();
			return;

		} 
		if (name.equals(
				ImageMosaicFormat.OUTPUT_TRANSPARENT_COLOR.getName())) {
        	final Object value = param.getValue();
        	if(value==null)
        		return;
			outputTransparentColor = (Color) value;
			// paranoiac check on the provided transparent color
			outputTransparentColor = new Color(
					outputTransparentColor.getRed(),
					outputTransparentColor.getGreen(), 
					outputTransparentColor.getBlue());
			return;

		}
		
		if (name.equals(
				ImageMosaicFormat.BACKGROUND_VALUES.getName())) {
        	final Object value = param.getValue();
        	if(value==null)
        		return;
			backgroundValues = (double[]) value;
			return;

		}		
	
		if (name.equals(ImageMosaicFormat.MAX_ALLOWED_TILES.getName())) {
        	final Object value = param.getValue();
        	if(value==null)
        		return;
			maximumNumberOfGranules=param.intValue();
			return;
		}	 
		
		if (name.equals(ImageMosaicFormat.DEFAULT_ARTIFACTS_FILTER_THRESHOLD.getName())) {
		        final Object value = param.getValue();
	                if(value==null)
	                        return;
	                defaultArtifactsFilterThreshold = param.intValue();
	                return;
	        }
		
		if (name.equals(ImageMosaicFormat.ARTIFACTS_FILTER_PTILE_THRESHOLD.getName())) {
                    final Object value = param.getValue();
                    if(value==null)
                            return;
                    artifactsFilterPTileThreshold = param.doubleValue();
                    return;
                }
		
		if (name.equals(ImageMosaicFormat.ALLOW_MULTITHREADING.getName())) {
        	final Object value = param.getValue();
        	if(value==null)
        		return;
			multithreadingAllowed = ((Boolean) value).booleanValue();
			return;
		}	 	
		
        if (name.equals(ImageMosaicFormat.FOOTPRINT_BEHAVIOR.getName())) {
            final Object value = param.getValue();
            if (value == null)
                return;
            footprintBehavior = FootprintBehavior.valueOf((String) value);
            return;
        }
		
        if (name.equals(ImageMosaicFormat.ACCURATE_RESOLUTION.getName())) {
            final Object value = param.getValue();
            if (value == null) {
                    return;
            }
            accurateResolution = ((Boolean) value).booleanValue();
            return;
        } 
       
        // //
        //
        // Suggested tile size parameter. It must be specified with
        // the syntax: "TileWidth,TileHeight" (without quotes where TileWidth
        // and TileHeight are integer values)
        //
        // //
        if (name.equals(AbstractGridFormat.SUGGESTED_TILE_SIZE.getName())) {
            final String suggestedTileSize = (String) param.getValue();

            // Preliminary checks on parameter value
            if ((suggestedTileSize != null)
                    && (suggestedTileSize.trim().length() > 0)) {

                if (suggestedTileSize
                        .contains(AbstractGridFormat.TILE_SIZE_SEPARATOR)) {
                    final String[] tilesSize = suggestedTileSize
                            .split(AbstractGridFormat.TILE_SIZE_SEPARATOR);
                    if (tilesSize.length == 2) {
                        try {
                            // Getting suggested tile size
                            final int tileWidth = Integer.valueOf(tilesSize[0] .trim());
                            final int tileHeight = Integer.valueOf(tilesSize[1].trim());
                            tileDimensions = new Dimension(tileWidth,tileHeight);
                        } catch (NumberFormatException nfe) {
                            if (LOGGER.isLoggable(Level.WARNING)) {
                                LOGGER.log(Level.WARNING, "Unable to parse "
                                        + "suggested tile size parameter");
                            }
                        }
                    }
                }
            }
        }		
        
        // //
        //
        // Time parameter
        //
        // //
        if (name.equals(ImageMosaicFormat.TIME.getName())) {
        	final Object value = param.getValue();
        	if(value==null)
        		return;
            final List<?> dates = (List<?>) value;
            if (dates==null|| dates.size()<=0) {
                return;
            }

            requestedTimes=dates;
            return;
        }      
        
        //
        //
        // Elevation parameter
        //
        // //
        if (name.equals(ImageMosaicFormat.ELEVATION.getName())) {
        	final Object value = param.getValue();
        	if(value==null)
        		return;
            elevation = (List<?>) value;
            return;
        }            

        // //
        //
        // Runtime parameter
        //
        // //
        if (name.equals(ImageMosaicFormat.FILTER.getName())) {
        	final Object value = param.getValue();
        	if(value==null)
        		return;
            filter = (Filter) value;
            return;
        }            

        // //
        //
        // Additional dimension parameter check
        //
        // //
        String paramName = name.getCode();
        if (rasterManager.domainsManager != null && rasterManager.domainsManager.isParameterSupported(name)) {
            final Object value = param.getValue();
            if (value == null){
                return;
            }
            if (value instanceof List) {
                List values = (List) value; // we are assuming it is a list !!!
                // remove last comma
                requestedAdditionalDomains.put(paramName, values);
                               
            }
            return;
        }
    }

    /**
     * @return the accurateResolution
     */
    public boolean isAccurateResolution() {
        return accurateResolution;
    }

    /**
     * @param accurateResolution the accurateResolution to set
     */
    public void setAccurateResolution(boolean accurateResolution) {
        this.accurateResolution = accurateResolution;
    }            

	/**
     * Check the type of read operation which will be performed and return
     * {@code true} if a JAI imageRead operation need to be performed or
     * {@code false} if a simple read operation is needed.
     * 
     * @return {@code true} if the read operation will use a JAI ImageRead
     *         operation instead of a simple {@code ImageReader.read(...)} call.
     */
    private void checkReadType() {
        // //
        //
        // First of all check if the ReadType was already set as part the
        // request parameters
        //
        // //
        if (readType != ReadType.UNSPECIFIED)
            return ;

        // //
        //
        // Ok, the request did not explicitly set the read type, let's check the
        // hints.
        //
        // //
        final Hints hints=rasterManager.getHints();
        if (hints != null) {
            final Object o = hints.get(Hints.USE_JAI_IMAGEREAD);
            if (o != null) {
            	readType=(ReadType) o;
            }
        }

        // //
        //
        // Last chance is to use the default read type.
        //
        // //
        readType = ReadType.getDefault();
    }

	public Color getInputTransparentColor() {
		return inputTransparentColor;
	}

	public Color getOutputTransparentColor() {
		return outputTransparentColor;
	}

	public int getMaximumNumberOfGranules() {
		return maximumNumberOfGranules;
	}
	
	public FootprintBehavior getFootprintBehavior() {
            return footprintBehavior;
    }

    public int getDefaultArtifactsFilterThreshold() {
        return defaultArtifactsFilterThreshold;
    }

    public double getArtifactsFilterPTileThreshold() {
        return artifactsFilterPTileThreshold;
    }
	public boolean isBlend() {
		return blend;
	}
	
	public ReadType getReadType() {
		return readType;
	}

	public double[] getBackgroundValues() {
		return backgroundValues;
	}

	public void setInterpolation(Interpolation interpolation) {
        this.interpolation = interpolation;
    }

    public Interpolation getInterpolation() {
        return interpolation;
    }

	public Dimension getTileDimensions() {
		return tileDimensions;
	}
	
	@Override
	public String toString() {
		final StringBuilder builder= new StringBuilder();
		builder.append("RasterLayerRequest description: \n");
		builder.append(spatialRequestHelper).append("\n");
		builder.append("\tReadType=").append(readType);
		return builder.toString();
	}

    public MergeBehavior getMergeBehavior() {
        return mergeBehavior;
    }

    public OverviewPolicy getOverviewPolicy() {
        return overviewPolicy;
    }

    public boolean isEmpty() {
        return spatialRequestHelper.isEmpty();
    }
   
}