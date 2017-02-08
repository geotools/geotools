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

class SampleImage implements Serializable {

    transient SampleModel sampleModel;

    transient ColorModel colorModel;

    public SampleImage(SampleModel sampleModel, ColorModel colorModel) {
        this.sampleModel = sampleModel;
        this.colorModel = colorModel;
    }

    public BufferedImage toBufferedImage() {
        final SampleModel sm = sampleModel.createCompatibleSampleModel(1, 1);
        final WritableRaster raster = RasterFactory.createWritableRaster(sm, null);
        final BufferedImage image = new BufferedImage(colorModel, raster,
                colorModel.isAlphaPremultiplied(), null);
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
