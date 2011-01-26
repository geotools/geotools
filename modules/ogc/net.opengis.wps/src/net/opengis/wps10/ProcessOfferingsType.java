/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package net.opengis.wps10;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Process Offerings Type</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link net.opengis.wps10.ProcessOfferingsType#getProcess <em>Process</em>}</li>
 * </ul>
 * </p>
 *
 * @see net.opengis.wps10.Wps10Package#getProcessOfferingsType()
 * @model extendedMetaData="name='ProcessOfferings_._type' kind='elementOnly'"
 * @generated
 */
public interface ProcessOfferingsType extends EObject {
    /**
     * Returns the value of the '<em><b>Process</b></em>' containment reference list.
     * The list contents are of type {@link net.opengis.wps10.ProcessBriefType}.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * Unordered list of one or more brief descriptions of all the processes offered by this WPS server.
     * <!-- end-model-doc -->
     * @return the value of the '<em>Process</em>' containment reference list.
     * @see net.opengis.wps10.Wps10Package#getProcessOfferingsType_Process()
     * @model type="net.opengis.wps10.ProcessBriefType" containment="true" required="true"
     *        extendedMetaData="kind='element' name='Process' namespace='##targetNamespace'"
     * @generated
     */
    EList getProcess();

} // ProcessOfferingsType
