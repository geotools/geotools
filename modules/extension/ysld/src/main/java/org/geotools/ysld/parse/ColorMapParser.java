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

import org.geotools.styling.ColorMap;
import org.geotools.styling.ColorMapEntry;
import org.geotools.ysld.Tuple;
import org.geotools.ysld.YamlMap;
import org.geotools.ysld.YamlObject;
import org.geotools.ysld.YamlSeq;

/** Handles the parsing of a Ysld "color-map" property to a {@link ColorMap} object. */
public abstract class ColorMapParser extends YsldParseHandler {

    ColorMap colorMap;

    public ColorMapParser(Factory factory) {
        super(factory);
        colorMap = factory.style.createColorMap();
    }

    @Override
    public void handle(YamlObject<?> obj, YamlParseContext context) {
        colorMap(colorMap);

        YamlMap map = obj.map();
        if (map.has("type")) {
            String value = map.str("type");
            if ("ramp".equals(value)) {
                colorMap.setType(ColorMap.TYPE_RAMP);
            } else if ("intervals".equals(value)) {
                colorMap.setType(ColorMap.TYPE_INTERVALS);
            } else if ("values".equals(value)) {
                colorMap.setType(ColorMap.TYPE_VALUES);
            } else {
                LOG.warning("Unknown color map type: " + value);
            }
        }
        context.push("entries", new EntriesParser());
    }

    protected abstract void colorMap(ColorMap colorMap);

    class EntriesParser extends YsldParseHandler {

        protected EntriesParser() {
            super(ColorMapParser.this.factory);
        }

        @Override
        public void handle(YamlObject<?> obj, YamlParseContext context) {
            YamlSeq seq = obj.seq();
            for (Object o : seq.raw()) {

                Tuple q = null;
                try {
                    q = Tuple.of(4).parse(o);
                } catch (IllegalArgumentException e) {
                    throw new IllegalArgumentException(
                            String.format(
                                    "Bad entry: '%s', must be of form [<color>,[<opacity>],[<value>],[<label>]]",
                                    o.toString()),
                            e);
                }

                ColorMapEntry e = factory.style.createColorMapEntry();
                if (q.at(0) != null) {
                    e.setColor(Util.color(Util.stripQuotes(q.strAt(0)), factory));
                }
                if (q.at(1) != null) {
                    e.setOpacity(Util.expression(q.strAt(1), factory));
                }
                if (q.at(2) != null) {
                    e.setQuantity(Util.expression(q.strAt(2), factory));
                }
                if (q.at(3) != null) {
                    e.setLabel(q.strAt(3));
                }

                colorMap.addColorMapEntry(e);
            }
        }
    }
}
