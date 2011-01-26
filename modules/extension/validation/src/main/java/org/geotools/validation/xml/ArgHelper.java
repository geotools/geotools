/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2004-2008, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.validation.xml;

import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.net.URI;
import java.net.URISyntaxException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;

import org.geotools.filter.ExpressionDOMParser;
import org.geotools.filter.FilterDOMParser;
import org.geotools.filter.FilterTransformer;
import org.geotools.gml.producer.GeometryTransformer;
import org.opengis.filter.Filter;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;

import com.vividsolutions.jts.geom.Envelope;
import com.vividsolutions.jts.geom.Geometry;


/**
 * ArgHelper purpose.
 *
 * @author dzwiers, Refractions Research, Inc.
 * @author $Author: dmzwiers $ (last modification)
 * @source $URL$
 * @version $Id$
 */
public class ArgHelper {
    private static final Mapping[] argumentTypeMappings = {
        new FilterMapping(), new GeometryMapping(), new EnvelopeMapping(),
        new ShortMapping(), new IntegerMapping(), new LongMapping(),
        new FloatMapping(), new DoubleMapping(), new DateMapping(),
        new URIMapping(), new BooleanMapping(), new StringMapping()
    };

    /**
     * getArgumentInstance purpose.
     * 
     * <p>
     * Returns an instance for the specified argument type from the Element
     * provided.
     * </p>
     *
     * @param elementName String the argument element name (type name).
     * @param value Element the element to create the Argument from.
     *
     * @return The Specified argument in Object form.
     *
     * @throws ValidationException DOCUMENT ME!
     * @throws NullPointerException DOCUMENT ME!
     */
    public static Object getArgumentInstance(String elementName, Element value)
        throws ValidationException {
        if (elementName == null) {
            throw new NullPointerException("A Typename must be specified.");
        }

        for (int i = 0; i < argumentTypeMappings.length; i++) {
            if (elementName.equals(argumentTypeMappings[i].getElementName())) {
                return argumentTypeMappings[i].getInstance(value);
            }
        }

        return null;
    }

    /**
     * getArgumentInstance purpose.
     * 
     * <p>
     * Returns an instance for the specified argument type from the Element
     * provided.
     * </p>
     *
     * @param elementName String the argument element name (type name).
     * @param value String the element to create the Argument from.
     *
     * @return The Specified argument in Object form.
     *
     * @throws ValidationException DOCUMENT ME!
     * @throws NullPointerException DOCUMENT ME!
     */
    public static Object getArgumentInstance(String elementName, String value)
        throws ValidationException {
        if (elementName == null) {
            throw new NullPointerException("A Typename must be specified.");
        }

        for (int i = 0; i < argumentTypeMappings.length; i++) {
            if (elementName.equals(argumentTypeMappings[i].getType())) {
                return argumentTypeMappings[i].getInstance(value);
            }
        }

        return null;
    }

    /**
     * getArgumentType purpose.
     * 
     * <p>
     * Finds the appropriate argument type if one exists.
     * </p>
     *
     * @param o The Object to search for it's type.
     *
     * @return The Object type or "" if not found.
     *
     * @throws NullPointerException DOCUMENT ME!
     */
    public static String getArgumentType(Object o) {
        if (o == null) {
            throw new NullPointerException(
                "An argument instance must be specified.");
        }

        if (o instanceof Class) {
            for (int i = 0; i < argumentTypeMappings.length; i++)
                if (argumentTypeMappings[i].isClass((Class) o)) {
                    return argumentTypeMappings[i].getType();
                }
        } else {
            for (int i = 0; i < argumentTypeMappings.length; i++)
                if (argumentTypeMappings[i].isClassInstance(o)) {
                    return argumentTypeMappings[i].getType();
                }
        }

        return "";
    }

    /**
     * getArgumentEncoding purpose.
     * 
     * <p>
     * Creates an XML encodeing of the Object if it is a known argument type.
     * </p>
     *
     * @param o Object the object to attempt to encode.
     *
     * @return an XML string if it is a known type, "" otherwise.
     *
     * @throws ValidationException DOCUMENT ME!
     * @throws NullPointerException DOCUMENT ME!
     */
    public static String getArgumentEncoding(Object o)
        throws ValidationException {
        if (o == null) {
            throw new NullPointerException(
                "An argument instance must be specified.");
        }

        for (int i = 0; i < argumentTypeMappings.length; i++)
            if (argumentTypeMappings[i].isClassInstance(o)) {
                return argumentTypeMappings[i].encode(o);
            }

        return "";
    }

    public static String getArgumentStringEncoding(Object o) {
        if (o == null) {
            throw new NullPointerException(
                "An argument instance must be specified.");
        }

        for (int i = 0; i < argumentTypeMappings.length; i++)
            if (argumentTypeMappings[i].isClassInstance(o)) {
                return argumentTypeMappings[i].toString(o);
            }

        return "";
    }

    /**
     * Mapping purpose.
     * 
     * <p>
     * Used to mask attribute specific fucntions from the user.
     * </p>
     *
     * @author dzwiers, Refractions Research, Inc.
     * @author $Author: dmzwiers $ (last modification)
     * @version $Id$
     */
    protected interface Mapping {
        /**
         * getType purpose.
         * 
         * <p>
         * Returns a constant type name.
         * </p>
         *
         * @return String a constant type name.
         */
        public abstract String getType();

        /**
         * getInstance purpose.
         * 
         * <p>
         * Creates an instance of the appropriate type for this Mapping.  This
         * is where type-dependant magic occurs
         * </p>
         *
         * @param value The Element to interpret.
         *
         * @return The particular argument type expected.
         */
        public abstract Object getInstance(Element value)
            throws ValidationException;

        /**
         * getInstance purpose.
         * 
         * <p>
         * Creates an instance of the appropriate type for this Mapping.  This
         * is where type-dependant magic occurs
         * </p>
         *
         * @param value The Element to interpret.
         *
         * @return The particular argument type expected.
         */
        public abstract Object getInstance(String value)
            throws ValidationException;

        /**
         * isClassInstance purpose.
         * 
         * <p>
         * Tests to see if this class is of the expected type.
         * </p>
         *
         * @param obj The object to test.
         *
         * @return true when they are compatible
         */
        public abstract boolean isClassInstance(Object obj);

        public abstract boolean isClass(Class c);

        /**
         * encode purpose.
         * 
         * <p>
         * Creates an XML String from the obj provided, if the  object is of
         * the expected type for this mapping.
         * </p>
         *
         * @param obj The object to try and encode.
         *
         * @return An XML String if the type is correct, ClassCastException
         *         otherwise.
         */
        public abstract String encode(Object obj) throws ValidationException;

        public abstract String toString(Object obj);

        /**
         * getElementName purpose.
         * 
         * <p>
         * This is the name of the element represented.
         * </p>
         *
         */
        public abstract String getElementName();
    }

    /**
     * FilterMapping purpose.
     * 
     * <p>
     * Represents the specifics for the Filter Argument type
     * </p>
     *
     * @author dzwiers, Refractions Research, Inc.
     * @author $Author: dmzwiers $ (last modification)
     * @version $Id$
     *
     * @see Mapping
     */
    protected static class FilterMapping implements Mapping {
        /**
         * Implementation of getType.
         *
         * @return the type name
         *
         * @see org.geotools.validation.xml.ArgHelper.Mapping#getType()
         */
        public String getType() {
            return "ogc:FilterType";
        }

        /**
         * Implementation of getElementName.
         *
         * @return the element name
         *
         * @see org.geotools.validation.xml.ArgHelper.Mapping#getElementName()
         */
        public String getElementName() {
            return "filter";
        }

        /**
         * Implementation of getInstance.
         *
         * @param value Element the element to parse into an instance of type
         *        Filter
         *
         * @return Filter the filter instance if posible, null otherwise.
         *
         * @see org.geotools.validation.xml.ArgHelper.Mapping#getInstance(org.w3c.dom.Element)
         */
        public Object getInstance(Element value) {
            // value must be the node for "ogc:Filter"
            if ((value != null)
                    && ((value = ReaderUtils.getFirstChildElement(value)) != null)) {
                return FilterDOMParser.parseFilter(value);
            }

            return null;
        }

        public Object getInstance(String elem) {
            Element value;

            try {
                StringReader sr = new StringReader(elem);
                value = ReaderUtils.loadConfig(sr);
                sr.close();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();

                return null;
            } catch (ParserConfigurationException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();

                return null;
            } catch (SAXException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();

                return null;
            }

            // value must be the node for "ogc:Filter"
            if ((value != null)
                    && ((value = ReaderUtils.getFirstChildElement(value)) != null)) {
                return FilterDOMParser.parseFilter(value);
            }

            return null;
        }

        /**
         * Implementation of isClassInstance.
         *
         * @param c The Object to test
         *
         * @return true when both of type Filter
         *
         * @see org.geotools.validation.xml.ArgHelper.Mapping#isClassInstance(java.lang.Object)
         */
        public boolean isClassInstance(Object c) {
            return ((c != null) && c instanceof Filter);
        }

        public boolean isClass(Class c) {
            return ((c != null) && Filter.class.equals(c));
        }

        /**
         * Implementation of encode.
         *
         * @param obj An object to encode as a filter.
         *
         * @return String the XML encoding
         *
         * @throws ValidationException DOCUMENT ME!
         * @throws NullPointerException DOCUMENT ME!
         * @throws ClassCastException when obj is not of type Filter
         *
         * @see org.geotools.validation.xml.ArgHelper.Mapping#encode(java.lang.Object)
         */
        public String encode(Object obj) throws ValidationException {
            Filter f = null;

            if (obj == null) {
                throw new NullPointerException("Cannot encode a null Filter.");
            }

            if (!(obj instanceof Filter)) {
                throw new ClassCastException("Cannot cast "
                    + obj.getClass().toString() + " to a Filter.");
            }

            f = (Filter) obj;

            StringWriter sw = new StringWriter();
            
            try {
                sw.write((new FilterTransformer()).transform(f));
            } catch (TransformerException e) {
                throw new ValidationException(e);
            }

            return "<filter>\n" + sw.toString() + "</filter>\n";
        }

        public String toString(Object obj) {
            Filter f = null;

            if (obj == null) {
                throw new NullPointerException("Cannot encode a null Filter.");
            }

            if (!(obj instanceof Filter)) {
                throw new ClassCastException("Cannot cast "
                    + obj.getClass().toString() + " to a Filter.");
            }

            f = (Filter) obj;

            StringWriter sw = new StringWriter();

            try {
                sw.write((new FilterTransformer()).transform(f));
            } catch (TransformerException e) {
                return null;
            }

            return "<filter>\n" + sw.toString() + "</filter>\n";
        }
    }

    /**
     * GeometryMapping purpose.
     * 
     * <p>
     * Represents the workings for a Geometry Mapping
     * </p>
     *
     * @author dzwiers, Refractions Research, Inc.
     * @author $Author: dmzwiers $ (last modification)
     * @version $Id$
     */
    protected static class GeometryMapping implements Mapping {
        /**
         * Implementation of getType.
         *
         * @return the type name
         *
         * @see org.geotools.validation.xml.ArgHelper.Mapping#getType()
         */
        public String getType() {
            return "gml:AbstractGeometryType";
        }

        /**
         * Implementation of getElementName.
         *
         * @return the element name
         *
         * @see org.geotools.validation.xml.ArgHelper.Mapping#getElementName()
         */
        public String getElementName() {
            return "geometry";
        }

        /**
         * Implementation of getInstance.
         *
         * @param value Element the element to parse into a Geometry.
         *
         * @return Geometry an instance of Geometry if one can be created, null
         *         otherwise.
         *
         * @see org.geotools.validation.xml.ArgHelper.Mapping#getInstance(org.w3c.dom.Element)
         */
        public Object getInstance(Element value) {
            return ExpressionDOMParser.parseGML(value);
        }

        public Object getInstance(String value) {
            Element elem;

            try {
                StringReader sr = new StringReader(value);
                elem = ReaderUtils.loadConfig(sr);
                sr.close();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();

                return null;
            } catch (ParserConfigurationException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();

                return null;
            } catch (SAXException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();

                return null;
            }

            return ExpressionDOMParser.parseGML(elem);
        }

        /**
         * Implementation of isClassInstance.
         *
         * @param c The Object to test
         *
         * @return true when both of type Geometry
         *
         * @see org.geotools.validation.xml.ArgHelper.Mapping#isClassInstance(java.lang.Object)
         */
        public boolean isClassInstance(Object c) {
            return ((c != null) && c instanceof Geometry);
        }

        public boolean isClass(Class c) {
            return ((c != null) && Geometry.class.equals(c));
        }

        /**
         * Implementation of encode.
         *
         * @param obj An object to encode as a geometry.
         *
         * @return String the XML encoding
         *
         * @throws ValidationException when obj is not of type geometry
         *
         * @see org.geotools.validation.xml.ArgHelper.Mapping#encode(java.lang.Object)
         */
        public String encode(Object obj) throws ValidationException {
            StringWriter sw = new StringWriter();
            GeometryTransformer transformer = new GeometryTransformer();

            try {
                transformer.transform(obj, sw);
            } catch (TransformerException e) {
                throw new ValidationException(e);
            }

            return "<geometry>\n" + sw.toString() + "</geometry>\n";
        }

        public String toString(Object obj) {
            StringWriter sw = new StringWriter();
            GeometryTransformer transformer = new GeometryTransformer();

            try {
                transformer.transform(obj, sw);
            } catch (TransformerException e) {
                return null;
            }

            return "<geometry>\n" + sw.toString() + "</geometry>\n";
        }
    }

    /**
     * EnvelopeMapping purpose.
     * 
     * <p>
     * Represents the workings for a Envelope Mapping
     * </p>
     *
     * @author dzwiers, Refractions Research, Inc.
     * @author $Author: dmzwiers $ (last modification)
     * @version $Id$
     */
    protected static class EnvelopeMapping implements Mapping {
        /**
         * Implementation of getType.
         *
         * @return the type name
         *
         * @see org.geotools.validation.xml.ArgHelper.Mapping#getType()
         */
        public String getType() {
            return "ogc:BBOXType";
        }

        /**
         * Implementation of getElementName.
         *
         * @return the element name
         *
         * @see org.geotools.validation.xml.ArgHelper.Mapping#getElementName()
         */
        public String getElementName() {
            return "bbox";
        }

        /**
         * Implementation of getInstance.
         *
         * @param bboxElem Element the element to parse into a Envelope.
         *
         * @return Geometry an instance of Envelope if one can be created, null
         *         otherwise.
         *
         * @throws ValidationException DOCUMENT ME!
         * @throws NullPointerException DOCUMENT ME!
         *
         * @see org.geotools.validation.xml.ArgHelper.Mapping#getInstance(org.w3c.dom.Element)
         */
        public Object getInstance(Element bboxElem) throws ValidationException {
            if (bboxElem == null) {
                throw new NullPointerException(
                    "The bounding Box element passed in was null");
            }

            try {
                boolean dynamic = ReaderUtils.getBooleanAttribute(bboxElem,
                        "dynamic", false);

                if (!dynamic) {
                    double minx = ReaderUtils.getDoubleAttribute(bboxElem,
                            "minx", true);
                    double miny = ReaderUtils.getDoubleAttribute(bboxElem,
                            "miny", true);
                    double maxx = ReaderUtils.getDoubleAttribute(bboxElem,
                            "maxx", true);
                    double maxy = ReaderUtils.getDoubleAttribute(bboxElem,
                            "maxy", true);

                    return new Envelope(minx, maxx, miny, maxy);
                }
            } catch (SAXException e) {
                throw new ValidationException(e);
            }

            return null;
        }

        public Object getInstance(String bbox) throws ValidationException {
            if (bbox == null) {
                throw new NullPointerException(
                    "The bounding Box element passed in was null");
            }

            try {
                String[] tmp = bbox.split(",");
                double minx = Double.parseDouble(tmp[0].trim());
                double maxx = Double.parseDouble(tmp[1].trim());
                double miny = Double.parseDouble(tmp[2].trim());
                double maxy = Double.parseDouble(tmp[3].trim());

                return new Envelope(minx, maxx, miny, maxy);
            } catch (Exception e) {
                throw new ValidationException(e);
            }
        }

        /**
         * Implementation of isClassInstance.
         *
         * @param c The Object to test
         *
         * @return true when both of type Envelope
         *
         * @see org.geotools.validation.xml.ArgHelper.Mapping#isClassInstance(java.lang.Object)
         */
        public boolean isClassInstance(Object c) {
            return ((c != null) && c instanceof Envelope);
        }

        public boolean isClass(Class c) {
            return ((c != null) && Envelope.class.equals(c));
        }

        /**
         * Implementation of encode.
         *
         * @param obj An object to encode as a Envelope.
         *
         * @return String the XML encoding
         *
         * @throws NullPointerException DOCUMENT ME!
         * @throws ClassCastException when obj is not of type Envelope
         *
         * @see org.geotools.validation.xml.ArgHelper.Mapping#encode(java.lang.Object)
         */
        public String encode(Object obj) {
            if (obj == null) {
                throw new NullPointerException(
                    "The bounding Box obj passed in was null");
            }

            if (!(obj instanceof Envelope)) {
                throw new ClassCastException(
                    "Object of type Envelope was expected.");
            }

            String s = "";
            Envelope e = (Envelope) obj;
            s += "<bbox ";

            if (!e.isNull()) {
                s += "dynamic = \"false\" ";
                s += ("minx = \"" + e.getMinX() + "\" ");
                s += ("miny = \"" + e.getMinY() + "\" ");
                s += ("maxx = \"" + e.getMaxX() + "\" ");
                s += ("maxy = \"" + e.getMaxY() + "\" ");
            } else {
                s += "dynamic = \"true\" ";
            }

            s += " />\n";

            return s;
        }

        public String toString(Object obj) {
            if (obj == null) {
                throw new NullPointerException(
                    "The bounding Box obj passed in was null");
            }

            if (!(obj instanceof Envelope)) {
                throw new ClassCastException(
                    "Object of type Envelope was expected.");
            }

            String s = "";
            Envelope e = (Envelope) obj;

            if (!e.isNull()) {
                s += e.getMinX();
                s += ("," + e.getMaxX());
                s += ("," + e.getMinY());
                s += ("," + e.getMaxY());
            }

            return s;
        }
    }

    /**
     * ShortMapping purpose.
     * 
     * <p>
     * Represents the workings for a Short Mapping
     * </p>
     *
     * @author dzwiers, Refractions Research, Inc.
     * @author $Author: dmzwiers $ (last modification)
     * @version $Id$
     */
    protected static class ShortMapping implements Mapping {
        /**
         * Implementation of getType.
         *
         * @return the type name
         *
         * @see org.geotools.validation.xml.ArgHelper.Mapping#getType()
         */
        public String getType() {
            return "xs:short";
        }

        /**
         * Implementation of getElementName.
         *
         * @return the element name
         *
         * @see org.geotools.validation.xml.ArgHelper.Mapping#getElementName()
         */
        public String getElementName() {
            return "short";
        }

        /**
         * Implementation of getInstance.
         *
         * @param elem Element the element to parse into a Short.
         *
         * @return Geometry an instance of Short if one can be created, null
         *         otherwise.
         *
         * @throws NullPointerException DOCUMENT ME!
         *
         * @see org.geotools.validation.xml.ArgHelper.Mapping#getInstance(org.w3c.dom.Element)
         */
        public Object getInstance(Element elem) {
            if (elem == null) {
                throw new NullPointerException(
                    "The short element passed in was null");
            }

            return new Short(ReaderUtils.getElementText(elem));
        }

        public Object getInstance(String value) {
            if (value == null) {
                throw new NullPointerException(
                    "The short element passed in was null");
            }

            return new Short(value);
        }

        /**
         * Implementation of isClassInstance.
         *
         * @param c The Object to test
         *
         * @return true when both of type Short
         *
         * @see org.geotools.validation.xml.ArgHelper.Mapping#isClassInstance(java.lang.Object)
         */
        public boolean isClassInstance(Object c) {
            return ((c != null) && c instanceof Short);
        }

        public boolean isClass(Class c) {
            return ((c != null)
            && (Short.class.equals(c) || short.class.equals(c)));
        }

        /**
         * Implementation of encode.
         *
         * @param obj An object to encode as a short.
         *
         * @return String the XML encoding
         *
         * @throws NullPointerException DOCUMENT ME!
         * @throws ClassCastException when obj is not of type short
         *
         * @see org.geotools.validation.xml.ArgHelper.Mapping#encode(java.lang.Object)
         */
        public String encode(Object obj) {
            if (obj == null) {
                throw new NullPointerException(
                    "The short obj passed in was null");
            }

            if (!(obj instanceof Short)) {
                throw new ClassCastException(
                    "Object of type Short was expected.");
            }

            return "<short>" + ((Short) obj).toString() + "</short>\n";
        }

        public String toString(Object o) {
            return o.toString();
        }
    }

    /**
     * IntegerMapping purpose.
     * 
     * <p>
     * Represents the workings for a Integer Mapping
     * </p>
     *
     * @author dzwiers, Refractions Research, Inc.
     * @author $Author: dmzwiers $ (last modification)
     * @version $Id$
     */
    protected static class IntegerMapping implements Mapping {
        /**
         * Implementation of getType.
         *
         * @return the type name
         *
         * @see org.geotools.validation.xml.ArgHelper.Mapping#getType()
         */
        public String getType() {
            return "xs:integer";
        }

        /**
         * Implementation of getElementName.
         *
         * @return the element name
         *
         * @see org.geotools.validation.xml.ArgHelper.Mapping#getElementName()
         */
        public String getElementName() {
            return "integer";
        }

        /**
         * Implementation of getInstance.
         *
         * @param elem Element the element to parse into a Integer.
         *
         * @return Geometry an instance of Integer if one can be created, null
         *         otherwise.
         *
         * @throws NullPointerException DOCUMENT ME!
         *
         * @see org.geotools.validation.xml.ArgHelper.Mapping#getInstance(org.w3c.dom.Element)
         */
        public Object getInstance(Element elem) {
            if (elem == null) {
                throw new NullPointerException("The integer passed in was null");
            }

            return new Integer(ReaderUtils.getElementText(elem));
        }

        public Object getInstance(String value) {
            if (value == null) {
                throw new NullPointerException("The integer passed in was null");
            }

            return new Integer(value);
        }

        /**
         * Implementation of isClassInstance.
         *
         * @param c The Object to test
         *
         * @return true when both of type Integer
         *
         * @see org.geotools.validation.xml.ArgHelper.Mapping#isClassInstance(java.lang.Object)
         */
        public boolean isClassInstance(Object c) {
            return ((c != null) && c instanceof Integer);
        }

        public boolean isClass(Class c) {
            return ((c != null)
            && (Integer.class.equals(c) || int.class.equals(c)));
        }

        /**
         * Implementation of encode.
         *
         * @param obj An object to encode as a Integer.
         *
         * @return String the XML encoding
         *
         * @throws NullPointerException DOCUMENT ME!
         * @throws ClassCastException when obj is not of type Integer
         *
         * @see org.geotools.validation.xml.ArgHelper.Mapping#encode(java.lang.Object)
         */
        public String encode(Object obj) {
            if (obj == null) {
                throw new NullPointerException(
                    "The integer obj passed in was null");
            }

            if (!(obj instanceof Integer)) {
                throw new ClassCastException(
                    "Object of type Integer was expected.");
            }

            return "<integer>" + ((Integer) obj).toString() + "</integer>\n";
        }

        public String toString(Object o) {
            return o.toString();
        }
    }

    /**
     * LongMapping purpose.
     * 
     * <p>
     * Represents the workings for a Long Mapping
     * </p>
     *
     * @author dzwiers, Refractions Research, Inc.
     * @author $Author: dmzwiers $ (last modification)
     * @version $Id$
     */
    protected static class LongMapping implements Mapping {
        /**
         * Implementation of getType.
         *
         * @return the type name
         *
         * @see org.geotools.validation.xml.ArgHelper.Mapping#getType()
         */
        public String getType() {
            return "xs:long";
        }

        /**
         * Implementation of getElementName.
         *
         * @return the element name
         *
         * @see org.geotools.validation.xml.ArgHelper.Mapping#getElementName()
         */
        public String getElementName() {
            return "long";
        }

        /**
         * Implementation of getInstance.
         *
         * @param elem Element the element to parse into a Long.
         *
         * @return Geometry an instance of Long if one can be created, null
         *         otherwise.
         *
         * @throws NullPointerException DOCUMENT ME!
         *
         * @see org.geotools.validation.xml.ArgHelper.Mapping#getInstance(org.w3c.dom.Element)
         */
        public Object getInstance(Element elem) {
            if (elem == null) {
                throw new NullPointerException("The long passed in was null");
            }

            return new Long(ReaderUtils.getElementText(elem));
        }

        public Object getInstance(String value) {
            if (value == null) {
                throw new NullPointerException("The long passed in was null");
            }

            return new Long(value);
        }

        /**
         * Implementation of isClassInstance.
         *
         * @param c The Object to test
         *
         * @return true when both of type Long
         *
         * @see org.geotools.validation.xml.ArgHelper.Mapping#isClassInstance(java.lang.Object)
         */
        public boolean isClassInstance(Object c) {
            return ((c != null) && c instanceof Long);
        }

        public boolean isClass(Class c) {
            return ((c != null)
            && (Long.class.equals(c) || long.class.equals(c)));
        }

        /**
         * Implementation of encode.
         *
         * @param obj An object to encode as a Integer.
         *
         * @return String the XML encoding
         *
         * @throws NullPointerException DOCUMENT ME!
         * @throws ClassCastException when obj is not of type Integer
         *
         * @see org.geotools.validation.xml.ArgHelper.Mapping#encode(java.lang.Object)
         */
        public String encode(Object obj) {
            if (obj == null) {
                throw new NullPointerException(
                    "The long obj passed in was null");
            }

            if (!(obj instanceof Long)) {
                throw new ClassCastException(
                    "Object of type Long was expected.");
            }

            return "<long>" + ((Long) obj).toString() + "</long>\n";
        }

        public String toString(Object o) {
            return o.toString();
        }
    }

    /**
     * FloatMapping purpose.
     * 
     * <p>
     * Represents the workings for a Float Mapping
     * </p>
     *
     * @author dzwiers, Refractions Research, Inc.
     * @author $Author: dmzwiers $ (last modification)
     * @version $Id$
     */
    protected static class FloatMapping implements Mapping {
        /**
         * Implementation of getType.
         *
         * @return the type name
         *
         * @see org.geotools.validation.xml.ArgHelper.Mapping#getType()
         */
        public String getType() {
            return "xs:float";
        }

        /**
         * Implementation of getElementName.
         *
         * @return the element name
         *
         * @see org.geotools.validation.xml.ArgHelper.Mapping#getElementName()
         */
        public String getElementName() {
            return "float";
        }

        /**
         * Implementation of getInstance.
         *
         * @param elem Element the element to parse into a Float.
         *
         * @return Geometry an instance of Float if one can be created, null
         *         otherwise.
         *
         * @throws NullPointerException DOCUMENT ME!
         *
         * @see org.geotools.validation.xml.ArgHelper.Mapping#getInstance(org.w3c.dom.Element)
         */
        public Object getInstance(Element elem) {
            if (elem == null) {
                throw new NullPointerException("The float passed in was null");
            }

            return new Float(ReaderUtils.getElementText(elem));
        }

        public Object getInstance(String elem) {
            if (elem == null) {
                throw new NullPointerException("The float passed in was null");
            }

            return new Float(elem);
        }

        /**
         * Implementation of isClassInstance.
         *
         * @param c The Object to test
         *
         * @return true when both of type Long
         *
         * @see org.geotools.validation.xml.ArgHelper.Mapping#isClassInstance(java.lang.Object)
         */
        public boolean isClassInstance(Object c) {
            return ((c != null) && c instanceof Float);
        }

        public boolean isClass(Class c) {
            return ((c != null)
            && (Float.class.equals(c) || float.class.equals(c)));
        }

        /**
         * Implementation of encode.
         *
         * @param obj An object to encode as a Float.
         *
         * @return String the XML encoding
         *
         * @throws NullPointerException DOCUMENT ME!
         * @throws ClassCastException when obj is not of type Float
         *
         * @see org.geotools.validation.xml.ArgHelper.Mapping#encode(java.lang.Object)
         */
        public String encode(Object obj) {
            if (obj == null) {
                throw new NullPointerException(
                    "The float obj passed in was null");
            }

            if (!(obj instanceof Float)) {
                throw new ClassCastException(
                    "Object of type Long was expected.");
            }

            return "<float>" + ((Float) obj).toString() + "</float>\n";
        }

        public String toString(Object o) {
            return o.toString();
        }
    }

    /**
     * DoubleMapping purpose.
     * 
     * <p>
     * Represents the workings for a Double Mapping
     * </p>
     *
     * @author dzwiers, Refractions Research, Inc.
     * @author $Author: dmzwiers $ (last modification)
     * @version $Id$
     */
    protected static class DoubleMapping implements Mapping {
        /**
         * Implementation of getType.
         *
         * @return the type name
         *
         * @see org.geotools.validation.xml.ArgHelper.Mapping#getType()
         */
        public String getType() {
            return "xs:double";
        }

        /**
         * Implementation of getElementName.
         *
         * @return the element name
         *
         * @see org.geotools.validation.xml.ArgHelper.Mapping#getElementName()
         */
        public String getElementName() {
            return "double";
        }

        /**
         * Implementation of getInstance.
         *
         * @param elem Element the element to parse into a Double.
         *
         * @return Geometry an instance of Double if one can be created, null
         *         otherwise.
         *
         * @throws NullPointerException DOCUMENT ME!
         *
         * @see org.geotools.validation.xml.ArgHelper.Mapping#getInstance(org.w3c.dom.Element)
         */
        public Object getInstance(Element elem) {
            if (elem == null) {
                throw new NullPointerException("The double passed in was null");
            }

            return new Double(ReaderUtils.getElementText(elem));
        }

        public Object getInstance(String elem) {
            if (elem == null) {
                throw new NullPointerException("The double passed in was null");
            }

            return new Double(elem);
        }

        /**
         * Implementation of isClassInstance.
         *
         * @param c The Object to test
         *
         * @return true when both of type Long
         *
         * @see org.geotools.validation.xml.ArgHelper.Mapping#isClassInstance(java.lang.Object)
         */
        public boolean isClassInstance(Object c) {
            return ((c != null) && c instanceof Double);
        }

        public boolean isClass(Class c) {
            return ((c != null)
            && (Double.class.equals(c) || double.class.equals(c)));
        }

        /**
         * Implementation of encode.
         *
         * @param obj An object to encode as a Double.
         *
         * @return String the XML encoding
         *
         * @throws NullPointerException DOCUMENT ME!
         * @throws ClassCastException when obj is not of type Double
         *
         * @see org.geotools.validation.xml.ArgHelper.Mapping#encode(java.lang.Object)
         */
        public String encode(Object obj) {
            if (obj == null) {
                throw new NullPointerException(
                    "The double obj passed in was null");
            }

            if (!(obj instanceof Double)) {
                throw new ClassCastException(
                    "Object of type Long was expected.");
            }

            return "<double>" + ((Double) obj).toString() + "</double>\n";
        }

        public String toString(Object o) {
            return o.toString();
        }
    }

    /**
     * DateMapping purpose.
     * 
     * <p>
     * Represents the workings for a Date Mapping
     * </p>
     *
     * @author dzwiers, Refractions Research, Inc.
     * @author $Author: dmzwiers $ (last modification)
     * @version $Id$
     */
    protected static class DateMapping implements Mapping {
        /**
         * Implementation of getType.
         *
         * @return the type name
         *
         * @see org.geotools.validation.xml.ArgHelper.Mapping#getType()
         */
        public String getType() {
            return "xs:dateTime";
        }

        /**
         * Implementation of getElementName.
         *
         * @return the element name
         *
         * @see org.geotools.validation.xml.ArgHelper.Mapping#getElementName()
         */
        public String getElementName() {
            return "dateTime";
        }

        /**
         * Implementation of getInstance.
         *
         * @param elem Element the element to parse into a Date.
         *
         * @return Geometry an instance of Date if one can be created, null
         *         otherwise.
         *
         * @throws ValidationException DOCUMENT ME!
         * @throws NullPointerException DOCUMENT ME!
         *
         * @see org.geotools.validation.xml.ArgHelper.Mapping#getInstance(org.w3c.dom.Element)
         */
        public Object getInstance(Element elem) throws ValidationException {
            if (elem == null) {
                throw new NullPointerException(
                    "The dateTime passed in was null");
            }

            SimpleDateFormat sdf = new SimpleDateFormat(
                    "yyyy-mm-dd'T'hh:mm:ssZ");

            try {
                return sdf.parse(ReaderUtils.getElementText(elem));
            } catch (ParseException e) {
                throw new ValidationException(e);
            }
        }

        public Object getInstance(String elem) throws ValidationException {
            if (elem == null) {
                throw new NullPointerException(
                    "The dateTime passed in was null");
            }

            SimpleDateFormat sdf = new SimpleDateFormat(
                    "yyyy-mm-dd'T'hh:mm:ssZ");

            try {
                return sdf.parse(elem);
            } catch (ParseException e) {
                throw new ValidationException(e);
            }
        }

        /**
         * Implementation of isClassInstance.
         *
         * @param c The Object to test
         *
         * @return true when both of type Long
         *
         * @see org.geotools.validation.xml.ArgHelper.Mapping#isClassInstance(java.lang.Object)
         */
        public boolean isClassInstance(Object c) {
            return ((c != null) && c instanceof Date);
        }

        public boolean isClass(Class c) {
            return ((c != null) && Date.class.equals(c));
        }

        /**
         * Implementation of encode.
         *
         * @param obj An object to encode as a Date.
         *
         * @return String the XML encoding
         *
         * @throws NullPointerException DOCUMENT ME!
         * @throws ClassCastException when obj is not of type Date
         *
         * @see org.geotools.validation.xml.ArgHelper.Mapping#encode(java.lang.Object)
         */
        public String encode(Object obj) {
            if (obj == null) {
                throw new NullPointerException(
                    "The dateTime obj passed in was null");
            }

            if (!(obj instanceof Date)) {
                throw new ClassCastException(
                    "Object of type Long was expected.");
            }

            SimpleDateFormat sdf = new SimpleDateFormat(
                    "yyyy-mm-dd'T'hh:mm:ssZ");

            return "<dateTime>" + sdf.format((Date) obj) + "</dateTime>\n";
        }

        public String toString(Object o) {
            SimpleDateFormat sdf = new SimpleDateFormat(
                    "yyyy-mm-dd'T'hh:mm:ssZ");

            return sdf.format((Date) o);
        }
    }

    /**
     * URIMapping purpose.
     * 
     * <p>
     * Represents the workings for a URI Mapping
     * </p>
     *
     * @author dzwiers, Refractions Research, Inc.
     * @author $Author: dmzwiers $ (last modification)
     * @version $Id$
     */
    protected static class URIMapping implements Mapping {
        /**
         * Implementation of getType.
         *
         * @return the type name
         *
         * @see org.geotools.validation.xml.ArgHelper.Mapping#getType()
         */
        public String getType() {
            return "xs:anyURI";
        }

        /**
         * Implementation of getElementName.
         *
         * @return the element name
         *
         * @see org.geotools.validation.xml.ArgHelper.Mapping#getElementName()
         */
        public String getElementName() {
            return "anyURI";
        }

        /**
         * Implementation of getInstance.
         *
         * @param elem Element the element to parse into a URI.
         *
         * @return Geometry an instance of URI if one can be created, null
         *         otherwise.
         *
         * @throws ValidationException DOCUMENT ME!
         * @throws NullPointerException DOCUMENT ME!
         *
         * @see org.geotools.validation.xml.ArgHelper.Mapping#getInstance(org.w3c.dom.Element)
         */
        public Object getInstance(Element elem) throws ValidationException {
            if (elem == null) {
                throw new NullPointerException("The anyUri passed in was null");
            }

            try {
                return new URI(ReaderUtils.getElementText(elem).trim());
            } catch (URISyntaxException e) {
                throw new ValidationException(e);
            }
        }

        public Object getInstance(String value) throws ValidationException {
            if (value == null) {
                throw new NullPointerException("The anyUri passed in was null");
            }

            try {
                return new URI(value);
            } catch (URISyntaxException e) {
                throw new ValidationException(e);
            }
        }

        /**
         * Implementation of isClassInstance.
         *
         * @param c The Object to test
         *
         * @return true when both of type Long
         *
         * @see org.geotools.validation.xml.ArgHelper.Mapping#isClassInstance(java.lang.Object)
         */
        public boolean isClassInstance(Object c) {
            return ((c != null) && c instanceof URI);
        }

        public boolean isClass(Class c) {
            return ((c != null) && URI.class.equals(c));
        }

        /**
         * Implementation of encode.
         *
         * @param obj An object to encode as a URI.
         *
         * @return String the XML encoding
         *
         * @throws NullPointerException DOCUMENT ME!
         * @throws ClassCastException when obj is not of type URI
         *
         * @see org.geotools.validation.xml.ArgHelper.Mapping#encode(java.lang.Object)
         */
        public String encode(Object obj) {
            if (obj == null) {
                throw new NullPointerException(
                    "The anyUri obj passed in was null");
            }

            if (!(obj instanceof URI)) {
                throw new ClassCastException(
                    "Object of type Long was expected.");
            }

            return "<anyURI>" + ((URI) obj).toString() + "</anyURI>\n";
        }

        public String toString(Object o) {
            return o.toString();
        }
    }

    /**
     * BooleanMapping purpose.
     * 
     * <p>
     * Represents the workings for a Boolean Mapping
     * </p>
     *
     * @author dzwiers, Refractions Research, Inc.
     * @author $Author: dmzwiers $ (last modification)
     * @version $Id$
     */
    protected static class BooleanMapping implements Mapping {
        /**
         * Implementation of getType.
         *
         * @return the type name
         *
         * @see org.geotools.validation.xml.ArgHelper.Mapping#getType()
         */
        public String getType() {
            return "xs:boolean";
        }

        /**
         * Implementation of getElementName.
         *
         * @return the element name
         *
         * @see org.geotools.validation.xml.ArgHelper.Mapping#getElementName()
         */
        public String getElementName() {
            return "boolean";
        }

        /**
         * Implementation of getInstance.
         *
         * @param elem Element the element to parse into a Boolean.
         *
         * @return Geometry an instance of Boolean if one can be created, null
         *         otherwise.
         *
         * @throws NullPointerException DOCUMENT ME!
         *
         * @see org.geotools.validation.xml.ArgHelper.Mapping#getInstance(org.w3c.dom.Element)
         */
        public Object getInstance(Element elem) {
            if (elem == null) {
                throw new NullPointerException("The boolean passed in was null");
            }

            return new Boolean(ReaderUtils.getElementText(elem));
        }

        public Object getInstance(String elem) {
            if (elem == null) {
                throw new NullPointerException("The boolean passed in was null");
            }

            return new Boolean(elem);
        }

        /**
         * Implementation of isClassInstance.
         *
         * @param c The Object to test
         *
         * @return true when both of type Long
         *
         * @see org.geotools.validation.xml.ArgHelper.Mapping#isClassInstance(java.lang.Object)
         */
        public boolean isClassInstance(Object c) {
            return ((c != null) && c instanceof Boolean);
        }

        public boolean isClass(Class c) {
            return ((c != null)
            && (Boolean.class.equals(c) || boolean.class.equals(c)));
        }

        /**
         * Implementation of encode.
         *
         * @param obj An object to encode as a Boolean.
         *
         * @return String the XML encoding
         *
         * @throws NullPointerException DOCUMENT ME!
         * @throws ClassCastException when obj is not of type Boolean
         *
         * @see org.geotools.validation.xml.ArgHelper.Mapping#encode(java.lang.Object)
         */
        public String encode(Object obj) {
            if (obj == null) {
                throw new NullPointerException(
                    "The boolean obj passed in was null");
            }

            if (!(obj instanceof Boolean)) {
                throw new ClassCastException(
                    "Object of type Long was expected.");
            }

            return "<boolean>" + ((Boolean) obj).toString() + "</boolean>\n";
        }

        public String toString(Object o) {
            return o.toString();
        }
    }

    /**
     * StringMapping purpose.
     * 
     * <p>
     * Represents the workings for a String Mapping
     * </p>
     *
     * @author dzwiers, Refractions Research, Inc.
     * @author $Author: dmzwiers $ (last modification)
     * @version $Id$
     */
    protected static class StringMapping implements Mapping {
        /**
         * Implementation of getType.
         *
         * @return the type name
         *
         * @see org.geotools.validation.xml.ArgHelper.Mapping#getType()
         */
        public String getType() {
            return "xs:string";
        }

        /**
         * Implementation of getElementName.
         *
         * @return the element name
         *
         * @see org.geotools.validation.xml.ArgHelper.Mapping#getElementName()
         */
        public String getElementName() {
            return "string";
        }

        /**
         * Implementation of getInstance.
         *
         * @param elem Element the element to parse into a String.
         *
         * @return Geometry an instance of String if one can be created, null
         *         otherwise.
         *
         * @throws NullPointerException DOCUMENT ME!
         *
         * @see org.geotools.validation.xml.ArgHelper.Mapping#getInstance(org.w3c.dom.Element)
         */
        public Object getInstance(Element elem) {
            if (elem == null) {
                throw new NullPointerException("The string passed in was null");
            }

            return new String(ReaderUtils.getElementText(elem));
        }

        public Object getInstance(String value) {
            if (value == null) {
                throw new NullPointerException("The string passed in was null");
            }

            return new String(value);
        }

        /**
         * Implementation of isClassInstance.
         *
         * @param c The Object to test
         *
         * @return true when both of type Long
         *
         * @see org.geotools.validation.xml.ArgHelper.Mapping#isClassInstance(java.lang.Object)
         */
        public boolean isClassInstance(Object c) {
            return ((c != null) && c instanceof String);
        }

        public boolean isClass(Class c) {
            return ((c != null) && String.class.equals(c));
        }

        /**
         * Implementation of encode.
         *
         * @param obj An object to encode as a String.
         *
         * @return String the XML encoding
         *
         * @throws NullPointerException DOCUMENT ME!
         * @throws ClassCastException when obj is not of type String
         *
         * @see org.geotools.validation.xml.ArgHelper.Mapping#encode(java.lang.Object)
         */
        public String encode(Object obj) {
            if (obj == null) {
                throw new NullPointerException(
                    "The string obj passed in was null");
            }

            if (!(obj instanceof String)) {
                throw new ClassCastException(
                    "Object of type Long was expected.");
            }

            return "<string>" + ((String) obj).toString() + "</string>\n";
        }

        public String toString(Object o) {
            return o.toString();
        }
    }
}
