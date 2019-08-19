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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.geotools.styling.Extent;
import org.geotools.styling.FeatureTypeConstraint;
import org.opengis.filter.Filter;

public class FeatureTypeConstraintBuilder extends AbstractSLDBuilder<FeatureTypeConstraint> {
    private List<Extent> extents = new ArrayList<Extent>();

    private Filter filter;

    private String featureTypeName;

    public FeatureTypeConstraintBuilder() {
        this(null);
    }

    public FeatureTypeConstraintBuilder(AbstractSLDBuilder<?> parent) {
        super(parent);
        reset();
    }

    public FeatureTypeConstraintBuilder extent(String name, String value) {
        this.unset = false;
        extents.add(sf.createExtent(name, value));
        return this;
    }

    public FeatureTypeConstraintBuilder filter(Filter filter) {
        this.unset = false;
        this.filter = filter;
        return this;
    }

    public FeatureTypeConstraintBuilder featureTypeName(String name) {
        this.unset = false;
        this.featureTypeName = name;
        return this;
    }

    public FeatureTypeConstraint build() {
        if (unset) {
            return null;
        }

        Extent[] ea = extents.toArray(new Extent[extents.size()]);
        FeatureTypeConstraint constraint =
                sf.createFeatureTypeConstraint(featureTypeName, filter, ea);

        if (parent == null) {
            reset();
        }

        return constraint;
    }

    public FeatureTypeConstraintBuilder reset() {
        unset = false;
        featureTypeName = null;
        filter = null;
        extents.clear();
        return this;
    }

    public FeatureTypeConstraintBuilder reset(FeatureTypeConstraint constraint) {
        if (constraint == null) {
            return unset();
        }
        featureTypeName = constraint.getFeatureTypeName();
        filter = constraint.getFilter();
        extents.clear();
        extents.addAll(Arrays.asList(constraint.getExtents()));
        unset = false;
        return this;
    }

    public FeatureTypeConstraintBuilder unset() {
        return (FeatureTypeConstraintBuilder) super.unset();
    }

    @Override
    protected void buildSLDInternal(StyledLayerDescriptorBuilder sb) {
        throw new UnsupportedOperationException(
                "Can't build a SLD out of a feature type contraint");
    }
}
