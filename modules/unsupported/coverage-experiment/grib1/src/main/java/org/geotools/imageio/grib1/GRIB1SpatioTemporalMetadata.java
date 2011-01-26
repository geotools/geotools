/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2007-2008, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.imageio.grib1;

import it.geosolutions.imageio.ndplugin.BaseImageMetadata;
import it.geosolutions.imageio.plugins.grib1.GRIB1ImageMetadata;
import it.geosolutions.imageio.plugins.grib1.GRIB1Utilities;
import it.geosolutions.imageio.plugins.netcdf.NetCDFUtilities;

import java.io.IOException;
import java.text.ParseException;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.imageio.metadata.IIOMetadata;

import org.geotools.coverage.io.util.Utilities;
import org.geotools.imageio.SpatioTemporalImageReader;
import org.geotools.imageio.metadata.Band;
import org.geotools.imageio.metadata.BoundedBy;
import org.geotools.imageio.metadata.CoordinateReferenceSystem;
import org.geotools.imageio.metadata.Identification;
import org.geotools.imageio.metadata.RectifiedGrid;
import org.geotools.imageio.metadata.SpatioTemporalMetadata;
import org.geotools.imageio.metadata.TemporalCRS;
import org.geotools.imageio.metadata.VerticalCRS;
import org.geotools.imageio.metadata.AbstractCoordinateReferenceSystem.Datum;
import org.geotools.temporal.object.DefaultInstant;
import org.geotools.temporal.object.DefaultPosition;
import org.geotools.util.NumberRange;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;

import ucar.nc2.constants.AxisType;
import ucar.nc2.constants.CF;

/**
 * Class involved in SpatioTemporal Metadata settings.
 * 
 * @author Alessio Fabiani, GeoSolutions
 * @author Daniele Romagnoli, GeoSolutions
 */
public class GRIB1SpatioTemporalMetadata extends SpatioTemporalMetadata {

    protected final static Logger LOGGER = Logger.getLogger(GRIB1SpatioTemporalMetadata.class.toString());

    public GRIB1SpatioTemporalMetadata(SpatioTemporalImageReader reader,int imageIndex) {
        super(reader, imageIndex);

    }

    private Map<String, String> attributesMap = null;

    /**
     * Get the attributes Map from the underlying ImageMetadata.
     * 
     * @param reader
     */
    private Map<String, String> getAttributesMap(SpatioTemporalImageReader reader) {
        if (attributesMap == null) {
            buildAttributesMap(reader);
        }
        return attributesMap;

    }

    /**
     * Initialize a Map of attributes available from the underlying
     * ImageMetadata.
     * 
     * @param reader
     */
    private void buildAttributesMap(SpatioTemporalImageReader reader) {
        attributesMap = new HashMap<String, String>();
        IIOMetadata metadata;
        final int imageIndex = getImageIndex();
        try {
            metadata = reader.getImageMetadata(imageIndex);
            if (metadata instanceof GRIB1ImageMetadata) {
                Node root = metadata.getAsTree(GRIB1ImageMetadata.nativeMetadataFormatName);
                if (root != null) {
                    Node gdsNode = root.getFirstChild();
                    if (gdsNode != null) {
                        final NamedNodeMap attributes = gdsNode.getAttributes();
                        if (attributes != null) {
                            final int numAttributes = attributes.getLength();
                            for (int i = 0; i < numAttributes; i++) {
                                final Node node = attributes.item(i);
                                if (node != null) {
                                    attributesMap.put(node.getNodeName(), node.getNodeValue());
                                }
                            }
                        }
                    }
                    final Node pdsNode = gdsNode.getNextSibling();
                    if (pdsNode != null) {
                        final NamedNodeMap attributes = pdsNode.getAttributes();
                        if (attributes != null) {
                            final int numAttributes = attributes.getLength();
                            for (int i = 0; i < numAttributes; i++) {
                                Node node = attributes.item(i);
                                if (node != null) {
                                    attributesMap.put(node.getNodeName(), node.getNodeValue());
                                }
                            }
                        }
                    }
                    final Node pdsLevelNode = pdsNode.getNextSibling();
                    if (pdsLevelNode != null) {
                        final NamedNodeMap attributes = pdsLevelNode.getAttributes();
                        if (attributes != null) {
                            final int numAttributes = attributes.getLength();
                            for (int i = 0; i < numAttributes; i++) {
                                Node node = attributes.item(i);
                                if (node != null) {
                                    attributesMap.put(node.getNodeName(), node.getNodeValue());
                                }
                            }
                        }
                    }

                }
            }
        } catch (IOException e) {
            throw new IllegalArgumentException("Unable parsing metadata");
        }
    }

    protected void setCoordinateReferenceSystemElement(
            SpatioTemporalImageReader reader) {
        Map<String, String> map = getAttributesMap(reader);
        final String gridTypeS = map.get(GRIB1ImageMetadata.GRID_TYPE);
        String projectionNameS = "";
        CoordinateReferenceSystem crs = getCRS();

        int gridType = 0;
        if (gridTypeS != null)
            gridType = Integer.parseInt(gridTypeS);

        switch (gridType) {
        case 0: // latlon
            crs.setIdentification(new Identification("WGS 84", null, null,"EPSG:4326"));
            crs.setCoordinateSystem(new Identification("WGS 84", null, null,null));
            break;
        case 10: // rotated latlon
            projectionNameS = "RotatedLatLon";
            crs.setIdentification(new Identification("WGS 84", null, null,"EPSG:4326"));
            crs.setCoordinateSystem(new Identification("WGS 84", null, null,null));
            break;
        case 3:
            projectionNameS = "LambertConformalConic";
            // /////////////////////////////////////////////////////////////
            //
            // Lambert Conformal Conic 2 - Standard Parallels
            //
            // /////////////////////////////////////////////////////////////
            final String latin1S = map.get(GRIB1ImageMetadata.GRID_LATIN_1);
            final String latin2S = map.get(GRIB1ImageMetadata.GRID_LATIN_2);
            final String lovS = map.get(GRIB1ImageMetadata.GRID_LOV);
            final String startXs = map.get(GRIB1ImageMetadata.GRID_STARTX);
            final String startYs = map.get(GRIB1ImageMetadata.GRID_STARTY);
            if (latin1S != null && latin2S != null && lovS != null&& startXs != null && startYs != null) {
                final double latin1 = Double.parseDouble(latin1S);
                final double latin2 = Double.parseDouble(latin2S);
                final double lov = Double.parseDouble(lovS);
                final double startX = Double.parseDouble(startXs);
                final double startY = Double.parseDouble(startYs);
                final boolean sp2 = latin1 != latin2;
                projectionNameS = sp2 ? "Lambert_Conformal_Conic_2SP": "Lambert_Conformal_Conic_1SP";

                crs.setDefinedByConversion(new Identification(projectionNameS),
                        null, null, null);
                if (sp2) {
                    crs.addParameterValue(new Identification("standard_parallel_1"), Double.toString(Math.toRadians(latin1)));
                    crs.addParameterValue(new Identification("standard_parallel_2"), Double.toString(Math.toRadians(latin2)));
                    crs.addParameterValue(new Identification("longitude_of_origin"), Double.toString(Math.toRadians(lov)));
                }
                crs.addParameterValue(new Identification("latitude_of_origin"),Double.toString(latin1));
                crs.addParameterValue(new Identification("false_easting"),Double.toString(-startX));
                crs.addParameterValue(new Identification("false_northing"),Double.toString(-startY));

            }

            break;
        }

        // //
        // Datum and Ellipsoid
        // //
        crs.setDatum(Datum.GEODETIC_DATUM, new Identification("WGS_1984","World Geodetic System 1984", null, "EPSG:6326"));
        crs.addPrimeMeridian("0.0", new Identification("Greenwich", null, null,"EPSG:8901"));
        crs.addEllipsoid("6378137.0", null, "298.257223563", "meter",new Identification("WGS 84", null, null, "EPSG:7030"));

        crs.addAxis(new Identification("longitude"), "east", "degrees", null);
        crs.addAxis(new Identification("latitude"), "north", "degrees", null);

        final String pdsLevelId = map.get(GRIB1ImageMetadata.PDSL_ID);
        final String pdsIsNumeric = map.get(GRIB1ImageMetadata.PDSL_ISNUMERIC);
        final String pdsLevelName = map.get(GRIB1ImageMetadata.PDSL_NAME);
        final String pdsLevelDescription = map.get(GRIB1ImageMetadata.PDSL_DESCRIPTION);
        String pdsUnits = map.get(GRIB1ImageMetadata.PDSL_UNITS);
        if(pdsUnits==null || pdsUnits.length()==0)
        	pdsUnits = "m";
        
        // //
        //
        // Setting Vertical CRS
        //
        // //
        // TODO: Improve this
        if (Integer.parseInt(pdsLevelId) >=0) {
            setHasVerticalCRS(true);
            VerticalCRS vCRS = getVerticalCRS();
            if (pdsLevelId != null) {
                final int levelID = Integer.parseInt(pdsLevelId);
                if ((levelID >= 1 && levelID <= 9)
                        || (levelID >= 204 && levelID <= 254)) {
                    vCRS.setIdentification(new Identification(pdsLevelDescription,null,null,null));
                    vCRS.setDatum(new Identification(pdsLevelName, null, null, null));
                    vCRS.addVerticalDatumType("other_surface");
                    vCRS.addAxis(new Identification(pdsLevelName), "up", "m" ,null);
                } else if (levelID == 100) {
                    vCRS.setDatum(new Identification("Ground", null, null, null));
                    vCRS.setIdentification(new Identification("Isobaric Pressure", null, null, null));
                    vCRS.addVerticalDatumType("barometric");
                    vCRS.addAxis(new Identification("pressure"), "down", pdsUnits, null);
                } else if (levelID == 101) {
                	vCRS.setDatum(new Identification("Ground", null, null, null));
                	vCRS.setIdentification(new Identification("Layer between two isobaric levels", null, null, null));
                	vCRS.addVerticalDatumType("barometric");
                    vCRS.addAxis(new Identification("pressure"), "down", pdsUnits,null);
                } else if (levelID == 102) {
	                vCRS.setDatum(new Identification("Mean Sea Level", null,null, "EPSG:5100"));
	                vCRS.setIdentification(new Identification("Mean sea level height", null, null, "EPSG:5714"));
	                vCRS.addVerticalDatumType("geoidal");
	                vCRS.addAxis(new Identification("height"), "up", pdsUnits, null);
	            }else if (levelID == 103) {
                    vCRS.setDatum(new Identification("Mean Sea Level", null, null, "EPSG:5100"));
                    vCRS.setIdentification(new Identification("Altitude above MSL", null, null, null));
                    vCRS.addVerticalDatumType("geoidal");
                    vCRS.addAxis(new Identification("height"), "up", pdsUnits, null);
                } else if (levelID == 105) {
                    vCRS.setDatum(new Identification("Ground", null, null, null));
                    vCRS.setIdentification(new Identification("Height level above ground", null, null, null));
                    vCRS.addVerticalDatumType("other_surface");
                    vCRS.addAxis(new Identification("height"), "up", pdsUnits, null);
                } else if (levelID == 106) {
                    vCRS.setDatum(new Identification("Ground", null, null, null));
                    vCRS.setIdentification(new Identification("Layer between 2 specified height level above ground", null, null, null));
                    vCRS.addVerticalDatumType("geoidal");
                    vCRS.addAxis(new Identification("height"), "up", pdsUnits, null);
                } else if (levelID == 109) {
                    vCRS.setDatum(new Identification("Ground", null, null, null));
                    vCRS.setIdentification(new Identification(pdsLevelName, null, null, null));
                    vCRS.addVerticalDatumType("other_surface");
                    vCRS.addAxis(new Identification("level"), "down", "m", null);
                } else if (levelID == 112) {
                	vCRS.setDatum(new Identification("Ground", null, null, null));
                	vCRS.setIdentification(new Identification("Layer between two depths below surface", null, null, null));
                	vCRS.addVerticalDatumType("other_surface");
                    vCRS.addAxis(new Identification("depth"), "up", pdsUnits,null);
                } else if (levelID == 116) {
                	vCRS.setDatum(new Identification("Ground", null, null, null));
                	vCRS.setIdentification(new Identification("Layer between two pressure difference from ground", null, null, null));
                	vCRS.addVerticalDatumType("barometric");
                    vCRS.addAxis(new Identification("pressure"), "down", pdsUnits,null);
                }else if (levelID == 160) {
                    vCRS.setDatum(new Identification("Mean Sea Level", null, null, "EPSG:5100"));
                    vCRS.addVerticalDatumType("depth");
                    vCRS.setIdentification(new Identification("Mean sea level depth", null, null, "EPSG:5715"));
                    vCRS.addAxis(new Identification("depth"), "down", pdsUnits, null);
                }
                //TODO: XXX TEMP SOLUTION: ADD More VERTICAL CRSs
                else{
                	 vCRS.setIdentification(new Identification(pdsLevelDescription,null,null,null));
                     vCRS.setDatum(new Identification(pdsLevelName, null, null, null));
                     vCRS.addVerticalDatumType("other_surface");
                     vCRS.addAxis(new Identification(pdsLevelName), "up", pdsUnits ,null);
                }
                if (Boolean.parseBoolean(pdsIsNumeric)) {

                }
            }
            // TODO: Add more cases

        }

        // //
        //
        // Setting Temporal CRS
        //
        // //
        setHasTemporalCRS(true);
        TemporalCRS tCRS = getTemporalCRS();
        
        tCRS.setDatum(new Identification("ISO8601", null, null, null));
        String timeUnits = map.get(GRIB1ImageMetadata.PROD_TIME_UNITS);
        String timeName = map.get(GRIB1ImageMetadata.PROD_TIME_NAME);
        tCRS.addAxis(new Identification(timeName),CF.POSITIVE_UP, timeUnits, null);
        
        String timeOrigin = null;
        final String[] unitsParts = timeUnits.split("(?i)\\s+since\\s+");
        if (unitsParts.length == 2) {
        	timeUnits = unitsParts[0].trim();
        	timeOrigin = unitsParts[1].trim();
        	if (timeOrigin != null) {
            	timeOrigin = NetCDFUtilities.trimFractionalPart(timeOrigin);
                try {
					Date epoch = (Date) NetCDFUtilities.getAxisFormat(AxisType.Time,
                    		timeOrigin).parseObject(timeOrigin);
                        GregorianCalendar cal = new GregorianCalendar();
                        cal.setTime(epoch);
                        DefaultInstant instant = new DefaultInstant(new DefaultPosition(cal.getTime()));
                        final String originDate = instant.getPosition().getDateTime().toString();
                        // TODO: Check this toString method
                        tCRS.addOrigin(originDate);
                } catch (ParseException e) {
                    throw new IllegalArgumentException(e);
                    // TODO: Change the handle this exception
                }
        	}
        }
    }

    @Override
    /**
     * TODO: HANDLE PDS LEVEL
     */
    protected void setRectifiedGridElement(SpatioTemporalImageReader reader) {
        final RectifiedGrid rg = getRectifiedGrid();
        Map<String, String> map = getAttributesMap(reader);
        final int imageIndex = getImageIndex();
        try {
            final int width = reader.getWidth(imageIndex);
            final int height = reader.getHeight(imageIndex);
            final String gridTypeS = map.get(GRIB1ImageMetadata.GRID_TYPE);
            int gridType = 0;
            if (gridTypeS != null)
                gridType = Integer.parseInt(gridTypeS);
            final String deltaX = map.get(GRIB1ImageMetadata.GRID_DELTA_X);
            final String deltaY = map.get(GRIB1ImageMetadata.GRID_DELTA_Y);
            final String startX = map.get(GRIB1ImageMetadata.GRID_LON_1);
            final String startY = map.get(GRIB1ImageMetadata.GRID_LAT_1);

            if (deltaX != null && deltaY != null && startX != null && startY != null) {
                rg.setLow(new int[] { 0, 0 });
                rg.setHigh(new int[] { width, height });

                // Handle More Grid Types

                // //
                //
                // Setting GridOrigin Point
                //
                // //
                switch (gridType) {
                case 10:
                    // TODO: Handle this
                case 0:
                    rg.setCoordinates(new double[] {Double.parseDouble(startX), Double.parseDouble(startY) });
                    rg.addOffsetVector(new double[] {Double.parseDouble(deltaX), 0d });
                    rg.addOffsetVector(new double[] { 0d, Double.parseDouble(deltaY) });
                    // TODO: Check this.
                    rg.addAxisName("Long");
                    rg.addAxisName("Lat");
                    break;
                case 3:
                }

                // TODO: Handle axis
                // for (String axisName : axesNames) {
                // rectifiedGrid.addAxisName(axisName);
                // }

            }
        } catch (IOException e) {
            throw new IllegalArgumentException("Unable parsing metadata");
        }
    }

    @Override
    protected void setBandsElement(SpatioTemporalImageReader reader) {
        GRIB1SpatioTemporalImageReader grib1Reader = ((GRIB1SpatioTemporalImageReader) reader);
        final int imageIndex = getImageIndex();
        Band band = addBand();

        try {
            IIOMetadata metadata = grib1Reader.getImageMetadata(imageIndex);
            if (metadata instanceof BaseImageMetadata) {
                final BaseImageMetadata commonMetadata = (BaseImageMetadata) metadata;
                setBandFromCommonMetadata(band, commonMetadata);
                Node node = commonMetadata.getAsTree(GRIB1ImageMetadata.nativeMetadataFormatName);
                node = node.getFirstChild();
                if (node != null) {
                    node = node.getNextSibling();
                    if (node != null) {
                        final NamedNodeMap attributesMap = node.getAttributes();
                        if (attributesMap != null) {
                            Node units = attributesMap.getNamedItem(GRIB1ImageMetadata.PROD_PARAMETER_UNIT);
                            if (units != null) {
                                String unit = units.getNodeValue();
                                if (unit != null) {
                                    band.setUoM(unit);
                                }
                            }
                        }
                    }
                }
            }

        } catch (IOException e) {
            if (LOGGER.isLoggable(Level.WARNING))
                LOGGER.warning("Unable to set band metadata");
        }
    }

    @Override
    protected void setBoundedByElement(SpatioTemporalImageReader reader) {
        Map<String, String> map = getAttributesMap(reader);
        BoundedBy bb = getBoundedBy();
        if (bb == null)
            throw new IllegalArgumentException(
                    "Provided BoundedBy element is null");

        final String typeS = map.get(GRIB1ImageMetadata.GRID_TYPE);
        // TODO: Add more check on grid types

        // ////////////////////////////////////////////////////////////////////
        //
        // Setting Envelope
        // 
        // ////////////////////////////////////////////////////////////////////
        if (typeS != null) {
            final int type = Integer.parseInt(typeS);
            final String lat1s = map.get(GRIB1ImageMetadata.GRID_LAT_1);
            final String lat2s = map.get(GRIB1ImageMetadata.GRID_LAT_2);
            final String lon1s = map.get(GRIB1ImageMetadata.GRID_LON_1);
            final String lon2s = map.get(GRIB1ImageMetadata.GRID_LON_2);
            if (lat1s != null && lat2s != null && lon1s != null
                    && lon2s != null) {
                double[] lc = new double[] { Double.parseDouble(lon1s),
                        Double.parseDouble(lat1s) };
                double[] uc = new double[] { Double.parseDouble(lon2s),
                        Double.parseDouble(lat2s) };

                switch (type) {
                case 10:
                    final String deltaX = map
                            .get(GRIB1ImageMetadata.GRID_DELTA_X);
                    final String deltaY = map
                            .get(GRIB1ImageMetadata.GRID_DELTA_Y);
                    final String latSP = map
                            .get(GRIB1ImageMetadata.GRID_LAT_SP);
                    final String lonSP = map
                            .get(GRIB1ImageMetadata.GRID_LON_SP);
                    final String nX = map.get(GRIB1ImageMetadata.GRID_N_X);
                    final String nY = map.get(GRIB1ImageMetadata.GRID_N_Y);
                    if (deltaX != null && deltaY != null && lonSP != null
                            && latSP != null && nY != null && nX != null) {
                        final double lonSp = Double.parseDouble(lonSP);
                        final double latSp = Double.parseDouble(latSP);
                        final double dX = Double.parseDouble(deltaX);
                        final double dY = Double.parseDouble(deltaY);
                        final double x = Double.parseDouble(nX);
                        final double y = Double.parseDouble(nY);

                        uc = getGridCoords(x * dX, y * dY, lc[0], lc[1], lonSp,
                                latSp);
                        lc = getGridCoords(0, 0, lc[0], lc[1], lonSp, latSp);

                    }
                    // TODO: Fix this
                case 0:
                    if (lc[0] > uc[0]) {
                        final double temp = lc[0];
                        lc[0] = uc[0];
                        uc[0] = temp;
                    }
                    if (lc[1] > uc[1]) {
                        final double temp = lc[1];
                        lc[1] = uc[1];
                        uc[1] = temp;
                    }
                }
                bb.setLowerCorner(lc);
                bb.setUpperCorner(uc);
            }
        }
        if (isHasVerticalCRS()) {
            final String pdsValues = map.get(GRIB1ImageMetadata.PDSL_VALUES);
            final String isNumeric = map.get(GRIB1ImageMetadata.PDSL_ISNUMERIC);
            if (isNumeric != null && Boolean.parseBoolean(isNumeric)) {
                final String[] values = pdsValues
                        .split(GRIB1Utilities.VALUES_SEPARATOR);
                if (values != null) {
                    if (values.length == 1)
                        bb.setVerticalExtent(Double.parseDouble(values[0]));
                    else if (values.length == 2) {
                        final double d1 = Double.parseDouble(values[0]);
                        final double d2 = Double.parseDouble(values[1]);
                        if (d1 != d2)
                            bb.setVerticalExtent(NumberRange.create(d1, d2));
                        else
                            bb.setVerticalExtent(d1);
                    } else {
                        throw new IllegalArgumentException(
                                "Unable to set a proper Vertical Extent");
                    }
                }
            } else {
                bb.setVerticalExtent(0);
            }
        }

        if (isHasTemporalCRS()) {
            // //
            //
            // Setting temporal Extent
            //
            // //
            final String time = map.get(GRIB1ImageMetadata.PROD_TIME);
            if (Utilities.ensureValidString(time)) {
//                if (time.contains(GRIB1Utilities.DATE_SEPARATOR)) {
//                    String beginEnd[] = time
//                            .split(GRIB1Utilities.DATE_SEPARATOR);
//                    bb.setTemporalExtent(beginEnd);
//                } else
//                    bb.setTemporalExtent(time);
            }

        }
    }

    /**
     * Get grid coordinates in longitude/latitude pairs Longitude is returned in
     * the range +/- 180 degrees
     * 
     * @returns longitide/latituide as doubles
     */
    public double[] getGridCoords(final double x, final double y,
            final double lon1, final double lat1, final double lonSp,
            final double latSp) {
        double[] coords = new double[2];

        double longi = lon1 + x;
        double lati = lat1 + y;

        // move x-coordinates to the range -180..180
        if (longi >= 180.0) {
            longi = longi - 360.0;
        }

        if (longi < -180.0) {
            longi = longi + 360.0;
        }

        if ((lati > 90.0) || (lati < -90.0)) {
            throw new IllegalArgumentException("GribGDSRotatedLatLon.getGridCoords: latitude out of range (-90 to 90).");
        }

        double[] real_lonLat = rtll(longi, lati, lonSp, latSp);
        coords[0] = real_lonLat[0];
        coords[1] = real_lonLat[1];
        return coords;
    }

    private double[] rtll(double lon, double lat, double lonSp, double latSp) {
        double[] all = new double[2];

        double dtr = Math.PI / 180.;

        double pole_lat = 90.0 + latSp;
        double pole_lon = lonSp;

        double ctph0 = Math.cos(pole_lat * dtr);
        double stph0 = Math.sin(pole_lat * dtr);

        double stph = Math.sin(lat * dtr);
        double ctph = Math.cos(lat * dtr);
        double ctlm = Math.cos(lon * dtr);
        double stlm = Math.sin(lon * dtr);

        // aph=asin(stph0.*ctph.*ctlm+ctph0.*stph);
        double aph = Math.asin((stph0 * ctph * ctlm) + (ctph0 * stph));

        // cph=cos(aph);
        double cph = Math.cos(aph);

        // almd=tlm0d+asin(stlm.*ctph./cph)/dtr;
        all[0] = pole_lon + (Math.asin((stlm * ctph) / cph) / dtr);
        // aphd=aph/dtr;
        all[1] = aph / dtr;

        return all;
    }
}
