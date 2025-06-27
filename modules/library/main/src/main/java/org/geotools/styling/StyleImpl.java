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
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.geotools.api.style.Description;
import org.geotools.api.style.FeatureTypeStyle;
import org.geotools.api.style.Fill;
import org.geotools.api.style.Style;
import org.geotools.api.style.StyleVisitor;
import org.geotools.api.style.Symbolizer;
import org.geotools.api.style.TraversingStyleVisitor;
import org.geotools.api.util.Cloneable;
import org.geotools.util.Utilities;

/**
 * Implementation of style.
 *
 * @author James Macgill, CCG
 * @version $Id$
 */
public class StyleImpl implements Style, Cloneable {
    /** The logger for the default core module. */
    private static final Logger LOGGER = org.geotools.util.logging.Logging.getLogger(StyleImpl.class);

    private List<FeatureTypeStyle> featureTypeStyles = new ArrayList<>();
    private DescriptionImpl description = new DescriptionImpl();
    private String name = "Default Styler";
    private boolean defaultB = false;
    private Fill background;

    private Symbolizer defaultSymbolizer;

    /** Creates a new instance of StyleImpl */
    protected StyleImpl() {}

    @Override
    public DescriptionImpl getDescription() {
        return description;
    }

    public FeatureTypeStyle[] getFeatureTypeStyles() {
        FeatureTypeStyle[] ret = {new FeatureTypeStyleImpl()};

        if (featureTypeStyles != null && !featureTypeStyles.isEmpty()) {
            if (LOGGER.isLoggable(Level.FINE)) LOGGER.fine("number of fts set " + featureTypeStyles.size());

            ret = featureTypeStyles.toArray(new FeatureTypeStyle[] {});
        }

        return ret;
    }

    @Override
    public List<FeatureTypeStyle> featureTypeStyles() {
        return featureTypeStyles;
    }

    @Override
    public Symbolizer getDefaultSpecification() {
        return defaultSymbolizer;
    }

    @Override
    public void setDefaultSpecification(Symbolizer defaultSymbolizer) {
        this.defaultSymbolizer = defaultSymbolizer;
    }

    public void setFeatureTypeStyles(FeatureTypeStyle... styles) {
        List<FeatureTypeStyle> newStyles = Arrays.asList(styles);

        this.featureTypeStyles.clear();
        this.featureTypeStyles.addAll(newStyles);

        LOGGER.fine("StyleImpl added " + featureTypeStyles.size() + " feature types");
    }

    public void addFeatureTypeStyle(FeatureTypeStyle type) {
        featureTypeStyles.add(type);
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public boolean isDefault() {
        return defaultB;
    }

    @Override
    public void setDefault(boolean isDefault) {
        defaultB = isDefault;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public void accept(StyleVisitor visitor) {
        visitor.visit(this);
    }

    /**
     * Clones the Style. Creates deep copy clone of the style.
     *
     * @return the Clone of the style.
     * @see Style
     */
    @Override
    public Object clone() {
        Style clone;

        try {
            clone = (Style) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new RuntimeException(e); // this should never happen since we implement Cloneable
        }

        List<FeatureTypeStyle> ftsCopies = new ArrayList<>();

        for (FeatureTypeStyle featureTypeStyle : featureTypeStyles) {
            ftsCopies.add((FeatureTypeStyle) ((Cloneable) featureTypeStyle).clone());
        }

        clone.featureTypeStyles().clear();
        clone.featureTypeStyles().addAll(ftsCopies);

        return clone;
    }

    @Override
    public int hashCode() {
        return Objects.hash(featureTypeStyles, description, name, defaultB, background, defaultSymbolizer);
    }

    /**
     * Compares this Style with another.
     *
     * <p>Two StyleImpl are equal if they have the same properties and the same list of FeatureTypeStyles.
     *
     * @param oth The object to compare with this for equality.
     * @return True if this and oth are equal.
     */
    @Override
    public boolean equals(Object oth) {
        if (this == oth) {
            return true;
        }

        if (oth instanceof StyleImpl) {
            StyleImpl other = (StyleImpl) oth;

            return Utilities.equals(name, other.name)
                    && Utilities.equals(description, other.description)
                    && Utilities.equals(featureTypeStyles, other.featureTypeStyles)
                    && Utilities.equals(background, other.background);
        }

        return false;
    }

    @Override
    public String toString() {
        StringBuffer buf = new StringBuffer();
        buf.append("StyleImpl");
        buf.append("[");
        if (name != null) {
            buf.append(" name=");
            buf.append(name);
        } else {
            buf.append(" UNNAMED");
        }
        if (defaultB) {
            buf.append(", DEFAULT");
        }
        //      if( title != null && title.length() != 0 ){
        //              buf.append(", title=");
        //              buf.append( title );
        //      }
        buf.append("]");
        return buf.toString();
    }

    @Override
    public Object accept(TraversingStyleVisitor visitor, Object extraData) {
        return visitor.visit(this, extraData);
    }

    public void setDescription(Description description) {
        if (description == null) {
            this.description = new DescriptionImpl();
        } else {
            this.description = DescriptionImpl.cast(description);
        }
    }

    @Override
    public Fill getBackground() {
        return background;
    }

    @Override
    public void setBackground(Fill background) {
        this.background = background;
    }
}
