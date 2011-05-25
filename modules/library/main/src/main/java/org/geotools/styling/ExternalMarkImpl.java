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
 *
 * @source $URL: http://svn.osgeo.org/geotools/branches/2.7.x/build/maven/javadoc/../../../modules/library/main/src/main/java/org/geotools/styling/ExternalMarkImpl.java $
 * @version $Id: MarkImpl.java 31133 2008-08-05 15:20:33Z johann.sorel $
 */
public class ExternalMarkImpl implements org.geotools.styling.ExternalMark {

    private OnLineResource onlineResource;
    private Icon inlineContent;
    private int index;
    private String format;

    public ExternalMarkImpl() {        
    }
    
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

    public String getFormat() {
        return format;
    }

    public Icon getInlineContent() {
        return inlineContent;
    }

    public int getMarkIndex() {
        return index;
    }

    public OnLineResource getOnlineResource() {
        return onlineResource;
    }

    public Object accept(StyleVisitor visitor, Object extraData) {
        return visitor.visit( this, extraData );
    }

    public void setInlineContent(Icon inline) {
        this.inlineContent = inline;
    }

    public void getInlineContent(Icon inline) {
        setInlineContent(inline);
    }
    
    public void setFormat(String mimeType) {
        this.format = mimeType;
    }

    public void setMarkIndex(int markIndex) {
        this.index = markIndex;
    }

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
            copy.setFormat( mark.getFormat() );
            copy.setMarkIndex( mark.getMarkIndex() );
            copy.setOnlineResource( mark.getOnlineResource() );
            return copy;
        }
    }
}
