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
package org.geotools.gce.imagemosaic;

import it.geosolutions.imageio.pam.PAMDataset;
import it.geosolutions.imageio.pam.PAMDataset.PAMRasterBand.GDALRasterAttributeTable;
import it.geosolutions.imageio.pam.PAMParser;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import org.geotools.api.data.ResourceInfo;
import org.geotools.coverage.grid.io.AbstractGridCoverage2DReader;
import org.geotools.coverage.grid.io.GridFormatFinder;
import org.geotools.coverage.grid.io.PAMResourceInfo;
import org.geotools.gce.imagemosaic.ImageMosaicEventHandlers.CompletionEvent;
import org.geotools.gce.imagemosaic.ImageMosaicEventHandlers.FileProcessingEvent;
import org.geotools.gce.imagemosaic.catalogbuilder.CatalogBuilderConfiguration;
import org.geotools.util.logging.Logging;

/** Collects Raster Attribute Tables (RAT) from sources and generates a sigle RAT for the mosaic. */
class RATCollectorListener extends ImageMosaicEventHandlers.ProcessingEventListener {

    static final Logger LOGGER = Logging.getLogger(RATCollectorListener.class);
    private final File pamFile;

    private List<RATCollector> collectors = new ArrayList<>();

    PAMDataset first = null;

    boolean stopCollection = false;

    public RATCollectorListener(CatalogBuilderConfiguration configuration) {
        String root = configuration.getParameter(Utils.Prop.ROOT_MOSAIC_DIR);
        String name = configuration.getParameter(Utils.Prop.INDEX_NAME);
        this.pamFile = new File(root, name + ".aux.xml");
    }

    public RATCollectorListener(File pamFile) {
        this.pamFile = pamFile;
    }

    @Override
    public void getNotification(ImageMosaicEventHandlers.ProcessingEvent event) {
        if (stopCollection) return;
        if (event instanceof FileProcessingEvent) {
            // grab the file and check if it exists
            FileProcessingEvent fileEvent = (FileProcessingEvent) event;
            // skip non ingested files
            if (!fileEvent.isIngested()) return;
            File file = fileEvent.getFile();
            collectRAT(file);
        } else if (event instanceof CompletionEvent) {
            generateMosaicRAT();
        }
    }

    /**
     * Collects a specific RAT from a file.
     *
     * @param file
     */
    public void collectRAT(File file) {
        File pamFile = new File(file.getParent(), file.getName() + ".aux.xml");
        if (pamFile.exists() && pamFile.isFile() && pamFile.canRead()) {
            collectExternalRAT(pamFile);
        } else {
            collectInternalRAT(file);
        }
    }

    /** Generates the mosaic RAT from the collected RATs. */
    public void generateMosaicRAT() {
        if (stopCollection) return;
        for (int band = 0; band < collectors.size(); band++) {
            RATCollector collector = collectors.get(band);
            if (collector != null) collector.replaceRows(first, band);
        }

        try {
            JAXBContext ctx = JAXBContext.newInstance("it.geosolutions.imageio.pam");
            Marshaller marshaller = ctx.createMarshaller();
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
            marshaller.marshal(first, pamFile);
        } catch (JAXBException e) {
            LOGGER.log(Level.SEVERE, "Failed to write the final PAM file", e);
            throw new RuntimeException(e);
        }
    }

    private void collectExternalRAT(File pamFile) {
        try {
            PAMParser parser = new PAMParser();
            PAMDataset pam = parser.parsePAM(pamFile);
            collectRAT(pam);
        } catch (IOException e) {
            LOGGER.log(Level.WARNING, "Error parsing PAM file", e);
        }
    }

    private void collectInternalRAT(File file) {
        AbstractGridCoverage2DReader reader = Optional.ofNullable(
                        GridFormatFinder.findFormat(file, Utils.EXCLUDE_MOSAIC_HINTS))
                .map(f -> f.getReader(file))
                .orElse(null);
        if (reader != null) {
            try {
                ResourceInfo info = reader.getInfo(reader.getGridCoverageNames()[0]);
                if (info instanceof PAMResourceInfo) {
                    PAMResourceInfo pamInfo = (PAMResourceInfo) info;
                    PAMDataset pam = pamInfo.getPAMDataset();
                    collectRAT(pam);
                }
            } finally {
                reader.dispose();
            }
        }
    }

    private void collectRAT(PAMDataset pam) {
        if (pam == null
                || pam.getPAMRasterBand() == null
                || pam.getPAMRasterBand().isEmpty()) return;

        List<PAMDataset.PAMRasterBand> bands = pam.getPAMRasterBand();
        if (first == null) {
            first = pam;
            for (int i = 0; i < bands.size(); i++) {
                GDALRasterAttributeTable rat = bands.get(i).getGdalRasterAttributeTable();
                if (rat == null) collectors.add(null);
                else collectors.add(new RATCollector(i, rat));
            }
        } else if (!checkCompatible(first, pam)) {
            stopCollection = true;
            return;
        } else {
            for (int i = 0; i < bands.size(); i++) {
                GDALRasterAttributeTable rat = bands.get(i).getGdalRasterAttributeTable();
                if (rat == null) continue;
                collectors.get(i).collect(rat);
            }
        }
    }

    /**
     * Checks the two PAM have the same number of bands, the same raster attribute tables, and the
     * same fields
     */
    private boolean checkCompatible(PAMDataset first, PAMDataset second) {
        List<PAMDataset.PAMRasterBand> bands1 = first.getPAMRasterBand();
        List<PAMDataset.PAMRasterBand> bands2 = second.getPAMRasterBand();
        if (bands1.size() != bands2.size()) {
            LOGGER.warning("Stopping PAM collection, different number of bands found in PAM files");
            return false;
        }

        for (int i = 0; i < bands1.size(); i++) {
            PAMDataset.PAMRasterBand band1 = bands1.get(i);
            PAMDataset.PAMRasterBand band2 = bands2.get(i);

            GDALRasterAttributeTable rat1 = band1.getGdalRasterAttributeTable();
            GDALRasterAttributeTable rat2 = band2.getGdalRasterAttributeTable();
            // not all bands might have a RAT
            if (rat1 == null && rat2 == null) continue;
            if (rat1 == null || rat2 == null) {
                LOGGER.warning("Stopping PAM collection, RATs found in some files but not in others for band " + i);
                return false;
            }

            // check field compatibility
            List<PAMDataset.PAMRasterBand.FieldDefn> fields1 = rat1.getFieldDefn();
            List<PAMDataset.PAMRasterBand.FieldDefn> fields2 = rat2.getFieldDefn();
            if (!Objects.equals(fields1, fields2)) {
                LOGGER.warning("Stopping PAM collection, different field definitions found in PAM files");
                return false;
            }
        }

        return true;
    }

    @Override
    public void exceptionOccurred(ImageMosaicEventHandlers.ExceptionEvent event) {}
}
