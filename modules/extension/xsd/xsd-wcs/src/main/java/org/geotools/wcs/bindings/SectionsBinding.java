package org.geotools.wcs.bindings;

import java.util.Iterator;
import java.util.List;
import javax.xml.namespace.QName;
import net.opengis.wcs20.DescribeEOCoverageSetType;
import net.opengis.wcs20.Section;
import net.opengis.wcs20.Sections;
import net.opengis.wcs20.Wcs20Factory;
import org.geotools.wcs.v2_0.WCSEO;
import org.geotools.xsd.AbstractComplexEMFBinding;
import org.geotools.xsd.ElementInstance;
import org.geotools.xsd.Node;

/**
 * Custom binding for the {@link DescribeEOCoverageSetType} type
 *
 * @author Andrea Aime - GeoSolutions
 */
public class SectionsBinding extends AbstractComplexEMFBinding {

    public SectionsBinding() {
        super(Wcs20Factory.eINSTANCE);
    }

    public QName getTarget() {
        return WCSEO.Sections;
    }

    public Class getType() {
        return Sections.class;
    }

    /**
     *
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     *
     * @generated modifiable
     */
    public Object parse(ElementInstance instance, Node node, Object value) throws Exception {

        List sections = node.getChildren("Section");

        if (null != sections) {
            for (Iterator iterator = sections.iterator(); iterator.hasNext(); ) {
                Node child = (Node) iterator.next();
                child.setValue(Section.get((String) child.getValue()));
            }
        }

        return super.parse(instance, node, value);
    }

    /*
     * (non-Javadoc)
     *
     * @see org.geotools.xsd.AbstractComplexBinding#getExecutionMode()
     */
    @Override
    public int getExecutionMode() {
        return OVERRIDE;
    }
}
