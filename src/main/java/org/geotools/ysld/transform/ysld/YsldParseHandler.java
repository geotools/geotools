package org.geotools.ysld.transform.ysld;

import org.geotools.ysld.parse.YamlParseHandler;
import org.xml.sax.ContentHandler;

public class YsldParseHandler extends YamlParseHandler {

    protected ContentHandler xml;

    public YsldParseHandler(ContentHandler xml) {
        this.xml = xml;
    }
}
