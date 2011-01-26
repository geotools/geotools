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
package org.geotools.gml2.bindings;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Logger;

import javax.xml.namespace.QName;

import org.eclipse.xsd.XSDElementDeclaration;
import org.eclipse.xsd.XSDParticle;
import org.geotools.feature.NameImpl;
import org.geotools.feature.simple.SimpleFeatureBuilder;
import org.geotools.feature.simple.SimpleFeatureTypeBuilder;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.gml2.FeatureTypeCache;
import org.geotools.gml2.GML;
import org.geotools.referencing.CRS;
import org.geotools.util.Converters;
import org.geotools.util.logging.Logging;
import org.geotools.xml.Binding;
import org.geotools.xml.BindingWalkerFactory;
import org.geotools.xml.ElementInstance;
import org.geotools.xml.Node;
import org.geotools.xml.Schemas;
import org.geotools.xml.impl.BindingWalker;
import org.geotools.xs.bindings.XSAnyTypeBinding;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.feature.type.AttributeDescriptor;
import org.opengis.feature.type.AttributeType;
import org.opengis.feature.type.FeatureType;
import org.opengis.referencing.FactoryException;
import org.opengis.referencing.NoSuchAuthorityCodeException;
import org.opengis.referencing.crs.CoordinateReferenceSystem;

import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryCollection;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.LineString;
import com.vividsolutions.jts.geom.MultiLineString;
import com.vividsolutions.jts.geom.MultiPoint;
import com.vividsolutions.jts.geom.MultiPolygon;
import com.vividsolutions.jts.geom.Point;
import com.vividsolutions.jts.geom.Polygon;


/**
 * Utility methods used by gml2 bindings when parsing.
 *
 * @author Justin Deoliveira, The Open Planning Project, jdeolive@openplans.org
 *
 *
 * @source $URL$
 */
public class GML2ParsingUtils {
    /**
     * logging instance
     */
    static Logger LOGGER = Logging.getLogger( "org.geotools.gml" );
    
    /**
     * Utility method to implement Binding.parse for a binding which parses
     * into A feature.
     *
     * @param instance The instance being parsed.
     * @param node The parse tree.
     * @param value The value from the last binding in the chain.
     * @param ftCache The feature type cache.
     * @param bwFactory Binding walker factory.
     *
     * @return A feature type.
     */
    public static SimpleFeature parseFeature(ElementInstance instance, Node node, Object value,
        FeatureTypeCache ftCache, BindingWalkerFactory bwFactory)
        throws Exception {
        //get the definition of the element
        XSDElementDeclaration decl = instance.getElementDeclaration();

        //special case, if the declaration is abstract it is probably "_Feautre" 
        // which means we are parsing an elemetn which could not be found in the 
        // schema, so instaed of using the element declaration to build the 
        // type, just use the node given to us
        SimpleFeatureType sfType = null;
        FeatureType fType = null;
        
        if (!decl.isAbstract()) {
            //first look in cache
            fType = ftCache.get(new NameImpl(decl.getTargetNamespace(), decl.getName()));

            if (fType == null || fType instanceof SimpleFeatureType) {
                sfType = (SimpleFeatureType) fType;
            } else {
                // TODO: support parsing of non-simple GML features
                throw new UnsupportedOperationException("Parsing of non-simple GML features not yet supported.");
            }

            if (sfType == null) {
                //build from element declaration
                sfType = GML2ParsingUtils.featureType(decl, bwFactory);
                ftCache.put(sfType);
            }
        } else {
            // first look in cache
            fType = ftCache.get(new NameImpl(node.getComponent().getNamespace(), node
                    .getComponent().getName()));

            if (fType == null || fType instanceof SimpleFeatureType) {
                sfType = (SimpleFeatureType) fType;
            } else {
                // TODO: support parsing of non-simple GML features
                throw new UnsupportedOperationException("Parsing of non-simple GML features not yet supported.");
            }

            if (sfType == null) {
                //build from node
                sfType = GML2ParsingUtils.featureType(node);
                ftCache.put(sfType);
            }
        }

        //fid
        String fid = (String) node.getAttributeValue("fid");

        if (fid == null) {
            //look for id
            fid = (String) node.getAttributeValue("id");
        }

        //create feature
        return GML2ParsingUtils.feature(sfType, fid, node);
    }

    /**
     * Turns a parse node instance into a geotools feature type.
     * <p>
     * For each child element and attribute of the node a geotools attribute
     * type is created. AttributeType#getName() is derived from the name of
     * the child element / attribute. Attribute#getType() is derived from the
     * class of the value of the child element / attribute.
     * </p>
     * <p>
     * Attribute types for the mandatory properties of any gml feature type
     * (description,name,boundedBy) are also created.
     * </p>
     * @param node The parse node / tree for the feature.
     *
     * @return A geotools feature type
     */
    public static SimpleFeatureType featureType(Node node)
        throws Exception {
        SimpleFeatureTypeBuilder ftBuilder = new SimpleFeatureTypeBuilder();
        ftBuilder.setName(node.getComponent().getName());
        ftBuilder.setNamespaceURI(node.getComponent().getNamespace());

        //mandatory gml attributes
        if (!node.hasChild("description")) {
            ftBuilder.add("description", String.class);
        }

        if (!node.hasChild("name")) {
            ftBuilder.add("name", String.class);
        }

        if (!node.hasChild("boundedBy")) {
            ftBuilder.add("boundedBy", ReferencedEnvelope.class);
        }

        //application schema defined attributes
        for (Iterator c = node.getChildren().iterator(); c.hasNext();) {
            Node child = (Node) c.next();
            String name = child.getComponent().getName();
            Object valu = child.getValue();

            ftBuilder.add(name, (valu != null) ? valu.getClass() : Object.class);
        }

        return ftBuilder.buildFeatureType();
    }

    /**
     * Turns a xml type definition into a geotools feature type.
     *
     * @param type
     *            The xml schema tupe.
     *
     * @return The corresponding geotools feature type.
     */
    public static SimpleFeatureType featureType(XSDElementDeclaration element,
        BindingWalkerFactory bwFactory) throws Exception {
        SimpleFeatureTypeBuilder ftBuilder = new SimpleFeatureTypeBuilder();
        ftBuilder.setName(element.getName());
        ftBuilder.setNamespaceURI(element.getTargetNamespace());

        // build the feaure type by walking through the elements of the
        // actual xml schema type
        List children = Schemas.getChildElementParticles(element.getType(), true);

        for (Iterator itr = children.iterator(); itr.hasNext();) {
            XSDParticle particle = (XSDParticle) itr.next();
            XSDElementDeclaration property = (XSDElementDeclaration) particle.getContent();

            if (property.isElementDeclarationReference()) {
                property = property.getResolvedElementDeclaration();
            }

            final ArrayList bindings = new ArrayList();
            BindingWalker.Visitor visitor = new BindingWalker.Visitor() {
                    public void visit(Binding binding) {
                        bindings.add(binding);
                    }
                };

            bwFactory.walk(property, visitor);

            if (bindings.isEmpty()) {
                // could not find a binding, use the defaults
                LOGGER.warning( "Could not find binding for " + property.getQName() + ", using XSAnyTypeBinding." );
                bindings.add( new XSAnyTypeBinding() );
            }

            // get hte last binding in the chain to execute
            Binding last = ((Binding) bindings.get(bindings.size() - 1));
            Class theClass = last.getType();

            if (theClass == null) {
                throw new RuntimeException("binding declares null type: " + last.getTarget());
            }

            // get the attribute properties
            int min = particle.getMinOccurs();
            int max = particle.getMaxOccurs();

            //check for uninitialized values
            if (min == -1) {
                min = 0;
            }

            if (max == -1) {
                max = 1;
            }

            // create the type
            ftBuilder.minOccurs(min).maxOccurs(max).add(property.getName(), theClass);

            //set the default geometry explicitly. Note we're comparing the GML namespace
            //with String.startsWith to catch up on the GML 3.2 namespace too, which is hacky.
            final String propNamespace = property.getTargetNamespace();
            if (Geometry.class.isAssignableFrom(theClass)
                    && (propNamespace == null || !propNamespace.startsWith(GML.NAMESPACE))) {
                //only set if non-gml, we do this because of "gml:location", 
                // we dont want that to be the default if the user has another
                // geometry attribute
                if (ftBuilder.getDefaultGeometry() == null) {
                    ftBuilder.setDefaultGeometry(property.getName());
                }
            }
        }

        return ftBuilder.buildFeatureType();
    }

    public static SimpleFeature feature(SimpleFeatureType fType, String fid, Node node)
        throws Exception {
        SimpleFeatureBuilder b = new SimpleFeatureBuilder(fType);

        Object[] attributes = new Object[fType.getAttributeCount()];

        for (int i = 0; i < fType.getAttributeCount(); i++) {
            AttributeDescriptor att = fType.getDescriptor(i);
            AttributeType attType = att.getType();
            Object attValue = node.getChildValue(att.getLocalName());

            if ((attValue != null) && !attType.getBinding().isAssignableFrom(attValue.getClass())) {
                //type mismatch, to try convert
                Object converted = Converters.convert(attValue, attType.getBinding());

                if (converted != null) {
                    attValue = converted;
                }
            }

            b.add(attValue);
        }

        //create the feature
        return b.buildFeature(fid);
    }

    public static CoordinateReferenceSystem crs(Node node) {
        if (node.getAttribute("srsName") != null) {
            URI srs = null;
            Object raw = node.getAttributeValue("srsName");

            if (raw instanceof URI) {
                srs = (URI) raw;
            } else if (raw instanceof String) {
                //try to parse into a uri
                try {
                    srs = new URI((String) raw);
                } catch (URISyntaxException e) {
                    //failed, continue on
                }
            }

            if (srs != null) {
                //TODO: JD, this is a hack until GEOT-1136 has been resolved
                if ("http".equals(srs.getScheme()) && "www.opengis.net".equals(srs.getAuthority())
                        && "/gml/srs/epsg.xml".equals(srs.getPath()) && (srs.getFragment() != null)) {
                    try {
                        return CRS.decode("EPSG:" + srs.getFragment());
                    } catch (Exception e) {
                        //failed, try as straight up uri
                        try {
                            return CRS.decode(srs.toString());
                        } catch (Exception e1) {
                            //failed again, do nothing ,should fail below as well
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

    /**
     * Wraps the elements of a geometry collection in a normal collection.
     */
    public static Collection asCollection(GeometryCollection gc) {
        ArrayList members = new ArrayList();

        for (int i = 0; i < gc.getNumGeometries(); i++) {
            members.add(gc.getGeometryN(i));
        }

        return members;
    }
    
    static GeometryCollection GeometryCollectionType_parse(Node node, Class clazz, GeometryFactory gFactory) {
        //round up children that are geometries, since this type is often 
        // extended by multi geometries, dont reference members by element name
        List geoms = new ArrayList();

        for (Iterator itr = node.getChildren().iterator(); itr.hasNext();) {
            Node cnode = (Node) itr.next();

            if (cnode.getValue() instanceof Geometry) {
                geoms.add(cnode.getValue());
            }
        }

        GeometryCollection gc = null;
        
        if (MultiPoint.class.isAssignableFrom(clazz)) {
            gc = gFactory.createMultiPoint((Point[]) geoms.toArray(new Point[geoms.size()]));
        }
        else if (MultiLineString.class.isAssignableFrom(clazz)) {
            gc = gFactory.createMultiLineString(
                (LineString[]) geoms.toArray(new LineString[geoms.size()]));
        }
        else if (MultiPolygon.class.isAssignableFrom(clazz)) {
            gc = gFactory.createMultiPolygon((Polygon[]) geoms.toArray(new Polygon[geoms.size()]));
        }
        
        else {
            gc = gFactory.createGeometryCollection((Geometry[]) geoms.toArray(
                new Geometry[geoms.size()]));
        }
        
        //set an srs if there is one
        CoordinateReferenceSystem crs = crs(node);

        if (crs != null) {
            gc.setUserData(crs);
        }
        
        return gc;
    }
    
    static Object GeometryCollectionType_getProperty(Object object, QName name) {
        if ( "srsName".equals( name.getLocalPart() ) ) {
            CoordinateReferenceSystem crs = GML2EncodingUtils.getCRS((GeometryCollection)object );
            if ( crs != null ) {
                return GML2EncodingUtils.toURI(crs,true);
            }
        }
        return null;
    }
}
