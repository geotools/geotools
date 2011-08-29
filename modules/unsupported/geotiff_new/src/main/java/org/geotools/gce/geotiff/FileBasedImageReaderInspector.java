package org.geotools.gce.geotiff;

import java.io.IOException;
import java.io.Serializable;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

import javax.imageio.spi.ImageInputStreamSpi;
import javax.imageio.spi.ImageReaderSpi;
import javax.imageio.stream.ImageInputStream;

import org.geotools.data.DataUtilities;
import org.geotools.data.Parameter;
import org.geotools.factory.Hints;
import org.geotools.image.io.ImageIOExt;
import org.geotools.util.Utilities;


public abstract class FileBasedImageReaderInspector extends ImageReaderInspector {

    /** Logger for the {@link FileBasedImageReaderInspector} class. */
    private final static Logger LOGGER = org.geotools.util.logging.Logging.getLogger(FileBasedImageReaderInspector.class.toString());
    
    /**
     * url to the  file.
     */
    public static final Parameter<URL> URLP = new Parameter<URL>("url", URL.class);
    
   
    private final URL sourceURL;
    
    public FileBasedImageReaderInspector(
            URL sourceURL, 
            Hints hints,
            ImageReaderSpi readerSPI) throws IOException {
        super(URLToParams(sourceURL), hints, null, readerSPI);
      this.sourceURL = sourceURL;
      
      // now do the real work
      initialize();
    }


    private final static Map<String, Serializable> URLToParams(URL url) {
        Utilities.ensureNonNull("SourceURL", url);
        Map<String, java.io.Serializable> params = new HashMap<String, java.io.Serializable>();
        params.put(URLP.key, url);
        return params;
    }

    public URL getSource() {
        return sourceURL;
    }

    // TODO we should try to avoid this lookup as much as possible
    @Override
    protected ImageInputStream createImageInputStream() throws IOException {
        return ImageIOExt.createImageInputStream(DataUtilities.urlToFile(sourceURL));
    }
}
