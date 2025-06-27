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

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import javax.swing.Icon;
import org.geotools.api.metadata.citation.OnLineResource;
import org.geotools.api.style.ColorReplacement;
import org.geotools.api.style.ExternalGraphic;
import org.geotools.api.style.GraphicalSymbol;
import org.geotools.api.style.StyleVisitor;
import org.geotools.api.style.Symbol;
import org.geotools.api.style.TraversingStyleVisitor;
import org.geotools.api.util.Cloneable;
import org.geotools.metadata.iso.citation.OnLineResourceImpl;
import org.geotools.util.Utilities;

/**
 * @author Ian Turton, CCG
 * @version $Id$
 */
public class ExternalGraphicImpl implements Symbol, Cloneable, ExternalGraphic {

    private Icon inlineContent;

    // online, location and uri should correspond
    private OnLineResource online;
    private URL location = null;
    private String uri = null;

    private String format = null;

    private Map<String, Object> customProps = null;

    private final Set<ColorReplacement> colorReplacements;

    public ExternalGraphicImpl() {
        this(null, null, null);
    }

    public ExternalGraphicImpl(Icon icon, Collection<ColorReplacement> replaces, OnLineResource source) {
        this.inlineContent = icon;
        if (replaces == null) {
            colorReplacements = new TreeSet<>();
        } else {
            colorReplacements = new TreeSet<>(replaces);
        }
        this.online = source;
        if (source != null) {
            this.uri = source.getLinkage().toString();
        }
    }

    @Override
    public void setURI(String uri) {
        this.uri = uri;
        online = null;
        location = null;
    }

    @Override
    public String getURI() {
        return this.uri;
    }

    /**
     * Provides the format of the external graphic.
     *
     * @return The format of the external graphic. Reported as its MIME type in a String object.
     */
    @Override
    public String getFormat() {
        return format;
    }

    /**
     * Provides the URL for where the external graphic resource can be located.
     *
     * @return The URL of the ExternalGraphic
     * @throws MalformedURLException If unable to represent external graphic as a URL
     */
    @Override
    public URL getLocation() throws MalformedURLException {
        if (uri == null) {
            return null;
        }
        if (location == null) {
            location = new URL(uri);
        }

        return location;
    }

    /**
     * Setter for property Format.
     *
     * @param format New value of property Format.
     */
    @Override
    public void setFormat(java.lang.String format) {
        this.format = format;
    }

    /**
     * Setter for property location.
     *
     * @param location New value of property location.
     */
    @Override
    public void setLocation(URL location) {
        if (location == null) {
            throw new IllegalArgumentException("ExternalGraphic location URL cannot be null");
        }
        this.uri = location.toString();
        this.location = location;
        online = null;
    }

    @Override
    public Object accept(TraversingStyleVisitor visitor, Object data) {
        return visitor.visit(this, data);
    }

    @Override
    public void accept(StyleVisitor visitor) {
        visitor.visit(this);
    }

    /**
     * Returns a clone of the ExternalGraphic
     *
     * @see org.geotools.styling.ExternalGraphic#clone()
     */
    @Override
    public Object clone() {
        try {
            return super.clone();
        } catch (CloneNotSupportedException e) {
            // This will never happen
            throw new AssertionError(e);
        }
    }

    /**
     * Generates a hashcode for the ExternalGraphic
     *
     * @return The hash code.
     */
    @Override
    public int hashCode() {
        final int PRIME = 1000003;
        int result = 0;

        if (format != null) {
            result = PRIME * result + format.hashCode();
        }

        if (uri != null) {
            result = PRIME * result + uri.hashCode();
        }

        return result;
    }

    /**
     * Compares this ExternalGraphi with another.
     *
     * <p>Two external graphics are equal if they have the same uri and format.
     *
     * @param oth The other External graphic.
     * @return True if this and the other external graphic are equal.
     */
    @Override
    public boolean equals(Object oth) {
        if (this == oth) {
            return true;
        }

        if (oth instanceof ExternalGraphicImpl) {
            ExternalGraphicImpl other = (ExternalGraphicImpl) oth;

            return Utilities.equals(uri, other.uri) && Utilities.equals(format, other.format);
        }

        return false;
    }

    @Override
    public Map<String, Object> getCustomProperties() {
        return customProps;
    }

    @Override
    public void setCustomProperties(Map<String, Object> list) {
        customProps = list;
    }

    @Override
    public OnLineResource getOnlineResource() {
        if (online == null) {
            OnLineResourceImpl impl = new OnLineResourceImpl();
            try {
                impl.setLinkage(new URI(uri));
            } catch (URISyntaxException e) {
                throw new IllegalArgumentException(e);
            }
            online = impl;
        }
        return online;
    }

    public void setOnlineResource(OnLineResource online) {
        this.online = online;
        this.uri = online != null ? online.getLinkage().toString() : null;
        this.location = null;
    }

    @Override
    public Icon getInlineContent() {
        return inlineContent;
    }

    public void setInlineContent(Icon inlineContent) {
        this.inlineContent = inlineContent;
    }

    @Override
    public Collection<ColorReplacement> getColorReplacements() {
        return Collections.unmodifiableCollection(colorReplacements);
    }

    public Set<ColorReplacement> colorReplacements() {
        return this.colorReplacements;
    }

    static GraphicalSymbol cast(GraphicalSymbol item) {
        if (item == null) {
            return null;
        } else if (item instanceof ExternalGraphicImpl) {
            return item;
        } else if (item instanceof ExternalGraphic) {
            ExternalGraphic graphic = (ExternalGraphic) item;
            ExternalGraphicImpl copy = new ExternalGraphicImpl();
            copy.colorReplacements().addAll(graphic.getColorReplacements());
            copy.setFormat(graphic.getFormat());
            copy.setInlineContent(graphic.getInlineContent());
            copy.setOnlineResource(graphic.getOnlineResource());

            return copy;
        }
        return null;
    }
}
