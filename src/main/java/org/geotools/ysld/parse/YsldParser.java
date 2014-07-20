package org.geotools.ysld.parse;

import org.geotools.styling.StyledLayerDescriptor;

import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;

/**
 * Parses a Yaml/Ysld stream into GeoTools style objects.
 */
public class YsldParser extends YamlParser {

    public YsldParser(InputStream ysld) throws IOException {
        super(ysld);
    }

    public YsldParser(Reader reader) throws IOException {
        super(reader);
    }

    public StyledLayerDescriptor parse() throws IOException {
        return super.parse(new RootHandler()).sld();
    }
}
