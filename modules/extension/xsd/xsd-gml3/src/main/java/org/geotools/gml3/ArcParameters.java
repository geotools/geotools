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
 * @source $URL: http://svn.osgeo.org/geotools/branches/2.7.x/build/maven/javadoc/../../../modules/extension/xsd/xsd-gml3/src/main/java/org/geotools/gml3/ArcParameters.java $
 */
public class ArcParameters {
    protected LinearizationTolerance linearizationTolerance;

    public ArcParameters() {
        // default:
        this.linearizationTolerance = new CircleRadiusTolerance(0.001);
    }

    /**
     *
     */
    public ArcParameters(LinearizationTolerance linearizationTolerance) {
        this.linearizationTolerance = linearizationTolerance;
    }

    public LinearizationTolerance getLinearizationTolerance() {
        return linearizationTolerance;
    }

}
