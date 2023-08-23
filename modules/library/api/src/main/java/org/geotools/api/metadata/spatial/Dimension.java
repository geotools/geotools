/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2011, Open Source Geospatial Foundation (OSGeo)
 *    (C) 2004-2005, Open Geospatial Consortium Inc.
 *
 *    All Rights Reserved. http://www.opengis.org/legal/
 */
package org.geotools.api.metadata.spatial;

/**
 * Axis properties.
 *
 * @version <A HREF="http://www.opengeospatial.org/standards/as#01-111">ISO 19115</A>
 * @author Martin Desruisseaux (IRD)
 * @author Cory Horner (Refractions Research)
 * @since GeoAPI 2.0
 */
public interface Dimension {
    /**
     * Name of the axis.
     *
     * @return Name of the axis.
     */
    DimensionNameType getDimensionName();

    /**
     * Number of elements along the axis.
     *
     * @return Number of elements along the axis.
     */
    Integer getDimensionSize();

    /**
     * Degree of detail in the grid dataset.
     *
     * @return Degree of detail in the grid dataset, or {@code null}.
     * @unitof Measure
     */
    Double getResolution();
}
