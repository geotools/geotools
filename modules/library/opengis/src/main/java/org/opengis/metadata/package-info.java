/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2011, Open Source Geospatial Foundation (OSGeo)
 *    (C) 2004-2005, Open Geospatial Consortium Inc.
 *    
 *    All Rights Reserved. http://www.opengis.org/legal/
 */

/**
 * Root package for {@linkplain org.opengis.metadata.MetaData metadata}. The following is adapted from
 * <A HREF="http://www.opengis.org/docs/01-111.pdf">OpenGIS&reg; Metadata (Topic 11)</A> specification.
 *
 * <P ALIGN="justify">The notion of cataloguing a set of related documents together in a discoverable
 * series is common in map catalogues. With digital spatial data, the definition of what constitutes a
 * "dataset" is more problematic and reflects the institutional and software environments of the originating
 * organization. Common metadata can be derived for a series of related spatial datasets, and such metadata
 * is generally relevant or can be inherited by each of the dataset instances. Software to support this
 * inheritance of metadata for geographic data within a cataloguing system can simplify data entry, update
 * and reporting.</P>
 *
 * <P ALIGN="justify">There is a potential hierarchy of re-usable metadata that can be employed in implementing
 * a metadata collection. By creating several levels of abstraction, a linked hierarchy can assist in filtering
 * or targeting user queries to the requested level of detail. The hierarchy should not necessarily be interpreted
 * to require multiple copies of metadata being managed on-line. Conversely, the definition of general metadata can
 * be supplemented by spatially specific metadata that, when queried, either inherits or overrides the general case.
 * Through use of pointers this method can reduce the redundancy of metadata managed at a site and provide for different
 * views of the holdings by users.</P>
 *
 * <H2>Data series metadata</H2>
 * <P ALIGN="justify">A series or collection of spatial data which share similar characteristics of theme, source date,
 * resolution, and methodology. The exact definition of what constitutes a series entry will be determined by the data
 * provider. Examples of data series metadata entries may include:</P>
 * <UL>
 *   <LI><P ALIGN="justify">A flight line of digital aerial photographs collected during a single flight with one
 *       camera and film type. A continuous scan swathe collected from a satellite using the same sensors on a single
 *       orbital pass.</P></LI>
 *   <LI><P ALIGN="justify">A collection of raster map data captured from a common series of paper maps.</P></LI>
 *   <LI><P ALIGN="justify">A collection of vector datasets depicting surface hydrography with associated attribution
 *        for multiple administrative areas within a country.</P></LI>
 * </UL>
 * <P ALIGN="justify">The creation of a "data series" metadata level is an optional feature that allows users to
 * consult higher-level characteristics for data search. The definition of this type of metadata may be adequate
 * for the initial characterization of available spatial data, but may not be adequate for detailed assessment of
 * data quality of specific datasets.</P>
 *
 * <H2>Dataset metadata</H2>
 * <P ALIGN="justify">For the purposes of this specification, a dataset should be a consistent spatial data product
 * instance that can be generated or made available by a spatial data distributor. A dataset may be a member of a
 * data series, as defined in the previous subclause. A dataset may be composed of a set of identified feature types
 * and instances, and attribute types and instances as described in the following four subclauses.</P>
 *
 * <P ALIGN="justify">On a demand basis, metadata from series and dataset information will be merged to present the
 * user with a view of the metadata at the dataset level of abstraction. Metadata for which no hierarchy is listed
 * are interpreted to be "dataset" metadata, by default.</P>
 *
 * <H2>Feature type metadata</H2>
 * <P ALIGN="justify">Spatial constructs known as features are grouped spatial primitives (0-, 1- and 2-dimensional
 * geometric objects) that have a common identity. Spatial data services may elect to support feature type-level
 * metadata where it is available and make such metadata available for query or retrieval. Feature Type -level metadata,
 * together with feature instance-, attribute type- and attribute instance-level metadata, will be grouped into datasets,
 * as defined in the previous subclause. Examples of feature type metadata entries may include:</P>
 * <UL>
 *   <LI>All bridges within a dataset.</LI>
 * </UL>
 *
 * <H2>Feature instance metadata</H2>
 * <P ALIGN="justify">Feature instances are spatial constructs (features) that have a direct correspondence with a
 * real world object. Spatial data services may elect to support feature instance-level metadata where it is available
 * and make such metadata available for query or retrieval. Feature Instance-level metadata, together with feature type-,
 * attribute type- and attribute instance-level metadata, will be grouped into datasets. Examples of feature
 * instance metadata entries may include:</P>
 * <UL>
 *   <LI>The Sydney harbour bridge.</LI>
 *   <LI>The Golden Gate bridge, in San Francisco.</LI>
 * </UL>
 *
 * <H2>Attribute type metadata</H2>
 * <P ALIGN="justify">Attribute types are the digital parameters that describe a common aspect of grouped spatial primitives
 * (0-, 1- and 2- dimensional geometric objects). Spatial data services may elect to support attribute type-level metadata
 * where it is available and make such metadata available for query or retrieval. Attribute type-level metadata, together with
 * feature type-, feature instance and attribute instance-level metadata, will be grouped into datasets. Examples of attribute
 * type metadata entries may include:</P>
 * <UL>
 *   <LI>Overhead clearance associated with a bridge.</LI>
 * </UL>
 *
 * <H2>Attribute instance metadata</H2>
 * <P ALIGN="justify">Attribute instances are the digital parameters that describe an aspect of a feature instance.
 * Spatial data services may elect to support attribute instance-level metadata where it is available and make such
 * metadata available for query or retrieval. Attribute instance-level metadata, together with feature type-, feature
 * instance and attribute typelevel metadata, will be grouped into datasets. Examples of attribute instance metadata
 * entries may include:</P>
 * <UL>
 *   <LI>The overhead clearance associated with a specific bridge across a road.</LI>
 * </UL>
 *
 * @version <A HREF="http://www.opengeospatial.org/standards/as#01-111">ISO 19115</A>
 * @since   GeoAPI 2.0
 */
package org.opengis.metadata;
