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

import org.geotools.api.filter.expression.Expression;
import org.geotools.api.style.StyleVisitor;

abstract class ConstantDisplacement extends Displacement
        implements org.geotools.api.style.Displacement {
    private void cannotModifyConstant() {
        throw new UnsupportedOperationException("Constant Displacement may not be modified");
    }

    @Override
    public void setDisplacementX(Expression x) {
        cannotModifyConstant();
    }

    @Override
    public void setDisplacementY(Expression y) {
        cannotModifyConstant();
    }

    @Override
    public void accept(StyleVisitor visitor) {
        super.accept(visitor);
    }
}
