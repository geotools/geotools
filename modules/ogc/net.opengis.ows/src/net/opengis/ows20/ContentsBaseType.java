/**
 */
package net.opengis.ows20;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Contents Base Type</b></em>'.
 * <!-- end-user-doc -->
 *
 * <!-- begin-model-doc -->
 * Contents of typical Contents section of an OWS service
 *       metadata (Capabilities) document. This type shall be extended and/or
 *       restricted if needed for specific OWS use to include the specific
 *       metadata needed.
 * <!-- end-model-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link net.opengis.ows20.ContentsBaseType#getDatasetDescriptionSummary <em>Dataset Description Summary</em>}</li>
 *   <li>{@link net.opengis.ows20.ContentsBaseType#getOtherSource <em>Other Source</em>}</li>
 * </ul>
 * </p>
 *
 * @see net.opengis.ows20.Ows20Package#getContentsBaseType()
 * @model extendedMetaData="name='ContentsBaseType' kind='elementOnly'"
 * @generated
 */
public interface ContentsBaseType extends EObject {
    /**
     * Returns the value of the '<em><b>Dataset Description Summary</b></em>' containment reference list.
     * The list contents are of type {@link net.opengis.ows20.DatasetDescriptionSummaryBaseType}.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * Unordered set of summary descriptions for the
     *           datasets available from this OWS server. This set shall be included
     *           unless another source is referenced and all this metadata is
     *           available from that source.
     * <!-- end-model-doc -->
     * @return the value of the '<em>Dataset Description Summary</em>' containment reference list.
     * @see net.opengis.ows20.Ows20Package#getContentsBaseType_DatasetDescriptionSummary()
     * @model containment="true"
     *        extendedMetaData="kind='element' name='DatasetDescriptionSummary' namespace='##targetNamespace'"
     * @generated
     */
    EList<DatasetDescriptionSummaryBaseType> getDatasetDescriptionSummary();

    /**
     * Returns the value of the '<em><b>Other Source</b></em>' containment reference list.
     * The list contents are of type {@link net.opengis.ows20.MetadataType}.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * Unordered set of references to other sources of
     *           metadata describing the coverage offerings available from this
     *           server.
     * <!-- end-model-doc -->
     * @return the value of the '<em>Other Source</em>' containment reference list.
     * @see net.opengis.ows20.Ows20Package#getContentsBaseType_OtherSource()
     * @model containment="true"
     *        extendedMetaData="kind='element' name='OtherSource' namespace='##targetNamespace'"
     * @generated
     */
    EList<MetadataType> getOtherSource();

} // ContentsBaseType
