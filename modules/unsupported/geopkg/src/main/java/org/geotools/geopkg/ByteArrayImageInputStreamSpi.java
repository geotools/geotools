package org.geotools.geopkg;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.util.Locale;

import javax.imageio.spi.ImageInputStreamSpi;
import javax.imageio.stream.ImageInputStream;
import javax.imageio.stream.MemoryCacheImageInputStream;

/**
 * Image input stream provider creating an image input stream directly from a byte array.
 *  
 * @author Justin Deoliveira, OpenGeo
 */
public class ByteArrayImageInputStreamSpi extends ImageInputStreamSpi{

    public ByteArrayImageInputStreamSpi() {
        super("GeoServer", "1.0", byte[].class);
    }

    @Override
    public ImageInputStream createInputStreamInstance(Object input, boolean useCache, File cacheDir) 
        throws IOException {
        if (input instanceof byte[]) {
            return new MemoryCacheImageInputStream(new ByteArrayInputStream((byte[])input));
        }
        return null;
    }

    @Override
    public String getDescription(Locale locale) {
        return "Byte array stream";
    }


}
