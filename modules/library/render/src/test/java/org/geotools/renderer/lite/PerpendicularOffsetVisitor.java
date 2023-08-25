/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2017, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.renderer.lite;

import org.geotools.styling.LinePlacementImpl;
import org.geotools.styling.StyleImpl;
import org.geotools.styling.TextSymbolizerImpl;
import org.geotools.styling.visitor.DuplicatingStyleVisitor;

/**
 * Applies a given perpendicular offset to all text symbolizers
 *
 * @author Andrea Aime - GeoSolutions
 */
class PerpendicularOffsetVisitor extends DuplicatingStyleVisitor {
    double distance;

    public static StyleImpl apply(StyleImpl style, double distance) {
        PerpendicularOffsetVisitor visitor = new PerpendicularOffsetVisitor(distance);
        style.accept(visitor);
        return (StyleImpl) visitor.getCopy();
    }

    public PerpendicularOffsetVisitor(double distance) {
        this.distance = distance;
    }

    @Override
    public void visit(org.geotools.api.style.LinePlacement lp) {
        super.visit(lp);
        LinePlacementImpl copy = (LinePlacementImpl) pages.peek();
        copy.setPerpendicularOffset(ff.literal(distance));
    }

    @Override
    public void visit(org.geotools.api.style.TextSymbolizer text) {
        super.visit(text);
        TextSymbolizerImpl ts = (TextSymbolizerImpl) pages.peek();

        // do we have follow line without line placement?
        if (ts.getLabelPlacement() == null
                && "true".equalsIgnoreCase(ts.getOptions().get("followLine"))) {
            ts.setLabelPlacement(new LinePlacementImpl());
        }
    }
}
