package org.geotools.wmts.bindings;

import javax.xml.namespace.QName;
import net.opengis.ows11.SectionsType;
import net.opengis.wmts.v_1.wmtsv_1Factory;
import org.geotools.wmts.WMTS;
import org.geotools.xsd.AbstractSimpleBinding;
import org.geotools.xsd.InstanceComponent;

/**
 * Binding object for the type http://www.opengis.net/wmts/1.0:SectionsType.
 *
 * <p>
 *
 * <pre>
 *  <code>
 *  &lt;?xml version="1.0" encoding="UTF-8"?&gt;&lt;simpleType name="SectionsType" xmlns="http://www.w3.org/2001/XMLSchema"&gt;
 *  		&lt;annotation&gt;
 *  			&lt;documentation&gt;
 *  				XML encoded identifier comma separated list of a standard
 *  				MIME type, possibly a parameterized MIME type.
 *  			&lt;/documentation&gt;
 *  		&lt;/annotation&gt;
 *  		&lt;restriction base="string"&gt;
 *  			&lt;annotation&gt;
 *  				&lt;documentation&gt;Comma separated list of available
 *  				ServiceMetadata root elements. &lt;/documentation&gt;
 *  			&lt;/annotation&gt;
 *  			&lt;pattern value="(ServiceIdentification|ServiceProvider|OperationsMetadata|Contents|Themes)(,(ServiceIdentification|ServiceProvider|OperationsMetadata|Contents|Themes))*"/&gt;
 *  		&lt;/restriction&gt;
 *  	&lt;/simpleType&gt;
 *
 *   </code>
 *  </pre>
 *
 * @generated
 */
public class SectionsTypeBinding extends AbstractSimpleBinding {

    wmtsv_1Factory factory;

    public SectionsTypeBinding(wmtsv_1Factory factory) {
        super();
        this.factory = factory;
    }

    /** @generated */
    public QName getTarget() {
        return WMTS.SectionsType;
    }

    /**
     *
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     *
     * @generated modifiable
     */
    public Class getType() {
        return SectionsType.class;
    }

    /**
     *
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     *
     * @generated modifiable
     */
    public Object parse(InstanceComponent instance, Object value) throws Exception {

        // TODO: implement and remove call to super
        return super.parse(instance, value);
    }
}
