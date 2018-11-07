/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2014, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.brewer.styling.builder;

import org.geotools.factory.CommonFactoryFinder;
import org.geotools.styling.StyleFactory;
import org.geotools.styling.StyledLayerDescriptor;
import org.opengis.filter.FilterFactory2;

abstract class AbstractSLDBuilder<T> implements Builder<T> {

    protected StyleFactory sf = CommonFactoryFinder.getStyleFactory(null);

    protected AbstractSLDBuilder<?> parent;

    protected static final FilterFactory2 FF = CommonFactoryFinder.getFilterFactory2(null);

    protected boolean unset = false;

    public AbstractSLDBuilder(AbstractSLDBuilder<?> parent) {
        this.parent = parent;
    }

    public StyledLayerDescriptor buildSLD() {
        if (parent != null) {
            return parent.buildSLD();
        } else {
            StyledLayerDescriptorBuilder sb = new StyledLayerDescriptorBuilder();
            buildSLDInternal(sb);
            return sb.buildSLD();
        }
    }

    public Object buildRoot() {
        if (parent != null) {
            return parent.build();
        } else {
            return build();
        }
    }

    protected abstract void buildSLDInternal(StyledLayerDescriptorBuilder sb);

    protected void init(Builder<T> other) {
        reset(other.build());
    }

    public AbstractSLDBuilder<T> unset() {
        reset();
        unset = true;
        return this;
    }

    boolean isUnset() {
        return unset;
    }
}
