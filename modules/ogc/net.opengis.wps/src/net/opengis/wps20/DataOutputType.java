/**
 */
package net.opengis.wps20;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Data Output Type</b></em>'.
 * <!-- end-user-doc -->
 *
 * <!-- begin-model-doc -->
 * 
 * 				This type describes a process output in the execute response.
 * 			
 * <!-- end-model-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link net.opengis.wps20.DataOutputType#getData <em>Data</em>}</li>
 *   <li>{@link net.opengis.wps20.DataOutputType#getReference <em>Reference</em>}</li>
 *   <li>{@link net.opengis.wps20.DataOutputType#getOutput <em>Output</em>}</li>
 *   <li>{@link net.opengis.wps20.DataOutputType#getId <em>Id</em>}</li>
 * </ul>
 *
 * @see net.opengis.wps20.Wps20Package#getDataOutputType()
 * @model extendedMetaData="name='DataOutputType' kind='elementOnly'"
 * @generated
 */
public interface DataOutputType extends EObject {
	/**
	 * Returns the value of the '<em><b>Data</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Data</em>' containment reference.
	 * @see #setData(DataType)
	 * @see net.opengis.wps20.Wps20Package#getDataOutputType_Data()
	 * @model containment="true"
	 *        extendedMetaData="kind='element' name='Data' namespace='##targetNamespace'"
	 * @generated
	 */
	DataType getData();

	/**
	 * Sets the value of the '{@link net.opengis.wps20.DataOutputType#getData <em>Data</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Data</em>' containment reference.
	 * @see #getData()
	 * @generated
	 */
	void setData(DataType value);

	/**
	 * Returns the value of the '<em><b>Reference</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * 
	 * 				This element is used for web accessible references to a data set or value.
	 * 			
	 * <!-- end-model-doc -->
	 * @return the value of the '<em>Reference</em>' containment reference.
	 * @see #setReference(ReferenceType)
	 * @see net.opengis.wps20.Wps20Package#getDataOutputType_Reference()
	 * @model containment="true"
	 *        extendedMetaData="kind='element' name='Reference' namespace='##targetNamespace'"
	 * @generated
	 */
	ReferenceType getReference();

	/**
	 * Sets the value of the '{@link net.opengis.wps20.DataOutputType#getReference <em>Reference</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Reference</em>' containment reference.
	 * @see #getReference()
	 * @generated
	 */
	void setReference(ReferenceType value);

	/**
	 * Returns the value of the '<em><b>Output</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Output</em>' containment reference.
	 * @see #setOutput(DataOutputType)
	 * @see net.opengis.wps20.Wps20Package#getDataOutputType_Output()
	 * @model containment="true"
	 *        extendedMetaData="kind='element' name='Output' namespace='##targetNamespace'"
	 * @generated
	 */
	DataOutputType getOutput();

	/**
	 * Sets the value of the '{@link net.opengis.wps20.DataOutputType#getOutput <em>Output</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Output</em>' containment reference.
	 * @see #getOutput()
	 * @generated
	 */
	void setOutput(DataOutputType value);

	/**
	 * Returns the value of the '<em><b>Id</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * 
	 * 					Identifier of this output.
	 * 				
	 * <!-- end-model-doc -->
	 * @return the value of the '<em>Id</em>' attribute.
	 * @see #setId(String)
	 * @see net.opengis.wps20.Wps20Package#getDataOutputType_Id()
	 * @model dataType="org.eclipse.emf.ecore.xml.type.AnyURI" required="true"
	 *        extendedMetaData="kind='attribute' name='id'"
	 * @generated
	 */
	String getId();

	/**
	 * Sets the value of the '{@link net.opengis.wps20.DataOutputType#getId <em>Id</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Id</em>' attribute.
	 * @see #getId()
	 * @generated
	 */
	void setId(String value);

} // DataOutputType
