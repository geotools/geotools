package org.geotools.sld.bindings;

import javax.xml.namespace.QName;

import org.geotools.sld.CssParameter;
import org.geotools.xml.AbstractComplexBinding;
import org.geotools.xml.ElementInstance;
import org.geotools.xml.Node;
import org.opengis.filter.FilterFactory;

/**
 * Binding object for the element http://www.opengis.net/sld:VendorOption.
 * 
 * <pre>
 * &lt;xsd:element name="VendorOption">
 *   &lt;xsd:annotation>
 *     &lt;xsd:documentation>
 *     GeoTools specific vendor extensions that allow for implementation 
 *     specific features not necessarily supported by the core SLD spec.
 *     &lt;/xsd:documentation>
 *   &lt;/xsd:annotation>
 *   &lt;xsd:complexType mixed="true">
 *     &lt;xsd:simpleContent>
 *         &lt;xsd:extension base="xsd:string">
 *            &lt;xsd:attribute name="name" type="xsd:string" />
 *         &lt;/xsd:extension>
 *     &lt;/xsd:simpleContent>
 *   &lt;/xsd:complexType>
 * &lt;/xsd:element>
 * </pre>
 * @author Justin Deoliveira, OpenGeo
 *
 *
 * @source $URL: http://svn.osgeo.org/geotools/branches/2.7.x/build/maven/javadoc/../../../modules/extension/xsd/xsd-sld/src/main/java/org/geotools/sld/bindings/SLDVendorOptionBinding.java $
 */
public class SLDVendorOptionBinding extends AbstractComplexBinding {

    FilterFactory filterFactory;
    
    public SLDVendorOptionBinding(FilterFactory filterFactory) {
        this.filterFactory = filterFactory;
    }
    
    public QName getTarget() {
        return SLD.VENDOROPTION;
    }

    public Class getType() {
        return CssParameter.class;
    }

    @Override
    public Object parse(ElementInstance instance, Node node, Object value) throws Exception {
        CssParameter option = new CssParameter((String) node.getAttributeValue("name"));
        option.setExpression(filterFactory.literal(instance.getText()));
        return option;
    }
}
