/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2021, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.styling.visitor;

import static org.geotools.styling.FeatureTypeStyle.RenderingSelectionOptions.LEGENDONLY;

/** RenderingSelectorStyleVisitor implementation meant to be used when rendering legends * */
public class LegendRenderingSelectorStyleVisitor extends RenderingSelectorStyleVisitor {
    @Override
    protected boolean canRenderInternal(String value) {
        if (value.equalsIgnoreCase(LEGENDONLY.name())) return true;
        return false;
    }
}
