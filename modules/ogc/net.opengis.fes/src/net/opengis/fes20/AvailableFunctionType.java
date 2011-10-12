/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package net.opengis.fes20;

import javax.xml.namespace.QName;

import net.opengis.ows11.MetadataType;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Available Function Type</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link net.opengis.fes20.AvailableFunctionType#getMetadata <em>Metadata</em>}</li>
 *   <li>{@link net.opengis.fes20.AvailableFunctionType#getReturns <em>Returns</em>}</li>
 *   <li>{@link net.opengis.fes20.AvailableFunctionType#getArguments <em>Arguments</em>}</li>
 *   <li>{@link net.opengis.fes20.AvailableFunctionType#getName <em>Name</em>}</li>
 * </ul>
 * </p>
 *
 * @see net.opengis.fes20.Fes20Package#getAvailableFunctionType()
 * @model extendedMetaData="name='AvailableFunctionType' kind='elementOnly'"
 * @generated
 */
public interface AvailableFunctionType extends EObject {
    /**
     * Returns the value of the '<em><b>Metadata</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Metadata</em>' containment reference isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Metadata</em>' containment reference.
     * @see #setMetadata(MetadataType)
     * @see net.opengis.fes20.Fes20Package#getAvailableFunctionType_Metadata()
     * @model containment="true"
     *        extendedMetaData="kind='element' name='Metadata' namespace='http://www.opengis.net/ows/1.1'"
     * @generated
     */
    MetadataType getMetadata();

    /**
     * Sets the value of the '{@link net.opengis.fes20.AvailableFunctionType#getMetadata <em>Metadata</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Metadata</em>' containment reference.
     * @see #getMetadata()
     * @generated
     */
    void setMetadata(MetadataType value);

    /**
     * Returns the value of the '<em><b>Returns</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Returns</em>' attribute isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Returns</em>' attribute.
     * @see #setReturns(QName)
     * @see net.opengis.fes20.Fes20Package#getAvailableFunctionType_Returns()
     * @model dataType="org.eclipse.emf.ecore.xml.type.QName" required="true"
     *        extendedMetaData="kind='element' name='Returns' namespace='##targetNamespace'"
     * @generated
     */
    QName getReturns();

    /**
     * Sets the value of the '{@link net.opengis.fes20.AvailableFunctionType#getReturns <em>Returns</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Returns</em>' attribute.
     * @see #getReturns()
     * @generated
     */
    void setReturns(QName value);

    /**
     * Returns the value of the '<em><b>Arguments</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Arguments</em>' containment reference isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Arguments</em>' containment reference.
     * @see #setArguments(ArgumentsType)
     * @see net.opengis.fes20.Fes20Package#getAvailableFunctionType_Arguments()
     * @model containment="true"
     *        extendedMetaData="kind='element' name='Arguments' namespace='##targetNamespace'"
     * @generated
     */
    ArgumentsType getArguments();

    /**
     * Sets the value of the '{@link net.opengis.fes20.AvailableFunctionType#getArguments <em>Arguments</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Arguments</em>' containment reference.
     * @see #getArguments()
     * @generated
     */
    void setArguments(ArgumentsType value);

    /**
     * Returns the value of the '<em><b>Name</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Name</em>' attribute isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Name</em>' attribute.
     * @see #setName(String)
     * @see net.opengis.fes20.Fes20Package#getAvailableFunctionType_Name()
     * @model dataType="org.eclipse.emf.ecore.xml.type.String" required="true"
     *        extendedMetaData="kind='attribute' name='name'"
     * @generated
     */
    String getName();

    /**
     * Sets the value of the '{@link net.opengis.fes20.AvailableFunctionType#getName <em>Name</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Name</em>' attribute.
     * @see #getName()
     * @generated
     */
    void setName(String value);

} // AvailableFunctionType
