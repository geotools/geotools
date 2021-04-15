/**
 */
package net.opengis.wps20;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.util.FeatureMap;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Output Description Type</b></em>'.
 * <!-- end-user-doc -->
 *
 * <!-- begin-model-doc -->
 * Description of a process Output. 
 * 
 * 						In this use, the DescriptionType shall describe a process output.
 * 					
 * <!-- end-model-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link net.opengis.wps20.OutputDescriptionType#getDataDescriptionGroup <em>Data Description Group</em>}</li>
 *   <li>{@link net.opengis.wps20.OutputDescriptionType#getDataDescription <em>Data Description</em>}</li>
 *   <li>{@link net.opengis.wps20.OutputDescriptionType#getOutput <em>Output</em>}</li>
 * </ul>
 *
 * @see net.opengis.wps20.Wps20Package#getOutputDescriptionType()
 * @model extendedMetaData="name='OutputDescriptionType' kind='elementOnly'"
 * @generated
 */
public interface OutputDescriptionType extends DescriptionType {
	/**
	 * Returns the value of the '<em><b>Data Description Group</b></em>' attribute list.
	 * The list contents are of type {@link org.eclipse.emf.ecore.util.FeatureMap.Entry}.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Data Description Group</em>' attribute list.
	 * @see net.opengis.wps20.Wps20Package#getOutputDescriptionType_DataDescriptionGroup()
	 * @model dataType="org.eclipse.emf.ecore.EFeatureMapEntry" many="false"
	 *        extendedMetaData="kind='group' name='DataDescription:group' namespace='##targetNamespace'"
	 * @generated
	 */
	FeatureMap getDataDescriptionGroup();

	/**
	 * Returns the value of the '<em><b>Data Description</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Data Description</em>' containment reference.
	 * @see net.opengis.wps20.Wps20Package#getOutputDescriptionType_DataDescription()
	 * @model containment="true" transient="true" changeable="false" volatile="true" derived="true"
	 *        extendedMetaData="kind='element' name='DataDescription' namespace='##targetNamespace' group='DataDescription:group'"
	 * @generated
	 */
	DataDescriptionType getDataDescription();

	/**
	 * Returns the value of the '<em><b>Output</b></em>' containment reference list.
	 * The list contents are of type {@link net.opengis.wps20.OutputDescriptionType}.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Output</em>' containment reference list.
	 * @see net.opengis.wps20.Wps20Package#getOutputDescriptionType_Output()
	 * @model containment="true"
	 *        extendedMetaData="kind='element' name='Output' namespace='##targetNamespace'"
	 * @generated
	 */
	EList<OutputDescriptionType> getOutput();

} // OutputDescriptionType
