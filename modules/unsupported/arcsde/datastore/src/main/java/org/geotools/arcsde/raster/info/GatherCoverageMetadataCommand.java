/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2002-2009, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.arcsde.raster.info;

import com.esri.sde.sdk.client.SDEPoint;
import com.esri.sde.sdk.client.SeColumnDefinition;
import com.esri.sde.sdk.client.SeConnection;
import com.esri.sde.sdk.client.SeCoordinateReference;
import com.esri.sde.sdk.client.SeException;
import com.esri.sde.sdk.client.SeExtent;
import com.esri.sde.sdk.client.SeQuery;
import com.esri.sde.sdk.client.SeRaster;
import com.esri.sde.sdk.client.SeRasterAttr;
import com.esri.sde.sdk.client.SeRasterBand;
import com.esri.sde.sdk.client.SeRasterColumn;
import com.esri.sde.sdk.client.SeRow;
import com.esri.sde.sdk.client.SeSqlConstruct;
import com.esri.sde.sdk.client.SeTable;
import java.awt.Point;
import java.awt.image.DataBuffer;
import java.awt.image.DataBufferByte;
import java.awt.image.DataBufferUShort;
import java.awt.image.IndexColorModel;
import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.geotools.arcsde.ArcSdeException;
import org.geotools.arcsde.session.Command;
import org.geotools.arcsde.session.ISession;
import org.geotools.arcsde.util.ArcSDEUtils;
import org.geotools.data.DataSourceException;
import org.geotools.geometry.GeneralEnvelope;
import org.geotools.referencing.crs.DefaultEngineeringCRS;
import org.geotools.util.logging.Logging;
import org.opengis.referencing.crs.CoordinateReferenceSystem;

/**
 * Session command to gather information for an ArcSDE Raster, such as dimensions, spatial extent,
 * number of pyramid levels, etc; into a {@link RasterDatasetInfo}
 *
 * @author Gabriel Roldan (OpenGeo)
 * @since 2.5.8
 */
public class GatherCoverageMetadataCommand extends Command<RasterDatasetInfo> {

    private static final Logger LOGGER = Logging.getLogger(GatherCoverageMetadataCommand.class);

    private final boolean statisticsMandatory;

    private final String rasterTableName;

    public GatherCoverageMetadataCommand(
            final String rasterTableName, final boolean statisticsMandatory) {
        this.rasterTableName = rasterTableName;
        this.statisticsMandatory = statisticsMandatory;
    }

    /**
     * @throws IOException if an exception occurs accessing the raster metadata
     * @throws IllegalArgumentException if the raster has no CRS, contains no raster attributes, has
     *     no pyramids, no bands or no statistics
     */
    @Override
    public RasterDatasetInfo execute(ISession session, SeConnection connection)
            throws SeException, IOException {
        LOGGER.fine("Gathering raster dataset metadata for " + rasterTableName);
        final String[] rasterColumns = getRasterColumns(connection, rasterTableName);
        final List<RasterInfo> rastersLayoutInfo = new ArrayList<RasterInfo>();
        {
            final List<SeRasterAttr> rasterAttributes;
            rasterAttributes = getSeRasterAttr(connection, rasterTableName, rasterColumns);

            if (rasterAttributes.size() == 0) {
                throw new IllegalArgumentException(
                        "Table " + rasterTableName + " contains no raster datasets");
            }

            final CoordinateReferenceSystem coverageCrs;

            /*
             * by bandId map of colormaps. The dataset may be composed of more than one raster so we
             * gather all the colormaps once and held them here by rasterband id
             */
            final Map<Long, IndexColorModel> rastersColorMaps;
            {
                final SeRasterColumn rasterColumn;
                final SeRasterBand sampleBand;
                final long rasterColumnId;
                final int bitsPerSample;
                try {
                    SeRasterAttr ratt = rasterAttributes.get(0);
                    rasterColumn = new SeRasterColumn(connection, ratt.getRasterColumnId());
                    rasterColumnId = rasterColumn.getID().longValue();
                    sampleBand = ratt.getBands()[0];
                    bitsPerSample = RasterCellType.valueOf(ratt.getPixelType()).getBitsPerSample();
                } catch (SeException e) {
                    throw new ArcSdeException(e);
                }
                final SeCoordinateReference seCoordRef = rasterColumn.getCoordRef();
                if (seCoordRef == null) {
                    throw new IllegalArgumentException(
                            rasterTableName + " has no coordinate reference system set");
                }
                LOGGER.finer("Looking CRS for raster column " + rasterTableName);
                coverageCrs = ArcSDEUtils.findCompatibleCRS(seCoordRef);
                if (DefaultEngineeringCRS.CARTESIAN_2D == coverageCrs) {
                    LOGGER.warning(
                            "Raster "
                                    + rasterTableName
                                    + " has not CRS set, using DefaultEngineeringCRS.CARTESIAN_2D");
                }
                if (sampleBand.hasColorMap()) {
                    rastersColorMaps = loadColorMaps(rasterColumnId, bitsPerSample, connection);
                } else {
                    rastersColorMaps = Collections.emptyMap();
                }
            }
            try {
                for (SeRasterAttr rAtt : rasterAttributes) {
                    LOGGER.fine(
                            "Gathering raster metadata for "
                                    + rasterTableName
                                    + " raster "
                                    + rAtt.getRasterId().longValue());

                    if (rAtt.getMaxLevel() == 0) {
                        throw new IllegalArgumentException(
                                "Raster cotains no pyramid levels, we don't support non pyramid rasters");
                    }
                    if (rAtt.getNumBands() == 0) {
                        throw new IllegalArgumentException(
                                "Raster "
                                        + rAtt.getRasterId().longValue()
                                        + " in "
                                        + rasterTableName
                                        + " contains no raster attribtues");
                    }
                    if (this.statisticsMandatory && !rAtt.getBandInfo(1).hasStats()) {
                        throw new IllegalArgumentException(
                                rasterTableName
                                        + " has no statistics generated (or not all it's rasters have). "
                                        + "Please use sderaster -o stats to create them before use");
                    }

                    RasterInfo rasterInfo = new RasterInfo(rAtt, coverageCrs);
                    rastersLayoutInfo.add(rasterInfo);

                    final GeneralEnvelope originalEnvelope;
                    originalEnvelope = calculateOriginalEnvelope(rAtt, coverageCrs);
                    rasterInfo.setOriginalEnvelope(originalEnvelope);
                    final List<RasterBandInfo> bands;
                    bands = setUpBandInfo(connection, rAtt, rastersColorMaps);
                    rasterInfo.setBands(bands);
                    if (LOGGER.isLoggable(Level.FINER)) {
                        LOGGER.finer(
                                "Gathered metadata for "
                                        + rasterTableName
                                        + "#"
                                        + rAtt.getRasterId().longValue()
                                        + ":\n"
                                        + rasterInfo.toString());
                    }

                    // System.out.println(rasterInfo);
                }
            } catch (SeException e) {
                throw new ArcSdeException("Gathering raster dataset information", e);
            }
        }

        RasterDatasetInfo rasterInfo = new RasterDatasetInfo();
        rasterInfo.setRasterTable(rasterTableName);
        rasterInfo.setRasterColumns(rasterColumns);
        rasterInfo.setPyramidInfo(rastersLayoutInfo);

        return rasterInfo;
    }

    private String[] getRasterColumns(final SeConnection scon, final String rasterTable)
            throws IOException, SeException {

        String[] rasterColumns;
        SeTable sTable = new SeTable(scon, rasterTable);
        SeColumnDefinition[] cols;
        try {
            cols = sTable.describe();
        } catch (SeException e) {
            throw new ArcSdeException(
                    "Exception fetching the list of columns for table " + rasterTable, e);
        }
        List<String> fetchColumns = new ArrayList<String>(cols.length / 2);
        for (int i = 0; i < cols.length; i++) {
            if (cols[i].getType() == SeColumnDefinition.TYPE_RASTER)
                fetchColumns.add(cols[i].getName());
        }
        if (fetchColumns.size() == 0) {
            throw new DataSourceException(
                    "Couldn't find any TYPE_RASTER columns in ArcSDE table " + rasterTable);
        }

        rasterColumns = fetchColumns.toArray(new String[fetchColumns.size()]);
        return rasterColumns;
    }

    private GeneralEnvelope calculateOriginalEnvelope(
            final SeRasterAttr rasterAttributes, CoordinateReferenceSystem coverageCrs)
            throws IOException {
        SeExtent sdeExtent;
        try {
            sdeExtent = rasterAttributes.getExtent();
        } catch (SeException e) {
            throw new ArcSdeException("Exception getting the raster extent", e);
        }
        GeneralEnvelope originalEnvelope = new GeneralEnvelope(coverageCrs);
        originalEnvelope.setRange(0, sdeExtent.getMinX(), sdeExtent.getMaxX());
        originalEnvelope.setRange(1, sdeExtent.getMinY(), sdeExtent.getMaxY());
        return originalEnvelope;
    }

    private List<SeRasterAttr> getSeRasterAttr(
            SeConnection scon, String rasterTable, String[] rasterColumns) throws IOException {

        LOGGER.fine("Gathering raster attributes for " + rasterTable);
        SeRasterAttr rasterAttributes;
        LinkedList<SeRasterAttr> rasterAttList = new LinkedList<SeRasterAttr>();
        SeQuery query = null;
        try {
            query = new SeQuery(scon, rasterColumns, new SeSqlConstruct(rasterTable));
            query.prepareQuery();
            query.execute();

            SeRow row = query.fetch();
            while (row != null) {
                rasterAttributes = row.getRaster(0);
                rasterAttList.addFirst(rasterAttributes);
                row = query.fetch();
            }
        } catch (SeException se) {
            throw new ArcSdeException("Error fetching raster attributes for " + rasterTable, se);
        } finally {
            if (query != null) {
                try {
                    query.close();
                } catch (SeException e) {
                    throw new ArcSdeException(e);
                }
            }
        }
        LOGGER.fine("Found " + rasterAttList.size() + " raster attributes for " + rasterTable);
        return rasterAttList;
    }

    /** */
    private Map<Long, IndexColorModel> loadColorMaps(
            final long rasterColumnId, final int bitsPerSample, SeConnection scon)
            throws IOException {
        LOGGER.fine("Reading colormap for raster column " + rasterColumnId);

        final String auxTableName = getAuxTableName(rasterColumnId, scon);
        LOGGER.fine("Quering auxiliary table " + auxTableName + " for color map data");

        Map<Long, IndexColorModel> colorMaps = new HashMap<Long, IndexColorModel>();
        SeQuery query = null;
        try {
            SeSqlConstruct sqlConstruct = new SeSqlConstruct();
            sqlConstruct.setTables(new String[] {auxTableName});
            String whereClause = "TYPE = 3";
            sqlConstruct.setWhere(whereClause);

            query = new SeQuery(scon, new String[] {"RASTERBAND_ID", "OBJECT"}, sqlConstruct);
            query.prepareQuery();
            query.execute();

            long bandId;
            ByteArrayInputStream colorMapIS;
            DataBuffer colorMapData;
            IndexColorModel colorModel;

            SeRow row = query.fetch();
            while (row != null) {
                bandId = ((Number) row.getObject(0)).longValue();
                colorMapIS = row.getBlob(1);

                colorMapData = readColorMap(colorMapIS);
                colorModel = RasterUtils.sdeColorMapToJavaColorModel(colorMapData, bitsPerSample);

                colorMaps.put(Long.valueOf(bandId), colorModel);

                row = query.fetch();
            }
        } catch (SeException e) {
            throw new ArcSdeException(
                    "Error fetching colormap data for column "
                            + rasterColumnId
                            + " from table "
                            + auxTableName,
                    e);
        } finally {
            if (query != null) {
                try {
                    query.close();
                } catch (SeException e) {
                    LOGGER.log(
                            Level.INFO,
                            "ignoring exception when closing query to " + "fetch colormap data",
                            e);
                }
            }
        }
        LOGGER.fine("Read color map data for " + colorMaps.size() + " rasters");
        return colorMaps;
    }

    private DataBuffer readColorMap(final ByteArrayInputStream colorMapIS) throws IOException {

        final DataInputStream dataIn = new DataInputStream(colorMapIS);
        // discard unneeded data
        dataIn.readInt();

        final int colorSpaceType = dataIn.readInt();
        final int numBanks;
        if (colorSpaceType == SeRaster.SE_COLORMAP_RGB) {
            numBanks = 3;
        } else if (colorSpaceType == SeRaster.SE_COLORMAP_RGBA) {
            numBanks = 4;
        } else {
            throw new IllegalStateException("Got unknown colormap type: " + colorSpaceType);
        }
        LOGGER.finest("Colormap has " + numBanks + " color components");

        final int buffType = dataIn.readInt();
        final int numElems = dataIn.readInt();
        LOGGER.finest("ColorMap length: " + numElems);

        final DataBuffer buff;
        if (buffType == SeRaster.SE_COLORMAP_DATA_BYTE) {
            LOGGER.finest(
                    "Creating Byte data buffer for "
                            + numBanks
                            + " banks and "
                            + numElems
                            + " elements per bank");
            buff = new DataBufferByte(numElems, numBanks);
            for (int elem = 0; elem < numElems; elem++) {
                for (int bank = 0; bank < numBanks; bank++) {
                    int val = dataIn.readUnsignedByte();
                    buff.setElem(bank, elem, val);
                }
            }
        } else if (buffType == SeRaster.SE_COLORMAP_DATA_SHORT) {
            LOGGER.finest(
                    "Creating Short data buffer for "
                            + numBanks
                            + " banks and "
                            + numElems
                            + " elements per bank");
            buff = new DataBufferUShort(numElems, numBanks);
            for (int elem = 0; elem < numElems; elem++) {
                for (int bank = 0; bank < numBanks; bank++) {
                    int val = dataIn.readUnsignedShort();
                    buff.setElem(bank, elem, val);
                }
            }
        } else {
            throw new IllegalStateException(
                    "Unknown databuffer type from colormap header: "
                            + buffType
                            + " expected one of TYPE_BYTE, TYPE_SHORT");
        }

        assert dataIn.read() == -1 : "color map data should have been exausted";
        return buff;
    }

    private String getAuxTableName(long rasterColumnId, SeConnection scon) throws IOException {

        final String owner;
        SeQuery query = null;
        try {
            final String dbaName = scon.getSdeDbaName();
            String rastersColumnsTable = dbaName + ".SDE_RASTER_COLUMNS";

            SeSqlConstruct sqlCons = new SeSqlConstruct(rastersColumnsTable);
            sqlCons.setWhere("RASTERCOLUMN_ID = " + rasterColumnId);

            try {
                query = new SeQuery(scon, new String[] {"OWNER"}, sqlCons);
                query.prepareQuery();
            } catch (SeException e) {
                // sde 9.3 calls it raster_columns, not sde_raster_columns...
                rastersColumnsTable = dbaName + ".RASTER_COLUMNS";
                sqlCons = new SeSqlConstruct(rastersColumnsTable);
                sqlCons.setWhere("RASTERCOLUMN_ID = " + rasterColumnId);
                query = new SeQuery(scon, new String[] {"OWNER"}, sqlCons);
                query.prepareQuery();
            }
            query.execute();

            SeRow row = query.fetch();
            if (row == null) {
                throw new IllegalArgumentException(
                        "No raster column registered with id " + rasterColumnId);
            }
            owner = row.getString(0);
            query.close();
        } catch (SeException e) {
            throw new ArcSdeException(
                    "Error getting auxiliary table for raster column " + rasterColumnId, e);
        } finally {
            if (query != null) {
                try {
                    query.close();
                } catch (SeException e) {
                    LOGGER.log(
                            Level.INFO,
                            "ignoring exception when closing query to " + "fetch colormap data",
                            e);
                }
            }
        }

        final String auxTableName = owner + ".SDE_AUX_" + rasterColumnId;

        return auxTableName;
    }

    private List<RasterBandInfo> setUpBandInfo(
            SeConnection scon,
            SeRasterAttr rasterAttributes,
            Map<Long, IndexColorModel> rastersColorMaps)
            throws IOException {
        final int numBands;
        final SeRasterBand[] seBands;
        final RasterCellType cellType;
        try {
            numBands = rasterAttributes.getNumBands();
            seBands = rasterAttributes.getBands();
            cellType = RasterCellType.valueOf(rasterAttributes.getPixelType());
        } catch (SeException e) {
            throw new ArcSdeException(e);
        }

        List<RasterBandInfo> detachedBandInfo = new ArrayList<RasterBandInfo>(numBands);

        RasterBandInfo bandInfo;
        SeRasterBand band;
        for (int bandN = 0; bandN < numBands; bandN++) {
            band = seBands[bandN];
            bandInfo = new RasterBandInfo();
            final int bitsPerSample = cellType.getBitsPerSample();
            setBandInfo(numBands, bandInfo, band, scon, bitsPerSample, rastersColorMaps);
            detachedBandInfo.add(bandInfo);
        }
        return detachedBandInfo;
    }

    /** @param bitsPerSample only used if the band is colormapped to create the IndexColorModel */
    private void setBandInfo(
            final int numBands,
            final RasterBandInfo bandInfo,
            final SeRasterBand band,
            final SeConnection scon,
            int bitsPerSample,
            final Map<Long, IndexColorModel> colorMaps)
            throws IOException {

        bandInfo.bandId = band.getId().longValue();
        bandInfo.bandNumber = band.getBandNumber();
        bandInfo.bandName = "Band " + bandInfo.bandNumber;

        final boolean hasColorMap = band.hasColorMap();
        if (hasColorMap) {
            IndexColorModel colorMap = colorMaps.get(Long.valueOf(bandInfo.bandId));
            LOGGER.finest("Setting band's color map: " + colorMap);
            bandInfo.nativeColorMap = colorMap;
            bandInfo.colorMap = RasterUtils.ensureNoDataPixelIsAvailable(colorMap);
        } else {
            bandInfo.nativeColorMap = null;
        }

        bandInfo.compressionType = CompressionType.valueOf(band.getCompressionType());
        bandInfo.cellType = RasterCellType.valueOf(band.getPixelType());
        bandInfo.interleaveType = InterleaveType.valueOf(band.getInterleave());
        bandInfo.interpolationType = InterpolationType.valueOf(band.getInterpolation());
        bandInfo.hasStats = band.hasStats();
        if (bandInfo.hasStats) {
            try {
                bandInfo.statsMin = band.getStatsMin();
                bandInfo.statsMax = band.getStatsMax();
                bandInfo.statsMean = band.getStatsMean();
                bandInfo.statsStdDev = band.getStatsStdDev();
            } catch (SeException e) {
                throw new ArcSdeException(e);
            }
            // double noDataValue = 0;
            // bandInfo.noDataValue = noDataValue;
        } else {
            bandInfo.statsMin = java.lang.Double.NaN;
            bandInfo.statsMax = java.lang.Double.NaN;
            bandInfo.statsMean = java.lang.Double.NaN;
            bandInfo.statsStdDev = java.lang.Double.NaN;
        }
        if (bandInfo.getColorMap() != null) {
            bandInfo.noDataValue = RasterUtils.determineNoDataValue(bandInfo.getColorMap());
        } else {
            double statsMin = bandInfo.getStatsMin();
            double statsMax = bandInfo.getStatsMax();
            RasterCellType nativeCellType = bandInfo.getCellType();
            bandInfo.noDataValue =
                    RasterUtils.determineNoDataValue(numBands, statsMin, statsMax, nativeCellType);
        }
        SDEPoint tOrigin;
        try {
            tOrigin = band.getTileOrigin();
        } catch (SeException e) {
            throw new ArcSdeException(e);
        }
        bandInfo.tileOrigin = new Point((int) tOrigin.getX(), (int) tOrigin.getY());
    }
}
