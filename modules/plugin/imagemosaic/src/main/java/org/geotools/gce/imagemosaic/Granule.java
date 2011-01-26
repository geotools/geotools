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

import it.geosolutions.imageio.utilities.Utilities;

import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.awt.geom.Area;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.RenderedImage;
import java.io.IOException;
import java.net.URL;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

import javax.imageio.ImageReadParam;
import javax.imageio.ImageReader;
import javax.imageio.spi.ImageReaderSpi;
import javax.imageio.stream.ImageInputStream;
import javax.media.jai.ImageLayout;
import javax.media.jai.Interpolation;
import javax.media.jai.InterpolationNearest;
import javax.media.jai.JAI;
import javax.media.jai.ROIShape;
import javax.media.jai.operator.AffineDescriptor;

import org.geotools.coverage.grid.GridEnvelope2D;
import org.geotools.gce.imagemosaic.RasterLayerResponse.GranuleLoadingResult;
import org.geotools.geometry.DirectPosition2D;
import org.geotools.geometry.GeneralEnvelope;
import org.geotools.geometry.jts.LiteShape2;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.metadata.iso.spatial.PixelTranslation;
import org.geotools.referencing.CRS;
import org.geotools.referencing.operation.builder.GridToEnvelopeMapper;
import org.geotools.referencing.operation.matrix.XAffineTransform;
import org.geotools.referencing.operation.transform.AffineTransform2D;
import org.geotools.referencing.operation.transform.ProjectiveTransform;
import org.geotools.resources.geometry.XRectangle2D;
import org.opengis.geometry.BoundingBox;
import org.opengis.referencing.FactoryException;
import org.opengis.referencing.datum.PixelInCell;
import org.opengis.referencing.operation.MathTransform2D;
import org.opengis.referencing.operation.TransformException;

import com.sun.media.jai.util.Rational;
import com.vividsolutions.jts.geom.Geometry;

/**
 * A granule is a single piece of the mosaic, with its own overviews and
 * everything.
 * 
 * <p>
 * This class is responsible for caching the various size of the different
 * levels of each single granule since computing them each time is expensive
 * (opening a file, looking for a reader, parsing metadata,etc...).
 * 
 * <p>
 * Right now we are making the assumption that a single granule is made a by a
 * single file with embedded overviews, either explicit or intrinsic through wavelets like MrSID,
 * ECW or JPEG2000.
 * 
 * @author Simone Giannecchini, GeoSolutions S.A.S.
 * @author Stefan Alfons Krueger (alfonx), Wikisquare.de : Support for jar:file:foo.jar/bar.properties URLs
 * @since 2.5.5
 */
class Granule {
	
	private static final String DIRECT_KAKADU_PLUGIN="it.geosolutions.imageio.plugins.jp2k.JP2KKakaduImageReader";

	/** Logger. */
	private final static Logger LOGGER = org.geotools.util.logging.Logging.getLogger(Granule.class); 
	   
    // FORMULAE FOR FORWARD MAP are derived as follows
    //     Nearest
    //        Minimum:
    //            srcMin = floor ((dstMin + 0.5 - trans) / scale)
    //            srcMin <= (dstMin + 0.5 - trans) / scale < srcMin + 1
    //            srcMin*scale <= dstMin + 0.5 - trans < (srcMin + 1)*scale
    //            srcMin*scale - 0.5 + trans
    //                       <= dstMin < (srcMin + 1)*scale - 0.5 + trans
    //            Let A = srcMin*scale - 0.5 + trans,
    //            Let B = (srcMin + 1)*scale - 0.5 + trans
    //
    //            dstMin = ceil(A)
    //
    //        Maximum:
    //            Note that srcMax is defined to be srcMin + dimension - 1
    //            srcMax = floor ((dstMax + 0.5 - trans) / scale)
    //            srcMax <= (dstMax + 0.5 - trans) / scale < srcMax + 1
    //            srcMax*scale <= dstMax + 0.5 - trans < (srcMax + 1)*scale
    //            srcMax*scale - 0.5 + trans
    //                       <= dstMax < (srcMax+1) * scale - 0.5 + trans
    //            Let float A = (srcMax + 1) * scale - 0.5 + trans
    //
    //            dstMax = floor(A), if floor(A) < A, else
    //            dstMax = floor(A) - 1
    //            OR dstMax = ceil(A - 1)
    //
    //     Other interpolations
    //
    //        First the source should be shrunk by the padding that is
    //        required for the particular interpolation. Then the
    //        shrunk source should be forward mapped as follows:
    //
    //        Minimum:
    //            srcMin = floor (((dstMin + 0.5 - trans)/scale) - 0.5)
    //            srcMin <= ((dstMin + 0.5 - trans)/scale) - 0.5 < srcMin+1
    //            (srcMin+0.5)*scale <= dstMin+0.5-trans <
    //                                                  (srcMin+1.5)*scale
    //            (srcMin+0.5)*scale - 0.5 + trans
    //                       <= dstMin < (srcMin+1.5)*scale - 0.5 + trans
    //            Let A = (srcMin+0.5)*scale - 0.5 + trans,
    //            Let B = (srcMin+1.5)*scale - 0.5 + trans
    //
    //            dstMin = ceil(A)
    //
    //        Maximum:
    //            srcMax is defined as srcMin + dimension - 1
    //            srcMax = floor (((dstMax + 0.5 - trans) / scale) - 0.5)
    //            srcMax <= ((dstMax + 0.5 - trans)/scale) - 0.5 < srcMax+1
    //            (srcMax+0.5)*scale <= dstMax + 0.5 - trans <
    //                                                   (srcMax+1.5)*scale
    //            (srcMax+0.5)*scale - 0.5 + trans
    //                       <= dstMax < (srcMax+1.5)*scale - 0.5 + trans
    //            Let float A = (srcMax+1.5)*scale - 0.5 + trans
    //
    //            dstMax = floor(A), if floor(A) < A, else
    //            dstMax = floor(A) - 1
    //            OR dstMax = ceil(A - 1)
    //
    

	private static float rationalTolerance = 0.000001F;
    private static Rectangle2D layoutHelper(RenderedImage source,
                                            float scaleX,
                                            float scaleY,
                                            float transX,
                                            float transY,
                                            Interpolation interp) {

        // Represent the scale factors as Rational numbers.
		// Since a value of 1.2 is represented as 1.200001 which
		// throws the forward/backward mapping in certain situations.
		// Convert the scale and translation factors to Rational numbers
		Rational scaleXRational = Rational.approximate(scaleX,rationalTolerance);
		Rational scaleYRational = Rational.approximate(scaleY,rationalTolerance);

		long scaleXRationalNum = (long) scaleXRational.num;
		long scaleXRationalDenom = (long) scaleXRational.denom;
		long scaleYRationalNum = (long) scaleYRational.num;
		long scaleYRationalDenom = (long) scaleYRational.denom;

		Rational transXRational = Rational.approximate(transX,rationalTolerance);
		Rational transYRational = Rational.approximate(transY,rationalTolerance);

		long transXRationalNum = (long) transXRational.num;
		long transXRationalDenom = (long) transXRational.denom;
		long transYRationalNum = (long) transYRational.num;
		long transYRationalDenom = (long) transYRational.denom;

		int x0 = source.getMinX();
		int y0 = source.getMinY();
		int w = source.getWidth();
		int h = source.getHeight();

		// Variables to store the calculated destination upper left coordinate
		long dx0Num, dx0Denom, dy0Num, dy0Denom;

		// Variables to store the calculated destination bottom right
		// coordinate
		long dx1Num, dx1Denom, dy1Num, dy1Denom;

		// Start calculations for destination

		dx0Num = x0;
		dx0Denom = 1;

		dy0Num = y0;
		dy0Denom = 1;

		// Formula requires srcMaxX + 1 = (x0 + w - 1) + 1 = x0 + w
		dx1Num = x0 + w;
		dx1Denom = 1;

		// Formula requires srcMaxY + 1 = (y0 + h - 1) + 1 = y0 + h
		dy1Num = y0 + h;
		dy1Denom = 1;

		dx0Num *= scaleXRationalNum;
		dx0Denom *= scaleXRationalDenom;

		dy0Num *= scaleYRationalNum;
		dy0Denom *= scaleYRationalDenom;

		dx1Num *= scaleXRationalNum;
		dx1Denom *= scaleXRationalDenom;

		dy1Num *= scaleYRationalNum;
		dy1Denom *= scaleYRationalDenom;

		// Equivalent to subtracting 0.5
		dx0Num = 2 * dx0Num - dx0Denom;
		dx0Denom *= 2;

		dy0Num = 2 * dy0Num - dy0Denom;
		dy0Denom *= 2;

		// Equivalent to subtracting 1.5
		dx1Num = 2 * dx1Num - 3 * dx1Denom;
		dx1Denom *= 2;

		dy1Num = 2 * dy1Num - 3 * dy1Denom;
		dy1Denom *= 2;

		// Adding translation factors

		// Equivalent to float dx0 += transX
		dx0Num = dx0Num * transXRationalDenom + transXRationalNum * dx0Denom;
		dx0Denom *= transXRationalDenom;

		// Equivalent to float dy0 += transY
		dy0Num = dy0Num * transYRationalDenom + transYRationalNum * dy0Denom;
		dy0Denom *= transYRationalDenom;

		// Equivalent to float dx1 += transX
		dx1Num = dx1Num * transXRationalDenom + transXRationalNum * dx1Denom;
		dx1Denom *= transXRationalDenom;

		// Equivalent to float dy1 += transY
		dy1Num = dy1Num * transYRationalDenom + transYRationalNum * dy1Denom;
		dy1Denom *= transYRationalDenom;

		// Get the integral coordinates
		int l_x0, l_y0, l_x1, l_y1;

		l_x0 = Rational.ceil(dx0Num, dx0Denom);
		l_y0 = Rational.ceil(dy0Num, dy0Denom);

		l_x1 = Rational.ceil(dx1Num, dx1Denom);
		l_y1 = Rational.ceil(dy1Num, dy1Denom);

		// Set the top left coordinate of the destination
		final Rectangle2D retValue= new Rectangle2D.Double();
		retValue.setFrame(l_x0, l_y0, l_x1 - l_x0 + 1, l_y1 - l_y0 + 1);
		return retValue;
	}
	/**
	 * This class represent an overview level in a single granule.
	 * 
	 * @author Simone Giannecchini, GeoSolutions S.A.S.
	 *
	 */
	class Level{

		final double scaleX;
		
		final double scaleY;
		
		final int width;
		
		final int height;

		final AffineTransform2D baseToLevelTransform;
		
		final AffineTransform2D gridToWorldTransform;

		final Rectangle rasterDimensions;
		
		
		public AffineTransform getBaseToLevelTransform() {
			return baseToLevelTransform;
		}

		public double getScaleX() {
			return scaleX;
		}

		public double getScaleY() {
			return scaleY;
		}

		public int getWidth() {
			return width;
		}

		public int getHeight() {
			return height;
		}

		public Level(final double scaleX,final double scaleY,final int width,final int height) {
			this.scaleX = scaleX;
			this.scaleY = scaleY;
			this.baseToLevelTransform=new AffineTransform2D( XAffineTransform.getScaleInstance(scaleX,scaleY,0,0));
			
			final AffineTransform gridToWorldTransform_ = new AffineTransform(baseToLevelTransform);
			gridToWorldTransform_.preConcatenate(ImageMosaicUtils.CENTER_TO_CORNER);
			gridToWorldTransform_.preConcatenate(baseGridToWorld);
			this.gridToWorldTransform=new AffineTransform2D(gridToWorldTransform_);
			this.width = width;
			this.height = height;
			this.rasterDimensions= new Rectangle(0,0,width,height);
		}

		public Rectangle getBounds() {
			return (Rectangle) rasterDimensions.clone();
		}

		public AffineTransform2D getGridToWorldTransform() {
			return gridToWorldTransform;
		}		
		


		@Override
		public String toString() {
			// build a decent representation for this level
			final StringBuilder buffer = new StringBuilder();
			buffer.append("Description of a granule level").append("\n");
			buffer.append("width:\t\t").append(width).append("\n");
			buffer.append("height:\t\t").append(height).append("\n");
			buffer.append("scaleX:\t\t").append(scaleX).append("\n");
			buffer.append("scaleY:\t\t").append(scaleY).append("\n");
			buffer.append("baseToLevelTransform:\t\t").append(baseToLevelTransform.toString()).append("\n");
			buffer.append("gridToWorldTransform:\t\t").append(gridToWorldTransform.toString()).append("\n");
			return super.toString();
		}
		
	}
	
	ReferencedEnvelope  granuleBBOX;
	
	URL granuleUrl;
	
	ROIShape granuleROIShape;
	
	Geometry inclusionGeometry;
	
	final Map<Integer,Level> granuleLevels= Collections.synchronizedMap(new HashMap<Integer,Level>());
	
	AffineTransform baseGridToWorld;
	
	ImageReaderSpi cachedSPI;

	public Granule(final BoundingBox granuleBBOX,final URL granuleUrl) {
		this(granuleBBOX, granuleUrl, null);
	}
	
	public Granule(final BoundingBox granuleBBOX,final URL granuleUrl, final ImageReaderSpi suggestedSPI) {
                this(granuleBBOX, granuleUrl, suggestedSPI, null);
	}

	
	public Granule(final BoundingBox granuleBBOX,final URL granuleUrl, final ImageReaderSpi suggestedSPI, final Geometry inclusionGeometry) {
		this.granuleBBOX = ReferencedEnvelope.reference(granuleBBOX);
		this.granuleUrl = granuleUrl;
		this.inclusionGeometry = inclusionGeometry;
		
		// create the base grid to world transformation
		ImageInputStream inStream = null;
		ImageReader reader = null;
		try {
			//
			//get info about the raster we have to read
			//
			
			// get a stream
			inStream = ImageMosaicUtils.getInputStream(granuleUrl);
			if(inStream == null)
				throw new IllegalArgumentException("Unable to get an input stream for the provided file "+granuleUrl.toString());
			
			// get a reader and try to cache the suggested SPI first
			if(cachedSPI == null){
				inStream.mark();
				if(suggestedSPI!=null && suggestedSPI.canDecodeInput(inStream))
				{
					cachedSPI=suggestedSPI;
					inStream.reset();
				}
				else{
					inStream.mark();
					reader = ImageMosaicUtils.getReader(inStream);
					if(reader != null)
						cachedSPI = reader.getOriginatingProvider();
					inStream.reset();
				}
				
			}
			reader = cachedSPI.createReaderInstance();
			if(reader == null)
				throw new IllegalArgumentException("Unable to get an ImageReader for the provided file "+granuleUrl.toString());
			
			//get selected level and base level dimensions
			final Rectangle originalDimension = ImageMosaicUtils.getDimension(0,inStream, reader);
			
			// build the g2W for this tile, in principle we should get it
			// somehow from the tile itself or from the index, but at the moment
			// we do not have such info, hence we assume that it is a simple
			// scale and translate
			final GridToEnvelopeMapper geMapper= new GridToEnvelopeMapper(new GridEnvelope2D(originalDimension), granuleBBOX);
			geMapper.setPixelAnchor(PixelInCell.CELL_CENTER);//this is the default behavior but it is nice to write it down anyway
			this.baseGridToWorld = geMapper.createAffineTransform();
	
			try {
			    if (inclusionGeometry != null){
					LiteShape2 shape = null;
					geMapper.setPixelAnchor(PixelInCell.CELL_CORNER);
			        shape = new LiteShape2(inclusionGeometry, geMapper.createTransform().inverse(),null, false);
			        this.granuleROIShape = (ROIShape) new ROIShape(shape);
			    }
        	            
			} catch (TransformException e1) {
			    throw new IllegalArgumentException(e1);
			} catch (FactoryException e1) {
			    throw new IllegalArgumentException(e1);
			}
			
			// add the base level
			this.granuleLevels.put(Integer.valueOf(0), new Level(1, 1, originalDimension.width, originalDimension.height));

		} catch (IllegalStateException e) {
			throw new IllegalArgumentException(e);
			
		} catch (IOException e) {
			throw new IllegalArgumentException(e);
		} finally {
			try{
				if(inStream != null)
					inStream.close();
			}
			catch (Throwable e) {
				throw new IllegalArgumentException(e);
			}
			finally{
				if(reader != null)
					reader.dispose();
			}
		}	
	}
	

	public GranuleLoadingResult loadRaster(
			final ImageReadParam readParameters,
			final int imageIndex, 
			final ReferencedEnvelope cropBBox,
			final MathTransform2D mosaicWorldToGrid,
			final RasterLayerRequest request,
			final Dimension tileDimension) throws IOException {
		
		if (LOGGER.isLoggable(java.util.logging.Level.FINE))
			LOGGER.fine("Loading raster data for granule "+this.toString());

		final ReferencedEnvelope bbox = inclusionGeometry != null? new ReferencedEnvelope(granuleBBOX.intersection(inclusionGeometry.getEnvelopeInternal()), granuleBBOX.getCoordinateReferenceSystem()):granuleBBOX;
		
		// intersection of this tile bound with the current crop bbox
		final ReferencedEnvelope intersection = new ReferencedEnvelope(bbox.intersection(cropBBox), cropBBox.getCoordinateReferenceSystem());
		if (intersection.isEmpty()) {
                    if (LOGGER.isLoggable(java.util.logging.Level.FINE))
                            LOGGER.fine("Got empty intersection for granule "+this.toString()+ " with request "+request.toString());
                    return null;
                }
		
		ImageInputStream inStream=null;
		ImageReader reader=null;
		try {
			//
			//get info about the raster we have to read
			//
			
			// get a stream
			inStream = ImageMosaicUtils.getInputStream(granuleUrl);
			if(inStream==null)
				return null;
	
			// get a reader and try to cache the relevant SPI
			if(cachedSPI==null){
				reader = ImageMosaicUtils.getReader( inStream);
				if(reader!=null)
					cachedSPI=reader.getOriginatingProvider();
			}
			else
				reader=cachedSPI.createReaderInstance();
			if(reader==null)
			{
				if (LOGGER.isLoggable(java.util.logging.Level.WARNING))
					LOGGER.warning("Unable to get reader for granule "+this.toString()+ " with request "+request.toString());
				return null;
			}
			
			//get selected level and base level dimensions
			final Level selectedlevel= getLevel(imageIndex);
	
			
			// now create the crop grid to world which can be used to decide
			// which source area we need to crop in the selected level taking
			// into account the scale factors imposed by the selection of this
			// level together with the base level grid to world transformation
			MathTransform2D cropWorldToGrid=(MathTransform2D) PixelTranslation.translate(ProjectiveTransform.create(selectedlevel.gridToWorldTransform), PixelInCell.CELL_CENTER, PixelInCell.CELL_CORNER).inverse();		
			
			// computing the crop source area which lives into the
			// selected level raster space, NOTICE that at the end we need to
			// take into account the fact that we might also decimate therefore
			// we cannot just use the crop grid to world but we need to correct
			// it.
			final Rectangle sourceArea = CRS.transform(cropWorldToGrid, new GeneralEnvelope(intersection)).toRectangle2D().getBounds();
			XRectangle2D.intersect(sourceArea, selectedlevel.rasterDimensions, sourceArea);//make sure roundings don't bother us
			// is it empty??
			if (sourceArea.isEmpty()) {
				if (LOGGER.isLoggable(java.util.logging.Level.FINE))
					LOGGER.fine("Got empty area for granule "+this.toString()+ " with request "+request.toString());
				return null;

			} else if (LOGGER.isLoggable(java.util.logging.Level.FINE))
				LOGGER.fine((new StringBuffer("Loading level ").append(
						imageIndex).append(" with source region ").append(
						sourceArea).toString()));

			// Setting subsampling 
			int newSubSamplingFactor = 0;
			final String pluginName = cachedSPI.getPluginClassName();
			if (pluginName != null && pluginName.equals(DIRECT_KAKADU_PLUGIN)){
				final int ssx = readParameters.getSourceXSubsampling();
				final int ssy = readParameters.getSourceYSubsampling();
				newSubSamplingFactor = Utilities.getSubSamplingFactor2(ssx , ssy);
				if (newSubSamplingFactor != 0)
					readParameters.setSourceSubsampling(newSubSamplingFactor, newSubSamplingFactor,0,0);
			}
			
			// set the source region
			readParameters.setSourceRegion(sourceArea);
			final RenderedImage raster;
			try{
				// read
				raster= request.getReadType().read(readParameters,imageIndex, granuleUrl, selectedlevel.rasterDimensions,tileDimension,cachedSPI);
				
			}
			catch (Throwable e) {
				if(LOGGER.isLoggable(java.util.logging.Level.FINE))
					LOGGER.log(java.util.logging.Level.FINE,"Unable to load raster for granule "+this.toString()+ " with request "+request.toString(),e);
				return null;
			}

			// use fixed source area
			sourceArea.setRect(readParameters.getSourceRegion());

			//
			// setting new coefficients to define a new affineTransformation
			// to be applied to the grid to world transformation
			// -----------------------------------------------------------------------------------
			//
			// With respect to the original envelope, the obtained planarImage
			// needs to be rescaled. The scaling factors are computed as the
			// ratio between the cropped source region sizes and the read
			// image sizes.
			//
			// place it in the mosaic using the coords created above;
			double decimationScaleX =  ((1.0 * sourceArea.width) / raster.getWidth());
			double decimationScaleY =  ((1.0 * sourceArea.height) / raster.getHeight());
			final AffineTransform decimationScaleTranform = XAffineTransform.getScaleInstance(decimationScaleX, decimationScaleY);

			// keep into account translation  to work into the selected level raster space
			final AffineTransform afterDecimationTranslateTranform =XAffineTransform.getTranslateInstance(sourceArea.x, sourceArea.y);
			
			// now we need to go back to the base level raster space
			final AffineTransform backToBaseLevelScaleTransform = selectedlevel.baseToLevelTransform;
			
			// now create the overall transform
			final AffineTransform finalRaster2Model = new AffineTransform(baseGridToWorld);
			finalRaster2Model.concatenate(ImageMosaicUtils.CENTER_TO_CORNER);
			final double x = finalRaster2Model.getTranslateX();
			final double y = finalRaster2Model.getTranslateY();
			
			if(!XAffineTransform.isIdentity(backToBaseLevelScaleTransform,10E-6)){
				finalRaster2Model.concatenate(backToBaseLevelScaleTransform);
			}
			if(!XAffineTransform.isIdentity(afterDecimationTranslateTranform,10E-6)){
				finalRaster2Model.concatenate(afterDecimationTranslateTranform);
			}
			if(!XAffineTransform.isIdentity(decimationScaleTranform,10E-6)){
				finalRaster2Model.concatenate(decimationScaleTranform);
			}

			// keep into account translation factors to place this tile
			finalRaster2Model.preConcatenate((AffineTransform) mosaicWorldToGrid);
			
			final InterpolationNearest nearest = new InterpolationNearest();
			//paranoiac check to avoid that JAI freaks out when computing its internal layouT on images that are too small
			final Rectangle2D finalLayout= layoutHelper(
					raster, 
					(float)finalRaster2Model.getScaleX(), 
					(float)finalRaster2Model.getScaleY(), 
					(float)finalRaster2Model.getTranslateX(), 
					(float)finalRaster2Model.getTranslateY(), 
					nearest);
			if(finalLayout.isEmpty()) {
				if(LOGGER.isLoggable(java.util.logging.Level.FINE))
					LOGGER.fine("Unable to create a granule "+this.toString()+ " due to jai scale bug");
				return null;
			}
			
			ROIShape granuleLoadingShape = null;
			if (granuleROIShape != null){
			    final Point2D translate = mosaicWorldToGrid.transform(new DirectPosition2D(x,y), (Point2D) null);
			    AffineTransform tx2 = new AffineTransform();
			    tx2.preConcatenate(AffineTransform.getScaleInstance(((AffineTransform)mosaicWorldToGrid).getScaleX(), 
			            -((AffineTransform)mosaicWorldToGrid).getScaleY()));
			    tx2.preConcatenate(AffineTransform.getScaleInstance(((AffineTransform)baseGridToWorld).getScaleX(), 
                                    -((AffineTransform)baseGridToWorld).getScaleY()));
			    tx2.preConcatenate(AffineTransform.getTranslateInstance(translate.getX(),translate.getY()));
			    final Shape sp = granuleROIShape.getAsShape();
			    final Area area = new Area(sp);
			    area.transform(tx2);
			    granuleLoadingShape = new ROIShape(area);
			    
			}
			
			// apply the affine transform  conserving indexed color model
			final RenderingHints localHints = new RenderingHints(JAI.KEY_REPLACE_INDEX_COLOR_MODEL, Boolean.FALSE);
			if(XAffineTransform.isIdentity(finalRaster2Model,10E-6)) {
				return new GranuleLoadingResult(raster, granuleLoadingShape);
			} else {
				//
				// In case we are asked to use certain tile dimensions we tile
				// also at this stage in case the read type is Direct since
				// buffered images comes up untiled and this can affect the
				// performances of the subsequent affine operation.
				//
				final Dimension tileDimensions = request.getTileDimensions();
				if(tileDimensions != null && request.getReadType().equals(ReadType.DIRECT_READ))
				{
					final ImageLayout layout = new ImageLayout();
					layout.setTileHeight(tileDimensions.width).setTileWidth(tileDimensions.height);
					localHints.add(new RenderingHints(JAI.KEY_IMAGE_LAYOUT,layout));
				}
				// border extender
				
				return new GranuleLoadingResult(AffineDescriptor.create(raster, finalRaster2Model, nearest, request.getBackgroundValues(),localHints), granuleLoadingShape);
			}
		
		} catch (IllegalStateException e) {
			if (LOGGER.isLoggable(java.util.logging.Level.WARNING))
				LOGGER.log(java.util.logging.Level.WARNING, "Unable to load raster for granule "+this.toString()+ " with request "+request.toString(), e);
			return null;
		} 
		catch (org.opengis.referencing.operation.NoninvertibleTransformException e) {
			if (LOGGER.isLoggable(java.util.logging.Level.WARNING))
				LOGGER.log(java.util.logging.Level.WARNING, "Unable to load raster for granule "+this.toString()+ " with request "+request.toString(), e);
			return null;
		} catch (TransformException e) {
			if (LOGGER.isLoggable(java.util.logging.Level.WARNING))
				LOGGER.log(java.util.logging.Level.WARNING, "Unable to load raster for granule "+this.toString()+ " with request "+request.toString(), e);
			return null;
		} finally {
			try {
				if(inStream!=null)
					inStream.close();
			} finally{
				if(reader!=null)
					reader.dispose();
			}
		}
	}


	public Level getLevel(final int index) {
		synchronized (granuleLevels) {
			if(granuleLevels.containsKey(Integer.valueOf(index)))
				return granuleLevels.get(Integer.valueOf(index));
			else
			{
				//load level
				// create the base grid to world transformation
				ImageInputStream inStream=null;
				ImageReader reader=null;
				try {
					//
					//get info about the raster we have to read
					//
					
					// get a stream
					inStream = ImageMosaicUtils.getInputStream(granuleUrl);
					if(inStream==null)
						throw new IllegalArgumentException();
			
					// get a reader and try to cache the relevant SPI
					if(cachedSPI==null){
						reader = ImageMosaicUtils.getReader( inStream);
						if(reader!=null)
							cachedSPI=reader.getOriginatingProvider();
					}
					else
						reader=cachedSPI.createReaderInstance();
					if(reader==null)
						throw new IllegalArgumentException("Unable to get an ImageReader for the provided file "+granuleUrl.toString());					
					
					//get selected level and base level dimensions
					final Rectangle levelDimension = ImageMosaicUtils.getDimension(index,inStream, reader);
					
					
					final Level baseLevel= granuleLevels.get(0);
					final double scaleX=baseLevel.width/(1.0*levelDimension.width);
					final double scaleY=baseLevel.height/(1.0*levelDimension.height);
					
					// add the base level
					final Level newLevel=new Level(scaleX,scaleY,levelDimension.width,levelDimension.height);
					this.granuleLevels.put(Integer.valueOf(index),newLevel);
					
					return newLevel;
					

				} catch (IllegalStateException e) {
					throw new IllegalArgumentException(e);
					
				} catch (IOException e) {
					throw new IllegalArgumentException(e);
				} 
				finally{
					try{
						if(inStream!=null)
							inStream.close();
					}
					catch (Throwable e) {
						throw new IllegalArgumentException(e);
					}
					finally{
						if(reader!=null)
							reader.dispose();
					}
				}	
			}			
		}
	}

	@Override
	public String toString() {
		// build a decent representation for this level
		final StringBuilder sb = new StringBuilder();
		sb.append("Description of a granule ").append("\n");
		sb.append("BBOX:\t\t").append(granuleBBOX.toString());
		sb.append("file:\t\t").append(granuleUrl);
		sb.append("gridToWorld:\t\t").append(baseGridToWorld);
		int i=1;
		for (final Level level : granuleLevels.values()) {
			i++;
			sb.append("Description of level ").append(i++).append("\n");
			sb.append(level.toString()).append("\n");
		}
		return sb.toString();
	}
	
}
