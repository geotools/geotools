/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2002-2016, Open Source Geospatial Foundation (OSGeo)
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

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.logging.Logger;
import javax.xml.namespace.QName;
import org.eclipse.xsd.XSDElementDeclaration;
import org.eclipse.xsd.XSDParticle;
import org.geotools.api.feature.simple.SimpleFeature;
import org.geotools.api.feature.simple.SimpleFeatureType;
import org.geotools.api.feature.type.AttributeDescriptor;
import org.geotools.api.feature.type.AttributeType;
import org.geotools.api.feature.type.FeatureType;
import org.geotools.api.referencing.FactoryException;
import org.geotools.api.referencing.NoSuchAuthorityCodeException;
import org.geotools.api.referencing.crs.CoordinateReferenceSystem;
import org.geotools.feature.NameImpl;
import org.geotools.feature.simple.SimpleFeatureBuilder;
import org.geotools.feature.simple.SimpleFeatureTypeBuilder;
import org.geotools.geometry.jts.JTS;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.gml2.FeatureTypeCache;
import org.geotools.gml2.GML;
import org.geotools.referencing.CRS;
import org.geotools.util.Converters;
import org.geotools.util.logging.Logging;
import org.geotools.xs.bindings.XSAnyTypeBinding;
import org.geotools.xsd.Binding;
import org.geotools.xsd.BindingWalkerFactory;
import org.geotools.xsd.ElementInstance;
import org.geotools.xsd.Node;
import org.geotools.xsd.Schemas;
import org.geotools.xsd.impl.BindingWalker;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.GeometryCollection;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.LineString;
import org.locationtech.jts.geom.MultiLineString;
import org.locationtech.jts.geom.MultiPoint;
import org.locationtech.jts.geom.MultiPolygon;
import org.locationtech.jts.geom.Point;
import org.locationtech.jts.geom.Polygon;

/**
 * Utility methods used by gml2 bindings when parsing.
 *
 * @author Justin Deoliveira, The Open Planning Project, jdeolive@openplans.org
 */
public class GML2ParsingUtils {
    /** logging instance */
    static Logger LOGGER = Logging.getLogger(GML2ParsingUtils.class);

    /**
     * Metadata key used to indicate if a feature type has been parsed from a XML schema, or reflected out of a sample
     * feature
     */
    public static String PARSED_FROM_SCHEMA_KEY;

    /**
     * Utility method to implement Binding.parse for a binding which parses into A feature.
     *
     * @param instance The instance being parsed.
     * @param node The parse tree.
     * @param value The value from the last binding in the chain.
     * @param ftCache The feature type cache.
     * @param bwFactory Binding walker factory.
     * @return A feature type.
     */
    public static SimpleFeature parseFeature(
            ElementInstance instance, Node node, Object value, FeatureTypeCache ftCache, BindingWalkerFactory bwFactory)
            throws Exception {
        // get the definition of the element
        XSDElementDeclaration decl = instance.getElementDeclaration();

        // special case, if the declaration is abstract it is probably "_Feautre"
        // which means we are parsing an element which could not be found in the
        // schema, so instead of using the element declaration to build the
        // type, just use the node given to us
        SimpleFeatureType sfType = null;
        FeatureType fType = null;

        if (!decl.isAbstract()) {
            // first look in cache
            fType = ftCache.get(new NameImpl(decl.getTargetNamespace(), decl.getName()));

            if (fType == null || fType instanceof SimpleFeatureType) {
                sfType = (SimpleFeatureType) fType;
            } else {
                // TODO: support parsing of non-simple GML features
                throw new UnsupportedOperationException("Parsing of non-simple GML features not yet supported.");
            }

            if (sfType == null) {
                // let's use the CRS from the node (only if it's available) on the feature type
                CoordinateReferenceSystem crs = null;
                if (node.hasChild("boundedBy") && node.getChild("boundedBy").hasChild("Box")) {
                    crs = crs(node.getChild("boundedBy").getChild("Box"));
                } else if (node.hasChild("boundedBy")
                        && node.getChild("boundedBy").hasChild("Envelope")) {
                    crs = crs(node.getChild("boundedBy").getChild("Envelope"));
                }

                // build from element declaration
                sfType = GML2ParsingUtils.featureType(decl, bwFactory, crs);
                ftCache.put(sfType);
            }
        } else {
            // first look in cache
            fType = ftCache.get(new NameImpl(
                    node.getComponent().getNamespace(), node.getComponent().getName()));

            if (fType == null || fType instanceof SimpleFeatureType) {
                sfType = (SimpleFeatureType) fType;
            } else {
                // TODO: support parsing of non-simple GML features
                throw new UnsupportedOperationException("Parsing of non-simple GML features not yet supported.");
            }

            if (sfType == null) {
                // build from node
                sfType = GML2ParsingUtils.featureType(node);
                ftCache.put(sfType);
            }
        }

        // fid
        String fid = (String) node.getAttributeValue("fid");

        if (fid == null) {
            // look for id
            fid = (String) node.getAttributeValue("id");
        }

        // create feature
        return GML2ParsingUtils.feature(sfType, fid, node);
    }

    /**
     * Turns a parse node instance into a geotools feature type.
     *
     * <p>For each child element and attribute of the node a geotools attribute type is created. AttributeType#getName()
     * is derived from the name of the child element / attribute. Attribute#getType() is derived from the class of the
     * value of the child element / attribute.
     *
     * <p>Attribute types for the mandatory properties of any gml feature type (description,name,boundedBy) are also
     * created.
     *
     * @param node The parse node / tree for the feature.
     * @return A geotools feature type
     */
    public static SimpleFeatureType featureType(Node node) throws Exception {
        SimpleFeatureTypeBuilder ftBuilder = new SimpleFeatureTypeBuilder();
        ftBuilder.setName(node.getComponent().getName());
        ftBuilder.setNamespaceURI(node.getComponent().getNamespace());
        ftBuilder.setCRS(null); // JD: set explicitly to null to avoid warning

        CoordinateReferenceSystem crs = null;

        // mandatory gml attributes
        if (!node.hasChild("description")) {
            ftBuilder.add("description", String.class);
        }

        if (!node.hasChild("name")) {
            ftBuilder.add("name", String.class);
        }

        if (!node.hasChild("boundedBy")) {
            ftBuilder.add("boundedBy", ReferencedEnvelope.class);
        } else {
            if (node.getChild("boundedBy").hasChild("Box")) {
                crs = crs(node.getChild("boundedBy").getChild("Box"));
            } else if (node.getChild("boundedBy").hasChild("Envelope")) {
                crs = crs(node.getChild("boundedBy").getChild("Envelope"));
            }
        }

        // application schema defined attributes
        for (Node child : node.getChildren()) {
            String name = child.getComponent().getName();
            Object value = child.getValue();

            // if the next property is of type geometry, let's set its CRS
            if (value != null && Geometry.class.isAssignableFrom(value.getClass()) && crs != null) {
                ftBuilder.crs(crs);
            }

            ftBuilder.add(name, value != null ? value.getClass() : Object.class);
        }
        ftBuilder.userData(PARSED_FROM_SCHEMA_KEY, false);

        return ftBuilder.buildFeatureType();
    }

    /**
     * Turns a xml type definition into a geotools feature type.
     *
     * @param element The element declaration.
     * @param bwFactory The binding walker factory.
     * @return The corresponding geotools feature type.
     */
    public static SimpleFeatureType featureType(XSDElementDeclaration element, BindingWalkerFactory bwFactory)
            throws Exception {
        return featureType(element, bwFactory, null);
    }

    /**
     * Turns a xml type definition into a geotools feature type.
     *
     * @param element The element declaration.
     * @param bwFactory The binding walker factory.
     * @param crs The coordinate reference system to use on this feature type.
     * @return The corresponding geotools feature type.
     */
    public static SimpleFeatureType featureType(
            XSDElementDeclaration element, BindingWalkerFactory bwFactory, CoordinateReferenceSystem crs)
            throws Exception {
        SimpleFeatureTypeBuilder ftBuilder = new SimpleFeatureTypeBuilder();
        ftBuilder.setName(element.getName());
        ftBuilder.setNamespaceURI(element.getTargetNamespace());

        // build the feature type by walking through the elements of the
        // actual xml schema type
        List children = Schemas.getChildElementParticles(element.getType(), true);

        for (Object child : children) {
            XSDParticle particle = (XSDParticle) child;
            XSDElementDeclaration property = (XSDElementDeclaration) particle.getContent();

            if (property.isElementDeclarationReference()) {
                property = property.getResolvedElementDeclaration();
            }

            final List<Binding> bindings = new ArrayList<>();
            BindingWalker.Visitor visitor = binding -> bindings.add(binding);

            bwFactory.walk(property, visitor);

            if (bindings.isEmpty()) {
                // could not find a binding, use the defaults
                LOGGER.fine("Could not find binding for " + property.getQName() + ", using XSAnyTypeBinding.");
                bindings.add(new XSAnyTypeBinding());
            }

            // get the last binding in the chain to execute
            Binding last = bindings.get(bindings.size() - 1);
            Class theClass = last.getType();

            if (theClass == null) {
                throw new RuntimeException("binding declares null type: " + last.getTarget());
            }

            // get the attribute properties
            int min = particle.getMinOccurs();
            int max = particle.getMaxOccurs();

            // check for uninitialized values
            if (min == -1) {
                min = 0;
            }

            if (max == -1) {
                max = 1;
            }

            // if the next property is of type geometry, let's set its CRS
            if (Geometry.class.isAssignableFrom(theClass) && crs != null) {
                ftBuilder.crs(crs);
            }

            // create the type
            ftBuilder.minOccurs(min).maxOccurs(max).add(property.getName(), theClass);

            // set the default geometry explicitly. Note we're comparing the GML namespace
            // with String.startsWith to catch up on the GML 3.2 namespace too, which is hacky.
            final String propNamespace = property.getTargetNamespace();
            if (Geometry.class.isAssignableFrom(theClass)
                    && (propNamespace == null || !propNamespace.startsWith(GML.NAMESPACE))) {
                // only set if non-gml, we do this because of "gml:location",
                // we dont want that to be the default if the user has another
                // geometry attribute
                if (ftBuilder.getDefaultGeometry() == null) {
                    ftBuilder.setDefaultGeometry(property.getName());
                }
            }
        }
        ftBuilder.userData(PARSED_FROM_SCHEMA_KEY, true);

        return ftBuilder.buildFeatureType();
    }

    public static SimpleFeature feature(SimpleFeatureType fType, String fid, Node node) throws Exception {
        SimpleFeatureBuilder b = new SimpleFeatureBuilder(fType);

        int attributeCount = fType.getAttributeCount();
        for (int i = 0; i < attributeCount; i++) {
            AttributeDescriptor att = fType.getDescriptor(i);
            AttributeType attType = att.getType();
            Object attValue = node.getChildValue(att.getLocalName());

            if (attValue != null && !attType.getBinding().isAssignableFrom(attValue.getClass())) {
                // type mismatch, to try convert
                Object converted = Converters.convert(attValue, attType.getBinding());

                if (converted != null) {
                    attValue = converted;
                }
            }

            b.add(attValue);
        }

        // create the feature
        return b.buildFeature(fid);
    }

    public static CoordinateReferenceSystem crs(Node node) {
        if (node.getAttribute("srsName") != null) {
            URI srs = null;
            Object raw = node.getAttributeValue("srsName");

            if (raw instanceof URI) {
                srs = (URI) raw;
            } else if (raw instanceof String) {
                // try to parse into a uri
                try {
                    srs = new URI((String) raw);
                } catch (URISyntaxException e) {
                    // failed, continue on
                }
            }

            if (srs != null) {
                // TODO: JD, this is a hack until GEOT-1136 has been resolved
                if ("http".equals(srs.getScheme())
                        && "www.opengis.net".equals(srs.getAuthority())
                        && "/gml/srs/epsg.xml".equals(srs.getPath())
                        && srs.getFragment() != null) {
                    try {
                        return CRS.decode("EPSG:" + srs.getFragment());
                    } catch (Exception e) {
                        // failed, try as straight up uri
                        try {
                            return CRS.decode(srs.toString());
                        } catch (Exception e1) {
                            // failed again, do nothing ,should fail below as well
                        }
                    }
                }
            }

            try {
                return CRS.decode(raw.toString());
            } catch (NoSuchAuthorityCodeException e) {
                // HACK HACK HACK!: remove when
                // http://jira.codehaus.org/browse/GEOT-1659 is fixed
                final String crs = raw.toString();
                if (crs.toUpperCase().startsWith("URN")) {
                    String code = crs.substring(crs.lastIndexOf(":") + 1);
                    try {
                        return CRS.decode("EPSG:" + code);
                    } catch (Exception e1) {
                        throw new RuntimeException("Could not create crs: " + srs, e);
                    }
                }
            } catch (FactoryException e) {
                throw new RuntimeException("Could not create crs: " + srs, e);
            }
        }

        return null;
    }

    /** Wraps the elements of a geometry collection in a normal collection. */
    public static Collection<Geometry> asCollection(GeometryCollection gc) {
        List<Geometry> members = new ArrayList<>();

        for (int i = 0; i < gc.getNumGeometries(); i++) {
            members.add(gc.getGeometryN(i));
        }

        return members;
    }

    static GeometryCollection GeometryCollectionType_parse(Node node, Class clazz, GeometryFactory gFactory) {
        // round up children that are geometries, since this type is often
        // extended by multi geometries, dont reference members by element name
        List<Geometry> geoms = new ArrayList<>();

        for (Node cnode : node.getChildren()) {
            if (cnode.getValue() instanceof Geometry) {
                geoms.add((Geometry) cnode.getValue());
            }
        }

        GeometryCollection gc = null;

        if (MultiPoint.class.isAssignableFrom(clazz)) {
            gc = gFactory.createMultiPoint(geoms.toArray(new Point[geoms.size()]));
        } else if (MultiLineString.class.isAssignableFrom(clazz)) {
            gc = gFactory.createMultiLineString(geoms.toArray(new LineString[geoms.size()]));
        } else if (MultiPolygon.class.isAssignableFrom(clazz)) {
            gc = gFactory.createMultiPolygon(geoms.toArray(new Polygon[geoms.size()]));
        } else {
            gc = gFactory.createGeometryCollection(geoms.toArray(new Geometry[geoms.size()]));
        }

        // set an srs if there is one
        CoordinateReferenceSystem crs = crs(node);

        if (crs != null) {
            gc.setUserData(crs);

            // since we're setting the CRS on the UserData object, might as well set the SRID for
            // the geom
            // collection
            try {
                gc.setSRID(CRS.lookupEpsgCode(crs, true));
            } catch (FactoryException e) {
                // as long as the provided CRS is valid, this block will be unreachable
            }
        }

        return gc;
    }

    static Object GeometryCollectionType_getProperty(Object object, QName name) {
        if ("srsName".equals(name.getLocalPart())) {
            CoordinateReferenceSystem crs = JTS.getCRS((GeometryCollection) object);
            if (crs != null) {
                return GML2EncodingUtils.toURI(crs, true);
            }
        }
        return null;
    }
}
