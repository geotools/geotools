/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2002-2008, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.styling;

import javax.swing.Icon;
import org.opengis.metadata.citation.OnLineResource;
import org.opengis.style.StyleVisitor;

/**
 * Default implementation of ExternalMark.
 *
 * @version $Id$
 */
public class ExternalMarkImpl implements org.geotools.styling.ExternalMark {

    private OnLineResource onlineResource;
    private Icon inlineContent;
    private int index;
    private String format;

    public ExternalMarkImpl() {}

    public ExternalMarkImpl(Icon icon) {
        this.inlineContent = icon;
        this.index = -1;
        this.onlineResource = null;
        this.format = null;
    }

    public ExternalMarkImpl(OnLineResource resource, String format, int markIndex) {
        this.inlineContent = null;
        this.index = markIndex;
        this.onlineResource = resource;
        this.format = format;
    }

    @Override
    public String getFormat() {
        return format;
    }

    @Override
    public Icon getInlineContent() {
        return inlineContent;
    }

    @Override
    public int getMarkIndex() {
        return index;
    }

    @Override
    public OnLineResource getOnlineResource() {
        return onlineResource;
    }

    @Override
    public Object accept(StyleVisitor visitor, Object extraData) {
        return visitor.visit(this, extraData);
    }

    @Override
    public void setInlineContent(Icon inline) {
        this.inlineContent = inline;
    }

    @Override
    public void setFormat(String mimeType) {
        this.format = mimeType;
    }

    @Override
    public void setMarkIndex(int markIndex) {
        this.index = markIndex;
    }

    @Override
    public void setOnlineResource(OnLineResource resource) {
        this.onlineResource = resource;
    }

    static ExternalMarkImpl cast(org.opengis.style.ExternalMark mark) {
        if (mark == null) {
            return null;
        } else if (mark instanceof ExternalMarkImpl) {
            return (ExternalMarkImpl) mark;
        } else {
            ExternalMarkImpl copy = new ExternalMarkImpl();
            copy.setFormat(mark.getFormat());
            copy.setMarkIndex(mark.getMarkIndex());
            copy.setOnlineResource(mark.getOnlineResource());
            return copy;
        }
    }
}
