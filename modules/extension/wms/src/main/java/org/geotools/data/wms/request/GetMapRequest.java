/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2004-2008, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.data.wms.request;

import java.awt.Dimension;
import java.util.Properties;

import org.geotools.data.ows.CRSEnvelope;
import org.geotools.data.ows.Layer;
import org.geotools.data.ows.Request;
import org.geotools.data.ows.StyleImpl;
import org.opengis.geometry.BoundingBox;

/**
 * Construct a WMS getMap request.
 * 
 * <p>
 * Constructs a getMapRequest based on the following property values:
 * 
 * <ul>
 * <li>
 * ELEVATION
 * </li>
 * <li>
 * TIME
 * </li>
 * <li>
 * EXCEPTIONS
 * </li>
 * <li>
 * BGCOLOR
 * </li>
 * <li>
 * TRANSPARENT
 * </li>
 * <li>
 * WIDTH
 * </li>
 * <li>
 * HEIGHT
 * </li>
 * <li>
 * SRS
 * </li>
 * <li>
 * REQUEST
 * </li>
 * <li>
 * LAYERS
 * </li>
 * <li>
 * STYLES
 * </li>
 * <li>
 * <i>vendor specific parameters</i>
 * </li>
 * </ul>
 * </p>
 * 
 * <p>
 * Q: List availableFormats and availableExceptions - why are these here? It
 * looks like they are designed to restrict the values used for SRS, format
 * and exceptions. If so the code never uses them. Q: How constant is the
 * GetMapRequest format across WMS versions? Do we need to generalize here?
 * </p>
 *
 * @author Richard Gould, Refractions Research
 *
 * @source $URL$
 */
public interface GetMapRequest extends Request{
    /** Represents the ELEVATION parameter */
    public static final String ELEVATION = "ELEVATION"; //$NON-NLS-1$
    /** Represents the TIME parameter */
    public static final String TIME = "TIME"; //$NON-NLS-1$
    /** Represents the EXCEPTIONS parameter */
    public static final String EXCEPTIONS = "EXCEPTIONS"; //$NON-NLS-1$
    /** Represents the BGCOLOR parameter */
    public static final String BGCOLOR = "BGCOLOR"; //$NON-NLS-1$
    /** Represents the TRANSPARENT parameter */
    public static final String TRANSPARENT = "TRANSPARENT"; //$NON-NLS-1$
    /** Represents the WIDTH parameter */
    public static final String WIDTH = "WIDTH"; //$NON-NLS-1$
    /** Represents the HEIGHT parameter */
    public static final String HEIGHT = "HEIGHT"; //$NON-NLS-1$
    /** Represents the FORMAT parameter */
    public static final String FORMAT = "FORMAT"; //$NON-NLS-1$
    /** Represents the BBOX parameter */
    public static final String BBOX = "BBOX"; //$NON-NLS-1$
    /** Represents the SRS parameter */
    public static final String SRS = "SRS"; //$NON-NLS-1$
    /** Represents the LAYERS parameter */
    public static final String LAYERS = "LAYERS"; //$NON-NLS-1$
    /** Represents the STYLES parameter */
    public static final String STYLES = "STYLES"; //$NON-NLS-1$
    
    
    public static final String EXCEPTION_XML = "application/vnd.ogc.se_xml";
    /** Represents the EXCEPTION_INIMAGE value */
    public static final String EXCEPTION_INIMAGE = "application/vnd.ogc.se_inimage"; //$NON-NLS-1$
    /** Represents the EXCEPTION_BLANK value */
    public static final String EXCEPTION_BLANK = "application/vnd.ogc.se_blank"; //$NON-NLS-1$
    

    /** =============== BEGIN SLD SPECIFICATION PARAMETERS =============== **/
    /** Represents the SLD parameter */
    public static final String SLD = "SLD"; //$NON-NLS-1$
    /** Represents the SLD_BODY parameter */
    public static final String SLD_BODY = "SLD_BODY"; //$NON-NLS-1$
    /** Represents the WFS parameter mentioned briefly in 1.1.1 */
    public static final String WFS = "WFS"; //$NON-NLS-1$
    /** Represents REMOTE_OWS_TYPE parameter */
    public static final String REMOTE_OWS_TYPE = "REMOTE_OWS_TYPE"; //$NON-NLS-1$
    /** Represents REMOTE_OWS_URL parameter */
    public static final String REMOVE_OWS_URL = "REMOTE_OWS_URL"; //$NON-NLS-1$
    
    
    /** <code>REMOTE_OWS_WFS</code> indicates WFS as a REMOTE_OWS_TYPE */
    public static final String REMOTE_OWS_WFS = "WFS";
    /** <code>REMOTE_OWS_WCS</code> indicates WCS as a REMOTE_OWS_TYPE */
    public static final String REMOTE_OWS_WCS = "WCS";
    /** =============== END SLD SPECIFICATION PARAMETERS =============== **/
    
    /**
     * Sets the version number of the request.
     *
     * @param version A String indicting a WMS Version ("1.0.0", "1.1.0",
     *        "1.1.1", "1.3.0", etc.)
     */
    public void setVersion(String version);
    
    /**
     * Adds a Layer to the list of layers to be requested. This layer will be drawn
     * below any previously added layers. 
     * @param layer the Layer to use
     * @param style the style to use. If it is null, the default style is used.
     */
    public void addLayer(Layer layer, StyleImpl style);
    
    /**
     * Adds a Layer to the list of layers to be requested. This layer will be drawn
     * below any previously added layers. 
     * 
     * @param layerName the Layer to use
     * @param style     the style to use. If it is null, the default style is used.
     */
    public void addLayer(String layerName, StyleImpl style);

    /**
     * Adds a Layer to the list of layers to be requested. This layer will be drawn
     * below any previously added layers. 
     * @param layerName the name of the layer to use
     * @param styleName the style to use to draw the layer, can also be NULL, "" or "default"
     */
    public void addLayer(String layerName, String styleName);
    
    /**
     * Adds a Layer to the list of layers to be requested. This layer will be drawn
     * below any previously added layers. 
     * @param layer the Layer to use
     * @param styleName the style to use to draw the layer, can also be NULL, "" or "default"
     */
    public void addLayer(Layer layer, String styleName);
    
    /**
     * Adds a Layer to the list of layers to be requested. This layer will be drawn
     * below any previously added layers. The style will be the default one. 
     * @param layer the Layer to use
     */
    public void addLayer(Layer layer);

    /**
     * From the Web Map Service Implementation Specification: "The required SRS
     * parameter states which Spatial Reference System applies to the values
     * in the BBOX parameter. The value of the SRS parameter shall be on of
     * the values defined in the character data section of an &lt;SRS> element
     * defined or inherited by the requested layer. The same SRS applies to
     * all layers in a single request. If the WMS has declared SRS=NONE for a
     * Layer, then the Layer does not have a well-defined spatial reference
     * system and should not be shown in conjunction with other layers. The
     * client shall specify SRS as "none" in the GetMap request and the Server
     * may issue a Service Exception otherwise."
     *
     * @param srs A String indicating the Spatial Reference System to render
     *        the layers in.
     */
    public void setSRS(String srs);

    /**
     * From the Web Map Service Implementation Specification: "The required
     * BBOX parameter allows a Client to request a particular Bounding Box.
     * The value of the BBOX parameter in a GetMap request is a list of
     * comma-separated numbers of the form "minx,miny,maxx,maxy". If the WMS
     * server has declared that a Layer is not subsettable, then the Client
     * shall specify exactly the declared Bounding Box values in the GetMap
     * request and the Server may issue a Service Exception otherwise."
     *
     * NOTE: In WMS 1.3.0, the specification of "EPSG:4326" has the axis
     * swapped, so a request made in 1.1.1 using "minx,miny,maxx,maxy" would
     * use "miny,minx,maxy,maxx" in 1.3.0. Only when using EPSG:4326!
     * 
     * Currently it is up to the client to do this on there own.
     * TODO Accept Envelopes and doubles instead of Strings, and perform
     * the 1.3.0 conversion automatically. Also note that not all servers
     * may implement this. Should provide an option to use 1.1.1 format even
     * when using 1.3.0.
     *
     * @param bbox A string representing a bounding box in the format
     *        "minx,miny,maxx,maxy"
     */
    public void setBBox(String bbox);
    public void setBBox(CRSEnvelope box);
    public void setBBox(BoundingBox box);
    
    /**
     * From the Web Map Service Implementation Specification: "The required
     * FORMAT parameter states the desired format of the response to an
     * operation. Supported values for a GetMap request on a WMS instance are
     * listed in one or more &lt;Format> elements in the
     * &;ltRequest>&lt;GetMap> element of its Capabilities XML. The entire
     * MIME type string in &lt;Format> is used as the value of the FORMAT
     * parameter."
     *
     * @param format The desired format for the GetMap response
     */
    public void setFormat(String format);

    /**
     * From the Web Map Service Implementation Specification: "The required
     * WIDTH and HEIGHT parameters specify the size in integer pixels of the
     * map image to be produced. WIDTH specifies the number of pixels to be
     * used between the minimum and maximum X values (inclusive) in the BBOX
     * parameter, while HEIGHT specifies the number of pixels between the
     * minimum and maximum Y values. If the WMS server has declared that a
     * Layer has fixed width and height, then the Client shall specify exactly
     * those WIDTH and HEIGHT values in the GetMap request and the Server may
     * issue a Service Exception otherwise."
     *
     * @param width
     * @param height
     */
    public void setDimensions(String width, String height);
    public void setDimensions(int width, int height);
    public void setDimensions(Dimension imageDimension); 
    // End required parameters, begin optional ones.
    //TODO Implement optional parameters.

    /**
     * From the Web Map Service Implementation Specification: "The optional
     * TRANSPARENT parameter specifies whether the map background is to be
     * made transparent or not. The default value is false if the parameter is
     * absent from the request."
     *
     * @param transparent true for transparency, false otherwise
     */
    public void setTransparent(boolean transparent);

    /**
     * Specifies the colour, in hexidecimal format, to be used as the
     * background of the map. It is a String representing RGB values in
     * hexidecimal format, prefixed by "0x". The format is: 0xRRGGBB. The
     * default value is 0xFFFFFF (white)
     *
     * @param bgColour the background colour of the map, in the format 0xRRGGBB
     */
    public void setBGColour(String bgColour);

    /**
     * The exceptions type specifies what format the server should return
     * exceptions in.
     * 
     * <p>
     * Valid values are:
     * 
     * <ul>
     * <li>
     * "application/vnd.ogc.se_xml" (the default)
     * </li>
     * <li>
     * "application/vnd.ogc.se_inimage"
     * </li>
     * <li>
     * "application/vnd.ogc.se_blank"
     * </li>
     * </ul>
     * </p>
     *
     * @param exceptions
     */
    public void setExceptions(String exceptions);

    /**
     * See the Web Map Server Implementation Specification 1.1.1, Annexes B and
     * C
     *
     * @param time See the Web Map Server Implementation Specification 1.1.1,
     *        Annexes B and C
     */
    public void setTime(String time);

    /**
     * See the Web Map Server Implementation Specification 1.1.1, Annex C, in
     * particular section C.4
     *
     * @param elevation See the Web Map Server Implementation Specification
     *        1.1.1, Annex C
     */
    public void setElevation(String elevation);

    /**
     * See the Web Map Server Implementation Specification 1.1.1, Annex C, in
     * particular section C.4.2
     * 
     * <p>
     * Example use: <code>request.setSampleDimensionValue("DIM_WAVELENGTH",
     * "4000");</code>
     * </p>
     *
     * @param name the request parameter name to set (usually with 'dim_' as
     *        prefix)
     * @param value the value of the request parameter (value, interval or
     *        comma-separated list)
     */
    public void setSampleDimensionValue(String name, String value);

    /**
     * Used to implement vendor specific parameters. Entirely optional.
     *
     * @param name a request parameter name
     * @param value a value to accompany the name
     */
    public void setVendorSpecificParameter(String name, String value);
    
    /** 
     * create a request using a properties file to save time
     */
    public void setProperties(Properties p);
    
}
