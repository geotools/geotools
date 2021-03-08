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

import org.opengis.filter.expression.Expression;

public class OtherTextImpl implements OtherText {

    String location;

    Expression text;

    @Override
    public String getTarget() {
        return location;
    }

    @Override
    public void setTarget(String location) {
        this.location = location;
    }

    @Override
    public Expression getText() {
        return text;
    }

    @Override
    public void setText(Expression text) {
        this.text = text;
    }
}
