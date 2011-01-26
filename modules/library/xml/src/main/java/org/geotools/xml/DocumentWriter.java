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
package org.geotools.xml;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.naming.OperationNotSupportedException;

import org.geotools.xml.handlers.xsi.AttributeHandler;
import org.geotools.xml.handlers.xsi.ComplexTypeHandler;
import org.geotools.xml.schema.All;
import org.geotools.xml.schema.Any;
import org.geotools.xml.schema.Attribute;
import org.geotools.xml.schema.AttributeGroup;
import org.geotools.xml.schema.Choice;
import org.geotools.xml.schema.ComplexType;
import org.geotools.xml.schema.Element;
import org.geotools.xml.schema.ElementGrouping;
import org.geotools.xml.schema.Facet;
import org.geotools.xml.schema.Group;
import org.geotools.xml.schema.Schema;
import org.geotools.xml.schema.Sequence;
import org.geotools.xml.schema.SimpleType;
import org.geotools.xml.schema.Type;
import org.geotools.xml.xsi.XSISimpleTypes;
import org.xml.sax.Attributes;
import org.xml.sax.helpers.AttributesImpl;


/**
 * This is the thing that writes documents.
 * 
 * <p>
 * This will create valid XML documents, given an object and a schema.
 * </p>
 *
 * @author dzwiers
 * @source $URL$
 */
public class DocumentWriter {
    /** DOCUMENT ME! */
    public static final Logger logger = org.geotools.util.logging.Logging.getLogger(
            "net.refractions.xml.write");
    private static Level level = Level.WARNING;

    /**
     * Writer ... include the key to represent true when writing to files,
     * include a Writer to write to otherwise.
     */
    public static final String WRITE_SCHEMA = "DocumentWriter_WRITE_SCHEMA";

    /**
     * Element or String ... include a ref to an Element to be used, or a
     * string representing the name of the element
     */
    public static final String BASE_ELEMENT = "DocumentWriter_BASE_ELEMENT";

    /**
     * Schema[] or String[]... The order to search the schemas for a valid
     * element, either an array of ref to Schema instances or an Array or
     * TargetNamespaces
     */
    public static final String SCHEMA_ORDER = "DocumentWriter_SCHEMA_ORDER";
    
    /**
     * The Encoding which should be used for the Document which should be created.
     */
    public static final String ENCODING = "DocumentWriter_ENCODING";

    // TODO implement this searchOrder

    /**
     * boolean ... include the key to use the "nearest" strategy for searching
     * schemas. This will be ignored if a schema order was set. When not
     * included the schema order as they appear in the orginal schema will be
     * used.
     */
    public static final String USE_NEAREST = "DocumentWriter_USE_NEAREST";

    /** a map of URI->URI representing targetNamespace->Location */
    public static final String SCHEMA_LOCATION_HINT = "DocumentWriter_SCHEMA_LOCATION_HINT";

    /**
     * Sets the logger level
     * 
     * @param l Level
     */
    public static void setLevel(Level l) {
        level = l;
        logger.setLevel(l);
    }

    /**
     * Write value to file using provided schema.
     * 
     * <p>
     * Hints:
     * 
     * <ul>
     * <li>
     * WRITE_SCHEMA - (non null) write to outputfilename.xsd
     * </li>
     * <li>
     * BASE_ELEMENT - (Element) mapping of value to element instance
     * </li>
     * <li>
     * USE_NEAREST - (Boolean) not implemented
     * </li>
     * <li>
     * SCHEMA_ORDER - (String[] or Schema[]) resolve ambiguity & import
     * </li>
     * </ul>
     * </p>
     *
     * @param value
     * @param schema
     * @param f
     * @param hints
     *
     * @throws OperationNotSupportedException
     * @throws IOException
     *
     */
    public static void writeDocument(Object value, Schema schema, File f,
        Map hints) throws OperationNotSupportedException, IOException {
        if ((f == null) || (!f.canWrite())) {
            throw new IOException("Cannot write to " + f);
        }

        if ((hints != null) && hints.containsKey(WRITE_SCHEMA)) {
            Map hints2 = new HashMap(hints);
            hints2.remove(WRITE_SCHEMA);

            File f2 = new File(f.getParentFile(),
                    f.getName().substring(0, f.getName().indexOf(".")) + ".xsd");
            FileWriter wf = new FileWriter(f2);
            writeSchema(schema, wf, hints2);
            wf.close();
        }

        FileWriter wf = new FileWriter(f);
        writeDocument(value, schema, wf, hints);
        wf.close();
    }

    /**
     * Entry Point to Document writer.
     * 
     * <p>
     * Hints:
     * 
     * <ul>
     * <li>
     * WRITE_SCHEMA - (Writer) will be used to write the schema
     * </li>
     * <li>
     * BASE_ELEMENT - (Element) mapping of value to element instance
     * </li>
     * <li>
     * USE_NEAREST - (Boolean) not implemented
     * </li>
     * <li>
     * SCHEMA_ORDER - (String[] or Schema[]) resolve ambiguity & import
     * </li>
     * </ul>
     * </p>
     *
     * @param value
     * @param schema
     * @param w
     * @param hints optional hints for writing
     *
     * @throws OperationNotSupportedException
     * @throws IOException
     *
     */
    public static void writeDocument(Object value, Schema schema, Writer w,
        Map hints) throws OperationNotSupportedException, IOException {
        if ((hints != null) && hints.containsKey(WRITE_SCHEMA)) {
            Writer w2 = (Writer) hints.get(WRITE_SCHEMA);
            writeSchema(schema, w2, hints);

        }

        WriterContentHandler wch = new WriterContentHandler(schema, w, hints); // should deal with xmlns declarations
        wch.startDocument();

        writeFragment(value, wch);

        wch.endDocument();
        w.flush();
    }

    /**
     * Write value to file using provided schema.
     * 
     * <p>
     * Hints:
     * 
     * <ul>
     * <li>
     * BASE_ELEMENT - (Element) mapping of value to element instance
     * </li>
     * <li>
     * USE_NEAREST - (Boolean) not implemented
     * </li>
     * <li>
     * SCHEMA_ORDER - (String[] or Schema[]) resolve ambiguity & import
     * </li>
     * </ul>
     * </p>
     *
     * @param value
     * @param schema
     * @param f
     * @param hints
     *
     * @throws OperationNotSupportedException
     * @throws IOException
     *
     */
    public static void writeFragment(Object value, Schema schema, File f,
        Map hints) throws OperationNotSupportedException, IOException {
        if ((f == null) || (!f.canWrite())) {
            throw new IOException("Cannot write to " + f);
        }

        FileWriter wf = new FileWriter(f);
        writeFragment(value, schema, wf, hints);
        wf.close();
    }

    /**
     * Entry Point to Document writer.
     * 
     * <p>
     * Hints:
     * 
     * <ul>
     * <li>
     * BASE_ELEMENT - (Element) mapping of value to element instance
     * </li>
     * <li>
     * USE_NEAREST - (Boolean) not implemented
     * </li>
     * <li>
     * SCHEMA_ORDER - (String[] or Schema[]) resolve ambiguity & import
     * </li>
     * </ul>
     * </p>
     *
     * @param value
     * @param schema
     * @param w
     * @param hints optional hints for writing
     *
     * @throws OperationNotSupportedException
     * @throws IOException
     *
     */
    public static void writeFragment(Object value, Schema schema, Writer w,
        Map hints) throws OperationNotSupportedException, IOException {
        WriterContentHandler wch = new WriterContentHandler(schema, w, hints); // should deal with xmlns declarations

        writeFragment(value, wch);

        w.flush();
    }

    private static void writeFragment(Object value, WriterContentHandler wch)
        throws OperationNotSupportedException, IOException {

        Element e = null;
        logger.setLevel(level);

        if ((wch.hints != null) && wch.hints.containsKey(BASE_ELEMENT)) {
            e = (Element) wch.hints.get(BASE_ELEMENT);

            if ((e != null) && (e.getType() != null)) {
                e = e.getType().canEncode(e, value, wch.hints) ? e : null;
            }
        }

        if (e == null) {
            e = wch.findElement(value);
        }

        if (e != null) {
        	Type type = e.getType();
            type.encode(e, value, wch, wch.hints);
        } else {
            throw new OperationNotSupportedException(
                "Could not find an appropriate Element to use for encoding of a "
                + ((value == null) ? null : value.getClass().getName()));
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param schema DOCUMENT ME!
     * @param w DOCUMENT ME!
     * @param hints DOCUMENT ME!
     *
     * @throws IOException
     */
    public static void writeSchema(Schema schema, Writer w, Map hints)
        throws IOException {
        WriterContentHandler wch = new WriterContentHandler(schema, w, hints); // should deal with xmlns declarations
        Element[] elems = schema.getElements();

        if (elems == null) {
            throw new IOException("Cannot write for Schema "
                + schema.getTargetNamespace());
        }

        wch.startDocument();

        writeSchema(schema, wch, hints);

        wch.endDocument();
    }

    private static void writeSchema(Schema schema, PrintHandler ph, Map hints)
        throws IOException {
        if (schema == null) {
            return;
        }

        AttributesImpl ai = new AttributesImpl();

        ai.addAttribute("", "targetNamespace", "", "anyUri",
            schema.getTargetNamespace().toString());
        ai.addAttribute("", "xmlns", "", "anyUri",
            XSISimpleTypes.NAMESPACE.toString());
        ai.addAttribute("", "xmlns:" + schema.getPrefix(), "", "anyUri",
            schema.getTargetNamespace().toString());

        Schema[] imports = schema.getImports();

        for (int i = 0; i < imports.length; i++) {
            ai.addAttribute("", "xmlns:" + imports[i].getPrefix(), "",
                "anyUri", imports[i].getTargetNamespace().toString());
        }

        if ((schema.getId() != null) && (schema.getId() != "")) {
            ai.addAttribute("", "id", "", "ID", schema.getId());
        }

        if ((schema.getVersion() != null) && (schema.getVersion() != "")) {
            ai.addAttribute("", "version", "", "String", schema.getVersion());
        }

        if (schema.isAttributeFormDefault()) {
            ai.addAttribute("", "attributeFormDefault", "", "NMTOKEN",
                "qualified");
        }

        if (schema.isElementFormDefault()) {
            ai.addAttribute("", "elementFormDefault", "", "NMTOKEN", "qualified");
        }

        if (schema.getBlockDefault() != Schema.NONE) {
            ai.addAttribute("", "blockDefault", "", "NMTOKENS",
                ComplexTypeHandler.writeBlock(schema.getBlockDefault()));
        }

        if (schema.getFinalDefault() != Schema.NONE) {
            ai.addAttribute("", "finalDefault", "", "NMTOKENS",
                ComplexTypeHandler.writeFinal(schema.getFinalDefault()));
        }

        ph.startElement(XSISimpleTypes.NAMESPACE, "schema", ai);

        for (int i = 0; i < imports.length; i++)
            writeImport(imports[i], ph);

        Element[] elems = schema.getElements();

        if (elems != null) {
            for (int i = 0; i < elems.length; i++)
                writeElement(elems[i], schema, ph, hints);
        }

        ComplexType[] cts = schema.getComplexTypes();

        if (elems != null) {
            for (int i = 0; i < cts.length; i++)
                writeComplexType(cts[i], schema, ph, hints);
        }

        SimpleType[] sts = schema.getSimpleTypes();

        if (elems != null) {
            for (int i = 0; i < sts.length; i++)
                writeSimpleType(sts[i], schema, ph, hints);
        }

        Group[] groups = schema.getGroups();

        if (elems != null) {
            for (int i = 0; i < groups.length; i++)
                writeGroup(groups[i], schema, ph, hints);
        }

        Attribute[] attrs = schema.getAttributes();

        if (elems != null) {
            for (int i = 0; i < attrs.length; i++)
                writeAttribute(attrs[i], schema, ph, hints);
        }

        AttributeGroup[] attrgrps = schema.getAttributeGroups();

        if (elems != null) {
            for (int i = 0; i < attrgrps.length; i++)
                writeAttributeGroup(attrgrps[i], schema, ph, hints);
        }

        ph.endElement(XSISimpleTypes.NAMESPACE, "schema");
    }

    private static void writeImport(Schema schema, PrintHandler ph)
        throws IOException {
        AttributesImpl ai = new AttributesImpl();

        if ((schema.getId() != null) && (schema.getId() != "")) {
            ai.addAttribute("", "id", "", "ID", schema.getId());
        }

        ai.addAttribute("", "namespace", "", "anyUri",
            schema.getTargetNamespace().toString());

        if (schema.getURI() != null) {
            ai.addAttribute("", "schemaLocation", "", "anyUri",
                schema.getURI().toString());
        }

        ph.element(XSISimpleTypes.NAMESPACE, "import", ai);
    }

    private static void writeElement(Element element, Schema schema,
        PrintHandler ph, Map hints) throws IOException {
        AttributesImpl ai = new AttributesImpl();

        if ((element.getId() != null) && (element.getId() != "")) {
            ai.addAttribute("", "id", "", "ID", element.getId());
        }

        if (element.getMaxOccurs() != 1) {
            ai.addAttribute("", "maxOccurs", "", "Union",
                (element.getMaxOccurs() == ElementGrouping.UNBOUNDED) ? "unbounded"
                                                              : (""
                + element.getMaxOccurs()));
        }

        if (element.getMinOccurs() != 1) {
            ai.addAttribute("", "minOccurs", "", "ID",
                "" + element.getMinOccurs());
        }

        boolean nested = false;

        if (element.getNamespace().equals(schema.getTargetNamespace())) {
            // search schema for element, then it's a ref
            Element[] elems = schema.getElements();
            boolean found = false;

            for (int i = 0; (i < elems.length) && !found; i++)
                if (element.getName().equals(elems[i].getName())) {
                    found = true;
                    ai.addAttribute("", "ref", "", "QName", element.getName());
                }

            if (!found) {
                // type in other NS
                if (!element.getType().getNamespace().equals(schema
                            .getTargetNamespace())) {
                    found = true;
                    XSISAXHandler.setLogLevel(logger.getLevel());
                    Schema s = SchemaFactory.getInstance(element.getNamespace());

                    if ((element.getName() != null)
                            && (element.getName() != "")) {
                        ai.addAttribute("", "name", "", "QName",
                            element.getName());
                    }

                    ai.addAttribute("", "type", "", "QName",
                        s.getPrefix() + ":" + element.getType().getName());
                }

                // search schema for type, then type can be a qName
                Type[] types = schema.getComplexTypes();

                for (int i = 0; (i < types.length) && !found; i++)

                    // TODO use equals here
                    if (element.getType().getName().equals(types[i].getName())) {
                        found = true;

                        if ((element.getName() != null)
                                && (element.getName() != "")) {
                            ai.addAttribute("", "name", "", "QName",
                                element.getName());
                        }

                        ai.addAttribute("", "type", "", "QName",
                            element.getType().getName());
                    }

                types = schema.getSimpleTypes();

                for (int i = 0; (i < types.length) && !found; i++)

                    // TODO use equals here
                    if (element.getType().getName().equals(types[i].getName())) {
                        found = true;

                        if ((element.getName() != null)
                                && (element.getName() != "")) {
                            ai.addAttribute("", "name", "", "QName",
                                element.getName());
                        }

                        ai.addAttribute("", "type", "", "QName",
                            element.getType().getName());
                    }

                if (!found) {
                    // 	we are nested  ... log this
                    nested = true;

                    if ((element.getName() != null)
                            && (element.getName() != "")) {
                        ai.addAttribute("", "name", "", "QName",
                            element.getName());
                    }
                }
            }
        } else {
            // use a ref
            Schema s = SchemaFactory.getInstance(element.getNamespace());
            ai.addAttribute("", "ref", "", "QName",
                s.getPrefix() + ":" + element.getName());
        }

        if (element.isNillable()) {
            ai.addAttribute("", "nillable", "", "boolean", "true");
        }

        if ((element.getDefault() != null) && (element.getDefault() != "")) {
            ai.addAttribute("", "default", "", "String", element.getDefault());
        } else {
            if ((element.getFixed() != null) && (element.getFixed() != "")) {
                ai.addAttribute("", "fixed", "", "String", element.getFixed());
            }
        }

        if (element.getSubstitutionGroup() != null) {
            String s = "";

            if (!element.getSubstitutionGroup().getNamespace().equals(schema
                        .getTargetNamespace())) {
                Schema sss = SchemaFactory.getInstance(element.getSubstitutionGroup()
                                                              .getNamespace());
                s = sss.getPrefix() + ":";
            }

            s += element.getSubstitutionGroup().getName();
            ai.addAttribute("", "substitutionGroup", "", "QName", s);
        }

        if (element.isForm()) {
            ai.addAttribute("", "form", "", "NMTOKEN", "qualified");
        }

        if (element.getFinal() != Schema.NONE) {
            ai.addAttribute("", "final", "", "NMTOKENS",
                ComplexTypeHandler.writeFinal(element.getFinal()));
        }

        if (element.getBlock() != Schema.NONE) {
            ai.addAttribute("", "block", "", "NMTOKENS",
                ComplexTypeHandler.writeBlock(element.getBlock()));
        }

        if (element.isAbstract()) {
            ai.addAttribute("", "abstract", "", "boolean", "true");
        }

        ph.startElement(XSISimpleTypes.NAMESPACE, "element", ai);

        if (nested) {
            if (element.getType() instanceof ComplexType) {
                writeComplexType((ComplexType) element.getType(), schema, ph,
                    hints);
            } else {
                writeSimpleType((SimpleType) element.getType(), schema, ph,
                    hints);
            }
        }

        ph.endElement(XSISimpleTypes.NAMESPACE, "element");
    }

    private static void writeAttribute(Attribute attribute, Schema schema,
        PrintHandler ph, Map hints) throws IOException {
        AttributesImpl ai = new AttributesImpl();

        if ((attribute.getId() != null) && (attribute.getId() != "")) {
            ai.addAttribute("", "id", "", "ID", attribute.getId());
        }

        boolean nested = false;

        if (attribute.getNamespace().equals(schema.getTargetNamespace())) {
            // search schema for element, then it's a ref
            Attribute[] elems = schema.getAttributes();
            boolean found = false;

            for (int i = 0; (i < elems.length) && !found; i++)
                if (attribute.getName().equals(elems[i].getName())) {
                    found = true;
                    ai.addAttribute("", "ref", "", "QName", attribute.getName());
                }

            if (!found) {
                // type in other NS
                if (!attribute.getSimpleType().getNamespace().equals(schema
                            .getTargetNamespace())) {
                    found = true;
                    XSISAXHandler.setLogLevel(logger.getLevel());
                    Schema s = SchemaFactory.getInstance(attribute.getNamespace());

                    if ((attribute.getName() != null)
                            && (attribute.getName() != "")) {
                        ai.addAttribute("", "name", "", "QName",
                            attribute.getName());
                    }

                    ai.addAttribute("", "type", "", "QName",
                        s.getPrefix() + ":"
                        + attribute.getSimpleType().getName());
                }

                // search schema for type, then type can be a qName
                SimpleType[] types = schema.getSimpleTypes();

                for (int i = 0; (i < types.length) && !found; i++)

                    // TODO use equals here
                    if (attribute.getSimpleType().getName().equals(types[i]
                                .getName())) {
                        found = true;

                        if ((attribute.getName() != null)
                                && (attribute.getName() != "")) {
                            ai.addAttribute("", "name", "", "QName",
                                attribute.getName());
                        }

                        ai.addAttribute("", "type", "", "QName",
                            attribute.getSimpleType().getName());
                    }

                if (!found) {
                    // 	we are nested  ... log this
                    nested = true;

                    if ((attribute.getName() != null)
                            && (attribute.getName() != "")) {
                        ai.addAttribute("", "name", "", "QName",
                            attribute.getName());
                    }
                }
            }
        } else {
            // use a ref
            Schema s = SchemaFactory.getInstance(attribute.getNamespace());
            ai.addAttribute("", "ref", "", "QName",
                s.getPrefix() + ":" + attribute.getName());
        }

        if (attribute.getUse() != Attribute.OPTIONAL) {
            ai.addAttribute("", "use", "", "NMTOKEN",
                AttributeHandler.writeUse(attribute.getUse()));
        }

        if ((attribute.getDefault() != null) && (attribute.getDefault() != "")) {
            ai.addAttribute("", "default", "", "String", attribute.getDefault());
        } else {
            if ((attribute.getFixed() != null) && (attribute.getFixed() != "")) {
                ai.addAttribute("", "fixed", "", "String", attribute.getFixed());
            }
        }

        if (attribute.isForm()) {
            ai.addAttribute("", "form", "", "NMTOKEN", "qualified");
        }

        ph.startElement(XSISimpleTypes.NAMESPACE, "attribute", ai);

        if (nested) {
            writeSimpleType(attribute.getSimpleType(), schema, ph, hints);
        }

        ph.endElement(XSISimpleTypes.NAMESPACE, "attribute");
    }

    private static void writeGroup(Group group, Schema schema, PrintHandler ph,
        Map hints) throws IOException {
        AttributesImpl ai = new AttributesImpl();

        if ((group.getId() != null) && (group.getId() != "")) {
            ai.addAttribute("", "id", "", "ID", group.getId());
        }

        boolean nested = false;

        if (group.getNamespace().equals(schema.getTargetNamespace())) {
            // search schema for element, then it's a ref
            Group[] groups = schema.getGroups();
            boolean found = false;

            for (int i = 0; (i < groups.length) && !found; i++)
                if (group.getName().equals(groups[i].getName())) {
                    found = true;
                    ai.addAttribute("", "ref", "", "QName", group.getName());
                }

            if (!found) {
                ai.addAttribute("", "name", "", "QName", group.getName());
                nested = true;
            }
        } else {
            // use a ref
        	XSISAXHandler.setLogLevel(logger.getLevel());
            Schema s = SchemaFactory.getInstance(group.getNamespace());
            ai.addAttribute("", "ref", "", "QName",
                s.getPrefix() + ":" + group.getName());
        }

        if (group.getMaxOccurs() != 1) {
            ai.addAttribute("", "maxOccurs", "", "Union",
                (group.getMaxOccurs() == ElementGrouping.UNBOUNDED) ? "unbounded"
                                                            : (""
                + group.getMaxOccurs()));
        }

        if (group.getMinOccurs() != 1) {
            ai.addAttribute("", "minOccurs", "", "ID", ""
                + group.getMinOccurs());
        }

        ph.startElement(XSISimpleTypes.NAMESPACE, "group", ai);

        if (nested) {
            if (group.getChild().getGrouping() == ElementGrouping.CHOICE) {
                writeChoice((Choice) group.getChild(), schema, ph, hints);
            } else {
                writeSequence((Sequence) group.getChild(), schema, ph, hints);
            }
        }

        ph.endElement(XSISimpleTypes.NAMESPACE, "group");
    }

    private static void writeAttributeGroup(AttributeGroup attributeGroup,
        Schema schema, PrintHandler ph, Map hints) throws IOException {
        AttributesImpl ai = new AttributesImpl();

        if ((attributeGroup.getId() != null) && (attributeGroup.getId() != "")) {
            ai.addAttribute("", "id", "", "ID", attributeGroup.getId());
        }

        boolean nested = false;

        if (attributeGroup.getNamespace().equals(schema.getTargetNamespace())) {
            // search schema for element, then it's a ref
            Group[] groups = schema.getGroups();
            boolean found = false;

            for (int i = 0; (i < groups.length) && !found; i++)
                if (attributeGroup.getName().equals(groups[i].getName())) {
                    found = true;
                    ai.addAttribute("", "ref", "", "QName",
                        attributeGroup.getName());
                }

            if (!found) {
                ai.addAttribute("", "name", "", "QName",
                    attributeGroup.getName());
                nested = true;
            }
        } else {
            // use a ref
        	XSISAXHandler.setLogLevel(logger.getLevel());
            Schema s = SchemaFactory.getInstance(attributeGroup.getNamespace());
            ai.addAttribute("", "ref", "", "QName",
                s.getPrefix() + ":" + attributeGroup.getName());
        }

        ph.startElement(XSISimpleTypes.NAMESPACE, "group", ai);

        if (nested) {
            if (attributeGroup.getAnyAttributeNameSpace() != null) {
                ai = new AttributesImpl();
                ai.addAttribute("", "namespace", "", "special",
                    attributeGroup.getAnyAttributeNameSpace());
                ph.element(XSISimpleTypes.NAMESPACE, "anyAttribute", ai);
            }

            if (attributeGroup.getAttributes() != null) {
                Attribute[] attrs = attributeGroup.getAttributes();

                for (int i = 0; i < attrs.length; i++)
                    writeAttribute(attrs[i], schema, ph, hints);
            }
        }

        ph.endElement(XSISimpleTypes.NAMESPACE, "group");
    }

    private static void writeSimpleType(SimpleType simpleType, Schema schema,
        PrintHandler ph, Map hints) throws IOException {
        if (XSISimpleTypes.NAMESPACE.equals(simpleType.getNamespace())) {
            //error - not sure what to do
            // TODO log the type error - throw an exception?
        }

        AttributesImpl ai = new AttributesImpl();

        if ((simpleType.getId() != null) && (simpleType.getId() != "")) {
            ai.addAttribute("", "id", "", "ID", simpleType.getId());
        }

        if ((simpleType.getName() != null) && (simpleType.getName() != "")) {
            ai.addAttribute("", "name", "", "NCName", simpleType.getName());
        }

        if (simpleType.getFinal() != Schema.NONE) {
            ai.addAttribute("", "final", "", "NMTOKENS",
                ComplexTypeHandler.writeFinal(simpleType.getFinal()));
        }

        ph.startElement(XSISimpleTypes.NAMESPACE, "simpleType", ai);

        switch (simpleType.getChildType()) {
        case SimpleType.RESTRICTION:

            // determine whether to print or reference the child st
            SimpleType st = simpleType.getParents()[0];
            ai = null;

            if (schema.getTargetNamespace().equals(st.getNamespace())) {
                if ((st.getName() != null) && (st.getName() != "")) {
                    SimpleType[] sts = schema.getSimpleTypes();

                    if (sts != null) {
                        for (int i = 0; i < sts.length; i++)
                            if (st.getName().equals(sts[i].getName())) {
                                ai = new AttributesImpl();
                                ai.addAttribute("", "base", "", "QName",
                                    st.getName());
                            }
                    }
                }
            } else {
                ai = new AttributesImpl();
                XSISAXHandler.setLogLevel(logger.getLevel());
                Schema s = SchemaFactory.getInstance(st.getNamespace());
                ai.addAttribute("", "base", "", "QName",
                    s.getPrefix() + ":" + st.getName());
            }

            ph.startElement(XSISimpleTypes.NAMESPACE, "restriction", ai);

            if (ai == null) {
                writeSimpleType(st, schema, ph, hints);
            }

            Facet[] facets = simpleType.getFacets();

            if (facets != null) {
                for (int i = 0; i < facets.length; i++)
                    writeFacet(facets[i], ph);
            }

            ph.endElement(XSISimpleTypes.NAMESPACE, "restriction");

            break;

        case SimpleType.LIST:

            // determine whether to print or reference the child st
            st = simpleType.getParents()[0];
            ai = null;

            if (schema.getTargetNamespace().equals(st.getNamespace())) {
                if ((st.getName() != null) && (st.getName() != "")) {
                    SimpleType[] sts = schema.getSimpleTypes();

                    if (sts != null) {
                        for (int i = 0; i < sts.length; i++)
                            if (st.getName().equals(sts[i].getName())) {
                                ai = new AttributesImpl();
                                ai.addAttribute("", "itemType", "", "QName",
                                    st.getName());
                            }
                    }
                }
            } else {
                ai = new AttributesImpl();

                Schema s = SchemaFactory.getInstance(st.getNamespace());
                ai.addAttribute("", "itemType", "", "QName",
                    s.getPrefix() + ":" + st.getName());
            }

            ph.startElement(XSISimpleTypes.NAMESPACE, "list", ai);

            if (ai == null) {
                writeSimpleType(st, schema, ph, hints);
            }

            ph.endElement(XSISimpleTypes.NAMESPACE, "list");

            break;

        case SimpleType.UNION:

            // determine whether to print or reference the child st
            SimpleType[] sts = simpleType.getParents();
            String memberTypes = null;
            List childTs = new LinkedList();

            if (sts != null) {
                for (int j = 0; j < sts.length; j++) {
                    st = sts[j];

                    if (schema.getTargetNamespace().equals(st.getNamespace())) {
                        boolean found = false;

                        if ((st.getName() != null) && (st.getName() != "")) {
                            SimpleType[] sts2 = schema.getSimpleTypes();

                            if (sts2 != null) {
                                for (int i = 0; i < sts2.length; i++)
                                    if (st.getName().equals(sts2[i].getName())) {
                                        found = true;

                                        if (memberTypes == null) {
                                            memberTypes = st.getName();
                                        } else {
                                            memberTypes += (" " + st.getName());
                                        }
                                    }
                            }
                        }

                        if (!found) {
                            childTs.add(st);
                        }
                    } else {
                        ai = new AttributesImpl();

                        Schema s = SchemaFactory.getInstance(st.getNamespace());

                        if (memberTypes == null) {
                            memberTypes = s.getPrefix() + ":" + st.getName();
                        } else {
                            memberTypes += (" " + s.getPrefix() + ":"
                            + st.getName());
                        }
                    }
                }
            }

            if (memberTypes != null) {
                ai = new AttributesImpl();
                ai.addAttribute("", "memberTypes", "", "QName", memberTypes);
            }

            ph.startElement(XSISimpleTypes.NAMESPACE, "union", ai);

            if (childTs.size() > 0) {
                Iterator i = childTs.iterator();

                while (i.hasNext())
                    writeSimpleType((SimpleType) i.next(), schema, ph, hints);
            }

            ph.endElement(XSISimpleTypes.NAMESPACE, "union");

            break;
        }

        ph.endElement(XSISimpleTypes.NAMESPACE, "simpleType");
    }

    private static void writeChoice(Choice choice, Schema schema,
        PrintHandler ph, Map hints) throws IOException {
        AttributesImpl ai = new AttributesImpl();

        if ((choice.getId() != null) && (choice.getId() != "")) {
            ai.addAttribute("", "id", "", "ID", choice.getId());
        }

        if (choice.getMaxOccurs() != 1) {
            ai.addAttribute("", "maxOccurs", "", "Union",
                (choice.getMaxOccurs() == ElementGrouping.UNBOUNDED) ? "unbounded"
                                                             : (""
                + choice.getMaxOccurs()));
        }

        if (choice.getMinOccurs() != 1) {
            ai.addAttribute("", "minOccurs", "", "ID",
                "" + choice.getMinOccurs());
        }

        ph.startElement(XSISimpleTypes.NAMESPACE, "choice", ai);

        ElementGrouping[] egs = choice.getChildren();

        if (egs != null) {
            for (int i = 0; i < egs.length; i++)
                if (egs[i] != null) {
                    switch (egs[i].getGrouping()) {
                    case ElementGrouping.ALL:
                        writeAll((All) egs[i], schema, ph, hints);

                        break;

                    case ElementGrouping.ANY:
                        writeAny((Any) egs[i], ph);

                        break;

                    case ElementGrouping.CHOICE:
                        writeChoice((Choice) egs[i], schema, ph, hints);

                        break;

                    case ElementGrouping.ELEMENT:
                        writeElement((Element) egs[i], schema, ph, hints);

                        break;

                    case ElementGrouping.GROUP:
                        writeGroup((Group) egs[i], schema, ph, hints);

                        break;

                    case ElementGrouping.SEQUENCE:
                        writeSequence((Sequence) egs[i], schema, ph, hints);

                        break;
                    }
                }
        }

        ph.endElement(XSISimpleTypes.NAMESPACE, "choice");
    }

    private static void writeSequence(Sequence sequence, Schema schema,
        PrintHandler ph, Map hints) throws IOException {
        AttributesImpl ai = new AttributesImpl();

        if ((sequence.getId() != null) && (sequence.getId() != "")) {
            ai.addAttribute("", "id", "", "ID", sequence.getId());
        }

        if (sequence.getMaxOccurs() != 1) {
            ai.addAttribute("", "maxOccurs", "", "Union",
                (sequence.getMaxOccurs() == ElementGrouping.UNBOUNDED) ? "unbounded"
                                                               : (""
                + sequence.getMaxOccurs()));
        }

        if (sequence.getMinOccurs() != 1) {
            ai.addAttribute("", "minOccurs", "", "ID",
                "" + sequence.getMinOccurs());
        }

        ph.startElement(XSISimpleTypes.NAMESPACE, "sequence", ai);

        ElementGrouping[] egs = sequence.getChildren();

        if (egs != null) {
            for (int i = 0; i < egs.length; i++)
                if (egs[i] != null) {
                    switch (egs[i].getGrouping()) {
                    case ElementGrouping.ANY:
                        writeAny((Any) egs[i], ph);

                        break;

                    case ElementGrouping.CHOICE:
                        writeChoice((Choice) egs[i], schema, ph, hints);

                        break;

                    case ElementGrouping.ELEMENT:
                        writeElement((Element) egs[i], schema, ph, hints);

                        break;

                    case ElementGrouping.GROUP:
                        writeGroup((Group) egs[i], schema, ph, hints);

                        break;

                    case ElementGrouping.SEQUENCE:
                        writeSequence((Sequence) egs[i], schema, ph, hints);

                        break;
                    }
                }
        }

        ph.endElement(XSISimpleTypes.NAMESPACE, "sequence");
    }

    private static void writeAll(All all, Schema schema, PrintHandler ph,
        Map hints) throws IOException {
        AttributesImpl ai = new AttributesImpl();

        if ((all.getId() != null) && (all.getId() != "")) {
            ai.addAttribute("", "id", "", "ID", all.getId());
        }

        if (all.getMaxOccurs() != 1) {
            ai.addAttribute("", "maxOccurs", "", "Union",
                (all.getMaxOccurs() == ElementGrouping.UNBOUNDED) ? "unbounded"
                                                          : (""
                + all.getMaxOccurs()));
        }

        if (all.getMinOccurs() != 1) {
            ai.addAttribute("", "minOccurs", "", "ID", "" + all.getMinOccurs());
        }

        ph.startElement(XSISimpleTypes.NAMESPACE, "all", ai);

        Element[] egs = all.getElements();

        if (egs != null) {
            for (int i = 0; i < egs.length; i++)
                if (egs[i] != null) {
                    writeElement(egs[i], schema, ph, hints);
                }
        }

        ph.endElement(XSISimpleTypes.NAMESPACE, "all");
    }

    private static void writeAny(Any any, PrintHandler ph) throws IOException {
        AttributesImpl ai = new AttributesImpl();

        if ((any.getId() != null) && (any.getId() != "")) {
            ai.addAttribute("", "id", "", "ID", any.getId());
        }

        if (any.getMaxOccurs() != 1) {
            ai.addAttribute("", "maxOccurs", "", "Union",
                (any.getMaxOccurs() == ElementGrouping.UNBOUNDED) ? "unbounded"
                                                          : (""
                + any.getMaxOccurs()));
        }

        if (any.getMinOccurs() != 1) {
            ai.addAttribute("", "minOccurs", "", "ID", "" + any.getMinOccurs());
        }

        if (any.getNamespace() != null) {
            ai.addAttribute("", "namespace", "", "special",
                "" + any.getNamespace());
        }

        ph.element(XSISimpleTypes.NAMESPACE, "any", ai);
    }

    private static void writeComplexType(ComplexType complexType,
        Schema schema, PrintHandler ph, Map hints) throws IOException {
        AttributesImpl ai = new AttributesImpl();

        if ((complexType.getId() != null) && (complexType.getId() != "")) {
            ai.addAttribute("", "id", "", "ID", complexType.getId());
        }

        if ((complexType.getName() != null) && (complexType.getName() != "")) {
            ai.addAttribute("", "name", "", "NCName", complexType.getName());
        }

        if (complexType.isAbstract()) {
            ai.addAttribute("", "abstract", "", "boolean", "true");
        }

        if (complexType.getFinal() != Schema.NONE) {
            ai.addAttribute("", "final", "", "NMTOKENS",
                ComplexTypeHandler.writeFinal(complexType.getFinal()));
        }

        if (complexType.getBlock() != Schema.NONE) {
            ai.addAttribute("", "block", "", "NMTOKENS",
                ComplexTypeHandler.writeBlock(complexType.getBlock()));
        }

        if (complexType.isMixed()) {
            ai.addAttribute("", "mixed", "", "boolean", "true");
        }

        ph.startElement(XSISimpleTypes.NAMESPACE, "complexType", ai);

        ElementGrouping egs = complexType.getChild();
        complexType.getChild();

        // TODO determine if this complexType isDerived ... and make a complexContent or Simplecontent to match
        if (egs != null) {
            switch (egs.getGrouping()) {
            // TODO determine if this will work
            //                    	case ElementGrouping.COMPLEXCONTENT:
            //                    	    writeAny((ComplexContent)egs,schema,ph,hints);
            //                    	    break;
            //                    	case ElementGrouping.SIMPLECONTENT:
            //                    	    writeAny((SimpleContent)egs,schema,ph,hints);
            //                    	    break;
            case ElementGrouping.ALL:
                writeAll((All) egs, schema, ph, hints);

                break;

            case ElementGrouping.CHOICE:
                writeChoice((Choice) egs, schema, ph, hints);

                break;

            case ElementGrouping.GROUP:
                writeGroup((Group) egs, schema, ph, hints);

                break;

            case ElementGrouping.SEQUENCE:
                writeSequence((Sequence) egs, schema, ph, hints);

                break;
            }
        }

        if (complexType.getAnyAttributeNameSpace() != null) {
            ai = new AttributesImpl();
            ai.addAttribute("", "namespace", "", "special",
                complexType.getAnyAttributeNameSpace());
            ph.element(XSISimpleTypes.NAMESPACE, "anyAttribute", ai);
        }

        // TODO think about checking for attribute groupings?
        // add a Attribute.getParentGroup() ... null or Group
        if (complexType.getAttributes() != null) {
            Attribute[] attrs = complexType.getAttributes();

            for (int i = 0; i < attrs.length; i++)
                writeAttribute(attrs[i], schema, ph, hints);
        }

        ph.endElement(XSISimpleTypes.NAMESPACE, "complexType");
    }

    private static void writeFacet(Facet facet, PrintHandler ph) throws IOException {
        if (facet == null) {
            return;
        }

        AttributesImpl ai = new AttributesImpl();
        ai.addAttribute("", "value", "", "ID", facet.getValue());

        switch (facet.getFacetType()) {
        case Facet.ENUMERATION:
            ph.element(XSISimpleTypes.NAMESPACE, "enumeration", ai);

            break;

        case Facet.FRACTIONDIGITS:
            ph.element(XSISimpleTypes.NAMESPACE, "fractionDigits", ai);

            break;

        case Facet.LENGTH:
            ph.element(XSISimpleTypes.NAMESPACE, "length", ai);

            break;

        case Facet.MAXEXCLUSIVE:
            ph.element(XSISimpleTypes.NAMESPACE, "maxExclusive", ai);

            break;

        case Facet.MAXINCLUSIVE:
            ph.element(XSISimpleTypes.NAMESPACE, "maxInclusive", ai);

            break;

        case Facet.MAXLENGTH:
            ph.element(XSISimpleTypes.NAMESPACE, "maxLength", ai);

            break;

        case Facet.MINEXCLUSIVE:
            ph.element(XSISimpleTypes.NAMESPACE, "minExclusive", ai);

            break;

        case Facet.MININCLUSIVE:
            ph.element(XSISimpleTypes.NAMESPACE, "minInclusive", ai);

            break;

        case Facet.MINLENGTH:
            ph.element(XSISimpleTypes.NAMESPACE, "minLength", ai);

            break;

        case Facet.PATTERN:
            ph.element(XSISimpleTypes.NAMESPACE, "pattern", ai);

            break;

        case Facet.TOTALDIGITS:
            ph.element(XSISimpleTypes.NAMESPACE, "totalDigits", ai);

            break;

        case Facet.WHITESPACE:
            ph.element(XSISimpleTypes.NAMESPACE, "whiteSpace", ai);

            break;
        }
    }

    private static class WriterContentHandler implements PrintHandler {
        private boolean firstElement = true; // needed for NS declarations
        private Writer writer;
        private Map prefixMappings; // when the value is null it has not been included yet into the document ...
        private Schema schema;
        protected Map hints;
        private Schema[] searchOrder = null;
        private String encoding = "UTF-8";

        WriterContentHandler(Schema schema, Writer writer, Map hints) {
            this.writer = writer;
            this.schema = schema;
            this.hints = hints;
            
            if (this.hints != null){
	            Object encodingValue = this.getHint(DocumentWriter.ENCODING);
	            if (encodingValue != null){
	            	this.encoding = encodingValue.toString();
	            }
            }
            
            prefixMappings = new HashMap();
            prefixMappings.put(schema.getTargetNamespace(), "");

            Schema[] imports = schema.getImports();

            if (imports != null) {
                for (int i = 0; i < imports.length; i++)
                    prefixMappings.put(imports[i].getTargetNamespace(),
                        imports[i].getPrefix());
            }
        }

        private void printXMLNSDecs(Map arg0) throws IOException {
            Schema[] imports = getSchemaOrdering();
            String s = "";
            Map schemaLocs = (Map) ((arg0 == null) ? null
                                                    : arg0.get(SCHEMA_LOCATION_HINT));
            schemaLocs = (schemaLocs == null) ? new HashMap() : schemaLocs;

            for (int i = 0; i < imports.length; i++) {
                if(imports[i] !=null){
                if (imports[i] == schema) {
                    writer.write(" xmlns=\"" + schema.getTargetNamespace()
                        + "\"");

                    if ((schema.getURI() != null)
                            && !schema.getTargetNamespace().equals(schema
                                .getURI())) {

                        String endResult = schema.getURI().toString();
                        endResult=endResult.replaceAll("&", "&amp;");
                        s = schema.getTargetNamespace() + " " + endResult;
                    }
                } else {
                    writer.write(" xmlns:" + imports[i].getPrefix() + "=\""
                        + imports[i].getTargetNamespace() + "\"");
                }

                URI location = imports[i].getURI();
                boolean forced = false;

                if (schemaLocs.containsKey(imports[i].getTargetNamespace())) {
                    location = (URI) schemaLocs.get(imports[i]
                            .getTargetNamespace());
                    forced = true;
                }

                if ((location != null) && location.isAbsolute()) {
                    if (imports[i].includesURI(location) || forced) {
                        if ((location != null)
                                && !location.equals(
                                    imports[i].getTargetNamespace())) {
                        	String endResult = location.toString();
                        	endResult=endResult.replaceAll("&", "&amp;");
                            s += (" " + imports[i].getTargetNamespace() + " "
                            + endResult);
                        }
                    }
                }
            }}

            s = s.trim();

            if (!"".equals(s)) {
                writer.write(
                    " xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"");
                writer.write(" xsi:schemaLocation=\"" + s + "\"");
            }
        }

        /**
         * @see PrintHandler#startElement(URI, String, Attributes)
         * 
         * @param namespaceURI URI
         * @param localName String
         * @param attributes Attributes
         */
        public void startElement(URI namespaceURI, String localName,
            Attributes attributes) throws IOException {
            String prefix = (String) prefixMappings.get(namespaceURI);

            if (prefix != null) {
                if (!prefix.equals("")) {
                    prefix = prefix + ":";
                }
            } else {
                if (namespaceURI.equals(schema.getTargetNamespace())) {
                    prefix = "";
                } else {
                	XSISAXHandler.setLogLevel(logger.getLevel());
                    prefix = SchemaFactory.getInstance(namespaceURI).getPrefix();

                    if (prefix == null) {
                        prefix = "";
                    }

                    if (prefix != "") {
                        prefix += ":";
                    }
                }
            }

            writer.write("<");
            writer.write(prefix + localName);

            if (firstElement) {
                printXMLNSDecs(hints);
                firstElement = false;
            }

            if (attributes != null) {
                for (int i = 0; i < attributes.getLength(); i++) {
                    String name = attributes.getLocalName(i);
                    String value = attributes.getValue(i);
                    writer.write(" ");
                    writer.write(name);
                    writer.write("=\"");
                    writer.write(value);
                    writer.write("\"");
                }
            }

            writer.write(">");

            // TODO format here
            //            writer.write("\n");
        }

        /**
         * @see PrintHandler#element(URI, String, Attributes)
         * 
         * @param namespaceURI URI
         * @param localName String
         * @param attributes Attributes
         */
        public void element(URI namespaceURI, String localName,
            Attributes attributes) throws IOException {
            String prefix = (String) prefixMappings.get(namespaceURI);

            if (prefix != null) {
                if (!prefix.equals("")) {
                    prefix = prefix + ":";
                }
            } else {
                if (namespaceURI.equals(schema.getTargetNamespace())) {
                    prefix = "";
                } else {
                	XSISAXHandler.setLogLevel(logger.getLevel());
                    prefix = SchemaFactory.getInstance(namespaceURI).getPrefix();

                    if (prefix == null) {
                        prefix = "";
                    }

                    if (prefix != "") {
                        prefix += ":";
                    }
                }
            }

            writer.write("<");
            writer.write(prefix + localName);

            if (firstElement) {
                printXMLNSDecs(hints);
                firstElement = false;
            }

            if (attributes != null) {
                for (int i = 0; i < attributes.getLength(); i++) {
                    String name = attributes.getLocalName(i);
                    String value = attributes.getValue(i);
                    writer.write(" ");
                    writer.write(name);
                    writer.write("=\"");
                    writer.write(value);
                    writer.write("\"");
                }
            }

            writer.write("/>");

            // TODO format here
            writer.write("\n");
        }

        /**
         * @see PrintHandler#endElement(URI, String)
         * @param namespaceURI URI
         * @param localName String
         */
        public void endElement(URI namespaceURI, String localName)
            throws IOException {
            String prefix = (String) prefixMappings.get(namespaceURI);

            if (prefix != null) {
                if (!prefix.equals("")) {
                    prefix = prefix + ":";
                }
            } else {
                if (namespaceURI.equals(schema.getTargetNamespace())) {
                    prefix = "";
                } else {
                	XSISAXHandler.setLogLevel(logger.getLevel());
                    prefix = SchemaFactory.getInstance(namespaceURI).getPrefix();

                    if (prefix == null) {
                        prefix = "";
                    }

                    if (prefix != "") {
                        prefix += ":";
                    }
                }
            }

            writer.write("</");
            writer.write(prefix + localName);
            writer.write(">");

            // TODO format here
            writer.write("\n");
        }

        /**
         * @see PrintHandler#characters(char[], int, int)
         * @see Writer#write(char[], int, int)
         * @param arg0
         * @param arg1
         * @param arg2
         */
        public void characters(char[] arg0, int arg1, int arg2)
            throws IOException {
            writer.write(arg0, arg1, arg2);
        }

        /**
         * @see PrintHandler#characters(String)
         * @see Writer#write(java.lang.String)
         * @param s String
         */
        public void characters(String s) throws IOException {
            writer.write(s);
        }

        /**
         * @see org.xml.sax.ContentHandler#ignorableWhitespace(char[], int,
         *      int)
         *      @see Writer#write(char[], int, int)
         * @param arg0
         * @param arg1
         * @param arg2
         * @throws IOException
         */
        public void ignorableWhitespace(char[] arg0, int arg1, int arg2)
            throws IOException {
            writer.write(arg0, arg1, arg2);
        }

        /**
         * @see PrintHandler#startDocument()
         */
        public void startDocument() throws IOException {
        	 writer.write("<?xml version=\"1.0\" encoding=\""+encoding+"\"?>");

            // TODO format here
            writer.write("\n");
        }

        /**
         * @see PrintHandler#endDocument()
         */
        public void endDocument() throws IOException {
            // TODO format here
            writer.write("\n");
            writer.flush();
        }

        /**
         * @see PrintHandler#getDocumentSchema()
         */
        public Schema getDocumentSchema() {
            return schema;
        }

        /**
         * @see PrintHandler#getHint(Object)
         */
        public Object getHint(Object key) {
            return hints.get(key);
        }

        /**
         * @see PrintHandler#findElement(Object)
         */
        public Element findElement(Object value) {
            Schema[] searchOrder1;

            try {
                searchOrder1 = getSchemaOrdering();
            } catch (IOException e) {
                logger.warning(e.toString());

                return null;
            }

            for (int i = 0; i < searchOrder1.length; i++) {
                Element[] elems = searchOrder1[i].getElements();

                if (elems != null) {
                    for (int j = 0; j < elems.length; j++)
                        if ((elems[j].getType() != null)
                                && elems[j].getType().canEncode(elems[j],
                                    value, hints)) {
                            return elems[j];
                        }
                }
            }

            return null;
        }

        /**
         * @see PrintHandler#findElement(String)
         */
        public Element findElement(String name) {
            Schema[] searchOrder1;

            try {
                searchOrder1 = getSchemaOrdering();
            } catch (IOException e) {
                logger.warning(e.toString());

                return null;
            }

            for (int i = 0; i < searchOrder1.length; i++) {
                Element[] elems = searchOrder1[i].getElements();

                if (elems != null) {
                    for (int j = 0; j < elems.length; j++)
                        if ((elems[j].getName() != null)
                                && elems[j].getName().equals(name)) {
                            return elems[j];
                        }
                }
            }

            return null;
        }

        private Schema[] getSchemaOrdering() throws IOException {
            if (searchOrder != null) {
                return searchOrder;
            }

            if (((hints == null) || (hints.get(SCHEMA_ORDER) == null))
                    && ((schema.getImports() == null)
                    || (schema.getImports().length == 0))) {
                searchOrder = new Schema[] { schema, };
            } else {
                List so = new LinkedList();

                if ((hints != null) && hints.containsKey(SCHEMA_ORDER)) {
                    Object order = hints.get(SCHEMA_ORDER);
                    List targNS = new LinkedList();
                    targNS.add(schema.getTargetNamespace());

                    if (schema.getImports() != null) {
                        for (int i = 0; i < schema.getImports().length; i++)
                            if (!targNS.contains(
                                        schema.getImports()[i]
                                        .getTargetNamespace())) {
                                targNS.add(schema.getImports()[i]
                                    .getTargetNamespace());
                            }
                    }

                    if (order instanceof Schema[]) {
                        Schema[] sOrder = (Schema[]) order;

                        for (int i = 0; i < sOrder.length; i++) {
                            int nsIndex = targNS.indexOf(sOrder[i]
                                    .getTargetNamespace());

                            if (nsIndex >= 0) { // found
                                so.add(sOrder[i]);
                                targNS.remove(nsIndex);
                            }
                        }
                    } else {
                        String[] stringOrder = (String[]) order;

                        for (int i = 0; i < stringOrder.length; i++) {
                            int nsIndex = targNS.indexOf(stringOrder[i]);

                            try {
                                if (nsIndex >= 0) { // found

                                    URI uri = new URI(stringOrder[i]);
                                    XSISAXHandler.setLogLevel(logger.getLevel());
                                    so.add(SchemaFactory.getInstance(uri));
                                    targNS.remove(nsIndex);
                                } else {
                                    URI uri = new URI(stringOrder[i]);
                                    so.add(SchemaFactory.getInstance(uri));
                                }
                            } catch (URISyntaxException e) {
                                logger.warning(e.toString());
                                IOException t = new IOException(e.toString());
                                t.initCause(e);
                                throw t;
                            }
                        }
                    }

                    if (!targNS.contains(schema.getTargetNamespace())) {
                        so.add(schema);
                    }

                    for (int i = 0; i < schema.getImports().length; i++) {
                        int nsIndex = targNS.indexOf(schema.getImports()[i]
                                .getTargetNamespace());

                        if (nsIndex >= 0) { // found
                            so.add(schema.getImports()[i]);
                            targNS.remove(nsIndex);
                        }
                    }
                } else {
                    if ((hints != null) && hints.containsKey(USE_NEAREST)) {
                        // TODO fill this in
                        so.add(schema);

                        for (int i = 0; i < schema.getImports().length; i++)
                            so.add(schema.getImports()[i]);
                    } else {
                        so.add(schema);

                        for (int i = 0; i < schema.getImports().length; i++)
                            so.add(schema.getImports()[i]);
                    }
                }

                searchOrder = (Schema[]) so.toArray(new Schema[so.size()]);
            }

            return searchOrder;
        }
    }
}
