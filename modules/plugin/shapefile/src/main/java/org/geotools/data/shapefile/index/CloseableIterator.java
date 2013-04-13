/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2008, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.data.shapefile.index;

import java.io.IOException;
import java.util.Iterator;

/**
 * An iterator backed by some resource that needs closing when done using
 * @author Andrea Aime - OpenGeo
 *
 *
 * @source $URL$
 */
public interface CloseableIterator<E> extends Iterator<E> {

    public void close() throws IOException;
}
