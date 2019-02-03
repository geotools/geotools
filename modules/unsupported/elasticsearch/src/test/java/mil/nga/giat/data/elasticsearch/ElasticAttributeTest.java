/**
 * This file is hereby placed into the Public Domain. This means anyone is
 * free to do whatever they wish with this file.
 */
package mil.nga.giat.data.elasticsearch;

import static org.junit.Assert.*;

import java.util.Map;

import org.junit.Before;
import org.junit.Test;

import mil.nga.giat.data.elasticsearch.ElasticAttribute.ElasticGeometryType;

public class ElasticAttributeTest {

    private ElasticAttribute attr;

    private ElasticAttribute other;
    
    private String name;

    private String shortName;

    private String customName;

    private String normalizedName;

    private boolean useShortName;

    private Class<?> type;

    private ElasticGeometryType geometryType;

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
        geometryType = ElasticGeometryType.GEO_SHAPE;
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
        assertTrue(attr.getDisplayName().equals(name));
        attr.setShortName("name");
        attr.setUseShortName(true);
        assertTrue(attr.getDisplayName().equals("name"));        
    }

    @Test
    public void testHashCode() {
        assertTrue(attr.hashCode()==(new ElasticAttribute("theName")).hashCode());
        assertTrue(attr.hashCode()!=(new ElasticAttribute("name")).hashCode());
    }

    @Test
    public void testEquals() {
        assertTrue(!attr.equals("name"));
        assertTrue(attr.equals(new ElasticAttribute("theName")));
        assertTrue(!attr.equals(new ElasticAttribute("name")));
    }

    @Test
    public void testClone() {
        assertEquals(attr, new ElasticAttribute(attr));
    }

    @Test
    public void testCompare() {     
      other = new ElasticAttribute("other");     
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
