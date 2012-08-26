/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package net.opengis.cat.csw20;

import java.lang.String;

import javax.xml.namespace.QName;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Domain Values Type</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link net.opengis.cat.csw20.DomainValuesType#getPropertyName <em>Property Name</em>}</li>
 *   <li>{@link net.opengis.cat.csw20.DomainValuesType#getParameterName <em>Parameter Name</em>}</li>
 *   <li>{@link net.opengis.cat.csw20.DomainValuesType#getListOfValues <em>List Of Values</em>}</li>
 *   <li>{@link net.opengis.cat.csw20.DomainValuesType#getConceptualScheme <em>Conceptual Scheme</em>}</li>
 *   <li>{@link net.opengis.cat.csw20.DomainValuesType#getRangeOfValues <em>Range Of Values</em>}</li>
 *   <li>{@link net.opengis.cat.csw20.DomainValuesType#getType <em>Type</em>}</li>
 *   <li>{@link net.opengis.cat.csw20.DomainValuesType#getUom <em>Uom</em>}</li>
 * </ul>
 * </p>
 *
 * @see net.opengis.cat.csw20.Csw20Package#getDomainValuesType()
 * @model extendedMetaData="name='DomainValuesType' kind='elementOnly'"
 * @generated
 */
public interface DomainValuesType extends EObject {
    /**
     * Returns the value of the '<em><b>Property Name</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Property Name</em>' attribute isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Property Name</em>' attribute.
     * @see #setPropertyName(String)
     * @see net.opengis.cat.csw20.Csw20Package#getDomainValuesType_PropertyName()
     * @model dataType="org.eclipse.emf.ecore.xml.type.AnyURI"
     *        extendedMetaData="kind='element' name='PropertyName' namespace='##targetNamespace'"
     * @generated
     */
    String getPropertyName();

    /**
     * Sets the value of the '{@link net.opengis.cat.csw20.DomainValuesType#getPropertyName <em>Property Name</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Property Name</em>' attribute.
     * @see #getPropertyName()
     * @generated
     */
    void setPropertyName(String value);

    /**
     * Returns the value of the '<em><b>Parameter Name</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Parameter Name</em>' attribute isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Parameter Name</em>' attribute.
     * @see #setParameterName(String)
     * @see net.opengis.cat.csw20.Csw20Package#getDomainValuesType_ParameterName()
     * @model dataType="org.eclipse.emf.ecore.xml.type.AnyURI"
     *        extendedMetaData="kind='element' name='ParameterName' namespace='##targetNamespace'"
     * @generated
     */
    String getParameterName();

    /**
     * Sets the value of the '{@link net.opengis.cat.csw20.DomainValuesType#getParameterName <em>Parameter Name</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Parameter Name</em>' attribute.
     * @see #getParameterName()
     * @generated
     */
    void setParameterName(String value);

    /**
     * Returns the value of the '<em><b>List Of Values</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>List Of Values</em>' containment reference isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>List Of Values</em>' containment reference.
     * @see #setListOfValues(ListOfValuesType)
     * @see net.opengis.cat.csw20.Csw20Package#getDomainValuesType_ListOfValues()
     * @model containment="true"
     *        extendedMetaData="kind='element' name='ListOfValues' namespace='##targetNamespace'"
     * @generated
     */
    ListOfValuesType getListOfValues();

    /**
     * Sets the value of the '{@link net.opengis.cat.csw20.DomainValuesType#getListOfValues <em>List Of Values</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>List Of Values</em>' containment reference.
     * @see #getListOfValues()
     * @generated
     */
    void setListOfValues(ListOfValuesType value);

    /**
     * Returns the value of the '<em><b>Conceptual Scheme</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Conceptual Scheme</em>' containment reference isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Conceptual Scheme</em>' containment reference.
     * @see #setConceptualScheme(ConceptualSchemeType)
     * @see net.opengis.cat.csw20.Csw20Package#getDomainValuesType_ConceptualScheme()
     * @model containment="true"
     *        extendedMetaData="kind='element' name='ConceptualScheme' namespace='##targetNamespace'"
     * @generated
     */
    ConceptualSchemeType getConceptualScheme();

    /**
     * Sets the value of the '{@link net.opengis.cat.csw20.DomainValuesType#getConceptualScheme <em>Conceptual Scheme</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Conceptual Scheme</em>' containment reference.
     * @see #getConceptualScheme()
     * @generated
     */
    void setConceptualScheme(ConceptualSchemeType value);

    /**
     * Returns the value of the '<em><b>Range Of Values</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Range Of Values</em>' containment reference isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Range Of Values</em>' containment reference.
     * @see #setRangeOfValues(RangeOfValuesType)
     * @see net.opengis.cat.csw20.Csw20Package#getDomainValuesType_RangeOfValues()
     * @model containment="true"
     *        extendedMetaData="kind='element' name='RangeOfValues' namespace='##targetNamespace'"
     * @generated
     */
    RangeOfValuesType getRangeOfValues();

    /**
     * Sets the value of the '{@link net.opengis.cat.csw20.DomainValuesType#getRangeOfValues <em>Range Of Values</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Range Of Values</em>' containment reference.
     * @see #getRangeOfValues()
     * @generated
     */
    void setRangeOfValues(RangeOfValuesType value);

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
     * @see net.opengis.cat.csw20.Csw20Package#getDomainValuesType_Type()
     * @model dataType="org.eclipse.emf.ecore.xml.type.QName" required="true"
     *        extendedMetaData="kind='attribute' name='type'"
     * @generated
     */
    QName getType();

    /**
     * Sets the value of the '{@link net.opengis.cat.csw20.DomainValuesType#getType <em>Type</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Type</em>' attribute.
     * @see #getType()
     * @generated
     */
    void setType(QName value);

    /**
     * Returns the value of the '<em><b>Uom</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Uom</em>' attribute isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Uom</em>' attribute.
     * @see #setUom(String)
     * @see net.opengis.cat.csw20.Csw20Package#getDomainValuesType_Uom()
     * @model dataType="org.eclipse.emf.ecore.xml.type.AnyURI"
     *        extendedMetaData="kind='attribute' name='uom'"
     * @generated
     */
    String getUom();

    /**
     * Sets the value of the '{@link net.opengis.cat.csw20.DomainValuesType#getUom <em>Uom</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Uom</em>' attribute.
     * @see #getUom()
     * @generated
     */
    void setUom(String value);

} // DomainValuesType
