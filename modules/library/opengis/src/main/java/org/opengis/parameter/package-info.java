/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2011, Open Source Geospatial Foundation (OSGeo)
 *    (C) 2003-2005, Open Geospatial Consortium Inc.
 *    
 *    All Rights Reserved. http://www.opengis.org/legal/
 */

/**
 * Description and storage of {@linkplain org.opengis.parameter.ParameterValue parameter values}.
 * The first two paragraphs below are adapted from
 * <A HREF="http://portal.opengeospatial.org/files/?artifact_id=6716">OpenGIS&reg;
 * Spatial Referencing by Coordinates (Topic 2)</A> specification.
 *
 * <P ALIGN="justify">Most {@linkplain org.opengis.parameter.ParameterValue parameter values} are
 * numeric, but for some operation methods, notably those implementing a grid interpolation
 * algorithm, the parameter value could be a file name and location (this may be a URI). An
 * example is the coordinate transformation from NAD&nbsp;27 to NAD&nbsp;83 in the USA; depending
 * on the locations of the points to be transformed, one of a series of grid files should be used.</P>
 *
 * <P ALIGN="justify">Some operation methods may require a large number of coordinate operation
 * parameters. Also, some operation methods require that groups of parameters be repeatable as
 * a group. In such cases, it is helpful to group related parameters in
 * {@linkplain org.opengis.parameter.ParameterDescriptorGroup parameter groups}. Two or more parameter
 * groups are then associated with a particular operation method, and each parameter group consists
 * of a set of {@linkplain org.opengis.parameter.ParameterDescriptor operation parameters}, or other,
 * nested parameter groups. This way of modelling is not mandatory; all coordinate operation
 * parameters may be assigned directly to the coordinate operation method.</P>
 *
 * @version <A HREF="http://portal.opengeospatial.org/files/?artifact_id=6716">Abstract specification 2.0</A>
 * @since   GeoAPI 1.0
 */
package org.opengis.parameter;
