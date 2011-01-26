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
package org.geotools.gml2;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import org.geotools.feature.NameImpl;
import org.geotools.feature.type.AttributeDescriptorImpl;
import org.geotools.feature.type.AttributeTypeImpl;
import org.geotools.feature.type.FeatureTypeImpl;
import org.geotools.feature.type.ProfileImpl;
import org.geotools.feature.type.SchemaImpl;
import org.geotools.gml2.GML;
import org.geotools.xs.XSSchema;
import org.opengis.feature.Feature;
import org.opengis.feature.type.AttributeType;
import org.opengis.feature.type.FeatureType;
import org.opengis.feature.type.Name;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.CoordinateSequence;
import com.vividsolutions.jts.geom.Envelope;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryCollection;
import com.vividsolutions.jts.geom.LineString;
import com.vividsolutions.jts.geom.LinearRing;
import com.vividsolutions.jts.geom.MultiLineString;
import com.vividsolutions.jts.geom.MultiPoint;
import com.vividsolutions.jts.geom.MultiPolygon;
import com.vividsolutions.jts.geom.Point;
import com.vividsolutions.jts.geom.Polygon;


public class GMLSchema extends SchemaImpl {
    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;complexType abstract="true" name="AbstractGeometryType"&gt;
     *      &lt;annotation&gt;
     *          &lt;documentation&gt;
     *          All geometry elements are derived from this abstract supertype;
     *          a geometry element may have an identifying attribute (gid).
     *          It may be associated with a spatial reference system.
     *        &lt;/documentation&gt;
     *      &lt;/annotation&gt;
     *      &lt;complexContent&gt;
     *          &lt;restriction base="anyType"&gt;
     *              &lt;attribute name="gid" type="ID" use="optional"/&gt;
     *              &lt;attribute name="srsName" type="anyURI" use="optional"/&gt;
     *          &lt;/restriction&gt;
     *      &lt;/complexContent&gt;
     *  &lt;/complexType&gt;
     *
     *    </code>
     *   </pre>
     * </p>
     *
     * @generated
     */
    public static final AttributeType ABSTRACTGEOMETRYTYPE_TYPE = new AttributeTypeImpl(new NameImpl(
                "http://www.opengis.net/gml", "AbstractGeometryType"), Geometry.class, false, true,
            Collections.EMPTY_LIST, XSSchema.ANYTYPE_TYPE, null);

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;complexType abstract="true" name="AbstractGeometryCollectionBaseType"&gt;
     *      &lt;annotation&gt;
     *          &lt;documentation&gt;
     *          This abstract base type for geometry collections just makes the
     *          srsName attribute mandatory.
     *        &lt;/documentation&gt;
     *      &lt;/annotation&gt;
     *      &lt;complexContent&gt;
     *          &lt;restriction base="gml:AbstractGeometryType"&gt;
     *              &lt;attribute name="gid" type="ID" use="optional"/&gt;
     *              &lt;attribute name="srsName" type="anyURI" use="required"/&gt;
     *          &lt;/restriction&gt;
     *      &lt;/complexContent&gt;
     *  &lt;/complexType&gt;
     *
     *    </code>
     *   </pre>
     * </p>
     *
     * @generated
     */
    public static final AttributeType ABSTRACTGEOMETRYCOLLECTIONBASETYPE_TYPE = new AttributeTypeImpl(new NameImpl(
                "http://www.opengis.net/gml", "AbstractGeometryCollectionBaseType"),
            GeometryCollection.class, false, true, Collections.EMPTY_LIST,
            ABSTRACTGEOMETRYTYPE_TYPE, null);

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;complexType name="GeometryAssociationType"&gt;
     *      &lt;annotation&gt;
     *          &lt;documentation&gt;
     *          An instance of this type (e.g. a geometryMember) can either
     *          enclose or point to a primitive geometry element. When serving
     *          as a simple link that references a remote geometry instance,
     *          the value of the gml:remoteSchema attribute can be used to
     *          locate a schema fragment that constrains the target instance.
     *        &lt;/documentation&gt;
     *      &lt;/annotation&gt;
     *      &lt;sequence minOccurs="0"&gt;
     *          &lt;element ref="gml:_Geometry"/&gt;
     *      &lt;/sequence&gt;optional
     *
     *      &lt;!-- &lt;attributeGroup ref="gml:AssociationAttributeGroup"/&gt; --&gt;
     *      &lt;attributeGroup ref="xlink:simpleLink"/&gt;
     *      &lt;attribute ref="gml:remoteSchema" use="optional"/&gt;
     *  &lt;/complexType&gt;
     *
     *    </code>
     *   </pre>
     * </p>
     *
     * @generated
     */
    public static final AttributeType GEOMETRYASSOCIATIONTYPE_TYPE = new AttributeTypeImpl(new NameImpl(
                "http://www.opengis.net/gml", "GeometryAssociationType"), Geometry.class, false,
            false, Collections.EMPTY_LIST, XSSchema.ANYTYPE_TYPE, null);

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;complexType name="GeometryCollectionType"&gt;
     *      &lt;annotation&gt;
     *          &lt;documentation&gt;
     *          A geometry collection must include one or more geometries, referenced
     *          through geometryMember elements. User-defined geometry collections
     *          that accept GML geometry classes as members must instantiate--or
     *          derive from--this type.
     *        &lt;/documentation&gt;
     *      &lt;/annotation&gt;
     *      &lt;complexContent&gt;
     *          &lt;extension base="gml:AbstractGeometryCollectionBaseType"&gt;
     *              &lt;sequence&gt;
     *                  &lt;element maxOccurs="unbounded" ref="gml:geometryMember"/&gt;
     *              &lt;/sequence&gt;
     *          &lt;/extension&gt;
     *      &lt;/complexContent&gt;
     *  &lt;/complexType&gt;
     *
     *    </code>
     *   </pre>
     * </p>
     *
     * @generated
     */
    public static final AttributeType GEOMETRYCOLLECTIONTYPE_TYPE = new AttributeTypeImpl(new NameImpl(
                "http://www.opengis.net/gml", "GeometryCollectionType"), GeometryCollection.class,
            false, false, Collections.EMPTY_LIST, ABSTRACTGEOMETRYCOLLECTIONBASETYPE_TYPE, null);

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;complexType name="CoordType"&gt;
     *      &lt;annotation&gt;
     *          &lt;documentation&gt;
     *          Represents a coordinate tuple in one, two, or three dimensions.
     *        &lt;/documentation&gt;
     *      &lt;/annotation&gt;
     *      &lt;sequence&gt;
     *          &lt;element name="X" type="decimal"/&gt;
     *          &lt;element minOccurs="0" name="Y" type="decimal"/&gt;
     *          &lt;element minOccurs="0" name="Z" type="decimal"/&gt;
     *      &lt;/sequence&gt;
     *  &lt;/complexType&gt;
     *
     *    </code>
     *   </pre>
     * </p>
     *
     * @generated
     */
    public static final AttributeType COORDTYPE_TYPE = new AttributeTypeImpl(new NameImpl(
                "http://www.opengis.net/gml", "CoordType"), Coordinate.class, false, false,
            Collections.EMPTY_LIST, XSSchema.ANYTYPE_TYPE, null);

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;complexType name="CoordinatesType"&gt;
     *      &lt;annotation&gt;
     *          &lt;documentation&gt;
     *          Coordinates can be included in a single string, but there is no
     *          facility for validating string content. The value of the 'cs' attribute
     *          is the separator for coordinate values, and the value of the 'ts'
     *          attribute gives the tuple separator (a single space by default); the
     *          default values may be changed to reflect local usage.
     *        &lt;/documentation&gt;
     *      &lt;/annotation&gt;
     *      &lt;simpleContent&gt;
     *          &lt;extension base="string"&gt;
     *              &lt;attribute default="." name="decimal" type="string" use="optional"/&gt;
     *              &lt;attribute default="," name="cs" type="string" use="optional"/&gt;
     *              &lt;attribute default=" " name="ts" type="string" use="optional"/&gt;
     *          &lt;/extension&gt;
     *      &lt;/simpleContent&gt;
     *  &lt;/complexType&gt;
     *
     *    </code>
     *   </pre>
     * </p>
     *
     * @generated
     */
    public static final AttributeType COORDINATESTYPE_TYPE = new AttributeTypeImpl(new NameImpl(
                "http://www.opengis.net/gml", "CoordinatesType"), CoordinateSequence.class, false,
            false, Collections.EMPTY_LIST, XSSchema.STRING_TYPE, null);

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;complexType name="LinearRingType"&gt;
     *      &lt;annotation&gt;
     *          &lt;documentation&gt;
     *          A LinearRing is defined by four or more coordinate tuples, with
     *          linear interpolation between them; the first and last coordinates
     *          must be coincident.
     *        &lt;/documentation&gt;
     *      &lt;/annotation&gt;
     *      &lt;complexContent&gt;
     *          &lt;extension base="gml:AbstractGeometryType"&gt;
     *              &lt;sequence&gt;
     *                  &lt;choice&gt;
     *                      &lt;element maxOccurs="unbounded" minOccurs="4" ref="gml:coord"/&gt;
     *                      &lt;element ref="gml:coordinates"/&gt;
     *                  &lt;/choice&gt;
     *              &lt;/sequence&gt;
     *          &lt;/extension&gt;
     *      &lt;/complexContent&gt;
     *  &lt;/complexType&gt;
     *
     *    </code>
     *   </pre>
     * </p>
     *
     * @generated
     */
    public static final AttributeType LINEARRINGTYPE_TYPE = new AttributeTypeImpl(new NameImpl(
                "http://www.opengis.net/gml", "LinearRingType"), LinearRing.class, false, false,
            Collections.EMPTY_LIST, ABSTRACTGEOMETRYTYPE_TYPE, null);

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;complexType name="LinearRingMemberType"&gt;
     *      &lt;annotation&gt;
     *          &lt;documentation&gt;Restricts the outer or inner boundary of a polygon instance
     *              to being a LinearRing.&lt;/documentation&gt;
     *      &lt;/annotation&gt;
     *      &lt;complexContent&gt;
     *          &lt;restriction base="gml:GeometryAssociationType"&gt;
     *              &lt;sequence minOccurs="0"&gt;
     *                  &lt;element ref="gml:LinearRing"/&gt;
     *              &lt;/sequence&gt;
     *              &lt;attributeGroup ref="gml:AssociationAttributeGroup"/&gt;
     *          &lt;/restriction&gt;
     *      &lt;/complexContent&gt;
     *  &lt;/complexType&gt;
     *
     *    </code>
     *   </pre>
     * </p>
     *
     * @generated
     */
    public static final AttributeType LINEARRINGMEMBERTYPE_TYPE = new AttributeTypeImpl(new NameImpl(
                "http://www.opengis.net/gml", "LinearRingMemberType"), LinearRing.class, false,
            false, Collections.EMPTY_LIST, GEOMETRYASSOCIATIONTYPE_TYPE, null);

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;complexType name="PolygonType"&gt;
     *      &lt;annotation&gt;
     *          &lt;documentation&gt;
     *          A Polygon is defined by an outer boundary and zero or more inner
     *          boundaries which are in turn defined by LinearRings.
     *        &lt;/documentation&gt;
     *      &lt;/annotation&gt;
     *      &lt;complexContent&gt;
     *          &lt;extension base="gml:AbstractGeometryType"&gt;
     *              &lt;sequence&gt;
     *                  &lt;element ref="gml:outerBoundaryIs"/&gt;
     *                  &lt;element maxOccurs="unbounded" minOccurs="0" ref="gml:innerBoundaryIs"/&gt;
     *              &lt;/sequence&gt;
     *          &lt;/extension&gt;
     *      &lt;/complexContent&gt;
     *  &lt;/complexType&gt;
     *
     *    </code>
     *   </pre>
     * </p>
     *
     * @generated
     */
    public static final AttributeType POLYGONTYPE_TYPE = new AttributeTypeImpl(new NameImpl(
                "http://www.opengis.net/gml", "PolygonType"), Polygon.class, false, false,
            Collections.EMPTY_LIST, ABSTRACTGEOMETRYTYPE_TYPE, null);

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;complexType name="PolygonMemberType"&gt;
     *      &lt;annotation&gt;
     *          &lt;documentation&gt;Restricts the geometry member to being a Polygon instance.&lt;/documentation&gt;
     *      &lt;/annotation&gt;
     *      &lt;complexContent&gt;
     *          &lt;restriction base="gml:GeometryAssociationType"&gt;
     *              &lt;sequence minOccurs="0"&gt;
     *                  &lt;element ref="gml:Polygon"/&gt;
     *              &lt;/sequence&gt;
     *              &lt;attributeGroup ref="gml:AssociationAttributeGroup"/&gt;
     *          &lt;/restriction&gt;
     *      &lt;/complexContent&gt;
     *  &lt;/complexType&gt;
     *
     *    </code>
     *   </pre>
     * </p>
     *
     * @generated
     */
    public static final AttributeType POLYGONMEMBERTYPE_TYPE = new AttributeTypeImpl(new NameImpl(
                "http://www.opengis.net/gml", "PolygonMemberType"), Polygon.class, false, false,
            Collections.EMPTY_LIST, GEOMETRYASSOCIATIONTYPE_TYPE, null);

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;complexType name="MultiPolygonType"&gt;
     *      &lt;annotation&gt;
     *          &lt;documentation&gt;
     *          A MultiPolygon is defined by one or more Polygons, referenced through
     *          polygonMember elements.
     *        &lt;/documentation&gt;
     *      &lt;/annotation&gt;
     *      &lt;complexContent&gt;
     *          &lt;restriction base="gml:GeometryCollectionType"&gt;
     *              &lt;sequence&gt;
     *                  &lt;element maxOccurs="unbounded" ref="gml:polygonMember"/&gt;
     *              &lt;/sequence&gt;
     *              &lt;attribute name="gid" type="ID" use="optional"/&gt;
     *              &lt;attribute name="srsName" type="anyURI" use="required"/&gt;
     *          &lt;/restriction&gt;
     *      &lt;/complexContent&gt;
     *  &lt;/complexType&gt;
     *
     *    </code>
     *   </pre>
     * </p>
     *
     * @generated
     */
    public static final AttributeType MULTIPOLYGONTYPE_TYPE = new AttributeTypeImpl(new NameImpl(
                "http://www.opengis.net/gml", "MultiPolygonType"), MultiPolygon.class, false,
            false, Collections.EMPTY_LIST, GEOMETRYCOLLECTIONTYPE_TYPE, null);

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;complexType name="MultiPolygonPropertyType"&gt;
     *      &lt;annotation&gt;
     *          &lt;documentation&gt;
     *          Encapsulates a MultiPolygon to represent the following discontiguous
     *          geometric properties: multiCoverage, multiExtentOf.
     *        &lt;/documentation&gt;
     *      &lt;/annotation&gt;
     *      &lt;complexContent&gt;
     *          &lt;restriction base="gml:GeometryAssociationType"&gt;
     *              &lt;sequence minOccurs="0"&gt;
     *                  &lt;element ref="gml:MultiPolygon"/&gt;
     *              &lt;/sequence&gt;
     *              &lt;attributeGroup ref="xlink:simpleLink"/&gt;
     *              &lt;attribute ref="gml:remoteSchema" use="optional"/&gt;
     *          &lt;/restriction&gt;
     *      &lt;/complexContent&gt;
     *  &lt;/complexType&gt;
     *
     *    </code>
     *   </pre>
     * </p>
     *
     * @generated
     */
    public static final AttributeType MULTIPOLYGONPROPERTYTYPE_TYPE = new AttributeTypeImpl(new NameImpl(
                "http://www.opengis.net/gml", "MultiPolygonPropertyType"), MultiPolygon.class,
            false, false, Collections.EMPTY_LIST, GEOMETRYASSOCIATIONTYPE_TYPE, null);

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;complexType name="MultiGeometryPropertyType"&gt;
     *      &lt;annotation&gt;
     *          &lt;documentation&gt;Encapsulates a MultiGeometry element.&lt;/documentation&gt;
     *      &lt;/annotation&gt;
     *      &lt;complexContent&gt;
     *          &lt;restriction base="gml:GeometryAssociationType"&gt;
     *              &lt;sequence minOccurs="0"&gt;
     *                  &lt;element ref="gml:MultiGeometry"/&gt;
     *              &lt;/sequence&gt;
     *              &lt;attributeGroup ref="xlink:simpleLink"/&gt;
     *              &lt;attribute ref="gml:remoteSchema" use="optional"/&gt;
     *          &lt;/restriction&gt;
     *      &lt;/complexContent&gt;
     *  &lt;/complexType&gt;
     *
     *    </code>
     *   </pre>
     * </p>
     *
     * @generated
     */
    public static final AttributeType MULTIGEOMETRYPROPERTYTYPE_TYPE = new AttributeTypeImpl(new NameImpl(
                "http://www.opengis.net/gml", "MultiGeometryPropertyType"), MultiPolygon.class,
            false, false, Collections.EMPTY_LIST, GEOMETRYASSOCIATIONTYPE_TYPE, null);

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;complexType name="LineStringType"&gt;
     *      &lt;annotation&gt;
     *          &lt;documentation&gt;
     *          A LineString is defined by two or more coordinate tuples, with
     *          linear interpolation between them.
     *        &lt;/documentation&gt;
     *      &lt;/annotation&gt;
     *      &lt;complexContent&gt;
     *          &lt;extension base="gml:AbstractGeometryType"&gt;
     *              &lt;sequence&gt;
     *                  &lt;choice&gt;
     *                      &lt;element maxOccurs="unbounded" minOccurs="2" ref="gml:coord"/&gt;
     *                      &lt;element ref="gml:coordinates"/&gt;
     *                  &lt;/choice&gt;
     *              &lt;/sequence&gt;
     *          &lt;/extension&gt;
     *      &lt;/complexContent&gt;
     *  &lt;/complexType&gt;
     *
     *    </code>
     *   </pre>
     * </p>
     *
     * @generated
     */
    public static final AttributeType LINESTRINGTYPE_TYPE = new AttributeTypeImpl(new NameImpl(
                "http://www.opengis.net/gml", "LineStringType"), LineString.class, false, false,
            Collections.EMPTY_LIST, ABSTRACTGEOMETRYTYPE_TYPE, null);

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;complexType name="LineStringMemberType"&gt;
     *      &lt;annotation&gt;
     *          &lt;documentation&gt;Restricts the geometry member to being a LineString instance.&lt;/documentation&gt;
     *      &lt;/annotation&gt;
     *      &lt;complexContent&gt;
     *          &lt;restriction base="gml:GeometryAssociationType"&gt;
     *              &lt;sequence minOccurs="0"&gt;
     *                  &lt;element ref="gml:LineString"/&gt;
     *              &lt;/sequence&gt;
     *              &lt;attributeGroup ref="gml:AssociationAttributeGroup"/&gt;
     *          &lt;/restriction&gt;
     *      &lt;/complexContent&gt;
     *  &lt;/complexType&gt;
     *
     *    </code>
     *   </pre>
     * </p>
     *
     * @generated
     */
    public static final AttributeType LINESTRINGMEMBERTYPE_TYPE = new AttributeTypeImpl(new NameImpl(
                "http://www.opengis.net/gml", "LineStringMemberType"), LineString.class, false,
            false, Collections.EMPTY_LIST, GEOMETRYASSOCIATIONTYPE_TYPE, null);

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;complexType name="MultiLineStringType"&gt;
     *      &lt;annotation&gt;
     *          &lt;documentation&gt;
     *          A MultiLineString is defined by one or more LineStrings, referenced
     *          through lineStringMember elements.
     *        &lt;/documentation&gt;
     *      &lt;/annotation&gt;
     *      &lt;complexContent&gt;
     *          &lt;restriction base="gml:GeometryCollectionType"&gt;
     *              &lt;sequence&gt;
     *                  &lt;element maxOccurs="unbounded" ref="gml:lineStringMember"/&gt;
     *              &lt;/sequence&gt;
     *              &lt;attribute name="gid" type="ID" use="optional"/&gt;
     *              &lt;attribute name="srsName" type="anyURI" use="required"/&gt;
     *          &lt;/restriction&gt;
     *      &lt;/complexContent&gt;
     *  &lt;/complexType&gt;
     *
     *    </code>
     *   </pre>
     * </p>
     *
     * @generated
     */
    public static final AttributeType MULTILINESTRINGTYPE_TYPE = new AttributeTypeImpl(new NameImpl(
                "http://www.opengis.net/gml", "MultiLineStringType"), MultiLineString.class, false,
            false, Collections.EMPTY_LIST, GEOMETRYCOLLECTIONTYPE_TYPE, null);

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;complexType name="MultiLineStringPropertyType"&gt;
     *      &lt;annotation&gt;
     *          &lt;documentation&gt;
     *          Encapsulates a MultiLineString element to represent the following
     *          discontiguous geometric properties: multiEdgeOf, multiCenterLineOf.
     *        &lt;/documentation&gt;
     *      &lt;/annotation&gt;
     *      &lt;complexContent&gt;
     *          &lt;restriction base="gml:GeometryAssociationType"&gt;
     *              &lt;sequence minOccurs="0"&gt;
     *                  &lt;element ref="gml:MultiLineString"/&gt;
     *              &lt;/sequence&gt;
     *              &lt;attributeGroup ref="xlink:simpleLink"/&gt;
     *              &lt;attribute ref="gml:remoteSchema" use="optional"/&gt;
     *          &lt;/restriction&gt;
     *      &lt;/complexContent&gt;
     *  &lt;/complexType&gt;
     *
     *    </code>
     *   </pre>
     * </p>
     *
     * @generated
     */
    public static final AttributeType MULTILINESTRINGPROPERTYTYPE_TYPE = new AttributeTypeImpl(new NameImpl(
                "http://www.opengis.net/gml", "MultiLineStringPropertyType"),
            MultiLineString.class, false, false, Collections.EMPTY_LIST,
            GEOMETRYASSOCIATIONTYPE_TYPE, null);

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;complexType name="BoxType"&gt;
     *      &lt;annotation&gt;
     *          &lt;documentation&gt;
     *          The Box structure defines an extent using a pair of coordinate tuples.
     *        &lt;/documentation&gt;
     *      &lt;/annotation&gt;
     *      &lt;complexContent&gt;
     *          &lt;extension base="gml:AbstractGeometryType"&gt;
     *              &lt;sequence&gt;
     *                  &lt;choice&gt;
     *                      &lt;element maxOccurs="2" minOccurs="2" ref="gml:coord"/&gt;
     *                      &lt;element ref="gml:coordinates"/&gt;
     *                  &lt;/choice&gt;
     *              &lt;/sequence&gt;
     *          &lt;/extension&gt;
     *      &lt;/complexContent&gt;
     *  &lt;/complexType&gt;
     *
     *    </code>
     *   </pre>
     * </p>
     *
     * @generated
     */
    public static final AttributeType BOXTYPE_TYPE = new AttributeTypeImpl(new NameImpl(
                "http://www.opengis.net/gml", "BoxType"), Envelope.class, false, false,
            Collections.EMPTY_LIST, ABSTRACTGEOMETRYTYPE_TYPE, null);

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;simpleType name="NullType"&gt;
     *      &lt;annotation&gt;
     *          &lt;documentation&gt;
     *          If a bounding shape is not provided for a feature collection,
     *          explain why. Allowable values are:
     *          innapplicable - the features do not have geometry
     *          unknown - the boundingBox cannot be computed
     *          unavailable - there may be a boundingBox but it is not divulged
     *          missing - there are no features
     *        &lt;/documentation&gt;
     *      &lt;/annotation&gt;
     *      &lt;restriction base="string"&gt;
     *          &lt;enumeration value="inapplicable"/&gt;
     *          &lt;enumeration value="unknown"/&gt;
     *          &lt;enumeration value="unavailable"/&gt;
     *          &lt;enumeration value="missing"/&gt;
     *      &lt;/restriction&gt;
     *  &lt;/simpleType&gt;
     *
     *    </code>
     *   </pre>
     * </p>
     *
     * @generated
     */
    public static final AttributeType NULLTYPE_TYPE = new AttributeTypeImpl(new NameImpl(
                "http://www.opengis.net/gml", "NullType"), java.lang.Object.class, false, false,
            Collections.EMPTY_LIST, XSSchema.STRING_TYPE, null);

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;complexType name="BoundingShapeType"&gt;
     *      &lt;annotation&gt;
     *          &lt;documentation&gt;
     *          Bounding shapes--a Box or a null element are currently allowed.
     *        &lt;/documentation&gt;
     *      &lt;/annotation&gt;
     *      &lt;sequence&gt;
     *          &lt;choice&gt;
     *              &lt;element ref="gml:Box"/&gt;
     *              &lt;element name="null" type="gml:NullType"/&gt;
     *          &lt;/choice&gt;
     *      &lt;/sequence&gt;
     *  &lt;/complexType&gt;
     *
     *    </code>
     *   </pre>
     * </p>
     *
     * @generated
     */
    public static final AttributeType BOUNDINGSHAPETYPE_TYPE = new AttributeTypeImpl(new NameImpl(
                "http://www.opengis.net/gml", "BoundingShapeType"), Envelope.class, false, false,
            Collections.EMPTY_LIST, XSSchema.ANYTYPE_TYPE, null);

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;complexType abstract="true" name="AbstractFeatureType"&gt;
     *      &lt;annotation&gt;
     *          &lt;documentation&gt;
     *          An abstract feature provides a set of common properties. A concrete
     *          feature type must derive from this type and specify additional
     *          properties in an application schema. A feature may optionally
     *          possess an identifying attribute ('fid').
     *        &lt;/documentation&gt;
     *      &lt;/annotation&gt;
     *      &lt;sequence&gt;
     *          &lt;element minOccurs="0" ref="gml:description"/&gt;
     *          &lt;element minOccurs="0" ref="gml:name"/&gt;
     *          &lt;element minOccurs="0" ref="gml:boundedBy"/&gt;
     *          &lt;!-- additional properties must be specified in an application schema --&gt;
     *      &lt;/sequence&gt;
     *      &lt;attribute name="fid" type="ID" use="optional"/&gt;
     *  &lt;/complexType&gt;
     *
     *    </code>
     *   </pre>
     * </p>
     *
     * @generated
     */
    private static List ABSTRACTFEATURETYPE_TYPE_schema = new ArrayList();

    static {
        ABSTRACTFEATURETYPE_TYPE_schema.add(new AttributeDescriptorImpl(XSSchema.STRING_TYPE,
                new NameImpl("http://www.opengis.net/gml", "description"), 1, 1, false, null));
        ABSTRACTFEATURETYPE_TYPE_schema.add(new AttributeDescriptorImpl(XSSchema.STRING_TYPE,
                new NameImpl("http://www.opengis.net/gml", "name"), 1, 1, false, null));
        ABSTRACTFEATURETYPE_TYPE_schema.add(new AttributeDescriptorImpl(BOUNDINGSHAPETYPE_TYPE,
                new NameImpl("http://www.opengis.net/gml", "boundedBy"), 1, 1, false, null));
    }

    public static final FeatureType ABSTRACTFEATURETYPE_TYPE = new FeatureTypeImpl(new NameImpl(
                "http://www.opengis.net/gml", "AbstractFeatureType"),
            ABSTRACTFEATURETYPE_TYPE_schema, null, true, Collections.EMPTY_LIST,
            XSSchema.ANYTYPE_TYPE, null);

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;complexType abstract="true" name="AbstractFeatureCollectionBaseType"&gt;
     *      &lt;annotation&gt;
     *          &lt;documentation&gt;
     *          This abstract base type just makes the boundedBy element mandatory
     *          for a feature collection.
     *        &lt;/documentation&gt;
     *      &lt;/annotation&gt;
     *      &lt;complexContent&gt;
     *          &lt;restriction base="gml:AbstractFeatureType"&gt;
     *              &lt;sequence&gt;
     *                  &lt;element minOccurs="0" ref="gml:description"/&gt;
     *                  &lt;element minOccurs="0" ref="gml:name"/&gt;
     *                  &lt;element ref="gml:boundedBy"/&gt;
     *              &lt;/sequence&gt;
     *              &lt;attribute name="fid" type="ID" use="optional"/&gt;
     *          &lt;/restriction&gt;
     *      &lt;/complexContent&gt;
     *  &lt;/complexType&gt;
     *
     *    </code>
     *   </pre>
     * </p>
     *
     * @generated
     */
    private static List ABSTRACTFEATURECOLLECTIONBASETYPE_TYPE_schema = new ArrayList();

    static {
        ABSTRACTFEATURECOLLECTIONBASETYPE_TYPE_schema.add(new AttributeDescriptorImpl(
                XSSchema.STRING_TYPE, new NameImpl("http://www.opengis.net/gml", "description"), 1, 1,
                false, null));
        ABSTRACTFEATURECOLLECTIONBASETYPE_TYPE_schema.add(new AttributeDescriptorImpl(
                XSSchema.STRING_TYPE, new NameImpl("http://www.opengis.net/gml", "name"), 1, 1, false,
                null));
        ABSTRACTFEATURECOLLECTIONBASETYPE_TYPE_schema.add(new AttributeDescriptorImpl(
                BOUNDINGSHAPETYPE_TYPE, new NameImpl("http://www.opengis.net/gml", "boundedBy"), 1, 1,
                false, null));
    }

    public static final FeatureType ABSTRACTFEATURECOLLECTIONBASETYPE_TYPE = new FeatureTypeImpl(new NameImpl(
                "http://www.opengis.net/gml", "AbstractFeatureCollectionBaseType"),
            ABSTRACTFEATURECOLLECTIONBASETYPE_TYPE_schema, null, true, Collections.EMPTY_LIST,
            ABSTRACTFEATURETYPE_TYPE, null);

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;complexType name="FeatureAssociationType"&gt;
     *      &lt;annotation&gt;
     *          &lt;documentation&gt;
     *          An instance of this type (e.g. a featureMember) can either
     *          enclose or point to a feature (or feature collection); this
     *          type can be restricted in an application schema to allow only
     *          specified features as valid participants in the association.
     *          When serving as a simple link that references a remote feature
     *          instance, the value of the gml:remoteSchema attribute can be
     *          used to locate a schema fragment that constrains the target
     *          instance.
     *        &lt;/documentation&gt;
     *      &lt;/annotation&gt;
     *      &lt;sequence minOccurs="0"&gt;
     *          &lt;element ref="gml:_Feature"/&gt;
     *      &lt;/sequence&gt;
     *      &lt;attributeGroup ref="xlink:simpleLink"/&gt;
     *      &lt;attribute ref="gml:remoteSchema" use="optional"/&gt;
     *  &lt;/complexType&gt;
     *
     *    </code>
     *   </pre>
     * </p>
     *
     * @generated
     */
    public static final AttributeType FEATUREASSOCIATIONTYPE_TYPE = new AttributeTypeImpl(new NameImpl(
                "http://www.opengis.net/gml", "FeatureAssociationType"), Feature.class, false,
            false, Collections.EMPTY_LIST, XSSchema.ANYTYPE_TYPE, null);

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;complexType abstract="true" name="AbstractFeatureCollectionType"&gt;
     *      &lt;annotation&gt;
     *          &lt;documentation&gt;
     *          A feature collection contains zero or more featureMember elements.
     *        &lt;/documentation&gt;
     *      &lt;/annotation&gt;
     *      &lt;complexContent&gt;
     *          &lt;extension base="gml:AbstractFeatureCollectionBaseType"&gt;
     *              &lt;sequence&gt;
     *                  &lt;element maxOccurs="unbounded" minOccurs="0" ref="gml:featureMember"/&gt;
     *              &lt;/sequence&gt;
     *          &lt;/extension&gt;
     *      &lt;/complexContent&gt;
     *  &lt;/complexType&gt;
     *
     *    </code>
     *   </pre>
     * </p>
     *
     * @generated
     */
    private static List ABSTRACTFEATURECOLLECTIONTYPE_TYPE_schema = new ArrayList();

    static {
        ABSTRACTFEATURECOLLECTIONTYPE_TYPE_schema.add(new AttributeDescriptorImpl(
                XSSchema.STRING_TYPE, new NameImpl("http://www.opengis.net/gml", "description"), 1, 1,
                false, null));
        ABSTRACTFEATURECOLLECTIONTYPE_TYPE_schema.add(new AttributeDescriptorImpl(
                XSSchema.STRING_TYPE, new NameImpl("http://www.opengis.net/gml", "name"), 1, 1, false,
                null));
        ABSTRACTFEATURECOLLECTIONTYPE_TYPE_schema.add(new AttributeDescriptorImpl(
                BOUNDINGSHAPETYPE_TYPE, new NameImpl("http://www.opengis.net/gml", "boundedBy"), 1, 1,
                false, null));
        ABSTRACTFEATURECOLLECTIONTYPE_TYPE_schema.add(new AttributeDescriptorImpl(
                FEATUREASSOCIATIONTYPE_TYPE,
                new NameImpl("http://www.opengis.net/gml", "featureMember"), 1, 1, false, null));
    }

    public static final FeatureType ABSTRACTFEATURECOLLECTIONTYPE_TYPE = new FeatureTypeImpl(new NameImpl(
                "http://www.opengis.net/gml", "AbstractFeatureCollectionType"),
            ABSTRACTFEATURECOLLECTIONTYPE_TYPE_schema, null, true, Collections.EMPTY_LIST,
            ABSTRACTFEATURECOLLECTIONBASETYPE_TYPE, null);

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;complexType name="GeometryPropertyType"&gt;
     *      &lt;annotation&gt;
     *          &lt;documentation&gt;
     *          A simple geometry property encapsulates a geometry element.
     *          Alternatively, it can function as a pointer (simple-type link)
     *          that refers to a remote geometry element.
     *        &lt;/documentation&gt;
     *      &lt;/annotation&gt;
     *      &lt;sequence minOccurs="0"&gt;
     *          &lt;element ref="gml:_Geometry"/&gt;
     *      &lt;/sequence&gt;
     *      &lt;attributeGroup ref="xlink:simpleLink"/&gt;
     *      &lt;attribute ref="gml:remoteSchema" use="optional"/&gt;
     *  &lt;/complexType&gt;
     *
     *    </code>
     *   </pre>
     * </p>
     *
     * @generated
     */
    public static final AttributeType GEOMETRYPROPERTYTYPE_TYPE = new AttributeTypeImpl(new NameImpl(
                "http://www.opengis.net/gml", "GeometryPropertyType"), Geometry.class, false,
            false, Collections.EMPTY_LIST, XSSchema.ANYTYPE_TYPE, null);

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;complexType name="PolygonPropertyType"&gt;
     *      &lt;annotation&gt;
     *          &lt;documentation&gt;
     *          Encapsulates a single polygon to represent coverage or extentOf
     *          properties.
     *        &lt;/documentation&gt;
     *      &lt;/annotation&gt;
     *      &lt;complexContent&gt;
     *          &lt;restriction base="gml:GeometryAssociationType"&gt;
     *              &lt;sequence minOccurs="0"&gt;
     *                  &lt;element ref="gml:Polygon"/&gt;
     *              &lt;/sequence&gt;
     *              &lt;attributeGroup ref="xlink:simpleLink"/&gt;
     *              &lt;attribute ref="gml:remoteSchema" use="optional"/&gt;
     *          &lt;/restriction&gt;
     *      &lt;/complexContent&gt;
     *  &lt;/complexType&gt;
     *
     *    </code>
     *   </pre>
     * </p>
     *
     * @generated
     */
    public static final AttributeType POLYGONPROPERTYTYPE_TYPE = new AttributeTypeImpl(new NameImpl(
                "http://www.opengis.net/gml", "PolygonPropertyType"), Polygon.class, false, false,
            Collections.EMPTY_LIST, GEOMETRYASSOCIATIONTYPE_TYPE, null);

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;complexType name="PointType"&gt;
     *      &lt;annotation&gt;
     *          &lt;documentation&gt;
     *          A Point is defined by a single coordinate tuple.
     *        &lt;/documentation&gt;
     *      &lt;/annotation&gt;
     *      &lt;complexContent&gt;
     *          &lt;extension base="gml:AbstractGeometryType"&gt;
     *              &lt;sequence&gt;
     *                  &lt;choice&gt;
     *                      &lt;element ref="gml:coord"/&gt;
     *                      &lt;element ref="gml:coordinates"/&gt;
     *                  &lt;/choice&gt;
     *              &lt;/sequence&gt;
     *          &lt;/extension&gt;
     *      &lt;/complexContent&gt;
     *  &lt;/complexType&gt;
     *
     *    </code>
     *   </pre>
     * </p>
     *
     * @generated
     */
    public static final AttributeType POINTTYPE_TYPE = new AttributeTypeImpl(new NameImpl(
                "http://www.opengis.net/gml", "PointType"), Point.class, false, false,
            Collections.EMPTY_LIST, ABSTRACTGEOMETRYTYPE_TYPE, null);

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;complexType name="PointPropertyType"&gt;
     *      &lt;annotation&gt;
     *          &lt;documentation&gt;
     *          Encapsulates a single point to represent position, location, or
     *          centerOf properties.
     *        &lt;/documentation&gt;
     *      &lt;/annotation&gt;
     *      &lt;complexContent&gt;
     *          &lt;restriction base="gml:GeometryAssociationType"&gt;
     *              &lt;sequence minOccurs="0"&gt;
     *                  &lt;element ref="gml:Point"/&gt;
     *              &lt;/sequence&gt;
     *              &lt;attributeGroup ref="xlink:simpleLink"/&gt;
     *              &lt;attribute ref="gml:remoteSchema" use="optional"/&gt;
     *          &lt;/restriction&gt;
     *      &lt;/complexContent&gt;
     *  &lt;/complexType&gt;
     *
     *    </code>
     *   </pre>
     * </p>
     *
     * @generated
     */
    public static final AttributeType POINTPROPERTYTYPE_TYPE = new AttributeTypeImpl(new NameImpl(
                "http://www.opengis.net/gml", "PointPropertyType"), Point.class, false, false,
            Collections.EMPTY_LIST, GEOMETRYASSOCIATIONTYPE_TYPE, null);

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;complexType name="PointMemberType"&gt;
     *      &lt;annotation&gt;
     *          &lt;documentation&gt;Restricts the geometry member to being a Point instance.&lt;/documentation&gt;
     *      &lt;/annotation&gt;
     *      &lt;complexContent&gt;
     *          &lt;restriction base="gml:GeometryAssociationType"&gt;
     *              &lt;sequence minOccurs="0"&gt;
     *                  &lt;element ref="gml:Point"/&gt;
     *              &lt;/sequence&gt;
     *              &lt;attributeGroup ref="gml:AssociationAttributeGroup"/&gt;
     *          &lt;/restriction&gt;
     *      &lt;/complexContent&gt;
     *  &lt;/complexType&gt;
     *
     *    </code>
     *   </pre>
     * </p>
     *
     * @generated
     */
    public static final AttributeType POINTMEMBERTYPE_TYPE = new AttributeTypeImpl(new NameImpl(
                "http://www.opengis.net/gml", "PointMemberType"), Point.class, false, false,
            Collections.EMPTY_LIST, GEOMETRYASSOCIATIONTYPE_TYPE, null);

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;complexType name="MultiPointType"&gt;
     *      &lt;annotation&gt;
     *          &lt;documentation&gt;
     *          A MultiPoint is defined by one or more Points, referenced through
     *          pointMember elements.
     *        &lt;/documentation&gt;
     *      &lt;/annotation&gt;
     *      &lt;complexContent&gt;
     *          &lt;restriction base="gml:GeometryCollectionType"&gt;
     *              &lt;sequence&gt;
     *                  &lt;element maxOccurs="unbounded" ref="gml:pointMember"/&gt;
     *              &lt;/sequence&gt;
     *              &lt;attribute name="gid" type="ID" use="optional"/&gt;
     *              &lt;attribute name="srsName" type="anyURI" use="required"/&gt;
     *          &lt;/restriction&gt;
     *      &lt;/complexContent&gt;
     *  &lt;/complexType&gt;
     *
     *    </code>
     *   </pre>
     * </p>
     *
     * @generated
     */
    public static final AttributeType MULTIPOINTTYPE_TYPE = new AttributeTypeImpl(new NameImpl(
                "http://www.opengis.net/gml", "MultiPointType"), MultiPoint.class, false, false,
            Collections.EMPTY_LIST, GEOMETRYCOLLECTIONTYPE_TYPE, null);

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;complexType name="MultiPointPropertyType"&gt;
     *      &lt;annotation&gt;
     *          &lt;documentation&gt;
     *          Encapsulates a MultiPoint element to represent the following
     *          discontiguous geometric properties: multiLocation, multiPosition,
     *          multiCenterOf.
     *        &lt;/documentation&gt;
     *      &lt;/annotation&gt;
     *      &lt;complexContent&gt;
     *          &lt;restriction base="gml:GeometryAssociationType"&gt;
     *              &lt;sequence minOccurs="0"&gt;
     *                  &lt;element ref="gml:MultiPoint"/&gt;
     *              &lt;/sequence&gt;
     *              &lt;attributeGroup ref="xlink:simpleLink"/&gt;
     *              &lt;attribute ref="gml:remoteSchema" use="optional"/&gt;
     *          &lt;/restriction&gt;
     *      &lt;/complexContent&gt;
     *  &lt;/complexType&gt;
     *
     *    </code>
     *   </pre>
     * </p>
     *
     * @generated
     */
    public static final AttributeType MULTIPOINTPROPERTYTYPE_TYPE = new AttributeTypeImpl(new NameImpl(
                "http://www.opengis.net/gml", "MultiPointPropertyType"), MultiPoint.class, false,
            false, Collections.EMPTY_LIST, GEOMETRYASSOCIATIONTYPE_TYPE, null);

    /**
     * <p>
     *  <pre>
     *   <code>
     *  &lt;complexType name="LineStringPropertyType"&gt;
     *      &lt;annotation&gt;
     *          &lt;documentation&gt;
     *          Encapsulates a single LineString to represent centerLineOf or
     *          edgeOf properties.
     *        &lt;/documentation&gt;
     *      &lt;/annotation&gt;
     *      &lt;complexContent&gt;
     *          &lt;restriction base="gml:GeometryAssociationType"&gt;
     *              &lt;sequence minOccurs="0"&gt;
     *                  &lt;element ref="gml:LineString"/&gt;
     *              &lt;/sequence&gt;
     *              &lt;attributeGroup ref="xlink:simpleLink"/&gt;
     *              &lt;attribute ref="gml:remoteSchema" use="optional"/&gt;
     *          &lt;/restriction&gt;
     *      &lt;/complexContent&gt;
     *  &lt;/complexType&gt;
     *
     *    </code>
     *   </pre>
     * </p>
     *
     * @generated
     */
    public static final AttributeType LINESTRINGPROPERTYTYPE_TYPE = new AttributeTypeImpl(new NameImpl(
                "http://www.opengis.net/gml", "LineStringPropertyType"), LineString.class, false,
            false, Collections.EMPTY_LIST, GEOMETRYASSOCIATIONTYPE_TYPE, null);

    public GMLSchema() {
        super("http://www.opengis.net/gml");

        put(new NameImpl("http://www.opengis.net/gml", "AbstractGeometryType"),
            ABSTRACTGEOMETRYTYPE_TYPE);
        put(new NameImpl("http://www.opengis.net/gml", "AbstractGeometryCollectionBaseType"),
            ABSTRACTGEOMETRYCOLLECTIONBASETYPE_TYPE);
        put(new NameImpl("http://www.opengis.net/gml", "GeometryAssociationType"),
            GEOMETRYASSOCIATIONTYPE_TYPE);
        put(new NameImpl("http://www.opengis.net/gml", "GeometryCollectionType"),
            GEOMETRYCOLLECTIONTYPE_TYPE);
        put(new NameImpl("http://www.opengis.net/gml", "CoordType"), COORDTYPE_TYPE);
        put(new NameImpl("http://www.opengis.net/gml", "CoordinatesType"), COORDINATESTYPE_TYPE);
        put(new NameImpl("http://www.opengis.net/gml", "LinearRingType"), LINEARRINGTYPE_TYPE);
        put(new NameImpl("http://www.opengis.net/gml", "LinearRingMemberType"),
            LINEARRINGMEMBERTYPE_TYPE);
        put(new NameImpl("http://www.opengis.net/gml", "PolygonType"), POLYGONTYPE_TYPE);
        put(new NameImpl("http://www.opengis.net/gml", "PolygonMemberType"), POLYGONMEMBERTYPE_TYPE);
        put(new NameImpl("http://www.opengis.net/gml", "MultiPolygonType"), MULTIPOLYGONTYPE_TYPE);
        put(new NameImpl("http://www.opengis.net/gml", "MultiPolygonPropertyType"),
            MULTIPOLYGONPROPERTYTYPE_TYPE);
        put(new NameImpl("http://www.opengis.net/gml", "MultiGeometryPropertyType"),
            MULTIGEOMETRYPROPERTYTYPE_TYPE);
        put(new NameImpl("http://www.opengis.net/gml", "LineStringType"), LINESTRINGTYPE_TYPE);
        put(new NameImpl("http://www.opengis.net/gml", "LineStringMemberType"),
            LINESTRINGMEMBERTYPE_TYPE);
        put(new NameImpl("http://www.opengis.net/gml", "MultiLineStringType"), MULTILINESTRINGTYPE_TYPE);
        put(new NameImpl("http://www.opengis.net/gml", "MultiLineStringPropertyType"),
            MULTILINESTRINGPROPERTYTYPE_TYPE);
        put(new NameImpl("http://www.opengis.net/gml", "BoxType"), BOXTYPE_TYPE);
        put(new NameImpl("http://www.opengis.net/gml", "NullType"), NULLTYPE_TYPE);
        put(new NameImpl("http://www.opengis.net/gml", "BoundingShapeType"), BOUNDINGSHAPETYPE_TYPE);
        put(new NameImpl("http://www.opengis.net/gml", "AbstractFeatureType"), ABSTRACTFEATURETYPE_TYPE);
        put(new NameImpl("http://www.opengis.net/gml", "AbstractFeatureCollectionBaseType"),
            ABSTRACTFEATURECOLLECTIONBASETYPE_TYPE);
        put(new NameImpl("http://www.opengis.net/gml", "FeatureAssociationType"),
            FEATUREASSOCIATIONTYPE_TYPE);
        put(new NameImpl("http://www.opengis.net/gml", "AbstractFeatureCollectionType"),
            ABSTRACTFEATURECOLLECTIONTYPE_TYPE);
        put(new NameImpl("http://www.opengis.net/gml", "GeometryPropertyType"),
            GEOMETRYPROPERTYTYPE_TYPE);
        put(new NameImpl("http://www.opengis.net/gml", "PolygonPropertyType"), POLYGONPROPERTYTYPE_TYPE);
        put(new NameImpl("http://www.opengis.net/gml", "PointType"), POINTTYPE_TYPE);
        put(new NameImpl("http://www.opengis.net/gml", "PointPropertyType"), POINTPROPERTYTYPE_TYPE);
        put(new NameImpl("http://www.opengis.net/gml", "PointMemberType"), POINTMEMBERTYPE_TYPE);
        put(new NameImpl("http://www.opengis.net/gml", "MultiPointType"), MULTIPOINTTYPE_TYPE);
        put(new NameImpl("http://www.opengis.net/gml", "MultiPointPropertyType"),
            MULTIPOINTPROPERTYTYPE_TYPE);
        put(new NameImpl("http://www.opengis.net/gml", "LineStringPropertyType"),
            LINESTRINGPROPERTYTYPE_TYPE);
    }
    
    /**
     * Profile of GMLSchema capturing a unique mapping of Java classes.
     * <p>
     * This profile mostly matches to JTS Geometry classes.
     * 
     * @return Subset of GMLSchema capturing a unique mapping of Java classes
     */
    public ProfileImpl profile() {
        Set<Name> profile = new LinkedHashSet<Name>();
        profile.add(new NameImpl(GML.PointPropertyType));
        profile.add(new NameImpl(GML.MultiPointPropertyType));
        profile.add(new NameImpl(GML.LineStringPropertyType));
        profile.add(new NameImpl(GML.MultiLineStringPropertyType));
        profile.add(new NameImpl(GML.PolygonPropertyType));
        profile.add(new NameImpl(GML.MultiPolygonPropertyType));
        profile.add(new NameImpl(GML.GeometryPropertyType));
        
        return new ProfileImpl(this,profile);
    }
}
