/**
 */
package net.opengis.gml311;

import org.eclipse.emf.common.util.EList;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Prime Meridian Type</b></em>'.
 * <!-- end-user-doc -->
 *
 * <!-- begin-model-doc -->
 * A prime meridian defines the origin from which longitude values are determined.
 * <!-- end-model-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link net.opengis.gml311.PrimeMeridianType#getMeridianID <em>Meridian ID</em>}</li>
 *   <li>{@link net.opengis.gml311.PrimeMeridianType#getRemarks <em>Remarks</em>}</li>
 *   <li>{@link net.opengis.gml311.PrimeMeridianType#getGreenwichLongitude <em>Greenwich Longitude</em>}</li>
 * </ul>
 *
 * @see net.opengis.gml311.Gml311Package#getPrimeMeridianType()
 * @model extendedMetaData="name='PrimeMeridianType' kind='elementOnly'"
 * @generated
 */
public interface PrimeMeridianType extends PrimeMeridianBaseType {
    /**
     * Returns the value of the '<em><b>Meridian ID</b></em>' containment reference list.
     * The list contents are of type {@link net.opengis.gml311.IdentifierType}.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * Set of alternative identifications of this prime meridian. The first meridianID, if any, is normally the primary identification code, and any others are aliases. 
     * <!-- end-model-doc -->
     * @return the value of the '<em>Meridian ID</em>' containment reference list.
     * @see net.opengis.gml311.Gml311Package#getPrimeMeridianType_MeridianID()
     * @model containment="true"
     *        extendedMetaData="kind='element' name='meridianID' namespace='##targetNamespace'"
     * @generated
     */
    EList<IdentifierType> getMeridianID();

    /**
     * Returns the value of the '<em><b>Remarks</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * Comments on or information about this prime meridian, including source information. 
     * <!-- end-model-doc -->
     * @return the value of the '<em>Remarks</em>' containment reference.
     * @see #setRemarks(StringOrRefType)
     * @see net.opengis.gml311.Gml311Package#getPrimeMeridianType_Remarks()
     * @model containment="true"
     *        extendedMetaData="kind='element' name='remarks' namespace='##targetNamespace'"
     * @generated
     */
    StringOrRefType getRemarks();

    /**
     * Sets the value of the '{@link net.opengis.gml311.PrimeMeridianType#getRemarks <em>Remarks</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Remarks</em>' containment reference.
     * @see #getRemarks()
     * @generated
     */
    void setRemarks(StringOrRefType value);

    /**
     * Returns the value of the '<em><b>Greenwich Longitude</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * <!-- begin-model-doc -->
     * Longitude of the prime meridian measured from the Greenwich meridian, positive eastward. The greenwichLongitude most common value is zero, and that value shall be used when the meridianName value is Greenwich. 
     * <!-- end-model-doc -->
     * @return the value of the '<em>Greenwich Longitude</em>' containment reference.
     * @see #setGreenwichLongitude(AngleChoiceType)
     * @see net.opengis.gml311.Gml311Package#getPrimeMeridianType_GreenwichLongitude()
     * @model containment="true" required="true"
     *        extendedMetaData="kind='element' name='greenwichLongitude' namespace='##targetNamespace'"
     * @generated
     */
    AngleChoiceType getGreenwichLongitude();

    /**
     * Sets the value of the '{@link net.opengis.gml311.PrimeMeridianType#getGreenwichLongitude <em>Greenwich Longitude</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Greenwich Longitude</em>' containment reference.
     * @see #getGreenwichLongitude()
     * @generated
     */
    void setGreenwichLongitude(AngleChoiceType value);

} // PrimeMeridianType
