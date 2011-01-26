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
 * A representation of the model object '<em><b>Action Type</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link net.opengis.wfs.ActionType#getMessage <em>Message</em>}</li>
 *   <li>{@link net.opengis.wfs.ActionType#getCode <em>Code</em>}</li>
 *   <li>{@link net.opengis.wfs.ActionType#getLocator <em>Locator</em>}</li>
 * </ul>
 * </p>
 *
 * @see net.opengis.wfs.WfsPackage#getActionType()
 * @model extendedMetaData="name='ActionType' kind='elementOnly'"
 * @generated
 */
public interface ActionType extends EObject {
	/**
     * Returns the value of the '<em><b>Message</b></em>' attribute.
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * 
     *                   If an action fails, the message element may be used
     *                   to supply an exception message.
     * <!-- end-model-doc -->
     * @return the value of the '<em>Message</em>' attribute.
     * @see #setMessage(String)
     * @see net.opengis.wfs.WfsPackage#getActionType_Message()
     * @model unique="false" dataType="org.eclipse.emf.ecore.xml.type.String"
     *        extendedMetaData="kind='element' name='Message' namespace='##targetNamespace'"
     * @generated
     */
	String getMessage();

	/**
     * Sets the value of the '{@link net.opengis.wfs.ActionType#getMessage <em>Message</em>}' attribute.
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @param value the new value of the '<em>Message</em>' attribute.
     * @see #getMessage()
     * @generated
     */
	void setMessage(String value);

	/**
     * Returns the value of the '<em><b>Code</b></em>' attribute.
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * 
     *                The code attribute may be used to specify an
     *                exception code indicating why an action failed.
     * <!-- end-model-doc -->
     * @return the value of the '<em>Code</em>' attribute.
     * @see #setCode(String)
     * @see net.opengis.wfs.WfsPackage#getActionType_Code()
     * @model unique="false" dataType="org.eclipse.emf.ecore.xml.type.String"
     *        extendedMetaData="kind='attribute' name='code'"
     * @generated
     */
	String getCode();

	/**
     * Sets the value of the '{@link net.opengis.wfs.ActionType#getCode <em>Code</em>}' attribute.
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @param value the new value of the '<em>Code</em>' attribute.
     * @see #getCode()
     * @generated
     */
	void setCode(String value);

	/**
     * Returns the value of the '<em><b>Locator</b></em>' attribute.
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * 
     *                The locator attribute is used to locate an action
     *                within a &lt;Transaction&gt; element.  The value
     *                of the locator attribute is either a string that
     *                is equal to the value of the handle attribute
     *                specified on an  &lt;Insert&gt;, &lt;Update&gt;
     *                or &lt;Delete&gt; action.  If a value is not
     *                specified for the handle attribute then a WFS
     *                may employ some other means of locating the
     *                action.  For example, the value of the locator
     *                attribute may be an integer indicating the order
     *                of the action (i.e. 1=First action, 2=Second action,
     *                etc.).
     * <!-- end-model-doc -->
     * @return the value of the '<em>Locator</em>' attribute.
     * @see #setLocator(String)
     * @see net.opengis.wfs.WfsPackage#getActionType_Locator()
     * @model unique="false" dataType="org.eclipse.emf.ecore.xml.type.String" required="true"
     *        extendedMetaData="kind='attribute' name='locator'"
     * @generated
     */
	String getLocator();

	/**
     * Sets the value of the '{@link net.opengis.wfs.ActionType#getLocator <em>Locator</em>}' attribute.
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @param value the new value of the '<em>Locator</em>' attribute.
     * @see #getLocator()
     * @generated
     */
	void setLocator(String value);

} // ActionType
