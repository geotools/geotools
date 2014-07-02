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

import it.geosolutions.imageio.pam.PAMDataset;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.geom.AffineTransform;
import java.awt.geom.NoninvertibleTransformException;
import java.awt.image.ColorModel;
import java.awt.image.IndexColorModel;
import java.awt.image.MultiPixelPackedSampleModel;
import java.awt.image.RenderedImage;
import java.awt.image.SampleModel;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.Future;
import java.util.concurrent.FutureTask;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.imageio.ImageReadParam;
import javax.measure.unit.Unit;
import javax.media.jai.BorderExtender;
import javax.media.jai.Histogram;
import javax.media.jai.ImageLayout;
import javax.media.jai.Interpolation;
import javax.media.jai.JAI;
import javax.media.jai.PlanarImage;
import javax.media.jai.ROI;
import javax.media.jai.RenderedOp;
import javax.media.jai.TileCache;
import javax.media.jai.TileScheduler;
import javax.media.jai.operator.ConstantDescriptor;
import javax.media.jai.operator.FormatDescriptor;
import javax.media.jai.operator.MosaicDescriptor;
import javax.media.jai.operator.TranslateDescriptor;

import org.apache.commons.io.FilenameUtils;
import org.geotools.coverage.Category;
import org.geotools.coverage.GridSampleDimension;
import org.geotools.coverage.TypeMap;
import org.geotools.coverage.grid.GridCoverage2D;
import org.geotools.coverage.grid.GridCoverageFactory;
import org.geotools.coverage.grid.GridEnvelope2D;
import org.geotools.coverage.grid.GridGeometry2D;
import org.geotools.coverage.grid.io.AbstractGridCoverage2DReader;
import org.geotools.coverage.grid.io.GridCoverage2DReader;
import org.geotools.data.DataSourceException;
import org.geotools.data.DataUtilities;
import org.geotools.data.Query;
import org.geotools.factory.Hints;
import org.geotools.filter.SortByImpl;
import org.geotools.gce.imagemosaic.GranuleDescriptor.GranuleLoadingResult;
import org.geotools.gce.imagemosaic.OverviewsController.OverviewLevel;
import org.geotools.gce.imagemosaic.RasterManager.DomainDescriptor;
import org.geotools.gce.imagemosaic.catalog.GranuleCatalogVisitor;
import org.geotools.gce.imagemosaic.processing.ArtifactsFilterDescriptor;
import org.geotools.geometry.Envelope2D;
import org.geotools.geometry.GeneralEnvelope;
import org.geotools.geometry.jts.JTS;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.image.ImageWorker;
import org.geotools.referencing.CRS;
import org.geotools.referencing.operation.matrix.XAffineTransform;
import org.geotools.referencing.operation.transform.AffineTransform2D;
import org.geotools.resources.coverage.CoverageUtilities;
import org.geotools.resources.coverage.FeatureUtilities;
import org.geotools.resources.geometry.XRectangle2D;
import org.geotools.resources.i18n.Vocabulary;
import org.geotools.resources.i18n.VocabularyKeys;
import org.geotools.resources.image.ImageUtilities;
import org.geotools.util.NumberRange;
import org.geotools.util.SimpleInternationalString;
import org.geotools.util.Utilities;
import org.jaitools.imageutils.ImageLayout2;
import org.jaitools.imageutils.ROIGeometry;
import org.opengis.coverage.ColorInterpretation;
import org.opengis.coverage.SampleDimension;
import org.opengis.coverage.SampleDimensionType;
import org.opengis.coverage.grid.GridCoverage;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.filter.Filter;
import org.opengis.filter.sort.SortBy;
import org.opengis.filter.sort.SortOrder;
import org.opengis.geometry.BoundingBox;
import org.opengis.referencing.datum.PixelInCell;
import org.opengis.referencing.operation.MathTransform;
import org.opengis.referencing.operation.MathTransform1D;
import org.opengis.referencing.operation.MathTransform2D;
import org.opengis.referencing.operation.TransformException;
import org.opengis.util.InternationalString;

import com.vividsolutions.jts.geom.Envelope;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.util.Assert;
/**
 * A RasterLayerResponse. An instance of this class is produced everytime a
 * requestCoverage is called to a reader.
 * 
 * @author Simone Giannecchini, GeoSolutions
 * @author Daniele Romagnoli, GeoSolutions
 * @author Stefan Alfons Krueger (alfonx), Wikisquare.de : Support for jar:file:foo.jar/bar.properties URLs
 */
@SuppressWarnings("rawtypes")
class RasterLayerResponse{

    class MosaicOutput {
        
        public MosaicOutput (MosaicElement element) {
            this.image = element.source;
            this.pamDataset = element.pamDataset;
        }

        public MosaicOutput(RenderedImage image, PAMDataset pamDataset) {
            super();
            this.image = image;
            this.pamDataset = pamDataset;
        }

        public RenderedImage getImage() {
            return image;
        }

        public void setImage(RenderedImage image) {
            this.image = image;
        }

        public PAMDataset getPamDataset() {
            return pamDataset;
        }

        public void setPamDataset(PAMDataset pamDataset) {
            this.pamDataset = pamDataset;
        }

        RenderedImage image;

        PAMDataset pamDataset;
    }
    
    private static final class SimplifiedGridSampleDimension extends GridSampleDimension implements SampleDimension{

		/**
		 * 
		 */
		private static final long serialVersionUID = 2227219522016820587L;

		private double nodata;
		private double minimum;
		private double maximum;
		private double scale;
		private double offset;
		private Unit<?> unit;
		private SampleDimensionType type;
		private ColorInterpretation color;
		private Category bkg;

		public SimplifiedGridSampleDimension(
				CharSequence description,
				SampleDimensionType type, 
				ColorInterpretation color,
				double nodata,
				double minimum, 
				double maximum, 
				double scale, 
				double offset,
				Unit<?> unit) {
			super(description,!Double.isNaN(nodata)?
					new Category[]{new Category(Vocabulary
		                    .formatInternational(VocabularyKeys.NODATA), new Color[]{new Color(0, 0, 0, 0)} , NumberRange
		                    .create(nodata, nodata), NumberRange
		                    .create(nodata, nodata))}:null,unit);
			this.nodata=nodata;
			this.minimum=minimum;
			this.maximum=maximum;
			this.scale=scale;
			this.offset=offset;
			this.unit=unit;
			this.type=type;
			this.color=color;
			this.bkg=new Category("Background", Utils.TRANSPARENT, 0);
		}



		@Override
		public double getMaximumValue() {
			return maximum;
		}

		@Override
		public double getMinimumValue() {
			return minimum;
		}

		@Override
		public double[] getNoDataValues() throws IllegalStateException {
			return new double[]{nodata};
		}

		@Override
		public double getOffset() throws IllegalStateException {
			return offset;
		}

		@Override
		public NumberRange<? extends Number> getRange() {
			return super.getRange();
		}

		@Override
		public SampleDimensionType getSampleDimensionType() {
			return type;
		}

		@Override
		public MathTransform1D getSampleToGeophysics() {
			return super.getSampleToGeophysics();
		}

		@Override
		public Unit<?> getUnits() {
			return unit;
		}
		
		@Override
		public double getScale() {
			return scale;
		}
		
		@Override
		public ColorInterpretation getColorInterpretation() {
			return color;
		}


		@Override
		public Category getBackground() {
			return bkg;
		}

		@Override
		public InternationalString[] getCategoryNames()
				throws IllegalStateException {
			return new InternationalString[]{SimpleInternationalString.wrap("Background")};
		}
	}
    
    /**
     * Represents the input  raster element for a mosaic operation, 
     * source {@link RenderedImage}, {@link ROI} and alpha channel.
     * 
     * <p>
     * This class is just a simple bean that holds a single element for the mosaic.
     * 
     * @author Simone Giannecchini, GeoSolutions SAS
     *
     */
    private class MosaicElement {

        private MosaicElement(PlanarImage alphaChannel, ROI roi, RenderedImage source, PAMDataset pamDataset) {
            this.alphaChannel = alphaChannel;
            this.roi = roi;
            this.source = source;
            this.pamDataset = pamDataset;
        }

        PlanarImage alphaChannel;

        ROI roi;

        RenderedImage source;

        PAMDataset pamDataset;

    }
    
    /**
     * This class represents the inputs for a mosaic JAI operation.
     * 
     * <p>
     * It contains
     * <ol>
     *  <li>the images</li>
     *  <li>their transparencies</li>
     *  <li>the ROIs</li>
     *  <li>source thresholds</li>
     *  <li>indications on the alpha</li>
     * </ol>
     * @author Simone Giannecchini, GeoSolutions SAS
     *
     */
    private class MosaicInputs {
        private MosaicInputs(boolean doInputTransparency, boolean hasAlpha,
                List<MosaicElement> sources, double[][] sourceThreshold) {
            this.doInputTransparency = doInputTransparency;
            this.hasAlpha = hasAlpha;
            this.sources = sources;
            this.sourceThreshold = sourceThreshold;
        }
        private final boolean doInputTransparency;
        private final boolean hasAlpha;
        private final List<MosaicElement> sources;
        private final double[][] sourceThreshold;
        
    }
    /**
     * 
     * This class is responsible for collecting the granules that 
     * @author Simone Giannecchini, GeoSolutions SAS
     *
     */
    private class GranuleCollector {

        /**
         * Constructor.
         * 
         * @param granuleFilter the {@link Filter} we are supposed to use to select granules for this {@link GranuleCollector}.
         * @param dryRun whether we need to make 
         */
        private GranuleCollector(Filter granuleFilter, boolean dryRun) {
            this.granuleFilter = granuleFilter;
            this.dryRun = dryRun;
            inputTransparentColor = request.getInputTransparentColor();
            doInputTransparency = inputTransparentColor != null && !footprintBehavior.handleFootprints();
        }

        /** The number of collected granules.**/
        private int granulesNumber;
        
        /** {@link Filter} instance used to collect granule.*/
        private final Filter granuleFilter;
        
        /**We can request a dry run (no tasks are spawn) with this member.*/
        private final boolean dryRun;

        /** The final lists for granules to be computed, splitted per dimension value.*/
        private final List<Future<GranuleLoadingResult>> granulesFutures = new ArrayList<Future<GranuleLoadingResult>>();

        private double[][] sourceThreshold;

        private boolean hasAlpha;

        private boolean doInputTransparency;

        private int[] alphaIndex= new int[1];

        private Color inputTransparentColor;
        
        /**
         * This method is responsible for collecting all the granules accepting a certain {@link Filter}.
         * 
         * <p>
         * The method return <code>true</code> when a {@link GranuleDescriptor} for which the {@link GranuleDescriptor#originator} {@link SimpleFeature}
         * is evaluated positively by the internal filter and retain the granule, or <code>false</code> otherwise so that the caller can keep trying
         * with a different {@link GranuleCollector}
         * @param granuleDescriptor the {@link GranuleDescriptor} to test with the internal {@link Filter}
         * @return <code>true</code> in case the {@link GranuleDescriptor} is added, <code>false</code> otherwise.
         */
        private boolean accept(GranuleDescriptor granuleDescriptor){
            Utilities.ensureNonNull("granuleDescriptor", granuleDescriptor);
            
            if (granuleFilter.evaluate(granuleDescriptor.originator)) {

                Object imageIndex = granuleDescriptor.originator.getAttribute("imageindex");
                if(imageIndex != null && imageIndex instanceof Integer) {
                    imageChoice = ((Integer) imageIndex).intValue();
                }
                
                final GranuleLoader loader = new GranuleLoader(baseReadParameters, imageChoice, mosaicBBox, finalWorldToGridCorner, granuleDescriptor, request, hints);
                if (!dryRun) {
                    if (multithreadingAllowed && rasterManager.parentReader.multiThreadedLoader != null) {
                        // MULTITHREADED EXECUTION submitting the task
                        granulesFutures.add(rasterManager.parentReader.multiThreadedLoader.submit(loader));
                    } else {
                        // SINGLE THREADED Execution, we defer the execution to when we have done the loading
                        final FutureTask<GranuleLoadingResult> task = new FutureTask<GranuleLoadingResult>(loader);
                        granulesFutures.add(task);
                        task.run(); // run in current thread
                    }
                }
                if (LOGGER.isLoggable(Level.FINE)) {
                    LOGGER.fine("We added the granule " + granuleDescriptor.toString());
                }
                
                // we added it
                granulesNumber++;
                return true;
            } else {
                if (LOGGER.isLoggable(Level.FINE)) {
                    LOGGER.fine("We filtered out the granule " + granuleDescriptor.toString());
                }
        }
            return false;
        }
        
        /**
         * This methods collects the granules from their eventual multithreaded processing
         * and turn them into a {@link MosaicInputs} object.
         * 
         * @return a {@link MosaicInputs} ready to be mosaicked.
         */
        private MosaicInputs collectGranules()throws IOException{
            // do we have anything to do?
            if (granulesNumber <= 0) {
                if (LOGGER.isLoggable(Level.FINE)){
                    LOGGER.log(Level.FINE, "granules number <= 0");
                }
                return null;
            }
            
           // execute them all
           final StringBuilder paths = new StringBuilder();
           final List<MosaicElement> returnValues= new ArrayList<RasterLayerResponse.MosaicElement>();
           // collect sources for the current dimension and then process them
           for (Future<GranuleLoadingResult> future :granulesFutures) {
                     
                try {
                    // get the resulting RenderedImage
                    final GranuleLoadingResult result = future.get();
                    if (result == null) {
                        if (LOGGER.isLoggable(Level.FINE)){
                            LOGGER.log(Level.FINE, "Unable to load the raster for granule with request " + request.toString());
                        }
                        continue;
                    }
                    final RenderedImage loadedImage = result.getRaster();
                    if (loadedImage == null) {
                        if (LOGGER.isLoggable(Level.FINE)) {
                           LOGGER.log(Level.FINE,
                                   "Unable to load the raster for granuleDescriptor " +result.granuleUrl+ " with request "+request.toString()
                           );
                       }
                       continue;
                   }
                   
                   // now process it
                    if (sourceThreshold == null) {
                            //
                            // We check here if the images have an alpha channel or some
                            // other sort of transparency. In case we have transparency
                            // I also save the index of the transparent channel.
                            //
                            // Specifically, I have to check if the loaded image have
                            // transparency, because if we do a ROI and/or we have a
                            // transparent color to set we have to remove it.
                            //
                            final ColorModel cm = loadedImage.getColorModel();
                            hasAlpha = cm.hasAlpha();
                            if (hasAlpha){
                                alphaIndex[0]= cm.getNumComponents() - 1 ;
                            }

                            //
                            // we set the input threshold accordingly to the input
                            // image data type. I find the default value (which is 0) very bad
                            // for data type other than byte and ushort. With float and double
                            // it can cut off a large par of the dynamic.
                            //
                            sourceThreshold = new double[][] { { CoverageUtilities.getMosaicThreshold(loadedImage.getSampleModel().getDataType()) } };                                                                  
                   }                                    

                   // moving on
                   if (LOGGER.isLoggable(Level.FINE)) {
                       LOGGER.fine("Adding to mosaic granule " +result.granuleUrl);
                   }

                   // path management
                   File inputFile = DataUtilities.urlToFile(result.granuleUrl);
                   String canonicalPath = inputFile.getCanonicalPath();                   
                   paths.append(canonicalPath).append(",");
                   
                   // add to the mosaic collection, with preprocessing
                   // TODO pluggable mechanism for processing (artifacts,etc...)
                   MosaicElement input = preProcessGranuleRaster(
                                           loadedImage,
                                           result,
                                           canonicalPath);  
                   returnValues.add(input);
                   
                } catch (Exception e) {
                    if (LOGGER.isLoggable(Level.INFO)){
                        LOGGER.info("Adding to mosaic failed, original request was " + request);
                    }
                    throw new IOException(e);
                }               
               

               // collect paths
                granulesPaths = paths.length() > 1 ? paths.substring(0, paths.length() - 1) : "";
           }
           if (returnValues == null || returnValues.isEmpty()) {
               if (LOGGER.isLoggable(Level.INFO)){
                   LOGGER.info("The MosaicElement list is null or empty");
               }
           }
           return new MosaicInputs(doInputTransparency, hasAlpha, returnValues, sourceThreshold);
        }

        private MosaicElement preProcessGranuleRaster(
            	RenderedImage granule,  
            	final GranuleLoadingResult result, 
            	String canonicalPath) {
        
            //
            // INDEX COLOR MODEL EXPANSION
            //
            // Take into account the need for an expansions of the original color
            // model.
            //
            // If the original color model is an index color model an expansion
            // might be requested in case the different palettes are not all the
            // same. In this case the mosaic operator from JAI would provide wrong
            // results since it would take the first palette and use that one for
            // all the other images.
            //
            // There is a special case to take into account here. In case the input
            // images use an IndexColorModel it might happen that the transparent
            // color is present in some of them while it is not present in some
            // others. This case is the case where for sure a color expansion is
            // needed. However we have to take into account that during the masking
            // phase the images where the requested transparent color was present
            // will have 4 bands, the other 3. If we want the mosaic to work we
            // have to add an extra band to the latter type of images for providing
            // alpha information to them.
            //
            //
            if (rasterManager.expandMe && granule.getColorModel() instanceof IndexColorModel) {
            	granule = new ImageWorker(granule).forceComponentColorModel().getRenderedImage();
            }
        
            //
            // TRANSPARENT COLOR MANAGEMENT
            //
            if (doInputTransparency) {
                if (LOGGER.isLoggable(Level.FINE)) {
                    LOGGER.fine("Support for alpha on input granule " + result.granuleUrl);
                }
                granule = new ImageWorker(granule).makeColorTransparent(inputTransparentColor).getRenderedImage();
                hasAlpha=granule.getColorModel().hasAlpha();
                if(!granule.getColorModel().hasAlpha()){
                    // if the resulting image has no transparency (can happen with IndexColorModel then we need to try component
                    // color model
                    granule = new ImageWorker(granule).forceComponentColorModel(true).makeColorTransparent(inputTransparentColor).getRenderedImage();
                    hasAlpha=granule.getColorModel().hasAlpha();
                }
                assert hasAlpha;
                
            }
            PlanarImage alphaChannel=null;		
            if (hasAlpha || doInputTransparency) {
                ImageWorker w = new ImageWorker(granule);
                if (granule.getSampleModel() instanceof MultiPixelPackedSampleModel||granule.getColorModel() instanceof IndexColorModel) {
                    w.forceComponentColorModel();
                    granule=w.getRenderedImage();
                }
                // doing this here gives the guarantee that I get the correct index for the transparency band
                alphaIndex[0] = granule.getColorModel().getNumComponents() - 1;
                assert alphaIndex[0]< granule.getSampleModel().getNumBands();
                
                //
                // ALPHA in INPUT
                //
                // I have to select the alpha band and provide it to the final
                // mosaic operator. I have to force going to ComponentColorModel in
                // case the image is indexed.
                //
                alphaChannel = w.retainBands(alphaIndex).getPlanarImage();
            }
        
        
            //
            // ROI
            //
            // we need to add its roi in order to avoid problems with the mosaics sources overlapping
            final Rectangle bounds = PlanarImage.wrapRenderedImage(granule).getBounds();
            Geometry mask = JTS.toGeometry(new Envelope(bounds.getMinX(), bounds.getMaxX(), bounds.getMinY(), bounds.getMaxY()));
            ROI imageROI = new ROIGeometry(mask);            
            if (footprintBehavior.handleFootprints()){ 

                
                // get the real footprint
                final ROI footprint = result.getFootprint();
                if (footprint != null) {
                    if (imageROI.contains(footprint.getBounds2D().getBounds())) {
                        imageROI = footprint;
                    } else {
                        imageROI = imageROI.intersect(footprint);
                    }
                }
                
                // ARTIFACTS FILTERING
                if (defaultArtifactsFilterThreshold != Integer.MIN_VALUE && result.isDoFiltering()){
                    int artifactThreshold = defaultArtifactsFilterThreshold; 
                    if (artifactsFilterPTileThreshold != -1){
                        
                        //Looking for a histogram for that granule in order to 
                        //setup dynamic threshold 
                        if (canonicalPath != null){
                            final String path = FilenameUtils.getFullPath(canonicalPath);
                            final String baseName = FilenameUtils.getBaseName(canonicalPath);
                            final String histogramPath = path + baseName + "." + "histogram";
                            final Histogram histogram = Utils.getHistogram(histogramPath);
                            if (histogram != null) {
                                final double[]p = histogram.getPTileThreshold(artifactsFilterPTileThreshold);
                                artifactThreshold = (int)p[0];
                            }
                        }
                    }
                    if (LOGGER.isLoggable(Level.FINE)){
                        LOGGER.log(Level.FINE, "Filtering granules artifacts");
                    }
                    granule = ArtifactsFilterDescriptor.create(granule, imageROI, new double[]{0}, artifactThreshold, 3, hints);
                }
            }
            
            // preparing input 
            return new MosaicElement(alphaChannel, imageROI, granule, result.getPamDataset());
        }
    }
    /**
     * A class doing the mosaic operation on top of a List of {@link MosaicElement}s.
     * 
     * @author Simone Giannecchini, GeoSolutions SAS
     *
     */
    private class Mosaicker {
        private final List<MosaicElement> inputs;
        private final double[][] sourceThreshold;
        private final boolean doInputTransparency;
        private final boolean hasAlpha;
        private final MergeBehavior mergeBehavior;

        private Mosaicker(MosaicInputs inputs, MergeBehavior mergeBehavior) {
            this.inputs = new ArrayList<RasterLayerResponse.MosaicElement>(inputs.sources);
            this.sourceThreshold = inputs.sourceThreshold;
            this.doInputTransparency = inputs.doInputTransparency;
            this.hasAlpha = inputs.hasAlpha;
            this.mergeBehavior=mergeBehavior;
        }

        /**
         * @return
         */
        private RenderingHints prepareHints() {
            // build final layout and use it for cropping purposes
            final ImageLayout layout = new ImageLayout(rasterBounds.x, rasterBounds.y, rasterBounds.width, rasterBounds.height);
            Dimension tileDimensions = request.getTileDimensions();
            if (tileDimensions == null) {
                tileDimensions=(Dimension) JAI.getDefaultTileSize().clone();
            }
            layout.setTileGridXOffset(0).setTileGridYOffset(0);
            layout.setTileHeight(tileDimensions.width).setTileWidth(tileDimensions.height);
            final RenderingHints localHints = new RenderingHints(JAI.KEY_IMAGE_LAYOUT, layout);
            
            // look for additional hints for caching and tile scheduling
            if (hints != null && !hints.isEmpty()) {
                
                // TileCache
                TileCache tc = Utils.getTileCacheHint(hints);
                if (tc != null) {
                        localHints.add(new RenderingHints(JAI.KEY_TILE_CACHE, (TileCache) tc));
                }
                
                // BorderExtender
                localHints.add(ImageUtilities.BORDER_EXTENDER_HINTS);// default
                BorderExtender be = Utils.getBorderExtenderHint(hints);
                if (be != null) {
                    localHints.add(new RenderingHints(JAI.KEY_BORDER_EXTENDER,be));
                }
        
                // TileScheduler 
                TileScheduler tileScheduler = Utils.getTileSchedulerHint(hints);
                if (tileScheduler!=null) {
                    localHints.add(new RenderingHints(JAI.KEY_TILE_SCHEDULER,tileScheduler));
                }
            }
            return localHints;
        }

        /**
         * Once we reach this method it means that we have loaded all the images
         * which were intersecting the requested envelope. Next step is to create
         * the final mosaic image and cropping it to the exact requested envelope.
         * 
         * @return A {@link MosaicElement}}.
         */
        private MosaicElement createMosaic() throws IOException  {
            
            // anything to do?
            final int size = inputs.size();
             if (size <= 0) {
                if (LOGGER.isLoggable(Level.FINE)) {
                    LOGGER.log(Level.FINE, "Unable to load any granuleDescriptor ");
                }
                return null;
            }

            // === prepare hints
            final RenderingHints localHints = prepareHints();

            //
            // SPECIAL CASE
            // 1 single tile, we try not do a mosaic.
            if (size == 1 && Utils.OPTIMIZE_CROP) {
                // prepare input
                MosaicElement in = inputs.get(0);
                if (in == null) {
                    throw new NullPointerException("The list of MosaicElements contains one element but it's null");
                }
                PAMDataset pamDataset = in.pamDataset;
    
                // the roi is exactly equal to the image
                ROI roi = in.roi;
                if (roi != null) {
                    Rectangle bounds = Utils.toRectangle(roi.getAsShape());
                    if(bounds!=null){
                        RenderedImage mosaic = in.source;
                        Rectangle imageBounds = PlanarImage.wrapRenderedImage(mosaic).getBounds();
                        if (imageBounds.equals(bounds)) {
    
                            // do we need to crop? (image is bigger than requested?)
                            if (!rasterBounds.contains(imageBounds)) {
                                // we have to crop
                                XRectangle2D.intersect(imageBounds, rasterBounds, imageBounds);
    
                                if (imageBounds.isEmpty()) {
                                    // return back a constant image
                                    return null;
                                }
                                // crop
                                ImageWorker iw = new ImageWorker(mosaic);
                                iw.setRenderingHints(localHints);
                                iw.crop(imageBounds.x, imageBounds.y, imageBounds.width, imageBounds.height);
                                mosaic = iw.getRenderedImage();
                                imageBounds = PlanarImage.wrapRenderedImage(mosaic).getBounds();
                            }
    
                            // and, do we need to add a BORDER around the image?
                            if (!imageBounds.contains(rasterBounds)) {
                                mosaic = MergeBehavior.FLAT
                                        .process(
                                                new RenderedImage[] { mosaic },
                                                backgroundValues,
                                                sourceThreshold,
                                                (hasAlpha || doInputTransparency) ? new PlanarImage[] { in.alphaChannel }: new PlanarImage[] { null },
                                                new ROI[] { in.roi },
                                                request.isBlend() ? MosaicDescriptor.MOSAIC_TYPE_BLEND: MosaicDescriptor.MOSAIC_TYPE_OVERLAY,
                                                localHints);
                                roi=roi.add(new ROIGeometry(JTS.toGeometry(new ReferencedEnvelope(rasterBounds, null))));
                                if (footprintBehavior!=FootprintBehavior.None) {
                                    // Adding globalRoi to the output
                                    RenderedOp rop = (RenderedOp) mosaic;
                                    rop.setProperty("ROI", in.roi);
                                }
                            }
    
                        // add to final list
                        return new MosaicElement(in.alphaChannel, roi, mosaic, pamDataset);
                    }
                }
            }
            }

            // === do the mosaic as usual
            // prepare sources for the mosaic operation
            final RenderedImage[] sources = new RenderedImage[size];
            final PlanarImage[] alphas = new PlanarImage[size];
            ROI[] rois = new ROI[size];
            final PAMDataset[] pams = new PAMDataset[size];
            int realROIs=0;
            for (int i = 0; i < size; i++) {
                final MosaicElement mosaicElement = inputs.get(i);
                sources[i] = mosaicElement.source;
                alphas[i] = mosaicElement.alphaChannel;
                rois[i] = mosaicElement.roi;
                pams[i] = mosaicElement.pamDataset;

                // compose the overall ROI if needed
                if (mosaicElement.roi != null) {
                    realROIs++;
                }
            }
            if (realROIs == 0){
                rois = null;
            }


            // execute mosaic
            final RenderedImage mosaic = 
                mergeBehavior.process(
                        sources,
                        backgroundValues, 
                        sourceThreshold,
                        (hasAlpha || doInputTransparency) ? alphas : null,
                        rois, 
                        request.isBlend() ? MosaicDescriptor.MOSAIC_TYPE_BLEND: MosaicDescriptor.MOSAIC_TYPE_OVERLAY, 
                        localHints);
            
            ROI overallROI = mosaicROIs(rois);
            if (footprintBehavior != FootprintBehavior.None) {
                // Adding globalRoi to the output
                RenderedOp rop = (RenderedOp) mosaic;                
                rop.setProperty("ROI", overallROI);
            }
            
            final RenderedImage postProcessed = footprintBehavior.postProcessMosaic(mosaic, overallROI,localHints);
    
            // prepare for next step
            if(hasAlpha || doInputTransparency){
                return new MosaicElement(new ImageWorker(postProcessed).retainLastBand().getPlanarImage(), overallROI, postProcessed, Utils.mergePamDatasets(pams));
             } else {
                return new MosaicElement(null, overallROI, postProcessed, Utils.mergePamDatasets(pams));
             }            
            
        }

        private ROI mosaicROIs(ROI[] inputROIArray) {
            if (inputROIArray == null || inputROIArray.length == 0) {
                return null;
            }

            List<ROI> rois = new ArrayList<ROI>();
            for (ROI roi : inputROIArray) {
                if (roi != null) {
                    rois.add(roi);
                }
            }

            int roiCount = rois.size();
            if (roiCount == 0) {
                return null;
            } else if (roiCount == 1) {
                return rois.get(0);
            } else {
                PlanarImage[] images = new PlanarImage[rois.size()];
                int i = 0;
                for (ROI roi : rois) {
                    images[i++] = roi.getAsImage();
                }
                ROI[] roisArray = (ROI[]) rois.toArray(new ROI[rois.size()]);
                RenderedOp overallROI = MosaicDescriptor.create(images,
                        MosaicDescriptor.MOSAIC_TYPE_OVERLAY, null, roisArray,
                        new double[][] { { 1.0 } }, new double[] { 0.0 }, hints);
                return new ROI(overallROI);
            }
        }
    }

    /**
     * This class is responsible for putting together the granules for the final mosaic.
     * 
     * @author Simone Giannecchini, GeoSolutions SAS
     * 
     */
    private class MosaicProducer implements GranuleCatalogVisitor {

        /** The number of granules actually dispatched to the internal collectors.*/
        private int granulesNumber;

        /** The {@link MergeBehavior} indicated into the request.*/
        private MergeBehavior mergeBehavior;

        /** The internal collectors for incoming granules. */
        private List<GranuleCollector> granuleCollectors = new ArrayList<GranuleCollector>();

        /**
         * Default {@link Constructor}
         */
        private MosaicProducer() {
            this(false);
        }

        /**
         * {@link MosaicProducer} constructor.
         * It can be used to specify that we want to perform a dry run just to count the
         * granules we would load with the specified query.
         * 
         * <p>
         * A dry run means: no tasks are executed.
         * 
         * @param dryRun <code>true</code> for a dry run, <code>false</code> otherwise.
         */
        private MosaicProducer(final boolean dryRun) {

            // get merge behavior as per request
            mergeBehavior = request.getMergeBehavior();

            // prepare dimensions management if needed, that is in case we use stacking
            if (mergeBehavior.equals(MergeBehavior.STACK)) {

                // create filter to filter results
                // === Custom Domains Management
                final Map<String, List> requestedAdditionalDomains = request.getRequestedAdditionalDomains();
                if (!requestedAdditionalDomains.isEmpty()) {
                    Set<Entry<String, List>> entries = requestedAdditionalDomains.entrySet();

                    // Preliminary check on additional domains specification
                    // we can't do stack in case there are multiple values selections for more than one domain 
                    checkMultipleSelection(entries);

                    // Prepare filtering
                    Entry<String, List> multipleSelectionEntry = null;
                    final List<Filter> filters = new ArrayList<Filter>(entries.size());

                    // Loop over the additional domains
                    for (Entry<String, List> entry: entries) {
                        if (entry.getValue().size() > 1) {
                            // take note of the entry containing multiple values
                            multipleSelectionEntry = entry;
                        } else {
                            // create single value domain filter
                            String domainName = entry.getKey() + DomainDescriptor.DOMAIN_SUFFIX;
                            filters.add(rasterManager.domainsManager.createFilter(domainName, Arrays.asList(entry.getValue())));
                        }
                    }

                    // Anding all filters together
                    Filter andFilter = filters.size() > 0 ? FeatureUtilities.DEFAULT_FILTER_FACTORY.and(filters) : null;

                    if (multipleSelectionEntry == null) {
                        // Simpler case... no multiple selections. All filter have already been combined
                        granuleCollectors.add(new GranuleCollector(andFilter, dryRun));
                    } else {
                        final String domainName = multipleSelectionEntry.getKey() + DomainDescriptor.DOMAIN_SUFFIX;

                        // Need to loop over the multiple values of a custom domains
                        final List values = (List) multipleSelectionEntry.getValue();
                        for (Object o : values) {

                            // create a filter for this value
                            Filter valueFilter = rasterManager.domainsManager.createFilter(domainName, Arrays.asList(o));

                            // combine that filter with the previously merged ones
                            Filter combinedFilter = andFilter == null ? valueFilter : FeatureUtilities.DEFAULT_FILTER_FACTORY.and(andFilter, valueFilter);
                            granuleCollectors.add(new GranuleCollector(combinedFilter, dryRun));
                        }
                    }
                }
            }

            // we don't stack them, either because we are not asked to or because we don't need to although
            // we were asked
            // let's use a default marker
            if (granuleCollectors.isEmpty()) {
                granuleCollectors.add(new GranuleCollector(Filter.INCLUDE, dryRun));
            }
        }

        /**
         * Check whether the specified custom domains contain multiple selection. That case isn't supported
         * so we will throw an exception
         * 
         * @param entries
         */
        private void checkMultipleSelection(Set<Entry<String, List>> entries) {
            int multipleDimensionsSelections = 0;
            for (Entry<String, List> entry: entries) {
                if (entry.getValue().size() > 1) {
                    multipleDimensionsSelections++;
                    if (multipleDimensionsSelections > 1) {
                        throw new IllegalStateException("Unable to handle dimensions stacking for more than 1 dimension");
                    }
                }
            }
        }

        /**
         * This method accepts incming granules and dispatch them to 
         * the correct {@link GranuleCollector} depending on the internal {@link Filter} 
         * per the dimension.
         * 
         * <p>
         * If not {@link MergeBehavior#STACK}ing is required, we collect them all together
         * with an include filter.
         */
        public void visit(GranuleDescriptor granuleDescriptor, Object o) {

            //
            // load raster data
            //
            // create a granuleDescriptor loader
            final Geometry bb = JTS.toGeometry((BoundingBox) mosaicBBox);
            final Geometry inclusionGeometry = granuleDescriptor.getFootprint();
            if (!footprintBehavior.handleFootprints() || inclusionGeometry == null || (footprintBehavior.handleFootprints() && inclusionGeometry.intersects(bb))) {

                // find the right filter for this granule
                boolean found = false;
                for (GranuleCollector collector : granuleCollectors) {
                    if (collector.accept(granuleDescriptor)) {
                        granulesNumber++;
                        found = true;
                        break;
                    }
                }
                
                // did we find a place for it?
                if (!found) {
                    throw new IllegalStateException("Unable to locate a filter for this granule:\n" + granuleDescriptor.toString()); 
                }

            } else {
                if(LOGGER.isLoggable(Level.FINE)){
                    LOGGER.fine("We rejected for non ROI inclusion the granule "+granuleDescriptor.toString());
                }
            }
        }
        
        /**
         * This method is responsible for producing the final mosaic.
         * 
         * <p>
         * Depending on whether or not a {@link MergeBehavior#STACK}ing is required, we perform 1 or 2 steps.
         * <ol>
         *      <li> step 1 is for merging flat on each value for the dimension</li>
         *      <li> step 2 is for merging stack on the resulting mosaics</li>
         * </ol>
         * @return
         * @throws IOException
         */
        private MosaicOutput produce() throws IOException{
            // checks
            if (granulesNumber == 0) {
                LOGGER.log(Level.FINE, "Unable to load any granuleDescriptor");     
                return null;
            }
            
            // STEP 1 collect all the mosaics from each single dimension
            LOGGER.fine("Producing the final mosaic, step 1, loop through granule collectors");   
            final List<MosaicElement> mosaicInputs = new ArrayList<RasterLayerResponse.MosaicElement>();
            GranuleCollector first = null; // we take this apart to steal some val
            int size = granuleCollectors.size();
            for (GranuleCollector collector : granuleCollectors) {
                if(LOGGER.isLoggable(Level.FINE)) {
                    LOGGER.fine("Using collector with filter:" + collector.granuleFilter.toString());
                }
                final MosaicElement preparedMosaic = new Mosaicker(collector.collectGranules(), MergeBehavior.FLAT).createMosaic();
                if(preparedMosaic != null) {
                    mosaicInputs.add(preparedMosaic);
                    if (first == null) {
                        first = collector;
                    }
                }else{
                    // we were not able to mosaic these granules, e.g. we have ROIs and the requested area
                    // fell outside the ROI
                    size--;
                }
            }
            LOGGER.fine("Producing the final mosaic, step 2, final mosaicking"); 
            // optimization 
            if (size == 1) {
                // we don't need to mosaick again
                return new MosaicOutput(mosaicInputs.get(0));
            }
            // no mosaics produced, it might happen, see above
            if(size==0){
                return null;
            }
            
            MosaicInputs mosaickingInputs = new MosaicInputs(
                    first.doInputTransparency, 
                    first.hasAlpha, 
                    mosaicInputs, 
                    first.sourceThreshold);
            // normal situan
            return new MosaicOutput(new Mosaicker(mosaickingInputs, mergeBehavior).createMosaic());
        }
    }

    /** Logger. */
	private final static Logger LOGGER = org.geotools.util.logging.Logging.getLogger(RasterLayerResponse.class);
	
	/** The GridCoverage produced after a {@link #createResponse()} method call */
	private GridCoverage2D gridCoverage;

	/** The {@link RasterLayerRequest} originating this response */
	private RasterLayerRequest request;

	/** The coverage factory producing a {@link GridCoverage} from an image */
	private GridCoverageFactory coverageFactory;

	/** The base envelope related to the input coverage */
	private GeneralEnvelope coverageEnvelope;

	private RasterManager rasterManager;

	private Color finalTransparentColor;

	private ReferencedEnvelope mosaicBBox;

	private Rectangle rasterBounds;

	private MathTransform2D finalGridToWorldCorner;

	private MathTransform2D finalWorldToGridCorner;

	private int imageChoice=0;

	private ImageReadParam baseReadParameters= new ImageReadParam();

	private boolean multithreadingAllowed;
	
	private FootprintBehavior footprintBehavior = FootprintBehavior.None;
	
	private int defaultArtifactsFilterThreshold = Integer.MIN_VALUE;
	
	private double artifactsFilterPTileThreshold = ImageMosaicFormat.DEFAULT_ARTIFACTS_FILTER_PTILE_THRESHOLD;
	
	private boolean oversampledRequest;

	private MathTransform baseGridToWorld;
	
	private Interpolation interpolation;
	
	private boolean needsReprojection;

	private double[] backgroundValues;
	
	private Hints hints;
	
	private String granulesPaths;
	
	/**
	 * Construct a {@code RasterLayerResponse} given a specific
	 * {@link RasterLayerRequest}, a {@code GridCoverageFactory} to produce
	 * {@code GridCoverage}s and an {@code ImageReaderSpi} to be used for
	 * instantiating an Image Reader for a read operation,
	 * 
	 * @param request
	 *            a {@link RasterLayerRequest} originating this response.
	 * @param coverageFactory
	 *            a {@code GridCoverageFactory} to produce a {@code
	 *            GridCoverage} when calling the {@link #createResponse()} method.
	 * @param readerSpi
	 *            the Image Reader Service provider interface.
	 */
	public RasterLayerResponse(final RasterLayerRequest request,
			final RasterManager rasterManager) {
		this.request = request;
		coverageEnvelope = rasterManager.spatialDomainManager.coverageEnvelope;
		this.coverageFactory = rasterManager.getCoverageFactory();
		this.rasterManager = rasterManager;
		this.hints = rasterManager.getHints();
		baseGridToWorld=rasterManager.spatialDomainManager.coverageGridToWorld2D;
		finalTransparentColor=request.getOutputTransparentColor();
		// are we doing multithreading?
		multithreadingAllowed= request.isMultithreadingAllowed();
		footprintBehavior = request.getFootprintBehavior();
		backgroundValues = request.getBackgroundValues();
		interpolation = request.getInterpolation();
		needsReprojection = request.spatialRequestHelper.isNeedsReprojection();
		defaultArtifactsFilterThreshold = request.getDefaultArtifactsFilterThreshold();
		artifactsFilterPTileThreshold = request.getArtifactsFilterPTileThreshold();
	}

	/**
	 * Compute the coverage request and produce a grid coverage which will be
	 * returned by {@link #createResponse()}. The produced grid coverage may be
	 * {@code null} in case of empty request.
	 * 
	 * @return the {@link GridCoverage} produced as computation of this response
	 *         using the {@link #createResponse()} method.
	 * @throws IOException
	 * @uml.property name="gridCoverage"
	 */
	public GridCoverage2D createResponse() throws IOException {
		processRequest();
		return gridCoverage;
	}

	/**
	 * @return the {@link RasterLayerRequest} originating this response.
	 * 
	 * @uml.property name="request"
	 */
	public RasterLayerRequest getOriginatingCoverageRequest() {
		return request;
	}

    /**
     * This method creates the GridCoverage2D from the underlying file given a specified envelope, and a requested dimension.
     * 
     * @param iUseJAI specify if the underlying read process should leverage on a JAI ImageRead operation or a simple direct call to the {@code read}
     *        method of a proper {@code ImageReader}.
     * @param overviewPolicy the overview policy which need to be adopted
     * @return a {@code GridCoverage}
     * 
     * @throws java.io.IOException
     */
    private void processRequest() throws IOException {

        if (request.isEmpty()) {
            if (LOGGER.isLoggable(Level.FINE)){
                LOGGER.log(Level.FINE, "Request is empty: " + request.toString());
            }
            this.gridCoverage = null;
            return;
        }

        // assemble granules
        final MosaicOutput mosaic = prepareResponse();
        if (mosaic == null || mosaic.image == null) {
            this.gridCoverage = null;
            return;
        }

        // postproc
        MosaicOutput finalMosaic = postProcessRaster(mosaic);
        // create the coverage
        gridCoverage = prepareCoverage(finalMosaic);

    }

	private MosaicOutput postProcessRaster(MosaicOutput mosaickedImage) {
		// alpha on the final mosaic
		if (finalTransparentColor != null) {
			if (LOGGER.isLoggable(Level.FINE)){
			    LOGGER.fine("Support for alpha on final mosaic");
			}
			return new MosaicOutput(new ImageWorker(mosaickedImage.image).makeColorTransparent(finalTransparentColor).getRenderedImage()
			        ,mosaickedImage.pamDataset);

		}
		if (!needsReprojection){
		    try {
		        
		        // creating source grid to world corrected to the pixel corner
                        final AffineTransform sourceGridToWorld = new AffineTransform((AffineTransform) finalGridToWorldCorner);
		        
		        // target world to grid at the corner
                        final AffineTransform targetGridToWorld = new AffineTransform(
                        		request.spatialRequestHelper.isNeedsReprojection()?
                        				request.spatialRequestHelper.getComputedGridToWorld():
                        		request.spatialRequestHelper.getComputedGridToWorld());
                        targetGridToWorld.concatenate(CoverageUtilities.CENTER_TO_CORNER);
                        
                        // target world to grid at the corner
                        final AffineTransform targetWorldToGrid=targetGridToWorld.createInverse();
                        // final complete transformation
                        targetWorldToGrid.concatenate(sourceGridToWorld);
                        
                        //update final grid to world
                        finalGridToWorldCorner=new AffineTransform2D(targetGridToWorld);
                        //
                        // Check and see if the affine transform is doing a copy.
                        // If so call the copy operation.
                        //
                        // we are in raster space here, so 1E-3 is safe
                        if(XAffineTransform.isIdentity(targetWorldToGrid, Utils.AFFINE_IDENTITY_EPS))
                            return mosaickedImage;
		        
		        // create final image
		        //
                        // In case we are asked to use certain tile dimensions we tile
                        // also at this stage in case the read type is Direct since
                        // buffered images comes up untiled and this can affect the
                        // performances of the subsequent affine operation.
                        //
                        final Hints localHints= new Hints(hints);
                        if (hints != null && !hints.containsKey(JAI.KEY_BORDER_EXTENDER)) {
                            final Object extender = hints.get(JAI.KEY_BORDER_EXTENDER);
                            if (!(extender != null && extender instanceof BorderExtender)) {
                                localHints.add(ImageUtilities.EXTEND_BORDER_BY_COPYING);
                            }
                        }
                        
                        ImageWorker iw = new ImageWorker(mosaickedImage.image);
                        iw.setRenderingHints(localHints);
                        iw.affine(targetWorldToGrid, interpolation, backgroundValues);
                        mosaickedImage.image = iw.getRenderedImage();
                    } catch (NoninvertibleTransformException e) {
                        if (LOGGER.isLoggable(Level.SEVERE)){
                            LOGGER.log(Level.SEVERE, "Unable to create the requested mosaic ", e );
                        }
                }
            }
            return mosaickedImage;
        }

    /**
     * This method loads the granules which overlap the requested {@link GeneralEnvelope} using the provided values for alpha and input ROI.
     * 
     * @return
     * @throws DataSourceException
     */
    private MosaicOutput prepareResponse() throws DataSourceException {

        try {
            //=== select overview
            chooseOverview();	

            // === extract bbox
            initBBOX();

            // === init transformations
            initTransformations();

            // === init raster bounds
            initRasterBounds();

            // === create query and basic BBOX filtering
            final Query query = initQuery();

            // === manage additional filters
            handleAdditionalFilters(query);

            // === sort by clause
            handleSortByClause(query);

            // === collect granules
            final MosaicProducer visitor = new MosaicProducer();
            rasterManager.getGranuleDescriptors(query, visitor);   
            
            // get those granules and create the final mosaic
            MosaicOutput returnValue = visitor.produce();

            //
            // Did we actually load anything?? Notice that it might happen that
            // either we have holes inside the definition area for the mosaic
            // or we had some problem with missing tiles, therefore it might
            // happen that for some bboxes we don't have anything to load.
            //

            //
            // Create the mosaic image by doing a crop if necessary and also
            // managing the transparent color if applicable. Be aware that
            // management of the transparent color involves removing
            // transparency information from the input images.
            //
            if (returnValue != null) {
                if (LOGGER.isLoggable(Level.FINE)) {
                    LOGGER.fine("Loaded bbox " + mosaicBBox.toString() + " while crop bbox "
                            + request.spatialRequestHelper.getComputedBBox().toString());
                }
                return returnValue;
            }

            // Redo the query without filter to check whether we got no granules due
            // to a filter. In that case we need to return null
            // Notice that we are using a dryRun visitor to make sure we don't
            // spawn any loading tasks, we also ensure we get only 1 feature at most
            // to make this blazing fast
            LOGGER.fine("We got no granules, let's do a dry run with no filters");
            final MosaicProducer dryRunVisitor = new MosaicProducer(true);
            final Utils.BBOXFilterExtractor bboxExtractor = new Utils.BBOXFilterExtractor();
            query.getFilter().accept(bboxExtractor, null);
            query.setFilter(FeatureUtilities.DEFAULT_FILTER_FACTORY.bbox(
                    FeatureUtilities.DEFAULT_FILTER_FACTORY.property(
                            rasterManager.getGranuleCatalog().getType(rasterManager.getTypeName()).getGeometryDescriptor().getName()),
                    bboxExtractor.getBBox()));
            query.setMaxFeatures(1);
            rasterManager.getGranuleDescriptors(query, dryRunVisitor);
            if (dryRunVisitor.granulesNumber > 0) {
                LOGGER.fine("Dry run got a target granule, returning null as the additional filters did filter all the granules out");
                // It means the previous lack of granule was due to a filter excluding all the results. Then we return null
                return null;
            }

            // prepare a blank response
            return createBlankResponse();

        } catch (Exception e) {
            throw new DataSourceException("Unable to create this mosaic", e);
        }
    }

    /**
     * This method is responsible for computing the raster bounds
     * of the final mosaic.
     * @throws TransformException In case transformation fails during the process.
     */
    private void initRasterBounds() throws TransformException {
        final GeneralEnvelope tempRasterBounds = CRS.transform(finalWorldToGridCorner, mosaicBBox);
        rasterBounds=tempRasterBounds.toRectangle2D().getBounds();
        
        
//       SG using the above may lead to problems since the reason is that  may be a little (1 px) bigger
//       than what we need. The code below is a bit better since it uses a proper logic (see GridEnvelope
//       Javadoc)
//      rasterBounds = new GridEnvelope2D(new Envelope2D(tempRasterBounds), PixelInCell.CELL_CORNER);
        if (rasterBounds.width == 0)
            rasterBounds.width++;
        if (rasterBounds.height == 0)
            rasterBounds.height++;
        if(oversampledRequest)
            rasterBounds.grow(2, 2);  
    }

    /**
     * This method is responsible for initializing transformations g2w and back
     * 
     * @throws Exception in case we don't manage to instantiate some of them.
     */
    private void initTransformations() throws Exception {
        //compute final world to grid
        // base grid to world for the center of pixels
        final AffineTransform g2w;
        if (!request.isHeterogeneousGranules()) {
            final OverviewLevel baseLevel = rasterManager.overviewsController.resolutionsLevels.get(0);
            final OverviewLevel selectedLevel = rasterManager.overviewsController.resolutionsLevels.get(imageChoice);
            final double resX = baseLevel.resolutionX;
            final double resY = baseLevel.resolutionY;
            final double[] requestRes = request.spatialRequestHelper.getComputedResolution();

            g2w = new AffineTransform((AffineTransform) baseGridToWorld);
            g2w.concatenate(CoverageUtilities.CENTER_TO_CORNER);

            if ((requestRes[0] < resX || requestRes[1] < resY)) {
                // Using the best available resolution
                oversampledRequest = true;
            } else {

                // SG going back to working on a per level basis to do the composition
                // g2w = new AffineTransform(request.getRequestedGridToWorld());
                g2w.concatenate(AffineTransform.getScaleInstance(selectedLevel.scaleFactor, selectedLevel.scaleFactor));
                g2w.concatenate(AffineTransform.getScaleInstance(
                        baseReadParameters.getSourceXSubsampling(),
                        baseReadParameters.getSourceYSubsampling()));
            }
        } else {
            g2w = new AffineTransform(
            		request.spatialRequestHelper.isNeedsReprojection()?
            				request.spatialRequestHelper.getComputedGridToWorld():
            		request.spatialRequestHelper.getComputedGridToWorld());
            g2w.concatenate(CoverageUtilities.CENTER_TO_CORNER);
        }
        // move it to the corner
        finalGridToWorldCorner = new AffineTransform2D(g2w);
        finalWorldToGridCorner = finalGridToWorldCorner.inverse();// compute raster bounds

    }

    /**
     * This method is responsible for initializing the bbox for the
     * mosaic produced by this response.
     * 
     */
    private void initBBOX() {
        // ok we got something to return, let's load records from the index
        final BoundingBox cropBBOX = request.spatialRequestHelper.getComputedBBox();
        if (cropBBOX != null){
            mosaicBBox = ReferencedEnvelope.reference(cropBBOX);
        }else{
            mosaicBBox = new ReferencedEnvelope(coverageEnvelope);
        }
        
    }

    /**
     * This method encloses the standard behavior for the selection of the
     * proper overview level.
     * 
     * See {@link ReadParamsController}
     */
    private void chooseOverview() throws IOException, TransformException {
        //
        // prepare the params for executing a mosaic operation.
        //
        // It might important to set the mosaic type to blend otherwise
        // sometimes strange results jump in.

        // select the relevant overview, notice that at this time we have
        // relaxed a bit the requirement to have the same exact resolution
        // for all the overviews, but still we do not allow for reading the
        // various grid to world transform directly from the input files,
        // therefore we are assuming that each granuleDescriptor has a scale and
        // translate only grid to world that can be deduced from its base
        // level dimension and envelope. The grid to world transforms for
        // the other levels can be computed accordingly knowing the scale
        // factors.            
        if (request.spatialRequestHelper.getComputedBBox() != null && request.spatialRequestHelper.getComputedRasterArea() != null && !request.isHeterogeneousGranules()){
            imageChoice = ReadParamsController.setReadParams(
                    request.spatialRequestHelper.getComputedResolution(),
                    request.getOverviewPolicy(),
                    request.getDecimationPolicy(),
                    baseReadParameters,
                    request.rasterManager,
                    request.rasterManager.overviewsController); // use general overviews controller
        } else {
            imageChoice = 0;
        }
        assert imageChoice>=0;
        if (LOGGER.isLoggable(Level.FINE)) {
            LOGGER.fine(new StringBuilder("Loading level ").append(imageChoice)
                    .append(" with subsampling factors ")
                    .append(baseReadParameters.getSourceXSubsampling()).append(" ")
                    .append(baseReadParameters.getSourceYSubsampling()).toString());
        }
    }

    /**
     * This method is responsible for initializing the {@link Query} object with the BBOX filter as per the incoming
     * {@link RasterLayerRequest}.
     * 
     * @return a {@link Query} object with the BBOX {@link Filter} in it.
     * @throws IOException in case something bad happens
     */
    private Query initQuery() throws Exception {
        final GeneralEnvelope levelRasterArea_ = CRS.transform(finalWorldToGridCorner, rasterManager.spatialDomainManager.coverageBBox);
        final GridEnvelope2D levelRasterArea = new GridEnvelope2D(new Envelope2D(levelRasterArea_), PixelInCell.CELL_CORNER);
        XRectangle2D.intersect(levelRasterArea, rasterBounds, rasterBounds);
        final String typeName = rasterManager.getTypeName();
        Filter bbox = null;
        if (typeName != null){
            Query query = new Query(typeName);
            // max number of elements
            if(request.getMaximumNumberOfGranules()>0){
                query.setMaxFeatures(request.getMaximumNumberOfGranules());
            }            
            bbox = FeatureUtilities.DEFAULT_FILTER_FACTORY.bbox(
                    FeatureUtilities.DEFAULT_FILTER_FACTORY.property(rasterManager.getGranuleCatalog().getType(typeName).getGeometryDescriptor().getName()),
                    mosaicBBox);
            query.setFilter( bbox);
            return query;
        } else {
                throw new IllegalStateException("GranuleCatalog feature type was null!!!");
        }
    }

    /**
     * This method is responsible for creating the filters needed for addtional dimensions like
     * TIME, ELEVATION additional Domains
     * @param query the {@link Query} to set filters for.
     */
    private void handleAdditionalFilters(Query query) {
        final List times = request.getRequestedTimes();
        final List elevations=request.getElevation();
        final Map<String, List> additionalDomains = request.getRequestedAdditionalDomains();
        final Filter filter = request.getFilter();
        final boolean hasTime=(times!=null&&times.size()>0);
        final boolean hasElevation=(elevations!=null && elevations.size()>0);
        final boolean hasAdditionalDomains = additionalDomains.size() > 0;
        final boolean hasFilter = filter != null && !Filter.INCLUDE.equals(filter);
        // prepare eventual filter for filtering granules
        // handle elevation indexing first since we then combine this with the max in case we are asking for current in time
        if (hasElevation) {            
            final Filter elevationF = rasterManager.elevationDomainManager.createFilter(
                    GridCoverage2DReader.ELEVATION_DOMAIN, elevations);
            query.setFilter(FeatureUtilities.DEFAULT_FILTER_FACTORY.and(query.getFilter(), elevationF));
        }

        // handle generic filter since we then combine this with the max in case we are asking for current in time
        if (hasFilter) {
            query.setFilter(FeatureUtilities.DEFAULT_FILTER_FACTORY.and(query.getFilter(), filter));
        }

        // fuse time query with the bbox query
        if (hasTime) {
            final Filter timeFilter = this.rasterManager.timeDomainManager.createFilter(GridCoverage2DReader.TIME_DOMAIN, times);
            query.setFilter(FeatureUtilities.DEFAULT_FILTER_FACTORY.and(query.getFilter(),timeFilter));
        }

        // === Custom Domains Management
        if (hasAdditionalDomains) {
            final List<Filter> additionalFilter = new ArrayList<Filter>();
            for (Entry<String, List> entry : additionalDomains.entrySet()) {

                // build a filter for each dimension
                final String domainName = entry.getKey()+DomainDescriptor.DOMAIN_SUFFIX;
                additionalFilter.add(rasterManager.domainsManager.createFilter(domainName, (List) entry.getValue()));

            }
            // merge with existing ones
            query.setFilter(FeatureUtilities.DEFAULT_FILTER_FACTORY.and(query.getFilter(),FeatureUtilities.DEFAULT_FILTER_FACTORY.and(additionalFilter)));
        }       
    }

    /**
     * Handles the optional {@link SortBy} clause for the query to the catalog
     * 
     * @param query the {@link Query} to set the {@link SortBy} for.
     * 
     */
    private void handleSortByClause(final Query query) {
        Utilities.ensureNonNull("query", query);
        LOGGER.fine("Prepping to manage SortBy Clause");
        final String sortByClause = request.getSortClause();
        if (sortByClause != null && sortByClause.length() > 0) {
            final String[] elements = sortByClause.split(",");
            if (elements != null && elements.length > 0) {
                final List<SortBy> clauses = new ArrayList<SortBy>(elements.length);
                for (String element : elements) {
                    // check
                    if (element == null || element.length() <= 0) {
                        continue;// next, please!
                    }
                    try {
                        // which clause?
                        // ASCENDING
                        element = element.trim();
                        if (element.endsWith(Utils.ASCENDING_ORDER_IDENTIFIER)) {
                            String attribute = element.substring(0, element.length() - 2);
                            clauses.add(new SortByImpl(FeatureUtilities.DEFAULT_FILTER_FACTORY.property(attribute), SortOrder.ASCENDING));
                            LOGGER.fine("Added clause ASCENDING on attribute:" + attribute);
                        } else
                        // DESCENDING
                        if (element.contains(Utils.DESCENDING_ORDER_IDENTIFIER)) {
                            String attribute = element.substring(0, element.length() - 2);
                            clauses.add(new SortByImpl(FeatureUtilities.DEFAULT_FILTER_FACTORY.property(attribute), SortOrder.DESCENDING));
                            LOGGER.fine("Added clause DESCENDING on attribute:" + attribute);
                        } else {
                            LOGGER.fine("Ignoring sort clause :" + element);
                        }
                    } catch (Exception e) {
                        if (LOGGER.isLoggable(Level.INFO)) {
                            LOGGER.log(Level.INFO, e.getLocalizedMessage(), e);
                        }
                    }
                }

                // assign to query if sorting is supported!
                final SortBy[] sb = clauses.toArray(new SortBy[] {});
                if (rasterManager.getGranuleCatalog().getQueryCapabilities(rasterManager.getTypeName()).supportsSorting(sb)) {
                    query.setSortBy(sb);
                }
            } else {
                LOGGER.fine("No SortBy Clause");
            }
        }
    }

    /**
     * This method is responsible for creating a blank image as a reponse to the query as it seems
     * we got a no data area.
     * 
     * @return a blank {@link RenderedImage} initialized using the background values
     */
    private MosaicOutput createBlankResponse() {
        // if we get here that means that we do not have anything to load
        // but still we are inside the definition area for the mosaic,
        // therefore we create a fake coverage using the background values,
        // if provided (defaulting to 0), as well as the compute raster
        // bounds, envelope and grid to world.
        LOGGER.fine("Creating constant image for area with no data");
        
        final ImageLayout2 il= new ImageLayout2();
        il.setColorModel(rasterManager.defaultCM);
        Dimension tileSize= request.getTileDimensions();
        if(tileSize==null){
            tileSize=JAI.getDefaultTileSize();
        } 

        il.setTileGridXOffset(0).setTileGridYOffset(0).setTileWidth((int)tileSize.getWidth()).setTileHeight((int)tileSize.getHeight());
        final RenderingHints renderingHints = new RenderingHints(JAI.KEY_IMAGE_LAYOUT,il);
        
        
        final Number[] values = ImageUtilities.getBackgroundValues(rasterManager.defaultSM,
                backgroundValues);
        RenderedImage finalImage;
        if (ImageUtilities.isMediaLibAvailable()) {
            // create a constant image with a proper layout
            finalImage = ConstantDescriptor.create(Float.valueOf(rasterBounds.width),
                    Float.valueOf(rasterBounds.height), values, renderingHints);
            if (rasterBounds.x != 0 || rasterBounds.y != 0) {
                finalImage = TranslateDescriptor.create(finalImage, Float.valueOf(rasterBounds.x),
                        Float.valueOf(rasterBounds.y),
                        Interpolation.getInstance(Interpolation.INTERP_NEAREST), null);
            }

            // impose the color model and samplemodel as the constant operation does not take them
            // into account!
            if (rasterManager.defaultCM != null) {
                il.setColorModel(rasterManager.defaultCM);
                il.setSampleModel(rasterManager.defaultCM.createCompatibleSampleModel(
                        tileSize.width, tileSize.height));
                finalImage = FormatDescriptor.create(finalImage,
                        Integer.valueOf(il.getSampleModel(null).getDataType()), renderingHints);
            }
        } else {
            il.setWidth(rasterBounds.width).setHeight(rasterBounds.height);
            if (rasterBounds.x != 0 || rasterBounds.y != 0) {
                il.setMinX(rasterBounds.x).setMinY(rasterBounds.y);
            }
            // impose the color model and samplemodel as the constant operation does not take them
            // into account!
            if (rasterManager.defaultCM != null) {
                il.setColorModel(rasterManager.defaultCM);
                il.setSampleModel(rasterManager.defaultCM.createCompatibleSampleModel(
                        tileSize.width, tileSize.height));

            }
            final double[] bkgValues = new double[values.length];
            for (int i = 0; i < values.length; i++) {
                bkgValues[i] = values[i].doubleValue();
            }
            Assert.isTrue(il.isValid(ImageLayout.WIDTH_MASK | ImageLayout.HEIGHT_MASK
                    | ImageLayout.SAMPLE_MODEL_MASK));
            finalImage = MosaicDescriptor.create(new RenderedImage[0],
                    MosaicDescriptor.MOSAIC_TYPE_OVERLAY, null, null,
                    new double[][] { { CoverageUtilities.getMosaicThreshold(il.getSampleModel(null)
                            .getDataType()) } }, bkgValues, renderingHints);
        }
        if (footprintBehavior != null) {
            finalImage = footprintBehavior.postProcessBlankResponse(finalImage, renderingHints);
        }
        
        return new MosaicOutput(finalImage, null);
    }

	/**
	 * This method is responsible for creating a coverage from the supplied {@link RenderedImage}.
	 * 
	 * @param image
	 * @return
	 * @throws IOException
	 */
	private GridCoverage2D prepareCoverage(MosaicOutput mosaicOutput) throws IOException {
		
		// creating bands
	    final RenderedImage image = mosaicOutput.image;
                final SampleModel sm=image.getSampleModel();
                final ColorModel cm=image.getColorModel();
		final int numBands = sm.getNumBands();
		final GridSampleDimension[] bands = new GridSampleDimension[numBands];
	        Set<String> bandNames = new HashSet<String>();		
		// setting bands names.
		for (int i = 0; i < numBands; i++) {
		        ColorInterpretation colorInterpretation=null;
		        String bandName=null;
		        if(cm!=null){
	                        // === color interpretation
		                colorInterpretation=TypeMap.getColorInterpretation(cm, i);
                	        if(colorInterpretation==null){
                	            throw new IOException("Unrecognized sample dimension type");
                	        }
                	        
                	        bandName = colorInterpretation.name();
                                if(colorInterpretation == ColorInterpretation.UNDEFINED || bandNames.contains(bandName)) {// make sure we create no duplicate band names
                                    bandName = "Band" + (i + 1);
                                }
        	        } else { // no color model
        	            bandName = "Band" + (i + 1);
        	            colorInterpretation=ColorInterpretation.UNDEFINED;
        	        }
 	        
	        
	        // sample dimension type
	        final SampleDimensionType st=TypeMap.getSampleDimensionType(sm, i);
		    
	        // set some no data values, as well as Min and Max values
	        final double noData;
	        double min=-Double.MAX_VALUE,max=Double.MAX_VALUE;
	        if(backgroundValues!=null)
	        {
	        	// sometimes background values are not specified as 1 per each band, therefore we need to be careful
	        	noData= backgroundValues[backgroundValues.length > i ? i:0];
	        }
	        else
	        {
	        	if(st.compareTo(SampleDimensionType.REAL_32BITS)==0)
	        		noData= Float.NaN;
	        	else
	        		if(st.compareTo(SampleDimensionType.REAL_64BITS)==0)
		        		noData= Double.NaN;
	        		else
		        		if(st.compareTo(SampleDimensionType.SIGNED_16BITS)==0)
		        		{
		        			noData=Short.MIN_VALUE;
		        			min=Short.MIN_VALUE;
		        			max=Short.MAX_VALUE;
		        		}
		        		else
		        			if(st.compareTo(SampleDimensionType.SIGNED_32BITS)==0)
		        			{
		        				noData= Integer.MIN_VALUE;

			        			min=Integer.MIN_VALUE;
			        			max=Integer.MAX_VALUE;		        				
		        			}
		        			else
			        			if(st.compareTo(SampleDimensionType.SIGNED_8BITS)==0)
			        			{
			        				noData= -128;
			        				min=-128;
			        				max=127;
			        			}
			        			else
			        			{
			        				//unsigned
				        			noData= 0;
				        			min=0;
				        			
				        			
				        			// compute max
				        			if(st.compareTo(SampleDimensionType.UNSIGNED_1BIT)==0)
				        				max=1;
				        			else
				        				if(st.compareTo(SampleDimensionType.UNSIGNED_2BITS)==0)
				        					max=3;
					        			else
					        				if(st.compareTo(SampleDimensionType.UNSIGNED_4BITS)==0)
					        					max=7;
					        				else
						        				if(st.compareTo(SampleDimensionType.UNSIGNED_8BITS)==0)
						        					max=255;
						        				else
							        				if(st.compareTo(SampleDimensionType.UNSIGNED_16BITS)==0)
							        					max=65535;
							        				else
								        				if(st.compareTo(SampleDimensionType.UNSIGNED_32BITS)==0)
								        					max=Math.pow(2, 32)-1;
				        							        			
			        			}
	        	
		        		     
	        }
	        bands[i] = new SimplifiedGridSampleDimension(
	        		bandName,
	        		st,
	        		colorInterpretation,
	        		noData,
	        		min,
	        		max,
	        		1,							//no scale 
	        		0,							//no offset
	        		null
	        		).geophysics(true);
		}
		
        // creating the final coverage by keeping into account the fact that we
        Map <String, Object> properties = null;
        if (granulesPaths != null) {
            properties = new HashMap<String,Object>();
            properties.put(AbstractGridCoverage2DReader.FILE_SOURCE_PROPERTY, granulesPaths);
        }

        if (mosaicOutput.pamDataset != null) {
            properties.put(Utils.PAM_DATASET, mosaicOutput.pamDataset);
        }
        return coverageFactory.create(
                rasterManager.getCoverageIdentifier(),
                image,
                new GridGeometry2D(
                        new GridEnvelope2D(PlanarImage.wrapRenderedImage(image).getBounds()),
                        PixelInCell.CELL_CORNER,
                        finalGridToWorldCorner,
                        this.mosaicBBox.getCoordinateReferenceSystem(),
                        hints),
                bands,
                null, 
                properties);
    }
}
