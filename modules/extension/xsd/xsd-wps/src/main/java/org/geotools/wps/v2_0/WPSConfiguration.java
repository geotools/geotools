/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2021, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.wps.v2_0;

import java.util.Map;
import javax.xml.namespace.QName;
import net.opengis.wps20.Wps20Factory;
import org.geotools.filter.v1_1.FilterParserDelegate;
import org.geotools.filter.v2_0.FESParserDelegate;
import org.geotools.gml3.GMLParserDelegate;
import org.geotools.ows.v2_0.OWSConfiguration;
import org.geotools.wfs.WFSParserDelegate;
import org.geotools.wps.v2_0.bindings.DataTransmissionModeTypeBinding;
import org.geotools.wps.v2_0.bindings.ExecuteRequestTypeBinding;
import org.geotools.wps.v2_0.bindings.InputDescriptionTypeBinding;
import org.geotools.wps.v2_0.bindings.JobControlOptionsTypeBinding;
import org.geotools.wps.v2_0.bindings.LiteralDataType_LiteralDataDomainBinding;
import org.geotools.wps.v2_0.bindings.OutputDefinitionTypeBinding;
import org.geotools.wps.v2_0.bindings.OutputDescriptionTypeBinding;
import org.geotools.wps.v2_0.bindings._DataBinding;
import org.geotools.xsd.ComplexEMFBinding;
import org.geotools.xsd.Configuration;
import org.picocontainer.MutablePicoContainer;

/**
 * Parser configuration for the http://www.opengis.net/wps/2.0 schema.
 *
 * @generated
 */
public class WPSConfiguration extends Configuration {

    /**
     * Creates a new configuration.
     *
     * @generated
     */
    public WPSConfiguration() {
        super(WPS.getInstance());
        addDependency(new OWSConfiguration());
    }

    @Override
    protected void registerBindings(Map<QName, Object> bindings) {

        bindings.put(WPS.ExecuteRequestType, ExecuteRequestTypeBinding.class);
        bindings.put(WPS.InputDescriptionType, InputDescriptionTypeBinding.class);
        bindings.put(WPS.DataTransmissionModeType, DataTransmissionModeTypeBinding.class);
        bindings.put(WPS.JobControlOptionsType, JobControlOptionsTypeBinding.class);
        bindings.put(WPS.OutputDefinitionType, OutputDefinitionTypeBinding.class);
        bindings.put(WPS.OutputDescriptionType, OutputDescriptionTypeBinding.class);
        bindings.put(WPS._Data, _DataBinding.class);
        bindings.put(
                WPS.LiteralDataType_LiteralDataDomain,
                LiteralDataType_LiteralDataDomainBinding.class);

        //
        //		//Complex Content
        bindings.put(
                WPS.ComplexDataType,
                new ComplexEMFBinding(Wps20Factory.eINSTANCE, WPS.ComplexDataType));
        bindings.put(
                WPS.DataDescriptionType,
                new ComplexEMFBinding(Wps20Factory.eINSTANCE, WPS.DataDescriptionType));
        bindings.put(
                WPS.DataInputType,
                new ComplexEMFBinding(Wps20Factory.eINSTANCE, WPS.DataInputType));
        bindings.put(
                WPS.DataOutputType,
                new ComplexEMFBinding(Wps20Factory.eINSTANCE, WPS.DataOutputType));
        bindings.put(
                WPS.DescriptionType,
                new ComplexEMFBinding(Wps20Factory.eINSTANCE, WPS.DescriptionType));
        bindings.put(
                WPS.GenericInputType,
                new ComplexEMFBinding(Wps20Factory.eINSTANCE, WPS.GenericInputType));
        bindings.put(
                WPS.GenericOutputType,
                new ComplexEMFBinding(Wps20Factory.eINSTANCE, WPS.GenericOutputType));
        bindings.put(
                WPS.GenericProcessType,
                new ComplexEMFBinding(Wps20Factory.eINSTANCE, WPS.GenericProcessType));
        bindings.put(
                WPS.GetCapabilitiesType,
                new ComplexEMFBinding(Wps20Factory.eINSTANCE, WPS.GetCapabilitiesType));
        bindings.put(
                WPS.LiteralDataType,
                new ComplexEMFBinding(Wps20Factory.eINSTANCE, WPS.LiteralDataType));
        bindings.put(
                WPS.ProcessDescriptionType,
                new ComplexEMFBinding(Wps20Factory.eINSTANCE, WPS.ProcessDescriptionType));
        bindings.put(
                WPS.ProcessSummaryType,
                new ComplexEMFBinding(Wps20Factory.eINSTANCE, WPS.ProcessSummaryType));
        bindings.put(
                WPS.ReferenceType,
                new ComplexEMFBinding(Wps20Factory.eINSTANCE, WPS.ReferenceType));
        bindings.put(
                WPS.RequestBaseType,
                new ComplexEMFBinding(Wps20Factory.eINSTANCE, WPS.RequestBaseType));
        bindings.put(
                WPS.WPSCapabilitiesType,
                new ComplexEMFBinding(Wps20Factory.eINSTANCE, WPS.WPSCapabilitiesType));
        bindings.put(
                WPS._BoundingBoxData,
                new ComplexEMFBinding(Wps20Factory.eINSTANCE, WPS._BoundingBoxData));
        bindings.put(WPS._Contents, new ComplexEMFBinding(Wps20Factory.eINSTANCE, WPS._Contents));
        bindings.put(
                WPS._DescribeProcess,
                new ComplexEMFBinding(Wps20Factory.eINSTANCE, WPS._DescribeProcess));
        bindings.put(WPS._Dismiss, new ComplexEMFBinding(Wps20Factory.eINSTANCE, WPS._Dismiss));
        bindings.put(WPS._Format, new ComplexEMFBinding(Wps20Factory.eINSTANCE, WPS._Format));
        bindings.put(WPS._GetResult, new ComplexEMFBinding(Wps20Factory.eINSTANCE, WPS._GetResult));
        bindings.put(WPS._GetStatus, new ComplexEMFBinding(Wps20Factory.eINSTANCE, WPS._GetStatus));
        bindings.put(
                WPS._LiteralValue,
                new ComplexEMFBinding(Wps20Factory.eINSTANCE, WPS._LiteralValue));
        bindings.put(
                WPS._ProcessOffering,
                new ComplexEMFBinding(Wps20Factory.eINSTANCE, WPS._ProcessOffering));
        bindings.put(
                WPS._ProcessOfferings,
                new ComplexEMFBinding(Wps20Factory.eINSTANCE, WPS._ProcessOfferings));
        bindings.put(WPS._Result, new ComplexEMFBinding(Wps20Factory.eINSTANCE, WPS._Result));
        bindings.put(
                WPS._StatusInfo, new ComplexEMFBinding(Wps20Factory.eINSTANCE, WPS._StatusInfo));
        bindings.put(
                WPS._SupportedCRS,
                new ComplexEMFBinding(Wps20Factory.eINSTANCE, WPS._SupportedCRS));
        bindings.put(
                WPS.ReferenceType_BodyReference,
                new ComplexEMFBinding(Wps20Factory.eINSTANCE, WPS.ReferenceType_BodyReference));
        bindings.put(
                WPS.WPSCapabilitiesType_Extension,
                new ComplexEMFBinding(Wps20Factory.eINSTANCE, WPS.WPSCapabilitiesType_Extension));
    }

    @Override
    protected void configureContext(MutablePicoContainer container) {
        container.registerComponentInstance(Wps20Factory.eINSTANCE);

        container.registerComponentInstance(new GMLParserDelegate());
        container.registerComponentInstance(new WFSParserDelegate());
        container.registerComponentInstance(new FilterParserDelegate());
        container.registerComponentInstance(new FESParserDelegate());
    }
}
