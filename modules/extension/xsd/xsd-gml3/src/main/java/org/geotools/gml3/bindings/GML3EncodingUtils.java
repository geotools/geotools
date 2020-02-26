/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2002-2015, Open Source Geospatial Foundation (OSGeo)
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
import java.util.Enumeration;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import javax.xml.namespace.QName;
import org.eclipse.xsd.XSDElementDeclaration;
import org.geotools.feature.NameImpl;
import org.geotools.geometry.jts.LiteCoordinateSequence;
import org.geotools.geometry.jts.SingleCurvedGeometry;
import org.geotools.gml2.SrsSyntax;
import org.geotools.gml2.bindings.GML2EncodingUtils;
import org.geotools.gml2.bindings.GMLEncodingUtils;
import org.geotools.gml3.GML;
import org.geotools.gml3.XSDIdRegistry;
import org.geotools.util.Converters;
import org.geotools.xlink.XLINK;
import org.geotools.xsd.ComplexBinding;
import org.geotools.xsd.Configuration;
import org.geotools.xsd.SchemaIndex;
import org.geotools.xsd.XSD;
import org.locationtech.jts.geom.CoordinateSequence;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.LineString;
import org.opengis.feature.ComplexAttribute;
import org.opengis.feature.Feature;
import org.opengis.feature.Property;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.type.Name;
import org.opengis.filter.identity.FeatureId;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Text;
import org.xml.sax.Attributes;
import org.xml.sax.helpers.NamespaceSupport;

/**
 * Utility class for gml3 encoding.
 *
 * @author Justin Deoliveira, The Open Planning Project, jdeolive@openplans.org
 * @author Ben Caradoc-Davies, CSIRO Exploration and Mining
 */
public class GML3EncodingUtils {

    public static GML3EncodingUtils INSTANCE = new GML3EncodingUtils();

    XSD gml;

    GMLEncodingUtils e;

    public GML3EncodingUtils() {
        this(GML.getInstance());
    }

    public GML3EncodingUtils(XSD gml) {
        this.gml = gml;
        e = new GMLEncodingUtils(gml);
    }

    static CoordinateSequence positions(LineString line) {
        if (line instanceof SingleCurvedGeometry<?>) {
            SingleCurvedGeometry<?> curved = (SingleCurvedGeometry<?>) line;
            return new LiteCoordinateSequence(curved.getControlPoints());
        } else {
            return line.getCoordinateSequence();
        }
    }

    public static URI toURI(CoordinateReferenceSystem crs, SrsSyntax srsSyntax) {
        if (crs == null) {
            return null;
        }

        try {
            String crsCode = GML2EncodingUtils.toURI(crs, srsSyntax);

            if (crsCode != null) {
                return new URI(crsCode);
            } else {
                return null;
            }
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }

    static CoordinateReferenceSystem getCRS(Geometry g) {
        return GML2EncodingUtils.getCRS(g);
    }

    /** Get uomLabels for the geometry if set in app-schema mapping configuration. */
    public static String getUomLabels(Geometry g) {
        Object userData = g.getUserData();
        if (userData != null && userData instanceof Map) {
            Object attributes = ((Map) userData).get(Attributes.class);
            if (attributes != null && attributes instanceof Map) {
                Name attribute = new NameImpl("uomLabels");
                Object uomLabels = ((Map) attributes).get(attribute);
                if (uomLabels != null) {
                    return uomLabels.toString();
                }
            }
        }
        return null;
    }

    /** Get axisLabels for the geometry if set in app-schema mapping configuration. */
    public static String getAxisLabels(Geometry g) {
        Object userData = g.getUserData();
        if (userData != null && userData instanceof Map) {
            Object attributes = ((Map) userData).get(Attributes.class);
            if (attributes != null && attributes instanceof Map) {
                Name attribute = new NameImpl("axisLabels");
                Object axisLabels = ((Map) attributes).get(attribute);
                if (axisLabels != null) {
                    return axisLabels.toString();
                }
            }
        }
        return null;
    }

    public static String getID(Geometry g) {
        return GML2EncodingUtils.getID(g);
    }

    public static void setID(Geometry g, String id) {
        GML2EncodingUtils.setID(g, id);
    }

    static String getName(Geometry g) {
        return GML2EncodingUtils.getName(g);
    }

    static void setName(Geometry g, String name) {
        GML2EncodingUtils.setName(g, name);
    }

    static String getDescription(Geometry g) {
        return GML2EncodingUtils.getDescription(g);
    }

    static void setDescription(Geometry g, String description) {
        GML2EncodingUtils.setDescription(g, description);
    }

    /**
     * Set a synthetic gml:id on each child of a multigeometry. If the multigeometry has no gml:id,
     * this method has no effect. The synthetic gml:id of each child is constructed from that of the
     * parent by appending "." and then an integer starting from one for the first child.
     *
     * @param multiGeometry parent multigeometry containing the children to be modified
     */
    static void setChildIDs(Geometry multiGeometry) {
        String id = getID(multiGeometry);
        if (id != null) {
            for (int i = 0; i < multiGeometry.getNumGeometries(); i++) {
                StringBuilder builder = new StringBuilder(id);
                builder.append("."); // separator
                builder.append(i + 1); // synthetic gml:id suffix one-based
                GML2EncodingUtils.setID(multiGeometry.getGeometryN(i), builder.toString());
            }
        }
    }

    /**
     * Helper method used to implement {@link ComplexBinding#getProperty(Object, QName)} for
     * bindings of geometry reference types:
     *
     * <ul>
     *   <li>GeometryPropertyType
     *   <li>PointPropertyType
     *   <li>LineStringPropertyType
     *   <li>PolygonPropertyType
     * </ul>
     */
    public Object GeometryPropertyType_GetProperty(Geometry geometry, QName name) {
        return e.GeometryPropertyType_getProperty(geometry, name);
    }

    /**
     * Helper method used to implement {@link ComplexBinding#getProperty(Object, QName)} for
     * bindings of geometry reference types:
     *
     * <ul>
     *   <li>GeometryPropertyType
     *   <li>PointPropertyType
     *   <li>LineStringPropertyType
     *   <li>PolygonPropertyType
     * </ul>
     */
    public Object GeometryPropertyType_GetProperty(
            Geometry geometry, QName name, boolean makeEmpty) {
        return e.GeometryPropertyType_getProperty(geometry, name, true, makeEmpty);
    }

    /**
     * Helper method used to implement {@link ComplexBinding#getProperties(Object,
     * XSDElementDeclaration)} for bindings of geometry reference types:
     *
     * <ul>
     *   <li>GeometryPropertyType
     *   <li>PointPropertyType
     *   <li>LineStringPropertyType
     *   <li>PolygonPropertyType
     * </ul>
     */
    public List GeometryPropertyType_GetProperties(Geometry geometry) {
        return e.GeometryPropertyType_getProperties(geometry);
    }

    public Element AbstractFeatureTypeEncode(
            Object object, Document document, Element value, XSDIdRegistry idSet) {
        Feature feature = (Feature) object;
        String id = null;
        FeatureId identifier = feature.getIdentifier();
        if (identifier != null) {
            id = identifier.getRid();
        }

        Name typeName;
        if (feature.getDescriptor() == null) {
            // no descriptor, assume WFS feature type name is the same as the name of the content
            // model type
            typeName = feature.getType().getName();
        } else {
            // honour the name set in the descriptor
            typeName = feature.getDescriptor().getName();
        }
        Element encoding =
                document.createElementNS(typeName.getNamespaceURI(), typeName.getLocalPart());
        if (id != null) {
            if (!(feature instanceof SimpleFeature) && idSet != null) {
                if (idSet.idExists(id)) {
                    // XSD type ids can only appear once in the same document, otherwise the
                    // document is
                    // not schema valid. Attributes of the same ids should be encoded as xlink:href
                    // to
                    // the existing attribute.
                    encoding.setAttributeNS(
                            XLINK.NAMESPACE, XLINK.HREF.getLocalPart(), "#" + id.toString());
                    // make sure the attributes aren't encoded
                    feature.setValue(Collections.emptyList());
                    return encoding;
                } else {
                    idSet.add(id);
                }
            }
            encoding.setAttributeNS(gml.getNamespaceURI(), "id", id);
        }
        encodeClientProperties(feature, encoding);

        return encoding;
    }

    public List AbstractFeatureTypeGetProperties(
            Object object,
            XSDElementDeclaration element,
            SchemaIndex schemaIndex,
            Configuration configuration) {
        return e.AbstractFeatureType_getProperties(
                object,
                element,
                schemaIndex,
                new HashSet<String>(
                        Arrays.asList(
                                "name",
                                "description",
                                "boundedBy",
                                "location",
                                "metaDataProperty")),
                configuration);
    }

    /**
     * Encode any client properties (XML attributes) found in the UserData map of a ComplexAttribute
     * as XML attributes of the element.
     *
     * @param complex the ComplexAttribute to search for client properties
     * @param element the element to which XML attributes should be added
     */
    @SuppressWarnings("unchecked")
    public static void encodeClientProperties(Property complex, Element element) {
        Map<Name, Object> clientProperties =
                (Map<Name, Object>) complex.getUserData().get(Attributes.class);
        if (clientProperties != null) {
            for (Name name : clientProperties.keySet()) {
                if (clientProperties.get(name) != null) {
                    element.setAttributeNS(
                            name.getNamespaceURI(),
                            name.getLocalPart(),
                            clientProperties.get(name).toString());
                }
            }
        }
    }

    /**
     * Encode the simpleContent property of a ComplexAttribute (if any) as an XML text node.
     *
     * <p>A property named simpleContent is a convention for representing XSD complexType with
     * simpleContent in GeoAPI.
     *
     * @param complex the ComplexAttribute to be searched for simpleContent
     * @param document the containing document
     * @param element the element to which text node should be added
     */
    public static void encodeSimpleContent(
            ComplexAttribute complex, Document document, Element element) {
        Object value = getSimpleContent(complex);
        encodeAsText(document, element, value);
    }

    public static void encodeAsText(Document document, Element element, Object value) {
        if (value != null) {
            Text text = document.createTextNode(Converters.convert(value, String.class));
            element.appendChild(text);
        }
    }

    /**
     * Return the simple content of a {@link ComplexAttribute} if it represents a complexType with
     * simpleContent, otherwise null.
     */
    public static Object getSimpleContent(ComplexAttribute complex) {
        Property simpleContent = complex.getProperty(new NameImpl("simpleContent"));
        if (simpleContent == null) {
            return null;
        } else {
            return simpleContent.getValue();
        }
    }

    /**
     * Deep clones a {@link NamespaceSupport} so that it can be used outside of this parse (as its
     * state changes during the parse, and we need to keep all namespace mapping present at this
     * point for later usage)
     */
    public static NamespaceSupport copyNamespaceSupport(NamespaceSupport namespaceSupport) {
        NamespaceSupport copy = new NamespaceSupport();
        Enumeration prefixes = namespaceSupport.getPrefixes();
        while (prefixes.hasMoreElements()) {
            String prefix = (String) prefixes.nextElement();
            String uri = namespaceSupport.getURI(prefix);
            copy.declarePrefix(prefix, uri);
        }
        // the above did not cover the default prefix
        String defaultUri = namespaceSupport.getURI("");
        if (defaultUri != null) {
            copy.declarePrefix("", defaultUri);
        }

        return copy;
    }
}
