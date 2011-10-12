/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package net.opengis.wfs20;

import javax.xml.namespace.QName;

import net.opengis.ows11.MetadataType;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Parameter Expression Type</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link net.opengis.wfs20.ParameterExpressionType#getTitle <em>Title</em>}</li>
 *   <li>{@link net.opengis.wfs20.ParameterExpressionType#getAbstract <em>Abstract</em>}</li>
 *   <li>{@link net.opengis.wfs20.ParameterExpressionType#getMetadata <em>Metadata</em>}</li>
 *   <li>{@link net.opengis.wfs20.ParameterExpressionType#getName <em>Name</em>}</li>
 *   <li>{@link net.opengis.wfs20.ParameterExpressionType#getType <em>Type</em>}</li>
 * </ul>
 * </p>
 *
 * @see net.opengis.wfs20.Wfs20Package#getParameterExpressionType()
 * @model extendedMetaData="name='ParameterExpressionType' kind='elementOnly'"
 * @generated
 */
public interface ParameterExpressionType extends EObject {
    /**
     * Returns the value of the '<em><b>Title</b></em>' containment reference list.
     * The list contents are of type {@link net.opengis.wfs20.TitleType}.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Title</em>' containment reference list isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Title</em>' containment reference list.
     * @see net.opengis.wfs20.Wfs20Package#getParameterExpressionType_Title()
     * @model containment="true"
     *        extendedMetaData="kind='element' name='Title' namespace='##targetNamespace'"
     * @generated
     */
    EList<TitleType> getTitle();

    /**
     * Returns the value of the '<em><b>Abstract</b></em>' containment reference list.
     * The list contents are of type {@link net.opengis.wfs20.AbstractType}.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Abstract</em>' containment reference list isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Abstract</em>' containment reference list.
     * @see net.opengis.wfs20.Wfs20Package#getParameterExpressionType_Abstract()
     * @model containment="true"
     *        extendedMetaData="kind='element' name='Abstract' namespace='##targetNamespace'"
     * @generated
     */
    EList<AbstractType> getAbstract();

    /**
     * Returns the value of the '<em><b>Metadata</b></em>' containment reference list.
     * The list contents are of type {@link net.opengis.ows11.MetadataType}.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Metadata</em>' containment reference list isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Metadata</em>' containment reference list.
     * @see net.opengis.wfs20.Wfs20Package#getParameterExpressionType_Metadata()
     * @model containment="true"
     *        extendedMetaData="kind='element' name='Metadata' namespace='http://www.opengis.net/ows/1.1'"
     * @generated
     */
    EList<MetadataType> getMetadata();

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
     * @see net.opengis.wfs20.Wfs20Package#getParameterExpressionType_Name()
     * @model dataType="org.eclipse.emf.ecore.xml.type.String" required="true"
     *        extendedMetaData="kind='attribute' name='name'"
     * @generated
     */
    String getName();

    /**
     * Sets the value of the '{@link net.opengis.wfs20.ParameterExpressionType#getName <em>Name</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Name</em>' attribute.
     * @see #getName()
     * @generated
     */
    void setName(String value);

    /**
     * Returns the value of the '<em><b>Type</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Type</em>' attribute isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Type</em>' attribute.
     * @see #setType(QName)
     * @see net.opengis.wfs20.Wfs20Package#getParameterExpressionType_Type()
     * @model dataType="org.eclipse.emf.ecore.xml.type.QName" required="true"
     *        extendedMetaData="kind='attribute' name='type'"
     * @generated
     */
    QName getType();

    /**
     * Sets the value of the '{@link net.opengis.wfs20.ParameterExpressionType#getType <em>Type</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Type</em>' attribute.
     * @see #getType()
     * @generated
     */
    void setType(QName value);

} // ParameterExpressionType
