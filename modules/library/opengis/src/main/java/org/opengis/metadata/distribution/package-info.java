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
 * {@linkplain org.opengis.metadata.distribution.Distribution} information.
 * The following is adapted from <A HREF="http://www.opengis.org/docs/01-111.pdf">OpenGIS&reg;
 * Metadata (Topic 11)</A> specification.
 *
 * <P ALIGN="justify">This package contains information about the distributor of, and options for
 * obtaining, a resource. The optional {@linkplain org.opengis.metadata.distribution.Distribution
 * distribution} entity is an aggregate of the options for the digital distribution of a dataset
 * ({@linkplain org.opengis.metadata.distribution.DigitalTransferOptions digital transfer options}),
 * identification of the {@linkplain org.opengis.metadata.distribution.Distributor distributor} and
 * the {@linkplain org.opengis.metadata.distribution.Format format} of the distribution, which contain
 * mandatory and optional elements. {@linkplain org.opengis.metadata.distribution.DigitalTransferOptions
 * Digital transfer options} contains the {@linkplain org.opengis.metadata.distribution.Medium medium}
 * used for the distribution of a dataset. {@linkplain org.opengis.metadata.distribution.Distributor}
 * is an aggregate of the process for ordering a distribution
 * ({@linkplain org.opengis.metadata.distribution.StandardOrderProcess standard order process}). The
 * {@linkplain org.opengis.metadata.distribution.Distribution#getDistributionFormats distribution format}
 * of {@linkplain org.opengis.metadata.distribution.Distribution distribution} is mandatory if the
 * {@linkplain org.opengis.metadata.distribution.Distributor#getDistributorFormats distribution format}
 * of {@linkplain org.opengis.metadata.distribution.Distributor distributor} is not set.</P>
 *
 * @version <A HREF="http://www.opengeospatial.org/standards/as#01-111">ISO 19115</A>
 * @since   GeoAPI 2.0
 */
package org.opengis.metadata.distribution;
