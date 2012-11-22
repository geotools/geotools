/**
 */
package net.opengis.wcs20;

import javax.xml.namespace.QName;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Coverage Subtype Parent Type</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link net.opengis.wcs20.CoverageSubtypeParentType#getCoverageSubtype <em>Coverage Subtype</em>}</li>
 *   <li>{@link net.opengis.wcs20.CoverageSubtypeParentType#getCoverageSubtypeParent <em>Coverage Subtype Parent</em>}</li>
 * </ul>
 * </p>
 *
 * @see net.opengis.wcs20.Wcs20Package#getCoverageSubtypeParentType()
 * @model extendedMetaData="name='CoverageSubtypeParentType' kind='elementOnly'"
 * @generated
 */
public interface CoverageSubtypeParentType extends EObject {
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
     * @see net.opengis.wcs20.Wcs20Package#getCoverageSubtypeParentType_CoverageSubtype()
     * @model required="true"
     *        extendedMetaData="kind='element' name='CoverageSubtype' namespace='##targetNamespace'"
     */
    QName getCoverageSubtype();

    /**
     * Sets the value of the '{@link net.opengis.wcs20.CoverageSubtypeParentType#getCoverageSubtype <em>Coverage Subtype</em>}' attribute.
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
     * @see net.opengis.wcs20.Wcs20Package#getCoverageSubtypeParentType_CoverageSubtypeParent()
     * @model containment="true"
     *        extendedMetaData="kind='element' name='CoverageSubtypeParent' namespace='##targetNamespace'"
     * @generated
     */
    CoverageSubtypeParentType getCoverageSubtypeParent();

    /**
     * Sets the value of the '{@link net.opengis.wcs20.CoverageSubtypeParentType#getCoverageSubtypeParent <em>Coverage Subtype Parent</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Coverage Subtype Parent</em>' containment reference.
     * @see #getCoverageSubtypeParent()
     * @generated
     */
    void setCoverageSubtypeParent(CoverageSubtypeParentType value);

} // CoverageSubtypeParentType
