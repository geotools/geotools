/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2006, Open Source Geospatial Foundation (OSGeo)
 *
 *    This library is free software; you can redistribute it and/or
 *    modify it under the terms of the GNU Lesser General Public
 *    License as published by the Free Software Foundation;
 *    version 2.1 of the License.
 *
 *    This library is distributed in the hope that it will be useful,
 *    but WITHOUT ANY WARRANTY; without even the implied warranty of
 *    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 *    Lesser General Public License for more details.
 */

/**
 * Extensions to the {@linkplain java.util.logging Java logging} framework. The GeoTools project uses the standard
 * {@link java.util.logging.Logger} API for its logging, but this package allows redirection of logs to some other
 * frameworks like <a href="https://logging.apache.org/log4j/">Log4J</a>.
 *
 * <p><strong>All GeoTools code should fetch their logger through a call to
 * {@link org.geotools.util.logging.Logging#getLogger(String)}</strong>, not
 * {@link java.util.logging.Logger#getLogger(String)}. This is necessary in order to give GeoTools a chance to redirect
 * log events to an other logging framework.
 */
package org.geotools.util.logging;
