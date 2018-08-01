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

import javax.xml.namespace.QName;
import org.geotools.filter.v1_0.OGCDWithinBinding;
import org.geotools.filter.v1_0.OGCUtils;
import org.geotools.filter.v2_0.FES;
import org.geotools.xml.ElementInstance;
import org.geotools.xml.Node;
import org.locationtech.jts.geom.GeometryFactory;
import org.opengis.filter.FilterFactory2;
import org.opengis.filter.expression.Expression;

/**
 * Binding object for the element http://www.opengis.net/ogc:DWithin.
 *
 * <p>
 *
 * <pre>
 *         <code>
 *  &lt;xsd:element name="DWithin" substitutionGroup="ogc:spatialOps" type="ogc:DistanceBufferType"/&gt;
 *
 *          </code>
 *         </pre>
 *
 * @generated
 * @source $URL$
 */
public class DWithinBinding extends OGCDWithinBinding {

    private final FilterFactory2 localFilterFactory;

    private final GeometryFactory localGeometryFactory;

    public DWithinBinding(FilterFactory2 filterFactory, GeometryFactory geometryFactory) {
        super(filterFactory, geometryFactory);
        this.localFilterFactory = filterFactory;
        this.localGeometryFactory = geometryFactory;
    }

    public QName getTarget() {
        return FES.DWithin;
    }

    @Override
    public Object parse(ElementInstance instance, Node node, Object value) throws Exception {
        Expression[] operands = OGCUtils.spatial(node, localFilterFactory, localGeometryFactory);
        double distance = ((Double) node.getChildValue("Distance")).doubleValue();
        Object units = node.getChild("Distance").getAttributeValue("uom");
        return localFilterFactory.dwithin(
                operands[0], operands[1], distance, units == null ? null : units.toString());
    }
}
