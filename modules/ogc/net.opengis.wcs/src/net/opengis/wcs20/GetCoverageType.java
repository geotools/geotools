/**
 */
package net.opengis.wcs20;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.util.FeatureMap;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Get Coverage Type</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link net.opengis.wcs20.GetCoverageType#getCoverageId <em>Coverage Id</em>}</li>
 *   <li>{@link net.opengis.wcs20.GetCoverageType#getDimensionSubsetGroup <em>Dimension Subset Group</em>}</li>
 *   <li>{@link net.opengis.wcs20.GetCoverageType#getDimensionSubset <em>Dimension Subset</em>}</li>
 *   <li>{@link net.opengis.wcs20.GetCoverageType#getFormat <em>Format</em>}</li>
 *   <li>{@link net.opengis.wcs20.GetCoverageType#getMediaType <em>Media Type</em>}</li>
 * </ul>
 * </p>
 *
 * @see net.opengis.wcs20.Wcs20Package#getGetCoverageType()
 * @model extendedMetaData="name='GetCoverageType' kind='elementOnly'"
 * @generated
 */
public interface GetCoverageType extends RequestBaseType {
    /**
     * Returns the value of the '<em><b>Coverage Id</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * Identifier of the coverage that this GetCoverage operation request shall draw from.
     * <!-- end-model-doc -->
     * @return the value of the '<em>Coverage Id</em>' attribute.
     * @see #setCoverageId(String)
     * @see net.opengis.wcs20.Wcs20Package#getGetCoverageType_CoverageId()
     * @model dataType="org.eclipse.emf.ecore.xml.type.NCName" required="true"
     *        extendedMetaData="kind='element' name='CoverageId' namespace='##targetNamespace'"
     * @generated
     */
    String getCoverageId();

    /**
     * Sets the value of the '{@link net.opengis.wcs20.GetCoverageType#getCoverageId <em>Coverage Id</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Coverage Id</em>' attribute.
     * @see #getCoverageId()
     * @generated
     */
    void setCoverageId(String value);

    /**
     * Returns the value of the '<em><b>Dimension Subset Group</b></em>' attribute list.
     * The list contents are of type {@link org.eclipse.emf.ecore.util.FeatureMap.Entry}.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * Definition of the desired subset of the domain of the coverage. This is either a Trim operation, or a Slice operation.
     * <!-- end-model-doc -->
     * @return the value of the '<em>Dimension Subset Group</em>' attribute list.
     * @see net.opengis.wcs20.Wcs20Package#getGetCoverageType_DimensionSubsetGroup()
     * @model unique="false" dataType="org.eclipse.emf.ecore.EFeatureMapEntry" many="true"
     *        extendedMetaData="kind='group' name='DimensionSubset:group' namespace='##targetNamespace'"
     * @generated
     */
    FeatureMap getDimensionSubsetGroup();

    /**
     * Returns the value of the '<em><b>Dimension Subset</b></em>' containment reference list.
     * The list contents are of type {@link net.opengis.wcs20.DimensionSubsetType}.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * Definition of the desired subset of the domain of the coverage. This is either a Trim operation, or a Slice operation.
     * <!-- end-model-doc -->
     * @return the value of the '<em>Dimension Subset</em>' containment reference list.
     * @see net.opengis.wcs20.Wcs20Package#getGetCoverageType_DimensionSubset()
     * @model containment="true" transient="true" changeable="false" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='DimensionSubset' namespace='##targetNamespace' group='DimensionSubset:group'"
     * @generated
     */
    EList<DimensionSubsetType> getDimensionSubset();

    /**
     * Returns the value of the '<em><b>Format</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * MimeType of the format the resulting coverage shall be encoded in. Dafault is the coverage's native format. Type is anyURI because of the type of the element "mimeType" in "gml:FileType".
     * <!-- end-model-doc -->
     * @return the value of the '<em>Format</em>' attribute.
     * @see #setFormat(String)
     * @see net.opengis.wcs20.Wcs20Package#getGetCoverageType_Format()
     * @model dataType="org.eclipse.emf.ecore.xml.type.AnyURI"
     *        extendedMetaData="kind='element' name='format' namespace='##targetNamespace'"
     * @generated
     */
    String getFormat();

    /**
     * Sets the value of the '{@link net.opengis.wcs20.GetCoverageType#getFormat <em>Format</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Format</em>' attribute.
     * @see #getFormat()
     * @generated
     */
    void setFormat(String value);

    /**
     * Returns the value of the '<em><b>Media Type</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * Optional element indicating the MimeType of the response of a GetCoverage request. Only currently allowed valued is "multipart/related".
     * <!-- end-model-doc -->
     * @return the value of the '<em>Media Type</em>' attribute.
     * @see #setMediaType(String)
     * @see net.opengis.wcs20.Wcs20Package#getGetCoverageType_MediaType()
     * @model dataType="org.eclipse.emf.ecore.xml.type.AnyURI"
     *        extendedMetaData="kind='element' name='mediaType' namespace='##targetNamespace'"
     * @generated
     */
    String getMediaType();

    /**
     * Sets the value of the '{@link net.opengis.wcs20.GetCoverageType#getMediaType <em>Media Type</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Media Type</em>' attribute.
     * @see #getMediaType()
     * @generated
     */
    void setMediaType(String value);

} // GetCoverageType
