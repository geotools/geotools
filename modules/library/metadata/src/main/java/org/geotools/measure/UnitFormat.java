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

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/** A factory for unit formatters that support the units required by GeoTools. */
public final class UnitFormat extends BaseUnitFormatter {

    public static UnitFormat getInstance() {
        return INSTANCE;
    }

    public static UnitFormat create() {
        return new UnitFormat(UNIT_DEFINITIONS);
    }

    private UnitFormat(List<UnitDefinition> unitDefinitions) {
        super(unitDefinitions);
    }

    private static final List<UnitDefinition> UNIT_DEFINITIONS =
            Stream.of(
                            UnitDefinitions.DIMENSIONLESS,
                            UnitDefinitions.CONSTANTS,
                            UnitDefinitions.SI_BASE,
                            UnitDefinitions.SI_DERIVED,
                            UnitDefinitions.NON_SI,
                            UnitDefinitions.US_CUSTOMARY,
                            UnitDefinitions.GEOTOOLS)
                    .flatMap(Collection::stream)
                    .collect(Collectors.toUnmodifiableList());

    private static final UnitFormat INSTANCE = new UnitFormat(UNIT_DEFINITIONS);
}
