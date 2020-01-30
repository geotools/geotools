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
package org.geotools.gml3.bindings.ext;

import org.geotools.gml3.XSDIdRegistry;
import org.geotools.gml3.bindings.CurveTypeBinding;
import org.geotools.gml3.bindings.GML3EncodingUtils;
import org.geotools.xsd.ElementInstance;
import org.geotools.xsd.Node;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.LineString;

/**
 * Binding object for the type http://www.opengis.net/gml:CurvePropertyType.
 *
 * <p>
 *
 * <pre>
 *         <code>
 *  &lt;complexType name="CurvePropertyType"&gt;
 *      &lt;annotation&gt;
 *          &lt;documentation&gt;A property that has a curve as its value domain can either be an appropriate geometry element encapsulated in an
 *                          element of this type or an XLink reference to a remote geometry element (where remote includes geometry elements located elsewhere
 *                          in the same document). Either the reference or the contained element must be given, but neither both nor none.&lt;/documentation&gt;
 *      &lt;/annotation&gt;
 *      &lt;sequence minOccurs="0"&gt;
 *          &lt;element ref="gml:_Curve"/&gt;
 *      &lt;/sequence&gt;
 *      &lt;attributeGroup ref="gml:AssociationAttributeGroup"&gt;
 *          &lt;annotation&gt;
 *              &lt;documentation&gt;This attribute group includes the XLink attributes (see xlinks.xsd). XLink is used in GML to reference remote
 *                                  resources (including those elsewhere in the same document). A simple link element can be constructed by including a specific
 *                                  set of XLink attributes. The XML Linking Language (XLink) is currently a Proposed Recommendation of the World Wide Web Consortium.
 *                                  XLink allows elements to be inserted into XML documents so as to create sophisticated links between resources; such links can be used
 *                                  to reference remote properties. A simple link element can be used to implement pointer functionality, and this functionality has been built
 *                                  into various GML 3 elements by including the gml:AssociationAttributeGroup.&lt;/documentation&gt;
 *          &lt;/annotation&gt;
 *      &lt;/attributeGroup&gt;
 *  &lt;/complexType&gt;
 *
 *          </code>
 * </pre>
 */
@SuppressWarnings("ComparableType")
public class CurvePropertyTypeBinding extends org.geotools.gml3.bindings.CurvePropertyTypeBinding
        implements Comparable {

    GeometryFactory gf;

    public CurvePropertyTypeBinding(
            GML3EncodingUtils encodingUtils, XSDIdRegistry idRegistry, GeometryFactory gf) {
        super(encodingUtils, idRegistry);
        this.gf = gf;
    }

    public Class<? extends Geometry> getGeometryType() {
        return LineString.class;
    }

    @Override
    public Object parse(ElementInstance instance, Node node, Object value) throws Exception {
        return node.getChildValue(LineString.class);
    }

    public int compareTo(Object o) {
        if (o instanceof CurveTypeBinding) {
            return 1;
        } else {
            return 0;
        }
    }
}
