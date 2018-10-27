/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2015 - 2016, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.gml3.simple;

import java.util.List;
import java.util.Map;
import org.eclipse.xsd.XSDElementDeclaration;
import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.geometry.jts.CircularRing;
import org.geotools.geometry.jts.CircularString;
import org.geotools.geometry.jts.CompoundCurve;
import org.geotools.geometry.jts.CompoundRing;
import org.geotools.gml2.SrsSyntax;
import org.geotools.gml2.simple.GMLWriter;
import org.geotools.gml2.simple.GeometryEncoder;
import org.geotools.gml2.simple.QualifiedName;
import org.geotools.gml3.bindings.GML3EncodingUtils;
import org.geotools.gml3.v3_2.GML;
import org.geotools.gml3.v3_2.bindings.GML32EncodingUtils;
import org.geotools.xsd.Configuration;
import org.geotools.xsd.Encoder;
import org.geotools.xsd.XSD;
import org.locationtech.jts.geom.GeometryCollection;
import org.locationtech.jts.geom.LineString;
import org.locationtech.jts.geom.LinearRing;
import org.locationtech.jts.geom.MultiLineString;
import org.locationtech.jts.geom.MultiPoint;
import org.locationtech.jts.geom.MultiPolygon;
import org.locationtech.jts.geom.Point;
import org.locationtech.jts.geom.Polygon;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.type.AttributeDescriptor;
import org.opengis.feature.type.FeatureType;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.AttributesImpl;

/**
 * SimpleFeatureCollection encoder delegate for fast GML3 encoding
 *
 * @author Andrea Aime - GeoSolutions
 */
public class GML32FeatureCollectionEncoderDelegate
        extends org.geotools.gml2.simple.FeatureCollectionEncoderDelegate {

    public GML32FeatureCollectionEncoderDelegate(
            SimpleFeatureCollection features, Encoder encoder) {
        super(features, encoder, new GML32Delegate(encoder));
        this.encodeGeometryIds = true;
    }

    @Override
    protected Attributes getPropertyAttributes(
            QualifiedName name,
            FeatureType featureType,
            AttributeDescriptor attribute,
            Object value) {
        if ("identifier".equals(name.getLocalPart())
                && GML.NAMESPACE.equals(name.getNamespaceURI())) {
            AttributesImpl atts = new AttributesImpl();
            atts.addAttribute(
                    null, "codeSpace", "codeSpace", null, featureType.getName().getNamespaceURI());
            return atts;
        }

        return null;
    }

    public static class GML32Delegate implements org.geotools.gml2.simple.GMLDelegate {

        SrsSyntax srsSyntax;

        static final QualifiedName MEMBER =
                new QualifiedName(GML.NAMESPACE, GML.member.getLocalPart(), "gml");

        protected QualifiedName member;

        String gmlPrefix;
        String gmlUri;
        GML32EncodingUtils encodingUtils;

        int numDecimals;

        private boolean padWithZeros;

        private boolean decimalEncoding;

        /** Controls if coordinates measures should be encoded in GML * */
        private boolean encodeMeasures;

        public GML32Delegate(Encoder encoder) {
            this.gmlUri = org.geotools.gml3.v3_2.GML.NAMESPACE;
            this.gmlPrefix = encoder.getNamespaces().getPrefix(gmlUri);

            this.member = MEMBER.derive(gmlPrefix, gmlUri);
            this.srsSyntax =
                    (SrsSyntax) encoder.getContext().getComponentInstanceOfType(SrsSyntax.class);
            this.encodingUtils = new GML32EncodingUtils();
            this.numDecimals = getNumDecimals(encoder.getConfiguration());
            this.padWithZeros = getPadWithZeros(encoder.getConfiguration());
            this.decimalEncoding = getForceDecimalEncoding(encoder.getConfiguration());
            this.encodeMeasures = getEncodecoordinatesMeasures(encoder.getConfiguration());
        }

        private int getNumDecimals(Configuration configuration) {
            org.geotools.gml3.v3_2.GMLConfiguration config;
            if (configuration instanceof org.geotools.gml3.v3_2.GMLConfiguration) {
                config = (org.geotools.gml3.v3_2.GMLConfiguration) configuration;
            } else {
                config = configuration.getDependency(org.geotools.gml3.v3_2.GMLConfiguration.class);
            }

            if (config == null) {
                return 6;
            } else {
                return config.getNumDecimals();
            }
        }

        private boolean getPadWithZeros(Configuration configuration) {
            org.geotools.gml3.v3_2.GMLConfiguration config;
            if (configuration instanceof org.geotools.gml3.v3_2.GMLConfiguration) {
                config = (org.geotools.gml3.v3_2.GMLConfiguration) configuration;
            } else {
                config = configuration.getDependency(org.geotools.gml3.v3_2.GMLConfiguration.class);
            }

            if (config == null) {
                return false;
            } else {
                return config.getPadWithZeros();
            }
        }

        private boolean getForceDecimalEncoding(Configuration configuration) {
            org.geotools.gml3.v3_2.GMLConfiguration config;
            if (configuration instanceof org.geotools.gml3.v3_2.GMLConfiguration) {
                config = (org.geotools.gml3.v3_2.GMLConfiguration) configuration;
            } else {
                config = configuration.getDependency(org.geotools.gml3.v3_2.GMLConfiguration.class);
            }

            if (config == null) {
                return true;
            } else {
                return config.getForceDecimalEncoding();
            }
        }

        private boolean getEncodecoordinatesMeasures(Configuration configuration) {
            org.geotools.gml3.v3_2.GMLConfiguration config;
            if (configuration instanceof org.geotools.gml3.v3_2.GMLConfiguration) {
                config = (org.geotools.gml3.v3_2.GMLConfiguration) configuration;
            } else {
                config = configuration.getDependency(org.geotools.gml3.v3_2.GMLConfiguration.class);
            }

            if (config == null) {
                // unless explicitly requested, coordinates measures are not encoded
                return false;
            } else {
                return config.getEncodeMeasures();
            }
        }

        public List getFeatureProperties(
                SimpleFeature f, XSDElementDeclaration element, Encoder e) {
            return encodingUtils.AbstractFeatureTypeGetProperties(
                    f, element, e.getSchemaIndex(), e.getConfiguration());
        }

        public EnvelopeEncoder createEnvelopeEncoder(Encoder e) {
            return new EnvelopeEncoder(e, gmlPrefix, gmlUri);
        }

        public void setSrsNameAttribute(AttributesImpl atts, CoordinateReferenceSystem crs) {

            atts.addAttribute(
                    null,
                    "srsName",
                    "srsName",
                    null,
                    GML3EncodingUtils.toURI(crs, srsSyntax).toString());
        }

        @Override
        public void setGeometryDimensionAttribute(AttributesImpl atts, int dimension) {
            atts.addAttribute(
                    null, "srsDimension", "srsDimension", null, String.valueOf(dimension));
        }

        public void initFidAttribute(AttributesImpl atts) {
            atts.addAttribute(GML.NAMESPACE, "id", "gml:id", null, "");
        }

        public void startFeatures(GMLWriter handler) throws Exception {}

        public void startFeature(GMLWriter handler) throws Exception {
            handler.startElement(member, null);
        }

        public void endFeature(GMLWriter handler) throws Exception {
            handler.endElement(member);
        }

        public void endFeatures(GMLWriter handler) throws Exception {}

        @Override
        public void registerGeometryEncoders(
                Map<Class, GeometryEncoder> encoders, Encoder encoder) {
            encoders.put(Point.class, new PointEncoder(encoder, gmlPrefix, gmlUri));
            encoders.put(MultiPoint.class, new MultiPointEncoder(encoder, gmlPrefix, gmlUri));
            encoders.put(LineString.class, new LineStringEncoder(encoder, gmlPrefix, gmlUri));
            encoders.put(LinearRing.class, new LinearRingEncoder(encoder, gmlPrefix, gmlUri));
            encoders.put(
                    MultiLineString.class,
                    new MultiLineStringEncoder(encoder, gmlPrefix, gmlUri, true));
            encoders.put(Polygon.class, new PolygonEncoder(encoder, gmlPrefix, gmlUri));
            encoders.put(MultiPolygon.class, new MultiPolygonEncoder(encoder, gmlPrefix, gmlUri));
            encoders.put(CircularString.class, new CurveEncoder(encoder, gmlPrefix, gmlUri));
            encoders.put(CompoundCurve.class, new CurveEncoder(encoder, gmlPrefix, gmlUri));
            encoders.put(CircularRing.class, new CurveEncoder(encoder, gmlPrefix, gmlUri));
            encoders.put(CompoundRing.class, new CurveEncoder(encoder, gmlPrefix, gmlUri));
            encoders.put(
                    GeometryCollection.class,
                    new GeometryCollectionEncoder(encoder, gmlPrefix, gmlUri));
        }

        @Override
        public String getGmlPrefix() throws Exception {
            return gmlPrefix;
        }

        @Override
        public boolean supportsTuples() {
            return false;
        }

        @Override
        public void startTuple(GMLWriter handler) throws SAXException {}

        @Override
        public void endTuple(GMLWriter handler) throws SAXException {}

        @Override
        public XSD getSchema() {
            return GML.getInstance();
        }

        @Override
        public int getNumDecimals() {
            return numDecimals;
        }

        @Override
        public boolean forceDecimalEncoding() {
            return decimalEncoding;
        }

        @Override
        public boolean getEncodeMeasures() {
            return encodeMeasures;
        }

        @Override
        public boolean padWithZeros() {
            return padWithZeros;
        }
    }
}
