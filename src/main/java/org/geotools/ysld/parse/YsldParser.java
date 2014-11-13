package org.geotools.ysld.parse;

import org.geotools.styling.ResourceLocator;
import org.geotools.styling.StyledLayerDescriptor;

import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Collections;
import java.util.List;

/**
 * Parses a Yaml/Ysld stream into GeoTools style objects.
 */
public class YsldParser extends YamlParser {
    
    List<ZoomContextFinder> zCtxtFinders = Collections.emptyList();
    ResourceLocator locator = new ResourceLocator() {

        @Override
        public URL locateResource(String uri) {
            try {
                return new URL(uri);
            } catch (MalformedURLException e) {
                throw new IllegalArgumentException(String.format("'%s' is not a valid URI", uri),e);
            }
        }
        
    };
    
    public YsldParser(InputStream ysld) throws IOException {
        super(ysld);
    }

    public YsldParser(Reader reader) throws IOException {
        super(reader);
    }
    
    public void setZoomContextFinders(List<ZoomContextFinder> zCtxtFinders) {
        this.zCtxtFinders = zCtxtFinders;
    }
    public void setResourceLocator(ResourceLocator locator) {
        this.locator = locator;
    }
    
    public StyledLayerDescriptor parse() throws IOException {

        return super.parse(
                new RootParser(zCtxtFinders),
                Collections.<String, Object>singletonMap("resourceLocator", locator)).sld();
    }
}
