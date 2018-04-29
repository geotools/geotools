package org.geotools.wcs.bindings;

import javax.xml.namespace.QName;
import org.geotools.wcs.WCS;
import org.geotools.xml.AbstractComplexBinding;
import org.geotools.xml.ElementInstance;
import org.geotools.xml.Node;

/**
 * Binding object for the type http://www.opengis.net/wcs:MetadataLinkType.
 *
 * <p>
 *
 * <pre>
 *  <code>
 *  &lt;complexType name="MetadataLinkType"&gt;
 *      &lt;annotation&gt;
 *          &lt;documentation&gt;Refers to a metadata package that contains metadata properties for an object. The metadataType attribute indicates the type of metadata referred to. &lt;/documentation&gt;
 *      &lt;/annotation&gt;
 *      &lt;complexContent&gt;
 *          &lt;extension base="wcs:MetadataAssociationType"&gt;
 *              &lt;attribute name="metadataType" use="required"&gt;
 *                  &lt;simpleType&gt;
 *                      &lt;restriction base="NMTOKEN"&gt;
 *                          &lt;enumeration value="TC211"&gt;
 *                              &lt;annotation&gt;
 *                                  &lt;documentation&gt;This metadata uses a profile of ISO TC211s Geospatial Metadata Standard 19115. &lt;/documentation&gt;
 *                              &lt;/annotation&gt;
 *                          &lt;/enumeration&gt;
 *                          &lt;enumeration value="FGDC"&gt;
 *                              &lt;annotation&gt;
 *                                  &lt;documentation&gt;This metadata uses a profile of the US FGDC Content Standard for Digital Geospatial Metadata. &lt;/documentation&gt;
 *                              &lt;/annotation&gt;
 *                          &lt;/enumeration&gt;
 *                          &lt;enumeration value="other"&gt;
 *                              &lt;annotation&gt;
 *                                  &lt;documentation&gt;This metadata uses some other metadata standard(s) and/or no standard. &lt;/documentation&gt;
 *                              &lt;/annotation&gt;
 *                          &lt;/enumeration&gt;
 *                      &lt;/restriction&gt;
 *                  &lt;/simpleType&gt;
 *              &lt;/attribute&gt;
 *          &lt;/extension&gt;
 *      &lt;/complexContent&gt;
 *  &lt;/complexType&gt;
 *
 *   </code>
 * </pre>
 *
 * @generated
 * @source $URL$
 */
public class MetadataLinkTypeBinding extends AbstractComplexBinding {

    /** @generated */
    public QName getTarget() {
        return WCS.MetadataLinkType;
    }

    /**
     *
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     *
     * @generated modifiable
     */
    public Class getType() {
        return null;
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
}
