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
package org.geotools.metadata.iso.quality;

import java.util.Collection;

import org.opengis.metadata.extent.Extent;
import org.opengis.metadata.quality.Scope;
import org.opengis.metadata.maintenance.ScopeCode;
import org.opengis.metadata.maintenance.ScopeDescription;
import org.geotools.metadata.iso.MetadataEntity;


/**
 * Description of the data specified by the scope.
 *
 *
 * @source $URL$
 * @version $Id$
 * @author Martin Desruisseaux (IRD)
 * @author Touraïvane
 *
 * @since 2.1
 */
public class ScopeImpl extends MetadataEntity implements Scope {
    /**
     * Serial number for interoperability with different versions.
     */
    private static final long serialVersionUID = -8021256328527422972L;

    /**
     * Hierarchical level of the data specified by the scope.
     */
    private ScopeCode level;

    /**
     * Information about the spatial, vertical and temporal extent of the data specified by the
     * scope.
     */
    private Extent extent;

    /**
     * Detailed description about the level of the data specified by the scope.
     */
    private Collection<ScopeDescription> levelDescription;

    /**
     * Constructs an initially empty scope.
     */
    public ScopeImpl() {
    }

    /**
     * Constructs a metadata entity initialized with the values from the specified metadata.
     *
     * @since 2.4
     */
    public ScopeImpl(final Scope source) {
        super(source);
    }

    /**
     * Creates a scope initialized to the given level.
     */
    public ScopeImpl(final ScopeCode level) {
        setLevel(level);
    }

    /**
     * Hierarchical level of the data specified by the scope.
     */
    public ScopeCode getLevel() {
        return level;
    }

    /**
     * Set the hierarchical level of the data specified by the scope.
     */
    public synchronized void setLevel(final ScopeCode newValue) {
        checkWritePermission();
        level = newValue;
    }

    /**
     * Returns detailed descriptions about the level of the data specified by the scope.
     * Should be defined only if the {@linkplain #getLevel level} is not equal
     * to {@link ScopeCode#DATASET DATASET} or {@link ScopeCode#SERIES SERIES}.
     *
     * @since 2.4
     */
    public synchronized Collection<ScopeDescription> getLevelDescription() {
        return levelDescription = nonNullCollection(levelDescription, ScopeDescription.class);
    }

    /**
     * Set detailed descriptions about the level of the data specified by the scope.
     *
     * @since 2.4
     */
    public synchronized void setLevelDescription(
            final Collection<? extends ScopeDescription> newValues)
    {
        levelDescription = copyCollection(newValues, levelDescription, ScopeDescription.class);
    }

    /**
     * Information about the spatial, vertical and temporal extent of the data specified by the
     * scope.
     */
    public Extent getExtent() {
        return extent;
    }

    /**
     * Set information about the spatial, vertical and temporal extent of the data specified
     * by the scope.
     */
    public synchronized void setExtent(final Extent newValue) {
        checkWritePermission();
        extent = newValue;
    }
}
