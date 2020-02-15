/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2006-2008, Open Source Geospatial Foundation (OSGeo)
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
 */
package org.geotools.data;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.geotools.geometry.DirectPosition2D;
import org.geotools.metadata.i18n.ErrorKeys;
import org.geotools.metadata.i18n.Errors;
import org.geotools.referencing.CRS;
import org.geotools.referencing.operation.builder.AffineTransformBuilder;
import org.geotools.referencing.operation.builder.MappedPosition;
import org.opengis.geometry.DirectPosition;
import org.opengis.referencing.FactoryException;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.opengis.referencing.operation.MathTransform;

/**
 * Reader for Mapinfo .TAB files Finds control points and CRS in .TAB file and parses them
 * accordingly and builds a mathtransform and CRS
 *
 * @author Niels Charlier, Scitus Development
 */
public class MapInfoFileReader {

    /** Logger for this class. */
    private static final Logger LOGGER =
            org.geotools.util.logging.Logging.getLogger(MapInfoFileReader.class);

    /** Projection mappings Mapinfo -> WKT */
    private static Map<Integer, String> PROJECTIONS = new HashMap<Integer, String>();

    /** Datum mappings Mapinfo -> WKT */
    private static Map<Integer, String> DATUMS = new HashMap<Integer, String>();

    /** Unit mappings Mapinfo -> WKT */
    private static Map<String, String> UNITS = new HashMap<String, String>();

    /** List of parameters */
    private static final String[] PARAMETERS1 =
            new String[] {
                "central_meridian",
                "latitude_of_origin",
                "standard_parallel_1",
                "standard_parallel_2",
                "false_easting",
                "false_northing"
            };

    private static final String[] PARAMETERS2 =
            new String[] {
                "central_meridian",
                "latitude_of_origin",
                "scale_factor",
                "false_easting",
                "false_northing"
            };

    static {
        PROJECTIONS.put(2, "Cylindrical_Equal_Area");
        PROJECTIONS.put(3, "Lambert_Conformal_Conic_2SP");
        PROJECTIONS.put(4, "Lambert_Azimuthal_Equal_Area");
        PROJECTIONS.put(7, "Hotine_Oblique_Mercator");
        PROJECTIONS.put(8, "Transverse_Mercator");
        PROJECTIONS.put(9, "Albers_Conic_Equal_Area");
        PROJECTIONS.put(10, "Mercator_1SP");
        PROJECTIONS.put(18, "New_Zealand_Map_Grid");
        PROJECTIONS.put(19, "Lambert_Conformal_Conic_2SP_Belgium");
        PROJECTIONS.put(26, "Mercator_1SP");
        PROJECTIONS.put(27, "Polyconic");
        PROJECTIONS.put(28, "Lambert_Azimuthal_Equal_Area");
        PROJECTIONS.put(30, "Cassini_Soldner");
        PROJECTIONS.put(32, "Krovak");

        DATUMS.put(
                1,
                "GEOGCS[\"Adindan\", DATUM[\"Adindan\", SPHEROID[\"Clarke 1880 (RGS)\",6378249.145,293.465], TOWGS84[-166,-15,204,0,0,0,0]], PRIMEM[\"Greenwich\",0], UNIT[\"degree\",0.0174532925199433]]");
        DATUMS.put(
                2,
                "GEOGCS[\"Afgooye\", DATUM[\"Afgooye\", SPHEROID[\"Krassowsky 1940\",6378245,298.3], TOWGS84[-43,-163,45,0,0,0,0]], PRIMEM[\"Greenwich\",0], UNIT[\"degree\",0.0174532925199433]]");
        DATUMS.put(
                3,
                "GEOGCS[\"Ain el Abd\", DATUM[\"Ain_el_Abd_1970\", SPHEROID[\"International 1924\",6378388,297], TOWGS84[-143,-236,7,0,0,0,0]], PRIMEM[\"Greenwich\",0], UNIT[\"degree\",0.0174532925199433]]");
        DATUMS.put(
                5,
                "GEOGCS[\"Arc 1950\", DATUM[\"Arc_1950\", SPHEROID[\"Clarke 1880 (Arc)\",6378249.145,293.4663077], TOWGS84[-143,-90,-294,0,0,0,0]], PRIMEM[\"Greenwich\",0], UNIT[\"degree\",0.0174532925199433]]");
        DATUMS.put(
                6,
                "GEOGCS[\"Arc 1960\", DATUM[\"Arc_1960\", SPHEROID[\"Clarke 1880 (RGS)\",6378249.145,293.465], TOWGS84[-160,-6,-302,0,0,0,0]], PRIMEM[\"Greenwich\",0], UNIT[\"degree\",0.0174532925199433]]");
        DATUMS.put(
                12,
                "GEOGCS[\"AGD66\", DATUM[\"Australian_Geodetic_Datum_1966\", SPHEROID[\"Australian National Spheroid\",6378160,298.25], TOWGS84[-117.808,-51.536,137.784,0.303,0.446,0.234,-0.29]], PRIMEM[\"Greenwich\",0], UNIT[\"degree\",0.0174532925199433]]");
        DATUMS.put(
                13,
                "GEOGCS[\"AGD84\", DATUM[\"Australian_Geodetic_Datum_1984\", SPHEROID[\"Australian National Spheroid\",6378160,298.25], TOWGS84[-134,-48,149,0,0,0,0]], PRIMEM[\"Greenwich\",0], UNIT[\"degree\",0.0174532925199433]]");
        DATUMS.put(
                15,
                "GEOGCS[\"Bermuda 1957\", DATUM[\"Bermuda_1957\", SPHEROID[\"Clarke 1866\",6378206.4,294.9786982139006], TOWGS84[-73,213,296,0,0,0,0]], PRIMEM[\"Greenwich\",0], UNIT[\"degree\",0.0174532925199433]]");
        DATUMS.put(
                16,
                "GEOGCS[\"Bogota 1975\", DATUM[\"Bogota_1975\", SPHEROID[\"International 1924\",6378388,297], TOWGS84[307,304,-318,0,0,0,0]], PRIMEM[\"Greenwich\",0], UNIT[\"degree\",0.0174532925199433]]");
        DATUMS.put(
                17,
                "GEOGCS[\"Campo Inchauspe\", DATUM[\"Campo_Inchauspe\", SPHEROID[\"International 1924\",6378388,297], TOWGS84[-148,136,90,0,0,0,0]], PRIMEM[\"Greenwich\",0], UNIT[\"degree\",0.0174532925199433]]");
        DATUMS.put(
                19,
                "GEOGCS[\"Cape\", DATUM[\"Cape\", SPHEROID[\"Clarke 1880 (Arc)\",6378249.145,293.4663077], TOWGS84[-136,-108,-292,0,0,0,0]], PRIMEM[\"Greenwich\",0], UNIT[\"degree\",0.0174532925199433]]");
        DATUMS.put(
                20,
                "GEOGCS[\"Cape Canaveral\", DATUM[\"Cape_Canaveral\", SPHEROID[\"Clarke 1866\",6378206.4,294.9786982139006], TOWGS84[-2,151,181,0,0,0,0]], PRIMEM[\"Greenwich\",0], UNIT[\"degree\",0.0174532925199433]]");
        DATUMS.put(
                21,
                "GEOGCS[\"Carthage\", DATUM[\"Carthage\", SPHEROID[\"Clarke 1880 (IGN)\",6378249.2,293.4660212936265], TOWGS84[-263,6,431,0,0,0,0]], PRIMEM[\"Greenwich\",0], UNIT[\"degree\",0.0174532925199433]]");
        DATUMS.put(
                22,
                "GEOGCS[\"Chatham Islands 1971\", DATUM[\"Chatham_Islands_Datum_1971\", SPHEROID[\"International 1924\",6378388,297], TOWGS84[175,-38,113,0,0,0,0]], PRIMEM[\"Greenwich\",0], UNIT[\"degree\",0.0174532925199433]]");
        DATUMS.put(
                23,
                "GEOGCS[\"Chua\", DATUM[\"Chua\", SPHEROID[\"International 1924\",6378388,297], TOWGS84[-134,229,-29,0,0,0,0]], PRIMEM[\"Greenwich\",0], UNIT[\"degree\",0.0174532925199433]]");
        DATUMS.put(
                24,
                "GEOGCS[\"Corrego Alegre 1970-72\", DATUM[\"Corrego_Alegre_1970_72\", SPHEROID[\"International 1924\",6378388,297], TOWGS84[-206,172,-6,0,0,0,0]], PRIMEM[\"Greenwich\",0], UNIT[\"degree\",0.0174532925199433]]");
        DATUMS.put(
                25,
                "GEOGCS[\"Batavia\", DATUM[\"Batavia\", SPHEROID[\"Bessel 1841\",6377397.155,299.1528128], TOWGS84[-377,681,-50,0,0,0,0]], PRIMEM[\"Greenwich\",0], UNIT[\"degree\",0.0174532925199433]]");
        DATUMS.put(
                27,
                "GEOGCS[\"Easter Island 1967\", DATUM[\"Easter_Island_1967\", SPHEROID[\"International 1924\",6378388,297], TOWGS84[211,147,111,0,0,0,0]], PRIMEM[\"Greenwich\",0], UNIT[\"degree\",0.0174532925199433]]");
        DATUMS.put(
                28,
                "GEOGCS[\"ED50\", DATUM[\"European_Datum_1950\", SPHEROID[\"International 1924\",6378388,297], TOWGS84[-87,-98,-121,0,0,0,0]], PRIMEM[\"Greenwich\",0], UNIT[\"degree\",0.0174532925199433]]");
        DATUMS.put(
                29,
                "GEOGCS[\"ED79\", DATUM[\"European_Datum_1979\", SPHEROID[\"International 1924\",6378388,297], TOWGS84[-86,-98,-119,0,0,0,0]], PRIMEM[\"Greenwich\",0], UNIT[\"degree\",0.0174532925199433]]");
        DATUMS.put(
                30,
                "GEOGCS[\"Gandajika 1970\", DATUM[\"Gandajika_1970\", SPHEROID[\"International 1924\",6378388,297], TOWGS84[-133,-321,50,0,0,0,0]], PRIMEM[\"Greenwich\",0], UNIT[\"degree\",0.0174532925199433]]");
        DATUMS.put(
                31,
                "GEOGCS[\"NZGD49\", DATUM[\"New_Zealand_Geodetic_Datum_1949\", SPHEROID[\"International 1924\",6378388,297], TOWGS84[59.47,-5.04,187.44,0.47,-0.1,1.024,-4.5993]], PRIMEM[\"Greenwich\",0], UNIT[\"degree\",0.0174532925199433]]");
        DATUMS.put(
                34,
                "GEOGCS[\"Guam 1963\", DATUM[\"Guam_1963\", SPHEROID[\"Clarke 1866\",6378206.4,294.9786982139006], TOWGS84[-100,-248,259,0,0,0,0]], PRIMEM[\"Greenwich\",0], UNIT[\"degree\",0.0174532925199433]]");
        DATUMS.put(
                36,
                "GEOGCS[\"Hito XVIII 1963\", DATUM[\"Hito_XVIII_1963\", SPHEROID[\"International 1924\",6378388,297], TOWGS84[16,196,93,0,0,0,0]], PRIMEM[\"Greenwich\",0], UNIT[\"degree\",0.0174532925199433]]");
        DATUMS.put(
                37,
                "GEOGCS[\"Hjorsey 1955\", DATUM[\"Hjorsey_1955\", SPHEROID[\"International 1924\",6378388,297], TOWGS84[-73,46,-86,0,0,0,0]], PRIMEM[\"Greenwich\",0], UNIT[\"degree\",0.0174532925199433]]");
        DATUMS.put(
                38,
                "GEOGCS[\"Hong Kong 1963\", DATUM[\"Hong_Kong_1963\", SPHEROID[\"Clarke 1858\",6378293.645208759,294.2606763692569]], PRIMEM[\"Greenwich\",0], UNIT[\"degree\",0.0174532925199433]]");
        DATUMS.put(
                39,
                "GEOGCS[\"Hu Tzu Shan 1950\", DATUM[\"Hu_Tzu_Shan_1950\", SPHEROID[\"International 1924\",6378388,297], TOWGS84[-637,-549,-203,0,0,0,0]], PRIMEM[\"Greenwich\",0], UNIT[\"degree\",0.0174532925199433]]");
        DATUMS.put(
                44,
                "GEOGCS[\"Johnston Island 1961\", DATUM[\"Johnston_Island_1961\", SPHEROID[\"International 1924\",6378388,297], TOWGS84[189,-79,-202,0,0,0,0]], PRIMEM[\"Greenwich\",0], UNIT[\"degree\",0.0174532925199433]]");
        DATUMS.put(
                45,
                "GEOGCS[\"Kandawala\", DATUM[\"Kandawala\", SPHEROID[\"Everest 1830 (1937 Adjustment)\",6377276.345,300.8017], TOWGS84[-97,787,86,0,0,0,0]], PRIMEM[\"Greenwich\",0], UNIT[\"degree\",0.0174532925199433]]");
        DATUMS.put(
                47,
                "GEOGCS[\"Kertau 1968\", DATUM[\"Kertau_1968\", SPHEROID[\"Everest 1830 Modified\",6377304.063,300.8017], TOWGS84[-11,851,5,0,0,0,0]], PRIMEM[\"Greenwich\",0], UNIT[\"degree\",0.0174532925199433]]");
        DATUMS.put(
                49,
                "GEOGCS[\"Liberia 1964\", DATUM[\"Liberia_1964\", SPHEROID[\"Clarke 1880 (RGS)\",6378249.145,293.465], TOWGS84[-90,40,88,0,0,0,0]], PRIMEM[\"Greenwich\",0], UNIT[\"degree\",0.0174532925199433]]");
        DATUMS.put(
                52,
                "GEOGCS[\"Mahe 1971\", DATUM[\"Mahe_1971\", SPHEROID[\"Clarke 1880 (RGS)\",6378249.145,293.465], TOWGS84[41,-220,-134,0,0,0,0]], PRIMEM[\"Greenwich\",0], UNIT[\"degree\",0.0174532925199433]]");
        DATUMS.put(
                54,
                "GEOGCS[\"Massawa\", DATUM[\"Massawa\", SPHEROID[\"Bessel 1841\",6377397.155,299.1528128], TOWGS84[639,405,60,0,0,0,0]], PRIMEM[\"Greenwich\",0], UNIT[\"degree\",0.0174532925199433]]");
        DATUMS.put(
                55,
                "GEOGCS[\"Merchich\", DATUM[\"Merchich\", SPHEROID[\"Clarke 1880 (IGN)\",6378249.2,293.4660212936265], TOWGS84[31,146,47,0,0,0,0]], PRIMEM[\"Greenwich\",0], UNIT[\"degree\",0.0174532925199433]]");
        DATUMS.put(
                57,
                "GEOGCS[\"Minna\", DATUM[\"Minna\", SPHEROID[\"Clarke 1880 (RGS)\",6378249.145,293.465], TOWGS84[-92,-93,122,0,0,0,0]], PRIMEM[\"Greenwich\",0], UNIT[\"degree\",0.0174532925199433]]");
        DATUMS.put(
                61,
                "GEOGCS[\"Naparima 1972\", DATUM[\"Naparima_1972\", SPHEROID[\"International 1924\",6378388,297], TOWGS84[-10,375,165,0,0,0,0]], PRIMEM[\"Greenwich\",0], UNIT[\"degree\",0.0174532925199433]]");
        DATUMS.put(
                62,
                "GEOGCS[\"NAD27\", DATUM[\"North_American_Datum_1927\", SPHEROID[\"Clarke 1866\",6378206.4,294.9786982139006]], PRIMEM[\"Greenwich\",0], UNIT[\"degree\",0.0174532925199433]]");
        DATUMS.put(
                74,
                "GEOGCS[\"NAD83\", DATUM[\"North_American_Datum_1983\", SPHEROID[\"GRS 1980\",6378137,298.257222101], TOWGS84[0,0,0,0,0,0,0]], PRIMEM[\"Greenwich\",0], UNIT[\"degree\",0.0174532925199433]]");
        DATUMS.put(
                77,
                "GEOGCS[\"Old Hawaiian\", DATUM[\"Old_Hawaiian\", SPHEROID[\"Clarke 1866\",6378206.4,294.9786982139006], TOWGS84[61,-285,-181,0,0,0,0]], PRIMEM[\"Greenwich\",0], UNIT[\"degree\",0.0174532925199433]]");
        DATUMS.put(
                79,
                "GEOGCS[\"OSGB 1936\", DATUM[\"OSGB_1936\", SPHEROID[\"Airy 1830\",6377563.396,299.3249646], TOWGS84[446.448,-125.157,542.06,0.15,0.247,0.842,-20.489]], PRIMEM[\"Greenwich\",0], UNIT[\"degree\",0.0174532925199433]]");
        DATUMS.put(
                81,
                "GEOGCS[\"Pitcairn 1967\", DATUM[\"Pitcairn_1967\", SPHEROID[\"International 1924\",6378388,297], TOWGS84[185,165,42,0,0,0,0]], PRIMEM[\"Greenwich\",0], UNIT[\"degree\",0.0174532925199433]]");
        DATUMS.put(
                82,
                "GEOGCS[\"PSAD56\", DATUM[\"Provisional_South_American_Datum_1956\", SPHEROID[\"International 1924\",6378388,297], TOWGS84[-288,175,-376,0,0,0,0]], PRIMEM[\"Greenwich\",0], UNIT[\"degree\",0.0174532925199433]]");
        DATUMS.put(
                83,
                "GEOGCS[\"Puerto Rico\", DATUM[\"Puerto_Rico\", SPHEROID[\"Clarke 1866\",6378206.4,294.9786982139006], TOWGS84[11,72,-101,0,0,0,0]], PRIMEM[\"Greenwich\",0], UNIT[\"degree\",0.0174532925199433]]");
        DATUMS.put(
                84,
                "GEOGCS[\"QND95\", DATUM[\"Qatar_National_Datum_1995\", SPHEROID[\"International 1924\",6378388,297], TOWGS84[-119.425,-303.659,-11.0006,1.1643,0.174458,1.09626,3.65706]], PRIMEM[\"Greenwich\",0], UNIT[\"degree\",0.0174532925199433]]");
        DATUMS.put(
                85,
                "GEOGCS[\"Qornoq\", DATUM[\"Qornoq\", SPHEROID[\"International 1924\",6378388,297], TOWGS84[164,138,-189,0,0,0,0]], PRIMEM[\"Greenwich\",0], UNIT[\"degree\",0.0174532925199433]]");
        DATUMS.put(
                86,
                "GEOGCS[\"RGR92\", DATUM[\"Reseau_Geodesique_de_la_Reunion_1992\", SPHEROID[\"GRS 1980\",6378137,298.257222101], TOWGS84[0,0,0,0,0,0,0]], PRIMEM[\"Greenwich\",0], UNIT[\"degree\",0.0174532925199433]]");
        DATUMS.put(
                87,
                "GEOGCS[\"Monte Mario\", DATUM[\"Monte_Mario\", SPHEROID[\"International 1924\",6378388,297], TOWGS84[-104.1,-49.1,-9.9,0.971,-2.917,0.714,-11.68]], PRIMEM[\"Greenwich\",0], UNIT[\"degree\",0.0174532925199433]]");
        DATUMS.put(
                90,
                "GEOGCS[\"Sapper Hill 1943\", DATUM[\"Sapper_Hill_1943\", SPHEROID[\"International 1924\",6378388,297], TOWGS84[-355,21,72,0,0,0,0]], PRIMEM[\"Greenwich\",0], UNIT[\"degree\",0.0174532925199433]]");
        DATUMS.put(
                91,
                "GEOGCS[\"Schwarzeck\", DATUM[\"Schwarzeck\", SPHEROID[\"Bessel Namibia (GLM)\",6377483.865280419,299.1528128], TOWGS84[616,97,-251,0,0,0,0]], PRIMEM[\"Greenwich\",0], UNIT[\"degree\",0.0174532925199433]]");
        DATUMS.put(
                92,
                "GEOGCS[\"SAD69\", DATUM[\"South_American_Datum_1969\", SPHEROID[\"GRS 1967\",6378160,298.247167427], TOWGS84[-57,1,-41,0,0,0,0]], PRIMEM[\"Greenwich\",0], UNIT[\"degree\",0.0174532925199433]]");
        DATUMS.put(
                92,
                "GEOGCS[\"SAD69\", DATUM[\"South_American_Datum_1969\", SPHEROID[\"GRS 1967 Modified\",6378160,298.25], TOWGS84[-57,1,-41,0,0,0,0]], PRIMEM[\"Greenwich\",0], UNIT[\"degree\",0.0174532925199433]]");
        DATUMS.put(
                96,
                "GEOGCS[\"Timbalai 1948\", DATUM[\"Timbalai_1948\", SPHEROID[\"Everest 1830 (1967 Definition)\",6377298.556,300.8017], TOWGS84[-679,669,-48,0,0,0,0]], PRIMEM[\"Greenwich\",0], UNIT[\"degree\",0.0174532925199433]]");
        DATUMS.put(
                97,
                "GEOGCS[\"Tokyo\", DATUM[\"Tokyo\", SPHEROID[\"Bessel 1841\",6377397.155,299.1528128], TOWGS84[-146.414,507.337,680.507,0,0,0,0]], PRIMEM[\"Greenwich\",0], UNIT[\"degree\",0.0174532925199433]]");
        DATUMS.put(
                99,
                "GEOGCS[\"Viti Levu 1916\", DATUM[\"Viti_Levu_1916\", SPHEROID[\"Clarke 1880 (RGS)\",6378249.145,293.465], TOWGS84[51,391,-36,0,0,0,0]], PRIMEM[\"Greenwich\",0], UNIT[\"degree\",0.0174532925199433]]");
        DATUMS.put(
                102,
                "GEOGCS[\"WGS 66\", DATUM[\"World_Geodetic_System_1966\", SPHEROID[\"NWL 9D\",6378145,298.25]], PRIMEM[\"Greenwich\",0], UNIT[\"degree\",0.0174532925199433]]");
        DATUMS.put(
                103,
                "GEOGCS[\"WGS 72\", DATUM[\"WGS_1972\", SPHEROID[\"WGS 72\",6378135,298.26], TOWGS84[0,0,4.5,0,0,0.554,0.2263]], PRIMEM[\"Greenwich\",0], UNIT[\"degree\",0.0174532925199433]]");
        DATUMS.put(
                104,
                "GEOGCS[\"WGS 84\", DATUM[\"WGS_1984\", SPHEROID[\"WGS 84\",6378137,298.257223563]], PRIMEM[\"Greenwich\",0], UNIT[\"degree\",0.0174532925199433]]");
        DATUMS.put(
                105,
                "GEOGCS[\"Yacare\", DATUM[\"Yacare\", SPHEROID[\"International 1924\",6378388,297], TOWGS84[-155,171,37,0,0,0,0]], PRIMEM[\"Greenwich\",0], UNIT[\"degree\",0.0174532925199433]]");
        DATUMS.put(
                106,
                "GEOGCS[\"Zanderij\", DATUM[\"Zanderij\", SPHEROID[\"International 1924\",6378388,297], TOWGS84[-265,120,-358,0,0,0,0]], PRIMEM[\"Greenwich\",0], UNIT[\"degree\",0.0174532925199433]]");
        DATUMS.put(
                108,
                "GEOGCS[\"ED87\", DATUM[\"European_Datum_1987\", SPHEROID[\"International 1924\",6378388,297], TOWGS84[-83.11,-97.38,-117.22,0.00569291,-0.0446976,0.0442851,0.1218]], PRIMEM[\"Greenwich\",0], UNIT[\"degree\",0.0174532925199433]]");
        DATUMS.put(
                112,
                "GEOGCS[\"RT90\", DATUM[\"Rikets_koordinatsystem_1990\", SPHEROID[\"Bessel 1841\",6377397.155,299.1528128], TOWGS84[414.1,41.3,603.1,-0.855,2.141,-7.023,0]], PRIMEM[\"Greenwich\",0], UNIT[\"degree\",0.0174532925199433]]");
        DATUMS.put(
                117,
                "GEOGCS[\"NZGD2000\", DATUM[\"New_Zealand_Geodetic_Datum_2000\", SPHEROID[\"GRS 1980\",6378137,298.257222101], TOWGS84[0,0,0,0,0,0,0]], PRIMEM[\"Greenwich\",0], UNIT[\"degree\",0.0174532925199433]]");
        DATUMS.put(
                118,
                "GEOGCS[\"American Samoa 1962\", DATUM[\"American_Samoa_1962\", SPHEROID[\"Clarke 1866\",6378206.4,294.9786982139006], TOWGS84[-115,118,426,0,0,0,0]], PRIMEM[\"Greenwich\",0], UNIT[\"degree\",0.0174532925199433]]");
        DATUMS.put(
                120,
                "GEOGCS[\"Ayabelle Lighthouse\", DATUM[\"Ayabelle_Lighthouse\", SPHEROID[\"Clarke 1880 (RGS)\",6378249.145,293.465], TOWGS84[-79,-129,145,0,0,0,0]], PRIMEM[\"Greenwich\",0], UNIT[\"degree\",0.0174532925199433]]");
        DATUMS.put(
                121,
                "GEOGCS[\"Bukit Rimpah\", DATUM[\"Bukit_Rimpah\", SPHEROID[\"Bessel 1841\",6377397.155,299.1528128], TOWGS84[-384,664,-48,0,0,0,0]], PRIMEM[\"Greenwich\",0], UNIT[\"degree\",0.0174532925199433]]");
        DATUMS.put(
                123,
                "GEOGCS[\"Dabola 1981\", DATUM[\"Dabola_1981\", SPHEROID[\"Clarke 1880 (IGN)\",6378249.2,293.4660212936265], TOWGS84[-83,37,124,0,0,0,0]], PRIMEM[\"Greenwich\",0], UNIT[\"degree\",0.0174532925199433]]");
        DATUMS.put(
                124,
                "GEOGCS[\"Deception Island\", DATUM[\"Deception_Island\", SPHEROID[\"Clarke 1880 (RGS)\",6378249.145,293.465], TOWGS84[260,12,-147,0,0,0,0]], PRIMEM[\"Greenwich\",0], UNIT[\"degree\",0.0174532925199433]]");
        DATUMS.put(
                127,
                "GEOGCS[\"Herat North\", DATUM[\"Herat_North\", SPHEROID[\"International 1924\",6378388,297], TOWGS84[-333,-222,114,0,0,0,0]], PRIMEM[\"Greenwich\",0], UNIT[\"degree\",0.0174532925199433]]");
        DATUMS.put(
                129,
                "GEOGCS[\"Indian 1975\", DATUM[\"Indian_1975\", SPHEROID[\"Everest 1830 (1937 Adjustment)\",6377276.345,300.8017], TOWGS84[210,814,289,0,0,0,0]], PRIMEM[\"Greenwich\",0], UNIT[\"degree\",0.0174532925199433]]");
        DATUMS.put(
                130,
                "GEOGCS[\"Indian 1954\", DATUM[\"Indian_1954\", SPHEROID[\"Everest 1830 (1937 Adjustment)\",6377276.345,300.8017], TOWGS84[217,823,299,0,0,0,0]], PRIMEM[\"Greenwich\",0], UNIT[\"degree\",0.0174532925199433]]");
        DATUMS.put(
                131,
                "GEOGCS[\"Indian 1960\", DATUM[\"Indian_1960\", SPHEROID[\"Everest 1830 (1937 Adjustment)\",6377276.345,300.8017], TOWGS84[198,881,317,0,0,0,0]], PRIMEM[\"Greenwich\",0], UNIT[\"degree\",0.0174532925199433]]");
        DATUMS.put(
                133,
                "GEOGCS[\"ID74\", DATUM[\"Indonesian_Datum_1974\", SPHEROID[\"Indonesian National Spheroid\",6378160,298.247], TOWGS84[-24,-15,5,0,0,0,0]], PRIMEM[\"Greenwich\",0], UNIT[\"degree\",0.0174532925199433]]");
        DATUMS.put(
                136,
                "GEOGCS[\"Leigon\", DATUM[\"Leigon\", SPHEROID[\"Clarke 1880 (RGS)\",6378249.145,293.465], TOWGS84[-130,29,364,0,0,0,0]], PRIMEM[\"Greenwich\",0], UNIT[\"degree\",0.0174532925199433]]");
        DATUMS.put(
                138,
                "GEOGCS[\"M'poraloko\", DATUM[\"M_poraloko\", SPHEROID[\"Clarke 1880 (IGN)\",6378249.2,293.4660212936265], TOWGS84[-74,-130,42,0,0,0,0]], PRIMEM[\"Greenwich\",0], UNIT[\"degree\",0.0174532925199433]]");
        DATUMS.put(
                141,
                "GEOGCS[\"Point 58\", DATUM[\"Point_58\", SPHEROID[\"Clarke 1880 (RGS)\",6378249.145,293.465], TOWGS84[-106,-129,165,0,0,0,0]], PRIMEM[\"Greenwich\",0], UNIT[\"degree\",0.0174532925199433]]");
        DATUMS.put(
                142,
                "GEOGCS[\"Pointe Noire\", DATUM[\"Congo_1960_Pointe_Noire\", SPHEROID[\"Clarke 1880 (IGN)\",6378249.2,293.4660212936265], TOWGS84[-148,51,-291,0,0,0,0]], PRIMEM[\"Greenwich\",0], UNIT[\"degree\",0.0174532925199433]]");
        DATUMS.put(
                143,
                "GEOGCS[\"Porto Santo\", DATUM[\"Porto_Santo_1936\", SPHEROID[\"International 1924\",6378388,297], TOWGS84[-499,-249,314,0,0,0,0]], PRIMEM[\"Greenwich\",0], UNIT[\"degree\",0.0174532925199433]]");
        DATUMS.put(
                144,
                "GEOGCS[\"Selvagem Grande\", DATUM[\"Selvagem_Grande\", SPHEROID[\"International 1924\",6378388,297], TOWGS84[-289,-124,60,0,0,0,0]], PRIMEM[\"Greenwich\",0], UNIT[\"degree\",0.0174532925199433]]");
        DATUMS.put(
                146,
                "GEOGCS[\"S-JTSK\", DATUM[\"System_Jednotne_Trigonometricke_Site_Katastralni\", SPHEROID[\"Bessel 1841\",6377397.155,299.1528128], TOWGS84[589,76,480,0,0,0,0]], PRIMEM[\"Greenwich\",0], UNIT[\"degree\",0.0174532925199433]]");
        DATUMS.put(
                147,
                "GEOGCS[\"Tananarive\", DATUM[\"Tananarive_1925\", SPHEROID[\"International 1924\",6378388,297], TOWGS84[-189,-242,-91,0,0,0,0]], PRIMEM[\"Greenwich\",0], UNIT[\"degree\",0.0174532925199433]]");
        DATUMS.put(
                150,
                "GEOGCS[\"Hartebeesthoek94\", DATUM[\"Hartebeesthoek94\", SPHEROID[\"WGS 84\",6378137,298.257223563], TOWGS84[0,0,0,0,0,0,0]], PRIMEM[\"Greenwich\",0], UNIT[\"degree\",0.0174532925199433]]");
        DATUMS.put(
                151,
                "GEOGCS[\"ATS77\", DATUM[\"Average_Terrestrial_System_1977\", SPHEROID[\"Average Terrestrial System 1977\",6378135,298.257]], PRIMEM[\"Greenwich\",0], UNIT[\"degree\",0.0174532925199433]]");
        DATUMS.put(
                152,
                "GEOGCS[\"JGD2000\", DATUM[\"Japanese_Geodetic_Datum_2000\", SPHEROID[\"GRS 1980\",6378137,298.257222101], TOWGS84[0,0,0,0,0,0,0]], PRIMEM[\"Greenwich\",0], UNIT[\"degree\",0.0174532925199433]]");
        DATUMS.put(
                1001,
                "GEOGCS[\"Pulkovo 1942\", DATUM[\"Pulkovo_1942\", SPHEROID[\"Krassowsky 1940\",6378245,298.3], TOWGS84[23.92,-141.27,-80.9,-0,0.35,0.82,-0.12]], PRIMEM[\"Greenwich\",0], UNIT[\"degree\",0.0174532925199433]]");
        DATUMS.put(
                1002,
                "GEOGCS[\"NTF\", DATUM[\"Nouvelle_Triangulation_Francaise\", SPHEROID[\"Clarke 1880 (IGN)\",6378249.2,293.4660212936265], TOWGS84[-168,-60,320,0,0,0,0]], PRIMEM[\"Greenwich\",0], UNIT[\"degree\",0.0174532925199433]]");
        DATUMS.put(
                1004,
                "GEOGCS[\"HD72\", DATUM[\"Hungarian_Datum_1972\", SPHEROID[\"GRS 1967\",6378160,298.247167427], TOWGS84[52.17,-71.82,-14.9,0,0,0,0]], PRIMEM[\"Greenwich\",0], UNIT[\"degree\",0.0174532925199433]]");
        DATUMS.put(
                1017,
                "GEOGCS[\"Xian 1980\", DATUM[\"Xian_1980\", SPHEROID[\"IAG 1975\",6378140,298.257]], PRIMEM[\"Greenwich\",0], UNIT[\"degree\",0.0174532925199433]]");
        DATUMS.put(
                1020,
                "GEOGCS[\"S-JTSK (Ferro)\", DATUM[\"System_Jednotne_Trigonometricke_Site_Katastralni_Ferro\", SPHEROID[\"Bessel 1841\",6377397.155,299.1528128], TOWGS84[589,76,480,0,0,0,0]], PRIMEM[\"Ferro\",-17.66666666666667], UNIT[\"degree\",0.0174532925199433]]");

        UNITS.put("m", "UNIT[\"Meter\",1]");
        UNITS.put("ft", "UNIT[\"foot\",0.3048]");
        UNITS.put("li", "UNIT[\"link\",0.201168]");
        UNITS.put("survey ft", "UNIT[\"US survey foot\",0.3048006096012192]");
    }

    /** The transform builder */
    private List<MappedPosition> controlPoints = new ArrayList<MappedPosition>();

    /** The Coordinate Reference System */
    private CoordinateReferenceSystem coordinateReferenceSystem;

    /**
     * Constructor for a {@link MapInfoFileReader}.
     *
     * @param tabfile holds the location where to read from.
     * @throws IOException in case something bad happens.
     */
    public MapInfoFileReader(final File tabfile) throws IOException {
        if (tabfile == null) {
            throw new IllegalArgumentException(
                    Errors.format(ErrorKeys.NULL_ARGUMENT_$1, "tabfile"));
        }
        if (!tabfile.isFile() || !tabfile.canRead()) {
            throw new IllegalArgumentException(
                    Errors.format(ErrorKeys.FILE_DOES_NOT_EXIST_$1, tabfile));
        }
        parseTabFile(new BufferedReader(new FileReader(tabfile)));
    }

    /**
     * Constructor for a {@link MapInfoFileReader}.
     *
     * @param tabfile {@link URL} where to read from.
     * @throws IOException in case something bad happens.
     */
    public MapInfoFileReader(final URL tabfile) throws IOException {
        if (tabfile == null) {
            throw new IllegalArgumentException(Errors.format(ErrorKeys.NULL_ARGUMENT_$1, "inFile"));
        }
        parseTabFile(new BufferedReader(new InputStreamReader(tabfile.openStream())));
    }

    /**
     * Parse Tab File
     *
     * @param bufferedreader the buffered reader
     */
    private void parseTabFile(final BufferedReader bufferedreader)
            throws IOException, DataSourceException {
        String str;
        Pattern patternPoint =
                Pattern.compile(" *\\(([0-9\\.]*),([0-9\\.]*)\\) *\\(([0-9\\.]*),([0-9\\.]*)\\).*");
        Pattern patternCoordSys =
                Pattern.compile("CoordSys *Earth *Projection *([0-9]*) *, *([0-9]*) *,?(.*)");
        Pattern patternUnit = Pattern.compile(" *\"([^\"]*)\" *,?(.*)");

        try {
            while ((str = bufferedreader.readLine()) != null) {
                Matcher matcherPoint = patternPoint.matcher(str);
                if (matcherPoint.find()) {
                    double d1 = Double.parseDouble(matcherPoint.group(1));
                    double d2 = Double.parseDouble(matcherPoint.group(2));
                    double d3 = Double.parseDouble(matcherPoint.group(3));
                    double d4 = Double.parseDouble(matcherPoint.group(4));

                    DirectPosition p1 = new DirectPosition2D(null, d1, d2);
                    DirectPosition p2 = new DirectPosition2D(null, d3, d4);

                    controlPoints.add(new MappedPosition(p2, p1));
                } else {
                    Matcher matcherCoordSys = patternCoordSys.matcher(str);
                    if (matcherCoordSys.find()) {
                        try {
                            int projectionType = Integer.parseInt(matcherCoordSys.group(1));
                            int datum = Integer.parseInt(matcherCoordSys.group(2));
                            String rest = matcherCoordSys.group(3);
                            String unit = "m";

                            Matcher matcherUnit = patternUnit.matcher(rest);
                            if (matcherUnit.find()) {
                                unit = matcherUnit.group(1);
                                rest = matcherUnit.group(2);
                            }

                            String s = "PROJCS[\"Unnamed\", " + DATUMS.get(datum) + ",";

                            String proj = PROJECTIONS.get(projectionType);
                            if (proj != null) {
                                s += " PROJECTION[\"" + proj + "\"], ";
                            }

                            String[] parts = rest.split(",");

                            final String[] PARAMETERS;
                            if (parts.length > 5) {
                                PARAMETERS = PARAMETERS1;
                            } else {
                                PARAMETERS = PARAMETERS2;
                            }

                            boolean flag = false;
                            for (int i = 0; i < parts.length && i < PARAMETERS.length; i++) {
                                String n = parts[i].trim();
                                int space = n.indexOf(" ");
                                if (space > 0) {
                                    n = n.substring(0, space);
                                }
                                double d = Double.parseDouble(n);

                                if (i == 3 && flag) {
                                    s += " PARAMETER[\"scale_factor\"," + d + "],";
                                } else if (i == 2 && d == 0) {
                                    flag = true;
                                } else {
                                    s += " PARAMETER[\"" + PARAMETERS[i] + "\"," + d + "],";
                                }
                            }

                            s += " " + UNITS.get(unit);

                            s += "]";

                            if (LOGGER.isLoggable(Level.FINE)) {
                                LOGGER.log(Level.FINE, "Mapinfo converted CRS: " + s);
                            }

                            coordinateReferenceSystem = CRS.parseWKT(s);
                        } catch (Exception e) {
                            LOGGER.log(
                                    Level.WARNING,
                                    "Failed to parse and encode mapinfo crs: "
                                            + e.getLocalizedMessage(),
                                    e);
                            // ignore coordinate reference system
                        }
                    }
                }
            }
        } catch (Exception e) {
            throw new DataSourceException(e);
        } finally {
            try {
                bufferedreader.close();
            } catch (Throwable t) {
                if (LOGGER.isLoggable(Level.FINE)) {
                    LOGGER.log(Level.FINE, t.getLocalizedMessage(), t);
                }
            }
        }
        // did we find all we were looking for?
        if (controlPoints.size() < 3) {
            throw new DataSourceException(
                    "Didn't find a minimum of three control points in the .tab file.");
        }
    }

    /**
     * Get Control Points
     *
     * @return Control Points
     */
    public synchronized List<MappedPosition> getControlPoints() {
        return controlPoints;
    }

    /**
     * Get Math Transform based on control points
     *
     * @return MathTransform
     */
    public synchronized MathTransform getTransform() {
        AffineTransformBuilder builder = new AffineTransformBuilder(controlPoints);
        try {
            return builder.getMathTransform();
        } catch (FactoryException e) {
            LOGGER.log(Level.WARNING, e.getLocalizedMessage(), e);
            return null;
        }
    }

    /**
     * Get CoordinateReferenceSysten
     *
     * @return Coordinate Reference System
     */
    public synchronized CoordinateReferenceSystem getCRS() {
        return coordinateReferenceSystem;
    }
}
