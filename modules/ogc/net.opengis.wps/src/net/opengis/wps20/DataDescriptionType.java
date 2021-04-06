/**
 */
package net.opengis.wps20;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Data Description Type</b></em>'.
 * <!-- end-user-doc -->
 *
 * <!-- begin-model-doc -->
 * Description type for process or input/output data items.
 * <!-- end-model-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link net.opengis.wps20.DataDescriptionType#getFormat <em>Format</em>}</li>
 * </ul>
 *
 * @see net.opengis.wps20.Wps20Package#getDataDescriptionType()
 * @model abstract="true"
 *        extendedMetaData="name='DataDescriptionType' kind='elementOnly'"
 * @generated
 */
public interface DataDescriptionType extends EObject {
	/**
	 * Returns the value of the '<em><b>Format</b></em>' containment reference list.
	 * The list contents are of type {@link net.opengis.wps20.FormatType}.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Format</em>' containment reference list.
	 * @see net.opengis.wps20.Wps20Package#getDataDescriptionType_Format()
	 * @model containment="true" required="true"
	 *        extendedMetaData="kind='element' name='Format' namespace='##targetNamespace'"
	 * @generated
	 */
	EList<FormatType> getFormat();

} // DataDescriptionType
