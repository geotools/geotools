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
import org.geotools.api.metadata.lineage.Lineage;

/**
 * Quality information for the data specified by a data quality scope.
 *
 * @version <A HREF="http://www.opengeospatial.org/standards/as#01-111">ISO 19115</A>
 * @author Martin Desruisseaux (IRD)
 * @since GeoAPI 2.0
 */
public interface DataQuality {
    /**
     * The specific data to which the data quality information applies.
     *
     * @return The specific data to which the data quality information applies.
     */
    Scope getScope();

    /**
     * Quantitative quality information for the data specified by the scope. Should be provided only
     * if {@linkplain Scope#getLevel scope level} is {@linkplain
     * org.geotools.api.metadata.maintenance.ScopeCode#DATASET dataset}.
     *
     * @return Quantitative quality information for the data.
     */
    Collection<? extends Element> getReports();

    /**
     * Non-quantitative quality information about the lineage of the data specified by the scope.
     * Should be provided only if {@linkplain Scope#getLevel scope level} is {@linkplain
     * org.geotools.api.metadata.maintenance.ScopeCode#DATASET dataset}.
     *
     * @return Non-quantitative quality information about the lineage of the data specified, or
     *     {@code null}.
     */
    Lineage getLineage();
}
