/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package net.opengis.gml;

import java.util.List;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Code List Type</b></em>'.
 * <!-- end-user-doc -->
 *
 * <!-- begin-model-doc -->
 * List of values on a uniform nominal scale.  List of text tokens.
 *       In a list context a token should not include any spaces, so xsd:Name is used instead of xsd:string.
 *       If a codeSpace attribute is present, then its value is a reference to
 *       a Reference System for the value, a dictionary or code list.
 * <!-- end-model-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link net.opengis.gml.CodeListType#getValue <em>Value</em>}</li>
 *   <li>{@link net.opengis.gml.CodeListType#getCodeSpace <em>Code Space</em>}</li>
 * </ul>
 * </p>
 *
 * @see net.opengis.gml.GmlPackage#getCodeListType()
 * @model extendedMetaData="name='CodeListType' kind='simple'"
 * @generated
 */
public interface CodeListType extends EObject {
    /**
	 * Returns the value of the '<em><b>Value</b></em>' attribute.
	 * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Value</em>' attribute isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
	 * @return the value of the '<em>Value</em>' attribute.
	 * @see #setValue(List)
	 * @see net.opengis.gml.GmlPackage#getCodeListType_Value()
	 * @model dataType="net.opengis.gml.NameList" many="false"
	 *        extendedMetaData="name=':0' kind='simple'"
	 * @generated
	 */
    List getValue();

    /**
	 * Sets the value of the '{@link net.opengis.gml.CodeListType#getValue <em>Value</em>}' attribute.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Value</em>' attribute.
	 * @see #getValue()
	 * @generated
	 */
    void setValue(List value);

    /**
	 * Returns the value of the '<em><b>Code Space</b></em>' attribute.
	 * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Code Space</em>' attribute isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
	 * @return the value of the '<em>Code Space</em>' attribute.
	 * @see #setCodeSpace(String)
	 * @see net.opengis.gml.GmlPackage#getCodeListType_CodeSpace()
	 * @model dataType="org.eclipse.emf.ecore.xml.type.AnyURI"
	 *        extendedMetaData="kind='attribute' name='codeSpace'"
	 * @generated
	 */
    String getCodeSpace();

    /**
	 * Sets the value of the '{@link net.opengis.gml.CodeListType#getCodeSpace <em>Code Space</em>}' attribute.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Code Space</em>' attribute.
	 * @see #getCodeSpace()
	 * @generated
	 */
    void setCodeSpace(String value);

} // CodeListType
