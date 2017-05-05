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

    private String name;

    private String shortName;

    private boolean useShortName;

    private Class<?> type;

    private ElasticGeometryType geometryType;

    private boolean use;

    private boolean defaultGeometry;

    private int srid;

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
        type = Map.class;
        geometryType = ElasticGeometryType.GEO_SHAPE;
        use = true;
        defaultGeometry = true;
        srid = 10;
        dateFormat = "yyyy-mm-dd";
        analyzed = true;
        stored = true;
        nested = true;
    }

    @Test
    public void testAttributes() {
        attr.setShortName(shortName);
        attr.setUseShortName(useShortName);
        attr.setType(type);
        attr.setGeometryType(geometryType);
        attr.setUse(use);
        attr.setDefaultGeometry(defaultGeometry);
        attr.setSrid(srid);
        attr.setDateFormat(dateFormat);
        attr.setAnalyzed(analyzed);
        attr.setStored(stored);
        attr.setNested(nested);
        assertEquals(attr.getName(), name);
        assertEquals(attr.getShortName(), shortName);
        assertEquals(attr.getUseShortName(), useShortName);
        assertEquals(attr.getType(), type);
        assertEquals(attr.getGeometryType(), geometryType);
        assertEquals(attr.isUse(), use);
        assertEquals(attr.isDefaultGeometry(), defaultGeometry);
        assertEquals(attr.getSrid(), srid, 1e-10);
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

}
