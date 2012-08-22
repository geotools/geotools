/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package net.opengis.cat.csw20;

import java.lang.String;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Get Record By Id Type</b></em>'.
 * <!-- end-user-doc -->
 *
 * <!-- begin-model-doc -->
 * 
 *             Convenience operation to retrieve default record representations
 *             by identifier.
 *             Id - object identifier (a URI) that provides a reference to a
 *                  catalogue item (or a result set if the catalogue supports
 *                  persistent result sets).
 *             ElementSetName - one of "brief, "summary", or "full"
 * <!-- end-model-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link net.opengis.cat.csw20.GetRecordByIdType#getId <em>Id</em>}</li>
 *   <li>{@link net.opengis.cat.csw20.GetRecordByIdType#getElementSetName <em>Element Set Name</em>}</li>
 *   <li>{@link net.opengis.cat.csw20.GetRecordByIdType#getOutputFormat <em>Output Format</em>}</li>
 *   <li>{@link net.opengis.cat.csw20.GetRecordByIdType#getOutputSchema <em>Output Schema</em>}</li>
 * </ul>
 * </p>
 *
 * @see net.opengis.cat.csw20.Csw20Package#getGetRecordByIdType()
 * @model extendedMetaData="name='GetRecordByIdType' kind='elementOnly'"
 * @generated
 */
public interface GetRecordByIdType extends RequestBaseType {
    /**
     * Returns the value of the '<em><b>Id</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Id</em>' attribute isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Id</em>' attribute.
     * @see #setId(String)
     * @see net.opengis.cat.csw20.Csw20Package#getGetRecordByIdType_Id()
     * @model unique="false" dataType="org.eclipse.emf.ecore.xml.type.AnyURI" required="true"
     *        extendedMetaData="kind='element' name='Id' namespace='##targetNamespace'"
     * @generated
     */
    String getId();

    /**
     * Sets the value of the '{@link net.opengis.cat.csw20.GetRecordByIdType#getId <em>Id</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Id</em>' attribute.
     * @see #getId()
     * @generated
     */
    void setId(String value);

    /**
     * Returns the value of the '<em><b>Element Set Name</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Element Set Name</em>' containment reference isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Element Set Name</em>' containment reference.
     * @see #setElementSetName(ElementSetNameType)
     * @see net.opengis.cat.csw20.Csw20Package#getGetRecordByIdType_ElementSetName()
     * @model containment="true"
     *        extendedMetaData="kind='element' name='ElementSetName' namespace='##targetNamespace'"
     * @generated
     */
    ElementSetNameType getElementSetName();

    /**
     * Sets the value of the '{@link net.opengis.cat.csw20.GetRecordByIdType#getElementSetName <em>Element Set Name</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Element Set Name</em>' containment reference.
     * @see #getElementSetName()
     * @generated
     */
    void setElementSetName(ElementSetNameType value);

    /**
     * Returns the value of the '<em><b>Output Format</b></em>' attribute.
     * The default value is <code>"application/xml"</code>.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Output Format</em>' attribute isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Output Format</em>' attribute.
     * @see #isSetOutputFormat()
     * @see #unsetOutputFormat()
     * @see #setOutputFormat(String)
     * @see net.opengis.cat.csw20.Csw20Package#getGetRecordByIdType_OutputFormat()
     * @model default="application/xml" unsettable="true" dataType="org.eclipse.emf.ecore.xml.type.String"
     *        extendedMetaData="kind='attribute' name='outputFormat'"
     * @generated
     */
    String getOutputFormat();

    /**
     * Sets the value of the '{@link net.opengis.cat.csw20.GetRecordByIdType#getOutputFormat <em>Output Format</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Output Format</em>' attribute.
     * @see #isSetOutputFormat()
     * @see #unsetOutputFormat()
     * @see #getOutputFormat()
     * @generated
     */
    void setOutputFormat(String value);

    /**
     * Unsets the value of the '{@link net.opengis.cat.csw20.GetRecordByIdType#getOutputFormat <em>Output Format</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #isSetOutputFormat()
     * @see #getOutputFormat()
     * @see #setOutputFormat(String)
     * @generated
     */
    void unsetOutputFormat();

    /**
     * Returns whether the value of the '{@link net.opengis.cat.csw20.GetRecordByIdType#getOutputFormat <em>Output Format</em>}' attribute is set.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return whether the value of the '<em>Output Format</em>' attribute is set.
     * @see #unsetOutputFormat()
     * @see #getOutputFormat()
     * @see #setOutputFormat(String)
     * @generated
     */
    boolean isSetOutputFormat();

    /**
     * Returns the value of the '<em><b>Output Schema</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Output Schema</em>' attribute isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Output Schema</em>' attribute.
     * @see #setOutputSchema(String)
     * @see net.opengis.cat.csw20.Csw20Package#getGetRecordByIdType_OutputSchema()
     * @model dataType="org.eclipse.emf.ecore.xml.type.AnyURI"
     *        extendedMetaData="kind='attribute' name='outputSchema'"
     * @generated
     */
    String getOutputSchema();

    /**
     * Sets the value of the '{@link net.opengis.cat.csw20.GetRecordByIdType#getOutputSchema <em>Output Schema</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Output Schema</em>' attribute.
     * @see #getOutputSchema()
     * @generated
     */
    void setOutputSchema(String value);

} // GetRecordByIdType
