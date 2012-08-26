/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package net.opengis.cat.csw20;

import java.lang.String;

import net.opengis.ows10.BoundingBoxType;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.util.FeatureMap;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Brief Record Type</b></em>'.
 * <!-- end-user-doc -->
 *
 * <!-- begin-model-doc -->
 * 
 *             This type defines a brief representation of the common record
 *             format.  It extends AbstractRecordType to include only the
 *              dc:identifier and dc:type properties.
 * <!-- end-model-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link net.opengis.cat.csw20.BriefRecordType#getIdentifierGroup <em>Identifier Group</em>}</li>
 *   <li>{@link net.opengis.cat.csw20.BriefRecordType#getIdentifier <em>Identifier</em>}</li>
 *   <li>{@link net.opengis.cat.csw20.BriefRecordType#getTitleGroup <em>Title Group</em>}</li>
 *   <li>{@link net.opengis.cat.csw20.BriefRecordType#getTitle <em>Title</em>}</li>
 *   <li>{@link net.opengis.cat.csw20.BriefRecordType#getType <em>Type</em>}</li>
 *   <li>{@link net.opengis.cat.csw20.BriefRecordType#getBoundingBoxGroup <em>Bounding Box Group</em>}</li>
 *   <li>{@link net.opengis.cat.csw20.BriefRecordType#getBoundingBox <em>Bounding Box</em>}</li>
 * </ul>
 * </p>
 *
 * @see net.opengis.cat.csw20.Csw20Package#getBriefRecordType()
 * @model extendedMetaData="name='BriefRecordType' kind='elementOnly'"
 * @generated
 */
public interface BriefRecordType extends AbstractRecordType {
    /**
     * Returns the value of the '<em><b>Identifier Group</b></em>' attribute list.
     * The list contents are of type {@link org.eclipse.emf.ecore.util.FeatureMap.Entry}.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * An unambiguous reference to the resource within a given context.
     *       Recommended best practice is to identify the resource by means of a
     *       string or number conforming to a formal identification system. Formal
     *       identification systems include but are not limited to the Uniform
     *       Resource Identifier (URI) (including the Uniform Resource Locator
     *       (URL)), the Digital Object Identifier (DOI), and the International
     *       Standard Book Number (ISBN).
     * <!-- end-model-doc -->
     * @return the value of the '<em>Identifier Group</em>' attribute list.
     * @see net.opengis.cat.csw20.Csw20Package#getBriefRecordType_IdentifierGroup()
     * @model unique="false" dataType="org.eclipse.emf.ecore.EFeatureMapEntry" required="true" many="true"
     *        extendedMetaData="kind='group' name='identifier:group' namespace='http://purl.org/dc/elements/1.1/'"
     * @generated
     */
    FeatureMap getIdentifierGroup();

    /**
     * Returns the value of the '<em><b>Identifier</b></em>' containment reference list.
     * The list contents are of type {@link java.lang.String}.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * An unambiguous reference to the resource within a given context.
     *       Recommended best practice is to identify the resource by means of a
     *       string or number conforming to a formal identification system. Formal
     *       identification systems include but are not limited to the Uniform
     *       Resource Identifier (URI) (including the Uniform Resource Locator
     *       (URL)), the Digital Object Identifier (DOI), and the International
     *       Standard Book Number (ISBN).
     * <!-- end-model-doc -->
     * @return the value of the '<em>Identifier</em>' containment reference list.
     * @see net.opengis.cat.csw20.Csw20Package#getBriefRecordType_Identifier()
     * @model type="net.opengis.cat.csw20.String" containment="true" required="true" transient="true" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='identifier' namespace='http://purl.org/dc/elements/1.1/' group='http://purl.org/dc/elements/1.1/#identifier:group'"
     * @generated
     */
    EList<String> getIdentifier();

    /**
     * Returns the value of the '<em><b>Title Group</b></em>' attribute list.
     * The list contents are of type {@link org.eclipse.emf.ecore.util.FeatureMap.Entry}.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * A name given to the resource. Typically, Title will be a name by
     *       which the resource is formally known.
     * <!-- end-model-doc -->
     * @return the value of the '<em>Title Group</em>' attribute list.
     * @see net.opengis.cat.csw20.Csw20Package#getBriefRecordType_TitleGroup()
     * @model unique="false" dataType="org.eclipse.emf.ecore.EFeatureMapEntry" required="true" many="true"
     *        extendedMetaData="kind='group' name='title:group' namespace='http://purl.org/dc/elements/1.1/'"
     * @generated
     */
    FeatureMap getTitleGroup();

    /**
     * Returns the value of the '<em><b>Title</b></em>' containment reference list.
     * The list contents are of type {@link java.lang.String}.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * A name given to the resource. Typically, Title will be a name by
     *       which the resource is formally known.
     * <!-- end-model-doc -->
     * @return the value of the '<em>Title</em>' containment reference list.
     * @see net.opengis.cat.csw20.Csw20Package#getBriefRecordType_Title()
     * @model type="net.opengis.cat.csw20.String" containment="true" required="true" transient="true" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='title' namespace='http://purl.org/dc/elements/1.1/' group='http://purl.org/dc/elements/1.1/#title:group'"
     * @generated
     */
    EList<String> getTitle();

    /**
     * Returns the value of the '<em><b>Type</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * The nature or genre of the content of the resource. Type includes
     *       terms describing general categories, functions, genres, or aggregation
     *       levels for content. Recommended best practice is to select a value
     *       from a controlled vocabulary (for example, the DCMI Type Vocabulary).
     *       To describe the physical or digital manifestation of the resource,
     *       use the Format element.
     * <!-- end-model-doc -->
     * @return the value of the '<em>Type</em>' containment reference.
     * @see #setType(String)
     * @see net.opengis.cat.csw20.Csw20Package#getBriefRecordType_Type()
     * @model dataType="org.eclipse.emf.ecore.xml.type.String"
     */
    String getType();

    /**
     * Sets the value of the '{@link net.opengis.cat.csw20.BriefRecordType#getType <em>Type</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Type</em>' attribute.
     * @see #getType()
     * @generated
     */
    void setType(String value);

    /**
     * Returns the value of the '<em><b>Bounding Box Group</b></em>' attribute list.
     * The list contents are of type {@link org.eclipse.emf.ecore.util.FeatureMap.Entry}.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Bounding Box Group</em>' attribute list isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Bounding Box Group</em>' attribute list.
     * @see net.opengis.cat.csw20.Csw20Package#getBriefRecordType_BoundingBoxGroup()
     * @model unique="false" dataType="org.eclipse.emf.ecore.EFeatureMapEntry" many="true"
     *        extendedMetaData="kind='group' name='BoundingBox:group' namespace='http://www.opengis.net/ows'"
     * @generated
     */
    FeatureMap getBoundingBoxGroup();

    /**
     * Returns the value of the '<em><b>Bounding Box</b></em>' containment reference list.
     * The list contents are of type {@link net.opengis.ows10.BoundingBoxType}.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Bounding Box</em>' containment reference list isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Bounding Box</em>' containment reference list.
     * @see net.opengis.cat.csw20.Csw20Package#getBriefRecordType_BoundingBox()
     * @model containment="true" transient="true" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='BoundingBox' namespace='http://www.opengis.net/ows' group='http://www.opengis.net/ows#BoundingBox:group'"
     * @generated
     */
    EList<BoundingBoxType> getBoundingBox();

} // BriefRecordType
