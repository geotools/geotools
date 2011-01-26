/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package net.opengis.wps10;

import java.math.BigInteger;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Input Description Type</b></em>'.
 * <!-- end-user-doc -->
 *
 * <!-- begin-model-doc -->
 * Description of an input to a process.
 * In this use, the DescriptionType shall describe this process input.
 * <!-- end-model-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link net.opengis.wps10.InputDescriptionType#getComplexData <em>Complex Data</em>}</li>
 *   <li>{@link net.opengis.wps10.InputDescriptionType#getLiteralData <em>Literal Data</em>}</li>
 *   <li>{@link net.opengis.wps10.InputDescriptionType#getBoundingBoxData <em>Bounding Box Data</em>}</li>
 *   <li>{@link net.opengis.wps10.InputDescriptionType#getMaxOccurs <em>Max Occurs</em>}</li>
 *   <li>{@link net.opengis.wps10.InputDescriptionType#getMinOccurs <em>Min Occurs</em>}</li>
 * </ul>
 * </p>
 *
 * @see net.opengis.wps10.Wps10Package#getInputDescriptionType()
 * @model extendedMetaData="name='InputDescriptionType' kind='elementOnly'"
 * @generated
 */
public interface InputDescriptionType extends DescriptionType {
    /**
     * Returns the value of the '<em><b>Complex Data</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * Indicates that this Input shall be a complex data structure (such as a GML document), and provides a list of Formats, Encodings, and Schemas supported for this Input. The value of this ComplexData structure can be input either embedded in the Execute request or remotely accessible to the server.  The client can select from among the identified combinations of Formats, Encodings, and Schemas to specify the form of the Input. This allows for complete specification of particular versions of GML, or image formats.
     * <!-- end-model-doc -->
     * @return the value of the '<em>Complex Data</em>' containment reference.
     * @see #setComplexData(SupportedComplexDataInputType)
     * @see net.opengis.wps10.Wps10Package#getInputDescriptionType_ComplexData()
     * @model containment="true"
     *        extendedMetaData="kind='element' name='ComplexData'"
     * @generated
     */
    SupportedComplexDataInputType getComplexData();

    /**
     * Sets the value of the '{@link net.opengis.wps10.InputDescriptionType#getComplexData <em>Complex Data</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Complex Data</em>' containment reference.
     * @see #getComplexData()
     * @generated
     */
    void setComplexData(SupportedComplexDataInputType value);

    /**
     * Returns the value of the '<em><b>Literal Data</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * Indicates that this Input shall be a simple numeric value or character string that is embedded in the execute request, and describes the possible values.
     * <!-- end-model-doc -->
     * @return the value of the '<em>Literal Data</em>' containment reference.
     * @see #setLiteralData(LiteralInputType)
     * @see net.opengis.wps10.Wps10Package#getInputDescriptionType_LiteralData()
     * @model containment="true"
     *        extendedMetaData="kind='element' name='LiteralData'"
     * @generated
     */
    LiteralInputType getLiteralData();

    /**
     * Sets the value of the '{@link net.opengis.wps10.InputDescriptionType#getLiteralData <em>Literal Data</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Literal Data</em>' containment reference.
     * @see #getLiteralData()
     * @generated
     */
    void setLiteralData(LiteralInputType value);

    /**
     * Returns the value of the '<em><b>Bounding Box Data</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * Indicates that this Input shall be a BoundingBox data structure that is embedded in the execute request, and provides a list of the Coordinate Reference System support for this Bounding Box.
     * <!-- end-model-doc -->
     * @return the value of the '<em>Bounding Box Data</em>' containment reference.
     * @see #setBoundingBoxData(SupportedCRSsType)
     * @see net.opengis.wps10.Wps10Package#getInputDescriptionType_BoundingBoxData()
     * @model containment="true"
     *        extendedMetaData="kind='element' name='BoundingBoxData'"
     * @generated
     */
    SupportedCRSsType getBoundingBoxData();

    /**
     * Sets the value of the '{@link net.opengis.wps10.InputDescriptionType#getBoundingBoxData <em>Bounding Box Data</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Bounding Box Data</em>' containment reference.
     * @see #getBoundingBoxData()
     * @generated
     */
    void setBoundingBoxData(SupportedCRSsType value);

    /**
     * Returns the value of the '<em><b>Max Occurs</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * The maximum number of times that values for this parameter are permitted in an Execute request. If "1" then this parameter may appear only once in an Execute request.  If greater than "1", then this input parameter may appear that many times in an Execute request.
     * <!-- end-model-doc -->
     * @return the value of the '<em>Max Occurs</em>' attribute.
     * @see #setMaxOccurs(BigInteger)
     * @see net.opengis.wps10.Wps10Package#getInputDescriptionType_MaxOccurs()
     * @model dataType="org.eclipse.emf.ecore.xml.type.PositiveInteger" required="true"
     *        extendedMetaData="kind='attribute' name='maxOccurs'"
     * @generated
     */
    BigInteger getMaxOccurs();

    /**
     * Sets the value of the '{@link net.opengis.wps10.InputDescriptionType#getMaxOccurs <em>Max Occurs</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Max Occurs</em>' attribute.
     * @see #getMaxOccurs()
     * @generated
     */
    void setMaxOccurs(BigInteger value);

    /**
     * Returns the value of the '<em><b>Min Occurs</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * The minimum number of times that values for this parameter are required in an Execute request.  If "0", this data input is optional. If greater than "0" then this process input is required.
     * <!-- end-model-doc -->
     * @return the value of the '<em>Min Occurs</em>' attribute.
     * @see #setMinOccurs(BigInteger)
     * @see net.opengis.wps10.Wps10Package#getInputDescriptionType_MinOccurs()
     * @model dataType="org.eclipse.emf.ecore.xml.type.NonNegativeInteger" required="true"
     *        extendedMetaData="kind='attribute' name='minOccurs'"
     * @generated
     */
    BigInteger getMinOccurs();

    /**
     * Sets the value of the '{@link net.opengis.wps10.InputDescriptionType#getMinOccurs <em>Min Occurs</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Min Occurs</em>' attribute.
     * @see #getMinOccurs()
     * @generated
     */
    void setMinOccurs(BigInteger value);

} // InputDescriptionType
