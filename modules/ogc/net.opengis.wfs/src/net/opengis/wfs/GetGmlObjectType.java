/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package net.opengis.wfs;

import java.math.BigInteger;

import org.opengis.filter.identity.GmlObjectId;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Get Gml Object Type</b></em>'.
 * <!-- end-user-doc -->
 *
 * <!-- begin-model-doc -->
 * 
 *             A GetGmlObjectType element contains exactly one GmlObjectId.
 *             The value of the gml:id attribute on that GmlObjectId is used
 *             as a unique key to retrieve the complex element with a
 *             gml:id attribute with the same value.
 * <!-- end-model-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link net.opengis.wfs.GetGmlObjectType#getGmlObjectId <em>Gml Object Id</em>}</li>
 *   <li>{@link net.opengis.wfs.GetGmlObjectType#getOutputFormat <em>Output Format</em>}</li>
 *   <li>{@link net.opengis.wfs.GetGmlObjectType#getTraverseXlinkDepth <em>Traverse Xlink Depth</em>}</li>
 *   <li>{@link net.opengis.wfs.GetGmlObjectType#getTraverseXlinkExpiry <em>Traverse Xlink Expiry</em>}</li>
 * </ul>
 * </p>
 *
 * @see net.opengis.wfs.WfsPackage#getGetGmlObjectType()
 * @model extendedMetaData="name='GetGmlObjectType' kind='elementOnly'"
 * @generated
 */
public interface GetGmlObjectType extends BaseRequestType {
	/**
     * Returns the value of the '<em><b>Gml Object Id</b></em>' attribute.
     * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Gml Object Id</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
     * @return the value of the '<em>Gml Object Id</em>' attribute.
     * @see #setGmlObjectId(Object)
     * @see net.opengis.wfs.WfsPackage#getGetGmlObjectType_GmlObjectId()
     * @model unique="false" dataType="org.eclipse.emf.ecore.xml.type.AnySimpleType" required="true"
     *        extendedMetaData="kind='element' name='GmlObjectId' namespace='http://www.opengis.net/ogc'"
     * @generated
     */
	GmlObjectId/*<String>*/ getGmlObjectId();

	/**
     * Sets the value of the '{@link net.opengis.wfs.GetGmlObjectType#getGmlObjectId <em>Gml Object Id</em>}' attribute.
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @param value the new value of the '<em>Gml Object Id</em>' attribute.
     * @see #getGmlObjectId()
     * @generated
     */
	void setGmlObjectId(GmlObjectId/*<String>*/ value);

	/**
     * Returns the value of the '<em><b>Output Format</b></em>' attribute.
     * The default value is <code>"GML3"</code>.
     * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Output Format</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
     * @return the value of the '<em>Output Format</em>' attribute.
     * @see #isSetOutputFormat()
     * @see #unsetOutputFormat()
     * @see #setOutputFormat(String)
     * @see net.opengis.wfs.WfsPackage#getGetGmlObjectType_OutputFormat()
     * @model default="GML3" unique="false" unsettable="true" dataType="org.eclipse.emf.ecore.xml.type.String"
     *        extendedMetaData="kind='attribute' name='outputFormat'"
     * @generated
     */
	String getOutputFormat();

	/**
     * Sets the value of the '{@link net.opengis.wfs.GetGmlObjectType#getOutputFormat <em>Output Format</em>}' attribute.
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @param value the new value of the '<em>Output Format</em>' attribute.
     * @see #isSetOutputFormat()
     * @see #unsetOutputFormat()
     * @see #getOutputFormat()
     * @generated
     */
	void setOutputFormat(String value);

	/**
     * Unsets the value of the '{@link net.opengis.wfs.GetGmlObjectType#getOutputFormat <em>Output Format</em>}' attribute.
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @see #isSetOutputFormat()
     * @see #getOutputFormat()
     * @see #setOutputFormat(String)
     * @generated
     */
	void unsetOutputFormat();

	/**
     * Returns whether the value of the '{@link net.opengis.wfs.GetGmlObjectType#getOutputFormat <em>Output Format</em>}' attribute is set.
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @return whether the value of the '<em>Output Format</em>' attribute is set.
     * @see #unsetOutputFormat()
     * @see #getOutputFormat()
     * @see #setOutputFormat(String)
     * @generated
     */
	boolean isSetOutputFormat();

	/**
     * Returns the value of the '<em><b>Traverse Xlink Depth</b></em>' attribute.
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * 
     *                      This attribute indicates the depth to which nested
     *                      property XLink linking element locator attribute
     *                      (href) XLinks are traversed and resolved if possible.
     *                      A value of "1" indicates that one linking element
     *                      locator attribute (href) XLink will be traversed
     *                      and the referenced element returned if possible, but
     *                      nested property XLink linking element locator attribute
     *                      (href) XLinks in the returned element are not traversed.
     *                      A value of "
     * " indicates that all nested property XLink
     *                      linking element locator attribute (href) XLinks will be
     *                      traversed and the referenced elements returned if
     *                      possible.  The range of valid values for this attribute
     *                      consists of positive integers plus "
     * ".
     * <!-- end-model-doc -->
     * @return the value of the '<em>Traverse Xlink Depth</em>' attribute.
     * @see #setTraverseXlinkDepth(String)
     * @see net.opengis.wfs.WfsPackage#getGetGmlObjectType_TraverseXlinkDepth()
     * @model unique="false" dataType="org.eclipse.emf.ecore.xml.type.String" required="true"
     *        extendedMetaData="kind='attribute' name='traverseXlinkDepth'"
     * @generated
     */
	String getTraverseXlinkDepth();

	/**
     * Sets the value of the '{@link net.opengis.wfs.GetGmlObjectType#getTraverseXlinkDepth <em>Traverse Xlink Depth</em>}' attribute.
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
     *                      The traverseXlinkExpiry attribute value is specified
     *                      in minutes.  It indicates how long a Web Feature Service
     *                      should wait to receive a response to a nested GetGmlObject
     *                      request.
     * <!-- end-model-doc -->
     * @return the value of the '<em>Traverse Xlink Expiry</em>' attribute.
     * @see #setTraverseXlinkExpiry(BigInteger)
     * @see net.opengis.wfs.WfsPackage#getGetGmlObjectType_TraverseXlinkExpiry()
     * @model unique="false" dataType="org.eclipse.emf.ecore.xml.type.PositiveInteger"
     *        extendedMetaData="kind='attribute' name='traverseXlinkExpiry'"
     * @generated
     */
	BigInteger getTraverseXlinkExpiry();

	/**
     * Sets the value of the '{@link net.opengis.wfs.GetGmlObjectType#getTraverseXlinkExpiry <em>Traverse Xlink Expiry</em>}' attribute.
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @param value the new value of the '<em>Traverse Xlink Expiry</em>' attribute.
     * @see #getTraverseXlinkExpiry()
     * @generated
     */
	void setTraverseXlinkExpiry(BigInteger value);

} // GetGmlObjectType
