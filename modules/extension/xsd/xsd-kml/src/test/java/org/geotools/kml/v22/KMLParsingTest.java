package org.geotools.kml.v22;

import java.util.List;
import java.util.Map;

import org.geotools.xml.Parser;
import org.geotools.xml.PullParser;
import org.geotools.xml.StreamingParser;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;

import com.vividsolutions.jts.geom.Point;

public class KMLParsingTest extends KMLTestSupport {

    public void testParseDocument() throws Exception {
        SimpleFeature doc = parseSamples();

        assertNotNull(doc);
        assertEquals("document", doc.getType().getTypeName());
        assertEquals("KML Samples", doc.getAttribute("name"));
        assertEquals(6, ((List)doc.getAttribute("Feature")).size());
    }

    public void testParseFolder() throws Exception {
        SimpleFeature doc = parseSamples();
        SimpleFeature folder = (SimpleFeature) ((List)doc.getAttribute("Feature")).get(0); 

        assertEquals("Placemarks", folder.getAttribute("name"));
        assertTrue(folder.getAttribute("description").toString().startsWith("These are just some"));
        assertEquals(3, ((List)folder.getAttribute("Feature")).size());
    }

    public void testParsePlacemark() throws Exception {
        SimpleFeature doc = parseSamples();
        SimpleFeature folder = (SimpleFeature) ((List)doc.getAttribute("Feature")).get(0); 
        SimpleFeature placemark = (SimpleFeature) ((List)folder.getAttribute("Feature")).get(0);
        
        assertEquals("Simple placemark", placemark.getAttribute("name"));
        assertTrue(placemark.getAttribute("description").toString().startsWith("Attached to the ground"));
        Point p = (Point) placemark.getDefaultGeometry();
        assertEquals(-122.08220, p.getX(), 0.0001);
        assertEquals(37.42229, p.getY(), 0.0001);
    }

    public void testParseWithSchema() throws Exception {
        
    }

    public void testStreamParse() throws Exception {
        StreamingParser p = new StreamingParser(createConfiguration(), 
            getClass().getResourceAsStream("KML_Samples.kml"), KML.Placemark);
        int count = 0;
        while(p.parse() != null) {
            count++;
        }
        assertEquals(20, count);
    }

    public void testPullParse() throws Exception {
        PullParser p = new PullParser(createConfiguration(),
            getClass().getResourceAsStream("KML_Samples.kml"), KML.Placemark);
     
        int count = 0;
        while(p.parse() != null) {
            count++;
        }
        assertEquals(20, count);
    }

    public void testPullParseOrHandler() throws Exception {
        PullParser p = new PullParser(createConfiguration(), getClass().getResourceAsStream(
                "KML_Samples.kml"), KML.Placemark, KML.GroundOverlay, KML.ScreenOverlay);
        int count = 0;
        while (p.parse() != null) {
            count++;
        }
        assertEquals(28, count);
    }

    public void testParseExtendedData() throws Exception {
        String xml = 
            " <Placemark> " + 
            "    <name>Club house</name> " + 
            "    <ExtendedData> " + 
            "      <Data name='holeNumber'> " + 
            "        <value>1</value> " + 
            "      </Data> " + 
            "      <Data name='holeYardage'> " + 
            "        <value>234</value> " + 
            "      </Data> " + 
            "      <Data name='holePar'> " + 
            "        <value>4</value> " + 
            "      </Data> " + 
            "    </ExtendedData> " + 
            "    <Point> " + 
            "      <coordinates>-111.956,33.5043</coordinates> " + 
            "    </Point> " + 
            "  </Placemark> ";
        buildDocument(xml);

        SimpleFeature f = (SimpleFeature) parse();
        Map<Object, Object> userData = f.getUserData();
        assertNotNull(userData);

        @SuppressWarnings("unchecked")
        Map<String, Object> untypedData = (Map<String, Object>) userData.get("UntypedExtendedData");
        assertEquals("1", untypedData.get("holeNumber"));
        assertEquals("234", untypedData.get("holeYardage"));
        assertEquals("4", untypedData.get("holePar"));
    }

    public void testExtendedDataTyped() throws Exception {
        String xml = 
            "<kml xmlns='http://www.opengis.net/kml/2.2'> "+
                    "<Document>   "+
                    "  <name>ExtendedData+SchemaData</name>   "+
                    "  <open>1</open>"+
                    ""+
                    "  <!-- Declare the type 'TrailHeadType' with 3 fields -->"+
                    "  <Schema name='TrailHeadType' id='TrailHeadTypeId'>     "+
                    "    <SimpleField type='string' name='TrailHeadName'>       "+
                    "      <displayName><![CDATA[<b>Trail Head Name</b>]]></displayName>     "+
                    "    </SimpleField>     "+
                    "    <SimpleField type='double' name='TrailLength'>       "+
                    "      <displayName><![CDATA[<i>Length in miles</i>]]></displayName>     "+
                    "    </SimpleField>     "+
                    "    <SimpleField type='int' name='ElevationGain'>       "+
                    "      <displayName><![CDATA[<i>Change in altitude</i>]]></displayName>     "+
                    "    </SimpleField>   "+
                    "  </Schema> "+
                    ""+
                    "<!-- This is analogous to adding three fields to a new element of type TrailHead:"+
                    ""+
                    "  <TrailHeadType>        "+
                    "    <TrailHeadName>...</TrailHeadName>        "+
                    "    <TrailLength>...</TrailLength>        "+
                    "    <ElevationGain>...</ElevationGain>    "+
                    " </TrailHeadType>"+
                    "-->      "+
                    ""+
                    "  <!-- Instantiate some Placemarks extended with TrailHeadType fields -->    "+
                    "  <Placemark>     "+
                    "    <name>Easy trail</name>"+
                    "    <ExtendedData>       "+
                    "      <SchemaData schemaUrl='#TrailHeadTypeId'>         "+
                    "        <SimpleData name='TrailHeadName'>Pi in the sky</SimpleData>         "+
                    "        <SimpleData name='TrailLength'>3.14159</SimpleData>         "+
                    "        <SimpleData name='ElevationGain'>10</SimpleData>       "+
                    "      </SchemaData>     "+
                    "    </ExtendedData>     "+
                    "    <Point>       "+
                    "      <coordinates>-122.000,37.002</coordinates>     "+
                    "    </Point>   "+
                    "  </Placemark>    "+
                    "  <Placemark>     "+
                    "    <name>Difficult trail</name>"+
                    "    <ExtendedData>"+
                    "      <SchemaData schemaUrl='#TrailHeadTypeId'>         "+
                    "        <SimpleData name='TrailHeadName'>Mount Everest</SimpleData>        "+
                    "        <SimpleData name='TrailLength'>347.45</SimpleData>         "+
                    "        <SimpleData name='ElevationGain'>10000</SimpleData>       "+
                    "      </SchemaData>     "+
                    "    </ExtendedData>    "+
                    "    <Point>       "+
                    "      <coordinates>-121.998,37.0078</coordinates>     "+
                    "    </Point>   "+
                    "  </Placemark>   "+
                    "</Document> "+
                "</kml>";
        buildDocument(xml);

        SimpleFeature doc = (SimpleFeature)parse();
        List<SimpleFeature> features = (List<SimpleFeature>) doc.getAttribute("Feature");
        assertEquals(2, features.size());

        SimpleFeature f = features.get(0);
        assertEquals("Pi in the sky", f.getAttribute("TrailHeadName"));
        assertEquals(3.14159, f.getAttribute("TrailLength"));
        assertEquals(10, f.getAttribute("ElevationGain"));

        SimpleFeatureType t = f.getType();
        assertEquals(String.class, t.getDescriptor("TrailHeadName").getType().getBinding());
        assertEquals(Double.class, t.getDescriptor("TrailLength").getType().getBinding());
        assertEquals(Integer.class, t.getDescriptor("ElevationGain").getType().getBinding());
    }

    SimpleFeature parseSamples() throws Exception {
        Parser p = new Parser(createConfiguration());
        SimpleFeature doc =
            (SimpleFeature) p.parse(getClass().getResourceAsStream("KML_Samples.kml"));
        return doc;
    }
}
