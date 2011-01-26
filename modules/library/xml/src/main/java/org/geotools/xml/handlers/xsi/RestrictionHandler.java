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

import java.util.LinkedList;
import java.util.List;

import org.geotools.xml.XSIElementHandler;
import org.geotools.xml.schema.Facet;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.SAXNotRecognizedException;


/**
 * RestrictionHandler purpose.
 * 
 * <p>
 * Represents a restriction element
 * </p>
 *
 * @author dzwiers, Refractions Research, Inc. http://www.refractions.net
 * @author $Author:$ (last modification)
 * @source $URL$
 * @version $Id$
 */
public class RestrictionHandler extends XSIElementHandler {
    /** 'restriction' */
    public final static String LOCALNAME = "restriction";
    private String id;
    private String base;
    private Object child;
    private List constraints;
    private List attrDecs;
    private AnyAttributeHandler anyAttribute;

    /**
     * @see java.lang.Object#hashCode()
     */
    public int hashCode() {
        return LOCALNAME.hashCode() * ((id == null) ? 1 : id.hashCode()) * ((base == null)
        ? 1 : base.hashCode());
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
            // simpleType
            if (SimpleTypeHandler.LOCALNAME.equalsIgnoreCase(localName)) {
                SimpleTypeHandler sch = new SimpleTypeHandler();

                if (child == null) {
                    child = sch;
                } else {
                    throw new SAXNotRecognizedException(getLocalName()
                        + " may only have one '" + SimpleTypeHandler.LOCALNAME
                        + "' declaration.");
                }

                return sch;
            }

            // enumeration
            if (EnumerationHandler.LOCALNAME.equalsIgnoreCase(localName)) {
                if (constraints == null) {
                    constraints = new LinkedList();
                }

                EnumerationHandler eh = new EnumerationHandler();
                constraints.add(eh);

                return eh;
            }

            // fractionDigits
            if (FractionDigitsHandler.LOCALNAME.equalsIgnoreCase(localName)) {
                if (constraints == null) {
                    constraints = new LinkedList();
                }

                FractionDigitsHandler eh = new FractionDigitsHandler();
                constraints.add(eh);

                return eh;
            }

            // length
            if (LengthHandler.LOCALNAME.equalsIgnoreCase(localName)) {
                if (constraints == null) {
                    constraints = new LinkedList();
                }

                LengthHandler eh = new LengthHandler();
                constraints.add(eh);

                return eh;
            }

            // minInclusive
            if (MinInclusiveHandler.LOCALNAME.equalsIgnoreCase(localName)) {
                if (constraints == null) {
                    constraints = new LinkedList();
                }

                MinInclusiveHandler eh = new MinInclusiveHandler();
                constraints.add(eh);

                return eh;
            }

            // maxInclusive
            if (MaxInclusiveHandler.LOCALNAME.equalsIgnoreCase(localName)) {
                if (constraints == null) {
                    constraints = new LinkedList();
                }

                MaxInclusiveHandler eh = new MaxInclusiveHandler();
                constraints.add(eh);

                return eh;
            }

            // minExclusive
            if (MinExclusiveHandler.LOCALNAME.equalsIgnoreCase(localName)) {
                if (constraints == null) {
                    constraints = new LinkedList();
                }

                MinExclusiveHandler eh = new MinExclusiveHandler();
                constraints.add(eh);

                return eh;
            }

            // maxExclusive
            if (MaxExclusiveHandler.LOCALNAME.equalsIgnoreCase(localName)) {
                if (constraints == null) {
                    constraints = new LinkedList();
                }

                MaxExclusiveHandler eh = new MaxExclusiveHandler();
                constraints.add(eh);

                return eh;
            }

            // maxLength
            if (MaxLengthHandler.LOCALNAME.equalsIgnoreCase(localName)) {
                if (constraints == null) {
                    constraints = new LinkedList();
                }

                MaxLengthHandler eh = new MaxLengthHandler();
                constraints.add(eh);

                return eh;
            }

            // minLength
            if (MinLengthHandler.LOCALNAME.equalsIgnoreCase(localName)) {
                if (constraints == null) {
                    constraints = new LinkedList();
                }

                MinLengthHandler eh = new MinLengthHandler();
                constraints.add(eh);

                return eh;
            }

            // pattern
            if (PatternHandler.LOCALNAME.equalsIgnoreCase(localName)) {
                if (constraints == null) {
                    constraints = new LinkedList();
                }

                PatternHandler eh = new PatternHandler();
                constraints.add(eh);

                return eh;
            }

            // totalDigits
            if (TotalDigitsHandler.LOCALNAME.equalsIgnoreCase(localName)) {
                if (constraints == null) {
                    constraints = new LinkedList();
                }

                TotalDigitsHandler eh = new TotalDigitsHandler();
                constraints.add(eh);

                return eh;
            }

            // all
            if (AllHandler.LOCALNAME.equalsIgnoreCase(localName)) {
                AllHandler sch = new AllHandler();

                if (child == null) {
                    child = sch;
                } else {
                    throw new SAXNotRecognizedException(getLocalName()
                        + " may only have one '" + AllHandler.LOCALNAME
                        + "' declaration.");
                }

                return sch;
            }

            // choice
            if (ChoiceHandler.LOCALNAME.equalsIgnoreCase(localName)) {
                ChoiceHandler sch = new ChoiceHandler();

                if (child == null) {
                    child = sch;
                } else {
                    throw new SAXNotRecognizedException(getLocalName()
                        + " may only have one '" + ChoiceHandler.LOCALNAME
                        + "' declaration.");
                }

                return sch;
            }

            // group
            if (GroupHandler.LOCALNAME.equalsIgnoreCase(localName)) {
                GroupHandler sch = new GroupHandler();

                if (child == null) {
                    child = sch;
                } else {
                    throw new SAXNotRecognizedException(getLocalName()
                        + " may only have one '" + GroupHandler.LOCALNAME
                        + "' declaration.");
                }

                return sch;
            }

            // sequence
            if (SequenceHandler.LOCALNAME.equalsIgnoreCase(localName)) {
                SequenceHandler sch = new SequenceHandler();

                if (child == null) {
                    child = sch;
                } else {
                    throw new SAXNotRecognizedException(getLocalName()
                        + " may only have one '" + SequenceHandler.LOCALNAME
                        + "' declaration.");
                }

                return sch;
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
                AnyAttributeHandler sch = new AnyAttributeHandler();

                if (anyAttribute == null) {
                    anyAttribute = sch;
                } else {
                    throw new SAXNotRecognizedException(getLocalName()
                        + " may only have one '"
                        + AnyAttributeHandler.LOCALNAME + "' declaration.");
                }

                return sch;
            }
        }

        return null;
    }

    /**
     * @see org.geotools.xml.XSIElementHandler#startElement(java.lang.String,
     *      java.lang.String, org.xml.sax.Attributes)
     */
    public void startElement(String namespaceURI, String localName,
        Attributes atts) {
        id = atts.getValue("", "id");

        if (id == null) {
            id = atts.getValue(namespaceURI, "id");
        }

        base = atts.getValue("", "base");

        if (base == null) {
            base = atts.getValue(namespaceURI, "base");
        }
    }

    /**
     * @see org.geotools.xml.XSIElementHandler#getLocalName()
     */
    public String getLocalName() {
        return LOCALNAME;
    }

    /**
     * <p>
     * returns the AnyAttribute child element
     * </p>
     * TODO use this !
     *
     */
    public AnyAttributeHandler getAnyAttribute() {
        return anyAttribute;
    }

    /**
     * <p>
     * returns the list of attribute declarations
     * </p>
     *
     */
    public List getAttributeDeclarations() {
        return attrDecs;
    }

    /**
     * <p>
     * Retusn the 'base' attribute
     * </p>
     *
     */
    public String getBase() {
        return base;
    }

    /**
     * <p>
     * Returns a list of Facets
     * </p>
     *
     */
    public List getConstraints() {
        return constraints;
    }

    /**
     * <p>
     * Returns the id attribute
     * </p>
     *
     */
    public String getId() {
        return id;
    }

    /**
     * <p>
     * Returns a nested child element declaration.
     * </p>
     *
     * @return (ElementGroupingHandler or SimpleTypeHandler)
     */
    public Object getChild() {
        return child;
    }

    /**
     * @see org.geotools.xml.XSIElementHandler#getHandlerType()
     */
    public int getHandlerType() {
        return RESTRICTION;
    }

    /**
     * @see org.geotools.xml.XSIElementHandler#endElement(java.lang.String,
     *      java.lang.String)
     */
    public void endElement(String namespaceURI, String localName){
        // do nothing
    }
}


/**
 * Represents an Enumeration element
 *
 * @author $author$
 * @version $Revision: 1.9 $
 */
class EnumerationHandler extends FacetHandler {
    /** 'enumeration' */
    public final static String LOCALNAME = "enumeration";

    /**
     * @see org.geotools.xml.XSIHandlers.FacetHandler#getBinding()
     */
    public int getType() {
        return Facet.ENUMERATION;
    }

    /**
     * @see org.geotools.xml.XSIElementHandler#getLocalName()
     */
    public String getLocalName() {
        return LOCALNAME;
    }
}


/**
 * Represents a FractionDigits element
 *
 * @author $author$
 * @version $Revision: 1.9 $
 */
class FractionDigitsHandler extends FacetHandler {
    /** 'fractionDigits' */
    public final static String LOCALNAME = "fractionDigits";

    /**
     * @see org.geotools.xml.XSIHandlers.FacetHandler#getBinding()
     */
    public int getType() {
        return Facet.FRACTIONDIGITS;
    }

    /**
     * @see org.geotools.xml.XSIElementHandler#getLocalName()
     */
    public String getLocalName() {
        return LOCALNAME;
    }
}


/**
 * Represents a Length element
 *
 * @author $author$
 * @version $Revision: 1.9 $
 */
class LengthHandler extends FacetHandler {
    /** 'length' */
    public final static String LOCALNAME = "length";

    /**
     * @see org.geotools.xml.XSIHandlers.FacetHandler#getBinding()
     */
    public int getType() {
        return Facet.LENGTH;
    }

    /**
     * @see org.geotools.xml.XSIElementHandler#getLocalName()
     */
    public String getLocalName() {
        return LOCALNAME;
    }
}


/**
 * Represents a MinInclusive element
 *
 * @author $author$
 * @version $Revision: 1.9 $
 */
class MinInclusiveHandler extends FacetHandler {
    /** 'minInclusive' */
    public final static String LOCALNAME = "minInclusive";

    /**
     * @see org.geotools.xml.XSIHandlers.FacetHandler#getBinding()
     */
    public int getType() {
        return Facet.MININCLUSIVE;
    }

    /**
     * @see org.geotools.xml.XSIElementHandler#getLocalName()
     */
    public String getLocalName() {
        return LOCALNAME;
    }
}


/**
 * Represents a MaxInclusive element
 *
 * @author $author$
 * @version $Revision: 1.9 $
 */
class MaxInclusiveHandler extends FacetHandler {
    /** 'maxInclusive' */
    public final static String LOCALNAME = "maxInclusive";

    /**
     * @see org.geotools.xml.XSIHandlers.FacetHandler#getBinding()
     */
    public int getType() {
        return Facet.MAXINCLUSIVE;
    }

    /**
     * @see org.geotools.xml.XSIElementHandler#getLocalName()
     */
    public String getLocalName() {
        return LOCALNAME;
    }
}


/**
 * represents a MinExclusive element
 *
 * @author $author$
 * @version $Revision: 1.9 $
 */
class MinExclusiveHandler extends FacetHandler {
    /** 'minExclusive' */
    public final static String LOCALNAME = "minExclusive";

    /**
     * @see org.geotools.xml.XSIHandlers.FacetHandler#getBinding()
     */
    public int getType() {
        return Facet.MINEXCLUSIVE;
    }

    /**
     * @see org.geotools.xml.XSIElementHandler#getLocalName()
     */
    public String getLocalName() {
        return LOCALNAME;
    }
}


/**
 * represents the MaxExclusive element
 *
 * @author $author$
 * @version $Revision: 1.9 $
 */
class MaxExclusiveHandler extends FacetHandler {
    /** 'maxExclusive' */
    public final static String LOCALNAME = "maxExclusive";

    /**
     * @see org.geotools.xml.XSIHandlers.FacetHandler#getBinding()
     */
    public int getType() {
        return Facet.MAXEXCLUSIVE;
    }

    /**
     * @see org.geotools.xml.XSIElementHandler#getLocalName()
     */
    public String getLocalName() {
        return LOCALNAME;
    }
}


/**
 * represents a minLength element
 *
 * @author $author$
 * @version $Revision: 1.9 $
 */
class MinLengthHandler extends FacetHandler {
    /** 'minLength' */
    public final static String LOCALNAME = "minLength";

    /**
     * @see org.geotools.xml.XSIHandlers.FacetHandler#getBinding()
     */
    public int getType() {
        return Facet.MINLENGTH;
    }

    /**
     * @see org.geotools.xml.XSIElementHandler#getLocalName()
     */
    public String getLocalName() {
        return LOCALNAME;
    }
}


/**
 * represents a maxLength element
 *
 * @author $author$
 * @version $Revision: 1.9 $
 */
class MaxLengthHandler extends FacetHandler {
    /** 'maxLength' */
    public final static String LOCALNAME = "maxLength";

    /**
     * @see org.geotools.xml.XSIHandlers.FacetHandler#getBinding()
     */
    public int getType() {
        return Facet.MAXLENGTH;
    }

    /**
     * @see org.geotools.xml.XSIElementHandler#getLocalName()
     */
    public String getLocalName() {
        return LOCALNAME;
    }
}


/**
 * represents a pattern element
 *
 * @author $author$
 * @version $Revision: 1.9 $
 */
class PatternHandler extends FacetHandler {
    /** 'pattern' */
    public final static String LOCALNAME = "pattern";

    /**
     * @see org.geotools.xml.XSIHandlers.FacetHandler#getBinding()
     */
    public int getType() {
        return Facet.PATTERN;
    }

    /**
     * @see org.geotools.xml.XSIElementHandler#getLocalName()
     */
    public String getLocalName() {
        return LOCALNAME;
    }
}


/**
 * Represents a totaldigits element
 *
 * @author $author$
 * @version $Revision: 1.9 $
 */
class TotalDigitsHandler extends FacetHandler {
    /** 'totalDigits' */
    public final static String LOCALNAME = "totalDigits";

    /**
     * @see org.geotools.xml.XSIHandlers.FacetHandler#getBinding()
     */
    public int getType() {
        return Facet.TOTALDIGITS;
    }

    /**
     * @see org.geotools.xml.XSIElementHandler#getLocalName()
     */
    public String getLocalName() {
        return LOCALNAME;
    }
}
