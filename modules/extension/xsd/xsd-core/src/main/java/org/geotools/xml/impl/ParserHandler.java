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
package org.geotools.xml.impl;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Stack;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.xml.namespace.QName;

import org.eclipse.xsd.XSDElementDeclaration;
import org.eclipse.xsd.XSDFactory;
import org.eclipse.xsd.XSDImport;
import org.eclipse.xsd.XSDParticle;
import org.eclipse.xsd.XSDSchema;
import org.eclipse.xsd.XSDTypeDefinition;
import org.eclipse.xsd.util.XSDSchemaLocationResolver;
import org.eclipse.xsd.util.XSDSchemaLocator;
import org.geotools.xml.BindingFactory;
import org.geotools.xml.Configuration;
import org.geotools.xml.ElementInstance;
import org.geotools.xml.ParserDelegate;
import org.geotools.xml.SchemaIndex;
import org.geotools.xml.Schemas;
import org.geotools.xs.XS;
import org.picocontainer.MutablePicoContainer;
import org.picocontainer.defaults.DefaultPicoContainer;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import org.xml.sax.helpers.DefaultHandler;
import org.xml.sax.helpers.NamespaceSupport;


/**
 *
 * The main sax event handler used for parsing the input document. This handler
 * maintains a stack of {@link Handler} objects. A handler is purshed onto the stack
 * when a startElement event is processed, and popped off the stack when the corresponding
 * endElement event is processed.
 *
 * @author Justin Deoliveira,Refractions Research Inc.,jdeolive@refractions.net
 *
 *
 *
 * @source $URL$
 */
public class ParserHandler extends DefaultHandler {
    /** execution stack **/
    protected Stack handlers;

    /** namespace support **/
    NamespaceSupport namespaces;

    /** imported schemas **/
    XSDSchema[] schemas;

    /** index used to look up schema elements **/
    SchemaIndex index;

    /** handler factory **/
    HandlerFactory handlerFactory;

    /** binding loader */
    BindingLoader bindingLoader;

    /** bindign walker */
    BindingWalker bindingWalker;

    /**
     * binding factory
     */
    BindingFactory bindingFactory;

    /** the document handler **/
    DocumentHandler documentHandler;

    /** parser config **/
    Configuration config;

    /** context, container **/
    MutablePicoContainer context;

    /** logger **/
    Logger logger;

    /** flag to indicate if the parser should validate or not */
    boolean validating;
    
    /** handler for validation errors */
    ValidatorHandler validator;
    
    /** whether the parser is strict or not */
    boolean strict = false;
    
    /** whether the parser should maintain order for elements with mixed content */ 
    boolean handleMixedContent = false;
    
    public ParserHandler(Configuration config) {
        this.config = config;
        namespaces = new NamespaceSupport();
        validating = false;
        validator = new ValidatorHandler();
    }

    public Configuration getConfiguration() {
        return config;
    }

    public void setStrict(boolean strict) {
        this.strict = strict;
    }

    public boolean isStrict() {
        return strict;
    }

    public boolean isValidating() {
        return validating;
    }

    public void setValidating(boolean validating) {
        this.validating = validating;
    }
    
    public void setFailOnValidationError( boolean failOnValidationError ) {
        validator.setFailOnValidationError(failOnValidationError);
    }
    
    public boolean isFailOnValidationError() {
        return validator.isFailOnValidationError();
    }

    public void setHandleMixedContent(boolean handleMixedContent) {
        this.handleMixedContent = handleMixedContent;
    }
    
    public boolean isHandleMixedContent() {
        return handleMixedContent; 
    }
    
    public List getValidationErrors() {
        return validator.getErrors();
    }
    
    public ValidatorHandler getValidator() {
        return validator;
    }

    public HandlerFactory getHandlerFactory() {
        return handlerFactory;
    }

    public BindingLoader getBindingLoader() {
        return bindingLoader;
    }

    public BindingWalker getBindingWalker() {
        return bindingWalker;
    }

    public BindingFactory getBindingFactory() {
        return bindingFactory;
    }

    public XSDSchema[] getSchemas() {
        return schemas;
    }

    public SchemaIndex getSchemaIndex() {
        return index;
    }

    public Logger getLogger() {
        return logger;
    }

    public NamespaceSupport getNamespaceSupport() {
        return namespaces;
    }

    public void startPrefixMapping(String prefix, String uri)
        throws SAXException {
        namespaces.declarePrefix(prefix, uri);
    }

    public void startDocument() throws SAXException {
        //perform teh configuration
        configure(config);

        //create the document handler + root context
        DocumentHandler docHandler = handlerFactory.createDocumentHandler(this);

        context = new DefaultPicoContainer();
        context = config.setupContext(context);

        docHandler.setContext(context);

        // create the stack and add handler for document element
        handlers = new Stack();
        handlers.push(docHandler);

        // get a logger from the context
        logger = (Logger) context.getComponentInstanceOfType(Logger.class);

        if (logger == null) {
            //create a default
            logger = org.geotools.util.logging.Logging.getLogger("org.geotools.xml");
            context.registerComponentInstance(logger);
        }

        //setup the namespace support
        context.registerComponentInstance(namespaces);
        context.registerComponentInstance(new NamespaceSupportWrapper(namespaces));

        //binding factory support
        bindingFactory = new BindingFactoryImpl(bindingLoader);
        context.registerComponentInstance(bindingFactory);

        //binding walker support
        context.registerComponentInstance(new BindingWalkerFactoryImpl(bindingLoader, context));
        
        //register configuration itself
        context.registerComponentInstance( config );

        validator.startDocument();
        docHandler.startDocument();
    }

    public void startElement(String uri, String localName, String qName, Attributes attributes)
        throws SAXException {
        if (logger.isLoggable(Level.FINEST)) {
            logger.finest("startElement(" + uri + "," + localName + "," + qName);
        }

        if (schemas == null) {
            //root element, parse the schema
            //TODO: this processing is too loose, do some validation will ya!
            String[] locations = null;

            for (int i = 0; i < attributes.getLength(); i++) {
                String name = attributes.getQName(i);

                if (name.endsWith("schemaLocation")) {
                    logger.finer( "schemaLocation found: " + attributes.getValue( i ) );
                    
                    //create an array of alternating namespace, location pairs
                    locations = attributes.getValue(i).split(" +");

                    break;
                }
            }

            //            }
            if (!isStrict() && (locations == null)) {
                //use the configuration
                logger.finer( "No schemaLocation found, using '" + config.getNamespaceURI() + " " + config.getSchemaFileURL()  );
                locations = new String[] { config.getNamespaceURI(), config.getSchemaFileURL() };
            }

            //look up schema overrides
            XSDSchemaLocator[] locators = findSchemaLocators();
            XSDSchemaLocationResolver[] resolvers = findSchemaLocationResolvers();

            if ((locations != null) && (locations.length > 0)) {
                //parse each namespace location pair into schema objects
                schemas = new XSDSchema[locations.length / 2];

                for (int i = 0; i < locations.length; i += 2) {
                    String namespace = locations[i];
                    String location = null;
                    if (i+1 < locations.length) {
                        location = locations[i + 1];
                    }
                    else {
                        logger.warning("Schema location not specified as namespace/location pair. "
                            + "Ignoring " + namespace);
                        continue;
                    }

                    //first check for a location override
                    for (int j = 0; j < resolvers.length; j++) {
                        String override = resolvers[j].resolveSchemaLocation(null, namespace,
                                location);
                        if (override != null) {
                            //ensure that override has no spaces
                            override = override.replaceAll( " ", "%20" ); 
                            logger.finer( "Found override for " + namespace  + 
                                ": " + location + " ==> " + override );
                            location = override;

                            break;
                        }
                    }

                    //next check for schema override 
                    for (int j = 0; j < locators.length; j++) {
                        XSDSchema schema = locators[j].locateSchema(null, namespace, location, null);

                        if (schema != null) {
                            schemas[i / 2] = schema;

                            break;
                        }
                    }

                    //if no schema override was found, parse location directly
                    if (schemas[i / 2] == null) {
                        //validate the schema location
                        if ( isValidating() ) {
                            try {
                                Schemas.validateImportsIncludes(location,locators,resolvers);
                            } 
                            catch (IOException e) {
                                throw (SAXException) new SAXException( "error validating" ).initCause(e);
                            }    
                        }
                        
                        //parse the document
                        try {
                            schemas[i / 2] = Schemas.parse(location, locators, resolvers);
                        } catch (Exception e) {
                            String msg = "Error parsing: " + location;
                            logger.warning(msg);

                            if (isStrict()) {
                                //strict mode, throw exception
                                throw (SAXException) new SAXException(msg).initCause(e);
                            }
                        }
                    }
                }
            } else {
                //could not find a schemaLocation attribute, use the locators
                //look for schema with locators
                for (int i = 0; i < locators.length; i++) {
                    XSDSchema schema = locators[i].locateSchema(null, uri, null, null);

                    if (schema != null) {
                        schemas = new XSDSchema[] { schema };

                        break;
                    }
                }
            }

            //strip out any null schemas
            int n = 0;

            for (int i = 0; i < schemas.length; i++) {
                if (schemas[i] != null) {
                    n++;
                }
            }

            if (n != schemas.length) {
                XSDSchema[] nschemas = new XSDSchema[n];
                int j = 0;

                for (int i = 0; i < schemas.length; i++) {
                    if (schemas[i] == null) {
                        continue;
                    }

                    nschemas[j++] = schemas[i];
                }

                schemas = nschemas;
            }

            if ((schemas == null) || (schemas.length == 0)) {
                logger.warning("Could not find a schema");

                if (isStrict()) {
                    //crap out
                    String msg = "Could not find a schemaLocation attribute or "
                        + "appropriate locator";
                    throw new SAXException(msg);
                } else {
                    //just use the schema from configuration
                    try {
                        schemas = new XSDSchema[] { config.getXSD().getSchema() };
                    } catch (IOException e) {
                        throw (SAXException) new SAXException().initCause(e);
                    }
                }
            }

            //check to make sure that the schemas that were created include
            // the schema for the parser configuration
            boolean found = false;

O:          for (int i = 0; i < schemas.length; i++) {
                if ( config.getNamespaceURI().equals( schemas[i].getTargetNamespace()) ) {
                    found = true;
                    break O;
                }
                
                List imports = Schemas.getImports(schemas[i]);

                for (Iterator im = imports.iterator(); im.hasNext();) {
                    XSDImport imprt = (XSDImport) im.next();

                    if (config.getNamespaceURI().equals(imprt.getNamespace())) {
                        found = true;

                        break O;
                    }
                }
            }

            if (!found) {
                //add it if not operating in strict mode
                if (!isStrict()) {
                    logger.fine(
                        "schema specified by parser configuration not found, supplementing...");

                    XSDSchema[] copy = new XSDSchema[schemas.length + 1];
                    System.arraycopy(schemas, 0, copy, 0, schemas.length);
                    copy[schemas.length] = config.schema();
                    schemas = copy;
                } else {
                    String msg = "parser configuration specified schema: '"
                        + config.getNamespaceURI()
                        + "', but instance document does not reference this schema.";
                    logger.info(msg);
                }
            }

            index = new SchemaIndexImpl(schemas);
            context.registerComponentInstance(index);
            
            //if no default prefix is set in this namespace context, then 
            // set it to be the namesapce of the configuration
            if (namespaces.getURI("") == null) {
                namespaces.declarePrefix("", config.getNamespaceURI());
            }
        }

        //set up a new namespace context
        namespaces.pushContext();

        //create a qName object from the string
        if ((uri == null) || uri.equals("")) {
            uri = namespaces.getURI("");
        }

        QName qualifiedName = new QName(uri, localName);

        //get the handler at top of the stack and lookup child

        //First ask teh parent handler for a child
        Handler parent = (Handler) handlers.peek();
        ElementHandler handler = (ElementHandler) parent.createChildHandler(qualifiedName);

        if (handler == null) {
            //look for a global element
            XSDElementDeclaration element = index.getElementDeclaration(qualifiedName);

            if (element != null) {
                handler = handlerFactory.createElementHandler(element, parent, this);
            }
        }

        if (handler == null) {
            //perform a lookup in the context for an element factory that create a child handler
            List handlerFactories = context.getComponentInstancesOfType(HandlerFactory.class);

            for (Iterator hf = handlerFactories.iterator(); (handler == null) && hf.hasNext();) {
                HandlerFactory handlerFactory = (HandlerFactory) hf.next();
                handler = handlerFactory.createElementHandler(qualifiedName, parent, this);
            }
        }

        if (handler == null) {
            //look for ParserDelegate instances in the context to see if there is a delegate
            // around to handle this
            List delegates = Schemas.getComponentInstancesOfType(context,ParserDelegate.class);
            for ( Iterator d = delegates.iterator(); d.hasNext(); ) {
                ParserDelegate delegate = (ParserDelegate) d.next();
                if ( delegate.canHandle( qualifiedName ) ) {
                    //found one
                    handler = new DelegatingHandler( delegate, qualifiedName, parent );
                    ((DelegatingHandler)handler).startDocument();
                }
                
            }
        }
        if (handler == null) {
            //if the type only contains one type of element, just assume the 
            // the element is of that type
            //if( context.getComponentInstance( Parser.Properties.PARSE_UNKNOWN_ELEMENTS ) != null) {
            if (!isStrict()) {
                if (logger.isLoggable(Level.FINE)) {
                    logger.fine("Could not find declaration for: " + qualifiedName
                        + ". Checking if containing type declares a single particle.");
                }

                if (parent.getComponent() instanceof ElementInstance) {
                    ElementInstance parentElement = (ElementInstance) parent.getComponent();
                    List childParticles = index.getChildElementParticles(parentElement
                            .getElementDeclaration());

                    if (childParticles.size() == 1) {
                        XSDParticle particle = (XSDParticle) childParticles.iterator().next();
                        XSDElementDeclaration child = (XSDElementDeclaration) particle.getContent();

                        if (child.isElementDeclarationReference()) {
                            child = child.getResolvedElementDeclaration();
                        }

                        handler = handlerFactory.createElementHandler(new QName(
                                    child.getTargetNamespace(), child.getName()), parent, this);
                    }
                }
            }
        }

        if (handler == null) {
            //check the case of where the namespace is wrong, do a lookup from 
            // the parent just on local name
            if (!isStrict()) {
                String msg = "Could not find declaration for: " + qualifiedName
                    + ". Performing lookup by ignoring namespace";
                logger.fine(msg);

                // * = match any namespace
                handler = (ElementHandler) parent.createChildHandler(new QName("*",
                            qualifiedName.getLocalPart()));
            }
        }

        if (handler == null) {
            //check the parser flag, and just parse it anyways
            //if( context.getComponentInstance( Parser.Properties.PARSE_UNKNOWN_ELEMENTS ) != null) {
            if (!isStrict()) {
                String msg = "Could not find declaration for: " + qualifiedName
                    + ". Creating a mock element declaration and parsing anyways...";
                logger.fine(msg);

                //create a mock element declaration
                XSDElementDeclaration decl = XSDFactory.eINSTANCE.createXSDElementDeclaration();
                decl.setName(qualifiedName.getLocalPart());
                decl.setTargetNamespace(qualifiedName.getNamespaceURI());

                //check for a type definition in the context, this is only used by
                // the parser in test mode
                QName typeDefinition = (QName) context.getComponentInstance(
                        "http://geotools.org/typeDefinition");

                if (typeDefinition != null) {
                    context.unregisterComponent("http://geotools.org/typeDefinition");

                    XSDTypeDefinition type = index.getTypeDefinition(typeDefinition);

                    if (type == null) {
                        throw new NullPointerException();
                    }

                    decl.setTypeDefinition(type);
                } else {
                    //normal case, just set the type to be of string
                    XSDTypeDefinition type = index.getTypeDefinition(XS.ANYTYPE);
                    decl.setTypeDefinition(type);
                }

                handler = handlerFactory.createElementHandler(decl, parent, this);
            }
        }

        if (handler != null) {
            //we may have actually matched an element whose namespace does 
            // not match the one passed in, update the context if so
            if ((handler.getElementDeclaration().getTargetNamespace() != null)
                    && !handler.getElementDeclaration().getTargetNamespace().equals(uri)) {
                
                if ( !handler.getElementDeclaration().isAbstract()) {
                    namespaces.declarePrefix("", handler.getElementDeclaration().getTargetNamespace());
                    qualifiedName = new QName(handler.getElementDeclaration().getTargetNamespace(),
                            qualifiedName.getLocalPart());
                }
               
            }

            //signal the handler to start the element, and place it on the stack
            handler.startElement(qualifiedName, attributes);
            handlers.push(handler);
        } else {
            String msg = "Handler for " + qName + " could not be found.";
            throw new SAXException(msg);
        }
    }

    public void characters(char[] ch, int start, int length)
        throws SAXException {
        //pull the handler from the top of stack
        ElementHandler handler = (ElementHandler) handlers.peek();
        handler.characters(ch, start, length);
    }

    public void endElement(String uri, String localName, String qName)
        throws SAXException {
        //pop the last handler off of the stack
        ElementHandler handler = (ElementHandler) handlers.pop();

        //create a qName object from the string
        QName qualifiedName = new QName(uri, localName);

        handler.endElement(qualifiedName);

        endElementInternal(handler);

        //if the upper most delegating handler, then end the document
        if ( handler instanceof DelegatingHandler && 
                !handlers.isEmpty() && !(handlers.peek() instanceof DelegatingHandler) ) {
            DelegatingHandler dh = (DelegatingHandler) handler;
            dh.endDocument();
            
            //grabbed the parsed value
            dh.getParseNode().setValue(dh.delegate.getParsedObject());
        }
        
        //pop namespace context
        namespaces.popContext();
    }

    protected void endElementInternal(ElementHandler handler) {
        //do nothing
    }

    public void endDocument() throws SAXException {
        validator.endDocument();
        
        //only the document handler should be left on the stack
        documentHandler = (DocumentHandler) handlers.pop();
        documentHandler.endDocument();
        
        //cleanup
        if ( index != null ) {
            index.destroy();
        }
        index = null;
        schemas = null;

        synchronized (this) {
            notifyAll();
        }
    }

    public void warning(SAXParseException e) throws SAXException {
        if ( isValidating() ) {
            validator.warning( e );
        }
    }

    public void error(SAXParseException e) throws SAXException {
        logger.log(Level.WARNING, e.getMessage());
        if ( isValidating() ) {
         
            validator.error( e );
        }
    }

    public Object getValue() {
        return documentHandler.getParseNode().getValue();
    }

    protected void configure(Configuration config) {
        //configure the bindings
        Map bindings = config.setupBindings();
        
        handlerFactory = new HandlerFactoryImpl();
        bindingLoader = new BindingLoader(bindings);
        bindingWalker = new BindingWalker(bindingLoader);
    }

    protected XSDSchemaLocator[] findSchemaLocators() {
        List l = Schemas.getComponentInstancesOfType(context, XSDSchemaLocator.class);

        //List l = context.getComponentInstancesOfType(XSDSchemaLocator.class);
        if ((l == null) || l.isEmpty()) {
            return new XSDSchemaLocator[] {  };
        }

        return (XSDSchemaLocator[]) l.toArray(new XSDSchemaLocator[l.size()]);
    }

    protected XSDSchemaLocationResolver[] findSchemaLocationResolvers() {
        //List l = context.getComponentInstancesOfType(XSDSchemaLocationResolver.class);
        List l = Schemas.getComponentInstancesOfType(context, XSDSchemaLocationResolver.class);

        if ((l == null) || l.isEmpty()) {
            return new XSDSchemaLocationResolver[] {  };
        }

        return (XSDSchemaLocationResolver[]) l.toArray(new XSDSchemaLocationResolver[l.size()]);
    }
}
