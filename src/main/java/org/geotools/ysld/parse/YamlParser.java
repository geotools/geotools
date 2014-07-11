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

    Iterator<Event> events;
    Deque<YamlParseHandler> handlers = new ArrayDeque<YamlParseHandler>();

    public YamlParser(InputStream ysld) throws IOException {
        this(new UnicodeReader(ysld));
    }

    public YamlParser(Reader ysld) throws IOException{
        events = new Yaml().parse(ysld).iterator();
    }

    public void doParse(YamlParseHandler root) throws IOException {
        handlers.push(root);

        while(events.hasNext()) {
            YamlParseHandler h = handlers.peek();

            Event evt = events.next();
            if (evt instanceof MappingStartEvent) {
                h.mapping((MappingStartEvent)evt, handlers);
            }
            else if (evt instanceof ScalarEvent) {
                h.scalar((ScalarEvent)evt, handlers);
            }
            else if (evt instanceof SequenceStartEvent) {
                h.sequence((SequenceStartEvent)evt, handlers);
            }
            else if (evt instanceof MappingEndEvent) {
                h.endMapping((MappingEndEvent)evt, handlers);
            }
            else if (evt instanceof SequenceEndEvent) {
                h.endSequence((SequenceEndEvent)evt, handlers);
            }
        }
    }
}
