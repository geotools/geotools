/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package net.opengis.wps10;

import net.opengis.ows11.BoundingBoxType;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Data Type</b></em>'.
 * <!-- end-user-doc -->
 *
 * <!-- begin-model-doc -->
 * Identifies the form of this input or output value, and provides supporting information.
 * <!-- end-model-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link net.opengis.wps10.DataType#getComplexData <em>Complex Data</em>}</li>
 *   <li>{@link net.opengis.wps10.DataType#getLiteralData <em>Literal Data</em>}</li>
 *   <li>{@link net.opengis.wps10.DataType#getBoundingBoxData <em>Bounding Box Data</em>}</li>
 * </ul>
 * </p>
 *
 * @see net.opengis.wps10.Wps10Package#getDataType()
 * @model extendedMetaData="name='DataType' kind='elementOnly'"
 * @generated
 */
public interface DataType extends EObject {
    /**
     * Returns the value of the '<em><b>Complex Data</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * Identifies this input or output value as a complex data structure encoded in XML (e.g., using GML), and provides that complex data structure. For an input, this element may be used by a client for any process input coded as ComplexData in the ProcessDescription. For an output, this element shall be used by a server when "store" in the Execute request is "false".
     * <!-- end-model-doc -->
     * @return the value of the '<em>Complex Data</em>' containment reference.
     * @see #setComplexData(ComplexDataType)
     * @see net.opengis.wps10.Wps10Package#getDataType_ComplexData()
     * @model containment="true"
     *        extendedMetaData="kind='element' name='ComplexData' namespace='##targetNamespace'"
     * @generated
     */
    ComplexDataType getComplexData();

    /**
     * Sets the value of the '{@link net.opengis.wps10.DataType#getComplexData <em>Complex Data</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Complex Data</em>' containment reference.
     * @see #getComplexData()
     * @generated
     */
    void setComplexData(ComplexDataType value);

    /**
     * Returns the value of the '<em><b>Literal Data</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * Identifies this input or output data as literal data of a simple quantity (e.g., one number), and provides that data.
     * <!-- end-model-doc -->
     * @return the value of the '<em>Literal Data</em>' containment reference.
     * @see #setLiteralData(LiteralDataType)
     * @see net.opengis.wps10.Wps10Package#getDataType_LiteralData()
     * @model containment="true"
     *        extendedMetaData="kind='element' name='LiteralData' namespace='##targetNamespace'"
     * @generated
     */
    LiteralDataType getLiteralData();

    /**
     * Sets the value of the '{@link net.opengis.wps10.DataType#getLiteralData <em>Literal Data</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Literal Data</em>' containment reference.
     * @see #getLiteralData()
     * @generated
     */
    void setLiteralData(LiteralDataType value);

    /**
     * Returns the value of the '<em><b>Bounding Box Data</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * Identifies this input or output data as an ows:BoundingBox data structure, and provides that ows:BoundingBox data.
     * <!-- end-model-doc -->
     * @return the value of the '<em>Bounding Box Data</em>' containment reference.
     * @see #setBoundingBoxData(BoundingBoxType)
     * @see net.opengis.wps10.Wps10Package#getDataType_BoundingBoxData()
     * @model containment="true"
     *        extendedMetaData="kind='element' name='BoundingBoxData' namespace='##targetNamespace'"
     * @generated
     */
    BoundingBoxType getBoundingBoxData();

    /**
     * Sets the value of the '{@link net.opengis.wps10.DataType#getBoundingBoxData <em>Bounding Box Data</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Bounding Box Data</em>' containment reference.
     * @see #getBoundingBoxData()
     * @generated
     */
    void setBoundingBoxData(BoundingBoxType value);

} // DataType
