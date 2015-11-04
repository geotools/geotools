/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2004-2008, Open Source Geospatial Foundation (OSGeo)
 *    (C) 2005, David Zwiers
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
package org.geotools.data.wms.test;

import org.geotools.data.ows.Specification;
import org.geotools.data.wms.WMS1_1_1;
import org.geotools.data.wms.WebMapServer;
import org.geotools.data.wms.request.GetStylesRequest;
import org.geotools.data.wms.response.GetStylesResponse;
import org.geotools.styling.NamedLayer;
import org.geotools.styling.SLDParser;
import org.geotools.styling.Style;
import org.geotools.styling.StyleFactory;
import org.geotools.styling.StyleFactoryImpl;
import org.geotools.styling.SLDTransformer;
import org.geotools.styling.StyledLayerDescriptor;
import org.geotools.styling.builder.NamedLayerBuilder;
import org.geotools.styling.builder.StyledLayerDescriptorBuilder;

import org.geotools.styling.builder.StyleBuilder;

import org.geotools.test.OnlineTestCase;
import org.junit.Ignore;
import org.junit.Test;

import javax.mail.internet.ContentType;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

/**
 * 
 * 
 * @source $URL$
 */
public class GetStyleOnlineTest extends OnlineTestCase {

    @Override
    protected String getFixtureId() {
        return "WMS";
    }

    @Test
    public void testGetStyle() throws Exception {

        String baseUrl = fixture.getProperty("base_url");
        String layers = fixture.getProperty("layers");

        URL url = new URL(baseUrl);
        //url = new URL("http://vbox:8080/geoserver/test_shp/wms");

        GetStylesResponse wmsResponse = null;
        GetStylesRequest wmsRequest = null;
        StyleFactory styleFactory = new StyleFactoryImpl();

        WebMapServer server = new WebMapServer(url) {
            // GetStyle is only implemented in WMS 1.1.1
            protected void setupSpecifications() {
                specs = new Specification[1];
                specs[0] = new WMS1_1_1();
            }
        };

        wmsRequest = server.createGetStylesRequest();
        wmsRequest.setLayers(layers);
        // Test URL
        String queryParamters = wmsRequest.getFinalURL().getQuery();
        Map parameters = new HashMap();
        String[] rawParameters = queryParamters.split("&");
        for(String param : rawParameters){
            String [] keyValue = param.split("=");
            parameters.put(keyValue[0],keyValue[1]);
        }

        assertEquals(4, parameters.size());
        assertEquals("WMS", parameters.get("SERVICE"));
        assertEquals("GetStyles", parameters.get("REQUEST"));
        assertEquals("1.1.1", parameters.get("VERSION"));
        assertEquals(layers, parameters.get("LAYERS"));

        wmsResponse = server.issueRequest(wmsRequest);

        // Set encoding of response from HTTP content-type header
        ContentType contentType = new ContentType(wmsResponse.getContentType());
        InputStreamReader stream;
        if(contentType.getParameter("charset") != null)
            stream = new InputStreamReader(wmsResponse.getInputStream(), contentType.getParameter("charset"));
        else
            stream = new InputStreamReader(wmsResponse.getInputStream());

        Style[] styles = (new SLDParser(styleFactory, stream)).readXML();

        assert styles.length > 0;

        SLDTransformer styleTransform = new SLDTransformer();
        StyledLayerDescriptorBuilder SLDBuilder = new StyledLayerDescriptorBuilder();

        NamedLayerBuilder namedLayerBuilder = SLDBuilder.namedLayer();
        namedLayerBuilder.name(layers);
        StyleBuilder styleBuilder = namedLayerBuilder.style();

        for(int i =0; i<styles.length; i++){
            styleBuilder.reset(styles[i]);
            styles[i] = styleBuilder.build();
        }

        NamedLayer namedLayer = namedLayerBuilder.build();

        for(Style style: styles)
            namedLayer.addStyle(style);

        StyledLayerDescriptor sld = (new StyledLayerDescriptorBuilder()).build();
        sld.addStyledLayer(namedLayer);
        String xml = styleTransform.transform(sld);
        assert xml.length() > 300;
    }


}
