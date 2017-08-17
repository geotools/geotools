/**
 */
package net.opengis.gml311;


/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Abstract Time Slice Type</b></em>'.
 * <!-- end-user-doc -->
 *
 * <!-- begin-model-doc -->
 * A timeslice encapsulates the time-varying properties of a dynamic feature--it 
 *         must be extended to represent a timestamped projection of a feature. The dataSource 
 *         property describes how the temporal data was acquired.
 * <!-- end-model-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link net.opengis.gml311.AbstractTimeSliceType#getValidTime <em>Valid Time</em>}</li>
 *   <li>{@link net.opengis.gml311.AbstractTimeSliceType#getDataSource <em>Data Source</em>}</li>
 * </ul>
 *
 * @see net.opengis.gml311.Gml311Package#getAbstractTimeSliceType()
 * @model abstract="true"
 *        extendedMetaData="name='AbstractTimeSliceType' kind='elementOnly'"
 * @generated
 */
public interface AbstractTimeSliceType extends AbstractGMLType {
    /**
     * Returns the value of the '<em><b>Valid Time</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Valid Time</em>' containment reference isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Valid Time</em>' containment reference.
     * @see #setValidTime(TimePrimitivePropertyType)
     * @see net.opengis.gml311.Gml311Package#getAbstractTimeSliceType_ValidTime()
     * @model containment="true" required="true"
     *        extendedMetaData="kind='element' name='validTime' namespace='##targetNamespace'"
     * @generated
     */
    TimePrimitivePropertyType getValidTime();

    /**
     * Sets the value of the '{@link net.opengis.gml311.AbstractTimeSliceType#getValidTime <em>Valid Time</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Valid Time</em>' containment reference.
     * @see #getValidTime()
     * @generated
     */
    void setValidTime(TimePrimitivePropertyType value);

    /**
     * Returns the value of the '<em><b>Data Source</b></em>' containment reference.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Data Source</em>' containment reference isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Data Source</em>' containment reference.
     * @see #setDataSource(StringOrRefType)
     * @see net.opengis.gml311.Gml311Package#getAbstractTimeSliceType_DataSource()
     * @model containment="true"
     *        extendedMetaData="kind='element' name='dataSource' namespace='##targetNamespace'"
     * @generated
     */
    StringOrRefType getDataSource();

    /**
     * Sets the value of the '{@link net.opengis.gml311.AbstractTimeSliceType#getDataSource <em>Data Source</em>}' containment reference.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Data Source</em>' containment reference.
     * @see #getDataSource()
     * @generated
     */
    void setDataSource(StringOrRefType value);

} // AbstractTimeSliceType
