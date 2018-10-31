package org.geotools.kml.v22.bindings;

import java.util.Map;
import javax.xml.namespace.QName;
import org.geotools.kml.v22.KML;
import org.geotools.xsd.AbstractComplexBinding;
import org.geotools.xsd.ElementInstance;
import org.geotools.xsd.Node;
import org.opengis.feature.type.Name;

/**
 * Binding object for the type http://www.opengis.net/kml/2.2:DataType.
 *
 * <p>
 *
 * <pre>
 *  <code>
 *  &lt;complexType final="#all" name="DataType"&gt;
 *      &lt;complexContent&gt;
 *          &lt;extension base="kml:AbstractObjectType"&gt;
 *              &lt;sequence&gt;
 *                  &lt;element minOccurs="0" ref="kml:displayName"/&gt;
 *                  &lt;element ref="kml:value"/&gt;
 *                  &lt;element maxOccurs="unbounded" minOccurs="0" ref="kml:DataExtension"/&gt;
 *              &lt;/sequence&gt;
 *              &lt;attribute name="name" type="string"/&gt;
 *          &lt;/extension&gt;
 *      &lt;/complexContent&gt;
 *  &lt;/complexType&gt;
 *
 *   </code>
 *  </pre>
 *
 * @generated
 */
public class DataTypeBinding extends AbstractComplexBinding {

    /** @generated */
    public QName getTarget() {
        return KML.DataType;
    }

    /**
     *
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     *
     * @generated modifiable
     */
    public Class getType() {
        return Map.Entry.class;
    }

    /**
     *
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     *
     * @generated modifiable
     */
    public Object parse(ElementInstance instance, Node node, Object value) throws Exception {

        // TODO: implement and remove call to super
        return super.parse(instance, node, value);
    }

    public Object getProperty(Object object, QName name) throws Exception {
        Map.Entry<Name, Object> data = (Map.Entry<Name, Object>) object;

        if ("name".equals(name.getLocalPart())) {
            return data.getKey().toString();
        }
        if ("value".equals(name.getLocalPart())) {
            if (data.getValue() != null) {
                return data.getValue().toString();
            } else {
                return null;
            }
        }

        return super.getProperty(object, name);
    }
}
