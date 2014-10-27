package org.geotools.ysld.parse;

import org.geotools.styling.StyledLayerDescriptor;

import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.util.Collections;
import java.util.List;

/**
 * Parses a Yaml/Ysld stream into GeoTools style objects.
 */
public class YsldParser extends YamlParser {
    
    List<ZoomContextFinder> zCtxtFinders = Collections.emptyList();
    
    public YsldParser(InputStream ysld) throws IOException {
        super(ysld);
    }

    public YsldParser(Reader reader) throws IOException {
        super(reader);
    }
    
    public void setZoomContextFinders(List<ZoomContextFinder> zCtxtFinders) {
        this.zCtxtFinders = zCtxtFinders;
    }
    
    public StyledLayerDescriptor parse() throws IOException {
        return super.parse(new RootParser(zCtxtFinders)).sld();
    }
}
