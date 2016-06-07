/*
 * GeoTools - The Open Source Java GIS Toolkit
 * http://geotools.org
 *
 * (C) 2016, Open Source Geospatial Foundation (OSGeo)
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation;
 * version 2.1 of the License.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 */

package org.geotools.dem;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Logger;

import org.apache.commons.io.FilenameUtils;
import org.geotools.feature.simple.SimpleFeatureTypeBuilder;
import org.geotools.gce.imagemosaic.CatalogManagerImpl;
import org.geotools.gce.imagemosaic.Utils;
import org.geotools.gce.imagemosaic.catalog.index.Indexer;
import org.geotools.gce.imagemosaic.catalogbuilder.CatalogBuilderConfiguration;
import org.geotools.process.raster.mask.OutliersMaskProcess;
import org.geotools.util.logging.Logging;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.referencing.crs.CoordinateReferenceSystem;

import com.vividsolutions.jts.geom.Polygon;

/**
 * Catalog manager used by the DEM format
 */
public class DEMCatalogManager extends CatalogManagerImpl {

    private static final Logger LOGGER = Logging.getLogger(DEMFormat.class);
    private static final String MASK_TIFF = ".mask.tiff";
    private OutliersMaskProcess outliersProcess = new OutliersMaskProcess();

    @Override
    public SimpleFeatureType createDefaultSchema(CatalogBuilderConfiguration runConfiguration,
            String name, CoordinateReferenceSystem actualCRS) {
        final SimpleFeatureTypeBuilder featureBuilder = new SimpleFeatureTypeBuilder();
        featureBuilder.setName(runConfiguration.getParameter(Utils.Prop.INDEX_NAME));
        featureBuilder.setNamespaceURI("http://boundlessgeo.com//");
        featureBuilder.add(runConfiguration.getParameter(Utils.Prop.LOCATION_ATTRIBUTE).trim(),
                String.class);
        featureBuilder.add("the_geom", Polygon.class, actualCRS);
        featureBuilder.setDefaultGeometry("the_geom");
        addAttributes("date", featureBuilder, Date.class);
        addAttributes("fsDate", featureBuilder, Date.class);
        addAttributes("resolution", featureBuilder, Double.class);
        addAttributes("crs", featureBuilder, String.class);

        return featureBuilder.buildFeatureType();
    }

    @Override
    public List<Indexer.Collectors.Collector> customCollectors() {
        List<Indexer.Collectors.Collector> list = new ArrayList<Indexer.Collectors.Collector>();

        Indexer.Collectors.Collector collectorDate =
                Utils.OBJECT_FACTORY.createIndexerCollectorsCollector();
        collectorDate.setSpi("DateExtractorSPI");
        collectorDate.setMapped("date");
        collectorDate.setValue("");
        list.add(collectorDate);

        Indexer.Collectors.Collector collectorFSDate =
                Utils.OBJECT_FACTORY.createIndexerCollectorsCollector();
        collectorFSDate.setSpi("FSDateExtractorSPI");
        collectorFSDate.setMapped("fsDate");
        collectorFSDate.setValue("");
        list.add(collectorFSDate);

        Indexer.Collectors.Collector collectorX =
                Utils.OBJECT_FACTORY.createIndexerCollectorsCollector();
        collectorX.setSpi("ResolutionExtractorSPI");
        collectorX.setMapped("resolution");
        collectorX.setValue("");
        list.add(collectorX);

        Indexer.Collectors.Collector collectorCrs =
                Utils.OBJECT_FACTORY.createIndexerCollectorsCollector();
        collectorCrs.setSpi("CRSExtractorSPI");
        collectorCrs.setMapped("crs");
        collectorCrs.setValue("");
        list.add(collectorCrs);

        return list;
    }
//
//    @Override
//    protected String prepareLocation(CatalogBuilderConfiguration runConfiguration,
//            final File fileBeingProcessed) throws IOException {
//        return super.prepareLocation(runConfiguration, getMaskedFile(fileBeingProcessed));
//    }

    private File getMaskedFile(File fileBeingProcessed) {
        return new File(fileBeingProcessed.getParent(),
                FilenameUtils.getBaseName(fileBeingProcessed.getName()) + MASK_TIFF);
    }

//    @Override
//    public void updateCatalog(final String coverageName, final File fileBeingProcessed,
//            final GridCoverage2DReader inputReader, final ImageMosaicReader mosaicReader,
//            final CatalogBuilderConfiguration configuration, final GeneralEnvelope envelope,
//            final DefaultTransaction transaction,
//            final List<PropertiesCollector> propertiesCollectors) throws IOException {
//
//        if (!fileBeingProcessed.getName().toLowerCase().endsWith(MASK_TIFF)) {
//            GridCoverage2D coverage = inputReader.read(null);
//            GridCoverage2D maskedCoverage = outliersProcess
//                    .execute(coverage, 0, 10.0, 1000, 1.0, null, OutliersMaskProcess.OutputMethod.AlphaMask, null,
//                            OutliersMaskProcess.StatisticMethod.InterquartileRange);
//            File maskedFile = getMaskedFile(fileBeingProcessed);
//            GeoTiffWriter writer = new GeoTiffWriter(maskedFile);
//            writer.write(maskedCoverage, null);
//            try {
//                Overviews.add(maskedFile, 2, 4, 6, 8, 16);
//            } catch (IOException e) {
//                LOGGER.log(Level.WARNING, "Failed to add overviews to " + maskedFile, e);
//            }
//
//            super.updateCatalog(coverageName, fileBeingProcessed, inputReader, mosaicReader,
//                    configuration, envelope, transaction, propertiesCollectors);
//        } //skip masks
//    }

}
