package org.geotools.wcs.bindings;

import javax.xml.namespace.QName;

import net.opengis.wcs20.ExtensionItemType;
import net.opengis.wcs20.ExtensionType;
import net.opengis.wcs20.Wcs20Factory;

import org.eclipse.emf.common.util.EList;
import org.geotools.wcs.v2_0.WCS;
import org.geotools.xml.AbstractComplexBinding;
import org.geotools.xml.ElementInstance;
import org.geotools.xml.Node;

/**
 * Custom binding for the open ended WCS:Extension element
 * 
 * @author Andrea Aime - GeoSolutions
 *
 */
public class ExtensionTypeBinding extends AbstractComplexBinding {

    public QName getTarget() {
        return WCS.ExtensionType;
    }

    public Class getType() {
        return ExtensionType.class;
    }

    
    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated modifiable
     */
    public Object parse(ElementInstance instance, Node node, Object value)
            throws Exception {
        
        ExtensionType et = Wcs20Factory.eINSTANCE.createExtensionType();
        
        EList<ExtensionItemType> contents = et.getContents();
        for(Object o : node.getChildren()) {
            Node child = (Node) o;
            String name = child.getComponent().getName();
            String namespace = child.getComponent().getNamespace();
            Object v = child.getValue();
            
            ExtensionItemType item = Wcs20Factory.eINSTANCE.createExtensionItemType();
            item.setName(name);
            item.setNamespace(namespace);
            if(v instanceof String) {
                item.setSimpleContent((String) v);
            } else {
                item.setObjectContent(v);
            }
            
            contents.add(item);
        }
        
        return et;
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
