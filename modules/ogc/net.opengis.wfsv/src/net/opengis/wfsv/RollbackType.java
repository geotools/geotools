/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package net.opengis.wfsv;

import javax.xml.namespace.QName;

import net.opengis.wfs.NativeType;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Rollback Type</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link net.opengis.wfsv.RollbackType#getFilter <em>Filter</em>}</li>
 *   <li>{@link net.opengis.wfsv.RollbackType#getHandle <em>Handle</em>}</li>
 *   <li>{@link net.opengis.wfsv.RollbackType#getToFeatureVersion <em>To Feature Version</em>}</li>
 *   <li>{@link net.opengis.wfsv.RollbackType#getTypeName <em>Type Name</em>}</li>
 *   <li>{@link net.opengis.wfsv.RollbackType#getUser <em>User</em>}</li>
 * </ul>
 * </p>
 *
 * @see net.opengis.wfsv.WfsvPackage#getRollbackType()
 * @model extendedMetaData="name='RollbackType' kind='elementOnly'"
 * @generated
 */
public interface RollbackType extends NativeType {
    /**
     * Returns the value of the '<em><b>Filter</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * 
     *                 The Filter element is used to define spatial and/or non-spatial constraints on
     *                 query. Spatial constrains use GML3 to specify the constraining geometry. A full
     *                 description of the Filter element can be found in the Filter Encoding Implementation
     *                 Specification.
     *               
     * <!-- end-model-doc -->
     * @return the value of the '<em>Filter</em>' attribute.
     * @see #setFilter(Object)
     * @see net.opengis.wfsv.WfsvPackage#getRollbackType_Filter()
     * @model dataType="org.eclipse.emf.ecore.xml.type.AnySimpleType"
     *        extendedMetaData="kind='element' name='Filter' namespace='http://www.opengis.net/ogc'"
     * @generated
     */
    Object getFilter();

    /**
     * Sets the value of the '{@link net.opengis.wfsv.RollbackType#getFilter <em>Filter</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Filter</em>' attribute.
     * @see #getFilter()
     * @generated
     */
    void setFilter(Object value);

    /**
     * Returns the value of the '<em><b>Handle</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * 
     *               The handle attribute allows a client application to assign a client-generated request
     *               identifier to an Insert action. The handle is included to facilitate error reporting.
     *               If a Rollback action in a Transaction request fails, then a Versioning WFS may include
     *               the handle in an exception report to localize the error. If no handle is included of
     *               the offending Rollback element then a WFS may employee other means of localizing the
     *               error (e.g. line number).
     *             
     * <!-- end-model-doc -->
     * @return the value of the '<em>Handle</em>' attribute.
     * @see #setHandle(String)
     * @see net.opengis.wfsv.WfsvPackage#getRollbackType_Handle()
     * @model dataType="org.eclipse.emf.ecore.xml.type.String"
     *        extendedMetaData="kind='attribute' name='handle'"
     * @generated
     */
    String getHandle();

    /**
     * Sets the value of the '{@link net.opengis.wfsv.RollbackType#getHandle <em>Handle</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Handle</em>' attribute.
     * @see #getHandle()
     * @generated
     */
    void setHandle(String value);

    /**
     * Returns the value of the '<em><b>To Feature Version</b></em>' attribute.
     * The default value is <code>"FIRST"</code>.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * 
     *               Same as featureVersion in QueryType. Rollback will restore the "toFeatureVersion" 
     *               feature state, removing all changes occurred between "toFeatureVersion" and current
     *             
     * <!-- end-model-doc -->
     * @return the value of the '<em>To Feature Version</em>' attribute.
     * @see #isSetToFeatureVersion()
     * @see #unsetToFeatureVersion()
     * @see #setToFeatureVersion(String)
     * @see net.opengis.wfsv.WfsvPackage#getRollbackType_ToFeatureVersion()
     * @model default="FIRST" unsettable="true" dataType="org.eclipse.emf.ecore.xml.type.String"
     *        extendedMetaData="kind='attribute' name='toFeatureVersion'"
     * @generated
     */
    String getToFeatureVersion();

    /**
     * Sets the value of the '{@link net.opengis.wfsv.RollbackType#getToFeatureVersion <em>To Feature Version</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>To Feature Version</em>' attribute.
     * @see #isSetToFeatureVersion()
     * @see #unsetToFeatureVersion()
     * @see #getToFeatureVersion()
     * @generated
     */
    void setToFeatureVersion(String value);

    /**
     * Unsets the value of the '{@link net.opengis.wfsv.RollbackType#getToFeatureVersion <em>To Feature Version</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #isSetToFeatureVersion()
     * @see #getToFeatureVersion()
     * @see #setToFeatureVersion(String)
     * @generated
     */
    void unsetToFeatureVersion();

    /**
     * Returns whether the value of the '{@link net.opengis.wfsv.RollbackType#getToFeatureVersion <em>To Feature Version</em>}' attribute is set.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return whether the value of the '<em>To Feature Version</em>' attribute is set.
     * @see #unsetToFeatureVersion()
     * @see #getToFeatureVersion()
     * @see #setToFeatureVersion(String)
     * @generated
     */
    boolean isSetToFeatureVersion();

    /**
     * Returns the value of the '<em><b>Type Name</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * 
     *               The typeName attribute is a single feature type name that indicates which type of
     *               feature instances should be included in the reponse set. The names must be a valid
     *               type that belong to this query's feature content as defined by the GML Application
     *               Schema.
     *             
     * <!-- end-model-doc -->
     * @return the value of the '<em>Type Name</em>' attribute.
     * @see #setTypeName(QName)
     * @see net.opengis.wfsv.WfsvPackage#getRollbackType_TypeName()
     * @model dataType="org.eclipse.emf.ecore.xml.type.QName" required="true"
     *        extendedMetaData="kind='attribute' name='typeName'"
     * @generated
     */
    QName getTypeName();

    /**
     * Sets the value of the '{@link net.opengis.wfsv.RollbackType#getTypeName <em>Type Name</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Type Name</em>' attribute.
     * @see #getTypeName()
     * @generated
     */
    void setTypeName(QName value);

    /**
     * Returns the value of the '<em><b>User</b></em>' attribute.
     * The default value is <code>""</code>.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * 
     *               User id to be used in addition to the filter in order to isolate the features to be
     *               rolled back. Only the features modified by the specified user between
     *               fromFeatureVersion and the current version will be subjected to roll back.
     *             
     * <!-- end-model-doc -->
     * @return the value of the '<em>User</em>' attribute.
     * @see #isSetUser()
     * @see #unsetUser()
     * @see #setUser(String)
     * @see net.opengis.wfsv.WfsvPackage#getRollbackType_User()
     * @model default="" unsettable="true" dataType="org.eclipse.emf.ecore.xml.type.String"
     *        extendedMetaData="kind='attribute' name='user'"
     * @generated
     */
    String getUser();

    /**
     * Sets the value of the '{@link net.opengis.wfsv.RollbackType#getUser <em>User</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>User</em>' attribute.
     * @see #isSetUser()
     * @see #unsetUser()
     * @see #getUser()
     * @generated
     */
    void setUser(String value);

    /**
     * Unsets the value of the '{@link net.opengis.wfsv.RollbackType#getUser <em>User</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #isSetUser()
     * @see #getUser()
     * @see #setUser(String)
     * @generated
     */
    void unsetUser();

    /**
     * Returns whether the value of the '{@link net.opengis.wfsv.RollbackType#getUser <em>User</em>}' attribute is set.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return whether the value of the '<em>User</em>' attribute is set.
     * @see #unsetUser()
     * @see #getUser()
     * @see #setUser(String)
     * @generated
     */
    boolean isSetUser();

} // RollbackType
