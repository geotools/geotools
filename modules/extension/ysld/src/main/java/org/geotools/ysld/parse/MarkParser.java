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

import org.geotools.styling.*;
import org.geotools.ysld.YamlMap;
import org.geotools.ysld.YamlObject;

/** Handles parsing a Ysld "mark" property into a {@link Mark} object. */
public abstract class MarkParser extends YsldParseHandler {

    Mark mark;

    protected MarkParser(Factory factory) {
        super(factory);
        mark = factory.style.createMark();
        mark.setStroke(null);
        mark.setFill(null);
    }

    @Override
    public void handle(YamlObject<?> obj, YamlParseContext context) {
        mark(mark);

        YamlMap map = obj.map();

        if (map.has("shape")) {
            mark.setWellKnownName(Util.expression(map.str("shape"), factory));
        }

        context.push(
                new StrokeParser(factory) {
                    @Override
                    protected void stroke(Stroke stroke) {
                        mark.setStroke(stroke);
                    }
                });
        context.push(
                new FillParser(factory) {
                    @Override
                    protected void fill(Fill fill) {
                        mark.setFill(fill);
                    }
                });
    }

    protected abstract void mark(Mark mark);
}
