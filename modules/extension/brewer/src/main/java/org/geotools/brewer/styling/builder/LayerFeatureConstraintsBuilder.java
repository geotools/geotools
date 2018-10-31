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

import org.geotools.brewer.styling.filter.expression.ChildExpressionBuilder;
import org.geotools.factory.CommonFactoryFinder;
import org.geotools.styling.FeatureTypeConstraint;
import org.geotools.styling.LayerFeatureConstraints;
import org.geotools.styling.StyleFactory;

public class LayerFeatureConstraintsBuilder<P> implements Builder<LayerFeatureConstraints> {
    private StyleFactory sf = CommonFactoryFinder.getStyleFactory(null);

    private P parent;

    private ChildExpressionBuilder<LayerFeatureConstraintsBuilder<P>> x =
            new ChildExpressionBuilder<LayerFeatureConstraintsBuilder<P>>(this);

    private ChildExpressionBuilder<LayerFeatureConstraintsBuilder<P>> y =
            new ChildExpressionBuilder<LayerFeatureConstraintsBuilder<P>>(this);

    boolean unset = true; // current value is null

    public LayerFeatureConstraintsBuilder() {
        this(null);
    }

    public LayerFeatureConstraintsBuilder(P parent) {
        this.parent = parent;
        reset();
    }

    public LayerFeatureConstraints build() {
        if (unset) {
            return null;
        }
        FeatureTypeConstraint[] featureTypeConstraints = null;
        LayerFeatureConstraints constraints =
                sf.createLayerFeatureConstraints(featureTypeConstraints);
        return constraints;
    }

    public P end() {
        return parent;
    }

    public LayerFeatureConstraintsBuilder<P> reset() {
        x.reset().literal(0);
        y.reset().literal(0);
        unset = false;
        return this;
    }

    public LayerFeatureConstraintsBuilder<P> reset(LayerFeatureConstraints constraints) {
        if (constraints == null) {
            return reset();
        }

        unset = false;
        return this;
    }

    public LayerFeatureConstraintsBuilder<P> unset() {
        x.unset();
        y.unset();
        unset = true;
        return this;
    }
}
