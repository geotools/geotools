/**
 */
package net.opengis.wps20;

import net.opengis.ows20.CodeType;

import org.eclipse.emf.common.util.EList;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Describe Process Type</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link net.opengis.wps20.DescribeProcessType#getIdentifier <em>Identifier</em>}</li>
 *   <li>{@link net.opengis.wps20.DescribeProcessType#getLang <em>Lang</em>}</li>
 * </ul>
 *
 * @see net.opengis.wps20.Wps20Package#getDescribeProcessType()
 * @model extendedMetaData="name='DescribeProcess_._type' kind='elementOnly'"
 * @generated
 */
public interface DescribeProcessType extends RequestBaseType {
	/**
	 * Returns the value of the '<em><b>Identifier</b></em>' containment reference list.
	 * The list contents are of type {@link net.opengis.ows20.CodeType}.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * 
	 * 									One or more identifiers for which the process description shall be obtained.
	 * 									"ALL"" is reserved to retrieve the  descriptions for all available process offerings.
	 * 								
	 * <!-- end-model-doc -->
	 * @return the value of the '<em>Identifier</em>' containment reference list.
	 * @see net.opengis.wps20.Wps20Package#getDescribeProcessType_Identifier()
	 * @model containment="true" required="true"
	 *        extendedMetaData="kind='element' name='Identifier' namespace='http://www.opengis.net/ows/2.0'"
	 * @generated
	 */
	EList<CodeType> getIdentifier();

	/**
	 * Returns the value of the '<em><b>Lang</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * 
	 * 								RFC 4646 language code of the human-readable text (e.g. "en-CA") in the process description.
	 * 							
	 * <!-- end-model-doc -->
	 * @return the value of the '<em>Lang</em>' attribute.
	 * @see #setLang(String)
	 * @see net.opengis.wps20.Wps20Package#getDescribeProcessType_Lang()
	 * @model dataType="org.eclipse.emf.ecore.xml.namespace.LangType"
	 *        extendedMetaData="kind='attribute' name='lang' namespace='http://www.w3.org/XML/1998/namespace'"
	 * @generated
	 */
	String getLang();

	/**
	 * Sets the value of the '{@link net.opengis.wps20.DescribeProcessType#getLang <em>Lang</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Lang</em>' attribute.
	 * @see #getLang()
	 * @generated
	 */
	void setLang(String value);

} // DescribeProcessType
