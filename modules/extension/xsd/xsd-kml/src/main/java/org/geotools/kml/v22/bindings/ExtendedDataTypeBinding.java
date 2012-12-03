package org.geotools.kml.v22.bindings;

import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.xml.namespace.QName;

import org.geotools.kml.v22.KML;
import org.geotools.xml.AbstractComplexBinding;
import org.geotools.xml.ElementInstance;
import org.geotools.xml.Node;

/**
 * Binding object for the type http://www.opengis.net/kml/2.2:ExtendedDataType.
 * 
 * <p>
 * 
 * <pre>
 *  <code>
 *  &lt;complexType final="#all" name="ExtendedDataType"&gt;
 *      &lt;sequence&gt;
 *          &lt;element maxOccurs="unbounded" minOccurs="0" ref="kml:Data"/&gt;
 *          &lt;element maxOccurs="unbounded" minOccurs="0" ref="kml:SchemaData"/&gt;
 *          &lt;any maxOccurs="unbounded" minOccurs="0" namespace="##other" processContents="lax"/&gt;
 *      &lt;/sequence&gt;
 *  &lt;/complexType&gt; 
 * 	
 *   </code>
 * </pre>
 * 
 * </p>
 * 
 * @generated
 */
public class ExtendedDataTypeBinding extends AbstractComplexBinding {

    /**
     * @generated
     */
    public QName getTarget() {
        return KML.ExtendedDataType;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated modifiable
     */
    @SuppressWarnings("rawtypes")
    public Class getType() {
        return Map.class;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated modifiable
     */
    @SuppressWarnings("unchecked")
    public Object parse(ElementInstance instance, Node node, Object value) throws Exception {

        Map<String, Object> extendedData = new HashMap<String, Object>();

        Map<String, Object> unTypedData = new LinkedHashMap<String, Object>();
        for (Node n : (List<Node>)node.getChildren("Data")) {
            unTypedData.put((String) n.getAttributeValue("name"), n.getChildValue("value"));
        }

        Map<String, Object> typedData = new LinkedHashMap<String, Object>();
        List<URI> schemas = new ArrayList<URI>();
        for (Node schemaData : (List<Node>)node.getChildren("SchemaData")) {
            URI schemaUrl = (URI) schemaData.getAttributeValue("schemaUrl");
            if (schemaUrl != null) {
                for (Node n : (List<Node>)schemaData.getChildren("SimpleData")) {
                    typedData.put((String) n.getAttributeValue("name"), n.getValue());
                }
                schemas.add(schemaUrl);
            }
        }

        extendedData.put("schemas", schemas);
        extendedData.put("untyped", unTypedData);
        extendedData.put("typed", typedData);
        return extendedData;
    }

}
