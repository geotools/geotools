/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2015, Open Source Geospatial Foundation (OSGeo)
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

import java.awt.AlphaComposite;
import java.awt.Composite;
import java.util.Map;
import org.geotools.renderer.style.SLDStyleFactory;
import org.geotools.styling.AbstractStyleVisitor;
import org.geotools.styling.FeatureTypeStyle;
import org.geotools.styling.Symbolizer;

/**
 * Checks if an AlphaComposite is used anywhere in the style. AlphaComposite is special, as it works
 * properly only if the target for merging also has an alpha channel. BlendComposite does not have
 * such requirements instead.
 *
 * @author Andrea Aime - GeoSolutions
 */
class AlphaCompositeVisitor extends AbstractStyleVisitor {

    boolean alphaComposite = false;

    @Override
    public void visit(FeatureTypeStyle fts) {
        super.visit(fts);
        Map<String, String> options = fts.getOptions();
        checkAlphaComposite(options);
    }

    @Override
    public void visit(Symbolizer sym) {
        // no need to drill down futher
        // super.visit(sym);

        checkAlphaComposite(sym.getOptions());
    }

    private void checkAlphaComposite(Map<String, String> options) {
        if (options != null) {
            Composite composite = SLDStyleFactory.getComposite(options);
            if (composite instanceof AlphaComposite) {
                alphaComposite = true;
            }
        }
    }
}
