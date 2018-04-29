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
package org.geotools.ysld.validate;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Deque;
import java.util.List;
import org.geotools.ysld.parse.Factory;
import org.geotools.ysld.parse.Util;
import org.geotools.ysld.parse.ZoomContext;
import org.geotools.ysld.parse.ZoomContextFinder;
import org.yaml.snakeyaml.error.Mark;
import org.yaml.snakeyaml.error.MarkedYAMLException;

/**
 * Validation context for {@link YsldValidator}
 *
 * <p>Manages the validation stack, applies handlers, and tracks errors.
 */
public class YsldValidateContext {

    Deque<YsldValidateHandler> handlers = new ArrayDeque<>();

    List<MarkedYAMLException> errors = new ArrayList<>();

    Factory factory = new Factory();

    List<ZoomContextFinder> zCtxtFinders = Collections.emptyList();

    ZoomContext zCtxt;

    public List<MarkedYAMLException> errors() {
        return errors;
    }

    public YsldValidateContext error(String problem, Mark mark) {
        return error(new MarkedYAMLException(null, null, problem, mark) {});
    }

    public YsldValidateContext error(MarkedYAMLException e) {
        errors.add(e);
        return this;
    }

    public YsldValidateHandler peek() {
        return handlers.peek();
    }

    public void pop() {
        handlers.pop();
    }

    public void push(YsldValidateHandler handler) {
        handlers.push(handler);
    }

    public ZoomContext getZCtxt() {
        if (zCtxt == null) {
            return Util.getNamedZoomContext("DEFAULT", zCtxtFinders);
        } else {
            return zCtxt;
        }
    }
}
