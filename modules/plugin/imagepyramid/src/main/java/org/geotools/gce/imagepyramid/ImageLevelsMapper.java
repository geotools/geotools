/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2016, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.gce.imagepyramid;

import java.io.IOException;
import java.net.URL;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.geotools.gce.imagemosaic.ImageMosaicReader;
import org.geotools.util.URLs;
import org.geotools.util.factory.Hints;

/**
 * Parse imagePyramid property files and setup the mapping to provide the proper {@link ImageMosaicReader} for the
 * required image choice. We are supporting ImagePyramids of ImageMosaic with internal overviews, therefore the mapper
 * should take care of different levels which can be related to the same underlying imageMosaic.
 *
 * <p>When ImageMosaic has overviews, the "Levels" property will look like this example: In this case, "Levels" in the
 * property file is like this: Levels=1,1;2,2 4,4;8,8 16,16;32,32
 *
 * <p>White spaces separate groups of resolutions from different mosaics as before. mosaic0 has resolutions 1,1;2,2
 * mosaic1 has resolutions 4,4;8,8 mosaic2 has resolutions 16,16;32,32
 *
 * <p>Semicolon (new char to support overviews) separates resolutions of the same mosaic. mosaic0 has native resolution
 * = 1,1 mosaic0 has 1 overview with resolution = 2,2
 *
 * <p>Comma separates x,y resolutions as before.
 */
class ImageLevelsMapper {

    /** Logger. */
    private static final Logger LOGGER = org.geotools.util.logging.Logging.getLogger(ImageLevelsMapper.class);

    /**
     * The whole number of overviews in the pyramid.
     *
     * <p>Note that all the available resolutions on the underlying mosaics are considered overviews, except the native
     * resolution level of the first mosaic.
     */
    private int numOverviews;

    /**
     * All the available resolutions in the pyramid, beside the highest one.
     *
     * <p>Note that all the available resolutions on the underlying mosaics are considered overviews, except the native
     * resolution level of the first mosaic.
     */
    private double[][] overViewResolutions;

    private double[] highestResolution;

    /** simple mapping between an imageChoice and a reader index */
    private int[] imageChoiceToReaderLookup;

    /** flags reporting if there is at least a reader with inner overviews */
    private boolean innerOverviews = false;

    /** The directories where to find the different resolutions levels in descending order. */
    private String[] levelsDirs;

    public ImageLevelsMapper(Properties properties) {
        levelsDirs = properties.getProperty("LevelsDirs").split(" ");

        // resolutions levels
        final String levels = properties.getProperty("Levels");
        String[] resolutionLevels = levels.split(" ");

        // Grouping the resolution levels
        int resolutionGroupsNumber = resolutionLevels.length;

        // array is organized in 3 layers of resolutions (see the main javadoc at the beginning of
        // the class):
        // layer 0 elements: the mosaics
        //  \-> layer 1 elements: the different levels in the selected mosaic
        //       \-> layer 2 elements: x,y resolutions of that level
        double[][][] resolutionsSet = new double[resolutionGroupsNumber][][];

        int numResolutions = 0;
        for (int i = 0; i < resolutionGroupsNumber; i++) {
            // loops along the groups
            String[] subLevels = resolutionLevels[i].split(";");
            int subLevelsLenght = subLevels.length;
            if (subLevelsLenght > 1) {
                // report we have inner overviews if a ";" has been found
                innerOverviews = true;
            }
            resolutionsSet[i] = new double[subLevelsLenght][];
            for (int k = 0; k < subLevelsLenght; k++) {
                String[] pair = subLevels[k].split(",");
                resolutionsSet[i][k] = new double[2];
                resolutionsSet[i][k][0] = Double.parseDouble(pair[0].trim());
                resolutionsSet[i][k][1] = Double.parseDouble(pair[1].trim());
                numResolutions++;
            }
        }
        // decrease by 1 to exclude native resolution from number of overviews
        numOverviews = numResolutions - 1;
        imageChoiceToReaderLookup = new int[numResolutions];

        // native resolution setting (first group, first level)
        highestResolution = new double[2];
        highestResolution[0] = resolutionsSet[0][0][0];
        highestResolution[1] = resolutionsSet[0][0][1];

        // Map native resolution to first reader
        imageChoiceToReaderLookup[0] = 0;
        overViewResolutions = numOverviews > 0 ? new double[numOverviews][2] : null;
        numResolutions = 0;

        // Mapping overviews
        for (int i = 0; i < resolutionGroupsNumber; i++) {
            for (int k = i != 0 ? 0 : 1; k < resolutionsSet[i].length; k++) {
                overViewResolutions[numResolutions][0] = resolutionsSet[i][k][0];
                overViewResolutions[numResolutions][1] = resolutionsSet[i][k][1];
                numResolutions++;
                imageChoiceToReaderLookup[numResolutions] = i;
            }
        }
    }

    /** Cache of {@link ImageMosaicReader} objects for the different levels. */
    ConcurrentHashMap<Integer, ImageMosaicReader> readers = new ConcurrentHashMap<>();

    public void dispose() {
        for (Entry<Integer, ImageMosaicReader> element : readers.entrySet()) {
            try {
                element.getValue().dispose();
            } catch (Exception e) {
                // Mimic the underlying imageMosaicReader dispose behavior
                if (LOGGER.isLoggable(Level.FINE)) LOGGER.log(Level.FINE, e.getLocalizedMessage(), e);
            }
        }
        readers.clear();
    }

    protected ImageMosaicReader getReader(Integer imageChoice, String coverageName, URL sourceURL, Hints hints)
            throws IOException {
        int imageIndex = getImageReaderIndex(imageChoice);
        // light check to see if this reader had been disposed, not synching for performance.
        if (readers == null) {
            throw new IllegalStateException("This ImagePyramidReader has already been disposed");
        }
        ImageMosaicReader reader = readers.get(imageIndex);

        if (reader == null) {

            //
            // we must create the underlying mosaic
            //
            final String levelDirName = levelsDirs[imageIndex];
            final URL parentUrl = URLs.getParentUrl(sourceURL);
            // look for a shapefile first
            final String extension = new StringBuilder(levelDirName)
                    .append("/")
                    .append(coverageName)
                    .append(".shp")
                    .toString();
            final URL shpFileUrl = URLs.extendUrl(parentUrl, extension);
            if (shpFileUrl.getProtocol() != null
                    && shpFileUrl.getProtocol().equalsIgnoreCase("file")
                    && !URLs.urlToFile(shpFileUrl).exists()) {
                reader = new ImageMosaicReader(URLs.extendUrl(parentUrl, levelDirName), hints);
            } else {
                reader = new ImageMosaicReader(shpFileUrl, hints);
            }
            final ImageMosaicReader putByOtherThreadJustNow = readers.putIfAbsent(imageIndex, reader);
            if (putByOtherThreadJustNow != null) {
                // some other thread just did inserted this
                try {
                    reader.dispose();
                } catch (Exception e) {
                    if (LOGGER.isLoggable(Level.FINE)) {
                        LOGGER.log(Level.FINE, e.getLocalizedMessage(), e);
                    }
                }

                // use the other one
                reader = putByOtherThreadJustNow;
            }
        }
        return reader;
    }

    public int getImageReaderIndex(Integer imageChoice) {
        return imageChoiceToReaderLookup[imageChoice];
    }

    public int getNumOverviews() {
        return numOverviews;
    }

    public double[][] getOverViewResolutions() {
        return overViewResolutions;
    }

    public double[] getHighestResolution() {
        return highestResolution;
    }

    public String[] getLevelsDirs() {
        return levelsDirs;
    }

    public boolean hasInnerOverviews() {
        return innerOverviews;
    }

    public boolean hasReaders() {
        return readers != null;
    }
}
