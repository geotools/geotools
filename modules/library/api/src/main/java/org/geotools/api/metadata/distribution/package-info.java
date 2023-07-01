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
 * {@linkplain org.geotools.api.metadata.distribution.Distribution} information. The following is
 * adapted from <A HREF="http://www.opengis.org/docs/01-111.pdf">OpenGIS&reg; Metadata (Topic
 * 11)</A> specification.
 *
 * <P ALIGN="justify">This package contains information about the distributor of, and options for
 * obtaining, a resource. The optional {@linkplain
 * org.geotools.api.metadata.distribution.Distribution distribution} entity is an aggregate of the
 * options for the digital distribution of a dataset ({@linkplain
 * org.geotools.api.metadata.distribution.DigitalTransferOptions digital transfer options}),
 * identification of the {@linkplain org.geotools.api.metadata.distribution.Distributor distributor}
 * and the {@linkplain org.geotools.api.metadata.distribution.Format format} of the distribution,
 * which contain mandatory and optional elements. {@linkplain
 * org.geotools.api.metadata.distribution.DigitalTransferOptions Digital transfer options} contains
 * the {@linkplain org.geotools.api.metadata.distribution.Medium medium} used for the distribution
 * of a dataset. {@linkplain org.geotools.api.metadata.distribution.Distributor} is an aggregate of
 * the process for ordering a distribution ({@linkplain
 * org.geotools.api.metadata.distribution.StandardOrderProcess standard order process}). The
 * {@linkplain org.geotools.api.metadata.distribution.Distribution#getDistributionFormats
 * distribution format} of {@linkplain org.geotools.api.metadata.distribution.Distribution
 * distribution} is mandatory if the {@linkplain
 * org.geotools.api.metadata.distribution.Distributor#getDistributorFormats distribution format} of
 * {@linkplain org.geotools.api.metadata.distribution.Distributor distributor} is not set.
 *
 * @version <A HREF="http://www.opengeospatial.org/standards/as#01-111">ISO 19115</A>
 * @since GeoAPI 2.0
 */
package org.geotools.api.metadata.distribution;
