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
 */
package org.geotools.data.hana.metadata;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

/**
 * Reader for the embedded unit-of-measure metadata.
 *
 * @author Stefan Uhrig, SAP SE
 */
class UomReader {

    public UomReader(InputStream is) {
        csvReader = new CsvReader(is);
    }

    private CsvReader csvReader;

    public Uom readNextUom() throws IOException {
        List<String> entries = csvReader.readNextRow();
        if (entries == null) {
            return null;
        }
        if (entries.size() != 3) {
            throw new RuntimeException("UOM-CSV is malformed");
        }
        String name = entries.get(0);
        String stype = entries.get(1);
        Uom.Type type;
        if ("linear".equals(stype)) {
            type = Uom.Type.LINEAR;
        } else if ("angular".equals(stype)) {
            type = Uom.Type.ANGULAR;
        } else {
            throw new RuntimeException("UOM-CSV is malformed");
        }
        double factor = Double.parseDouble(entries.get(2));
        return new Uom(name, type, factor);
    }
}
