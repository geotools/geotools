package org.geotools.wps.v2_0;

import net.opengis.wps20.Wps20Factory;
import org.geotools.filter.v1_1.FilterParserDelegate;
import org.geotools.filter.v2_0.FESParserDelegate;
import org.geotools.gml3.GMLParserDelegate;
import org.geotools.ows.v2_0.OWSConfiguration;
import org.geotools.wfs.WFSParserDelegate;
import org.geotools.wps.v2_0.bindings.ComplexDataTypeBinding;
import org.geotools.wps.v2_0.bindings.DataDescriptionTypeBinding;
import org.geotools.wps.v2_0.bindings.DataInputTypeBinding;
import org.geotools.wps.v2_0.bindings.DataOutputTypeBinding;
import org.geotools.wps.v2_0.bindings.DataTransmissionModeTypeBinding;
import org.geotools.wps.v2_0.bindings.DescriptionTypeBinding;
import org.geotools.wps.v2_0.bindings.ExecuteRequestTypeBinding;
import org.geotools.wps.v2_0.bindings.GenericInputTypeBinding;
import org.geotools.wps.v2_0.bindings.GenericOutputTypeBinding;
import org.geotools.wps.v2_0.bindings.GenericProcessTypeBinding;
import org.geotools.wps.v2_0.bindings.GetCapabilitiesTypeBinding;
import org.geotools.wps.v2_0.bindings.InputDescriptionTypeBinding;
import org.geotools.wps.v2_0.bindings.JobControlOptionsTypeBinding;
import org.geotools.wps.v2_0.bindings.LiteralDataDomainTypeBinding;
import org.geotools.wps.v2_0.bindings.LiteralDataTypeBinding;
import org.geotools.wps.v2_0.bindings.LiteralDataType_LiteralDataDomainBinding;
import org.geotools.wps.v2_0.bindings.OutputDefinitionTypeBinding;
import org.geotools.wps.v2_0.bindings.OutputDescriptionTypeBinding;
import org.geotools.wps.v2_0.bindings.ProcessDescriptionTypeBinding;
import org.geotools.wps.v2_0.bindings.ProcessSummaryTypeBinding;
import org.geotools.wps.v2_0.bindings.ReferenceTypeBinding;
import org.geotools.wps.v2_0.bindings.ReferenceType_BodyReferenceBinding;
import org.geotools.wps.v2_0.bindings.RequestBaseTypeBinding;
import org.geotools.wps.v2_0.bindings.WPSCapabilitiesTypeBinding;
import org.geotools.wps.v2_0.bindings.WPSCapabilitiesType_ExtensionBinding;
import org.geotools.wps.v2_0.bindings._BoundingBoxDataBinding;
import org.geotools.wps.v2_0.bindings._ContentsBinding;
import org.geotools.wps.v2_0.bindings._DataBinding;
import org.geotools.wps.v2_0.bindings._DescribeProcessBinding;
import org.geotools.wps.v2_0.bindings._DismissBinding;
import org.geotools.wps.v2_0.bindings._FormatBinding;
import org.geotools.wps.v2_0.bindings._GetResultBinding;
import org.geotools.wps.v2_0.bindings._GetStatusBinding;
import org.geotools.wps.v2_0.bindings._LiteralValueBinding;
import org.geotools.wps.v2_0.bindings._ProcessOfferingBinding;
import org.geotools.wps.v2_0.bindings._ProcessOfferingsBinding;
import org.geotools.wps.v2_0.bindings._ResultBinding;
import org.geotools.wps.v2_0.bindings._StatusInfoBinding;
import org.geotools.wps.v2_0.bindings._SupportedCRSBinding;
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

    /**
     * Registers the bindings for the configuration.
     *
     * @generated
     */
    protected final void registerBindings(MutablePicoContainer container) {
        // Types
        container.registerComponentImplementation(
                WPS.ComplexDataType, ComplexDataTypeBinding.class);
        container.registerComponentImplementation(
                WPS.DataDescriptionType, DataDescriptionTypeBinding.class);
        container.registerComponentImplementation(WPS.DataInputType, DataInputTypeBinding.class);
        container.registerComponentImplementation(WPS.DataOutputType, DataOutputTypeBinding.class);
        container.registerComponentImplementation(
                WPS.DataTransmissionModeType, DataTransmissionModeTypeBinding.class);
        container.registerComponentImplementation(
                WPS.DescriptionType, DescriptionTypeBinding.class);
        container.registerComponentImplementation(
                WPS.ExecuteRequestType, ExecuteRequestTypeBinding.class);
        container.registerComponentImplementation(
                WPS.GenericInputType, GenericInputTypeBinding.class);
        container.registerComponentImplementation(
                WPS.GenericOutputType, GenericOutputTypeBinding.class);
        container.registerComponentImplementation(
                WPS.GenericProcessType, GenericProcessTypeBinding.class);
        container.registerComponentImplementation(
                WPS.GetCapabilitiesType, GetCapabilitiesTypeBinding.class);
        container.registerComponentImplementation(
                WPS.InputDescriptionType, InputDescriptionTypeBinding.class);
        container.registerComponentImplementation(
                WPS.JobControlOptionsType, JobControlOptionsTypeBinding.class);
        container.registerComponentImplementation(
                WPS.LiteralDataDomainType, LiteralDataDomainTypeBinding.class);
        container.registerComponentImplementation(
                WPS.LiteralDataType, LiteralDataTypeBinding.class);
        container.registerComponentImplementation(
                WPS.OutputDefinitionType, OutputDefinitionTypeBinding.class);
        container.registerComponentImplementation(
                WPS.OutputDescriptionType, OutputDescriptionTypeBinding.class);
        container.registerComponentImplementation(
                WPS.ProcessDescriptionType, ProcessDescriptionTypeBinding.class);
        container.registerComponentImplementation(
                WPS.ProcessSummaryType, ProcessSummaryTypeBinding.class);
        container.registerComponentImplementation(WPS.ReferenceType, ReferenceTypeBinding.class);
        container.registerComponentImplementation(
                WPS.RequestBaseType, RequestBaseTypeBinding.class);
        container.registerComponentImplementation(
                WPS.WPSCapabilitiesType, WPSCapabilitiesTypeBinding.class);
        container.registerComponentImplementation(
                WPS._BoundingBoxData, _BoundingBoxDataBinding.class);
        container.registerComponentImplementation(WPS._Contents, _ContentsBinding.class);
        container.registerComponentImplementation(WPS._Data, _DataBinding.class);
        container.registerComponentImplementation(
                WPS._DescribeProcess, _DescribeProcessBinding.class);
        container.registerComponentImplementation(WPS._Dismiss, _DismissBinding.class);
        container.registerComponentImplementation(WPS._Format, _FormatBinding.class);
        container.registerComponentImplementation(WPS._GetResult, _GetResultBinding.class);
        container.registerComponentImplementation(WPS._GetStatus, _GetStatusBinding.class);
        container.registerComponentImplementation(WPS._LiteralValue, _LiteralValueBinding.class);
        container.registerComponentImplementation(
                WPS._ProcessOffering, _ProcessOfferingBinding.class);
        container.registerComponentImplementation(
                WPS._ProcessOfferings, _ProcessOfferingsBinding.class);
        container.registerComponentImplementation(WPS._Result, _ResultBinding.class);
        container.registerComponentImplementation(WPS._StatusInfo, _StatusInfoBinding.class);
        container.registerComponentImplementation(WPS._SupportedCRS, _SupportedCRSBinding.class);
        container.registerComponentImplementation(
                WPS.LiteralDataType_LiteralDataDomain,
                LiteralDataType_LiteralDataDomainBinding.class);
        container.registerComponentImplementation(
                WPS.ReferenceType_BodyReference, ReferenceType_BodyReferenceBinding.class);
        container.registerComponentImplementation(
                WPS.WPSCapabilitiesType_Extension, WPSCapabilitiesType_ExtensionBinding.class);
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
