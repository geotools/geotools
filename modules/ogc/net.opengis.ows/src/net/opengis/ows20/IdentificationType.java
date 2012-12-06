/**
 */
package net.opengis.ows20;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.util.FeatureMap;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Identification Type</b></em>'.
 * <!-- end-user-doc -->
 *
 * <!-- begin-model-doc -->
 * Extended metadata identifying and describing a set of
 *       data. This type shall be extended if needed for each specific OWS to
 *       include additional metadata for each type of dataset. If needed, this
 *       type should first be restricted for each specific OWS to change the
 *       multiplicity (or optionality) of some elements.
 * <!-- end-model-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link net.opengis.ows20.IdentificationType#getBoundingBoxGroup <em>Bounding Box Group</em>}</li>
 *   <li>{@link net.opengis.ows20.IdentificationType#getBoundingBox <em>Bounding Box</em>}</li>
 *   <li>{@link net.opengis.ows20.IdentificationType#getOutputFormat <em>Output Format</em>}</li>
 *   <li>{@link net.opengis.ows20.IdentificationType#getAvailableCRSGroup <em>Available CRS Group</em>}</li>
 *   <li>{@link net.opengis.ows20.IdentificationType#getAvailableCRS <em>Available CRS</em>}</li>
 * </ul>
 * </p>
 *
 * @see net.opengis.ows20.Ows20Package#getIdentificationType()
 * @model extendedMetaData="name='IdentificationType' kind='elementOnly'"
 * @generated
 */
public interface IdentificationType extends BasicIdentificationType {
    /**
     * Returns the value of the '<em><b>Bounding Box Group</b></em>' attribute list.
     * The list contents are of type {@link org.eclipse.emf.ecore.util.FeatureMap.Entry}.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * Unordered list of zero or more bounding boxes
     *               whose union describes the extent of this
     *               dataset.
     * <!-- end-model-doc -->
     * @return the value of the '<em>Bounding Box Group</em>' attribute list.
     * @see net.opengis.ows20.Ows20Package#getIdentificationType_BoundingBoxGroup()
     * @model unique="false" dataType="org.eclipse.emf.ecore.EFeatureMapEntry" many="true"
     *        extendedMetaData="kind='group' name='BoundingBox:group' namespace='##targetNamespace'"
     * @generated
     */
    FeatureMap getBoundingBoxGroup();

    /**
     * Returns the value of the '<em><b>Bounding Box</b></em>' containment reference list.
     * The list contents are of type {@link net.opengis.ows20.BoundingBoxType}.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * Unordered list of zero or more bounding boxes
     *               whose union describes the extent of this
     *               dataset.
     * <!-- end-model-doc -->
     * @return the value of the '<em>Bounding Box</em>' containment reference list.
     * @see net.opengis.ows20.Ows20Package#getIdentificationType_BoundingBox()
     * @model containment="true" transient="true" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='BoundingBox' namespace='##targetNamespace' group='BoundingBox:group'"
     * @generated
     */
    EList<BoundingBoxType> getBoundingBox();

    /**
     * Returns the value of the '<em><b>Output Format</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * Unordered list of zero or more references to data
     *               formats supported for server outputs.
     * <!-- end-model-doc -->
     * @return the value of the '<em>Output Format</em>' attribute.
     * @see #setOutputFormat(String)
     * @see net.opengis.ows20.Ows20Package#getIdentificationType_OutputFormat()
     * @model unique="false" dataType="net.opengis.ows20.MimeType"
     *        extendedMetaData="kind='element' name='OutputFormat' namespace='##targetNamespace'"
     * @generated
     */
    String getOutputFormat();

    /**
     * Sets the value of the '{@link net.opengis.ows20.IdentificationType#getOutputFormat <em>Output Format</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Output Format</em>' attribute.
     * @see #getOutputFormat()
     * @generated
     */
    void setOutputFormat(String value);

    /**
     * Returns the value of the '<em><b>Available CRS Group</b></em>' attribute list.
     * The list contents are of type {@link org.eclipse.emf.ecore.util.FeatureMap.Entry}.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * Unordered list of zero or more available
     *               coordinate reference systems.
     * <!-- end-model-doc -->
     * @return the value of the '<em>Available CRS Group</em>' attribute list.
     * @see net.opengis.ows20.Ows20Package#getIdentificationType_AvailableCRSGroup()
     * @model unique="false" dataType="org.eclipse.emf.ecore.EFeatureMapEntry" many="true"
     *        extendedMetaData="kind='group' name='AvailableCRS:group' namespace='##targetNamespace'"
     * @generated
     */
    FeatureMap getAvailableCRSGroup();

    /**
     * Returns the value of the '<em><b>Available CRS</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * Unordered list of zero or more available
     *               coordinate reference systems.
     * <!-- end-model-doc -->
     * @return the value of the '<em>Available CRS</em>' attribute.
     * @see #setAvailableCRS(String)
     * @see net.opengis.ows20.Ows20Package#getIdentificationType_AvailableCRS()
     * @model unique="false" dataType="org.eclipse.emf.ecore.xml.type.AnyURI" transient="true" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='AvailableCRS' namespace='##targetNamespace' group='AvailableCRS:group'"
     * @generated
     */
    String getAvailableCRS();

    /**
     * Sets the value of the '{@link net.opengis.ows20.IdentificationType#getAvailableCRS <em>Available CRS</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Available CRS</em>' attribute.
     * @see #getAvailableCRS()
     * @generated
     */
    void setAvailableCRS(String value);

} // IdentificationType
