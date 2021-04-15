/**
 */
package net.opengis.wps20;

import org.eclipse.emf.common.util.EList;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Bounding Box Data Type</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link net.opengis.wps20.BoundingBoxDataType#getSupportedCRS <em>Supported CRS</em>}</li>
 * </ul>
 *
 * @see net.opengis.wps20.Wps20Package#getBoundingBoxDataType()
 * @model extendedMetaData="name='BoundingBoxData_._type' kind='elementOnly'"
 * @generated
 */
public interface BoundingBoxDataType extends DataDescriptionType {
	/**
	 * Returns the value of the '<em><b>Supported CRS</b></em>' containment reference list.
	 * The list contents are of type {@link net.opengis.wps20.SupportedCRSType}.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * 
	 * 				Supported CRS supported for this Input/Output. "default" shall be used
	 * 				on only one element. This default element identifies the default CRS.
	 * 			
	 * <!-- end-model-doc -->
	 * @return the value of the '<em>Supported CRS</em>' containment reference list.
	 * @see net.opengis.wps20.Wps20Package#getBoundingBoxDataType_SupportedCRS()
	 * @model containment="true" required="true"
	 *        extendedMetaData="kind='element' name='SupportedCRS' namespace='##targetNamespace'"
	 * @generated
	 */
	EList<SupportedCRSType> getSupportedCRS();

} // BoundingBoxDataType
