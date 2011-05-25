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

import java.awt.geom.Rectangle2D;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.Map.Entry;

import org.geotools.geometry.Envelope2D;
import org.geotools.geometry.jts.JTS;
import org.geotools.referencing.CRS;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.opengis.referencing.operation.MathTransform;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Envelope;

/**
 * Represents the geographic region used in the jGrass engines.
 * 
 * <p>
 * JGrass calculations always work against a particular geographic region, which
 * contains the boundaries of the region as well as the information of the
 * region's resolution and the number of rows and cols of the region.
 * </p>
 * <p>
 * <b>Warning</b>: since the rows and cols have to be integers, the resolution
 * is may be recalculated to fulfill this constraint. Users should not wonder if
 * the asked resolution is not available in the supplied boundaries.
 * </p>
 * 
 * @author Andrea Antonello - www.hydrologis.com
 * @since 3.0
 * 
 * @see {@link JGrassMapEnvironment}
 *
 * @source $URL: http://svn.osgeo.org/geotools/branches/2.7.x/build/maven/javadoc/../../../modules/unsupported/grassraster/src/main/java/org/geotools/gce/grassraster/JGrassRegion.java $
 */
public class JGrassRegion {

    /**
     * The identifier string for the {@link JGrassRegion}.
     * <p>
     * Useful for environments that work with blackboards. For example the Udig
     * layers support blackboards.
     * </p>
     */
    public static final String BLACKBOARD_KEY = "eu.hydrologis.jgrass.libs.region"; //$NON-NLS-1$

    /**
     * The representation of a dummy region.
     */
    public final static String BLANK_REGION = "proj:           0\nzone:           0\nnorth:          1\nsouth:          0\nwest:           0\neast:           1\nrows:           1\ncols:           1\ne-w resol:      1\nn-s resol:      1"; //$NON-NLS-1$

    /**
     * The old projection identifier value in the GRASS wind files (used before
     * proj4).
     * <p>
     * See package description for more info.
     * </p>
     */
    private int proj = 0;

    /**
     * The old zone identifier value in the GRASS wind files (used before
     * proj4).
     * <p>
     * See package description for more info.
     * </p>
     */
    private int zone = 0;

    /**
     * The northern boundary of the region.
     */
    private double north = Double.NaN;

    /**
     * The southern boundary of the region.
     */
    private double south = Double.NaN;

    /**
     * The western boundary of the region.
     */
    private double west = Double.NaN;

    /**
     * The eastern boundary of the region.
     */
    private double east = Double.NaN;

    /**
     * The north-south resolution of the region.
     */
    private double ns_res = Double.NaN;

    /**
     * The east-west resolution of the region.
     */
    private double we_res = Double.NaN;

    /**
     * The number of rows of the region.
     */
    private int rows = 0;

    /**
     * The number of columns of the region.
     */
    private int cols = 0;

    /**
     * The hashmap of additional GRASS entries in the GRASS region files.
     * 
     * <p>
     * These are currently not used in JGrass. These are important to keep, in
     * order to write them back to the WIND file (see package description), when
     * necessary.
     * </p>
     */
    private LinkedHashMap<String, String> additionalGrassEntries = null;

    /**
     * Creates a new instance of {@link JGrassRegion}.
     * 
     * <p>
     * The supplied path has to be of the format of a GRASS WIND file.
     * </p>
     * 
     * @param regionFilePath
     *            a GRASS region file path.
     * @throws IOException
     */
    public JGrassRegion( String regionFilePath ) throws IOException {
        readRegionFromFile(regionFilePath, this);
    }

    /**
     * Creates a new instance of {@link JGrassRegion}.
     * 
     * <p>
     * This constructor may be used when boundaries and number of rows and
     * columns are available.
     * </p>
     * 
     * @param west
     *            the western boundary.
     * @param east
     *            the eastern boundary.
     * @param south
     *            the southern boundary.
     * @param north
     *            the nothern boundary.
     * @param rows
     *            the number of rows.
     * @param cols
     *            the number of cols.
     */
    public JGrassRegion( double west, double east, double south, double north, int rows, int cols ) {
        this.west = west;
        this.east = east;
        this.south = south;
        this.north = north;
        this.rows = rows;
        this.cols = cols;
        fixResolution();
    }

    /**
     * Creates a new instance of {@link JGrassRegion}.
     * 
     * <p>
     * This constructor may be used when boundaries and the resolution is
     * available.
     * </p>
     * 
     * @param west
     *            the western boundary.
     * @param east
     *            the eastern boundary.
     * @param south
     *            the southern boundary.
     * @param north
     *            the northern boundary.
     * @param weres
     *            the east-west resolution.
     * @param nsres
     *            the north -south resolution.
     */
    public JGrassRegion( double west, double east, double south, double north, double weres,
            double nsres ) {
        this.west = west;
        this.east = east;
        this.south = south;
        this.north = north;
        we_res = weres;
        ns_res = nsres;

        fixRowsAndCols();
        fixResolution();
    }

    /**
     * Creates a new instance of {@link JGrassRegion} by duplicating an existing
     * region.
     * 
     * @param region
     *            a region from which to take the setting from.
     */
    public JGrassRegion( JGrassRegion region ) {
        west = region.getWest();
        east = region.getEast();
        south = region.getSouth();
        north = region.getNorth();
        rows = region.getRows();
        cols = region.getCols();
        fixResolution();
    }

    /**
     * Creates a new instance of {@link JGrassRegion} from an {@link Envelope2D}
     * .
     * 
     * @param envelope2D
     *            the envelope2D from which to take the setting from.
     */
    public JGrassRegion( Envelope2D envelope2D ) {
        west = envelope2D.getMinX();
        east = envelope2D.getMaxX();
        south = envelope2D.getMinY();
        north = envelope2D.getMaxY();
        we_res = envelope2D.getHeight();
        ns_res = envelope2D.getWidth();

        fixRowsAndCols();
        fixResolution();
    }

    /**
     * Creates a new instance of {@link JGrassRegion} from given strings.
     * 
     * @param west
     *            the western boundary string.
     * @param east
     *            the eastern boundary string.
     * @param south
     *            the southern boundary string.
     * @param north
     *            the nothern boundary string.
     * @param ewres the x resolution string.
     * @param nsres the y resolution string.
     */
    public JGrassRegion( String west, String east, String south, String north, String ewres,
            String nsres ) {

        double[] nsew = nsewStringsToNumbers(north, south, east, west);
        double[] xyRes = xyResStringToNumbers(ewres, nsres);
        double no = nsew[0];
        double so = nsew[1];
        double ea = nsew[2];
        double we = nsew[3];
        double xres = xyRes[0];
        double yres = xyRes[1];

        JGrassRegion tmp = new JGrassRegion(we, ea, so, no, xres, yres);
        setExtent(tmp);

    }

    /**
     * Creates a new instance of {@link JGrassRegion} from given strings.
     * 
     * @param west
     *            the western boundary string.
     * @param east
     *            the eastern boundary string.
     * @param south
     *            the southern boundary string.
     * @param north
     *            the nothern boundary string.
     * @param rows
     *            the string of rows.
     * @param cols
     *            the string of cols.
     */
    public JGrassRegion( String west, String east, String south, String north, int rows, int cols ) {
        double[] nsew = nsewStringsToNumbers(north, south, east, west);
        double no = nsew[0];
        double so = nsew[1];
        double ea = nsew[2];
        double we = nsew[3];
        JGrassRegion tmp = new JGrassRegion(we, ea, so, no, rows, cols);
        setExtent(tmp);
    }

    /**
     * Sets the extent of this window using another window.
     * 
     * @param win another window object
     */
    public void setExtent( JGrassRegion region ) {
        west = region.getWest();
        east = region.getEast();
        south = region.getSouth();
        north = region.getNorth();
        rows = region.getRows();
        cols = region.getCols();
        fixResolution();
        fixRowsAndCols();
    }

    /**
     * Creates JTS envelope from the current region.
     * 
     * @return the JTS envelope wrapping the current region.
     */
    public Envelope getEnvelope() {
        return new Envelope(new Coordinate(west, north), new Coordinate(east, south));
    }

    /**
     * Creates a {@linkplain Rectangle2D.Double rectangle} from the current
     * region.
     * 
     * <p>
     * Note that the rectangle width and height are world coordinates.
     * </p>
     * 
     * @return the rectangle wrapping the current region.
     */
    public Rectangle2D.Double getRectangle() {
        return new Rectangle2D.Double(west, south, east - west, north - south);
    }

    @SuppressWarnings("nls")
    public String toString() {
        return ("region:\nwest=" + west + "\neast=" + east + "\nsouth=" + south + "\nnorth="
                + north + "\nwe_res=" + we_res + "\nns_res=" + ns_res + "\nrows=" + rows
                + "\ncols=" + cols);
    }

    /**
     * Reprojects a {@link JGrassRegion region}.
     * 
     * @param sourceCRS
     *            the original {@link CoordinateReferenceSystem crs} of the
     *            region.
     * @param targetCRS
     *            the target {@link CoordinateReferenceSystem crs} of the
     *            region.
     * @param lenient
     *            defines whether to apply a lenient transformation or not.
     * @return a new {@link JGrassRegion region}.
     * @throws Exception
     *             exception that may be thrown when applying the
     *             transformation.
     */
    public JGrassRegion reproject( CoordinateReferenceSystem sourceCRS,
            CoordinateReferenceSystem targetCRS, boolean lenient ) throws Exception {

        MathTransform transform = CRS.findMathTransform(sourceCRS, targetCRS, lenient);
        Envelope envelope = getEnvelope();
        Envelope targetEnvelope = JTS.transform(envelope, transform);

        return new JGrassRegion(targetEnvelope.getMinX(), targetEnvelope.getMaxX(), targetEnvelope
                .getMinY(), targetEnvelope.getMaxY(), getRows(), getCols());

    }

    /**
     * calculates the resolution from the boundaries of the region and the rows
     * and cols.
     */
    private void fixResolution() {
        we_res = (east - west) / cols;
        ns_res = (north - south) / rows;
    }

    /**
     * calculates rows and cols from the region and its resolution.
     * 
     * <p>
     * Rows and cols have to be integers, rounding is applied if required.
     * </p>
     */
    private void fixRowsAndCols() {
        rows = (int) Math.round((north - south) / ns_res);
        if (rows < 1)
            rows = 1;
        cols = (int) Math.round((east - west) / we_res);
        if (cols < 1)
            cols = 1;
    }

    /**
     * Snaps a geographic point to be on the region grid.
     * 
     * <p>
     * Moves the point given by X and Y to be on the grid of the supplied
     * region.
     * </p>
     * 
     * @param x
     *            the easting of the arbitrary point.
     * @param y
     *            the northing of the arbitrary point.
     * @param region
     *            the active window from which to take the grid.
     * @return the snapped coordinate.
     */
    public static Coordinate snapToNextHigherInRegionResolution( double x, double y,
            JGrassRegion region ) {

        double minx = region.getRectangle().getBounds2D().getMinX();
        double ewres = region.getWEResolution();
        double xsnap = minx + (Math.ceil((x - minx) / ewres) * ewres);

        double miny = region.getRectangle().getBounds2D().getMinY();
        double nsres = region.getNSResolution();
        double ysnap = miny + (Math.ceil((y - miny) / nsres) * nsres);

        return new Coordinate(xsnap, ysnap);

    }

    /**
     * Computes the active region from the supplied mapset path.
     * 
     * @param mapsetPath
     *            the path to the mapset folder.
     * @return the active region.
     */
    public static JGrassRegion getActiveRegionFromMapset( String mapsetPath ) throws IOException {
        File windFile = new File(mapsetPath + File.separator + JGrassConstants.WIND);
        if (!windFile.exists()) {
            return null;
        }
        return new JGrassRegion(windFile.getAbsolutePath());
    }

    /**
     * Writes active region window to the supplied mapset.
     * 
     * @param mapsetPath
     *            the path to the mapset folder.
     * @param activeRegion
     *            the active region.
     * @throws IOException
     */
    public static void writeWINDToMapset( String mapsetPath, JGrassRegion activeRegion )
            throws IOException {
        writeRegionToFile(mapsetPath + File.separator + JGrassConstants.WIND, activeRegion);
    }

    /**
     * Write default region to the PERMANENT mapset.
     * 
     * @param locationPath
     *            the path to the location folder.
     * @param region
     *            a region.
     * @throws IOException
     */
    public static void writeDEFAULTWINDToLocation( String locationPath, JGrassRegion region )
            throws IOException {
        writeRegionToFile(locationPath + File.separator + JGrassConstants.PERMANENT_MAPSET
                + File.separator + JGrassConstants.DEFAULT_WIND, region);
    }

    /**
     * Creates a region from envelope bounds snapped to a region grid.
     * 
     * <p>
     * This takes an envelope and a JGrass region and creates a new region to
     * match the bounds of the envelope, but the grid of the region. This is
     * important if the region has to match some feature layer.
     * </p>
     * <p>
     * The bounds of the new region contain completely the envelope.
     * </p>
     * 
     * @param sourceEnvelope
     *            the envelope to adapt.
     * @param sourceRegion
     *            the region from which to take the grid to be snapped.
     * @return a new region, created from the envelope bounds snapped to the
     *         region grid.
     */
    public static JGrassRegion adaptActiveRegionToEnvelope( Envelope sourceEnvelope,
            JGrassRegion sourceRegion ) {
        Coordinate eastNorth = JGrassRegion.snapToNextHigherInRegionResolution(sourceEnvelope
                .getMaxX(), sourceEnvelope.getMaxY(), sourceRegion);
        Coordinate westsouth = JGrassRegion.snapToNextHigherInRegionResolution(sourceEnvelope
                .getMinX()
                - sourceRegion.getWEResolution(), sourceEnvelope.getMinY()
                - sourceRegion.getNSResolution(), sourceRegion);
        JGrassRegion newRegion = new JGrassRegion(westsouth.x, eastNorth.x, westsouth.y,
                eastNorth.y, sourceRegion.getWEResolution(), sourceRegion.getNSResolution());
        return newRegion;
    }

    /**
     * @param subregionsNum
     * @return
     */
    public List<JGrassRegion> toSubRegions( int subregionsNum ) {
        int tmpR = getRows();
        int tmpC = getCols();

        double tmpWest = getWest();
        double tmpSouth = getSouth();
        double tmpWERes = getWEResolution();
        double tmpNSRes = getNSResolution();

        if (subregionsNum > tmpR || subregionsNum > tmpC) {
            throw new IllegalArgumentException(
                    "The number of subregions has to be smaller than the number of rows and columns.");
        }

        int subregRows = (int) Math.floor(tmpR / (double) subregionsNum);
        int subregCols = (int) Math.floor(tmpC / (double) subregionsNum);

        List<JGrassRegion> regions = new ArrayList<JGrassRegion>();

        double runningEasting = tmpWest;
        double runningNorthing = tmpSouth;
        for( int i = 0; i < subregionsNum; i++ ) {
            double n = runningNorthing + subregRows * tmpNSRes;
            double s = runningNorthing;
            for( int j = 0; j < subregionsNum; j++ ) {
                double w = runningEasting;
                double e = runningEasting + subregCols * tmpWERes;

                if (e > getEast()) {
                    e = getEast();
                }
                if (n > getNorth()) {
                    n = getNorth();
                }

                JGrassRegion r = new JGrassRegion(w, e, s, n, tmpWERes, tmpNSRes);
                if (r.getWEResolution() == 0 || r.getNSResolution() == 0) {
                    continue;
                }
                regions.add(r);

                runningEasting = e;
            }
            runningEasting = tmpWest;
            runningNorthing = n;
        }

        return regions;
    }
    /**
     * Reads a region file and sets a given region to the supplied region file.
     * 
     * @param filePath
     *            the path to the region file.
     * @param region
     *            the region to be set to the region file informations.
     */
    @SuppressWarnings("nls")
    private void readRegionFromFile( String filePath, JGrassRegion region ) throws IOException {
        String line;

        BufferedReader windReader = new BufferedReader(new FileReader(filePath));
        LinkedHashMap<String, String> store = new LinkedHashMap<String, String>();
        while( (line = windReader.readLine()) != null ) {
            if (line.matches(".*reclass.*")) {
                /*
                 * it is a reclass map and we are reading the cellhead file.
                 * Need to redirect to the original cellhead file.
                 */
                String mapLine = windReader.readLine();
                String mapsetLine = windReader.readLine();
                if (mapLine == null || mapsetLine == null) {
                    throw new IOException("Wrong reclass file format");
                }

                String mapName = mapLine.trim().split(":")[1].trim();
                String mapsetName = mapsetLine.trim().split(":")[1].trim();
                File f = new File(filePath).getParentFile().getParentFile().getParentFile();
                File reclassMap = new File(f, mapsetName + "/" + JGrassConstants.CELLHD + "/"
                        + mapName);
                if (!reclassMap.exists()) {
                    throw new IOException(
                            "The reclass cellhead file doesn't seem to exist. Unable to read the file region.");
                }
                windReader.close();
                windReader = new BufferedReader(new FileReader(reclassMap));
                line = windReader.readLine();
            }

            if (line == null) {
                throw new IOException("Wrong reclass file format");
            }

            String[] lineSplit = line.split(":", 2);
            if (lineSplit.length == 2) {
                String key = lineSplit[0].trim();
                String value = lineSplit[1].trim();
                /*
                 * If key is 'e-w res' or 'n-s resol' or 'res3' then store 'xxx
                 * resol'
                 */
                // this is to keep compatibility with GRASS, which seems to
                // have changed
                if ((key.indexOf("res") != -1 && key.indexOf("resol") == -1) //$NON-NLS-1$ //$NON-NLS-2$
                        || key.indexOf("res3") != -1) { //$NON-NLS-1$
                    if (!key.startsWith("compressed")) //$NON-NLS-1$
                        store.put(key.replaceAll("res", "resol"), value); //$NON-NLS-1$ //$NON-NLS-2$
                } else {
                    store.put(key, value);
                }
            }
        }

        try {
            region.setProj(Integer.parseInt(store.get("proj"))); //$NON-NLS-1$
            region.setZone(Integer.parseInt(store.get("zone"))); //$NON-NLS-1$
            store.remove("proj");
            store.remove("zone");
        } catch (Exception e) {
            // do nothing
        }
        // assign the values
        String tmpNorth = store.get("north");
        String tmpSouth = store.get("south");
        String tmpEast = store.get("east");
        String tmpWest = store.get("west");
        double[] nsew = nsewStringsToNumbers(tmpNorth, tmpSouth, tmpEast, tmpWest);

        region.setNorth(nsew[0]); //$NON-NLS-1$
        region.setSouth(nsew[1]); //$NON-NLS-1$
        region.setEast(nsew[2]); //$NON-NLS-1$
        region.setWest(nsew[3]); //$NON-NLS-1$
        store.remove("north");
        store.remove("south");
        store.remove("east");
        store.remove("west");

        // if the resolution if undefined, at least the row and cols have to
        // be supplied
        if (!store.containsKey("e-w resol") && !store.containsKey("n-s resol")) { //$NON-NLS-1$ //$NON-NLS-2$
            region.setCols(Integer.parseInt(store.get("cols"))); //$NON-NLS-1$
            region.setRows(Integer.parseInt(store.get("rows"))); //$NON-NLS-1$
            store.remove("cols");
            store.remove("rows");

            region.fixResolution();
        } else {
            double[] xyRes = xyResStringToNumbers(store.get("e-w resol"), store.get("n-s resol"));

            region.setWEResolution(xyRes[0]); //$NON-NLS-1$
            region.setNSResolution(xyRes[1]); //$NON-NLS-1$
            store.remove("e-w resol");
            store.remove("n-s resol");

            region.fixRowsAndCols();
        }

        // what is not needed in JGrass is needed in GRASS, so keep it
        region.setAdditionalGrassEntries(store);

        windReader.close();
        windReader = null;
        store = null;
    }

    /**
     * Transforms degree string into the decimal value.
     * 
     * @param value the string in degrees.
     * @return the translated value.
     */
    private double degreeToNumber( String value ) {
        double number = -1;

        String[] valueSplit = value.trim().split(":"); //$NON-NLS-1$
        if (valueSplit.length == 3) {
            // deg:min:sec.ss
            double deg = Double.parseDouble(valueSplit[0]);
            double min = Double.parseDouble(valueSplit[1]);
            double sec = Double.parseDouble(valueSplit[2]);
            number = deg + min / 60.0 + sec / 60.0 / 60.0;
        } else if (valueSplit.length == 2) {
            // deg:min
            double deg = Double.parseDouble(valueSplit[0]);
            double min = Double.parseDouble(valueSplit[1]);
            number = deg + min / 60.0;
        } else if (valueSplit.length == 1) {
            // deg
            number = Double.parseDouble(valueSplit[0]);
        }
        return number;
    }

    /**
     * Transforms a GRASS resolution string in metric or degree to decimal.
     * 
     * @param ewres the x resolution string.
     * @param nsres the y resolution string.
     * @return the array of x and y resolution doubles.
     */
    private double[] xyResStringToNumbers( String ewres, String nsres ) {
        double xres = -1.0;
        double yres = -1.0;
        if (ewres.indexOf(':') != -1) {
            xres = degreeToNumber(ewres);
        } else {
            xres = Double.parseDouble(ewres);
        }
        if (nsres.indexOf(':') != -1) {
            yres = degreeToNumber(nsres);
        } else {
            yres = Double.parseDouble(nsres);
        }

        return new double[]{xres, yres};
    }

    /**
     * Transforms the GRASS bounds strings in metric or degree to decimal.
     * 
     * @param north the north string.
     * @param south the south string.
     * @param east the east string.
     * @param west the west string.
     * @return the array of the bounds in doubles.
     */
    @SuppressWarnings("nls")
    private double[] nsewStringsToNumbers( String north, String south, String east, String west ) {

        double no = -1.0;
        double so = -1.0;
        double ea = -1.0;
        double we = -1.0;

        if (north.indexOf("N") != -1 || north.indexOf("n") != -1) {
            north = north.substring(0, north.length() - 1);
            no = degreeToNumber(north);
        } else if (north.indexOf("S") != -1 || north.indexOf("s") != -1) {
            north = north.substring(0, north.length() - 1);
            no = -degreeToNumber(north);
        } else {
            no = Double.parseDouble(north);
        }
        if (south.indexOf("N") != -1 || south.indexOf("n") != -1) {
            south = south.substring(0, south.length() - 1);
            so = degreeToNumber(south);
        } else if (south.indexOf("S") != -1 || south.indexOf("s") != -1) {
            south = south.substring(0, south.length() - 1);
            so = -degreeToNumber(south);
        } else {
            so = Double.parseDouble(south);
        }
        if (west.indexOf("E") != -1 || west.indexOf("e") != -1) {
            west = west.substring(0, west.length() - 1);
            we = degreeToNumber(west);
        } else if (west.indexOf("W") != -1 || west.indexOf("w") != -1) {
            west = west.substring(0, west.length() - 1);
            we = -degreeToNumber(west);
        } else {
            we = Double.parseDouble(west);
        }
        if (east.indexOf("E") != -1 || east.indexOf("e") != -1) {
            east = east.substring(0, east.length() - 1);
            ea = degreeToNumber(east);
        } else if (east.indexOf("W") != -1 || east.indexOf("w") != -1) {
            east = east.substring(0, east.length() - 1);
            ea = -degreeToNumber(east);
        } else {
            ea = Double.parseDouble(east);
        }

        return new double[]{no, so, ea, we};
    }

    /**
     * Writes a region to file.
     * 
     * <p>
     * Reads a text file and changes only the region values using the supplied
     * region.
     * </p>
     * 
     * @param regionFilePath
     *            the path to the region file.
     * @param region
     *            the region to be written to file
     * @throws IOException
     */
    private static void writeRegionToFile( String regionFilePath, JGrassRegion region )
            throws IOException {

        String line;
        File file = new File(regionFilePath);
        if (!file.exists()) {
            /*
             * if on vfat filesystem it could be a problem of case, often
             * happens with WIND file. So at least try that check.
             */
            String nameLower = file.getName().toLowerCase();
            String nameUpper = file.getName().toUpperCase();
            String baseDir = file.getParent();
            File tmpFile = null;
            if ((tmpFile = new File(baseDir + File.separator + nameLower)).exists()) {
                file = tmpFile;
            } else if ((tmpFile = new File(baseDir + File.separator + nameUpper)).exists()) {
                file = tmpFile;
            } else {
                // ok, file doesn't really exist, just create a blank window
                // first
                BufferedWriter out = new BufferedWriter(new FileWriter(file));
                out.write(BLANK_REGION);
                out.close();
            }
        }
        BufferedReader windReader = new BufferedReader(new FileReader(file));
        LinkedHashMap<String, String> store = new LinkedHashMap<String, String>();
        while( (line = windReader.readLine()) != null ) {
            StringTokenizer tok = new StringTokenizer(line, ":"); //$NON-NLS-1$
            if (tok.countTokens() == 2) {
                String key = tok.nextToken().trim();
                String value = tok.nextToken().trim();
                /*
                 * this is now corrected, since GRASS seems to support only
                 * resol from 6.2 on
                 */
                if ((key.indexOf("res") != -1 && key.indexOf("resol") == -1) //$NON-NLS-1$ //$NON-NLS-2$
                        || key.indexOf("res3") != -1) { //$NON-NLS-1$
                    store.put(key.replaceAll("res", "resol"), value); //$NON-NLS-1$ //$NON-NLS-2$
                } else
                    store.put(key, value);
            }
        }

        /*
         * Now overwrite the window region entries using the values in the
         * supplied window object.
         */
        store.put("north", new java.lang.Double(region.getNorth()).toString()); //$NON-NLS-1$
        store.put("south", new java.lang.Double(region.getSouth()).toString()); //$NON-NLS-1$
        store.put("east", new java.lang.Double(region.getEast()).toString()); //$NON-NLS-1$
        store.put("west", new java.lang.Double(region.getWest()).toString()); //$NON-NLS-1$
        store.put("n-s resol", new java.lang.Double(region.getNSResolution()).toString()); //$NON-NLS-1$
        store.put("e-w resol", new java.lang.Double(region.getWEResolution()).toString()); //$NON-NLS-1$
        store.put("cols", new java.lang.Integer(region.getCols()).toString()); //$NON-NLS-1$
        store.put("rows", new java.lang.Integer(region.getRows()).toString()); //$NON-NLS-1$
        windReader.close();
        windReader = null;

        /* Now write the data back to the file */
        StringBuffer data = new StringBuffer(512);
        Set<Entry<String, String>> entrySet = store.entrySet();
        for( Entry<String, String> entry : entrySet ) {
            data.append(entry.getKey() + ":   " + entry.getValue() + "\n"); //$NON-NLS-1$ //$NON-NLS-2$
        }

        BufferedWriter windWriter = new BufferedWriter(new FileWriter(file));
        windWriter.write(data.toString());
        windWriter.flush();
        windWriter.close();
        windWriter = null;
    }

    /**
     * Getter for proj
     * 
     * @return the proj
     */
    public int getProj() {
        return proj;
    }

    /**
     * Setter for proj
     * 
     * @param proj
     *            the proj to set
     */
    public void setProj( int proj ) {
        this.proj = proj;
    }

    /**
     * Getter for zone
     * 
     * @return the zone
     */
    public int getZone() {
        return zone;
    }

    /**
     * Setter for zone
     * 
     * @param zone
     *            the zone to set
     */
    public void setZone( int zone ) {
        this.zone = zone;
    }

    /**
     * Getter for north
     * 
     * @return the north
     */
    public double getNorth() {
        return north;
    }

    /**
     * Setter for north
     * 
     * @param north
     *            the north to set
     */
    public void setNorth( double north ) {
        this.north = north;
    }

    /**
     * Getter for south
     * 
     * @return the south
     */
    public double getSouth() {
        return south;
    }

    /**
     * Setter for south
     * 
     * @param south
     *            the south to set
     */
    public void setSouth( double south ) {
        this.south = south;
    }

    /**
     * Getter for west
     * 
     * @return the west
     */
    public double getWest() {
        return west;
    }

    /**
     * Setter for west
     * 
     * @param west
     *            the west to set
     */
    public void setWest( double west ) {
        this.west = west;
    }

    /**
     * Getter for east
     * 
     * @return the east
     */
    public double getEast() {
        return east;
    }

    /**
     * Setter for east
     * 
     * @param east
     *            the east to set
     */
    public void setEast( double east ) {
        this.east = east;
    }

    /**
     * Getter for ns_res
     * 
     * @return the ns_res
     */
    public double getNSResolution() {
        return ns_res;
    }

    /**
     * Setter for ns_res
     * 
     * @param ns_res
     *            the ns_res to set
     */
    public void setNSResolution( double ns_res ) {
        this.ns_res = ns_res;
        fixRowsAndCols();
        fixResolution();
    }

    /**
     * Getter for we_res
     * 
     * @return the we_res
     */
    public double getWEResolution() {
        return we_res;
    }

    /**
     * Setter for we_res
     * 
     * @param we_res
     *            the we_res to set
     */
    public void setWEResolution( double we_res ) {
        this.we_res = we_res;
        fixRowsAndCols();
        fixResolution();
    }

    /**
     * Getter for rows
     * 
     * @return the rows
     */
    public int getRows() {
        return rows;
    }

    /**
     * Setter for rows
     * 
     * @param rows
     *            the rows to set
     */
    public void setRows( int rows ) {
        this.rows = rows;
        fixResolution();
    }

    /**
     * Getter for cols.
     * 
     * @return the cols.
     */
    public int getCols() {
        return cols;
    }

    /**
     * Setter for cols.
     * 
     * @param cols
     *            the cols to set.
     */
    public void setCols( int cols ) {
        this.cols = cols;
        fixResolution();
    }

    /**
     * Getter for additionalGrassEntries.
     * 
     * @return the additionalGrassEntries.
     */
    public LinkedHashMap<String, String> getAdditionalGrassEntries() {
        return additionalGrassEntries;
    }

    /**
     * Setter for additionalGrassEntries.
     * 
     * @param additionalGrassEntries
     *            the additionalGrassEntries to set.
     */
    public void setAdditionalGrassEntries( LinkedHashMap<String, String> additionalGrassEntries ) {
        this.additionalGrassEntries = additionalGrassEntries;
    }
}
