/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2007-2014, Open Source Geospatial Foundation (OSGeo)
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
import net.opengis.ows11.NoValuesType;
import net.opengis.ows11.Ows11Factory;
import org.geotools.ows.bindings.AcceptFormatsTypeBinding;
import org.geotools.ows.bindings.AddressTypeBinding;
import org.geotools.ows.bindings.BasicIdentificationTypeOws11Binding;
import org.geotools.ows.bindings.BoundingBoxTypeBinding;
import org.geotools.ows.bindings.CapabilitiesBaseTypeBinding;
import org.geotools.ows.bindings.ContactTypeBinding;
import org.geotools.ows.bindings.DescriptionTypeBinding;
import org.geotools.ows.bindings.DomainTypeBinding;
import org.geotools.ows.bindings.ExceptionTypeBinding;
import org.geotools.ows.bindings.GetCapabilitiesTypeBinding;
import org.geotools.ows.bindings.IdentificationTypeOws11Binding;
import org.geotools.ows.bindings.MetadataTypeBinding;
import org.geotools.ows.bindings.MimeTypeBinding;
import org.geotools.ows.bindings.OnlineResourceTypeBinding;
import org.geotools.ows.bindings.PositionType2DBinding;
import org.geotools.ows.bindings.PositionTypeBinding;
import org.geotools.ows.bindings.RangeBinding;
import org.geotools.ows.bindings.RangeClosureBinding;
import org.geotools.ows.bindings.RequestMethodTypeBinding;
import org.geotools.ows.bindings.ResponsiblePartySubsetTypeBinding;
import org.geotools.ows.bindings.ResponsiblePartyTypeBinding;
import org.geotools.ows.bindings.SectionsTypeBinding;
import org.geotools.ows.bindings.ServiceTypeBinding;
import org.geotools.ows.bindings.TelephoneTypeBinding;
import org.geotools.ows.bindings.UnitBinding;
import org.geotools.ows.bindings.UpdateSequenceTypeBinding;
import org.geotools.ows.bindings.VersionTypeBinding;
import org.geotools.ows.bindings.WGS84BoundingBoxTypeBinding;
import org.geotools.ows.bindings._DCPBinding;
import org.geotools.ows.bindings._ExceptionReportBinding;
import org.geotools.ows.bindings._HTTPBinding;
import org.geotools.ows.bindings._OperationBinding;
import org.geotools.ows.bindings._OperationsMetadataBinding;
import org.geotools.ows.bindings._ServiceIdentificationBinding;
import org.geotools.ows.bindings._ServiceProviderBinding;
import org.geotools.xlink.XLINKConfiguration;
import org.geotools.xsd.ComplexEMFBinding;
import org.geotools.xsd.Configuration;
import org.geotools.xsd.SimpleContentComplexEMFBinding;
import org.geotools.xsd.XMLConfiguration;
import org.picocontainer.MutablePicoContainer;

/**
 * Parser configuration for the http://www.opengis.net/ows/1.1 schema.
 *
 * @generated
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
    @Override
    protected final void registerBindings(MutablePicoContainer container) {
        // Types
        container.registerComponentImplementation(
                org.geotools.xsd.ows.OWS.AcceptFormatsType, AcceptFormatsTypeBinding.class);
        container.registerComponentInstance(
                org.geotools.xsd.ows.OWS.AcceptVersionsType,
                new ComplexEMFBinding(
                        Ows11Factory.eINSTANCE, org.geotools.xsd.ows.OWS.AcceptVersionsType));
        container.registerComponentImplementation(
                org.geotools.xsd.ows.OWS.AddressType, AddressTypeBinding.class);
        container.registerComponentInstance(
                org.geotools.xsd.ows.OWS.WGS84BoundingBoxType,
                new WGS84BoundingBoxTypeBinding(
                        Ows11Factory.eINSTANCE, org.geotools.xsd.ows.OWS.WGS84BoundingBoxType));
        container.registerComponentInstance(
                org.geotools.xsd.ows.OWS.BoundingBoxType,
                new BoundingBoxTypeBinding(
                        Ows11Factory.eINSTANCE, org.geotools.xsd.ows.OWS.BoundingBoxType));
        container.registerComponentImplementation(
                org.geotools.xsd.ows.OWS.CapabilitiesBaseType, CapabilitiesBaseTypeBinding.class);
        container.registerComponentInstance(
                org.geotools.xsd.ows.OWS.CodeType,
                new SimpleContentComplexEMFBinding(
                        Ows11Factory.eINSTANCE, org.geotools.xsd.ows.OWS.CodeType));
        container.registerComponentImplementation(
                org.geotools.xsd.ows.OWS.ContactType, ContactTypeBinding.class);
        container.registerComponentImplementation(
                org.geotools.xsd.ows.OWS.DescriptionType, DescriptionTypeBinding.class);
        container.registerComponentImplementation(
                org.geotools.xsd.ows.OWS.DomainType, DomainTypeBinding.class);
        container.registerComponentImplementation(
                org.geotools.xsd.ows.OWS.ExceptionType, ExceptionTypeBinding.class);
        container.registerComponentImplementation(
                org.geotools.xsd.ows.OWS.GetCapabilitiesType, GetCapabilitiesTypeBinding.class);
        container.registerComponentImplementation(
                org.geotools.xsd.ows.OWS.BasicIdentificationType,
                BasicIdentificationTypeOws11Binding.class);
        container.registerComponentImplementation(
                org.geotools.xsd.ows.OWS.IdentificationType, IdentificationTypeOws11Binding.class);
        container.registerComponentInstance(
                org.geotools.xsd.ows.OWS.KeywordsType,
                new ComplexEMFBinding(
                        Ows11Factory.eINSTANCE, org.geotools.xsd.ows.OWS.KeywordsType));
        container.registerComponentImplementation(
                org.geotools.xsd.ows.OWS.MetadataType, MetadataTypeBinding.class);
        container.registerComponentImplementation(
                org.geotools.xsd.ows.OWS.MimeType, MimeTypeBinding.class);
        container.registerComponentImplementation(
                org.geotools.xsd.ows.OWS.OnlineResourceType, OnlineResourceTypeBinding.class);
        container.registerComponentImplementation(
                org.geotools.xsd.ows.OWS.PositionType, PositionTypeBinding.class);
        container.registerComponentImplementation(
                org.geotools.xsd.ows.OWS.PositionType2D, PositionType2DBinding.class);
        container.registerComponentImplementation(
                org.geotools.xsd.ows.OWS.RequestMethodType, RequestMethodTypeBinding.class);
        container.registerComponentImplementation(
                org.geotools.xsd.ows.OWS.ResponsiblePartySubsetType,
                ResponsiblePartySubsetTypeBinding.class);
        container.registerComponentImplementation(
                org.geotools.xsd.ows.OWS.ResponsiblePartyType, ResponsiblePartyTypeBinding.class);
        container.registerComponentImplementation(
                org.geotools.xsd.ows.OWS.SectionsType, SectionsTypeBinding.class);
        container.registerComponentImplementation(
                org.geotools.xsd.ows.OWS.ServiceType, ServiceTypeBinding.class);
        container.registerComponentImplementation(
                org.geotools.xsd.ows.OWS.TelephoneType, TelephoneTypeBinding.class);
        container.registerComponentImplementation(
                org.geotools.xsd.ows.OWS.UpdateSequenceType, UpdateSequenceTypeBinding.class);
        container.registerComponentImplementation(
                org.geotools.xsd.ows.OWS.VersionType, VersionTypeBinding.class);
        container.registerComponentImplementation(org.geotools.xsd.ows.OWS._DCP, _DCPBinding.class);
        container.registerComponentImplementation(
                org.geotools.xsd.ows.OWS._ExceptionReport, _ExceptionReportBinding.class);
        container.registerComponentImplementation(
                org.geotools.xsd.ows.OWS._HTTP, _HTTPBinding.class);
        container.registerComponentImplementation(
                org.geotools.xsd.ows.OWS._Operation, _OperationBinding.class);
        container.registerComponentImplementation(
                org.geotools.xsd.ows.OWS._OperationsMetadata, _OperationsMetadataBinding.class);
        container.registerComponentImplementation(
                org.geotools.xsd.ows.OWS._ServiceIdentification,
                _ServiceIdentificationBinding.class);
        container.registerComponentImplementation(
                org.geotools.xsd.ows.OWS._ServiceProvider, _ServiceProviderBinding.class);
    }

    @Override
    @SuppressWarnings("unchecked")
    protected void registerBindings(Map bindings) {
        bindings.put(
                OWS.AcceptVersionsType,
                new ComplexEMFBinding(Ows11Factory.eINSTANCE, OWS.AcceptVersionsType));
        bindings.put(
                OWS.AddressType, new ComplexEMFBinding(Ows11Factory.eINSTANCE, OWS.AddressType));
        bindings.put(
                OWS.AllowedValues,
                new ComplexEMFBinding(
                        Ows11Factory.eINSTANCE, OWS.AllowedValues, AllowedValuesType.class));
        bindings.put(
                OWS.AnyValue,
                new ComplexEMFBinding(Ows11Factory.eINSTANCE, OWS.AnyValue, AnyValueType.class));
        bindings.put(
                OWS.NoValues,
                new ComplexEMFBinding(Ows11Factory.eINSTANCE, OWS.NoValues, NoValuesType.class));

        bindings.put(
                OWS.GetCapabilitiesType,
                new ComplexEMFBinding(Ows11Factory.eINSTANCE, OWS.GetCapabilitiesType));
        bindings.put(
                OWS.SectionsType, new ComplexEMFBinding(Ows11Factory.eINSTANCE, OWS.SectionsType));
        bindings.put(
                OWS.AcceptFormatsType,
                new ComplexEMFBinding(Ows11Factory.eINSTANCE, OWS.AcceptFormatsType));
        bindings.put(
                OWS.BoundingBoxType,
                new BoundingBoxTypeBinding(Ows11Factory.eINSTANCE, OWS.BoundingBoxType));
        bindings.put(
                OWS.WGS84BoundingBoxType,
                new WGS84BoundingBoxTypeBinding(Ows11Factory.eINSTANCE, OWS.WGS84BoundingBoxType));

        bindings.put(
                OWS.CodeType,
                new SimpleContentComplexEMFBinding(Ows11Factory.eINSTANCE, OWS.CodeType));
        bindings.put(
                OWS.ContactType, new ComplexEMFBinding(Ows11Factory.eINSTANCE, OWS.ContactType));
        bindings.put(
                OWS.DomainMetadataType,
                new SimpleContentComplexEMFBinding(Ows11Factory.eINSTANCE, OWS.DomainMetadataType));
        bindings.put(OWS.DomainType, new ComplexEMFBinding(Ows11Factory.eINSTANCE, OWS.DomainType));

        bindings.put(
                OWS.ExceptionType,
                new ComplexEMFBinding(Ows11Factory.eINSTANCE, OWS.ExceptionType));
        bindings.put(
                OWS.KeywordsType, new ComplexEMFBinding(Ows11Factory.eINSTANCE, OWS.KeywordsType));
        bindings.put(
                OWS.LanguageStringType,
                new SimpleContentComplexEMFBinding(Ows11Factory.eINSTANCE, OWS.LanguageStringType));
        bindings.put(
                OWS.MetadataType, new ComplexEMFBinding(Ows11Factory.eINSTANCE, OWS.MetadataType));
        bindings.put(
                OWS.OnlineResourceType,
                new ComplexEMFBinding(Ows11Factory.eINSTANCE, OWS.OnlineResourceType));
        bindings.put(
                OWS.RequestMethodType,
                new ComplexEMFBinding(Ows11Factory.eINSTANCE, OWS.RequestMethodType));
        bindings.put(
                OWS.ResponsiblePartySubsetType,
                new ComplexEMFBinding(Ows11Factory.eINSTANCE, OWS.ResponsiblePartySubsetType));
        bindings.put(
                OWS.TelephoneType,
                new ComplexEMFBinding(Ows11Factory.eINSTANCE, OWS.TelephoneType));

        bindings.put(OWS._DCP, new ComplexEMFBinding(Ows11Factory.eINSTANCE, OWS._DCP));
        bindings.put(OWS._HTTP, new ComplexEMFBinding(Ows11Factory.eINSTANCE, OWS._HTTP));
        bindings.put(
                OWS._ExceptionReport,
                new ComplexEMFBinding(Ows11Factory.eINSTANCE, OWS._ExceptionReport));

        bindings.put(OWS._Operation, new ComplexEMFBinding(Ows11Factory.eINSTANCE, OWS._Operation));
        bindings.put(
                OWS._OperationsMetadata,
                new ComplexEMFBinding(Ows11Factory.eINSTANCE, OWS._OperationsMetadata));
        bindings.put(
                OWS._ServiceIdentification,
                new ComplexEMFBinding(Ows11Factory.eINSTANCE, OWS._ServiceIdentification));
        bindings.put(
                OWS._ServiceProvider,
                new ComplexEMFBinding(Ows11Factory.eINSTANCE, OWS._ServiceProvider));

        bindings.put(OWS.UOM, new UnitBinding());
        bindings.put(
                OWS.ValueType,
                new SimpleContentComplexEMFBinding(Ows11Factory.eINSTANCE, OWS.ValueType));
        bindings.put(OWS.rangeClosure, new RangeClosureBinding());
        bindings.put(OWS.RangeType, new RangeBinding(Ows11Factory.eINSTANCE, OWS.RangeType));
    }

    @Override
    protected void configureContext(MutablePicoContainer container) {
        container.registerComponentInstance(Ows11Factory.eINSTANCE);
    }
}
