package org.geotools.ysld.transform.ysld;

import org.geotools.ysld.parse.YamlParser;
import org.xml.sax.ContentHandler;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;

public class YsldTransformer extends YamlParser {

    ContentHandler xml;
    public YsldTransformer(InputStream yaml, ContentHandler xml) throws IOException {
        super(yaml);
        this.xml = xml;
    }

    public YsldTransformer(Reader yaml, ContentHandler xml) throws IOException {
        super(yaml);
        this.xml = xml;
    }

    public void transform() throws IOException, SAXException {
        //doParse();
    }

}
