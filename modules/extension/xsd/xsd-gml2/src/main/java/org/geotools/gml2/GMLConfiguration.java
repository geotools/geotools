/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2002-2015, Open Source Geospatial Foundation (OSGeo)
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

import javax.xml.namespace.QName;
import org.geotools.feature.DefaultFeatureCollections;
import org.geotools.gml2.bindings.GMLAbstractFeatureCollectionBaseTypeBinding;
import org.geotools.gml2.bindings.GMLAbstractFeatureCollectionTypeBinding;
import org.geotools.gml2.bindings.GMLAbstractFeatureTypeBinding;
import org.geotools.gml2.bindings.GMLAbstractGeometryCollectionBaseTypeBinding;
import org.geotools.gml2.bindings.GMLAbstractGeometryTypeBinding;
import org.geotools.gml2.bindings.GMLBoundingShapeTypeBinding;
import org.geotools.gml2.bindings.GMLBoxTypeBinding;
import org.geotools.gml2.bindings.GMLCoordTypeBinding;
import org.geotools.gml2.bindings.GMLCoordinatesTypeBinding;
import org.geotools.gml2.bindings.GMLFeatureAssociationTypeBinding;
import org.geotools.gml2.bindings.GMLGeometryAssociationTypeBinding;
import org.geotools.gml2.bindings.GMLGeometryCollectionTypeBinding;
import org.geotools.gml2.bindings.GMLGeometryPropertyTypeBinding;
import org.geotools.gml2.bindings.GMLLineStringMemberTypeBinding;
import org.geotools.gml2.bindings.GMLLineStringPropertyTypeBinding;
import org.geotools.gml2.bindings.GMLLineStringTypeBinding;
import org.geotools.gml2.bindings.GMLLinearRingMemberTypeBinding;
import org.geotools.gml2.bindings.GMLLinearRingTypeBinding;
import org.geotools.gml2.bindings.GMLMultiGeometryPropertyTypeBinding;
import org.geotools.gml2.bindings.GMLMultiLineStringPropertyTypeBinding;
import org.geotools.gml2.bindings.GMLMultiLineStringTypeBinding;
import org.geotools.gml2.bindings.GMLMultiPointPropertyTypeBinding;
import org.geotools.gml2.bindings.GMLMultiPointTypeBinding;
import org.geotools.gml2.bindings.GMLMultiPolygonPropertyTypeBinding;
import org.geotools.gml2.bindings.GMLMultiPolygonTypeBinding;
import org.geotools.gml2.bindings.GMLNullTypeBinding;
import org.geotools.gml2.bindings.GMLPointMemberTypeBinding;
import org.geotools.gml2.bindings.GMLPointPropertyTypeBinding;
import org.geotools.gml2.bindings.GMLPointTypeBinding;
import org.geotools.gml2.bindings.GMLPolygonMemberTypeBinding;
import org.geotools.gml2.bindings.GMLPolygonPropertyTypeBinding;
import org.geotools.gml2.bindings.GMLPolygonTypeBinding;
import org.geotools.xlink.XLINKConfiguration;
import org.geotools.xml.Configuration;
import org.geotools.xml.Parser;
import org.locationtech.jts.geom.CoordinateSequenceFactory;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.impl.CoordinateArraySequenceFactory;
import org.picocontainer.MutablePicoContainer;

/**
 * Configuration used by gml2 parsers.
 *
 * @author Justin Deoliveira, The Open Planning Project
 * @source $URL$
 */
public class GMLConfiguration extends Configuration {
    /** Property which controls whether encoded features should include bounds. */
    public static final QName NO_FEATURE_BOUNDS = new QName("org.geotools.gml", "noFeatureBounds");

    /**
     * Boolean property which controls whether the FeatureCollection should be encoded with multiple
     * featureMember as opposed to a single featureMembers
     */
    public static final QName ENCODE_FEATURE_MEMBER =
            new QName("org.geotools.gml", "encodeFeatureMember");

    /**
     * Boolean property which controls whether geometry and envelope objects are encoded with an srs
     * dimension attribute (not really used for GML2, added for compatibility with GML3)
     */
    public static final QName NO_SRS_DIMENSION = new QName("org.geotools.gml", "noSrsDimension");

    /** Property which engages "fast" gml encoding. */
    public static final QName OPTIMIZED_ENCODING =
            new QName("org.geotools.gml", "optimizedEncoding");

    /** Srs name style to encode srsName URI's with */
    protected SrsSyntax srsSyntax = SrsSyntax.OGC_HTTP_URL;

    /** Number of decimals in the output */
    protected int numDecimals = 6;

    /** Creates the new gml configuration, with a depenendency on {@link XLINKConfiguration} */
    public GMLConfiguration() {
        super(GML.getInstance());

        // add xlink cdependency
        addDependency(new XLINKConfiguration());

        // add the parse unknown attributes property, this is mostly for
        // the "fid" attribute
        getProperties().add(Parser.Properties.PARSE_UNKNOWN_ELEMENTS);
        getProperties().add(Parser.Properties.PARSE_UNKNOWN_ATTRIBUTES);
    }

    /**
     * Sets the syntax to use for encoding srs uris.
     *
     * <p>If this method is not explicitly called {@link SrsSyntax#LEGACY} is used as the default.
     */
    public void setSrsSyntax(SrsSyntax srsSyntax) {
        this.srsSyntax = srsSyntax;
    }

    /** Returns the syntax to use for encoding srs uris. */
    public SrsSyntax getSrsSyntax() {
        return srsSyntax;
    }

    public void registerBindings(MutablePicoContainer container) {
        // geometry
        container.registerComponentImplementation(
                GML.AbstractGeometryCollectionBaseType,
                GMLAbstractGeometryCollectionBaseTypeBinding.class);
        container.registerComponentImplementation(
                GML.AbstractGeometryType, GMLAbstractGeometryTypeBinding.class);
        container.registerComponentImplementation(GML.BoxType, GMLBoxTypeBinding.class);
        container.registerComponentImplementation(
                GML.CoordinatesType, GMLCoordinatesTypeBinding.class);
        container.registerComponentImplementation(GML.CoordType, GMLCoordTypeBinding.class);
        container.registerComponentImplementation(
                GML.GeometryAssociationType, GMLGeometryAssociationTypeBinding.class);
        container.registerComponentImplementation(
                GML.GeometryCollectionType, GMLGeometryCollectionTypeBinding.class);
        container.registerComponentImplementation(
                GML.LinearRingMemberType, GMLLinearRingMemberTypeBinding.class);
        container.registerComponentImplementation(
                GML.LinearRingType, GMLLinearRingTypeBinding.class);
        container.registerComponentImplementation(
                GML.LineStringMemberType, GMLLineStringMemberTypeBinding.class);
        container.registerComponentImplementation(
                GML.LineStringType, GMLLineStringTypeBinding.class);
        container.registerComponentImplementation(
                GML.MultiLineStringType, GMLMultiLineStringTypeBinding.class);
        container.registerComponentImplementation(
                GML.MultiPointType, GMLMultiPointTypeBinding.class);
        container.registerComponentImplementation(
                GML.MultiPolygonType, GMLMultiPolygonTypeBinding.class);
        container.registerComponentImplementation(
                GML.PointMemberType, GMLPointMemberTypeBinding.class);
        container.registerComponentImplementation(GML.PointType, GMLPointTypeBinding.class);
        container.registerComponentImplementation(
                GML.PolygonMemberType, GMLPolygonMemberTypeBinding.class);
        container.registerComponentImplementation(GML.PolygonType, GMLPolygonTypeBinding.class);

        // feature
        container.registerComponentImplementation(
                GML.AbstractFeatureCollectionBaseType,
                GMLAbstractFeatureCollectionBaseTypeBinding.class);
        container.registerComponentImplementation(
                GML.AbstractFeatureCollectionType, GMLAbstractFeatureCollectionTypeBinding.class);
        container.registerComponentImplementation(
                GML.AbstractFeatureType, GMLAbstractFeatureTypeBinding.class);
        container.registerComponentImplementation(
                GML.BoundingShapeType, GMLBoundingShapeTypeBinding.class);
        container.registerComponentImplementation(
                GML.FeatureAssociationType, GMLFeatureAssociationTypeBinding.class);
        container.registerComponentImplementation(
                GML.GeometryPropertyType, GMLGeometryPropertyTypeBinding.class);
        container.registerComponentImplementation(
                GML.LineStringPropertyType, GMLLineStringPropertyTypeBinding.class);
        container.registerComponentImplementation(
                GML.MultiGeometryPropertyType, GMLMultiGeometryPropertyTypeBinding.class);
        container.registerComponentImplementation(
                GML.MultiLineStringPropertyType, GMLMultiLineStringPropertyTypeBinding.class);
        container.registerComponentImplementation(
                GML.MultiPointPropertyType, GMLMultiPointPropertyTypeBinding.class);
        container.registerComponentImplementation(
                GML.MultiPolygonPropertyType, GMLMultiPolygonPropertyTypeBinding.class);
        container.registerComponentImplementation(GML.NullType, GMLNullTypeBinding.class);
        container.registerComponentImplementation(
                GML.PointPropertyType, GMLPointPropertyTypeBinding.class);
        container.registerComponentImplementation(
                GML.PolygonPropertyType, GMLPolygonPropertyTypeBinding.class);
    }

    /**
     * Configures the gml2 context.
     *
     * <p>The following classes are registered:
     *
     * <ul>
     *   <li>{@link CoordinateArraySequenceFactory} under {@link CoordinateSequenceFactory}
     *   <li>{@link GeometryFactory}
     *   <li>{@link FeatureTypeCache}
     *   <li>{@link DefaultFeatureCollections}
     * </ul>
     */
    public void configureContext(MutablePicoContainer container) {
        super.configureContext(container);

        container.registerComponentInstance(new FeatureTypeCache());

        container.registerComponentInstance(
                CoordinateSequenceFactory.class, CoordinateArraySequenceFactory.instance());
        container.registerComponentImplementation(GeometryFactory.class);
        container.registerComponentImplementation(DefaultFeatureCollections.class);

        container.registerComponentInstance(srsSyntax);
    }

    /**
     * Returns the number of decimals that should be used for encoding coordinates (defaults to 6)
     *
     * @return the numDecimals
     */
    public int getNumDecimals() {
        return numDecimals;
    }

    /**
     * Sets the number of decimals that should be used for encoding coordinates
     *
     * @param numDecimals the numDecimals to set
     */
    public void setNumDecimals(int numDecimals) {
        this.numDecimals = numDecimals;
    }
}
