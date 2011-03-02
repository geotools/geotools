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

import java.util.Date;

import javax.xml.namespace.QName;

import org.geotools.feature.simple.SimpleFeatureBuilder;
import org.geotools.feature.simple.SimpleFeatureTypeBuilder;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.gml3.GML;
import org.geotools.referencing.CRS;
import org.geotools.xml.XSD;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

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
    static XSD gml = GML.getInstance();
    
    public static void setGML(XSD gml) {
        if (gml == null) {
            gml = GML.getInstance();
        }
        GML3MockData.gml = gml;
    }
    
    public static Element point(Document document, Node parent) {
        Element point = element(GML.Point, document, parent);
        point.setAttribute("srsName", "urn:x-ogc:def:crs:EPSG:6.11.2:4326");

        Element pos = element(qName("pos"), document, point);
        pos.appendChild(document.createTextNode("1.0 2.0 "));

        return point;
    }

    public static CoordinateReferenceSystem crs() {
        try {
            return CRS.decode("urn:x-ogc:def:crs:EPSG:6.11.2:4326");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static Envelope bounds() {
        return new ReferencedEnvelope(0, 10, 0, 10, crs());
    }

    public static Point point() {
        Point p = gf.createPoint(new Coordinate(1, 2));
        p.setUserData(crs());

        return p;
    }

    public static LineString lineString() {
        return gf.createLineString(new Coordinate[] { new Coordinate(1, 2), new Coordinate(3, 4) });
    }

    public static Element lineString(Document document, Node parent) {
        return lineStringWithPos(document, parent);
    }
    
    public static Element lineStringProperty(Document document, Node parent) {
        Element property = element(qName("lineStringProperty"), document, parent);

        lineString(document, property);

        return property;
    }

    public static Element lineStringWithPos(Document document, Node parent) {
        Element lineString = element(qName("LineString"), document, parent);

        Element pos = element(qName("pos"), document, lineString);
        pos.appendChild(document.createTextNode("1.0 2.0"));

        pos = element(qName("pos"), document, lineString);
        pos.appendChild(document.createTextNode("3.0 4.0"));

        return lineString;
    }

    public static Element lineStringWithPosList(Document document, Node parent) {
        Element lineString = element(qName("LineString"), document, parent);
        Element posList = element(qName("posList"), document, lineString);
        posList.appendChild(document.createTextNode("1.0 2.0 3.0 4.0"));

        return lineString;
    }

    public static Element arcWithPosList(Document document, Node parent) {
        Element arc = element(qName("Arc"), document, parent);
        Element posList = element(qName("posList"), document, arc);
        posList.appendChild(document.createTextNode("1.0 1.0 2.0 2.0 3.0 1.0"));

        return arc;
    }

    public static Element circleWithPosList(Document document, Node parent) {
        Element circle = element(qName("Circle"), document, parent);
        Element posList = element(qName("posList"), document, circle);
        posList.appendChild(document.createTextNode("1.0 1.0 2.0 2.0 3.0 1.0"));

        return circle;
    }

    public static LinearRing linearRing() {
        return gf.createLinearRing(new Coordinate[] {
                new Coordinate(1, 1), new Coordinate(2, 2), new Coordinate(3, 3),
                new Coordinate(1, 1)
            });
    }

    public static Element linearRing(Document document, Node parent) {
        return linearRingWithPos(document, parent);
    }

    public static Element linearRingWithPos(Document document, Node parent) {
        Element linearRing = element(qName("LinearRing"), document, parent);

        Element pos = element(qName("pos"), document, linearRing);
        pos.appendChild(document.createTextNode("1.0 2.0"));

        pos = element(qName("pos"), document, linearRing);
        pos.appendChild(document.createTextNode("3.0 4.0"));

        pos = element(qName("pos"), document, linearRing);
        pos.appendChild(document.createTextNode("5.0 6.0"));

        pos = element(qName("pos"), document, linearRing);
        pos.appendChild(document.createTextNode("1.0 2.0"));

        return linearRing;
    }

    public static Element linearRingWithPosList(Document document, Node parent) {
        Element linearRing = element(qName("LinearRing"), document, parent);

        Element posList = element(qName("posList"), document, linearRing);

        linearRing.appendChild(posList);
        posList.appendChild(document.createTextNode("1.0 2.0 3.0 4.0 5.0 6.0 1.0 2.0"));

        return linearRing;
    }

    public static Polygon polygon() {
        return gf.createPolygon(linearRing(), null);
    }

    public static Element polygon(Document document, Node parent) {
        return polygon(document,parent,qName("Polygon"),false); 
    }
    
    public static Element polygon(Document document, Node parent, QName name, boolean withInterior) {
        Element polygon = element(name, document, parent);

        Element exterior = element(qName("exterior"), document, polygon);
        linearRing(document, exterior);
        
        if ( withInterior ) {
            Element interior = element(qName("interior"), document, polygon);
            linearRing(document,interior);
        }

        return polygon;
    }

    public static MultiPoint multiPoint() {
        return gf.createMultiPoint(new Coordinate[] { new Coordinate(1, 1), new Coordinate(2, 2) });
    }

    public static Element multiPoint(Document document, Node parent) {
        Element multiPoint = element(qName("MultiPoint"), document, parent);

        // 2 pointMember elements
        Element pointMember = element(qName("pointMember"), document, multiPoint);
        point(document, pointMember);

        pointMember = element(qName("pointMember"), document, multiPoint);
        point(document, pointMember);

        //1 pointMembers elmenet with 2 members
        Element pointMembers = element(qName("pointMembers"), document, multiPoint);
        point(document, pointMembers);
        point(document, pointMembers);

        return multiPoint;
    }

    public static MultiLineString multiLineString() {
        return gf.createMultiLineString(new LineString[] { lineString(), lineString() });
    }

    public static Element multiLineString(Document document, Node parent) {
        Element multiLineString = element(qName("MultiLineString"), document, parent);

        Element lineStringMember = element(qName("lineStringMember"), document, multiLineString);
        lineString(document, lineStringMember);

        lineStringMember = element(qName("lineStringMember"), document, multiLineString);
        lineString(document, lineStringMember);

        return multiLineString;
    }

    public static Element multiCurve(Document document, Node parent) {
        return multiCurve(document, parent, true);
    }
    
    public static Element multiCurve(Document document, Node parent, boolean useCurveMember) {
        Element multiCurve = element(qName("MultiCurve"), document, parent);

        if (useCurveMember) {
            Element curveMember = element(qName("curveMember"), document, multiCurve);
            lineString(document, curveMember);
    
            curveMember = element(qName("curveMember"), document, multiCurve);
            lineString(document, curveMember);
        }
        else {
            Element curveMembers = element(qName("curveMembers"), document, multiCurve);
            lineString(document, curveMembers);
            lineString(document, curveMembers);
        }

        return multiCurve;
    }
    
    public static MultiPolygon multiPolygon() {
        return gf.createMultiPolygon(new Polygon[] { polygon(), polygon() });
    }

    public static Element multiPolygon(Document document, Node parent) {
        Element multiPolygon = element(qName("MultiPolygon"), document, parent);

        Element polygonMember = element(qName("polygonMember"), document, multiPolygon);
        polygon(document, polygonMember);

        polygonMember = element(qName("polygonMember"), document, multiPolygon);
        polygon(document, polygonMember);

        return multiPolygon;
    }
    
    public static Element multiSurface(Document document, Node parent) {
        return multiSurface(document, parent, true);
    }
        
    public static Element multiSurface(Document document, Node parent, boolean useSurfaceMember) {
        Element multiSurface = element(qName("MultiSurface"), document, parent);

        if (useSurfaceMember) {
            Element surfaceMember = element(qName("surfaceMember"), document, multiSurface);
            polygon(document, surfaceMember);
    
            surfaceMember = element(qName("surfaceMember"), document, multiSurface);
            polygon(document, surfaceMember);
        }
        else {
            Element surfaceMembers = element(qName("surfaceMembers"), document, multiSurface);
            polygon(document, surfaceMembers);
            polygon(document, surfaceMembers);
        }

        return multiSurface;
    }
    
    
    public static GeometryCollection multiGeometry() {
        return gf.createGeometryCollection(new Geometry[]{point(),lineString(),polygon()});
    }
    
    public static Element multiGeometry(Document document, Node parent ) {
        Element multiGeometry = element(qName("MultiGeometry"), document, parent );
        
        Element geometryMember = element(qName("geometryMember"), document, multiGeometry);
        point(document,geometryMember);
        
        geometryMember = element(qName("geometryMember"), document, multiGeometry);
        lineString(document,geometryMember);
        
        geometryMember = element(qName("geometryMember"), document, multiGeometry);
        polygon(document,geometryMember);
        
        return multiGeometry;
    }

    public static Element surface(Document document, Node parent) {
        Element surface = element(qName("Surface"), document ,parent);
        Element patches = element(qName("patches"), document, surface);
        
        polygon(document,patches,qName("PolygonPatch"),true);
        
        return surface;
    }
    
    public static Element feature(Document document, Node parent) {
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

    public static Element featureMember(Document document, Node parent) {
        Element featureMember = element(qName("featureMember"), document, parent);
        feature(document, featureMember);

        return featureMember;
    }

    public static Element element(QName name, Document document, Node parent) {
        Element element = document.createElementNS(name.getNamespaceURI(), name.getLocalPart());

        if (parent != null) {
            parent.appendChild(element);
        }

        return element;
    }
    
    public static QName qName(String local) {
        return gml.qName(local);
    }
}
