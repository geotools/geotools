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

import java.util.logging.Logger;
import org.geotools.api.filter.FilterFactory;
import org.geotools.api.filter.expression.Expression;
import org.geotools.api.style.LinePlacement;
import org.geotools.api.style.StyleVisitor;
import org.geotools.api.style.TraversingStyleVisitor;
import org.geotools.api.util.Cloneable;
import org.geotools.factory.CommonFactoryFinder;
import org.geotools.util.Utilities;
import org.geotools.util.factory.GeoTools;

/**
 * Default implementation of LinePlacement.
 *
 * @author Ian Turton, CCG
 * @author Johann Sorel (Geomatys)
 * @version $Id$
 */
public class LinePlacementImpl implements LinePlacement, Cloneable {
    /** The logger for the default core module. */
    private static final Logger LOGGER = org.geotools.util.logging.Logging.getLogger(LinePlacementImpl.class);

    private FilterFactory filterFactory;

    private Expression perpendicularOffset;
    private boolean generalized;
    private boolean aligned;
    private boolean repeated;
    private Expression gap;
    private Expression initialGap;

    public LinePlacementImpl() {
        this(CommonFactoryFinder.getFilterFactory(GeoTools.getDefaultHints()));
    }

    public LinePlacementImpl(org.geotools.api.style.LinePlacement placement) {
        this.gap = placement.getGap();
        this.initialGap = placement.getInitialGap();
        this.generalized = placement.isGeneralizeLine();
        this.perpendicularOffset = placement.getPerpendicularOffset();
        this.repeated = placement.isRepeated();
        this.aligned = placement.isAligned();
    }

    public LinePlacementImpl(FilterFactory factory) {
        this(factory, false, false, false, null, null);
    }

    public LinePlacementImpl(
            FilterFactory factory,
            boolean aligned,
            boolean repeated,
            boolean generalized,
            Expression gap,
            Expression initialGap) {
        filterFactory = factory;
        this.gap = gap;
        this.initialGap = initialGap;
        this.generalized = generalized;
        this.aligned = aligned;
        this.repeated = repeated;
        init();
    }

    /** Creates a new instance of DefaultLinePlacement */
    private void init() {
        try {
            perpendicularOffset = filterFactory.literal(0);
        } catch (org.geotools.filter.IllegalFilterException ife) {
            LOGGER.severe("Failed to build defaultLinePlacement: " + ife);
        }
    }

    /**
     * Getter for property perpendicularOffset.
     *
     * @return Value of property perpendicularOffset.
     */
    @Override
    public Expression getPerpendicularOffset() {
        return perpendicularOffset;
    }

    /**
     * Setter for property perpendicularOffset.
     *
     * @param perpendicularOffset New value of property perpendicularOffset.
     */
    @Override
    public void setPerpendicularOffset(Expression perpendicularOffset) {
        this.perpendicularOffset = perpendicularOffset;
    }

    @Override
    public Expression getInitialGap() {
        return initialGap;
    }

    @Override
    public Expression getGap() {
        return gap;
    }

    @Override
    public boolean isRepeated() {
        return repeated;
    }

    @Override
    public boolean isAligned() {
        return aligned;
    }

    @Override
    public boolean isGeneralizeLine() {
        return generalized;
    }

    @Override
    public Object accept(TraversingStyleVisitor visitor, Object data) {
        return visitor.visit(this, data);
    }

    @Override
    public void accept(StyleVisitor visitor) {
        visitor.visit(this);
    }

    /* (non-Javadoc)
     * @see Cloneable#clone()
     */
    @Override
    public Object clone() {
        try {
            return super.clone();
        } catch (CloneNotSupportedException e) {
            throw new RuntimeException("This can not happen");
        }
    }

    /* (non-Javadoc)
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }

        if (obj instanceof LinePlacementImpl) {
            LinePlacementImpl other = (LinePlacementImpl) obj;

            return Utilities.equals(perpendicularOffset, other.perpendicularOffset)
                    && Utilities.equals(repeated, other.repeated)
                    && Utilities.equals(generalized, other.generalized)
                    && Utilities.equals(aligned, other.aligned)
                    && Utilities.equals(initialGap, other.initialGap)
                    && Utilities.equals(gap, other.gap);
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

        if (perpendicularOffset != null) {
            result = result * PRIME + perpendicularOffset.hashCode();
        }

        if (gap != null) {
            result = result * PRIME + gap.hashCode();
        }

        if (initialGap != null) {
            result = result * PRIME + initialGap.hashCode();
        }

        result = result * PRIME + Boolean.valueOf(generalized).hashCode();
        result = result * PRIME + Boolean.valueOf(aligned).hashCode();
        result = result * PRIME + Boolean.valueOf(repeated).hashCode();

        return result;
    }

    static LinePlacementImpl cast(org.geotools.api.style.LabelPlacement placement) {
        if (placement == null) {
            return null;
        } else if (placement instanceof LinePlacementImpl) {
            return (LinePlacementImpl) placement;
        } else if (placement instanceof LinePlacement) {
            LinePlacementImpl copy = new LinePlacementImpl((LinePlacement) placement);
            return copy;
        }
        return null;
    }

    @Override
    public void setRepeated(boolean repeated) {
        this.repeated = repeated;
    }

    @Override
    public void setGeneralized(boolean generalized) {
        this.generalized = generalized;
    }

    @Override
    public void setAligned(boolean aligned) {
        this.aligned = aligned;
    }

    @Override
    public void setGap(Expression gap) {
        this.gap = gap;
    }

    @Override
    public void setInitialGap(Expression initialGap) {
        this.initialGap = initialGap;
    }
}
