/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2002-2008, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.referencing.operation.projection;

import org.geotools.metadata.iso.citation.Citations;
import org.geotools.referencing.CRS;
import org.geotools.referencing.wkt.Formatter;
import org.geotools.referencing.wkt.Symbols;
import org.junit.Assert;
import org.junit.Test;
import org.opengis.referencing.crs.CoordinateReferenceSystem;

/**
 * Test the ESRI NamedIdentifier aliases for several projections
 *
 * @author Cesar Martinez Izquierdo (Scolab)
 */
public class EsriNamedIdentifierAliasesTest {

    /**
     * Constructs a CRS by parsing the provided WKT, then checks to format the CRS back to WKT using
     * ESRI and EPSG citations, to ensure the right projection names are used.
     */
    protected void checkCitation(String sourceWkt, String esriProj, String epsgProj)
            throws Exception {
        CoordinateReferenceSystem crs = CRS.parseWKT(sourceWkt);

        Formatter esriFormatter = new Formatter(Symbols.DEFAULT, 0);
        esriFormatter.setAuthority(Citations.ESRI);
        esriFormatter.append(crs);

        String wkt = esriFormatter.toString();
        Assert.assertTrue(wkt.contains(esriProj));

        Formatter epsgFormatter = new Formatter(Symbols.DEFAULT, 0);
        epsgFormatter.setAuthority(Citations.EPSG);
        epsgFormatter.append(crs);
        wkt = epsgFormatter.toString();
        Assert.assertTrue(wkt.contains(epsgProj));
    }

    /**
     * Test citations for the ESRI wkt equivalent to EPSG:4398, which uses EPSG:9806 Cassini-Soldner
     * projection method
     */
    @Test
    public void cassiniSoldnerCitation() throws Exception {
        // @formatter:off
        String cassiniSoldnerWkt =
                "PROJCS[\"Kertau_1968_Kelantan_Grid\","
                        + "GEOGCS[\"GCS_Kertau\","
                        + "DATUM[\"D_Kertau\",SPHEROID[\"Everest_1830_Modified\",6377304.063,300.8017]],"
                        + "PRIMEM[\"Greenwich\",0.0],UNIT[\"Degree\",0.0174532925199433]],"
                        + "PROJECTION[\"Cassini\"],PARAMETER[\"False_Easting\",0.0],"
                        + "PARAMETER[\"False_Northing\",0.0],"
                        + "PARAMETER[\"Central_Meridian\",102.1772916666667],"
                        + "PARAMETER[\"Latitude_Of_Origin\",5.893922222222224],"
                        + "UNIT[\"Meter\",1.0]]";
        checkCitation(
                cassiniSoldnerWkt, "PROJECTION[\"Cassini\"]", "PROJECTION[\"Cassini-Soldner\"]");
    }

    /**
     * Test citations for the ESRI wkt equivalent to EPSG:4087 CRS, which uses EPSG:1028 method -
     * Equidistant Cylindrical [Ellipsoidal]
     */
    @Test
    public void equidistantCylindricalCitation() throws Exception {
        // @formatter:off
        String equidistantCylindricalWkt =
                "PROJCS[\"WGS_1984_World_Equidistant_Cylindrical\","
                        + "GEOGCS[\"GCS_WGS_1984\",DATUM[\"D_WGS_1984\",SPHEROID[\"WGS_1984\",6378137.0,298.257223563]],"
                        + "PRIMEM[\"Greenwich\",0.0],UNIT[\"Degree\",0.0174532925199433]],"
                        + "PROJECTION[\"Equidistant_Cylindrical_Ellipsoidal\"],"
                        + "PARAMETER[\"False_Easting\",0.0],PARAMETER[\"False_Northing\",0.0],PARAMETER[\"Central_Meridian\",0.0],PARAMETER[\"Standard_Parallel_1\",0.0],"
                        + "UNIT[\"Meter\",1.0]]";
        // @formatter:on
        checkCitation(
                equidistantCylindricalWkt,
                "PROJECTION[\"Equidistant_Cylindrical_Ellipsoidal\"]",
                "PROJECTION[\"Equidistant Cylindrical\"]");
    }

    /**
     * Test citations for the ESRI wkt equivalent to EPSG:4088 CRS, which uses EPSG:1029 method -
     * Equidistant Cylindrical (Spherical)
     */
    @Test
    public void equidistantCylindricalSphericalCitation() throws Exception {
        // @formatter:off
        String equidistantCylindricalSphericalWkt =
                "PROJCS[\"World_Equidistant_Cylindrical_(Sphere)\","
                        + "GEOGCS[\"GCS_Sphere_GRS_1980_Authalic\",DATUM[\"D_Sphere_GRS_1980_Authalic\",SPHEROID[\"Sphere_GRS_1980_Authalic\",6371007.0,0.0]],"
                        + "PRIMEM[\"Greenwich\",0.0],UNIT[\"Degree\",0.0174532925199433]],"
                        + "PROJECTION[\"Equidistant_Cylindrical\"],"
                        + "PARAMETER[\"False_Easting\",0.0],PARAMETER[\"False_Northing\",0.0],PARAMETER[\"Central_Meridian\",0.0],PARAMETER[\"Standard_Parallel_1\",0.0],"
                        + "UNIT[\"Meter\",1.0]]";
        // @formatter:on
        // Since ESRI "Equidistant_Cylindrical" (Spherical) definition has the same name as OGC
        // "Equidistant_Cylindrical" (Ellipsoidal), we can only test that
        // some of these providers have been used. It is not a big issue, since both share the same
        // projection class so it will behave the same in both cases.
        checkCitation(
                equidistantCylindricalSphericalWkt,
                "PROJECTION[\"Equidistant_Cylindrical",
                "PROJECTION[\"Equidistant Cylindrical");
    }
}
