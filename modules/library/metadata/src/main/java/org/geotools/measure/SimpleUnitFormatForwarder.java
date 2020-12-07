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

import javax.measure.Unit;
import tech.units.indriya.format.SimpleUnitFormat;

abstract class SimpleUnitFormatForwarder extends SimpleUnitFormat {

    /**
     * Base class that includes {@link javax.measure.format.UnitFormat} default instance contents as
     * well as common NonSI units.
     *
     * @author Andrea Aime - GeoSolutions
     */
    protected static class BaseUnitFormatter extends DefaultFormat implements UnitFormatter {

        public BaseUnitFormatter() {
            // SimpleUnitFormat.initDefaultFormat(this);
            Units.registerCustomUnits(this);
        }

        @Override
        public void label(Unit<?> unit, String label) {
            super.label(unit, label);
            addUnit(unit);
        }

        /** Defaults to being a no-op, subclasses can override */
        protected void addUnit(Unit<?> unit) {}
    }
}
