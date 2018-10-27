package org.geotools.ows.v1_1;

import java.util.Map;
import net.opengis.ows11.DomainType;
import net.opengis.ows11.OperationType;
import net.opengis.ows11.OperationsMetadataType;
import net.opengis.ows11.ValueType;
import org.geotools.xsd.Parser;

public class CapabilitiesParseTest extends OWSTestSupport_1_1 {

    public void testParseCapabilities() throws Exception {
        Parser p = new Parser(createConfiguration());
        Object o = p.parse(getClass().getResourceAsStream("exampleCapabilities1.xml"));

        // the core ows schema is abstract, and meant to be extended, so the root caps document
        // is usually declared by the specific schema, since hte ows schema doens't know about it
        // we get back a map
        assertTrue(o instanceof Map);

        Map caps = (Map) o;
        assertTrue(caps.containsKey("OperationsMetadata"));

        OperationsMetadataType om = (OperationsMetadataType) caps.get("OperationsMetadata");
        assertEquals(3, om.getOperation().size());

        OperationType op = (OperationType) om.getOperation().get(0);
        assertEquals("GetCapabilities", op.getName());

        assertEquals(1, op.getParameter().size());
        DomainType d = (DomainType) op.getParameter().get(0);
        assertEquals("Format", d.getName());

        assertEquals(1, d.getAllowedValues().getValue().size());

        ValueType v = (ValueType) d.getAllowedValues().getValue().get(0);
        assertEquals("text/xml", v.getValue());
    }

    public void testConstraints() throws Exception {
        Parser p = new Parser(createConfiguration());
        Object o = p.parse(getClass().getResourceAsStream("exampleCapabilities1.xml"));

        // the core ows schema is abstract, and meant to be extended, so the
        // root caps document
        // is usually declared by the specific schema, since hte ows schema
        // doens't know about it
        // we get back a map
        assertTrue(o instanceof Map);

        Map caps = (Map) o;
        assertTrue(caps.containsKey("OperationsMetadata"));

        OperationsMetadataType om = (OperationsMetadataType) caps.get("OperationsMetadata");
        assertEquals(3, om.getOperation().size());

        DomainType maximumHeightConstraint = (DomainType) om.getConstraint().get(2);
        assertEquals("MaximumHeight", maximumHeightConstraint.getName());
        assertNotNull(maximumHeightConstraint.getAllowedValues());
        assertNull(maximumHeightConstraint.getNoValues());
        assertEquals(1, maximumHeightConstraint.getAllowedValues().getValue().size());
        assertEquals(
                "4000",
                ((ValueType) maximumHeightConstraint.getAllowedValues().getValue().get(0))
                        .getValue());

        DomainType noValuesAllowedConstraint = (DomainType) om.getConstraint().get(3);

        assertEquals("NoValuesAllowed", noValuesAllowedConstraint.getName());
        assertNull(noValuesAllowedConstraint.getAllowedValues());
        assertNotNull(noValuesAllowedConstraint.getNoValues());
    }
}
