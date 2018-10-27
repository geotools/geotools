package org.geotools.wfs.v2_0.bindings;

import java.util.ArrayList;
import java.util.List;
import javax.xml.namespace.QName;
import net.opengis.wfs20.InsertType;
import net.opengis.wfs20.Wfs20Factory;
import org.eclipse.xsd.XSDElementDeclaration;
import org.geotools.gml3.v3_2.GML;
import org.geotools.wfs.v2_0.WFS;
import org.geotools.xsd.AbstractComplexEMFBinding;

public class InsertTypeBinding extends AbstractComplexEMFBinding {

    public InsertTypeBinding(Wfs20Factory factory) {
        super(factory);
    }

    public QName getTarget() {
        return WFS.InsertType;
    }

    public Class<?> getType() {
        return InsertType.class;
    }

    @Override
    public List getProperties(Object object, XSDElementDeclaration element) throws Exception {
        InsertType insert = (InsertType) object;
        List properties = new ArrayList();
        for (final Object feature : insert.getAny()) {
            properties.add(new Object[] {GML.AbstractFeature, feature});
        }
        return properties;
    }
}
