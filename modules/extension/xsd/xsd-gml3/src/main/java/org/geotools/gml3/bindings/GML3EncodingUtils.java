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
package org.geotools.gml3.bindings;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import javax.xml.XMLConstants;
import javax.xml.namespace.QName;

import org.eclipse.xsd.XSDElementDeclaration;
import org.geotools.feature.NameImpl;
import org.geotools.geometry.DirectPosition2D;
import org.geotools.gml2.bindings.GML2EncodingUtils;
import org.geotools.gml2.bindings.GMLEncodingUtils;
//import org.geotools.gml3.GML;
import org.geotools.gml3.GML;
import org.geotools.gml3.XSDIdRegistry;
import org.geotools.util.Converters;
import org.geotools.xlink.XLINK;
import org.geotools.xml.ComplexBinding;
import org.geotools.xml.Configuration;
import org.geotools.xml.SchemaIndex;
import org.geotools.xml.XSD;
import org.opengis.feature.ComplexAttribute;
import org.opengis.feature.Feature;
import org.opengis.feature.Property;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.type.Name;
import org.opengis.geometry.DirectPosition;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Text;
import org.xml.sax.Attributes;

import com.vividsolutions.jts.geom.CoordinateSequence;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.LineString;

/**
 * Utility class for gml3 encoding.
 * 
 * @author Justin Deoliveira, The Open Planning Project, jdeolive@openplans.org
 * @author Ben Caradoc-Davies, CSIRO Exploration and Mining
 * 
 * @source $URL:
 *         http://svn.osgeo.org/geotools/trunk/modules/extension/xsd/xsd-gml3/src/main/java/org
 *         /geotools/gml3/bindings/GML3EncodingUtils.java $
 */
public class GML3EncodingUtils {

    static GML3EncodingUtils INSTANCE = new GML3EncodingUtils();

    XSD gml;

    GMLEncodingUtils e;

    public GML3EncodingUtils() {
        this(GML.getInstance());
    }

    public GML3EncodingUtils(XSD gml) {
        this.gml = gml;
        e = new GMLEncodingUtils(gml);
    }

    static DirectPosition[] positions(LineString line) {
        CoordinateSequence coordinates = line.getCoordinateSequence();
        DirectPosition[] dps = new DirectPosition[coordinates.size()];

        double x;
        double y;

        for (int i = 0; i < dps.length; i++) {
            x = coordinates.getOrdinate(i, 0);
            y = coordinates.getOrdinate(i, 1);
            dps[i] = new DirectPosition2D(x, y);
        }

        return dps;
    }

    static URI toURI(CoordinateReferenceSystem crs) {
        if (crs == null) {
            return null;
        }

        try {
            String crsCode = GML2EncodingUtils.crs(crs);

            if (crsCode != null) {
                return new URI(crsCode);
            } else {
                return null;
            }
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * @deprecated use {@link #toURI(CoordinateReferenceSystem)}.
     */
    static URI crs(CoordinateReferenceSystem crs) {
        return toURI(crs);
    }

    static CoordinateReferenceSystem getCRS(Geometry g) {
        return GML2EncodingUtils.getCRS(g);
    }

    static String getID(Geometry g) {
        return GML2EncodingUtils.getID(g);
    }

    static String getName(Geometry g) {
        return GML2EncodingUtils.getName(g);
    }

    static String getDescription(Geometry g) {
        return GML2EncodingUtils.getDescription(g);
    }

    /**
     * Helper method used to implement {@link ComplexBinding#getProperty(Object, QName)} for
     * bindings of geometry reference types:
     * <ul>
     * <li>GeometryPropertyType
     * <li>PointPropertyType
     * <li>LineStringPropertyType
     * <li>PolygonPropertyType
     * </ul>
     */
    public Object GeometryPropertyType_GetProperty(Geometry geometry, QName name) {
        return e.GeometryPropertyType_getProperty(geometry, name);
    }

    /**
     * Helper method used to implement {@link ComplexBinding#getProperty(Object, QName)} for
     * bindings of geometry reference types:
     * <ul>
     * <li>GeometryPropertyType
     * <li>PointPropertyType
     * <li>LineStringPropertyType
     * <li>PolygonPropertyType
     * </ul>
     */
    public Object GeometryPropertyType_GetProperty(Geometry geometry, QName name, boolean makeEmpty) {
        return e.GeometryPropertyType_getProperty(geometry, name, true, makeEmpty);
    }

    /**
     * @deprecated use {@link #GeometryPropertyType_GetProperty(Geometry, QName)}
     */
    public static Object getProperty(Geometry geometry, QName name) {
        return INSTANCE.GeometryPropertyType_GetProperty(geometry, name);
    }

    /**
     * Helper method used to implement {@link ComplexBinding#getProperties(Object)} for bindings of
     * geometry reference types:
     * <ul>
     * <li>GeometryPropertyType
     * <li>PointPropertyType
     * <li>LineStringPropertyType
     * <li>PolygonPropertyType
     * </ul>
     */

    public List GeometryPropertyType_GetProperties(Geometry geometry) {
        return e.GeometryPropertyType_getProperties(geometry);
    }

    /**
     * @deprecated use {@link #GeometryPropertyType_GetProperties(Geometry)}
     */
    public static List getProperties(Geometry geometry) {
        return INSTANCE.GeometryPropertyType_GetProperties(geometry);
    }

    public Element AbstractFeatureTypeEncode(Object object, Document document, Element value,
            XSDIdRegistry idSet) {
        Feature feature = (Feature) object;
        String id = (feature.getIdentifier() != null ? feature.getIdentifier().getID() : null);
        Name typeName;
        if (feature.getDescriptor() == null) {
            // no descriptor, assume WFS feature type name is the same as the name of the content
            // model type
            typeName = feature.getType().getName();
        } else {
            // honour the name set in the descriptor
            typeName = feature.getDescriptor().getName();
        }
        Element encoding = document.createElementNS(typeName.getNamespaceURI(),
                typeName.getLocalPart());
        if (id != null) {
            if (!(feature instanceof SimpleFeature) && idSet != null) {
                if (idSet.idExists(id)) {
                    // XSD type ids can only appear once in the same document, otherwise the
                    // document is
                    // not schema valid. Attributes of the same ids should be encoded as xlink:href
                    // to
                    // the existing attribute.
                    encoding.setAttributeNS(XLINK.NAMESPACE, XLINK.HREF.getLocalPart(), "#"
                            + id.toString());
                    // make sure the attributes aren't encoded
                    feature.setValue(Collections.emptyList());
                    return encoding;
                } else {
                    idSet.add(id);
                }
            }
            encoding.setAttributeNS(gml.getNamespaceURI(), "id", id);
        }
        encodeClientProperties(feature, value);

        return encoding;
    }

    /**
     * @deprecated use {@link #AbstractFeatureTypeEncode(Object, Document, Element, XSDIdRegistry)}
     */
    public static Element AbstractFeatureType_encode(Object object, Document document,
            Element value, XSDIdRegistry idSet) {
        return INSTANCE.AbstractFeatureTypeEncode(object, document, value, idSet);
    }

    public List AbstractFeatureTypeGetProperties(Object object, XSDElementDeclaration element,
            SchemaIndex schemaIndex, Configuration configuration) {
        return e.AbstractFeatureType_getProperties(
                object,
                element,
                schemaIndex,
                new HashSet<String>(Arrays.asList("name", "description", "boundedBy", "location",
                        "metaDataProperty")), configuration);
    }

    /**
     * @deprecated use
     *             {@link #AbstractFeatureTypeGetProperties(Object, XSDElementDeclaration, SchemaIndex, Configuration)

     */
    public static List AbstractFeatureType_getProperties(Object object,
            XSDElementDeclaration element, SchemaIndex schemaIndex, Configuration configuration) {
        return INSTANCE.AbstractFeatureTypeGetProperties(object, element, schemaIndex,
                configuration);
    }

    /**
     * Encode any client properties (XML attributes) found in the UserData map of a ComplexAttribute
     * as XML attributes of the element.
     * 
     * @param complex
     *            the ComplexAttribute to search for client properties
     * @param element
     *            the element to which XML attributes should be added
     */
    @SuppressWarnings("unchecked")
    public static void encodeClientProperties(Property complex, Element element) {
        Map<Name, Object> clientProperties = (Map<Name, Object>) complex.getUserData().get(
                Attributes.class);
        if (clientProperties != null) {
            for (Name name : clientProperties.keySet()) {
                if (clientProperties.get(name) != null) {
                    element.setAttributeNS(name.getNamespaceURI(), name.getLocalPart(),
                            clientProperties.get(name).toString());
                }
            }
        }
    }

    /**
     * Encode the simpleContent property of a ComplexAttribute (if any) as an XML text node.
     * 
     * <p>
     * 
     * A property named simpleContent is a convention for representing XSD complexType with
     * simpleContent in GeoAPI.
     * 
     * @param complex
     *            the ComplexAttribute to be searched for simpleContent
     * @param document
     *            the containing document
     * @param element
     *            the element to which text node should be added
     */
    public static void encodeSimpleContent(ComplexAttribute complex, Document document,
            Element element) {
        Object value = getSimpleContent(complex);
        if (value != null) {
            Text text = document.createTextNode(Converters.convert(value, String.class));
            element.appendChild(text);
        }
    }

    /**
     * Return the simple content of a {@link ComplexAttribute} if it represents a complexType with
     * simpleContent, otherwise null.
     * 
     * @param complex
     * @return
     */
    public static Object getSimpleContent(ComplexAttribute complex) {
        Property simpleContent = complex.getProperty(new NameImpl("simpleContent"));
        if (simpleContent == null) {
            return null;
        } else {
            return simpleContent.getValue();
        }
    }

}
