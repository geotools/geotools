/**
 */
package net.opengis.wps20;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Contents Type</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link net.opengis.wps20.ContentsType#getProcessSummary <em>Process Summary</em>}</li>
 * </ul>
 *
 * @see net.opengis.wps20.Wps20Package#getContentsType()
 * @model extendedMetaData="name='Contents_._type' kind='elementOnly'"
 * @generated
 */
public interface ContentsType extends EObject {
	/**
	 * Returns the value of the '<em><b>Process Summary</b></em>' containment reference list.
	 * The list contents are of type {@link net.opengis.wps20.ProcessSummaryType}.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * Unordered list of one or more brief descriptions of all the processes offered by this WPS server. 
	 * <!-- end-model-doc -->
	 * @return the value of the '<em>Process Summary</em>' containment reference list.
	 * @see net.opengis.wps20.Wps20Package#getContentsType_ProcessSummary()
	 * @model containment="true" required="true"
	 *        extendedMetaData="kind='element' name='ProcessSummary' namespace='##targetNamespace'"
	 * @generated
	 */
	EList<ProcessSummaryType> getProcessSummary();

} // ContentsType
