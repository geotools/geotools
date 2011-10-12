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
package org.geotools.filter.v2_0.bindings;

import java.util.List;

import javax.xml.namespace.QName;
import com.vividsolutions.jts.geom.GeometryFactory;
import org.opengis.filter.FilterFactory2;
import org.opengis.filter.expression.Expression;
import org.opengis.filter.spatial.Intersects;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.eclipse.xsd.XSDElementDeclaration;
import org.geotools.filter.v1_0.OGCIntersectsBinding;
import org.geotools.filter.v2_0.FES;
import org.geotools.xml.AbstractComplexBinding;
import org.geotools.xml.ElementInstance;
import org.geotools.xml.Node;


/**
 * Binding object for the element http://www.opengis.net/fes/2.0:Intersects.
 *
 * <p>
 *      <pre>
 *       <code>
 *  &lt;xsd:element name="Intersects" substitutionGroup="fes:spatialOps" type="fes:BinarySpatialOpType"/&gt; 
 *              
 *        </code>
 *       </pre>
 * </p>
 *
 * @generated
 */
public class IntersectsBinding extends OGCIntersectsBinding {

    public IntersectsBinding(FilterFactory2 filterFactory, GeometryFactory geometryFactory) {
        super(filterFactory, geometryFactory);
    }

    public QName getTarget() {
        return FES.Intersects;
    }
}
