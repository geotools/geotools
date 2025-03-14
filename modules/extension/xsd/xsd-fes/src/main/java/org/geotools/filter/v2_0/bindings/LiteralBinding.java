/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2019, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.filter.v2_0.bindings;

import javax.xml.namespace.QName;
import org.geotools.api.filter.FilterFactory;
import org.geotools.filter.v1_0.OGCLiteralTypeBinding;
import org.geotools.filter.v2_0.FES;

/**
 * Binding object for the element http://www.opengis.net/fes/2.0:Literal.
 *
 * <p>
 *
 * <pre>
 *  <code>
 *  &lt;xsd:element name="Literal" substitutionGroup="fes:expression" type="fes:LiteralType"/&gt;
 *
 *   </code>
 * </pre>
 *
 * @generated
 */
public class LiteralBinding extends OGCLiteralTypeBinding {

    public LiteralBinding(FilterFactory factory) {
        super(factory);
    }

    @Override
    public QName getTarget() {
        return FES.Literal;
    }
}
