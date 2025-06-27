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
package org.geotools.gml2.bindings;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import javax.xml.XMLConstants;
import javax.xml.namespace.QName;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.eclipse.xsd.XSDComplexTypeDefinition;
import org.eclipse.xsd.XSDCompositor;
import org.eclipse.xsd.XSDDerivationMethod;
import org.eclipse.xsd.XSDElementDeclaration;
import org.eclipse.xsd.XSDFactory;
import org.eclipse.xsd.XSDModelGroup;
import org.eclipse.xsd.XSDParticle;
import org.eclipse.xsd.XSDTypeDefinition;
import org.eclipse.xsd.util.XSDConstants;
import org.geotools.api.feature.ComplexAttribute;
import org.geotools.api.feature.Feature;
import org.geotools.api.feature.GeometryAttribute;
import org.geotools.api.feature.Property;
import org.geotools.api.feature.simple.SimpleFeature;
import org.geotools.api.feature.simple.SimpleFeatureType;
import org.geotools.api.feature.type.AttributeDescriptor;
import org.geotools.api.feature.type.ComplexType;
import org.geotools.api.feature.type.FeatureType;
import org.geotools.api.feature.type.GeometryDescriptor;
import org.geotools.api.feature.type.Name;
import org.geotools.api.geometry.BoundingBox;
import org.geotools.api.referencing.crs.CoordinateReferenceSystem;
import org.geotools.feature.NameImpl;
import org.geotools.geometry.jts.JTS;
import org.geotools.gml2.GMLConfiguration;
import org.geotools.util.logging.Logging;
import org.geotools.xlink.XLINK;
import org.geotools.xs.XS;
import org.geotools.xsd.Configuration;
import org.geotools.xsd.SchemaIndex;
import org.geotools.xsd.Schemas;
import org.geotools.xsd.XSD;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.GeometryCollection;
import org.locationtech.jts.geom.LineString;
import org.locationtech.jts.geom.MultiLineString;
import org.locationtech.jts.geom.MultiPoint;
import org.locationtech.jts.geom.MultiPolygon;
import org.locationtech.jts.geom.Point;
import org.locationtech.jts.geom.Polygon;
import org.w3c.dom.Document;
import org.xml.sax.Attributes;

public class GMLEncodingUtils {

    /** logging instance */
    static Logger LOGGER = Logging.getLogger(GMLEncodingUtils.class);

    XSD gml;

    public GMLEncodingUtils(XSD gml) {
        this.gml = gml;
    }

    @SuppressWarnings("unchecked")
    public List<Object[]> AbstractFeatureType_getProperties(
            Object object,
            XSDElementDeclaration element,
            SchemaIndex schemaIndex,
            Set<String> toFilter,
            Configuration configuration) {

        Feature feature = (Feature) object;

        // check if this was a resolved feature, if so dont return anything
        // TODO: this is just a hack for our lame xlink implementation
        if (feature.getUserData().get("xlink:id") != null) {
            return Collections.emptyList();
        }

        FeatureType featureType = feature.getType();

        String namespace = featureType.getName().getNamespaceURI();

        if (namespace == null) {
            namespace = element.getTargetNamespace();
        }

        String typeName = featureType.getName().getLocalPart();
        QName qualifiedTypeName = new QName(namespace, typeName);

        // find the type in the schema
        XSDTypeDefinition type = schemaIndex.getTypeDefinition(qualifiedTypeName);

        if (type == null) {
            // type not found, do a check for an element, and use its type
            XSDElementDeclaration e = schemaIndex.getElementDeclaration(qualifiedTypeName);

            if (e != null) {
                type = e.getTypeDefinition();
            }
        }

        if (type == null) {
            if (featureType instanceof SimpleFeatureType) {
                // could not find the feature type in the schema, create a mock one
                LOGGER.fine("Could find type for " + typeName + " in the schema, generating type from feature.");
                type = createXmlTypeFromFeatureType((SimpleFeatureType) featureType, schemaIndex, toFilter);
            } else {
                // look for an element declaration smuggled in the UserData map.
                XSDElementDeclaration e = (XSDElementDeclaration)
                        feature.getDescriptor().getUserData().get(XSDElementDeclaration.class);
                if (e != null) {
                    type = e.getTypeDefinition();
                } else if (element != null) {
                    // as a last resort, use type definition from element declaration
                    XSDTypeDefinition elementTypeDef = element.getTypeDefinition();
                    QName qualifiedElementTypeName =
                            new QName(elementTypeDef.getTargetNamespace(), elementTypeDef.getName());
                    if (qualifiedTypeName.equals(qualifiedElementTypeName)) {
                        type = elementTypeDef;
                    }
                }
            }
        }

        if (type == null) {
            throw new RuntimeException("Could not find type for " + qualifiedTypeName + " in schema");
        }

        List particles = Schemas.getChildElementParticles(type, true);
        List properties = new ArrayList<>();
        Set<Name> unsubstPropertyNames = null;

        O:
        for (int i = 0; i < particles.size(); i++) {
            XSDParticle particle = (XSDParticle) particles.get(i);
            XSDElementDeclaration attribute = (XSDElementDeclaration) particle.getContent();

            if (attribute.isElementDeclarationReference()) {
                attribute = attribute.getResolvedElementDeclaration();
            }

            if (gml.qName("boundedBy").equals(new QName(attribute.getTargetNamespace(), attribute.getName()))) {
                BoundingBox bounds = getBoundedBy(feature, configuration);
                if (bounds != null) {
                    properties.add(new Object[] {particle, bounds});
                }
            } else if (featureType instanceof SimpleFeatureType) {
                // first simple feature hack, if the schema "overrides" gml attributes like
                // name and description, ignore the gml version
                boolean skip = false;
                if (gml.getNamespaceURI().equals(attribute.getTargetNamespace())) {
                    for (int j = i + 1; j < particles.size(); j++) {
                        XSDParticle particle2 = (XSDParticle) particles.get(j);
                        XSDElementDeclaration attribute2 = (XSDElementDeclaration) particle2.getContent();
                        if (attribute2.isElementDeclarationReference()) {
                            attribute2 = attribute2.getResolvedElementDeclaration();
                        }
                        if (attribute2.getName().equals(attribute.getName())) {
                            skip = true;
                            break;
                        }
                    }
                }
                if (skip) {
                    continue;
                }

                // simple feature brain damage: discard namespace
                // make sure the feature type has an element
                if (!isValidDescriptor(featureType, new NameImpl(attribute.getName()))) {
                    continue;
                }
                // get the value
                Object attributeValue = ((SimpleFeature) feature).getAttribute(attribute.getName());
                if (attributeValue != null && attributeValue instanceof Geometry) {
                    JTS.setCRS((Geometry) attributeValue, featureType.getCoordinateReferenceSystem());
                }
                properties.add(new Object[] {particle, attributeValue});
            } else {
                // namespaces matter for non-simple feature types
                Name propertyName = new NameImpl(attribute.getTargetNamespace(), attribute.getName());
                // make sure the feature type has an element
                if (!isValidDescriptor(featureType, propertyName)) {
                    continue;
                }
                Collection<Property> featureProperties = feature.getProperties(propertyName);
                // if no feature properties are found for this element check substitution groups
                if (featureProperties.isEmpty()) {
                    if (unsubstPropertyNames == null) {
                        // lazy initialisation of a set of all property names that
                        // will be obtained without considering substitution groups
                        unsubstPropertyNames = (Set<Name>) particles.stream()
                                .map(GMLEncodingUtils::resolvedName)
                                .collect(Collectors.toSet());
                    }
                    for (XSDElementDeclaration xsdElementDeclaration : attribute.getSubstitutionGroup()) {
                        Name substPropertyName = new NameImpl(
                                xsdElementDeclaration.getTargetNamespace(), xsdElementDeclaration.getName());
                        if (!unsubstPropertyNames.contains(substPropertyName)) {
                            featureProperties = feature.getProperties(substPropertyName);
                            if (!featureProperties.isEmpty()) {
                                // the particle is used outside this class, replace
                                // the particle with the correct substituted element
                                particle = (XSDParticle) particle.cloneConcreteComponent(true, false);
                                particle.setContent(xsdElementDeclaration);
                                break;
                            }
                        }
                    }
                }
                // get the value (might be multiple)
                for (Property property : featureProperties) {
                    Object value;
                    if (property instanceof ComplexAttribute) {
                        // do not unpack complex attributes as these may have their own bindings,
                        // which
                        // will be applied by the encoder
                        value = property;
                    } else if (property instanceof GeometryAttribute) {
                        value = property.getValue();
                        if (value != null) {
                            // ensure CRS is passed to the Geometry object
                            Geometry geometry = (Geometry) value;
                            CoordinateReferenceSystem crs = ((GeometryAttribute) property)
                                    .getDescriptor()
                                    .getCoordinateReferenceSystem();
                            Map<Object, Object> userData = new HashMap<>();
                            Object obj = geometry.getUserData();
                            if (obj != null && obj instanceof Map) {
                                userData.putAll((Map) obj);
                            }
                            userData.put(CoordinateReferenceSystem.class, crs);
                            geometry.setUserData(userData);
                        }
                    } else {
                        // non-complex bindings are unpacked as for simple feature case
                        value = property.getValue();
                    }
                    properties.add(new Object[] {particle, value});
                }
            }
        }

        return properties;
    }

    private static Object resolvedName(Object p) {
        XSDElementDeclaration attr = (XSDElementDeclaration) ((XSDParticle) p).getContent();
        if (attr.isElementDeclarationReference()) {
            attr = attr.getResolvedElementDeclaration();
        }
        return new NameImpl(attr.getTargetNamespace(), attr.getName());
    }

    public XSDTypeDefinition createXmlTypeFromFeatureType(
            SimpleFeatureType featureType, SchemaIndex schemaIndex, Set<String> toFilter) {
        XSDFactory f = XSDFactory.eINSTANCE;
        Document dom;
        try {
            dom = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();
        } catch (ParserConfigurationException e) {
            throw new RuntimeException(e);
        }

        XSDComplexTypeDefinition type = f.createXSDComplexTypeDefinition();
        type.setTargetNamespace(featureType.getName().getNamespaceURI());
        type.setName(featureType.getTypeName() + "Type");
        type.setDerivationMethod(XSDDerivationMethod.EXTENSION_LITERAL);
        type.setBaseTypeDefinition(schemaIndex.getTypeDefinition(gml.qName("AbstractFeatureType")));

        XSDModelGroup group = f.createXSDModelGroup();
        group.setCompositor(XSDCompositor.SEQUENCE_LITERAL);

        List attributes = featureType.getAttributeDescriptors();
        for (Object o : attributes) {
            AttributeDescriptor attribute = (AttributeDescriptor) o;

            if (toFilter.contains(attribute.getLocalName())) {
                continue;
            }

            XSDElementDeclaration element = f.createXSDElementDeclaration();
            element.setName(attribute.getLocalName());
            element.setNillable(attribute.isNillable());

            // check for geometry
            if (attribute instanceof GeometryDescriptor) {
                Class binding = attribute.getType().getBinding();
                if (Point.class.isAssignableFrom(binding)) {
                    element.setTypeDefinition(schemaIndex.getTypeDefinition(gml.qName("PointPropertyType")));
                } else if (LineString.class.isAssignableFrom(binding)) {
                    // check both GML 3.1 and GML 3.2 types names
                    element.setTypeDefinition(searchType(schemaIndex, "LineStringPropertyType", "CurvePropertyType"));
                } else if (Polygon.class.isAssignableFrom(binding)) {
                    // check both GML 3.1 and GML 3.2 types names
                    element.setTypeDefinition(searchType(schemaIndex, "PolygonPropertyType", "SurfacePropertyType"));
                } else if (MultiPoint.class.isAssignableFrom(binding)) {
                    element.setTypeDefinition(schemaIndex.getTypeDefinition(gml.qName("MultiPointPropertyType")));
                } else if (MultiLineString.class.isAssignableFrom(binding)) {
                    // check both GML 3.1 and GML 3.2 types names
                    element.setTypeDefinition(
                            searchType(schemaIndex, "MultiLineStringPropertyType", "MultiCurvePropertyType"));
                } else if (MultiPolygon.class.isAssignableFrom(binding)) {
                    // check both GML 3.1 and GML 3.2 types names
                    element.setTypeDefinition(
                            searchType(schemaIndex, "MultiPolygonPropertyType", "MultiSurfacePropertyType"));
                } else {
                    element.setTypeDefinition(schemaIndex.getTypeDefinition(gml.qName("GeometryPropertyType")));
                }
            } else {
                // TODO: do a proper mapping
                element.setTypeDefinition(schemaIndex.getTypeDefinition(XS.STRING));
            }

            String attributeNs = attribute.getName().getNamespaceURI();
            if (attributeNs != null && !attributeNs.trim().isEmpty()) {
                element.setTargetNamespace(attributeNs);
            } else {
                element.setTargetNamespace(featureType.getName().getNamespaceURI());
            }

            XSDParticle particle = f.createXSDParticle();
            particle.setMinOccurs(attribute.getMinOccurs());
            particle.setMaxOccurs(attribute.getMaxOccurs());
            particle.setContent(element);
            particle.setElement(dom.createElementNS(XSDConstants.SCHEMA_FOR_SCHEMA_URI_2001, "element"));

            group.getContents().add(particle);
        }

        XSDParticle particle = f.createXSDParticle();
        particle.setContent(group);
        particle.setElement(dom.createElementNS(XSDConstants.SCHEMA_FOR_SCHEMA_URI_2001, "sequence"));
        type.setContent(particle);
        return type;
    }

    /**
     * Return the first XSD type definition found in the schema index for the provided GML types names. NULL is returned
     * if no XSD type definition is found.
     */
    private XSDTypeDefinition searchType(SchemaIndex schemaIndex, String... typesNames) {
        for (String typeName : typesNames) {
            XSDTypeDefinition type = schemaIndex.getTypeDefinition(gml.qName(typeName));
            if (type != null) {
                // we found a matching XSD type
                return type;
            }
        }
        // no matching XSD type found
        LOGGER.fine(String.format(
                "No type definition found for types [%s].",
                Arrays.stream(typesNames).collect(Collectors.joining(", "))));
        return null;
    }

    /**
     * Return true if name is the name of a descriptor of the type or of an ancestor type.
     *
     * @param type type to test
     * @param name name of descriptor
     * @return true if the type or an ancestor has a descriptor of this name
     */
    private boolean isValidDescriptor(ComplexType type, Name name) {
        if (type.getDescriptor(name) != null) {
            return true;
        } else if (type.getSuper() instanceof ComplexType) {
            return isValidDescriptor((ComplexType) type.getSuper(), name);
        } else {
            return false;
        }
    }

    /**
     * Return gml:boundedBy property if wanted.
     *
     * @param feature feature for which bounds might be required
     * @param configuration encoder configuration, used to suppress feature bounds
     * @return the feature bounds, or null if none or unwanted
     */
    private BoundingBox getBoundedBy(Feature feature, Configuration configuration) {
        // check for flag not to include bounds
        if (configuration.hasProperty(GMLConfiguration.NO_FEATURE_BOUNDS)) {
            return null;
        } else {
            BoundingBox bounds = feature.getBounds();
            // do a check for the case where the feature has no geometry properties
            if (bounds.isEmpty()
                    && (feature.getDefaultGeometryProperty() == null
                            || feature.getDefaultGeometryProperty().getValue() == null)) {
                return null;
            } else {
                return bounds;
            }
        }
    }

    public Object GeometryPropertyType_getProperty(Geometry geometry, QName name) {
        return GeometryPropertyType_getProperty(geometry, name, true, false);
    }

    public Object GeometryPropertyType_getProperty(Geometry geometry, QName name, boolean includeAbstractGeometry) {
        return GeometryPropertyType_getProperty(geometry, name, includeAbstractGeometry, false);
    }

    public Object GeometryPropertyType_getProperty(
            Geometry geometry, QName name, boolean includeAbstractGeometry, boolean makeEmpty) {

        if (name.equals(gml.qName("Point"))
                || name.equals(gml.qName("LineString"))
                || name.equals(gml.qName("Polygon"))
                || name.equals(gml.qName("MultiPoint"))
                || name.equals(gml.qName("MultiLineString"))
                || name.equals(gml.qName("MultiPolygon"))
                || name.equals(gml.qName("MultiSurface"))
                || name.equals(gml.qName("AbstractSurface"))
                || name.equals(gml.qName("_Surface"))
                || name.equals(gml.qName("_Curve"))
                || name.equals(gml.qName("AbstractCurve"))
                || name.equals(gml.qName("MultiCurve"))
                || includeAbstractGeometry
                        && (name.equals(gml.qName("_Geometry")) || name.equals(gml.qName("AbstractGeometry")))) {
            // if the geometry is null, return null
            if (isEmpty(geometry) || makeEmpty) {
                return null;
            }

            return geometry;
        }

        if (geometry.getUserData() instanceof Map) {
            @SuppressWarnings("unchecked")
            Map<Name, Object> clientProperties =
                    (Map<Name, Object>) ((Map) geometry.getUserData()).get(Attributes.class);

            Name cname = toTypeName(name);
            if (clientProperties != null && clientProperties.keySet().contains(cname))
                return clientProperties.get(cname);
        }

        if (XLINK.HREF.equals(name)) {
            // only process if geometry is empty and ID exists
            String id = getID(geometry);
            if ((makeEmpty || isEmpty(geometry)) && id != null) {
                return "#" + id;
            }
        }

        return null;
    }

    /** Convert a {@link QName} to a {@link Name}. */
    private static Name toTypeName(QName name) {
        if (XMLConstants.NULL_NS_URI.equals(name.getNamespaceURI())) {
            return new NameImpl(name.getLocalPart());
        } else {
            return new NameImpl(name.getNamespaceURI(), name.getLocalPart());
        }
    }

    public List<Object[]> GeometryPropertyType_getProperties(Geometry geometry) {
        return null;
    }

    public static boolean isEmpty(Geometry geometry) {
        if (geometry.isEmpty()) {
            // check for case of multi geometry, if it has > 0 goemetries
            // we consider this to be not empty
            if (geometry instanceof GeometryCollection) {
                if (geometry.getNumGeometries() != 0) {
                    return false;
                }
            }
            return true;
        }

        return false;
    }

    /**
     * Determines the identifier (gml:id) of the geometry by checking {@link Geometry#getUserData()}.
     *
     * <p>This method returns <code>null</code> when no id can be found.
     */
    public String getID(Geometry g) {
        return getMetadata(g, "gml:id");
    }

    /**
     * Set the identifier (gml:id) of the geometry as a key in the user data map {@link Geometry#getUserData()}
     * (creating it with{@link Geometry#getUserData()} if it does not already exist). If the user data exists and is not
     * a {@link Map}, this method has no effect.
     *
     * @param g the geometry
     * @param id the gml:id to be set
     */
    public void setID(Geometry g, String id) {
        setMetadata(g, "gml:id", id);
    }

    /**
     * Determines the description (gml:description) of the geometry by checking {@link Geometry#getUserData()}.
     *
     * <p>This method returns <code>null</code> when no name can be found.
     */
    public String getName(Geometry g) {
        return getMetadata(g, "gml:name");
    }

    /**
     * Set the name (gml:name) of the geometry as a key in the user data map {@link Geometry#getUserData()} (creating it
     * with{@link Geometry#getUserData()} if it does not already exist). If the user data exists and is not a
     * {@link Map}, this method has no effect.
     *
     * @param g the geometry
     * @param name the gml:name to be set
     */
    public void setName(Geometry g, String name) {
        setMetadata(g, "gml:name", name);
    }

    /**
     * Determines the name (gml:name) of the geometry by checking {@link Geometry#getUserData()}.
     *
     * <p>This method returns <code>null</code> when no description can be found.
     */
    public String getDescription(Geometry g) {
        return getMetadata(g, "gml:description");
    }

    /**
     * Set the description (gml:description) of the geometry as a key in the user data map
     * {@link Geometry#getUserData()} (creating it with{@link Geometry#getUserData()} if it does not already exist). If
     * the user data exists and is not a {@link Map}, this method has no effect.
     *
     * @param g the geometry
     * @param description the gml:description to be set
     */
    public void setDescription(Geometry g, String description) {
        setMetadata(g, "gml:description", description);
    }

    String getMetadata(Geometry g, String metadata) {
        if (g.getUserData() instanceof Map) {
            Map userData = (Map) g.getUserData();
            return (String) userData.get(metadata);
        }
        return null;
    }

    @SuppressWarnings("unchecked")
    void setMetadata(Geometry g, String metadata, String value) {
        if (g.getUserData() == null) {
            g.setUserData(new HashMap<>());
        }
        if (g.getUserData() instanceof Map) {
            ((Map) g.getUserData()).put(metadata, value);
        }
    }

    /** Checks if a feature is a joined one */
    public static boolean isJoinedFeature(Object obj) {
        if (!(obj instanceof SimpleFeature)) {
            return false;
        }

        SimpleFeature feature = (SimpleFeature) obj;
        for (Object att : feature.getAttributes()) {
            if (att != null && att instanceof SimpleFeature) {
                return true;
            }
        }

        return false;
    }

    /** Splits a joined feature into its components */
    public static SimpleFeature[] splitJoinedFeature(Object obj) {
        SimpleFeature feature = (SimpleFeature) obj;
        List<SimpleFeature> features = new ArrayList<>();
        features.add(feature);
        for (int i = 0; i < feature.getAttributeCount(); i++) {
            Object att = feature.getAttribute(i);
            if (att != null && att instanceof SimpleFeature) {
                features.add((SimpleFeature) att);

                // TODO: come up with a better approcach user, use user data or something to mark
                // the attribute as encoded
                feature.setAttribute(i, null);
            }
        }

        return features.toArray(new SimpleFeature[features.size()]);
    }
}
