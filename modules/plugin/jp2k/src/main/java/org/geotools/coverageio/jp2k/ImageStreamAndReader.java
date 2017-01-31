package org.geotools.coverageio.jp2k;

import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;
import java.io.Closeable;
import java.io.IOException;

class ImageStreamAndReader implements Closeable {
    private ImageReader reader;
    private ImageInputStream inputStream;

    public ImageStreamAndReader(ImageReader imageReader, ImageInputStream inputStream) {
        this.reader = imageReader;
        this.inputStream = inputStream;
    }

    @Override
    public void close() throws IOException {
        try {
            if(reader != null) {
                reader.dispose();
            }
        }
        catch (Exception e) {
        }

        if(inputStream != null) {
            inputStream.close();
        }
    }

    public ImageReader getReader() {
        return reader;
    }
}