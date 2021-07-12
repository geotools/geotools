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
package org.geotools.referencing.wkt;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.geotools.measure.BaseUnitFormatter;
import org.geotools.measure.UnitDefinition;
import org.geotools.measure.UnitDefinitions;
import org.geotools.measure.UnitFormatter;

/** A factory for unit formatters that support the EPSG dialect. */
public final class EpsgUnitFormat {

    public static UnitFormatter getInstance() {
        return INSTANCE;
    }

    private EpsgUnitFormat() {}

    private static final List<UnitDefinition> UNIT_DEFINITIONS =
            Stream.of(
                            UnitDefinitions.DIMENSIONLESS,
                            UnitDefinitions.CONSTANTS,
                            UnitDefinitions.SI_BASE,
                            UnitDefinitions.SI_DERIVED,
                            UnitDefinitions.NON_SI,
                            UnitDefinitions.US_CUSTOMARY,
                            UnitDefinitions.EPSG)
                    .flatMap(Collection::stream)
                    .collect(Collectors.toList());

    private static final BaseUnitFormatter INSTANCE = new BaseUnitFormatter(UNIT_DEFINITIONS);
}
