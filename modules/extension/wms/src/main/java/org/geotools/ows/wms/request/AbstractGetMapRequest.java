/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2004-2016, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.ows.wms.request;

import java.awt.Dimension;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ListIterator;
import java.util.Properties;
import java.util.Stack;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.geotools.ows.wms.Layer;
import org.geotools.ows.wms.StyleImpl;
import org.geotools.referencing.CRS;
import org.geotools.referencing.CRS.AxisOrder;
import org.geotools.referencing.crs.DefaultEngineeringCRS;
import org.geotools.util.factory.GeoTools;
import org.geotools.util.factory.Hints;
import org.geotools.util.logging.Logging;
import org.opengis.geometry.Envelope;
import org.opengis.referencing.FactoryException;
import org.opengis.referencing.NoSuchAuthorityCodeException;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.opengis.referencing.operation.TransformException;

/** @author Richard Gould */
public abstract class AbstractGetMapRequest extends AbstractWMSRequest implements GetMapRequest {

    Stack layers = new Stack();
    Stack styles = new Stack();
    static final Logger LOGGER = Logging.getLogger(AbstractGetMapRequest.class);

    /**
     * Constructs a GetMapRequest. The data passed in represents valid values that can be used.
     *
     * @param onlineResource the location that the request should be applied to
     * @param properties pre-set properties to be used. Can be null.
     */
    public AbstractGetMapRequest(URL onlineResource, Properties properties) {
        super(onlineResource, properties);
    }

    public URL getFinalURL() {
        if (!layers.isEmpty()) {
            String layerString = ""; // $NON-NLS-1$
            String styleString = ""; // $NON-NLS-1$

            ListIterator layerIter = layers.listIterator(layers.size());
            ListIterator styleIter = styles.listIterator(styles.size());
            while (layerIter.hasPrevious()) {

                String layerName = (String) layerIter.previous();
                String styleName = (String) styleIter.previous();

                try {
                    // spaces are converted to plus signs, but must be %20 for url calls [GEOT-4317]
                    layerString =
                            layerString
                                    + URLEncoder.encode(layerName, "UTF-8")
                                            .replaceAll("\\+", "%20");
                } catch (UnsupportedEncodingException | NullPointerException e) {
                    layerString = layerString + layerName;
                }
                styleName = styleName == null ? "" : styleName;
                try {

                    styleString =
                            styleString
                                    + URLEncoder.encode(styleName, "UTF-8")
                                            .replaceAll("\\+", "%20");
                } catch (UnsupportedEncodingException | NullPointerException e1) {
                    styleString = styleString + styleName;
                }

                if (layerIter.hasPrevious()) {
                    layerString = layerString + ","; // $NON-NLS-1$
                    styleString = styleString + ","; // $NON-NLS-1$
                }
            }

            setProperty(LAYERS, layerString);
            setProperty(STYLES, styleString);
        }

        return super.getFinalURL();
    }

    protected abstract void initVersion();

    protected void initRequest() {
        setProperty(REQUEST, "GetMap"); // $NON-NLS-1$
    }

    /**
     * Sets the version number of the request.
     *
     * @param version A String indicting a WMS Version ("1.0.0", "1.1.0", "1.1.1", or "1.3.0")
     */
    public void setVersion(String version) {
        properties.setProperty(VERSION, version);
    }

    public void addLayer(Layer layer, String style) {
        addLayer(layer.getName(), style);
    }

    public void addLayer(Layer layer) {
        addLayer(layer, "");
    }

    public void addLayer(String layerName, String style) {
        layers.push(layerName);
        if (style == null) {
            style = ""; // $NON-NLS-1$
        }
        styles.push(style);
    }

    public void addLayer(Layer layer, StyleImpl style) {
        if (style == null) {
            addLayer(layer.getName(), "");
            return;
        }
        addLayer(layer.getName(), style.getName());
    }

    public void addLayer(String layerName, StyleImpl style) {
        if (style == null) {
            addLayer(layerName, "");
            return;
        }
        addLayer(layerName, style.getName());
    }

    /**
     * From the Web Map Service Implementation Specification: "The required SRS parameter states
     * which Spatial Reference System applies to the values in the BBOX parameter. The value of the
     * SRS parameter shall be on of the values defined in the character data section of an &lt;SRS>
     * element defined or inherited by the requested layer. The same SRS applies to all layers in a
     * single request. If the WMS has declared SRS=NONE for a Layer, then the Layer does not have a
     * well-defined spatial reference system and should not be shown in conjunction with other
     * layers. The client shall specify SRS as "none" in the GetMap request and the Server may issue
     * a Service Exception otherwise."
     *
     * @param srs A String indicating the Spatial Reference System to render the layers in.
     */
    public void setSRS(String srs) {
        properties.setProperty(SRS, srs);
    }

    /**
     * From the Web Map Service Implementation Specification: "The required BBOX parameter allows a
     * Client to request a particular Bounding Box. The value of the BBOX parameter in a GetMap
     * request is a list of comma-separated numbers of the form "minx,miny,maxx,maxy". If the WMS
     * server has declared that a Layer is not subsettable, then the Client shall specify exactly
     * the declared Bounding Box values in the GetMap request and the Server may issue a Service
     * Exception otherwise."
     *
     * <p>Yu must also call setSRS to provide the spatial reference system information (or CRS:84
     * will be assumed)
     *
     * @param bbox A string representing a bounding box in the format "minx,miny,maxx,maxy"
     */
    public void setBBox(String bbox) {
        // TODO enforce non-subsettable layers
        // make sure there are no spaces in the bbox
        bbox = bbox.replace(" ", "");
        properties.setProperty(BBOX, bbox);
    }

    public static CoordinateReferenceSystem toServerCRS(String srsName, boolean forceXY) {
        try {
            if (srsName != null) {
                if (forceXY) {
                    CoordinateReferenceSystem crs = CRS.decode(srsName, true);
                    // have we been requested a srs that cannot be forced to lon/lat?
                    if (CRS.getAxisOrder(crs) == AxisOrder.NORTH_EAST) {
                        Integer epsgCode = CRS.lookupEpsgCode(crs, false);
                        if (epsgCode == null) {
                            throw new IllegalArgumentException(
                                    "Could not find EPSG code for " + srsName);
                        }
                        return CRS.decode("EPSG:" + epsgCode, true);
                    } else {
                        return crs;
                    }
                } else if (srsName.startsWith("EPSG:")
                        && isGeotoolsLongitudeFirstAxisOrderForced()) {
                    // how do we look up the unmodified axis order?
                    String explicit = srsName.replace("EPSG:", "urn:x-ogc:def:crs:EPSG::");
                    return CRS.decode(explicit, false);
                } else {
                    return CRS.decode(srsName, false);
                }
            } else {
                return CRS.decode("CRS:84");
            }
        } catch (NoSuchAuthorityCodeException e) {
            LOGGER.log(
                    Level.FINE,
                    "Failed to build a coordiante reference system from "
                            + srsName
                            + " with forceXY "
                            + forceXY,
                    e);
        } catch (FactoryException e) {
            LOGGER.log(
                    Level.FINE,
                    "Failed to build a coordiante reference system from "
                            + srsName
                            + " with forceXY "
                            + forceXY,
                    e);
        }
        return DefaultEngineeringCRS.CARTESIAN_2D;
    }

    protected static boolean isGeotoolsLongitudeFirstAxisOrderForced() {
        return Boolean.getBoolean(GeoTools.FORCE_LONGITUDE_FIRST_AXIS_ORDER)
                || GeoTools.getDefaultHints().get(Hints.FORCE_LONGITUDE_FIRST_AXIS_ORDER)
                        == Boolean.TRUE;
    }
    /** Sets BBOX and SRS using the provided Envelope. */
    public void setBBox(Envelope envelope) {
        String version = properties.getProperty(VERSION);
        boolean forceXY = version == null || !version.startsWith("1.3");
        String srsName = CRS.toSRS(envelope.getCoordinateReferenceSystem());

        CoordinateReferenceSystem crs = toServerCRS(srsName, forceXY);
        Envelope bbox;
        try {
            bbox = CRS.transform(envelope, crs);
        } catch (TransformException e) {
            bbox = envelope;
        }
        StringBuffer sb = new StringBuffer();
        sb.append(bbox.getMinimum(0));
        sb.append(",");
        sb.append(bbox.getMinimum(1) + ",");
        sb.append(bbox.getMaximum(0) + ",");
        sb.append(bbox.getMaximum(1));
        setBBox(sb.toString());
    }
    /**
     * From the Web Map Service Implementation Specification: "The required FORMAT parameter states
     * the desired format of the response to an operation. Supported values for a GetMap request on
     * a WMS instance are listed in one or more &lt;Format> elements in the &;ltRequest>&lt;GetMap>
     * element of its Capabilities XML. The entire MIME type string in &lt;Format> is used as the
     * value of the FORMAT parameter."
     *
     * @param format The desired format for the GetMap response
     */
    public void setFormat(String format) {
        properties.setProperty(FORMAT, format);
    }

    /**
     * From the Web Map Service Implementation Specification: "The required WIDTH and HEIGHT
     * parameters specify the size in integer pixels of the map image to be produced. WIDTH
     * specifies the number of pixels to be used between the minimum and maximum X values
     * (inclusive) in the BBOX parameter, while HEIGHT specifies the number of pixels between the
     * minimum and maximum Y values. If the WMS server has declared that a Layer has fixed width and
     * height, then the Client shall specify exactly those WIDTH and HEIGHT values in the GetMap
     * request and the Server may issue a Service Exception otherwise."
     */
    public void setDimensions(String width, String height) {
        properties.setProperty(HEIGHT, height);
        properties.setProperty(WIDTH, width);
    }

    public void setDimensions(Dimension imageDimension) {
        setDimensions(imageDimension.width, imageDimension.height);
    }

    // End required parameters, being optional ones.
    // TODO Implement optional parameters.

    /**
     * From the Web Map Service Implementation Specification: "The optional TRANSPARENT parameter
     * specifies whether the map background is to be made transparent or not. The default value is
     * false if the parameter is absent from the request."
     *
     * @param transparent true for transparency, false otherwise
     */
    public void setTransparent(boolean transparent) {
        String value = "FALSE"; // $NON-NLS-1$

        if (transparent) {
            value = "TRUE"; // $NON-NLS-1$
        }

        properties.setProperty(TRANSPARENT, value);
    }

    /**
     * Specifies the colour, in hexidecimal format, to be used as the background of the map. It is a
     * String representing RGB values in hexidecimal format, prefixed by "0x". The format is:
     * 0xRRGGBB. The default value is 0xFFFFFF (white)
     *
     * @param bgColour the background colour of the map, in the format 0xRRGGBB
     */
    public void setBGColour(String bgColour) {
        properties.setProperty(BGCOLOR, bgColour);
    }

    /**
     * The exceptions type specifies what format the server should return exceptions in.
     *
     * <p>Valid values are:
     *
     * <ul>
     *   <li>"application/vnd.ogc.se_xml" (the default)
     *   <li>"application/vnd.ogc.se_inimage"
     *   <li>"application/vnd.ogc.se_blank"
     * </ul>
     */
    public void setExceptions(String exceptions) {
        properties.setProperty(EXCEPTIONS, exceptions);
    }

    /**
     * See the Web Map Server Implementation Specification 1.1.1, Annexes B and C
     *
     * @param time See the Web Map Server Implementation Specification 1.1.1, Annexes B and C
     */
    public void setTime(String time) {
        properties.setProperty(TIME, time);
    }

    /**
     * See the Web Map Server Implementation Specification 1.1.1, Annex C, in particular section C.4
     *
     * @param elevation See the Web Map Server Implementation Specification 1.1.1, Annex C
     */
    public void setElevation(String elevation) {
        properties.setProperty(ELEVATION, elevation);
    }

    /**
     * See the Web Map Server Implementation Specification 1.1.1, Annex C, in particular section
     * C.4.2
     *
     * <p>Example use: <code>request.setSampleDimensionValue("DIM_WAVELENGTH",
     * "4000");</code>
     *
     * @param name the request parameter name to set (usually with 'dim_' as prefix)
     * @param value the value of the request parameter (value, interval or comma-separated list)
     */
    public void setSampleDimensionValue(String name, String value) {
        properties.setProperty(name, value);
    }

    /**
     * Used to implement vendor specific parameters. Entirely optional.
     *
     * @param name a request parameter name
     * @param value a value to accompany the name
     */
    public void setVendorSpecificParameter(String name, String value) {
        properties.setProperty(name, value);
    }

    public void setDimensions(int width, int height) {
        setDimensions("" + width, "" + height);
    }

    public void setProperties(Properties p) {
        properties = p;
    }
}
