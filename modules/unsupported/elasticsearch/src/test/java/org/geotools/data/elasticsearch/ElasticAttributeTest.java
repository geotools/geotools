/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2020, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.data.elasticsearch;

import static org.junit.Assert.*;

import java.util.Map;
import org.junit.Before;
import org.junit.Test;

public class ElasticAttributeTest {

    private ElasticAttribute attr;

    private String name;

    private String shortName;

    private String customName;

    private String normalizedName;

    private boolean useShortName;

    private Class<?> type;

    private ElasticAttribute.ElasticGeometryType geometryType;

    private boolean use;

    private boolean defaultGeometry;

    private int srid;

    private int order;

    private String dateFormat;

    private boolean analyzed;

    private boolean stored;

    private boolean nested;

    @Before
    public void setup() {
        name = "theName";
        attr = new ElasticAttribute(name);
        shortName = "name";
        useShortName = true;
        customName = "XML Custom Name";
        normalizedName = "_XML_Custom_Name";
        type = Map.class;
        geometryType = ElasticAttribute.ElasticGeometryType.GEO_SHAPE;
        use = true;
        defaultGeometry = true;
        srid = 10;
        order = 1;
        dateFormat = "yyyy-mm-dd";
        analyzed = true;
        stored = true;
        nested = true;
    }

    @Test
    public void testAttributes() {
        attr.setShortName(shortName);
        attr.setUseShortName(useShortName);
        attr.setCustomName(customName);
        attr.setType(type);
        attr.setGeometryType(geometryType);
        attr.setUse(use);
        attr.setDefaultGeometry(defaultGeometry);
        attr.setSrid(srid);
        attr.setOrder(order);
        attr.setDateFormat(dateFormat);
        attr.setAnalyzed(analyzed);
        attr.setStored(stored);
        attr.setNested(nested);
        assertEquals(attr.getName(), name);
        assertEquals(attr.getShortName(), shortName);
        assertEquals(attr.getUseShortName(), useShortName);
        assertEquals(attr.getCustomName(), normalizedName);
        assertEquals(attr.getType(), type);
        assertEquals(attr.getGeometryType(), geometryType);
        assertEquals(attr.isUse(), use);
        assertEquals(attr.isDefaultGeometry(), defaultGeometry);
        assertEquals(attr.getSrid(), srid, 1e-10);
        assertEquals(attr.getOrder(), Integer.valueOf(order));
        assertEquals(attr.getDateFormat(), dateFormat);
        assertEquals(attr.getAnalyzed(), analyzed);
        assertEquals(attr.isStored(), stored);
        assertEquals(attr.isNested(), nested);
    }

    @Test
    public void testDisplayName() {
        assertEquals(attr.getDisplayName(), name);
        attr.setShortName("name");
        attr.setUseShortName(true);
        assertEquals("name", attr.getDisplayName());
    }

    @Test
    public void testHashCode() {
        assertEquals(attr.hashCode(), (new ElasticAttribute("theName")).hashCode());
        assertTrue(attr.hashCode() != (new ElasticAttribute("name")).hashCode());
    }

    @Test
    public void testEquals() {
        assertEquals(attr, new ElasticAttribute("theName"));
        assertTrue(!attr.equals(new ElasticAttribute("name")));
    }

    @Test
    public void testClone() {
        assertEquals(attr, new ElasticAttribute(attr));
    }

    @Test
    public void testCompare() {
        ElasticAttribute other = new ElasticAttribute("other");
        attr.setOrder(1);
        other.setOrder(2);
        assertEquals(-1, attr.compareTo(other));
        attr.setOrder(3);
        other.setOrder(2);
        assertEquals(1, attr.compareTo(other));
        attr.setOrder(null);
        other.setOrder(1);
        assertEquals(1, attr.compareTo(other));
        attr.setOrder(1);
        other.setOrder(null);
        assertEquals(-1, attr.compareTo(other));
        other = new ElasticAttribute("zAfter");
        attr.setOrder(null);
        other.setOrder(null);
        assertTrue(attr.compareTo(other) < 0);
        other = new ElasticAttribute("before");
        attr.setOrder(1);
        other.setOrder(1);
        assertTrue(attr.compareTo(other) > 0);
    }
}
