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

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Logger;

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
//import org.geotools.gml2.GML;
import org.geotools.gml2.GML;
import org.geotools.gml2.GMLConfiguration;
import org.geotools.gml2.bindings.GMLEncodingUtils;
import org.geotools.metadata.iso.citation.Citations;
import org.geotools.referencing.CRS;
import org.geotools.referencing.CRS.AxisOrder;
import org.geotools.util.logging.Logging;
import org.geotools.xlink.XLINK;
import org.geotools.xml.Configuration;
import org.geotools.xml.Encoder;
import org.geotools.xml.SchemaIndex;
import org.geotools.xml.Schemas;
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
import org.opengis.metadata.Identifier;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.opengis.referencing.crs.GeographicCRS;
import org.opengis.referencing.crs.ProjectedCRS;
import org.opengis.referencing.cs.AxisDirection;
import org.opengis.referencing.cs.CoordinateSystem;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryCollection;

/**
 * Utility methods used by gml2 bindigns when encodding.
 *
 * @author Justin Deoliveira, The Open Planning Project, jdeolive@openplans.org
 * @author Ben Caradoc-Davies, CSIRO Exploration and Mining
 *
 *
 * @source $URL$
 */
public class GML2EncodingUtils {
    
    /** logging instance */
    static Logger LOGGER = Logging.getLogger( "org.geotools.gml");
    
    static GMLEncodingUtils e = new GMLEncodingUtils(GML.getInstance());

    public static String epsgCode(CoordinateReferenceSystem crs) {
        if (crs == null) {
            return null;
        }

        for (Iterator i = crs.getIdentifiers().iterator(); i.hasNext();) {
            Identifier id = (Identifier) i.next();

            //return "EPSG:" + id.getCode();
            if ((id.getAuthority() != null)
                    && id.getAuthority().getTitle().equals(Citations.EPSG.getTitle())) {
                return id.getCode();
            }
        }

        return null;
    }

    /**
     * @deprecated use {@link #toURI(CoordinateReferenceSystem)}.
     */
    public static String crs(CoordinateReferenceSystem crs) {
        return toURI(crs);
    }

    /**
     * Encodes the crs object as a uri.
     */
    public static String toURI(CoordinateReferenceSystem crs) {
        return toURI(crs,false);
    }
    
    /**
     * Encodes the crs object as a uri.
     * <p>
     * The axis order of the crs determines which form of uri is used.
     * </p>
     */
    public static String toURI(CoordinateReferenceSystem crs, boolean forceOldStyle) {
        String code = epsgCode(crs);
        AxisOrder axisOrder = CRS.getAxisOrder(crs, true);

        if (code != null) {
            if (forceOldStyle ||( (axisOrder == AxisOrder.EAST_NORTH) || 
                    (axisOrder == AxisOrder.INAPPLICABLE)) ) {
                return "http://www.opengis.net/gml/srs/epsg.xml#" + code;
            } else {
                //return "urn:x-ogc:def:crs:EPSG:6.11.2:" + code;
                return "urn:x-ogc:def:crs:EPSG:" + code;
            }
        }

        return null;
    }

    /**
     * Determines the crs of the geometry by checking {@link Geometry#getUserData()}.
     * <p>
     * This method returns <code>null</code> when no crs can be found.
     * </p>
     */
    public static CoordinateReferenceSystem getCRS(Geometry g) {
        if (g.getUserData() == null) {
            return null;
        }

        if (g.getUserData() instanceof CoordinateReferenceSystem) {
            return (CoordinateReferenceSystem) g.getUserData();
        }

        if (g.getUserData() instanceof Map) {
            Map userData = (Map) g.getUserData();

            return (CoordinateReferenceSystem) userData.get(CoordinateReferenceSystem.class);
        }

        return null;
    }

    /**
     * Determines the identifier (gml:id) of the geometry by checking
     * {@link Geometry#getUserData()}.
     * <p>
     * This method returns <code>null</code> when no id can be found.
     * </p>
     */
    public static String getID(Geometry g) {
        return e.getMetadata( g, "gml:id" );
    }
    
    /**
     * Determines the description (gml:description) of the geometry by checking
     * {@link Geometry#getUserData()}.
     * <p>
     * This method returns <code>null</code> when no name can be found.
     * </p>
     */
    public static String getName(Geometry g) {
        return e.getMetadata( g, "gml:name" );
    }
    
    /**
     * Determines the name (gml:name) of the geometry by checking
     * {@link Geometry#getUserData()}.
     * <p>
     * This method returns <code>null</code> when no description can be found.
     * </p>
     */
    public static String getDescription(Geometry g) {
        return e.getMetadata( g, "gml:description" );
    }
    
    public static Element AbstractFeatureType_encode(Object object, Document document, Element value) {
        Feature feature = (Feature) object;
        FeatureType featureType = feature.getType();

        String namespace = featureType.getName().getNamespaceURI();
        String typeName = featureType.getName().getLocalPart();

        Element encoding = document.createElementNS(namespace, typeName);
        encoding.setAttributeNS(null, "fid", feature.getIdentifier().getID());

        return encoding;
    }
    
    public static List AbstractFeatureType_getProperties(Object object,
            XSDElementDeclaration element, SchemaIndex schemaIndex, Set<String> toFilter,
            Configuration configuration) {

        return e.AbstractFeatureType_getProperties(object, element, schemaIndex, toFilter, configuration);
    }
    
    public static XSDTypeDefinition createXmlTypeFromFeatureType(SimpleFeatureType featureType, SchemaIndex schemaIndex, Set<String> toFilter ) { 
       return e.createXmlTypeFromFeatureType(featureType, schemaIndex, toFilter);
    }

    public static Object GeometryPropertyType_getProperty(Geometry geometry,
            QName name) {
        return e.GeometryPropertyType_getProperty(geometry,name);
    }
    public static Object GeometryPropertyType_getProperty(Geometry geometry,
            QName name, boolean includeAbstractGeometry ) {
        return e.GeometryPropertyType_getProperty(geometry, name, includeAbstractGeometry);
    }

    public static List GeometryPropertyType_getProperties(Geometry geometry) {
        return e.GeometryPropertyType_getProperties(geometry);
    }
    
   
}
