/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2002-2010, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.sld.v1_1;

import org.geotools.se.v1_1.SEConfiguration;
import org.geotools.sld.v1_1.bindings.NamedLayerBinding;
import org.geotools.sld.v1_1.bindings.NamedStyleBinding;
import org.geotools.sld.v1_1.bindings.RemoteOWSBinding;
import org.geotools.sld.v1_1.bindings.StyledLayerDescriptorBinding;
import org.geotools.sld.v1_1.bindings.UserLayerBinding;
import org.geotools.sld.v1_1.bindings.UserStyleBinding;
import org.geotools.xml.Configuration;
import org.geotools.xml.Parser;
import org.picocontainer.MutablePicoContainer;
import org.xml.sax.EntityResolver;

/**
 * Parser configuration for the http://www.opengis.net/sld schema.
 *
 * @generated
 *
 *
 * @source $URL$
 */
public class SLDConfiguration extends Configuration {

    private EntityResolver entityResolver;

    /**
     * Creates a new configuration.
     */     
    public SLDConfiguration() {
        this(null);
    }
    
    /**
     * Creates a new configuration.
     * 
     * @generated
     */     
    public SLDConfiguration(EntityResolver entityResolver) {
       super(SLD.getInstance());
       
       addDependency(new SEConfiguration());
       
       this.entityResolver = entityResolver;
    }
    
    /**
     * Registers the bindings for the configuration.
     *
     * @generated
     */
    protected final void registerBindings( MutablePicoContainer container ) {
        //Elements
        container.registerComponentImplementation(SLD.NamedLayer,NamedLayerBinding.class);
        container.registerComponentImplementation(SLD.NamedStyle,NamedStyleBinding.class);
        container.registerComponentImplementation(SLD.RemoteOWS,RemoteOWSBinding.class);
        container.registerComponentImplementation(SLD.StyledLayerDescriptor,StyledLayerDescriptorBinding.class);
        container.registerComponentImplementation(SLD.UserLayer,UserLayerBinding.class);
        container.registerComponentImplementation(SLD.UserStyle,UserStyleBinding.class);
    }
    
    @Override
    protected void configureParser(Parser parser) {
        parser.setEntityResolver(entityResolver);
    }    
} 
