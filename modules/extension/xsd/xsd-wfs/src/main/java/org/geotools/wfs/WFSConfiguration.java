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
package org.geotools.wfs;

import net.opengis.wfs.WfsFactory;

import org.geotools.wfs.bindings.ActionTypeBinding;
import org.geotools.wfs.bindings.AllSomeTypeBinding;
import org.geotools.wfs.bindings.BaseRequestTypeBinding;
import org.geotools.wfs.bindings.Base_TypeNameListTypeBinding;
import org.geotools.wfs.bindings.DeleteElementTypeBinding;
import org.geotools.wfs.bindings.DescribeFeatureTypeTypeBinding;
import org.geotools.wfs.bindings.FeatureCollectionTypeBinding;
import org.geotools.wfs.bindings.FeatureTypeListTypeBinding;
import org.geotools.wfs.bindings.FeatureTypeTypeBinding;
import org.geotools.wfs.bindings.FeatureTypeType_NoSRSBinding;
import org.geotools.wfs.bindings.FeaturesLockedTypeBinding;
import org.geotools.wfs.bindings.FeaturesNotLockedTypeBinding;
import org.geotools.wfs.bindings.GMLObjectTypeListTypeBinding;
import org.geotools.wfs.bindings.GMLObjectTypeTypeBinding;
import org.geotools.wfs.bindings.GetCapabilitiesTypeBinding;
import org.geotools.wfs.bindings.GetFeatureTypeBinding;
import org.geotools.wfs.bindings.GetFeatureWithLockTypeBinding;
import org.geotools.wfs.bindings.GetGmlObjectTypeBinding;
import org.geotools.wfs.bindings.IdentifierGenerationOptionTypeBinding;
import org.geotools.wfs.bindings.InsertElementTypeBinding;
import org.geotools.wfs.bindings.InsertResultsTypeBinding;
import org.geotools.wfs.bindings.InsertedFeatureTypeBinding;
import org.geotools.wfs.bindings.LockFeatureResponseTypeBinding;
import org.geotools.wfs.bindings.LockFeatureTypeBinding;
import org.geotools.wfs.bindings.LockTypeBinding;
import org.geotools.wfs.bindings.MetadataURLTypeBinding;
import org.geotools.wfs.bindings.NativeTypeBinding;
import org.geotools.wfs.bindings.OperationTypeBinding;
import org.geotools.wfs.bindings.OperationsTypeBinding;
import org.geotools.wfs.bindings.OutputFormatListTypeBinding;
import org.geotools.wfs.bindings.PropertyTypeBinding;
import org.geotools.wfs.bindings.QueryTypeBinding;
import org.geotools.wfs.bindings.ResultTypeTypeBinding;
import org.geotools.wfs.bindings.TransactionResponseTypeBinding;
import org.geotools.wfs.bindings.TransactionResultsTypeBinding;
import org.geotools.wfs.bindings.TransactionSummaryTypeBinding;
import org.geotools.wfs.bindings.TransactionTypeBinding;
import org.geotools.wfs.bindings.TypeNameListTypeBinding;
import org.geotools.wfs.bindings.UpdateElementTypeBinding;
import org.geotools.wfs.bindings.WFS_CapabilitiesTypeBinding;
import org.geotools.wfs.bindings._XlinkPropertyNameBinding;
import org.geotools.xml.Configuration;
import org.picocontainer.MutablePicoContainer;


/**
 * Parser configuration for the http://www.opengis.net/wfs schema.
 *
 * @generated
 *
 * @source $URL$
 */
public abstract class WFSConfiguration extends Configuration {
    /**
     * Creates a new configuration.
     *
     * @generated
     */
    protected WFSConfiguration(WFS wfs) {
        super(wfs);
    }

    /**
     * Registers an instanceof {@link WfsFactory} in the context.
     */
    protected void configureContext(MutablePicoContainer container) {
        container.registerComponentInstance(WfsFactory.eINSTANCE);
    }

    /**
     * Registers the bindings for the configuration.
     *
     * @generated
     */
    protected final void registerBindings(MutablePicoContainer container) {
        //Types
        container.registerComponentImplementation(WFS.ActionType, ActionTypeBinding.class);
        container.registerComponentImplementation(WFS.AllSomeType, AllSomeTypeBinding.class);
        container.registerComponentImplementation(WFS.Base_TypeNameListType,
            Base_TypeNameListTypeBinding.class);
        container.registerComponentImplementation(WFS.BaseRequestType, BaseRequestTypeBinding.class);
        container.registerComponentImplementation(WFS.DeleteElementType,
            DeleteElementTypeBinding.class);
        container.registerComponentImplementation(WFS.DescribeFeatureTypeType,
            DescribeFeatureTypeTypeBinding.class);
        container.registerComponentImplementation(WFS.FeatureCollectionType,
            FeatureCollectionTypeBinding.class);
        container.registerComponentImplementation(WFS.FeaturesLockedType,
            FeaturesLockedTypeBinding.class);
        container.registerComponentImplementation(WFS.FeaturesNotLockedType,
            FeaturesNotLockedTypeBinding.class);
        container.registerComponentImplementation(WFS.FeatureTypeListType,
            FeatureTypeListTypeBinding.class);
        container.registerComponentImplementation(WFS.FeatureTypeType, FeatureTypeTypeBinding.class);
        container.registerComponentImplementation(WFS.GetCapabilitiesType,
            GetCapabilitiesTypeBinding.class);
        container.registerComponentImplementation(WFS.GetFeatureType, GetFeatureTypeBinding.class);
        container.registerComponentImplementation(WFS.GetFeatureWithLockType,
            GetFeatureWithLockTypeBinding.class);
        container.registerComponentImplementation(WFS.GetGmlObjectType,
            GetGmlObjectTypeBinding.class);
        container.registerComponentImplementation(WFS.GMLObjectTypeListType,
            GMLObjectTypeListTypeBinding.class);
        container.registerComponentImplementation(WFS.GMLObjectTypeType,
            GMLObjectTypeTypeBinding.class);
        container.registerComponentImplementation(WFS.IdentifierGenerationOptionType,
            IdentifierGenerationOptionTypeBinding.class);
        container.registerComponentImplementation(WFS.InsertedFeatureType,
            InsertedFeatureTypeBinding.class);
        container.registerComponentImplementation(WFS.InsertElementType,
            InsertElementTypeBinding.class);
        container.registerComponentImplementation(WFS.InsertResultsType,
            InsertResultsTypeBinding.class);
        container.registerComponentImplementation(WFS.LockFeatureResponseType,
            LockFeatureResponseTypeBinding.class);
        container.registerComponentImplementation(WFS.LockFeatureType, LockFeatureTypeBinding.class);
        container.registerComponentImplementation(WFS.LockType, LockTypeBinding.class);
        container.registerComponentImplementation(WFS.MetadataURLType, MetadataURLTypeBinding.class);
        container.registerComponentImplementation(WFS.NativeType, NativeTypeBinding.class);
        container.registerComponentImplementation(WFS.OperationsType, OperationsTypeBinding.class);
        container.registerComponentImplementation(WFS.OperationType, OperationTypeBinding.class);
        container.registerComponentImplementation(WFS.OutputFormatListType,
            OutputFormatListTypeBinding.class);
        container.registerComponentImplementation(WFS.PropertyType, PropertyTypeBinding.class);
        container.registerComponentImplementation(WFS.QueryType, QueryTypeBinding.class);
        container.registerComponentImplementation(WFS.ResultTypeType, ResultTypeTypeBinding.class);
        container.registerComponentImplementation(WFS.TransactionResponseType,
            TransactionResponseTypeBinding.class);
        container.registerComponentImplementation(WFS.TransactionResultsType,
            TransactionResultsTypeBinding.class);
        container.registerComponentImplementation(WFS.TransactionSummaryType,
            TransactionSummaryTypeBinding.class);
        container.registerComponentImplementation(WFS.TransactionType, TransactionTypeBinding.class);
        container.registerComponentImplementation(WFS.TypeNameListType,
            TypeNameListTypeBinding.class);
        container.registerComponentImplementation(WFS.UpdateElementType,
            UpdateElementTypeBinding.class);
        container.registerComponentImplementation(WFS.WFS_CapabilitiesType,
            WFS_CapabilitiesTypeBinding.class);
        container.registerComponentImplementation(WFS._XlinkPropertyName,
            _XlinkPropertyNameBinding.class);
        container.registerComponentImplementation(WFS.FeatureTypeType_NoSRS,
            FeatureTypeType_NoSRSBinding.class);
    }
}
