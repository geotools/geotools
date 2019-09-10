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
package org.geotools.gml4wcs.bindings;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Collections;
import java.util.List;
import javax.xml.namespace.QName;
import org.eclipse.xsd.XSDElementDeclaration;
import org.geotools.geometry.DirectPosition2D;
import org.geotools.gml2.bindings.GML2EncodingUtils;
import org.geotools.gml3.GML;
import org.geotools.xlink.XLINK;
import org.geotools.xsd.ComplexBinding;
import org.geotools.xsd.Encoder;
import org.locationtech.jts.geom.CoordinateSequence;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.GeometryCollection;
import org.locationtech.jts.geom.LineString;
import org.opengis.geometry.DirectPosition;
import org.opengis.referencing.crs.CoordinateReferenceSystem;

/**
 * Utility class for gml3 encoding.
 *
 * @author Justin Deoliveira, The Open Planning Project, jdeolive@openplans.org
 */
public class GML3EncodingUtils {
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
            String crsCode = GML2EncodingUtils.toURI(crs);

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
     *
     * <ul>
     *   <li>GeometryPropertyType
     *   <li>PointPropertyType
     *   <li>LineStringPropertyType
     *   <li>PolygonPropertyType
     * </ul>
     */
    static Object getProperty(Geometry geometry, QName name) {

        if (GML._Geometry.equals(name)
                || GML.Point.equals(name)
                || GML.LineString.equals(name)
                || GML.Polygon.equals(name)) {
            // if the geometry is null, return null
            if (isEmpty(geometry)) {
                return null;
            }

            return geometry;
        }

        if (XLINK.HREF.equals(name)) {
            // only process if geometry is empty
            if (isEmpty(geometry)) {
                String id = GML3EncodingUtils.getID(geometry);
                if (id != null) {
                    return "#" + id;
                }
            }
        }

        return null;
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
    static List getProperties(Geometry geometry) {

        String id = GML3EncodingUtils.getID(geometry);

        if (!isEmpty(geometry) && id != null) {
            // return a comment which is hte xlink href
            return Collections.singletonList(new Object[] {Encoder.COMMENT, "#" + id});
        }

        return null;
    }

    static boolean isEmpty(Geometry geometry) {
        if (geometry.isEmpty()) {
            // check for case of multi geometry, if it has > 0 goemetries
            // we consider this to be not empty
            if (geometry instanceof GeometryCollection) {
                if (((GeometryCollection) geometry).getNumGeometries() != 0) {
                    return false;
                }
            }
            return true;
        }

        return false;
    }
}
