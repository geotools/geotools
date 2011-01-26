/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package net.opengis.wfs;

import java.math.BigInteger;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Xlink Property Name Type</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link net.opengis.wfs.XlinkPropertyNameType#getValue <em>Value</em>}</li>
 *   <li>{@link net.opengis.wfs.XlinkPropertyNameType#getTraverseXlinkDepth <em>Traverse Xlink Depth</em>}</li>
 *   <li>{@link net.opengis.wfs.XlinkPropertyNameType#getTraverseXlinkExpiry <em>Traverse Xlink Expiry</em>}</li>
 * </ul>
 * </p>
 *
 * @see net.opengis.wfs.WfsPackage#getXlinkPropertyNameType()
 * @model extendedMetaData="name='XlinkPropertyName_._type' kind='simple'"
 * @generated
 */
public interface XlinkPropertyNameType extends EObject {
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
     * @see net.opengis.wfs.WfsPackage#getXlinkPropertyNameType_Value()
     * @model unique="false" dataType="org.eclipse.emf.ecore.xml.type.String"
     *        extendedMetaData="name=':0' kind='simple'"
     * @generated
     */
	String getValue();

	/**
     * Sets the value of the '{@link net.opengis.wfs.XlinkPropertyNameType#getValue <em>Value</em>}' attribute.
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @param value the new value of the '<em>Value</em>' attribute.
     * @see #getValue()
     * @generated
     */
	void setValue(String value);

	/**
     * Returns the value of the '<em><b>Traverse Xlink Depth</b></em>' attribute.
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * 
     *                   This attribute indicates the depth to which nested property
     *                   XLink linking element locator attribute (href) XLinks are
     *                   traversed and resolved if possible.  A value of "1" indicates
     *                   that one linking element locator attribute (href) Xlink
     *                   will be traversed and the referenced element returned if
     *                   possible, but nested property XLink linking element locator
     *                   attribute (href) XLinks in the returned element are not
     *                   traversed.  A value of  "
     * " indicates that all nested property
     *                   XLink linking element locator attribute (href) XLinks will be
     *                   traversed and the referenced elements returned if possible.
     *                   The range of valid values for this attribute consists of
     *                   positive integers plus "
     * ".
     * <!-- end-model-doc -->
     * @return the value of the '<em>Traverse Xlink Depth</em>' attribute.
     * @see #setTraverseXlinkDepth(String)
     * @see net.opengis.wfs.WfsPackage#getXlinkPropertyNameType_TraverseXlinkDepth()
     * @model unique="false" dataType="org.eclipse.emf.ecore.xml.type.String" required="true"
     *        extendedMetaData="kind='attribute' name='traverseXlinkDepth'"
     * @generated
     */
	String getTraverseXlinkDepth();

	/**
     * Sets the value of the '{@link net.opengis.wfs.XlinkPropertyNameType#getTraverseXlinkDepth <em>Traverse Xlink Depth</em>}' attribute.
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @param value the new value of the '<em>Traverse Xlink Depth</em>' attribute.
     * @see #getTraverseXlinkDepth()
     * @generated
     */
	void setTraverseXlinkDepth(String value);

	/**
     * Returns the value of the '<em><b>Traverse Xlink Expiry</b></em>' attribute.
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * 
     *                   The traverseXlinkExpiry attribute value is specified in
     *                   minutes It indicates how long a Web Feature Service should
     *                   wait to receive a response to a nested GetGmlObject request.
     * <!-- end-model-doc -->
     * @return the value of the '<em>Traverse Xlink Expiry</em>' attribute.
     * @see #setTraverseXlinkExpiry(BigInteger)
     * @see net.opengis.wfs.WfsPackage#getXlinkPropertyNameType_TraverseXlinkExpiry()
     * @model unique="false" dataType="org.eclipse.emf.ecore.xml.type.PositiveInteger"
     *        extendedMetaData="kind='attribute' name='traverseXlinkExpiry'"
     * @generated
     */
	BigInteger getTraverseXlinkExpiry();

	/**
     * Sets the value of the '{@link net.opengis.wfs.XlinkPropertyNameType#getTraverseXlinkExpiry <em>Traverse Xlink Expiry</em>}' attribute.
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @param value the new value of the '<em>Traverse Xlink Expiry</em>' attribute.
     * @see #getTraverseXlinkExpiry()
     * @generated
     */
	void setTraverseXlinkExpiry(BigInteger value);

} // XlinkPropertyNameType
