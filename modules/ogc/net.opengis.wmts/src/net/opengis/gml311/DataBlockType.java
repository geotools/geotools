/**
 */
package net.opengis.gml311;

import java.util.List;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Data Block Type</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link net.opengis.gml311.DataBlockType#getRangeParameters <em>Range Parameters</em>}</li>
 *   <li>{@link net.opengis.gml311.DataBlockType#getTupleList <em>Tuple List</em>}</li>
 *   <li>{@link net.opengis.gml311.DataBlockType#getDoubleOrNullTupleList <em>Double Or Null Tuple List</em>}</li>
 * </ul>
 *
 * @see net.opengis.gml311.Gml311Package#getDataBlockType()
 * @model extendedMetaData="name='DataBlockType' kind='elementOnly'"
 * @generated
 */
public interface DataBlockType extends EObject {
    /**
     * Returns the value of the '<em><b>Range Parameters</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Range Parameters</em>' containment reference isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Range Parameters</em>' containment reference.
     * @see #setRangeParameters(RangeParametersType)
     * @see net.opengis.gml311.Gml311Package#getDataBlockType_RangeParameters()
     * @model containment="true" required="true"
     *        extendedMetaData="kind='element' name='rangeParameters' namespace='##targetNamespace'"
     * @generated
     */
    RangeParametersType getRangeParameters();

    /**
     * Sets the value of the '{@link net.opengis.gml311.DataBlockType#getRangeParameters <em>Range Parameters</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Range Parameters</em>' containment reference.
     * @see #getRangeParameters()
     * @generated
     */
    void setRangeParameters(RangeParametersType value);

    /**
     * Returns the value of the '<em><b>Tuple List</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Tuple List</em>' containment reference isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Tuple List</em>' containment reference.
     * @see #setTupleList(CoordinatesType)
     * @see net.opengis.gml311.Gml311Package#getDataBlockType_TupleList()
     * @model containment="true"
     *        extendedMetaData="kind='element' name='tupleList' namespace='##targetNamespace'"
     * @generated
     */
    CoordinatesType getTupleList();

    /**
     * Sets the value of the '{@link net.opengis.gml311.DataBlockType#getTupleList <em>Tuple List</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Tuple List</em>' containment reference.
     * @see #getTupleList()
     * @generated
     */
    void setTupleList(CoordinatesType value);

    /**
     * Returns the value of the '<em><b>Double Or Null Tuple List</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Double Or Null Tuple List</em>' attribute isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Double Or Null Tuple List</em>' attribute.
     * @see #setDoubleOrNullTupleList(List)
     * @see net.opengis.gml311.Gml311Package#getDataBlockType_DoubleOrNullTupleList()
     * @model dataType="net.opengis.gml311.DoubleOrNullList" many="false"
     *        extendedMetaData="kind='element' name='doubleOrNullTupleList' namespace='##targetNamespace'"
     * @generated
     */
    List<Object> getDoubleOrNullTupleList();

    /**
     * Sets the value of the '{@link net.opengis.gml311.DataBlockType#getDoubleOrNullTupleList <em>Double Or Null Tuple List</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Double Or Null Tuple List</em>' attribute.
     * @see #getDoubleOrNullTupleList()
     * @generated
     */
    void setDoubleOrNullTupleList(List<Object> value);

} // DataBlockType
