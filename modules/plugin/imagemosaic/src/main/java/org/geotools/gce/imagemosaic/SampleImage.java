/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2017, Open Source Geospatial Foundation (OSGeo)
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

import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.SampleModel;
import java.awt.image.WritableRaster;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import javax.media.jai.RasterFactory;
import javax.media.jai.remote.SerializableState;
import javax.media.jai.remote.SerializerFactory;

/**
 * Simple serializable class holding a sample model and a color model
 *
 * @author Andrea Aime - GeoSolutions
 */
public class SampleImage implements Serializable {

    private static final long serialVersionUID = 6324143924454724262l;

    transient SampleModel sampleModel;

    transient ColorModel colorModel;

    /** Builds a new sample image */
    public SampleImage(SampleModel sampleModel, ColorModel colorModel) {
        this.sampleModel = sampleModel;
        this.colorModel = colorModel;
    }

    /** Builds a 1x1 BufferedImage with the provided sample model and color model */
    public BufferedImage toBufferedImage() {
        final SampleModel sm = sampleModel.createCompatibleSampleModel(1, 1);
        final WritableRaster raster = RasterFactory.createWritableRaster(sm, null);
        final BufferedImage image =
                new BufferedImage(colorModel, raster, colorModel.isAlphaPremultiplied(), null);
        return image;
    }

    private void writeObject(ObjectOutputStream out) throws IOException {
        out.defaultWriteObject();
        out.writeObject(SerializerFactory.getState(sampleModel, null));
        out.writeObject(SerializerFactory.getState(colorModel, null));
    }

    private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
        in.defaultReadObject();
        SerializableState smState = (SerializableState) in.readObject();
        sampleModel = (SampleModel) smState.getObject();
        SerializableState cmState = (SerializableState) in.readObject();
        colorModel = (ColorModel) cmState.getObject();
    }
}
