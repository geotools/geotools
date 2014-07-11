package org.geotools.ysld.parse;

import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.events.*;
import org.yaml.snakeyaml.reader.UnicodeReader;

import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Iterator;

/**
 * Base Yaml parsing class.
 */
public class YamlParser {

    YamlParseContext context;

    public YamlParser(InputStream ysld) throws IOException {
        this(new UnicodeReader(ysld));
    }

    public YamlParser(Reader ysld) throws IOException{
        context = new YamlParseContext(new Yaml().parse(ysld).iterator());
    }

    public void doParse(YamlParseHandler root) throws IOException {
        context.push(root);

        while(context.hasMoreEvents()) {
            YamlParseHandler h = context.handler();

            Event evt = context.event();
            if (evt instanceof MappingStartEvent) {
                h.mapping((MappingStartEvent)evt, context);
            }
            else if (evt instanceof ScalarEvent) {
                h.scalar((ScalarEvent)evt, context);
            }
            else if (evt instanceof SequenceStartEvent) {
                h.sequence((SequenceStartEvent)evt, context);
            }
            else if (evt instanceof MappingEndEvent) {
                h.endMapping((MappingEndEvent)evt, context);
            }
            else if (evt instanceof SequenceEndEvent) {
                h.endSequence((SequenceEndEvent)evt, context);
            }
        }
    }
}
