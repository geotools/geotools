package mil.nga.giat.data.elasticsearch;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class ElasticAttributeTest {

    private ElasticAttribute attr;
    
    private String name;
    
    @Before
    public void setup() {
        name = "theName";
        attr = new ElasticAttribute(name);
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
}
