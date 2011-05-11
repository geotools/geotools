/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2006-2011, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.gce.grassraster;

import java.util.HashMap;
import java.util.Map;

/**
 * <p>
 * Constants used by the JGrass engine
 * </p>
 * 
 * @author Andrea Antonello - www.hydrologis.com
 * @since 1.1.0
 */
@SuppressWarnings("nls")
public class JGrassConstants {

    /*
     * jgrass database files and folders
     */
    /** folder of the JGrass database structure */
    public static final String PERMANENT_MAPSET = "PERMANENT";

    /** folder of the JGrass database structure */
    public static final String DEFAULT_WIND = "DEFAULT_WIND";

    /** folder of the JGrass database structure */
    public static final String PROJ_INFO = "PROJ_INFO";

    /** folder of the JGrass database structure */
    public static final String PROJ_WKT = "PROJ_INFO.WKT";

    /** folder of the JGrass database structure */
    public static final String PROJ_UNITS = "PROJ_UNITS";

    /** folder of the JGrass database structure */
    public static final String WIND = "WIND";

    /** folder of the JGrass database structure */
    public static final String MYNAME = "MYNAME";

    /** folder of the JGrass database structure */
    public static final String FCELL = "fcell";

    /** folder of the JGrass database structure */
    public static final String CELL = "cell";

    /** folder of the JGrass database structure */
    public static final String CATS = "cats";

    /** folder of the JGrass database structure */
    public static final String HIST = "hist";

    /** folder of the JGrass database structure */
    public static final String CELLHD = "cellhd";

    /** folder of the JGrass database structure */
    public static final String COLR = "colr";

    /** folder of the JGrass database structure */
    public static final String CELL_MISC = "cell_misc";

    /** folder of the JGrass database structure */
    public static final String CELLMISC_FORMAT = "f_format";

    /** folder of the JGrass database structure */
    public static final String CELLMISC_QUANT = "f_quant";

    /** folder of the JGrass database structure */
    public static final String CELLMISC_RANGE = "f_range";

    /** folder of the JGrass database structure */
    public static final String CELLMISC_NULL = "null";

    /** folder of the JGrass database structure */
    public static final String DIG = "dig";

    /** folder of the JGrass database structure */
    public static final String DIG_ATTS = "dig_atts";

    /** folder of the JGrass database structure */
    public static final String DIG_CATS = "dig_cats";

    /** folder of the JGrass database structure */
    public static final String SITE_LISTS = "site_lists";

    /** folder of the JGrass database structure */
    public static final String VECTORS = "vector";

    /** grass sites map type */
    public static final String SITESMAP = "sites";

    /** grass ascii raster format */
    public static final String GRASSASCIIRASTER = "grassascii";

    /** fluidturtle ascii raster format */
    public static final String FLUIDTURTLEASCIIRASTER = "fluidturtleascii";

    /** esri ascii raster format */
    public static final String ESRIASCIIRASTER = "esriasciigrid";

    /** tmp data for hortons */
    public static String HORTON_MACHINE_PATH = "hortonmachine";

    /*
     * map formats
     */
    /** raster map types */
    public static final String GRASSBINARYRASTERMAP = "grassbinaryraster";
    public static final String GRASSASCIIRASTERMAP = "grassasciiraster";
    public static final String FTRASTERMAP = "fluidturtleasciiraster";
    public static final String ESRIRASTERMAP = "esriasciigrid";

    /** grass 6 vector map types */
    public static final String GRASS6VECTORMAP = "grass6vector";
    public static final String OLDGRASSVECTORMAP = "oldgrassvector";

    /** grass application paths */
    public static final String GRASSBIN = "bin";
    public static final String GRASSLIB = "lib";

    /*
     * region definition headers
     */
    public static final String HEADER_EW_RES = "e-w res";
    public static final String HEADER_NS_RES = "n-s res";
    public static final String HEADER_NORTH = "north";
    public static final String HEADER_SOUTH = "south";
    public static final String HEADER_EAST = "east";
    public static final String HEADER_WEST = "west";
    public static final String HEADER_ROWS = "rows";
    public static final String HEADER_COLS = "cols";

    /*
     * esri header pieces
     */
    public static final String ESRI_HEADER_XLL_PIECE = "xll";
    public static final String ESRI_HEADER_XLL = "xllcorner";
    public static final String ESRI_HEADER_YLL_PIECE = "yll";
    public static final String ESRI_HEADER_YLL = "yllcorner";
    public static final String ESRI_HEADER_NROWS_PIECE = "nr";
    public static final String ESRI_HEADER_NROWS = "nrows";
    public static final String ESRI_HEADER_NCOLS_PIECE = "nc";
    public static final String ESRI_HEADER_NCOLS = "ncols";
    public static final String ESRI_HEADER_DIMENSION = "dim";
    public static final String ESRI_HEADER_CELLSIZE = "cellsize";
    public static final String ESRI_HEADER_NOVALUE_PIECE = "nov";
    public static final String ESRI_HEADER_NOVALUE = "nodata_value";

    /**
     * Array of supported types for csv imports.
     */
    public static final String[] CSVTYPESARRAY = new String[]{"X", "Y", "Z", "String", "Double",
            "Integer"};
    /**
     * {@link Map} of classes for csv types.
     */
    public static final Map<String, Class<?>> CSVTYPESCLASSESMAP = new HashMap<String, Class<?>>();
    static {
        CSVTYPESCLASSESMAP.put(CSVTYPESARRAY[3], String.class);
        CSVTYPESCLASSESMAP.put(CSVTYPESARRAY[4], Double.class);
        CSVTYPESCLASSESMAP.put(CSVTYPESARRAY[5], Integer.class);
    }

    private JGrassConstants() {
    }
}
