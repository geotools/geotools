/**
 */
package net.opengis.wcs20;

import org.eclipse.emf.common.util.EList;

/**
 * <!-- begin-user-doc --> A representation of the model object '
 * <em><b>Describe EO Coverage Set</b></em>'. <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link net.opengis.wcs20.DescribeEOCoverageSetType#getEoId <em>Eo Id</em>}</li>
 *   <li>{@link net.opengis.wcs20.DescribeEOCoverageSetType#getContainmentType <em>Containment Type</em>}</li>
 *   <li>{@link net.opengis.wcs20.DescribeEOCoverageSetType#getSections <em>Sections</em>}</li>
 *   <li>{@link net.opengis.wcs20.DescribeEOCoverageSetType#getDimensionTrim <em>Dimension Trim</em>}</li>
 *   <li>{@link net.opengis.wcs20.DescribeEOCoverageSetType#getCount <em>Count</em>}</li>
 * </ul>
 * </p>
 *
 * @see net.opengis.wcs20.Wcs20Package#getDescribeEOCoverageSetType()
 * @model extendedMetaData="name='DescribeCoverageType' kind='elementOnly'"
 * @generated
 */
public interface DescribeEOCoverageSetType extends RequestBaseType {
    /**
     * Returns the value of the '<em><b>Coverage Id</b></em>' attribute. <!-- begin-user-doc -->
     * <!-- end-user-doc --> <!-- begin-model-doc --> Unordered list of identifiers of desired
     * coverages. A client can obtain identifiers by a prior GetCapabilities request, or from a
     * third-party source. <!-- end-model-doc -->
     * 
     * @return the value of the '<em>Coverage Id</em>' attribute.
     * @see #setCoverageId(String)
     * @see net.opengis.wcs20.Wcs20Package#getDescribeCoverageType_CoverageId()
     * @model unique="false" required="true"
     *        extendedMetaData="kind='element' name='CoverageId' namespace='##targetNamespace'"
     */
    EList<String> getEoId();

    /**
     * @model dataType="org.eclipse.emf.ecore.xml.type.String" required="true"
     * extendedMetaData="kind='element' name='ContainmentType' namespace='##targetNamespace'"
     */
    String getContainmentType();
    
    /**
	 * Sets the value of the '{@link net.opengis.wcs20.DescribeEOCoverageSetType#getContainmentType <em>Containment Type</em>}' attribute.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Containment Type</em>' attribute.
	 * @see #getContainmentType()
	 * @generated
	 */
    void setContainmentType(String value);

    /**
     * Returns the value of the '<em><b>Sections</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * When omitted or not supported by server, server shall
     *           return complete service metadata (Capabilities)
     *           document.
     * <!-- end-model-doc -->
     * @return the value of the '<em>Sections</em>' containment reference.
     * @see #setSections(Sections)
     * @see net.opengis.ows20.Ows20Package#getGetCapabilitiesType_Sections()
     * @model containment="true"
     *        extendedMetaData="kind='element' name='Sections' namespace='##targetNamespace'"
     */
    Sections getSections();
    
    /**
	 * Sets the value of the '{@link net.opengis.wcs20.DescribeEOCoverageSetType#getSections <em>Sections</em>}' containment reference.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Sections</em>' containment reference.
	 * @see #getSections()
	 * @generated
	 */
    void setSections(Sections value);

    /**
    * @model unique="false" required="true"
    *        extendedMetaData="kind='element' name='DimensionTime' namespace='##targetNamespace'"
    */
    EList<DimensionTrimType> getDimensionTrim();
    
    /**
     * @model
     */
    int getCount();

    /**
	 * Sets the value of the '{@link net.opengis.wcs20.DescribeEOCoverageSetType#getCount <em>Count</em>}' attribute.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Count</em>' attribute.
	 * @see #getCount()
	 * @generated
	 */
    void setCount(int value);

} // DescribeCoverageType
