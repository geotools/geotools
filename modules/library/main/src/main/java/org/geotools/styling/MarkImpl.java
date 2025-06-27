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

import org.geotools.api.filter.FilterFactory;
import org.geotools.api.filter.expression.Expression;
import org.geotools.api.style.ExternalMark;
import org.geotools.api.style.GraphicalSymbol;
import org.geotools.api.style.Mark;
import org.geotools.api.style.StyleFactory;
import org.geotools.api.style.StyleVisitor;
import org.geotools.api.style.Symbol;
import org.geotools.api.style.TraversingStyleVisitor;
import org.geotools.api.util.Cloneable;
import org.geotools.factory.CommonFactoryFinder;
import org.geotools.util.factory.GeoTools;

/**
 * Default implementation of Mark.
 *
 * @author Ian Turton, CCG
 * @author Johann Sorel (Geomatys)
 * @version $Id$
 */
public class MarkImpl implements Mark, Cloneable, Symbol {

    /** The logger for the default core module. */
    private static final java.util.logging.Logger LOGGER = org.geotools.util.logging.Logging.getLogger(MarkImpl.class);

    private final FilterFactory filterFactory;
    private FillImpl fill;
    private StrokeImpl stroke;

    private ExternalMarkImpl external;
    private Expression wellKnownName = null;

    /** Creates a new instance of DefaultMark */
    public MarkImpl() {
        this(CommonFactoryFinder.getFilterFactory(GeoTools.getDefaultHints()), null);
    }

    public MarkImpl(String name) {
        this(CommonFactoryFinder.getFilterFactory(GeoTools.getDefaultHints()), null);
        LOGGER.fine("creating " + name + " type mark");
        setWellKnownName(name);
    }

    public MarkImpl(FilterFactory filterFactory, ExternalMark external) {
        this.filterFactory = filterFactory;
        LOGGER.fine("creating defaultMark");

        try {
            StyleFactory sfac = new StyleFactoryImpl();
            fill = FillImpl.cast(sfac.getDefaultFill());
            stroke = StrokeImpl.cast(sfac.getDefaultStroke());

            wellKnownName = filterFactory.literal("square");
        } catch (org.geotools.filter.IllegalFilterException ife) {
            severe("<init>", "Failed to build default mark: ", ife);
        }
        this.external = ExternalMarkImpl.cast(external);
    }

    /** Convenience method for logging a message with an exception. */
    private static void severe(final String method, final String message, final Exception exception) {
        final java.util.logging.LogRecord record =
                new java.util.logging.LogRecord(java.util.logging.Level.SEVERE, message);
        record.setSourceMethodName(method);
        record.setThrown(exception);
        LOGGER.log(record);
    }

    /**
     * This parameter defines which fill style to use when rendering the Mark.
     *
     * @return the Fill definition to use when rendering the Mark.
     */
    @Override
    public FillImpl getFill() {
        return fill;
    }

    /**
     * This paramterer defines which stroke style should be used when rendering the Mark.
     *
     * @return The Stroke definition to use when rendering the Mark.
     */
    @Override
    public StrokeImpl getStroke() {
        return stroke;
    }

    /**
     * This parameter gives the well-known name of the shape of the mark.<br>
     * Allowed names include at least "square", "circle", "triangle", "star", "cross" and "x" though renderers may draw
     * a different symbol instead if they don't have a shape for all of these. <br>
     *
     * @return The well-known name of a shape. The default value is "square".
     */
    @Override
    public Expression getWellKnownName() {
        return wellKnownName;
    }

    /**
     * Setter for property fill.
     *
     * @param fill New value of property fill.
     */
    @Override
    public void setFill(org.geotools.api.style.Fill fill) {
        this.fill = FillImpl.cast(fill);
    }

    /**
     * Setter for property stroke.
     *
     * @param stroke New value of property stroke.
     */
    @Override
    public void setStroke(org.geotools.api.style.Stroke stroke) {
        this.stroke = StrokeImpl.cast(stroke);
    }

    /**
     * Setter for property wellKnownName.
     *
     * @param wellKnownName New value of property wellKnownName.
     */
    @Override
    public void setWellKnownName(Expression wellKnownName) {
        LOGGER.entering("DefaultMark", "setWellKnownName");
        this.wellKnownName = wellKnownName;
    }

    public void setWellKnownName(String name) {
        setWellKnownName(filterFactory.literal(name));
    }

    @Override
    public String toString() {
        return wellKnownName.toString();
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
     * Creates a deep copy of the Mark.
     *
     * <p>Only the fill and stroke are cloned since Expressions should be immutable.
     *
     * @see org.geotools.api.style.Mark
     */
    @Override
    public Object clone() {
        try {
            MarkImpl clone = (MarkImpl) super.clone();
            if (fill != null) {
                clone.fill = (FillImpl) fill.clone();
            }
            if (stroke != null && stroke instanceof Cloneable) {
                clone.stroke = (StrokeImpl) stroke.clone();
            }

            return clone;
        } catch (CloneNotSupportedException e) {
            // this will never happen
            throw new RuntimeException("Failed to clone MarkImpl");
        }
    }

    /**
     * The hashcode override for the MarkImpl.
     *
     * @return the Hashcode.
     */
    @Override
    public int hashCode() {
        final int PRIME = 1000003;
        int result = 0;

        if (fill != null) {
            result = PRIME * result + fill.hashCode();
        }

        if (stroke != null) {
            result = PRIME * result + stroke.hashCode();
        }

        if (wellKnownName != null) {
            result = PRIME * result + wellKnownName.hashCode();
        }

        return result;
    }

    /**
     * Compares this MarkImpl with another for equality.
     *
     * <p>Two MarkImpls are equal if they have the same well Known Name, the same size and rotation and the same stroke
     * and fill.
     *
     * @param oth The Other MarkImpl to compare with.
     * @return True if this and oth are equal.
     */
    @Override
    public boolean equals(Object oth) {
        if (this == oth) {
            return true;
        }

        if (oth == null) {
            return false;
        }

        if (oth.getClass() != getClass()) {
            return false;
        }

        MarkImpl other = (MarkImpl) oth;

        // check expressions first - easiest
        if (this.wellKnownName == null) {
            if (other.wellKnownName != null) {
                return false;
            }
        } else {
            if (!this.wellKnownName.equals(other.wellKnownName)) {
                return false;
            }
        }

        if (this.fill == null) {
            if (other.fill != null) {
                return false;
            }
        } else {
            if (!this.fill.equals(other.fill)) {
                return false;
            }
        }

        if (this.stroke == null) {
            if (other.stroke != null) {
                return false;
            }
        } else {
            if (!this.stroke.equals(other.stroke)) {
                return false;
            }
        }

        return true;
    }

    @Override
    public ExternalMarkImpl getExternalMark() {
        return external;
    }

    @Override
    public void setExternalMark(org.geotools.api.style.ExternalMark external) {
        this.external = ExternalMarkImpl.cast(external);
    }

    static MarkImpl cast(GraphicalSymbol item) {
        if (item == null) {
            return null;
        } else if (item instanceof MarkImpl) {
            return (MarkImpl) item;
        } else if (item instanceof Mark) {
            Mark mark = (Mark) item;
            MarkImpl copy = new MarkImpl();
            copy.setStroke(mark.getStroke());
            copy.setWellKnownName(mark.getWellKnownName());
            copy.setExternalMark(mark.getExternalMark());
            return copy;
        }
        return null;
    }
}
