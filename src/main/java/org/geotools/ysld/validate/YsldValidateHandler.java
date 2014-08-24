package org.geotools.ysld.validate;

import org.yaml.snakeyaml.events.MappingEndEvent;
import org.yaml.snakeyaml.events.MappingStartEvent;
import org.yaml.snakeyaml.events.ScalarEvent;
import org.yaml.snakeyaml.events.SequenceEndEvent;
import org.yaml.snakeyaml.events.SequenceStartEvent;

/**
 * Handles a Yaml parse event.
 */
public class YsldValidateHandler {

    public void mapping(MappingStartEvent evt, YsldValidateContext context) {
    }

    public void scalar(ScalarEvent evt, YsldValidateContext context) {
    }

    public void sequence(SequenceStartEvent evt, YsldValidateContext context) {
    }

    public void endMapping(MappingEndEvent evt, YsldValidateContext context) {
    }

    public void endSequence(SequenceEndEvent evt, YsldValidateContext context) {
    }
}
