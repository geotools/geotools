/*
 *    Geotools2 - OpenSource mapping toolkit
 *    http://geotools.org
 *    (C) 2002, Geotools Project Managment Committee (PMC)
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
 *
 */
package org.geotools.arcsde.gce.producer;

import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;

import com.esri.sde.sdk.client.SeRasterAttr;
import com.esri.sde.sdk.client.SeRasterConsumer;
import com.esri.sde.sdk.client.SeRasterRenderedImage;
import com.esri.sde.sdk.client.SeRasterScanLineGenerator;
import com.esri.sde.sdk.client.SeRasterScanLineProducer;

public class ArcSDERasterOneBitPerBandProducerImpl extends ArcSDERasterProducer {

    /**
     * Use this constructor if you need to pass a particular implementation to code which will later
     * inject the SeRasterAttr, maskType and source BufferedImage later.
     */
    public ArcSDERasterOneBitPerBandProducerImpl() {
        super(null, null, SeRasterScanLineGenerator.MASK_ALL_ON);
    }

    public ArcSDERasterOneBitPerBandProducerImpl(SeRasterAttr attr, BufferedImage sourceImage,
            int maskType) {
        super(attr, sourceImage, maskType);
    }

    /**
     * @see org.geotools.arcsde.gce.producer.ArcSDERasterProducer#setSourceImage(java.awt.image.BufferedImage)
     */
    @Override
    public void setSourceImage(BufferedImage sourceImage) {
        for (int i = 0; i < sourceImage.getSampleModel().getSampleSize().length; i++) {
            if (sourceImage.getSampleModel().getSampleSize(i) != 1) {
                throw new IllegalArgumentException(
                        "Source image must be one bit per sample on all bands.  Band " + i
                                + " has " + sourceImage.getSampleModel().getSampleSize(i)
                                + " bits per sample.");
            }
        }
        if (!(sourceImage.getData().getDataBuffer() instanceof DataBufferByte)) {
            throw new IllegalArgumentException("Currently one-bit-per-band raster loading is "
                    + "only supported from images backed with a DataBufferByte");
        }
        this.sourceImage = sourceImage;
    }

    /**
     * @see com.esri.sde.sdk.client.SeRasterProducer#startProduction(com.esri.sde.sdk.client.SeRasterConsumer)
     *      Also take a look at {@link SeRasterScanLineProducer} for an (opaque) ESRI supplied
     *      implementation of this functionality that only works with some sort of 8-bit images.
     *      Note that due to some synchronization problems inherent in the SDE api code, the
     *      startProduction() method MUST return before consumer.setScanLines() or
     *      consumer.setRasterTiles() is called. Hence the thread implementation.
     */
    public void startProduction(final SeRasterConsumer consumer) {

        Thread runme;
        if (consumer instanceof SeRasterRenderedImage) {

            runme = new Thread() {
                @Override
                public void run() {
                    try {
                        final int imageHeight = sourceImage.getHeight();

                        // for each band...
                        for (int i = 0; i < sourceImage.getData().getNumBands(); i++) {
                            // luckily the byte-packed data format in MultiPixelPackedSampleModel is
                            // identical
                            // to the one-bit-per-pixel format expected by ArcSDE.
                            final byte[] imgBandData = ((DataBufferByte) sourceImage.getData()
                                    .getDataBuffer()).getData(i);
                            consumer.setScanLines(imageHeight, imgBandData, null);
                            consumer.rasterComplete(SeRasterConsumer.SINGLEFRAMEDONE);
                        }
                        consumer.rasterComplete(SeRasterConsumer.STATICIMAGEDONE);
                    } catch (Exception se) {
                        se.printStackTrace();
                        consumer.rasterComplete(SeRasterConsumer.IMAGEERROR);
                    }
                }
            };
        } else {
            throw new IllegalArgumentException("You must set SeRasterAttr.setImportMode(false) to "
                    + "load data using this SeProducer implementation.");
        }

        runme.start();
    }
}
