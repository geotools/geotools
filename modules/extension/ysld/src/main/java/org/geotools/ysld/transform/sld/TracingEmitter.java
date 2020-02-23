/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2016 Open Source Geospatial Foundation (OSGeo)
 *    (C) 2014-2016 Boundless Spatial
 *
 *    This library is free software; you can redistribute it and/or
 *    modify it under the terms of the GNU Lesser General Public
 *    License as published by the Free Software Foundation;
 *    version 2.1 of the License.
 *
 *    This library is distributed in the hope that it will be useful,
 *    but WITHOUT ANY WARRANTY; without even the implied warranty of
 *    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 *    Lesser General Public License for more details.
 */
package org.geotools.ysld.transform.sld;

import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;
import org.yaml.snakeyaml.emitter.Emitable;
import org.yaml.snakeyaml.events.*;

/** Wrapper for a yaml {@link Emitable} which logs {@link Event}s. */
public class TracingEmitter implements Emitable {

    Emitable delegate;

    List<Pair> events = new ArrayList();

    int stack = 0;

    public TracingEmitter(Emitable delegate) {
        this.delegate = delegate;
    }

    @Override
    public void emit(Event event) throws IOException {
        if (event instanceof StreamStartEvent) {
            events.add(new Pair(event, stack++));
        } else if (event instanceof StreamEndEvent) {
            events.add(new Pair(event, --stack));
        } else if (event instanceof DocumentStartEvent) {
            events.add(new Pair(event, stack++));
        } else if (event instanceof DocumentEndEvent) {
            events.add(new Pair(event, --stack));
        } else if (event instanceof ScalarEvent) {
            events.add(new Pair(event, stack));
        } else if (event instanceof MappingStartEvent) {
            events.add(new Pair(event, stack++));
        } else if (event instanceof MappingEndEvent) {
            events.add(new Pair(event, --stack));
        } else if (event instanceof SequenceStartEvent) {
            events.add(new Pair(event, stack++));
        } else if (event instanceof SequenceEndEvent) {
            events.add(new Pair(event, --stack));
        }

        delegate.emit(event);
    }

    /** Writes logged events to out */
    public void dump(PrintStream out) {
        for (Pair p : events) {
            for (int i = 0; i < p.stack; i++) {
                out.print("\t");
            }
            out.println(p.event);
        }
    }

    static class Pair {
        Event event;

        int stack;

        Pair(Event event, int stack) {
            this.event = event;
            this.stack = stack;
        }
    }
}
