/**
 */
package org.w3._2001.smil20;

import org.eclipse.emf.common.util.EMap;

import org.eclipse.emf.ecore.EObject;

import org.eclipse.emf.ecore.util.FeatureMap;

import org.w3._2001.smil20.language.AnimateColorType;
import org.w3._2001.smil20.language.AnimateMotionType;
import org.w3._2001.smil20.language.AnimateType;
import org.w3._2001.smil20.language.SetType;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Document Root</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link org.w3._2001.smil20.DocumentRoot#getMixed <em>Mixed</em>}</li>
 *   <li>{@link org.w3._2001.smil20.DocumentRoot#getXMLNSPrefixMap <em>XMLNS Prefix Map</em>}</li>
 *   <li>{@link org.w3._2001.smil20.DocumentRoot#getXSISchemaLocation <em>XSI Schema Location</em>}</li>
 *   <li>{@link org.w3._2001.smil20.DocumentRoot#getAnimate <em>Animate</em>}</li>
 *   <li>{@link org.w3._2001.smil20.DocumentRoot#getAnimateColor <em>Animate Color</em>}</li>
 *   <li>{@link org.w3._2001.smil20.DocumentRoot#getAnimateMotion <em>Animate Motion</em>}</li>
 *   <li>{@link org.w3._2001.smil20.DocumentRoot#getSet <em>Set</em>}</li>
 * </ul>
 *
 * @see org.w3._2001.smil20.Smil20Package#getDocumentRoot()
 * @model extendedMetaData="name='' kind='mixed'"
 * @generated
 */
public interface DocumentRoot extends EObject {
    /**
     * Returns the value of the '<em><b>Mixed</b></em>' attribute list.
     * The list contents are of type {@link org.eclipse.emf.ecore.util.FeatureMap.Entry}.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Mixed</em>' attribute list isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Mixed</em>' attribute list.
     * @see org.w3._2001.smil20.Smil20Package#getDocumentRoot_Mixed()
     * @model unique="false" dataType="org.eclipse.emf.ecore.EFeatureMapEntry" many="true"
     *        extendedMetaData="kind='elementWildcard' name=':mixed'"
     * @generated
     */
    FeatureMap getMixed();

    /**
     * Returns the value of the '<em><b>XMLNS Prefix Map</b></em>' map.
     * The key is of type {@link java.lang.String},
     * and the value is of type {@link java.lang.String},
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>XMLNS Prefix Map</em>' map isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>XMLNS Prefix Map</em>' map.
     * @see org.w3._2001.smil20.Smil20Package#getDocumentRoot_XMLNSPrefixMap()
     * @model mapType="org.eclipse.emf.ecore.EStringToStringMapEntry<org.eclipse.emf.ecore.EString, org.eclipse.emf.ecore.EString>" transient="true"
     *        extendedMetaData="kind='attribute' name='xmlns:prefix'"
     * @generated
     */
    EMap<String, String> getXMLNSPrefixMap();

    /**
     * Returns the value of the '<em><b>XSI Schema Location</b></em>' map.
     * The key is of type {@link java.lang.String},
     * and the value is of type {@link java.lang.String},
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>XSI Schema Location</em>' map isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>XSI Schema Location</em>' map.
     * @see org.w3._2001.smil20.Smil20Package#getDocumentRoot_XSISchemaLocation()
     * @model mapType="org.eclipse.emf.ecore.EStringToStringMapEntry<org.eclipse.emf.ecore.EString, org.eclipse.emf.ecore.EString>" transient="true"
     *        extendedMetaData="kind='attribute' name='xsi:schemaLocation'"
     * @generated
     */
    EMap<String, String> getXSISchemaLocation();

    /**
     * Returns the value of the '<em><b>Animate</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Animate</em>' containment reference isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Animate</em>' containment reference.
     * @see #setAnimate(AnimateType)
     * @see org.w3._2001.smil20.Smil20Package#getDocumentRoot_Animate()
     * @model containment="true" upper="-2" transient="true" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='animate' namespace='##targetNamespace' affiliation='http://www.w3.org/2001/SMIL20/Language#animate'"
     * @generated
     */
    AnimateType getAnimate();

    /**
     * Sets the value of the '{@link org.w3._2001.smil20.DocumentRoot#getAnimate <em>Animate</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Animate</em>' containment reference.
     * @see #getAnimate()
     * @generated
     */
    void setAnimate(AnimateType value);

    /**
     * Returns the value of the '<em><b>Animate Color</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Animate Color</em>' containment reference isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Animate Color</em>' containment reference.
     * @see #setAnimateColor(AnimateColorType)
     * @see org.w3._2001.smil20.Smil20Package#getDocumentRoot_AnimateColor()
     * @model containment="true" upper="-2" transient="true" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='animateColor' namespace='##targetNamespace' affiliation='http://www.w3.org/2001/SMIL20/Language#animateColor'"
     * @generated
     */
    AnimateColorType getAnimateColor();

    /**
     * Sets the value of the '{@link org.w3._2001.smil20.DocumentRoot#getAnimateColor <em>Animate Color</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Animate Color</em>' containment reference.
     * @see #getAnimateColor()
     * @generated
     */
    void setAnimateColor(AnimateColorType value);

    /**
     * Returns the value of the '<em><b>Animate Motion</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Animate Motion</em>' containment reference isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Animate Motion</em>' containment reference.
     * @see #setAnimateMotion(AnimateMotionType)
     * @see org.w3._2001.smil20.Smil20Package#getDocumentRoot_AnimateMotion()
     * @model containment="true" upper="-2" transient="true" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='animateMotion' namespace='##targetNamespace' affiliation='http://www.w3.org/2001/SMIL20/Language#animateMotion'"
     * @generated
     */
    AnimateMotionType getAnimateMotion();

    /**
     * Sets the value of the '{@link org.w3._2001.smil20.DocumentRoot#getAnimateMotion <em>Animate Motion</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Animate Motion</em>' containment reference.
     * @see #getAnimateMotion()
     * @generated
     */
    void setAnimateMotion(AnimateMotionType value);

    /**
     * Returns the value of the '<em><b>Set</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Set</em>' containment reference isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Set</em>' containment reference.
     * @see #setSet(SetType)
     * @see org.w3._2001.smil20.Smil20Package#getDocumentRoot_Set()
     * @model containment="true" upper="-2" transient="true" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='set' namespace='##targetNamespace' affiliation='http://www.w3.org/2001/SMIL20/Language#set'"
     * @generated
     */
    SetType getSet();

    /**
     * Sets the value of the '{@link org.w3._2001.smil20.DocumentRoot#getSet <em>Set</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Set</em>' containment reference.
     * @see #getSet()
     * @generated
     */
    void setSet(SetType value);

} // DocumentRoot
