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

/** Names of GML elements parseable with XmlStreamGeometryReader. */
public class GML {
    private GML() {}

    public static final String NAMESPACE = "http://www.opengis.net/gml";
    public static final String NAMESPACE_3_2 = "http://www.opengis.net/gml/3.2";

    public static final String Arc = "Arc";
    public static final String CompositeCurve = "CompositeCurve";
    public static final String coord = "coord";
    public static final String coordinates = "coordinates";
    public static final String Curve = "Curve";
    public static final String curveMember = "curveMember";
    public static final String exterior = "exterior";
    public static final String innerBoundaryIs = "innerBoundaryIs";
    public static final String interior = "interior";
    public static final String LinearRing = "LinearRing";
    public static final String LineString = "LineString";
    public static final String lineStringMember = "lineStringMember";
    public static final String LineStringSegment = "LineStringSegment";
    public static final String MultiCurve = "MultiCurve";
    public static final String MultiLineString = "MultiLineString";
    public static final String MultiPoint = "MultiPoint";
    public static final String MultiPolygon = "MultiPolygon";
    public static final String MultiSurface = "MultiSurface";
    public static final String OrientableCurve = "OrientableCurve";
    public static final String outerBoundaryIs = "outerBoundaryIs";
    public static final String Point = "Point";
    public static final String pointMember = "pointMember";
    public static final String pointMembers = "pointMembers";
    public static final String Polygon = "Polygon";
    public static final String polygonMember = "polygonMember";
    public static final String Surface = "Surface";
    public static final String patches = "patches";
    public static final String PolygonPatch = "PolygonPatch";
    public static final String pos = "pos";
    public static final String posList = "posList";
    public static final String Ring = "Ring";
    public static final String segments = "segments";
    public static final String srsName = "srsName";
    public static final String surfaceMember = "surfaceMember";
    public static final String surfaceMembers = "surfaceMembers";
}
