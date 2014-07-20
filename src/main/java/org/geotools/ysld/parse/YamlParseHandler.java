package org.geotools.ysld.parse;

import org.geotools.ysld.YamlObject;
import org.yaml.snakeyaml.events.*;

import java.util.Deque;

/**
 * Handles a Yaml parse event.
 */
public abstract class YamlParseHandler {

//    public void mapping(MappingStartEvent evt, YamlParseContext context) {
//    }
//
//    public void scalar(ScalarEvent evt, YamlParseContext context) {
//    }
//
//    public void sequence(SequenceStartEvent evt, YamlParseContext context) {
//    }
//
//    public void endMapping(MappingEndEvent evt, YamlParseContext context) {
//    }
//
//    public void endSequence(SequenceEndEvent evt, YamlParseContext context) {
//    }

    public abstract void handle(YamlObject<?> obj, YamlParseContext context);
}
