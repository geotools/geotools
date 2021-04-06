/**
 */
package net.opengis.wps20;

import java.math.BigInteger;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.util.FeatureMap;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Input Description Type</b></em>'.
 * <!-- end-user-doc -->
 *
 * <!-- begin-model-doc -->
 * Description of an input to a process. 
 * 
 * 						In this use, the DescriptionType shall describe a process input.
 * 					
 * <!-- end-model-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link net.opengis.wps20.InputDescriptionType#getDataDescriptionGroup <em>Data Description Group</em>}</li>
 *   <li>{@link net.opengis.wps20.InputDescriptionType#getDataDescription <em>Data Description</em>}</li>
 *   <li>{@link net.opengis.wps20.InputDescriptionType#getInput <em>Input</em>}</li>
 *   <li>{@link net.opengis.wps20.InputDescriptionType#getMaxOccurs <em>Max Occurs</em>}</li>
 *   <li>{@link net.opengis.wps20.InputDescriptionType#getMinOccurs <em>Min Occurs</em>}</li>
 * </ul>
 *
 * @see net.opengis.wps.wps20.Wps20Package#getInputDescriptionType()
 * @model extendedMetaData="name='InputDescriptionType' kind='elementOnly'"
 * @generated
 */
public interface InputDescriptionType extends DescriptionType {
	/**
	 * Returns the value of the '<em><b>Data Description Group</b></em>' attribute list.
	 * The list contents are of type {@link org.eclipse.emf.ecore.util.FeatureMap.Entry}.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Data Description Group</em>' attribute list.
	 * @see net.opengis.wps.wps20.Wps20Package#getInputDescriptionType_DataDescriptionGroup()
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
	 * @see net.opengis.wps.wps20.Wps20Package#getInputDescriptionType_DataDescription()
	 * @model containment="true" transient="true" changeable="false" volatile="true" derived="true"
	 *        extendedMetaData="kind='element' name='DataDescription' namespace='##targetNamespace' group='DataDescription:group'"
	 * @generated
	 */
	DataDescriptionType getDataDescription();

	/**
	 * Returns the value of the '<em><b>Input</b></em>' containment reference list.
	 * The list contents are of type {@link net.opengis.wps20.InputDescriptionType}.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Input</em>' containment reference list.
	 * @see net.opengis.wps.wps20.Wps20Package#getInputDescriptionType_Input()
	 * @model containment="true"
	 *        extendedMetaData="kind='element' name='Input' namespace='##targetNamespace'"
	 * @generated
	 */
	EList<InputDescriptionType> getInput();

	/**
	 * Returns the value of the '<em><b>Max Occurs</b></em>' attribute.
	 * The default value is <code>"1"</code>.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Max Occurs</em>' attribute.
	 * @see #isSetMaxOccurs()
	 * @see #unsetMaxOccurs()
	 * @see #setMaxOccurs(Object)
	 * @see net.opengis.wps.wps20.Wps20Package#getInputDescriptionType_MaxOccurs()
	 * @model default="1" unsettable="true" dataType="schema.AllNNI"
	 *        extendedMetaData="kind='attribute' name='maxOccurs'"
	 * @generated
	 */
	Object getMaxOccurs();

	/**
	 * Sets the value of the '{@link net.opengis.wps20.InputDescriptionType#getMaxOccurs <em>Max Occurs</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Max Occurs</em>' attribute.
	 * @see #isSetMaxOccurs()
	 * @see #unsetMaxOccurs()
	 * @see #getMaxOccurs()
	 * @generated
	 */
	void setMaxOccurs(Object value);

	/**
	 * Unsets the value of the '{@link net.opengis.wps20.InputDescriptionType#getMaxOccurs <em>Max Occurs</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isSetMaxOccurs()
	 * @see #getMaxOccurs()
	 * @see #setMaxOccurs(Object)
	 * @generated
	 */
	void unsetMaxOccurs();

	/**
	 * Returns whether the value of the '{@link net.opengis.wps20.InputDescriptionType#getMaxOccurs <em>Max Occurs</em>}' attribute is set.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return whether the value of the '<em>Max Occurs</em>' attribute is set.
	 * @see #unsetMaxOccurs()
	 * @see #getMaxOccurs()
	 * @see #setMaxOccurs(Object)
	 * @generated
	 */
	boolean isSetMaxOccurs();

	/**
	 * Returns the value of the '<em><b>Min Occurs</b></em>' attribute.
	 * The default value is <code>"1"</code>.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Min Occurs</em>' attribute.
	 * @see #isSetMinOccurs()
	 * @see #unsetMinOccurs()
	 * @see #setMinOccurs(BigInteger)
	 * @see net.opengis.wps.wps20.Wps20Package#getInputDescriptionType_MinOccurs()
	 * @model default="1" unsettable="true" dataType="org.eclipse.emf.ecore.xml.type.NonNegativeInteger"
	 *        extendedMetaData="kind='attribute' name='minOccurs'"
	 * @generated
	 */
	BigInteger getMinOccurs();

	/**
	 * Sets the value of the '{@link net.opengis.wps20.InputDescriptionType#getMinOccurs <em>Min Occurs</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Min Occurs</em>' attribute.
	 * @see #isSetMinOccurs()
	 * @see #unsetMinOccurs()
	 * @see #getMinOccurs()
	 * @generated
	 */
	void setMinOccurs(BigInteger value);

	/**
	 * Unsets the value of the '{@link net.opengis.wps20.InputDescriptionType#getMinOccurs <em>Min Occurs</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isSetMinOccurs()
	 * @see #getMinOccurs()
	 * @see #setMinOccurs(BigInteger)
	 * @generated
	 */
	void unsetMinOccurs();

	/**
	 * Returns whether the value of the '{@link net.opengis.wps20.InputDescriptionType#getMinOccurs <em>Min Occurs</em>}' attribute is set.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return whether the value of the '<em>Min Occurs</em>' attribute is set.
	 * @see #unsetMinOccurs()
	 * @see #getMinOccurs()
	 * @see #setMinOccurs(BigInteger)
	 * @generated
	 */
	boolean isSetMinOccurs();

} // InputDescriptionType
