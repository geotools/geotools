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
 * {@linkplain org.opengis.metadata.constraint.Constraints} information
 * (includes legal and security). The following is adapted from
 * <A HREF="http://www.opengis.org/docs/01-111.pdf">OpenGIS&reg; Metadata (Topic 11)</A> specification.
 *
 * <P ALIGN="justify">This package contains information concerning the restrictions placed on data.
 * The {@linkplain org.opengis.metadata.constraint.Constraints constraints} entity is optional and
 * may be specified as {@linkplain org.opengis.metadata.constraint.LegalConstraints legal constraints}
 * and/or {@linkplain org.opengis.metadata.constraint.SecurityConstraints security constraints}. The
 * {@linkplain org.opengis.metadata.constraint.LegalConstraints#getOtherConstraints other constraint}
 * element shall be non-null (used) only if
 * {@linkplain org.opengis.metadata.constraint.LegalConstraints#getAccessConstraints access constraints} and/or
 * {@linkplain org.opengis.metadata.constraint.LegalConstraints#getUseConstraints use constraints} elements have
 * a value of "{@linkplain org.opengis.metadata.constraint.Restriction#OTHER_RESTRICTIONS other restrictions}".</P>
 *
 * @version <A HREF="http://www.opengeospatial.org/standards/as#01-111">ISO 19115</A>
 * @since   GeoAPI 2.0
 */
package org.opengis.metadata.constraint;
