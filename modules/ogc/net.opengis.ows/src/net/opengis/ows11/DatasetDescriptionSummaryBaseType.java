/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package net.opengis.ows11;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.util.FeatureMap;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Dataset Description Summary Base Type</b></em>'.
 * <!-- end-user-doc -->
 *
 * <!-- begin-model-doc -->
 * Typical dataset metadata in typical Contents section of an OWS service metadata (Capabilities) document. This type shall be extended and/or restricted if needed for specific OWS use, to include the specific Dataset  description metadata needed. 
 * <!-- end-model-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link net.opengis.ows11.DatasetDescriptionSummaryBaseType#getWGS84BoundingBox <em>WGS84 Bounding Box</em>}</li>
 *   <li>{@link net.opengis.ows11.DatasetDescriptionSummaryBaseType#getIdentifier <em>Identifier</em>}</li>
 *   <li>{@link net.opengis.ows11.DatasetDescriptionSummaryBaseType#getBoundingBoxGroup <em>Bounding Box Group</em>}</li>
 *   <li>{@link net.opengis.ows11.DatasetDescriptionSummaryBaseType#getBoundingBox <em>Bounding Box</em>}</li>
 *   <li>{@link net.opengis.ows11.DatasetDescriptionSummaryBaseType#getMetadata <em>Metadata</em>}</li>
 *   <li>{@link net.opengis.ows11.DatasetDescriptionSummaryBaseType#getDatasetDescriptionSummary <em>Dataset Description Summary</em>}</li>
 * </ul>
 * </p>
 *
 * @see net.opengis.ows11.Ows11Package#getDatasetDescriptionSummaryBaseType()
 * @model extendedMetaData="name='DatasetDescriptionSummaryBaseType' kind='elementOnly'"
 * @generated
 */
public interface DatasetDescriptionSummaryBaseType extends DescriptionType {
    /**
     * Returns the value of the '<em><b>WGS84 Bounding Box</b></em>' containment reference list.
     * The list contents are of type {@link net.opengis.ows11.WGS84BoundingBoxType}.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * Unordered list of zero or more minimum bounding rectangles surrounding coverage data, using the WGS 84 CRS with decimal degrees and longitude before latitude. If no WGS 84 bounding box is recorded for a coverage, any such bounding boxes recorded for a higher level in a hierarchy of datasets shall apply to this coverage. If WGS 84 bounding box(es) are recorded for a coverage, any such bounding boxes recorded for a higher level in a hierarchy of datasets shall be ignored. For each lowest-level coverage in a hierarchy, at least one applicable WGS84BoundingBox shall be either recorded or inherited, to simplify searching for datasets that might overlap a specified region. If multiple WGS 84 bounding boxes are included, this shall be interpreted as the union of the areas of these bounding boxes. 
     * <!-- end-model-doc -->
     * @return the value of the '<em>WGS84 Bounding Box</em>' containment reference list.
     * @see net.opengis.ows11.Ows11Package#getDatasetDescriptionSummaryBaseType_WGS84BoundingBox()
     * @model type="net.opengis.ows11.WGS84BoundingBoxType" containment="true"
     *        extendedMetaData="kind='element' name='WGS84BoundingBox' namespace='##targetNamespace'"
     * @generated
     */
    EList getWGS84BoundingBox();

    /**
     * Returns the value of the '<em><b>Identifier</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * Unambiguous identifier or name of this coverage, unique for this server. 
     * <!-- end-model-doc -->
     * @return the value of the '<em>Identifier</em>' containment reference.
     * @see #setIdentifier(CodeType)
     * @see net.opengis.ows11.Ows11Package#getDatasetDescriptionSummaryBaseType_Identifier()
     * @model containment="true" required="true"
     *        extendedMetaData="kind='element' name='Identifier' namespace='##targetNamespace'"
     * @generated
     */
    CodeType getIdentifier();

    /**
     * Sets the value of the '{@link net.opengis.ows11.DatasetDescriptionSummaryBaseType#getIdentifier <em>Identifier</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Identifier</em>' containment reference.
     * @see #getIdentifier()
     * @generated
     */
    void setIdentifier(CodeType value);

    /**
     * Returns the value of the '<em><b>Bounding Box Group</b></em>' attribute list.
     * The list contents are of type {@link org.eclipse.emf.ecore.util.FeatureMap.Entry}.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * Unordered list of zero or more minimum bounding rectangles surrounding coverage data, in AvailableCRSs.  Zero or more BoundingBoxes are  allowed in addition to one or more WGS84BoundingBoxes to allow more precise specification of the Dataset area in AvailableCRSs. These Bounding Boxes shall not use any CRS not listed as an AvailableCRS. However, an AvailableCRS can be listed without a corresponding Bounding Box. If no such bounding box is recorded for a coverage, any such bounding boxes recorded for a higher level in a hierarchy of datasets shall apply to this coverage. If such bounding box(es) are recorded for a coverage, any such bounding boxes recorded for a higher level in a hierarchy of datasets shall be ignored. If multiple bounding boxes are included with the same CRS, this shall be interpreted as the union of the areas of these bounding boxes. 
     * <!-- end-model-doc -->
     * @return the value of the '<em>Bounding Box Group</em>' attribute list.
     * @see net.opengis.ows11.Ows11Package#getDatasetDescriptionSummaryBaseType_BoundingBoxGroup()
     * @model unique="false" dataType="org.eclipse.emf.ecore.EFeatureMapEntry" many="true"
     *        extendedMetaData="kind='group' name='BoundingBox:group' namespace='##targetNamespace'"
     * @generated
     */
    FeatureMap getBoundingBoxGroup();

    /**
     * Returns the value of the '<em><b>Bounding Box</b></em>' containment reference list.
     * The list contents are of type {@link net.opengis.ows11.BoundingBoxType}.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * Unordered list of zero or more minimum bounding rectangles surrounding coverage data, in AvailableCRSs.  Zero or more BoundingBoxes are  allowed in addition to one or more WGS84BoundingBoxes to allow more precise specification of the Dataset area in AvailableCRSs. These Bounding Boxes shall not use any CRS not listed as an AvailableCRS. However, an AvailableCRS can be listed without a corresponding Bounding Box. If no such bounding box is recorded for a coverage, any such bounding boxes recorded for a higher level in a hierarchy of datasets shall apply to this coverage. If such bounding box(es) are recorded for a coverage, any such bounding boxes recorded for a higher level in a hierarchy of datasets shall be ignored. If multiple bounding boxes are included with the same CRS, this shall be interpreted as the union of the areas of these bounding boxes. 
     * <!-- end-model-doc -->
     * @return the value of the '<em>Bounding Box</em>' containment reference list.
     * @see net.opengis.ows11.Ows11Package#getDatasetDescriptionSummaryBaseType_BoundingBox()
     * @model type="net.opengis.ows11.BoundingBoxType" containment="true" transient="true" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='BoundingBox' namespace='##targetNamespace' group='BoundingBox:group'"
     * @generated
     */
    EList getBoundingBox();

    /**
     * Returns the value of the '<em><b>Metadata</b></em>' containment reference list.
     * The list contents are of type {@link net.opengis.ows11.MetadataType}.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * Optional unordered list of additional metadata about this dataset. A list of optional metadata elements for this dataset description could be specified in the Implementation Specification for this service. 
     * <!-- end-model-doc -->
     * @return the value of the '<em>Metadata</em>' containment reference list.
     * @see net.opengis.ows11.Ows11Package#getDatasetDescriptionSummaryBaseType_Metadata()
     * @model type="net.opengis.ows11.MetadataType" containment="true"
     *        extendedMetaData="kind='element' name='Metadata' namespace='##targetNamespace'"
     * @generated
     */
    EList getMetadata();

    /**
     * Returns the value of the '<em><b>Dataset Description Summary</b></em>' containment reference list.
     * The list contents are of type {@link net.opengis.ows11.DatasetDescriptionSummaryBaseType}.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * Metadata describing zero or more unordered subsidiary datasets available from this server. 
     * <!-- end-model-doc -->
     * @return the value of the '<em>Dataset Description Summary</em>' containment reference list.
     * @see net.opengis.ows11.Ows11Package#getDatasetDescriptionSummaryBaseType_DatasetDescriptionSummary()
     * @model type="net.opengis.ows11.DatasetDescriptionSummaryBaseType" containment="true"
     *        extendedMetaData="kind='element' name='DatasetDescriptionSummary' namespace='##targetNamespace'"
     * @generated
     */
    EList getDatasetDescriptionSummary();

} // DatasetDescriptionSummaryBaseType
