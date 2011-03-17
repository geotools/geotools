package org.geotools.gml2.bindings;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Logger;

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
import org.geotools.feature.NameImpl;
import org.geotools.gml2.GML;
import org.geotools.gml2.GMLConfiguration;
import org.geotools.util.logging.Logging;
import org.geotools.xlink.XLINK;
import org.geotools.xml.Configuration;
import org.geotools.xml.Encoder;
import org.geotools.xml.SchemaIndex;
import org.geotools.xml.Schemas;
import org.geotools.xml.XSD;
import org.geotools.xs.XS;
import org.opengis.feature.ComplexAttribute;
import org.opengis.feature.Feature;
import org.opengis.feature.GeometryAttribute;
import org.opengis.feature.Property;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.feature.type.AttributeDescriptor;
import org.opengis.feature.type.ComplexType;
import org.opengis.feature.type.FeatureType;
import org.opengis.feature.type.GeometryDescriptor;
import org.opengis.feature.type.Name;
import org.opengis.geometry.BoundingBox;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.w3c.dom.Document;
import org.xml.sax.Attributes;

import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryCollection;
import com.vividsolutions.jts.geom.LineString;
import com.vividsolutions.jts.geom.MultiLineString;
import com.vividsolutions.jts.geom.MultiPoint;
import com.vividsolutions.jts.geom.MultiPolygon;
import com.vividsolutions.jts.geom.Point;
import com.vividsolutions.jts.geom.Polygon;

public class GMLEncodingUtils {

    /** logging instance */
    static Logger LOGGER = Logging.getLogger( "org.geotools.gml");
    
    XSD gml;
    
    public GMLEncodingUtils(XSD gml) {
        this.gml = gml;
    }
    
    public List AbstractFeatureType_getProperties(Object object,
            XSDElementDeclaration element, SchemaIndex schemaIndex, Set<String> toFilter,
            Configuration configuration) {
        
        Feature feature = (Feature) object;
        
        //check if this was a resolved feature, if so dont return anything
        // TODO: this is just a hack for our lame xlink implementation
        if (feature.getUserData().get("xlink:id") != null) {
            return Collections.EMPTY_LIST;
        }

        FeatureType featureType = feature.getType();

        String namespace = featureType.getName().getNamespaceURI();

        if (namespace == null) {
            namespace = element.getTargetNamespace();
        }

        String typeName = featureType.getName().getLocalPart();
        QName qualifiedTypeName = new QName(namespace, typeName);

        //find the type in the schema
        XSDTypeDefinition type = schemaIndex.getTypeDefinition(qualifiedTypeName);

        if (type == null) {
            //type not found, do a check for an element, and use its type
            XSDElementDeclaration e = schemaIndex.getElementDeclaration(qualifiedTypeName);

            if (e != null) {
                type = e.getTypeDefinition();
            }
        }

        if (type == null) {
            if (featureType instanceof SimpleFeatureType) {
                // could not find the feature type in the schema, create a mock one
                LOGGER.warning("Could find type for " + typeName
                        + " in the schema, generating type from feature.");
                type = createXmlTypeFromFeatureType((SimpleFeatureType) featureType, schemaIndex,
                        toFilter);
            } else {
                // look for an element declaration smuggled in the UserData map.
                XSDElementDeclaration e = (XSDElementDeclaration) feature.getDescriptor()
                        .getUserData().get(XSDElementDeclaration.class);
                if (e != null) {
                    type = e.getTypeDefinition();
                } else {
                    throw new RuntimeException("Could not find type for " + qualifiedTypeName
                            + " in schema");
                }
            }
        }

        List particles = Schemas.getChildElementParticles(type, true);
        List properties = new ArrayList();

    O:  for (int i = 0; i < particles.size(); i++) {
            XSDParticle particle = (XSDParticle) particles.get(i);
            XSDElementDeclaration attribute = (XSDElementDeclaration) particle.getContent();

            if (attribute.isElementDeclarationReference()) {
                attribute = attribute.getResolvedElementDeclaration();
            }
            
            if (gml.qName("boundedBy")
                    .equals(new QName(attribute.getTargetNamespace(), attribute.getName()))) {
                BoundingBox bounds = getBoundedBy(feature, configuration);
                if (bounds != null) {
                    properties.add(new Object[] { particle, bounds });
                }
            } else if (featureType instanceof SimpleFeatureType) {
                // first simple feature hack, if the schema "overrides" gml attributes like
                // name and description, ignore the gml version
                boolean skip = false;
                if (gml.getNamespaceURI().equals(attribute.getTargetNamespace())) {
                    for (int j = i+1; j < particles.size(); j++) {
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
                    Object obj = ((Geometry) attributeValue).getUserData();
                    Map<Object, Object> userData = new HashMap<Object, Object>();
                    if (obj != null && obj instanceof Map) {
                        userData.putAll((Map) obj);
                    }
                    userData.put(CoordinateReferenceSystem.class, featureType
                            .getCoordinateReferenceSystem());
                    ((Geometry) attributeValue).setUserData(userData);
                }
                properties.add(new Object[] { particle, attributeValue });
            } else {
                // namespaces matter for non-simple feature types
                Name propertyName = new NameImpl(attribute.getTargetNamespace(), attribute
                        .getName());
                // make sure the feature type has an element
                if (!isValidDescriptor(featureType, propertyName)) {
                    continue;
                }
                // get the value (might be multiple)
                for (Property property : feature.getProperties(propertyName)) {
                    Object value;
                    if (property instanceof ComplexAttribute) {
                        // do not unpack complex attributes as these may have their own bindings, which
                        // will be applied by the encoder
                        value = property;
                    } else if (property instanceof GeometryAttribute) {
                        value = property.getValue();
                        if (value != null) {
                            // ensure CRS is passed to the Geometry object
                            Geometry geometry = (Geometry) value;
                            CoordinateReferenceSystem crs = ((GeometryAttribute) property)
                                    .getDescriptor().getCoordinateReferenceSystem();
                            Map<Object, Object> userData = new HashMap<Object, Object>();
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
                    properties.add(new Object[] { particle, value });
                }
            }
        }

        return properties;
    }
    
    public XSDTypeDefinition createXmlTypeFromFeatureType(SimpleFeatureType featureType, SchemaIndex schemaIndex, Set<String> toFilter ) { 
        XSDFactory f = XSDFactory.eINSTANCE;
        Document dom;
        try {
            dom = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();
        } catch (ParserConfigurationException e) {
            throw new RuntimeException( e );
        }
        
        XSDComplexTypeDefinition type = f.createXSDComplexTypeDefinition();
        type.setTargetNamespace( featureType.getName().getNamespaceURI() );
        type.setName( featureType.getTypeName() + "Type" );
        type.setDerivationMethod(XSDDerivationMethod.EXTENSION_LITERAL);
        type.setBaseTypeDefinition(schemaIndex.getTypeDefinition( gml.qName("AbstractFeatureType") ) );
                
        XSDModelGroup group = f.createXSDModelGroup();
        group.setCompositor(XSDCompositor.SEQUENCE_LITERAL);

        List attributes = featureType.getAttributeDescriptors();
        for (int i = 0; i < attributes.size(); i++) {
            AttributeDescriptor attribute = (AttributeDescriptor) attributes.get(i);

            if ( toFilter.contains( attribute.getLocalName() ) ) {
                continue;
            }
           
            XSDElementDeclaration element = f.createXSDElementDeclaration();
            element.setName(attribute.getLocalName());
            element.setNillable(attribute.isNillable());

            //check for geometry
            if ( attribute instanceof GeometryDescriptor ) {
                Class binding = attribute.getType().getBinding();
                if ( Point.class.isAssignableFrom( binding ) ) {
                    element.setTypeDefinition( schemaIndex.getTypeDefinition(gml.qName("PointPropertyType")));
                }
                else if ( LineString.class.isAssignableFrom( binding ) ) {
                    element.setTypeDefinition( schemaIndex.getTypeDefinition(gml.qName("LineStringPropertyType")));
                }
                else if ( Polygon.class.isAssignableFrom( binding) ) {
                    element.setTypeDefinition( schemaIndex.getTypeDefinition(gml.qName("PolygonPropertyType")));
                }
                else if ( MultiPoint.class.isAssignableFrom( binding ) ) {
                    element.setTypeDefinition( schemaIndex.getTypeDefinition(gml.qName("MultiPointPropertyType")));
                }
                else if ( MultiLineString.class.isAssignableFrom( binding ) ) {
                    element.setTypeDefinition( schemaIndex.getTypeDefinition(gml.qName("MultiLineStringPropertyType")));
                }
                else if ( MultiPolygon.class.isAssignableFrom( binding) ) {
                    element.setTypeDefinition( schemaIndex.getTypeDefinition(gml.qName("MultiPolygonPropertyType")));
                }
                else {
                    element.setTypeDefinition( schemaIndex.getTypeDefinition(gml.qName("GeometryPropertyType")));
                }
            }
            else {
                //TODO: do a proper mapping
                element.setTypeDefinition(schemaIndex.getTypeDefinition(XS.STRING));
            }
            

            XSDParticle particle = f.createXSDParticle();
            particle.setMinOccurs(attribute.getMinOccurs());
            particle.setMaxOccurs(attribute.getMaxOccurs());
            particle.setContent(element);
            particle.setElement( dom.createElementNS( XSDConstants.SCHEMA_FOR_SCHEMA_URI_2001, "element" ) );
            
            group.getContents().add(particle);
        }

        XSDParticle particle = f.createXSDParticle();
        particle.setContent(group);
        particle.setElement( dom.createElementNS( XSDConstants.SCHEMA_FOR_SCHEMA_URI_2001, "sequence") );
        type.setContent(particle);
        return type;
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
     * @param feature
     *            feature for which bounds might be required
     * @param configuration
     *            encoder configuration, used to suppress feature bounds
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
                    && (feature.getDefaultGeometryProperty() == null || feature
                            .getDefaultGeometryProperty().getValue() == null)) {
                return null;
            } else {
                return bounds;
            }
        }
    }


    public Object GeometryPropertyType_getProperty(Geometry geometry, QName name) {
        return GeometryPropertyType_getProperty(geometry, name, true, false);
    }

    public Object GeometryPropertyType_getProperty(Geometry geometry, QName name,
            boolean includeAbstractGeometry) {
        return GeometryPropertyType_getProperty(geometry, name, includeAbstractGeometry, false);

    }

    public Object GeometryPropertyType_getProperty(Geometry geometry, QName name,
            boolean includeAbstractGeometry, boolean makeEmpty) {

        if (name.equals(gml.qName("Point")) || name.equals(gml.qName("LineString"))
                || name.equals(gml.qName("Polygon")) || name.equals(gml.qName("MultiPoint"))
                || name.equals(gml.qName("MultiLineString"))
                || name.equals(gml.qName("MultiPolygon")) || name.equals(gml.qName("MultiSurface"))
                || name.equals(gml.qName("AbstractSurface")) || name.equals(gml.qName("_Surface"))
                || name.equals(gml.qName("_Curve")) || name.equals(gml.qName("AbstractCurve"))
                || name.equals(gml.qName("MultiCurve"))
                || (includeAbstractGeometry && name.equals(gml.qName("_Geometry")))) {
            // if the geometry is null, return null
            if (isEmpty(geometry) || makeEmpty) {
                return null;
            }

            return geometry;
        }

        if (geometry.getUserData() instanceof Map) {
            Map<Name, Object> clientProperties = (Map<Name, Object>) ((Map) geometry.getUserData())
                    .get(Attributes.class);

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

    /**
     * Convert a {@link QName} to a {@link Name}.
     * 
     * @param name
     * @return
     */
    private static Name toTypeName(QName name) {
        if (XMLConstants.NULL_NS_URI.equals(name.getNamespaceURI())) {
            return new NameImpl(name.getLocalPart());
        } else {
            return new NameImpl(name.getNamespaceURI(), name.getLocalPart());
        }
    }
    
    public List GeometryPropertyType_getProperties(Geometry geometry) {

        String id = getID( geometry );
        
        if ( !isEmpty(geometry) && id != null ) {
            // return a comment which is hte xlink href
            return Collections.singletonList(new Object[] { Encoder.COMMENT, "#" +id });            
        }
        
        return null;
    }
    
    private boolean isEmpty( Geometry geometry ) {
        if ( geometry.isEmpty() ) {
            //check for case of multi geometry, if it has > 0 goemetries 
            // we consider this to be not empty
            if ( geometry instanceof GeometryCollection ) {
                if ( ((GeometryCollection) geometry).getNumGeometries() != 0 ) {
                    return false;
                }
            }
            return true;
        }
        
        return false;
    }
    
    /**
     * Determines the identifier (gml:id) of the geometry by checking
     * {@link Geometry#getUserData()}.
     * <p>
     * This method returns <code>null</code> when no id can be found.
     * </p>
     */
    public String getID(Geometry g) {
        return getMetadata( g, "gml:id" );
    }
    
    /**
     * Determines the description (gml:description) of the geometry by checking
     * {@link Geometry#getUserData()}.
     * <p>
     * This method returns <code>null</code> when no name can be found.
     * </p>
     */
    public String getName(Geometry g) {
        return getMetadata( g, "gml:name" );
    }
    
    /**
     * Determines the name (gml:name) of the geometry by checking
     * {@link Geometry#getUserData()}.
     * <p>
     * This method returns <code>null</code> when no description can be found.
     * </p>
     */
    public String getDescription(Geometry g) {
        return getMetadata( g, "gml:description" );
    }
    
    String getMetadata(Geometry g, String metadata) {
        if (g.getUserData() instanceof Map) {
            Map userData = (Map) g.getUserData();

            return (String) userData.get(metadata);
        }

        return null;
    }
    
}
