package org.geotools.ysld.parse;

import org.yaml.snakeyaml.events.*;

import java.util.Deque;

/**
 * Handles a Yaml parse event.
 */
public class YamlParseHandler {

    public void mapping(MappingStartEvent evt, Deque<YamlParseHandler> handlers) {
    }

    public void scalar(ScalarEvent evt, Deque<YamlParseHandler> handlers) {
    }

    public void sequence(SequenceStartEvent evt, Deque<YamlParseHandler> handlers) {
    }

    public void endMapping(MappingEndEvent evt, Deque<YamlParseHandler> handlers) {
    }

    public void endSequence(SequenceEndEvent evt, Deque<YamlParseHandler> handlers) {
    }
}
