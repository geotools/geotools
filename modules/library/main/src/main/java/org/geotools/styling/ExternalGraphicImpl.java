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
import java.net.URL;
import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import javax.swing.Icon;

import org.geotools.util.Utilities;
import org.opengis.metadata.citation.OnLineResource;
import org.opengis.style.ColorReplacement;
import org.opengis.style.GraphicalSymbol;
import org.opengis.style.StyleVisitor;
import org.opengis.util.Cloneable;


/**
 * DOCUMENT ME!
 *
 * @author Ian Turton, CCG
 *
 * @source $URL$
 * @version $Id$
 */
public class ExternalGraphicImpl implements ExternalGraphic, Symbol, Cloneable {
    /** The logger for the default core module. */
    //private static final java.util.logging.Logger LOGGER = org.geotools.util.logging.Logging.getLogger("org.geotools.core");
    private Icon inlineContent;
    private OnLineResource online;
    
    
    private URL location = null;
    private String format = null;
    private String uri = null;
    private Map<String,Object> customProps = null;
    
    private final Set<ColorReplacement> colorReplacements;

    public ExternalGraphicImpl(){
        this(null,null,null);
    }
    
    public ExternalGraphicImpl(Icon icon,Collection<ColorReplacement> replaces, OnLineResource source){
        this.inlineContent = icon;
        if(replaces == null){
            colorReplacements = new TreeSet<ColorReplacement>();
        }else{
            colorReplacements = new TreeSet<ColorReplacement>(replaces);
        }       
        this.online = source;        
    }
    
    
    @Deprecated
    public void setURI(String uri) {
        this.uri = uri;
    }

    /**
     * Provides the format of the external graphic.
     *
     * @return The format of the external graphic.  Reported as its MIME type
     *         in a String object.
     */
    public String getFormat() {
        return format;
    }

    /**
     * Provides the URL for where the external graphic resource can be located.
     *
     * @return The URL of the ExternalGraphic
     *
     * @throws MalformedURLException If unable to represent external graphic as a URL
     */
    public java.net.URL getLocation() throws MalformedURLException {
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
    public void setFormat(java.lang.String format) {
        this.format = format;
    }

    /**
     * Setter for property location.
     *
     * @param location New value of property location.
     */
    public void setLocation(java.net.URL location) {
        this.uri = location.toString();
        this.location = location;
    }

    public Object accept(StyleVisitor visitor,Object data) {
        return visitor.visit(this,data);
    }

    public void accept(org.geotools.styling.StyleVisitor visitor) {
        visitor.visit(this);
    }
    
    /**
     * Returns a clone of the ExternalGraphic
     *
     * @see org.geotools.styling.ExternalGraphic#clone()
     */
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
    public int hashCode() {
        final int PRIME = 1000003;
        int result = 0;

        if (format != null) {
            result = (PRIME * result) + format.hashCode();
        }

        if (uri != null) {
            result = (PRIME * result) + uri.hashCode();
        }
        
//        if (inlineContent != null) {
//            result = (PRIME * result) + inlineContent.hashCode();
//        }
//        
//        if (online != null) {
//            result = (PRIME * result) + online.hashCode();
//        }
//        
//        if (replacements != null) {
//            result = (PRIME * result) + replacements.hashCode();
//        }

        return result;
    }
    
    /**
     * Compares this ExternalGraphi with another.
     * 
     * <p>
     * Two external graphics are equal if they have the same uri and format.
     * </p>
     *
     * @param oth The other External graphic.
     *
     * @return True if this and the other external graphic are equal.
     */
    public boolean equals(Object oth) {
        if (this == oth) {
            return true;
        }

        if (oth instanceof ExternalGraphicImpl) {
            ExternalGraphicImpl other = (ExternalGraphicImpl) oth;

            return Utilities.equals(uri, other.uri)
            && Utilities.equals(format, other.format);
        }

        return false;
    }

    public java.util.Map<String,Object> getCustomProperties() {
        return customProps;
    }

    public void setCustomProperties(java.util.Map<String,Object> list) {
        customProps = list;
    }

    public OnLineResource getOnlineResource() {
        return online;
    }
    
    public void setOnlineResource(OnLineResource online) {
        this.online = online;
    }

    public Icon getInlineContent() {
        return inlineContent;
    }
    
    public void setInlineContent(Icon inlineContent) {
        this.inlineContent = inlineContent;
    }

    public Collection<ColorReplacement> getColorReplacements() {
        return Collections.unmodifiableCollection( colorReplacements );
    }
    
    public Set<ColorReplacement> colorReplacements() {
        return this.colorReplacements;
    }

    static GraphicalSymbol cast(GraphicalSymbol item) {
        if( item == null ){
            return null;
        }
        else if (item instanceof ExternalGraphicImpl){
            return (ExternalGraphicImpl) item;
        }
        else if (item instanceof org.opengis.style.ExternalGraphic){
            org.opengis.style.ExternalGraphic graphic = (org.opengis.style.ExternalGraphic) item;
            ExternalGraphicImpl copy = new ExternalGraphicImpl();
            copy.colorReplacements().addAll( graphic.getColorReplacements() );
            copy.setFormat( graphic.getFormat() );
            copy.setInlineContent( graphic.getInlineContent() );
            copy.setOnlineResource( graphic.getOnlineResource() );

            return copy;
        }
        return null;
    }


}
