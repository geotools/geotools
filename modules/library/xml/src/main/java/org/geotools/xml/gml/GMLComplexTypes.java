/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2004-2016, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.xml.gml;

import java.io.IOException;
import java.net.URI;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.naming.OperationNotSupportedException;
import org.geotools.data.DataUtilities;
import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.data.simple.SimpleFeatureIterator;
import org.geotools.feature.AttributeTypeBuilder;
import org.geotools.feature.FeatureCollection;
import org.geotools.feature.NameImpl;
import org.geotools.feature.simple.SimpleFeatureBuilder;
import org.geotools.feature.simple.SimpleFeatureTypeBuilder;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.referencing.CRS;
import org.geotools.util.SuppressFBWarnings;
import org.geotools.xml.PrintHandler;
import org.geotools.xml.XMLHandlerHints;
import org.geotools.xml.gml.FCBuffer.StopException;
import org.geotools.xml.gml.GMLSchema.AttributeList;
import org.geotools.xml.gml.GMLSchema.GMLAttribute;
import org.geotools.xml.gml.GMLSchema.GMLComplexType;
import org.geotools.xml.gml.GMLSchema.GMLElement;
import org.geotools.xml.gml.GMLSchema.GMLNullType;
import org.geotools.xml.schema.All;
import org.geotools.xml.schema.Attribute;
import org.geotools.xml.schema.Choice;
import org.geotools.xml.schema.ComplexType;
import org.geotools.xml.schema.Element;
import org.geotools.xml.schema.ElementGrouping;
import org.geotools.xml.schema.ElementValue;
import org.geotools.xml.schema.Group;
import org.geotools.xml.schema.Sequence;
import org.geotools.xml.schema.SimpleType;
import org.geotools.xml.schema.Type;
import org.geotools.xml.xLink.XLinkSchema;
import org.geotools.xml.xsi.XSISimpleTypes;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.CoordinateSequence;
import org.locationtech.jts.geom.Envelope;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.GeometryCollection;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.LineString;
import org.locationtech.jts.geom.LinearRing;
import org.locationtech.jts.geom.MultiLineString;
import org.locationtech.jts.geom.MultiPoint;
import org.locationtech.jts.geom.MultiPolygon;
import org.locationtech.jts.geom.Point;
import org.locationtech.jts.geom.Polygon;
import org.locationtech.jts.geom.impl.CoordinateArraySequenceFactory;
import org.opengis.feature.IllegalAttributeException;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.feature.type.AttributeDescriptor;
import org.opengis.feature.type.GeometryDescriptor;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.SAXNotSupportedException;
import org.xml.sax.helpers.AttributesImpl;

/**
 * This class is intended to act as a collection of package visible GML complexType definition to be
 * used by the GMLSchema
 *
 * @author $author$
 * @version $Revision: 1.9 $
 * @see GMLSchema
 * @see ComplexType
 */
public class GMLComplexTypes {
    // used for debugging
    protected static Logger logger = getLogger();

    /**
     * Returns the logger to be used for this class.
     *
     * @todo Logger.setLevel(...) should not be invoked, because it override any user setting in
     *     {@code jre/lib/logging.properties}. Users should edit their properties file instead. If
     *     Geotools is too verbose below the warning level, then some log messages should probably
     *     be changed from Level.INFO to Level.FINE.
     * @todo The logger package should probably be {@code "org.geotools.xml.gml"}.
     */
    private static final Logger getLogger() {
        Logger l = org.geotools.util.logging.Logging.getLogger(GMLComplexTypes.class);
        l.setLevel(Level.WARNING);
        return l;
    }

    private static final String STREAM_FEATURE_NAME_HINT =
            "org.geotools.xml.gml.STREAM_FEATURE_NAME_HINT";

    static void encode(Element e, Geometry g, PrintHandler output)
            throws OperationNotSupportedException, IOException {
        if (g instanceof Point) {
            encode(e, (Point) g, output);

            return;
        }

        if (g instanceof Polygon) {
            encode(e, (Polygon) g, output);

            return;
        }

        if (g instanceof LinearRing) {
            encode(e, (LinearRing) g, output);

            return;
        }

        if (g instanceof LineString) {
            encode(e, (LineString) g, output);

            return;
        }

        if (g instanceof MultiLineString) {
            encode(e, (MultiLineString) g, output);

            return;
        }

        if (g instanceof MultiPolygon) {
            encode(e, (MultiPolygon) g, output);

            return;
        }

        if (g instanceof MultiPoint) {
            encode(e, (MultiPoint) g, output);

            return;
        }

        if (g instanceof GeometryCollection) {
            encode(e, (GeometryCollection) g, output);

            return;
        }
    }

    static void encode(Element e, Point g, PrintHandler output) throws IOException {
        if ((g == null) || (g.getCoordinate() == null)) {
            throw new IOException("Bad Point Data");
        }

        AttributesImpl ai = getSrsNameAttribute(g);

        if (e == null) {
            output.startElement(GMLSchema.NAMESPACE, "Point", ai);
        } else {
            output.startElement(e.getNamespace(), e.getName(), ai);
        }

        encodeCoords(null, g.getCoordinates(), output);

        if (e == null) {
            output.endElement(GMLSchema.NAMESPACE, "Point");
        } else {
            output.endElement(e.getNamespace(), e.getName());
        }
    }

    private static AttributesImpl getSrsNameAttribute(Geometry g) {
        AttributesImpl ai = new AttributesImpl();

        // no GID
        if (g.getUserData() != null) {
            // TODO Fix this when parsing is better ... should be a coord reference system
            if (g.getUserData() instanceof String) {
                ai.addAttribute("", "srsName", "", "anyURI", (String) g.getUserData());
            } else if (g.getUserData() instanceof CoordinateReferenceSystem) {
                CoordinateReferenceSystem crs = (CoordinateReferenceSystem) g.getUserData();
                try {
                    Integer code = CRS.lookupEpsgCode(crs, false);
                    if (code != null) {
                        ai.addAttribute("", "srsName", "", "anyURI", "EPSG:" + code);
                    }
                } catch (Exception ex) {
                    logger.log(
                            Level.WARNING,
                            "Could not encode the srsName for geometry, no EPSG code found",
                            ex);
                }
            }
        } else {
            //            if (g.getSRID() != 0) {
            //                // deprecated version
            //                ai.addAttribute("", "srsName", "", "anyURI", "" + g.getSRID());
            //            } else {
            ai = null;
            //            }
        }
        return ai;
    }

    static void encode(Element e, LineString g, PrintHandler output) throws IOException {
        if ((g == null) || (g.getNumPoints() == 0)) {
            throw new IOException("Bad LineString Data");
        }

        AttributesImpl ai = getSrsNameAttribute(g);

        if (e == null) {
            output.startElement(GMLSchema.NAMESPACE, "LineString", ai);
        } else {
            output.startElement(e.getNamespace(), e.getName(), ai);
        }

        encodeCoords(null, g.getCoordinates(), output);

        if (e == null) {
            output.endElement(GMLSchema.NAMESPACE, "LineString");
        } else {
            output.endElement(e.getNamespace(), e.getName());
        }
    }

    static void encode(Element e, LinearRing g, PrintHandler output) throws IOException {
        if ((g == null) || (g.getNumPoints() == 0)) {
            throw new IOException("Bad LinearRing Data");
        }

        if (e == null) {
            encode(((GMLSchema.getInstance()).getElements()[39]), (LineString) g, output);
        } else {
            encode(e, (LineString) g, output);
        }
    }

    static void encode(Element e, Polygon g, PrintHandler output)
            throws OperationNotSupportedException, IOException {
        if ((g == null) || (g.getNumPoints() == 0)) {
            throw new IOException("Bad Polygon Data");
        }

        AttributesImpl ai = getSrsNameAttribute(g);

        if (e == null) {
            output.startElement(GMLSchema.NAMESPACE, "Polygon", ai);
        } else {
            output.startElement(e.getNamespace(), e.getName(), ai);
        }

        ((GMLSchema.getInstance()).getElements()[35])
                .getType()
                .encode(
                        (GMLSchema.getInstance()).getElements()[35],
                        g.getExteriorRing(),
                        output,
                        null);

        if (g.getNumInteriorRing() > 0) {
            for (int i = 0; i < g.getNumInteriorRing(); i++)
                ((GMLSchema.getInstance()).getElements()[36])
                        .getType()
                        .encode(
                                (GMLSchema.getInstance()).getElements()[36],
                                g.getInteriorRingN(i),
                                output,
                                null);
        }

        if (e == null) {
            output.endElement(GMLSchema.NAMESPACE, "Polygon");
        } else {
            output.endElement(e.getNamespace(), e.getName());
        }
    }

    static void encode(Element e, MultiPoint g, PrintHandler output) throws IOException {
        if ((g == null) || (g.getNumGeometries() <= 0)) {
            throw new IOException("Bad MultiPoint Data");
        }

        AttributesImpl ai = getSrsNameAttribute(g);

        if (e == null) {
            output.startElement(GMLSchema.NAMESPACE, "MultiPoint", ai);
        } else {
            output.startElement(e.getNamespace(), e.getName(), ai);
        }

        for (int i = 0; i < g.getNumGeometries(); i++) {
            output.startElement(GMLSchema.NAMESPACE, "pointMember", null);
            encode(null, (Point) g.getGeometryN(i), output);
            output.endElement(GMLSchema.NAMESPACE, "pointMember");
        }

        if (e == null) {
            output.endElement(GMLSchema.NAMESPACE, "MultiPoint");
        } else {
            output.endElement(e.getNamespace(), e.getName());
        }
    }

    static void encode(Element e, MultiLineString g, PrintHandler output) throws IOException {

        if ((g == null) || g.getNumGeometries() <= 0) {
            throw new IOException("Bad MultiLineString Data");
        }

        AttributesImpl ai = getSrsNameAttribute(g);

        if (e == null) {
            output.startElement(GMLSchema.NAMESPACE, "MultiLineString", ai);
        } else {
            output.startElement(e.getNamespace(), e.getName(), ai);
        }

        for (int i = 0; i < g.getNumGeometries(); i++) {
            output.startElement(GMLSchema.NAMESPACE, "lineStringMember", null);
            encode(null, (LineString) g.getGeometryN(i), output);
            output.endElement(GMLSchema.NAMESPACE, "lineStringMember");
        }

        if (e == null) {
            output.endElement(GMLSchema.NAMESPACE, "MultiLineString");
        } else {
            output.endElement(e.getNamespace(), e.getName());
        }
    }

    static void encode(Element e, MultiPolygon g, PrintHandler output)
            throws OperationNotSupportedException, IOException {
        if ((g == null) || (g.getNumGeometries() <= 0)) {
            throw new IOException("Bad MultiPolygon Data");
        }

        AttributesImpl ai = getSrsNameAttribute(g);

        if (e == null) {
            output.startElement(GMLSchema.NAMESPACE, "MultiPolygon", ai);
        } else {
            output.startElement(e.getNamespace(), e.getName(), ai);
        }

        for (int i = 0; i < g.getNumGeometries(); i++) {
            output.startElement(GMLSchema.NAMESPACE, "polygonMember", null);
            encode(null, (Polygon) g.getGeometryN(i), output);
            output.endElement(GMLSchema.NAMESPACE, "polygonMember");
        }

        if (e == null) {
            output.endElement(GMLSchema.NAMESPACE, "MultiPolygon");
        } else {
            output.endElement(e.getNamespace(), e.getName());
        }
    }

    static void encode(Element e, GeometryCollection g, PrintHandler output)
            throws OperationNotSupportedException, IOException {
        if ((g == null) || (g.getNumGeometries() <= 0)) {
            throw new IOException("Bad GeometryCollection Data");
        }

        AttributesImpl ai = getSrsNameAttribute(g);

        if (e == null) {
            output.startElement(GMLSchema.NAMESPACE, "MultiGeometry", ai);
        } else {
            output.startElement(e.getNamespace(), e.getName(), ai);
        }

        for (int i = 0; i < g.getNumGeometries(); i++) {
            output.startElement(GMLSchema.NAMESPACE, "geometryMember", null);
            encode(null, (Polygon) g.getGeometryN(i), output);
            output.endElement(GMLSchema.NAMESPACE, "geometryMember");
        }

        if (e == null) {
            output.endElement(GMLSchema.NAMESPACE, "MultiGeometry");
        } else {
            output.endElement(e.getNamespace(), e.getName());
        }
    }

    static void encodeCoord(Element e, Coordinate coord, PrintHandler output) throws IOException {
        if (coord == null) {
            return;
        }

        AttributesImpl ai = new AttributesImpl();
        ai.addAttribute("", "X", "", "decimal", "" + coord.x);
        ai.addAttribute("", "Y", "", "decimal", "" + coord.y);

        if (!Double.isNaN(coord.getZ())) {
            ai.addAttribute("", "Z", "", "decimal", "" + coord.getZ());
        }

        if (e == null) {
            output.element(GMLSchema.NAMESPACE, "coord", ai);
        } else {
            output.element(e.getNamespace(), e.getName(), ai);
        }
    }

    static void encodeCoords(Element e, CoordinateSequence coords, PrintHandler output)
            throws IOException {
        if ((coords == null) || (coords.size() == 0)) {
            return;
        }

        encodeCoords(e, coords.toCoordinateArray(), output);
    }

    static void encodeCoords(Element e, Coordinate[] coords, PrintHandler output)
            throws IOException {
        if ((coords == null) || (coords.length == 0)) {
            return;
        }

        AttributesImpl ai = new AttributesImpl();
        String dec;
        String cs;
        String ts;
        dec = ".";
        cs = ",";
        ts = " ";
        ai.addAttribute("", "decimal", "", "string", dec);
        ai.addAttribute("", "cs", "", "string", cs);
        ai.addAttribute("", "ts", "", "string", ts);

        if (e == null) {
            output.startElement(GMLSchema.NAMESPACE, "coordinates", ai);
        } else {
            output.startElement(e.getNamespace(), e.getName(), ai);
        }

        Coordinate c = coords[0];

        if (Double.isNaN(c.getZ())) {
            output.characters(c.x + cs + c.y);
        } else {
            output.characters(c.x + cs + c.y + cs + c.getZ());
        }

        for (int i = 1; i < coords.length; i++) {
            c = coords[i];

            if (Double.isNaN(c.getZ())) {
                output.characters(ts + c.x + cs + c.y);
            } else {
                output.characters(ts + c.x + cs + c.y + cs + c.getZ());
            }
        }

        if (e == null) {
            output.endElement(GMLSchema.NAMESPACE, "coordinates");
        } else {
            output.endElement(e.getNamespace(), e.getName());
        }
    }

    static void encodeCoords(Element e, Envelope env, PrintHandler output) throws IOException {
        if ((env == null)) {
            return;
        }

        AttributesImpl ai = new AttributesImpl();
        String dec;
        String cs;
        String ts;
        dec = ".";
        cs = ",";
        ts = " ";
        ai.addAttribute("", "decimal", "", "string", dec);
        ai.addAttribute("", "cs", "", "string", cs);
        ai.addAttribute("", "ts", "", "string", ts);

        if (e == null) {
            output.startElement(GMLSchema.NAMESPACE, "coordinates", ai);
        } else {
            output.startElement(e.getNamespace(), e.getName(), ai);
        }

        output.characters(
                env.getMinX() + cs + env.getMinY() + ts + env.getMaxX() + cs + env.getMaxY());

        if (e == null) {
            output.endElement(GMLSchema.NAMESPACE, "coordinates");
        } else {
            output.endElement(e.getNamespace(), e.getName());
        }
    }

    /**
     * Default implementation, used to pass data to parents in the inheritance tree.
     *
     * @author dzwiers
     * @see ElementValue
     */
    private static class DefaultElementValue implements ElementValue {
        // local data variables
        private Element elem;
        private Object value;

        /** The input method for the data to store. */
        public DefaultElementValue(Element elem, Object value) {
            this.elem = elem;
            this.value = value;
        }

        /** @see schema.ElementValue#getElement() */
        public Element getElement() {
            return elem;
        }

        /** @see schema.ElementValue#getValue() */
        public Object getValue() {
            return value;
        }
        /* (non-Javadoc)
         * @see java.lang.Object#toString()
         */
        public String toString() {
            StringBuffer buf = new StringBuffer();
            if (getElement() != null && getElement().toString() != null) {
                buf.append(getElement().toString());
            } else {
                buf.append(getClass().getName());
            }
            buf.append("[");
            buf.append(value);
            buf.append("]");
            return buf.toString();
        }
    }

    /**
     * Many complexTypes have Choices as part of their definition. Instances of this class are used
     * to represent these choices.
     *
     * @author dzwiers
     * @see Choice
     */
    private static class DefaultChoice implements Choice {
        // the element set to pick one of
        private Element[] elements = null;

        /*
         * Should not be called
         */
        private DefaultChoice() {
            // Should not be called
        }

        /** Initializes this instance with a set of elements to choose from. */
        public DefaultChoice(Element[] elems) {
            elements = elems;
        }

        /** @see schema.Choice#getId() */
        public String getId() {
            return null;
        }

        /** @see schema.Choice#getMaxOccurs() */
        public int getMaxOccurs() {
            return 1;
        }

        /** @see schema.Choice#getMinOccurs() */
        public int getMinOccurs() {
            return 1;
        }

        /** @see schema.Choice#getChildren() */
        public ElementGrouping[] getChildren() {
            return elements;
        }

        /** @see schema.ElementGrouping#getGrouping() */
        public int getGrouping() {
            return CHOICE;
        }

        /** @see schema.ElementGrouping#findChildElement(java.lang.String) */
        public Element findChildElement(String name) {
            if ((elements == null) || (elements.length == 0) || (name == null)) {
                return null;
            }

            for (int i = 0; i < elements.length; i++)
                if (name.equals(elements[i].getName())) {
                    return elements[i];
                }

            return null;
        }

        public Element findChildElement(String localName, URI namespaceURI) {
            if ((elements == null) || (elements.length == 0) || (localName == null)) {
                return null;
            }

            for (int i = 0; i < elements.length; i++)
                if (localName.equals(elements[i].getName())
                        && namespaceURI.equals(elements[i].getNamespace())) {
                    return elements[i];
                }

            return null;
        }
    }

    /**
     * Many complexTypes have Sequences as part of their definition. Instances of this class are
     * used to represent these sequences.
     *
     * @author dzwiers
     */
    private static class DefaultSequence implements Sequence {
        // the list of elements in the sequence (order matters here)
        private Element[] elements = null;

        /*
         * Should not be called
         */
        private DefaultSequence() {
            // Should not be called
        }

        /** Initializes the Sequence with a list of elements within the Sequence */
        public DefaultSequence(Element[] elems) {
            elements = elems;
        }

        /** @see schema.Sequence#getChildren() */
        public ElementGrouping[] getChildren() {
            return elements;
        }

        /** @see schema.Sequence#getId() */
        public String getId() {
            return null;
        }

        /** @see schema.Sequence#getMaxOccurs() */
        public int getMaxOccurs() {
            return 1;
        }

        /** @see schema.Sequence#getMinOccurs() */
        public int getMinOccurs() {
            return 1;
        }

        /** @see schema.ElementGrouping#getGrouping() */
        public int getGrouping() {
            return SEQUENCE;
        }

        /** @see schema.ElementGrouping#findChildElement(java.lang.String) */
        public Element findChildElement(String name) {
            if ((elements == null) || (elements.length == 0) || (name == null)) {
                return null;
            }

            for (int i = 0; i < elements.length; i++)
                if (name.equals(elements[i].getName())) {
                    return elements[i];
                }

            return null;
        }

        public Element findChildElement(String localName, URI namespaceURI) {
            if ((elements == null) || (elements.length == 0) || (localName == null)) {
                return null;
            }

            for (int i = 0; i < elements.length; i++)
                if (localName.equals(elements[i].getName())
                        && namespaceURI.equals(elements[i].getNamespace())) {
                    return elements[i];
                }

            return null;
        }
    }

    /**
     * This class represents an AbstractGeometryType within the GML Schema. This includes both the
     * data and parsing functionality associated with an AbstractGeometryType.
     *
     * @author dzwiers
     * @see GMLComplexType
     * @see ComplexType
     */
    public static class AbstractGeometryType extends GMLComplexType {

        /** @see org.geotools.xml.schema.ComplexType#getChildElements() */
        public Element[] getChildElements() {
            return null;
        }

        // singleton instance
        private static final GMLComplexType instance = new AbstractGeometryType();

        // static list of attributes
        protected static Attribute[] attributes = {
            new GMLSchema.GMLAttribute("gid", XSISimpleTypes.ID.getInstance()),
            new GMLSchema.GMLAttribute("srsName", XSISimpleTypes.AnyURI.getInstance())
        };

        /*
         * part of the singleton pattern
         *
         * @see GMLComplexType#getInstance()
         */
        public static GMLComplexType getInstance() {
            return instance;
        }

        /** @see schema.ComplexType#isAbstract() */
        public boolean isAbstract() {
            return true;
        }

        /** @see schema.ComplexType#getAnyAttributeNameSpace() */
        public String getAnyAttributeNameSpace() {
            return null;
        }

        /** @see schema.ComplexType#getAttributeDescriptors() */
        public Attribute[] getAttributes() {
            return new Attribute[0];
        }

        /** @see schema.ComplexType#getChildren() */
        public ElementGrouping getChild() {
            return new DefaultSequence(new Element[0]);
        }

        /** @see schema.ComplexType#getLocalName() */
        public String getName() {
            return "AbstractGeometryType";
        }

        /**
         * @see org.geotools.xml.xsi.Type#getValue(org.geotools.xml.xsi.Element,
         *     org.geotools.xml.xsi.ElementValue[], org.xml.sax.Attributes)
         */
        public Object getValue(Element element, ElementValue[] value, Attributes attrs, Map hints)
                throws SAXException {
            try {
                Geometry g = (Geometry) value[0].getValue();

                // TODO have someone that knows more help.
                String srsName = attrs.getValue("srsName");

                if ((srsName != null) && !"".equals(srsName)) {
                    // TODO support real coord systems here
                    //                    if(srsName.matches(".*epsg#\\d*")){
                    //                        String[] t = srsName.split(".*epsg#");
                    //                        if(t!=null && t.length==1){
                    //                            String epsg = t[0];
                    //                            CoordinateSystem cs = (new
                    // CoordinateSystemEPSGFactory()).createCoordinateSystem(epsg);
                    //                            g.setUserData(cs);
                    //                        }
                    //                    }
                    g.setUserData(srsName);
                }

                return g;
            } catch (ClassCastException e) {
                // there was an error, this is an abstract type
                throw new SAXException("Expected a Geometry to be passed to this abstract type");

                //            } catch (FactoryException e) {
                //                throw new SAXException(e);
            }
        }

        /** @see org.geotools.xml.xsi.Type#getInstanceType() */
        public Class getInstanceType() {
            return Geometry.class;
        }

        /** @see schema.ComplexType#findChildElement(java.lang.String) */
        public Element findChildElement(String name) {
            return null;
        }

        /**
         * @see org.geotools.xml.schema.Type#canEncode(org.geotools.xml.schema.Element,
         *     java.lang.Object, java.util.Map)
         */
        public boolean canEncode(Element element, Object value, Map hints) {
            if (element.getType() == null || !(value instanceof Geometry)) return false;
            // need to check inheritance ...
            if (element.getType() instanceof SimpleType) return false;
            return true;
        }

        /**
         * @see org.geotools.xml.schema.Type#encode(org.geotools.xml.schema.Element,
         *     java.lang.Object, org.geotools.xml.PrintHandler, java.util.Map)
         */
        public void encode(Element element, Object value, PrintHandler output, Map hints)
                throws IOException, OperationNotSupportedException {
            if (!canEncode(element, value, hints)) throw new OperationNotSupportedException();
            Geometry g = (Geometry) value;
            GMLComplexTypes.encode(null, g, output);
        }
    }

    /**
     * This class represents an AbstractGeometryCollectionBaseType within the GML Schema. This
     * includes both the data and parsing functionality associated with an
     * AbstractGeometryCollectionBaseType.
     *
     * @author dzwiers
     * @see GMLComplexType
     * @see ComplexType
     */
    public static class AbstractGeometryCollectionBaseType extends GMLComplexType {

        /** @see org.geotools.xml.schema.ComplexType#getChildElements() */
        public Element[] getChildElements() {
            return null;
        }

        protected static final Attribute[] attributes = {
            new GMLSchema.GMLAttribute("gid", XSISimpleTypes.ID.getInstance()),
            new GMLSchema.GMLAttribute(
                    "srsName", XSISimpleTypes.AnyURI.getInstance(), Attribute.REQUIRED)
        };

        // singleton instance
        private static final GMLComplexType instance = new AbstractGeometryCollectionBaseType();

        /** @see schema.ComplexType#isAbstract() */
        public boolean isAbstract() {
            return true;
        }

        /** @see schema.ComplexType#getAnyAttributeNameSpace() */
        public String getAnyAttributeNameSpace() {
            return null;
        }

        /** @see schema.ComplexType#getAttributeDescriptors() */
        public Attribute[] getAttributes() {
            return new Attribute[0];
        }

        /** @see schema.ComplexType#getChildren() */
        public ElementGrouping getChild() {
            return new DefaultSequence(new Element[0]);
        }

        /** @see schema.ComplexType#getLocalName() */
        public String getName() {
            return "AbstractGeometryCollectionBaseType";
        }

        /**
         * @see org.geotools.xml.xsi.Type#getValue(org.geotools.xml.xsi.Element,
         *     org.geotools.xml.xsi.ElementValue[], org.xml.sax.Attributes)
         */
        public Object getValue(Element element, ElementValue[] value, Attributes attrs, Map hints)
                throws SAXException {
            try {
                Geometry g = (Geometry) value[0].getValue();

                // TODO have someone that knows more help.
                String srsName = attrs.getValue("srsName");

                if ((srsName != null) && !"".equals(srsName)) {
                    // TODO support real coord systems here
                    //                    if(srsName.matches(".*epsg#\\d*")){
                    //                        String[] t = srsName.split(".*epsg#");
                    //                        if(t!=null && t.length==1){
                    //                            String epsg = t[0];
                    //                            CoordinateSystem cs = (new
                    // CoordinateSystemEPSGFactory()).createCoordinateSystem(epsg);
                    //                            g.setUserData(cs);
                    //                        }
                    //                    }
                    g.setUserData(srsName);
                }

                return g;
            } catch (ClassCastException e) {
                // there was an error, this is an abstract type
                throw new SAXException("Expected a Geometry to be passed to this abstract type");

                //            } catch (FactoryException e) {
                //                throw new SAXException(e);
            }
        }

        /** @see org.geotools.xml.xsi.Type#getInstanceType() */
        public Class getInstanceType() {
            return Geometry.class;
        }

        /*
         * part of the singleton pattern
         *
         * @see GMLComplexType#getInstance()
         */
        public static GMLComplexType getInstance() {
            return instance;
        }

        /** @see schema.ComplexType#findChildElement(java.lang.String) */
        public Element findChildElement(String name) {
            return null;
        }

        /**
         * @see org.geotools.xml.schema.Type#canEncode(org.geotools.xml.schema.Element,
         *     java.lang.Object, java.util.Map)
         */
        public boolean canEncode(Element element, Object value, Map hints) {
            return false;
        }

        /**
         * @see org.geotools.xml.schema.Type#encode(org.geotools.xml.schema.Element,
         *     java.lang.Object, org.geotools.xml.PrintHandler, java.util.Map)
         */
        public void encode(Element element, Object value, PrintHandler output, Map hints)
                throws OperationNotSupportedException {
            throw new OperationNotSupportedException();
        }
    }

    /**
     * This class represents an GeometryAssociationType within the GML Schema. This includes both
     * the data and parsing functionality associated with an GeometryAssociationType.
     *
     * @author dzwiers
     * @see GMLComplexType
     * @see ComplexType
     */
    public static class GeometryAssociationType extends GMLComplexType {

        /** @see org.geotools.xml.schema.ComplexType#getChildElements() */
        public Element[] getChildElements() {
            return elems;
        }

        // singleton instance
        private static final GMLComplexType instance = new GeometryAssociationType();

        // the static attribute list
        protected static final Attribute[] attributes = loadAttributes();

        // the static element list
        private static final Element[] elems = {
            new GMLElement(
                    "_Geometry",
                    GMLComplexTypes.AbstractGeometryType.getInstance(),
                    0,
                    1,
                    true,
                    null),
        };

        // static child sequence
        private static final DefaultSequence elements = new DefaultSequence(elems);

        /*
         * part of the singleton pattern
         *
         * @see GMLComplexType#getInstance()
         */
        public static GMLComplexType getInstance() {
            return instance;
        }

        /** @see schema.ComplexType#isAbstract() */
        public boolean isAbstract() {
            return false;
        }

        /** @see schema.ComplexType#getAnyAttributeNameSpace() */
        public String getAnyAttributeNameSpace() {
            return null;
        }

        /*
         * statically loads the attributes which GeometryAssociations include
         */
        private static Attribute[] loadAttributes() {
            Attribute[] gp = XLinkSchema.SimpleLink.getInstance().getAttributes();
            Attribute[] r = new Attribute[gp.length + 1];

            for (int i = 1; i < gp.length; i++) r[i] = gp[i];

            r[gp.length] = AttributeList.attributes1[0];

            return r;
        }

        /** @see schema.ComplexType#getAttributeDescriptors() */
        public Attribute[] getAttributes() {
            return attributes;
        }

        /** @see schema.ComplexType#getChildren() */
        public ElementGrouping getChild() {
            return elements;
        }

        /** @see schema.ComplexType#getLocalName() */
        public String getName() {
            return "GeometryAssociationType";
        }

        /** @see schema.Type#getValue(java.util.List) */
        public Object getValue(Element element, ElementValue[] value, Attributes attrs, Map hints)
                throws SAXException {
            if ((value == null) || (value.length == 0) || (value[0] == null)) {
                return null; // do nothing ... this is allowed
            }

            if (value.length > 1) {
                throw new SAXException("Cannot have more than one geom per " + getName());
            }

            Element e = value[0].getElement();

            if (e == null) {
                throw new SAXException(
                        "Internal error, ElementValues require an associated Element.");
            }

            return (Geometry) value[0].getValue();
        }

        public Class getInstanceType() {
            return Geometry.class;
        }

        /** @see schema.ComplexType#findChildElement(java.lang.String) */
        public Element findChildElement(String name) {
            if (name == null) {
                return null;
            }

            for (int i = 0; i < elems.length; i++)
                if (name.equals(elems[i].getName())) {
                    return elems[i];
                }

            return null;
        }

        /**
         * @see org.geotools.xml.schema.Type#canEncode(org.geotools.xml.schema.Element,
         *     java.lang.Object, java.util.Map)
         */
        public boolean canEncode(Element element, Object value, Map hints) {
            ComplexType t =
                    (element.getType() instanceof ComplexType)
                            ? (ComplexType) element.getType()
                            : null;

            while ((t != null) && (t != this))
                t = (t.getParent() instanceof ComplexType) ? (ComplexType) t.getParent() : null;

            return ((t != null) && (value instanceof Geometry));
        }

        /**
         * @see org.geotools.xml.schema.Type#encode(org.geotools.xml.schema.Element,
         *     java.lang.Object, org.geotools.xml.PrintHandler, java.util.Map)
         */
        public void encode(Element element, Object value, PrintHandler output, Map hints)
                throws IOException, OperationNotSupportedException {
            if (!canEncode(element, value, hints)) {
                throw new OperationNotSupportedException("Cannot encode");
            }

            Geometry g = (Geometry) value;

            output.startElement(element.getNamespace(), element.getName(), null);
            GMLComplexTypes.encode(null, g, output);
            output.endElement(element.getNamespace(), element.getName());
        }
    }

    /**
     * This class represents an PointMemberType within the GML Schema. This includes both the data
     * and parsing functionality associated with an PointMemberType.
     *
     * @author dzwiers
     * @see GMLComplexType
     * @see ComplexType
     */
    public static class PointMemberType extends GMLComplexType {

        /** @see org.geotools.xml.schema.ComplexType#getChildElements() */
        public Element[] getChildElements() {
            return elements;
        }

        // singleton instance
        private static final GMLComplexType instance = new PointMemberType();

        // static list of attributes
        private static final Attribute[] attributes = loadAttributes();

        // static list of elements
        private static final Element[] elements = {
            new GMLElement(
                    "Point",
                    GMLComplexTypes.PointType.getInstance(),
                    0,
                    1,
                    false,
                    new GMLElement(
                            "_Geometry",
                            GMLComplexTypes.AbstractGeometryType.getInstance(),
                            1,
                            1,
                            true,
                            null)),
        };

        // static sequence
        private static final DefaultSequence seq = new DefaultSequence(elements);

        /*
         * part of the singleton pattern
         *
         * @see GMLComplexType#getInstance()
         */
        public static GMLComplexType getInstance() {
            return instance;
        }

        /** @see schema.ComplexType#isAbstract() */
        public boolean isAbstract() {
            return false;
        }

        /** @see schema.ComplexType#getAnyAttributeNameSpace() */
        public String getAnyAttributeNameSpace() {
            return null;
        }

        /*
         * statically loads the attributes required for a PointMember
         */
        private static Attribute[] loadAttributes() {
            Attribute[] parent = GeometryAssociationType.attributes;
            Attribute[] gp = GMLSchema.GMLAssociationAttributeGroup.attributes1;
            Attribute[] r = new Attribute[parent.length + gp.length];

            for (int i = 0; i < parent.length; i++) r[i] = parent[i];

            for (int i = 0; i < gp.length; i++) r[i + parent.length] = gp[i];

            return r;
        }

        /** @see schema.ComplexType#getAttributeDescriptors() */
        public Attribute[] getAttributes() {
            return attributes;
        }

        /** @see schema.ComplexType#getChildren() */
        public ElementGrouping getChild() {
            return seq;
        }

        /** @see schema.ComplexType#getLocalName() */
        public String getName() {
            return "PointMemberType";
        }

        /** @see schema.Type#getValue(java.util.List) */
        public Object getValue(Element element, ElementValue[] value, Attributes attrs, Map hints)
                throws SAXException {
            if ((value == null) || (value.length == 0) || (value[0] == null)) {
                return null; // do nothing ... this is allowed
            }

            if (value.length > 1) {
                throw new SAXException("Cannot have more than one geom per " + getName());
            }

            Element e = value[0].getElement();

            if (e == null) {
                throw new SAXException(
                        "Internal error, ElementValues require an associated Element.");
            }

            return (Point) value[0].getValue();
        }

        /** @see org.geotools.xml.xsi.Type#getInstanceType() */
        public Class getInstanceType() {
            return Point.class;
        }

        /** @see schema.ComplexType#findChildElement(java.lang.String) */
        public Element findChildElement(String name) {
            if (name == null) {
                return null;
            }

            for (int i = 0; i < elements.length; i++)
                if (name.equals(elements[i].getName())) {
                    return elements[i];
                }

            return null;
        }

        /**
         * @see org.geotools.xml.schema.Type#canEncode(org.geotools.xml.schema.Element,
         *     java.lang.Object, java.util.Map)
         */
        public boolean canEncode(Element element, Object value, Map hints) {
            ComplexType t =
                    (element.getType() instanceof ComplexType)
                            ? (ComplexType) element.getType()
                            : null;

            while ((t != null) && (t != this))
                t = (t.getParent() instanceof ComplexType) ? (ComplexType) t.getParent() : null;

            return ((t != null) && (value instanceof Geometry));
        }

        /**
         * @see org.geotools.xml.schema.Type#encode(org.geotools.xml.schema.Element,
         *     java.lang.Object, org.geotools.xml.PrintHandler, java.util.Map)
         */
        public void encode(Element element, Object value, PrintHandler output, Map hints)
                throws IOException, OperationNotSupportedException {
            if (!canEncode(element, value, hints)) {
                throw new OperationNotSupportedException("Cannot encode");
            }

            Point g = (Point) value;

            output.startElement(element.getNamespace(), element.getName(), null);
            GMLComplexTypes.encode(null, g, output);
            output.endElement(element.getNamespace(), element.getName());
        }
    }

    /**
     * This class represents an LineStringMemberType within the GML Schema. This includes both the
     * data and parsing functionality associated with an LineStringMemberType.
     *
     * @author dzwiers
     * @see GMLComplexType
     * @see ComplexType
     */
    public static class LineStringMemberType extends GMLComplexType {

        /** @see org.geotools.xml.schema.ComplexType#getChildElements() */
        public Element[] getChildElements() {
            return elements;
        }

        // singleton instance
        private static final GMLComplexType instance = new LineStringMemberType();

        // static list of attributes
        private static final Attribute[] attributes = loadAttributes();

        // static list of elements
        private static final Element[] elements = {
            new GMLElement(
                    "LineString",
                    GMLComplexTypes.LineStringType.getInstance(),
                    0,
                    1,
                    false,
                    new GMLElement(
                            "_Geometry",
                            GMLComplexTypes.AbstractGeometryType.getInstance(),
                            1,
                            1,
                            true,
                            null)),
        };

        // static sequence
        private static final DefaultSequence seq = new DefaultSequence(elements);

        /*
         * part of the singleton pattern
         *
         * @see GMLComplexType#getInstance()
         */
        public static GMLComplexType getInstance() {
            return instance;
        }

        /** @see schema.ComplexType#isAbstract() */
        public boolean isAbstract() {
            return false;
        }

        /** @see schema.ComplexType#getAnyAttributeNameSpace() */
        public String getAnyAttributeNameSpace() {
            return null;
        }

        /*
         * statically loads the attributes required for a LineStringMember
         */
        private static Attribute[] loadAttributes() {
            Attribute[] parent = GeometryAssociationType.attributes;
            Attribute[] gp = GMLSchema.GMLAssociationAttributeGroup.attributes1;
            Attribute[] r = new Attribute[parent.length + gp.length];

            for (int i = 0; i < parent.length; i++) r[i] = parent[i];

            for (int i = 0; i < gp.length; i++) r[i + parent.length] = gp[i];

            return r;
        }

        /** @see schema.ComplexType#getAttributeDescriptors() */
        public Attribute[] getAttributes() {
            return attributes;
        }

        /** @see schema.ComplexType#getChildren() */
        public ElementGrouping getChild() {
            return seq;
        }

        /** @see schema.ComplexType#getLocalName() */
        public String getName() {
            return "LineStringMemberType";
        }

        /** @see schema.Type#getValue(java.util.List) */
        public Object getValue(Element element, ElementValue[] value, Attributes attrs, Map hints)
                throws SAXException {
            if ((value == null) || (value.length == 0) || (value[0] == null)) {
                return null; // do nothing ... this is allowed
            }

            if (value.length > 1) {
                throw new SAXException("Cannot have more than one geom per " + getName());
            }

            Element e = value[0].getElement();

            if (e == null) {
                if (!element.isNillable())
                    throw new SAXException(
                            "Internal error, ElementValues require an associated Element.");
                return null;
            }

            return (LineString) value[0].getValue();
        }

        public Class getInstanceType() {
            return LineString.class;
        }

        /** @see schema.ComplexType#findChildElement(java.lang.String) */
        public Element findChildElement(String name) {
            if (name == null) {
                return null;
            }

            for (int i = 0; i < elements.length; i++)
                if (name.equals(elements[i].getName())) {
                    return elements[i];
                }

            return null;
        }

        /**
         * @see org.geotools.xml.schema.Type#canEncode(org.geotools.xml.schema.Element,
         *     java.lang.Object, java.util.Map)
         */
        public boolean canEncode(Element element, Object value, Map hints) {
            ComplexType t =
                    (element.getType() instanceof ComplexType)
                            ? (ComplexType) element.getType()
                            : null;

            while ((t != null) && (t != this))
                t = (t.getParent() instanceof ComplexType) ? (ComplexType) t.getParent() : null;

            return ((t != null) && (value instanceof LineString));
        }

        /**
         * @see org.geotools.xml.schema.Type#encode(org.geotools.xml.schema.Element,
         *     java.lang.Object, org.geotools.xml.PrintHandler, java.util.Map)
         */
        public void encode(Element element, Object value, PrintHandler output, Map hints)
                throws IOException, OperationNotSupportedException {
            if (!canEncode(element, value, hints)) {
                throw new OperationNotSupportedException("Cannot encode");
            }

            LineString g = (LineString) value;

            output.startElement(element.getNamespace(), element.getName(), null);
            GMLComplexTypes.encode(null, g, output);
            output.endElement(element.getNamespace(), element.getName());
        }
    }

    /**
     * This class represents an PolygonMemberType within the GML Schema. This includes both the data
     * and parsing functionality associated with an PolygonMemberType.
     *
     * @author dzwiers
     * @see GMLComplexType
     * @see ComplexType
     */
    public static class PolygonMemberType extends GMLComplexType {

        /** @see org.geotools.xml.schema.ComplexType#getChildElements() */
        public Element[] getChildElements() {
            return elements;
        }

        // singleton instance
        private static final GMLComplexType instance = new PolygonMemberType();

        // static attribute list
        private static final Attribute[] attributes = loadAttributes();

        // static list of elements
        private static final Element[] elements = {
            new GMLElement(
                    "Polygon",
                    GMLComplexTypes.PolygonType.getInstance(),
                    0,
                    1,
                    false,
                    new GMLElement(
                            "_Geometry",
                            GMLComplexTypes.AbstractGeometryType.getInstance(),
                            1,
                            1,
                            true,
                            null)),
        };

        // static sequence
        private static final DefaultSequence seq = new DefaultSequence(elements);

        /*
         * part of the singleton pattern
         *
         * @see GMLComplexType#getInstance()
         */
        public static GMLComplexType getInstance() {
            return instance;
        }

        /** @see schema.ComplexType#isAbstract() */
        public boolean isAbstract() {
            return false;
        }

        /** @see schema.ComplexType#getAnyAttributeNameSpace() */
        public String getAnyAttributeNameSpace() {
            return null;
        }

        /*
         * statically loads the attributes required for a PolygonMember
         */
        private static Attribute[] loadAttributes() {
            Attribute[] parent = GeometryAssociationType.attributes;
            Attribute[] gp = GMLSchema.GMLAssociationAttributeGroup.attributes1;
            Attribute[] r = new Attribute[parent.length + gp.length];

            for (int i = 0; i < parent.length; i++) r[i] = parent[i];

            for (int i = 0; i < gp.length; i++) r[i + parent.length] = gp[i];

            return r;
        }

        /** @see schema.ComplexType#getAttributeDescriptors() */
        public Attribute[] getAttributes() {
            return attributes;
        }

        /** @see schema.ComplexType#getChildren() */
        public ElementGrouping getChild() {
            return seq;
        }

        /** @see schema.ComplexType#getLocalName() */
        public String getName() {
            return "PolygonMemberType";
        }

        /** @see schema.Type#getValue(java.util.List) */
        public Object getValue(Element element, ElementValue[] value, Attributes attrs, Map hints)
                throws SAXException {
            if ((value == null) || (value.length == 0) || (value[0] == null)) {
                return null; // do nothing ... this is allowed
            }

            if (value.length > 1) {
                throw new SAXException("Cannot have more than one geom per " + getName());
            }

            Element e = value[0].getElement();

            if (e == null) {
                if (!element.isNillable())
                    throw new SAXException(
                            "Internal error, ElementValues require an associated Element.");
                return null;
            }

            return (Polygon) value[0].getValue();
        }

        /** @see org.geotools.xml.xsi.Type#getInstanceType() */
        public Class getInstanceType() {
            return Polygon.class;
        }

        /** @see schema.ComplexType#findChildElement(java.lang.String) */
        public Element findChildElement(String name) {
            if (name == null) {
                return null;
            }

            for (int i = 0; i < elements.length; i++)
                if (name.equals(elements[i].getName())) {
                    return elements[i];
                }

            return null;
        }

        /**
         * @see org.geotools.xml.schema.Type#canEncode(org.geotools.xml.schema.Element,
         *     java.lang.Object, java.util.Map)
         */
        public boolean canEncode(Element element, Object value, Map hints) {
            ComplexType t =
                    (element.getType() instanceof ComplexType)
                            ? (ComplexType) element.getType()
                            : null;

            while ((t != null) && (t != this))
                t = (t.getParent() instanceof ComplexType) ? (ComplexType) t.getParent() : null;

            return ((t != null) && (value instanceof Polygon));
        }

        /**
         * @see org.geotools.xml.schema.Type#encode(org.geotools.xml.schema.Element,
         *     java.lang.Object, org.geotools.xml.PrintHandler, java.util.Map)
         */
        public void encode(Element element, Object value, PrintHandler output, Map hints)
                throws IOException, OperationNotSupportedException {
            if (!canEncode(element, value, hints)) {
                throw new OperationNotSupportedException("Cannot encode");
            }

            Polygon g = (Polygon) value;

            output.startElement(element.getNamespace(), element.getName(), null);
            GMLComplexTypes.encode(null, g, output);
            output.endElement(element.getNamespace(), element.getName());
        }
    }

    /**
     * This class represents an LinearRingMemberType within the GML Schema. This includes both the
     * data and parsing functionality associated with an LinearRingMemberType.
     *
     * @author dzwiers
     * @see GMLComplexType
     * @see ComplexType
     */
    public static class LinearRingMemberType extends GMLComplexType {

        /** @see org.geotools.xml.schema.ComplexType#getChildElements() */
        public Element[] getChildElements() {
            return elements;
        }

        // singleton instance
        private static final GMLComplexType instance = new LinearRingMemberType();

        // static attribute list
        private static final Attribute[] attributes = loadAttributes();

        // static element list
        private static final Element[] elements = {
            new GMLElement(
                    "LinearRing",
                    GMLComplexTypes.LinearRingType.getInstance(),
                    0,
                    1,
                    false,
                    new GMLElement(
                            "_Geometry",
                            GMLComplexTypes.AbstractGeometryType.getInstance(),
                            1,
                            1,
                            true,
                            null)),
        };

        // static sequence
        private static final DefaultSequence seq = new DefaultSequence(elements);

        /*
         * part of the singleton pattern
         *
         * @see GMLComplexType#getInstance()
         */
        public static GMLComplexType getInstance() {
            return instance;
        }

        /** @see schema.ComplexType#isAbstract() */
        public boolean isAbstract() {
            return false;
        }

        /** @see schema.ComplexType#getAnyAttributeNameSpace() */
        public String getAnyAttributeNameSpace() {
            return null;
        }

        /*
         * statically loads the attributes required for a LinearRingMember
         */
        private static Attribute[] loadAttributes() {
            Attribute[] parent = GeometryAssociationType.attributes;
            Attribute[] gp = GMLSchema.GMLAssociationAttributeGroup.attributes1;
            Attribute[] r = new Attribute[parent.length + gp.length];

            for (int i = 0; i < parent.length; i++) r[i] = parent[i];

            for (int i = 0; i < gp.length; i++) r[i + parent.length] = gp[i];

            return r;
        }

        /** @see schema.ComplexType#getAttributeDescriptors() */
        public Attribute[] getAttributes() {
            return attributes;
        }

        /** @see schema.ComplexType#getChildren() */
        public ElementGrouping getChild() {
            return seq;
        }

        /** @see schema.ComplexType#getLocalName() */
        public String getName() {
            return "LinearRingMemberType";
        }

        /** @see schema.Type#getValue(java.util.List) */
        public Object getValue(Element element, ElementValue[] value, Attributes attrs, Map hints)
                throws SAXException {
            if ((value == null) || (value.length == 0) || (value[0] == null)) {
                return null; // do nothing ... this is allowed
            }

            if (value.length > 1) {
                throw new SAXException("Cannot have more than one geom per " + getName());
            }

            Element e = value[0].getElement();

            if (e == null) {
                if (!element.isNillable())
                    throw new SAXException(
                            "Internal error, ElementValues require an associated Element.");
                return null;
            }
            return (LinearRing) value[0].getValue();
        }

        /** @see org.geotools.xml.xsi.Type#getInstanceType() */
        public Class getInstanceType() {
            return LinearRing.class;
        }

        /** @see schema.ComplexType#findChildElement(java.lang.String) */
        public Element findChildElement(String name) {
            if (name == null) {
                return null;
            }

            for (int i = 0; i < elements.length; i++)
                if (name.equals(elements[i].getName())) {
                    return elements[i];
                }

            return null;
        }

        /**
         * @see org.geotools.xml.schema.Type#canEncode(org.geotools.xml.schema.Element,
         *     java.lang.Object, java.util.Map)
         */
        public boolean canEncode(Element element, Object value, Map hints) {
            ComplexType t =
                    (element.getType() instanceof ComplexType)
                            ? (ComplexType) element.getType()
                            : null;

            while ((t != null) && (t != this))
                t = (t.getParent() instanceof ComplexType) ? (ComplexType) t.getParent() : null;

            return ((t != null) && (value instanceof LinearRing));
        }

        /**
         * @see org.geotools.xml.schema.Type#encode(org.geotools.xml.schema.Element,
         *     java.lang.Object, org.geotools.xml.PrintHandler, java.util.Map)
         */
        public void encode(Element element, Object value, PrintHandler output, Map hints)
                throws IOException, OperationNotSupportedException {
            if (!canEncode(element, value, hints)) {
                throw new OperationNotSupportedException("Cannot encode");
            }

            LinearRing g = (LinearRing) value;

            output.startElement(element.getNamespace(), element.getName(), null);
            GMLComplexTypes.encode(null, g, output);
            output.endElement(element.getNamespace(), element.getName());
        }
    }

    /**
     * This class represents an PointType within the GML Schema. This includes both the data and
     * parsing functionality associated with an PointType.
     *
     * @author dzwiers
     * @see GMLComplexType
     * @see ComplexType
     */
    public static class PointType extends GMLComplexType {

        /** @see org.geotools.xml.schema.ComplexType#getChildElements() */
        public Element[] getChildElements() {
            return elements;
        }

        // singleton instance
        private static final GMLComplexType instance = new PointType();

        // static element list
        private static final Element[] elements = {
            new GMLElement("coord", GMLComplexTypes.CoordType.getInstance(), 1, 1, false, null),
            new GMLElement(
                    "coordinates", GMLComplexTypes.CoordinatesType.getInstance(), 1, 1, false, null)
        };

        // static choice
        private static final DefaultChoice seq = new DefaultChoice(elements);

        /*
         * part of the singleton pattern
         *
         * @see GMLComplexType#getInstance()
         */
        public static GMLComplexType getInstance() {
            return instance;
        }

        /** @see schema.ComplexType#isAbstract() */
        public boolean isAbstract() {
            return false;
        }

        /** @see schema.ComplexType#getAnyAttributeNameSpace() */
        public String getAnyAttributeNameSpace() {
            return null;
        }

        /** @see schema.ComplexType#getAttributeDescriptors() */
        public Attribute[] getAttributes() {
            return AbstractGeometryType.attributes;
        }

        /** @see schema.ComplexType#getChildren() */
        public ElementGrouping getChild() {
            return seq;
        }

        /** @see schema.ComplexType#getLocalName() */
        public String getName() {
            return "PointType";
        }

        /** @see schema.Type#getValue(java.util.List) */
        public Object getValue(Element element, ElementValue[] value, Attributes attrs, Map hints)
                throws OperationNotSupportedException, SAXException {
            if (value.length > 1) {
                throw new SAXException("Cannot have more than one coord per " + getName());
            }

            Element e = value[0].getElement();

            if (e == null) {
                if (!element.isNillable())
                    throw new SAXException(
                            "Internal error, ElementValues require an associated Element.");
                return null;
            }

            Object t = value[0].getValue();
            Point p = null;

            if (t == null) {
                throw new SAXException("Invalid coordinate specified");
            }

            GeometryFactory gf = new GeometryFactory(CoordinateArraySequenceFactory.instance());

            if (t instanceof Coordinate) {
                Coordinate c = (Coordinate) t;
                p = gf.createPoint(c);
            } else {
                CoordinateSequence c = (CoordinateSequence) t;
                p = gf.createPoint(c);
            }

            ElementValue[] ev = new ElementValue[1];
            ev[0] = new DefaultElementValue(element, p);

            return AbstractGeometryType.getInstance().getValue(element, ev, attrs, hints);
        }

        /** @see org.geotools.xml.xsi.Type#getInstanceType() */
        public Class getInstanceType() {
            return Point.class;
        }

        /** @see schema.ComplexType#findChildElement(java.lang.String) */
        public Element findChildElement(String name) {
            if (name == null) {
                return null;
            }

            for (int i = 0; i < elements.length; i++)
                if (name.equals(elements[i].getName())) {
                    return elements[i];
                }

            return null;
        }

        /**
         * @see org.geotools.xml.schema.Type#canEncode(org.geotools.xml.schema.Element,
         *     java.lang.Object, java.util.Map)
         */
        public boolean canEncode(Element element, Object value, Map hints) {
            ComplexType t =
                    (element.getType() instanceof ComplexType)
                            ? (ComplexType) element.getType()
                            : null;

            while ((t != null) && (t != this))
                t = (t.getParent() instanceof ComplexType) ? (ComplexType) t.getParent() : null;

            return ((t != null) && (value instanceof Point));
        }

        /**
         * @see org.geotools.xml.schema.Type#encode(org.geotools.xml.schema.Element,
         *     java.lang.Object, org.geotools.xml.PrintHandler, java.util.Map)
         */
        public void encode(Element element, Object value, PrintHandler output, Map hints)
                throws IOException, OperationNotSupportedException {
            if (!canEncode(element, value, hints)) {
                throw new OperationNotSupportedException("Cannot encode");
            }

            Point g = (Point) value;

            GMLComplexTypes.encode(element, g, output);
        }
    }

    /**
     * This class represents an LineStringType within the GML Schema. This includes both the data
     * and parsing functionality associated with an LineStringType.
     *
     * @author dzwiers
     * @see GMLComplexType
     * @see ComplexType
     */
    public static class LineStringType extends GMLComplexType {

        /** @see org.geotools.xml.schema.ComplexType#getChildElements() */
        public Element[] getChildElements() {
            return elements;
        }

        // singleton instance
        private static final GMLComplexType instance = new LineStringType();

        // static element list
        private static final Element[] elements = {
            new GMLElement(
                    "coord",
                    GMLComplexTypes.CoordType.getInstance(),
                    2,
                    ElementGrouping.UNBOUNDED,
                    false,
                    null),
            new GMLElement(
                    "coordinates", GMLComplexTypes.CoordinatesType.getInstance(), 1, 1, false, null)
        };

        // static choice
        private static final DefaultChoice seq = new DefaultChoice(elements);

        /*
         * part of the singleton pattern
         *
         * @see GMLComplexType#getInstance()
         */
        public static GMLComplexType getInstance() {
            return instance;
        }

        /** @see schema.ComplexType#isAbstract() */
        public boolean isAbstract() {
            return false;
        }

        /** @see schema.ComplexType#getAnyAttributeNameSpace() */
        public String getAnyAttributeNameSpace() {
            return null;
        }

        /** @see schema.ComplexType#getAttributeDescriptors() */
        public Attribute[] getAttributes() {
            return AbstractGeometryType.attributes;
        }

        /** @see schema.ComplexType#getChildren() */
        public ElementGrouping getChild() {
            return seq;
        }

        /** @see schema.ComplexType#getLocalName() */
        public String getName() {
            return "LineStringType";
        }

        /** @see schema.Type#getValue(java.util.List) */
        public Object getValue(Element element, ElementValue[] value, Attributes attrs, Map hints)
                throws OperationNotSupportedException, SAXException {
            Element e = value[0].getElement();

            if (e == null) {
                if (!element.isNillable())
                    throw new SAXException(
                            "Internal error, ElementValues require an associated Element.");
                return null;
            }

            Object t = value[0].getValue();
            LineString p = null;

            if (t == null) {
                throw new SAXException("Invalid coordinate specified");
            }

            GeometryFactory gf = new GeometryFactory(CoordinateArraySequenceFactory.instance());

            if (t instanceof Coordinate) {
                if (value.length < 2) {
                    throw new SAXException("Cannot have more than one coord per " + getName());
                }

                // there should be more
                Coordinate[] c = new Coordinate[value.length];

                for (int i = 0; i < c.length; i++) c[i] = (Coordinate) value[i].getValue();

                p = gf.createLineString(c);
            } else {
                if (value.length > 1) {
                    throw new SAXException(
                            "Cannot have more than one coordinate sequence per " + getName());
                }

                CoordinateSequence c = (CoordinateSequence) t;
                // TODO -- be forgiving
                if (c.size() == 1) {
                    c =
                            CoordinateArraySequenceFactory.instance()
                                    .create(
                                            new Coordinate[] {
                                                c.getCoordinate(0), c.getCoordinate(0)
                                            });
                }
                p = gf.createLineString(c);
            }

            ElementValue[] ev = new ElementValue[1];
            ev[0] = new DefaultElementValue(element, p);

            return AbstractGeometryType.getInstance().getValue(element, ev, attrs, hints);
        }

        /** @see org.geotools.xml.xsi.Type#getInstanceType() */
        public Class getInstanceType() {
            return LineString.class;
        }

        /** @see schema.ComplexType#findChildElement(java.lang.String) */
        public Element findChildElement(String name) {
            if (name == null) {
                return null;
            }

            for (int i = 0; i < elements.length; i++)
                if (name.equals(elements[i].getName())) {
                    return elements[i];
                }

            return null;
        }

        /**
         * @see org.geotools.xml.schema.Type#canEncode(org.geotools.xml.schema.Element,
         *     java.lang.Object, java.util.Map)
         */
        public boolean canEncode(Element element, Object value, Map hints) {
            ComplexType t =
                    (element.getType() instanceof ComplexType)
                            ? (ComplexType) element.getType()
                            : null;

            while ((t != null) && (t != this))
                t = (t.getParent() instanceof ComplexType) ? (ComplexType) t.getParent() : null;

            return ((t != null) && (value instanceof LineString));
        }

        /**
         * @see org.geotools.xml.schema.Type#encode(org.geotools.xml.schema.Element,
         *     java.lang.Object, org.geotools.xml.PrintHandler, java.util.Map)
         */
        public void encode(Element element, Object value, PrintHandler output, Map hints)
                throws IOException, OperationNotSupportedException {
            if (!canEncode(element, value, hints)) {
                throw new OperationNotSupportedException("Cannot encode");
            }

            LineString g = (LineString) value;

            GMLComplexTypes.encode(element, g, output);
        }
    }

    /**
     * This class represents an LinearRingType within the GML Schema. This includes both the data
     * and parsing functionality associated with an LinearRingType.
     *
     * @author dzwiers
     * @see GMLComplexType
     * @see ComplexType
     */
    public static class LinearRingType extends GMLComplexType {

        /** @see org.geotools.xml.schema.ComplexType#getChildElements() */
        public Element[] getChildElements() {
            return elements;
        }

        // singleton instance
        private static final GMLComplexType instance = new LinearRingType();

        // static element list
        private static final Element[] elements = {
            new GMLElement("coord", GMLComplexTypes.CoordType.getInstance(), 2, 4, false, null),
            new GMLElement(
                    "coordinates", GMLComplexTypes.CoordinatesType.getInstance(), 1, 1, false, null)
        };

        // static sequence
        private static final DefaultChoice seq = new DefaultChoice(elements);

        /*
         * part of the singleton pattern
         *
         * @see GMLComplexType#getInstance()
         */
        public static GMLComplexType getInstance() {
            return instance;
        }

        /** @see schema.ComplexType#isAbstract() */
        public boolean isAbstract() {
            return false;
        }

        /** @see schema.ComplexType#getAnyAttributeNameSpace() */
        public String getAnyAttributeNameSpace() {
            return null;
        }

        /** @see schema.ComplexType#getAttributeDescriptors() */
        public Attribute[] getAttributes() {
            return AbstractGeometryType.attributes;
        }

        /** @see schema.ComplexType#getChildren() */
        public ElementGrouping getChild() {
            return seq;
        }

        /** @see schema.ComplexType#getLocalName() */
        public String getName() {
            return "LinearRingType";
        }

        /** @see schema.Type#getValue(java.util.List) */
        public Object getValue(Element element, ElementValue[] value, Attributes attrs, Map hints)
                throws OperationNotSupportedException, SAXException {
            Element e = value[0].getElement();

            if (e == null) {
                if (!element.isNillable())
                    throw new SAXException(
                            "Internal error, ElementValues require an associated Element.");
                return null;
            }

            Object t = value[0].getValue();
            LinearRing p = null;

            if (t == null) {
                throw new SAXException("Invalid coordinate specified");
            }

            GeometryFactory gf = new GeometryFactory(CoordinateArraySequenceFactory.instance());

            if (t instanceof Coordinate) {
                if (value.length < 4) {
                    throw new SAXException("Cannot have more than one coord per " + getName());
                }

                // there should be more
                Coordinate[] c = new Coordinate[value.length];

                for (int i = 0; i < c.length; i++) c[i] = (Coordinate) value[i].getValue();

                p = gf.createLinearRing(c);
            } else {
                if (value.length > 1) {
                    throw new SAXException(
                            "Cannot have more than one coordinate sequence per " + getName());
                }

                CoordinateSequence c = (CoordinateSequence) t;
                p = gf.createLinearRing(c);
            }

            ElementValue[] ev = new ElementValue[1];
            ev[0] = new DefaultElementValue(element, p);

            return AbstractGeometryType.getInstance().getValue(element, ev, attrs, hints);
        }

        /** @see org.geotools.xml.xsi.Type#getInstanceType() */
        public Class getInstanceType() {
            return LinearRing.class;
        }

        /** @see schema.ComplexType#findChildElement(java.lang.String) */
        public Element findChildElement(String name) {
            if (name == null) {
                return null;
            }

            for (int i = 0; i < elements.length; i++)
                if (name.equals(elements[i].getName())) {
                    return elements[i];
                }

            return null;
        }

        /**
         * @see org.geotools.xml.schema.Type#canEncode(org.geotools.xml.schema.Element,
         *     java.lang.Object, java.util.Map)
         */
        public boolean canEncode(Element element, Object value, Map hints) {
            ComplexType t =
                    (element.getType() instanceof ComplexType)
                            ? (ComplexType) element.getType()
                            : null;

            while ((t != null) && (t != this))
                t = (t.getParent() instanceof ComplexType) ? (ComplexType) t.getParent() : null;

            return ((t != null) && (value instanceof LinearRing));
        }

        /**
         * @see org.geotools.xml.schema.Type#encode(org.geotools.xml.schema.Element,
         *     java.lang.Object, org.geotools.xml.PrintHandler, java.util.Map)
         */
        public void encode(Element element, Object value, PrintHandler output, Map hints)
                throws IOException, OperationNotSupportedException {
            if (!canEncode(element, value, hints)) {
                throw new OperationNotSupportedException("Cannot encode");
            }

            LinearRing g = (LinearRing) value;

            GMLComplexTypes.encode(element, g, output);
        }
    }

    /**
     * This class represents an BoxType within the GML Schema. This includes both the data and
     * parsing functionality associated with an BoxType.
     *
     * @author dzwiers
     * @see GMLComplexType
     * @see ComplexType
     */
    public static class BoxType extends GMLComplexType {

        /** @see org.geotools.xml.schema.ComplexType#getChildElements() */
        public Element[] getChildElements() {
            return elements;
        }

        // singleton instance
        private static final GMLComplexType instance = new BoxType();

        // static element list
        private static final Element[] elements = {
            new GMLElement("coord", GMLComplexTypes.CoordType.getInstance(), 2, 2, false, null),
            new GMLElement(
                    "coordinates", GMLComplexTypes.CoordinatesType.getInstance(), 1, 1, false, null)
        };

        // static sequence
        private static final DefaultChoice seq = new DefaultChoice(elements);

        /*
         * part of the singleton pattern
         *
         * @see GMLComplexType#getInstance()
         */
        public static GMLComplexType getInstance() {
            return instance;
        }

        /** @see schema.ComplexType#isAbstract() */
        public boolean isAbstract() {
            return false;
        }

        /** @see schema.ComplexType#getAnyAttributeNameSpace() */
        public String getAnyAttributeNameSpace() {
            return null;
        }

        /** @see schema.ComplexType#getAttributeDescriptors() */
        public Attribute[] getAttributes() {
            return AbstractGeometryType.attributes;
        }

        /** @see schema.ComplexType#getChildren() */
        public ElementGrouping getChild() {
            return seq;
        }

        /** @see schema.ComplexType#getLocalName() */
        public String getName() {
            return "BoxType";
        }

        /** @see schema.Type#getValue(java.util.List) */
        public Object getValue(Element element, ElementValue[] value, Attributes attrs, Map hints)
                throws OperationNotSupportedException, SAXException {
            Element e = value[0].getElement();

            if (e == null) {
                if (!element.isNillable())
                    throw new SAXException(
                            "Internal error, ElementValues require an associated Element.");
                return null;
            }

            Object t = value[0].getValue();
            LineString p = null;

            if (t == null) {
                throw new SAXException("Invalid coordinate specified");
            }

            GeometryFactory gf = new GeometryFactory(CoordinateArraySequenceFactory.instance());

            try {
                if (t instanceof Coordinate) {
                    if (value.length != 2) {
                        throw new SAXException("Cannot have more than one coord per " + getName());
                    }

                    // there should be more
                    Coordinate[] c = new Coordinate[value.length];

                    for (int i = 0; i < c.length; i++) c[i] = (Coordinate) value[i].getValue();

                    p = gf.createLineString(c);
                } else {
                    if (value.length > 1) {
                        throw new SAXException(
                                "Cannot have more than one coordinate sequence per " + getName());
                    }

                    CoordinateSequence c = (CoordinateSequence) t;
                    p = gf.createLineString(c);
                }
            } catch (ClassCastException cce) {
                logger.warning(cce.toString());
                logger.warning(t + ((t == null) ? "" : t.getClass().getName()));
                throw cce;
            }

            ElementValue[] ev = new ElementValue[1];
            ev[0] = new DefaultElementValue(element, p.getEnvelope());

            return AbstractGeometryType.getInstance().getValue(element, ev, attrs, hints);
        }

        /** @see org.geotools.xml.xsi.Type#getInstanceType() */
        public Class getInstanceType() {
            return Geometry.class;
        }

        /** @see schema.ComplexType#findChildElement(java.lang.String) */
        public Element findChildElement(String name) {
            if (name == null) {
                return null;
            }

            for (int i = 0; i < elements.length; i++)
                if (name.equals(elements[i].getName())) {
                    return elements[i];
                }

            return null;
        }

        /**
         * @see org.geotools.xml.schema.Type#canEncode(org.geotools.xml.schema.Element,
         *     java.lang.Object, java.util.Map)
         */
        public boolean canEncode(Element element, Object value, Map hints) {
            // since bounds is a derived attibute in our feature model and not
            // an attribute which is modelled explicitly in feature types, there
            // might not be an element around for it, so we dont require that
            // there be one and simply check that the value being encoded is a
            // geometry
            return value instanceof Geometry || value instanceof ReferencedEnvelope;
        }

        /**
         * @see org.geotools.xml.schema.Type#encode(org.geotools.xml.schema.Element,
         *     java.lang.Object, org.geotools.xml.PrintHandler, java.util.Map)
         */
        public void encode(Element element, Object value, PrintHandler output, Map hints)
                throws IOException, OperationNotSupportedException {
            if (!canEncode(element, value, hints)) {
                throw new OperationNotSupportedException("Cannot encode " + value);
            }
            if (value instanceof ReferencedEnvelope) {
                ReferencedEnvelope bbox = (ReferencedEnvelope) value;
                AttributesImpl ai = new AttributesImpl();

                // no GID
                if (bbox != null && bbox.getCoordinateReferenceSystem() != null) {
                    // TODO Fix this when parsing is better ... should be a coord reference system
                    String srsName = CRS.toSRS(bbox.getCoordinateReferenceSystem());
                    ai.addAttribute("", "srsName", "", "anyURI", srsName);
                } else {
                    ai = null;
                }

                if (bbox == null || bbox.isNull() || bbox.isEmpty()) {
                    return;
                }
                // we handle the case for a null element, see canEncode for details
                if (element != null) {
                    output.startElement(GMLSchema.NAMESPACE, element.getName(), ai);
                } else {
                    output.startElement(GMLSchema.NAMESPACE, "Box", ai);
                }

                // Coordinate[] coords = g.getCoordinates();
                Envelope e = bbox;
                encodeCoords(elements[1], e, output);

                if (element != null) {
                    output.endElement(GMLSchema.NAMESPACE, element.getName());
                } else {
                    output.endElement(GMLSchema.NAMESPACE, "Box");
                }
            } else {
                Geometry g = (Geometry) value;

                if ((g == null) || (g.getNumPoints() == 0) || (g.getCoordinates().length == 0)) {
                    return;
                }

                AttributesImpl ai = getSrsNameAttribute(g);

                // we handle the case for a null element, see canEncode for details
                if (element != null) {
                    output.startElement(GMLSchema.NAMESPACE, element.getName(), ai);
                } else {
                    output.startElement(GMLSchema.NAMESPACE, "Box", ai);
                }

                //            Coordinate[] coords = g.getCoordinates();
                Envelope e = g.getEnvelopeInternal();
                encodeCoords(elements[1], e, output);

                if (element != null) {
                    output.endElement(GMLSchema.NAMESPACE, element.getName());
                } else {
                    output.endElement(GMLSchema.NAMESPACE, "Box");
                }
            }
        }
    }

    /**
     * This class represents an PolygonType within the GML Schema. This includes both the data and
     * parsing functionality associated with an PolygonType.
     *
     * @author dzwiers
     * @see GMLComplexType
     * @see ComplexType
     */
    public static class PolygonType extends GMLComplexType {

        /** @see org.geotools.xml.schema.ComplexType#getChildElements() */
        public Element[] getChildElements() {
            return elements;
        }

        // singleton instance
        private static final GMLComplexType instance = new PolygonType();

        // static list of elements
        private static final Element[] elements = {
            new GMLElement(
                    "outerBoundaryIs",
                    GMLComplexTypes.LinearRingMemberType.getInstance(),
                    1,
                    1,
                    false,
                    null),
            new GMLElement(
                    "innerBoundaryIs",
                    GMLComplexTypes.LinearRingMemberType.getInstance(),
                    0,
                    ElementGrouping.UNBOUNDED,
                    false,
                    null)
        };

        // static sequence
        private static final DefaultSequence seq = new DefaultSequence(elements);

        /*
         * part of the singleton pattern
         *
         * @see GMLComplexType#getInstance()
         */
        public static GMLComplexType getInstance() {
            return instance;
        }

        /** @see schema.ComplexType#isAbstract() */
        public boolean isAbstract() {
            return false;
        }

        /** @see schema.ComplexType#getAnyAttributeNameSpace() */
        public String getAnyAttributeNameSpace() {
            return null;
        }

        /** @see schema.ComplexType#getAttributeDescriptors() */
        public Attribute[] getAttributes() {
            return AbstractGeometryType.attributes;
        }

        /** @see schema.ComplexType#getChildren() */
        public ElementGrouping getChild() {
            return seq;
        }

        /** @see schema.ComplexType#getLocalName() */
        public String getName() {
            return "PolygonType";
        }

        /** @see schema.Type#getValue(java.util.List) */
        public Object getValue(Element element, ElementValue[] value, Attributes attrs, Map hints)
                throws OperationNotSupportedException, SAXException {
            Element e = value[0].getElement();

            if (e == null) {
                if (!element.isNillable())
                    throw new SAXException(
                            "Internal error, ElementValues require an associated Element.");
                return null;
            }

            GeometryFactory gf = new GeometryFactory(CoordinateArraySequenceFactory.instance());

            LinearRing outerLR = null;
            LinearRing[] innerLR = new LinearRing[(value.length > 1) ? (value.length - 1) : 0];
            int innerIndex = 0;

            for (int i = 0; i < value.length; i++) {
                if (elements[0].getName().equalsIgnoreCase(value[i].getElement().getName())) {
                    outerLR = (LinearRing) value[i].getValue();
                } else {
                    innerLR[innerIndex++] = (LinearRing) value[i].getValue();
                }
            }

            Polygon p = gf.createPolygon(outerLR, innerLR);

            ElementValue[] ev = new ElementValue[1];
            ev[0] = new DefaultElementValue(element, p);

            return AbstractGeometryType.getInstance().getValue(element, ev, attrs, hints);
        }

        /** @see org.geotools.xml.xsi.Type#getInstanceType() */
        public Class getInstanceType() {
            return Polygon.class;
        }

        /** @see schema.ComplexType#findChildElement(java.lang.String) */
        public Element findChildElement(String name) {
            if (name == null) {
                return null;
            }

            for (int i = 0; i < elements.length; i++)
                if (name.equals(elements[i].getName())) {
                    return elements[i];
                }

            return null;
        }

        /**
         * @see org.geotools.xml.schema.Type#canEncode(org.geotools.xml.schema.Element,
         *     java.lang.Object, java.util.Map)
         */
        public boolean canEncode(Element element, Object value, Map hints) {
            ComplexType t =
                    (element.getType() instanceof ComplexType)
                            ? (ComplexType) element.getType()
                            : null;

            while ((t != null) && (t != this)) {

                if (t.getParent() instanceof ComplexType) {
                    t = (ComplexType) t.getParent();
                } else t = null;
            }

            return ((t != null) && (value instanceof Polygon));
        }

        /**
         * @see org.geotools.xml.schema.Type#encode(org.geotools.xml.schema.Element,
         *     java.lang.Object, org.geotools.xml.PrintHandler, java.util.Map)
         */
        public void encode(Element element, Object value, PrintHandler output, Map hints)
                throws IOException, OperationNotSupportedException {
            if (!canEncode(element, value, hints)) {
                throw new OperationNotSupportedException("Cannot encode");
            }

            Polygon g = (Polygon) value;

            GMLComplexTypes.encode(element, g, output);
        }
    }

    /**
     * This class represents an GeometryCollectionType within the GML Schema. This includes both the
     * data and parsing functionality associated with an GeometryCollectionType.
     *
     * @author dzwiers
     * @see GMLComplexType
     * @see ComplexType
     */
    public static class GeometryCollectionType extends GMLComplexType {

        /** @see org.geotools.xml.schema.ComplexType#getChildElements() */
        public Element[] getChildElements() {
            return elements;
        }

        // singleton instance
        private static final GMLComplexType instance = new GeometryCollectionType();

        // static lsit of elements
        private static final Element[] elements = {
            new GMLElement(
                    "geometryMember",
                    GMLComplexTypes.GeometryAssociationType.getInstance(),
                    1,
                    ElementGrouping.UNBOUNDED,
                    false,
                    null),
        };

        // static sequence
        private static final DefaultSequence seq = new DefaultSequence(elements);

        /*
         * part of the singleton pattern
         *
         * @see GMLComplexType#getInstance()
         */
        public static GMLComplexType getInstance() {
            return instance;
        }

        /** @see schema.ComplexType#isAbstract() */
        public boolean isAbstract() {
            return false;
        }

        /** @see schema.ComplexType#getAnyAttributeNameSpace() */
        public String getAnyAttributeNameSpace() {
            return null;
        }

        /** @see schema.ComplexType#getAttributeDescriptors() */
        public Attribute[] getAttributes() {
            return AbstractGeometryCollectionBaseType.attributes;
        }

        /** @see schema.ComplexType#getChildren() */
        public ElementGrouping getChild() {
            return seq;
        }

        /** @see schema.ComplexType#getLocalName() */
        public String getName() {
            return "GeometryCollectionType";
        }

        /** @see schema.Type#getValue(java.util.List) */
        public Object getValue(Element element, ElementValue[] value, Attributes attrs, Map hints)
                throws SAXException {
            Element e = value[0].getElement();

            if (e == null) {
                if (!element.isNillable())
                    throw new SAXException(
                            "Internal error, ElementValues require an associated Element.");
                return null;
            }

            GeometryFactory gf = new GeometryFactory(CoordinateArraySequenceFactory.instance());

            Geometry[] geoms = new Geometry[value.length];

            for (int i = 0; i < value.length; i++) {
                geoms[i] = (Geometry) value[i].getValue();
            }

            return gf.createGeometryCollection(geoms);
        }

        /** @see org.geotools.xml.xsi.Type#getInstanceType() */
        public Class getInstanceType() {
            return GeometryCollection.class;
        }

        /** @see schema.ComplexType#findChildElement(java.lang.String) */
        public Element findChildElement(String name) {
            if (name == null) {
                return null;
            }

            for (int i = 0; i < elements.length; i++)
                if (name.equals(elements[i].getName())) {
                    return elements[i];
                }

            return null;
        }

        /**
         * @see org.geotools.xml.schema.Type#canEncode(org.geotools.xml.schema.Element,
         *     java.lang.Object, java.util.Map)
         */
        public boolean canEncode(Element element, Object value, Map hints) {
            ComplexType t =
                    (element.getType() instanceof ComplexType)
                            ? (ComplexType) element.getType()
                            : null;

            while ((t != null) && (t != this))
                t = (t.getParent() instanceof ComplexType) ? (ComplexType) t.getParent() : null;

            return ((t != null) && (value instanceof GeometryCollection));
        }

        /**
         * @see org.geotools.xml.schema.Type#encode(org.geotools.xml.schema.Element,
         *     java.lang.Object, org.geotools.xml.PrintHandler, java.util.Map)
         */
        public void encode(Element element, Object value, PrintHandler output, Map hints)
                throws IOException, OperationNotSupportedException {
            if (!canEncode(element, value, hints)) {
                throw new OperationNotSupportedException("Cannot encode");
            }

            GeometryCollection g = (GeometryCollection) value;

            GMLComplexTypes.encode(element, g, output);
        }
    }

    /**
     * This class represents an MultiPointType within the GML Schema. This includes both the data
     * and parsing functionality associated with an MultiPointType.
     *
     * @author dzwiers
     * @see GMLComplexType
     * @see ComplexType
     */
    public static class MultiPointType extends GMLComplexType {

        /** @see org.geotools.xml.schema.ComplexType#getChildElements() */
        public Element[] getChildElements() {
            return elements;
        }

        // singleton instance
        private static final GMLComplexType instance = new MultiPointType();

        // static element list
        private static final Element[] elements = {
            new GMLElement(
                    "pointMember",
                    GMLComplexTypes.PointMemberType.getInstance(),
                    1,
                    ElementGrouping.UNBOUNDED,
                    false,
                    new GMLElement(
                            "geometryMember",
                            GMLComplexTypes.GeometryAssociationType.getInstance(),
                            1,
                            1,
                            false,
                            null)),
        };

        // static sequence
        private static final DefaultSequence seq = new DefaultSequence(elements);

        /*
         * part of the singleton pattern
         *
         * @see GMLComplexType#getInstance()
         */
        public static GMLComplexType getInstance() {
            return instance;
        }

        /** @see schema.ComplexType#isAbstract() */
        public boolean isAbstract() {
            return false;
        }

        /** @see schema.ComplexType#getAnyAttributeNameSpace() */
        public String getAnyAttributeNameSpace() {
            return null;
        }

        /** @see schema.ComplexType#getAttributeDescriptors() */
        public Attribute[] getAttributes() {
            return AbstractGeometryCollectionBaseType.attributes;
        }

        /** @see schema.ComplexType#getChildren() */
        public ElementGrouping getChild() {
            return seq;
        }

        /** @see schema.ComplexType#getLocalName() */
        public String getName() {
            return "MultiPointType";
        }

        /** @see schema.Type#getValue(java.util.List) */
        public Object getValue(Element element, ElementValue[] value, Attributes attrs, Map hints)
                throws SAXException, OperationNotSupportedException {
            Element e = value[0].getElement();

            if (e == null) {
                if (!element.isNillable())
                    throw new SAXException(
                            "Internal error, ElementValues require an associated Element.");
                return null;
            }

            GeometryFactory gf = new GeometryFactory(CoordinateArraySequenceFactory.instance());

            Point[] geoms = new Point[value.length];

            for (int i = 0; i < value.length; i++) {
                geoms[i] = (Point) value[i].getValue();
            }

            MultiPoint mp = gf.createMultiPoint(geoms);

            ElementValue[] ev = new ElementValue[1];
            ev[0] = new DefaultElementValue(element, mp);

            return AbstractGeometryType.getInstance().getValue(element, ev, attrs, hints);
        }

        /** @see org.geotools.xml.xsi.Type#getInstanceType() */
        public Class getInstanceType() {
            return MultiPoint.class;
        }

        /** @see schema.ComplexType#findChildElement(java.lang.String) */
        public Element findChildElement(String name) {
            if (name == null) {
                return null;
            }

            for (int i = 0; i < elements.length; i++)
                if (name.equals(elements[i].getName())) {
                    return elements[i];
                }

            return null;
        }

        /**
         * @see org.geotools.xml.schema.Type#canEncode(org.geotools.xml.schema.Element,
         *     java.lang.Object, java.util.Map)
         */
        public boolean canEncode(Element element, Object value, Map hints) {
            ComplexType t =
                    (element.getType() instanceof ComplexType)
                            ? (ComplexType) element.getType()
                            : null;

            while ((t != null) && (t != this))
                t = (t.getParent() instanceof ComplexType) ? (ComplexType) t.getParent() : null;

            return ((t != null) && (value instanceof MultiPoint));
        }

        /**
         * @see org.geotools.xml.schema.Type#encode(org.geotools.xml.schema.Element,
         *     java.lang.Object, org.geotools.xml.PrintHandler, java.util.Map)
         */
        public void encode(Element element, Object value, PrintHandler output, Map hints)
                throws IOException, OperationNotSupportedException {
            if (!canEncode(element, value, hints)) {
                throw new OperationNotSupportedException("Cannot encode");
            }

            MultiPoint g = (MultiPoint) value;

            GMLComplexTypes.encode(element, g, output);
        }
    }

    /**
     * This class represents an MultiLineStringType within the GML Schema. This includes both the
     * data and parsing functionality associated with an MultiLineStringType.
     *
     * @author dzwiers
     * @see GMLComplexType
     * @see ComplexType
     */
    public static class MultiLineStringType extends GMLComplexType {

        /** @see org.geotools.xml.schema.ComplexType#getChildElements() */
        public Element[] getChildElements() {
            return elements;
        }

        // singleton instance
        private static final GMLComplexType instance = new MultiLineStringType();

        // static element list
        private static final Element[] elements = {
            new GMLElement(
                    "lineStringMember",
                    GMLComplexTypes.LineStringMemberType.getInstance(),
                    1,
                    ElementGrouping.UNBOUNDED,
                    false,
                    new GMLElement(
                            "geometryMember",
                            GMLComplexTypes.GeometryAssociationType.getInstance(),
                            1,
                            1,
                            false,
                            null)),
        };

        // static sequence
        private static final DefaultSequence seq = new DefaultSequence(elements);

        /*
         * part of the singleton pattern
         *
         * @see GMLComplexType#getInstance()
         */
        public static GMLComplexType getInstance() {
            return instance;
        }

        /** @see schema.ComplexType#isAbstract() */
        public boolean isAbstract() {
            return false;
        }

        /** @see schema.ComplexType#getAnyAttributeNameSpace() */
        public String getAnyAttributeNameSpace() {
            return null;
        }

        /** @see schema.ComplexType#getAttributeDescriptors() */
        public Attribute[] getAttributes() {
            return AbstractGeometryCollectionBaseType.attributes;
        }

        /** @see schema.ComplexType#getChildren() */
        public ElementGrouping getChild() {
            return seq;
        }

        /** @see schema.ComplexType#getLocalName() */
        public String getName() {
            return "MultiLineStringType";
        }

        /** @see schema.Type#getValue(java.util.List) */
        public Object getValue(Element element, ElementValue[] value, Attributes attrs, Map hints)
                throws SAXException, OperationNotSupportedException {
            Element e = value[0].getElement();

            if (e == null) {
                if (!element.isNillable())
                    throw new SAXException(
                            "Internal error, ElementValues require an associated Element.");
                return null;
            }

            GeometryFactory gf = new GeometryFactory(CoordinateArraySequenceFactory.instance());

            LineString[] geoms = new LineString[value.length];

            for (int i = 0; i < value.length; i++) {
                geoms[i] = (LineString) value[i].getValue();
            }

            MultiLineString mp = gf.createMultiLineString(geoms);

            ElementValue[] ev = new ElementValue[1];
            ev[0] = new DefaultElementValue(element, mp);

            return AbstractGeometryType.getInstance().getValue(element, ev, attrs, hints);
        }

        /** @see org.geotools.xml.xsi.Type#getInstanceType() */
        public Class getInstanceType() {
            return MultiLineString.class;
        }

        /** @see schema.ComplexType#findChildElement(java.lang.String) */
        public Element findChildElement(String name) {
            if (name == null) {
                return null;
            }

            for (int i = 0; i < elements.length; i++)
                if (name.equals(elements[i].getName())) {
                    return elements[i];
                }

            return null;
        }

        /**
         * @see org.geotools.xml.schema.Type#canEncode(org.geotools.xml.schema.Element,
         *     java.lang.Object, java.util.Map)
         */
        public boolean canEncode(Element element, Object value, Map hints) {
            ComplexType t =
                    (element.getType() instanceof ComplexType)
                            ? (ComplexType) element.getType()
                            : null;

            while ((t != null) && (t != this))
                t = (t.getParent() instanceof ComplexType) ? (ComplexType) t.getParent() : null;

            return ((t != null) && (value instanceof MultiLineString));
        }

        /**
         * @see org.geotools.xml.schema.Type#encode(org.geotools.xml.schema.Element,
         *     java.lang.Object, org.geotools.xml.PrintHandler, java.util.Map)
         */
        public void encode(Element element, Object value, PrintHandler output, Map hints)
                throws IOException, OperationNotSupportedException {
            if (!canEncode(element, value, hints)) {
                throw new OperationNotSupportedException("Cannot encode");
            }

            MultiLineString g = (MultiLineString) value;

            GMLComplexTypes.encode(element, g, output);
        }
    }

    /**
     * This class represents an MultiPolygonType within the GML Schema. This includes both the data
     * and parsing functionality associated with an MultiPolygonType.
     *
     * @author dzwiers
     * @see GMLComplexType
     * @see ComplexType
     */
    public static class MultiPolygonType extends GMLComplexType {

        /** @see org.geotools.xml.schema.ComplexType#getChildElements() */
        public Element[] getChildElements() {
            return elements;
        }

        // singleton instance
        private static final GMLComplexType instance = new MultiPolygonType();

        // static element list
        private static final Element[] elements = {
            new GMLElement(
                    "polygonMember",
                    GMLComplexTypes.PolygonMemberType.getInstance(),
                    1,
                    ElementGrouping.UNBOUNDED,
                    false,
                    new GMLElement(
                            "geometryMember",
                            GMLComplexTypes.GeometryAssociationType.getInstance(),
                            1,
                            1,
                            false,
                            null)),
        };

        // static sequence
        private static final DefaultSequence seq = new DefaultSequence(elements);

        /*
         * part of the singleton pattern
         *
         * @see GMLComplexType#getInstance()
         */
        public static GMLComplexType getInstance() {
            return instance;
        }

        /** @see schema.ComplexType#isAbstract() */
        public boolean isAbstract() {
            return false;
        }

        /** @see schema.ComplexType#getAnyAttributeNameSpace() */
        public String getAnyAttributeNameSpace() {
            return null;
        }

        /** @see schema.ComplexType#getAttributeDescriptors() */
        public Attribute[] getAttributes() {
            return AbstractGeometryCollectionBaseType.attributes;
        }

        /** @see schema.ComplexType#getChildren() */
        public ElementGrouping getChild() {
            return seq;
        }

        /** @see schema.ComplexType#getLocalName() */
        public String getName() {
            return "MultiPolygonType";
        }

        /** @see schema.Type#getValue(java.util.List) */
        public Object getValue(Element element, ElementValue[] value, Attributes attrs, Map hints)
                throws SAXException, OperationNotSupportedException {
            Element e = value[0].getElement();

            if (e == null) {
                if (!element.isNillable())
                    throw new SAXException(
                            "Internal error, ElementValues require an associated Element.");
                return null;
            }

            GeometryFactory gf = new GeometryFactory(CoordinateArraySequenceFactory.instance());

            Polygon[] geoms = new Polygon[value.length];

            for (int i = 0; i < value.length; i++) {
                geoms[i] = (Polygon) value[i].getValue();
            }

            MultiPolygon mp = gf.createMultiPolygon(geoms);

            ElementValue[] ev = new ElementValue[1];
            ev[0] = new DefaultElementValue(element, mp);

            return AbstractGeometryType.getInstance().getValue(element, ev, attrs, hints);
        }

        /** @see org.geotools.xml.xsi.Type#getInstanceType() */
        public Class getInstanceType() {
            return MultiPolygon.class;
        }

        /** @see schema.ComplexType#findChildElement(java.lang.String) */
        public Element findChildElement(String name) {
            if (name == null) {
                return null;
            }

            for (int i = 0; i < elements.length; i++)
                if (name.equals(elements[i].getName())) {
                    return elements[i];
                }

            return null;
        }

        /**
         * @see org.geotools.xml.schema.Type#canEncode(org.geotools.xml.schema.Element,
         *     java.lang.Object, java.util.Map)
         */
        public boolean canEncode(Element element, Object value, Map hints) {
            ComplexType t =
                    (element.getType() instanceof ComplexType)
                            ? (ComplexType) element.getType()
                            : null;

            while ((t != null) && (t != this))
                t = (t.getParent() instanceof ComplexType) ? (ComplexType) t.getParent() : null;

            return ((t != null) && (value instanceof MultiPolygon));
        }

        /**
         * @see org.geotools.xml.schema.Type#encode(org.geotools.xml.schema.Element,
         *     java.lang.Object, org.geotools.xml.PrintHandler, java.util.Map)
         */
        public void encode(Element element, Object value, PrintHandler output, Map hints)
                throws IOException, OperationNotSupportedException {
            if (!canEncode(element, value, hints)) {
                throw new OperationNotSupportedException("Cannot encode");
            }

            MultiPolygon g = (MultiPolygon) value;

            GMLComplexTypes.encode(element, g, output);
        }
    }

    /**
     * This class represents an CoordType within the GML Schema. This includes both the data and
     * parsing functionality associated with an CoordType.
     *
     * @author dzwiers
     * @see GMLComplexType
     * @see ComplexType
     */
    public static class CoordType extends GMLComplexType {

        /** @see org.geotools.xml.schema.ComplexType#getChildElements() */
        public Element[] getChildElements() {
            return elements;
        }

        // singleton instance
        private static final GMLComplexType instance = new CoordType();

        // static element list
        private static final Element[] elements = {
            new GMLElement("X", XSISimpleTypes.Decimal.getInstance(), 1, 1, false, null),
            new GMLElement("Y", XSISimpleTypes.Decimal.getInstance(), 0, 1, false, null),
            new GMLElement("Z", XSISimpleTypes.Decimal.getInstance(), 0, 1, false, null)
        };

        // static sequence
        private static final DefaultSequence seq = new DefaultSequence(elements);

        /*
         * part of the singleton pattern
         *
         * @see GMLComplexType#getInstance()
         */
        public static GMLComplexType getInstance() {
            return instance;
        }

        /** @see schema.ComplexType#isAbstract() */
        public boolean isAbstract() {
            return false;
        }

        /** @see schema.ComplexType#getAnyAttributeNameSpace() */
        public String getAnyAttributeNameSpace() {
            return null;
        }

        /** @see schema.ComplexType#getAttributeDescriptors() */
        public Attribute[] getAttributes() {
            return AbstractGeometryCollectionBaseType.attributes;
        }

        /** @see schema.ComplexType#getChildren() */
        public ElementGrouping getChild() {
            return seq;
        }

        /** @see schema.ComplexType#getLocalName() */
        public String getName() {
            return "CoordType";
        }

        /** @see schema.Type#getValue(java.util.List) */
        public Object getValue(Element element, ElementValue[] value, Attributes attrs, Map hints)
                throws SAXException {
            Element e = value[0].getElement();

            if (e == null) {
                if (!element.isNillable())
                    throw new SAXException(
                            "Internal error, ElementValues require an associated Element.");
                return null;
            }

            Double x;
            Double y;
            Double z;
            x = y = z = null;

            for (int i = 0; i < value.length; i++) {
                if (elements[0].getName().equals(value[i].getElement().getName())) {
                    x = (Double) value[i].getValue();
                }

                if (elements[1].getName().equals(value[i].getElement().getName())) {
                    y = (Double) value[i].getValue();
                }

                if (elements[2].getName().equals(value[i].getElement().getName())) {
                    z = (Double) value[i].getValue();
                }
            }

            if (x == null) {
                throw new SAXException("An X coord is required");
            }

            if (y == null) {
                return new Coordinate(x.doubleValue(), 0);
            }

            if (z == null) {
                return new Coordinate(x.doubleValue(), y.doubleValue());
            }

            return new Coordinate(x.doubleValue(), y.doubleValue(), z.doubleValue());
        }

        /** @see org.geotools.xml.xsi.Type#getInstanceType() */
        public Class getInstanceType() {
            return Coordinate.class;
        }

        /** @see schema.ComplexType#findChildElement(java.lang.String) */
        public Element findChildElement(String name) {
            if (name == null) {
                return null;
            }

            for (int i = 0; i < elements.length; i++)
                if (name.equals(elements[i].getName())) {
                    return elements[i];
                }

            return null;
        }

        /**
         * @see org.geotools.xml.schema.Type#canEncode(org.geotools.xml.schema.Element,
         *     java.lang.Object, java.util.Map)
         */
        public boolean canEncode(Element element, Object value, Map hints) {
            ComplexType t =
                    (element.getType() instanceof ComplexType)
                            ? (ComplexType) element.getType()
                            : null;

            while ((t != null) && (t != this))
                t = (t.getParent() instanceof ComplexType) ? (ComplexType) t.getParent() : null;

            return ((t != null) && (value instanceof Coordinate));
        }

        /**
         * @see org.geotools.xml.schema.Type#encode(org.geotools.xml.schema.Element,
         *     java.lang.Object, org.geotools.xml.PrintHandler, java.util.Map)
         */
        public void encode(Element element, Object value, PrintHandler output, Map hints)
                throws IOException, OperationNotSupportedException {
            if (!canEncode(element, value, hints)) {
                throw new OperationNotSupportedException("Cannot encode");
            }

            Coordinate g = (Coordinate) value;

            GMLComplexTypes.encodeCoord(element, g, output);
        }
    }

    /**
     * This class represents an CoordinatesType within the GML Schema. This includes both the data
     * and parsing functionality associated with an CoordinatesType.
     *
     * @author dzwiers
     * @see GMLComplexType
     * @see ComplexType
     */
    public static class CoordinatesType extends GMLComplexType {

        /** @see org.geotools.xml.schema.ComplexType#getChildElements() */
        public Element[] getChildElements() {
            return null;
        }

        // singleton instance
        private static final GMLComplexType instance = new CoordinatesType();

        // static attribute list
        private static final Attribute[] attributes = {
            new GMLAttribute(
                    "decimal", XSISimpleTypes.String.getInstance(), Attribute.OPTIONAL, "."),
            new GMLAttribute("cs", XSISimpleTypes.String.getInstance(), Attribute.OPTIONAL, ","),
            new GMLAttribute("ts", XSISimpleTypes.String.getInstance(), Attribute.OPTIONAL, "\t")
        };

        /*
         * part of the singleton pattern
         *
         * @see GMLComplexType#getInstance()
         */
        public static GMLComplexType getInstance() {
            return instance;
        }

        /** @see schema.ComplexType#isAbstract() */
        public boolean isAbstract() {
            return false;
        }

        /** @see schema.ComplexType#getAnyAttributeNameSpace() */
        public String getAnyAttributeNameSpace() {
            return null;
        }

        /** @see schema.ComplexType#getAttributeDescriptors() */
        public Attribute[] getAttributes() {
            //            return AbstractGeometryCollectionBaseType.attributes;
            return attributes;
        }

        /** @see schema.ComplexType#getChildren() */
        public ElementGrouping getChild() {
            return new DefaultSequence(new Element[0]);
        }

        /** @see schema.ComplexType#getLocalName() */
        public String getName() {
            return "CoordinatesType";
        }

        /** @see schema.Type#getValue(java.util.List) */
        public Object getValue(Element element, ElementValue[] value, Attributes attrs, Map hints)
                throws SAXException {
            if (value.length != 1) {
                throw new SAXException(
                        "Internal error, ElementValues require an associated Element.");
            }

            String dec;
            String cs;
            String ts;
            dec = attrs.getValue("", "decimal");

            if (dec == null) {
                dec = attrs.getValue(GMLSchema.NAMESPACE.toString(), "decimal");
            }

            dec = ((dec == null) || (dec == "")) ? "." : dec;

            cs = attrs.getValue("", "cs");

            if (cs == null) {
                cs = attrs.getValue(GMLSchema.NAMESPACE.toString(), "cs");
            }

            cs = ((cs == null) || (cs == "")) ? ",\\s*" : (cs + "\\s*");
            ts = attrs.getValue("", "ts");

            if (ts == null) {
                ts = attrs.getValue(GMLSchema.NAMESPACE.toString(), "ts");
            }

            ts =
                    ((ts == null) || (ts == "") || ts.matches("\\s"))
                            ? "\\s+"
                            : (ts + "\\s*"); // handle whitespace

            String val = (String) value[0].getValue();

            String[] touples = val.split(ts);
            Coordinate[] coordinates = new Coordinate[touples.length];

            for (int i = 0; i < touples.length; i++) {
                String[] points = touples[i].split(cs);
                double[] pts = new double[points.length];

                for (int j = 0; j < points.length; j++) {
                    String t = "";

                    try {
                        if (!dec.equals(".")) {
                            // dec = dec.replaceAll("\\", "\\");
                            t = points[j].replaceAll(dec, ".");
                        } else {
                            t = points[j];
                        }

                        pts[j] = Double.parseDouble(t);
                    } catch (NumberFormatException e) {
                        logger.warning(e.toString());
                        logger.warning(
                                "Double = '"
                                        + t
                                        + "' "
                                        + j
                                        + "/"
                                        + points.length
                                        + "  Touples = "
                                        + i
                                        + "/"
                                        + touples.length);
                        throw e;
                    }
                }

                if (pts.length == 1) {
                    coordinates[i] = new Coordinate(pts[0], 0);
                } else {
                    if (pts.length == 2) {
                        coordinates[i] = new Coordinate(pts[0], pts[1]);
                    } else {
                        // should be three or there was an error
                        coordinates[i] = new Coordinate(pts[0], pts[1], pts[2]);
                    }
                }
            }

            return CoordinateArraySequenceFactory.instance().create(coordinates);
        }

        /** @see org.geotools.xml.xsi.Type#getInstanceType() */
        public Class getInstanceType() {
            return CoordinateSequence.class;
        }

        /** @see schema.ComplexType#findChildElement(java.lang.String) */
        public Element findChildElement(String name) {
            return null;
        }

        /** @see schema.ComplexType#isMixed() */
        public boolean isMixed() {
            return true;
        }

        /**
         * @see org.geotools.xml.schema.Type#canEncode(org.geotools.xml.schema.Element,
         *     java.lang.Object, java.util.Map)
         */
        public boolean canEncode(Element element, Object value, Map hints) {
            ComplexType t =
                    (element.getType() instanceof ComplexType)
                            ? (ComplexType) element.getType()
                            : null;

            while ((t != null) && (t != this))
                t = (t.getParent() instanceof ComplexType) ? (ComplexType) t.getParent() : null;

            return ((t != null) && (value instanceof CoordinateSequence));
        }

        /**
         * @see org.geotools.xml.schema.Type#encode(org.geotools.xml.schema.Element,
         *     java.lang.Object, org.geotools.xml.PrintHandler, java.util.Map)
         */
        public void encode(Element element, Object value, PrintHandler output, Map hints)
                throws IOException, OperationNotSupportedException {
            if (!canEncode(element, value, hints)) {
                throw new OperationNotSupportedException("Cannot encode");
            }

            CoordinateSequence g = (CoordinateSequence) value;

            GMLComplexTypes.encodeCoords(element, g, output);
        }
    }

    /**
     * This class represents an AbstractFeatureType within the GML Schema. This includes both the
     * data and parsing functionality associated with an AbstractFeatureType.
     *
     * @author dzwiers
     * @see GMLComplexType
     * @see ComplexType
     */
    public static class AbstractFeatureType extends GMLComplexType {

        /** @see org.geotools.xml.schema.ComplexType#getChildElements() */
        public Element[] getChildElements() {
            return elements;
        }

        // static attribute list
        protected static final Attribute[] attributes = {
            new GMLAttribute("fid", XSISimpleTypes.ID.getInstance(), Attribute.OPTIONAL),
        };

        // static element list
        private static final Element[] elements = {
            new GMLElement("description", XSISimpleTypes.String.getInstance(), 0, 1, false, null) {
                public boolean isNillable() {
                    return true;
                }
            },
            new GMLElement("name", XSISimpleTypes.String.getInstance(), 0, 1, false, null) {
                public boolean isNillable() {
                    return true;
                }
            },
            new GMLElement(
                    "boundedBy",
                    GMLComplexTypes.BoundingShapeType.getInstance(),
                    0,
                    1,
                    false,
                    null) {
                public boolean isNillable() {
                    return true;
                }
            },
        };

        // static sequence
        private static final DefaultSequence seq = new DefaultSequence(elements);

        // singleton instance
        private static final GMLComplexType instance = new AbstractFeatureType();

        // used for mapping ftName + namespace to actual FTs
        private static final HashMap featureTypeMappings = new HashMap();

        /*
         * part of the singleton pattern
         *
         * @see GMLComplexType#getInstance()
         */
        public static GMLComplexType getInstance() {
            return instance;
        }

        /** @see schema.ComplexType#isAbstract() */
        public boolean isAbstract() {
            return true;
        }

        /** @see schema.ComplexType#getAnyAttributeNameSpace() */
        public String getAnyAttributeNameSpace() {
            return null;
        }

        /** @see schema.ComplexType#getAttributeDescriptors() */
        public Attribute[] getAttributes() {
            return attributes;
        }

        /** @see schema.ComplexType#getChildren() */
        public ElementGrouping getChild() {
            return seq;
        }

        /** @see schema.ComplexType#getLocalName() */
        public String getName() {
            return "AbstractFeatureType";
        }

        /** @see schema.Type#getValue(java.util.List) */
        public Object getValue(Element element, ElementValue[] value, Attributes attrs, Map hints)
                throws SAXException {

            if ((hints == null) || (hints.get(XMLHandlerHints.STREAM_HINT) == null)) {
                return getFeature(element, value, attrs, hints, null);
            }

            if (hints.get(STREAM_FEATURE_NAME_HINT) == null) {
                hints.put(STREAM_FEATURE_NAME_HINT, element.getName());
            }

            String nm = (String) hints.get(STREAM_FEATURE_NAME_HINT);
            SimpleFeature f;
            if ((nm != null) && nm.equals(element.getName())) {
                f =
                        getFeature(
                                element,
                                value,
                                attrs,
                                hints,
                                ((FCBuffer) hints.get(XMLHandlerHints.STREAM_HINT)).ft);
                stream(f, (FCBuffer) hints.get(XMLHandlerHints.STREAM_HINT));

                return null;
            }
            f = getFeature(element, value, attrs, hints, null);
            return f;
        }

        public SimpleFeature getFeature(
                Element element,
                ElementValue[] value,
                Attributes attrs,
                Map hints,
                SimpleFeatureType ft)
                throws SAXException {
            if (ft == null)
                ft =
                        (SimpleFeatureType)
                                featureTypeMappings.get(
                                        element.getType().getNamespace() + "#" + element.getName());

            if (ft == null) {
                ft = loadFeatureType(element, value, attrs);
            }

            Object[] values = new Object[ft.getAttributeCount()];
            for (int i = 0; i < values.length; i++) values[i] = null;

            for (int i = 0; i < value.length; i++) {
                // find index for value
                int j = -1;
                setAttribute(value, ft, values, i, j);
            }

            String fid = attrs.getValue("", "fid");

            if ((fid == null) || "".equals(fid)) {
                fid = attrs.getValue(GMLSchema.NAMESPACE.toString(), "fid");
            }

            SimpleFeature rt = null;
            if ((fid != null) || !"".equals(fid)) {
                try {
                    rt = SimpleFeatureBuilder.build(ft, values, fid);
                } catch (IllegalAttributeException e) {
                    logger.warning(e.toString());
                    throw new SAXException(e);
                }
            }

            try {
                if (rt == null) {
                    rt = SimpleFeatureBuilder.build(ft, values, null);
                }
            } catch (IllegalAttributeException e1) {
                logger.warning(e1.toString());
                throw new SAXException(e1);
            }

            return rt;
        }

        private void setAttribute(
                ElementValue[] value, SimpleFeatureType ft, Object[] values, int i, int j) {
            j = searchByName(value, ft, i, j);
            if (j != -1) {
                assignValue(value, values, ft.getDescriptor(j), i, j);
            } else {
                searchByType(value, ft, values, i);
            }
        }

        /**
         * HACK !!!
         *
         * <p>not found ... is it the same name as the FT (ie ... is this part of the consequences
         * of GT's featureType model)?
         */
        private void searchByType(
                ElementValue[] value, SimpleFeatureType ft, Object[] values, int i) {

            if (value[i].getValue() != null) {
                // first check same index since it is supposed to be the same...
                if (isMatch(value, ft, i, i)) {
                    assignValue(value, values, ft.getDescriptor(i), i, i);
                    return;
                }
                for (int k = 0; k < ft.getAttributeCount(); k++) {
                    if (isMatch(value, ft, i, k)) {
                        if (values[i] == null)
                            assignValue(value, values, ft.getDescriptor(k), i, k);
                    }
                }
            }
        }

        private void assignValue(
                ElementValue[] value, Object[] values, AttributeDescriptor at, int i, int k) {
            if (at instanceof ChoiceAttributeType) {
                ChoiceAttributeType choiceAT = (ChoiceAttributeType) at;
                values[k] = choiceAT.convert(value[i].getValue());
            } else values[k] = value[i].getValue();
        }

        private boolean isMatch(ElementValue[] value, SimpleFeatureType ft, int i, int k) {
            AttributeDescriptor AttributeDescriptor = ft.getDescriptor(k);
            String typeName = ft.getTypeName();

            if (!AttributeDescriptor.getLocalName().equals(typeName)) return false;

            Class instanceClass = value[i].getValue().getClass();

            if (AttributeDescriptor instanceof ChoiceAttributeType) {
                ChoiceAttributeType choiceAT = (ChoiceAttributeType) AttributeDescriptor;
                Class[] choices = choiceAT.getChoices();
                for (int j = 0; j < choices.length; j++) {
                    if (choices[j].isAssignableFrom(instanceClass)) return true;
                }
            }
            return AttributeDescriptor.getType().getBinding().isAssignableFrom(instanceClass);
        }

        private int searchByName(ElementValue[] value, SimpleFeatureType ft, int i, int j) {
            for (int k = 0; k < ft.getAttributeCount() && j == -1; k++) {
                // TODO use equals
                if ((ft.getDescriptor(k).getLocalName() == null
                                && value[i].getElement().getName() == null)
                        || ft.getDescriptor(k)
                                .getLocalName()
                                .equals(value[i].getElement().getName())) j = k;
            }
            return j;
        }

        private void stream(SimpleFeature feature, FCBuffer featureCollectionBuffer)
                throws SAXNotSupportedException, SAXException {
            if (!featureCollectionBuffer.addFeature(feature)) {
                throw new SAXException("Buffer overflow");
            }

            if (featureCollectionBuffer.state <= 0) {
                switch (featureCollectionBuffer.state) {
                    case FCBuffer.STOP:
                        throw new StopException(); // alternative to stop()

                    case FCBuffer.FINISH:
                        return;

                    default:
                        featureCollectionBuffer.state =
                                (featureCollectionBuffer.getCapacity()
                                                - featureCollectionBuffer.getSize())
                                        / 3;
                        logger.finest(
                                "New State "
                                        + featureCollectionBuffer.state
                                        + " "
                                        + featureCollectionBuffer.getSize());

                        while (featureCollectionBuffer.getSize()
                                > (featureCollectionBuffer.getCapacity() - 1)) {
                            logger.finest("waiting for reader");
                            synchronized (featureCollectionBuffer) {
                                try {
                                    featureCollectionBuffer.wait(100);
                                } catch (InterruptedException e) {
                                    throw new StopException(); // alternative to stop()
                                }
                            }
                            //                        Thread.yield();
                            if (featureCollectionBuffer.state == FCBuffer.STOP)
                                throw new StopException(); // alternative to stop()
                        }
                }
            } else {
                featureCollectionBuffer.state--;
                logger.finest(
                        "New State "
                                + featureCollectionBuffer.state
                                + " "
                                + featureCollectionBuffer.getSize());
            }
        }

        /** @see org.geotools.xml.xsi.Type#getInstanceType() */
        public Class getInstanceType() {
            return SimpleFeature.class;
        }

        /*
         * Creates a FT from the information provided
         */
        private SimpleFeatureType loadFeatureType(
                Element element, ElementValue[] value, Attributes attrs) throws SAXException {

            SimpleFeatureType ft = createFeatureType(element);
            featureTypeMappings.put(element.getType().getNamespace() + "#" + element.getName(), ft);
            return ft;
        }

        /** @see schema.ComplexType#findChildElement(java.lang.String) */
        public Element findChildElement(String name) {
            if (name == null) {
                return null;
            }

            for (int i = 0; i < elements.length; i++)
                if (name.equals(elements[i].getName())) {
                    return elements[i];
                }

            return null;
        }

        /**
         * @see org.geotools.xml.schema.Type#canEncode(org.geotools.xml.schema.Element,
         *     java.lang.Object, java.util.Map)
         */
        public boolean canEncode(Element element, Object value, Map hints) {
            if ((value == null)
                    || (element == null)
                    || value instanceof FeatureCollection
                    || !(value instanceof SimpleFeature)) {
                return false;
            }

            ComplexType t =
                    (element.getType() instanceof ComplexType)
                            ? (ComplexType) element.getType()
                            : null;

            while ((t != null) && (t != this))
                t = (t.getParent() instanceof ComplexType) ? (ComplexType) t.getParent() : null;
            return t != null;
        }

        /**
         * @see org.geotools.xml.schema.Type#encode(org.geotools.xml.schema.Element,
         *     java.lang.Object, org.geotools.xml.PrintHandler, java.util.Map)
         */
        public void encode(Element element, Object value, PrintHandler output, Map hints)
                throws IOException, OperationNotSupportedException {
            if (canEncode(element, value, hints)) {
                SimpleFeature f = (SimpleFeature) value;
                if (element == null) {
                    print(f, output, hints);
                } else {
                    print(element, f, output, hints);
                }
            }
        }

        private void print(Element e, SimpleFeature f, PrintHandler ph, Map hints)
                throws OperationNotSupportedException, IOException {
            AttributesImpl ai = new AttributesImpl();

            if ((f.getID() != null) && !f.getID().equals("")) {
                ai.addAttribute("", "fid", "", "ID", f.getID());
            } else {
                ai = null;
            }

            ph.startElement(e.getNamespace(), e.getName(), ai);

            SimpleFeatureType ft = f.getFeatureType();
            List<AttributeDescriptor> ats = ft.getAttributeDescriptors();
            if (ats != null) {
                for (int i = 0; i < ats.size(); i++) {
                    Element e2 = e.findChildElement(ats.get(i).getLocalName());
                    Type type = e2.getType();
                    Object value = f.getAttribute(i);
                    if (!canEncodeValue(type, value)) {
                        continue;
                    }
                    e2.getType().encode(e2, value, ph, hints);
                }
            }

            ph.endElement(e.getNamespace(), e.getName());
        }

        private void print(SimpleFeature f, PrintHandler ph, Map hints)
                throws OperationNotSupportedException, IOException {
            AttributesImpl ai = new AttributesImpl();

            if ((f.getID() != null) && !f.getID().equals("")) {
                ai.addAttribute("", "fid", "", "ID", f.getID());
            } else {
                ai = null;
            }

            ph.startElement(GMLSchema.NAMESPACE, "_Feature", ai);

            SimpleFeatureType ft = f.getFeatureType();
            List<AttributeDescriptor> ats = ft.getAttributeDescriptors();

            if (ats != null) {
                for (int i = 0; i < ats.size(); i++) {
                    Type t = XSISimpleTypes.find(ats.get(i).getType().getBinding());
                    Object value = f.getAttribute(i);
                    if (!canEncodeValue(t, value)) {
                        continue;
                    }
                    t.encode(null, value, ph, hints);
                }
            }
            ph.endElement(GMLSchema.NAMESPACE, "_Feature");
        }

        private boolean canEncodeValue(Type type, Object value) {
            // intended to filter null geometries
            // which would otherwise cause error later on
            boolean cantPrint = (value == null && !(type instanceof SimpleType));
            return !cantPrint;
        }
    }

    /**
     * This class represents an AbstractFeatureCollectionsBaseType within the GML Schema. This
     * includes both the data and parsing functionality associated with an
     * AbstractFeatureCollectionsBaseType.
     *
     * @author dzwiers
     * @see GMLComplexType
     * @see ComplexType
     */
    public static class AbstractFeatureCollectionsBaseType extends AbstractFeatureType {
        // static element list
        private static final Element[] elements1 = {
            new GMLElement("description", XSISimpleTypes.String.getInstance(), 0, 1, false, null),
            new GMLElement("name", XSISimpleTypes.String.getInstance(), 0, 1, false, null),
            new GMLElement(
                    "boundedBy",
                    GMLComplexTypes.BoundingShapeType.getInstance(),
                    1,
                    1,
                    false,
                    null),
        };

        // static sequence
        private static final DefaultSequence seq1 = new DefaultSequence(elements1);

        // singleton instance
        private static final GMLComplexType instance1 = new AbstractFeatureCollectionsBaseType();

        /*
         * part of the singleton pattern
         *
         * @see GMLComplexType#getInstance()
         */
        public static GMLComplexType getInstance() {
            return instance1;
        }

        /** @see schema.ComplexType#isAbstract() */
        public boolean isAbstract() {
            return true;
        }

        /** @see schema.ComplexType#getAnyAttributeNameSpace() */
        public String getAnyAttributeNameSpace() {
            return null;
        }

        /** @see schema.ComplexType#getAttributeDescriptors() */
        public Attribute[] getAttributes() {
            return AbstractFeatureType.attributes;
        }

        /** @see schema.ComplexType#getChildren() */
        public ElementGrouping getChild() {
            return seq1;
        }

        /** @see schema.ComplexType#getLocalName() */
        public String getName() {
            return "AbstractFeatureCollectionBaseType";
        }

        /** @see schema.ComplexType#findChildElement(java.lang.String) */
        public Element findChildElement(String name) {
            if (name == null) {
                return null;
            }

            for (int i = 0; i < elements1.length; i++)
                if (name.equals(elements1[i].getName())) {
                    return elements1[i];
                }

            return null;
        }
    }

    /**
     * This class represents an AbstractFeatureCollectionType within the GML Schema. This includes
     * both the data and parsing functionality associated with an AbstractFeatureCollectionType.
     *
     * @author dzwiers
     * @see GMLComplexType
     * @see ComplexType
     */
    public static class AbstractFeatureCollectionType extends GMLComplexType {

        /** @see org.geotools.xml.schema.ComplexType#getChildElements() */
        public Element[] getChildElements() {
            return elements;
        }

        // singleton instance
        private static final GMLComplexType instance = new AbstractFeatureCollectionType();

        // static element list
        private static final Element[] elements = {
            new GMLElement("description", XSISimpleTypes.String.getInstance(), 0, 1, false, null),
            new GMLElement("name", XSISimpleTypes.String.getInstance(), 0, 1, false, null),
            new GMLElement(
                    "boundedBy",
                    GMLComplexTypes.BoundingShapeType.getInstance(),
                    1,
                    1,
                    false,
                    null),
            new GMLElement(
                    "featureMember",
                    GMLComplexTypes.FeatureAssociationType.getInstance(),
                    0,
                    ElementGrouping.UNBOUNDED,
                    false,
                    null),
        };

        // static sequence
        private static final DefaultSequence seq = new DefaultSequence(elements);

        /*
         * part of the singleton pattern
         *
         * @see GMLComplexType#getInstance()
         */
        public static GMLComplexType getInstance() {
            return instance;
        }

        /** @see schema.ComplexType#isAbstract() */
        public boolean isAbstract() {
            return true;
        }

        /** @see schema.ComplexType#getAnyAttributeNameSpace() */
        public String getAnyAttributeNameSpace() {
            return null;
        }

        /** @see schema.ComplexType#getAttributeDescriptors() */
        public Attribute[] getAttributes() {
            return null;
        }

        /** @see schema.Type#getValue(java.util.List) */
        public Object getValue(Element element, ElementValue[] value, Attributes attrs, Map hints) {
            if ((hints == null) || (hints.get(XMLHandlerHints.STREAM_HINT) == null)) {
                return getCollection(attrs, value);
            }

            @SuppressWarnings("PMD.CloseResource") // not managed here
            FCBuffer fcb = (FCBuffer) hints.get(XMLHandlerHints.STREAM_HINT);
            fcb.state = FCBuffer.FINISH;

            return null;
        }

        /**
         * @see org.geotools.xml.schema.ComplexType#cache(org.geotools.xml.schema.Element,
         *     java.util.Map)
         */
        public boolean cache(Element element, Map hints) {
            if ((hints == null) || (hints.get(XMLHandlerHints.STREAM_HINT) == null)) {
                return true;
            }

            ComplexType e =
                    (element.getType() instanceof ComplexType)
                            ? (ComplexType) element.getType()
                            : null;

            while (e != null) {
                if ((e.getName() != null) && e.getName().equals(BoxType.getInstance().getName())) {
                    return true;
                }

                e = (e.getParent() instanceof ComplexType) ? (ComplexType) e.getParent() : null;
            }

            return false;
        }

        private SimpleFeatureCollection getCollection(Attributes attrs, ElementValue[] value) {

            String id = "";
            id = attrs.getValue("", "ID");
            if (id == null) id = attrs.getValue(GMLSchema.NAMESPACE.toString(), "ID");
            Object value2 = value[0].getValue();
            Envelope envelopeInternal = ((Geometry) value2).getEnvelopeInternal();
            // bbox slot
            GMLFeatureCollection fc = new GMLFeatureCollection(id, envelopeInternal);

            for (int i = 1; i < value.length; i++) // bbox is slot 0
            fc.add((SimpleFeature) value[i].getValue());

            return fc;
        }

        /** @see org.geotools.xml.xsi.Type#getInstanceType() */
        public Class getInstanceType() {
            return FeatureCollection.class;
        }

        /** @see schema.ComplexType#getChildren() */
        public ElementGrouping getChild() {
            return seq;
        }

        /** @see schema.ComplexType#getLocalName() */
        public String getName() {
            return "AbstractFeatureCollectionType";
        }

        /** @see schema.ComplexType#findChildElement(java.lang.String) */
        public Element findChildElement(String name) {
            if (name == null) {
                return null;
            }

            for (int i = 0; i < elements.length; i++)
                if (name.equals(elements[i].getName())) {
                    return elements[i];
                }

            return null;
        }

        /**
         * @see org.geotools.xml.schema.Type#canEncode(org.geotools.xml.schema.Element,
         *     java.lang.Object, java.util.Map)
         */
        public boolean canEncode(Element element, Object value, Map hints) {
            ComplexType t =
                    (element.getType() instanceof ComplexType)
                            ? (ComplexType) element.getType()
                            : null;

            while ((t != null) && (t != this))
                t = (t.getParent() instanceof ComplexType) ? (ComplexType) t.getParent() : null;

            return ((t != null)
                    && (value instanceof FeatureCollection)
                    && ((SimpleFeatureCollection) value).getBounds() != null);
        }

        /**
         * @see org.geotools.xml.schema.Type#encode(org.geotools.xml.schema.Element,
         *     java.lang.Object, org.geotools.xml.PrintHandler, java.util.Map)
         */
        public void encode(Element element, Object value, PrintHandler output, Map hints)
                throws IOException, OperationNotSupportedException {
            if ((value == null) || (!(value instanceof FeatureCollection))) {
                return;
            }

            if (element == null) {
                output.startElement(GMLSchema.NAMESPACE, "_featureCollection", null);
            } else {
                output.startElement(element.getNamespace(), element.getName(), null);
            }

            SimpleFeatureCollection fc = (SimpleFeatureCollection) value;

            if (fc.getBounds() != null) {
                BoundingShapeType.getInstance().encode(null, fc.getBounds(), output, hints);
            } else {
                throw new IOException("Bounding box required for the FeatureCollection");
            }

            Element e = null;
            try (SimpleFeatureIterator i = fc.features()) {

                while (i.hasNext()) {
                    SimpleFeature f = i.next();
                    output.startElement(GMLSchema.NAMESPACE, "featureMember", null);

                    if (e == null) { // first time
                        e = output.findElement(f.getFeatureType().getTypeName());
                        // should go to an abstract FT eventually
                        ComplexType t =
                                e.getType() instanceof ComplexType
                                        ? (ComplexType) e.getType()
                                        : null;
                        while (t != null && t != AbstractFeatureType.getInstance())
                            t =
                                    t.getParent() instanceof ComplexType
                                            ? (ComplexType) t.getParent()
                                            : null;
                        if (t != AbstractFeatureType.getInstance()) {
                            // not the right element ... try by type
                            e = output.findElement(value);
                            // should go to an abstract FT eventually
                            t =
                                    e.getType() instanceof ComplexType
                                            ? (ComplexType) e.getType()
                                            : null;
                            while (t != null && t != AbstractFeatureType.getInstance())
                                t =
                                        t.getParent() instanceof ComplexType
                                                ? (ComplexType) t.getParent()
                                                : null;
                            if (t != AbstractFeatureType.getInstance()) {
                                throw new OperationNotSupportedException(
                                        "Could not find a correct Element for FeatureType "
                                                + f.getFeatureType().getTypeName());
                            }
                        }
                    }

                    if (e == null) {
                        throw new NullPointerException(
                                "Feature Definition not found in Schema " + element.getNamespace());
                    }

                    AbstractFeatureType.getInstance().encode(e, f, output, hints);
                    output.endElement(GMLSchema.NAMESPACE, "featureMember");
                }
            }

            if (element == null) {
                output.endElement(GMLSchema.NAMESPACE, "_featureCollection");
            } else {
                output.endElement(element.getNamespace(), element.getName());
            }
        }
    }

    /**
     * This class represents an GeometryPropertyType within the GML Schema. This includes both the
     * data and parsing functionality associated with an GeometryPropertyType.
     *
     * @author dzwiers
     * @see GMLComplexType
     * @see ComplexType
     */
    public static class GeometryPropertyType extends GMLComplexType {

        /** @see org.geotools.xml.schema.ComplexType#getChildElements() */
        public Element[] getChildElements() {
            return elements;
        }

        // singleton instance
        private static final GMLComplexType instance = new GeometryPropertyType();

        // static attribute list
        private static final Attribute[] attributes = loadAttributes();

        // static element list
        private static final Element[] elements = {
            new GMLElement(
                    "_Geometry",
                    GMLComplexTypes.AbstractGeometryType.getInstance(),
                    0,
                    1,
                    true,
                    null),
        };

        // static sequence
        private static final DefaultSequence seq = new DefaultSequence(elements);

        /*
         * part of the singleton pattern
         *
         * @see GMLComplexType#getInstance()
         */
        public static GMLComplexType getInstance() {
            return instance;
        }

        /** @see schema.ComplexType#isAbstract() */
        public boolean isAbstract() {
            return false;
        }

        /** @see schema.ComplexType#getAnyAttributeNameSpace() */
        public String getAnyAttributeNameSpace() {
            return null;
        }

        /*
         * statically loads the attributes for a Geom Prop Type
         */
        private static Attribute[] loadAttributes() {
            Attribute[] gp = XLinkSchema.SimpleLink.getInstance().getAttributes();
            Attribute[] r = new Attribute[gp.length + 1];

            for (int i = 1; i < gp.length; i++) r[i] = gp[i];

            r[gp.length] = GMLSchema.AttributeList.attributes1[0];

            return r;
        }

        /** @see schema.ComplexType#getAttributeDescriptors() */
        public Attribute[] getAttributes() {
            return attributes;
        }

        /** @see schema.ComplexType#getChildren() */
        public ElementGrouping getChild() {
            return seq;
        }

        /** @see schema.ComplexType#getLocalName() */
        public String getName() {
            return "GeometryPropertyType";
        }

        /** @see schema.Type#getValue(java.util.List) */
        public Object getValue(Element element, ElementValue[] value, Attributes attrs, Map hints)
                throws SAXException {
            if ((value == null) || (value.length != 1)) {
                throw new SAXException("must be one geometry");
            }

            return (Geometry) value[0].getValue();
        }

        /** @see org.geotools.xml.xsi.Type#getInstanceType() */
        public Class getInstanceType() {
            return Geometry.class;
        }

        /** @see schema.ComplexType#findChildElement(java.lang.String) */
        public Element findChildElement(String name) {
            if (name == null) {
                return null;
            }

            for (int i = 0; i < elements.length; i++)
                if (name.equals(elements[i].getName())) {
                    return elements[i];
                }

            return null;
        }

        /**
         * @see org.geotools.xml.schema.Type#canEncode(org.geotools.xml.schema.Element,
         *     java.lang.Object, java.util.Map)
         */
        public boolean canEncode(Element element, Object value, Map hints) {
            ComplexType t =
                    (element.getType() instanceof ComplexType)
                            ? (ComplexType) element.getType()
                            : null;

            while ((t != null) && (t != this))
                t = (t.getParent() instanceof ComplexType) ? (ComplexType) t.getParent() : null;

            return ((t != null) && (value instanceof Geometry));
        }

        /**
         * @see org.geotools.xml.schema.Type#encode(org.geotools.xml.schema.Element,
         *     java.lang.Object, org.geotools.xml.PrintHandler, java.util.Map)
         */
        public void encode(Element element, Object value, PrintHandler output, Map hints)
                throws IOException, OperationNotSupportedException {
            if (!(value instanceof Geometry)) {
                return;
            }

            Geometry g = (Geometry) value;

            if (element == null) {
                output.startElement(GMLSchema.NAMESPACE, "geometryProperty", null);
            } else {
                output.startElement(element.getNamespace(), element.getName(), null);
            }

            GMLComplexTypes.encode(null, g, output);

            if (element == null) {
                output.endElement(GMLSchema.NAMESPACE, "geometryProperty");
            } else {
                output.endElement(element.getNamespace(), element.getName());
            }
        }
    }

    /**
     * This class represents an FeatureAssociationType within the GML Schema. This includes both the
     * data and parsing functionality associated with an FeatureAssociationType.
     *
     * @author dzwiers
     * @see GMLComplexType
     * @see ComplexType
     */
    public static class FeatureAssociationType extends GMLComplexType {

        /** @see org.geotools.xml.schema.ComplexType#getChildElements() */
        public Element[] getChildElements() {
            return elements;
        }

        // singleton instance
        private static final GMLComplexType instance = new FeatureAssociationType();

        // static attribute list
        protected static final Attribute[] attributes = loadAttributes();

        // static element list
        private static final Element[] elements = {
            new GMLElement(
                    "_Feature",
                    GMLComplexTypes.AbstractFeatureType.getInstance(),
                    0,
                    1,
                    true,
                    null),
        };

        // static sequence
        static final DefaultSequence seq = new DefaultSequence(elements);

        /*
         * part of the singleton pattern
         *
         * @see GMLComplexType#getInstance()
         */
        public static GMLComplexType getInstance() {
            return instance;
        }

        /** @see schema.ComplexType#isAbstract() */
        public boolean isAbstract() {
            return false;
        }

        /** @see schema.ComplexType#getAnyAttributeNameSpace() */
        public String getAnyAttributeNameSpace() {
            return null;
        }

        /*
         * statically loads the attributes for a Feature Association Type
         */
        private static Attribute[] loadAttributes() {
            Attribute[] gp = XLinkSchema.SimpleLink.getInstance().getAttributes();
            Attribute[] r = new Attribute[gp.length + 1];

            for (int i = 1; i < gp.length; i++) r[i] = gp[i];

            r[gp.length] = GMLSchema.AttributeList.attributes1[0];

            return r;
        }

        /** @see schema.ComplexType#getAttributeDescriptors() */
        public Attribute[] getAttributes() {
            return attributes;
        }

        /** @see schema.ComplexType#getChildren() */
        public ElementGrouping getChild() {
            return seq;
        }

        /** @see schema.ComplexType#getLocalName() */
        public String getName() {
            return "FeatureAssociationType";
        }

        /** @see schema.Type#getValue(java.util.List) */
        @SuppressFBWarnings("NP_NULL_ON_SOME_PATH")
        public SimpleFeature getValue(
                Element element, ElementValue[] value, Attributes attrs, Map hints)
                throws SAXException {
            if ((value == null) || (value.length != 1)) {
                throw new SAXException("must be one feature " + value.length);
            }

            logger.finest(
                    (value[0].getValue() == null)
                            ? "null"
                            : value[0].getValue().getClass().getName());

            return (SimpleFeature) value[0].getValue();
        }

        /** @see org.geotools.xml.xsi.Type#getInstanceType() */
        public Class getInstanceType() {
            return SimpleFeature.class;
        }

        /** @see schema.ComplexType#findChildElement(java.lang.String) */
        public Element findChildElement(String name) {
            if (name == null) {
                return null;
            }

            for (int i = 0; i < elements.length; i++)
                if (name.equals(elements[i].getName())) {
                    return elements[i];
                }

            return null;
        }

        /**
         * @see org.geotools.xml.schema.Type#canEncode(org.geotools.xml.schema.Element,
         *     java.lang.Object, java.util.Map)
         */
        public boolean canEncode(Element element, Object value, Map hints) {
            if (!(value instanceof SimpleFeature)) {
                return false;
            }

            ComplexType t =
                    (element.getType() instanceof ComplexType)
                            ? (ComplexType) element.getType()
                            : null;

            while ((t != null) && (t != this))
                t = (t.getParent() instanceof ComplexType) ? (ComplexType) t.getParent() : null;

            return t == this;
        }

        /**
         * @see org.geotools.xml.schema.Type#encode(org.geotools.xml.schema.Element,
         *     java.lang.Object, org.geotools.xml.PrintHandler, java.util.Map)
         */
        public void encode(Element element, Object value, PrintHandler output, Map hints)
                throws IOException, OperationNotSupportedException {
            if (!(value instanceof SimpleFeature)) {
                return;
            }

            if (element == null) {
                output.startElement(GMLSchema.NAMESPACE, "featureMember", null);
                AbstractFeatureType.getInstance().encode(null, value, output, hints);
                output.endElement(GMLSchema.NAMESPACE, "featureMember");
            } else {
                output.startElement(element.getNamespace(), element.getName(), null);
                AbstractFeatureType.getInstance()
                        .encode(
                                element.findChildElement(
                                        ((SimpleFeature) value).getFeatureType().getTypeName()),
                                value,
                                output,
                                hints);
                output.endElement(element.getNamespace(), element.getName());
            }
        }
    }

    /**
     * This class represents an BoundingShapeType within the GML Schema. This includes both the data
     * and parsing functionality associated with an BoundingShapeType.
     *
     * @author dzwiers
     * @see GMLComplexType
     * @see ComplexType
     */
    public static class BoundingShapeType extends GMLComplexType {

        /** @see org.geotools.xml.schema.ComplexType#getChildElements() */
        public Element[] getChildElements() {
            return elements;
        }

        // singleton instance
        private static final GMLComplexType instance = new BoundingShapeType();

        // static element list
        private static final Element[] elements = {
            new GMLElement("Box", GMLComplexTypes.BoxType.getInstance(), 1, 1, false, null),
            new GMLElement("null", new GMLNullType(), 1, 1, false, null),
        };

        // static choice
        private static final DefaultChoice seq = new DefaultChoice(elements);

        private static final Geometry NULL_ENV;

        static {
            GeometryFactory fac = new GeometryFactory();
            NULL_ENV = fac.toGeometry(new Envelope());
        }

        /*
         * part of the singleton pattern
         *
         * @see GMLComplexType#getInstance()
         */
        public static GMLComplexType getInstance() {
            return instance;
        }

        /** @see schema.ComplexType#isAbstract() */
        public boolean isAbstract() {
            return false;
        }

        /** @see schema.ComplexType#getAnyAttributeNameSpace() */
        public String getAnyAttributeNameSpace() {
            return null;
        }

        /** @see schema.ComplexType#getAttributeDescriptors() */
        public Attribute[] getAttributes() {
            return null;
        }

        /** @see schema.ComplexType#getChildren() */
        public ElementGrouping getChild() {
            return seq;
        }

        /** @see schema.ComplexType#getLocalName() */
        public String getName() {
            return "BoundingShapeType";
        }

        /** @see schema.Type#getValue(java.util.List) */
        public Object getValue(Element element, ElementValue[] value, Attributes attrs, Map hints)
                throws SAXException {
            if ((value == null) || (value.length != 1)) {
                throw new SAXException("must be one geometry");
            }

            return value[0].getValue() instanceof Geometry
                    ? (Geometry) value[0].getValue()
                    : NULL_ENV;
        }

        /** @see org.geotools.xml.xsi.Type#getInstanceType() */
        public Class getInstanceType() {
            return Geometry.class;
        }

        /** @see schema.ComplexType#findChildElement(java.lang.String) */
        public Element findChildElement(String name) {
            if (name == null) {
                return null;
            }

            for (int i = 0; i < elements.length; i++)
                if (name.equals(elements[i].getName())) {
                    return elements[i];
                }

            return null;
        }

        /**
         * @see org.geotools.xml.schema.Type#canEncode(org.geotools.xml.schema.Element,
         *     java.lang.Object, java.util.Map)
         */
        public boolean canEncode(Element element, Object value, Map hints) {
            if ((value == null) || (element == null) || !(value instanceof Geometry)) {
                return false;
            }

            ComplexType t =
                    (element.getType() instanceof ComplexType)
                            ? (ComplexType) element.getType()
                            : null;

            while ((t != null) && (t != this))
                t = (t.getParent() instanceof ComplexType) ? (ComplexType) t.getParent() : null;

            return t != null;
        }

        /**
         * @see org.geotools.xml.schema.Type#encode(org.geotools.xml.schema.Element,
         *     java.lang.Object, org.geotools.xml.PrintHandler, java.util.Map)
         */
        public void encode(Element element, Object value, PrintHandler output, Map hints)
                throws IOException, OperationNotSupportedException {
            if (!(value instanceof Geometry)) {
                return;
            }

            if (element == null) {
                output.startElement(GMLSchema.NAMESPACE, "boundedBy", null);
                BoxType.getInstance().encode(null, value, output, hints);
                output.endElement(GMLSchema.NAMESPACE, "boundedBy");
            } else {
                output.startElement(element.getNamespace(), element.getName(), null);

                //                if (element.findChildElement("Box") != null) {
                //                    if
                // (element.findChildElement("Box").getType().canEncode(element
                //                                .findChildElement("Box"), value, hints)) {
                //                        element.findChildElement("Box").getType().encode(element
                //                            .findChildElement("Box"), value, output, hints);
                //                    }
                //                }else{
                BoxType.getInstance().encode(null, value, output, hints);
                //                }

                //                BoxType.getInstance().encode(null, value, output, hints);
                output.endElement(element.getNamespace(), element.getName());
            }
        }
    }

    /**
     * This class represents an PointPropertyType within the GML Schema. This includes both the data
     * and parsing functionality associated with an PointPropertyType.
     *
     * @author dzwiers
     * @see GMLComplexType
     * @see ComplexType
     */
    public static class PointPropertyType extends GMLComplexType {

        /** @see org.geotools.xml.schema.ComplexType#getChildElements() */
        public Element[] getChildElements() {
            return elements;
        }

        // singleton instance
        private static final GMLComplexType instance = new PointPropertyType();

        // static element list
        private static final Element[] elements = {
            new GMLElement(
                    "Point",
                    GMLComplexTypes.PointType.getInstance(),
                    0,
                    1,
                    false,
                    new GMLElement(
                            "_Geometry",
                            GMLComplexTypes.AbstractGeometryType.getInstance(),
                            1,
                            1,
                            true,
                            null)),
        };

        // static sequence
        private static final DefaultSequence seq = new DefaultSequence(elements);

        /*
         * part of the singleton pattern
         *
         * @see GMLComplexType#getInstance()
         */
        public static GMLComplexType getInstance() {
            return instance;
        }

        /** @see schema.ComplexType#isAbstract() */
        public boolean isAbstract() {
            return false;
        }

        /** @see schema.ComplexType#getAnyAttributeNameSpace() */
        public String getAnyAttributeNameSpace() {
            return null;
        }

        /** @see schema.ComplexType#getAttributeDescriptors() */
        public Attribute[] getAttributes() {
            return FeatureAssociationType.attributes;
        }

        /** @see schema.ComplexType#getChildren() */
        public ElementGrouping getChild() {
            return seq;
        }

        /** @see schema.ComplexType#getLocalName() */
        public String getName() {
            return "PointPropertyType";
        }

        /** @see schema.Type#getValue(java.util.List) */
        public Object getValue(Element element, ElementValue[] value, Attributes attrs, Map hints)
                throws SAXException {
            if ((value == null) || (value.length != 1)) {
                throw new SAXException("must be one geometry");
            }

            return (Point) value[0].getValue();
        }

        /** @see org.geotools.xml.xsi.Type#getInstanceType() */
        public Class getInstanceType() {
            return Point.class;
        }

        /** @see schema.ComplexType#findChildElement(java.lang.String) */
        public Element findChildElement(String name) {
            if (name == null) {
                return null;
            }

            for (int i = 0; i < elements.length; i++)
                if (name.equals(elements[i].getName())) {
                    return elements[i];
                }

            return null;
        }

        /**
         * @see org.geotools.xml.schema.Type#canEncode(org.geotools.xml.schema.Element,
         *     java.lang.Object, java.util.Map)
         */
        public boolean canEncode(Element element, Object value, Map hints) {
            if ((value == null) || !(value instanceof Point)) {
                return false;
            }

            ComplexType t =
                    (element.getType() instanceof ComplexType)
                            ? (ComplexType) element.getType()
                            : null;

            while ((t != null) && (t != this))
                t = (t.getParent() instanceof ComplexType) ? (ComplexType) t.getParent() : null;

            return t != null;
        }

        /**
         * @see org.geotools.xml.schema.Type#encode(org.geotools.xml.schema.Element,
         *     java.lang.Object, org.geotools.xml.PrintHandler, java.util.Map)
         */
        public void encode(Element element, Object value, PrintHandler output, Map hints)
                throws IOException, OperationNotSupportedException {
            if ((value == null) || !(value instanceof Point)) {
                throw new OperationNotSupportedException(
                        "Value is " + value == null ? "null" : value.getClass().getName());
            }

            if (element == null) {
                output.startElement(GMLSchema.NAMESPACE, "pointProperty", null);
                GMLComplexTypes.encode(null, (Point) value, output);
                output.endElement(GMLSchema.NAMESPACE, "pointProperty");
            } else {
                output.startElement(element.getNamespace(), element.getName(), null);
                GMLComplexTypes.encode(null, (Point) value, output);
                output.endElement(element.getNamespace(), element.getName());
            }
        }
    }

    /**
     * This class represents an PolygonPropertyType within the GML Schema. This includes both the
     * data and parsing functionality associated with an PolygonPropertyType.
     *
     * @author dzwiers
     * @see GMLComplexType
     * @see ComplexType
     */
    public static class PolygonPropertyType extends GMLComplexType {

        /** @see org.geotools.xml.schema.ComplexType#getChildElements() */
        public Element[] getChildElements() {
            return elements;
        }

        // singleton instance
        private static final GMLComplexType instance = new PolygonPropertyType();

        // static element list
        private static final Element[] elements = {
            new GMLElement(
                    "Polygon",
                    GMLComplexTypes.PolygonType.getInstance(),
                    0,
                    1,
                    false,
                    new GMLElement(
                            "_Geometry",
                            GMLComplexTypes.AbstractGeometryType.getInstance(),
                            1,
                            1,
                            true,
                            null)),
        };

        // static sequence
        private static final DefaultSequence seq = new DefaultSequence(elements);

        /*
         * part of the singleton pattern
         *
         * @see GMLComplexType#getInstance()
         */
        public static GMLComplexType getInstance() {
            return instance;
        }

        /** @see schema.ComplexType#isAbstract() */
        public boolean isAbstract() {
            return false;
        }

        /** @see schema.ComplexType#getAnyAttributeNameSpace() */
        public String getAnyAttributeNameSpace() {
            return null;
        }

        /** @see schema.ComplexType#getAttributeDescriptors() */
        public Attribute[] getAttributes() {
            return FeatureAssociationType.attributes;
        }

        /** @see schema.ComplexType#getChildren() */
        public ElementGrouping getChild() {
            return seq;
        }

        /** @see schema.ComplexType#getLocalName() */
        public String getName() {
            return "PolygonPropertyType";
        }

        /** @see schema.Type#getValue(java.util.List) */
        public Object getValue(Element element, ElementValue[] value, Attributes attrs, Map hints)
                throws SAXException {
            if ((value == null) || (value.length != 1)) {
                throw new SAXException("must be one geometry");
            }

            return (Polygon) value[0].getValue();
        }

        /** @see org.geotools.xml.xsi.Type#getInstanceType() */
        public Class getInstanceType() {
            return Polygon.class;
        }

        /** @see schema.ComplexType#findChildElement(java.lang.String) */
        public Element findChildElement(String name) {
            if (name == null) {
                return null;
            }

            for (int i = 0; i < elements.length; i++)
                if (name.equals(elements[i].getName())) {
                    return elements[i];
                }

            return null;
        }

        /**
         * @see org.geotools.xml.schema.Type#canEncode(org.geotools.xml.schema.Element,
         *     java.lang.Object, java.util.Map)
         */
        public boolean canEncode(Element element, Object value, Map hints) {
            if ((value == null) || !(value instanceof Polygon)) {
                return false;
            }

            ComplexType t =
                    (element.getType() instanceof ComplexType)
                            ? (ComplexType) element.getType()
                            : null;

            while ((t != null) && (t != this))
                t = (t.getParent() instanceof ComplexType) ? (ComplexType) t.getParent() : null;

            return t != null;
        }

        /**
         * @see org.geotools.xml.schema.Type#encode(org.geotools.xml.schema.Element,
         *     java.lang.Object, org.geotools.xml.PrintHandler, java.util.Map)
         */
        public void encode(Element element, Object value, PrintHandler output, Map hints)
                throws IOException, OperationNotSupportedException {
            if ((value == null) || !(value instanceof Polygon)) {
                throw new OperationNotSupportedException(
                        "Value is " + value == null ? "null" : value.getClass().getName());
            }

            if (element == null) {
                output.startElement(GMLSchema.NAMESPACE, "polygonProperty", null);
                GMLComplexTypes.encode(null, (Polygon) value, output);
                output.endElement(GMLSchema.NAMESPACE, "polygonProperty");
            } else {
                output.startElement(element.getNamespace(), element.getName(), null);
                GMLComplexTypes.encode(null, (Polygon) value, output);
                output.endElement(element.getNamespace(), element.getName());
            }
        }
    }

    /**
     * This class represents an LineStringPropertyType within the GML Schema. This includes both the
     * data and parsing functionality associated with an LineStringPropertyType.
     *
     * @author dzwiers
     * @see GMLComplexType
     * @see ComplexType
     */
    public static class LineStringPropertyType extends GMLComplexType {

        /** @see org.geotools.xml.schema.ComplexType#getChildElements() */
        public Element[] getChildElements() {
            return elements;
        }

        // singleton instance
        private static final GMLComplexType instance = new LineStringPropertyType();

        // static element list
        private static final Element[] elements = {
            new GMLElement(
                    "LineString",
                    GMLComplexTypes.LineStringType.getInstance(),
                    0,
                    1,
                    false,
                    new GMLElement(
                            "_Geometry",
                            GMLComplexTypes.AbstractGeometryType.getInstance(),
                            1,
                            1,
                            true,
                            null)),
        };

        // static sequence
        private static final DefaultSequence seq = new DefaultSequence(elements);

        /*
         * part of the singleton pattern
         *
         * @see GMLComplexType#getInstance()
         */
        public static GMLComplexType getInstance() {
            return instance;
        }

        /** @see schema.ComplexType#isAbstract() */
        public boolean isAbstract() {
            return false;
        }

        /** @see schema.ComplexType#getAnyAttributeNameSpace() */
        public String getAnyAttributeNameSpace() {
            return null;
        }

        /** @see schema.ComplexType#getAttributeDescriptors() */
        public Attribute[] getAttributes() {
            return FeatureAssociationType.attributes;
        }

        /** @see schema.ComplexType#getChildren() */
        public ElementGrouping getChild() {
            return seq;
        }

        /** @see schema.ComplexType#getLocalName() */
        public String getName() {
            return "LineStringPropertyType";
        }

        /** @see schema.Type#getValue(java.util.List) */
        public Object getValue(Element element, ElementValue[] value, Attributes attrs, Map hints)
                throws SAXException {
            if ((value == null) || (value.length != 1)) {
                throw new SAXException("must be one geometry");
            }

            return (LineString) value[0].getValue();
        }

        /** @see org.geotools.xml.xsi.Type#getInstanceType() */
        public Class getInstanceType() {
            return LineString.class;
        }

        /** @see schema.ComplexType#findChildElement(java.lang.String) */
        public Element findChildElement(String name) {
            if (name == null) {
                return null;
            }

            for (int i = 0; i < elements.length; i++)
                if (name.equals(elements[i].getName())) {
                    return elements[i];
                }

            return null;
        }

        /**
         * @see org.geotools.xml.schema.Type#canEncode(org.geotools.xml.schema.Element,
         *     java.lang.Object, java.util.Map)
         */
        public boolean canEncode(Element element, Object value, Map hints) {
            if ((value == null) || !(value instanceof LineString)) {
                return false;
            }

            ComplexType t =
                    (element.getType() instanceof ComplexType)
                            ? (ComplexType) element.getType()
                            : null;

            while ((t != null) && (t != this))
                t = (t.getParent() instanceof ComplexType) ? (ComplexType) t.getParent() : null;

            return t != null;
        }

        /**
         * @see org.geotools.xml.schema.Type#encode(org.geotools.xml.schema.Element,
         *     java.lang.Object, org.geotools.xml.PrintHandler, java.util.Map)
         */
        public void encode(Element element, Object value, PrintHandler output, Map hints)
                throws IOException, OperationNotSupportedException {
            if ((value == null) || !(value instanceof LineString)) {
                throw new OperationNotSupportedException(
                        "Value is " + value == null ? "null" : value.getClass().getName());
            }

            if (element == null) {
                output.startElement(GMLSchema.NAMESPACE, "lineStringProperty", null);
                GMLComplexTypes.encode(null, (LineString) value, output);
                output.endElement(GMLSchema.NAMESPACE, "lineStringProperty");
            } else {
                output.startElement(element.getNamespace(), element.getName(), null);
                GMLComplexTypes.encode(null, (LineString) value, output);
                output.endElement(element.getNamespace(), element.getName());
            }
        }
    }

    /**
     * This class represents an MultiPointPropertyType within the GML Schema. This includes both the
     * data and parsing functionality associated with an MultiPointPropertyType.
     *
     * @author dzwiers
     * @see GMLComplexType
     * @see ComplexType
     */
    public static class MultiPointPropertyType extends GMLComplexType {

        /** @see org.geotools.xml.schema.ComplexType#getChildElements() */
        public Element[] getChildElements() {
            return elements;
        }

        // singleton instance
        private static final GMLComplexType instance = new MultiPointPropertyType();

        // static element list
        private static final Element[] elements = {
            new GMLElement(
                    "MultiPoint",
                    GMLComplexTypes.MultiPointType.getInstance(),
                    0,
                    1,
                    false,
                    new GMLElement(
                            "_Geometry",
                            GMLComplexTypes.AbstractGeometryType.getInstance(),
                            1,
                            1,
                            true,
                            null)),
        };

        // static sequence
        private static final DefaultSequence seq = new DefaultSequence(elements);

        /*
         * part of the singleton pattern
         *
         * @see GMLComplexType#getInstance()
         */
        public static GMLComplexType getInstance() {
            return instance;
        }

        /** @see schema.ComplexType#isAbstract() */
        public boolean isAbstract() {
            return false;
        }

        /** @see schema.ComplexType#getAnyAttributeNameSpace() */
        public String getAnyAttributeNameSpace() {
            return null;
        }

        /** @see schema.ComplexType#getAttributeDescriptors() */
        public Attribute[] getAttributes() {
            return FeatureAssociationType.attributes;
        }

        /** @see schema.ComplexType#getChildren() */
        public ElementGrouping getChild() {
            return seq;
        }

        /** @see schema.ComplexType#getLocalName() */
        public String getName() {
            return "MultiPointPropertyType";
        }

        /** @see schema.Type#getValue(java.util.List) */
        public Object getValue(Element element, ElementValue[] value, Attributes attrs, Map hints)
                throws SAXException {
            if ((value == null) || (value.length != 1)) {
                throw new SAXException("must be one geometry");
            }

            Object value2 = value[0].getValue();
            return (MultiPoint) value2;
            //            if( value2 instanceof MultiPoint)
            //            	return (MultiPoint) value2;
            //            else if( value2 instanceof Point ){
            //            	GeometryFactory fac=new GeometryFactory();
            //            	return fac.createMultiPoint(new Point[]{(Point) value2});
            //            }
            //            throw new SAXException("Expected value was a MultiPoint, instead it was a:
            // "+value2.getClass());
        }

        /** @see org.geotools.xml.xsi.Type#getInstanceType() */
        public Class getInstanceType() {
            return MultiPoint.class;
        }

        /** @see schema.ComplexType#findChildElement(java.lang.String) */
        public Element findChildElement(String name) {
            if (name == null) {
                return null;
            }

            for (int i = 0; i < elements.length; i++)
                if (name.equals(elements[i].getName())) {
                    return elements[i];
                }

            return null;
        }

        /**
         * @see org.geotools.xml.schema.Type#canEncode(org.geotools.xml.schema.Element,
         *     java.lang.Object, java.util.Map)
         */
        public boolean canEncode(Element element, Object value, Map hints) {
            if ((value == null) || !(value instanceof MultiPoint)) {
                return false;
            }

            ComplexType t =
                    (element.getType() instanceof ComplexType)
                            ? (ComplexType) element.getType()
                            : null;

            while ((t != null) && (t != this))
                t = (t.getParent() instanceof ComplexType) ? (ComplexType) t.getParent() : null;

            return t != null;
        }

        /**
         * @see org.geotools.xml.schema.Type#encode(org.geotools.xml.schema.Element,
         *     java.lang.Object, org.geotools.xml.PrintHandler, java.util.Map)
         */
        public void encode(Element element, Object value, PrintHandler output, Map hints)
                throws IOException, OperationNotSupportedException {
            if ((value == null) || !(value instanceof MultiPoint)) {
                throw new OperationNotSupportedException(
                        "Value is " + value == null ? "null" : value.getClass().getName());
            }

            if (element == null) {
                output.startElement(GMLSchema.NAMESPACE, "multiPointProperty", null);
                GMLComplexTypes.encode(null, (MultiPoint) value, output);
                output.endElement(GMLSchema.NAMESPACE, "multiPointProperty");
            } else {
                output.startElement(element.getNamespace(), element.getName(), null);
                GMLComplexTypes.encode(null, (MultiPoint) value, output);
                output.endElement(element.getNamespace(), element.getName());
            }
        }
    }

    /**
     * This class represents an MultiLineStringPropertyType within the GML Schema. This includes
     * both the data and parsing functionality associated with an MultiLineStringPropertyType.
     *
     * @author dzwiers
     * @see GMLComplexType
     * @see ComplexType
     */
    public static class MultiLineStringPropertyType extends GMLComplexType {

        /** @see org.geotools.xml.schema.ComplexType#getChildElements() */
        public Element[] getChildElements() {
            return elements;
        }

        // singleton instance
        private static final GMLComplexType instance = new MultiLineStringPropertyType();

        // static element list
        private static final Element[] elements = {
            new GMLElement(
                    "MultiLineString",
                    GMLComplexTypes.MultiLineStringType.getInstance(),
                    0,
                    1,
                    false,
                    new GMLElement(
                            "_Geometry",
                            GMLComplexTypes.AbstractGeometryType.getInstance(),
                            1,
                            1,
                            true,
                            null)),
        };

        // static sequence
        private static final DefaultSequence seq = new DefaultSequence(elements);

        /*
         * part of the singleton pattern
         *
         * @see GMLComplexType#getInstance()
         */
        public static GMLComplexType getInstance() {
            return instance;
        }

        /** @see schema.ComplexType#isAbstract() */
        public boolean isAbstract() {
            return false;
        }

        /** @see schema.ComplexType#getAnyAttributeNameSpace() */
        public String getAnyAttributeNameSpace() {
            return null;
        }

        /** @see schema.ComplexType#getAttributeDescriptors() */
        public Attribute[] getAttributes() {
            return FeatureAssociationType.attributes;
        }

        /** @see schema.ComplexType#getChildren() */
        public ElementGrouping getChild() {
            return seq;
        }

        /** @see schema.ComplexType#getLocalName() */
        public String getName() {
            return "MultiLineStringPropertyType";
        }

        /** @see schema.Type#getValue(java.util.List) */
        public Object getValue(Element element, ElementValue[] value, Attributes attrs, Map hints)
                throws SAXException {
            if ((value == null) || (value.length != 1)) {
                throw new SAXException("must be one geometry");
            }

            Object value2 = value[0].getValue();
            return (MultiLineString) value2;

            //            if( value2 instanceof MultiLineString)
            //            	return (MultiLineString) value2;
            //            else if( value2 instanceof LineString ){
            //            	GeometryFactory fac=new GeometryFactory();
            //            	return fac.createMultiLineString(new LineString[]{(LineString) value2});
            //            }
            //            throw new SAXException("Expected value was a MultiLineString, instead it
            // was a: "+value2.getClass());
        }

        /** @see org.geotools.xml.xsi.Type#getInstanceType() */
        public Class getInstanceType() {
            return MultiLineString.class;
        }

        /** @see schema.ComplexType#findChildElement(java.lang.String) */
        public Element findChildElement(String name) {
            if (name == null) {
                return null;
            }

            for (int i = 0; i < elements.length; i++)
                if (name.equals(elements[i].getName())) {
                    return elements[i];
                }

            return null;
        }

        /**
         * @see org.geotools.xml.schema.Type#canEncode(org.geotools.xml.schema.Element,
         *     java.lang.Object, java.util.Map)
         */
        public boolean canEncode(Element element, Object value, Map hints) {
            if ((value == null) || !(value instanceof MultiLineString)) {
                return false;
            }

            ComplexType t =
                    (element.getType() instanceof ComplexType)
                            ? (ComplexType) element.getType()
                            : null;

            while ((t != null) && (t != this))
                t = (t.getParent() instanceof ComplexType) ? (ComplexType) t.getParent() : null;

            return t != null;
        }

        /**
         * @see org.geotools.xml.schema.Type#encode(org.geotools.xml.schema.Element,
         *     java.lang.Object, org.geotools.xml.PrintHandler, java.util.Map)
         */
        public void encode(Element element, Object value, PrintHandler output, Map hints)
                throws IOException, OperationNotSupportedException {
            if ((value == null) || !(value instanceof MultiLineString)) {
                throw new OperationNotSupportedException(
                        "Value is " + value == null ? "null" : value.getClass().getName());
            }

            if (element == null) {
                output.startElement(GMLSchema.NAMESPACE, "multiLineStringProperty", null);
                GMLComplexTypes.encode(null, (MultiLineString) value, output);
                output.endElement(GMLSchema.NAMESPACE, "multiLineStringProperty");
            } else {
                output.startElement(element.getNamespace(), element.getName(), null);
                GMLComplexTypes.encode(null, (MultiLineString) value, output);
                output.endElement(element.getNamespace(), element.getName());
            }
        }
    }

    /**
     * This class represents an MultiPolygonPropertyType within the GML Schema. This includes both
     * the data and parsing functionality associated with an MultiPolygonPropertyType.
     *
     * @author dzwiers
     * @see GMLComplexType
     * @see ComplexType
     */
    public static class MultiPolygonPropertyType extends GMLComplexType {

        /** @see org.geotools.xml.schema.ComplexType#getChildElements() */
        public Element[] getChildElements() {
            return elements;
        }

        // singleton instance
        private static final GMLComplexType instance = new MultiPolygonPropertyType();

        // static element list
        private static final Element[] elements = {
            new GMLElement(
                    "MultiPolygon",
                    GMLComplexTypes.MultiPolygonType.getInstance(),
                    0,
                    1,
                    false,
                    new GMLElement(
                            "_Geometry",
                            GMLComplexTypes.AbstractGeometryType.getInstance(),
                            1,
                            1,
                            true,
                            null)),
        };

        // static sequence
        private static final DefaultSequence seq = new DefaultSequence(elements);

        /*
         * part of the singleton pattern
         *
         * @see GMLComplexType#getInstance()
         */
        public static GMLComplexType getInstance() {
            return instance;
        }

        /** @see schema.ComplexType#isAbstract() */
        public boolean isAbstract() {
            return false;
        }

        /** @see schema.ComplexType#getAnyAttributeNameSpace() */
        public String getAnyAttributeNameSpace() {
            return null;
        }

        /** @see schema.ComplexType#getAttributeDescriptors() */
        public Attribute[] getAttributes() {
            return FeatureAssociationType.attributes;
        }

        /** @see schema.ComplexType#getChildren() */
        public ElementGrouping getChild() {
            return seq;
        }

        /** @see schema.ComplexType#getLocalName() */
        public String getName() {
            return "MultiPolygonPropertyType";
        }

        /** @see schema.Type#getValue(java.util.List) */
        public Object getValue(Element element, ElementValue[] value, Attributes attrs, Map hints)
                throws SAXException {
            if ((value == null) || (value.length != 1)) {
                throw new SAXException("must be one geometry");
            }

            Object value2 = value[0].getValue();

            return (MultiPolygon) value2;
            //            if( value2 instanceof MultiPolygon)
            //            	return (MultiPolygon) value2;
            //            else if( value2 instanceof Polygon ){
            //            	GeometryFactory fac=new GeometryFactory();
            //            	return fac.createMultiPolygon(new Polygon[]{(Polygon) value2});
            //            }
            //            throw new SAXException("Expected value was a MultiPolygon, instead it was
            // a: "+value2.getClass());

        }

        /** @see org.geotools.xml.xsi.Type#getInstanceType() */
        public Class getInstanceType() {
            return MultiPolygon.class;
        }

        /** @see schema.ComplexType#findChildElement(java.lang.String) */
        public Element findChildElement(String name) {
            if (name == null) {
                return null;
            }

            for (int i = 0; i < elements.length; i++)
                if (name.equals(elements[i].getName())) {
                    return elements[i];
                }

            return null;
        }

        /**
         * @see org.geotools.xml.schema.Type#canEncode(org.geotools.xml.schema.Element,
         *     java.lang.Object, java.util.Map)
         */
        public boolean canEncode(Element element, Object value, Map hints) {
            if ((value == null) || !(value instanceof MultiPolygon)) {
                return false;
            }

            ComplexType t =
                    (element.getType() instanceof ComplexType)
                            ? (ComplexType) element.getType()
                            : null;

            while ((t != null) && (t != this))
                t = (t.getParent() instanceof ComplexType) ? (ComplexType) t.getParent() : null;

            return t != null;
        }

        /**
         * @see org.geotools.xml.schema.Type#encode(org.geotools.xml.schema.Element,
         *     java.lang.Object, org.geotools.xml.PrintHandler, java.util.Map)
         */
        public void encode(Element element, Object value, PrintHandler output, Map hints)
                throws IOException, OperationNotSupportedException {
            if ((value == null) || !(value instanceof MultiPolygon)) {
                throw new OperationNotSupportedException(
                        "Value is " + (value == null ? "null" : value.getClass().getName()));
            }

            if (element == null) {
                output.startElement(GMLSchema.NAMESPACE, "multiPolygonProperty", null);
                GMLComplexTypes.encode(null, (MultiPolygon) value, output);
                output.endElement(GMLSchema.NAMESPACE, "multiPolygonProperty");
            } else {
                output.startElement(element.getNamespace(), element.getName(), null);
                GMLComplexTypes.encode(null, (MultiPolygon) value, output);
                output.endElement(element.getNamespace(), element.getName());
            }
        }
    }

    /**
     * This class represents an MultiGeometryPropertyType within the GML Schema. This includes both
     * the data and parsing functionality associated with an MultiGeometryPropertyType.
     *
     * @author dzwiers
     * @see GMLComplexType
     * @see ComplexType
     */
    public static class MultiGeometryPropertyType extends GMLComplexType {

        /** @see org.geotools.xml.schema.ComplexType#getChildElements() */
        public Element[] getChildElements() {
            return elements;
        }

        // singleton instance
        private static final GMLComplexType instance = new MultiGeometryPropertyType();

        // static element list
        private static final Element[] elements = {
            new GMLElement(
                    "MultiGeometry",
                    GMLComplexTypes.GeometryCollectionType.getInstance(),
                    0,
                    1,
                    false,
                    new GMLElement(
                            "_Geometry",
                            GMLComplexTypes.AbstractGeometryType.getInstance(),
                            1,
                            1,
                            true,
                            null)),
        };

        // static sequence
        private static final DefaultSequence seq = new DefaultSequence(elements);

        /*
         * part of the singleton pattern
         *
         * @see GMLComplexType#getInstance()
         */
        public static GMLComplexType getInstance() {
            return instance;
        }

        /** @see schema.ComplexType#isAbstract() */
        public boolean isAbstract() {
            return false;
        }

        /** @see schema.ComplexType#getAnyAttributeNameSpace() */
        public String getAnyAttributeNameSpace() {
            return null;
        }

        /** @see schema.ComplexType#getAttributeDescriptors() */
        public Attribute[] getAttributes() {
            return FeatureAssociationType.attributes;
        }

        /** @see schema.ComplexType#getChildren() */
        public ElementGrouping getChild() {
            return seq;
        }

        /** @see schema.ComplexType#getLocalName() */
        public String getName() {
            return "MultiGeometryPropertyType";
        }

        /** @see schema.Type#getValue(java.util.List) */
        public Object getValue(Element element, ElementValue[] value, Attributes attrs, Map hints)
                throws SAXException {
            if ((value == null) || (value.length != 1)) {
                throw new SAXException("must be one geometry");
            }

            Object value2 = value[0].getValue();
            return (GeometryCollection) value2;

            //            if( value2 instanceof GeometryCollection)
            //            	return (GeometryCollection) value2;
            //            else if( value2 instanceof Geometry ){
            //            	GeometryFactory fac=new GeometryFactory();
            //            	return fac.createGeometryCollection(new Geometry[]{(Geometry) value2});
            //            }
            //            throw new SAXException("Expected value was a Geometry, instead it was a:
            // "+value2.getClass());
        }

        /** @see org.geotools.xml.xsi.Type#getInstanceType() */
        public Class getInstanceType() {
            return GeometryCollection.class;
        }

        /** @see schema.ComplexType#findChildElement(java.lang.String) */
        public Element findChildElement(String name) {
            if (name == null) {
                return null;
            }

            for (int i = 0; i < elements.length; i++)
                if (name.equals(elements[i].getName())) {
                    return elements[i];
                }

            return null;
        }

        /**
         * @see org.geotools.xml.schema.Type#canEncode(org.geotools.xml.schema.Element,
         *     java.lang.Object, java.util.Map)
         */
        public boolean canEncode(Element element, Object value, Map hints) {
            if ((value == null) || !(value instanceof GeometryCollection)) {
                return false;
            }

            ComplexType t =
                    (element.getType() instanceof ComplexType)
                            ? (ComplexType) element.getType()
                            : null;

            while ((t != null) && (t != this))
                t = (t.getParent() instanceof ComplexType) ? (ComplexType) t.getParent() : null;

            return t != null;
        }

        /**
         * @see org.geotools.xml.schema.Type#encode(org.geotools.xml.schema.Element,
         *     java.lang.Object, org.geotools.xml.PrintHandler, java.util.Map)
         */
        public void encode(Element element, Object value, PrintHandler output, Map hints)
                throws IOException, OperationNotSupportedException {
            if ((value == null) || !(value instanceof GeometryCollection)) {
                throw new OperationNotSupportedException(
                        "Value is " + value == null ? "null" : value.getClass().getName());
            }

            if (element == null) {
                output.startElement(GMLSchema.NAMESPACE, "multiGeometryProperty", null);
                GMLComplexTypes.encode(null, (GeometryCollection) value, output);
                output.endElement(GMLSchema.NAMESPACE, "multiGeometryProperty");
            } else {
                output.startElement(element.getNamespace(), element.getName(), null);
                GMLComplexTypes.encode(null, (GeometryCollection) value, output);
                output.endElement(element.getNamespace(), element.getName());
            }
        }
    }

    /*
     * Creates a FT from the information provided
     */
    public static SimpleFeatureType createFeatureType(Element element) throws SAXException {
        String ftName = element.getName();
        URI ftNS = element.getType().getNamespace();
        logger.finest("Creating feature type for " + ftName + ":" + ftNS);

        SimpleFeatureTypeBuilder build = new SimpleFeatureTypeBuilder();
        build.setName(ftName);
        build.setNamespaceURI(ftNS);

        GeometryDescriptor geometryAttribute = null;

        ElementGrouping child = ((ComplexType) element.getType()).getChild();
        //        FeatureType parent = null;
        //        if(((ComplexType)element.getType()).getParent()instanceof ComplexType)
        //            parent =
        // createFeatureType((ComplexType)((ComplexType)element.getType()).getParent());

        //        if(parent != null && parent.getAttributeDescriptors()!=null){
        //            typeFactory.addTypes(parent.getAttributeDescriptors());
        //            if(parent.getDefaultGeometry()!=null){
        //                geometryAttribute = parent.getDefaultGeometry();
        //            }
        //        }

        AttributeDescriptor[] attrs =
                (AttributeDescriptor[])
                        getAttributes(element.getName(), child)
                                .toArray(new AttributeDescriptor[] {,});
        for (int i = 0; i < attrs.length; i++) {
            if (attrs[i] != null) {
                build.add(attrs[i]);

                if ((geometryAttribute == null) && attrs[i] instanceof GeometryDescriptor) {
                    if (!attrs[i].getLocalName()
                            //
                            // .equalsIgnoreCase(BoxType.getInstance().getName())) {
                            .equalsIgnoreCase(
                                    AbstractFeatureType.getInstance()
                                            .getChildElements()[2]
                                            .getName())) {
                        geometryAttribute = (GeometryDescriptor) attrs[i];
                    }
                }
            }
        }

        if (geometryAttribute != null) {
            build.setDefaultGeometry(geometryAttribute.getLocalName());
        }

        try {
            SimpleFeatureType ft = build.buildFeatureType();
            return ft;
        } catch (IllegalArgumentException e) {
            logger.warning(e.toString());
            throw new SAXException(e);
        }
    }

    public static SimpleFeatureType createFeatureType(ComplexType element) throws SAXException {
        String ftName = element.getName();
        URI ftNS = element.getNamespace();
        logger.finest("Creating feature type for " + ftName + ":" + ftNS);

        SimpleFeatureTypeBuilder build = new SimpleFeatureTypeBuilder();
        build.setNamespaceURI(ftNS);
        build.setName(ftName);

        GeometryDescriptor geometryAttribute = null;

        ElementGrouping child = (element).getChild();

        List<AttributeDescriptor> attrs = getAttributes(element.getName(), child);
        for (AttributeDescriptor attributeDescriptor : attrs) {
            if (attributeDescriptor == null) continue;

            build.add(attributeDescriptor);

            if ((geometryAttribute == null) && attributeDescriptor instanceof GeometryDescriptor) {
                if (!attributeDescriptor
                        .getLocalName()
                        .equalsIgnoreCase(
                                AbstractFeatureType.getInstance()
                                        .getChildElements()[2]
                                        .getName())) {
                    geometryAttribute = (GeometryDescriptor) attributeDescriptor;
                }
            }
        }

        if (geometryAttribute != null) {
            build.setDefaultGeometry(geometryAttribute.getLocalName());
        }

        try {
            SimpleFeatureType ft = build.buildFeatureType();
            return ft;
        } catch (IllegalArgumentException e) {
            logger.warning(e.toString());
            throw new SAXException(e);
        }
    }

    private static List<AttributeDescriptor> getAttributes(String name, ElementGrouping eg) {
        List<AttributeDescriptor> attributes = new LinkedList<AttributeDescriptor>();
        AttributeDescriptor t = null;
        switch (eg.getGrouping()) {
            case ElementGrouping.CHOICE:
                t = getAttribute(name, (Choice) eg);
                if (t != null) attributes.add(t);
                break;
            case ElementGrouping.GROUP:
                attributes.addAll(getAttributes(name, ((Group) eg).getChild()));
                break;
            case ElementGrouping.ELEMENT:
                t = getAttribute((Element) eg);
                if (t != null) attributes.add(t);
                return attributes;

            case ElementGrouping.ALL:
                Element[] elems = ((All) eg).getElements();
                if (elems != null)
                    for (int i = 0; i < elems.length; i++) attributes.add(getAttribute(elems[i]));
                break;
            case ElementGrouping.SEQUENCE:
                ElementGrouping[] children = ((Sequence) eg).getChildren();
                if (children != null)
                    for (int i = 0; i < children.length; i++)
                        attributes.addAll(getAttributes(name, children[i]));
                break;
        }
        return attributes;
    }

    private static AttributeDescriptor getAttribute(Element eg) {
        if (eg.getNamespace() == GMLSchema.NAMESPACE
                && (AbstractFeatureType.getInstance().getChildElements()[0] == eg
                        || AbstractFeatureType.getInstance().getChildElements()[1] == eg
                        || AbstractFeatureType.getInstance().getChildElements()[2] == eg))
            return null;

        Class<?> type = Object.class;
        if (eg.getType() != null) {
            if (eg.getType() instanceof SimpleType) {
                type = eg.getType().getInstanceType();
            } else {
                if (Object.class.equals(eg.getType().getInstanceType())
                        || Object[].class.equals(eg.getType().getInstanceType())) {
                    // some work now
                    ElementGrouping child = ((ComplexType) eg.getType()).getChild();
                    if (child != null) {
                        List l = getAttributes(eg.getName(), child);
                        if (l.isEmpty()) {
                            // who knows ... this really shouldn't happen
                            type = eg.getType().getInstanceType();
                        } else {
                            if (l.size() == 1) {
                                return (AttributeDescriptor) l.iterator().next();
                            }
                            // Do some magic to find the type
                            type = getCommonType(l);
                        }
                    } else {
                        // who knows ... this really shouldn't happen
                        type = eg.getType().getInstanceType();
                    }
                } else {
                    // we have a real type
                    type = eg.getType().getInstanceType();
                }
            }
        }
        if (type == null) type = Object.class;

        // nillable should really be nillable, but in gt2.X nillable
        // in an attribute is equivalent to minOccurs == 0 as well
        boolean nillable = eg.isNillable() || eg.getMinOccurs() == 0;
        if (!nillable) {
            try {
                Object defaultValue = DataUtilities.defaultValue(type);
                AttributeTypeBuilder build = new AttributeTypeBuilder();
                build.setName(eg.getName());
                build.setBinding(type);
                build.setNillable(nillable);
                build.setDefaultValue(defaultValue);

                return build.buildDescriptor(eg.getName());

                //                return AttributeTypeFactory.newAttributeDescriptor(
                //                        eg.getName(),
                //                        type,
                //                        nillable,
                //                        Filter.INCLUDE,
                //                        defaultValue,
                //                        null);
            } catch (IllegalArgumentException e) {
                // can happen if the type is not supported by the method.
                // in this case I'm taking the easy way out and just not
                // having a default value.
                logger.warning(
                        "Don't know how to make a default value for: "
                                + type
                                + ". Consider making it nillable.");

                AttributeTypeBuilder build = new AttributeTypeBuilder();
                build.setName(eg.getName());
                build.setBinding(type);
                build.setNillable(nillable);
                return build.buildDescriptor(eg.getName());
                //                return AttributeTypeFactory.newAttributeType(
                //                        eg.getName(),
                //                        type,
                //                        nillable);
            }
        }
        AttributeTypeBuilder build = new AttributeTypeBuilder();
        build.setName(eg.getName());
        build.setBinding(type);
        build.setNillable(nillable);
        return build.buildDescriptor(eg.getName());
        // return AttributeTypeFactory.newAttributeType(eg.getName(),type,(nillable));
    }

    private static AttributeDescriptor getAttribute(String name, Choice eg) {

        List l = new LinkedList();
        ElementGrouping[] children = eg.getChildren();
        if (children != null)
            for (int i = 0; i < children.length; i++) {
                l.addAll(getAttributes(name, children[i]));
            }

        if (l.isEmpty()) {
            // who knows ... this really shouldn't happen
            return null;
        }
        if (l.size() == 1) {
            return (AttributeDescriptor) l.iterator().next();
        }
        // Do some magic to find the type
        Class<?> type = getCommonType(l);
        if (type == null) type = Object.class;
        // Take the first name ... cause we need one anyways
        // nillable should really be nillable, but in gt2.X nillable in an attribute is equivalent
        // to minOccurs == 0 as well
        boolean nillable = eg.getMinOccurs() == 0;
        if (!nillable && children != null) {
            for (int i = 0; i < children.length && !nillable; i++) {
                if (eg.getMinOccurs() == 0) nillable = true;
            }
        }
        Class[] choices = collectionChoices(l);
        NameImpl typeName = new NameImpl(name);
        if (Geometry.class.isAssignableFrom(type)) {
            return new ChoiceGeometryTypeImpl(
                    typeName, choices, type, nillable, 1, 1, null, null, Collections.EMPTY_LIST);
        } else {
            return new ChoiceAttributeTypeImpl(
                    typeName, choices, type, nillable, 1, 1, null, Collections.EMPTY_LIST);
        }
    }

    private static Class[] collectionChoices(List l) {
        Class[] choices = new Class[l.size()];
        int i = 0;
        for (Iterator iter = l.iterator(); iter.hasNext(); i++) {
            AttributeDescriptor type = (AttributeDescriptor) iter.next();
            choices[i] = type.getType().getBinding();
        }
        return choices;
    }

    // takes List<AttributeDescriptor>
    private static Class getCommonType(List AttributeDescriptors) {
        if (AttributeDescriptors == null || AttributeDescriptors.isEmpty()) return null;
        if (AttributeDescriptors.size() == 1)
            return AttributeDescriptors.iterator().next().getClass();

        Class common = null;
        AttributeDescriptor at = null;
        Iterator i = AttributeDescriptors.iterator();
        while (i.hasNext()) {
            at = (AttributeDescriptor) i.next();
            if (at != null) {
                if (common == null) {
                    common = at.getType().getBinding();
                } else {
                    // merge two types
                    Class t = at.getType().getBinding();
                    if (t != null) {
                        if (!common.isAssignableFrom(t)) {
                            // either t is super class .. or they share one
                            if (t.isAssignableFrom(common)) {
                                common = t;
                            } else {
                                int selection = findCompatible(common, t);
                                switch (selection) {
                                    case 0:
                                        common = findCommon(common, t);
                                        break;
                                    case 1:
                                        // common=common;
                                        break;
                                    case 2:
                                        common = t;
                                        break;

                                    default:
                                        break;
                                }
                            }
                        }
                    }
                }
            }
        }
        return common;
    }

    /**
     * This is a bit of a hack to allow certain special cases. This is required because the feature
     * model does not support choices and making a Choice (Polygon, MultiPolygon) a Geometry is
     * terrible because editing will allow all types to be added but really that is incorrect.
     *
     * <p>For example: Polygon and MultiPolygon are compatible. Polygons can be made MultiPolygons
     * easily.
     *
     * @return 0 if no compatibility is possible, 1 if c1 is a good choice, 2 if c2 is a good
     *     choice.
     */
    private static int findCompatible(Class c1, Class c2) {
        if (c1 == Polygon.class && c2 == MultiPolygon.class) {
            return 2;
        }
        if (c2 == Polygon.class && c1 == MultiPolygon.class) {
            return 1;
        }
        if (c1 == Point.class && c2 == MultiPoint.class) {
            return 2;
        }
        if (c2 == Point.class && c1 == MultiPoint.class) {
            return 1;
        }
        if (c1 == LineString.class && c2 == MultiLineString.class) {
            return 2;
        }
        if (c2 == LineString.class && c1 == MultiLineString.class) {
            return 1;
        }
        if (c1 == Geometry.class && c2 == GeometryCollection.class) {
            return 2;
        }
        if (c2 == Geometry.class && c1 == GeometryCollection.class) {
            return 1;
        }

        return 0;
    }

    private static Class findCommon(Class c1, Class c2) {
        if (Object.class == c1) return c2;
        if (Object.class == c2) return c1;

        Class p1 = c1.getSuperclass();
        if (p1.isAssignableFrom(c2)) return p1;
        Class p2 = c2.getSuperclass();
        if (p2.isAssignableFrom(c1)) return p2;

        Class t = findCommon(p1, p2);
        if (!(t == Object.class)) return t;

        // interfaces?
        Class[] it1 = c1.getInterfaces();
        Class[] it2 = c2.getInterfaces();

        if (it1 != null && it1.length > 0 && it2 != null && it2.length > 0) {
            for (int i = 0; i < it1.length; i++) {
                for (int j = 0; j < it2.length; j++) {
                    if (it1[i].isAssignableFrom(it2[j])) return it1[i];
                    if (it2[j].isAssignableFrom(it1[i])) return it2[j];
                }
            }
        }

        return Object.class;
    }
}
