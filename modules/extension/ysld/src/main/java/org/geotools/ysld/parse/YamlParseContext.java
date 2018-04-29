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
package org.geotools.ysld.parse;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.HashMap;
import java.util.Map;
import javax.annotation.Nullable;
import org.geotools.ysld.YamlMap;
import org.geotools.ysld.YamlObject;

/**
 * Holds the context of a call to {@link YamlParser#parse(YamlParseHandler, Map)} during its descent
 * into the {@link YamlObject} being parsed.
 */
public class YamlParseContext {
    /*
     * Handlers may handle a YAML object as a whole, or they may handle some specific subset of properties for one. The former delegates the the
     * latter by pushing the latter on to the stack with the current object. Example: TextHandler handling X pushes a FontHandler with X as the
     * current node.
     *
     * Odd design resulted from initially planning to do stream parsing then recycling the code for a in memory parser.
     */

    Deque<Entry> stack;

    Entry curr;

    Map<String, Object> docHints = new HashMap<>();

    public YamlParseContext() {
        stack = new ArrayDeque<Entry>();
    }

    /**
     * Parse a child of the current object if present
     *
     * @param key key of the child entry
     * @param handler handler to use
     * @return self
     */
    public YamlParseContext push(String key, YamlParseHandler handler) {
        return push(curr.obj, key, handler);
    }

    /**
     * Parse a child of the specified object if present
     *
     * @param scope object to start from
     * @param key key of the child entry
     * @param handler handler to use
     * @return self
     */
    public YamlParseContext push(YamlObject scope, String key, YamlParseHandler handler) {
        YamlMap map = scope.map();
        if (map.has(key)) {
            return doPush(scope.map().obj(key), handler);
        }
        return this;
    }

    /**
     * Add a handler to the stack handling the current object. Used for "inlined"/common properties.
     *
     * @param handler handler to use
     * @return self
     */
    public YamlParseContext push(YamlParseHandler handler) {
        return doPush(curr.obj, handler);
    }

    /**
     * Add a handler to the stack handling the specified object
     *
     * @param obj the object to parse
     * @param handler handler to use
     * @return self
     */
    public YamlParseContext push(YamlObject obj, YamlParseHandler handler) {
        return doPush(obj, handler);
    }

    YamlParseContext doPush(YamlObject obj, YamlParseHandler handler) {
        stack.push(new Entry(obj, handler));
        return this;
    }

    public YamlParseContext pop() {
        stack.pop();
        return this;
    }

    /**
     * Pop a {@link YamlParseHandler} from the handler stack and execute its {@link
     * YamlParseHandler#handle(YamlObject, YamlParseContext)} method on the {@link YamlObject} with
     * which it was pushed.
     *
     * @return True if more handlers remain on handler stack; false if the handler stack is empty.
     */
    public boolean next() {
        curr = stack.pop();
        curr.handler.handle(curr.obj, this);
        return !stack.isEmpty();
    }

    public @Nullable Object getDocHint(String key) {
        return docHints.get(key);
    }

    public void setDocHint(String key, Object value) {
        docHints.put(key, value);
    }

    public void mergeDocHints(Map<String, Object> hints) {
        docHints.putAll(hints);
    }

    /**
     * Container object for a {@link YamlParseHandler} and the {@link YamlObject} it should handle.
     * Instances of this class are added to the stack in the {@link YamlParseContext} by a {@link
     * YamlParseHandler} as it descends into the {@link YamlObject} it is parsing.
     */
    static class Entry {
        YamlObject obj;

        YamlParseHandler handler;

        Entry(YamlObject obj, YamlParseHandler handler) {
            this.obj = obj;
            this.handler = handler;
        }
    }
}
