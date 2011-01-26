/*
 *    GeoTools - The Open Source Java GIS Tookit
 *    http://geotools.org
 * 
 *    (C) 2006-2008, Open Source Geospatial Foundation (OSGeo)
 *
 *    This file is hereby placed into the Public Domain. This means anyone is
 *    free to do whatever they wish with this file. Use it well and enjoy!
 */
package org.geotools.po.bindings;

import java.util.Map;

import org.eclipse.xsd.util.XSDSchemaLocationResolver;	
import org.geotools.po.ObjectFactory;
import org.geotools.xml.Configuration;
import org.picocontainer.MutablePicoContainer;

/**
 * Parser configuration for the http://www.geotools.org/po schema.
 *
 * @generated
 *
 * @source $URL$
 */
public class POConfiguration extends Configuration {

    /**
     * Creates a new configuration.
     * 
     * @generated
     */     
    public POConfiguration() {
       super(PO.getInstance());
    }
    
    /**
     * Registers an instance of {@link ObjectFactory}.
     */
    protected void configureContext(MutablePicoContainer context) {
    	context.registerComponentImplementation( ObjectFactory.class );
    }
    
    @Override
    protected void configureBindings(Map bindings) {
        //Types
        bindings.put(PO.Items,ItemsBinding.class);
        bindings.put(PO.PurchaseOrderType,PurchaseOrderTypeBinding.class);
        bindings.put(PO.SKU,SKUBinding.class);
        bindings.put(PO.USAddress,USAddressBinding.class);
    }
    
} 
