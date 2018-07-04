/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2017, Open Source Geospatial Foundation (OSGeo)
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

package org.geotools.styling.css;

import static org.junit.Assert.assertEquals;

import org.geotools.styling.css.selector.Data;
import org.geotools.styling.css.selector.Id;
import org.geotools.styling.css.selector.ScaleRange;
import org.geotools.styling.css.selector.Selector;
import org.geotools.styling.css.selector.TypeName;
import org.junit.Test;

public class SelectorCombineTest {

    @Test
    public void testAndIds() {
        Id id1 = new Id("states.1");
        Id id2 = new Id("states.2");
        assertEquals(Selector.REJECT, Selector.and(id1, id2));
        Id id13 = new Id("states.1", "states.3");
        assertEquals(new Id("states.1"), Selector.and(id1, id13));
    }

    @Test
    public void testAndSeparateDataFilters() {
        Data s1 = new Data("att1 > 10");
        Data s2 = new Data("att2 > 10");
        assertEquals(new Data("att1 > 10 and att2 > 10"), Selector.and(s1, s2));
    }

    @Test
    public void testAndRepeatedFilters() {
        Data s1 = new Data("att1 > 10");
        Data s2 = new Data("att1 > 10");
        // simplifying filter visitor to the rescue
        assertEquals(new Data("att1 > 10.0"), Selector.and(s1, s2));
    }

    @Test
    public void testAndTypename() {
        TypeName states1 = new TypeName("states");
        TypeName states2 = new TypeName("states");
        assertEquals(new TypeName("states"), Selector.and(states1, states2));
        TypeName spearfish = new TypeName("spearfish");
        assertEquals(Selector.REJECT, Selector.and(states1, spearfish));
    }

    @Test
    public void testAndScaleRange() {
        ScaleRange range1 = new ScaleRange(0, false, 20000, false);
        ScaleRange range2 = new ScaleRange(10000, false, 30000, true);
        assertEquals(new ScaleRange(10000, false, 20000, false), Selector.and(range1, range2));
        ScaleRange range3 = new ScaleRange(30000, true, Double.POSITIVE_INFINITY, false);
        assertEquals(Selector.REJECT, Selector.and(range1, range3));
    }
}
