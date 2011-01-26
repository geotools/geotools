/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package net.opengis.wfs.impl;

import net.opengis.wfs.DeleteElementType;
import net.opengis.wfs.DescribeFeatureTypeType;
import net.opengis.wfs.DocumentRoot;
import net.opengis.wfs.FeatureCollectionType;
import net.opengis.wfs.FeatureTypeListType;
import net.opengis.wfs.GMLObjectTypeListType;
import net.opengis.wfs.GetCapabilitiesType;
import net.opengis.wfs.GetFeatureType;
import net.opengis.wfs.GetFeatureWithLockType;
import net.opengis.wfs.GetGmlObjectType;
import net.opengis.wfs.InsertElementType;
import net.opengis.wfs.LockFeatureResponseType;
import net.opengis.wfs.LockFeatureType;
import net.opengis.wfs.NativeType;
import net.opengis.wfs.PropertyType;
import net.opengis.wfs.QueryType;
import net.opengis.wfs.TransactionResponseType;
import net.opengis.wfs.TransactionType;
import net.opengis.wfs.UpdateElementType;
import net.opengis.wfs.WFSCapabilitiesType;
import net.opengis.wfs.WfsPackage;
import net.opengis.wfs.XlinkPropertyNameType;

import org.eclipse.emf.common.notify.NotificationChain;

import org.eclipse.emf.common.util.EMap;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.EcorePackage;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.impl.EObjectImpl;
import org.eclipse.emf.ecore.impl.EStringToStringMapEntryImpl;

import org.eclipse.emf.ecore.util.BasicFeatureMap;
import org.eclipse.emf.ecore.util.EcoreEMap;
import org.eclipse.emf.ecore.util.FeatureMap;
import org.eclipse.emf.ecore.util.InternalEList;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Document Root</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link net.opengis.wfs.impl.DocumentRootImpl#getMixed <em>Mixed</em>}</li>
 *   <li>{@link net.opengis.wfs.impl.DocumentRootImpl#getXMLNSPrefixMap <em>XMLNS Prefix Map</em>}</li>
 *   <li>{@link net.opengis.wfs.impl.DocumentRootImpl#getXSISchemaLocation <em>XSI Schema Location</em>}</li>
 *   <li>{@link net.opengis.wfs.impl.DocumentRootImpl#getDelete <em>Delete</em>}</li>
 *   <li>{@link net.opengis.wfs.impl.DocumentRootImpl#getDescribeFeatureType <em>Describe Feature Type</em>}</li>
 *   <li>{@link net.opengis.wfs.impl.DocumentRootImpl#getFeatureCollection <em>Feature Collection</em>}</li>
 *   <li>{@link net.opengis.wfs.impl.DocumentRootImpl#getFeatureTypeList <em>Feature Type List</em>}</li>
 *   <li>{@link net.opengis.wfs.impl.DocumentRootImpl#getGetCapabilities <em>Get Capabilities</em>}</li>
 *   <li>{@link net.opengis.wfs.impl.DocumentRootImpl#getGetFeature <em>Get Feature</em>}</li>
 *   <li>{@link net.opengis.wfs.impl.DocumentRootImpl#getGetFeatureWithLock <em>Get Feature With Lock</em>}</li>
 *   <li>{@link net.opengis.wfs.impl.DocumentRootImpl#getGetGmlObject <em>Get Gml Object</em>}</li>
 *   <li>{@link net.opengis.wfs.impl.DocumentRootImpl#getInsert <em>Insert</em>}</li>
 *   <li>{@link net.opengis.wfs.impl.DocumentRootImpl#getLockFeature <em>Lock Feature</em>}</li>
 *   <li>{@link net.opengis.wfs.impl.DocumentRootImpl#getLockFeatureResponse <em>Lock Feature Response</em>}</li>
 *   <li>{@link net.opengis.wfs.impl.DocumentRootImpl#getLockId <em>Lock Id</em>}</li>
 *   <li>{@link net.opengis.wfs.impl.DocumentRootImpl#getNative <em>Native</em>}</li>
 *   <li>{@link net.opengis.wfs.impl.DocumentRootImpl#getProperty <em>Property</em>}</li>
 *   <li>{@link net.opengis.wfs.impl.DocumentRootImpl#getPropertyName <em>Property Name</em>}</li>
 *   <li>{@link net.opengis.wfs.impl.DocumentRootImpl#getQuery <em>Query</em>}</li>
 *   <li>{@link net.opengis.wfs.impl.DocumentRootImpl#getServesGMLObjectTypeList <em>Serves GML Object Type List</em>}</li>
 *   <li>{@link net.opengis.wfs.impl.DocumentRootImpl#getSupportsGMLObjectTypeList <em>Supports GML Object Type List</em>}</li>
 *   <li>{@link net.opengis.wfs.impl.DocumentRootImpl#getTransaction <em>Transaction</em>}</li>
 *   <li>{@link net.opengis.wfs.impl.DocumentRootImpl#getTransactionResponse <em>Transaction Response</em>}</li>
 *   <li>{@link net.opengis.wfs.impl.DocumentRootImpl#getUpdate <em>Update</em>}</li>
 *   <li>{@link net.opengis.wfs.impl.DocumentRootImpl#getWfsCapabilities <em>Wfs Capabilities</em>}</li>
 *   <li>{@link net.opengis.wfs.impl.DocumentRootImpl#getXlinkPropertyName <em>Xlink Property Name</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class DocumentRootImpl extends EObjectImpl implements DocumentRoot {
	/**
     * The cached value of the '{@link #getMixed() <em>Mixed</em>}' attribute list.
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @see #getMixed()
     * @generated
     * @ordered
     */
	protected FeatureMap mixed;

	/**
     * The cached value of the '{@link #getXMLNSPrefixMap() <em>XMLNS Prefix Map</em>}' map.
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @see #getXMLNSPrefixMap()
     * @generated
     * @ordered
     */
	protected EMap xMLNSPrefixMap;

	/**
     * The cached value of the '{@link #getXSISchemaLocation() <em>XSI Schema Location</em>}' map.
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @see #getXSISchemaLocation()
     * @generated
     * @ordered
     */
	protected EMap xSISchemaLocation;

	/**
     * The default value of the '{@link #getLockId() <em>Lock Id</em>}' attribute.
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @see #getLockId()
     * @generated
     * @ordered
     */
	protected static final String LOCK_ID_EDEFAULT = null;

	/**
     * The default value of the '{@link #getPropertyName() <em>Property Name</em>}' attribute.
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @see #getPropertyName()
     * @generated
     * @ordered
     */
	protected static final String PROPERTY_NAME_EDEFAULT = null;

	/**
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @generated
     */
	protected DocumentRootImpl() {
        super();
    }

	/**
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @generated
     */
	protected EClass eStaticClass() {
        return WfsPackage.Literals.DOCUMENT_ROOT;
    }

	/**
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @generated
     */
	public FeatureMap getMixed() {
        if (mixed == null) {
            mixed = new BasicFeatureMap(this, WfsPackage.DOCUMENT_ROOT__MIXED);
        }
        return mixed;
    }

	/**
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @generated
     */
	public EMap getXMLNSPrefixMap() {
        if (xMLNSPrefixMap == null) {
            xMLNSPrefixMap = new EcoreEMap(EcorePackage.Literals.ESTRING_TO_STRING_MAP_ENTRY, EStringToStringMapEntryImpl.class, this, WfsPackage.DOCUMENT_ROOT__XMLNS_PREFIX_MAP);
        }
        return xMLNSPrefixMap;
    }

	/**
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @generated
     */
	public EMap getXSISchemaLocation() {
        if (xSISchemaLocation == null) {
            xSISchemaLocation = new EcoreEMap(EcorePackage.Literals.ESTRING_TO_STRING_MAP_ENTRY, EStringToStringMapEntryImpl.class, this, WfsPackage.DOCUMENT_ROOT__XSI_SCHEMA_LOCATION);
        }
        return xSISchemaLocation;
    }

	/**
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @generated
     */
	public DeleteElementType getDelete() {
        return (DeleteElementType)getMixed().get(WfsPackage.Literals.DOCUMENT_ROOT__DELETE, true);
    }

	/**
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @generated
     */
	public NotificationChain basicSetDelete(DeleteElementType newDelete, NotificationChain msgs) {
        return ((FeatureMap.Internal)getMixed()).basicAdd(WfsPackage.Literals.DOCUMENT_ROOT__DELETE, newDelete, msgs);
    }

	/**
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @generated
     */
	public void setDelete(DeleteElementType newDelete) {
        ((FeatureMap.Internal)getMixed()).set(WfsPackage.Literals.DOCUMENT_ROOT__DELETE, newDelete);
    }

	/**
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @generated
     */
	public DescribeFeatureTypeType getDescribeFeatureType() {
        return (DescribeFeatureTypeType)getMixed().get(WfsPackage.Literals.DOCUMENT_ROOT__DESCRIBE_FEATURE_TYPE, true);
    }

	/**
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @generated
     */
	public NotificationChain basicSetDescribeFeatureType(DescribeFeatureTypeType newDescribeFeatureType, NotificationChain msgs) {
        return ((FeatureMap.Internal)getMixed()).basicAdd(WfsPackage.Literals.DOCUMENT_ROOT__DESCRIBE_FEATURE_TYPE, newDescribeFeatureType, msgs);
    }

	/**
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @generated
     */
	public void setDescribeFeatureType(DescribeFeatureTypeType newDescribeFeatureType) {
        ((FeatureMap.Internal)getMixed()).set(WfsPackage.Literals.DOCUMENT_ROOT__DESCRIBE_FEATURE_TYPE, newDescribeFeatureType);
    }

	/**
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @generated
     */
	public FeatureCollectionType getFeatureCollection() {
        return (FeatureCollectionType)getMixed().get(WfsPackage.Literals.DOCUMENT_ROOT__FEATURE_COLLECTION, true);
    }

	/**
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @generated
     */
	public NotificationChain basicSetFeatureCollection(FeatureCollectionType newFeatureCollection, NotificationChain msgs) {
        return ((FeatureMap.Internal)getMixed()).basicAdd(WfsPackage.Literals.DOCUMENT_ROOT__FEATURE_COLLECTION, newFeatureCollection, msgs);
    }

	/**
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @generated
     */
	public void setFeatureCollection(FeatureCollectionType newFeatureCollection) {
        ((FeatureMap.Internal)getMixed()).set(WfsPackage.Literals.DOCUMENT_ROOT__FEATURE_COLLECTION, newFeatureCollection);
    }

	/**
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @generated
     */
	public FeatureTypeListType getFeatureTypeList() {
        return (FeatureTypeListType)getMixed().get(WfsPackage.Literals.DOCUMENT_ROOT__FEATURE_TYPE_LIST, true);
    }

	/**
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @generated
     */
	public NotificationChain basicSetFeatureTypeList(FeatureTypeListType newFeatureTypeList, NotificationChain msgs) {
        return ((FeatureMap.Internal)getMixed()).basicAdd(WfsPackage.Literals.DOCUMENT_ROOT__FEATURE_TYPE_LIST, newFeatureTypeList, msgs);
    }

	/**
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @generated
     */
	public void setFeatureTypeList(FeatureTypeListType newFeatureTypeList) {
        ((FeatureMap.Internal)getMixed()).set(WfsPackage.Literals.DOCUMENT_ROOT__FEATURE_TYPE_LIST, newFeatureTypeList);
    }

	/**
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @generated
     */
	public GetCapabilitiesType getGetCapabilities() {
        return (GetCapabilitiesType)getMixed().get(WfsPackage.Literals.DOCUMENT_ROOT__GET_CAPABILITIES, true);
    }

	/**
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @generated
     */
	public NotificationChain basicSetGetCapabilities(GetCapabilitiesType newGetCapabilities, NotificationChain msgs) {
        return ((FeatureMap.Internal)getMixed()).basicAdd(WfsPackage.Literals.DOCUMENT_ROOT__GET_CAPABILITIES, newGetCapabilities, msgs);
    }

	/**
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @generated
     */
	public void setGetCapabilities(GetCapabilitiesType newGetCapabilities) {
        ((FeatureMap.Internal)getMixed()).set(WfsPackage.Literals.DOCUMENT_ROOT__GET_CAPABILITIES, newGetCapabilities);
    }

	/**
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @generated
     */
	public GetFeatureType getGetFeature() {
        return (GetFeatureType)getMixed().get(WfsPackage.Literals.DOCUMENT_ROOT__GET_FEATURE, true);
    }

	/**
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @generated
     */
	public NotificationChain basicSetGetFeature(GetFeatureType newGetFeature, NotificationChain msgs) {
        return ((FeatureMap.Internal)getMixed()).basicAdd(WfsPackage.Literals.DOCUMENT_ROOT__GET_FEATURE, newGetFeature, msgs);
    }

	/**
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @generated
     */
	public void setGetFeature(GetFeatureType newGetFeature) {
        ((FeatureMap.Internal)getMixed()).set(WfsPackage.Literals.DOCUMENT_ROOT__GET_FEATURE, newGetFeature);
    }

	/**
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @generated
     */
	public GetFeatureWithLockType getGetFeatureWithLock() {
        return (GetFeatureWithLockType)getMixed().get(WfsPackage.Literals.DOCUMENT_ROOT__GET_FEATURE_WITH_LOCK, true);
    }

	/**
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @generated
     */
	public NotificationChain basicSetGetFeatureWithLock(GetFeatureWithLockType newGetFeatureWithLock, NotificationChain msgs) {
        return ((FeatureMap.Internal)getMixed()).basicAdd(WfsPackage.Literals.DOCUMENT_ROOT__GET_FEATURE_WITH_LOCK, newGetFeatureWithLock, msgs);
    }

	/**
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @generated
     */
	public void setGetFeatureWithLock(GetFeatureWithLockType newGetFeatureWithLock) {
        ((FeatureMap.Internal)getMixed()).set(WfsPackage.Literals.DOCUMENT_ROOT__GET_FEATURE_WITH_LOCK, newGetFeatureWithLock);
    }

	/**
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @generated
     */
	public GetGmlObjectType getGetGmlObject() {
        return (GetGmlObjectType)getMixed().get(WfsPackage.Literals.DOCUMENT_ROOT__GET_GML_OBJECT, true);
    }

	/**
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @generated
     */
	public NotificationChain basicSetGetGmlObject(GetGmlObjectType newGetGmlObject, NotificationChain msgs) {
        return ((FeatureMap.Internal)getMixed()).basicAdd(WfsPackage.Literals.DOCUMENT_ROOT__GET_GML_OBJECT, newGetGmlObject, msgs);
    }

	/**
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @generated
     */
	public void setGetGmlObject(GetGmlObjectType newGetGmlObject) {
        ((FeatureMap.Internal)getMixed()).set(WfsPackage.Literals.DOCUMENT_ROOT__GET_GML_OBJECT, newGetGmlObject);
    }

	/**
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @generated
     */
	public InsertElementType getInsert() {
        return (InsertElementType)getMixed().get(WfsPackage.Literals.DOCUMENT_ROOT__INSERT, true);
    }

	/**
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @generated
     */
	public NotificationChain basicSetInsert(InsertElementType newInsert, NotificationChain msgs) {
        return ((FeatureMap.Internal)getMixed()).basicAdd(WfsPackage.Literals.DOCUMENT_ROOT__INSERT, newInsert, msgs);
    }

	/**
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @generated
     */
	public void setInsert(InsertElementType newInsert) {
        ((FeatureMap.Internal)getMixed()).set(WfsPackage.Literals.DOCUMENT_ROOT__INSERT, newInsert);
    }

	/**
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @generated
     */
	public LockFeatureType getLockFeature() {
        return (LockFeatureType)getMixed().get(WfsPackage.Literals.DOCUMENT_ROOT__LOCK_FEATURE, true);
    }

	/**
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @generated
     */
	public NotificationChain basicSetLockFeature(LockFeatureType newLockFeature, NotificationChain msgs) {
        return ((FeatureMap.Internal)getMixed()).basicAdd(WfsPackage.Literals.DOCUMENT_ROOT__LOCK_FEATURE, newLockFeature, msgs);
    }

	/**
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @generated
     */
	public void setLockFeature(LockFeatureType newLockFeature) {
        ((FeatureMap.Internal)getMixed()).set(WfsPackage.Literals.DOCUMENT_ROOT__LOCK_FEATURE, newLockFeature);
    }

	/**
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @generated
     */
	public LockFeatureResponseType getLockFeatureResponse() {
        return (LockFeatureResponseType)getMixed().get(WfsPackage.Literals.DOCUMENT_ROOT__LOCK_FEATURE_RESPONSE, true);
    }

	/**
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @generated
     */
	public NotificationChain basicSetLockFeatureResponse(LockFeatureResponseType newLockFeatureResponse, NotificationChain msgs) {
        return ((FeatureMap.Internal)getMixed()).basicAdd(WfsPackage.Literals.DOCUMENT_ROOT__LOCK_FEATURE_RESPONSE, newLockFeatureResponse, msgs);
    }

	/**
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @generated
     */
	public void setLockFeatureResponse(LockFeatureResponseType newLockFeatureResponse) {
        ((FeatureMap.Internal)getMixed()).set(WfsPackage.Literals.DOCUMENT_ROOT__LOCK_FEATURE_RESPONSE, newLockFeatureResponse);
    }

	/**
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @generated
     */
	public String getLockId() {
        return (String)getMixed().get(WfsPackage.Literals.DOCUMENT_ROOT__LOCK_ID, true);
    }

	/**
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @generated
     */
	public void setLockId(String newLockId) {
        ((FeatureMap.Internal)getMixed()).set(WfsPackage.Literals.DOCUMENT_ROOT__LOCK_ID, newLockId);
    }

	/**
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @generated
     */
	public NativeType getNative() {
        return (NativeType)getMixed().get(WfsPackage.Literals.DOCUMENT_ROOT__NATIVE, true);
    }

	/**
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @generated
     */
	public NotificationChain basicSetNative(NativeType newNative, NotificationChain msgs) {
        return ((FeatureMap.Internal)getMixed()).basicAdd(WfsPackage.Literals.DOCUMENT_ROOT__NATIVE, newNative, msgs);
    }

	/**
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @generated
     */
	public void setNative(NativeType newNative) {
        ((FeatureMap.Internal)getMixed()).set(WfsPackage.Literals.DOCUMENT_ROOT__NATIVE, newNative);
    }

	/**
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @generated
     */
	public PropertyType getProperty() {
        return (PropertyType)getMixed().get(WfsPackage.Literals.DOCUMENT_ROOT__PROPERTY, true);
    }

	/**
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @generated
     */
	public NotificationChain basicSetProperty(PropertyType newProperty, NotificationChain msgs) {
        return ((FeatureMap.Internal)getMixed()).basicAdd(WfsPackage.Literals.DOCUMENT_ROOT__PROPERTY, newProperty, msgs);
    }

	/**
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @generated
     */
	public void setProperty(PropertyType newProperty) {
        ((FeatureMap.Internal)getMixed()).set(WfsPackage.Literals.DOCUMENT_ROOT__PROPERTY, newProperty);
    }

	/**
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @generated
     */
	public String getPropertyName() {
        return (String)getMixed().get(WfsPackage.Literals.DOCUMENT_ROOT__PROPERTY_NAME, true);
    }

	/**
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @generated
     */
	public void setPropertyName(String newPropertyName) {
        ((FeatureMap.Internal)getMixed()).set(WfsPackage.Literals.DOCUMENT_ROOT__PROPERTY_NAME, newPropertyName);
    }

	/**
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @generated
     */
	public QueryType getQuery() {
        return (QueryType)getMixed().get(WfsPackage.Literals.DOCUMENT_ROOT__QUERY, true);
    }

	/**
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @generated
     */
	public NotificationChain basicSetQuery(QueryType newQuery, NotificationChain msgs) {
        return ((FeatureMap.Internal)getMixed()).basicAdd(WfsPackage.Literals.DOCUMENT_ROOT__QUERY, newQuery, msgs);
    }

	/**
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @generated
     */
	public void setQuery(QueryType newQuery) {
        ((FeatureMap.Internal)getMixed()).set(WfsPackage.Literals.DOCUMENT_ROOT__QUERY, newQuery);
    }

	/**
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @generated
     */
	public GMLObjectTypeListType getServesGMLObjectTypeList() {
        return (GMLObjectTypeListType)getMixed().get(WfsPackage.Literals.DOCUMENT_ROOT__SERVES_GML_OBJECT_TYPE_LIST, true);
    }

	/**
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @generated
     */
	public NotificationChain basicSetServesGMLObjectTypeList(GMLObjectTypeListType newServesGMLObjectTypeList, NotificationChain msgs) {
        return ((FeatureMap.Internal)getMixed()).basicAdd(WfsPackage.Literals.DOCUMENT_ROOT__SERVES_GML_OBJECT_TYPE_LIST, newServesGMLObjectTypeList, msgs);
    }

	/**
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @generated
     */
	public void setServesGMLObjectTypeList(GMLObjectTypeListType newServesGMLObjectTypeList) {
        ((FeatureMap.Internal)getMixed()).set(WfsPackage.Literals.DOCUMENT_ROOT__SERVES_GML_OBJECT_TYPE_LIST, newServesGMLObjectTypeList);
    }

	/**
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @generated
     */
	public GMLObjectTypeListType getSupportsGMLObjectTypeList() {
        return (GMLObjectTypeListType)getMixed().get(WfsPackage.Literals.DOCUMENT_ROOT__SUPPORTS_GML_OBJECT_TYPE_LIST, true);
    }

	/**
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @generated
     */
	public NotificationChain basicSetSupportsGMLObjectTypeList(GMLObjectTypeListType newSupportsGMLObjectTypeList, NotificationChain msgs) {
        return ((FeatureMap.Internal)getMixed()).basicAdd(WfsPackage.Literals.DOCUMENT_ROOT__SUPPORTS_GML_OBJECT_TYPE_LIST, newSupportsGMLObjectTypeList, msgs);
    }

	/**
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @generated
     */
	public void setSupportsGMLObjectTypeList(GMLObjectTypeListType newSupportsGMLObjectTypeList) {
        ((FeatureMap.Internal)getMixed()).set(WfsPackage.Literals.DOCUMENT_ROOT__SUPPORTS_GML_OBJECT_TYPE_LIST, newSupportsGMLObjectTypeList);
    }

	/**
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @generated
     */
	public TransactionType getTransaction() {
        return (TransactionType)getMixed().get(WfsPackage.Literals.DOCUMENT_ROOT__TRANSACTION, true);
    }

	/**
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @generated
     */
	public NotificationChain basicSetTransaction(TransactionType newTransaction, NotificationChain msgs) {
        return ((FeatureMap.Internal)getMixed()).basicAdd(WfsPackage.Literals.DOCUMENT_ROOT__TRANSACTION, newTransaction, msgs);
    }

	/**
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @generated
     */
	public void setTransaction(TransactionType newTransaction) {
        ((FeatureMap.Internal)getMixed()).set(WfsPackage.Literals.DOCUMENT_ROOT__TRANSACTION, newTransaction);
    }

	/**
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @generated
     */
	public TransactionResponseType getTransactionResponse() {
        return (TransactionResponseType)getMixed().get(WfsPackage.Literals.DOCUMENT_ROOT__TRANSACTION_RESPONSE, true);
    }

	/**
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @generated
     */
	public NotificationChain basicSetTransactionResponse(TransactionResponseType newTransactionResponse, NotificationChain msgs) {
        return ((FeatureMap.Internal)getMixed()).basicAdd(WfsPackage.Literals.DOCUMENT_ROOT__TRANSACTION_RESPONSE, newTransactionResponse, msgs);
    }

	/**
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @generated
     */
	public void setTransactionResponse(TransactionResponseType newTransactionResponse) {
        ((FeatureMap.Internal)getMixed()).set(WfsPackage.Literals.DOCUMENT_ROOT__TRANSACTION_RESPONSE, newTransactionResponse);
    }

	/**
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @generated
     */
	public UpdateElementType getUpdate() {
        return (UpdateElementType)getMixed().get(WfsPackage.Literals.DOCUMENT_ROOT__UPDATE, true);
    }

	/**
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @generated
     */
	public NotificationChain basicSetUpdate(UpdateElementType newUpdate, NotificationChain msgs) {
        return ((FeatureMap.Internal)getMixed()).basicAdd(WfsPackage.Literals.DOCUMENT_ROOT__UPDATE, newUpdate, msgs);
    }

	/**
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @generated
     */
	public void setUpdate(UpdateElementType newUpdate) {
        ((FeatureMap.Internal)getMixed()).set(WfsPackage.Literals.DOCUMENT_ROOT__UPDATE, newUpdate);
    }

	/**
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @generated
     */
	public WFSCapabilitiesType getWfsCapabilities() {
        return (WFSCapabilitiesType)getMixed().get(WfsPackage.Literals.DOCUMENT_ROOT__WFS_CAPABILITIES, true);
    }

	/**
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @generated
     */
	public NotificationChain basicSetWfsCapabilities(WFSCapabilitiesType newWfsCapabilities, NotificationChain msgs) {
        return ((FeatureMap.Internal)getMixed()).basicAdd(WfsPackage.Literals.DOCUMENT_ROOT__WFS_CAPABILITIES, newWfsCapabilities, msgs);
    }

	/**
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @generated
     */
	public void setWfsCapabilities(WFSCapabilitiesType newWfsCapabilities) {
        ((FeatureMap.Internal)getMixed()).set(WfsPackage.Literals.DOCUMENT_ROOT__WFS_CAPABILITIES, newWfsCapabilities);
    }

	/**
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @generated
     */
	public XlinkPropertyNameType getXlinkPropertyName() {
        return (XlinkPropertyNameType)getMixed().get(WfsPackage.Literals.DOCUMENT_ROOT__XLINK_PROPERTY_NAME, true);
    }

	/**
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @generated
     */
	public NotificationChain basicSetXlinkPropertyName(XlinkPropertyNameType newXlinkPropertyName, NotificationChain msgs) {
        return ((FeatureMap.Internal)getMixed()).basicAdd(WfsPackage.Literals.DOCUMENT_ROOT__XLINK_PROPERTY_NAME, newXlinkPropertyName, msgs);
    }

	/**
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @generated
     */
	public void setXlinkPropertyName(XlinkPropertyNameType newXlinkPropertyName) {
        ((FeatureMap.Internal)getMixed()).set(WfsPackage.Literals.DOCUMENT_ROOT__XLINK_PROPERTY_NAME, newXlinkPropertyName);
    }

	/**
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @generated
     */
	public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
        switch (featureID) {
            case WfsPackage.DOCUMENT_ROOT__MIXED:
                return ((InternalEList)getMixed()).basicRemove(otherEnd, msgs);
            case WfsPackage.DOCUMENT_ROOT__XMLNS_PREFIX_MAP:
                return ((InternalEList)getXMLNSPrefixMap()).basicRemove(otherEnd, msgs);
            case WfsPackage.DOCUMENT_ROOT__XSI_SCHEMA_LOCATION:
                return ((InternalEList)getXSISchemaLocation()).basicRemove(otherEnd, msgs);
            case WfsPackage.DOCUMENT_ROOT__DELETE:
                return basicSetDelete(null, msgs);
            case WfsPackage.DOCUMENT_ROOT__DESCRIBE_FEATURE_TYPE:
                return basicSetDescribeFeatureType(null, msgs);
            case WfsPackage.DOCUMENT_ROOT__FEATURE_COLLECTION:
                return basicSetFeatureCollection(null, msgs);
            case WfsPackage.DOCUMENT_ROOT__FEATURE_TYPE_LIST:
                return basicSetFeatureTypeList(null, msgs);
            case WfsPackage.DOCUMENT_ROOT__GET_CAPABILITIES:
                return basicSetGetCapabilities(null, msgs);
            case WfsPackage.DOCUMENT_ROOT__GET_FEATURE:
                return basicSetGetFeature(null, msgs);
            case WfsPackage.DOCUMENT_ROOT__GET_FEATURE_WITH_LOCK:
                return basicSetGetFeatureWithLock(null, msgs);
            case WfsPackage.DOCUMENT_ROOT__GET_GML_OBJECT:
                return basicSetGetGmlObject(null, msgs);
            case WfsPackage.DOCUMENT_ROOT__INSERT:
                return basicSetInsert(null, msgs);
            case WfsPackage.DOCUMENT_ROOT__LOCK_FEATURE:
                return basicSetLockFeature(null, msgs);
            case WfsPackage.DOCUMENT_ROOT__LOCK_FEATURE_RESPONSE:
                return basicSetLockFeatureResponse(null, msgs);
            case WfsPackage.DOCUMENT_ROOT__NATIVE:
                return basicSetNative(null, msgs);
            case WfsPackage.DOCUMENT_ROOT__PROPERTY:
                return basicSetProperty(null, msgs);
            case WfsPackage.DOCUMENT_ROOT__QUERY:
                return basicSetQuery(null, msgs);
            case WfsPackage.DOCUMENT_ROOT__SERVES_GML_OBJECT_TYPE_LIST:
                return basicSetServesGMLObjectTypeList(null, msgs);
            case WfsPackage.DOCUMENT_ROOT__SUPPORTS_GML_OBJECT_TYPE_LIST:
                return basicSetSupportsGMLObjectTypeList(null, msgs);
            case WfsPackage.DOCUMENT_ROOT__TRANSACTION:
                return basicSetTransaction(null, msgs);
            case WfsPackage.DOCUMENT_ROOT__TRANSACTION_RESPONSE:
                return basicSetTransactionResponse(null, msgs);
            case WfsPackage.DOCUMENT_ROOT__UPDATE:
                return basicSetUpdate(null, msgs);
            case WfsPackage.DOCUMENT_ROOT__WFS_CAPABILITIES:
                return basicSetWfsCapabilities(null, msgs);
            case WfsPackage.DOCUMENT_ROOT__XLINK_PROPERTY_NAME:
                return basicSetXlinkPropertyName(null, msgs);
        }
        return super.eInverseRemove(otherEnd, featureID, msgs);
    }

	/**
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @generated
     */
	public Object eGet(int featureID, boolean resolve, boolean coreType) {
        switch (featureID) {
            case WfsPackage.DOCUMENT_ROOT__MIXED:
                if (coreType) return getMixed();
                return ((FeatureMap.Internal)getMixed()).getWrapper();
            case WfsPackage.DOCUMENT_ROOT__XMLNS_PREFIX_MAP:
                if (coreType) return getXMLNSPrefixMap();
                else return getXMLNSPrefixMap().map();
            case WfsPackage.DOCUMENT_ROOT__XSI_SCHEMA_LOCATION:
                if (coreType) return getXSISchemaLocation();
                else return getXSISchemaLocation().map();
            case WfsPackage.DOCUMENT_ROOT__DELETE:
                return getDelete();
            case WfsPackage.DOCUMENT_ROOT__DESCRIBE_FEATURE_TYPE:
                return getDescribeFeatureType();
            case WfsPackage.DOCUMENT_ROOT__FEATURE_COLLECTION:
                return getFeatureCollection();
            case WfsPackage.DOCUMENT_ROOT__FEATURE_TYPE_LIST:
                return getFeatureTypeList();
            case WfsPackage.DOCUMENT_ROOT__GET_CAPABILITIES:
                return getGetCapabilities();
            case WfsPackage.DOCUMENT_ROOT__GET_FEATURE:
                return getGetFeature();
            case WfsPackage.DOCUMENT_ROOT__GET_FEATURE_WITH_LOCK:
                return getGetFeatureWithLock();
            case WfsPackage.DOCUMENT_ROOT__GET_GML_OBJECT:
                return getGetGmlObject();
            case WfsPackage.DOCUMENT_ROOT__INSERT:
                return getInsert();
            case WfsPackage.DOCUMENT_ROOT__LOCK_FEATURE:
                return getLockFeature();
            case WfsPackage.DOCUMENT_ROOT__LOCK_FEATURE_RESPONSE:
                return getLockFeatureResponse();
            case WfsPackage.DOCUMENT_ROOT__LOCK_ID:
                return getLockId();
            case WfsPackage.DOCUMENT_ROOT__NATIVE:
                return getNative();
            case WfsPackage.DOCUMENT_ROOT__PROPERTY:
                return getProperty();
            case WfsPackage.DOCUMENT_ROOT__PROPERTY_NAME:
                return getPropertyName();
            case WfsPackage.DOCUMENT_ROOT__QUERY:
                return getQuery();
            case WfsPackage.DOCUMENT_ROOT__SERVES_GML_OBJECT_TYPE_LIST:
                return getServesGMLObjectTypeList();
            case WfsPackage.DOCUMENT_ROOT__SUPPORTS_GML_OBJECT_TYPE_LIST:
                return getSupportsGMLObjectTypeList();
            case WfsPackage.DOCUMENT_ROOT__TRANSACTION:
                return getTransaction();
            case WfsPackage.DOCUMENT_ROOT__TRANSACTION_RESPONSE:
                return getTransactionResponse();
            case WfsPackage.DOCUMENT_ROOT__UPDATE:
                return getUpdate();
            case WfsPackage.DOCUMENT_ROOT__WFS_CAPABILITIES:
                return getWfsCapabilities();
            case WfsPackage.DOCUMENT_ROOT__XLINK_PROPERTY_NAME:
                return getXlinkPropertyName();
        }
        return super.eGet(featureID, resolve, coreType);
    }

	/**
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @generated
     */
	public void eSet(int featureID, Object newValue) {
        switch (featureID) {
            case WfsPackage.DOCUMENT_ROOT__MIXED:
                ((FeatureMap.Internal)getMixed()).set(newValue);
                return;
            case WfsPackage.DOCUMENT_ROOT__XMLNS_PREFIX_MAP:
                ((EStructuralFeature.Setting)getXMLNSPrefixMap()).set(newValue);
                return;
            case WfsPackage.DOCUMENT_ROOT__XSI_SCHEMA_LOCATION:
                ((EStructuralFeature.Setting)getXSISchemaLocation()).set(newValue);
                return;
            case WfsPackage.DOCUMENT_ROOT__DELETE:
                setDelete((DeleteElementType)newValue);
                return;
            case WfsPackage.DOCUMENT_ROOT__DESCRIBE_FEATURE_TYPE:
                setDescribeFeatureType((DescribeFeatureTypeType)newValue);
                return;
            case WfsPackage.DOCUMENT_ROOT__FEATURE_COLLECTION:
                setFeatureCollection((FeatureCollectionType)newValue);
                return;
            case WfsPackage.DOCUMENT_ROOT__FEATURE_TYPE_LIST:
                setFeatureTypeList((FeatureTypeListType)newValue);
                return;
            case WfsPackage.DOCUMENT_ROOT__GET_CAPABILITIES:
                setGetCapabilities((GetCapabilitiesType)newValue);
                return;
            case WfsPackage.DOCUMENT_ROOT__GET_FEATURE:
                setGetFeature((GetFeatureType)newValue);
                return;
            case WfsPackage.DOCUMENT_ROOT__GET_FEATURE_WITH_LOCK:
                setGetFeatureWithLock((GetFeatureWithLockType)newValue);
                return;
            case WfsPackage.DOCUMENT_ROOT__GET_GML_OBJECT:
                setGetGmlObject((GetGmlObjectType)newValue);
                return;
            case WfsPackage.DOCUMENT_ROOT__INSERT:
                setInsert((InsertElementType)newValue);
                return;
            case WfsPackage.DOCUMENT_ROOT__LOCK_FEATURE:
                setLockFeature((LockFeatureType)newValue);
                return;
            case WfsPackage.DOCUMENT_ROOT__LOCK_FEATURE_RESPONSE:
                setLockFeatureResponse((LockFeatureResponseType)newValue);
                return;
            case WfsPackage.DOCUMENT_ROOT__LOCK_ID:
                setLockId((String)newValue);
                return;
            case WfsPackage.DOCUMENT_ROOT__NATIVE:
                setNative((NativeType)newValue);
                return;
            case WfsPackage.DOCUMENT_ROOT__PROPERTY:
                setProperty((PropertyType)newValue);
                return;
            case WfsPackage.DOCUMENT_ROOT__PROPERTY_NAME:
                setPropertyName((String)newValue);
                return;
            case WfsPackage.DOCUMENT_ROOT__QUERY:
                setQuery((QueryType)newValue);
                return;
            case WfsPackage.DOCUMENT_ROOT__SERVES_GML_OBJECT_TYPE_LIST:
                setServesGMLObjectTypeList((GMLObjectTypeListType)newValue);
                return;
            case WfsPackage.DOCUMENT_ROOT__SUPPORTS_GML_OBJECT_TYPE_LIST:
                setSupportsGMLObjectTypeList((GMLObjectTypeListType)newValue);
                return;
            case WfsPackage.DOCUMENT_ROOT__TRANSACTION:
                setTransaction((TransactionType)newValue);
                return;
            case WfsPackage.DOCUMENT_ROOT__TRANSACTION_RESPONSE:
                setTransactionResponse((TransactionResponseType)newValue);
                return;
            case WfsPackage.DOCUMENT_ROOT__UPDATE:
                setUpdate((UpdateElementType)newValue);
                return;
            case WfsPackage.DOCUMENT_ROOT__WFS_CAPABILITIES:
                setWfsCapabilities((WFSCapabilitiesType)newValue);
                return;
            case WfsPackage.DOCUMENT_ROOT__XLINK_PROPERTY_NAME:
                setXlinkPropertyName((XlinkPropertyNameType)newValue);
                return;
        }
        super.eSet(featureID, newValue);
    }

	/**
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @generated
     */
	public void eUnset(int featureID) {
        switch (featureID) {
            case WfsPackage.DOCUMENT_ROOT__MIXED:
                getMixed().clear();
                return;
            case WfsPackage.DOCUMENT_ROOT__XMLNS_PREFIX_MAP:
                getXMLNSPrefixMap().clear();
                return;
            case WfsPackage.DOCUMENT_ROOT__XSI_SCHEMA_LOCATION:
                getXSISchemaLocation().clear();
                return;
            case WfsPackage.DOCUMENT_ROOT__DELETE:
                setDelete((DeleteElementType)null);
                return;
            case WfsPackage.DOCUMENT_ROOT__DESCRIBE_FEATURE_TYPE:
                setDescribeFeatureType((DescribeFeatureTypeType)null);
                return;
            case WfsPackage.DOCUMENT_ROOT__FEATURE_COLLECTION:
                setFeatureCollection((FeatureCollectionType)null);
                return;
            case WfsPackage.DOCUMENT_ROOT__FEATURE_TYPE_LIST:
                setFeatureTypeList((FeatureTypeListType)null);
                return;
            case WfsPackage.DOCUMENT_ROOT__GET_CAPABILITIES:
                setGetCapabilities((GetCapabilitiesType)null);
                return;
            case WfsPackage.DOCUMENT_ROOT__GET_FEATURE:
                setGetFeature((GetFeatureType)null);
                return;
            case WfsPackage.DOCUMENT_ROOT__GET_FEATURE_WITH_LOCK:
                setGetFeatureWithLock((GetFeatureWithLockType)null);
                return;
            case WfsPackage.DOCUMENT_ROOT__GET_GML_OBJECT:
                setGetGmlObject((GetGmlObjectType)null);
                return;
            case WfsPackage.DOCUMENT_ROOT__INSERT:
                setInsert((InsertElementType)null);
                return;
            case WfsPackage.DOCUMENT_ROOT__LOCK_FEATURE:
                setLockFeature((LockFeatureType)null);
                return;
            case WfsPackage.DOCUMENT_ROOT__LOCK_FEATURE_RESPONSE:
                setLockFeatureResponse((LockFeatureResponseType)null);
                return;
            case WfsPackage.DOCUMENT_ROOT__LOCK_ID:
                setLockId(LOCK_ID_EDEFAULT);
                return;
            case WfsPackage.DOCUMENT_ROOT__NATIVE:
                setNative((NativeType)null);
                return;
            case WfsPackage.DOCUMENT_ROOT__PROPERTY:
                setProperty((PropertyType)null);
                return;
            case WfsPackage.DOCUMENT_ROOT__PROPERTY_NAME:
                setPropertyName(PROPERTY_NAME_EDEFAULT);
                return;
            case WfsPackage.DOCUMENT_ROOT__QUERY:
                setQuery((QueryType)null);
                return;
            case WfsPackage.DOCUMENT_ROOT__SERVES_GML_OBJECT_TYPE_LIST:
                setServesGMLObjectTypeList((GMLObjectTypeListType)null);
                return;
            case WfsPackage.DOCUMENT_ROOT__SUPPORTS_GML_OBJECT_TYPE_LIST:
                setSupportsGMLObjectTypeList((GMLObjectTypeListType)null);
                return;
            case WfsPackage.DOCUMENT_ROOT__TRANSACTION:
                setTransaction((TransactionType)null);
                return;
            case WfsPackage.DOCUMENT_ROOT__TRANSACTION_RESPONSE:
                setTransactionResponse((TransactionResponseType)null);
                return;
            case WfsPackage.DOCUMENT_ROOT__UPDATE:
                setUpdate((UpdateElementType)null);
                return;
            case WfsPackage.DOCUMENT_ROOT__WFS_CAPABILITIES:
                setWfsCapabilities((WFSCapabilitiesType)null);
                return;
            case WfsPackage.DOCUMENT_ROOT__XLINK_PROPERTY_NAME:
                setXlinkPropertyName((XlinkPropertyNameType)null);
                return;
        }
        super.eUnset(featureID);
    }

	/**
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @generated
     */
	public boolean eIsSet(int featureID) {
        switch (featureID) {
            case WfsPackage.DOCUMENT_ROOT__MIXED:
                return mixed != null && !mixed.isEmpty();
            case WfsPackage.DOCUMENT_ROOT__XMLNS_PREFIX_MAP:
                return xMLNSPrefixMap != null && !xMLNSPrefixMap.isEmpty();
            case WfsPackage.DOCUMENT_ROOT__XSI_SCHEMA_LOCATION:
                return xSISchemaLocation != null && !xSISchemaLocation.isEmpty();
            case WfsPackage.DOCUMENT_ROOT__DELETE:
                return getDelete() != null;
            case WfsPackage.DOCUMENT_ROOT__DESCRIBE_FEATURE_TYPE:
                return getDescribeFeatureType() != null;
            case WfsPackage.DOCUMENT_ROOT__FEATURE_COLLECTION:
                return getFeatureCollection() != null;
            case WfsPackage.DOCUMENT_ROOT__FEATURE_TYPE_LIST:
                return getFeatureTypeList() != null;
            case WfsPackage.DOCUMENT_ROOT__GET_CAPABILITIES:
                return getGetCapabilities() != null;
            case WfsPackage.DOCUMENT_ROOT__GET_FEATURE:
                return getGetFeature() != null;
            case WfsPackage.DOCUMENT_ROOT__GET_FEATURE_WITH_LOCK:
                return getGetFeatureWithLock() != null;
            case WfsPackage.DOCUMENT_ROOT__GET_GML_OBJECT:
                return getGetGmlObject() != null;
            case WfsPackage.DOCUMENT_ROOT__INSERT:
                return getInsert() != null;
            case WfsPackage.DOCUMENT_ROOT__LOCK_FEATURE:
                return getLockFeature() != null;
            case WfsPackage.DOCUMENT_ROOT__LOCK_FEATURE_RESPONSE:
                return getLockFeatureResponse() != null;
            case WfsPackage.DOCUMENT_ROOT__LOCK_ID:
                return LOCK_ID_EDEFAULT == null ? getLockId() != null : !LOCK_ID_EDEFAULT.equals(getLockId());
            case WfsPackage.DOCUMENT_ROOT__NATIVE:
                return getNative() != null;
            case WfsPackage.DOCUMENT_ROOT__PROPERTY:
                return getProperty() != null;
            case WfsPackage.DOCUMENT_ROOT__PROPERTY_NAME:
                return PROPERTY_NAME_EDEFAULT == null ? getPropertyName() != null : !PROPERTY_NAME_EDEFAULT.equals(getPropertyName());
            case WfsPackage.DOCUMENT_ROOT__QUERY:
                return getQuery() != null;
            case WfsPackage.DOCUMENT_ROOT__SERVES_GML_OBJECT_TYPE_LIST:
                return getServesGMLObjectTypeList() != null;
            case WfsPackage.DOCUMENT_ROOT__SUPPORTS_GML_OBJECT_TYPE_LIST:
                return getSupportsGMLObjectTypeList() != null;
            case WfsPackage.DOCUMENT_ROOT__TRANSACTION:
                return getTransaction() != null;
            case WfsPackage.DOCUMENT_ROOT__TRANSACTION_RESPONSE:
                return getTransactionResponse() != null;
            case WfsPackage.DOCUMENT_ROOT__UPDATE:
                return getUpdate() != null;
            case WfsPackage.DOCUMENT_ROOT__WFS_CAPABILITIES:
                return getWfsCapabilities() != null;
            case WfsPackage.DOCUMENT_ROOT__XLINK_PROPERTY_NAME:
                return getXlinkPropertyName() != null;
        }
        return super.eIsSet(featureID);
    }

	/**
     * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
     * @generated
     */
	public String toString() {
        if (eIsProxy()) return super.toString();

        StringBuffer result = new StringBuffer(super.toString());
        result.append(" (mixed: ");
        result.append(mixed);
        result.append(')');
        return result.toString();
    }

} //DocumentRootImpl