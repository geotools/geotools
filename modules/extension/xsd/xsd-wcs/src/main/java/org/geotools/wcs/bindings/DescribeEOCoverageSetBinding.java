package org.geotools.wcs.bindings;

import javax.xml.namespace.QName;

import net.opengis.wcs20.ContainmentType;
import net.opengis.wcs20.DescribeEOCoverageSetType;
import net.opengis.wcs20.Wcs20Factory;

import org.geotools.wcs.v2_0.WCSEO;
import org.geotools.xml.AbstractComplexEMFBinding;
import org.geotools.xml.ElementInstance;
import org.geotools.xml.Node;

/**
 * Custom binding for the {@link DescribeEOCoverageSetType} type
 * 
 * @author Andrea Aime - GeoSolutions
 *
 */
public class DescribeEOCoverageSetBinding extends AbstractComplexEMFBinding {

    public DescribeEOCoverageSetBinding() {
        super(Wcs20Factory.eINSTANCE);
    }
    
    public QName getTarget() {
        return WCSEO.DescribeEOCoverageSetType;
    }

    public Class getType() {
        return DescribeEOCoverageSetType.class;
    }
    
    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated modifiable
     */
    public Object parse(ElementInstance instance, Node node, Object value)
            throws Exception {
        
        Node attr = node.getChild("containment");

        if (null != attr) {
            attr.setValue(ContainmentType.get((String)attr.getValue()));
        }

        return super.parse(instance, node, value);

    }

    /*
     * (non-Javadoc)
     * 
     * @see org.geotools.xml.AbstractComplexBinding#getExecutionMode()
     */
    @Override
    public int getExecutionMode() {
        return OVERRIDE;
    }
}
