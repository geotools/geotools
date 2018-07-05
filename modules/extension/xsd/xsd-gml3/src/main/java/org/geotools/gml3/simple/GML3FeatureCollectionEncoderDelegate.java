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

import java.util.Enumeration;
import java.util.List;
import java.util.Map;
import org.eclipse.xsd.XSDElementDeclaration;
import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.geometry.jts.CircularRing;
import org.geotools.geometry.jts.CircularString;
import org.geotools.geometry.jts.CompoundCurve;
import org.geotools.geometry.jts.CompoundRing;
import org.geotools.geometry.jts.MultiCurve;
import org.geotools.gml2.SrsSyntax;
import org.geotools.gml2.simple.GMLWriter;
import org.geotools.gml2.simple.GeometryEncoder;
import org.geotools.gml2.simple.QualifiedName;
import org.geotools.gml3.GML;
import org.geotools.gml3.GMLConfiguration;
import org.geotools.gml3.bindings.GML3EncodingUtils;
import org.geotools.xml.Configuration;
import org.geotools.xml.Encoder;
import org.geotools.xml.XSD;
import org.locationtech.jts.geom.GeometryCollection;
import org.locationtech.jts.geom.LineString;
import org.locationtech.jts.geom.LinearRing;
import org.locationtech.jts.geom.MultiLineString;
import org.locationtech.jts.geom.MultiPoint;
import org.locationtech.jts.geom.MultiPolygon;
import org.locationtech.jts.geom.Point;
import org.locationtech.jts.geom.Polygon;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.xml.sax.helpers.AttributesImpl;
import org.xml.sax.helpers.NamespaceSupport;

/**
 * {@link SimpleFeatureCollection} encoder delegate for fast GML3 encoding
 *
 * @author Andrea Aime - GeoSolutions
 */
public class GML3FeatureCollectionEncoderDelegate
        extends org.geotools.gml2.simple.FeatureCollectionEncoderDelegate {

    public GML3FeatureCollectionEncoderDelegate(SimpleFeatureCollection features, Encoder encoder) {
        super(features, encoder, new GML3Delegate(encoder));
    }

    static class GML3Delegate implements org.geotools.gml2.simple.GMLDelegate {

        SrsSyntax srsSyntax;

        static final QualifiedName FEATURE_MEMBERS =
                new QualifiedName(GML.NAMESPACE, GML.featureMembers.getLocalPart(), "gml");

        static final QualifiedName FEATURE_MEMBER =
                new QualifiedName(GML.NAMESPACE, GML.featureMember.getLocalPart(), "gml");

        QualifiedName featureMembers;

        QualifiedName featureMember;

        private String gmlPrefix;

        private String gmlUri;

        private int numDecimals;

        private boolean encodeSeparateMember;

        public GML3Delegate(Encoder encoder) {
            this.gmlPrefix = findGMLPrefix(encoder);

            String gmlURI = encoder.getNamespaces().getURI(gmlPrefix);
            this.gmlUri = gmlURI != null ? gmlURI : GML.NAMESPACE;

            this.featureMembers = FEATURE_MEMBERS.derive(gmlPrefix, gmlURI);
            this.featureMember = FEATURE_MEMBER.derive(gmlPrefix, gmlURI);
            this.srsSyntax =
                    (SrsSyntax) encoder.getContext().getComponentInstanceOfType(SrsSyntax.class);
            this.numDecimals = getNumDecimals(encoder.getConfiguration());
            this.encodeSeparateMember =
                    encoder.getConfiguration().hasProperty(GMLConfiguration.ENCODE_FEATURE_MEMBER);
        }

        String findGMLPrefix(Encoder encoder) {
            NamespaceSupport ns = encoder.getNamespaces();
            Enumeration<String> p = ns.getPrefixes();
            while (p.hasMoreElements()) {
                String prefix = p.nextElement();
                String uri = ns.getURI(prefix);

                if (uri.startsWith(GML.NAMESPACE)) {
                    return prefix;
                }
            }

            return "gml";
        }

        private int getNumDecimals(Configuration configuration) {
            GMLConfiguration config;
            if (configuration instanceof GMLConfiguration) {
                config = (GMLConfiguration) configuration;
            } else {
                config = configuration.getDependency(GMLConfiguration.class);
            }

            if (config == null) {
                return 6;
            } else {
                return config.getNumDecimals();
            }
        }

        public List getFeatureProperties(
                SimpleFeature f, XSDElementDeclaration element, Encoder e) {
            return GML3EncodingUtils.INSTANCE.AbstractFeatureTypeGetProperties(
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

        public void startFeatures(GMLWriter handler) throws Exception {
            if (!encodeSeparateMember) {
                handler.startElement(featureMembers, null);
            }
        }

        public void startFeature(GMLWriter handler) throws Exception {
            if (encodeSeparateMember) {
                handler.startElement(featureMember, null);
            }
        }

        public void endFeature(GMLWriter handler) throws Exception {
            if (encodeSeparateMember) {
                handler.endElement(featureMember);
            }
        }

        public void endFeatures(GMLWriter handler) throws Exception {
            if (!encodeSeparateMember) {
                handler.endElement(featureMembers);
            }
        }

        @Override
        public void registerGeometryEncoders(
                Map<Class, GeometryEncoder> encoders, Encoder encoder) {
            encoders.put(Point.class, new PointEncoder(encoder, gmlPrefix, gmlUri));
            encoders.put(MultiPoint.class, new MultiPointEncoder(encoder, gmlPrefix, gmlUri));
            encoders.put(LineString.class, new LineStringEncoder(encoder, gmlPrefix, gmlUri));
            encoders.put(LinearRing.class, new LinearRingEncoder(encoder, gmlPrefix, gmlUri));
            encoders.put(
                    MultiLineString.class,
                    new MultiLineStringEncoder(encoder, gmlPrefix, gmlUri, false));
            encoders.put(
                    MultiCurve.class, new MultiLineStringEncoder(encoder, gmlPrefix, gmlUri, true));
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
        public void startTuple(GMLWriter output) {}

        @Override
        public void endTuple(GMLWriter output) {}

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
            return false;
        }
    }
}
