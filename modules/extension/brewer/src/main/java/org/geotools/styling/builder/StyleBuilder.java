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
package org.geotools.styling.builder;

import java.util.ArrayList;
import java.util.List;
import org.geotools.styling.FeatureTypeStyle;
import org.geotools.styling.Style;
import org.geotools.util.SimpleInternationalString;

/** @source $URL$ */
public class StyleBuilder extends AbstractStyleBuilder<Style> {
    List<FeatureTypeStyleBuilder> fts = new ArrayList<FeatureTypeStyleBuilder>();

    String name;

    String styleAbstract;

    String title;

    boolean isDefault;

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
        this.unset = unset;
        FeatureTypeStyleBuilder ftsBuilder = new FeatureTypeStyleBuilder(this);
        fts.add(ftsBuilder);

        return ftsBuilder;
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
        styleAbstract = style.getAbstract();
        title = style.getTitle();
        isDefault = style.isDefault();
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
