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
package org.geotools.xsd.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import javax.xml.namespace.QName;
import org.eclipse.xsd.XSDAttributeDeclaration;
import org.eclipse.xsd.XSDComplexTypeDefinition;
import org.eclipse.xsd.XSDElementDeclaration;
import org.eclipse.xsd.XSDFactory;
import org.eclipse.xsd.XSDSchemaContent;
import org.eclipse.xsd.XSDSimpleTypeDefinition;
import org.eclipse.xsd.XSDTypeDefinition;
import org.eclipse.xsd.util.XSDConstants;
import org.eclipse.xsd.util.XSDUtil;
import org.geotools.xsd.AttributeInstance;
import org.geotools.xsd.Binding;
import org.geotools.xsd.ElementInstance;
import org.geotools.xsd.InstanceComponent;
import org.geotools.xsd.Node;
import org.geotools.xsd.SchemaIndex;
import org.geotools.xsd.Schemas;
import org.geotools.xsd.Text;
import org.geotools.xsd.TextInstance;
import org.picocontainer.defaults.DefaultPicoContainer;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

public class ElementHandlerImpl extends HandlerImpl implements ElementHandler {
    /** parent handler * */
    Handler parent;

    /** the element declaration * */
    XSDElementDeclaration content;

    /** the element instance * */
    ElementImpl element;

    /** the running parser */
    ParserHandler parser;

    /** the element type strategy * */
    Binding strategy;

    /** child handlers * */
    // ArrayList childHandlers;

    /** parse tree for the element * */
    NodeImpl node;

    /** parsed value * */
    Object value;

    public ElementHandlerImpl(XSDElementDeclaration content, Handler parent, ParserHandler parser) {
        this.content = content;
        this.parent = parent;
        this.parser = parser;

        // childHandlers = new ArrayList();
    }

    public void startElement(QName qName, Attributes attributes) throws SAXException {
        // clear handler list
        // childHandlers.clear();

        // create the attributes
        List atts = new ArrayList();

        for (int i = 0; i < attributes.getLength(); i++) {
            String rawAttQName = attributes.getQName(i);

            if (rawAttQName != null) {
                // ignore namespace declarations
                if (rawAttQName.startsWith("xmlns:")) {
                    continue;
                }

                // ignore xsi:schemaLocation
                if (rawAttQName.endsWith("schemaLocation")) {
                    String prefix = "";

                    if (rawAttQName.indexOf(':') != -1) {
                        prefix = rawAttQName.substring(0, rawAttQName.indexOf(':'));
                    }

                    String uri = parser.getNamespaceSupport().getURI(prefix);

                    if ((uri != null) && uri.equals(XSDConstants.SCHEMA_INSTANCE_URI_2001)) {
                        continue;
                    }
                }
            }

            //        	String qName = attributes.getQName(i);
            //
            //
            //        	//ignore schema location attribute
            //        	if ( attributes.getQName(i) != null && attributes.getQName(index))
            //
            String uri = attributes.getURI(i);
            String name = attributes.getLocalName(i);

            QName attQName = new QName(uri, name);

            XSDAttributeDeclaration decl = Schemas.getAttributeDeclaration(content, attQName);

            if (decl == null) {
                // check wether unknown attributes should be parsed
                if (!parser.isStrict()) {
                    if (parser.getLogger().isLoggable(Level.FINE)) {
                        parser.getLogger().fine("Parsing unknown attribute: " + attQName);
                    }

                    // create a mock attribute and continue
                    decl = XSDFactory.eINSTANCE.createXSDAttributeDeclaration();
                    decl.setName(attQName.getLocalPart());
                    decl.setTargetNamespace(attQName.getNamespaceURI());

                    // set the type to be of string
                    XSDSimpleTypeDefinition type =
                            (XSDSimpleTypeDefinition)
                                    XSDUtil.getSchemaForSchema(XSDUtil.SCHEMA_FOR_SCHEMA_URI_2001)
                                            .getSimpleTypeIdMap()
                                            .get("string");

                    decl.setTypeDefinition(type);
                }
            }

            // TODO: validate, if there is no declaration for an attribute, then
            // TODO: make sure no required attributes are missing
            // validation should fail, this is being side stepped for now until
            // a good way of handling the namespace attributes on the root
            // element, for now we just ignore attributes we dont find in the
            // schema
            if (decl != null) {
                AttributeInstance att = new AttributeImpl(decl);
                att.setNamespace(decl.getTargetNamespace());
                att.setName(decl.getName());
                att.setText(attributes.getValue(i));

                atts.add(att);
            } else {
                parser.getLogger().warning("Could not find attribute declaration: " + attQName);
            }
        }

        // create the element
        element = new ElementImpl(content);
        element.setNamespace(qName.getNamespaceURI());
        element.setName(qName.getLocalPart());
        element.setAttributes(
                (AttributeInstance[]) atts.toArray(new AttributeInstance[atts.size()]));

        // create the parse tree for the node
        node = new NodeImpl(element);

        // parse the attributes
        for (int i = 0; i < element.getAttributes().length; i++) {
            AttributeInstance attribute = element.getAttributes()[i];
            ParseExecutor executor =
                    new ParseExecutor(attribute, null, parent.getContext(), parser);

            parser.getBindingWalker()
                    .walk(attribute.getAttributeDeclaration(), executor, parent.getContext());

            Object parsed = executor.getValue();
            node.addAttribute(new NodeImpl(attribute, parsed));
        }

        // trigger the leading edge initialize callback
        ElementInitializer initer = new ElementInitializer(element, node, parent.getContext());
        parser.getBindingWalker()
                .walk(element.getElementDeclaration(), initer, container(), parent.getContext());

        // create context for children
        // TODO: this should only be done if the element is complex, this class
        // needs to be split into two, one for complex, other for simple
        setContext(new DefaultPicoContainer(parent.getContext()));

        // set the context on the binding factory
        ((BindingFactoryImpl) parser.getBindingFactory()).setContext(getContext());

        // "start" the child handler
        parent.startChildHandler(this);

        //        ContextInitializer initer = new ContextInitializer(element, node,
        //                getContext());
        //        parser.getBindingWalker().walk(element .getElementDeclaration(), initer,
        // getContext() );
    }

    public void characters(char[] ch, int start, int length) throws SAXException {

        // simply add the text to the element
        element.addText(ch, start, length);

        if (isMixed()) {
            String text = new String(ch, start, length);
            node.addChild(new NodeImpl(TextInstance.INSTANCE, new Text(text)));
        }
    }

    public void endElement(QName qName) throws SAXException {
        if (isMixed()) {
            ((NodeImpl) node).collapseWhitespace();
        }

        if (isNil(element)) {
            value = null;
        } else {
            ParseExecutor executor =
                    new ParseExecutor(element, node, getParentHandler().getContext(), parser);

            parser.getBindingWalker()
                    .walk(
                            element.getElementDeclaration(),
                            executor,
                            container(),
                            getParentHandler().getContext());

            // cache the parsed value
            value = executor.getValue();

            if (value == null) {
                // TODO: instead of continuing, just remove the element from
                // the parent, or figure out if the element is 'optional' and
                // remove
                if (parser.getLogger().isLoggable(Level.FINE)) {
                    parser.getLogger().fine("Binding for " + element.getName() + " returned null");
                }
            }
        }

        // set the value for this node in the parse tree
        node.setValue(value);

        // end this child handler
        parent.endChildHandler(this);

        // kill the context
        parent.getContext().removeChildContainer(getContext());
    }

    /** Checks if a certain attribute is nil */
    private boolean isNil(ElementImpl element) {
        for (AttributeInstance att : element.getAttributes()) {
            if ("nil".equals(att.getName())
                    && "http://www.w3.org/2001/XMLSchema-instance".equals(att.getNamespace())) {
                return "true".equals(att.getText());
            }
        }

        return false;
    }

    public Handler createChildHandler(QName qName) {
        return getChildHandlerInternal(qName);
    }

    private Handler getChildHandlerInternal(QName qName) {
        SchemaIndex index = parser.getSchemaIndex();

        XSDElementDeclaration element = index.getChildElement(content, qName);

        if (element != null) {
            // TODO: determine wether the element is complex or simple, and create
            ElementHandler handler =
                    parser.getHandlerFactory().createElementHandler(element, this, parser);

            return handler;
        }

        // could not find the element as a direct child of the parent, check
        // for a global element, and then check its substituation group
        element = index.getElementDeclaration(qName);

        if (element != null) {
            XSDElementDeclaration sub = element.getSubstitutionGroupAffiliation();

            if (sub != null) {
                QName subQName = new QName(sub.getTargetNamespace(), sub.getName());
                Handler handler = getChildHandlerInternal(subQName);

                if (handler != null) {
                    // this means that hte element is substituatable for an
                    // actual child. now we have have choice, do we return
                    // a handler for the actual element, or the element it
                    // substituable for - the answer is to check the bindings
                    // TODO: ask the binding
                    handler =
                            parser.getHandlerFactory().createElementHandler(element, this, parser);

                    return handler;
                }
            }
        }

        // if
        return null;
    }

    private XSDTypeDefinition container() {
        // get the containing type (we do this for anonymous complex types)
        XSDTypeDefinition container = null;
        if (getParentHandler().getComponent() != null) {
            container = getParentHandler().getComponent().getTypeDefinition();
        }
        return container;
    }

    //    public List getChildHandlers() {
    //        return childHandlers;
    //    }
    public void startChildHandler(Handler child) {
        // childHandlers.add(child);
        node.addChild(child.getParseNode());

        // initialize the context for the handler
        if (child instanceof ElementHandler) {
            // get the containing type (we do this for anonymous complex types)
            ElementInstance childInstance = (ElementInstance) child.getComponent();
            ContextInitializer initer =
                    new ContextInitializer(childInstance, node, child.getContext());
            parser.getBindingWalker()
                    .walk(element.getElementDeclaration(), initer, container(), getContext());
        }
    }

    public void endChildHandler(Handler child) {
        // add the node to the parse tree
        // childHandlers.remove(child);
    }

    public Handler getParentHandler() {
        return parent;
    }

    public XSDSchemaContent getSchemaContent() {
        return content;
    }

    public Node getParseNode() {
        return node;
    }

    public XSDElementDeclaration getElementDeclaration() {
        return content;
    }

    public InstanceComponent getComponent() {
        return element;
    }

    public void setComponent(ElementImpl element) {
        this.element = element;
    }

    public Object getValue() {
        return value;
    }

    boolean isMixed() {
        if (!parser.isHandleMixedContent()) {
            return false;
        }

        return content.getType() != null
                && content.getType() instanceof XSDComplexTypeDefinition
                && ((XSDComplexTypeDefinition) content.getType()).isMixed();
    }

    public String toString() {
        return (node != null) ? node.toString() : "";
    }
}
