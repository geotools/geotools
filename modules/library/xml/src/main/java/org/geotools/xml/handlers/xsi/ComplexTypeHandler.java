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
package org.geotools.xml.handlers.xsi;

import java.io.IOException;
import java.net.URI;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.naming.OperationNotSupportedException;

import org.geotools.xml.PrintHandler;
import org.geotools.xml.XSIElementHandler;
import org.geotools.xml.schema.All;
import org.geotools.xml.schema.Attribute;
import org.geotools.xml.schema.AttributeGroup;
import org.geotools.xml.schema.Choice;
import org.geotools.xml.schema.ComplexType;
import org.geotools.xml.schema.Element;
import org.geotools.xml.schema.ElementGrouping;
import org.geotools.xml.schema.ElementValue;
import org.geotools.xml.schema.Group;
import org.geotools.xml.schema.Sequence;
import org.geotools.xml.schema.SimpleType;
import org.geotools.xml.schema.Type;
import org.geotools.xml.schema.impl.SimpleTypeGT;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.SAXNotRecognizedException;
import org.xml.sax.SAXNotSupportedException;


/**
 * ComplexTypeHandler purpose.
 * 
 * <p>
 * Represents a ComplexType element
 * </p>
 * <p>
 * When a specific method of encoding is not specified then the following output will be returned:
 * 
 * ElementValue[]{(null,Attributes),(Element,Value)*,(null,String)?}
 * Where the last element will be included iff there is child text. 
 * </p>
 *
 * @author dzwiers, Refractions Research, Inc. http://www.refractions.net
 * @author $Author:$ (last modification)
 * @source $URL$
 * @version $Id$
 */
public class ComplexTypeHandler extends XSIElementHandler {
    private static final long serialVersionUID = -2001189506633342497L;

    /** 'complexType' */
    public final static String LOCALNAME = "complexType";

    /** ALL */
    public static final int ALL = 128;
    private static int offset = 0;
    private String id;
    private String name;
    private boolean abstracT;
    private boolean mixed;
    private int block;
    private int finaL;
    private List attrDecs = new LinkedList(); // attr or attrGrps
    private AnyAttributeHandler anyAttribute;
    private Object child; //should be either a ComplexType or a SimpleType,
    private int hashCodeOffset = getOffset();
    private ComplexType cache = null;

    /*
     * helper for hashCode();
     */
    private static int getOffset() {
        return offset++;
    }

    /**
     * @see java.lang.Object#hashCode()
     */
    public int hashCode() {
        return (LOCALNAME.hashCode() * ((id == null) ? 1 : id.hashCode()) * ((attrDecs == null)
        ? 1 : attrDecs.hashCode()) * ((name == null) ? 1 : name.hashCode()))
        + hashCodeOffset;
    }

    /**
     * @see org.geotools.xml.XSIElementHandler#getHandler(java.lang.String,
     *      java.lang.String)
     */
    public XSIElementHandler getHandler(String namespaceURI, String localName)
        throws SAXException {
        if (SchemaHandler.namespaceURI.equalsIgnoreCase(namespaceURI)) {
            // child types
            //
            // group
            if (GroupHandler.LOCALNAME.equalsIgnoreCase(localName)) {
                GroupHandler sth = new GroupHandler();

                if (child == null) {
                    child = sth;
                } else {
                    throw new SAXNotRecognizedException(LOCALNAME
                        + " may only have one child declaration.");
                }

                return sth;
            }

            // all
            if (AllHandler.LOCALNAME.equalsIgnoreCase(localName)) {
                AllHandler sth = new AllHandler();

                if (child == null) {
                    child = sth;
                } else {
                    throw new SAXNotRecognizedException(LOCALNAME
                        + " may only have one child declaration.");
                }

                return sth;
            }

            // choice
            if (ChoiceHandler.LOCALNAME.equalsIgnoreCase(localName)) {
                ChoiceHandler sth = new ChoiceHandler();

                if (child == null) {
                    child = sth;
                } else {
                    throw new SAXNotRecognizedException(LOCALNAME
                        + " may only have one child declaration.");
                }

                return sth;
            }

            // sequence
            if (SequenceHandler.LOCALNAME.equalsIgnoreCase(localName)) {
                SequenceHandler sth = new SequenceHandler();

                if (child == null) {
                    child = sth;
                } else {
                    throw new SAXNotRecognizedException(LOCALNAME
                        + " may only have one child declaration.");
                }

                return sth;
            }

            // attribute
            if (AttributeHandler.LOCALNAME.equalsIgnoreCase(localName)) {
                if (attrDecs == null) {
                    attrDecs = new LinkedList();
                }

                AttributeHandler ah = new AttributeHandler();
                attrDecs.add(ah);

                return ah;
            }

            // attributeGroup
            if (AttributeGroupHandler.LOCALNAME.equalsIgnoreCase(localName)) {
                if (attrDecs == null) {
                    attrDecs = new LinkedList();
                }

                AttributeGroupHandler ah = new AttributeGroupHandler();
                attrDecs.add(ah);

                return ah;
            }

            // anyAttribute
            if (AnyAttributeHandler.LOCALNAME.equalsIgnoreCase(localName)) {
                AnyAttributeHandler sth = new AnyAttributeHandler();

                if (anyAttribute == null) {
                    anyAttribute = sth;
                } else {
                    throw new SAXNotRecognizedException(LOCALNAME
                        + " may only have one child declaration.");
                }

                return sth;
            }

            // complexContent
            if (ComplexContentHandler.LOCALNAME.equalsIgnoreCase(localName)) {
                ComplexContentHandler sth = new ComplexContentHandler();

                if (child == null) {
                    child = sth;
                } else {
                    throw new SAXNotRecognizedException(LOCALNAME
                        + " may only have one child declaration.");
                }

                return sth;
            }

            // simpleContent
            if (SimpleContentHandler.LOCALNAME.equalsIgnoreCase(localName)) {
                SimpleContentHandler sth = new SimpleContentHandler();

                if (child == null) {
                    child = sth;
                } else {
                    throw new SAXNotRecognizedException(LOCALNAME
                        + " may only have one child declaration.");
                }

                return sth;
            }
        }

        return null;
    }

    /**
     * @see org.geotools.xml.XSIElementHandler#startElement(java.lang.String,
     *      java.lang.String, org.xml.sax.Attributes)
     */
    public void startElement(String namespaceURI, String localName,
        Attributes atts) throws SAXException {
        id = atts.getValue("", "id");

        if (id == null) {
            id = atts.getValue(namespaceURI, "id");
        }

        name = atts.getValue("", "name");

        if (name == null) {
            name = atts.getValue(namespaceURI, "name");
        }

        String abstracT1 = atts.getValue("", "abstract");

        if (abstracT1 == null) {
            abstracT1 = atts.getValue(namespaceURI, "abstract");
        }

        if ((abstracT1 == null) || "".equals(abstracT1)) {
            this.abstracT = false;
        } else {
            this.abstracT = Boolean.valueOf(abstracT1).booleanValue();
        }

        String block1 = atts.getValue("", "block");

        if (block1 == null) {
            block1 = atts.getValue(namespaceURI, "block");
        }

        this.block = ComplexTypeHandler.findBlock(block1);

        String finaL1 = atts.getValue("", "final");

        if (finaL1 == null) {
            finaL1 = atts.getValue(namespaceURI, "final");
        }

        this.finaL = ComplexTypeHandler.findFinal(finaL1);

        String mixed1 = atts.getValue("", "mixed");

        if (mixed1 == null) {
            mixed1 = atts.getValue(namespaceURI, "mixed");
        }

        if ((mixed1 == null) || "".equalsIgnoreCase(mixed1)) {
            this.mixed = false;
        } else {
            this.mixed = Boolean.getBoolean(mixed1);
        }

        this.block = findBlock(block1);
        this.finaL = findFinal(finaL1);
    }

    /**
     * @see org.geotools.xml.XSIElementHandler#getLocalName()
     */
    public String getLocalName() {
        return LOCALNAME;
    }

    // Group, Sequence, All Choice

    /**
     * <p>
     * Converts a 'block' attribute value into an int mask
     * </p>
     *
     * @param block block
     *
     * @return int
     *
     * @throws SAXException
     */
    public static int findBlock(String block) throws SAXException {
        if ((block == null) || "".equalsIgnoreCase(block)) {
            return DEFAULT;
        }

        if ("extension".equalsIgnoreCase(block)) {
            return EXTENSION;
        }

        if ("restriction".equalsIgnoreCase(block)) {
            return RESTRICTION;
        }

        if ("#all".equalsIgnoreCase(block)) {
            return ALL;
        }

        throw new SAXException("Unknown Block Type: '" + block + "'");
    }

    /**
     * <p>
     * Reverses the translation from mask to String
     * </p>
     *
     * @param block
     *
     */
    public static String writeBlock(int block) {
        switch (block) {
        case EXTENSION:
            return "extension";

        case RESTRICTION:
            return "restriction";

        case ALL:
            return "#all";

        default:
            return "";
        }
    }

    /**
     * <p>
     * Converts a 'final' attribute value to an int mask
     * </p>
     *
     * @param finaL
     *
     *
     * @throws SAXException
     */
    public static int findFinal(String finaL) throws SAXException {
        try {
            return findBlock(finaL); // same enum
        } catch (SAXException e) {
            throw new SAXException("Unknown Final Type: '" + finaL + "'");
        }
    }

    /**
     * <p>
     * reverses the conversion of an int mask representing the 'final'
     * attribute to String
     * </p>
     *
     * @param finaL
     *
     */
    public static String writeFinal(int finaL) {
        return writeBlock(finaL); // same enum
    }

    /**
     * <p>
     * returns the complexType's name
     * </p>
     *
     */
    public String getName() {
        return name;
    }

    /**
     * <p>
     * compresses the inheritance tree, caching a more efficient copy.
     * </p>
     *
     * @param parent
     *
     *
     * @throws SAXException
     * @throws NullPointerException
     */
    protected ComplexType compress(SchemaHandler parent)
        throws SAXException {
        logger.fine("Start compressing ComplexType " + getName());

        if (cache != null) {
            return cache;
        }

        DefaultComplexType dct = new DefaultComplexType();
        dct.abstracT = abstracT;
        dct.anyAttributeNameSpace = (anyAttribute != null)
            ? anyAttribute.getNamespace() : null;

        HashSet attr = new HashSet();

        if (child instanceof SimpleContentHandler
                || child instanceof ComplexContentHandler) {
            if (child instanceof SimpleContentHandler) {
                logger.finest("SimpleContentHandler");

                SimpleContentHandler sch = (SimpleContentHandler) child;

                if (sch.getChild() instanceof ExtensionHandler) {
                    ExtensionHandler ext = (ExtensionHandler) sch.getChild();

                    // attributes
                    if (ext.getAttributeDeclarations() != null) {
                        Iterator it = ext.getAttributeDeclarations().iterator();

                        while (it.hasNext()) {
                            Object o = it.next();

                            if (o instanceof AttributeHandler) {
                                AttributeHandler ah = (AttributeHandler) o;
                                attr.add(ah.compress(parent));
                            } else {
                                AttributeGroupHandler agh = (AttributeGroupHandler) o;
                                AttributeGroup ag = agh.compress(parent);
                                attr.addAll(Arrays.asList(ag.getAttributes()));
                            }
                        }
                    }

                    SimpleType st;

                    if ((ext.getBase() == null)
                            || ext.getBase().equalsIgnoreCase("")) {
                        st = ((SimpleTypeHandler) ext.getChild()).compress(parent);
                    } else {
                        st = parent.lookUpSimpleType(ext.getBase());
                    }

                    dct.parent = st;
                    dct.simple = true;
                } else {
                    // restriction
                    RestrictionHandler rest = (RestrictionHandler) sch.getChild();

                    // attributes
                    if (rest.getAttributeDeclarations() != null) {
                        Iterator it = rest.getAttributeDeclarations().iterator();

                        while (it.hasNext()) {
                            Object o = it.next();

                            if (o instanceof AttributeHandler) {
                                AttributeHandler ah = (AttributeHandler) o;
                                attr.add(ah.compress(parent));
                            } else {
                                AttributeGroupHandler agh = (AttributeGroupHandler) o;
                                AttributeGroup ag = agh.compress(parent);
                                attr.addAll(Arrays.asList(ag.getAttributes()));
                            }
                        }
                    }

                    SimpleType st = new SimpleTypeGT(id, name,
                            parent.getTargetNamespace(),
                            SimpleType.RESTRICTION,
                            SimpleTypeHandler.getSimpleTypes(rest, parent),
                            SimpleTypeHandler.getFacets(rest), finaL);
                    dct.parent = st;
                    dct.simple = true;
                }

                dct.mixed = true;
            } else {
                // ComplexContentHandler
                // TODO deal with these as a special case to incure call backs in the heiarchy
                ComplexContentHandler cch = (ComplexContentHandler) child;

                if (cch.getChild() instanceof ExtensionHandler) {
                    ExtensionHandler ext = (ExtensionHandler) cch.getChild();

                    ComplexType ct = parent.lookUpComplexType(ext.getBase());
                    dct.parent = ct;

                    // attributes
                    if (ct!=null && ct.getAttributes() != null) {
                        Attribute[] it = ct.getAttributes();

                        for (int i = 0; i < it.length; i++) {
                            attr.add(it[i]);
                        }
                    }

                    if (ext.getAttributeDeclarations() != null) {
                        Iterator it = ext.getAttributeDeclarations().iterator();

                        while (it.hasNext()) {
                            Object o = it.next();

                            if (o instanceof AttributeHandler) {
                                AttributeHandler ah = (AttributeHandler) o;
                                attr.add(ah.compress(parent));
                            } else {
                                AttributeGroupHandler agh = (AttributeGroupHandler) o;
                                AttributeGroup ag = agh.compress(parent);
                                attr.addAll(Arrays.asList(ag.getAttributes()));
                            }
                        }
                    }

                    if (ct!=null && ext.getChild() != null) {
                        logger.finest("Looked up " + ext.getBase()
                            + " and found "
                            + ((ct == null) ? null
                                            : (ct.getName() + ":::"
                            + ct.getNamespace())) + " for " + name);

                        ElementGrouping extensionBaseType = ct.getChild();
						ElementGrouping extensionChild =  ((ElementGroupingHandler)ext.getChild()).compress(parent);
                        dct.child = loadNewEG(extensionBaseType,extensionChild, parent); // note should override element def only ... not spot
                    } else {
                    	if (ct != null) 
                    		dct.child = ct.getChild();
                    }
                } else {
                    //restriction
                    RestrictionHandler ext = (RestrictionHandler) cch.getChild();

                    // attributes
                    if (ext.getAttributeDeclarations() != null) {
                        Iterator it = ext.getAttributeDeclarations().iterator();

                        while (it.hasNext()) {
                            Object o = it.next();

                            if (o instanceof AttributeHandler) {
                                AttributeHandler ah = (AttributeHandler) o;
                                attr.add(ah.compress(parent));
                            } else {
                                AttributeGroupHandler agh = (AttributeGroupHandler) o;
                                AttributeGroup ag = agh.compress(parent);
                                attr.addAll(Arrays.asList(ag.getAttributes()));
                            }
                        }
                    }

                    if (ext.getChild() == null) {
                        dct.child = null; // empty child
                    } else {
                        dct.child = ((ElementGroupingHandler) ext.getChild())
                            .compress(parent);
                    }

                    dct.parent = parent.lookUpComplexType(ext.getBase());
                }

                if (dct.child == null) {
                    dct.child = new DefaultSequence();
                }

                dct.isDerived = true;
            }
        } else {
            // one of Choice, Group, Sequence, All
            // attributes
            if (attrDecs != null) {
                Iterator it = attrDecs.iterator();

                while (it.hasNext()) {
                    Object o = it.next();

                    if (o instanceof AttributeHandler) {
                        AttributeHandler ah = (AttributeHandler) o;
                        attr.add(ah.compress(parent));
                    } else {
                        AttributeGroupHandler agh = (AttributeGroupHandler) o;
                        AttributeGroup ag = agh.compress(parent);
                        attr.addAll(Arrays.asList(ag.getAttributes()));
                    }
                }
            }

            if (child != null) {
                dct.child = ((ElementGroupingHandler) child).compress(parent);
            } else {
                dct.child = new DefaultSequence();
            }
        }

        dct.attributes = (Attribute[]) attr.toArray(new Attribute[attr.size()]);
        dct.namespace = parent.getTargetNamespace();
        dct.block = block;
        dct.finaL = finaL;
        dct.id = id;

        if (!dct.mixed) { // simpleContent flag ...
            dct.mixed = mixed;
        }

        dct.name = name;
        cache = dct;

        if (((cache.getChild() == null) && !abstracT) && !dct.simple) {
            logger.warning(getName() + " :: " + parent.getTargetNamespace()
                + " should have a real child: ");
            throw new NullPointerException();
        }

        if ((dct.parent == null) && dct.simple) {
            logger.warning(getName() + " :: " + parent.getTargetNamespace()
                + " should have a real parent: ");
            throw new NullPointerException();
        }

        logger.fine("End compressing ComplexType " + getName());

        attrDecs = null;
        anyAttribute = null;
        child = null;

        return cache;
    }

    /*
     * Helper method that removes a level of indirection through combining
     * the levels into more compact representations
     */
    private ElementGrouping loadNewEG(ElementGrouping extensionBaseType, ElementGrouping extensionChild,
        SchemaHandler parent) throws SAXException {
        if (extensionChild == null) {

            if (extensionBaseType.getGrouping() == ElementGrouping.GROUP) {
                return ((Group) extensionBaseType).getChild();
            }

            return extensionBaseType;
        }

        switch (extensionBaseType.getGrouping()) {

        case ElementGrouping.CHOICE:
            logger.finest("ElementGrouping eg is CHOICE in loadNewEG");

                return new DefaultSequence((Choice) extensionBaseType, extensionChild);
                
        case ElementGrouping.GROUP:
            logger.finest("ElementGrouping eg is GROUP in loadNewEG");

                Group baseGroup = (Group) extensionBaseType;

                if (baseGroup.getChild() == null) {
                    return extensionChild;
                }
                return loadNewEG(baseGroup.getChild(),extensionChild,parent);

        case ElementGrouping.SEQUENCE:
            logger.finest("ElementGrouping eg is SEQUENCE");
                return new DefaultSequence((Sequence) extensionBaseType,extensionChild);

        default:
            return extensionBaseType;
        }
    }

    /**
     * @see org.geotools.xml.XSIElementHandler#getHandlerType()
     */
    public int getHandlerType() {
        return DEFAULT;
    }

    /**
     * @see org.geotools.xml.XSIElementHandler#endElement(java.lang.String,
     *      java.lang.String)
     */
    public void endElement(String namespaceURI, String localName){
        // do nothing
    }

    /**
     * <p>
     * implementation of a default sequence with some  extra logic in the
     * constructors.
     * </p>
     *
     * @author dzwiers
     *
     * @see Sequence
     */
    private static class DefaultSequence implements Sequence {
        ElementGrouping[] children;
        String id;
        int maxOccurs;
        int minOccurs;

        /*
         * Should not be called
         */
        private DefaultSequence() {
            // do nothing
        }

        /**
         * <p>
         * Combines the specified Sequence with the element grouping into  a
         * new Sequence
         * </p>
         *
         * @param sequence
         * @param eg
         */
        public DefaultSequence(Sequence sequence, ElementGrouping eg) {
            logger.finest("merging sequence with an ElementGrouping");
            id = sequence.getId();
            maxOccurs = sequence.getMaxOccurs();
            minOccurs = sequence.getMinOccurs();

            if (sequence.getChildren() == null) {
                logger.finest("Sequence children are null");
                children = new ElementGrouping[1];
                children[0] = eg;

                return;
            }

            if (eg.getGrouping() == ElementGrouping.SEQUENCE) {
                logger.finest("Two sequences being merged");

                Sequence sq2 = (Sequence) eg;
                children = new ElementGrouping[sequence.getChildren().length
                    + sq2.getChildren().length];
                logger.finest("There are a total of " + children.length
                    + " Children");

                for (int i = 0; i < sequence.getChildren().length; i++) {
                    children[i] = sequence.getChildren()[i];
                }

                for (int i = 0; i < sq2.getChildren().length; i++) {
                    children[sequence.getChildren().length + i] = sq2
                        .getChildren()[i];
                }
            } else {
                children = new ElementGrouping[sequence.getChildren().length
                    + 1];
                logger.finest("There are a total of " + children.length
                    + " Children");

                for (int i = 0; i < sequence.getChildren().length; i++)
                    children[i] = sequence.getChildren()[i];

                children[sequence.getChildren().length] = eg;
            }
        }

        /**
         * <p>
         * Combines the Choice with the ElementGrouping to form a new Sequence
         * </p>
         *
         * @param sequence
         * @param eg
         */
        public DefaultSequence(Choice sequence, ElementGrouping eg) {
            id = sequence.getId();
            maxOccurs = sequence.getMaxOccurs();
            minOccurs = sequence.getMinOccurs();

            if (sequence.getChildren() == null) {
                children = new ElementGrouping[1];
                children[0] = eg;

                return;
            }

            children = new ElementGrouping[2];
            children[0] = sequence;
            children[1] = eg;
        }

        /**
         * @see org.geotools.xml.xsi.Sequence#getChildren()
         */
        public ElementGrouping[] getChildren() {
            return children;
        }

        /**
         * @see org.geotools.xml.xsi.Sequence#getId()
         */
        public String getId() {
            return id;
        }

        /**
         * @see org.geotools.xml.xsi.ElementGrouping#getMaxOccurs()
         */
        public int getMaxOccurs() {
            return maxOccurs;
        }

        /**
         * @see org.geotools.xml.xsi.ElementGrouping#getMinOccurs()
         */
        public int getMinOccurs() {
            return minOccurs;
        }

        /**
         * @see org.geotools.xml.xsi.ElementGrouping#getGrouping()
         */
        public int getGrouping() {
            return SEQUENCE;
        }

        /**
         * @see org.geotools.xml.xsi.ElementGrouping#findChildElement(java.lang.String)
         */
        public Element findChildElement(String name) {
            if (children == null) {
                return null;
            }
            for (int i = 0; i < children.length; i++) {
                Element t = children[i].findChildElement(name);
                if (t != null) { // found it

                    return t;
                }
            }

            return null;
        }

		public Element findChildElement(String localName, URI namespaceURI) {
			if (children == null) {
                return null;
            }
            for (int i = 0; i < children.length; i++) {
                Element t = children[i].findChildElement(localName,namespaceURI);
                if (t != null) { // found it

                    return t;
                }
            }

            return null;
		}
    }

    /**
     * <p>
     * Default implementation of a ComplexType
     * </p>
     *
     * @author dzwiers
     *
     * @see ComplexType
     */
    private static class DefaultComplexType implements ComplexType {
        boolean isDerived = false;
        Type parent;
        String anyAttributeNameSpace;
        URI namespace;
        String id;
        String name;
        int block;
        int finaL;
        ElementGrouping child;
        boolean simple = false;
        Attribute[] attributes;
        boolean abstracT;
        boolean mixed;

        public Element[] getChildElements() {
            if (child == null) {
                return null;
            }

            return getChildElements(child);
        }

        public Element[] getChildElements(ElementGrouping child11) {
        	if(child11 == null)
        		return new Element[0];
            switch (child11.getGrouping()) {
            case ElementGrouping.ALL:
                return ((All) child11).getElements();

            case ElementGrouping.ANY:
                return null;

            case ElementGrouping.CHOICE:

                ElementGrouping[] children = ((Choice) child11).getChildren();
                List l = new LinkedList();

                for (int i = 0; i < children.length; i++) {
                    Element[] t = getChildElements(children[i]);

                    if (t != null) {
                        l.addAll(Arrays.asList(t));
                    }
                }

                return (l.size() > 0)
                ? (Element[]) l.toArray(new Element[l.size()]) : null;

            case ElementGrouping.ELEMENT:
                return new Element[] { (Element) child11, };

            case ElementGrouping.GROUP:

                ElementGrouping c = ((Group) child11).getChild();
                if(c == null)
                	return new Element[0];
                return getChildElements(c);

            case ElementGrouping.SEQUENCE:
                children = ((Sequence) child11).getChildren();
                l = new LinkedList();
                if(children!=null){
                for (int i = 0; i < children.length; i++) {
                    Element[] t = getChildElements(children[i]);

                    if (t != null) {
                        l.addAll(Arrays.asList(t));
                    }
                }}

                return (l.size() > 0)
                ? (Element[]) l.toArray(new Element[l.size()]) : null;
            }

            return null;
        }

        /**
         * @see org.geotools.xml.schema.ComplexType#cache()
         */
        public boolean cache(Element e, Map m) {
            return true;
        }

        /**
         * @see org.geotools.xml.xsi.Type#getNamespace()
         */
        public URI getNamespace() {
            return namespace;
        }

        /**
         * @see org.geotools.xml.xsi.Type#getParent()
         */
        public Type getParent() {
            return parent;
        }

        /**
         * @see org.geotools.xml.xsi.ComplexType#isDerived()
         */
        public boolean isDerived() {
            return isDerived;
        }

        /**
         * @see org.geotools.xml.xsi.ComplexType#getAttributeDescriptors()
         */
        public Attribute[] getAttributes() {
            return attributes;
        }

        /**
         * @see org.geotools.xml.xsi.ComplexType#isAbstract()
         */
        public boolean isAbstract() {
            return abstracT;
        }

        /**
         * @see org.geotools.xml.xsi.ComplexType#getBlock()
         */
        public int getBlock() {
            return block;
        }

        /**
         * @see org.geotools.xml.xsi.ComplexType#getChild()
         */
        public ElementGrouping getChild() {
            return child;
        }

        /**
         * @see org.geotools.xml.xsi.ComplexType#getFinal()
         */
        public int getFinal() {
            return finaL;
        }

        /**
         * @see org.geotools.xml.xsi.ComplexType#isMixed()
         */
        public boolean isMixed() {
            return mixed;
        }

        /**
         * @see org.geotools.xml.xsi.Type#getLocalName()
         */
        public String getName() {
            return name;
        }

        /**
         * @see org.geotools.xml.xsi.ComplexType#getAnyAttributeNameSpace()
         */
        public String getAnyAttributeNameSpace() {
            return anyAttributeNameSpace;
        }

        /**
         * @see org.geotools.xml.xsi.ComplexType#getId()
         */
        public String getId() {
            return id;
        }

        /**
         * @throws SAXException
         * @throws OperationNotSupportedException
         * @see org.geotools.xml.xsi.Type#getValue(org.geotools.xml.xsi.Element,
         *      org.geotools.xml.xsi.ElementValue[], org.xml.sax.Attributes)
         */
        public Object getValue(Element element, ElementValue[] value,
            final Attributes attrs, Map hints) throws OperationNotSupportedException, SAXException {
            Object[] values = null;

            logger.finest("Getting value for " + name);

            if (isDerived || simple) {
                try {
                    Object v = parent.getValue(element, value, attrs, hints);

                    return v; // this means it's meant to be extended ... and so should already include the extensions.
                } catch (SAXNotSupportedException snse) {
                    logger.finest(snse.toString());

                    // do nothing ... except pretend it is not derived
                }
            }

            if (simple) {
                return null;
            }

            if( element.getType() instanceof ComplexType
            		&& ((ComplexType)element.getType()).getChild() instanceof Choice){
            	if( value.length>0)
            		return value[0].getValue();
            }else{
	            values = new Object[value.length+1];
	            logger.finest("Getting value for " + element.getName() + ":" + name);
	
	            // This seems to some how be for mixed content?  Don't really understand what
	            // is going on here.
	            values[0] = new ElementValue(){
					public Element getElement() {
						return null;
					}
	
					public Object getValue() {
						return attrs;
					}
	            	
	            };
	            
	            
	            for (int i = 1; i < value.length+1; i++) {
	                values[i] = value[i-(isMixed()?0:1)].getValue();
	                logger.finest("*"
	                    + ((values[i] != null) ? values[i].getClass().getName()
	                                           : "null"));
	            }
	            
	            if(isMixed())
	            	values[values.length-1] = value[0];

            }
            return values;
        }

        /**
         * @see org.geotools.xml.xsi.Type#getInstanceType()
         */
        public Class getInstanceType() {
            return Object[].class;
        }

        /**
         * @see org.geotools.xml.xsi.ComplexType#findChildElement(java.lang.String)
         */
        public Element findChildElement(String name1) {
            Element e = (child == null) ? null : child.findChildElement(name1);
            e = e==null?(parent==null?null:parent.findChildElement(name1)):e;
            return e;
        }

        /**
         * @see org.geotools.xml.schema.Type#canEncode(org.geotools.xml.schema.Element,
         *      java.lang.Object, java.util.Map)
         */
        public boolean canEncode(Element element, Object value, Map hints) {
            if ((parent != null) && parent.canEncode(element, value, hints)) {
                return true;
            }

            return false;
        }

        /**
         * @see org.geotools.xml.schema.Type#encode(org.geotools.xml.schema.Element,
         *      java.lang.Object, org.geotools.xml.PrintHandler,
         *      java.util.Map)
         */
        public void encode(Element element, Object value, PrintHandler output,
            Map hints) throws IOException, OperationNotSupportedException {
            if ((parent != null) && parent.canEncode(element, value, hints)) {
                parent.encode(element, value, output, hints);
            } else {
                output.startElement(element.getNamespace(), element.getName(), null);
                Type type=element.getType();
                if( type instanceof SimpleType ){
                    SimpleType simple=(SimpleType) type;
                    simple.encode(element, value, output, hints);
                }else if (type instanceof ComplexType) {
                    ComplexType complex = (ComplexType) type;
                    Element[] children = complex.getChildElements();
                    boolean found=false;
                    for (int i = 0; i < children.length; i++) {
                        Element child = children[i];
                        if( child.getType().canEncode(child, value, hints) ){
                            child.getType().encode(child, value, output, hints);
                            found=true;
                        }
                    }
                    if( !found )
                        throw new RuntimeException(
                        "It is not known how to print this element");
                }else{
                    throw new OperationNotSupportedException(
                        "It is not known how to print this element");
                }
                output.endElement(element.getNamespace(), element.getName());
            }
        }
    }
}
