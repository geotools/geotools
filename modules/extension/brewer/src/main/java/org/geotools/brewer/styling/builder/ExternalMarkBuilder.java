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

import javax.swing.Icon;
import org.geotools.styling.ExternalMark;
import org.opengis.metadata.citation.OnLineResource;

public class ExternalMarkBuilder extends AbstractStyleBuilder<ExternalMark> {
    private Icon inline;

    private String format;

    private int index;

    private OnLineResource resource;

    public ExternalMarkBuilder() {
        this(null);
    }

    public ExternalMarkBuilder(AbstractStyleBuilder<?> parent) {
        super(parent);
        reset();
    }

    public ExternalMarkBuilder inline(Icon icon) {
        this.unset = false;
        this.inline = icon;
        return this;
    }

    public ExternalMarkBuilder format(String format) {
        this.unset = false;
        this.format = format;
        return this;
    }

    public ExternalMarkBuilder index(int index) {
        this.unset = false;
        this.index = index;
        return this;
    }

    public ExternalMarkBuilder resouce(OnLineResource resource) {
        this.unset = false;
        this.resource = resource;
        return this;
    }

    public ExternalMark build() {
        if (unset) {
            return null;
        }
        if (inline != null) {
            return sf.externalMark(inline);
        } else {
            return sf.externalMark(resource, format, index);
        }
    }

    public ExternalMarkBuilder reset() {
        this.format = null;
        this.resource = null;
        this.index = 0;
        this.inline = null;
        unset = false;
        return this;
    }

    public ExternalMarkBuilder reset(org.opengis.style.ExternalMark mark) {
        if (mark == null) {
            return reset();
        }
        this.format = mark.getFormat();
        this.index = mark.getMarkIndex();
        this.inline = mark.getInlineContent();
        this.resource = mark.getOnlineResource();
        this.unset = false;

        return this;
    }

    public ExternalMarkBuilder reset(ExternalMark mark) {
        if (mark == null) {
            return reset();
        }
        this.format = mark.getFormat();
        this.index = mark.getMarkIndex();
        this.inline = mark.getInlineContent();
        this.resource = mark.getOnlineResource();
        this.unset = false;

        return this;
    }

    public ExternalMarkBuilder unset() {
        return (ExternalMarkBuilder) super.unset();
    }

    @Override
    protected void buildStyleInternal(StyleBuilder sb) {
        sb.featureTypeStyle().rule().point().graphic().mark().externalMark().init(this);
    }
}
