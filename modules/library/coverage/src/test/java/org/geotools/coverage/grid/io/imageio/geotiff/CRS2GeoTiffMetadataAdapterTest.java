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

import static org.junit.Assert.assertEquals;

import org.geotools.api.referencing.FactoryException;
import org.geotools.api.referencing.crs.CoordinateReferenceSystem;
import org.geotools.coverage.grid.io.imageio.geotiff.codes.GeoTiffGCSCodes;
import org.geotools.referencing.CRS;
import org.junit.Test;

public class CRS2GeoTiffMetadataAdapterTest {

    /** Checks encoding of custom CRS parameters is using the expected structure (key/value) */
    @Test
    public void testEncodeIAU() throws GeoTiffException, FactoryException {
        CoordinateReferenceSystem crs = CRS.decode("IAU:49900");
        CRS2GeoTiffMetadataAdapter adapter = new CRS2GeoTiffMetadataAdapter(crs);
        GeoTiffIIOMetadataEncoder meta = adapter.parseCoordinateReferenceSystem();
        String value = meta.getGeoAsciiParam(GeoTiffGCSCodes.GeogCitationGeoKey);

        assertEquals(
                "GCS Name = Mars (2015) - Sphere / Ocentric|Datum = Mars (2015) - Sphere|Ellipsoid = Mars (2015) - Sphere|Primem = Reference Meridian|",
                value);
    }

    @Test
    public void testBasic() throws GeoTiffException, FactoryException {
        CoordinateReferenceSystem crs = CRS.decode("EPSG:4326", true);
        CRS2GeoTiffMetadataAdapter adapter = new CRS2GeoTiffMetadataAdapter(crs);
        GeoTiffIIOMetadataEncoder meta = adapter.parseCoordinateReferenceSystem();
        // root of the key array, has special meaning, {KeyDirectoryVersion, KeyRevision, MinorRevision, NumberOfKeys}
        GeoKeyEntry entry = meta.getGeoKeyEntryAt(0);
        assertEquals(1, entry.getTiffTagLocation()); // this is the major revision)
        assertEquals(2, entry.getCount()); // this is the major revision)

        // check the CRS
        assertEquals(GeoTiffGCSCodes.ModelTypeGeographic, meta.getGeoShortParam(GeoTiffConstants.GTModelTypeGeoKey));
        assertEquals(4326, meta.getGeoShortParam(GeoTiffGCSCodes.GeographicTypeGeoKey));
    }
}
