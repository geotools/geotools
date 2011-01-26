/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package net.opengis.wcs10;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Typed Literal Type</b></em>'.
 * <!-- end-user-doc -->
 *
 * <!-- begin-model-doc -->
 * A single value for a variable, encoded as a string. This type can be used for one value, for a spacing between allowed values, or for the default value of a parameter. The "type" attribute indicates the datatype of this value (default is a string). The value for a typed literal is found by applying the datatype mapping associated with the datatype URI to the lexical form string.
 * <!-- end-model-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link net.opengis.wcs10.TypedLiteralType#getValue <em>Value</em>}</li>
 *   <li>{@link net.opengis.wcs10.TypedLiteralType#getType <em>Type</em>}</li>
 * </ul>
 * </p>
 *
 * @see net.opengis.wcs10.Wcs10Package#getTypedLiteralType()
 * @model extendedMetaData="name='TypedLiteralType' kind='simple'"
 * @generated
 */
public interface TypedLiteralType extends EObject {
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
	 * @see net.opengis.wcs10.Wcs10Package#getTypedLiteralType_Value()
	 * @model dataType="org.eclipse.emf.ecore.xml.type.String"
	 *        extendedMetaData="name=':0' kind='simple'"
	 * @generated
	 */
    String getValue();

    /**
	 * Sets the value of the '{@link net.opengis.wcs10.TypedLiteralType#getValue <em>Value</em>}' attribute.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Value</em>' attribute.
	 * @see #getValue()
	 * @generated
	 */
    void setValue(String value);

    /**
	 * Returns the value of the '<em><b>Type</b></em>' attribute.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * Should be included unless the datatype is string, or this "type" attribute is included in an enclosing element.
	 * <!-- end-model-doc -->
	 * @return the value of the '<em>Type</em>' attribute.
	 * @see #setType(String)
	 * @see net.opengis.wcs10.Wcs10Package#getTypedLiteralType_Type()
	 * @model dataType="org.eclipse.emf.ecore.xml.type.AnyURI"
	 *        extendedMetaData="kind='attribute' name='type' namespace='##targetNamespace'"
	 * @generated
	 */
    String getType();

    /**
	 * Sets the value of the '{@link net.opengis.wcs10.TypedLiteralType#getType <em>Type</em>}' attribute.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Type</em>' attribute.
	 * @see #getType()
	 * @generated
	 */
    void setType(String value);

} // TypedLiteralType
