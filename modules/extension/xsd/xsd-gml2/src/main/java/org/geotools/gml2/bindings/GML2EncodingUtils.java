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
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Logger;
import javax.xml.namespace.QName;
import org.eclipse.xsd.XSDElementDeclaration;
import org.eclipse.xsd.XSDTypeDefinition;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.geometry.jts.coordinatesequence.CoordinateSequences;
import org.geotools.gml2.GML;
import org.geotools.gml2.GMLConfiguration;
import org.geotools.gml2.SrsSyntax;
import org.geotools.metadata.iso.citation.Citations;
import org.geotools.referencing.CRS;
import org.geotools.referencing.CRS.AxisOrder;
import org.geotools.util.logging.Logging;
import org.geotools.xsd.Configuration;
import org.geotools.xsd.SchemaIndex;
import org.locationtech.jts.geom.Geometry;
import org.opengis.feature.Feature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.feature.type.FeatureType;
import org.opengis.metadata.Identifier;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 * Utility methods used by gml2 bindigns when encodding.
 *
 * @author Justin Deoliveira, The Open Planning Project, jdeolive@openplans.org
 * @author Ben Caradoc-Davies, CSIRO Exploration and Mining
 */
public class GML2EncodingUtils {

    /** logging instance */
    static Logger LOGGER = Logging.getLogger(GML2EncodingUtils.class);

    static GMLEncodingUtils e = new GMLEncodingUtils(GML.getInstance());

    public static String epsgCode(CoordinateReferenceSystem crs) {
        if (crs == null) {
            return null;
        }

        for (Iterator i = crs.getIdentifiers().iterator(); i.hasNext(); ) {
            Identifier id = (Identifier) i.next();

            // return "EPSG:" + id.getCode();
            if ((id.getAuthority() != null)
                    && id.getAuthority().getTitle().equals(Citations.EPSG.getTitle())) {
                return id.getCode();
            }
        }

        return null;
    }

    /** Encodes the crs object as a uri. */
    public static String toURI(CoordinateReferenceSystem crs) {
        return toURI(crs, false);
    }

    /**
     * Encodes the crs object as a uri.
     *
     * <p>The axis order of the crs determines which form of uri is used.
     */
    public static String toURI(CoordinateReferenceSystem crs, boolean forceOldStyle) {
        return toURI(crs, forceOldStyle ? SrsSyntax.OGC_HTTP_URL : SrsSyntax.OGC_URN_EXPERIMENTAL);
    }

    /**
     * Encodes the crs object as a uri using the specified syntax.
     *
     * <p>The axis order of the crs is taken into account. In cases where
     */
    public static String toURI(CoordinateReferenceSystem crs, SrsSyntax srsSyntax) {
        String code = epsgCode(crs);
        AxisOrder axisOrder = CRS.getAxisOrder(crs, true);

        if (code != null) {
            // do an axis order check, if axisOrder is east/north or inapplicable force the legacy
            // syntax since the newer syntaxes define a different axis ordering
            // JD: TODO: perhaps we don't want to do this override and just want to use the
            // specified
            // syntax verbatim, maintaining this check for to maintain the excision behavior of this
            // method
            if (axisOrder == AxisOrder.EAST_NORTH || axisOrder == AxisOrder.INAPPLICABLE) {
                srsSyntax = SrsSyntax.OGC_HTTP_URL;
            }

            return srsSyntax.getPrefix() + code;
        }

        // if crs has no EPSG code but its identifier is a URI, then use its identifier
        if (crs != null && crs.getName() != null && crs.getName().getCode() != null) {
            try {
                // test whether identifier is a valid URI
                new URI(crs.getName().getCode());
                // acceptable to java.net.URI so return it
                return crs.getName().getCode();
            } catch (URISyntaxException e) {
                // not a valid URI so ignore
            }
        }
        return null;
    }

    /**
     * Determines the crs of the geometry by checking {@link Geometry#getUserData()}.
     *
     * <p>This method returns <code>null</code> when no crs can be found.
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
     * Determines the identifier (gml:id) of the geometry by checking {@link
     * Geometry#getUserData()}.
     *
     * <p>This method returns <code>null</code> when no id can be found.
     */
    public static String getID(Geometry g) {
        return e.getMetadata(g, "gml:id");
    }

    /**
     * Set the identifier (gml:id) of the geometry as a key in the user data map {@link
     * Geometry#getUserData()} (creating it with{@link Geometry#getUserData()} if it does not
     * already exist). If the user data exists and is not a {@link Map}, this method has no effect.
     *
     * @param g the geometry
     * @param id the gml:id to be set
     */
    public static void setID(Geometry g, String id) {
        e.setMetadata(g, "gml:id", id);
    }

    /**
     * Determines the description (gml:description) of the geometry by checking {@link
     * Geometry#getUserData()}.
     *
     * <p>This method returns <code>null</code> when no name can be found.
     */
    public static String getName(Geometry g) {
        return e.getMetadata(g, "gml:name");
    }

    /**
     * Set the name (gml:name) of the geometry as a key in the user data map {@link
     * Geometry#getUserData()} (creating it with{@link Geometry#getUserData()} if it does not
     * already exist). If the user data exists and is not a {@link Map}, this method has no effect.
     *
     * @param g the geometry
     * @param name the gml:name to be set
     */
    public static void setName(Geometry g, String name) {
        e.setMetadata(g, "gml:name", name);
    }

    /**
     * Determines the name (gml:name) of the geometry by checking {@link Geometry#getUserData()}.
     *
     * <p>This method returns <code>null</code> when no description can be found.
     */
    public static String getDescription(Geometry g) {
        return e.getMetadata(g, "gml:description");
    }

    /**
     * Set the description (gml:description) of the geometry as a key in the user data map {@link
     * Geometry#getUserData()} (creating it with{@link Geometry#getUserData()} if it does not
     * already exist). If the user data exists and is not a {@link Map}, this method has no effect.
     *
     * @param g the geometry
     * @param description the gml:description to be set
     */
    public static void setDescription(Geometry g, String description) {
        e.setMetadata(g, "gml:description", description);
    }

    public static Element AbstractFeatureType_encode(
            Object object, Document document, Element value) {
        Feature feature = (Feature) object;
        FeatureType featureType = feature.getType();

        String namespace = featureType.getName().getNamespaceURI();
        String typeName = featureType.getName().getLocalPart();

        Element encoding = document.createElementNS(namespace, typeName);
        encoding.setAttributeNS(null, "fid", feature.getIdentifier().getID());

        return encoding;
    }

    public static List AbstractFeatureType_getProperties(
            Object object,
            XSDElementDeclaration element,
            SchemaIndex schemaIndex,
            Set<String> toFilter,
            Configuration configuration) {

        return e.AbstractFeatureType_getProperties(
                object, element, schemaIndex, toFilter, configuration);
    }

    public static XSDTypeDefinition createXmlTypeFromFeatureType(
            SimpleFeatureType featureType, SchemaIndex schemaIndex, Set<String> toFilter) {
        return e.createXmlTypeFromFeatureType(featureType, schemaIndex, toFilter);
    }

    public static Object GeometryPropertyType_getProperty(Geometry geometry, QName name) {
        return e.GeometryPropertyType_getProperty(geometry, name);
    }

    public static Object GeometryPropertyType_getProperty(
            Geometry geometry, QName name, boolean includeAbstractGeometry) {
        return e.GeometryPropertyType_getProperty(geometry, name, includeAbstractGeometry);
    }

    public static List GeometryPropertyType_getProperties(Geometry geometry) {
        return e.GeometryPropertyType_getProperties(geometry);
    }

    /**
     * Returns the geometry dimension, either as forced in the configuration, or the geometry
     * natural one
     */
    public static Integer getGeometryDimension(Geometry geometry, Configuration config) {
        if (GMLEncodingUtils.isEmpty(geometry)) {
            return null;
        }

        // check if srsDimension is turned off
        if (config.hasProperty(GMLConfiguration.NO_SRS_DIMENSION)) {
            return null;
        }

        /**
         * For the dimension, use the actual dimension of the geometry. Using the dimension of the
         * CRS is not sufficient, since currently CRSes don't support 3D.
         */
        return CoordinateSequences.coordinateDimension(geometry);
    }

    public static Integer getEnvelopeDimension(ReferencedEnvelope e, Configuration configuration) {
        if (e == null || e.isNull() || e.getCoordinateReferenceSystem() == null) {
            return null;
        }

        // check if srsDimension is turned off
        if (configuration.hasProperty(GMLConfiguration.NO_SRS_DIMENSION)) {
            return null;
        }

        return e.getCoordinateReferenceSystem().getCoordinateSystem().getDimension();
    }
}
