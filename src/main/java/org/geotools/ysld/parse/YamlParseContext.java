package org.geotools.ysld.parse;

import org.yaml.snakeyaml.events.Event;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Iterator;

public class YamlParseContext {

    Iterator<Event> events;
    Deque<YamlParseHandler> handlers;
    Event event;
    boolean pause;

    public YamlParseContext(Iterator<Event> events) {
        this.events = events;
        this.handlers = new ArrayDeque<YamlParseHandler>();
        this.event = null;
        this.pause = false;
    }

    public YamlParseContext push(YamlParseHandler handler) {
        handlers.push(handler);
        return this;
    }

    public YamlParseContext pause() {
        pause = true;
        return this;
    }

    public YamlParseContext pop() {
        handlers.pop();
        return this;
    }

    public boolean hasMoreEvents() {
        try {
            if (pause) {
                return true;
            }
            else {
                if (events.hasNext()) {
                    event = events.next();
                    return true;
                }
                return false;
            }
        }
        finally {
            pause = false;
        }
    }

    public Event event() {
        return event;
    }

    public YamlParseHandler handler() {
        return handlers.peek();
    }
}
