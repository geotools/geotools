/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2014, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.brewer.styling.builder;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.geotools.styling.FeatureTypeStyle;
import org.geotools.styling.Style;
import org.geotools.util.SimpleInternationalString;

public class StyleBuilder extends AbstractStyleBuilder<Style> {
    List<FeatureTypeStyleBuilder> fts = new ArrayList<FeatureTypeStyleBuilder>();

    String name;

    String styleAbstract;

    String title;

    boolean isDefault;

    FillBuilder background;

    public StyleBuilder() {
        super(null);
        reset();
    }

    StyleBuilder(AbstractSLDBuilder<?> parent) {
        super(parent);
        reset();
    }

    public StyleBuilder name(String name) {
        this.name = name;
        return this;
    }

    public StyleBuilder title(String title) {
        this.title = title;
        return this;
    }

    public StyleBuilder styleAbstract(String styleAbstract) {
        this.styleAbstract = styleAbstract;
        return this;
    }

    public FeatureTypeStyleBuilder featureTypeStyle() {
        this.unset = false;
        FeatureTypeStyleBuilder ftsBuilder = new FeatureTypeStyleBuilder(this);
        fts.add(ftsBuilder);

        return ftsBuilder;
    }

    public FillBuilder background() {
        this.unset = false;
        this.background = new FillBuilder();

        return background;
    }

    public Style build() {
        if (unset) {
            return null;
        }

        Style s;
        if (fts.size() == 0) {
            s = sf.createNamedStyle();
            s.setName(name);
        } else {
            s = sf.createStyle();
            s.setName(name);
            if (styleAbstract != null)
                s.getDescription().setAbstract(new SimpleInternationalString(styleAbstract));
            if (title != null) s.getDescription().setTitle(new SimpleInternationalString(title));
            for (FeatureTypeStyleBuilder builder : fts) {
                s.featureTypeStyles().add(builder.build());
            }
            s.setDefault(isDefault);
        }
        if (background != null) {
            s.setBackground(background.build());
        }

        reset();
        return s;
    }

    public StyleBuilder unset() {
        return (StyleBuilder) super.unset();
    }

    public StyleBuilder reset() {
        fts.clear();
        name = null;
        styleAbstract = null;
        title = null;
        isDefault = false;
        background = null;
        unset = false;
        return this;
    }

    public StyleBuilder reset(Style style) {
        if (style == null) {
            return unset();
        }
        fts.clear();
        for (FeatureTypeStyle ft : style.featureTypeStyles()) {
            fts.add(new FeatureTypeStyleBuilder(this).reset(ft));
        }
        name = style.getName();
        styleAbstract =
                Optional.ofNullable(style.getDescription().getAbstract())
                        .map(Object::toString)
                        .orElse(null);
        title =
                Optional.ofNullable(style.getDescription().getTitle())
                        .map(Object::toString)
                        .orElse(null);
        isDefault = style.isDefault();
        background = new FillBuilder().reset(style.getBackground());
        unset = false;
        return this;
    }

    @Override
    public Style buildStyle() {
        return build();
    }

    @Override
    protected void buildStyleInternal(StyleBuilder sb) {
        // no-op
    }
}
