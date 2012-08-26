/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package net.opengis.cat.csw20;

import java.lang.String;
import org.eclipse.emf.ecore.EObject;

/**
 * @model extendedMetaData="name='SimpleLiteral' kind='elementOnly'"
 */
public interface SimpleLiteral extends EObject {
    /**
     * @model 
     */
    Object getValue();
    
    /**
     * Sets the value of the '{@link net.opengis.cat.csw20.SimpleLiteral#getValue <em>Value</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Value</em>' attribute.
     * @see #getValue()
     * @generated
     */
    void setValue(Object value);

    /**
     * @model dataType="org.eclipse.emf.ecore.xml.type.AnyURI"
     *        extendedMetaData="kind='attribute' name='scheme'" 
     */
    String getScheme();

    /**
     * Sets the value of the '{@link net.opengis.cat.csw20.SimpleLiteral#getScheme <em>Scheme</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Scheme</em>' attribute.
     * @see #getScheme()
     * @generated
     */
    void setScheme(String value);


} // BriefRecordType
