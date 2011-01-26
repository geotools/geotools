package org.geotools.gml3.bindings;

import java.util.List;

import javax.xml.namespace.QName;
import org.geotools.gml2.bindings.GML2EncodingUtils;
import org.geotools.gml3.XSDIdRegistry;
import org.geotools.xml.AbstractComplexBinding;
import org.geotools.xml.ElementInstance;
import org.geotools.xml.Node;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import com.vividsolutions.jts.geom.Geometry;

public abstract class GeometryPropertyTypeBindingBase extends AbstractComplexBinding {

    private XSDIdRegistry idSet;

    private boolean makeEmpty = false;

    private GML3EncodingUtils encodingUtils;

    public GeometryPropertyTypeBindingBase(GML3EncodingUtils encodingUtils, XSDIdRegistry idRegistry) {
        this.idSet = idRegistry;
        this.encodingUtils = encodingUtils;
    }

    public Class getType() {
        return getGeometryType();
    }

    public Class<? extends Geometry> getGeometryType() {
        return Geometry.class;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated modifiable
     */
    public Object parse(ElementInstance instance, Node node, Object value) throws Exception {
        return node.getChildValue(getGeometryType());
    }

    /**
     * @see org.geotools.xml.AbstractComplexBinding#encode(java.lang.Object, org.w3c.dom.Document,
     *      org.w3c.dom.Element)
     */
    @Override
    public Element encode(Object object, Document document, Element value) throws Exception {
        checkExistingId((Geometry) object);
        return value;
    }

    public Object getProperty(Object object, QName name) throws Exception {

        return encodingUtils.GeometryPropertyType_GetProperty((Geometry) object, name, makeEmpty);
    }

    public List getProperties(Object object) throws Exception {
        return encodingUtils.GeometryPropertyType_GetProperties((Geometry) object);
    }

    /**
     * Check if the geometry contains a feature which id is pre-existing in the document. If it's
     * true, make the geometry empty and add xlink:href property
     * 
     * @param value
     *            The complex attribute value
     * @param att
     *            The complex attribute itself
     */
    private void checkExistingId(Geometry geom) {
        if (geom != null) {
            String id = GML2EncodingUtils.getID(geom);

            if (id != null && idSet.idExists(id)) {
                // make geometry empty, href will added by getproperty
                makeEmpty = true;

            } else if (id != null) {

                idSet.add(id);
            }
        }
        return;
    }

}
