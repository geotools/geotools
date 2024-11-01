/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2023, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.coverage.grid.io.imageio.geotiff;

/**
 * Parsers for the {@link org.geotools.coverage.grid.io.imageio.geotiff.codes.GeoTiffGCSCodes#GeogCitationGeoKey} values
 * and returns them as separate fields
 */
public class GeographicCitation {

    String pcsName;
    String prjName;

    String lunits;

    String gcsName;

    String datum;

    String ellipsoid;

    String primem;

    String aunits;

    public GeographicCitation(String citation) {
        String[] maps = citation.split("\\|");

        for (String map : maps) {
            int idx = map.indexOf("=");
            if (idx > 0 && idx < map.length() - 1) {
                String key = map.substring(0, idx).trim();
                String value = map.substring(idx + 1).trim();

                switch (key) {
                    case "PCS Name":
                        pcsName = value;
                        break;
                    case "PRJ Name":
                        prjName = value;
                        break;
                    case "Lunits":
                        lunits = value;
                        break;
                    case "GCS Name":
                        gcsName = value;
                        break;
                    case "Datum":
                        datum = value;
                        break;
                    case "Ellipsoid":
                        ellipsoid = value;
                        break;
                    case "Primem":
                        primem = value;
                        break;
                    case "Aunits":
                        aunits = value;
                        break;
                }
            }
        }
    }

    public String getPcsName() {
        return pcsName;
    }

    public String getPrjName() {
        return prjName;
    }

    public String getLunits() {
        return lunits;
    }

    public String getGcsName() {
        return gcsName;
    }

    public String getDatum() {
        return datum;
    }

    public String getEllipsoid() {
        return ellipsoid;
    }

    public String getPrimem() {
        return primem;
    }

    public String getAunits() {
        return aunits;
    }
}
