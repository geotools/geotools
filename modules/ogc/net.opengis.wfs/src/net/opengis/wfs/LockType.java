/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package net.opengis.wfs;

import javax.xml.namespace.QName;

import org.eclipse.emf.ecore.EObject;
import org.opengis.filter.Filter;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Lock Type</b></em>'.
 * <!-- end-user-doc -->
 *
 * <!-- begin-model-doc -->
 * 
 *             This type defines the Lock element.  The Lock element
 *             defines a locking operation on feature instances of
 *             a single type. An OGC Filter is used to constrain the
 *             scope of the operation.  Features to be locked can be
 *             identified individually by using their feature identifier
 *             or they can be locked by satisfying the spatial and
 *             non-spatial constraints defined in the filter.
 * <!-- end-model-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link net.opengis.wfs.LockType#getFilter <em>Filter</em>}</li>
 *   <li>{@link net.opengis.wfs.LockType#getHandle <em>Handle</em>}</li>
 *   <li>{@link net.opengis.wfs.LockType#getTypeName <em>Type Name</em>}</li>
 * </ul>
 * </p>
 *
 * @see net.opengis.wfs.WfsPackage#getLockType()
 * @model extendedMetaData="name='LockType' kind='elementOnly'"
 * @generated
 */
public interface LockType extends EObject {
	/**
	 * Returns the value of the '<em><b>Filter</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Filter</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Filter</em>' attribute.
	 * @see #setFilter(Object)
	 * @see net.opengis.wfs.WFSPackage#getLockType_Filter()
	 * @model 
	 */
	Filter getFilter();

	/**
     * Sets the value of the '{@link net.opengis.wfs.LockType#getFilter <em>Filter</em>}' attribute.
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
     *                to a Lock action.  The handle is included to
     *                facilitate error reporting.  If one of a set of
     *                Lock actions failed while processing a LockFeature
     *                request, a WFS may report the handle in an exception
     *                report to localize the error.  If a handle is not
     *                present then a WFS may employ some other means of
     *                localizing the error (e.g. line number).
     * <!-- end-model-doc -->
     * @return the value of the '<em>Handle</em>' attribute.
     * @see #setHandle(String)
     * @see net.opengis.wfs.WfsPackage#getLockType_Handle()
     * @model unique="false" dataType="org.eclipse.emf.ecore.xml.type.String"
     *        extendedMetaData="kind='attribute' name='handle'"
     * @generated
     */
	String getHandle();

	/**
     * Sets the value of the '{@link net.opengis.wfs.LockType#getHandle <em>Handle</em>}' attribute.
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @param value the new value of the '<em>Handle</em>' attribute.
     * @see #getHandle()
     * @generated
     */
	void setHandle(String value);

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
	 * @see net.opengis.wfs.WFSPackage#getLockType_TypeName()
	 * @model 
	 */
	QName getTypeName();

	/**
     * Sets the value of the '{@link net.opengis.wfs.LockType#getTypeName <em>Type Name</em>}' attribute.
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @param value the new value of the '<em>Type Name</em>' attribute.
     * @see #getTypeName()
     * @generated
     */
	void setTypeName(QName value);

} // LockType
