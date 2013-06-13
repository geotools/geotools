/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2008, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.gce.imagemosaic.jdbc;

import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.awt.image.Raster;
import java.awt.image.SampleModel;
import java.awt.image.WritableRaster;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;
import java.util.concurrent.LinkedBlockingQueue;

import org.geotools.geometry.GeneralEnvelope;

/**
 * this class is the base class for concrete thread classes
 * 
 * @author mcr
 * 
 */
abstract class AbstractThread extends Thread {
	LinkedBlockingQueue<TileQueueElement> tileQueue;

	Config config;

	GeneralEnvelope requestEnvelope;

	Rectangle pixelDimension;

	ImageLevelInfo levelInfo;

	double rescaleX;

	double rescaleY;

	double resX;

	double resY;

	/**
	 * Constructor
	 * 
	 * @param pixelDimenison
	 *            the requested pixel dimension
	 * @param requestEnvelope
	 *            the requested world rectangle
	 * @param levelInfo
	 *            levelinfo of selected pyramid
	 * @param tileQueue
	 *            queue for thread synchronization
	 * @param config
	 *            the configuraton of the plugin
	 * 
	 */
	AbstractThread(Rectangle pixelDimenison, GeneralEnvelope requestEnvelope,
			ImageLevelInfo levelInfo,
			LinkedBlockingQueue<TileQueueElement> tileQueue, Config config) {
		super();
		this.config = config;
		this.tileQueue = tileQueue;
		this.requestEnvelope = requestEnvelope;
		this.levelInfo = levelInfo;
		this.pixelDimension = pixelDimenison;

		resX = requestEnvelope.getSpan(0) / pixelDimenison.getWidth();
		resY = requestEnvelope.getSpan(1) / pixelDimenison.getHeight();
		rescaleX = levelInfo.getResX() / resX;
		rescaleY = levelInfo.getResY() / resY;
	}

	/**
	 * @param image
	 *            to scale
	 * @return rescaled image fitting into the requested pixel dimension
	 */
	protected BufferedImage rescaleImage(BufferedImage image) {
	    	    
		
		Map<String,Object> properties = null;
	                
	        if (image.getPropertyNames()!=null) {
	            properties = new HashMap<String, Object>();
	            for (String name : image.getPropertyNames()) {
	                 properties.put(name, image.getProperty(name));
	            }
	        }
	                            
	        int newWidth = (int) Math.floor(image.getWidth()* rescaleX);
	        int newHeight= (int) Math.floor(image.getHeight() * rescaleY);
	        SampleModel sm = image.getSampleModel().createCompatibleSampleModel(newWidth,newHeight);
	        WritableRaster raster = Raster.createWritableRaster(sm, null);

	        BufferedImage scaledImage = new BufferedImage(image.getColorModel(), 
	                raster, image.isAlphaPremultiplied(), (Hashtable<?, ?>) properties); 


                Object interpolation=RenderingHints.VALUE_INTERPOLATION_NEAREST_NEIGHBOR;
                
                if (config.getInterpolation().intValue() == 2) 
                           interpolation = RenderingHints.VALUE_INTERPOLATION_BILINEAR;

                if (config.getInterpolation().intValue() == 3) 
                           interpolation = RenderingHints.VALUE_INTERPOLATION_BICUBIC;

                
                Graphics2D g = scaledImage.createGraphics();
                g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, interpolation);
                g.drawImage(image, 0, 0, newWidth, newHeight, 0, 0, image.getWidth(), image.getHeight(), null);
                g.dispose();
                return scaledImage;
	        
	       /* 
	       int interpolation=AffineTransformOp.TYPE_NEAREST_NEIGHBOR;
               
	       if (config.getInterpolation().intValue() == 2) 
	                   interpolation = AffineTransformOp.TYPE_BILINEAR;

                if (config.getInterpolation().intValue() == 3) 
	                     interpolation = AffineTransformOp.TYPE_BICUBIC;
                
		AffineTransformOp op= new AffineTransformOp(AffineTransform.getScaleInstance(rescaleX,
		        rescaleY), interpolation);
		op.filter(image.getRaster(),raster);

		return scaledImage;
		*/
		
	}
}
