/**
 */
package net.opengis.wcs20;

import javax.xml.namespace.QName;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Service Parameters Type</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link net.opengis.wcs20.ServiceParametersType#getCoverageSubtype <em>Coverage Subtype</em>}</li>
 *   <li>{@link net.opengis.wcs20.ServiceParametersType#getCoverageSubtypeParent <em>Coverage Subtype Parent</em>}</li>
 *   <li>{@link net.opengis.wcs20.ServiceParametersType#getNativeFormat <em>Native Format</em>}</li>
 *   <li>{@link net.opengis.wcs20.ServiceParametersType#getExtension <em>Extension</em>}</li>
 * </ul>
 * </p>
 *
 * @see net.opengis.wcs20.Wcs20Package#getServiceParametersType()
 * @model extendedMetaData="name='ServiceParametersType' kind='elementOnly'"
 * @generated
 */
public interface ServiceParametersType extends EObject {
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
     * @see net.opengis.wcs20.Wcs20Package#getServiceParametersType_CoverageSubtype()
     * @model required="true"
     *        extendedMetaData="kind='element' name='CoverageSubtype' namespace='##targetNamespace'"
     */
    QName getCoverageSubtype();

    /**
     * Sets the value of the '{@link net.opengis.wcs20.ServiceParametersType#getCoverageSubtype <em>Coverage Subtype</em>}' attribute.
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
     * @see net.opengis.wcs20.Wcs20Package#getServiceParametersType_CoverageSubtypeParent()
     * @model containment="true"
     *        extendedMetaData="kind='element' name='CoverageSubtypeParent' namespace='##targetNamespace'"
     * @generated
     */
    CoverageSubtypeParentType getCoverageSubtypeParent();

    /**
     * Sets the value of the '{@link net.opengis.wcs20.ServiceParametersType#getCoverageSubtypeParent <em>Coverage Subtype Parent</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Coverage Subtype Parent</em>' containment reference.
     * @see #getCoverageSubtypeParent()
     * @generated
     */
    void setCoverageSubtypeParent(CoverageSubtypeParentType value);

    /**
     * Returns the value of the '<em><b>Native Format</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Native Format</em>' attribute isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Native Format</em>' attribute.
     * @see #setNativeFormat(String)
     * @see net.opengis.wcs20.Wcs20Package#getServiceParametersType_NativeFormat()
     * @model dataType="org.eclipse.emf.ecore.xml.type.AnyURI" required="true"
     *        extendedMetaData="kind='element' name='nativeFormat' namespace='##targetNamespace'"
     * @generated
     */
    String getNativeFormat();

    /**
     * Sets the value of the '{@link net.opengis.wcs20.ServiceParametersType#getNativeFormat <em>Native Format</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Native Format</em>' attribute.
     * @see #getNativeFormat()
     * @generated
     */
    void setNativeFormat(String value);

    /**
     * Returns the value of the '<em><b>Extension</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * Extension element used to hook in additional content e.g. in extensions or application profiles.
     * <!-- end-model-doc -->
     * @return the value of the '<em>Extension</em>' containment reference.
     * @see #setExtension(ExtensionType)
     * @see net.opengis.wcs20.Wcs20Package#getServiceParametersType_Extension()
     * @model containment="true"
     *        extendedMetaData="kind='element' name='Extension' namespace='##targetNamespace'"
     * @generated
     */
    ExtensionType getExtension();

    /**
     * Sets the value of the '{@link net.opengis.wcs20.ServiceParametersType#getExtension <em>Extension</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Extension</em>' containment reference.
     * @see #getExtension()
     * @generated
     */
    void setExtension(ExtensionType value);

} // ServiceParametersType
