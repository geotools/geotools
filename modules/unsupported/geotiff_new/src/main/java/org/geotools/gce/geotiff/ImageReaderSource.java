package org.geotools.gce.geotiff;

import java.io.File;

import javax.imageio.spi.ImageInputStreamSpi;
import javax.imageio.spi.ImageReaderSpi;

public class ImageReaderSource<T> {
    
    public int getImageIndex() {
        return imageIndex;
    }

    public static ImageReaderSource<File> wrapFile(
            int imageIndex,
            File source, 
            ImageInputStreamSpi inputStreamSPI,
            ImageReaderSpi imageReaderSpi){
        return new FileImageReaderSource(imageIndex,source, inputStreamSPI, imageReaderSpi);
    }

    public static class FileImageReaderSource extends ImageReaderSource<File> {
        public FileImageReaderSource(
                int imageIndex,File source, ImageInputStreamSpi inputStreamSPI,
                ImageReaderSpi imageReaderSpi) {
            super(imageIndex,source, inputStreamSPI, imageReaderSpi);
        }

    }

    public T getSource() {
        return source;
    }

    public ImageInputStreamSpi getInputStreamSPI() {
        return inputStreamSPI;
    }

    public ImageReaderSpi getImageReaderSpi() {
        return imageReaderSpi;
    }

    public ImageReaderSource(
            int imageIndex,
            T source, 
            ImageInputStreamSpi inputStreamSPI,
            ImageReaderSpi imageReaderSpi) {
        this.imageIndex=imageIndex;
        this.source = source;
        this.inputStreamSPI = inputStreamSPI;
        this.imageReaderSpi = imageReaderSpi;
    }

    private int imageIndex;
    
    private T source;

    private ImageInputStreamSpi inputStreamSPI;

    private ImageReaderSpi imageReaderSpi;

}
