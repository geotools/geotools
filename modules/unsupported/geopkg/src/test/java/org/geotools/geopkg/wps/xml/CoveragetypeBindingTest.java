package org.geotools.geopkg.wps.xml;


import org.geotools.geopkg.wps.GeoPackageProcessRequest;
import org.geotools.geopkg.wps.xml.GPKG;
import org.geotools.xml.Binding;


/**
 * Binding test case for http://www.opengis.net/gpkg:coveragetype.
 *
 * <p>
 *  <pre>
 *   <code>
 *  &lt;?xml version="1.0" encoding="UTF-8"?&gt;&lt;xs:complexType name="coveragetype" xmlns:xs="http://www.w3.org/2001/XMLSchema"&gt;
 *      &lt;xs:sequence&gt;
 *        &lt;xs:element minOccurs="0" name="minZoom" type="xs:nonNegativeInteger"/&gt;
 *        &lt;xs:element minOccurs="0" name="maxZoom" type="xs:nonNegativeInteger"/&gt;
 *        &lt;xs:element minOccurs="0" name="minColumn" type="xs:nonNegativeInteger"/&gt;
 *        &lt;xs:element minOccurs="0" name="maxColumn" type="xs:nonNegativeInteger"/&gt;
 *        &lt;xs:element minOccurs="0" name="minRow" type="xs:nonNegativeInteger"/&gt;
 *        &lt;xs:element minOccurs="0" name="maxRow" type="xs:nonNegativeInteger"/&gt;
 *      &lt;/xs:sequence&gt;
 *    &lt;/xs:complexType&gt; 
 *      
 *    </code>
 *   </pre>
 * </p>
 *
 * @generated
 */
public class CoveragetypeBindingTest extends GPKGTestSupport {

    public void testType() {
        assertEquals(  GeoPackageProcessRequest.TilesLayer.TilesCoverage.class, binding( GPKG.coveragetype ).getType() );
    }
    
    public void testExecutionMode() {
        assertEquals( Binding.OVERRIDE, binding( GPKG.coveragetype ).getExecutionMode() );
    }
    
    public void testParse() throws Exception {
        buildDocument("<coverage><minZoom>1</minZoom><maxZoom>10</maxZoom><minColumn>100</minColumn><maxColumn>1000</maxColumn><minRow>50</minRow><maxRow>500</maxRow></coverage>");
        Object result = parse(GPKG.coveragetype);
        assertTrue(result instanceof GeoPackageProcessRequest.TilesLayer.TilesCoverage);  
        GeoPackageProcessRequest.TilesLayer.TilesCoverage coverage = (GeoPackageProcessRequest.TilesLayer.TilesCoverage) result;
        assertEquals (1, coverage.getMinZoom().intValue());
        assertEquals (10, coverage.getMaxZoom().intValue());
        assertEquals (100, coverage.getMinColumn().intValue());
        assertEquals (1000, coverage.getMaxColumn().intValue());
        assertEquals (50, coverage.getMinRow().intValue());
        assertEquals (500, coverage.getMaxRow().intValue());
    }
    
}