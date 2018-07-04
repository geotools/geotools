/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2016 Open Source Geospatial Foundation (OSGeo)
 *    (C) 2014-2016 Boundless Spatial
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
package org.geotools.ysld;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import javax.measure.Unit;
import javax.measure.quantity.Length;
import org.geotools.styling.UomOgcMapping;

/** Maps YSLD uom names and aliases defined by {@link UomOgcMapping} to {@link Unit}s. */
public class UomMapper {
    private final Map<Unit<Length>, String> toId = new HashMap<>();

    private final Map<String, Unit<Length>> toUnit = new HashMap<>();

    public static final String KEY = "uomMapper";

    public UomMapper() {
        // Canonical
        toId.put(UomOgcMapping.METRE.getUnit(), "metre"); // Preferred spelling in SI and OGC
        toUnit.put("metre", UomOgcMapping.METRE.getUnit());

        toId.put(UomOgcMapping.FOOT.getUnit(), "foot");
        toUnit.put("foot", UomOgcMapping.FOOT.getUnit());

        toId.put(UomOgcMapping.PIXEL.getUnit(), "pixel");
        toUnit.put("pixel", UomOgcMapping.PIXEL.getUnit());

        // Aliases
        toUnit.put("meter", UomOgcMapping.METRE.getUnit());
        toUnit.put("m", UomOgcMapping.METRE.getUnit());
        toUnit.put(UomOgcMapping.METRE.getSEString(), UomOgcMapping.METRE.getUnit());

        toUnit.put("ft", UomOgcMapping.FOOT.getUnit());
        toUnit.put(UomOgcMapping.FOOT.getSEString(), UomOgcMapping.FOOT.getUnit());

        toUnit.put("px", UomOgcMapping.PIXEL.getUnit());
        toUnit.put(UomOgcMapping.PIXEL.getSEString(), UomOgcMapping.PIXEL.getUnit());
    }

    public Unit<Length> getUnit(String identifier) {
        Unit<Length> unit = toUnit.get(identifier.toLowerCase(Locale.ENGLISH));
        if (unit == null) {
            throw new IllegalArgumentException("Unknown unit identifier: " + identifier);
        }
        return (Unit<Length>) unit;
    }

    public String getIdentifier(Unit<Length> unit) {
        String identifier = toId.get(unit);
        if (identifier == null) {
            throw new IllegalArgumentException("Unit not supported by YSLD: " + unit.toString());
        }
        return identifier;
    }
}
