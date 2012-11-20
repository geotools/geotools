/**
 */
package net.opengis.wcs20;

import javax.xml.namespace.QName;

import net.opengis.ows20.BoundingBoxType;
import net.opengis.ows20.DescriptionType;
import net.opengis.ows20.MetadataType;
import net.opengis.ows20.WGS84BoundingBoxType;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.util.FeatureMap;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Coverage Summary Type</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link net.opengis.wcs20.CoverageSummaryType#getWGS84BoundingBox <em>WGS84 Bounding Box</em>}</li>
 *   <li>{@link net.opengis.wcs20.CoverageSummaryType#getCoverageId <em>Coverage Id</em>}</li>
 *   <li>{@link net.opengis.wcs20.CoverageSummaryType#getCoverageSubtype <em>Coverage Subtype</em>}</li>
 *   <li>{@link net.opengis.wcs20.CoverageSummaryType#getCoverageSubtypeParent <em>Coverage Subtype Parent</em>}</li>
 *   <li>{@link net.opengis.wcs20.CoverageSummaryType#getBoundingBoxGroup <em>Bounding Box Group</em>}</li>
 *   <li>{@link net.opengis.wcs20.CoverageSummaryType#getBoundingBox <em>Bounding Box</em>}</li>
 *   <li>{@link net.opengis.wcs20.CoverageSummaryType#getMetadataGroup <em>Metadata Group</em>}</li>
 *   <li>{@link net.opengis.wcs20.CoverageSummaryType#getMetadata <em>Metadata</em>}</li>
 * </ul>
 * </p>
 *
 * @see net.opengis.wcs20.Wcs20Package#getCoverageSummaryType()
 * @model extendedMetaData="name='CoverageSummaryType' kind='elementOnly'"
 * @generated
 */
public interface CoverageSummaryType extends DescriptionType {
    /**
     * Returns the value of the '<em><b>WGS84 Bounding Box</b></em>' containment reference list.
     * The list contents are of type {@link net.opengis.ows20.WGS84BoundingBoxType}.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>WGS84 Bounding Box</em>' containment reference list isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>WGS84 Bounding Box</em>' containment reference list.
     * @see net.opengis.wcs20.Wcs20Package#getCoverageSummaryType_WGS84BoundingBox()
     * @model containment="true"
     *        extendedMetaData="kind='element' name='WGS84BoundingBox' namespace='http://www.opengis.net/ows/2.0'"
     * @generated
     */
    EList<WGS84BoundingBoxType> getWGS84BoundingBox();

    /**
     * Returns the value of the '<em><b>Coverage Id</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * This element represents coverage identifiers. It uses the same type as gml:id to allow for identifier values to be used in both contexts.
     * <!-- end-model-doc -->
     * @return the value of the '<em>Coverage Id</em>' attribute.
     * @see #setCoverageId(String)
     * @see net.opengis.wcs20.Wcs20Package#getCoverageSummaryType_CoverageId()
     * @model dataType="org.eclipse.emf.ecore.xml.type.NCName" required="true"
     *        extendedMetaData="kind='element' name='CoverageId' namespace='##targetNamespace'"
     * @generated
     */
    String getCoverageId();

    /**
     * Sets the value of the '{@link net.opengis.wcs20.CoverageSummaryType#getCoverageId <em>Coverage Id</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Coverage Id</em>' attribute.
     * @see #getCoverageId()
     * @generated
     */
    void setCoverageId(String value);

    /**
     * Returns the value of the '<em><b>Coverage Subtype</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * CoverageSubtype characterizes the type of a coverage. This element shall contain the name of the XML root element that would be delivered if a GML encoded result were requested from the GetCoverage operation. The content model of the named element shall be described by a schema that is either normatively referenced by the WCS core specification or by a requirement in a WCS extension, the associated conformance class for which has been included in the ows:Profiles of the server's GetCapabilities response.
     * This CoverageSubtype is delivered in GetCapabilities and DescribeCoverage to allow clients an estimation of the amount of data to be expected in the domain and range set. For example, a GridCoverage has a small domain set structure, but typically a large range set; a MultiSolidCoverage, on the other hand, tends to have large domain sets and small range sets.
     * <!-- end-model-doc -->
     * @return the value of the '<em>Coverage Subtype</em>' attribute.
     * @see #setCoverageSubtype(QName)
     * @see net.opengis.wcs20.Wcs20Package#getCoverageSummaryType_CoverageSubtype()
     * @model required="true"
     *        extendedMetaData="kind='element' name='CoverageSubtype' namespace='##targetNamespace'"
     */
    QName getCoverageSubtype();

    /**
     * Sets the value of the '{@link net.opengis.wcs20.CoverageSummaryType#getCoverageSubtype <em>Coverage Subtype</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Coverage Subtype</em>' attribute.
     * @see #getCoverageSubtype()
     * @generated
     */
    void setCoverageSubtype(QName value);

    /**
     * Returns the value of the '<em><b>Coverage Subtype Parent</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * 
     * <!-- end-model-doc -->
     * @return the value of the '<em>Coverage Subtype Parent</em>' containment reference.
     * @see #setCoverageSubtypeParent(CoverageSubtypeParentType)
     * @see net.opengis.wcs20.Wcs20Package#getCoverageSummaryType_CoverageSubtypeParent()
     * @model containment="true"
     *        extendedMetaData="kind='element' name='CoverageSubtypeParent' namespace='##targetNamespace'"
     * @generated
     */
    CoverageSubtypeParentType getCoverageSubtypeParent();

    /**
     * Sets the value of the '{@link net.opengis.wcs20.CoverageSummaryType#getCoverageSubtypeParent <em>Coverage Subtype Parent</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Coverage Subtype Parent</em>' containment reference.
     * @see #getCoverageSubtypeParent()
     * @generated
     */
    void setCoverageSubtypeParent(CoverageSubtypeParentType value);

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
     * @see net.opengis.wcs20.Wcs20Package#getCoverageSummaryType_BoundingBoxGroup()
     * @model unique="false" dataType="org.eclipse.emf.ecore.EFeatureMapEntry" many="true"
     *        extendedMetaData="kind='group' name='BoundingBox:group' namespace='http://www.opengis.net/ows/2.0'"
     * @generated
     */
    FeatureMap getBoundingBoxGroup();

    /**
     * Returns the value of the '<em><b>Bounding Box</b></em>' containment reference list.
     * The list contents are of type {@link net.opengis.ows20.BoundingBoxType}.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Bounding Box</em>' containment reference list isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Bounding Box</em>' containment reference list.
     * @see net.opengis.wcs20.Wcs20Package#getCoverageSummaryType_BoundingBox()
     * @model containment="true" transient="true" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='BoundingBox' namespace='http://www.opengis.net/ows/2.0' group='http://www.opengis.net/ows/2.0#BoundingBox:group'"
     * @generated
     */
    EList<BoundingBoxType> getBoundingBox();

    /**
     * Returns the value of the '<em><b>Metadata Group</b></em>' attribute list.
     * The list contents are of type {@link org.eclipse.emf.ecore.util.FeatureMap.Entry}.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Metadata Group</em>' attribute list isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Metadata Group</em>' attribute list.
     * @see net.opengis.wcs20.Wcs20Package#getCoverageSummaryType_MetadataGroup()
     * @model unique="false" dataType="org.eclipse.emf.ecore.EFeatureMapEntry" many="true"
     *        extendedMetaData="kind='group' name='Metadata:group' namespace='http://www.opengis.net/ows/2.0'"
     * @generated
     */
    FeatureMap getMetadataGroup();

    /**
     * Returns the value of the '<em><b>Metadata</b></em>' containment reference list.
     * The list contents are of type {@link net.opengis.ows20.MetadataType}.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Metadata</em>' containment reference list isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Metadata</em>' containment reference list.
     * @see net.opengis.wcs20.Wcs20Package#getCoverageSummaryType_Metadata()
     * @model containment="true" transient="true" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='Metadata' namespace='http://www.opengis.net/ows/2.0' group='http://www.opengis.net/ows/2.0#Metadata:group'"
     * @generated
     */
    EList<MetadataType> getMetadata();

} // CoverageSummaryType
