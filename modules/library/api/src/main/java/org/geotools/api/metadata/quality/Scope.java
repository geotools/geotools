/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2011, Open Source Geospatial Foundation (OSGeo)
 *    (C) 2004-2005, Open Geospatial Consortium Inc.
 *
 *    All Rights Reserved. http://www.opengis.org/legal/
 */
package org.geotools.api.metadata.quality;

import java.util.Collection;
import org.geotools.api.metadata.extent.Extent;
import org.geotools.api.metadata.maintenance.ScopeCode;
import org.geotools.api.metadata.maintenance.ScopeDescription;

/**
 * Description of the data specified by the scope.
 *
 * @version <A HREF="http://www.opengeospatial.org/standards/as#01-111">ISO 19115</A>
 * @author Martin Desruisseaux (IRD)
 * @since GeoAPI 2.0
 */
public interface Scope {
    /**
     * Hierarchical level of the data specified by the scope.
     *
     * @return Hierarchical level of the data.
     */
    ScopeCode getLevel();

    /**
     * Detailed description about the level of the data specified by the scope. Should be defined
     * only if the {@linkplain #getLevel level} is not equal to {@link ScopeCode#DATASET DATASET} or
     * {@link ScopeCode#SERIES SERIES}.
     *
     * @return Detailed description about the level of the data.
     * @since GeoAPI 2.1
     */
    Collection<? extends ScopeDescription> getLevelDescription();

    /**
     * Information about the spatial, vertical and temporal extent of the data specified by the
     * scope.
     *
     * @return Information about the extent of the data, or {@code null}.
     */
    Extent getExtent();
}
