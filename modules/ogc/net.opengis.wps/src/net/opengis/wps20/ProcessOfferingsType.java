/**
 */
package net.opengis.wps20;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Process Offerings Type</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link net.opengis.wps20.ProcessOfferingsType#getProcessOffering <em>Process Offering</em>}</li>
 * </ul>
 *
 * @see net.opengis.wps20.Wps20Package#getProcessOfferingsType()
 * @model extendedMetaData="name='ProcessOfferings_._type' kind='elementOnly'"
 * @generated
 */
public interface ProcessOfferingsType extends EObject {
	/**
	 * Returns the value of the '<em><b>Process Offering</b></em>' containment reference list.
	 * The list contents are of type {@link net.opengis.wps20.ProcessOfferingType}.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * 
	 * 							Ordered list of one or more full Process
	 * 							descriptions, listed in the order in which they were requested
	 * 							in the DescribeProcess operation request.
	 * 						
	 * <!-- end-model-doc -->
	 * @return the value of the '<em>Process Offering</em>' containment reference list.
	 * @see net.opengis.wps20.Wps20Package#getProcessOfferingsType_ProcessOffering()
	 * @model containment="true" required="true"
	 *        extendedMetaData="kind='element' name='ProcessOffering' namespace='##targetNamespace'"
	 * @generated
	 */
	EList<ProcessOfferingType> getProcessOffering();

} // ProcessOfferingsType
