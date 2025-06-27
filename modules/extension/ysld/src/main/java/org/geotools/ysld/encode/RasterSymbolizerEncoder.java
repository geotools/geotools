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
package org.geotools.ysld.encode;

import java.util.Arrays;
import java.util.Iterator;
import org.geotools.api.style.ChannelSelection;
import org.geotools.api.style.ColorMap;
import org.geotools.api.style.ColorMapEntry;
import org.geotools.api.style.ContrastEnhancement;
import org.geotools.api.style.ContrastMethod;
import org.geotools.api.style.RasterSymbolizer;
import org.geotools.api.style.SelectedChannelType;
import org.geotools.ysld.Band;
import org.geotools.ysld.Tuple;

/** Encodes a {@link RasterSymbolizer} as YSLD. */
public class RasterSymbolizerEncoder extends SymbolizerEncoder<RasterSymbolizer> {
    public RasterSymbolizerEncoder(RasterSymbolizer sym) {
        super(sym);
    }

    private boolean emptyColourMap(ColorMap map) {
        if (map == null) return true;
        ColorMapEntry[] entries = map.getColorMapEntries();
        if (entries == null) return true;
        return map.getColorMapEntries().length == 0;
    }

    private boolean emptyContrastEnhancement(ContrastEnhancement ch) {
        if (ch == null) return true;
        if (ch.getMethod() != null && ch.getMethod() != ContrastMethod.NONE) return false;
        if (ch.getGammaValue() != null) return false;
        return true;
    }

    private boolean emptyChannelSelection(ChannelSelection ch) {
        if (ch == null) return true;
        for (Band b : Band.values()) {
            if (b.getFrom(ch) != null) return false;
        }
        return true;
    }

    @Override
    protected void encode(RasterSymbolizer sym) {
        put("opacity", sym.getOpacity());
        if (!emptyColourMap(sym.getColorMap())) {
            inline(new ColorMapEncoder(sym.getColorMap()));
        }

        if (!emptyContrastEnhancement(sym.getContrastEnhancement())) {
            inline(new ContrastEnhancementEncoder(sym.getContrastEnhancement()));
        }
        if (!emptyChannelSelection(sym.getChannelSelection())) {
            inline(new ChannelSelectionEncoder(sym.getChannelSelection()));
        }

        super.encode(sym);
    }

    class ColorMapEncoder extends YsldEncodeHandler<ColorMap> {

        ColorMapEncoder(ColorMap colorMap) {
            super(colorMap);
        }

        @Override
        protected void encode(ColorMap colorMap) {
            push("color-map");
            switch (colorMap.getType()) {
                case org.geotools.api.style.ColorMap.TYPE_INTERVALS:
                    put("type", "intervals");
                    break;
                case org.geotools.api.style.ColorMap.TYPE_RAMP:
                    put("type", "ramp");
                    break;
                case org.geotools.api.style.ColorMap.TYPE_VALUES:
                    put("type", "values");
                    break;
            }

            put("entries", new ColorMapEntryIterator(colorMap));
        }
    }

    class ColorMapEntryIterator implements Iterator<Tuple> {

        Iterator<ColorMapEntry> entries;

        public ColorMapEntryIterator(ColorMap colorMap) {
            entries = Arrays.asList(colorMap.getColorMapEntries()).iterator();
        }

        @Override
        public boolean hasNext() {
            return entries.hasNext();
        }

        @Override
        public Tuple next() {
            ColorMapEntry entry = entries.next();

            return Tuple.of(
                    toColorOrNull(entry.getColor()),
                    toObjOrNull(entry.getOpacity()),
                    toObjOrNull(entry.getQuantity()),
                    entry.getLabel());
        }

        @Override
        public void remove() {
            throw new UnsupportedOperationException();
        }
    }

    static class ContrastEnhancementEncoder extends YsldEncodeHandler<ContrastEnhancement> {
        public ContrastEnhancementEncoder(ContrastEnhancement contrast) {
            super(contrast);
        }

        @Override
        protected void encode(ContrastEnhancement contrast) {
            push("contrast-enhancement");
            if (contrast.getMethod() != null) {
                put("mode", contrast.getMethod().name().toLowerCase());
            }
            put("gamma", contrast.getGammaValue());
        }
    }

    class ChannelSelectionEncoder extends YsldEncodeHandler<ChannelSelection> {

        public ChannelSelectionEncoder(ChannelSelection obj) {
            super(obj);
        }

        @Override
        protected void encode(ChannelSelection next) {
            push("channels");
            for (Band band : Band.values()) {
                SelectedChannelType channel = band.getFrom(next);
                if (channel != null) {
                    inline(new SelectedChannelTypeEncoder(band, channel));
                }
            }
        }
    }

    class SelectedChannelTypeEncoder extends YsldEncodeHandler<SelectedChannelType> {

        private Band band;

        public SelectedChannelTypeEncoder(Band band, SelectedChannelType it) {
            super(it);
            this.band = band;
        }

        @Override
        protected void encode(SelectedChannelType channel) {
            push(band.key);
            put("name", channel.getChannelName());
            if (!emptyContrastEnhancement(channel.getContrastEnhancement())) {
                inline(new ContrastEnhancementEncoder(channel.getContrastEnhancement()));
            }
        }
    }
}
