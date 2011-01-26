/*
 *    GeoTools - The Open Source Java GIS Tookit
 *    http://geotools.org
 * 
 *    (C) 2006-2008, Open Source Geospatial Foundation (OSGeo)
 *
 *    This file is hereby placed into the Public Domain. This means anyone is
 *    free to do whatever they wish with this file. Use it well and enjoy!
 */
package org.geotools.demo.xml;

import java.io.StringReader;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.geotools.filter.FilterDOMParser;
import org.opengis.filter.Filter;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

public class DOMExample {
	static String xml = "<Filter xmlns:gml=\"http://www.opengis.net/gml\">"
			+ "  <Overlaps>"
			+ "    <PropertyName>testGeometry</PropertyName>"
			+ "<gml:Polygon srsName=\"http://www.opengis.net/gml/srs/EPSG#4326\">"
			+ "<gml:outerBoundaryIs>" + "<gml:LinearRing>"
			+ "<gml:coordinates>0,0 0,10 10,10 10,0 0,0</gml:coordinates>"
			+ "</gml:LinearRing>" + "</gml:outerBoundaryIs>" + "</gml:Polygon>"
			+ "  </Overlaps>" + "</Filter>";

	public static void main(String args[]) throws Exception {
		System.out.println("Parsing:" + xml);
		StringReader reader = new StringReader(xml);
		InputSource input = new InputSource(reader);

		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		DocumentBuilder db = dbf.newDocumentBuilder();
		Document dom = db.parse(input);

		Filter filter = null;

		// first grab a filter node
		NodeList nodes = dom.getElementsByTagName("Filter");

		for (int j = 0; j < nodes.getLength(); j++) {
			Element filterNode = (Element) nodes.item(j);
			NodeList list = filterNode.getChildNodes();
			Node child = null;

			for (int i = 0; i < list.getLength(); i++) {
				child = list.item(i);

				if ((child == null)
						|| (child.getNodeType() != Node.ELEMENT_NODE)) {
					continue;
				}

				filter = FilterDOMParser.parseFilter(child);
			}
		}
		System.out.println("got:" + filter);
	}

}
