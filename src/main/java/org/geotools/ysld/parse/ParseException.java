package org.geotools.ysld.parse;

import org.yaml.snakeyaml.events.Event;

public class ParseException extends RuntimeException {

    Event event;

    public ParseException(String msg, Event event) {
        super(msg);
        this.event = event;
    }

    public ParseException(String msg, Event event, Throwable cause) {
        super(msg, cause);
        this.event = event;
    }

    public Event getEvent() {
        return event;
    }
}
