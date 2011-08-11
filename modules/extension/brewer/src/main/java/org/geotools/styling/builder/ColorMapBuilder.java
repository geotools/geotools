package org.geotools.styling.builder;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.geotools.styling.ColorMap;
import org.geotools.styling.ColorMapEntry;

public class ColorMapBuilder extends AbstractStyleBuilder<ColorMap> {

    int type = ColorMap.TYPE_RAMP;

    boolean extended = false;

    List<ColorMapEntry> entries = new ArrayList<ColorMapEntry>();

    ColorMapEntryBuilder colorMapEntryBuilder = null;

    public ColorMapBuilder() {
        this(null);
    }

    public ColorMapBuilder(AbstractStyleBuilder<?> parent) {
        super(parent);
        reset();
    }

    public ColorMapBuilder type(int type) {
        this.type = type;
        unset = false;
        return this;
    }

    public ColorMapBuilder extended(boolean extended) {
        this.extended = extended;
        unset = false;
        return this;
    }

    public ColorMapEntryBuilder entry() {
        if (colorMapEntryBuilder != null && !colorMapEntryBuilder.isUnset()) {
            entries.add(colorMapEntryBuilder.build());
            unset = false;
        }
        colorMapEntryBuilder = new ColorMapEntryBuilder();
        return colorMapEntryBuilder;
    }

    public ColorMap build() {
        // force the dump of the last entry builder
        entry();

        if (unset) {
            return null;
        }
        ColorMap colorMap = sf.createColorMap();
        colorMap.setType(type);
        colorMap.setExtendedColors(extended);
        for (ColorMapEntry entry : entries) {
            colorMap.addColorMapEntry(entry);
        }
        if (parent == null) {
            reset();
        }
        return colorMap;
    }

    public ColorMapBuilder reset() {
        type = ColorMap.TYPE_RAMP;
        extended = false;
        entries = new ArrayList<ColorMapEntry>();
        unset = false;
        return this;
    }

    public ColorMapBuilder reset(ColorMap original) {
        if (original == null) {
            return reset();
        }
        type = original.getType();
        extended = original.getExtendedColors();
        entries = new ArrayList<ColorMapEntry>(Arrays.asList(original.getColorMapEntries()));
        unset = false;
        return this;
    }

    public ColorMapBuilder unset() {
        return (ColorMapBuilder) super.unset();
    }

    @Override
    protected void buildStyleInternal(StyleBuilder sb) {
        sb.featureTypeStyle().rule().raster().colorMap().init(this);
    }

}
