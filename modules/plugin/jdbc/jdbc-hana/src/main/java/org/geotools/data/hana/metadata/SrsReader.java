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
 * Reader for the embedded spatial reference system metadata.
 *
 * @author Stefan Uhrig, SAP SE
 */
class SrsReader {

    public SrsReader(InputStream is) {
        csvReader = new CsvReader(is);
    }

    private CsvReader csvReader;

    public Srs readNextSrs() throws IOException {
        List<String> entries = csvReader.readNextRow();
        if (entries == null) {
            return null;
        }
        if (entries.size() != 16) {
            throw new RuntimeException("SRS-CSV is malformed");
        }

        String name = entries.get(0);
        int srid = Integer.parseInt(entries.get(1));
        String organization = entries.get(2);
        int organizationId = Integer.parseInt(entries.get(3));
        String wkt = entries.get(4);
        String proj4 = entries.get(5);
        String linearUom = entries.get(6);
        String angularUom = entries.get(7);
        String stype = entries.get(8);
        Srs.Type type;
        if ("projected".equals(stype)) {
            type = Srs.Type.PROJECTED;
        } else if ("geographic".equals(stype)) {
            type = Srs.Type.GEOGRAPHIC;
        } else if ("flat".equals(stype)) {
            type = Srs.Type.FLAT;
        } else {
            throw new RuntimeException("SRS-CSV is malformed");
        }
        Double majorAxis =
                "null".equals(entries.get(9)) ? null : Double.parseDouble(entries.get(9));
        Double minorAxis =
                "null".equals(entries.get(10)) ? null : Double.parseDouble(entries.get(10));
        Double invFlattening =
                "null".equals(entries.get(11)) ? null : Double.parseDouble(entries.get(11));
        double minx = Double.parseDouble(entries.get(12));
        double maxx = Double.parseDouble(entries.get(13));
        double miny = Double.parseDouble(entries.get(14));
        double maxy = Double.parseDouble(entries.get(15));
        return new Srs(
                name,
                srid,
                organization,
                organizationId,
                wkt,
                proj4,
                linearUom,
                angularUom,
                type,
                majorAxis,
                minorAxis,
                invFlattening,
                minx,
                maxx,
                miny,
                maxy);
    }
}
