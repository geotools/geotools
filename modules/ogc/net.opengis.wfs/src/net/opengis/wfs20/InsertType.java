/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package net.opengis.wfs20;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.util.FeatureMap;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Insert Type</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link net.opengis.wfs20.InsertType#getAny <em>Any</em>}</li>
 *   <li>{@link net.opengis.wfs20.InsertType#getInputFormat <em>Input Format</em>}</li>
 *   <li>{@link net.opengis.wfs20.InsertType#getSrsName <em>Srs Name</em>}</li>
 * </ul>
 * </p>
 *
 * @see net.opengis.wfs20.Wfs20Package#getInsertType()
 * @model extendedMetaData="name='InsertType' kind='elementOnly'"
 * @generated
 */
public interface InsertType extends AbstractTransactionActionType {
    /**
     * Returns the value of the '<em><b>Any</b></em>' attribute list.
     * The list contents are of type {@link org.eclipse.emf.ecore.util.FeatureMap.Entry}.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Any</em>' attribute list isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Any</em>' attribute list.
     * @see net.opengis.wfs20.Wfs20Package#getInsertType_Any()
     * @model 
     */
    EList<Object> getAny();

    /**
     * Returns the value of the '<em><b>Input Format</b></em>' attribute.
     * The default value is <code>"application/gml+xml; version=3.2"</code>.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Input Format</em>' attribute isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Input Format</em>' attribute.
     * @see #isSetInputFormat()
     * @see #unsetInputFormat()
     * @see #setInputFormat(String)
     * @see net.opengis.wfs20.Wfs20Package#getInsertType_InputFormat()
     * @model default="application/gml+xml; version=3.2" unsettable="true" dataType="org.eclipse.emf.ecore.xml.type.String"
     *        extendedMetaData="kind='attribute' name='inputFormat'"
     * @generated
     */
    String getInputFormat();

    /**
     * Sets the value of the '{@link net.opengis.wfs20.InsertType#getInputFormat <em>Input Format</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Input Format</em>' attribute.
     * @see #isSetInputFormat()
     * @see #unsetInputFormat()
     * @see #getInputFormat()
     * @generated
     */
    void setInputFormat(String value);

    /**
     * Unsets the value of the '{@link net.opengis.wfs20.InsertType#getInputFormat <em>Input Format</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #isSetInputFormat()
     * @see #getInputFormat()
     * @see #setInputFormat(String)
     * @generated
     */
    void unsetInputFormat();

    /**
     * Returns whether the value of the '{@link net.opengis.wfs20.InsertType#getInputFormat <em>Input Format</em>}' attribute is set.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return whether the value of the '<em>Input Format</em>' attribute is set.
     * @see #unsetInputFormat()
     * @see #getInputFormat()
     * @see #setInputFormat(String)
     * @generated
     */
    boolean isSetInputFormat();

    /**
     * Returns the value of the '<em><b>Srs Name</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Srs Name</em>' attribute isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Srs Name</em>' attribute.
     * @see #setSrsName(String)
     * @see net.opengis.wfs20.Wfs20Package#getInsertType_SrsName()
     * @model dataType="org.eclipse.emf.ecore.xml.type.AnyURI"
     *        extendedMetaData="kind='attribute' name='srsName'"
     * @generated
     */
    String getSrsName();

    /**
     * Sets the value of the '{@link net.opengis.wfs20.InsertType#getSrsName <em>Srs Name</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Srs Name</em>' attribute.
     * @see #getSrsName()
     * @generated
     */
    void setSrsName(String value);

} // InsertType
