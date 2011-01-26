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
package org.geotools.metadata;

import java.util.Collection;
import java.util.Set;
import java.util.HashSet;

import org.opengis.util.CodeList;
import org.opengis.metadata.MetaData;
import org.opengis.metadata.extent.VerticalExtent;
import org.opengis.metadata.citation.OnLineResource;
import org.opengis.metadata.citation.CitationFactory;
import org.opengis.metadata.content.ImagingCondition;
import org.opengis.metadata.content.CoverageContentType;
import org.opengis.metadata.maintenance.ScopeDescription;
import org.opengis.metadata.identification.AggregateInformation;
import org.opengis.metadata.identification.RepresentativeFraction;

import org.geotools.resources.Classes;
import org.geotools.util.CheckedCollection;
import org.geotools.metadata.iso.MetaDataImpl;

import org.junit.*;
import static org.junit.Assert.*;


/**
 * Tests every implementation in the {@link org.geotools.metadata.iso} package.
 *
 * @source $URL$
 * @version $Id$
 * @author Martin Desruisseaux (Geomatys)
 *
 * @todo Current implementation relies on {@link MetaData} dependencies. This is probably
 *       not enough; we should provide an explicit list of metadata interface.
 */
public final class ISOTest {
    /**
     * {@code true} for displaying debugging informations.
     */
    private static final boolean VERBOSE = false;

    /**
     * Root package for interfaces, with trailing dot.
     */
    private static final String INTERFACE_PACKAGE = "org.opengis.metadata.";

    /**
     * Root package for implementations, with trailing dot.
     */
    private static final String IMPLEMENTATION_PACKAGE = "org.geotools.metadata.iso.";

    /**
     * Suffix for implementation classes.
     */
    private static final String IMPLEMENTATION_SUFFIX = "Impl";

    /**
     * List of GeoAPI interfaces to test. This list is not exclusive, since this test suite
     * will automatically scans for dependencies even if an interface do not appears in this
     * list. This list should not contains any {@link CodeList}.
     */
    private static final Class<?>[] TEST = new Class[] {
        org.opengis.metadata.ApplicationSchemaInformation.class,
        org.opengis.metadata.ExtendedElementInformation.class,
        org.opengis.metadata.FeatureTypeList.class,
        org.opengis.metadata.Identifier.class,
        org.opengis.metadata.MetaData.class,
        org.opengis.metadata.MetadataExtensionInformation.class,
        org.opengis.metadata.PortrayalCatalogueReference.class,
        org.opengis.metadata.SpatialAttributeSupplement.class,
        org.opengis.metadata.citation.Address.class,
        org.opengis.metadata.citation.Citation.class,
        org.opengis.metadata.citation.CitationDate.class,
        org.opengis.metadata.citation.CitationFactory.class,
        org.opengis.metadata.citation.Contact.class,
        org.opengis.metadata.citation.OnLineResource.class,
        org.opengis.metadata.citation.ResponsibleParty.class,
        org.opengis.metadata.citation.Series.class,
        org.opengis.metadata.citation.Telephone.class,
        org.opengis.metadata.constraint.Constraints.class,
        org.opengis.metadata.constraint.LegalConstraints.class,
        org.opengis.metadata.constraint.SecurityConstraints.class,
        org.opengis.metadata.content.Band.class,
        org.opengis.metadata.content.ContentInformation.class,
        org.opengis.metadata.content.CoverageDescription.class,
        org.opengis.metadata.content.FeatureCatalogueDescription.class,
        org.opengis.metadata.content.ImageDescription.class,
        org.opengis.metadata.content.RangeDimension.class,
        org.opengis.metadata.distribution.DigitalTransferOptions.class,
        org.opengis.metadata.distribution.Distribution.class,
        org.opengis.metadata.distribution.Distributor.class,
        org.opengis.metadata.distribution.Format.class,
        org.opengis.metadata.distribution.Medium.class,
        org.opengis.metadata.distribution.StandardOrderProcess.class,
        org.opengis.metadata.extent.BoundingPolygon.class,
        org.opengis.metadata.extent.Extent.class,
        org.opengis.metadata.extent.GeographicBoundingBox.class,
        org.opengis.metadata.extent.GeographicDescription.class,
        org.opengis.metadata.extent.GeographicExtent.class,
        org.opengis.metadata.extent.SpatialTemporalExtent.class,
        org.opengis.metadata.extent.TemporalExtent.class,
        org.opengis.metadata.extent.VerticalExtent.class,
        org.opengis.metadata.identification.AggregateInformation.class,
        org.opengis.metadata.identification.BrowseGraphic.class,
        org.opengis.metadata.identification.DataIdentification.class,
        org.opengis.metadata.identification.Identification.class,
        org.opengis.metadata.identification.Keywords.class,
        org.opengis.metadata.identification.RepresentativeFraction.class,
        org.opengis.metadata.identification.Resolution.class,
        org.opengis.metadata.identification.ServiceIdentification.class,
        org.opengis.metadata.identification.Usage.class,
        org.opengis.metadata.lineage.Lineage.class,
        org.opengis.metadata.lineage.ProcessStep.class,
        org.opengis.metadata.lineage.Source.class,
        org.opengis.metadata.maintenance.MaintenanceInformation.class,
        org.opengis.metadata.maintenance.ScopeDescription.class,
        org.opengis.metadata.quality.AbsoluteExternalPositionalAccuracy.class,
        org.opengis.metadata.quality.AccuracyOfATimeMeasurement.class,
        org.opengis.metadata.quality.Completeness.class,
        org.opengis.metadata.quality.CompletenessCommission.class,
        org.opengis.metadata.quality.CompletenessOmission.class,
        org.opengis.metadata.quality.ConceptualConsistency.class,
        org.opengis.metadata.quality.ConformanceResult.class,
        org.opengis.metadata.quality.DataQuality.class,
        org.opengis.metadata.quality.DomainConsistency.class,
        org.opengis.metadata.quality.Element.class,
        org.opengis.metadata.quality.FormatConsistency.class,
        org.opengis.metadata.quality.GriddedDataPositionalAccuracy.class,
        org.opengis.metadata.quality.LogicalConsistency.class,
        org.opengis.metadata.quality.NonQuantitativeAttributeAccuracy.class,
        org.opengis.metadata.quality.PositionalAccuracy.class,
        org.opengis.metadata.quality.QuantitativeAttributeAccuracy.class,
        org.opengis.metadata.quality.QuantitativeResult.class,
        org.opengis.metadata.quality.RelativeInternalPositionalAccuracy.class,
        org.opengis.metadata.quality.Result.class,
        org.opengis.metadata.quality.Scope.class,
        org.opengis.metadata.quality.TemporalAccuracy.class,
        org.opengis.metadata.quality.TemporalConsistency.class,
        org.opengis.metadata.quality.TemporalValidity.class,
        org.opengis.metadata.quality.ThematicAccuracy.class,
        org.opengis.metadata.quality.ThematicClassificationCorrectness.class,
        org.opengis.metadata.quality.TopologicalConsistency.class,
        org.opengis.metadata.spatial.Dimension.class,
        org.opengis.metadata.spatial.GeometricObjects.class,
        org.opengis.metadata.spatial.Georectified.class,
        org.opengis.metadata.spatial.Georeferenceable.class,
        org.opengis.metadata.spatial.GridSpatialRepresentation.class,
        org.opengis.metadata.spatial.SpatialRepresentation.class,
        org.opengis.metadata.spatial.VectorSpatialRepresentation.class
    };

    /**
     * GeoAPI interfaces that are know to be unimplemented at this stage.
     */
    private static final Class<?>[] UNIMPLEMENTED = new Class[] {
        AggregateInformation.class,
        CoverageContentType.class,
        ImagingCondition.class,
        CitationFactory.class,          // SHOULD THIS INTERFACE REALLY EXISTS IN GEOAPI?
        RepresentativeFraction.class,   // Implemented on top of 'Number'.
        VerticalExtent.class,           // Inconsistent 'verticalCRS' type in GeoAPI interface.
        ScopeDescription.class,         // Only partially implemented (no references to Features).
        OnLineResource.class            // No 'setProtocol' method.
    };

    /**
     * Ensures that the {@link #TEST} array do not contains code list.
     */
    @Test
    public void testNoCodeList() {
        for (int i=0; i<TEST.length; i++) {
            final Class type = TEST[i];
            assertFalse(type.getName(), CodeList.class.isAssignableFrom(type));
        }
    }

    /**
     * Tests all dependencies starting from the {@link MetaDataImpl} class.
     */
    @Test
    public void testDependencies() {
        assertNull(getImplementation(Number.class));
        assertSame(MetaDataImpl.class, getImplementation(MetaData.class));
        final Set<Class<?>> done = new HashSet<Class<?>>();
        for (int i=0; i<TEST.length; i++) {
            final Class<?> type = TEST[i];
            final Class<?> impl = getImplementation(type);
            if (impl == null) {
                if (isImplemented(type)) {
                    fail(type.getName() + " is not implemented.");
                }
                continue;
            }
            assertSetters(new PropertyAccessor(impl, type), done);
        }
        if (VERBOSE) {
            System.out.println(done);
        }
    }

    /**
     * Recursively ensures that the specified metadata implementation has
     * setters for every methods.
     */
    private static void assertSetters(final PropertyAccessor accessor, final Set<Class<?>> done) {
        if (done.add(accessor.type)) {
            /*
             * Tries to instantiate the implementation. Every implementation should have a
             * no-args constructor, and their instantiation should never fail. Note that
             * this dummy will also be of some help later in this test.
             */
            final Object dummyInstance;
            final boolean isImplemented = isImplemented(accessor.type);
            if (isImplemented) try {
                dummyInstance = accessor.implementation.getConstructor((Class[]) null).
                        newInstance((Object[]) null);
            } catch (Exception e) {
                fail(e.toString());
                return;
            } else {
                dummyInstance = null;
            }
            /*
             * Iterates over all properties defined in the interface,
             * and checks for the existences of a setter method.
             */
            final String classname = Classes.getShortName(accessor.type) + '.';
            final int count = accessor.count();
            for (int i=0; i<count; i++) {
                final String name = accessor.name(i);
                assertNotNull(String.valueOf(i), name);
                final String fullname = classname + name;
                assertEquals(fullname, i, accessor.indexOf(name));
                if (!isImplemented) {
                    continue;
                }
                // We can not continue below this point for
                // implementations that are only partial.
                assertTrue(fullname, accessor.isWritable(i));
                /*
                 * Get the property type. In the special case where the property type
                 * is a collection, get an empty collection from the implementation.
                 * This is needed in order to get the element type in the collection.
                 */
                Class<?> type = accessor.type(i);
                if (Collection.class.isAssignableFrom(type)) {
                    final Object example = accessor.get(i, dummyInstance);
                    if (example instanceof CheckedCollection) {
                        type = ((CheckedCollection) example).getElementType();
                    }
                }
                final Class<?> impl = getImplementation(type);
                if (impl != null) {
                    assertSetters(new PropertyAccessor(impl, type), done);
                }
            }
        }
    }

    /**
     * Returns the implementation class for the specified interface class,
     * or {@code null} if none.
     */
    private static Class<?> getImplementation(final Class<?> type) {
        if (!CodeList.class.isAssignableFrom(type)) {
            String name = type.getName();
            if (name.startsWith(INTERFACE_PACKAGE)) {
                name = IMPLEMENTATION_PACKAGE +
                        name.substring(INTERFACE_PACKAGE.length()) + IMPLEMENTATION_SUFFIX;
                try {
                    return Class.forName(name);
                } catch (ClassNotFoundException e) {
                    /*
                     * Found a class which is not implemented. Before to report an error,
                     * check if it is part of the list of known unimplemented interfaces.
                     */
                    if (isImplemented(type)) {
                        fail(e.toString());
                    }
                }
            }
        }
        return null;
    }

    /**
     * Returns {@code true} if the specified type is not in the list of
     * known unimplemented types.
     */
    private static boolean isImplemented(final Class<?> type) {
        for (int i=0; i<UNIMPLEMENTED.length; i++) {
            if (type.equals(UNIMPLEMENTED[i])) {
                return false;
            }
        }
        return true;
    }
}
