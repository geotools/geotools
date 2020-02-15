/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2012, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.wcs.v2_0;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Map;
import javax.xml.namespace.QName;
import net.opengis.ows20.Ows20Factory;
import net.opengis.wcs20.Wcs20Factory;
import org.geotools.ows.v2_0.OWSConfiguration;
import org.geotools.wcs.bindings.ExtensionTypeBinding;
import org.geotools.xsd.ComplexEMFBinding;
import org.geotools.xsd.Configuration;
import org.picocontainer.MutablePicoContainer;

/**
 * Parser configuration for the http://www.opengis.net/wcs/2.0 schema.
 *
 * @generated
 */
public class WCSConfiguration extends Configuration {

    /**
     * Creates a new configuration.
     *
     * @generated
     */
    public WCSConfiguration() {
        super(WCS.getInstance());

        addDependency(new OWSConfiguration());
    }

    protected void configureContext(MutablePicoContainer container) {
        container.registerComponentInstance(Ows20Factory.eINSTANCE);

        // register parser delegate for extension schemas
        container.registerComponentInstance(new RangeSubsetParserDelegate());
        container.registerComponentInstance(new ScalingParserDelegate());
        container.registerComponentInstance(new InterpolationParserDelegate());
    }

    /**
     * Registers the bindings for the configuration.
     *
     * @generated
     */
    @SuppressWarnings("unchecked")
    protected final void registerBindings(Map bindings) {
        // manually setup bindings
        bindings.put(WCS.ExtensionType, new ExtensionTypeBinding());

        // "automatic" bindings
        bindings.put(
                WCS.CapabilitiesType,
                new ComplexEMFBinding(Wcs20Factory.eINSTANCE, WCS.CapabilitiesType));
        bindings.put(
                WCS.ContentsType, new ComplexEMFBinding(Wcs20Factory.eINSTANCE, WCS.ContentsType));
        bindings.put(
                WCS.CoverageDescriptionsType,
                new ComplexEMFBinding(Wcs20Factory.eINSTANCE, WCS.CoverageDescriptionsType));
        bindings.put(
                WCS.CoverageDescriptionType,
                new ComplexEMFBinding(Wcs20Factory.eINSTANCE, WCS.CoverageDescriptionType));
        bindings.put(
                WCS.CoverageOfferingsType,
                new ComplexEMFBinding(Wcs20Factory.eINSTANCE, WCS.CoverageOfferingsType));
        bindings.put(
                WCS.CoverageSubtypeParentType,
                new ComplexEMFBinding(Wcs20Factory.eINSTANCE, WCS.CoverageSubtypeParentType));
        bindings.put(
                WCS.CoverageSummaryType,
                new ComplexEMFBinding(Wcs20Factory.eINSTANCE, WCS.CoverageSummaryType));
        bindings.put(
                WCS.DescribeCoverageType,
                new ComplexEMFBinding(Wcs20Factory.eINSTANCE, WCS.DescribeCoverageType));
        bindings.put(
                WCS.DimensionSliceType,
                new ComplexEMFBinding(Wcs20Factory.eINSTANCE, WCS.DimensionSliceType));
        bindings.put(
                WCS.DimensionSubsetType,
                new ComplexEMFBinding(Wcs20Factory.eINSTANCE, WCS.DimensionSubsetType));
        bindings.put(
                WCS.DimensionTrimType,
                new ComplexEMFBinding(Wcs20Factory.eINSTANCE, WCS.DimensionTrimType));
        bindings.put(
                WCS.GetCapabilitiesType,
                new ComplexEMFBinding(Wcs20Factory.eINSTANCE, WCS.GetCapabilitiesType));
        bindings.put(
                WCS.GetCoverageType,
                new ComplexEMFBinding(Wcs20Factory.eINSTANCE, WCS.GetCoverageType));
        bindings.put(
                WCS.OfferedCoverageType,
                new ComplexEMFBinding(Wcs20Factory.eINSTANCE, WCS.OfferedCoverageType));
        bindings.put(
                WCS.RequestBaseType,
                new ComplexEMFBinding(Wcs20Factory.eINSTANCE, WCS.RequestBaseType));
        bindings.put(
                WCS.ServiceMetadataType,
                new ComplexEMFBinding(Wcs20Factory.eINSTANCE, WCS.ServiceMetadataType));
        bindings.put(
                WCS.ServiceParametersType,
                new ComplexEMFBinding(Wcs20Factory.eINSTANCE, WCS.ServiceParametersType));
        // bindings.put(WCS.VersionStringType, new ComplexEMFBinding(Wcs20Factory.eINSTANCE,
        // WCS.VersionStringType));
        /*
        bindings.put(WCS.Capabilities, new ComplexEMFBinding(Wcs20Factory.eINSTANCE, WCS.Capabilities));
        bindings.put(WCS.Contents, new ComplexEMFBinding(Wcs20Factory.eINSTANCE, WCS.Contents));
        bindings.put(WCS.CoverageDescription, new ComplexEMFBinding(Wcs20Factory.eINSTANCE, WCS.CoverageDescription));
        bindings.put(WCS.CoverageDescriptions, new ComplexEMFBinding(Wcs20Factory.eINSTANCE, WCS.CoverageDescriptions));
        // bindings.put(WCS.CoverageId, new ComplexEMFBinding(Wcs20Factory.eINSTANCE, WCS.CoverageId));
        bindings.put(WCS.CoverageOfferings, new ComplexEMFBinding(Wcs20Factory.eINSTANCE, WCS.CoverageOfferings));
        bindings.put(WCS.CoverageSubtype, new ComplexEMFBinding(Wcs20Factory.eINSTANCE, WCS.CoverageSubtype));
        bindings.put(WCS.CoverageSubtypeParent, new ComplexEMFBinding(Wcs20Factory.eINSTANCE, WCS.CoverageSubtypeParent));
        bindings.put(WCS.CoverageSummary, new ComplexEMFBinding(Wcs20Factory.eINSTANCE, WCS.CoverageSummary));
        // bindings.put(WCS.DescribeCoverage, new ComplexEMFBinding(Wcs20Factory.eINSTANCE, WCS.DescribeCoverage));
        // bindings.put(WCS.DimensionSlice, new ComplexEMFBinding(Wcs20Factory.eINSTANCE, WCS.DimensionSlice));
        bindings.put(WCS.DimensionSubset, new ComplexEMFBinding(Wcs20Factory.eINSTANCE, WCS.DimensionSubset));
        // bindings.put(WCS.DimensionTrim, new ComplexEMFBinding(Wcs20Factory.eINSTANCE, WCS.DimensionTrim));
        // bindings.put(WCS.Extension, new ComplexEMFBinding(Wcs20Factory.eINSTANCE, WCS.Extension));
        // bindings.put(WCS.GetCapabilities, new ComplexEMFBinding(Wcs20Factory.eINSTANCE, WCS.GetCapabilities));
        // bindings.put(WCS.GetCoverage, new ComplexEMFBinding(Wcs20Factory.eINSTANCE, WCS.GetCoverage));
        bindings.put(WCS.OfferedCoverage, new ComplexEMFBinding(Wcs20Factory.eINSTANCE, WCS.OfferedCoverage));
        bindings.put(WCS.ServiceMetadata, new ComplexEMFBinding(Wcs20Factory.eINSTANCE, WCS.ServiceMetadata));
        bindings.put(WCS.ServiceParameters, new ComplexEMFBinding(Wcs20Factory.eINSTANCE, WCS.ServiceParameters));
        */
    }

    /** Generates the bindings registrations for this class */
    @SuppressWarnings("PMD.SystemPrintln")
    public static void main(String[] args) {
        for (Field f : WCS.class.getFields()) {
            if ((f.getModifiers() & (Modifier.STATIC | Modifier.FINAL)) != 0
                    && f.getType().equals(QName.class)) {
                System.out.println(
                        "bindings.put(WCS."
                                + f.getName()
                                + ", new ComplexEMFBinding(Wcs20Factory.eINSTANCE, WCS."
                                + f.getName()
                                + "));");
            }
        }
    }
}
