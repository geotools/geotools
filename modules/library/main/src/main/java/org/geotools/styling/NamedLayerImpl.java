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
 *
 * Created on November 3, 2003, 10:10 AM
 */
package org.geotools.styling;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import org.geotools.api.style.FeatureTypeConstraint;
import org.geotools.api.style.NamedLayer;
import org.geotools.api.style.Style;
import org.geotools.api.style.StyleVisitor;
import org.geotools.util.Utilities;

/**
 * Default implementation of named layer.
 *
 * @author jamesm
 */
public class NamedLayerImpl extends StyledLayerImpl implements NamedLayer {
    List<Style> styles = new ArrayList<>();

    // FeatureTypeConstraint[] featureTypeConstraints = new FeatureTypeConstraint[0];
    List<FeatureTypeConstraint> featureTypeConstraints = new ArrayList<>();

    @Override
    public List<FeatureTypeConstraint> layerFeatureConstraints() {
        return featureTypeConstraints;
    }

    @Override
    public FeatureTypeConstraint[] getLayerFeatureConstraints() {
        return featureTypeConstraints.toArray(new FeatureTypeConstraint[0]);
    }

    @Override
    public void setLayerFeatureConstraints(FeatureTypeConstraint... featureTypeConstraints) {
        this.featureTypeConstraints.clear();
        this.featureTypeConstraints.addAll(Arrays.asList(featureTypeConstraints));
    }

    @Override
    public Style[] getStyles() {
        return styles.toArray(new Style[0]);
    }

    @Override
    public List<Style> styles() {
        return styles;
    }

    @Override
    public void addStyle(Style sl) {
        styles.add(sl);
    }

    @Override
    public void accept(StyleVisitor visitor) {
        visitor.visit(this);
    }

    @Override
    public boolean equals(Object oth) {
        if (this == oth) {
            return true;
        }

        if (oth instanceof NamedLayerImpl) {
            NamedLayerImpl other = (NamedLayerImpl) oth;

            if (!Utilities.equals(styles, other.styles)) return false;

            if (!Utilities.equals(featureTypeConstraints, other.featureTypeConstraints)) {
                return false;
            }
            return true;
        }

        return false;
    }

    @Override
    public int hashCode() {
        return Objects.hash(styles, featureTypeConstraints);
    }
}
