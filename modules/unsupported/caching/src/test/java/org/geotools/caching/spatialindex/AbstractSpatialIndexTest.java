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
package org.geotools.caching.spatialindex;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

import junit.framework.TestCase;


/** A common test framework for spatial indexes.
 * Creates a random 2-D data set in the unit square.
 * Concrete tests must implement the createIndex method :
 * <code>
 * TypeOfIndex index ;
 * protected AbstractSpatialIndex createIndex() {
 *    index = new TypeOfIndex() ;
 *    return index ;
 * }
 * </code>
 *
 * @author Christophe Rousson, SoC 2007, CRG-ULAVAL
 *
 *
 * @source $URL$
 */
public abstract class AbstractSpatialIndexTest extends TestCase {
	private static final String DATA_PREFIX = "Object: ";
    protected AbstractSpatialIndex index;
    int setSize = 1000;
    protected ArrayList<Region> regions = new ArrayList<Region>(setSize);
    protected Random generator = new Random();
    protected Region universe = new Region(new double[] { 0, 0 }, new double[] { 1, 1 });
    protected double meansize = 0.01;

    protected void setUp() {
        index = createIndex();
        regions = new ArrayList<Region>(setSize);
        double width = universe.getHigh(0) - universe.getLow(0);
        double height = universe.getHigh(1) - universe.getLow(1);
        
        //validate all nodes as data is only inserted into validated nodes
        for (int i = 0; i < index.rootNode.getChildrenCount(); i ++){
        	NodeIdentifier child = index.rootNode.getChildIdentifier(i);
        	child.setValid(true);
        }
        
        
        for (int i = 0; i < setSize; i++) {
            double centerx = (meansize) + (generator.nextDouble() * (width - (2 * meansize)));
            double centery = (meansize) + (generator.nextDouble() * (height - (2 * meansize)));
            double h = generator.nextDouble() * meansize * 2;
            double w = generator.nextDouble() * meansize * 2;
            Region reg = new Region(new double[] { centerx - (w / 2), centery - (h / 2) },
                    new double[] { centerx + (w / 2), centery + (h / 2) });
            regions.add(reg);
            index.insertData(DATA_PREFIX + i, reg);
        }
        
    }

    public void testInsertion() {
        Statistics stats = index.getStatistics();
        //assertEquals(setSize, stats.getNumberOfData());
        // data may be inserted more than once
        assertTrue(stats.getNumberOfData() >= setSize);
    }

    public void testIntersectionQuery() {
        HarvestingVisitor v = new HarvestingVisitor();
        Region query = new Region(new double[] { 0, 0 }, new double[] { 1, 1 });
        index.intersectionQuery(query, v);
        assertEquals(index.getStatistics().getNumberOfNodes(), v.visited_nodes);
        assertEquals(setSize, v.harvest.size());

        Set<String> comp_result = noIndexQuery(regions, query, AbstractSpatialIndex.IntersectionQuery);
        assertEquals(comp_result, v.harvest);
        v = new HarvestingVisitor();
        query = new Region(new double[] { .25, .25 }, new double[] { .75, .75 });
        index.intersectionQuery(query, v);
        assertTrue(v.harvest.size() < setSize);
        comp_result = noIndexQuery(regions, query, AbstractSpatialIndex.IntersectionQuery);
        assertEquals(comp_result, v.harvest);
    }

    public void testContainmentQuery() {
        HarvestingVisitor v = new HarvestingVisitor();
        Region query = new Region(new double[] { 0, 0 }, new double[] { 1, 1 });
        index.containmentQuery(query, v);
        assertEquals(setSize, v.harvest.size());
        assertEquals(index.getStatistics().getNumberOfNodes(), v.visited_nodes);
        Set<String> comp_result = noIndexQuery(regions, query, AbstractSpatialIndex.ContainmentQuery);
        assertEquals(comp_result, v.harvest);
        
        v = new HarvestingVisitor();
        query = new Region(new double[] { .25, .25 }, new double[] { .75, .75 });
        index.containmentQuery(query, v);
        comp_result = noIndexQuery(regions, query, AbstractSpatialIndex.ContainmentQuery);
        assertEquals(comp_result, v.harvest);
    }

    public void testPointQuery() {
        HarvestingVisitor v = new HarvestingVisitor();
        Point query = new Point(new double[] { generator.nextDouble(), generator.nextDouble() });
        index.intersectionQuery(query, v);

        Set<String> comp_result = noIndexQuery(regions, query, AbstractSpatialIndex.IntersectionQuery);
        assertEquals(comp_result, v.harvest);
    }

    public void testQueryStrategy() {
        // TODO: 
    }

    public void testClear() {
        index.clear();

        HarvestingVisitor v = new HarvestingVisitor();
        Region query = new Region(new double[] { 0, 0 }, new double[] { 1, 1 });
        index.containmentQuery(query, v);
        assertEquals(0, v.harvest.size());
        assertEquals(index.getStatistics().getNumberOfNodes(), v.visited_nodes);
    }

    HashSet<String> noIndexQuery(ArrayList<Region> searchset, Shape query, int type) {
        HashSet<String> harvest = new HashSet<String>();

        for (int i = 0; i < setSize; i++) {
            Region r = (Region) regions.get(i);

            if (((type == AbstractSpatialIndex.IntersectionQuery) && (query.intersects(r)))
                    || ((type == AbstractSpatialIndex.ContainmentQuery) && (query.contains(r)))) {
                harvest.add("Object: " + i);
            }
        }

        return harvest;
    }

    protected abstract AbstractSpatialIndex createIndex();

    public class HarvestingVisitor implements Visitor {
        public HashSet<Object> harvest = new HashSet<Object>(20);
        public int visited_nodes = 0;

        public void visitData(Data d) {
            harvest.add(d.getData());
        }

        public void visitNode(Node n) {
            visited_nodes++;
        }

        public boolean isDataVisitor() {
            return true;
        }
    }
}
