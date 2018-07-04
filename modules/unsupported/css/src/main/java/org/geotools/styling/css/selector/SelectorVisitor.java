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
package org.geotools.styling.css.selector;

public interface SelectorVisitor {

    public Object visit(Accept accept);

    public Object visit(Reject reject);

    public Object visit(Id id);

    public Object visit(Data data);

    public Object visit(And and);

    public Object visit(Or or);

    public Object visit(TypeName typeName);

    public Object visit(ScaleRange scaleRange);

    public Object visit(PseudoClass pseudoClass);
}
