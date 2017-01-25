/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2017, Open Source Geospatial Foundation (OSGeo)
 *
 *    This file is hereby placed into the Public Domain. This means anyone is
 *    free to do whatever they wish with this file. Use it well and enjoy!
 */
package org.geotools.po.bindings;

import org.geotools.xml.Configuration;
import org.picocontainer.MutablePicoContainer;

/**
 * Parser configuration for the http://www.geotools.org/po schema.
 *
 * @generated
 */
public class POConfiguration extends Configuration {

    /**
     * Creates a new configuration.
     * 
     * @generated
     */     
    public POConfiguration() {
       super(PO.getInstance());
       
       //TODO: add dependencies here
    }
    
    /**
     * Registers the bindings for the configuration.
     *
     * @generated
     */
    protected final void registerBindings( MutablePicoContainer container ) {
        //Types
        container.registerComponentImplementation(PO.Items,ItemsBinding.class);
        container.registerComponentImplementation(PO.PurchaseOrderType,PurchaseOrderTypeBinding.class);
        container.registerComponentImplementation(PO.SKU,SKUBinding.class);
        container.registerComponentImplementation(PO.USAddress,USAddressBinding.class);
        container.registerComponentImplementation(PO.Items_item,Items_itemBinding.class);
        
        //Elements
        container.registerComponentImplementation(PO.comment,CommentBinding.class);
        container.registerComponentImplementation(PO.purchaseOrder,PurchaseOrderBinding.class);
    
    }
} 