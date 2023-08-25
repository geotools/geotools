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

import static org.geotools.api.annotation.Obligation.MANDATORY;
import static org.geotools.api.annotation.Specification.ISO_19117;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.logging.Logger;
import org.geotools.api.style.*;
import org.geotools.api.util.Cloneable;
import org.geotools.util.Utilities;

/**
 * A NamedStyle is used to refer to a style that has a name in a WMS.
 *
 * <p>A NamedStyle is a Style that has only Name, so all setters other than setName will throw an
 * <code>UnsupportedOperationException</code>
 *
 * @author jamesm
 */
public class NamedStyleImpl extends StyleImpl implements NamedStyle, Cloneable, Style {
    /** The logger for the default core module. */
    private static final Logger LOGGER =
            org.geotools.util.logging.Logging.getLogger(StyleImpl.class);
    /** Style name */
    private List<FeatureTypeStyle> featureTypeStyles = new ArrayList<>();

    private DescriptionImpl description = new DescriptionImpl();
    private String name = "Default Styler";
    private boolean defaultB = false;
    private FillImpl background;
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
    public FeatureTypeStyleImpl[] getFeatureTypeStyles() {
        return new FeatureTypeStyleImpl[0];
    }

    /** */
    public void setFeatureTypeStyles(FeatureTypeStyleImpl[] types) {
        throw new UnsupportedOperationException();
    }

    /** */
    @Override
    public void addFeatureTypeStyle(FeatureTypeStyleImpl type) {
        throw new UnsupportedOperationException();
    }

    @org.geotools.api.annotation.UML(
            identifier = "featurePortrayal",
            obligation = MANDATORY,
            specification = ISO_19117)
    public List<FeatureTypeStyle> featureTypeStyles() {
        return featureTypeStyles;
    }

    public void setDefaultSpecification(Symbolizer defaultSymbolizer) {
        this.defaultSymbolizer = defaultSymbolizer;
    }

    @Override
    public void accept(StyleVisitor visitor) {
        visitor.visit((NamedLayer) this);
    }

    /**
     * Clones the Style. Creates deep copy clone of the style.
     *
     * @return the Clone of the style.
     * @see StyleImpl#clone()
     */
    @Override
    public Object clone() {
        StyleImpl clone;

        clone = (StyleImpl) super.clone();

        List<FeatureTypeStyleImpl> ftsCopies = new ArrayList<>();

        for (FeatureTypeStyle featureTypeStyle : featureTypeStyles) {
            ftsCopies.add((FeatureTypeStyleImpl) ((Cloneable) featureTypeStyle).clone());
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

        if (oth instanceof StyleImpl) {
            StyleImpl other = (StyleImpl) oth;

            return Utilities.equals(name, other.getName())
                    && Utilities.equals(description, other.getDescription())
                    && Utilities.equals(featureTypeStyles, other.getFeatureTypeStyles())
                    && Utilities.equals(background, other.getBackground());
        }

        return false;
    }

    public void setDescription(org.geotools.api.style.Description description) {
        if (description == super.getDescription()) {
            this.description = new DescriptionImpl();
        } else {
            this.description = DescriptionImpl.cast(description);
        }
    }

    public void setBackground(FillImpl background) {
        this.background = background;
    }
}
