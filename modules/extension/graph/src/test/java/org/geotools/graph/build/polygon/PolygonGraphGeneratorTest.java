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
package org.geotools.graph.build.polygon;

import java.util.StringTokenizer;

import junit.framework.TestCase;

import org.geotools.graph.build.basic.BasicGraphBuilder;
import org.geotools.graph.structure.Graph;
import org.geotools.graph.structure.Node;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.Polygon;

public class PolygonGraphGeneratorTest extends TestCase {

	static GeometryFactory gf = new GeometryFactory();
	PolygonGraphGenerator gg;
	
	protected void setUp() throws Exception {
		super.setUp();
		PolygonGraphGenerator.PolygonRelationship rel = 
			new PolygonGraphGenerator.PolygonRelationship() {

				public boolean related(Polygon p1, Polygon p2) {
					return p1.intersects(p2);
				}

				public boolean equal(Polygon p1, Polygon p2) {
					return p1.equals(p2);
				}
			
		};
		
		gg = new PolygonGraphGenerator(new BasicGraphBuilder(),rel);
	}
	
	public void testAdd() {
		Polygon p1 = createPolygon("0 0,1 1,2 2,0 0");
		Polygon p2 = createPolygon("3 3,4 4,5 5,3 3");
		Polygon p3 = createPolygon("6 6,7 7,8 8,6 6");
		
		Node n1 = (Node) gg.add(p1);
		Node n2 = (Node) gg.add(p2);
		Node n3 = (Node) gg.add(p3);

		assertNotNull(n1);
		assertEquals(n1.getObject(),p1);

		assertNotNull(n2);
		assertEquals(n2.getObject(),p2);
		
		assertNotNull(n3);
		assertEquals(n3.getObject(),p3);
		
		Graph g = gg.getGraph();
		assertEquals(3,g.getNodes().size());
		assertEquals(0,g.getEdges().size());
	}
	
	public void testRelationships() {
		Polygon p1 = createPolygon("0 0,5 0,5 5,0 5,0 0");
		Polygon p2 = createPolygon("4 4,9 4,9 9,4 9,4 4");
		Polygon p3 = createPolygon("2 2,7 2,7 -3,2 -3,2 2");
		
		Node n1 = (Node) gg.add(p1);
		Node n2 = (Node) gg.add(p2);
		Node n3 = (Node) gg.add(p3);

		assertNotNull(n1.getEdge(n2));
		assertNotNull(n2.getEdge(n1));
		assertNotNull(n1.getEdge(n3));
		assertNotNull(n2.getEdge(n1));
		
		assertNull(n2.getEdge(n3));
		assertNull(n2.getEdge(n2));
		
		Graph g = gg.getGraph();
		assertEquals(3,g.getNodes().size());
		assertEquals(2,g.getEdges().size());
	}
	
	protected Polygon createPolygon(String coordinates) {
		StringTokenizer tokens = new StringTokenizer(coordinates,",");
		Coordinate[] c = new Coordinate[tokens.countTokens()];
		
		int i = 0;
		while(tokens.hasMoreTokens()) {
			String token = tokens.nextToken();
			String[] oordinates = token.split(" ");
			c[i++] = new Coordinate(
				Double.parseDouble(oordinates[0]),
				Double.parseDouble(oordinates[1])
			);
		}
		
		return gf.createPolygon(gf.createLinearRing(c), null);
	}
}
