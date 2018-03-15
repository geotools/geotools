package org.geotools.gce.imagemosaic.remote;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.util.Locale;

import javax.imageio.spi.ImageInputStreamSpi;
import javax.imageio.stream.ImageInputStream;

import org.geotools.TestData;
import org.geotools.gce.imagemosaic.RemoteTest;

import com.sun.imageio.spi.FileImageInputStreamSpi;

public class RemoteImageInputStreamSPI extends ImageInputStreamSpi {
    
    private FileImageInputStreamSpi delegate = new FileImageInputStreamSpi();
    
    public RemoteImageInputStreamSPI() {
        super("geotools", "0.0.1", URL.class);
    }
    
    @Override
    public ImageInputStream createInputStreamInstance(Object input, boolean useCache, File cacheDir)
            throws IOException {
        try {
            return delegate.createInputStreamInstance(TestData.file(RemoteTest.class, "remote_test" + ((URL) input).getPath()), 
                useCache, cacheDir);
        } catch (FileNotFoundException e) {
            return null;
        }
    }

    @Override
    public String getDescription(Locale locale) {
         return "Remote Test Image InputStream SPI";
    }

}
