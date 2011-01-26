/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2006-2010, Open Source Geospatial Foundation (OSGeo)
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

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;

import org.geotools.gce.grassraster.core.color.AttributeTable;
import org.geotools.gce.grassraster.core.color.JGrassColorTable;
import org.geotools.gce.grassraster.core.color.JlsTokenizer;
import org.geotools.gce.grassraster.core.color.AttributeTable.CellAttribute;
import org.geotools.gce.grassraster.metadata.GrassBinaryImageMetadata;
import org.geotools.gce.grassraster.spi.GrassBinaryImageReaderSpi;
import org.geotools.referencing.CRS;
import org.opengis.referencing.FactoryException;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.opengis.referencing.datum.PixelInCell;

/**
 * Represents the structure around a map inside a grass database.
 * <p>
 * Given a map file path, all the related files to that map in the
 * GRASS Location/Mapset are defined.
 * </p>
 * 
 * @author Andrea Antonello (www.hydrologis.com)
 * @since 3.0
 * @see {@link JGrassConstants}
 * @see {@link JGrassRegion}
 */
public class JGrassMapEnvironment {

    /**
     * A minimum value to be used when no range is available for a map.
     */
    public static double defaultMapMin = 0.0;

    /**
     * A maximum value to be used when no range is available for a map.
     */
    public static double defaultMapMax = 5000.0;

    /**
     * The default, always existing Mapset inside a Location.
     */
    private File PERMANENT_MAPSET = null;

    /**
     * The Mapset file of the map, through which the 
     * {@linkplain JGrassMapEnvironment} was built.
     */
    private File MAPSET = null;
    /**
     * The Location file of the map, through which the 
     * {@linkplain JGrassMapEnvironment} was built.
     */
    private File LOCATION = null;
    /**
     * The DEFAULT_WIND file, that keeps the default region informations.
     */
    private File DEFAULT_WIND = null;

    /**
     * The PROJ_INFO file, that keeps the projection informations as defined 
     * by GRASS through the g.proj command.
     */
    private File PROJ_INFO = null;

    /**
     * The PROJ_WKT file, that keeps the projection informations as defined by
     * JGRASS to be compatible with geotools.
     */
    private File PROJ_WKT = null;

    /**
     * The PROJ_WKT file, that keeps the projection units informations as 
     * defined by GRASS.
     */
    private File PROJ_UNITS = null;

    /**
     * The WIND file, that keeps the current active region information, 
     * i.e. the region on which calculations are executed.
     */
    private File WIND = null;

    /**
     * The FCELL file, i.e. the file that holds the raw data for floating 
     * value maps.
     */
    private File FCELL = null;

    /**
     * The CELL file, i.e. the file that holds the raw data for integer 
     * value maps. This file also always exist since it is placeholder also 
     * for floating point maps.
     */
    private File CELL = null;

    /**
     * The CATS file, holding the categories for the map.
     */
    private File CATS = null;

    /**
     * The HIST file, holding the history of the map.
     */
    private File HIST = null;

    /**
     * The CELLHD file, holding the native file region information of the file.
     */
    private File CELLHD = null;

    /**
     * The COLR file, holding the colortable for the map.
     */
    private File COLR = null;

    /**
     * The CELLMISC_FORMAT file, holding the format and compression information 
     * for the map.
     */
    private File CELLMISC_FORMAT = null;

    /**
     * The CELLMISC_QUANT file, holding quantization rules for passing from 
     * floating points to integers.
     */
    private File CELLMISC_QUANT = null;

    /**
     * The CELLMISC_RANGE file, holding the range values for the map.
     */
    private File CELLMISC_RANGE = null;
    /**
     * The CELLMISC_NULL file, holding bitmap for novalues for the map.
     */
    private File CELLMISC_NULL = null;

    /**
     * The {@linkplain JGrassMapEnvironment} that is created in the case a raster
     * map was reclassed. 
     */
    private JGrassMapEnvironment RECLASSEDENVIRONMENT = null;

    /**
     * The name of the map to which the environment refers to.
     */
    private String mapName;

    private GrassBinaryImageReader coverageReader;

    private HashMap<String, String> coverageMetadataMap;

    /**
     * Constructs an instance of {@linkplain JGrassMapEnvironment}.
     * 
     * @param cellFile the absolute path to a grass raster map, through which 
     * the environment is defined.
     */
    public JGrassMapEnvironment( File cellFile ) {
        CELL = cellFile;
        mapName = CELL.getName();
        MAPSET = CELL.getParentFile().getParentFile();
        LOCATION = MAPSET.getParentFile();
        String permanentFolderPath = LOCATION.getAbsolutePath() + File.separator
                + JGrassConstants.PERMANENT_MAPSET;
        PERMANENT_MAPSET = new File(permanentFolderPath);
        DEFAULT_WIND = new File(permanentFolderPath + File.separator + JGrassConstants.DEFAULT_WIND);
        PROJ_INFO = new File(permanentFolderPath + File.separator + JGrassConstants.PROJ_INFO);
        PROJ_WKT = new File(permanentFolderPath + File.separator + JGrassConstants.PROJ_WKT);
        PROJ_UNITS = new File(permanentFolderPath + File.separator + JGrassConstants.PROJ_UNITS);

        String mapsetPath = MAPSET.getAbsolutePath();
        WIND = new File(mapsetPath + File.separator + JGrassConstants.WIND);
        FCELL = new File(mapsetPath + File.separator + JGrassConstants.FCELL + File.separator
                + mapName);
        CELLHD = new File(mapsetPath + File.separator + JGrassConstants.CELLHD + File.separator
                + mapName);
        CATS = new File(mapsetPath + File.separator + JGrassConstants.CATS + File.separator
                + mapName);
        COLR = new File(mapsetPath + File.separator + JGrassConstants.COLR + File.separator
                + mapName);
        HIST = new File(mapsetPath + File.separator + JGrassConstants.HIST + File.separator
                + mapName);

        String cellMiscPath = mapsetPath + File.separator + JGrassConstants.CELL_MISC
                + File.separator + mapName + File.separator;
        CELLMISC_NULL = new File(cellMiscPath + JGrassConstants.CELLMISC_NULL);
        CELLMISC_FORMAT = new File(cellMiscPath + JGrassConstants.CELLMISC_FORMAT);
        CELLMISC_QUANT = new File(cellMiscPath + JGrassConstants.CELLMISC_QUANT);
        CELLMISC_RANGE = new File(cellMiscPath + JGrassConstants.CELLMISC_RANGE);

    }

    /**
     * Constructs an instance of {@linkplain JGrassMapEnvironment}.
     * <p>
     * This constructor is a facility method in the case the mapset file and the 
     * name of the GRASS raster map are passed.
     * </p>
     * 
     * @param mapsetFile file of the mapset path
     * @param mapName name of the GRASS raster map
     */
    public JGrassMapEnvironment( File mapsetFile, String mapName ) {
        this(new File(mapsetFile.getAbsolutePath() + File.separator + JGrassConstants.CELL
                + File.separator + mapName));
    }

    /**
     * Reclasses the environment.
     * 
     * <p>
     * In the case a reclassed map was found, this method has to be called, in 
     * order to set the current environment to the new reclassed one.
     * </p>
     * 
     * @param reclassedMapset the name of the mapset holding the reclassed map
     * @param reclassedMap the name of the reclassed map
     */
    public void setReclassed( String reclassedMapset, String reclassedMap ) {
        String reclassedCell = LOCATION.getAbsolutePath() + File.separator + reclassedMapset
                + File.separator + JGrassConstants.CELL + File.separator + reclassedMap;
        CELL = new File(reclassedCell);
        mapName = CELL.getName();
        MAPSET = CELL.getParentFile().getParentFile();
        LOCATION = MAPSET.getParentFile();
        String permanentFolderPath = LOCATION.getAbsolutePath() + File.separator
                + JGrassConstants.PERMANENT_MAPSET;
        PERMANENT_MAPSET = new File(permanentFolderPath);
        DEFAULT_WIND = new File(permanentFolderPath + File.separator + JGrassConstants.DEFAULT_WIND);
        PROJ_INFO = new File(permanentFolderPath + File.separator + JGrassConstants.PROJ_INFO);
        PROJ_WKT = new File(permanentFolderPath + File.separator + JGrassConstants.PROJ_WKT);
        PROJ_UNITS = new File(permanentFolderPath + File.separator + JGrassConstants.PROJ_UNITS);

        String mapsetPath = MAPSET.getAbsolutePath();
        WIND = new File(mapsetPath + File.separator + JGrassConstants.WIND);
        FCELL = new File(mapsetPath + File.separator + JGrassConstants.FCELL + File.separator
                + mapName);
        CELLHD = new File(mapsetPath + File.separator + JGrassConstants.CELLHD + File.separator
                + mapName);
        CATS = new File(mapsetPath + File.separator + JGrassConstants.CATS + File.separator
                + mapName);
        COLR = new File(mapsetPath + File.separator + JGrassConstants.COLR + File.separator
                + mapName);
        HIST = new File(mapsetPath + File.separator + JGrassConstants.HIST + File.separator
                + mapName);

        String cellMiscPath = mapsetPath + File.separator + JGrassConstants.CELL_MISC
                + File.separator + mapName + File.separator;
        CELLMISC_NULL = new File(cellMiscPath + JGrassConstants.CELLMISC_NULL);
        CELLMISC_FORMAT = new File(cellMiscPath + JGrassConstants.CELLMISC_FORMAT);
        CELLMISC_QUANT = new File(cellMiscPath + JGrassConstants.CELLMISC_QUANT);
        CELLMISC_RANGE = new File(cellMiscPath + JGrassConstants.CELLMISC_RANGE);

        RECLASSEDENVIRONMENT = this;
    }

    /**
     * Getter for mapName.
     * 
     * @return the map name.
     */
    public String getMapName() {
        return mapName;
    }

    /**
     * Getter for PERMANENT MAPSET folder.
     * 
     * @return the PERMANENT MAPSET folder file.
     */
    public File getPERMANENT_MAPSET() {
        return PERMANENT_MAPSET;
    }

    /**
     * Getter for MAPSET folder.
     * 
     * @return the MAPSET folder file.
     */
    public File getMAPSET() {
        return MAPSET;
    }

    /**
     * Getter for LOCATION folder.
     * 
     * @return the LOCATION file folder.
     */
    public File getLOCATION() {
        return LOCATION;
    }

    /**
     * Getter for DEFAULT_WIND file.
     * 
     * @return the DEFAULT_WIND file.
     */
    public File getDEFAULT_WIND() {
        return DEFAULT_WIND;
    }

    /**
     * Getter for PROJ_INFO file.
     * 
     * @return the PROJ_INFO file.
     */
    public File getPROJ_INFO() {
        return PROJ_INFO;
    }

    /**
     * Getter for PROJ_WKT file.
     * 
     * @return the PROJ_WKT file.
     */
    public File getPROJ_WKT() {
        return PROJ_WKT;
    }

    /**
     * Getter for PROJ_UNITS file.
     * 
     * @return the PROJ_UNITS file.
     */
    public File getPROJ_UNITS() {
        return PROJ_UNITS;
    }

    /**
     * Getter for WIND file.
     * 
     * @return the WIND file.
     */
    public File getWIND() {
        return WIND;
    }

    /**
     * Getter for FCELL file.
     * 
     * @return the FCELL file.
     */
    public File getFCELL() {
        return FCELL;
    }

    /**
     * Getter for FCELL folder.
     * 
     * @return the FCELL folder file.
     */
    public File getFcellFolder() {
        return FCELL.getParentFile();
    }

    /**
     * Getter for CELL file, i.e. the main map file.
     * 
     * @return the CELL file.
     */
    public File getCELL() {
        return CELL;
    }

    /**
     * Getter for the main map file.
     * 
     * @return the main map file.
     */
    public File getMapFile() {
        return CELL;
    }

    /**
     * Getter for CELL folder.
     * 
     * @return the CELL folder file.
     */
    public File getCellFolder() {
        return CELL.getParentFile();
    }

    /**
     * Getter for CATS file.
     * 
     * @return the CATS file.
     */
    public File getCATS() {
        return CATS;
    }

    /**
     * Getter for CATS folder.
     * 
     * @return the CATS folder file.
     */
    public File getCatsFolder() {
        return CATS.getParentFile();
    }

    /**
     * Getter for the history file.
     * 
     * @return the HIST file.
     */
    public File getHIST() {
        return HIST;
    }

    /**
     * Getter for CELLHD file.
     * 
     * @return the CELLHD file.
     */
    public File getCELLHD() {
        return CELLHD;
    }

    /**
     * Getter for COLR file.
     * 
     * @return the COLR file.
     */
    public File getCOLR() {
        return COLR;
    }

    /**
     * Getter for COLR folder.
     * 
     * @return the COLR folder file.
     */
    public File getColrFolder() {
        return COLR.getParentFile();
    }

    /**
     * Getter for CELLMISC_FORMAT file.
     * 
     * @return the CELLMISC_FORMAT file.
     */
    public File getCELLMISC_FORMAT() {
        return CELLMISC_FORMAT;
    }

    /**
     * Getter for CELLMISC_QUANT file.
     * 
     * @return the CELLMISC_QUANT file.
     */
    public File getCELLMISC_QUANT() {
        return CELLMISC_QUANT;
    }

    /**
     * Getter for CELLMISC_RANGE file.
     * 
     * @return the CELLMISC_RANGE file.
     */
    public File getCELLMISC_RANGE() {
        return CELLMISC_RANGE;
    }

    /**
     * Getter for CELLMISC_NULL file.
     * 
     * @return the CELLMISC_NULL file.
     */
    public File getCELLMISC_NULL() {
        return CELLMISC_NULL;
    }

    /**
     * Getter for RECLASSEDENVIRONMENT.
     * 
     * @return the RECLASSEDENVIRONMENT.
     */
    public JGrassMapEnvironment getRECLASSEDENVIRONMENT() {
        return RECLASSEDENVIRONMENT;
    }

    /**
     * Read the colorrules for the map wrapped by this {@link JGrassMapEnvironment}.
     * 
     * @param range the range to use for the default colortable, in the case of 
     *              missing color file. Can be null.
     * @return a {@link List} of color rules in string format.
     * @throws IOException
     */
    public List<String> getColorRules( double[] range ) throws IOException {
        if (range == null) {
            range = new double[]{defaultMapMin, defaultMapMax};
        }
        JGrassColorTable colorTable = new JGrassColorTable(this, range);
        return colorTable.getColorRules();
    }

    /**
     * Reads the categories for the map wrapped by this {@link JGrassMapEnvironment}.
     * 
     * <p>
     * The categories are returned in a {@link List} of strings that may 
     * be of two types:<br>
     * <ul>
     *  <li>value:categorytext</li>
     *  <li>value1-value2:categorytext</li>
     * </ul>
     * </p>
     * 
     * @return the list of categories in text format.
     * @throws IOException 
     */
    public List<String> getCategories() throws IOException {
        List<String> categoriesList = new ArrayList<String>();
        /*
         * File is a standard file where the categories values are stored in
         * the cats directory.
         */
        BufferedReader rdr = new BufferedReader(new FileReader(getCATS()));
        try {
            /* Instantiate attribute table */
            AttributeTable attTable = new AttributeTable();
            /* Ignore first 4 lines. */
            rdr.readLine();
            rdr.readLine();
            rdr.readLine();
            rdr.readLine();
            /* Read next n lines */
            String line;
            while( (line = rdr.readLine()) != null ) {
                /* All lines other than '0:no data' are processed */
                //            if (line.indexOf("0:no data") == -1) { //$NON-NLS-1$
                JlsTokenizer tk = new JlsTokenizer(line, ":"); //$NON-NLS-1$
                if (tk.countTokens() == 2) {
                    float f = Float.parseFloat(tk.nextToken());
                    String att = tk.nextToken().trim();
                    attTable.addAttribute(f, att);
                } else if (tk.countTokens() == 3) {
                    float f0 = Float.parseFloat(tk.nextToken());
                    float f1 = Float.parseFloat(tk.nextToken());
                    String att = tk.nextToken().trim();
                    attTable.addAttribute(f0, f1, att);
                }
                // }
            }

            Enumeration<CellAttribute> categories = attTable.getCategories();
            while( categories.hasMoreElements() ) {
                AttributeTable.CellAttribute object = categories.nextElement();
                categoriesList.add(object.toString());
            }
        } finally {
            rdr.close();
        }

        return categoriesList;

    }

    /**
     * Read the {@link JGrassRegion} from the active region file.
     * 
     * @return the active grass region.
     * @throws IOException
     */
    public JGrassRegion getActiveRegion() throws IOException {
        JGrassRegion jGrassRegion = new JGrassRegion(getWIND().getAbsolutePath());
        return jGrassRegion;
    }

    /**
     * Reads the data range from a color table file, if existing.
     * 
     * @return the data range or null if no range could be read.
     * @throws IOException
     */
    public double[] getRangeFromColorTable() throws IOException {
        double[] dataRange = new double[2];
        JGrassColorTable colorTable = new JGrassColorTable(this, null);
        List<String> rules = colorTable.getColorRules();
        if (rules.size() == 0) {
            return null;
        }
        for( int i = 0; i < rules.size(); i++ ) {
            String rule = rules.get(i);
            double[] values = new double[2];
            JGrassColorTable.parseColorRule(rule, values, null);

            if (i == 0) {
                dataRange[0] = values[0];
            }
            if (i == rules.size() - 1) {
                dataRange[1] = values[1];
            }
        }
        if (dataRange == null || Double.isNaN(dataRange[0]) || Double.isInfinite(dataRange[0])
                || Double.isNaN(dataRange[1]) || Double.isInfinite(dataRange[1])) {
            return null;
        }
        return dataRange;
    }

    /**
     * Reads the data range from the GRASS range file.
     * 
     * @return the data range or null if the content is infinite or NaN.
     * @throws IOException
     */
    public double[] getRangeFromRangeFile() throws IOException {
        double[] dataRange = null;
        /*
         * first check if there is a range file available
         */
        File rangeFile = getCELLMISC_RANGE();
        // if the file exists, read the range.
        if (rangeFile.exists()) {
            dataRange = new double[2];
            InputStream is = new FileInputStream(rangeFile);
            byte[] numbers = new byte[16];
            int testread = is.read(numbers);
            is.close();
            if (testread == 16) {
                ByteBuffer rangeBuffer = ByteBuffer.wrap(numbers);
                dataRange[0] = rangeBuffer.getDouble();
                dataRange[1] = rangeBuffer.getDouble();
            }
        }
        if (dataRange == null || Double.isNaN(dataRange[0]) || Double.isInfinite(dataRange[0])
                || Double.isNaN(dataRange[1]) || Double.isInfinite(dataRange[1])) {
            return null;
        }
        return dataRange;
    }
    /**
     * Reads the data range by reading the map.
     * 
     * @return the data range.
     * @throws IOException
     */
    public double[] getRangeFromMapScan() throws IOException {
        /*
         * if the range file doesn't exist, the only way is to scan the whole map.
         */
        GrassCoverageReader coverageReader = new GrassCoverageReader(getCELL());
        coverageReader.setParams(PixelInCell.CELL_CENTER, null,
                false, false, null);
        coverageReader.read(null);
        double[] dataRange = coverageReader.getRange();

        // write the range to disk
        OutputStream cell_miscRangeStream = new FileOutputStream(getCELLMISC_RANGE());
        cell_miscRangeStream.write(double2bytearray(dataRange[0]));
        cell_miscRangeStream.write(double2bytearray(dataRange[1]));
        cell_miscRangeStream.close();
        return dataRange;
    }

    /**
     * Getter for the legend string.
     * 
     * @return the legendstring.
     */
    public String getLegendString() throws IOException {
        checkReader();
        String legendString = coverageMetadataMap
                .get(GrassBinaryImageMetadata.COLOR_RULES_DESCRIPTOR);
        return legendString;
    }

    /**
     * Getter for the categories string.
     * 
     * @return the categories string.
     */
    public String getCategoriesString() throws IOException {
        checkReader();
        String categoriesString = coverageMetadataMap
                .get(GrassBinaryImageMetadata.CATEGORIES_DESCRIPTOR);
        return categoriesString;
    }

    /**
     * Read the {@link CoordinateReferenceSystem crs} from the location.
     * 
     * @return the crs of the location containing the map.
     * @throws Exception
     */
    public CoordinateReferenceSystem getCoordinateReferenceSystem() throws Exception {
        File projWtkFile = getPROJ_WKT();
        if (projWtkFile.exists()) {
            BufferedReader crsReader = new BufferedReader(new FileReader(projWtkFile));
            StringBuffer wtkString = new StringBuffer();
            try {
                String line = null;
                while( (line = crsReader.readLine()) != null ) {
                    wtkString.append(line.trim());
                }

            } finally {
                crsReader.close();
            }
            CoordinateReferenceSystem readCrs = null;
            try {
                readCrs = CRS.parseWKT(wtkString.toString());
            } catch (FactoryException e) {
                throw new IOException(e.getLocalizedMessage());
            }
            return readCrs;
        } else {
            return null;
        }

    }

    /**
     * Read the file region of the map.
     * 
     * @return the {@link JGrassRegion} of the file.
     * @throws IOException 
     */
    public JGrassRegion getFileRegion() throws IOException {
        // checkReader();
        // double fileNorth = Double.parseDouble(coverageMetadataMap
        // .get(GrassBinaryImageMetadata.NORTH));
        // double fileSouth = Double.parseDouble(coverageMetadataMap
        // .get(GrassBinaryImageMetadata.SOUTH));
        // double fileEast = Double
        // .parseDouble(coverageMetadataMap.get(GrassBinaryImageMetadata.EAST));
        // double fileWest = Double
        // .parseDouble(coverageMetadataMap.get(GrassBinaryImageMetadata.WEST));
        // int fileRows = Integer.parseInt(coverageMetadataMap.get(GrassBinaryImageMetadata.NROWS));
        // int fileCols = Integer.parseInt(coverageMetadataMap.get(GrassBinaryImageMetadata.NCOLS));
        // JGrassRegion fileRegion = new JGrassRegion(fileWest, fileEast, fileSouth, fileNorth,
        // fileRows, fileCols);

        File cellhdFile = getCELLHD();
        JGrassRegion fileRegion = new JGrassRegion(cellhdFile.getAbsolutePath());
        return fileRegion;
    }

    private void checkReader() throws IOException {
        if (coverageReader == null) {
            coverageReader = new GrassBinaryImageReader(new GrassBinaryImageReaderSpi());
            coverageReader.setInput(getCELL());
            coverageMetadataMap = ((GrassBinaryImageMetadata) coverageReader.getImageMetadata(0))
                    .toHashMap();
        }
    }
    
    private static byte[] double2bytearray( double doubleValue ) {
        long l = Double.doubleToLongBits(doubleValue);
        byte[] b = new byte[8];
        int shift = 64 - 8;
        for( int k = 0; k < 8; k++, shift -= 8 ) {
            b[k] = (byte) (l >>> shift);
        }
        return b;
    }

}
