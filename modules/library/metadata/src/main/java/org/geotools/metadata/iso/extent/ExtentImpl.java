/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2004-2008, Open Source Geospatial Foundation (OSGeo)
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
 *
 *    This package contains documentation from OpenGIS specifications.
 *    OpenGIS consortium's work is fully acknowledged here.
 */
package org.geotools.metadata.iso.extent;

import java.util.Collection;
import org.geotools.api.metadata.extent.Extent;
import org.geotools.api.metadata.extent.GeographicBoundingBox;
import org.geotools.api.metadata.extent.GeographicExtent;
import org.geotools.api.metadata.extent.TemporalExtent;
import org.geotools.api.metadata.extent.VerticalExtent;
import org.geotools.api.util.InternationalString;
import org.geotools.metadata.iso.MetadataEntity;

/**
 * Information about spatial, vertical, and temporal extent. This interface has four optional attributes
 * ({@linkplain #getGeographicElements geographic elements}, {@linkplain #getTemporalElements temporal elements}, and
 * {@linkplain #getVerticalElements vertical elements}) and an element called {@linkplain #getDescription description}.
 * At least one of the four shall be used.
 *
 * @version $Id$
 * @author Martin Desruisseaux (IRD)
 * @author Touraïvane
 * @since 2.1
 */
public class ExtentImpl extends MetadataEntity implements Extent {
    /** Serial number for interoperability with different versions. */
    private static final long serialVersionUID = 7812213837337326257L;

    /**
     * A geographic extent ranging from 180°W to 180°E and 90°S to 90°N.
     *
     * @since 2.2
     */
    public static final Extent WORLD;

    static {
        final ExtentImpl world = new ExtentImpl();
        world.getGeographicElements().add(GeographicBoundingBoxImpl.WORLD);
        world.freeze();
        WORLD = world;
    }

    /** Returns the spatial and temporal extent for the referring object. */
    private InternationalString description;

    /** Provides geographic component of the extent of the referring object */
    private Collection<GeographicExtent> geographicElements;

    /** Provides temporal component of the extent of the referring object */
    private Collection<TemporalExtent> temporalElements;

    /** Provides vertical component of the extent of the referring object */
    private Collection<VerticalExtent> verticalElements;

    /** Constructs an initially empty extent. */
    public ExtentImpl() {}

    /**
     * Constructs a metadata entity initialized with the values from the specified metadata.
     *
     * @since 2.4
     */
    public ExtentImpl(final Extent source) {
        super(source);
    }

    /** Returns the spatial and temporal extent for the referring object. */
    @Override
    public InternationalString getDescription() {
        return description;
    }

    /** Set the spatial and temporal extent for the referring object. */
    public void setDescription(final InternationalString newValue) {
        checkWritePermission();
        description = newValue;
    }

    /** Provides geographic component of the extent of the referring object */
    @Override
    public Collection<GeographicExtent> getGeographicElements() {
        return geographicElements = nonNullCollection(geographicElements, GeographicExtent.class);
    }

    /** Set geographic component of the extent of the referring object */
    public void setGeographicElements(final Collection<? extends GeographicExtent> newValues) {
        geographicElements = copyCollection(newValues, geographicElements, GeographicExtent.class);
    }

    /** Provides temporal component of the extent of the referring object */
    @Override
    public Collection<TemporalExtent> getTemporalElements() {
        return temporalElements = nonNullCollection(temporalElements, TemporalExtent.class);
    }

    /** Set temporal component of the extent of the referring object */
    public void setTemporalElements(final Collection<? extends TemporalExtent> newValues) {
        temporalElements = copyCollection(newValues, temporalElements, TemporalExtent.class);
    }

    /** Provides vertical component of the extent of the referring object */
    @Override
    public Collection<VerticalExtent> getVerticalElements() {
        return verticalElements = nonNullCollection(verticalElements, VerticalExtent.class);
    }

    /** Set vertical component of the extent of the referring object */
    public void setVerticalElements(final Collection<? extends VerticalExtent> newValues) {
        verticalElements = copyCollection(newValues, verticalElements, VerticalExtent.class);
    }

    /**
     * Convenience method returning a single geographic bounding box from the specified extent. If no bounding box was
     * found, then this method returns {@code null}. If more than one box is found, then boxes are
     * {@linkplain GeographicBoundingBoxImpl#add added} together.
     *
     * @since 2.2
     */
    public static GeographicBoundingBox getGeographicBoundingBox(final Extent extent) {
        GeographicBoundingBox candidate = null;
        if (extent != null) {
            GeographicBoundingBoxImpl modifiable = null;
            for (final GeographicExtent element : extent.getGeographicElements()) {
                final GeographicBoundingBox bounds;
                if (element instanceof GeographicBoundingBox) {
                    bounds = (GeographicBoundingBox) element;
                } else {
                    continue;
                }
                /*
                 * A single geographic bounding box has been extracted. Now add it to previous
                 * ones (if any). All exclusion boxes before the first inclusion box are ignored.
                 */
                if (candidate == null) {
                    /*
                     * Reminder: 'inclusion' is a mandatory attribute, so it should never be
                     * null for a valid metadata object.  If the metadata object is invalid,
                     * it is better to get an exception than having a code doing silently
                     * some probably inappropriate work.
                     */
                    final Boolean inclusion = bounds.getInclusion();
                    ensureNonNull("inclusion", inclusion);
                    if (inclusion.booleanValue()) {
                        candidate = bounds;
                    }
                } else {
                    if (modifiable == null) {
                        modifiable = new GeographicBoundingBoxImpl(candidate);
                        candidate = modifiable;
                    }
                    modifiable.add(bounds);
                }
            }
            if (modifiable != null) {
                modifiable.freeze();
            }
        }
        return candidate;
    }
}
