package org.geotools.ysld.encode;

import org.geotools.styling.ColorMap;
import org.geotools.styling.ColorMapEntry;
import org.geotools.styling.ContrastEnhancement;
import org.geotools.styling.RasterSymbolizer;
import org.geotools.ysld.Tuple;

import java.util.Arrays;
import java.util.Iterator;

public class RasterSymbolizerEncoder extends SymbolizerEncoder<RasterSymbolizer> {
    public RasterSymbolizerEncoder(RasterSymbolizer sym) {
        super(sym);
    }

    @Override
    protected void encode(RasterSymbolizer sym) {
        put("opacity", sym.getOpacity());
        if (sym.getColorMap() != null) {
            inline(new ColorMapEncoder(sym.getColorMap()));
        }

        if (sym.getContrastEnhancement() != null) {
            inline(new ContrastEnhancementEncoder(sym.getContrastEnhancement()));
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
            switch(colorMap.getType()) {
                case ColorMap.TYPE_INTERVALS:
                    put("type", "intervals");
                    break;
                case ColorMap.TYPE_RAMP:
                    put("type", "ramp");
                    break;
                case ColorMap.TYPE_VALUES:
                    put("type", "values");
                    break;
            }

            put("entries", new ColorMapEntryIterator(colorMap));
        }
    }

    class ColorMapEntryIterator implements Iterator<String> {

        Iterator<ColorMapEntry> entries;

        public ColorMapEntryIterator(ColorMap colorMap) {
            entries = Arrays.asList(colorMap.getColorMapEntries()).iterator();
        }

        @Override
        public boolean hasNext() {
            return entries.hasNext();
        }

        @Override
        public String next() {
            ColorMapEntry entry = entries.next();

            return Tuple.of(toColorOrNull(entry.getColor()), toObjOrNull(entry.getOpacity()),
                toObjOrNull(entry.getQuantity()), entry.getLabel()).toString();
        }

        @Override
        public void remove() {
            throw new UnsupportedOperationException();
        }
    }

    class ContrastEnhancementEncoder extends YsldEncodeHandler<ContrastEnhancement> {
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
}
