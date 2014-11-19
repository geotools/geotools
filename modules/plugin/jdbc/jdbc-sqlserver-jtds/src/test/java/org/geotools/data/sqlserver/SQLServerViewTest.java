package org.geotools.data.sqlserver;

import org.geotools.jdbc.JDBCViewTest;
import org.geotools.jdbc.JDBCViewTestSetup;
import org.opengis.feature.type.AttributeDescriptor;
import org.opengis.feature.type.AttributeType;

import com.vividsolutions.jts.geom.Geometry;

public class SQLServerViewTest extends JDBCViewTest {

    @Override
    protected JDBCViewTestSetup createTestSetup() {
        return new SQLServerViewTestSetup();
    }

    
    /**
     * Override since sql server metadata over nullability over views works differently than in other databases
     */
    protected void assertAttributesEqual(AttributeDescriptor expected, AttributeDescriptor actual) {
        assertEquals(aname(expected.getName()), actual.getName());
        // assertEquals(expected.getMinOccurs(), actual.getMinOccurs());
        assertEquals(expected.getMaxOccurs(), actual.getMaxOccurs());
        // assertEquals(expected.isNillable(), actual.isNillable());
        assertEquals(expected.getDefaultValue(), actual.getDefaultValue());

        AttributeType texpected = expected.getType();
        AttributeType tactual = actual.getType();

        if ( Number.class.isAssignableFrom( texpected.getBinding() ) ) {
            assertTrue( Number.class.isAssignableFrom( tactual.getBinding() ) );
        }
        else if ( Geometry.class.isAssignableFrom( texpected.getBinding())) {
            assertTrue( Geometry.class.isAssignableFrom( tactual.getBinding()));
        }
        else {
            assertTrue(texpected.getBinding().isAssignableFrom(tactual.getBinding()));    
        }
    }
}
