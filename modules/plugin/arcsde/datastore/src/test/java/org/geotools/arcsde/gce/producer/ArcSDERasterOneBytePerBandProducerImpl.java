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
import java.awt.image.SampleModel;

import com.esri.sde.sdk.client.SeRasterAttr;
import com.esri.sde.sdk.client.SeRasterConsumer;
import com.esri.sde.sdk.client.SeRasterRenderedImage;
import com.esri.sde.sdk.client.SeRasterScanLineGenerator;
import com.esri.sde.sdk.client.SeRasterScanLineProducer;

public class ArcSDERasterOneBytePerBandProducerImpl extends ArcSDERasterProducer {

    public ArcSDERasterOneBytePerBandProducerImpl() {
        super(null, null, SeRasterScanLineGenerator.MASK_ALL_ON);
    }

    public ArcSDERasterOneBytePerBandProducerImpl(SeRasterAttr attr, BufferedImage sourceImage,
            int maskType) {
        super(attr, sourceImage, maskType);
    }

    @Override
    public void setSourceImage(BufferedImage sourceImage) {
        final SampleModel sm = sourceImage.getSampleModel();
        for (int i = 0; i < sm.getNumBands(); i++) {
            if (sm.getSampleSize(i) != 8) {
                throw new IllegalArgumentException(
                        "ArcSDERasterOneBytePerBandProducerImpl can't handle images with "
                                + sm.getSampleSize(i) + " bits/sample (in band " + i + ")");
            }
        }
        this.sourceImage = sourceImage;
    }

    /**
     * @see com.esri.sde.sdk.client.SeRasterProducer#startProduction(com.esri.sde.sdk.client.SeRasterConsumer)
     *      this implementation defers completely to {@link SeRasterScanLineProducer}
     */
    public void startProduction(final SeRasterConsumer consumer) {
        if (!sourceImage.getColorModel().getColorSpace().isCS_sRGB()) {
            // it's a grayscale image...load it differently?
            if (!(consumer instanceof SeRasterRenderedImage)) {
                throw new IllegalArgumentException("You must set "
                        + "SeRasterAttr.setImportMode(false) to load "
                        + "data using this SeProducer implementation.");

            }

            Thread runme;
            runme = new Thread() {
                @Override
                public void run() {
                    try {
                        final int imageHeight = sourceImage.getHeight();

                        // for each band...
                        for (int i = 0; i < sourceImage.getData().getNumBands(); i++) {
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
            runme.start();

        } else {
            SeRasterScanLineProducer prod = new SeRasterScanLineProducer(attr, sourceImage,
                    sourceImage.getHeight());
            prod.setBitMaskType(maskType);
            prod.addConsumer(consumer);
            prod.startProduction(consumer);
        }
    }

}
