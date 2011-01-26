/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package net.opengis.wps10;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Literal Data Type</b></em>'.
 * <!-- end-user-doc -->
 *
 * <!-- begin-model-doc -->
 * One simple literal value (such as an integer or real number) that is embedded in the Execute operation request or response.
 * <!-- end-model-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link net.opengis.wps10.LiteralDataType#getValue <em>Value</em>}</li>
 *   <li>{@link net.opengis.wps10.LiteralDataType#getDataType <em>Data Type</em>}</li>
 *   <li>{@link net.opengis.wps10.LiteralDataType#getUom <em>Uom</em>}</li>
 * </ul>
 * </p>
 *
 * @see net.opengis.wps10.Wps10Package#getLiteralDataType()
 * @model extendedMetaData="name='LiteralDataType' kind='simple'"
 * @generated
 */
public interface LiteralDataType extends EObject {
    /**
     * Returns the value of the '<em><b>Value</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * String containing the Literal value (e.g., "49").
     * <!-- end-model-doc -->
     * @return the value of the '<em>Value</em>' attribute.
     * @see #setValue(String)
     * @see net.opengis.wps10.Wps10Package#getLiteralDataType_Value()
     * @model dataType="org.eclipse.emf.ecore.xml.type.String"
     *        extendedMetaData="name=':0' kind='simple'"
     * @generated
     */
    String getValue();

    /**
     * Sets the value of the '{@link net.opengis.wps10.LiteralDataType#getValue <em>Value</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Value</em>' attribute.
     * @see #getValue()
     * @generated
     */
    void setValue(String value);

    /**
     * Returns the value of the '<em><b>Data Type</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * Identifies the data type of this literal input or output. This dataType should be included for each quantity whose value is not a simple string.
     * <!-- end-model-doc -->
     * @return the value of the '<em>Data Type</em>' attribute.
     * @see #setDataType(String)
     * @see net.opengis.wps10.Wps10Package#getLiteralDataType_DataType()
     * @model dataType="org.eclipse.emf.ecore.xml.type.AnyURI"
     *        extendedMetaData="kind='attribute' name='dataType'"
     * @generated
     */
    String getDataType();

    /**
     * Sets the value of the '{@link net.opengis.wps10.LiteralDataType#getDataType <em>Data Type</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Data Type</em>' attribute.
     * @see #getDataType()
     * @generated
     */
    void setDataType(String value);

    /**
     * Returns the value of the '<em><b>Uom</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * Identifies the unit of measure of this literal input or output. This unit of measure should be referenced for any numerical value that has units (e.g., "meters", but not a more complete reference system). Shall be a UOM identified in the Process description for this input or output.
     * <!-- end-model-doc -->
     * @return the value of the '<em>Uom</em>' attribute.
     * @see #setUom(String)
     * @see net.opengis.wps10.Wps10Package#getLiteralDataType_Uom()
     * @model dataType="org.eclipse.emf.ecore.xml.type.AnyURI"
     *        extendedMetaData="kind='attribute' name='uom'"
     * @generated
     */
    String getUom();

    /**
     * Sets the value of the '{@link net.opengis.wps10.LiteralDataType#getUom <em>Uom</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Uom</em>' attribute.
     * @see #getUom()
     * @generated
     */
    void setUom(String value);

} // LiteralDataType
