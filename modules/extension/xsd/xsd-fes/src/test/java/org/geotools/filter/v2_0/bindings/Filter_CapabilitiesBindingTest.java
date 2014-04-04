package org.geotools.filter.v2_0.bindings;

import javax.xml.namespace.QName;

import org.geotools.filter.v2_0.FESTestSupport;
import org.geotools.gml3.v3_2.GML;
import org.opengis.filter.capability.ArithmeticOperators;
import org.opengis.filter.capability.FilterCapabilities;
import org.opengis.filter.capability.GeometryOperand;
import org.opengis.filter.capability.IdCapabilities;
import org.opengis.filter.capability.Operator;
import org.opengis.filter.capability.ScalarCapabilities;
import org.opengis.filter.capability.SpatialCapabilities;
import org.opengis.filter.capability.SpatialOperator;

public class Filter_CapabilitiesBindingTest extends FESTestSupport {
	public void testParse() throws Exception {
        String xml = 
            "<fes:Filter_Capabilities xmlns:fes='http://www.opengis.net/fes/2.0' xmlns:gml='http://schemas.opengis.net/gml'>" +
	            "<fes:Id_Capabilities>" +
	            	"<fes:ResourceIdentifier name=\"fes:ResourceId\"/>" +
	            "</fes:Id_Capabilities>" +
	            "<fes:Scalar_Capabilities>" +
					"<fes:LogicalOperators/>" +
					"<fes:ComparisonOperators>" +
						"<fes:ComparisonOperator name=\"PropertyIsLessThan\"/>" +
						"<fes:ComparisonOperator name=\"PropertyIsGreaterThan\"/>" +
					"</fes:ComparisonOperators>" +
				"</fes:Scalar_Capabilities>" +
				"<fes:Spatial_Capabilities>" +
					"<fes:GeometryOperands>" +
						"<fes:GeometryOperand name=\"gml:Envelope\"/>" +
						"<fes:GeometryOperand name=\"gml:Point\"/>" +
						"<fes:GeometryOperand name=\"gml:MultiPoint\"/>" +
						"<fes:GeometryOperand name=\"gml:LineString\"/>" +
						"<fes:GeometryOperand name=\"gml:MultiLineString\"/>" +
						"<fes:GeometryOperand name=\"gml:Polygon\"/>" +
						"<fes:GeometryOperand name=\"gml:MultiPolygon\"/>" +
						"<fes:GeometryOperand name=\"gml:MultiGeometry\"/>" +
					"</fes:GeometryOperands>" +
					"<fes:SpatialOperators>" +
						"<fes:SpatialOperator name=\"Disjoint\"/>" +
						"<fes:SpatialOperator name=\"Equals\"/>" +
						"<fes:SpatialOperator name=\"DWithin\"/>" +
						"<fes:SpatialOperator name=\"Beyond\"/>" +
						"<fes:SpatialOperator name=\"Intersects\"/>" +
						"<fes:SpatialOperator name=\"Touches\"/>" +
						"<fes:SpatialOperator name=\"Crosses\"/>" +
						"<fes:SpatialOperator name=\"Within\"/>" +
						"<fes:SpatialOperator name=\"Contains\"/>" +
						"<fes:SpatialOperator name=\"Overlaps\"/>" +
						"<fes:SpatialOperator name=\"BBOX\">" +
							"<fes:GeometryOperands>" +
								"<fes:GeometryOperand name=\"gml:Envelope\"/>" +
							"</fes:GeometryOperands>" +
						"</fes:SpatialOperator>" +
					"</fes:SpatialOperators>" +
				"</fes:Spatial_Capabilities>" +
				"<fes:Functions>" +
					"<fes:Function name=\"abs\">" +
						"<fes:Returns>xs:int</fes:Returns>" +
						"<fes:Arguments>" +
							"<fes:Argument name=\"int\">" +
								"<fes:Type>xs:int</fes:Type>" +
							"</fes:Argument>" +
						"</fes:Arguments>" +
					"</fes:Function>" +
					"<fes:Function name=\"abs_2\">" +
						"<fes:Returns>xs:long</fes:Returns>" +
						"<fes:Arguments>" +
							"<fes:Argument name=\"number\">" +
								"<fes:Type>xs:long</fes:Type>" +
							"</fes:Argument>" +
						"</fes:Arguments>" +
					"</fes:Function>" +
				"</fes:Functions>" +
            "</fes:Filter_Capabilities>";

        buildDocument(xml);
        FilterCapabilities filterCapabilities = (FilterCapabilities) parse();
        assertNotNull(filterCapabilities);
        
        // Id_Capabilities
        IdCapabilities id = filterCapabilities.getIdCapabilities();
        assertNotNull(id);
        
        assertTrue(id.hasFID());
        assertFalse(id.hasEID());
        
        // Scalar_Capabilities
        ScalarCapabilities scalar = filterCapabilities.getScalarCapabilities();
        assertNotNull(scalar);
        
        assertTrue(scalar.hasLogicalOperators());
        assertEquals(2, scalar.getComparisonOperators().getOperators().size());
        for (Object o : scalar.getComparisonOperators().getOperators()) {
        	assertNotNull(o);
        	assertTrue(o instanceof Operator);
        	Operator op = (Operator)o;
        	
        	assertTrue(op.getName().equals("PropertyIsLessThan") ||
        			op.getName().equals("PropertyIsGreaterThan"));
        }
        
        // The hard part: scalar arithmetic functions
        ArithmeticOperators arithmetic = scalar.getArithmeticOperators();
        assertNotNull(arithmetic);
        if (false) { // Not parsed until we can figure out if we really need these or not
        	assertEquals(2, arithmetic.getFunctions().getFunctionNames().size());
        }
        
        // Spatial_Capabilities
        SpatialCapabilities spatial = filterCapabilities.getSpatialCapabilities();
        assertNotNull(spatial);
        
        assertEquals(11, spatial.getSpatialOperators().getOperators().size());
        SpatialOperator equals = spatial.getSpatialOperators().getOperator("Equals");
        assertNotNull(equals);
        assertEquals(0, equals.getGeometryOperands().size());
        
        SpatialOperator bbox = spatial.getSpatialOperators().getOperator("BBOX");
        assertNotNull(bbox);
        assertEquals(1, bbox.getGeometryOperands().size());
        
        GeometryOperand envelope = bbox.getGeometryOperands().iterator().next();
        
        assertEquals("http://schemas.opengis.net/gml", envelope.getNamespaceURI());
        assertEquals("Envelope", envelope.getLocalPart());
        
	}
}
