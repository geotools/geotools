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

import static org.geotools.api.annotation.Obligation.MANDATORY;
import static org.geotools.api.annotation.Specification.ISO_19117;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.logging.Logger;
import org.geotools.api.style.FeatureTypeStyle;
import org.geotools.api.style.StyleVisitor;
import org.geotools.api.style.Symbolizer;
import org.geotools.api.util.Cloneable;
import org.geotools.util.Utilities;

/**
 * A style object is quite hard to set up, involving fills, strokes, symbolizers and rules.
 *
 * @author James Macgill, CCG
 * @version $Id$
 */
public abstract class BasicPolygonStyle extends StyleImpl
        implements Cloneable, org.geotools.api.style.Style {
    /** The logger for the default core module. */
    private static final Logger LOGGER =
            org.geotools.util.logging.Logging.getLogger(StyleImpl.class);

    private List<FeatureTypeStyle> featureTypeStyles = new ArrayList<>();
    private DescriptionImpl description = new DescriptionImpl();
    private String name = "Default Styler";
    private boolean defaultB = false;
    private FillImpl background;
    private Symbolizer defaultSymbolizer;

    /** Creates a new instance of BasicPolygonStyle */
    public BasicPolygonStyle() {
        this(new FillImpl(), new StrokeImpl());
    }

    public BasicPolygonStyle(FillImpl fill, StrokeImpl stroke) {
        PolygonSymbolizerImpl polysym = new PolygonSymbolizerImpl();
        polysym.setFill(fill);
        polysym.setStroke(stroke);

        RuleImpl rule = new RuleImpl();
        rule.symbolizers().add(polysym);

        FeatureTypeStyleImpl fts = new FeatureTypeStyleImpl();
        fts.rules().add(rule);
        this.featureTypeStyles().add(fts);
    }

    public String getAbstract() {
        return "A simple polygon style";
    }

    @Override
    public String getName() {
        return "default polygon style";
    }

    public String getTitle() {
        return "default polygon style";
    }

    @Override
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

    public void setFeatureTypeStyles(FeatureTypeStyleImpl... styles) {
        List<FeatureTypeStyleImpl> newStyles = Arrays.asList(styles);

        this.featureTypeStyles.clear();
        this.featureTypeStyles.addAll(newStyles);

        LOGGER.fine("StyleImpl added " + featureTypeStyles.size() + " feature types");
    }

    public void setDefault(boolean isDefault) {
        defaultB = isDefault;
    }

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
        if (description == super.getBackground()) {
            this.description = new DescriptionImpl();
        } else {
            this.description = DescriptionImpl.cast(description);
        }
    }

    public void setBackground(FillImpl background) {
        this.background = background;
    }
}
