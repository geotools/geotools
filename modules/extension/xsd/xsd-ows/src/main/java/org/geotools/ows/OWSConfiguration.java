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
package org.geotools.ows;

import net.opengis.ows10.Ows10Factory;

import org.geotools.filter.v1_1.OGCConfiguration;
import org.geotools.ows.bindings.AcceptFormatsTypeBinding;
import org.geotools.ows.bindings.AddressTypeBinding;
import org.geotools.ows.bindings.BoundingBoxTypeBinding;
import org.geotools.ows.bindings.CapabilitiesBaseTypeBinding;
import org.geotools.ows.bindings.ContactTypeBinding;
import org.geotools.ows.bindings.DescriptionTypeBinding;
import org.geotools.ows.bindings.DomainTypeBinding;
import org.geotools.ows.bindings.ExceptionTypeBinding;
import org.geotools.ows.bindings.GetCapabilitiesTypeBinding;
import org.geotools.ows.bindings.IdentificationTypeBinding;
import org.geotools.ows.bindings.MetadataTypeBinding;
import org.geotools.ows.bindings.MimeTypeBinding;
import org.geotools.ows.bindings.OnlineResourceTypeBinding;
import org.geotools.ows.bindings.PositionType2DBinding;
import org.geotools.ows.bindings.PositionTypeBinding;
import org.geotools.ows.bindings.RequestMethodTypeBinding;
import org.geotools.ows.bindings.ResponsiblePartySubsetTypeBinding;
import org.geotools.ows.bindings.ResponsiblePartyTypeBinding;
import org.geotools.ows.bindings.SectionsTypeBinding;
import org.geotools.ows.bindings.ServiceTypeBinding;
import org.geotools.ows.bindings.TelephoneTypeBinding;
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
import org.geotools.xml.ComplexEMFBinding;
import org.geotools.xml.Configuration;
import org.geotools.xml.SimpleContentComplexEMFBinding;
import org.picocontainer.MutablePicoContainer;


/**
 * Parser configuration for the http://www.opengis.net/ows schema.
 *
 * @generated
 *
 *
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

        addDependency(new XLINKConfiguration());
        addDependency(new OGCConfiguration());
    }

    protected void configureContext(MutablePicoContainer container) {
        container.registerComponentInstance(Ows10Factory.eINSTANCE);
    }

    /**
     * Registers the bindings for the configuration.
     *
     * @generated
     */
    protected final void registerBindings(MutablePicoContainer container) {
        //Types
        container.registerComponentImplementation(OWS.AcceptFormatsType,
            AcceptFormatsTypeBinding.class);
        container.registerComponentInstance(OWS.AcceptVersionsType, 
                new ComplexEMFBinding(Ows10Factory.eINSTANCE, OWS.AcceptVersionsType));
        container.registerComponentImplementation(OWS.AddressType, AddressTypeBinding.class);
        container.registerComponentImplementation(OWS.WGS84BoundingBoxType, WGS84BoundingBoxTypeBinding.class);
        container.registerComponentInstance(OWS.BoundingBoxType, new BoundingBoxTypeBinding(Ows10Factory.eINSTANCE, OWS.BoundingBoxType));
        container.registerComponentImplementation(OWS.CapabilitiesBaseType,
            CapabilitiesBaseTypeBinding.class);
        container.registerComponentInstance(OWS.CodeType, new SimpleContentComplexEMFBinding(Ows10Factory.eINSTANCE, OWS.CodeType));
        container.registerComponentImplementation(OWS.ContactType, ContactTypeBinding.class);
        container.registerComponentImplementation(OWS.DescriptionType, DescriptionTypeBinding.class);
        container.registerComponentImplementation(OWS.DomainType, DomainTypeBinding.class);
        container.registerComponentImplementation(OWS.ExceptionType, ExceptionTypeBinding.class);
        container.registerComponentImplementation(OWS.GetCapabilitiesType,
            GetCapabilitiesTypeBinding.class);
        container.registerComponentImplementation(OWS.IdentificationType,
            IdentificationTypeBinding.class);
        container.registerComponentInstance(OWS.KeywordsType, new ComplexEMFBinding(Ows10Factory.eINSTANCE, OWS.KeywordsType));
        container.registerComponentImplementation(OWS.MetadataType, MetadataTypeBinding.class);
        container.registerComponentImplementation(OWS.MimeType, MimeTypeBinding.class);
        container.registerComponentImplementation(OWS.OnlineResourceType,
            OnlineResourceTypeBinding.class);
        container.registerComponentImplementation(OWS.PositionType, PositionTypeBinding.class);
        container.registerComponentImplementation(OWS.PositionType2D, PositionType2DBinding.class);
        container.registerComponentImplementation(OWS.RequestMethodType,
            RequestMethodTypeBinding.class);
        container.registerComponentImplementation(OWS.ResponsiblePartySubsetType,
            ResponsiblePartySubsetTypeBinding.class);
        container.registerComponentImplementation(OWS.ResponsiblePartyType,
            ResponsiblePartyTypeBinding.class);
        container.registerComponentImplementation(OWS.SectionsType, SectionsTypeBinding.class);
        container.registerComponentImplementation(OWS.ServiceType, ServiceTypeBinding.class);
        container.registerComponentImplementation(OWS.TelephoneType, TelephoneTypeBinding.class);
        container.registerComponentImplementation(OWS.UpdateSequenceType,
            UpdateSequenceTypeBinding.class);
        container.registerComponentImplementation(OWS.VersionType, VersionTypeBinding.class);
        container.registerComponentImplementation(OWS._DCP, _DCPBinding.class);
        container.registerComponentImplementation(OWS._ExceptionReport,
            _ExceptionReportBinding.class);
        container.registerComponentImplementation(OWS._HTTP, _HTTPBinding.class);
        container.registerComponentImplementation(OWS._Operation, _OperationBinding.class);
        container.registerComponentImplementation(OWS._OperationsMetadata,
            _OperationsMetadataBinding.class);
        container.registerComponentImplementation(OWS._ServiceIdentification,
            _ServiceIdentificationBinding.class);
        container.registerComponentImplementation(OWS._ServiceProvider,
            _ServiceProviderBinding.class);
    }
}
