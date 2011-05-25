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
package org.geotools.data.wfs.v1_0_0.demo;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 *  summary sentence.
 * <p>
 * Paragraph ...
 * </p><p>
 * Responsibilities:
 * <ul>
 * <li>
 * <li>
 * </ul>
 * </p><p>
 * Example:<pre><code>
 * PostDemo x = new PostDemo( ... );
 * TODO code example
 * </code></pre>
 * </p>
 * @author dzwiers
 * @since 0.6.0
 *
 * @source $URL$
 */
public class PostDemo {
    public static void main(String[] args) throws IOException{
        String s = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>" +
             "<DescribeFeatureType xmlns=\"http://www.opengis.net/wfs\" " +
             "xmlns:gml=\"http://www.opengis.net/gml\" " +
             "xmlns:ogc=\"http://www.opengis.net/ogc\" version=\"1.0.0\" " +
             "service=\"WFS\" outputFormat=\"XMLSCHEMA\">" +
             "<TypeName>envirodat</TypeName></DescribeFeatureType>";
        
        System.out.println(s+"\n\n\n");
        URL url = new URL("http://map.ns.ec.gc.ca/envdat/map.aspx?");

        HttpURLConnection connection = (HttpURLConnection)url.openConnection();
        connection.setRequestMethod("POST");
        connection.setDoOutput(true);
        connection.setDoInput(true);
        connection.setRequestProperty("Content-type", "application/xml");
        
        url.openConnection().connect();
        Writer w = new OutputStreamWriter(connection.getOutputStream());
        w.write(s);
        w.flush();
        w.close();
        BufferedReader r = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        while(r.ready()){
            System.out.print((String)r.readLine());
        }
    }

//    public static void main(String[] args) throws IOException{
//        String s = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>" +
//             "<DescribeFeatureType xmlns=\"http://www.opengis.net/wfs\" " +
//             "xmlns:gml=\"http://www.opengis.net/gml\" " +
//             "xmlns:ogc=\"http://www.opengis.net/ogc\" version=\"1.0.0\" " +
//             "service=\"WFS\" outputFormat=\"XMLSCHEMA\">" +
//             "</DescribeFeatureType>";
//        
//        System.out.println(s+"\n\n\n");
//        URL url = new URL("http://www.refractions.net:8080/geoserver/wfs?");
//
//        HttpURLConnection connection = (HttpURLConnection)url.openConnection();
//        connection.setRequestMethod("POST");
//        connection.setDoOutput(true);
//        connection.setDoInput(true);
//        connection.setRequestProperty("Content-type", "application/xml");
//        
//        url.openConnection().connect();
//        Writer w = new OutputStreamWriter(connection.getOutputStream());
//        w.write(s);
//        w.flush();
//        w.close();
//        BufferedReader r = new BufferedReader(new InputStreamReader(connection.getInputStream()));
//        while(r.ready()){
//            System.out.print((String)r.readLine());
//        }
//    }
}
