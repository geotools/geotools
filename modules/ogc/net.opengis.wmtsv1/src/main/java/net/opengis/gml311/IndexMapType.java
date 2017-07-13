/**
 */
package net.opengis.gml311;

import java.math.BigInteger;

import java.util.List;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Index Map Type</b></em>'.
 * <!-- end-user-doc -->
 *
 * <!-- begin-model-doc -->
 * Exends GridFunctionType with a lookUpTable.  This contains a list of indexes of members within the rangeSet corresponding with the members of the domainSet.  The domainSet is traversed in list order if it is enumerated explicitly, or in the order specified by a SequenceRule if the domain is an implicit set.    The length of the lookUpTable corresponds with the length of the subset of the domainSet for which the coverage is defined.
 * <!-- end-model-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link net.opengis.gml311.IndexMapType#getLookUpTable <em>Look Up Table</em>}</li>
 * </ul>
 *
 * @see net.opengis.gml311.Gml311Package#getIndexMapType()
 * @model extendedMetaData="name='IndexMapType' kind='elementOnly'"
 * @generated
 */
public interface IndexMapType extends GridFunctionType {
    /**
     * Returns the value of the '<em><b>Look Up Table</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Look Up Table</em>' attribute isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Look Up Table</em>' attribute.
     * @see #setLookUpTable(List)
     * @see net.opengis.gml311.Gml311Package#getIndexMapType_LookUpTable()
     * @model dataType="net.opengis.gml311.IntegerList" required="true" many="false"
     *        extendedMetaData="kind='element' name='lookUpTable' namespace='##targetNamespace'"
     * @generated
     */
    List<BigInteger> getLookUpTable();

    /**
     * Sets the value of the '{@link net.opengis.gml311.IndexMapType#getLookUpTable <em>Look Up Table</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Look Up Table</em>' attribute.
     * @see #getLookUpTable()
     * @generated
     */
    void setLookUpTable(List<BigInteger> value);

} // IndexMapType
