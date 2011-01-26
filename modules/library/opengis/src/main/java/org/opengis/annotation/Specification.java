/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2011, Open Source Geospatial Foundation (OSGeo)
 *    (C) 2003-2005 Open Geospatial Consortium Inc.
 *    
 *    All Rights Reserved. http://www.opengis.org/legal/
 */
package org.opengis.annotation;


/**
 * The specifications from which an interface, method or code list was derived.
 *
 * @author  Martin Desruisseaux (IRD)
 * @since   GeoAPI 2.0
 */
public enum Specification {
    /**
     * ISO 19103, Geographic information - Conceptual schema language.
     * This is the specification for some interfaces in package {@link org.opengis.util}.
     */
    ISO_19103,

    /**
     * ISO 19107, Feature Geometry (Topic 1).
     * This is the specification for package {@link org.opengis.geometry} and sub-packages.
     *
     * @see <A HREF="http://www.opengeospatial.org/standards/as">Buy from ISO</A>
     */
    ISO_19107,

    /**
     * ISO 19108, Temporal Schema.
     * This is the specification for package {@link org.opengis.temporal} and sub-packages.
     */
    ISO_19108,

    /**
     * ISO 19111, Spatial Referencing by Coordinates (Topic 2).
     * This is the specification for package {@link org.opengis.referencing} and sub-packages.
     *
     * @see #OGC_01009
     * @see <A HREF="http://www.opengeospatial.org/standards/as#04-046r3">Download from OGC</A>
     */
    ISO_19111,

    /**
     * ISO 19115, Metadata (Topic 11).
     * This is the specification for package {@link org.opengis.metadata} and sub-packages.
     *
     * @see <A HREF="http://www.opengeospatial.org/standards/as#01-111">Buy from ISO</A>
     */
    ISO_19115,

    /**
     * ISO 19117, Portrayal.
     * This is an abstract specification for portraying features.
     *
     * @see <A HREF="http://www.iso.org/iso/iso_catalogue/catalogue_tc/catalogue_detail.htm?csnumber=40395">Buy from ISO</A>
     */
    ISO_19117,

    /**
     * ISO 19123, Schema for coverage geometry and functions.
     * This is the specification for package {@link org.opengis.coverage} and sub-packages.
     *
     * @see #OGC_01004
     */
    ISO_19123,

    /**
     * ISO 19128, Layers and styles.
     * This is the specification for package {@link org.opengis.layer} and sub-packages.
     *
     * @see <A HREF="http://portal.opengeospatial.org/files/?artifact_id=5316">Download from OGC</A>
     */
    ISO_19128,

    /**
     * GO-1 Application Objects.
     * This is the specification for package {@link org.opengis.go} and sub-packages.
     *
     * @see <A HREF="http://www.opengeospatial.org/standards/go">Download from OGC</A>
     */
    OGC_03064,

    /**
     * Coordinate Transformation Services implementation specification.
     * This is the specification used as a complement of {@linkplain #ISO_19111 ISO 19111}
     * when an aspect was not defined in the ISO specification.
     *
     * @see #ISO_19111
     * @see <A HREF="http://www.opengeospatial.org/standards/ct">Download from OGC</A>
     */
    OGC_01009,

    /**
     * Grid Coverages implementation specification.
     * This is the specification used as a complement of {@linkplain #ISO_19123 ISO 19123}
     * when an aspect was not defined in the ISO specification.
     *
     * @see #ISO_19123
     * @see <A HREF="http://www.opengeospatial.org/standards/gc">Download from OGC</A>
     */
    OGC_01004,

    /**
     * Filter encoding implementation specification.
     * This is the specification for package {@link org.opengis.filter} and sub-packages.
     *
     * @see <A HREF="http://www.opengeospatial.org/standards/filter">Download from OGC</A>
     *
     * @todo Need to be updated to {@code OGC 04-095}.
     */
    OGC_02059,

    /**
     * Styled Layer Descriptor (SLD) implementation specification.
     * This is the specification for package {@link org.opengis.sld} and sub-packages.
     *
     * @see <A HREF="http://www.opengeospatial.org/standards/sld">Download from OGC</A>
     *
     * @todo Need to be updated to {@code OGC 05-078}.
     */
    OGC_02070,

    /**
     * Web Feature Service implementation specification.
     * This is the specification for package {@link org.opengis.feature} and sub-packages.
     *
     * @see <A HREF="http://www.opengeospatial.org/standards/wfs">Download from OGC</A>
     */
    OGC_04094,

    /**
     * Observations and Measurements, part 1.
     * This is the specification for package {@link org.opengis.observation}.
     *
     * @see <A HREF="http://www.opengeospatial.org/standards/bp#05-087r4">Download from OGC</A>
     *
     * @todo Need to update the hyperlink.
     */
    OGC_07022,

    /**
     * Observations and Measurements, part 2: Sampling Features.
     * This is the specification for package {@link org.opengis.observation.sampling}.
     *
     * @see <A HREF="http://www.opengeospatial.org/standards/bp#05-087r4">Download from OGC</A>
     *
     * @todo Need to update the hyperlink.
     */
    OGC_07002,

    /**
     * Specification not yet determined. This is a temporary enumeration
     * for the processing of API submitted by some contributors.
     */
    UNSPECIFIED
}
