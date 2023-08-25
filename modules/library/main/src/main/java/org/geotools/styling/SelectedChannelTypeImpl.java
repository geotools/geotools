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
import org.geotools.api.style.StyleVisitor;
import org.geotools.factory.CommonFactoryFinder;
import org.geotools.util.Utilities;

/** Default implementation of SelectedChannelType. */
public class SelectedChannelTypeImpl implements org.geotools.api.style.SelectedChannelType {
    private FilterFactory filterFactory;

    // private Expression contrastEnhancement;
    private ContrastEnhancementImpl contrastEnhancement;
    private Expression name = Expression.NIL;

    public SelectedChannelTypeImpl() {
        this(CommonFactoryFinder.getFilterFactory(null));
    }

    public SelectedChannelTypeImpl(FilterFactory factory) {
        filterFactory = factory;
        contrastEnhancement = contrastEnhancement(filterFactory.literal(1.0));
    }

    public SelectedChannelTypeImpl(FilterFactory factory, ContrastEnhancementImpl contrast) {
        filterFactory = factory;
        contrastEnhancement = contrast;
    }

    public SelectedChannelTypeImpl(org.geotools.api.style.SelectedChannelType gray) {
        filterFactory = CommonFactoryFinder.getFilterFactory(null);
        name = gray.getChannelName();
        if (gray.getContrastEnhancement() != null) {
            contrastEnhancement = new ContrastEnhancementImpl(gray.getContrastEnhancement());
        }
    }

    @Override
    public Expression getChannelName() {
        return name;
    }

    @Override
    public ContrastEnhancementImpl getContrastEnhancement() {
        return contrastEnhancement;
    }

    public void setChannelName(Expression name) {
        this.name = name;
    }

    public void setChannelName(String name) {
        FilterFactory ff = CommonFactoryFinder.getFilterFactory(null);
        this.name = ff.literal(name);
    }

    public void setContrastEnhancement(org.geotools.api.style.ContrastEnhancement enhancement) {
        this.contrastEnhancement = ContrastEnhancementImpl.cast(enhancement);
    }

    public void setContrastEnhancement(Expression gammaValue) {
        contrastEnhancement.setGammaValue(gammaValue);
    }

    protected ContrastEnhancementImpl contrastEnhancement(Expression expr) {
        ContrastEnhancementImpl enhancement = new ContrastEnhancementImpl();
        enhancement.setGammaValue(filterFactory.literal(1.0));

        return enhancement;
    }

    @Override
    public void accept(StyleVisitor visitor) {
        visitor.visit(this);
    }

    @Override
    public int hashCode() {
        final int PRIME = 1000003;
        int result = 0;

        if (name != null) {
            result = (PRIME * result) + name.hashCode();
        }

        if (contrastEnhancement != null) {
            result = (PRIME * result) + contrastEnhancement.hashCode();
        }

        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }

        if (obj instanceof SelectedChannelTypeImpl) {
            SelectedChannelTypeImpl other = (SelectedChannelTypeImpl) obj;

            return Utilities.equals(name, other.name)
                    && Utilities.equals(contrastEnhancement, other.contrastEnhancement);
        }

        return false;
    }
}
