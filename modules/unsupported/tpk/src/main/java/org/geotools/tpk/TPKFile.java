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
 *
 */

package org.geotools.tpk;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.opengis.geometry.Envelope;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class TPKFile {

    private static final Logger LOGGER = org.geotools.util.logging.Logging.getLogger(TPKFile.class);

    // circumference of the earth in metres at the equator
    private static double WORLD_CIRCUMFERENCE = 40075016.69;
    private static double ORIGIN_OFFSET = WORLD_CIRCUMFERENCE / 2.0;
    // in a web map service tileset all tiles are 256x256 pixels
    private static long TILE_PIXEL_SIZE = 256;

    // parsing the conf.xml file
    private static String CONFIGURATION_FILE = "/conf.xml";
    private static String TAG_STORAGE_FORMAT = "StorageFormat";
    private static String TAG_LOD_INFO = "LODInfo";
    private static String TAG_TILE_FORMAT = "CacheTileFormat";
    private static String TAG_LEVEL_ID = "LevelID";
    private static String TAG_RESOLUTION = "Resolution";

    // support for compact cache v1 and v2!!
    private static String COMPACT_CACHE_V1 = "esriMapCacheStorageModeCompact";
    private static String COMPACT_CACHE_V2 = "esriMapCacheStorageModeCompactV2";

    // finding zoom levels and their bundles/indexes
    private static String LEVEL_FOLDER = "_alllayers/L%02d/";
    private static String BUNDLE_DATA_EXTENSION = ".bundle";
    private static String BUNDLE_INDEX_EXTENSION = ".bundlx";

    // the TPK file (a TPK file is a ZIP file)
    private ZipFile theTPK;

    // map of contents of TPK file by file "path/name"
    private Map<String, ZipEntry> zipEntryMap = new HashMap<>();

    // TPKZoomLevel object for each WTMS zoom level in file
    private Map<Long, TPKZoomLevel> zoomLevelMap;

    // individual tiles stored in this format
    private String imageFormat;

    // holds the Geographical bounds of the map coverage
    private Envelope bounds;

    // maps conf.xml//CacheInfo/TileCacheInfo/LODInfos/LODInfo/LevelID values to actual Web Map
    // Tile Service zoom levels (only used in initial open of TPK)
    private Map<Long, Long> zoomLevelMapping;

    // maps WMTS zoom level to it's resolution (only used in initial open of TPK)
    private Map<Long, Double> zoomLevelResolutionMap;

    // support the original compact cache format as well as the new one
    private enum CacheType {
        V1, // bundle data and index files are separate
        V2 // index is included within the bundle data file and is different format
    }

    private CacheType cacheType;

    /**
     * Constructor -- used for first open of a TPK file, parses the conf.xml file and builds the
     * zoomLevelMap
     *
     * @param theFile -- the TPK file in question
     * @param zoomLevelMap -- reference to an empty hashmap object that we will populate
     */
    public TPKFile(File theFile, Map<Long, TPKZoomLevel> zoomLevelMap) {

        // open the file and build the zipEntryMap
        openTPK(theFile);

        // the zoom level map is "owned" by the caller and will be initialized by this
        // constructor so it can be re-used over and over
        this.zoomLevelMap = zoomLevelMap;

        // find the "conf.xml" file
        String xmlConf =
                zipEntryMap.keySet().stream()
                        .filter(s -> s.endsWith(CONFIGURATION_FILE))
                        .findFirst()
                        .orElse(null);

        // extract metadata we need from the conf.xml file of the TPK
        parseConfigurationFile(zipEntryMap.get(xmlConf));

        // create a TPKZoomLevel object for each zoom level
        loadZoomLevels();
    }

    /**
     * Constructor -- used for all subsequent opens of the TPK file (ie once the zoomLevelMap has
     * been created)
     *
     * @param theFile -- the TPK file
     * @param zoomLevelMap -- previously constructed zoomLevelMap
     * @param bounds -- bounds of the map
     * @param imageFormat -- the image format being used
     */
    public TPKFile(
            File theFile,
            Map<Long, TPKZoomLevel> zoomLevelMap,
            Envelope bounds,
            String imageFormat) {
        openTPK(theFile);
        this.imageFormat = imageFormat;
        this.bounds = bounds;
        this.zoomLevelMap = zoomLevelMap;
        zoomLevelMap.values().forEach(zl -> zl.setTPKandEntryMap(theTPK, zipEntryMap));
    }

    // ***********************************************************************
    // **                                                                   **
    // **   Public Methods                                                  **
    // **                                                                   **
    // ***********************************************************************

    /** Called to close the TPK file and release resources being held */
    public void close() {
        zoomLevelMap.values().forEach(TPKZoomLevel::releaseResources);
        zipEntryMap.clear();
        try {
            theTPK.close();
        } catch (IOException ex) { // don't care
        }
    }

    /** @return -- the minimum Web Tile Service zoom level contained in the TPK */
    public long getMinZoomLevel() {
        return zoomLevelMap.keySet().stream().min(Long::compare).get();
    }

    /** @return -- the maximum Web Tile Service zoom level contained in the TPK */
    public long getMaxZoomLevel() {
        return zoomLevelMap.keySet().stream().max(Long::compare).get();
    }

    /** @return -- the image format used in the TPK (eg JPEG, PNG) */
    public String getImageFormat() {
        return imageFormat;
    }

    /**
     * Using the highest zoom level contained in the TPK file use the tile coverage to calculate the
     * geographical bounds of the map
     *
     * @return -- the geographical coverage of the map
     */
    public Envelope getBounds() {
        if (bounds == null) {
            if (zoomLevelMap == null || zoomLevelMap.isEmpty()) {
                throw new RuntimeException("Can't get bounds, zoomLevelMap not initialized");
            }
            // calculate the coverage bounds from the highest zoom level tile set
            calculateBoundsFromTileset(zoomLevelMap.get(getMaxZoomLevel()));
        }
        return bounds;
    }

    /**
     * Find the closest zoom level in the TPK to the given zoom level
     *
     * @param zoomLevel -- zoom level to match or approximate
     * @return -- the given zoom level of the nearest one to it
     */
    public long getClosestZoom(long zoomLevel) {
        Set<Long> zooms = zoomLevelMap.keySet();
        if (zooms.contains(zoomLevel)) {
            return zoomLevel;
        } else {
            long smallestDiff = Long.MAX_VALUE;
            long closestZoom = 0;
            for (long zl : zooms) {
                long diff = Math.abs(zoomLevel - zl);
                if (diff < smallestDiff) {
                    smallestDiff = diff;
                    closestZoom = zl;
                }
            }
            return closestZoom;
        }
    }

    /**
     * Find the minimum Web Tile Map Service column number for the given zoom level
     *
     * @param zoomLevel -- zoom level in question
     * @return -- -1 if zoom level not in TPK else minimum column at zoom level
     */
    public long getMinColumn(long zoomLevel) {
        long retValue = -1;
        if (zoomLevelMap.containsKey(zoomLevel)) {
            retValue = zoomLevelMap.get(zoomLevel).getMinColumn();
        }
        return retValue;
    }

    /**
     * Find the maximum Web Tile Map Service column number for the given zoom level
     *
     * @param zoomLevel -- zoom level in question
     * @return -- -1 if zoom level not in TPK else maximum column at zoom level
     */
    public long getMaxColumn(long zoomLevel) {
        long retValue = -1;
        if (zoomLevelMap.containsKey(zoomLevel)) {
            retValue = zoomLevelMap.get(zoomLevel).getMaxColumn();
        }
        return retValue;
    }

    /**
     * Find the minimum Web Tile Map Service row number for the given zoom level
     *
     * @param zoomLevel -- zoom level in question
     * @return -- -1 if zoom level not in TPK else minimum row at zoom level
     */
    public long getMinRow(long zoomLevel) {
        long retValue = -1;
        if (zoomLevelMap.containsKey(zoomLevel)) {
            retValue = zoomLevelMap.get(zoomLevel).getMinRow();
        }
        return retValue;
    }

    /**
     * Find the maximum Web Tile Map Service row number for the given zoom level
     *
     * @param zoomLevel -- zoom level in question
     * @return -- -1 if zoom level not in TPK else maximum row at zoom level
     */
    public long getMaxRow(long zoomLevel) {
        long retValue = -1;
        if (zoomLevelMap.containsKey(zoomLevel)) {
            retValue = zoomLevelMap.get(zoomLevel).getMaxRow();
        }
        return retValue;
    }

    /**
     * Return a list of tile objects, each with its intended location, format and raw data
     *
     * @param zoomLevel -- zoom level of tiles to return
     * @param top -- topmost row of tiles (lattitude)
     * @param bottom -- bottommost row of tiles
     * @param left -- leftmost column of tiles (longitude)
     * @param right -- rightmost column of tiles
     * @param format -- format to interpret tile data (PNG, JPEG)
     * @return -- list of TPKTile objects
     */
    public List<TPKTile> getTiles(
            long zoomLevel, long top, long bottom, long left, long right, String format) {
        if (zoomLevelMap.containsKey(zoomLevel)) {
            return zoomLevelMap.get(zoomLevel).getTiles(top, bottom, left, right, format);
        }
        return null;
    }

    // ***********************************************************************
    // **                                                                   **
    // **   Private Methods                                                 **
    // **                                                                   **
    // ***********************************************************************

    /**
     * Open the TPK file and map the ZIPEntries by their path/filename
     *
     * <p>Populates theTPK and zipEntryMap
     *
     * @param theFile -- TPK File
     */
    private void openTPK(File theFile) {
        // open the TPK file as a ZIP archive
        try {
            theTPK = new ZipFile(theFile);
        } catch (IOException ex) {
            throw new RuntimeException("Unable to open TPK file", ex);
        }

        // build a map of file path/name -> ZipEntry
        Enumeration<? extends ZipEntry> zipEntries = theTPK.entries();
        while (zipEntries.hasMoreElements()) {
            ZipEntry entry = zipEntries.nextElement();
            zipEntryMap.put(entry.getName(), entry);
        }
    }

    /**
     * Extract required metatdata from conf.xml file
     *
     * <p>Populates imageFormat, creates and poulates zoomLevelMapping and zoomLevelResolutionMap
     * hashmaps
     *
     * @param confFile -- ZipEntry object for the TPK's conf.xml file
     */
    private void parseConfigurationFile(ZipEntry confFile) {
        zoomLevelMapping = new HashMap<>();
        zoomLevelResolutionMap = new HashMap<>();

        try (InputStream is = theTPK.getInputStream(confFile)) {
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document confDoc = dBuilder.parse(is);

            // find the "StorageFormat" element to determine compact cache version/format
            NodeList sfs = confDoc.getElementsByTagName(TAG_STORAGE_FORMAT);
            Node sf = sfs.item(0);
            if (sf.getNodeType() == Node.ELEMENT_NODE) {
                String storageFormat = sf.getTextContent();
                if (storageFormat.equals(COMPACT_CACHE_V1)) {
                    cacheType = CacheType.V1;
                } else if (storageFormat.equals(COMPACT_CACHE_V2)) {
                    cacheType = CacheType.V2;
                } else {
                    throw new RuntimeException("Unknown value for StorageFormat element");
                }
            }

            // get the format of each tile stored in the cache
            NodeList ctfs = confDoc.getElementsByTagName(TAG_TILE_FORMAT);
            Node ctf = ctfs.item(0);
            if (ctf.getNodeType() == Node.ELEMENT_NODE) {
                imageFormat = ctf.getTextContent();
            }

            // get a list of the zoom-level information elements and go parse them
            NodeList lods = confDoc.getElementsByTagName(TAG_LOD_INFO);
            for (int i = 0; i < lods.getLength(); i++) {
                parseLodInfo(lods.item(i));
            }

        } catch (Exception ex) {
            throw new RuntimeException("Caught exception opening/processing conf.xml", ex);
        }
    }

    /**
     * parse LODInfo/LevelId and LODInfo/Resolution in order to map the nominal LevelID value to an
     * actual zoom level
     *
     * <p>Note: this method loads the zoomLevelMapping and zoomLevelResolutionMap hashmaps
     *
     * @param lodInfo -- "Level of Detail Information" xml element
     */
    private void parseLodInfo(Node lodInfo) {
        if (lodInfo.getNodeType() == Node.ELEMENT_NODE) {
            Element lod = (Element) lodInfo;
            NodeList lidList = lod.getElementsByTagName(TAG_LEVEL_ID);
            NodeList resList = lod.getElementsByTagName(TAG_RESOLUTION);
            Node lid = lidList.item(0);
            Node res = resList.item(0);
            if (lid.getNodeType() == Node.ELEMENT_NODE && res.getNodeType() == Node.ELEMENT_NODE) {
                String levelString = lid.getTextContent();
                Long level = Long.valueOf(levelString);

                String resString = res.getTextContent();
                Double resolution = Double.valueOf(resString);
                long zoom_level =
                        Math.round(log2(WORLD_CIRCUMFERENCE / (resolution * TILE_PIXEL_SIZE)));
                zoomLevelMapping.put(level, zoom_level);
                zoomLevelResolutionMap.put(zoom_level, resolution);
            }
        }
    }

    /**
     * @param number -- number in question
     * @return -- log base 2 of the given number
     */
    private double log2(double number) {
        return Math.log(number) / Math.log(2.0);
    }

    /**
     * Iterate over the Zoom Levels contained in the TPK archive and build a TPKZoomLevel object for
     * each
     *
     * <p>The TPKZoomLevel object caches control information about the zoom level and each bundle
     * that comprises the zoom level. Access to individual tile data is done via this object.
     */
    private void loadZoomLevels() {

        long startLoad = System.currentTimeMillis();
        for (Long levelId : zoomLevelMapping.keySet()) {

            // "LevelID" folder
            String levelFolder = String.format(LEVEL_FOLDER, levelId);

            List<String> indexes = null;

            // find names of all bundles for level
            List<String> bundles =
                    zipEntryMap.keySet().stream()
                            .filter(s -> s.contains(levelFolder))
                            .filter(s -> s.endsWith(BUNDLE_DATA_EXTENSION))
                            .collect(Collectors.toList());

            // find names of all bundle indexes for level
            if (cacheType == CacheType.V1) { // V2 caches don't have independent indexes
                indexes =
                        zipEntryMap.keySet().stream()
                                .filter(s -> s.contains(levelFolder))
                                .filter(s -> s.endsWith(BUNDLE_INDEX_EXTENSION))
                                .collect(Collectors.toList());
            }

            if (!bundles.isEmpty()) {

                // get the LODInfo/LevelID mapping to actual WTMS zoom level
                Long zoomLevel = zoomLevelMapping.get(levelId);

                // go build a zoom level object using the related bundles and bundle-indexes
                TPKZoomLevel zlObj = null;
                if (cacheType == CacheType.V1) {
                    zlObj = new TPKZoomLevelV1(theTPK, zipEntryMap, bundles, indexes, zoomLevel);
                } else if (cacheType == CacheType.V2) {
                    zlObj = new TPKZoomLevelV2(theTPK, zipEntryMap, bundles, zoomLevel);
                }

                // keep track of it
                zoomLevelMap.put(zoomLevel, zlObj);
            }
        }

        String msg =
                String.format(
                        "Loaded zoom levels in %d milliseconds",
                        System.currentTimeMillis() - startLoad);
        LOGGER.fine(msg);
    }

    /**
     * Using the supplied TPKZoomLevel object calculate the geographical bounds of the map using the
     * zoom level's minColumn, maxColumn, minRow, maxRow and resolution value
     *
     * @param zl -- the highest zoom level found in the TPK
     */
    private void calculateBoundsFromTileset(TPKZoomLevel zl) {

        double resolution = zoomLevelResolutionMap.get(zl.getZoomLevel());

        double minX =
                ((zl.getMinColumn() * TILE_PIXEL_SIZE * resolution - ORIGIN_OFFSET) / ORIGIN_OFFSET)
                        * 180.0;
        double maxX =
                (((zl.getMaxColumn() + 1) * TILE_PIXEL_SIZE * resolution - ORIGIN_OFFSET)
                                / ORIGIN_OFFSET)
                        * 180.0;

        double lat =
                ((zl.getMinRow() * TILE_PIXEL_SIZE * resolution - ORIGIN_OFFSET) / ORIGIN_OFFSET)
                        * 180;
        double minY =
                180.0 / Math.PI * (2 * Math.atan(Math.exp(lat * Math.PI / 180.0)) - Math.PI / 2.0);

        lat =
                (((zl.getMaxRow() + 1) * TILE_PIXEL_SIZE * resolution - ORIGIN_OFFSET)
                                / ORIGIN_OFFSET)
                        * 180;
        double maxY =
                180.0 / Math.PI * (2 * Math.atan(Math.exp(lat * Math.PI / 180.0)) - Math.PI / 2.0);

        bounds = new ReferencedEnvelope(minX, maxX, minY, maxY, TPKReader.WGS_84);
    }
}
