/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package net.opengis.cat.csw20;

import org.eclipse.emf.ecore.EObject;

import org.eclipse.emf.ecore.util.FeatureMap;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Echoed Request Type</b></em>'.
 * <!-- end-user-doc -->
 *
 * <!-- begin-model-doc -->
 * Includes a copy of the request message body.
 * <!-- end-model-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link net.opengis.cat.csw20.EchoedRequestType#getAny <em>Any</em>}</li>
 * </ul>
 * </p>
 *
 * @see net.opengis.cat.csw20.Csw20Package#getEchoedRequestType()
 * @model extendedMetaData="name='EchoedRequestType' kind='elementOnly'"
 * @generated
 */
public interface EchoedRequestType extends EObject {
    /**
     * Returns the value of the '<em><b>Any</b></em>' attribute list.
     * The list contents are of type {@link org.eclipse.emf.ecore.util.FeatureMap.Entry}.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Any</em>' attribute list isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Any</em>' attribute list.
     * @see net.opengis.cat.csw20.Csw20Package#getEchoedRequestType_Any()
     * @model dataType="org.eclipse.emf.ecore.EFeatureMapEntry" required="true" many="false"
     *        extendedMetaData="kind='elementWildcard' wildcards='##any' name=':0' processing='lax'"
     * @generated
     */
    FeatureMap getAny();

} // EchoedRequestType
