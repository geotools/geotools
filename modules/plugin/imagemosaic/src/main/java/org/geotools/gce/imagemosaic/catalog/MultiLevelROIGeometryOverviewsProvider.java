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
package org.geotools.gce.imagemosaic.catalog;

import it.geosolutions.imageio.maskband.DatasetLayout;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.io.FilenameUtils;
import org.geotools.coverage.grid.io.AbstractGridCoverage2DReader;
import org.geotools.coverage.grid.io.AbstractGridFormat;
import org.geotools.coverage.grid.io.GridFormatFinder;
import org.geotools.coverage.grid.io.footprint.FootprintLoader;
import org.geotools.coverage.grid.io.footprint.MultiLevelROI;
import org.geotools.coverage.grid.io.footprint.MultiLevelROIProvider;
import org.geotools.coverage.grid.io.footprint.SidecarFootprintProvider;
import org.geotools.factory.Hints;
import org.geotools.gce.imagemosaic.Utils;
import org.locationtech.jts.geom.Geometry;
import org.opengis.feature.simple.SimpleFeature;

/**
 * A {@link MultiLevelROIProvider} implementation used for returning {@link
 * MultiLevelROIGeometryOverviews}s
 */
public class MultiLevelROIGeometryOverviewsProvider implements MultiLevelROIProvider {

    public static final int LOOK_FOR_OVERVIEWS = Integer.MIN_VALUE;

    public static final String OVERVIEWS_SUFFIX_FORMAT_KEY = "overviewsSuffixFormat";

    public static final String DEFAULT_OVERVIEWS_SUFFIX_FORMAT = "_%d";

    public static final boolean DEFAULT_OVERVIEWS_ROI_IN_RASTER_SPACE = false;

    public static final String OVERVIEWS_ROI_IN_RASTER_SPACE_KEY = "overviewsRoiInRasterSpace";

    public static final String FOOTPRINT_LOADER_SPI = "footprintLoaderSPI";

    public static final String OVERVIEWS_FOOTPRINT_LOADER_SPI = "overviewsFootprintLoaderSPI";

    /**
     * The String format syntax used to setup the overviews file name suffix. As an instance, -%d
     * means that a file named fileR1C1 will have its first overview stored as fileR1C1-1
     */
    private String overviewSuffixFormat;

    private File baseFile;

    /**
     * The known number of overviews available. When set to LOOK_FOR_OVERVIEWS, the provider will
     * use a Reader to retrieve the number of overviews
     */
    private int numOverviews;

    /** Footprint loader instance to load the original footprint */
    private FootprintLoader footprintLoader;

    /** Footprint loader instance to load the Overviews footprints */
    private FootprintLoader overviewsFootprintLoader;

    private SidecarFootprintProvider footprintProvider;

    private Hints hints;

    /**
     * Flag specifying whether overview's ROI are expressed in raster space coordinates (True) or
     * model space coordinates (False)
     */
    private boolean overviewsRoiInRasterSpace;

    public MultiLevelROIGeometryOverviewsProvider(
            File baseFile,
            String overviewSuffixFormat,
            int numOverviews,
            FootprintLoader footprintLoader,
            FootprintLoader overviewsFootprintLoader,
            boolean overviewsRoiInRasterSpace,
            Hints hints) {
        this.overviewSuffixFormat = overviewSuffixFormat;
        this.baseFile = baseFile;
        this.footprintLoader = footprintLoader;
        this.overviewsFootprintLoader = overviewsFootprintLoader;
        this.numOverviews = numOverviews;
        this.overviewsRoiInRasterSpace = overviewsRoiInRasterSpace;
        this.hints = hints;
        if (footprintLoader == null) {
            // When footprintLoader is not specified, fallback through
            // sidecarFootprintProvider based on SPI scan
            footprintProvider = new SidecarFootprintProvider(baseFile);
        }
    }

    @Override
    public MultiLevelROI getMultiScaleROI(SimpleFeature sf) throws IOException {
        String path = null;
        // Prepare the path, extracting it from the feature (when provided)
        // or taking it from the baseFile
        if (sf == null) {
            path = baseFile.getAbsolutePath();
        } else {
            Object value = sf.getAttribute("location");
            if (value != null && value instanceof String) {
                String strValue = (String) value;
                File file = Utils.getFile(strValue, baseFile);
                path = file.getAbsolutePath();
            }
        }
        final String baseName = FilenameUtils.getBaseName(path);
        final String fullPath = FilenameUtils.getFullPath(path);
        final String baseFullName = fullPath + File.separatorChar + baseName;
        Geometry footprint;
        List<Geometry> footprintOverviews;
        AbstractGridCoverage2DReader reader = null;
        try {
            footprint = loadFootprint(baseFullName, false);
            int nOverviews = numOverviews;
            if (numOverviews == LOOK_FOR_OVERVIEWS) {
                // No number of overviews have been provided.
                // Getting a reader to retrieve that number.
                nOverviews = 0;
                File file = new File(path);
                AbstractGridFormat format =
                        (AbstractGridFormat)
                                GridFormatFinder.findFormat(file, Utils.EXCLUDE_MOSAIC_HINTS);
                reader = format.getReader(file);
                DatasetLayout layout = reader.getDatasetLayout();
                int extOv = layout.getNumExternalOverviews();
                int intOv = layout.getNumInternalOverviews();
                nOverviews = (extOv > 0 ? extOv : 0) + (intOv > 0 ? intOv : 0);
            }
            footprintOverviews = new ArrayList<Geometry>(nOverviews);
            for (int i = 0; i < nOverviews; i++) {
                // Setting up the path of the overview's footprint file
                String pathOverview = baseFullName + String.format(overviewSuffixFormat, i + 1);
                Geometry overviewFootprint = loadFootprint(pathOverview, true);
                footprintOverviews.add(overviewFootprint);
            }
            return new MultiLevelROIGeometryOverviews(
                    footprint, footprintOverviews, overviewsRoiInRasterSpace, hints);
        } catch (Exception e) {
            throw new IOException("Exception occurred while loading footprints ", e);
        } finally {
            if (reader != null) {
                try {
                    reader.dispose();
                } catch (Throwable t) {
                    // silently ignore exceptions on reader dispose
                }
            }
        }
    }

    private Geometry loadFootprint(String baseFullName, boolean isOverview) throws Exception {
        FootprintLoader loader = isOverview ? footprintLoader : overviewsFootprintLoader;
        if (loader != null) {
            return loader.loadFootprint(baseFullName);
        }
        return footprintProvider.getFootprint(baseFullName);
    }

    @Override
    public void dispose() {}
}
