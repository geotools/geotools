/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package net.opengis.wfs;

import java.net.URI;

import javax.xml.namespace.QName;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EObject;
import org.opengis.filter.Filter;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Update Element Type</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link net.opengis.wfs.UpdateElementType#getProperty <em>Property</em>}</li>
 *   <li>{@link net.opengis.wfs.UpdateElementType#getFilter <em>Filter</em>}</li>
 *   <li>{@link net.opengis.wfs.UpdateElementType#getHandle <em>Handle</em>}</li>
 *   <li>{@link net.opengis.wfs.UpdateElementType#getInputFormat <em>Input Format</em>}</li>
 *   <li>{@link net.opengis.wfs.UpdateElementType#getSrsName <em>Srs Name</em>}</li>
 *   <li>{@link net.opengis.wfs.UpdateElementType#getTypeName <em>Type Name</em>}</li>
 * </ul>
 * </p>
 *
 * @see net.opengis.wfs.WfsPackage#getUpdateElementType()
 * @model extendedMetaData="name='UpdateElementType' kind='elementOnly'"
 * @generated
 */
public interface UpdateElementType extends EObject {
	/**
     * Returns the value of the '<em><b>Property</b></em>' containment reference list.
     * The list contents are of type {@link net.opengis.wfs.PropertyType}.
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * 
     *                   Changing or updating a feature instance means that
     *                   the current value of one or more properties of
     *                   the feature are replaced with new values.  The Update
     *                   element contains  one or more Property elements.  A
     *                   Property element contains the name or a feature property
     *                   who's value is to be changed and the replacement value
     *                   for that property.
     * <!-- end-model-doc -->
     * @return the value of the '<em>Property</em>' containment reference list.
     * @see net.opengis.wfs.WfsPackage#getUpdateElementType_Property()
     * @model type="net.opengis.wfs.PropertyType" containment="true" required="true"
     *        extendedMetaData="kind='element' name='Property' namespace='##targetNamespace'"
     * @generated
     */
	EList getProperty();

	/**
	 * Returns the value of the '<em><b>Filter</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * 
	 *                   The Filter element is used to constrain the scope
	 *                   of the update operation to those features identified
	 *                   by the filter.  Feature instances can be specified
	 *                   explicitly and individually using the identifier of
	 *                   each feature instance OR a set of features to be
	 *                   operated on can be identified by specifying spatial
	 *                   and non-spatial constraints in the filter.
	 *                   If no filter is specified then update operation 
	 *                   applies to all feature instances.
	 *                
	 * <!-- end-model-doc -->
	 * @return the value of the '<em>Filter</em>' attribute.
	 * @see #setFilter(Object)
	 * @see net.opengis.wfs.WFSPackage#getUpdateElementType_Filter()
	 * @model 
	 */
	Filter getFilter();

	/**
     * Sets the value of the '{@link net.opengis.wfs.UpdateElementType#getFilter <em>Filter</em>}' attribute.
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @param value the new value of the '<em>Filter</em>' attribute.
     * @see #getFilter()
     * @generated
     */
	void setFilter(Filter value);

	/**
     * Returns the value of the '<em><b>Handle</b></em>' attribute.
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * 
     *                The handle attribute allows a client application
     *                to assign a client-generated request identifier
     *                to an Insert action.  The handle is included to
     *                facilitate error reporting.  If an Update action
     *                in a Transaction request fails, then a WFS may
     *                include the handle in an exception report to localize
     *                the error.  If no handle is included of the offending
     *                Insert element then a WFS may employee other means of
     *                localizing the error (e.g. line number).
     * <!-- end-model-doc -->
     * @return the value of the '<em>Handle</em>' attribute.
     * @see #setHandle(String)
     * @see net.opengis.wfs.WfsPackage#getUpdateElementType_Handle()
     * @model unique="false" dataType="org.eclipse.emf.ecore.xml.type.String"
     *        extendedMetaData="kind='attribute' name='handle'"
     * @generated
     */
	String getHandle();

	/**
     * Sets the value of the '{@link net.opengis.wfs.UpdateElementType#getHandle <em>Handle</em>}' attribute.
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @param value the new value of the '<em>Handle</em>' attribute.
     * @see #getHandle()
     * @generated
     */
	void setHandle(String value);

	/**
     * Returns the value of the '<em><b>Input Format</b></em>' attribute.
     * The default value is <code>"x-application/gml:3"</code>.
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * 
     *                This inputFormat attribute is used to indicate
     *                the format used to encode a feature instance in
     *                an Insert element.  The default value of
     *                'text/xml; subtype=gml/3.1.1' is used to indicate
     *                that feature encoding is GML3.  Another example
     *                might be 'text/xml; subtype=gml/2.1.2' indicating
     *                that the feature us encoded in GML2.  A WFS must
     *                declare in the capabilities document, using a
     *                Parameter element, which version of GML it supports.
     * <!-- end-model-doc -->
     * @return the value of the '<em>Input Format</em>' attribute.
     * @see #isSetInputFormat()
     * @see #unsetInputFormat()
     * @see #setInputFormat(String)
     * @see net.opengis.wfs.WfsPackage#getUpdateElementType_InputFormat()
     * @model default="x-application/gml:3" unique="false" unsettable="true" dataType="org.eclipse.emf.ecore.xml.type.String"
     *        extendedMetaData="kind='attribute' name='inputFormat'"
     * @generated
     */
	String getInputFormat();

	/**
     * Sets the value of the '{@link net.opengis.wfs.UpdateElementType#getInputFormat <em>Input Format</em>}' attribute.
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @param value the new value of the '<em>Input Format</em>' attribute.
     * @see #isSetInputFormat()
     * @see #unsetInputFormat()
     * @see #getInputFormat()
     * @generated
     */
	void setInputFormat(String value);

	/**
     * Unsets the value of the '{@link net.opengis.wfs.UpdateElementType#getInputFormat <em>Input Format</em>}' attribute.
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @see #isSetInputFormat()
     * @see #getInputFormat()
     * @see #setInputFormat(String)
     * @generated
     */
	void unsetInputFormat();

	/**
     * Returns whether the value of the '{@link net.opengis.wfs.UpdateElementType#getInputFormat <em>Input Format</em>}' attribute is set.
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @return whether the value of the '<em>Input Format</em>' attribute is set.
     * @see #unsetInputFormat()
     * @see #getInputFormat()
     * @see #setInputFormat(String)
     * @generated
     */
	boolean isSetInputFormat();

	/**
	 * Returns the value of the '<em><b>Srs Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * 
	 *                DO WE NEED THIS HERE?
	 *            
	 * <!-- end-model-doc -->
	 * @return the value of the '<em>Srs Name</em>' attribute.
	 * @see #setSrsName(String)
	 * @see net.opengis.wfs.WFSPackage#getUpdateElementType_SrsName()
	 * @model 
	 */
	URI getSrsName();

	/**
     * Sets the value of the '{@link net.opengis.wfs.UpdateElementType#getSrsName <em>Srs Name</em>}' attribute.
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @param value the new value of the '<em>Srs Name</em>' attribute.
     * @see #getSrsName()
     * @generated
     */
	void setSrsName(URI value);

	/**
	 * Returns the value of the '<em><b>Type Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * 
	 *               The value of the typeName attribute is the name 
	 *               of the feature type to be updated. The name
	 *               specified must be a valid type that belongs to
	 *               the feature content as defined by the GML
	 *               Application Schema.
	 *            
	 * <!-- end-model-doc -->
	 * @return the value of the '<em>Type Name</em>' attribute.
	 * @see #setTypeName(Object)
	 * @see net.opengis.wfs.WFSPackage#getUpdateElementType_TypeName()
	 * @model 
	 */
	QName getTypeName();

	/**
     * Sets the value of the '{@link net.opengis.wfs.UpdateElementType#getTypeName <em>Type Name</em>}' attribute.
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @param value the new value of the '<em>Type Name</em>' attribute.
     * @see #getTypeName()
     * @generated
     */
	void setTypeName(QName value);

} // UpdateElementType
