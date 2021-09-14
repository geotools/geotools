/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2021, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.measure;

import tech.units.indriya.format.SimpleUnitFormat;

abstract class SimpleUnitFormatForwarder extends SimpleUnitFormat {

    /**
     * A package-private forwarder for BaseUnitFormatter, such that users can use that top-level
     * class instead of this nested class here.
     */
    static class DefaultFormatForwarder extends DefaultFormat implements UnitFormatter {}
}
