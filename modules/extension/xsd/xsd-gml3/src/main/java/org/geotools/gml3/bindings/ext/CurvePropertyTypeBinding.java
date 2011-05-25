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

import javax.xml.namespace.QName;
import org.geotools.gml3.GML;
import org.geotools.gml3.XSDIdRegistry;
import org.geotools.gml3.bindings.CurveTypeBinding;
import org.geotools.gml3.bindings.GML3EncodingUtils;
import org.geotools.xml.ElementInstance;
import org.geotools.xml.Node;

import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.LineString;
import com.vividsolutions.jts.geom.MultiLineString;

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
 * 
 * </p>
 * 
 * @generated
 * 
 *
 * @source $URL: http://svn.osgeo.org/geotools/branches/2.7.x/build/maven/javadoc/../../../modules/extension/xsd/xsd-gml3/src/main/java/org/geotools/gml3/bindings/ext/CurvePropertyTypeBinding.java $
 *         http://svn.osgeo.org/geotools/trunk/modules/extension/xsd/xsd-gml3/src/main/java/org
 *         /geotools/gml3/bindings/CurvePropertyTypeBinding.java $
 */
public class CurvePropertyTypeBinding extends org.geotools.gml3.bindings.CurvePropertyTypeBinding
    implements Comparable {

    GeometryFactory gf;
    
    public CurvePropertyTypeBinding(GML3EncodingUtils encodingUtils, XSDIdRegistry idRegistry, GeometryFactory gf) {
        super(encodingUtils, idRegistry);
        this.gf = gf;
    }

    public Class<? extends Geometry> getGeometryType() {
        return MultiLineString.class;
    }
    
    @Override
    public Object parse(ElementInstance instance, Node node, Object value) throws Exception {
        // LineString
        LineString lineString = (LineString)node.getChildValue(LineString.class);
        // or Curve or OrientableCurve
        MultiLineString multiLineString = (MultiLineString)node.getChildValue(MultiLineString.class);

        if (lineString != null) {
            return new MultiLineString(new LineString[] {lineString}, gf);
        } else {
            return multiLineString;
        }
    }

    @Override
    public Object getProperty(Object object, QName name)
        throws Exception {
        if ("_Curve".equals(name.getLocalPart()) || "AbstractCurve".equals(name.getLocalPart())) {
            MultiLineString multiLineString = (MultiLineString) object;
            // this MultiLineString consists of a single LineString wrapped in a MultiLineString:
            return multiLineString.getGeometryN(0);
        }
        
        return super.getProperty(object, name);
    }

    public int compareTo(Object o) {
        if (o instanceof CurveTypeBinding) {
            return 1;
        } else {
            return 0;
        }
    }

}
