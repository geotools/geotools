/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2008, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.wps;

import java.util.Map;

import net.opengis.wps10.DataInputsType1;
import net.opengis.wps10.DefaultType1;
import net.opengis.wps10.DefaultType2;
import net.opengis.wps10.ProcessOutputsType1;
import net.opengis.wps10.Wps10Factory;

import org.geotools.gml3.GMLParserDelegate;
import org.geotools.ows.v1_1.OWSConfiguration;
import org.geotools.wfs.WFSParserDelegate;
import org.geotools.wps.bindings.ComplexDataTypeBinding;
import org.geotools.wps.bindings.InputReferenceTypeBinding;
import org.geotools.wps.bindings.LanguagesBinding;
import org.geotools.xml.ComplexEMFBinding;
import org.geotools.xml.Configuration;
import org.geotools.xml.SimpleContentComplexEMFBinding;
import org.picocontainer.MutablePicoContainer;

/**
 * Parser configuration for the http://www.opengis.net/wps/1.0.0 schema.
 *
 * @generated
 *
 *
 * @source $URL$
 */
public class WPSConfiguration extends Configuration {

    /**
     * Creates a new configuration.
     *
     * @generated
     */
    public WPSConfiguration() {
       super(WPS.getInstance());

       addDependency( new OWSConfiguration());
    }

    @SuppressWarnings("unchecked")
    @Override
    protected void registerBindings(Map bindings) {
        bindings.put(WPS.InputReferenceType, InputReferenceTypeBinding.class);
        bindings.put(WPS.ComplexDataType,    ComplexDataTypeBinding.class);
        bindings.put(WPS.ComplexDataCombinationsType, new ComplexEMFBinding(Wps10Factory.eINSTANCE,WPS.ComplexDataCombinationsType));
        bindings.put(WPS.ComplexDataCombinationType,new ComplexEMFBinding(Wps10Factory.eINSTANCE,WPS.ComplexDataCombinationType));
        bindings.put(WPS.ComplexDataDescriptionType, new ComplexEMFBinding(Wps10Factory.eINSTANCE,WPS.ComplexDataDescriptionType));
        bindings.put(WPS.CRSsType,new ComplexEMFBinding(Wps10Factory.eINSTANCE,WPS.CRSsType));
        bindings.put(WPS.DataInputsType, new ComplexEMFBinding(Wps10Factory.eINSTANCE,WPS.DataInputsType, DataInputsType1.class));
        bindings.put(WPS.DataType, new ComplexEMFBinding(Wps10Factory.eINSTANCE,WPS.DataType));
        bindings.put(WPS.DescriptionType, new ComplexEMFBinding(Wps10Factory.eINSTANCE,WPS.DescriptionType));
        bindings.put(WPS.DocumentOutputDefinitionType, new ComplexEMFBinding(Wps10Factory.eINSTANCE,WPS.DocumentOutputDefinitionType));
        bindings.put(WPS.ExecuteResponse_ProcessOutputs, new ComplexEMFBinding(Wps10Factory.eINSTANCE,WPS.ExecuteResponse_ProcessOutputs, ProcessOutputsType1.class));
        bindings.put(WPS.InputDescriptionType, new ComplexEMFBinding(Wps10Factory.eINSTANCE,WPS.InputDescriptionType));
        bindings.put(WPS.InputType, new ComplexEMFBinding(Wps10Factory.eINSTANCE,WPS.InputType));
        bindings.put(WPS.LanguagesType, new ComplexEMFBinding(Wps10Factory.eINSTANCE,WPS.LanguagesType));
        bindings.put(WPS.LiteralDataType, new SimpleContentComplexEMFBinding(Wps10Factory.eINSTANCE,WPS.LiteralDataType));
        bindings.put(WPS.LiteralInputType, new ComplexEMFBinding(Wps10Factory.eINSTANCE,WPS.LiteralInputType));
        bindings.put(WPS.LiteralOutputType, new ComplexEMFBinding(Wps10Factory.eINSTANCE,WPS.LiteralOutputType));
        bindings.put(WPS.OutputDataType, new ComplexEMFBinding(Wps10Factory.eINSTANCE,WPS.OutputDataType));
        bindings.put(WPS.OutputDefinitionsType, new ComplexEMFBinding(Wps10Factory.eINSTANCE,WPS.OutputDefinitionsType));
        bindings.put(WPS.OutputDefinitionType, new ComplexEMFBinding(Wps10Factory.eINSTANCE,WPS.OutputDefinitionType));
        bindings.put(WPS.OutputDescriptionType, new ComplexEMFBinding(Wps10Factory.eINSTANCE,WPS.OutputDescriptionType));
        bindings.put(WPS.OutputReferenceType, new ComplexEMFBinding(Wps10Factory.eINSTANCE,WPS.OutputReferenceType));
        bindings.put(WPS.ProcessBriefType, new ComplexEMFBinding(Wps10Factory.eINSTANCE,WPS.ProcessBriefType));
        bindings.put(WPS.ProcessDescriptionType, new ComplexEMFBinding(Wps10Factory.eINSTANCE,WPS.ProcessDescriptionType));
        bindings.put(WPS.ProcessFailedType, new ComplexEMFBinding(Wps10Factory.eINSTANCE,WPS.ProcessFailedType));
        bindings.put(WPS.ProcessStartedType, new ComplexEMFBinding(Wps10Factory.eINSTANCE,WPS.ProcessStartedType));
        bindings.put(WPS.RequestBaseType, new ComplexEMFBinding(Wps10Factory.eINSTANCE,WPS.RequestBaseType));
        bindings.put(WPS.ResponseBaseType, new ComplexEMFBinding(Wps10Factory.eINSTANCE,WPS.ResponseBaseType));
        bindings.put(WPS.ResponseDocumentType, new ComplexEMFBinding(Wps10Factory.eINSTANCE,WPS.ResponseDocumentType));
        bindings.put(WPS.ResponseFormType, new ComplexEMFBinding(Wps10Factory.eINSTANCE,WPS.ResponseFormType));
        bindings.put(WPS.StatusType, new ComplexEMFBinding(Wps10Factory.eINSTANCE,WPS.StatusType));
        bindings.put(WPS.SupportedComplexDataInputType, new ComplexEMFBinding(Wps10Factory.eINSTANCE,WPS.SupportedComplexDataInputType));
        bindings.put(WPS.SupportedComplexDataType, new ComplexEMFBinding(Wps10Factory.eINSTANCE,WPS.SupportedComplexDataType));
        bindings.put(WPS.SupportedCRSsType, new ComplexEMFBinding(Wps10Factory.eINSTANCE,WPS.SupportedCRSsType));
        bindings.put(WPS.SupportedUOMsType, new ComplexEMFBinding(Wps10Factory.eINSTANCE,WPS.SupportedUOMsType));
        bindings.put(WPS.UOMsType, new ComplexEMFBinding(Wps10Factory.eINSTANCE,WPS.UOMsType));
        bindings.put(WPS.ValuesReferenceType, new ComplexEMFBinding(Wps10Factory.eINSTANCE,WPS.ValuesReferenceType));
        bindings.put(WPS.WPSCapabilitiesType, new ComplexEMFBinding(Wps10Factory.eINSTANCE,WPS.WPSCapabilitiesType));
        bindings.put(WPS._DescribeProcess, new ComplexEMFBinding(Wps10Factory.eINSTANCE,WPS._DescribeProcess));
        bindings.put(WPS._Execute, new ComplexEMFBinding(Wps10Factory.eINSTANCE,WPS._Execute));
        bindings.put(WPS._ExecuteResponse, new ComplexEMFBinding(Wps10Factory.eINSTANCE,WPS._ExecuteResponse));
        bindings.put(WPS._GetCapabilities, new ComplexEMFBinding(Wps10Factory.eINSTANCE,WPS._GetCapabilities));
        bindings.put(WPS._Languages, LanguagesBinding.class);
        //bindings.put(WPS._Languages, new ComplexEMFBinding(Wps10Factory.eINSTANCE,WPS._Languages, LanguagesType1.class ));
        bindings.put(WPS._Languages_Default, new ComplexEMFBinding(Wps10Factory.eINSTANCE, WPS._Languages_Default, DefaultType2.class ));
        bindings.put(WPS._ProcessDescriptions, new ComplexEMFBinding(Wps10Factory.eINSTANCE,WPS._ProcessDescriptions));
        bindings.put(WPS._ProcessOfferings, new ComplexEMFBinding(Wps10Factory.eINSTANCE,WPS._ProcessOfferings));
        bindings.put(WPS._WSDL, new ComplexEMFBinding(Wps10Factory.eINSTANCE,WPS._WSDL));
        bindings.put(WPS.InputReferenceType_Header, new ComplexEMFBinding(Wps10Factory.eINSTANCE,WPS.InputReferenceType_Header));
        bindings.put(WPS.InputReferenceType_BodyReference, new ComplexEMFBinding(Wps10Factory.eINSTANCE,WPS.InputReferenceType_BodyReference));
        bindings.put(WPS.ProcessDescriptionType_DataInputs, new ComplexEMFBinding(Wps10Factory.eINSTANCE,WPS.ProcessDescriptionType_DataInputs));
        bindings.put(WPS.ProcessDescriptionType_ProcessOutputs, new ComplexEMFBinding(Wps10Factory.eINSTANCE,WPS.ProcessDescriptionType_ProcessOutputs));
        bindings.put(WPS.SupportedCRSsType_Default, new ComplexEMFBinding(Wps10Factory.eINSTANCE,WPS.SupportedCRSsType_Default));
        bindings.put(WPS.SupportedUOMsType_Default, new ComplexEMFBinding(Wps10Factory.eINSTANCE,WPS.SupportedUOMsType_Default, DefaultType1.class));
    }

    @Override
    protected void configureContext(MutablePicoContainer container) {
        container.registerComponentInstance(Wps10Factory.eINSTANCE);
        
        //register parser delegates for parsing schemas we do not know about
        container.registerComponentInstance( new GMLParserDelegate() );
        container.registerComponentInstance( new WFSParserDelegate() );
    }
}
