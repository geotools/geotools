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
 * {@linkplain org.opengis.metadata.spatial.SpatialRepresentation Spatial representation} information
 * (includes grid and vector representation).
 * The following is adapted from <A HREF="http://www.opengis.org/docs/01-111.pdf">OpenGIS&reg;
 * Metadata (Topic 11)</A> specification.
 *
 * <P ALIGN="justify">This package contains information concerning the mechanisms used to represent
 * spatial information in a dataset. The {@linkplain org.opengis.metadata.spatial.SpatialRepresentation
 * spatial representation} entity is optional and can be specified as
 * {@linkplain org.opengis.metadata.spatial.GridSpatialRepresentation grid spatial representation} and
 * {@linkplain org.opengis.metadata.spatial.VectorSpatialRepresentation vector spatial representation}.
 * Each of the specified entities contains mandatory and optional metadata elements. When further
 * description is necessary,
 * {@linkplain org.opengis.metadata.spatial.VectorSpatialRepresentation vector spatial representation}
 * may be specified as {@linkplain org.opengis.metadata.spatial.Georectified georectified}. and/or
 * {@linkplain org.opengis.metadata.spatial.Georeferenceable georeferenceable} entity.</P>
 *
 * @version <A HREF="http://www.opengeospatial.org/standards/as#01-111">ISO 19115</A>
 * @since   GeoAPI 2.0
 */
package org.opengis.metadata.spatial;
