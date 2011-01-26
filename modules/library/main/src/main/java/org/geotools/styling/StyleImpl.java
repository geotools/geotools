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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.geotools.util.SimpleInternationalString;
import org.geotools.util.Utilities;
import org.opengis.style.Description;
import org.opengis.util.Cloneable;

/**
 * Implementation of style.
 *
 * @author James Macgill, CCG
 * @source $URL$
 * @version $Id$
 */
public class StyleImpl implements org.geotools.styling.Style, Cloneable {
    /** The logger for the default core module. */
    private static final Logger LOGGER = org.geotools.util.logging.Logging.getLogger(
            "org.geotools.styling");
    
    private List<FeatureTypeStyle> featureTypeStyles = new ArrayList<FeatureTypeStyle>();
    private DescriptionImpl description = new DescriptionImpl();
    private String name = "Default Styler";
    private boolean defaultB = false;

    private Symbolizer defaultSymbolizer;

    /**
     * Creates a new instance of StyleImpl
     */
    protected StyleImpl() {
    }
    
    public DescriptionImpl getDescription() {
        return description;
    }
    @Deprecated
    public String getAbstract() {
        if( description == null || description.getAbstract() == null ){
                return null;
        }
        return description.getAbstract().toString();
    }

    public FeatureTypeStyle[] getFeatureTypeStyles() {
        FeatureTypeStyle[] ret = new FeatureTypeStyleImpl[] { new FeatureTypeStyleImpl() };

        if ((featureTypeStyles != null) && (featureTypeStyles.size() != 0)) {
            if (LOGGER.isLoggable(Level.FINE))
                LOGGER.fine("number of fts set " + featureTypeStyles.size());

            ret = (FeatureTypeStyle[]) featureTypeStyles.toArray(new FeatureTypeStyle[] {

            });
        }

        return ret;
    }

    public List<FeatureTypeStyle> featureTypeStyles() {
        return featureTypeStyles;
    }

    public Symbolizer getDefaultSpecification() {
        return defaultSymbolizer;
    }
    public void setDefaultSpecification(org.geotools.styling.Symbolizer defaultSymbolizer) {
        this.defaultSymbolizer = defaultSymbolizer;
    }
   
    public void setFeatureTypeStyles(FeatureTypeStyle[] styles) {
        List<FeatureTypeStyle> newStyles = Arrays.asList(styles);

        this.featureTypeStyles.clear();
        this.featureTypeStyles.addAll(newStyles);

        LOGGER.fine("StyleImpl added " + featureTypeStyles.size()
            + " feature types");
    }

    public void addFeatureTypeStyle(FeatureTypeStyle type) {
        featureTypeStyles.add(type);
    }

    public String getName() {
        return name;
    }
    @Deprecated
    public String getTitle() {
        if( description == null || description.getTitle() == null ){
                return null;
        }
        return description.getTitle().toString();
    }

    public boolean isDefault() {
        return defaultB;
    }

    @Deprecated
    public void setAbstract(String abstractStr) {
        if( description == null ){
             description = new DescriptionImpl();
        }
        description.setAbstract( abstractStr == null ? null :new SimpleInternationalString( abstractStr ));        
    }

    public void setDefault(boolean isDefault) {
        defaultB = isDefault;
    }

    public void setName(String name) {
        this.name = name;
    }
    @Deprecated
    public void setTitle(String title) {
        if( description == null ){
                description = new DescriptionImpl();
        }
        description.setTitle( title == null ? null : new SimpleInternationalString( title));
    }

    public void accept(StyleVisitor visitor) {
        visitor.visit(this);
    }

    /**
     * Clones the Style.  Creates deep copy clone of the style.
     *
     * @return the Clone of the style.
     *
     * @throws RuntimeException DOCUMENT ME!
     *
     * @see org.geotools.styling.Style#clone()
     */
    public Object clone() {
        Style clone;

        try {
            clone = (Style) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new RuntimeException(e); // this should never happen since we implement Cloneable
        }

        FeatureTypeStyle[] ftsArray = new FeatureTypeStyle[featureTypeStyles
            .size()];

        for (int i = 0; i < ftsArray.length; i++) {
            FeatureTypeStyle fts = (FeatureTypeStyle) featureTypeStyles.get(i);
            ftsArray[i] = (FeatureTypeStyle) ((Cloneable) fts).clone();
        }

        clone.setFeatureTypeStyles(ftsArray);

        return clone;
    }

    /**
     * Overrides hashcode.
     *
     * @return The hash code.
     */
    public int hashCode() {
        final int PRIME = 1000003;
        int result = 0;

        if (featureTypeStyles != null) {
            result = (PRIME * result) + featureTypeStyles.hashCode();
        }

        if (description != null) {
            result = (PRIME * result) + description.hashCode();
        }

        if (name != null) {
            result = (PRIME * result) + name.hashCode();
        }

        result = (PRIME * result) + (defaultB ? 1 : 0);

        return result;
    }

    /**
     * Compares this Style with another.
     * 
     * <p>
     * Two StyleImpl are equal if they have the same properties and the same
     * list of FeatureTypeStyles.
     * </p>
     *
     * @param oth The object to compare with this for equality.
     *
     * @return True if this and oth are equal.
     */
    public boolean equals(Object oth) {
        if (this == oth) {
            return true;
        }

        if (oth instanceof StyleImpl) {
            StyleImpl other = (StyleImpl) oth;

            return Utilities.equals(name, other.name)
            && Utilities.equals(description, other.description)
            && Utilities.equals(featureTypeStyles, other.featureTypeStyles);
        }

        return false;
    }
    public String toString() {
        StringBuffer buf = new StringBuffer();
        buf.append( "StyleImpl");
        buf.append( "[");
        if( name != null ) {
                buf.append(" name=");
                buf.append( name );
        }
        else {
                buf.append( " UNNAMED");
        }
        if( defaultB ) {
                buf.append( ", DEFAULT");
        }
//      if( title != null && title.length() != 0 ){
//              buf.append(", title=");
//              buf.append( title );
//      }
        buf.append("]");
        return buf.toString();
    }

    public Object accept(org.opengis.style.StyleVisitor visitor, Object extraData) {
        return visitor.visit( this, extraData );
    }

    public void setDescription(Description description) {
        this.description = DescriptionImpl.cast(description);
    }
}
