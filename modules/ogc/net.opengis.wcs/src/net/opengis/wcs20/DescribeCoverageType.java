/**
 */
package net.opengis.wcs20;

import org.eclipse.emf.common.util.EList;
import java.util.List;


/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Describe Coverage Type</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link net.opengis.wcs20.DescribeCoverageType#getCoverageId <em>Coverage Id</em>}</li>
 * </ul>
 * </p>
 *
 * @see net.opengis.wcs20.Wcs20Package#getDescribeCoverageType()
 * @model extendedMetaData="name='DescribeCoverageType' kind='elementOnly'"
 * @generated
 */
public interface DescribeCoverageType extends RequestBaseType {
    /**
     * Returns the value of the '<em><b>Coverage Id</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * Unordered list of identifiers of desired coverages. A client can obtain identifiers by a prior GetCapabilities request, or from a third-party source.
     * <!-- end-model-doc -->
     * @return the value of the '<em>Coverage Id</em>' attribute.
     * @see #setCoverageId(String)
     * @see net.opengis.wcs20.Wcs20Package#getDescribeCoverageType_CoverageId()
     * @model unique="false" required="true"
     *        extendedMetaData="kind='element' name='CoverageId' namespace='##targetNamespace'"
     */
    EList<String> getCoverageId();

} // DescribeCoverageType
