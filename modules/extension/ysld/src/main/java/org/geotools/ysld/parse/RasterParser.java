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

import java.util.Map;
import org.geotools.styling.*;
import org.geotools.ysld.Band;
import org.geotools.ysld.YamlMap;
import org.geotools.ysld.YamlObject;
import org.opengis.style.ContrastMethod;

/** Handles the parsing of a Ysld "raster" symbolizer property into a {@link Symbolizer} object. */
public class RasterParser extends SymbolizerParser<RasterSymbolizer> {

    public RasterParser(Rule rule, Factory factory) {
        super(rule, factory.style.createRasterSymbolizer(), factory);
    }

    @Override
    public void handle(YamlObject<?> obj, YamlParseContext context) {
        super.handle(obj, context);

        YamlMap map = obj.map();

        if (map.has("opacity")) {
            sym.setOpacity(Util.expression(map.str("opacity"), factory));
        }

        context.push(
                "color-map",
                new ColorMapParser(factory) {
                    @Override
                    protected void colorMap(ColorMap colorMap) {
                        sym.setColorMap(colorMap);
                    }
                });
        context.push("contrast-enhancement", new ContrastEnhancementHandler());
        context.push("channels", new ChannelsHandler());
    }

    class ContrastEnhancementHandler extends YsldParseHandler {

        ContrastEnhancement contrast;

        protected ContrastEnhancementHandler() {
            super(RasterParser.this.factory);
            contrast = factory.style.createContrastEnhancement();
            set();
        }

        protected void set() {
            sym.setContrastEnhancement(contrast);
        }

        @Override
        public void handle(YamlObject<?> obj, YamlParseContext context) {
            YamlMap map = obj.map();
            if (map.has("mode")) {
                String mode = map.str("mode");
                ContrastMethod method = ContrastMethod.valueOf(mode);
                if (method != null) {
                    contrast.setMethod(method);
                } else {
                    LOG.warning("Unknown contrast method: " + mode);
                }
            }
            if (map.has("gamma")) {
                contrast.setGammaValue(Util.expression(map.str("gamma"), factory));
            }
        }
    }

    class ChannelsHandler extends YsldParseHandler {

        ChannelSelection selection;

        protected ChannelsHandler() {
            super(RasterParser.this.factory);
            this.selection = sym.getChannelSelection();
        }

        void parse(Band band, SelectedChannelType sel, YamlMap map, YamlParseContext context) {
            if (map.get(band.key) instanceof Map) {
                context.push(band.key, new SelectedChannelHandler(sel));
            } else {
                sel.setChannelName(map.str(band.key));
            }
        }

        @Override
        public void handle(YamlObject<?> obj, YamlParseContext context) {

            YamlMap map = obj.map();
            if (map.has(Band.GRAY.key)) {
                if (map.has(Band.RED.key) || map.has(Band.GREEN.key) || map.has(Band.BLUE.key))
                    throw new IllegalArgumentException("grey and RGB can not be combined");
                SelectedChannelType gray = factory.style.selectedChannelType((String) null, null);
                selection.setGrayChannel(gray);
                parse(Band.GRAY, gray, map, context);
            } else {
                if (!(map.has(Band.RED.key) && map.has(Band.GREEN.key) && map.has(Band.BLUE.key)))
                    throw new IllegalArgumentException("all of red green and blue must be preset");
                SelectedChannelType red = factory.style.selectedChannelType((String) null, null);
                SelectedChannelType green = factory.style.selectedChannelType((String) null, null);
                SelectedChannelType blue = factory.style.selectedChannelType((String) null, null);
                selection.setRGBChannels(red, green, blue);
                parse(Band.RED, red, map, context);
                parse(Band.GREEN, green, map, context);
                parse(Band.BLUE, blue, map, context);
            }
        }
    }

    class SelectedChannelHandler extends YsldParseHandler {
        SelectedChannelType sel;

        public SelectedChannelHandler(SelectedChannelType sel) {
            super(RasterParser.this.factory);
            this.sel = sel;
        }

        @Override
        public void handle(YamlObject<?> obj, YamlParseContext context) {
            String name = obj.map().str("name");
            sel.setChannelName(name);
            context.push(
                    "contrast-enhancement",
                    new ContrastEnhancementHandler() {

                        @Override
                        protected void set() {
                            sel.setContrastEnhancement(this.contrast);
                        }
                    });
        }
    }
}
