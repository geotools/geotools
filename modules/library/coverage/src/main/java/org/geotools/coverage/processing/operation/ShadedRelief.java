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
package org.geotools.coverage.processing.operation;

import it.geosolutions.jaiext.JAIExt;
import java.awt.image.RenderedImage;
import java.util.Map;
import javax.media.jai.ParameterBlockJAI;
import org.geotools.coverage.grid.GridCoverage2D;
import org.geotools.coverage.processing.OperationJAI;
import org.opengis.coverage.processing.OperationNotFoundException;
import org.opengis.parameter.ParameterValueGroup;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.opengis.referencing.operation.MathTransform;
import org.opengis.util.InternationalString;

/**
 * A simple wrapper for the jai-ext ShadedRelief operation.
 *
 * <p>Creates a hillshade effect on a grid containing altitude measures.
 *
 * <p><STRONG>Name:</STRONG>&nbsp;<CODE>"ShadedRelief"</CODE><br>
 * <STRONG>JAI operator:</STRONG>&nbsp;<CODE>"{@linkplain ShadedReliefDescriptor ShadedRelief}"
 * </CODE><br>
 * <STRONG>Parameters:</STRONG>
 *
 * <table border='3' cellpadding='6' bgcolor='F4F8FF'>
 *   <tr bgcolor='#B9DCFF'>
 *     <th>Name</th>
 *     <th>Class</th>
 *     <th>Default value</th>
 *     <th>Allowed values</th>
 *     <th>Property name</th>
 *   </tr>
 *   <tr>
 *     <td>{@code "Source"}</td>
 *     <td>{@link org.geotools.coverage.grid.GridCoverage2D}</td>
 *     <td align="center">N/A</td>
 *     <td align="center"></td>
 *     <td align="center"></td>
 *   </tr>
 *   <tr>
 *     <td>{@code "roi"}</td>
 *     <td>{@code javax.media.jai.ROI}</td>
 *     <td align="center"></td>
 *     <td align="center">N/A</td>
 *     <td align="center"></td>
 *   </tr>
 *   <tr>
 *     <td>{@code "srcNoData"}</td>
 *     <td>{@code it.geosolutions.jaiext.range.Range}</td>
 *     <td align="center"></td>
 *     <td align="center"></td>
 *     <td align="center"></td>
 *   </tr>
 *   <tr>
 *     <td>{@code "dstNoData"}</td>
 *     <td>{@code double}</td>
 *     <td align="center">0.0</td>
 *     <td align="center"></td>
 *     <td align="center"></td>
 *   </tr>
 *   <tr>
 *     <td>{@code "resX"}</td>
 *     <td>{@code double}</td>
 *     <td align="center">1.0</td>
 *     <td align="center"></td>
 *     <td align="center"></td>
 *   </tr>
 *   <tr>
 *     <td>{@code "resY"}</td>
 *     <td>{@code double}</td>
 *     <td align="center">1.0</td>
 *     <td align="center"></td>
 *     <td align="center"></td>
 *   </tr>
 *   <tr>
 *     <td>{@code "zetaFactor"}</td>
 *     <td>{@code double}</td>
 *     <td align="center">1.0</td>
 *     <td align="center"></td>
 *     <td align="center"></td>
 *   </tr>
 *   <tr>
 *     <td>{@code "scale"}</td>
 *     <td>{@code double}</td>
 *     <td align="center">1.0</td>
 *     <td align="center"></td>
 *     <td align="center"></td>
 *   </tr>
 *   <tr>
 *     <td>{@code "altitude"}</td>
 *     <td>{@code double}</td>
 *     <td align="center">1.0</td>
 *     <td align="center"></td>
 *     <td align="center">JAI-EXT.shadedrelief.altitude</td>
 *   </tr>
 *   <tr>
 *     <td>{@code "azimuth"}</td>
 *     <td>{@code double}</td>
 *     <td align="center">1.0</td>
 *     <td align="center"></td>
 *     <td align="center">JAI-EXT.shadedrelief.azimuth</td>
 *   </tr>
 *   <tr>
 *     <td>{@code "algorithm"}</td>
 *     <td>{@code it.geosolutions.jaiext.shadedrelief.ShadedReliefAlgorithm}</td>
 *     <td align="center">COMBINED</td>
 *     <td align="center">ZEVENBERGEN_THORNE, ZEVENBERGEN_THORNE_COMBINED, COMBINED</td>
 *     <td align="center">JAI-EXT.shadedrelief.algorithm</td>
 *   </tr>
 * </table>
 *
 * @author Emanuele Tajariol <etj at geo-solutions.it>
 */
public class ShadedRelief extends OperationJAI {
    /** Serial number for interoperability with different versions. */
    private static final long serialVersionUID = -1077713495023498436L;

    /** ShadedRelief property name */
    public static final String STATS_PROPERTY = "JAI-EXT.shadedrelief";

    public static final String PARAM_ROI = "roi";
    public static final String PARAM_SRCNODATA = "srcNoData";
    public static final String PARAM_DSTNODATA = "dstNoData";
    public static final String PARAM_RESX = "resX";
    public static final String PARAM_RESY = "resY";
    public static final String PARAM_ZETA = "zetaFactor";
    public static final String PARAM_SCALE = "scale";
    public static final String PARAM_ALTITUDE = "altitude";
    public static final String PARAM_AZIMUTH = "azimuth";
    public static final String PARAM_ALGORITHM = "algorithm";

    public static final String CUSTOMIZABLE_PARAMS[] = {
        PARAM_ALTITUDE, PARAM_AZIMUTH, PARAM_ALGORITHM
    };

    public ShadedRelief() throws OperationNotFoundException {
        super("ShadedRelief", getOperationDescriptor(JAIExt.getOperationName("ShadedRelief")));
    }

    @Override
    public String getName() {
        return "ShadedRelief";
    }

    @Override
    protected void handleJAIEXTParams(ParameterBlockJAI jaiParams, ParameterValueGroup params) {
        GridCoverage2D source = (GridCoverage2D) params.parameter("source0").getValue();

        // Handle ROI and NoData
        handleROINoDataInternal(jaiParams, source, "ShadedRelief", 0, 1);

        // Get some values from system props
        for (String paramName : CUSTOMIZABLE_PARAMS) {
            String propName = STATS_PROPERTY + "." + paramName;
            String propVal = System.getProperty(propName);
            if (propVal != null) {
                jaiParams.setParameter(paramName, propVal);
            }
        }
    }

    protected Map<String, ?> getProperties(
            RenderedImage data,
            CoordinateReferenceSystem crs,
            InternationalString name,
            MathTransform gridToCRS,
            GridCoverage2D[] sources,
            OperationJAI.Parameters parameters) {
        return handleROINoDataProperties(
                null, parameters.parameters, sources[0], "ShadedRelief", 0, 1, 2);
    }
}
