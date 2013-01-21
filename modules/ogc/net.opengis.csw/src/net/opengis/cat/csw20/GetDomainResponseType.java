/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package net.opengis.cat.csw20;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Get Domain Response Type</b></em>'.
 * <!-- end-user-doc -->
 *
 * <!-- begin-model-doc -->
 * Returns the actual values for some property. In general this is a
 *          subset of the value domain (that is, set of permissible values),
 *          although in some cases these may be the same.
 * <!-- end-model-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link net.opengis.cat.csw20.GetDomainResponseType#getDomainValues <em>Domain Values</em>}</li>
 * </ul>
 * </p>
 *
 * @see net.opengis.cat.csw20.Csw20Package#getGetDomainResponseType()
 * @model extendedMetaData="name='GetDomainResponseType' kind='elementOnly'"
 * @generated
 */
public interface GetDomainResponseType extends EObject {
    /**
     * Returns the value of the '<em><b>Domain Values</b></em>' containment reference list.
     * The list contents are of type {@link net.opengis.cat.csw20.DomainValuesType}.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Domain Values</em>' containment reference list isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Domain Values</em>' containment reference list.
     * @see net.opengis.cat.csw20.Csw20Package#getGetDomainResponseType_DomainValues()
     * @model containment="true" required="true"
     *        extendedMetaData="kind='element' name='DomainValues' namespace='##targetNamespace'"
     * @generated
     */
    EList<DomainValuesType> getDomainValues();

} // GetDomainResponseType
