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

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.TreeSet;

import org.geotools.xml.SchemaFactory;
import org.geotools.xml.XSIElementHandler;
import org.geotools.xml.schema.All;
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
import org.xml.sax.SAXException;

/**
 * SchemaHandler purpose.
 * <p>
 * represents a Schema element
 * </p>
 * 
 * @author dzwiers, Refractions Research, Inc. http://www.refractions.net
 * @author $Author:$ (last modification)
 * @source $URL$
 * @version $Id$
 */
public class SchemaHandler extends XSIElementHandler {
    /** "http://www.w3.org/2001/XMLSchema" */
    public static final String namespaceURI = "http://www.w3.org/2001/XMLSchema";

    /** 'schema' */
    public final static String LOCALNAME = "schema";
    private String id;
    private String prefix;
    private URI targetNamespace;
    private String version;
    private boolean elementFormDefault;
    private boolean attributeFormDefault;
    private int finalDefault;
    private int blockDefault;
    private List includes;
    private List imports;
    private List redefines;
    private List attributes;
    private List attributeGroups;
    private List complexTypes;
    private List elements;
    private List groups;
    private List simpleTypes;
    private URI uri;
    private Schema schema = null;
    private HashMap prefixCache;

    /**
     * @see java.lang.Object#hashCode()
     */
    public int hashCode() {
        return LOCALNAME.hashCode() * ((id == null) ? 1 : id.hashCode()) * ((version == null) ? 1 : version.hashCode())
                * ((targetNamespace == null) ? 1 : targetNamespace.hashCode());
    }

    /**
     * @see org.xml.sax.ContentHandler#startPrefixMapping(java.lang.String, java.lang.String)
     */
    public void startPrefixMapping( String pref, String uri1 ) {
        if (targetNamespace == null) {
            if (prefixCache == null) {
                prefixCache = new HashMap();
            }
            
            if( !pref.trim().equals("") || !prefixCache.containsKey(uri1) )
                prefixCache.put(uri1, pref);
                
            if (this.uri == null && (pref == null || "".equals(pref)))
                try {
                    this.uri = new URI(uri1);
                } catch (URISyntaxException e) {
                    logger.warning(e.getMessage());
                }
        } else {
            // we have already started
            if (targetNamespace.equals(uri1.toString())) {
                prefix = pref;
            }
        }
    }

    /**
     * @see org.geotools.xml.XSIElementHandler#startElement(java.lang.String, java.lang.String, org.xml.sax.Attributes)
     */
    public void startElement( String namespaceURI1, String localName, Attributes atts ) throws SAXException {
        // targetNamespace
        String targetNamespace1 = atts.getValue("", "targetNamespace");

        if (targetNamespace1 == null) {
            targetNamespace1 = atts.getValue(namespaceURI1, "targetNamespace");
        }

        try {
            this.targetNamespace = new URI(targetNamespace1);
        } catch (URISyntaxException e) {
            logger.warning(e.toString());
            throw new SAXException(e);
        }

        if ((prefixCache != null) && (targetNamespace1 != null) && (!targetNamespace1.equals(""))) {
            Iterator i = prefixCache.keySet().iterator();

            while( (i != null) && i.hasNext() ) {
                String uriT = (String) i.next();

                if (targetNamespace1.equals(uriT)) {
                    prefix = (String) prefixCache.get(uriT);
                    i = null;
                }
            }
        }

        // prefixCache = null;

        // attributeFormDefault
        String attributeFormDefault1 = atts.getValue("", "attributeFormDefault");

        if (attributeFormDefault1 == null) {
            attributeFormDefault1 = atts.getValue(namespaceURI1, "attributeFormDefault");
        }

        this.attributeFormDefault = "qualified".equalsIgnoreCase(attributeFormDefault1);

        // blockDefault
        String blockDefault1 = atts.getValue("", "blockDefault");

        if (blockDefault1 == null) {
            blockDefault1 = atts.getValue(namespaceURI1, "blockDefault");
        }

        this.blockDefault = ComplexTypeHandler.findBlock(blockDefault1);

        // elementFormDefault
        String elementFormDefault1 = atts.getValue("", "elementFormDefault");

        if (elementFormDefault1 == null) {
            elementFormDefault1 = atts.getValue(namespaceURI1, "elementFormDefault");
        }

        this.elementFormDefault = "qualified".equalsIgnoreCase(elementFormDefault1);

        // finalDefault
        String finalDefault1 = atts.getValue("", "finalDefault");

        if (finalDefault1 == null) {
            finalDefault1 = atts.getValue(namespaceURI1, "finalDefault");
        }

        this.finalDefault = SimpleTypeHandler.findFinal(finalDefault1);

        // id
        id = atts.getValue("", "id");

        if (id == null) {
            id = atts.getValue(namespaceURI1, "id");
        }

        // version
        version = atts.getValue("", "version");

        if (version == null) {
            version = atts.getValue(namespaceURI1, "version");
        }
    }

    /**
     * @see org.geotools.xml.XSIElementHandler#getHandler(java.lang.String, java.lang.String)
     */
    public XSIElementHandler getHandler( String namespaceURI1, String localName ) {
        // check that we are working with a known namespace
        if (SchemaHandler.namespaceURI.equalsIgnoreCase(namespaceURI1)) {
            // child elements:
            //
            // This list order has been picked in an adhock manner
            // attempting to improve performance. Re-order the
            // child elements if this order does not appear optimal.
            // complexType
            if (ComplexTypeHandler.LOCALNAME.equalsIgnoreCase(localName)) {
                if (complexTypes == null) {
                    complexTypes = new LinkedList();
                }

                ComplexTypeHandler cth = new ComplexTypeHandler();
                complexTypes.add(cth);

                return cth;
            }

            // simpleType
            if (SimpleTypeHandler.LOCALNAME.equalsIgnoreCase(localName)) {
                if (simpleTypes == null) {
                    simpleTypes = new LinkedList();
                }

                SimpleTypeHandler sth = new SimpleTypeHandler();
                simpleTypes.add(sth);

                return sth;
            }

            // element
            if (ElementTypeHandler.LOCALNAME.equalsIgnoreCase(localName)) {
                if (elements == null) {
                    elements = new LinkedList();
                }

                ElementTypeHandler eth = new ElementTypeHandler();
                elements.add(eth);

                return eth;
            }

            // attribute
            if (AttributeHandler.LOCALNAME.equalsIgnoreCase(localName)) {
                if (attributes == null) {
                    attributes = new LinkedList();
                }

                AttributeHandler ah = new AttributeHandler();
                attributes.add(ah);

                return ah;
            }

            // include
            if (IncludeHandler.LOCALNAME.equalsIgnoreCase(localName)) {
                if (includes == null) {
                    includes = new LinkedList();
                }

                IncludeHandler ih = new IncludeHandler();
                includes.add(ih);

                return ih;
            }

            // import
            if (ImportHandler.LOCALNAME.equalsIgnoreCase(localName)) {
                if (imports == null) {
                    imports = new LinkedList();
                }

                ImportHandler ih = new ImportHandler();
                imports.add(ih);

                return ih;
            }

            // group
            if (GroupHandler.LOCALNAME.equalsIgnoreCase(localName)) {
                if (groups == null) {
                    groups = new LinkedList();
                }

                GroupHandler gh = new GroupHandler();
                groups.add(gh);

                return gh;
            }

            // redefine
            if (RedefineHandler.LOCALNAME.equalsIgnoreCase(localName)) {
                if (redefines == null) {
                    redefines = new LinkedList();
                }

                RedefineHandler rh = new RedefineHandler();
                redefines.add(rh);

                return rh;
            }

            // attributeGroup
            if (AttributeGroupHandler.LOCALNAME.equalsIgnoreCase(localName)) {
                if (attributeGroups == null) {
                    attributeGroups = new LinkedList();
                }

                AttributeGroupHandler agh = new AttributeGroupHandler();
                attributeGroups.add(agh);

                return agh;
            }
        }

        return null;
    }

    /**
     * @see org.geotools.xml.XSIElementHandler#getLocalName()
     */
    public String getLocalName() {
        return LOCALNAME;
    }

    /**
     * <p>
     * creates a smaller, more compact version of the schema
     * </p>
     * 
     * @param thisURI
     * @throws SAXException
     */
    protected Schema compress( URI thisURI ) throws SAXException {
        if (schema != null) {
            return schema; // already compressed.
        }

        if (uri == null) {
            uri = thisURI;
        } else {
            if (thisURI != null) {
                uri = thisURI.resolve(uri);
            }
        }

        if (prefix == null && prefixCache != null) {
            prefix = (String) prefixCache.get(targetNamespace);
        }

        Iterator it = null;

        if (includes != null) {
            // do these first
            it = includes.iterator();

            while( it.hasNext() ) {
                IncludeHandler inc = (IncludeHandler) it.next();
                logger.finest("compressing include " + inc.getSchemaLocation());

                if (inc != null && inc.getSchemaLocation() != null) {
                    Schema cs;
                    URI incURI = null;
                    if (thisURI == null) {
                        try {
                            incURI = new URI(inc.getSchemaLocation());
                        } catch (URISyntaxException e) {
                            logger.warning(e.getMessage());
                        }
                    } else {
                        incURI = thisURI.normalize().resolve(inc.getSchemaLocation());
                    }
                    cs = SchemaFactory.getInstance(targetNamespace, incURI, logger.getLevel());

                    if (uri != null) {
                        uri = incURI.resolve(uri);
                    } else {
                        uri = incURI;
                    }

                    // already compressed
                    addSchema(cs);
                }
            }
        }

        includes = null;

        // imports may be schema or schemaHandler
        if (this.imports != null) {
            // have now loaded the included stuff.
            LinkedList imports1 = new LinkedList();
            it = this.imports.iterator();

            while( it.hasNext() ) {
                Object obj = it.next();

                if (obj instanceof ImportHandler) {
                    ImportHandler imp = (ImportHandler) obj;
                    URI incURI = null;

                    // if ((imp.getSchemaLocation() != null) && (thisURI != null)) {
                    // incURI = thisURI.normalize().resolve(imp
                    // .getSchemaLocation());
                    // }
                    // fix from chris dillard
                    if (imp.getSchemaLocation() != null) {
                        if (thisURI != null) {
                            // For resolving relative URIs
                            incURI = thisURI.normalize().resolve(imp.getSchemaLocation());
                        } else {
                            // If thisURI is null, we have to assume the
                            // URI is absolute.
                            incURI = imp.getSchemaLocation();
                        }
                    }

                    Schema cs = SchemaFactory.getInstance(imp.getNamespace(), incURI, logger.getLevel());

                    imports1.add(cs);
                } else {
                    imports1.add(obj);
                }
            }

            this.imports = imports1;
        }

        // TODO redefines?
        // should do over-writing here
        // build schema object ... be thrown out
        DefaultSchema schema1 = new DefaultSchema();
        schema1.attributeFormDefault = attributeFormDefault;
        schema1.elementFormDefault = elementFormDefault;
        schema1.finalDefault = finalDefault;
        schema1.blockDefault = blockDefault;
        schema1.id = id;
        schema1.prefix = prefix;
        schema1.targetNamespace = targetNamespace;
        schema1.version = version;
        schema1.uri = uri;

        if (imports != null) {
            TreeSet tmp = new TreeSet(SchemaComparator.getInstance());
            tmp.addAll(imports);
            schema1.imports = (Schema[]) tmp.toArray(new Schema[tmp.size()]);
        }

        // these need to be retyped

        if (simpleTypes != null) {
            it = simpleTypes.iterator();
            HashSet cache = new HashSet();
            while( it.hasNext() ) {
                Object t = it.next();
                if (t instanceof SimpleTypeHandler) {
                    SimpleTypeHandler tt = (SimpleTypeHandler) t;
                    cache.add(tt.compress(this));
                    it.remove();
                }
            }
            it = cache.iterator();
            while( it.hasNext() )
                simpleTypes.add(it.next());

            TreeSet tmp = new TreeSet(SchemaComparator.getInstance());
            tmp.addAll(simpleTypes);
            schema1.simpleTypes = (SimpleType[]) tmp.toArray(new SimpleType[tmp.size()]);
        }

        if (attributeGroups != null) {
            it = attributeGroups.iterator();
            HashSet cache = new HashSet();
            while( it.hasNext() ) {
                Object t = it.next();
                if (t instanceof AttributeGroupHandler) {
                    AttributeGroupHandler tt = (AttributeGroupHandler) t;
                    cache.add(tt.compress(this));
                    it.remove();
                }
            }
            it = cache.iterator();
            while( it.hasNext() )
                attributeGroups.add(it.next());

            TreeSet tmp = new TreeSet(SchemaComparator.getInstance());
            tmp.addAll(attributeGroups);
            schema1.attributeGroups = (AttributeGroup[]) tmp.toArray(new AttributeGroup[tmp
                    .size()]);
        }

        if (attributes != null) {
            it = attributes.iterator();
            HashSet cache = new HashSet();
            while( it.hasNext() ) {
                Object t = it.next();
                if (t instanceof AttributeHandler) {
                    AttributeHandler tt = (AttributeHandler) t;
                    cache.add(tt.compress(this));
                    it.remove();
                }
            }
            it = cache.iterator();
            while( it.hasNext() )
                attributes.add(it.next());

            TreeSet tmp = new TreeSet(SchemaComparator.getInstance());
            tmp.addAll(attributes);
            schema1.attributes = (Attribute[]) tmp.toArray(new Attribute[tmp.size()]);
        }

        if (complexTypes != null) {
            it = complexTypes.iterator();
            HashSet cache = new HashSet();
            while( it.hasNext() ) {
                Object t = it.next();
                if (t instanceof ComplexTypeHandler) {
                    ComplexTypeHandler tt = (ComplexTypeHandler) t;
                    cache.add(tt.compress(this));
                }
            }
            complexTypes.clear();
            it = cache.iterator();
            while( it.hasNext() )
                complexTypes.add(it.next());

            TreeSet tmp = new TreeSet(SchemaComparator.getInstance());
            tmp.addAll(complexTypes);
            schema1.complexTypes = (ComplexType[]) tmp.toArray(new ComplexType[tmp.size()]);
        }

        if (elements != null) {
            it = elements.iterator();
            HashSet cache = new HashSet();
            while( it.hasNext() ) {
                Object t = it.next();
                if (t instanceof ElementTypeHandler) {
                    ElementTypeHandler tt = (ElementTypeHandler) t;
                    cache.add(tt.compress(this));
                    it.remove();
                }
            }
            it = cache.iterator();
            while( it.hasNext() )
                elements.add(it.next());

            TreeSet tmp = new TreeSet(SchemaComparator.getInstance());
            tmp.addAll(elements);
            schema1.elements = (Element[]) tmp.toArray(new Element[tmp.size()]);
        }

        if (groups != null) {
            it = groups.iterator();
            HashSet cache = new HashSet();
            while( it.hasNext() ) {
                Object t = it.next();
                if (t instanceof GroupHandler) {
                    GroupHandler tt = (GroupHandler) t;
                    cache.add(tt.compress(this));
                    it.remove();
                }
            }
            it = cache.iterator();
            while( it.hasNext() )
                groups.add(it.next());

            TreeSet tmp = new TreeSet(SchemaComparator.getInstance());
            tmp.addAll(groups);
            schema1.groups = (Group[]) tmp.toArray(new Group[tmp.size()]);
        }

        attributeGroups = attributes = complexTypes = simpleTypes = elements = groups = imports = includes = redefines = null;

        return schema1;
    }

    /*
     * Helper method for lookUpSimpleType(String)
     */
    private SimpleType lookUpSimpleType( String localName, Schema s, TreeSet targets ) {
        if (s == null) {
            return null;
        }

        targets.add(s.getTargetNamespace());

        if (s.getSimpleTypes() != null) {
            SimpleType[] sts = s.getSimpleTypes();

            for( int i = 0; (sts != null) && (i < sts.length); i++ ) {
                if (localName.equalsIgnoreCase(sts[i].getName())) {
                    return sts[i];
                }
            }
        }

        if (s.getImports() != null) {
            Schema[] ss = s.getImports();

            for( int i = 0; (ss != null) && (i < ss.length); i++ ) {
                if (!targets.contains(ss[i].getTargetNamespace())) {
                    SimpleType st = lookUpSimpleType(localName, ss[i], targets);

                    if (st != null) {
                        return st;
                    }
                }
            }
        }

        return null;
    }

    /**
     * <p>
     * convinience method for package classes
     * </p>
     * 
     * @param qname
     */
    protected SimpleType lookUpSimpleType( String qname ) {
        int index = qname.indexOf(":");
        String localName, prefix1;
        localName = prefix1 = null;
        if (index >= 0) {
            localName = qname.substring(index + 1);
            prefix1 = qname.substring(0, index);
        } else {
            prefix1 = "";
            localName = qname;
        }
        logger.finest("prefix is " + prefix1);
        logger.finest("localName is " + localName);
        Iterator it;
        if ((this.prefix == null && prefix1 == null) || (this.prefix != null && this.prefix.equals(prefix1))) {
            if (schema != null)
                return lookUpSimpleType(localName, schema, new TreeSet());
        } else {
            if (imports != null) {
                it = imports.iterator();
                while( it.hasNext() ) {
                    Schema s = (Schema) it.next();
                    String ns = s.getTargetNamespace().toString();

                    String prefixLookup = prefixCache != null ? (String) prefixCache.get(ns) : null;
                    if (prefix1 == null || prefixLookup == null || prefix1.equals(prefixLookup)) {
                        SimpleType st = lookUpSimpleType(localName, s, new TreeSet());
                        if (st != null) {
                            return st;
                        }
                    }
                }
            }
        }

        if (simpleTypes != null) {
            it = simpleTypes.iterator();

            while( it.hasNext() ) {
                Object o = it.next();

                if (o instanceof SimpleTypeHandler) {
                    SimpleTypeHandler sst = (SimpleTypeHandler) o;

                    if (localName.equalsIgnoreCase(sst.getName())) {
                        return sst.compress(this);
                    }
                } else {
                    SimpleType sst = (SimpleType) o;

                    if (localName.equalsIgnoreCase(sst.getName())) {
                        return sst;
                    }
                }
            }
        }

        SimpleType sti = XSISimpleTypes.find(localName);

        if (sti != null) {
            return sti;
        }

        return null;
    }

    /*
     * helper for lookUpComplexType(String)
     */
    private ComplexType lookUpComplexType( String localName, Schema s, TreeSet targets ) {
        if (s == null) {
            return null;
        }

        targets.add(s.getTargetNamespace());

        if (s.getComplexTypes() != null) {
            ComplexType[] sts = s.getComplexTypes();

            for( int i = 0; (sts != null) && (i < sts.length); i++ ) {
                String name = sts[i].getName();
				if (localName.equalsIgnoreCase(name)) {
                    return sts[i];
                }
            }
        }

        if (s.getImports() != null) {
            Schema[] ss = s.getImports();

            for( int i = 0; (ss != null) && (i < ss.length); i++ ) {
                if (!targets.contains(ss[i].getTargetNamespace())) {
                    ComplexType st = lookUpComplexType(localName, ss[i], targets);

                    if (st != null) {
                        return st;
                    }
                }
            }
        }

        return null;
    }

    /**
     * <p>
     * convinience method for package
     * </p>
     * 
     * @param qname
     * @throws SAXException
     */
    protected ComplexType lookUpComplexType( String qname ) throws SAXException {
        int index = qname.indexOf(":");
        String localName, prefix1;
        localName = prefix1 = null;
        if (index >= 0) {
            localName = qname.substring(index + 1);
            prefix1 = qname.substring(0, index);
        } else {
            prefix1 = "";
            localName = qname;
        }
        logger.finest("prefix is " + prefix1);
        logger.finest("localName is " + localName);

        Iterator it;
        if ((this.prefix == null && prefix1 == null) || (this.prefix != null && this.prefix.equals(prefix1))) {
            if (schema != null)
                return lookUpComplexType(localName, schema, new TreeSet());
        } else {
            if (imports != null) {
                it = imports.iterator();
                while( it.hasNext() ) {
                    Schema s = (Schema) it.next();
                    String ns = s.getTargetNamespace().toString();
                    String prefixLookup = prefixCache != null ? (String) prefixCache.get(ns) : null;
                    if (prefix1 == null || prefixLookup == null || prefix1.equals(prefixLookup)) {
                        ComplexType ct = lookUpComplexType(localName, s, new TreeSet());
                        if (ct != null) {
                            return ct;
                        }
                    }
                }
            }
        }

        if (complexTypes != null) {
            it = complexTypes.iterator();

            while( it.hasNext() ) {
                Object o = it.next();

                if (o instanceof ComplexTypeHandler) {
                    ComplexTypeHandler sst = (ComplexTypeHandler) o;

                    if (localName.equalsIgnoreCase(sst.getName())) {
                        return sst.compress(this);
                    }
                } else {
                    ComplexType sst = (ComplexType) o;

                    if (localName.equalsIgnoreCase(sst.getName())) {
                        return sst;
                    }
                }
            }
        }

        return null;
    }

    /*
     * helper method for lookupElement(String)
     */
    private Element lookupElement( String localName, Schema s, TreeSet targets ) {
        if (s == null) {
            return null;
        }

        logger.finest("looking for element in " + s.getTargetNamespace());
        targets.add(s.getTargetNamespace());

        if (s.getElements() != null) {
            Element[] sts = s.getElements();

            for( int i = 0; (sts != null) && (i < sts.length); i++ ) {
                String name = sts[i].getName();
				logger.finest("checking element " + name);

                if (localName.equalsIgnoreCase(name)) {
                    return sts[i];
                }
            }
        }

        if (s.getImports() != null) {
            Schema[] ss = s.getImports();

            for( int i = 0; (ss != null) && (i < ss.length); i++ ) {
                if (!targets.contains(ss[i].getTargetNamespace())) {
                    Element st = lookupElement(localName, ss[i], targets);

                    if (st != null) {
                        return st;
                    }
                }
            }
        }

        return null;
    }

    /**
     * <p>
     * convinience method for package
     * </p>
     * 
     * @param qname
     * @throws SAXException
     */
    protected Element lookUpElement( String qname ) throws SAXException {
        int index = qname.indexOf(":");
        String localName, prefix1;
        localName = prefix1 = null;
        if (index >= 0) {
            localName = qname.substring(index + 1);
            prefix1 = qname.substring(0, index);
        } else {
            prefix1 = "";
            localName = qname;
        }
        logger.finest("prefix is " + prefix1);
        logger.finest("localName is " + localName);

        Iterator it;
        if ((this.prefix == null && prefix1 == null) || (this.prefix != null && this.prefix.equals(prefix1))) {
            if (schema != null)
                return lookupElement(localName, schema, new TreeSet());
        } else {
            if (imports != null) {
                it = imports.iterator();
                while( it.hasNext() ) {
                    Schema s = (Schema) it.next();
                    String ns = s.getTargetNamespace().toString();
                    String prefixLookup = prefixCache != null ? (String) prefixCache.get(ns) : null;
                    if (prefix1 == null || prefixLookup == null || prefix1.equals(prefixLookup)) {
                        Element ct = lookupElement(localName, s, new TreeSet());
                        if (ct != null) {
                            return ct;
                        }
                    }
                }
            }
            if (includes != null) {
                it = includes.iterator();
                while( it.hasNext() ) {
                    Schema s = (Schema) it.next();
                    String ns = s.getTargetNamespace().toString();
                    String prefixLookup = prefixCache != null ? (String) prefixCache.get(ns) : null;
                    if (prefix1 == null || prefixLookup == null || prefix1.equals(prefixLookup)) {
                        Element ct = lookupElement(localName, s, new TreeSet());
                        if (ct != null) {
                            return ct;
                        }
                    }
                }
            }
        }

        it = elements.iterator();

        while( it.hasNext() ) {
            Object o = it.next();

            if (o instanceof ElementTypeHandler) {
                ElementTypeHandler sst = (ElementTypeHandler) o;

                if (localName.equalsIgnoreCase(sst.getName())) {
                    return (Element) sst.compress(this);
                }
            } else {
                Element sst = (Element) o;

                if (localName.equalsIgnoreCase(sst.getName())) {
                    return sst;
                }
            }
        }
        return null;
    }

    /*
     * helper for lookUpGroup
     */
    private Group lookUpGroup( String localName, Schema s, TreeSet targets ) {
        if (s == null) {
            return null;
        }

        targets.add(s.getTargetNamespace());

        if (s.getGroups() != null) {
            Group[] sts = s.getGroups();

            for( int i = 0; (sts != null) && (i < sts.length); i++ ) {
                if (localName.equalsIgnoreCase(sts[i].getName())) {
                    return sts[i];
                }
            }
        }

        if (s.getImports() != null) {
            Schema[] ss = s.getImports();

            for( int i = 0; (ss != null) && (i < ss.length); i++ ) {
                if (!targets.contains(ss[i].getTargetNamespace())) {
                    Group st = lookUpGroup(localName, ss[i], targets);

                    if (st != null) {
                        return st;
                    }
                }
            }
        }

        return null;
    }

    /**
     * <p>
     * convinience method for package
     * </p>
     * 
     * @param qname
     * @throws SAXException
     */
    protected Group lookUpGroup( String qname ) throws SAXException {
        int index = qname.indexOf(":");
        String localName, prefix1;
        localName = prefix1 = null;
        if (index >= 0) {
            localName = qname.substring(index + 1);
            prefix1 = qname.substring(0, index);
        } else {
            prefix1 = "";
            localName = qname;
        }
        logger.finest("prefix is " + prefix1);
        logger.finest("localName is " + localName);

        Iterator it;
        if ((this.prefix == null && prefix1 == null) || (this.prefix != null && this.prefix.equals(prefix1))) {
            if (schema != null)
                return lookUpGroup(localName, schema, new TreeSet());
        } else {
            if (imports != null) {
                it = imports.iterator();
                while( it.hasNext() ) {
                    Schema s = (Schema) it.next();
                    String ns = s.getTargetNamespace().toString();
                    String prefixLookup = prefixCache != null ? (String) prefixCache.get(ns) : null;
                    if (prefix1 == null || prefixLookup == null || prefix1.equals(prefixLookup)) {
                        Group ct = lookUpGroup(localName, s, new TreeSet());
                        if (ct != null) {
                            return ct;
                        }
                    }
                }
            }
        }

        if (groups != null) {
            it = groups.iterator();

            while( it.hasNext() ) {
                Object o = it.next();

                if (o instanceof GroupHandler) {
                    GroupHandler sst = (GroupHandler) o;

                    if (localName.equalsIgnoreCase(sst.getName())) {
                        return (Group) sst.compress(this);
                    }
                } else {
                    Group sst = (Group) o;

                    if (localName.equalsIgnoreCase(sst.getName())) {
                        return sst;
                    }
                }
            }
        }

        return null;
    }

    /*
     * helper method for lookUpAttributeGroup
     */
    private AttributeGroup lookUpAttributeGroup( String localName, Schema s, TreeSet targets ) {
        if (s == null) {
            return null;
        }

        targets.add(s.getTargetNamespace());

        if (s.getAttributeGroups() != null) {
            AttributeGroup[] sts = s.getAttributeGroups();

            for( int i = 0; (sts != null) && (i < sts.length); i++ ) {
                if (localName.equalsIgnoreCase(sts[i].getName())) {
                    return sts[i];
                }
            }
        }

        if (s.getImports() != null) {
            Schema[] ss = s.getImports();

            for( int i = 0; (ss != null) && (i < ss.length); i++ ) {
                if (!targets.contains(ss[i].getTargetNamespace())) {
                    AttributeGroup st = lookUpAttributeGroup(localName, ss[i], targets);

                    if (st != null) {
                        return st;
                    }
                }
            }
        }

        return null;
    }

    /**
     * <p>
     * convinience method for the package
     * </p>
     * 
     * @param qname
     * @throws SAXException
     */
    protected AttributeGroup lookUpAttributeGroup( String qname ) throws SAXException {
        int index = qname.indexOf(":");
        String localName, prefix1;
        localName = prefix1 = null;
        if (index >= 0) {
            localName = qname.substring(index + 1);
            prefix1 = qname.substring(0, index);
        } else {
            prefix1 = "";
            localName = qname;
        }
        logger.finest("prefix is " + prefix1);
        logger.finest("localName is " + localName);

        Iterator it;
        if ((this.prefix == null && prefix1 == null) || (this.prefix != null && this.prefix.equals(prefix1))) {
            if (schema != null)
                return lookUpAttributeGroup(localName, schema, new TreeSet());
        } else {
            if (imports != null) {
                it = imports.iterator();
                while( it.hasNext() ) {
                    Schema s = (Schema) it.next();
                    String ns = s.getTargetNamespace().toString();
                    String prefixLookup = prefixCache != null ? (String) prefixCache.get(ns) : null;
                    if (prefix1 == null || prefixLookup == null || prefix1.equals(prefixLookup)) {
                        AttributeGroup ct = lookUpAttributeGroup(localName, s, new TreeSet());
                        if (ct != null) {
                            return ct;
                        }
                    }
                }
            }
        }

        if (attributeGroups != null) {
            it = attributeGroups.iterator();

            while( it.hasNext() ) {
                Object o = it.next();

                if (o instanceof AttributeGroupHandler) {
                    AttributeGroupHandler sst = (AttributeGroupHandler) o;

                    if (localName.equalsIgnoreCase(sst.getName())) {
                        return sst.compress(this);
                    }
                } else {
                    AttributeGroup sst = (AttributeGroup) o;

                    if (localName.equalsIgnoreCase(sst.getName())) {
                        return sst;
                    }
                }
            }
        }

        return null;
    }

    /*
     * helper method for lookUpAttribute
     */
    private Attribute lookUpAttribute( String localName, Schema s, TreeSet targets ) {
        if (s == null) {
            return null;
        }

        targets.add(s.getTargetNamespace());

        if (s.getAttributes() != null) {
            Attribute[] sts = s.getAttributes();

            for( int i = 0; (sts != null) && (i < sts.length); i++ ) {
                if (sts[i] != null && sts[i].getName() != null) {
                    if (localName.equalsIgnoreCase(sts[i].getName())) {
                        return sts[i];
                    }
                }
            }
        }

        if (s.getImports() != null) {
            Schema[] ss = s.getImports();

            for( int i = 0; (ss != null) && (i < ss.length); i++ ) {
                if (!targets.contains(ss[i].getTargetNamespace())) {
                    Attribute st = lookUpAttribute(localName, ss[i], targets);

                    if (st != null) {
                        return st;
                    }
                }
            }
        }

        return null;
    }

    /**
     * <p>
     * convinience method for package
     * </p>
     * 
     * @param qname
     * @throws SAXException
     */
    protected Attribute lookUpAttribute( String qname ) throws SAXException {
        int index = qname.indexOf(":");
        String localName, prefix1;
        localName = prefix1 = null;
        if (index >= 0) {
            localName = qname.substring(index + 1);
            prefix1 = qname.substring(0, index);
        } else {
            prefix1 = "";
            localName = qname;
        }
        logger.finest("prefix is " + prefix1);
        logger.finest("localName is " + localName);

        Iterator it;
        if ((this.prefix == null && prefix1 == null) || (this.prefix != null && this.prefix.equals(prefix1))) {
            if (schema != null)
                return lookUpAttribute(localName, schema, new TreeSet());
        } else {
            if (imports != null) {
                it = imports.iterator();
                while( it.hasNext() ) {
                    Schema s = (Schema) it.next();
                    String ns = s.getTargetNamespace().toString();
                    String prefixLookup = prefixCache != null ? (String) prefixCache.get(ns) : null;
                    if (prefix1 == null || prefixLookup == null || prefix1.equals(prefixLookup)) {
                        Attribute ct = lookUpAttribute(localName, s, new TreeSet());
                        if (ct != null) {
                            return ct;
                        }
                    }
                }
            }
        }

        if (attributes != null) {
            it = attributes.iterator();

            while( it.hasNext() ) {
                Object o = it.next();

                if (o instanceof AttributeHandler) {
                    AttributeHandler sst = (AttributeHandler) o;

                    if (localName.equalsIgnoreCase(sst.getName())) {
                        return sst.compress(this);
                    }
                } else {
                    Attribute sst = (Attribute) o;

                    if (localName.equalsIgnoreCase(sst.getName())) {
                        return sst;
                    }
                }
            }
        }

        return null;
    }

    /**
     * <p>
     * convinience method for package
     * </p>
     * 
     * @param qname
     * @throws SAXException
     */
    protected Type lookUpType( String qname ) throws SAXException {
        if (qname == null)
            return null;
        Type t = null;
        t = lookUpComplexType(qname);
        t = t == null ? lookUpSimpleType(qname) : t;
        return t;
    }

    /*
     * helper method that merges the provided Schema into this Schema
     */
    private void addSchema( Schema s ) {
        Object[] objs = null;

        objs = s.getAttributes();

        if (objs != null) {
            if ((attributes == null) && (objs.length > 0)) {
                attributes = new LinkedList();
            }

            for( int i = 0; i < objs.length; i++ )
                attributes.add(objs[i]);
        }

        objs = s.getAttributeGroups();

        if (objs != null) {
            if ((attributeGroups == null) && (objs.length > 0)) {
                attributeGroups = new LinkedList();
            }

            for( int i = 0; i < objs.length; i++ )
                attributeGroups.add(objs[i]);
        }

        objs = s.getComplexTypes();

        if (objs != null) {
            if ((complexTypes == null) && (objs.length > 0)) {
                complexTypes = new LinkedList();
            }

            for( int i = 0; i < objs.length; i++ )
                complexTypes.add(objs[i]);
        }

        objs = s.getElements();

        if (objs != null) {
            if ((elements == null) && (objs.length > 0)) {
                elements = new LinkedList();
            }

            for( int i = 0; i < objs.length; i++ )
                elements.add(objs[i]);
        }

        objs = s.getGroups();

        if (objs != null) {
            if ((groups == null) && (objs.length > 0)) {
                groups = new LinkedList();
            }

            for( int i = 0; i < objs.length; i++ )
                groups.add(objs[i]);
        }

        objs = s.getImports();

        if (objs != null) {
            if ((imports == null) && (objs.length > 0)) {
                imports = new LinkedList();
            }

            for( int i = 0; i < objs.length; i++ )
                imports.add(objs[i]);
        }

        objs = s.getSimpleTypes();

        if (objs != null) {
            if ((simpleTypes == null) && (objs.length > 0)) {
                simpleTypes = new LinkedList();
            }

            for( int i = 0; i < objs.length; i++ )
                simpleTypes.add(objs[i]);
        }

        URI tempuri = s.getURI();

        if (uri == null) {
            uri = tempuri;
        } else {
            if (tempuri != null) {
                uri = tempuri.resolve(uri);
            }
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @return Returns the targetNamespace.
     */
    public URI getTargetNamespace() {
        return targetNamespace;
    }

    /**
     * @see org.geotools.xml.XSIElementHandler#getHandlerType()
     */
    public int getHandlerType() {
        return DEFAULT;
    }

    /**
     * @see org.geotools.xml.XSIElementHandler#endElement(java.lang.String,
     * java.lang.String)
     */
    public void endElement( String namespaceURI1, String localName ) {
        // do nothing
    }

    /**
     * <p>
     * Default implementation of a Schema for a parsed schema.
     * </p>
     *
     * @author dzwiers
     *
     * @see Schema
     */
    private static class DefaultSchema implements Schema {
        // file visible to avoid set* methods
        boolean attributeFormDefault;
        boolean elementFormDefault;
        String id;
        URI targetNamespace;
        String version;
        int finalDefault;
        int blockDefault;
        URI uri;
        Schema[] imports;
        SimpleType[] simpleTypes;
        ComplexType[] complexTypes;
        AttributeGroup[] attributeGroups;
        Attribute[] attributes;
        Element[] elements;
        Group[] groups;
        String prefix;

        /**
         * @see org.geotools.xml.xsi.Schema#isAttributeFormDefault()
         */
        public boolean isAttributeFormDefault() {
            return attributeFormDefault;
        }

        /**
         * @see org.geotools.xml.xsi.Schema#getAttributeGroups()
         */
        public AttributeGroup[] getAttributeGroups() {
            return attributeGroups;
        }

        /**
         * @see org.geotools.xml.xsi.Schema#getAttributeDescriptors()
         */
        public Attribute[] getAttributes() {
            return attributes;
        }

        /**
         * @see org.geotools.xml.xsi.Schema#getBlockDefault()
         */
        public int getBlockDefault() {
            return blockDefault;
        }

        /**
         * @see org.geotools.xml.xsi.Schema#getComplexTypes()
         */
        public ComplexType[] getComplexTypes() {
            return complexTypes;
        }

        /**
         * @see org.geotools.xml.xsi.Schema#isElementFormDefault()
         */
        public boolean isElementFormDefault() {
            return elementFormDefault;
        }

        /**
         * @see org.geotools.xml.xsi.Schema#getElements()
         */
        public Element[] getElements() {
            return elements;
        }

        /**
         * @see org.geotools.xml.xsi.Schema#getFinalDefault()
         */
        public int getFinalDefault() {
            return finalDefault;
        }

        /**
         * @see org.geotools.xml.xsi.Schema#getId()
         */
        public String getId() {
            return id;
        }

        /**
         * @see org.geotools.xml.xsi.Schema#getImports()
         */
        public Schema[] getImports() {
            return imports;
        }

        /**
         * @see org.geotools.xml.xsi.Schema#getSimpleTypes()
         */
        public SimpleType[] getSimpleTypes() {
            return simpleTypes;
        }

        /**
         * @see org.geotools.xml.xsi.Schema#getTargetNamespace()
         */
        public URI getTargetNamespace() {
            return targetNamespace;
        }

        /**
         * @see org.geotools.xml.xsi.Schema#getURI()
         */
        public URI getURI() {
            return uri;
        }

        /**
         * @see org.geotools.xml.xsi.Schema#getVersion()
         */
        public String getVersion() {
            return version;
        }

        /**
         * @see org.geotools.xml.xsi.Schema#getGroups()
         */
        public Group[] getGroups() {
            return groups;
        }

        /**
         * @see org.geotools.xml.xsi.Schema#includesURI(java.net.URI)
         */
        public boolean includesURI( URI uri1 ) {
            if (this.uri == null) {
                return false;
            }
            return this.uri.equals(uri1);
        }

        /**
         * @see org.geotools.xml.schema.Schema#getPrefix()
         */
        public String getPrefix() {
            return prefix;
        }

        /**
         * Returns the implementation hints. The default implementation returns en empty map.
         */
        public Map getImplementationHints() {
            return Collections.EMPTY_MAP;
        }
    }

    /**
     *
     * This class breaks both the collections api and the comparable api.
     * When an object is a temp ... thus we don't care ... it's always less
     than.
     * Please do not use this unless you fully understand it. It is intended to
     * be used for compressing two schemas and to remove duplicates resulting
     values (not placeholders).
     * Making this evaluate place holders would cause the parser to fail !!!
     *
     * @author dzwiers
     *
     */
    private static class SchemaComparator implements Comparator {
        private static SchemaComparator instance = new SchemaComparator();
        public static SchemaComparator getInstance() {
            return instance;
        }
        /* (non-Javadoc)
         * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
         */
        public int compare( Object arg0, Object arg1 ) {
            // attribute
            if (arg0 instanceof Attribute && arg1 instanceof Attribute)
                return compareAttribute((Attribute) arg0, (Attribute) arg1);
            // attrbute group
            if (arg0 instanceof AttributeGroup && arg1 instanceof AttributeGroup)
                return compareAttributeGroup((AttributeGroup) arg0, (AttributeGroup) arg1);
            // complex type
            if (arg0 instanceof ComplexType && arg1 instanceof ComplexType)
                return compareComplexType((ComplexType) arg0, (ComplexType) arg1);
            // simpletype
            if (arg0 instanceof SimpleType && arg1 instanceof SimpleType)
                return compareSimpleType((SimpleType) arg0, (SimpleType) arg1);
            // group
            if (arg0 instanceof Group && arg1 instanceof Group)
                return compareGroup((Group) arg0, (Group) arg1);
            // element
            if (arg0 instanceof Element && arg1 instanceof Element)
                return compareElement((Element) arg0, (Element) arg1);
            // imports
            if (arg0 instanceof Schema && arg1 instanceof Schema)
                return compareImport((Schema) arg0, (Schema) arg1);

            return -1; // hack for unresolved portions
            // throw new ClassCastException("Unknown type "+arg0.getClass().getName());
        }

        private int compareAttribute( Attribute arg0, Attribute arg1 ) {
            if (arg0 == arg1)
                return 0;
            if (arg0 == null)
                return 1;
            if (arg1 == null)
                return -1;
            int i = arg0.getName() == null ? arg1.getName() == null ? 0 : 1 : arg0.getName().compareTo(arg1.getName());
            if (i != 0)
                return i;
            i = arg0.getNamespace() == null ? arg1.getNamespace() == null ? 0 : 1 : arg0.getNamespace().compareTo(
                    arg1.getNamespace());
            if (i != 0)
                return i;
            i = arg0.getUse() - arg1.getUse();
            if (i != 0)
                return i;
            return compareSimpleType(arg0.getSimpleType(), arg1.getSimpleType());
        }

        private int compareAttributeGroup( AttributeGroup arg0, AttributeGroup arg1 ) {
            if (arg0 == arg1)
                return 0;
            if (arg0 == null)
                return 1;
            if (arg1 == null)
                return -1;
            int i = arg0.getName() == null ? arg1.getName() == null ? 0 : 1 : arg0.getName().compareTo(arg1.getName());
            if (i != 0)
                return i;
            i = arg0.getNamespace() == null ? arg1.getNamespace() == null ? 0 : 1 : arg0.getNamespace().compareTo(
                    arg1.getNamespace());
            if (i != 0)
                return i;
            i = arg0.getAnyAttributeNameSpace() == null ? arg1.getAnyAttributeNameSpace() == null ? 0 : 1 : arg0
                    .getAnyAttributeNameSpace().compareTo(arg1.getAnyAttributeNameSpace());
            if (i != 0)
                return i;

            Attribute[] a0 = arg0.getAttributes();
            Arrays.sort(a0, this);
            Attribute[] a1 = arg1.getAttributes();
            Arrays.sort(a1, this);

            if (a0 == a1)
                return 0;
            if (a0 == null)
                return 1;
            if (a1 == null)
                return -1;

            if (a0.length < a1.length)
                return -1;
            if (a0.length > a1.length)
                return 1;

            for( int j = 0; j < a0.length && i == 0; j++ )
                i = compareAttribute(a0[j], a1[j]);

            return i;
        }

        private int compareGroup( Group arg0, Group arg1 ) {
            if (arg0 == arg1)
                return 0;
            if (arg0 == null)
                return 1;
            if (arg1 == null)
                return -1;
            int i = arg0.getName() == null ? arg1.getName() == null ? 0 : 1 : arg0.getName().compareTo(arg1.getName());
            if (i != 0)
                return i;
            i = arg0.getNamespace() == null ? arg1.getNamespace() == null ? 0 : 1 : arg0.getNamespace().compareTo(
                    arg1.getNamespace());
            if (i != 0)
                return i;

            i = arg0.getMaxOccurs() - arg1.getMaxOccurs();
            if (i != 0)
                return i;

            i = arg0.getMinOccurs() - arg1.getMinOccurs();
            if (i != 0)
                return i;

            ElementGrouping a0 = arg0.getChild();
            ElementGrouping a1 = arg1.getChild();

            if (a0 == a1)
                return 0;
            if (a0 == null)
                return 1;
            if (a1 == null)
                return -1;

            return compareElementGrouping(a0, a1);
        }

        private int compareElement( Element arg0, Element arg1 ) {
            if (arg0 == arg1)
                return 0;
            if (arg0 == null)
                return 1;
            if (arg1 == null)
                return -1;
            int i = arg0.getName() == null ? arg1.getName() == null ? 0 : 1 : arg0.getName().compareTo(arg1.getName());
            if (i != 0)
                return i;
            i = arg0.getNamespace() == null ? arg1.getNamespace() == null ? 0 : 1 : arg0.getNamespace().compareTo(
                    arg1.getNamespace());
            if (i != 0)
                return i;

            i = arg0.getMaxOccurs() - arg1.getMaxOccurs();
            if (i != 0)
                return i;

            i = arg0.getMinOccurs() - arg1.getMinOccurs();
            if (i != 0)
                return i;

            i = compareElement(arg0.getSubstitutionGroup(), arg1.getSubstitutionGroup());
            if (i != 0)
                return i;

            // ignore a few things here ... might need them back

            return compare(arg0.getType(), arg1.getType());
        }

        private int compareElementGrouping( ElementGrouping arg0, ElementGrouping arg1 ) {
            if (arg0 == arg1)
                return 0;
            if (arg0 == null)
                return 1;
            if (arg1 == null)
                return -1;

            int i = 0;
            i = arg0.getGrouping() - arg1.getGrouping();
            if (i != 0)
                return i;

            i = arg0.getMaxOccurs() - arg1.getMaxOccurs();
            if (i != 0)
                return i;

            i = arg0.getMinOccurs() - arg1.getMinOccurs();
            if (i != 0)
                return i;

            ElementGrouping[] eg0 = null;
            ElementGrouping[] eg1 = null;

            switch( arg0.getGrouping() ) {
            case ElementGrouping.ELEMENT:
                return compareElement((Element) arg0, (Element) arg1);
            case ElementGrouping.GROUP:
                return compareGroup((Group) arg0, (Group) arg1);
            case ElementGrouping.CHOICE:
                Choice c0 = (Choice) arg0;
                Choice c1 = (Choice) arg1;
                eg0 = c0.getChildren();
                eg1 = c1.getChildren();
            case ElementGrouping.SEQUENCE:
                Sequence s0 = (Sequence) arg0;
                Sequence s1 = (Sequence) arg1;
                eg0 = s0.getChildren();
                eg1 = s1.getChildren();
            case ElementGrouping.ALL:
                All a0 = (All) arg0;
                All a1 = (All) arg1;
                eg0 = a0.getElements();
                eg1 = a1.getElements();
            }

            if (eg0.length < eg1.length)
                return -1;
            if (eg0.length > eg1.length)
                return 1;

            for( int j = 0; j < eg0.length && i != 0; j++ )
                i = compareElementGrouping(eg0[j], eg1[j]);
            return 0;
        }

        private int compareImport( Schema arg0, Schema arg1 ) {
            if (arg0 == arg1)
                return 0;
            if (arg0 == null)
                return 1;
            if (arg1 == null)
                return -1;
            int i = arg0.getTargetNamespace() == null ? arg1.getTargetNamespace() == null ? 0 : 1 : arg0
                    .getTargetNamespace().compareTo(arg1.getTargetNamespace());
            if (i != 0)
                return i;
            i = arg0.getURI() == null ? arg1.getURI() == null ? 0 : 1 : arg0.getURI().compareTo(arg1.getURI());
            if (i != 0)
                return i;

            i = arg0.getElements().length - arg1.getElements().length;
            if (i != 0)
                return i;

            i = arg0.getComplexTypes().length - arg1.getComplexTypes().length;
            if (i != 0)
                return i;

            i = arg0.getSimpleTypes().length - arg1.getSimpleTypes().length;
            if (i != 0)
                return i;

            i = arg0.getAttributes().length - arg1.getAttributes().length;
            if (i != 0)
                return i;

            i = arg0.getAttributeGroups().length - arg1.getAttributeGroups().length;
            if (i != 0)
                return i;

            i = arg0.getGroups().length - arg1.getGroups().length;
            if (i != 0)
                return i;

            // Yes we are making a huge assumption here.

            return 0;
        }

        private int compareSimpleType( SimpleType arg0, SimpleType arg1 ) {
            if (arg0 == arg1)
                return 0;
            if (arg0 == null)
                return 1;
            if (arg1 == null)
                return -1;
            int i = arg0.getName() == null ? arg1.getName() == null ? 0 : 1 : arg0.getName().compareTo(arg1.getName());
            if (i != 0)
                return i;
            i = arg0.getNamespace() == null ? arg1.getNamespace() == null ? 0 : 1 : arg0.getNamespace().compareTo(
                    arg1.getNamespace());
            if (i != 0)
                return i;

            SimpleType[] a0 = arg0.getParents();
            Arrays.sort(a0, this);
            SimpleType[] a1 = arg1.getParents();
            Arrays.sort(a1, this);

            if (a0 == a1)
                return 0;
            if (a0 == null)
                return 1;
            if (a1 == null)
                return -1;

            if (a0.length < a1.length)
                return -1;
            if (a0.length > a1.length)
                return 1;

            for( int j = 0; j < a0.length && i == 0; j++ )
                i = compareSimpleType(a0[j], a1[j]);

            Facet[] a01 = arg0.getFacets();
            Arrays.sort(a0, this);
            Facet[] a11 = arg1.getFacets();
            Arrays.sort(a1, this);

            if (a01 == a11)
                return 0;
            if (a01 == null)
                return 1;
            if (a11 == null)
                return -1;

            if (a01.length < a11.length)
                return -1;
            if (a01.length > a11.length)
                return 1;

            for( int j = 0; j < a01.length && i == 0; j++ )
                i = compareFacet(a01[j], a11[j]);

            return i;
        }

        private int compareFacet( Facet arg0, Facet arg1 ) {
            if (arg0 == arg1)
                return 0;
            if (arg0 == null)
                return 1;
            if (arg1 == null)
                return -1;

            int i = 0;
            i = arg0.getFacetType() - arg1.getFacetType();
            if (i != 0)
                return i;

            i = arg0.getValue() == null ? arg1.getValue() == null ? 0 : 1 : arg0.getValue().compareTo(arg1.getValue());
            return i;
        }

        private int compareComplexType( ComplexType arg0, ComplexType arg1 ) {
            if (arg0 == arg1)
                return 0;
            if (arg0 == null)
                return 1;
            if (arg1 == null)
                return -1;
            int i = arg0.getName() == null ? arg1.getName() == null ? 0 : 1 : arg0.getName().compareTo(arg1.getName());
            if (i != 0)
                return i;
            i = arg0.getNamespace() == null ? arg1.getNamespace() == null ? 0 : 1 : arg0.getNamespace().compareTo(
                    arg1.getNamespace());
            if (i != 0)
                return i;

            Type a00 = arg0.getParent();
            Type a01 = arg1.getParent();

            if (a00 == a01)
                return 0;
            if (a00 == null)
                return 1;
            if (a01 == null)
                return -1;

            i = compare(a00, a01);
            if (i != 0)
                return i;

            i = arg0.getAnyAttributeNameSpace() == null ? arg1.getAnyAttributeNameSpace() == null ? 0 : 1 : arg0
                    .getAnyAttributeNameSpace().compareTo(arg1.getAnyAttributeNameSpace());
            if (i != 0)
                return i;

            Attribute[] a10 = arg0.getAttributes();
            Arrays.sort(a10, this);
            Attribute[] a11 = arg1.getAttributes();
            Arrays.sort(a11, this);

            if (a10 == a11)
                return 0;
            if (a10 == null)
                return 1;
            if (a11 == null)
                return -1;

            if (a10.length < a11.length)
                return -1;
            if (a10.length > a11.length)
                return 1;

            for( int j = 0; j < a10.length && i == 0; j++ )
                i = compareAttribute(a10[j], a11[j]);
            if (i != 0)
                return i;

            Element[] a0 = arg0.getChildElements();
            Arrays.sort(a0, this);
            Element[] a1 = arg1.getChildElements();
            Arrays.sort(a1, this);

            if (a0 == a1)
                return 0;
            if (a0 == null)
                return 1;
            if (a1 == null)
                return -1;

            if (a0.length < a1.length)
                return -1;
            if (a0.length > a1.length)
                return 1;

            for( int j = 0; j < a0.length && i == 0; j++ )
                i = compareElement(a0[j], a1[j]);

            return i;
        }
    }
}
