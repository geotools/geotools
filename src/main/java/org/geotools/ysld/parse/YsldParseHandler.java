package org.geotools.ysld.parse;

import org.geotools.util.logging.Logging;

import java.util.logging.Logger;

public class YsldParseHandler extends YamlParseHandler {

    protected static Logger LOG = Logging.getLogger(YsldParser.class);

    protected Factory factory;

    protected YsldParseHandler(Factory factory) {
        this.factory = factory;
    }
}
