/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2011, Open Source Geospatial Foundation (OSGeo)
 *    (C) 2005 Open Geospatial Consortium Inc.
 *
 *    All Rights Reserved. http://www.opengis.org/legal/
 */
/**
 * Profile of the general ISO 19107 feature model built around the idea of a simple feature
 * composed of a list of values.
 *
 * <p>
 * A {@link SimpleFeature} is a wrapper around a list of values. The values are interpreted
 * based on order; and must be supplied in exactly the the order indicated by the {@link SimpleFeatureType}.
 *
 * <center><img src="doc-files/simple.GIF"></center>
 *
 * This model matches the assumptions of GeoAPI 2.1 and is applicable in a wide range of applications
 * from the representation of shapefiles; to simple database tables (with no external references).
 */
package org.geotools.api.feature.simple;
