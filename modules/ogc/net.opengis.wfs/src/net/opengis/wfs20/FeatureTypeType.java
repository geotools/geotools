/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package net.opengis.wfs20;

import javax.xml.namespace.QName;

import net.opengis.ows11.KeywordsType;
import net.opengis.ows11.WGS84BoundingBoxType;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Feature Type Type</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link net.opengis.wfs20.FeatureTypeType#getName <em>Name</em>}</li>
 *   <li>{@link net.opengis.wfs20.FeatureTypeType#getTitle <em>Title</em>}</li>
 *   <li>{@link net.opengis.wfs20.FeatureTypeType#getAbstract <em>Abstract</em>}</li>
 *   <li>{@link net.opengis.wfs20.FeatureTypeType#getKeywords <em>Keywords</em>}</li>
 *   <li>{@link net.opengis.wfs20.FeatureTypeType#getDefaultCRS <em>Default CRS</em>}</li>
 *   <li>{@link net.opengis.wfs20.FeatureTypeType#getOtherCRS <em>Other CRS</em>}</li>
 *   <li>{@link net.opengis.wfs20.FeatureTypeType#getNoCRS <em>No CRS</em>}</li>
 *   <li>{@link net.opengis.wfs20.FeatureTypeType#getOutputFormats <em>Output Formats</em>}</li>
 *   <li>{@link net.opengis.wfs20.FeatureTypeType#getWGS84BoundingBox <em>WGS84 Bounding Box</em>}</li>
 *   <li>{@link net.opengis.wfs20.FeatureTypeType#getMetadataURL <em>Metadata URL</em>}</li>
 *   <li>{@link net.opengis.wfs20.FeatureTypeType#getExtendedDescription <em>Extended Description</em>}</li>
 * </ul>
 * </p>
 *
 * @see net.opengis.wfs20.Wfs20Package#getFeatureTypeType()
 * @model extendedMetaData="name='FeatureTypeType' kind='elementOnly'"
 * @generated
 */
public interface FeatureTypeType extends EObject {
    /**
     * Returns the value of the '<em><b>Name</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Name</em>' attribute isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Name</em>' attribute.
     * @see #setName(QName)
     * @see net.opengis.wfs20.Wfs20Package#getFeatureTypeType_Name()
     * @model dataType="org.eclipse.emf.ecore.xml.type.QName" required="true"
     *        extendedMetaData="kind='element' name='Name' namespace='##targetNamespace'"
     * @generated
     */
    QName getName();

    /**
     * Sets the value of the '{@link net.opengis.wfs20.FeatureTypeType#getName <em>Name</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Name</em>' attribute.
     * @see #getName()
     * @generated
     */
    void setName(QName value);

    /**
     * Returns the value of the '<em><b>Title</b></em>' containment reference list.
     * The list contents are of type {@link net.opengis.wfs20.TitleType}.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Title</em>' containment reference list isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Title</em>' containment reference list.
     * @see net.opengis.wfs20.Wfs20Package#getFeatureTypeType_Title()
     * @model containment="true"
     *        extendedMetaData="kind='element' name='Title' namespace='##targetNamespace'"
     * @generated
     */
    EList<TitleType> getTitle();

    /**
     * Returns the value of the '<em><b>Abstract</b></em>' containment reference list.
     * The list contents are of type {@link net.opengis.wfs20.AbstractType}.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Abstract</em>' containment reference list isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Abstract</em>' containment reference list.
     * @see net.opengis.wfs20.Wfs20Package#getFeatureTypeType_Abstract()
     * @model containment="true"
     *        extendedMetaData="kind='element' name='Abstract' namespace='##targetNamespace'"
     * @generated
     */
    EList<AbstractType> getAbstract();

    /**
     * Returns the value of the '<em><b>Keywords</b></em>' containment reference list.
     * The list contents are of type {@link net.opengis.ows11.KeywordsType}.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Keywords</em>' containment reference list isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Keywords</em>' containment reference list.
     * @see net.opengis.wfs20.Wfs20Package#getFeatureTypeType_Keywords()
     * @model containment="true"
     *        extendedMetaData="kind='element' name='Keywords' namespace='http://www.opengis.net/ows/1.1'"
     * @generated
     */
    EList<KeywordsType> getKeywords();

    /**
     * Returns the value of the '<em><b>Default CRS</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Default CRS</em>' attribute isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Default CRS</em>' attribute.
     * @see #setDefaultCRS(String)
     * @see net.opengis.wfs20.Wfs20Package#getFeatureTypeType_DefaultCRS()
     * @model dataType="org.eclipse.emf.ecore.xml.type.AnyURI"
     *        extendedMetaData="kind='element' name='DefaultCRS' namespace='##targetNamespace'"
     * @generated
     */
    String getDefaultCRS();

    /**
     * Sets the value of the '{@link net.opengis.wfs20.FeatureTypeType#getDefaultCRS <em>Default CRS</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Default CRS</em>' attribute.
     * @see #getDefaultCRS()
     * @generated
     */
    void setDefaultCRS(String value);

    /**
     * Returns the value of the '<em><b>Other CRS</b></em>' attribute list.
     * The list contents are of type {@link java.lang.String}.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Other CRS</em>' attribute list isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Other CRS</em>' attribute list.
     * @see net.opengis.wfs20.Wfs20Package#getFeatureTypeType_OtherCRS()
     * @model unique="false" dataType="org.eclipse.emf.ecore.xml.type.AnyURI"
     *        extendedMetaData="kind='element' name='OtherCRS' namespace='##targetNamespace'"
     * @generated
     */
    EList<String> getOtherCRS();

    /**
     * Returns the value of the '<em><b>No CRS</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>No CRS</em>' containment reference isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>No CRS</em>' containment reference.
     * @see #setNoCRS(NoCRSType)
     * @see net.opengis.wfs20.Wfs20Package#getFeatureTypeType_NoCRS()
     * @model containment="true"
     *        extendedMetaData="kind='element' name='NoCRS' namespace='##targetNamespace'"
     * @generated
     */
    NoCRSType getNoCRS();

    /**
     * Sets the value of the '{@link net.opengis.wfs20.FeatureTypeType#getNoCRS <em>No CRS</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>No CRS</em>' containment reference.
     * @see #getNoCRS()
     * @generated
     */
    void setNoCRS(NoCRSType value);

    /**
     * Returns the value of the '<em><b>Output Formats</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Output Formats</em>' containment reference isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Output Formats</em>' containment reference.
     * @see #setOutputFormats(OutputFormatListType)
     * @see net.opengis.wfs20.Wfs20Package#getFeatureTypeType_OutputFormats()
     * @model containment="true"
     *        extendedMetaData="kind='element' name='OutputFormats' namespace='##targetNamespace'"
     * @generated
     */
    OutputFormatListType getOutputFormats();

    /**
     * Sets the value of the '{@link net.opengis.wfs20.FeatureTypeType#getOutputFormats <em>Output Formats</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Output Formats</em>' containment reference.
     * @see #getOutputFormats()
     * @generated
     */
    void setOutputFormats(OutputFormatListType value);

    /**
     * Returns the value of the '<em><b>WGS84 Bounding Box</b></em>' containment reference list.
     * The list contents are of type {@link net.opengis.ows11.WGS84BoundingBoxType}.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>WGS84 Bounding Box</em>' containment reference list isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>WGS84 Bounding Box</em>' containment reference list.
     * @see net.opengis.wfs20.Wfs20Package#getFeatureTypeType_WGS84BoundingBox()
     * @model containment="true"
     *        extendedMetaData="kind='element' name='WGS84BoundingBox' namespace='http://www.opengis.net/ows/1.1'"
     * @generated
     */
    EList<WGS84BoundingBoxType> getWGS84BoundingBox();

    /**
     * Returns the value of the '<em><b>Metadata URL</b></em>' containment reference list.
     * The list contents are of type {@link net.opengis.wfs20.MetadataURLType}.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Metadata URL</em>' containment reference list isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Metadata URL</em>' containment reference list.
     * @see net.opengis.wfs20.Wfs20Package#getFeatureTypeType_MetadataURL()
     * @model containment="true"
     *        extendedMetaData="kind='element' name='MetadataURL' namespace='##targetNamespace'"
     * @generated
     */
    EList<MetadataURLType> getMetadataURL();

    /**
     * Returns the value of the '<em><b>Extended Description</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Extended Description</em>' containment reference isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Extended Description</em>' containment reference.
     * @see #setExtendedDescription(ExtendedDescriptionType)
     * @see net.opengis.wfs20.Wfs20Package#getFeatureTypeType_ExtendedDescription()
     * @model containment="true"
     *        extendedMetaData="kind='element' name='ExtendedDescription' namespace='##targetNamespace'"
     * @generated
     */
    ExtendedDescriptionType getExtendedDescription();

    /**
     * Sets the value of the '{@link net.opengis.wfs20.FeatureTypeType#getExtendedDescription <em>Extended Description</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Extended Description</em>' containment reference.
     * @see #getExtendedDescription()
     * @generated
     */
    void setExtendedDescription(ExtendedDescriptionType value);

} // FeatureTypeType
