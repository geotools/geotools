/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package net.opengis.wfsv;

import org.eclipse.emf.common.util.EMap;

import org.eclipse.emf.ecore.EObject;

import org.eclipse.emf.ecore.util.FeatureMap;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Document Root</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link net.opengis.wfsv.DocumentRoot#getMixed <em>Mixed</em>}</li>
 *   <li>{@link net.opengis.wfsv.DocumentRoot#getXMLNSPrefixMap <em>XMLNS Prefix Map</em>}</li>
 *   <li>{@link net.opengis.wfsv.DocumentRoot#getXSISchemaLocation <em>XSI Schema Location</em>}</li>
 *   <li>{@link net.opengis.wfsv.DocumentRoot#getDescribeVersionedFeatureType <em>Describe Versioned Feature Type</em>}</li>
 *   <li>{@link net.opengis.wfsv.DocumentRoot#getDifferenceQuery <em>Difference Query</em>}</li>
 *   <li>{@link net.opengis.wfsv.DocumentRoot#getGetDiff <em>Get Diff</em>}</li>
 *   <li>{@link net.opengis.wfsv.DocumentRoot#getGetLog <em>Get Log</em>}</li>
 *   <li>{@link net.opengis.wfsv.DocumentRoot#getGetVersionedFeature <em>Get Versioned Feature</em>}</li>
 *   <li>{@link net.opengis.wfsv.DocumentRoot#getRollback <em>Rollback</em>}</li>
 *   <li>{@link net.opengis.wfsv.DocumentRoot#getVersionedDelete <em>Versioned Delete</em>}</li>
 *   <li>{@link net.opengis.wfsv.DocumentRoot#getVersionedFeatureCollection <em>Versioned Feature Collection</em>}</li>
 *   <li>{@link net.opengis.wfsv.DocumentRoot#getVersionedUpdate <em>Versioned Update</em>}</li>
 * </ul>
 * </p>
 *
 * @see net.opengis.wfsv.WfsvPackage#getDocumentRoot()
 * @model extendedMetaData="name='' kind='mixed'"
 * @generated
 */
public interface DocumentRoot extends EObject {
    /**
     * Returns the value of the '<em><b>Mixed</b></em>' attribute list.
     * The list contents are of type {@link org.eclipse.emf.ecore.util.FeatureMap.Entry}.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Mixed</em>' attribute list isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Mixed</em>' attribute list.
     * @see net.opengis.wfsv.WfsvPackage#getDocumentRoot_Mixed()
     * @model unique="false" dataType="org.eclipse.emf.ecore.EFeatureMapEntry" many="true"
     *        extendedMetaData="kind='elementWildcard' name=':mixed'"
     * @generated
     */
    FeatureMap getMixed();

    /**
     * Returns the value of the '<em><b>XMLNS Prefix Map</b></em>' map.
     * The key is of type {@link java.lang.String},
     * and the value is of type {@link java.lang.String},
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>XMLNS Prefix Map</em>' map isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>XMLNS Prefix Map</em>' map.
     * @see net.opengis.wfsv.WfsvPackage#getDocumentRoot_XMLNSPrefixMap()
     * @model mapType="org.eclipse.emf.ecore.EStringToStringMapEntry" keyType="java.lang.String" valueType="java.lang.String" transient="true"
     *        extendedMetaData="kind='attribute' name='xmlns:prefix'"
     * @generated
     */
    EMap getXMLNSPrefixMap();

    /**
     * Returns the value of the '<em><b>XSI Schema Location</b></em>' map.
     * The key is of type {@link java.lang.String},
     * and the value is of type {@link java.lang.String},
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>XSI Schema Location</em>' map isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>XSI Schema Location</em>' map.
     * @see net.opengis.wfsv.WfsvPackage#getDocumentRoot_XSISchemaLocation()
     * @model mapType="org.eclipse.emf.ecore.EStringToStringMapEntry" keyType="java.lang.String" valueType="java.lang.String" transient="true"
     *        extendedMetaData="kind='attribute' name='xsi:schemaLocation'"
     * @generated
     */
    EMap getXSISchemaLocation();

    /**
     * Returns the value of the '<em><b>Describe Versioned Feature Type</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * 
     *             Same as wfs:DescribeFeatureType, but with the option to output
     *             a versioned feature type instead of a plain one
     *          
     * <!-- end-model-doc -->
     * @return the value of the '<em>Describe Versioned Feature Type</em>' attribute.
     * @see #setDescribeVersionedFeatureType(Object)
     * @see net.opengis.wfsv.WfsvPackage#getDocumentRoot_DescribeVersionedFeatureType()
     * @model unique="false" dataType="org.eclipse.emf.ecore.xml.type.AnySimpleType" upper="-2" transient="true" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='DescribeVersionedFeatureType' namespace='##targetNamespace' affiliation='http://www.opengis.net/wfs#DescribeFeatureType'"
     * @generated
     */
    Object getDescribeVersionedFeatureType();

    /**
     * Sets the value of the '{@link net.opengis.wfsv.DocumentRoot#getDescribeVersionedFeatureType <em>Describe Versioned Feature Type</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Describe Versioned Feature Type</em>' attribute.
     * @see #getDescribeVersionedFeatureType()
     * @generated
     */
    void setDescribeVersionedFeatureType(Object value);

    /**
     * Returns the value of the '<em><b>Difference Query</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * 
     *         The DifferenceFilter element is used to gather differences in features matched by a standard
     *         OGC filter at starting and ending featureVersion, and a filter used to match
     *       
     * <!-- end-model-doc -->
     * @return the value of the '<em>Difference Query</em>' containment reference.
     * @see #setDifferenceQuery(DifferenceQueryType)
     * @see net.opengis.wfsv.WfsvPackage#getDocumentRoot_DifferenceQuery()
     * @model containment="true" upper="-2" transient="true" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='DifferenceQuery' namespace='##targetNamespace'"
     * @generated
     */
    DifferenceQueryType getDifferenceQuery();

    /**
     * Sets the value of the '{@link net.opengis.wfsv.DocumentRoot#getDifferenceQuery <em>Difference Query</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Difference Query</em>' containment reference.
     * @see #getDifferenceQuery()
     * @generated
     */
    void setDifferenceQuery(DifferenceQueryType value);

    /**
     * Returns the value of the '<em><b>Get Diff</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * 
     *         The GetDiff element is used to request that a Versioning Web Feature Service returns a
     *         transaction command that can be used to alter features at version m to turn them into
     *         version n
     *       
     * <!-- end-model-doc -->
     * @return the value of the '<em>Get Diff</em>' containment reference.
     * @see #setGetDiff(GetDiffType)
     * @see net.opengis.wfsv.WfsvPackage#getDocumentRoot_GetDiff()
     * @model containment="true" upper="-2" transient="true" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='GetDiff' namespace='##targetNamespace'"
     * @generated
     */
    GetDiffType getGetDiff();

    /**
     * Sets the value of the '{@link net.opengis.wfsv.DocumentRoot#getGetDiff <em>Get Diff</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Get Diff</em>' containment reference.
     * @see #getGetDiff()
     * @generated
     */
    void setGetDiff(GetDiffType value);

    /**
     * Returns the value of the '<em><b>Get Log</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * 
     *         The GetLog element is used to request that a Versioning Web Feature Service return the
     *         change history for features of one or more feature types.
     *       
     * <!-- end-model-doc -->
     * @return the value of the '<em>Get Log</em>' containment reference.
     * @see #setGetLog(GetLogType)
     * @see net.opengis.wfsv.WfsvPackage#getDocumentRoot_GetLog()
     * @model containment="true" upper="-2" transient="true" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='GetLog' namespace='##targetNamespace'"
     * @generated
     */
    GetLogType getGetLog();

    /**
     * Sets the value of the '{@link net.opengis.wfsv.DocumentRoot#getGetLog <em>Get Log</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Get Log</em>' containment reference.
     * @see #getGetLog()
     * @generated
     */
    void setGetLog(GetLogType value);

    /**
     * Returns the value of the '<em><b>Get Versioned Feature</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * 
     *             The GetVersionedFeature element is used to request that a Versioning 
     *             Web Feature Service return versioned feature type instances 
     *             of one or more feature types.
     *          
     * <!-- end-model-doc -->
     * @return the value of the '<em>Get Versioned Feature</em>' containment reference.
     * @see #setGetVersionedFeature(GetVersionedFeatureType)
     * @see net.opengis.wfsv.WfsvPackage#getDocumentRoot_GetVersionedFeature()
     * @model containment="true" upper="-2" transient="true" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='GetVersionedFeature' namespace='##targetNamespace' affiliation='http://www.opengis.net/wfs#GetFeature'"
     * @generated
     */
    GetVersionedFeatureType getGetVersionedFeature();

    /**
     * Sets the value of the '{@link net.opengis.wfsv.DocumentRoot#getGetVersionedFeature <em>Get Versioned Feature</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Get Versioned Feature</em>' containment reference.
     * @see #getGetVersionedFeature()
     * @generated
     */
    void setGetVersionedFeature(GetVersionedFeatureType value);

    /**
     * Returns the value of the '<em><b>Rollback</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * 
     *         Rolls back the changes occurred on all matched features between fromFeatureVersion and
     *         featureVersion
     *       
     * <!-- end-model-doc -->
     * @return the value of the '<em>Rollback</em>' containment reference.
     * @see #setRollback(RollbackType)
     * @see net.opengis.wfsv.WfsvPackage#getDocumentRoot_Rollback()
     * @model containment="true" upper="-2" transient="true" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='Rollback' namespace='##targetNamespace' affiliation='http://www.opengis.net/wfs#Native'"
     * @generated
     */
    RollbackType getRollback();

    /**
     * Sets the value of the '{@link net.opengis.wfsv.DocumentRoot#getRollback <em>Rollback</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Rollback</em>' containment reference.
     * @see #getRollback()
     * @generated
     */
    void setRollback(RollbackType value);

    /**
     * Returns the value of the '<em><b>Versioned Delete</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Versioned Delete</em>' containment reference isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Versioned Delete</em>' containment reference.
     * @see #setVersionedDelete(VersionedDeleteElementType)
     * @see net.opengis.wfsv.WfsvPackage#getDocumentRoot_VersionedDelete()
     * @model containment="true" upper="-2" transient="true" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='VersionedDelete' namespace='##targetNamespace' affiliation='http://www.opengis.net/wfs#Delete'"
     * @generated
     */
    VersionedDeleteElementType getVersionedDelete();

    /**
     * Sets the value of the '{@link net.opengis.wfsv.DocumentRoot#getVersionedDelete <em>Versioned Delete</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Versioned Delete</em>' containment reference.
     * @see #getVersionedDelete()
     * @generated
     */
    void setVersionedDelete(VersionedDeleteElementType value);

    /**
     * Returns the value of the '<em><b>Versioned Feature Collection</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Versioned Feature Collection</em>' containment reference isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Versioned Feature Collection</em>' containment reference.
     * @see #setVersionedFeatureCollection(VersionedFeatureCollectionType)
     * @see net.opengis.wfsv.WfsvPackage#getDocumentRoot_VersionedFeatureCollection()
     * @model containment="true" upper="-2" transient="true" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='VersionedFeatureCollection' namespace='##targetNamespace' affiliation='http://www.opengis.net/wfs#FeatureCollection'"
     * @generated
     */
    VersionedFeatureCollectionType getVersionedFeatureCollection();

    /**
     * Sets the value of the '{@link net.opengis.wfsv.DocumentRoot#getVersionedFeatureCollection <em>Versioned Feature Collection</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Versioned Feature Collection</em>' containment reference.
     * @see #getVersionedFeatureCollection()
     * @generated
     */
    void setVersionedFeatureCollection(VersionedFeatureCollectionType value);

    /**
     * Returns the value of the '<em><b>Versioned Update</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Versioned Update</em>' containment reference isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Versioned Update</em>' containment reference.
     * @see #setVersionedUpdate(VersionedUpdateElementType)
     * @see net.opengis.wfsv.WfsvPackage#getDocumentRoot_VersionedUpdate()
     * @model containment="true" upper="-2" transient="true" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='VersionedUpdate' namespace='##targetNamespace' affiliation='http://www.opengis.net/wfs#Update'"
     * @generated
     */
    VersionedUpdateElementType getVersionedUpdate();

    /**
     * Sets the value of the '{@link net.opengis.wfsv.DocumentRoot#getVersionedUpdate <em>Versioned Update</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Versioned Update</em>' containment reference.
     * @see #getVersionedUpdate()
     * @generated
     */
    void setVersionedUpdate(VersionedUpdateElementType value);

} // DocumentRoot
