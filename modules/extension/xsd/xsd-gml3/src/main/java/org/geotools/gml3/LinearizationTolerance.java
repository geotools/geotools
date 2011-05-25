/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2002-2011, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.gml3;

/**
 *
 * @author Erik van de Pol
 *
 * @source $URL: http://svn.osgeo.org/geotools/branches/2.7.x/build/maven/javadoc/../../../modules/extension/xsd/xsd-gml3/src/main/java/org/geotools/gml3/LinearizationTolerance.java $
 */
public interface LinearizationTolerance {
    /**
     * Returns the linearization tolerance for the arc linearization algorithm.
     * @param circle The circle the Arc is part of. This circle is calculated during the Arc linearization algorithm.
     * @return The maximum distance after linearization, between the original Arc and the linearized Arc.
     */
    public double getTolerance(Circle circle);
}
