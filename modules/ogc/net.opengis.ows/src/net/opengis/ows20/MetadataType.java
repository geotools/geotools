/**
 */
package net.opengis.ows20;

import org.eclipse.emf.ecore.EObject;

import org.eclipse.emf.ecore.util.FeatureMap;

import org.w3.xlink.ActuateType;
import org.w3.xlink.ShowType;
import org.w3.xlink.TypeType;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Metadata Type</b></em>'.
 * <!-- end-user-doc -->
 *
 * <!-- begin-model-doc -->
 * This element either references or contains more metadata
 *       about the element that includes this element. To reference metadata
 *       stored remotely, at least the xlinks:href attribute in xlink:simpleAttrs
 *       shall be included. Either at least one of the attributes in
 *       xlink:simpleAttrs or a substitute for the AbstractMetaData element shall
 *       be included, but not both. An Implementation Specification can restrict
 *       the contents of this element to always be a reference or always contain
 *       metadata. (Informative: This element was adapted from the
 *       metaDataProperty element in GML 3.0.)
 * <!-- end-model-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link net.opengis.ows20.MetadataType#getAbstractMetaDataGroup <em>Abstract Meta Data Group</em>}</li>
 *   <li>{@link net.opengis.ows20.MetadataType#getAbstractMetaData <em>Abstract Meta Data</em>}</li>
 *   <li>{@link net.opengis.ows20.MetadataType#getAbout <em>About</em>}</li>
 *   <li>{@link net.opengis.ows20.MetadataType#getActuate <em>Actuate</em>}</li>
 *   <li>{@link net.opengis.ows20.MetadataType#getArcrole <em>Arcrole</em>}</li>
 *   <li>{@link net.opengis.ows20.MetadataType#getHref <em>Href</em>}</li>
 *   <li>{@link net.opengis.ows20.MetadataType#getRole <em>Role</em>}</li>
 *   <li>{@link net.opengis.ows20.MetadataType#getShow <em>Show</em>}</li>
 *   <li>{@link net.opengis.ows20.MetadataType#getTitle <em>Title</em>}</li>
 *   <li>{@link net.opengis.ows20.MetadataType#getType <em>Type</em>}</li>
 * </ul>
 * </p>
 *
 * @see net.opengis.ows20.Ows20Package#getMetadataType()
 * @model extendedMetaData="name='MetadataType' kind='elementOnly'"
 * @generated
 */
public interface MetadataType extends EObject {
    /**
     * Returns the value of the '<em><b>Abstract Meta Data Group</b></em>' attribute list.
     * The list contents are of type {@link org.eclipse.emf.ecore.util.FeatureMap.Entry}.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * Abstract element containing more metadata about the
     *       element that includes the containing "metadata" element. A specific
     *       server implementation, or an Implementation Specification, can define
     *       concrete elements in the AbstractMetaData substitution
     *       group.
     * <!-- end-model-doc -->
     * @return the value of the '<em>Abstract Meta Data Group</em>' attribute list.
     * @see net.opengis.ows20.Ows20Package#getMetadataType_AbstractMetaDataGroup()
     * @model dataType="org.eclipse.emf.ecore.EFeatureMapEntry" many="false"
     *        extendedMetaData="kind='group' name='AbstractMetaData:group' namespace='##targetNamespace'"
     * @generated
     */
    FeatureMap getAbstractMetaDataGroup();

    /**
     * Returns the value of the '<em><b>Abstract Meta Data</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * Abstract element containing more metadata about the
     *       element that includes the containing "metadata" element. A specific
     *       server implementation, or an Implementation Specification, can define
     *       concrete elements in the AbstractMetaData substitution
     *       group.
     * <!-- end-model-doc -->
     * @return the value of the '<em>Abstract Meta Data</em>' containment reference.
     * @see net.opengis.ows20.Ows20Package#getMetadataType_AbstractMetaData()
     * @model containment="true" transient="true" changeable="false" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='AbstractMetaData' namespace='##targetNamespace' group='AbstractMetaData:group'"
     * @generated
     */
    EObject getAbstractMetaData();

    /**
     * Returns the value of the '<em><b>About</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * Optional reference to the aspect of the element which
     *         includes this "metadata" element that this metadata provides more
     *         information about.
     * <!-- end-model-doc -->
     * @return the value of the '<em>About</em>' attribute.
     * @see #setAbout(String)
     * @see net.opengis.ows20.Ows20Package#getMetadataType_About()
     * @model dataType="org.eclipse.emf.ecore.xml.type.AnyURI"
     *        extendedMetaData="kind='attribute' name='about'"
     * @generated
     */
    String getAbout();

    /**
     * Sets the value of the '{@link net.opengis.ows20.MetadataType#getAbout <em>About</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>About</em>' attribute.
     * @see #getAbout()
     * @generated
     */
    void setAbout(String value);

    /**
     * Returns the value of the '<em><b>Actuate</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Actuate</em>' attribute isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Actuate</em>' attribute.
     * @see #isSetActuate()
     * @see #unsetActuate()
     * @see #setActuate(ActuateType)
     * @see net.opengis.ows20.Ows20Package#getMetadataType_Actuate()
     * @model unsettable="true" dataType="net.opengis.ows20.ActuateType"
     *        extendedMetaData="kind='attribute' name='actuate' namespace='http://www.w3.org/1999/xlink'"
     * @generated
     */
    ActuateType getActuate();

    /**
     * Sets the value of the '{@link net.opengis.ows20.MetadataType#getActuate <em>Actuate</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Actuate</em>' attribute.
     * @see #isSetActuate()
     * @see #unsetActuate()
     * @see #getActuate()
     * @generated
     */
    void setActuate(ActuateType value);

    /**
     * Unsets the value of the '{@link net.opengis.ows20.MetadataType#getActuate <em>Actuate</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #isSetActuate()
     * @see #getActuate()
     * @see #setActuate(ActuateType)
     * @generated
     */
    void unsetActuate();

    /**
     * Returns whether the value of the '{@link net.opengis.ows20.MetadataType#getActuate <em>Actuate</em>}' attribute is set.
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
     * @see net.opengis.ows20.Ows20Package#getMetadataType_Arcrole()
     * @model dataType="net.opengis.ows20.ArcroleType_1"
     *        extendedMetaData="kind='attribute' name='arcrole' namespace='http://www.w3.org/1999/xlink'"
     * @generated
     */
    String getArcrole();

    /**
     * Sets the value of the '{@link net.opengis.ows20.MetadataType#getArcrole <em>Arcrole</em>}' attribute.
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
     * @see net.opengis.ows20.Ows20Package#getMetadataType_Href()
     * @model dataType="net.opengis.ows20.HrefType_1"
     *        extendedMetaData="kind='attribute' name='href' namespace='http://www.w3.org/1999/xlink'"
     * @generated
     */
    String getHref();

    /**
     * Sets the value of the '{@link net.opengis.ows20.MetadataType#getHref <em>Href</em>}' attribute.
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
     * @see net.opengis.ows20.Ows20Package#getMetadataType_Role()
     * @model dataType="net.opengis.ows20.RoleType_1"
     *        extendedMetaData="kind='attribute' name='role' namespace='http://www.w3.org/1999/xlink'"
     * @generated
     */
    String getRole();

    /**
     * Sets the value of the '{@link net.opengis.ows20.MetadataType#getRole <em>Role</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Role</em>' attribute.
     * @see #getRole()
     * @generated
     */
    void setRole(String value);

    /**
     * Returns the value of the '<em><b>Show</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Show</em>' attribute isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Show</em>' attribute.
     * @see #isSetShow()
     * @see #unsetShow()
     * @see #setShow(ShowType)
     * @see net.opengis.ows20.Ows20Package#getMetadataType_Show()
     * @model unsettable="true" dataType="net.opengis.ows20.ShowType"
     *        extendedMetaData="kind='attribute' name='show' namespace='http://www.w3.org/1999/xlink'"
     * @generated
     */
    ShowType getShow();

    /**
     * Sets the value of the '{@link net.opengis.ows20.MetadataType#getShow <em>Show</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Show</em>' attribute.
     * @see #isSetShow()
     * @see #unsetShow()
     * @see #getShow()
     * @generated
     */
    void setShow(ShowType value);

    /**
     * Unsets the value of the '{@link net.opengis.ows20.MetadataType#getShow <em>Show</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #isSetShow()
     * @see #getShow()
     * @see #setShow(ShowType)
     * @generated
     */
    void unsetShow();

    /**
     * Returns whether the value of the '{@link net.opengis.ows20.MetadataType#getShow <em>Show</em>}' attribute is set.
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
     * Returns the value of the '<em><b>Title</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Title</em>' attribute isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Title</em>' attribute.
     * @see #setTitle(String)
     * @see net.opengis.ows20.Ows20Package#getMetadataType_Title()
     * @model dataType="net.opengis.ows20.TitleAttrType_1"
     *        extendedMetaData="kind='attribute' name='title' namespace='http://www.w3.org/1999/xlink'"
     * @generated
     */
    String getTitle();

    /**
     * Sets the value of the '{@link net.opengis.ows20.MetadataType#getTitle <em>Title</em>}' attribute.
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
     * @see #setType(TypeType)
     * @see net.opengis.ows20.Ows20Package#getMetadataType_Type()
     * @model default="simple" unsettable="true" dataType="net.opengis.ows20.TypeType"
     *        extendedMetaData="kind='attribute' name='type' namespace='http://www.w3.org/1999/xlink'"
     * @generated
     */
    TypeType getType();

    /**
     * Sets the value of the '{@link net.opengis.ows20.MetadataType#getType <em>Type</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Type</em>' attribute.
     * @see #isSetType()
     * @see #unsetType()
     * @see #getType()
     * @generated
     */
    void setType(TypeType value);

    /**
     * Unsets the value of the '{@link net.opengis.ows20.MetadataType#getType <em>Type</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #isSetType()
     * @see #getType()
     * @see #setType(TypeType)
     * @generated
     */
    void unsetType();

    /**
     * Returns whether the value of the '{@link net.opengis.ows20.MetadataType#getType <em>Type</em>}' attribute is set.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return whether the value of the '<em>Type</em>' attribute is set.
     * @see #unsetType()
     * @see #getType()
     * @see #setType(TypeType)
     * @generated
     */
    boolean isSetType();

} // MetadataType
