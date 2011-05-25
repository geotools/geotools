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

import java.util.Date;

import javax.xml.namespace.QName;

import org.geotools.feature.simple.SimpleFeatureBuilder;
import org.geotools.feature.simple.SimpleFeatureTypeBuilder;
import org.geotools.gml2.GML;
import org.geotools.gml2.TEST;
import org.geotools.referencing.CRS;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.CoordinateSequence;
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
import com.vividsolutions.jts.geom.impl.CoordinateArraySequence;


/**
 * Collection of static methods for creating mock data for binding unit tests.
 *
 * @author Justin Deoliveira, The Open Planning Project
 *
 *
 *
 * @source $URL$
 */
public class GML2MockData {
    /** factory used to create geometries */
    static GeometryFactory gf = new GeometryFactory();

    //
    //Geometries
    //
    static Coordinate coordinate() {
        return new Coordinate(1, 2);
    }

    static Element coordinate(Document document, Node parent) {
        Element coord = element(GML.coord, document, parent);

        Element x = element(new QName(GML.NAMESPACE, "X"), document, coord);
        x.appendChild(document.createTextNode("1.0"));

        Element y = element(new QName(GML.NAMESPACE, "Y"), document, coord);
        y.appendChild(document.createTextNode("2.0"));

        return coord;
    }

    static CoordinateSequence coordinates() {
        return new CoordinateArraySequence(new Coordinate[] {
                new Coordinate(1, 2), new Coordinate(3, 4)
            });
    }

    static Element coordinates(Document document, Node parent) {
        Element coordinates = element(GML.coordinates, document, parent);
        coordinates.appendChild(document.createTextNode("1.0,2.0 3.0,4.0"));

        return coordinates;
    }

    static Element boundedBy(Document document, Node parent) {
        Element boundedBy = element(GML.boundedBy, document, parent);

        box(document, boundedBy);

        return boundedBy;
    }

    static Element boundedByWithNull(Document document, Node parent) {
        Element boundedBy = element(GML.boundedBy, document, parent);

        nil(document, boundedBy);

        return boundedBy;
    }

    static Element box(Document document, Node parent) {
        Element box = element(GML.Box, document, parent);

        coordinate(document, box);
        coordinate(document, box);

        return box;
    }

    static Element nil(Document document, Node parent) {
        return element(new QName(GML.NAMESPACE, "null"), document, parent);
    }

    static Point point() {
        return gf.createPoint(coordinate());
    }

    static Element point(Document document, Node parent) {
        Element point = element(GML.Point, document, parent);

        coordinate(document, point);

        return point;
    }

    static Element pointProperty(Document document, Node parent) {
        Element pointProperty = element(GML.pointProperty, document, parent);

        point(document, pointProperty);

        return pointProperty;
    }

    static LineString lineString() {
        return gf.createLineString(coordinates());
    }

    static Element lineString(Document document, Node parent) {
        Element lineString = element(GML.LineString, document, parent);

        coordinates(document, lineString);

        return lineString;
    }

    static LinearRing linearRing() {
        return gf.createLinearRing(new Coordinate[] {
                new Coordinate(1, 1), new Coordinate(2, 2), new Coordinate(3, 3),
                new Coordinate(1, 1)
            });
    }

    static Element lineStringProperty(Document document, Node parent) {
        Element property = element(GML.lineStringProperty, document, parent);

        lineString(document, property);

        return property;
    }

    static Element linearRing(Document document, Node parent) {
        Element linearRing = element(GML.LinearRing, document, parent);

        Element coordinates = element(GML.coordinates, document, linearRing);
        coordinates.appendChild(document.createTextNode("1.0,2.0 3.0,4.0 5.0,6.0 1.0,2.0"));

        return linearRing;
    }

    static Polygon polygon() {
        return gf.createPolygon(linearRing(), null);
    }

    static Element polygon(Document document, Node parent) {
        Element polygon = element(GML.Polygon, document, parent);

        Element exterior = element(GML.outerBoundaryIs, document, polygon);
        linearRing(document, exterior);

        return polygon;
    }

    static Element polygonProperty(Document document, Node parent) {
        Element property = element(GML.polygonProperty, document, parent);

        polygon(document, property);

        return property;
    }

    static MultiPoint multiPoint() {
        return setCRS(gf.createMultiPoint(new Coordinate[] { new Coordinate(1, 1), new Coordinate(2, 2) }));
    }

    static Element multiPoint(Document document, Node parent) {
        Element multiPoint = element(GML.MultiPoint, document, parent);

        // 2 pointMember elements
        Element pointMember = element(GML.pointMember, document, multiPoint);
        point(document, pointMember);

        pointMember = element(GML.pointMember, document, multiPoint);
        point(document, pointMember);

        return multiPoint;
    }
    
    static <G extends Geometry> G setCRS( G geometry ) {
        geometry.setUserData( crs("4326") );
        return geometry;
    }

    static CoordinateReferenceSystem crs( String srid ) {
        try {
            return CRS.decode( "EPSG:4326" );
        } 
        catch (Exception e) {
            throw new RuntimeException( e );
        }
    }
    
    static Element multiPointProperty(Document document, Node parent) {
        Element multiPointProperty = element(GML.multiPointProperty, document, parent);
        multiPoint(document, multiPointProperty);

        return multiPointProperty;
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

    static Element multiLineStringProperty(Document document, Node parent) {
        Element multiLineStringProperty = element(GML.multiLineStringProperty, document, parent);
        multiLineString(document, multiLineStringProperty);

        return multiLineStringProperty;
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

    static Element multiPolygonProperty(Document document, Node parent) {
        Element multiPolygonProperty = element(GML.multiPolygonProperty, document, parent);
        multiPolygon(document, multiPolygonProperty);

        return multiPolygonProperty;
    }

    static GeometryCollection multiGeometry() {
        return gf.createGeometryCollection(new Geometry[]{point(), lineString(), polygon()});
    }
    //
    // features
    //
    static Element feature(Document document, Node parent) {
        Element feature = element(TEST.TestFeature, document, parent);
        Element geom = element(new QName(TEST.NAMESPACE, "geom"), document, feature);
        point(document, geom);

        Element count = element(new QName(TEST.NAMESPACE, "count"), document, feature);
        count.appendChild(document.createTextNode("1"));

        return feature;
    }

    static SimpleFeature feature() throws Exception {
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
