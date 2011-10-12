/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package net.opengis.wfs20;

import org.eclipse.emf.ecore.EObject;

import org.eclipse.emf.ecore.util.FeatureMap;

import org.w3.xlink.ActuateType;
import org.w3.xlink.ShowType;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Member Property Type</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link net.opengis.wfs20.MemberPropertyType#getMixed <em>Mixed</em>}</li>
 *   <li>{@link net.opengis.wfs20.MemberPropertyType#getAny <em>Any</em>}</li>
 *   <li>{@link net.opengis.wfs20.MemberPropertyType#getTuple <em>Tuple</em>}</li>
 *   <li>{@link net.opengis.wfs20.MemberPropertyType#getSimpleFeatureCollectionGroup <em>Simple Feature Collection Group</em>}</li>
 *   <li>{@link net.opengis.wfs20.MemberPropertyType#getSimpleFeatureCollection <em>Simple Feature Collection</em>}</li>
 *   <li>{@link net.opengis.wfs20.MemberPropertyType#getActuate <em>Actuate</em>}</li>
 *   <li>{@link net.opengis.wfs20.MemberPropertyType#getArcrole <em>Arcrole</em>}</li>
 *   <li>{@link net.opengis.wfs20.MemberPropertyType#getHref <em>Href</em>}</li>
 *   <li>{@link net.opengis.wfs20.MemberPropertyType#getRole <em>Role</em>}</li>
 *   <li>{@link net.opengis.wfs20.MemberPropertyType#getShow <em>Show</em>}</li>
 *   <li>{@link net.opengis.wfs20.MemberPropertyType#getState <em>State</em>}</li>
 *   <li>{@link net.opengis.wfs20.MemberPropertyType#getTitle <em>Title</em>}</li>
 *   <li>{@link net.opengis.wfs20.MemberPropertyType#getType <em>Type</em>}</li>
 * </ul>
 * </p>
 *
 * @see net.opengis.wfs20.Wfs20Package#getMemberPropertyType()
 * @model extendedMetaData="name='MemberPropertyType' kind='mixed'"
 * @generated
 */
public interface MemberPropertyType extends EObject {
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
     * @see net.opengis.wfs20.Wfs20Package#getMemberPropertyType_Mixed()
     * @model unique="false" dataType="org.eclipse.emf.ecore.EFeatureMapEntry" many="true"
     *        extendedMetaData="kind='elementWildcard' name=':mixed'"
     * @generated
     */
    FeatureMap getMixed();

    /**
     * Returns the value of the '<em><b>Any</b></em>' attribute list.
     * The list contents are of type {@link org.eclipse.emf.ecore.util.FeatureMap.Entry}.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Any</em>' attribute list isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Any</em>' attribute list.
     * @see net.opengis.wfs20.Wfs20Package#getMemberPropertyType_Any()
     * @model dataType="org.eclipse.emf.ecore.EFeatureMapEntry" many="false" transient="true" volatile="true" derived="true"
     *        extendedMetaData="kind='elementWildcard' wildcards='##other' name=':1' processing='lax'"
     * @generated
     */
    FeatureMap getAny();

    /**
     * Returns the value of the '<em><b>Tuple</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Tuple</em>' containment reference isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Tuple</em>' containment reference.
     * @see #setTuple(TupleType)
     * @see net.opengis.wfs20.Wfs20Package#getMemberPropertyType_Tuple()
     * @model containment="true" transient="true" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='Tuple' namespace='##targetNamespace'"
     * @generated
     */
    TupleType getTuple();

    /**
     * Sets the value of the '{@link net.opengis.wfs20.MemberPropertyType#getTuple <em>Tuple</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Tuple</em>' containment reference.
     * @see #getTuple()
     * @generated
     */
    void setTuple(TupleType value);

    /**
     * Returns the value of the '<em><b>Simple Feature Collection Group</b></em>' attribute list.
     * The list contents are of type {@link org.eclipse.emf.ecore.util.FeatureMap.Entry}.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Simple Feature Collection Group</em>' attribute list isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Simple Feature Collection Group</em>' attribute list.
     * @see net.opengis.wfs20.Wfs20Package#getMemberPropertyType_SimpleFeatureCollectionGroup()
     * @model dataType="org.eclipse.emf.ecore.EFeatureMapEntry" many="false" transient="true" volatile="true" derived="true"
     *        extendedMetaData="kind='group' name='SimpleFeatureCollection:group' namespace='##targetNamespace'"
     * @generated
     */
    FeatureMap getSimpleFeatureCollectionGroup();

    /**
     * Returns the value of the '<em><b>Simple Feature Collection</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Simple Feature Collection</em>' containment reference isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Simple Feature Collection</em>' containment reference.
     * @see #setSimpleFeatureCollection(SimpleFeatureCollectionType)
     * @see net.opengis.wfs20.Wfs20Package#getMemberPropertyType_SimpleFeatureCollection()
     * @model containment="true" transient="true" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='SimpleFeatureCollection' namespace='##targetNamespace' group='SimpleFeatureCollection:group'"
     * @generated
     */
    SimpleFeatureCollectionType getSimpleFeatureCollection();

    /**
     * Sets the value of the '{@link net.opengis.wfs20.MemberPropertyType#getSimpleFeatureCollection <em>Simple Feature Collection</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Simple Feature Collection</em>' containment reference.
     * @see #getSimpleFeatureCollection()
     * @generated
     */
    void setSimpleFeatureCollection(SimpleFeatureCollectionType value);

    /**
     * Returns the value of the '<em><b>Actuate</b></em>' attribute.
     * The literals are from the enumeration {@link org.w3.xlink.ActuateType}.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * 
     *         The 'actuate' attribute is used to communicate the desired timing 
     *         of traversal from the starting resource to the ending resource; 
     *         it's value should be treated as follows:
     *         onLoad - traverse to the ending resource immediately on loading 
     *                  the starting resource 
     *         onRequest - traverse from the starting resource to the ending 
     *                     resource only on a post-loading event triggered for 
     *                     this purpose 
     *         other - behavior is unconstrained; examine other markup in link 
     *                 for hints 
     *         none - behavior is unconstrained
     *       
     * <!-- end-model-doc -->
     * @return the value of the '<em>Actuate</em>' attribute.
     * @see org.w3.xlink.ActuateType
     * @see #isSetActuate()
     * @see #unsetActuate()
     * @see #setActuate(ActuateType)
     * @see net.opengis.wfs20.Wfs20Package#getMemberPropertyType_Actuate()
     * @model unsettable="true"
     *        extendedMetaData="kind='attribute' name='actuate' namespace='http://www.w3.org/1999/xlink'"
     * @generated
     */
    ActuateType getActuate();

    /**
     * Sets the value of the '{@link net.opengis.wfs20.MemberPropertyType#getActuate <em>Actuate</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Actuate</em>' attribute.
     * @see org.w3.xlink.ActuateType
     * @see #isSetActuate()
     * @see #unsetActuate()
     * @see #getActuate()
     * @generated
     */
    void setActuate(ActuateType value);

    /**
     * Unsets the value of the '{@link net.opengis.wfs20.MemberPropertyType#getActuate <em>Actuate</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #isSetActuate()
     * @see #getActuate()
     * @see #setActuate(ActuateType)
     * @generated
     */
    void unsetActuate();

    /**
     * Returns whether the value of the '{@link net.opengis.wfs20.MemberPropertyType#getActuate <em>Actuate</em>}' attribute is set.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return whether the value of the '<em>Actuate</em>' attribute is set.
     * @see #unsetActuate()
     * @see #getActuate()
     * @see #setActuate(ActuateType)
     * @generated
     */
    boolean isSetActuate();

    /**
     * Returns the value of the '<em><b>Arcrole</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Arcrole</em>' attribute isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Arcrole</em>' attribute.
     * @see #setArcrole(String)
     * @see net.opengis.wfs20.Wfs20Package#getMemberPropertyType_Arcrole()
     * @model dataType="org.eclipse.emf.ecore.xml.type.AnyURI"
     *        extendedMetaData="kind='attribute' name='arcrole' namespace='http://www.w3.org/1999/xlink'"
     * @generated
     */
    String getArcrole();

    /**
     * Sets the value of the '{@link net.opengis.wfs20.MemberPropertyType#getArcrole <em>Arcrole</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Arcrole</em>' attribute.
     * @see #getArcrole()
     * @generated
     */
    void setArcrole(String value);

    /**
     * Returns the value of the '<em><b>Href</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Href</em>' attribute isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Href</em>' attribute.
     * @see #setHref(String)
     * @see net.opengis.wfs20.Wfs20Package#getMemberPropertyType_Href()
     * @model dataType="org.eclipse.emf.ecore.xml.type.AnyURI"
     *        extendedMetaData="kind='attribute' name='href' namespace='http://www.w3.org/1999/xlink'"
     * @generated
     */
    String getHref();

    /**
     * Sets the value of the '{@link net.opengis.wfs20.MemberPropertyType#getHref <em>Href</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Href</em>' attribute.
     * @see #getHref()
     * @generated
     */
    void setHref(String value);

    /**
     * Returns the value of the '<em><b>Role</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Role</em>' attribute isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Role</em>' attribute.
     * @see #setRole(String)
     * @see net.opengis.wfs20.Wfs20Package#getMemberPropertyType_Role()
     * @model dataType="org.eclipse.emf.ecore.xml.type.AnyURI"
     *        extendedMetaData="kind='attribute' name='role' namespace='http://www.w3.org/1999/xlink'"
     * @generated
     */
    String getRole();

    /**
     * Sets the value of the '{@link net.opengis.wfs20.MemberPropertyType#getRole <em>Role</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Role</em>' attribute.
     * @see #getRole()
     * @generated
     */
    void setRole(String value);

    /**
     * Returns the value of the '<em><b>Show</b></em>' attribute.
     * The literals are from the enumeration {@link org.w3.xlink.ShowType}.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * 
     *         The 'show' attribute is used to communicate the desired presentation 
     *         of the ending resource on traversal from the starting resource; it's 
     *         value should be treated as follows: 
     *         new - load ending resource in a new window, frame, pane, or other 
     *               presentation context
     *         replace - load the resource in the same window, frame, pane, or 
     *                   other presentation context
     *         embed - load ending resource in place of the presentation of the 
     *                 starting resource
     *         other - behavior is unconstrained; examine other markup in the 
     *                 link for hints 
     *         none - behavior is unconstrained 
     *       
     * <!-- end-model-doc -->
     * @return the value of the '<em>Show</em>' attribute.
     * @see org.w3.xlink.ShowType
     * @see #isSetShow()
     * @see #unsetShow()
     * @see #setShow(ShowType)
     * @see net.opengis.wfs20.Wfs20Package#getMemberPropertyType_Show()
     * @model unsettable="true"
     *        extendedMetaData="kind='attribute' name='show' namespace='http://www.w3.org/1999/xlink'"
     * @generated
     */
    ShowType getShow();

    /**
     * Sets the value of the '{@link net.opengis.wfs20.MemberPropertyType#getShow <em>Show</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Show</em>' attribute.
     * @see org.w3.xlink.ShowType
     * @see #isSetShow()
     * @see #unsetShow()
     * @see #getShow()
     * @generated
     */
    void setShow(ShowType value);

    /**
     * Unsets the value of the '{@link net.opengis.wfs20.MemberPropertyType#getShow <em>Show</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #isSetShow()
     * @see #getShow()
     * @see #setShow(ShowType)
     * @generated
     */
    void unsetShow();

    /**
     * Returns whether the value of the '{@link net.opengis.wfs20.MemberPropertyType#getShow <em>Show</em>}' attribute is set.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return whether the value of the '<em>Show</em>' attribute is set.
     * @see #unsetShow()
     * @see #getShow()
     * @see #setShow(ShowType)
     * @generated
     */
    boolean isSetShow();

    /**
     * Returns the value of the '<em><b>State</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>State</em>' attribute isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>State</em>' attribute.
     * @see #setState(Object)
     * @see net.opengis.wfs20.Wfs20Package#getMemberPropertyType_State()
     * @model dataType="net.opengis.wfs20.StateValueType"
     *        extendedMetaData="kind='attribute' name='state'"
     * @generated
     */
    Object getState();

    /**
     * Sets the value of the '{@link net.opengis.wfs20.MemberPropertyType#getState <em>State</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>State</em>' attribute.
     * @see #getState()
     * @generated
     */
    void setState(Object value);

    /**
     * Returns the value of the '<em><b>Title</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Title</em>' attribute isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Title</em>' attribute.
     * @see #setTitle(String)
     * @see net.opengis.wfs20.Wfs20Package#getMemberPropertyType_Title()
     * @model dataType="org.eclipse.emf.ecore.xml.type.String"
     *        extendedMetaData="kind='attribute' name='title' namespace='http://www.w3.org/1999/xlink'"
     * @generated
     */
    String getTitle();

    /**
     * Sets the value of the '{@link net.opengis.wfs20.MemberPropertyType#getTitle <em>Title</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Title</em>' attribute.
     * @see #getTitle()
     * @generated
     */
    void setTitle(String value);

    /**
     * Returns the value of the '<em><b>Type</b></em>' attribute.
     * The default value is <code>"simple"</code>.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Type</em>' attribute isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Type</em>' attribute.
     * @see #isSetType()
     * @see #unsetType()
     * @see #setType(String)
     * @see net.opengis.wfs20.Wfs20Package#getMemberPropertyType_Type()
     * @model default="simple" unsettable="true" dataType="org.eclipse.emf.ecore.xml.type.String"
     *        extendedMetaData="kind='attribute' name='type' namespace='http://www.w3.org/1999/xlink'"
     * @generated
     */
    String getType();

    /**
     * Sets the value of the '{@link net.opengis.wfs20.MemberPropertyType#getType <em>Type</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Type</em>' attribute.
     * @see #isSetType()
     * @see #unsetType()
     * @see #getType()
     * @generated
     */
    void setType(String value);

    /**
     * Unsets the value of the '{@link net.opengis.wfs20.MemberPropertyType#getType <em>Type</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #isSetType()
     * @see #getType()
     * @see #setType(String)
     * @generated
     */
    void unsetType();

    /**
     * Returns whether the value of the '{@link net.opengis.wfs20.MemberPropertyType#getType <em>Type</em>}' attribute is set.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return whether the value of the '<em>Type</em>' attribute is set.
     * @see #unsetType()
     * @see #getType()
     * @see #setType(String)
     * @generated
     */
    boolean isSetType();

} // MemberPropertyType
