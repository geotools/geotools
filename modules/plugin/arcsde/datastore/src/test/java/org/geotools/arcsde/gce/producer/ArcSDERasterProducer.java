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
import java.util.ArrayList;

import com.esri.sde.sdk.client.SeRasterAttr;
import com.esri.sde.sdk.client.SeRasterConsumer;
import com.esri.sde.sdk.client.SeRasterProducer;

public abstract class ArcSDERasterProducer implements SeRasterProducer {

    ArrayList<SeRasterConsumer> consumers = new ArrayList<SeRasterConsumer>();

    BufferedImage sourceImage;

    SeRasterAttr attr;

    int maskType;

    protected ArcSDERasterProducer(SeRasterAttr attr, BufferedImage sourceImage, int maskType) {
        this.attr = attr;
        if (sourceImage != null)
            setSourceImage(sourceImage);
        this.maskType = maskType;
    }

    public void setSeRasterAttr(SeRasterAttr attr) {
        this.attr = attr;
    }

    public void setMaskType(int maskType) {
        this.maskType = maskType;
    }

    /**
     * This method should check that the supplied image is compatible with this class's
     * startProduction() implementation
     * 
     * @param sourceImage
     */
    public abstract void setSourceImage(BufferedImage sourceImage);

    public void addConsumer(SeRasterConsumer arg0) {
        consumers.add(arg0);
        arg0.setHints(SeRasterConsumer.COMPLETETILES);
    }

    public boolean isConsumer(SeRasterConsumer arg0) {
        return consumers.contains(arg0);
    }

    public void removeConsumer(SeRasterConsumer arg0) {
        consumers.remove(arg0);
    }

}
