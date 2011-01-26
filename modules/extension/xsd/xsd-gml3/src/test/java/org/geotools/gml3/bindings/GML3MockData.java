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
package org.geotools.gml3.bindings;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import java.util.Date;
import javax.xml.namespace.QName;
import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Envelope;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryCollection;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.LineString;
import com.vividsolutions.jts.geom.LinearRing;
import com.vividsolutions.jts.geom.MultiLineString;
import com.vividsolutions.jts.geom.MultiPoint;
import com.vividsolutions.jts.geom.MultiPolygon;
import com.vividsolutions.jts.geom.Point;
import com.vividsolutions.jts.geom.Polygon;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.geotools.feature.simple.SimpleFeatureBuilder;
import org.geotools.feature.simple.SimpleFeatureTypeBuilder;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.gml3.GML;
import org.geotools.referencing.CRS;


/**
 * Utility class for creating test xml data for gml3 bindings.
 *
 * @author Justin Deoliveira, The Open Planning Project
 *
 *
 * @source $URL$
 */
public class GML3MockData {
    static GeometryFactory gf = new GeometryFactory();

    static Element point(Document document, Node parent) {
        Element point = element(GML.Point, document, parent);
        point.setAttribute("srsName", "urn:x-ogc:def:crs:EPSG:6.11.2:4326");

        Element pos = element(GML.pos, document, point);
        pos.appendChild(document.createTextNode("1.0 2.0 "));

        return point;
    }

    static CoordinateReferenceSystem crs() {
        try {
            return CRS.decode("urn:x-ogc:def:crs:EPSG:6.11.2:4326");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    static Envelope bounds() {
        return new ReferencedEnvelope(0, 10, 0, 10, crs());
    }

    static Point point() {
        Point p = gf.createPoint(new Coordinate(1, 2));
        p.setUserData(crs());

        return p;
    }

    static LineString lineString() {
        return gf.createLineString(new Coordinate[] { new Coordinate(1, 2), new Coordinate(3, 4) });
    }

    static Element lineString(Document document, Node parent) {
        return lineStringWithPos(document, parent);
    }
    
    static Element lineStringProperty(Document document, Node parent) {
        Element property = element(GML.lineStringProperty, document, parent);

        lineString(document, property);

        return property;
    }

    static Element lineStringWithPos(Document document, Node parent) {
        Element lineString = element(GML.LineString, document, parent);

        Element pos = element(GML.pos, document, lineString);
        pos.appendChild(document.createTextNode("1.0 2.0"));

        pos = element(GML.pos, document, lineString);
        pos.appendChild(document.createTextNode("3.0 4.0"));

        return lineString;
    }

    static Element lineStringWithPosList(Document document, Node parent) {
        Element lineString = element(GML.LineString, document, parent);
        Element posList = element(GML.posList, document, lineString);
        posList.appendChild(document.createTextNode("1.0 2.0 3.0 4.0"));

        return lineString;
    }

    static LinearRing linearRing() {
        return gf.createLinearRing(new Coordinate[] {
                new Coordinate(1, 1), new Coordinate(2, 2), new Coordinate(3, 3),
                new Coordinate(1, 1)
            });
    }

    static Element linearRing(Document document, Node parent) {
        return linearRingWithPos(document, parent);
    }

    static Element linearRingWithPos(Document document, Node parent) {
        Element linearRing = element(GML.LinearRing, document, parent);

        Element pos = element(GML.pos, document, linearRing);
        pos.appendChild(document.createTextNode("1.0 2.0"));

        pos = element(GML.pos, document, linearRing);
        pos.appendChild(document.createTextNode("3.0 4.0"));

        pos = element(GML.pos, document, linearRing);
        pos.appendChild(document.createTextNode("5.0 6.0"));

        pos = element(GML.pos, document, linearRing);
        pos.appendChild(document.createTextNode("1.0 2.0"));

        return linearRing;
    }

    static Element linearRingWithPosList(Document document, Node parent) {
        Element linearRing = element(GML.LinearRing, document, parent);

        Element posList = element(GML.posList, document, linearRing);

        linearRing.appendChild(posList);
        posList.appendChild(document.createTextNode("1.0 2.0 3.0 4.0 5.0 6.0 1.0 2.0"));

        return linearRing;
    }

    static Polygon polygon() {
        return gf.createPolygon(linearRing(), null);
    }

    static Element polygon(Document document, Node parent) {
        return polygon(document,parent,GML.Polygon,false); 
    }
    
    static Element polygon(Document document, Node parent, QName name, boolean withInterior) {
        Element polygon = element(name, document, parent);

        Element exterior = element(GML.exterior, document, polygon);
        linearRing(document, exterior);
        
        if ( withInterior ) {
            Element interior = element(GML.interior, document, polygon);
            linearRing(document,interior);
        }

        return polygon;
    }

    static MultiPoint multiPoint() {
        return gf.createMultiPoint(new Coordinate[] { new Coordinate(1, 1), new Coordinate(2, 2) });
    }

    static Element multiPoint(Document document, Node parent) {
        Element multiPoint = element(GML.MultiPoint, document, parent);

        // 2 pointMember elements
        Element pointMember = element(GML.pointMember, document, multiPoint);
        point(document, pointMember);

        pointMember = element(GML.pointMember, document, multiPoint);
        point(document, pointMember);

        //1 pointMembers elmenet with 2 members
        Element pointMembers = element(GML.pointMembers, document, multiPoint);
        point(document, pointMembers);
        point(document, pointMembers);

        return multiPoint;
    }

    static MultiLineString multiLineString() {
        return gf.createMultiLineString(new LineString[] { lineString(), lineString() });
    }

    static Element multiLineString(Document document, Node parent) {
        Element multiLineString = element(GML.MultiLineString, document, parent);

        Element lineStringMember = element(GML.lineStringMember, document, multiLineString);
        lineString(document, lineStringMember);

        lineStringMember = element(GML.lineStringMember, document, multiLineString);
        lineString(document, lineStringMember);

        return multiLineString;
    }
    
    public static Element multiCurve(Document document, Node parent) {
        return multiCurve(document, parent, true);
    }
    
    public static Element multiCurve(Document document, Node parent, boolean useCurveMember) {
        Element multiCurve = element(GML.MultiCurve, document, parent);

        if (useCurveMember) {
            Element curveMember = element(GML.curveMember, document, multiCurve);
            lineString(document, curveMember);
    
            curveMember = element(GML.curveMember, document, multiCurve);
            lineString(document, curveMember);
        }
        else {
            Element curveMembers = element(GML.curveMembers, document, multiCurve);
            lineString(document, curveMembers);
            lineString(document, curveMembers);
        }

        return multiCurve;
    }

    static MultiPolygon multiPolygon() {
        return gf.createMultiPolygon(new Polygon[] { polygon(), polygon() });
    }

    static Element multiPolygon(Document document, Node parent) {
        Element multiPolygon = element(GML.MultiPolygon, document, parent);

        Element polygonMember = element(GML.polygonMember, document, multiPolygon);
        polygon(document, polygonMember);

        polygonMember = element(GML.polygonMember, document, multiPolygon);
        polygon(document, polygonMember);

        return multiPolygon;
    }
    
    public static Element multiSurface(Document document, Node parent) {
        return multiSurface(document, parent, true);
    }
        
    public static Element multiSurface(Document document, Node parent, boolean useSurfaceMember) {
        Element multiSurface = element(GML.MultiSurface, document, parent);

        if (useSurfaceMember) {
            Element surfaceMember = element(GML.surfaceMember, document, multiSurface);
            polygon(document, surfaceMember);
    
            surfaceMember = element(GML.surfaceMember, document, multiSurface);
            polygon(document, surfaceMember);
        }
        else {
            Element surfaceMembers = element(GML.surfaceMembers, document, multiSurface);
            polygon(document, surfaceMembers);
            polygon(document, surfaceMembers);
        }

        return multiSurface;
    }
    
    static GeometryCollection multiGeometry() {
        return gf.createGeometryCollection(new Geometry[]{point(),lineString(),polygon()});
    }
    
    static Element multiGeometry(Document document, Node parent ) {
        Element multiGeometry = element(GML.MultiGeometry, document, parent );
        
        Element geometryMember = element(GML.geometryMember, document, multiGeometry);
        point(document,geometryMember);
        
        geometryMember = element(GML.geometryMember, document, multiGeometry);
        lineString(document,geometryMember);
        
        geometryMember = element(GML.geometryMember, document, multiGeometry);
        polygon(document,geometryMember);
        
        return multiGeometry;
    }

    static Element surface(Document document, Node parent) {
        Element surface = element(GML.Surface, document ,parent);
        Element patches = element(GML.patches, document, surface);
        
        polygon(document,patches,GML.PolygonPatch,true);
        
        return surface;
    }
    
    static Element feature(Document document, Node parent) {
        Element feature = element(TEST.TestFeature, document, parent);
        Element geom = element(new QName(TEST.NAMESPACE, "geom"), document, feature);
        point(document, geom);

        Element count = GML3MockData.element(new QName(TEST.NAMESPACE, "count"), document, feature);
        count.appendChild(document.createTextNode("1"));

        return feature;
    }

    public static SimpleFeature feature() throws Exception {
        SimpleFeatureTypeBuilder typeBuilder = new SimpleFeatureTypeBuilder();
        typeBuilder.setName(TEST.TestFeature.getLocalPart());
        typeBuilder.setNamespaceURI(TEST.TestFeature.getNamespaceURI());

        typeBuilder.add("name", String.class);
        typeBuilder.add("description", String.class);
        typeBuilder.add("geom", Point.class);
        typeBuilder.add("count", Integer.class);
        typeBuilder.add("date", Date.class);

        SimpleFeatureType type = (SimpleFeatureType) typeBuilder.buildFeatureType();

        SimpleFeatureBuilder builder = new SimpleFeatureBuilder(type);
        builder.add("theName");
        builder.add("theDescription");
        builder.add(point());
        builder.add(new Integer(1));
        builder.add(new Date());

        return (SimpleFeature) builder.buildFeature("fid.1");
    }

    static Element featureMember(Document document, Node parent) {
        Element featureMember = element(GML.featureMember, document, parent);
        feature(document, featureMember);

        return featureMember;
    }

    static Element element(QName name, Document document, Node parent) {
        Element element = document.createElementNS(name.getNamespaceURI(), name.getLocalPart());

        if (parent != null) {
            parent.appendChild(element);
        }

        return element;
    }
}
