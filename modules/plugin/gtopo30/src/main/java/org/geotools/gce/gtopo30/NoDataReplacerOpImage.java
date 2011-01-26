/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2001-2008, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.gce.gtopo30;




// J2SE dependencies
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.image.ColorModel;
import java.awt.image.ComponentColorModel;
import java.awt.image.ComponentSampleModel;
import java.awt.image.DataBuffer;
import java.awt.image.RenderedImage;
import java.awt.image.SampleModel;
import java.awt.image.WritableRaster;
import java.awt.image.renderable.ParameterBlock;
import java.awt.image.renderable.RenderedImageFactory;
import java.util.logging.Level;
import java.util.logging.LogRecord;

import javax.media.jai.CRIFImpl;
import javax.media.jai.ComponentSampleModelJAI;
import javax.media.jai.ImageLayout;
import javax.media.jai.JAI;
import javax.media.jai.OperationDescriptorImpl;
import javax.media.jai.OperationRegistry;
import javax.media.jai.PlanarImage;
import javax.media.jai.PointOpImage;
import javax.media.jai.iterator.RectIterFactory;
import javax.media.jai.iterator.WritableRectIter;
import javax.media.jai.registry.RenderedRegistryMode;

import org.geotools.coverage.GridSampleDimension;
import org.geotools.coverage.grid.AbstractGridCoverage;
import org.geotools.image.TransfertRectIter;
import org.geotools.resources.i18n.Loggings;
import org.geotools.resources.i18n.LoggingKeys;
import org.geotools.resources.image.ImageUtilities;


/**
 * An image that contains transformed samples, specifically this method will transform the NoData value
 * using a new supplied one. A new layout is used in order to convert to the required image layout. Default
 * values for this operation can be used to set the NoData and the layout to the values needed for
 * the GTOPO30 writer.
 *
 * Images are created using the
 * {@code NoDataReplacerOpImage.NoDataReplacerCRIF} inner class, where "CRIF" stands for
 * {@link java.awt.image.renderable.ContextualRenderedImageFactory}. The image
 * operation name is "org.geotools.gce.NoDataReplacer".
 *
 * @source $URL$
 * @version $Id$
 * @author Simone Giannecchini
 *
 * @since 2.2
 */
public final class NoDataReplacerOpImage extends PointOpImage {
    /**The operation name.*/
    public static final String OPERATION_NAME = "org.geotools.gce.gtopo30.NoDataReplacer";

    /**Constant that tell me the margin when checking for equality with floating point values*/
	private double EPS;

	/**Old no data value.*/
	private Number oldNoData;

	/**New no data value.*/
	private int newNoData;

	/**It tells me whether or not the old no data is NaN.*/
	private boolean oldNoDataIsNaN;




    /**
     * Constructs a new {@code NoDataReplacerOpImage}.
     *
     * @param image      The source image.
     * @param oldNoData The old NoData value to be substituted.
     * @param newNoData The new NoData value to be employed.
     * @param EPS Margin for equality checks.
     * @param hints Suplpied RenderingHints.
     */
    private NoDataReplacerOpImage(final RenderedImage  image,
                             final Number oldNoData,
                             final Short newNoData,
                             final Double EPS,
                             final RenderingHints hints)
    {
        super(image, NoDataReplacerOpImage.getRightLayout(image), hints, false);
        this.EPS = EPS.doubleValue();
        this.oldNoData=oldNoData;
        this.oldNoDataIsNaN=Double.isNaN(oldNoData.doubleValue());
        this.newNoData=newNoData.intValue();

        permitInPlaceOperation();
    }

    /**
     * @todo Creation of non banded sample models
	 * @param image Image to work on.
	 * @return New Image Layout.
	 */
	private static ImageLayout getRightLayout(RenderedImage image) {

		final SampleModel sm=image.getSampleModel();
		final int dataType=DataBuffer.TYPE_SHORT;
		if(sm.getDataType()==dataType)
			return new ImageLayout(image);
		final ColorModel cm=image.getColorModel();
		if(sm instanceof ComponentSampleModel)
		{
	        final ColorModel newCm = new ComponentColorModel(
	        		cm.getColorSpace(),
	        		false,
	        		false,
	        		cm.getTransparency(),
	        		dataType);
	        final SampleModel newSm = new ComponentSampleModelJAI(
	        		dataType,
	        		sm.getWidth(),
	        		sm.getHeight(),
	        		((ComponentSampleModel)sm).getPixelStride(),
	        		((ComponentSampleModel)sm).getScanlineStride(),
	        		((ComponentSampleModel)sm).getBankIndices(),
	        		((ComponentSampleModel)sm).getBandOffsets()
	        		);

		   final        ImageLayout layout = ImageUtilities.getImageLayout(image);
		   layout.setColorModel(newCm);
		   layout.setSampleModel(newSm);
		   return layout;
		}
		else
			;//do nothing for the moment

		return null;
	}



	/**
     * Computes one of the destination image tile.
     *
     * @todo There is two optimisations we could do here:
     *       <ul>
     *         <li>If source and destination are the same raster, then a single
     *             {@link WritableRectIter} object would be more efficient (the
     *             hard work is to detect if source and destination are the same).</li>
     *         <li>If the destination image is a single-banded, non-interleaved
     *             sample model, we could apply the transform directly in the
     *             {@link java.awt.image.DataBuffer}. We can even avoid to copy
     *             sample value if source and destination raster are the same.</li>
     *       </ul>
     *
     * @param sources  An array of length 1 with source image.
     * @param dest     The destination tile.
     * @param destRect the rectangle within the destination to be written.
     */
    protected void computeRect(final PlanarImage[] sources,
                               final WritableRaster   dest,
                               final Rectangle    destRect)
    {
        final PlanarImage source = sources[0];
        WritableRectIter iterator = RectIterFactory.createWritable(dest, destRect);
        if (true) {
            // TODO: Detect if source and destination rasters are the same. If they are
            //       the same, we should skip this block. Iteration will then be faster.
            iterator = TransfertRectIter.create(RectIterFactory.create(source, destRect), iterator);
        }
        formatRect(iterator);
    }




	 /**
     * Transform a raster. Only the current band in {@code iterator} will be transformed.
     * The transformed value are write back in the {@code iterator}. If a different
     * destination raster is wanted, a {@link org.geotools.resources.image.DualRectIter}
     * may be used.
     *
     * @param  iterator An iterator to iterate among the samples to transform.
     */
	private void formatRect(WritableRectIter iterator) {


		double actualValue=0.0;
        iterator.startLines();
        if (!iterator.finishedLines())
        	do {
        		iterator.startPixels();
        		if (!iterator.finishedPixels())
        			do {

		            	//get the actual value
		                actualValue = iterator.getSampleDouble();

		                //substituting a NaN
		                if(oldNoDataIsNaN)
		                	if(Double.isNaN(actualValue))
		                			iterator.setSample(newNoData);
		                else
		                	if(Math.abs(oldNoData.doubleValue()-actualValue)<=EPS)
		                		iterator.setSample(newNoData);
		                	else
		                		iterator.setSample(actualValue);

        			}
	                while (!iterator.nextPixelDone());
            }
            while (!iterator.nextLineDone());



	}




	/////////////////////////////////////////////////////////////////////////////////
    ////////                                                                 ////////
    ////////        REGISTRATION OF "NoDataReplacer" IMAGE OPERATION        ////////
    ////////                                                                 ////////
    /////////////////////////////////////////////////////////////////////////////////
    /**
     * The operation descriptor for the "NoDataReplacer" operation. This operation
     * is used to change the format of an Image while replacing the NoData value with a new
     * one as requested. The difference between this method and the usual format operation presents
     * in JAI is the possibility to replace the NoData value directly when it is like Double.NaN or
     * Float.NaN.
     *
     */
    private static final class NoDataReplacerDescriptor extends OperationDescriptorImpl {
		/**
		 * Comment for <code>serialVersionUID</code>
		 */
		private static final long serialVersionUID = 1L;

		/**
         * Construct the descriptor.
         */
        public NoDataReplacerDescriptor() {
            super(new String[][]{{"GlobalName",  OPERATION_NAME},
                                 {"LocalName",   OPERATION_NAME},
                                 {"Vendor",      "Geotools 2"},
                                 {"Description", "Nodata replacement and layout adjustment."},
                                 {"DocURL",      "http://www.geotools.org/"},
                                 {"Version",     "1.0"}},
                  new String[]   {RenderedRegistryMode.MODE_NAME}, 1,
                  new String[]   {"oldNoData","newNoData","EPS"},          // Argument names
                  new Class []   {Number.class,Short.class,Double.class}, // Argument classes
                  new Object[]   {new Double(Double.NaN),new Short((short)-9999),new Double(10.0E-6)},        // Default values for parameters,
                  null // No restriction on valid parameter values.
            );
        }

        /**
         * Returns {@code true} if the parameters are valids. This implementation check
         * that the number of bands in the source image is equals to the number of supplied
         * sample dimensions, and that all sample dimensions has categories.
         *
         * @param modeName The mode name (usually "Rendered").
         * @param param The parameter block for the operation to performs.
         * @param message A buffer for formatting an error message if any.
         */
        protected boolean validateParameters(final String      modeName,
                                             final ParameterBlock param,
                                             final StringBuffer message)
        {
            if (!super.validateParameters(modeName, param, message)) {
                return false;
            }
            try{
//            	param.
//	            final RenderedImage source = (RenderedImage) param.getSource(0);
//	            final Number  oldNoData= (Number) param.getObjectParameter(0);
//	            final Number  newNoData= (Number) param.getObjectParameter(1);
//	            final Double  EPS=  (Double) param.getObjectParameter(1);
            }
            catch(Exception e){
            	message.append(e.getMessage());
            	return false;
            }
            return true;
        }
    }

    /**
     * The {@link RenderedImageFactory} for the "SampleTranscode" operation.
     */
    private static final class NoDataReplacerCRIF extends CRIFImpl {
        /**
         * Creates a {@link RenderedImage} representing the results of an imaging
         * operation for a given {@link ParameterBlock} and {@link RenderingHints}.
         */
        public RenderedImage create(final ParameterBlock param,
                                    final RenderingHints hints)
        {
            final RenderedImage source = (RenderedImage) param.getSource(0);

            final Number  oldNoData= (Number) param.getObjectParameter(0);
            final Short  newNoData= (Short) param.getObjectParameter(1);
            final Double  EPS=  (Double) param.getObjectParameter(2);
            return new NoDataReplacerOpImage(source, oldNoData,newNoData,EPS, hints);
        }


    }

    /**
     * Register the "SampleTranscode" image operation to the operation registry of
     * the specified JAI instance. This method is invoked by the static initializer
     * of {@link GridSampleDimension}.
     */
    public static void register(final JAI jai) {
        final OperationRegistry registry = jai.getOperationRegistry();
        try {
            registry.registerDescriptor(new NoDataReplacerDescriptor());
            registry.registerFactory(RenderedRegistryMode.MODE_NAME, OPERATION_NAME,
                                     "gce.geotools.org", new NoDataReplacerCRIF());
        } catch (IllegalArgumentException exception) {
            final LogRecord record = Loggings.format(Level.SEVERE,
                   LoggingKeys.CANT_REGISTER_JAI_OPERATION_$1, OPERATION_NAME);
            record.setSourceClassName("GridSampleDimension");
            record.setSourceMethodName("<classinit>");
            record.setThrown(exception);
            AbstractGridCoverage.LOGGER.log(record);
        }
    }
}
