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
 * {@linkplain org.opengis.metadata.content.ContentInformation Content information}
 * (includes Feature catalogue and Coverage descriptions).
 * The following is adapted from <A HREF="http://www.opengis.org/docs/01-111.pdf">OpenGIS&reg;
 * Metadata (Topic 11)</A> specification.
 *
 * <P ALIGN="justify">This package contains information identifying the feature catalogue used
 * ({@linkplain org.opengis.metadata.content.FeatureCatalogueDescription feature catalogue description})
 * and/or information describing the content of a coverage dataset
 * ({@linkplain org.opengis.metadata.content.CoverageDescription coverage description}). Both description
 * entities are subclasses of the {@linkplain org.opengis.metadata.content.ContentInformation content
 * information} entity. {@linkplain org.opengis.metadata.content.CoverageDescription Coverage description}
 * may be subclassed as {@linkplain org.opengis.metadata.content.ImageDescription image description}.</P>
 *
 * @version <A HREF="http://www.opengeospatial.org/standards/as#01-111">ISO 19115</A>
 * @since   GeoAPI 2.0
 */
package org.opengis.metadata.content;
