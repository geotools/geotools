/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package net.opengis.wfs;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Native Type</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link net.opengis.wfs.NativeType#isSafeToIgnore <em>Safe To Ignore</em>}</li>
 *   <li>{@link net.opengis.wfs.NativeType#getVendorId <em>Vendor Id</em>}</li>
 * </ul>
 * </p>
 *
 * @see net.opengis.wfs.WfsPackage#getNativeType()
 * @model extendedMetaData="name='NativeType' kind='empty'"
 * @generated
 */
public interface NativeType extends EObject {
	/**
     * Returns the value of the '<em><b>Safe To Ignore</b></em>' attribute.
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * 
     *                In the event that a Web Feature Service does not recognize
     *                the vendorId or does not recognize the vendor specific command,
     *                the safeToIgnore attribute is used to indicate whether the
     *                exception can be safely ignored.  A value of TRUE means that
     *                the Web Feature Service may ignore the command.  A value of
     *                FALSE means that a Web Feature Service cannot ignore the
     *                command and an exception should be raised if a problem is
     *                encountered.
     * <!-- end-model-doc -->
     * @return the value of the '<em>Safe To Ignore</em>' attribute.
     * @see #isSetSafeToIgnore()
     * @see #unsetSafeToIgnore()
     * @see #setSafeToIgnore(boolean)
     * @see net.opengis.wfs.WfsPackage#getNativeType_SafeToIgnore()
     * @model unique="false" unsettable="true" dataType="org.eclipse.emf.ecore.xml.type.Boolean" required="true"
     *        extendedMetaData="kind='attribute' name='safeToIgnore'"
     * @generated
     */
	boolean isSafeToIgnore();

	/**
     * Sets the value of the '{@link net.opengis.wfs.NativeType#isSafeToIgnore <em>Safe To Ignore</em>}' attribute.
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @param value the new value of the '<em>Safe To Ignore</em>' attribute.
     * @see #isSetSafeToIgnore()
     * @see #unsetSafeToIgnore()
     * @see #isSafeToIgnore()
     * @generated
     */
	void setSafeToIgnore(boolean value);

	/**
     * Unsets the value of the '{@link net.opengis.wfs.NativeType#isSafeToIgnore <em>Safe To Ignore</em>}' attribute.
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @see #isSetSafeToIgnore()
     * @see #isSafeToIgnore()
     * @see #setSafeToIgnore(boolean)
     * @generated
     */
	void unsetSafeToIgnore();

	/**
     * Returns whether the value of the '{@link net.opengis.wfs.NativeType#isSafeToIgnore <em>Safe To Ignore</em>}' attribute is set.
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @return whether the value of the '<em>Safe To Ignore</em>' attribute is set.
     * @see #unsetSafeToIgnore()
     * @see #isSafeToIgnore()
     * @see #setSafeToIgnore(boolean)
     * @generated
     */
	boolean isSetSafeToIgnore();

	/**
     * Returns the value of the '<em><b>Vendor Id</b></em>' attribute.
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * 
     *                The vendorId attribute is used to specify the name of
     *                vendor who's vendor specific command the client
     *                application wishes to execute.
     * <!-- end-model-doc -->
     * @return the value of the '<em>Vendor Id</em>' attribute.
     * @see #setVendorId(String)
     * @see net.opengis.wfs.WfsPackage#getNativeType_VendorId()
     * @model unique="false" dataType="org.eclipse.emf.ecore.xml.type.String" required="true"
     *        extendedMetaData="kind='attribute' name='vendorId'"
     * @generated
     */
	String getVendorId();

	/**
     * Sets the value of the '{@link net.opengis.wfs.NativeType#getVendorId <em>Vendor Id</em>}' attribute.
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @param value the new value of the '<em>Vendor Id</em>' attribute.
     * @see #getVendorId()
     * @generated
     */
	void setVendorId(String value);

} // NativeType
