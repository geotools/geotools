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
 * {@linkplain org.opengis.metadata.lineage.Lineage} information. The following is adapted from
 * <A HREF="http://www.opengis.org/docs/01-111.pdf">OpenGIS&reg; Metadata (Topic 11)</A> specification.
 *
 * <P ALIGN="justify">This package also contains information about the sources and production processes
 * used in producing a dataset. The {@linkplain org.opengis.metadata.lineage.Lineage lineage} entity is
 * optional and contains a statement about the lineage. It is an aggregate of
 * {@linkplain org.opengis.metadata.lineage.ProcessStep process step} and
 * {@linkplain org.opengis.metadata.lineage.Source source}. The
 * {@linkplain org.opengis.metadata.lineage.Lineage#getStatement statement} element is mandatory if
 * <CODE>{@linkplain org.opengis.metadata.quality.DataQuality#getScope DataQuality.scope}.{@linkplain
 * org.opengis.metadata.quality.Scope#getLevel level}</CODE> has a value of
 * "{@linkplain org.opengis.metadata.maintenance.ScopeCode#DATASET dataset}" or
 * "{@linkplain org.opengis.metadata.maintenance.ScopeCode#SERIES series}" and the
 * {@linkplain org.opengis.metadata.lineage.Source source} and
 * {@linkplain org.opengis.metadata.lineage.ProcessStep process step} are not set.</P>
 *
 * @version <A HREF="http://www.opengeospatial.org/standards/as#01-111">ISO 19115</A>
 * @since   GeoAPI 2.0
 */
package org.opengis.metadata.lineage;
