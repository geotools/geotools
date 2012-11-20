/**
 */
package net.opengis.wcs20;

import net.opengis.ows20.ContentsBaseType;

import org.eclipse.emf.common.util.EList;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Contents Type</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link net.opengis.wcs20.ContentsType#getCoverageSummary <em>Coverage Summary</em>}</li>
 *   <li>{@link net.opengis.wcs20.ContentsType#getExtension <em>Extension</em>}</li>
 * </ul>
 * </p>
 *
 * @see net.opengis.wcs20.Wcs20Package#getContentsType()
 * @model extendedMetaData="name='ContentsType' kind='elementOnly'"
 * @generated
 */
public interface ContentsType extends ContentsBaseType {
    /**
     * Returns the value of the '<em><b>Coverage Summary</b></em>' containment reference list.
     * The list contents are of type {@link net.opengis.wcs20.CoverageSummaryType}.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * A CoverageSummary contains information essential for accessing a coverage served by a WCS. The CoverageId is the identifier used to address a particular coverage. The CoverageSubtype is the name of the root of this coverage when expressed in XML.
     * <!-- end-model-doc -->
     * @return the value of the '<em>Coverage Summary</em>' containment reference list.
     * @see net.opengis.wcs20.Wcs20Package#getContentsType_CoverageSummary()
     * @model containment="true"
     *        extendedMetaData="kind='element' name='CoverageSummary' namespace='##targetNamespace'"
     * @generated
     */
    EList<CoverageSummaryType> getCoverageSummary();

    /**
     * Returns the value of the '<em><b>Extension</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * Extension element used to hook in additional content e.g. in extensions or application profiles.
     * <!-- end-model-doc -->
     * @return the value of the '<em>Extension</em>' containment reference.
     * @see #setExtension(ExtensionType)
     * @see net.opengis.wcs20.Wcs20Package#getContentsType_Extension()
     * @model containment="true"
     *        extendedMetaData="kind='element' name='Extension' namespace='##targetNamespace'"
     * @generated
     */
    ExtensionType getExtension();

    /**
     * Sets the value of the '{@link net.opengis.wcs20.ContentsType#getExtension <em>Extension</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Extension</em>' containment reference.
     * @see #getExtension()
     * @generated
     */
    void setExtension(ExtensionType value);

} // ContentsType
