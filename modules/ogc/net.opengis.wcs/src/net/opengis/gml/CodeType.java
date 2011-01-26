/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package net.opengis.gml;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Code Type</b></em>'.
 * <!-- end-user-doc -->
 *
 * <!-- begin-model-doc -->
 * Name or code with an (optional) authority.  Text token.
 *       If the codeSpace attribute is present, then its value should identify a dictionary, thesaurus
 *       or authority for the term, such as the organisation who assigned the value,
 *       or the dictionary from which it is taken.
 *       A text string with an optional codeSpace attribute.
 * <!-- end-model-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link net.opengis.gml.CodeType#getValue <em>Value</em>}</li>
 *   <li>{@link net.opengis.gml.CodeType#getCodeSpace <em>Code Space</em>}</li>
 * </ul>
 * </p>
 *
 * @see net.opengis.gml.GmlPackage#getCodeType()
 * @model extendedMetaData="name='CodeType' kind='simple'"
 * @generated
 */
public interface CodeType extends EObject {
    /**
	 * Returns the value of the '<em><b>Value</b></em>' attribute.
	 * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Value</em>' attribute isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
	 * @return the value of the '<em>Value</em>' attribute.
	 * @see #setValue(String)
	 * @see net.opengis.gml.GmlPackage#getCodeType_Value()
	 * @model dataType="org.eclipse.emf.ecore.xml.type.String"
	 *        extendedMetaData="name=':0' kind='simple'"
	 * @generated
	 */
    String getValue();

    /**
	 * Sets the value of the '{@link net.opengis.gml.CodeType#getValue <em>Value</em>}' attribute.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Value</em>' attribute.
	 * @see #getValue()
	 * @generated
	 */
    void setValue(String value);

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
	 * @see net.opengis.gml.GmlPackage#getCodeType_CodeSpace()
	 * @model dataType="org.eclipse.emf.ecore.xml.type.AnyURI"
	 *        extendedMetaData="kind='attribute' name='codeSpace'"
	 * @generated
	 */
    String getCodeSpace();

    /**
	 * Sets the value of the '{@link net.opengis.gml.CodeType#getCodeSpace <em>Code Space</em>}' attribute.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Code Space</em>' attribute.
	 * @see #getCodeSpace()
	 * @generated
	 */
    void setCodeSpace(String value);

} // CodeType
