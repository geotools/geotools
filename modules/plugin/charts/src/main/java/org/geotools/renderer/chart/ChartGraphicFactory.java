/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2002-2008, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.renderer.chart;

import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.Map;

import javax.swing.Icon;
import javax.swing.ImageIcon;

import org.geotools.renderer.style.ExternalGraphicFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.eastwood.ChartEngine;
import org.jfree.eastwood.Parameters;
import org.opengis.feature.Feature;
import org.opengis.filter.expression.Expression;

/**
 * Parses Google charts like requests into various kinds of charts.
 * <p>
 * Underlying implementation is based on <a href="http://www.jfree.org/eastwood/">Eastwood charts</a>
 * and <a href="http://www.jfree.org/jfreechart/index.html">JFreeChart</a>
 * <p>
 * And example of a valid symbolizer use in SLD is:
 * <pre>
 * &lt;ExternalGraphic&gt;
 *   &lt;OnlineResource xlink:href=&quot;http://chart?cht=p&amp;amp;chl=male|female&amp;amp;chd=t:${100 * male / (male + female)},${100 * female / (male + female)}&amp;amp;chs=200x100&amp;amp;chf=bg,s,FFFFFF00&quot;/&gt;
 *   &lt;Format&gt;application/chart&lt;/Format&gt;
 * &lt;/ExternalGraphic&gt;
 * </pre>
 * This request will generate a pie chart representing the percentages of female and male
 * population using attribute features and CQL expression to buid the percentage value to be
 * used.
 * </p>
 * <p>
 * For details on the URL format documentation refer to the online 
 * <a href="http://code.google.com/intl/it-IT/apis/chart/">Google charts API</a>.
 * </p>
 * <p>
 * Also mind Eastwood does not implement all of the Google charts API, for example,
 * Venn diagram and spider diagrams are not supported. Check on the Eastwood project
 * pages for details. 
 * </p>
 * @since 2.5.6
 *
 * @source $URL$
 */
public class ChartGraphicFactory implements ExternalGraphicFactory {

    public static final String FORMAT = "application/chart";
    private static final String HTTP_CHART = "http://chart?";

    public Icon getIcon(Feature feature, Expression urlExpression, String format, int size)
            throws Exception {
        // evaluate the expression as a string, get the query params
        String url = urlExpression.evaluate(feature, String.class);
        if (!validRequest(url, format))
            return null;
        Map params = Parameters.parseQueryString(url.substring(HTTP_CHART.length()));

        // build the chart, guess its optimal size, return the icon representing it
        JFreeChart chart = ChartEngine.buildChart(params);
        int[] chartSize = computeChartSize(size, params);
        return new ImageIcon(drawChart(chart, chartSize[0], chartSize[1]));
    }

    private boolean validRequest(String url, String format) {
        return FORMAT.equals(format) && url.startsWith(HTTP_CHART);
    }

    /**
     * This method has been provided as a test utility only
     */
    JFreeChart getChart(Feature feature, Expression urlExpression, String format, int size)
            throws Exception {
        // evaluate the expression as a string, get the query params
        String url = urlExpression.evaluate(feature, String.class);
        if (!validRequest(url, format))
            return null;
        Map params = Parameters.parseQueryString(url.substring(HTTP_CHART.length()));

        // build the chart, guess its optimal size, return the icon representing it
        return ChartEngine.buildChart(params);
    }

    BufferedImage drawChart(JFreeChart chart, int w, int h) {
        BufferedImage bi = new BufferedImage(w, h, BufferedImage.TYPE_4BYTE_ABGR);
        Graphics2D gr = bi.createGraphics();
        try {
            chart.draw(gr, new Rectangle2D.Double(0, 0, w, h));
        } finally {
            gr.dispose();
        }

        return bi;
    }

    int[] computeChartSize(int size, Map params) {
        // We have two size params here, the one coming from SLD, the one coming from the chs
        // parameter, let's try to respect the SLD one, but keep the form factor coming from
        // the chs param
        String[] sizes = (String[]) params.get("chs");
        int[] dims = null;
        if (sizes != null)
            dims = parseCHS(sizes);

        if (dims == null && size <= 0)
            throw new IllegalArgumentException("Chart size cannot be computed, a SLD size "
                    + "is missing, so is the chs chart param");

        if (size > 0) {
            if (dims == null) {
                dims = new int[] { size, size };
            } else {
                if (dims[0] > dims[1]) {
                    dims[1] = dims[1] * size / dims[0];
                    dims[0] = size;
                } else {
                    dims[0] = dims[0] * size / dims[1];
                    dims[1] = size;
                }
            }
        }

        return dims;
    }

    /**
     * Parses the CHS parameter, should be in the form wxh, where w and h are integers and x is the
     * separator
     * 
     * @param sizes
     * @return
     */
    int[] parseCHS(String[] sizes) {
        int[] dims;
        dims = new int[2];
        String[] xy = sizes[0].split("x");
        if (xy.length != 2)
            throw new IllegalArgumentException("The chs parameter should be in wxh form, "
                    + "where w and h are measured in pixels");
        dims[0] = Integer.parseInt(xy[0]);
        dims[1] = Integer.parseInt(xy[1]);
        return dims;
    }

}
