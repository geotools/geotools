package org.geotools.ysld.parse;

import org.geotools.ysld.YamlObject;
import org.yaml.snakeyaml.events.*;

import java.util.Deque;

/**
 * Handles a parsed Yaml object.
 */
public abstract class YamlParseHandler {

    public abstract void handle(YamlObject<?> obj, YamlParseContext context);
}
