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
import java.io.Writer;
import java.util.ArrayDeque;
import java.util.Deque;
import org.geotools.util.Version;
import org.geotools.ysld.Tuple;
import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.emitter.Emitable;
import org.yaml.snakeyaml.emitter.Emitter;
import org.yaml.snakeyaml.events.*;

/**
 * Context for {@link SldTransformer}
 *
 * <p>Handles the Yaml Stack during transformation and applies {@link SldTransformHandler}s. Tracks
 * SLD version.
 */
class SldTransformContext {

    public static final Version V_100 = new Version("1.0.0");

    public static final Version V_110 = new Version("1.1.0");

    static final Version DEFAULT_VERSION = new Version("1.0.0");

    Version version = DEFAULT_VERSION;

    Writer output;

    Emitable yaml;

    boolean moveToNext;

    Deque<SldTransformHandler> handlers;

    SldTransformHandler last;

    public SldTransformContext(Writer output) {
        this.output = output;

        DumperOptions dumpOpts = new DumperOptions();
        dumpOpts.setDefaultFlowStyle(DumperOptions.FlowStyle.BLOCK);

        yaml = new Emitter(output, dumpOpts);

        handlers = new ArrayDeque<SldTransformHandler>();
    }

    public void trace() {
        yaml = new TracingEmitter(yaml);
    }

    public Emitable emitter() {
        return yaml;
    }

    public Writer output() {
        return output;
    }

    public SldTransformContext version(String ver) {
        version = new Version(ver);
        return this;
    }

    public Version version() {
        return version;
    }

    public SldTransformContext reset() {
        moveToNext = true;
        return this;
    }

    public SldTransformContext push(SldTransformHandler handler) {
        handlers.push(handler);
        moveToNext = false;
        return this;
    }

    public SldTransformContext pop() {
        moveToNext = false;
        last = handlers.pop();
        return this;
    }

    public SldTransformHandler last() {
        return last;
    }

    public SldTransformContext stream() throws IOException {
        yaml.emit(new StreamStartEvent(null, null));
        return this;
    }

    public SldTransformContext document() throws IOException {
        yaml.emit(new DocumentStartEvent(null, null, false, null, null));
        return this;
    }

    public SldTransformContext mapping() throws IOException {
        yaml.emit(
                new MappingStartEvent(null, null, true, null, null, DumperOptions.FlowStyle.BLOCK));
        return this;
    }

    public SldTransformContext scalar(String value) throws IOException {
        yaml.emit(
                new ScalarEvent(
                        null,
                        null,
                        new ImplicitTuple(true, false),
                        value,
                        null,
                        null,
                        DumperOptions.ScalarStyle.PLAIN));
        return this;
    }

    public SldTransformContext sequence() throws IOException {
        yaml.emit(
                new SequenceStartEvent(
                        null, null, true, null, null, DumperOptions.FlowStyle.BLOCK));
        return this;
    }

    public SldTransformContext endSequence() throws IOException {
        yaml.emit(new SequenceEndEvent(null, null));
        return this;
    }

    public SldTransformContext endMapping() throws IOException {
        yaml.emit(new MappingEndEvent(null, null));
        return this;
    }

    public SldTransformContext endDocument() throws IOException {
        yaml.emit(new DocumentEndEvent(null, null, true));
        return this;
    }

    public SldTransformContext endStream() throws IOException {
        yaml.emit(new StreamEndEvent(null, null));
        output.flush();
        return this;
    }

    public SldTransformContext tuple(String first, String second) throws IOException {
        return scalar(Tuple.of(first, second).toString());
    }
}
