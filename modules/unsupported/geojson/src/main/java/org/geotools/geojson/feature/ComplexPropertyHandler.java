/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2002-2010, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.geojson.feature;

import java.io.IOException;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Stack;
import java.util.concurrent.Callable;

import org.geotools.geojson.HandlerBase;
import org.geotools.geojson.IContentHandler;
import org.json.simple.parser.ParseException;

/**
 * 
 *
 * @source $URL$
 */
public class ComplexPropertyHandler extends HandlerBase implements IContentHandler<Object> {

    Object result = null;

    Deque<Object> destinationStack = new ArrayDeque<>();
    String objectKey = null;

    public ComplexPropertyHandler() {
    }

    void pushState(Object destination) {
        destinationStack.push(destination);
    }

    void popState() {
        destinationStack.pop();
    }

    public boolean keepGoing() {
        return ! destinationStack.isEmpty();
    }

    void addValue(Object obj) {
        if (result == null)
        {
            this.result = obj;
            return;
        }
        if (objectKey != null) {
            ((Map)destinationStack.peek()).put(objectKey, obj);
            objectKey = null;
        } else {
            ((List)destinationStack.peek()).add(obj);
        }
    }

    @Override
    public boolean startArray() throws ParseException, IOException {
        final ArrayList<Object> destination = new ArrayList<>();

        addValue(destination);
        pushState(destination);
        return true;
    }

    @Override
    public boolean endArray() throws ParseException, IOException {
        popState();
        return keepGoing();
    }

    @Override
    public boolean startObject() throws ParseException, IOException {
        final LinkedHashMap<String, Object> destinationObject = new LinkedHashMap<>();
        addValue(destinationObject);
        pushState(destinationObject);
        return true;
    }
    
    @Override
    public boolean endObject() throws ParseException, IOException {
        popState();
        return keepGoing();
    }

    @Override
    public boolean startObjectEntry(String key) throws ParseException, IOException {
        this.objectKey = key; // Keep until next call
        return true;
    }

    @Override
    public boolean primitive(Object value) throws ParseException, IOException {
        addValue(value);
        return true;
    }


    public Object getValue() {
        return result;
    }

}
