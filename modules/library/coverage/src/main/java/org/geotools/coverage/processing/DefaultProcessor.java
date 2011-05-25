/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2001-2008, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.coverage.processing;

import org.opengis.coverage.Coverage;


/**
 * Default implementation of a {@linkplain Coverage coverage} processor.
 * This default implementation makes the following assumptions:
 * <p>
 * <ul>
 *   <li>Operations are declared in the
 *       {@code META-INF/services/org.opengis.coverage.processing.Operation} file.</li>
 *   <li>Operations are actually instances of {@link AbstractOperation} (note: this constraint
 *       may be relaxed in a future version after GeoAPI interfaces for grid coverage will be
 *       redesigned).</li>
 *   <li>Most operations are backed by <cite>Java Advanced Imaging</cite>.</li>
 * </ul>
 * <p>
 * <strong>Note:</strong> This implementation do not caches produced coverages. Since coverages
 * may be big, consider wrapping {@code DefaultProcessor} instances in {@link BufferedProcessor}.
 *
 * @since 2.2
 *
 * @source $URL: http://svn.osgeo.org/geotools/branches/2.7.x/build/maven/javadoc/../../../modules/library/coverage/src/main/java/org/geotools/coverage/processing/DefaultProcessor.java $
 * @version $Id$
 * @author Martin Desruisseaux (IRD) * 
 * @deprecated use {@link CoverageProcessor}
 */
public class DefaultProcessor extends AbstractProcessor {
	
}
