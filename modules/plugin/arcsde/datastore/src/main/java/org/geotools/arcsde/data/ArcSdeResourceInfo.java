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

package org.geotools.arcsde.data;

import java.io.IOException;
import java.net.URI;
import java.util.HashSet;
import java.util.Set;

import org.geotools.data.ResourceInfo;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.opengis.util.InternationalString;

/**
 * {@link ResourceInfo} adapter for a {@link FeatureTypeInfo} and {@link ArcSdeFeatureSource}
 * 
 * @author Gabriel Roldan (TOPP)
 * @version $Id$
 * @since 2.5.x
 * @source $URL:
 *         http://svn.geotools.org/geotools/trunk/gt/modules/plugin/arcsde/datastore/src/main/java
 *         /org/geotools/arcsde/data/ArcSdeResourceInfo.java $
 */
public final class ArcSdeResourceInfo implements ResourceInfo {

    private FeatureTypeInfo info;

    private ArcSdeFeatureSource source;

    private Set<String> cachedKeywords;

    private URI namespace;

    /**
     * @param info
     *            where to grab most of the information from
     * @param source
     *            where the grab the bounds. Its a live value, so asked every time
     *            {@link #getBounds()} is called
     */
    ArcSdeResourceInfo(final FeatureTypeInfo info, final ArcSdeFeatureSource source) {
        this.info = info;
        this.source = source;
        String nsUri = info.getFeatureType().getName().getNamespaceURI();
        if (nsUri != null) {
            namespace = URI.create(nsUri);
        }
    }

    /**
     * @see org.geotools.data.ResourceInfo#getBounds()
     */
    public synchronized ReferencedEnvelope getBounds() {
        ReferencedEnvelope bounds;
        try {
            bounds = source.getBounds();
        } catch (IOException e) {
            CoordinateReferenceSystem crs = info.getFeatureType().getCoordinateReferenceSystem();
            bounds = new ReferencedEnvelope(crs);
        }
        return bounds;
    }

    /**
     * @see org.geotools.data.ResourceInfo#getCRS()
     */
    public CoordinateReferenceSystem getCRS() {
        return info.getFeatureType().getCoordinateReferenceSystem();
    }

    /**
     * @see org.geotools.data.ResourceInfo#getDescription()
     */
    public String getDescription() {
        return null;
    }

    /**
     * @see org.geotools.data.ResourceInfo#getKeywords()
     */
    public synchronized Set<String> getKeywords() {
        if (this.cachedKeywords == null) {
            cachedKeywords = new HashSet<String>();
            cachedKeywords.add("ArcSDE");
            cachedKeywords.add(info.getFeatureTypeName());
            if (info.isInProcessView()) {
                cachedKeywords.add("in-process view");
            }
            if (info.isVersioned()) {
                cachedKeywords.add("versioned");
            }
            if (info.isView()) {
                cachedKeywords.add("registered view");
            }
            if (info.isWritable()) {
                cachedKeywords.add("writable");
            }
        }
        return new HashSet<String>(this.cachedKeywords);
    }

    /**
     * @see org.geotools.data.ResourceInfo#getName()
     */
    public String getName() {
        return info.getFeatureTypeName();
    }

    /**
     * @see org.geotools.data.ResourceInfo#getSchema()
     */
    public URI getSchema() {
        return namespace;
    }

    /**
     * @see org.geotools.data.ResourceInfo#getTitle()
     */
    public String getTitle() {
        InternationalString description = info.getFeatureType().getDescription();
        return description == null ? info.getFeatureTypeName() : description.toString();
    }

    // ArcSDE specific extensions

    /**
     * Returns whether the ArcSDE Table is multi-versioned
     * 
     * @return {@code true} if the table is marked as multiversioned, {@code false} otherwise
     */
    public boolean isVersioned() {
        return info.isVersioned();
    }

    /**
     * Returns whether the ArcSDE Table is a registered View
     * 
     * @return {@code true} if the table is an ArcSDE registered View, {@code false} otherwise
     */
    public boolean isView() {
        return info.isView();
    }

    /**
     * Returns whether the FeatureType is built at runtime by a SQL SELECT statement in the ArcSDE
     * DataStore configuration parameters.
     * 
     * @return {@code true} if the FeatureType does not refers to an actual table or registered
     *         view, but is built from a SQL SELECT statement, {@code false} otherwise
     */
    public boolean isInProcessView() {
        return info.isInProcessView();
    }
}
