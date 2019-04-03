/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2019, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.imageio.netcdf;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

class MultipleBandsDimensionInfo {

    private final List<String> bandsNames = new ArrayList<>();

    MultipleBandsDimensionInfo(String rawBandsNames) {
        Collections.addAll(bandsNames, rawBandsNames.split("\\s*,\\s*"));
    }

    void addBandName(String name) {
        bandsNames.add(name);
    }

    int getNumberOfBands() {
        return bandsNames.size();
    }

    List<String> getBandsNamesInOrder() {
        return bandsNames;
    }
}
