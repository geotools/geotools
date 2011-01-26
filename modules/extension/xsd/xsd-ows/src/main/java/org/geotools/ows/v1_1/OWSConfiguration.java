/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2007-2008, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.ows.v1_1;

import java.util.Map;

import net.opengis.ows11.AllowedValuesType;
import net.opengis.ows11.AnyValueType;
import net.opengis.ows11.Ows11Factory;

import org.geotools.ows.bindings.BoundingBoxTypeBinding;
import org.geotools.ows.bindings.UnitBinding;
import org.geotools.xlink.XLINKConfiguration;
import org.geotools.xml.ComplexEMFBinding;
import org.geotools.xml.Configuration;
import org.geotools.xml.SimpleContentComplexEMFBinding;
import org.geotools.xml.XMLConfiguration;
import org.picocontainer.MutablePicoContainer;

/**
 * Parser configuration for the http://www.opengis.net/ows/1.1 schema.
 * 
 * @generated
 *
 * @source $URL$
 */
public class OWSConfiguration extends Configuration {

    /**
     * Creates a new configuration.
     * 
     * @generated
     */
    public OWSConfiguration() {
        super(OWS.getInstance());

        addDependency(new XMLConfiguration());
        addDependency(new XLINKConfiguration());
    }

    /**
     * Registers the bindings for the configuration.
     * 
     * @generated
     */
    protected final void registerBindings(MutablePicoContainer container) {
        
    }
    
    @SuppressWarnings("unchecked")
    protected void registerBindings(Map bindings) {
        bindings.put(OWS.AcceptVersionsType,new ComplexEMFBinding(Ows11Factory.eINSTANCE, OWS.AcceptVersionsType));        
        bindings.put(OWS.AddressType,new ComplexEMFBinding(Ows11Factory.eINSTANCE, OWS.AddressType));
        bindings.put(OWS.AllowedValues, new ComplexEMFBinding(Ows11Factory.eINSTANCE, OWS.AllowedValues, AllowedValuesType.class));
        bindings.put(OWS.AnyValue,new ComplexEMFBinding(Ows11Factory.eINSTANCE, OWS.AnyValue, AnyValueType.class));
        
        bindings.put(OWS.GetCapabilitiesType,new ComplexEMFBinding(Ows11Factory.eINSTANCE, OWS.GetCapabilitiesType));
        bindings.put(OWS.SectionsType ,new ComplexEMFBinding(Ows11Factory.eINSTANCE, OWS.SectionsType));
        bindings.put(OWS.AcceptFormatsType,new ComplexEMFBinding(Ows11Factory.eINSTANCE, OWS.AcceptFormatsType));
        bindings.put(OWS.BoundingBoxType,BoundingBoxTypeBinding.class);
        
        bindings.put(OWS.CodeType,new SimpleContentComplexEMFBinding(Ows11Factory.eINSTANCE, OWS.CodeType));
        bindings.put(OWS.ContactType,new ComplexEMFBinding(Ows11Factory.eINSTANCE, OWS.ContactType));
        bindings.put(OWS.DomainMetadataType,new SimpleContentComplexEMFBinding(Ows11Factory.eINSTANCE,OWS.DomainMetadataType));
        
        bindings.put(OWS.ExceptionType,new ComplexEMFBinding(Ows11Factory.eINSTANCE, OWS.ExceptionType));
        bindings.put(OWS.KeywordsType, new ComplexEMFBinding(Ows11Factory.eINSTANCE, OWS.KeywordsType));
        bindings.put(OWS.LanguageStringType,new SimpleContentComplexEMFBinding(Ows11Factory.eINSTANCE,OWS.LanguageStringType));
        bindings.put(OWS.MetadataType,new ComplexEMFBinding(Ows11Factory.eINSTANCE, OWS.MetadataType));
        bindings.put(OWS.OnlineResourceType, new ComplexEMFBinding(Ows11Factory.eINSTANCE, OWS.OnlineResourceType));
        bindings.put(OWS.RangeType, new ComplexEMFBinding(Ows11Factory.eINSTANCE, OWS.RangeType));
        bindings.put(OWS.RequestMethodType, new ComplexEMFBinding(Ows11Factory.eINSTANCE, OWS.RequestMethodType));
        bindings.put(OWS.ResponsiblePartySubsetType, new ComplexEMFBinding(Ows11Factory.eINSTANCE, OWS.ResponsiblePartySubsetType));
        bindings.put(OWS.TelephoneType, new ComplexEMFBinding(Ows11Factory.eINSTANCE, OWS.TelephoneType));
        
        bindings.put(OWS._DCP,new ComplexEMFBinding(Ows11Factory.eINSTANCE, OWS._DCP));
        bindings.put(OWS._HTTP,new ComplexEMFBinding(Ows11Factory.eINSTANCE, OWS._HTTP));
        bindings.put(OWS._ExceptionReport,new ComplexEMFBinding(Ows11Factory.eINSTANCE, OWS._ExceptionReport));
        
        bindings.put(OWS._Operation, new ComplexEMFBinding(Ows11Factory.eINSTANCE, OWS._Operation));
        bindings.put(OWS._OperationsMetadata, new ComplexEMFBinding(Ows11Factory.eINSTANCE, OWS._OperationsMetadata));
        bindings.put(OWS._ServiceIdentification, new ComplexEMFBinding(Ows11Factory.eINSTANCE, OWS._ServiceIdentification));
        bindings.put(OWS._ServiceProvider, new ComplexEMFBinding(Ows11Factory.eINSTANCE, OWS._ServiceProvider));

        bindings.put(OWS.UOM, new UnitBinding());
        bindings.put(OWS.ValueType, new SimpleContentComplexEMFBinding(Ows11Factory.eINSTANCE, OWS.ValueType));
    }
    
    protected void configureContext(MutablePicoContainer container) {
        container.registerComponentInstance(Ows11Factory.eINSTANCE);
    }
    
    
}
