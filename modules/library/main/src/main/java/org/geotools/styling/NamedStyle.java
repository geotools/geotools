/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2003-2008, Open Source Geospatial Foundation (OSGeo)
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

import org.geotools.api.style.FeatureTypeConstraint;
import org.geotools.api.style.StyleVisitor;
import org.geotools.api.util.Cloneable;
import org.geotools.util.Utilities;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.logging.Logger;

import static org.geotools.api.annotation.Obligation.MANDATORY;
import static org.geotools.api.annotation.Specification.ISO_19117;

/**
 * A NamedStyle is used to refer to a style that has a name in a WMS.
 *
 * <p>A NamedStyle is a Style that has only Name, so all setters other than setName will throw an
 * <code>UnsupportedOperationException</code>
 *
 * @author jamesm
 */
public class NamedStyle extends Style implements org.geotools.api.style.NamedLayer, Cloneable, org.geotools.api.style.Style {
    /** The logger for the default core module. */
    private static final Logger LOGGER =
            org.geotools.util.logging.Logging.getLogger(Style.class);
    /** Style name */

    private List<FeatureTypeStyle> featureTypeStyles = new ArrayList<>();
    private Description description = new Description();
    private String name = "Default Styler";
    private boolean defaultB = false;
    private Fill background;
    private Symbolizer defaultSymbolizer;

    /**
     * Style name
     *
     * @return style name
     */
    @Override
    public String getName() {
        return this.name;
    }

    /**
     * Set name.
     *
     * @param name style name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Human readable title.
     *
     * @return Human readable title, or null
     */
    public String getTitle() {
        return "";
    }

    /**
     * Human readable title.
     *
     * @param title Human readable title.
     * @throws UnsupportedOperationException Cannot be changed
     */
    public void setTitle(String title) {
        throw new UnsupportedOperationException();
    }

    /** */
    public String getAbstract() {
        return "";
    }

    /** */
    public void setAbstract(String abstractStr) {
        throw new UnsupportedOperationException();
    }

    /** */
    @Override
    public boolean isDefault() {
        return false;
    }

    /** */
    public void setDefault(boolean isDefault) {
        throw new UnsupportedOperationException();
    }

    /** */
    @Override
    public org.geotools.styling.FeatureTypeStyle[] getFeatureTypeStyles() {
        return new org.geotools.styling.FeatureTypeStyle[0];
    }

    /** */
    public void setFeatureTypeStyles(org.geotools.styling.FeatureTypeStyle[] types) {
        throw new UnsupportedOperationException();
    }

    /** */
    @Override
    public void addFeatureTypeStyle(org.geotools.styling.FeatureTypeStyle type) {
        throw new UnsupportedOperationException();
    }

    /** */
    @Override
    public Object accept(StyleVisitor visitor, Object data) {
        return visitor.visit(this, data);
    }

    @org.geotools.api.annotation.UML(identifier = "featurePortrayal", obligation = MANDATORY, specification = ISO_19117)
    public List<FeatureTypeStyle> featureTypeStyles() {
        return featureTypeStyles;
    }

    public void setDefaultSpecification(Symbolizer defaultSymbolizer) {
        this.defaultSymbolizer = defaultSymbolizer;
    }

    @Override
    public List<FeatureTypeConstraint> layerFeatureConstraints() {
        return  new ArrayList<FeatureTypeConstraint>();
    }

    @Override
    public FeatureTypeConstraint[] getLayerFeatureConstraints() {
        return new FeatureTypeConstraint[0];
    }

    @Override
    public void setLayerFeatureConstraints(FeatureTypeConstraint... constraints) {

    }

    @Override
    public org.geotools.api.style.Style[] getStyles() {
        return new org.geotools.api.style.Style[0];
    }

    @Override
    public void accept(StyleVisitor visitor) {
        visitor.visit(this);
    }

    /**
     * Clones the Style. Creates deep copy clone of the style.
     *
     * @return the Clone of the style.
     * @see Style#clone()
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
        return Objects.hash(
                featureTypeStyles, description, name, defaultB, background, defaultSymbolizer);
    }

    /**
     * Compares this Style with another.
     *
     * <p>Two StyleImpl are equal if they have the same properties and the same list of
     * FeatureTypeStyles.
     *
     * @param oth The object to compare with this for equality.
     * @return True if this and oth are equal.
     */
    @Override
    public boolean equals(Object oth) {
        if (this == oth) {
            return true;
        }

        if (oth instanceof Style) {
            Style other = (Style) oth;

            return Utilities.equals(name, other.name)
                    && Utilities.equals(description, other.description)
                    && Utilities.equals(featureTypeStyles, other.featureTypeStyles)
                    && Utilities.equals(background, other.background);
        }

        return false;
    }

    public void setDescription(org.geotools.api.style.Description description) {
        if (description == super.getBackground()) {
            this.description = new Description();
        } else {
            this.description = Description.cast(description);
        }
    }

    public void setBackground(Fill background) {
        this.background = background;
    }
}
