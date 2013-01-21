/**
 */
package net.opengis.cat.csw20;

import net.opengis.ows10.BoundingBoxType;

import org.eclipse.emf.common.util.EList;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Summary Record Type</b></em>'.
 * <!-- end-user-doc -->
 *
 * <!-- begin-model-doc -->
 * 
 *             This type defines a summary representation of the common record
 *             format.  It extends AbstractRecordType to include the core
 *             properties.
 * <!-- end-model-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link net.opengis.cat.csw20.SummaryRecordType#getIdentifier <em>Identifier</em>}</li>
 *   <li>{@link net.opengis.cat.csw20.SummaryRecordType#getTitle <em>Title</em>}</li>
 *   <li>{@link net.opengis.cat.csw20.SummaryRecordType#getType <em>Type</em>}</li>
 *   <li>{@link net.opengis.cat.csw20.SummaryRecordType#getSubject <em>Subject</em>}</li>
 *   <li>{@link net.opengis.cat.csw20.SummaryRecordType#getFormat <em>Format</em>}</li>
 *   <li>{@link net.opengis.cat.csw20.SummaryRecordType#getRelation <em>Relation</em>}</li>
 *   <li>{@link net.opengis.cat.csw20.SummaryRecordType#getModified <em>Modified</em>}</li>
 *   <li>{@link net.opengis.cat.csw20.SummaryRecordType#getAbstract <em>Abstract</em>}</li>
 *   <li>{@link net.opengis.cat.csw20.SummaryRecordType#getSpatial <em>Spatial</em>}</li>
 *   <li>{@link net.opengis.cat.csw20.SummaryRecordType#getBoundingBox <em>Bounding Box</em>}</li>
 * </ul>
 * </p>
 *
 * @see net.opengis.cat.csw20.Csw20Package#getSummaryRecordType()
 * @model extendedMetaData="name='SummaryRecordType' kind='elementOnly'"
 * @generated
 */
public interface SummaryRecordType extends AbstractRecordType {
    /**
     * Returns the value of the '<em><b>Identifier</b></em>' containment reference list.
     * The list contents are of type {@link net.opengis.cat.csw20.SimpleLiteral}.
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
     * @see net.opengis.cat.csw20.Csw20Package#getSummaryRecordType_Identifier()
     * @model containment="true" required="true" transient="true" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='identifier' namespace='http://purl.org/dc/elements/1.1/' group='http://purl.org/dc/elements/1.1/#identifier:group'"
     * @generated
     */
    EList<SimpleLiteral> getIdentifier();

    /**
     * Returns the value of the '<em><b>Title</b></em>' containment reference list.
     * The list contents are of type {@link net.opengis.cat.csw20.SimpleLiteral}.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * A name given to the resource. Typically, Title will be a name by
     *       which the resource is formally known.
     * <!-- end-model-doc -->
     * @return the value of the '<em>Title</em>' containment reference list.
     * @see net.opengis.cat.csw20.Csw20Package#getSummaryRecordType_Title()
     * @model containment="true" required="true" transient="true" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='title' namespace='http://purl.org/dc/elements/1.1/' group='http://purl.org/dc/elements/1.1/#title:group'"
     * @generated
     */
    EList<SimpleLiteral> getTitle();

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
     * @see #setType(SimpleLiteral)
     * @see net.opengis.cat.csw20.Csw20Package#getSummaryRecordType_Type()
     * @model
     */
    SimpleLiteral getType();

    /**
     * Sets the value of the '{@link net.opengis.cat.csw20.SummaryRecordType#getType <em>Type</em>}' reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Type</em>' reference.
     * @see #getType()
     * @generated
     */
    void setType(SimpleLiteral value);

    /**
     * Returns the value of the '<em><b>Subject</b></em>' containment reference list.
     * The list contents are of type {@link net.opengis.cat.csw20.SimpleLiteral}.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * A topic of the content of the resource. Typically, Subject will be
     *       expressed as keywords, key phrases, or classification codes that
     *       describe a topic of the resource. Recommended best practice is to
     *       select a value from a controlled vocabulary or formal classification
     *       scheme.
     * <!-- end-model-doc -->
     * @return the value of the '<em>Subject</em>' containment reference list.
     * @see net.opengis.cat.csw20.Csw20Package#getSummaryRecordType_Subject()
     * @model containment="true"
     *        extendedMetaData="kind='element' name='subject' namespace='http://purl.org/dc/elements/1.1/'"
     * @generated
     */
    EList<SimpleLiteral> getSubject();

    /**
     * Returns the value of the '<em><b>Format</b></em>' containment reference list.
     * The list contents are of type {@link net.opengis.cat.csw20.SimpleLiteral}.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * The physical or digital manifestation of the resource. Typically,
     *       Format will include the media-type or dimensions of the resource.
     *       Format may be used to identify the software, hardware, or other
     *       equipment needed to display or operate the resource. Examples of
     *       dimensions include size and duration. Recommended best practice is to
     *       select a value from a controlled vocabulary (for example, the list
     *       of Internet Media Types defining computer media formats).
     * <!-- end-model-doc -->
     * @return the value of the '<em>Format</em>' containment reference list.
     * @see net.opengis.cat.csw20.Csw20Package#getSummaryRecordType_Format()
     * @model containment="true" transient="true" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='format' namespace='http://purl.org/dc/elements/1.1/' group='http://purl.org/dc/elements/1.1/#format:group'"
     * @generated
     */
    EList<SimpleLiteral> getFormat();

    /**
     * Returns the value of the '<em><b>Relation</b></em>' containment reference list.
     * The list contents are of type {@link net.opengis.cat.csw20.SimpleLiteral}.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * A reference to a related resource. Recommended best practice is to
     *       identify the referenced resource by means of a string or number
     *       conforming to a formal identification system.
     * <!-- end-model-doc -->
     * @return the value of the '<em>Relation</em>' containment reference list.
     * @see net.opengis.cat.csw20.Csw20Package#getSummaryRecordType_Relation()
     * @model containment="true" transient="true" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='relation' namespace='http://purl.org/dc/elements/1.1/' group='http://purl.org/dc/elements/1.1/#relation:group'"
     * @generated
     */
    EList<SimpleLiteral> getRelation();

    /**
     * Returns the value of the '<em><b>Modified</b></em>' containment reference list.
     * The list contents are of type {@link net.opengis.cat.csw20.SimpleLiteral}.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Modified</em>' containment reference list isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Modified</em>' containment reference list.
     * @see net.opengis.cat.csw20.Csw20Package#getSummaryRecordType_Modified()
     * @model containment="true"
     *        extendedMetaData="kind='element' name='modified' namespace='http://purl.org/dc/terms/'"
     * @generated
     */
    EList<SimpleLiteral> getModified();

    /**
     * Returns the value of the '<em><b>Abstract</b></em>' containment reference list.
     * The list contents are of type {@link net.opengis.cat.csw20.SimpleLiteral}.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Abstract</em>' containment reference list isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Abstract</em>' containment reference list.
     * @see net.opengis.cat.csw20.Csw20Package#getSummaryRecordType_Abstract()
     * @model containment="true"
     *        extendedMetaData="kind='element' name='abstract' namespace='http://purl.org/dc/terms/'"
     * @generated
     */
    EList<SimpleLiteral> getAbstract();

    /**
     * Returns the value of the '<em><b>Spatial</b></em>' containment reference list.
     * The list contents are of type {@link net.opengis.cat.csw20.SimpleLiteral}.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Spatial</em>' containment reference list isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Spatial</em>' containment reference list.
     * @see net.opengis.cat.csw20.Csw20Package#getSummaryRecordType_Spatial()
     * @model containment="true"
     *        extendedMetaData="kind='element' name='spatial' namespace='http://purl.org/dc/terms/'"
     * @generated
     */
    EList<SimpleLiteral> getSpatial();


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
     * @see net.opengis.cat.csw20.Csw20Package#getSummaryRecordType_BoundingBox()
     * @model containment="true" transient="true" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='BoundingBox' namespace='http://www.opengis.net/ows' group='http://www.opengis.net/ows#BoundingBox:group'"
     * @generated
     */
    EList<BoundingBoxType> getBoundingBox();

} // SummaryRecordType
