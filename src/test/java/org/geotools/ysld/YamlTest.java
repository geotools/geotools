package org.geotools.ysld;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

/**
 * Test Yaml wrappers.
 */
public class YamlTest {

    @Test
    public void lookupTest(){
        YamlMap yaml = YamlMap.from(
           "name","test",
           "list", YamlSeq.from(1,2,3,4,5),
           "array", new String[]{"a","b","c","d"},
           "map",YamlMap.from("x",143.23,"y",48.9)
       );
       // fluent api access
       assertEquals( "test", yaml.get("name") );
       assertEquals( 1, yaml.seq("list").get(0) );
       assertEquals( 143.23, yaml.map("map").get("x"));
       
       // lazy api access
       assertEquals( "test", yaml.lookup("name") );
       assertEquals( 1, yaml.lookup("list/0") );
       assertEquals( 143.23, yaml.lookup("map/x"));
       
       // extra lazy api access into array
       assertEquals( "a", yaml.lookup("array/0"));
       
       // super lazy api using index into map
       assertEquals( "a", yaml.lookup("2/0"));
    }
}
