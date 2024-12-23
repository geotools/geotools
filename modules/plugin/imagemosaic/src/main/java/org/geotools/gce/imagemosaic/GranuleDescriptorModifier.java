/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2020, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.gce.imagemosaic;

import org.geotools.util.factory.Hints;

/**
 * Simple interface to update a GranuleDescriptor. Implementations may use provided hints to update editable fields of a
 * GranuleDescriptor, accordingly
 */
public interface GranuleDescriptorModifier {

    void update(GranuleDescriptor granuleDescriptor, Hints hints);
}
