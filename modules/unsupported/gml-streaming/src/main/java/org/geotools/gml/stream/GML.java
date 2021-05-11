/*
 * GeoTools - The Open Source Java GIS Toolkit
 * http://geotools.org
 *
 * (C) 2021, Open Source Geospatial Foundation (OSGeo)
 *
 * This library is free software; you can redistribute it and/or modify it under the terms of the
 * GNU Lesser General Public License as published by the Free Software Foundation; version 2.1 of
 * the License.
 *
 * This library is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without
 * even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 */

package org.geotools.gml.stream;

import javax.xml.namespace.QName;

/** Only the constants from the GML schema which are used by XmlStreamGeometryReader. */
public class GML {
    private GML() {}

    public static final String NAMESPACE = "http://www.opengis.net/gml";

    public static final QName CompositeCurve = new QName(NAMESPACE, "CompositeCurve");
    public static final QName coord = new QName(NAMESPACE, "coord");
    public static final QName coordinates = new QName(NAMESPACE, "coordinates");
    public static final QName Curve = new QName(NAMESPACE, "Curve");
    public static final QName curveMember = new QName(NAMESPACE, "curveMember");
    public static final QName exterior = new QName(NAMESPACE, "exterior");
    public static final QName innerBoundaryIs = new QName(NAMESPACE, "innerBoundaryIs");
    public static final QName interior = new QName(NAMESPACE, "interior");
    public static final QName LinearRing = new QName(NAMESPACE, "LinearRing");
    public static final QName LineString = new QName(NAMESPACE, "LineString");
    public static final QName lineStringMember = new QName(NAMESPACE, "lineStringMember");
    public static final QName MultiCurve = new QName(NAMESPACE, "MultiCurve");
    public static final QName MultiLineString = new QName(NAMESPACE, "MultiLineString");
    public static final QName MultiPoint = new QName(NAMESPACE, "MultiPoint");
    public static final QName MultiPolygon = new QName(NAMESPACE, "MultiPolygon");
    public static final QName MultiSurface = new QName(NAMESPACE, "MultiSurface");
    public static final QName OrientableCurve = new QName(NAMESPACE, "OrientableCurve");
    public static final QName outerBoundaryIs = new QName(NAMESPACE, "outerBoundaryIs");
    public static final QName Point = new QName(NAMESPACE, "Point");
    public static final QName pointMember = new QName(NAMESPACE, "pointMember");
    public static final QName pointMembers = new QName(NAMESPACE, "pointMembers");
    public static final QName Polygon = new QName(NAMESPACE, "Polygon");
    public static final QName polygonMember = new QName(NAMESPACE, "polygonMember");
    public static final QName pos = new QName(NAMESPACE, "pos");
    public static final QName posList = new QName(NAMESPACE, "posList");
    public static final QName srsName = new QName(NAMESPACE, "srsName");
    public static final QName surfaceMember = new QName(NAMESPACE, "surfaceMember");
    public static final QName surfaceMembers = new QName(NAMESPACE, "surfaceMembers");
}
