package org.geotools.filter.v2_0.bindings;

import org.geotools.filter.v1_0.OGCLiteralTypeBinding;
import org.geotools.filter.v2_0.FES;
import org.geotools.xml.*;
import org.opengis.filter.FilterFactory;

import javax.xml.namespace.QName;

/**
 * Binding object for the element http://www.opengis.net/fes/2.0:Literal.
 * 
 * <p>
 * 
 * <pre>
 *  <code>
 *  &lt;xsd:element name="Literal" substitutionGroup="fes:expression" type="fes:LiteralType"/&gt; 
 * 	
 *   </code>
 * </pre>
 * 
 * </p>
 * 
 * @generated
 */
public class LiteralBinding extends OGCLiteralTypeBinding {

    public LiteralBinding(FilterFactory factory) {
        super(factory);
    }

    public QName getTarget() {
        return FES.Literal;
    }
}