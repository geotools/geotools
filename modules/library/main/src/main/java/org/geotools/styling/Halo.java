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

// OpenGIS dependencies

import org.geotools.api.filter.FilterFactory;
import org.geotools.api.filter.expression.Expression;
import org.geotools.api.style.StyleVisitor;
import org.geotools.api.util.Cloneable;
import org.geotools.factory.CommonFactoryFinder;
import org.geotools.util.Utilities;
import org.geotools.util.factory.GeoTools;

/**
 * Direct implementation of Halo.
 *
 * @author Ian Turton, CCG
 * @version $Id$
 */
public class Halo implements  Cloneable, org.geotools.api.style.Halo {
    /** The logger for the default core module. */
    private static final java.util.logging.Logger LOGGER =
            org.geotools.util.logging.Logging.getLogger(Halo.class);

    private FilterFactory filterFactory;
    private Fill fill;
    private Expression radius = null;

    /**
     * Cast to HaloImpl (creating a copy if needed).
     *
     * @return HaloImpl equal to the provided halo
     */
    static Halo cast(org.geotools.api.style.Halo halo) {
        if (halo == null) {
            return null;
        } else if (halo instanceof Halo) {
            return (Halo) halo;
        } else {
            Halo copy = new Halo();
            copy.setFill(halo.getFill());
            copy.setRadius(halo.getRadius());

            return copy;
        }
    }

    public Halo() {
        this(CommonFactoryFinder.getFilterFactory(GeoTools.getDefaultHints()));
    }

    public Halo(FilterFactory factory) {
        filterFactory = factory;
        init();
    }

    public void setFilterFactory(FilterFactory factory) {
        filterFactory = factory;
        init();
    }

    private void init() {
        try {
            fill = new Fill(filterFactory);
            radius = filterFactory.literal(1);
        } catch (org.geotools.filter.IllegalFilterException ife) {
            LOGGER.severe("Failed to build defaultHalo: " + ife);
        }

        fill.setColor(filterFactory.literal("#FFFFFF")); // default halo is white
    }

    /**
     * Getter for property fill.
     *
     * @return Value of property fill.
     */
    @Override
    public Fill getFill() {
        return fill;
    }

    /**
     * Setter for property fill.
     *
     * @param fill New value of property fill.
     */
    public void setFill(org.geotools.api.style.Fill fill) {
        this.fill = Fill.cast(fill);
    }

    /**
     * Getter for property radius.
     *
     * @return Value of property radius.
     */
    @Override
    public Expression getRadius() {
        return radius;
    }

    /**
     * Setter for property radius.
     *
     * @param radius New value of property radius.
     */
    public void setRadius(Expression radius) {
        this.radius = radius;
    }

    @Override
    public Object accept(StyleVisitor visitor, Object data) {
        return visitor.visit(this, data);
    }

    @Override
    public void accept(StyleVisitor visitor) {
        visitor.visit(this);
    }

    /**
     * Creates a deep copy clone of the Halo.
     *
     * @return The clone.
     */
    @Override
    public Object clone() {
        try {
            Halo clone = (Halo) super.clone();
            clone.fill = (Fill) ((Cloneable) fill).clone();

            return clone;
        } catch (CloneNotSupportedException e) {
            throw new RuntimeException("This will never happen");
        }
    }

    /**
     * Compares this HaloImpl with another for equality.
     *
     * @param obj THe other HaloImpl.
     * @return True if they are equal. They are equal if their fill and radius is equal.
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }

        if (obj instanceof Halo) {
            Halo other = (Halo) obj;

            return Utilities.equals(radius, other.radius) && Utilities.equals(fill, other.fill);
        }

        return false;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        final int PRIME = 37;
        int result = 17;

        if (radius != null) {
            result = (result * PRIME) + radius.hashCode();
        }

        if (fill != null) {
            result = (result * PRIME) + fill.hashCode();
        }

        return result;
    }
}
