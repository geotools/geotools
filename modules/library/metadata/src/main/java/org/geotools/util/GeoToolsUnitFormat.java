/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2018, Open Source Geospatial Foundation (OSGeo)
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
 *
 *    This package contains documentation from OpenGIS specifications.
 *    OpenGIS consortium's work is fully acknowledged here.
 */

/*
 * Units of Measurement Implementation for Java SE
 * Copyright (c) 2005-2018, Jean-Marie Dautelle, Werner Keil, Otavio Santana.
 *
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without modification,
 * are permitted provided that the following conditions are met:
 *
 * 1. Redistributions of source code must retain the above copyright notice,
 *    this list of conditions and the following disclaimer.
 *
 * 2. Redistributions in binary form must reproduce the above copyright notice, this list of conditions
 *    and the following disclaimer in the documentation and/or other materials provided with the distribution.
 *
 * 3. Neither the name of JSR-363 nor the names of its contributors may be used to endorse or promote products
 *    derived from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO,
 * THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE
 * FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED
 * AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE,
 * EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package org.geotools.util;

import java.util.HashMap;
import java.util.Map;
import javax.measure.Unit;
import javax.measure.format.UnitFormat;
import tech.units.indriya.format.SimpleUnitFormat;

/**
 * Base class extending UOM SimpleUnitFormat that provides the same setup as {@link
 * SimpleUnitFormat#getInstance()} but allows for extension and can, unlike the libraries we depend
 * on, actually parse back what it formatted (might be reworked/removed once this ticket is solved,
 * https://github.com/unitsofmeasurement/uom-se/issues/201, but will require an overhaul of our unit
 * dependencies to a new set of jars). It can also be used to build special subclasses without
 * littering the main SimpleUnitFormat with a single global configuration summing up different
 * points of view.
 *
 * @author Andrea Aime - GeoSolutions
 */
public abstract class GeoToolsUnitFormat extends SimpleUnitFormat {

    private static BaseGT2Format INSTANCE;

    static {
        INSTANCE = new BaseGT2Format();
        INSTANCE.initUnits(SimpleUnitFormat.getInstance());
    }

    public static SimpleUnitFormat getInstance() {
        return INSTANCE;
    }

    /**
     * Base class that just copies {@link UnitFormat} default instance contents
     *
     * @author Andrea Aime - GeoSolutions
     */
    protected static class BaseGT2Format extends DefaultFormat {

        public BaseGT2Format() {}

        protected void initUnits(UnitFormat base) {
            /**
             * Labels and alias are only defined on the DEFAULT format instance, so these
             * definitions are not inherited by subclassing DefaultFormat. Therefore, we need to
             * clone these definitions in our GT formats
             */
            try {
                HashMap<String, Unit<?>> nameToUnitMap = getNameToUnitMap(base);
                HashMap<Unit<?>, String> unitToNameMap = getUnitToNameMap(base);

                for (Map.Entry<String, Unit<?>> entry : nameToUnitMap.entrySet()) {
                    String name = entry.getKey();
                    Unit<?> unit = entry.getValue();

                    if (unitToNameMap.containsKey(unit) && name.equals(unitToNameMap.get(unit))) {
                        label(unit, name);
                        addUnit(unit);
                    } else {
                        alias(unit, name);
                    }
                }
            } catch (Throwable t) {
                throw new RuntimeException(
                        "Failed to initialize the "
                                + this.getClass().getCanonicalName()
                                + " format unit parser with the same values as "
                                + base.getClass().getCanonicalName()
                                + "  one",
                        t);
            }
        }

        private HashMap<Unit<?>, String> getUnitToNameMap(UnitFormat base)
                throws NoSuchFieldException, IllegalAccessException {
            java.lang.reflect.Field unitToNameField =
                    DefaultFormat.class.getDeclaredField("unitToName");
            unitToNameField.setAccessible(true);

            //noinspection unchecked
            return (HashMap<Unit<?>, String>) unitToNameField.get(base);
        }

        private HashMap<String, Unit<?>> getNameToUnitMap(UnitFormat base)
                throws NoSuchFieldException, IllegalAccessException {
            java.lang.reflect.Field nameToUnitField =
                    DefaultFormat.class.getDeclaredField("nameToUnit");
            nameToUnitField.setAccessible(true);

            //noinspection unchecked
            return (HashMap<String, Unit<?>>) nameToUnitField.get(base);
        }

        /** Defaults to being a no-op, subclasses can override */
        protected void addUnit(Unit<?> unit) {}
    }
}
