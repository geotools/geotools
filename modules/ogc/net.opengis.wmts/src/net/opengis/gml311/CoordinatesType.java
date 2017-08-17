/**
 */
package net.opengis.gml311;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Coordinates Type</b></em>'.
 * <!-- end-user-doc -->
 *
 * <!-- begin-model-doc -->
 * Tables or arrays of tuples.  
 *         May be used for text-encoding of values from a table.  
 *         Actually just a string, but allows the user to indicate which characters are used as separators.  
 *         The value of the 'cs' attribute is the separator for coordinate values, 
 *         and the value of the 'ts' attribute gives the tuple separator (a single space by default); 
 *         the default values may be changed to reflect local usage.
 *         Defaults to CSV within a tuple, space between tuples.  
 *         However, any string content will be schema-valid.  
 * <!-- end-model-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link net.opengis.gml311.CoordinatesType#getValue <em>Value</em>}</li>
 *   <li>{@link net.opengis.gml311.CoordinatesType#getCs <em>Cs</em>}</li>
 *   <li>{@link net.opengis.gml311.CoordinatesType#getDecimal <em>Decimal</em>}</li>
 *   <li>{@link net.opengis.gml311.CoordinatesType#getTs <em>Ts</em>}</li>
 * </ul>
 *
 * @see net.opengis.gml311.Gml311Package#getCoordinatesType()
 * @model extendedMetaData="name='CoordinatesType' kind='simple'"
 * @generated
 */
public interface CoordinatesType extends EObject {
    /**
     * Returns the value of the '<em><b>Value</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Value</em>' attribute isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Value</em>' attribute.
     * @see #setValue(String)
     * @see net.opengis.gml311.Gml311Package#getCoordinatesType_Value()
     * @model dataType="org.eclipse.emf.ecore.xml.type.String"
     *        extendedMetaData="name=':0' kind='simple'"
     * @generated
     */
    String getValue();

    /**
     * Sets the value of the '{@link net.opengis.gml311.CoordinatesType#getValue <em>Value</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Value</em>' attribute.
     * @see #getValue()
     * @generated
     */
    void setValue(String value);

    /**
     * Returns the value of the '<em><b>Cs</b></em>' attribute.
     * The default value is <code>","</code>.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Cs</em>' attribute isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Cs</em>' attribute.
     * @see #isSetCs()
     * @see #unsetCs()
     * @see #setCs(String)
     * @see net.opengis.gml311.Gml311Package#getCoordinatesType_Cs()
     * @model default="," unsettable="true" dataType="org.eclipse.emf.ecore.xml.type.String"
     *        extendedMetaData="kind='attribute' name='cs'"
     * @generated
     */
    String getCs();

    /**
     * Sets the value of the '{@link net.opengis.gml311.CoordinatesType#getCs <em>Cs</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Cs</em>' attribute.
     * @see #isSetCs()
     * @see #unsetCs()
     * @see #getCs()
     * @generated
     */
    void setCs(String value);

    /**
     * Unsets the value of the '{@link net.opengis.gml311.CoordinatesType#getCs <em>Cs</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #isSetCs()
     * @see #getCs()
     * @see #setCs(String)
     * @generated
     */
    void unsetCs();

    /**
     * Returns whether the value of the '{@link net.opengis.gml311.CoordinatesType#getCs <em>Cs</em>}' attribute is set.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return whether the value of the '<em>Cs</em>' attribute is set.
     * @see #unsetCs()
     * @see #getCs()
     * @see #setCs(String)
     * @generated
     */
    boolean isSetCs();

    /**
     * Returns the value of the '<em><b>Decimal</b></em>' attribute.
     * The default value is <code>"."</code>.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Decimal</em>' attribute isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Decimal</em>' attribute.
     * @see #isSetDecimal()
     * @see #unsetDecimal()
     * @see #setDecimal(String)
     * @see net.opengis.gml311.Gml311Package#getCoordinatesType_Decimal()
     * @model default="." unsettable="true" dataType="org.eclipse.emf.ecore.xml.type.String"
     *        extendedMetaData="kind='attribute' name='decimal'"
     * @generated
     */
    String getDecimal();

    /**
     * Sets the value of the '{@link net.opengis.gml311.CoordinatesType#getDecimal <em>Decimal</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Decimal</em>' attribute.
     * @see #isSetDecimal()
     * @see #unsetDecimal()
     * @see #getDecimal()
     * @generated
     */
    void setDecimal(String value);

    /**
     * Unsets the value of the '{@link net.opengis.gml311.CoordinatesType#getDecimal <em>Decimal</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #isSetDecimal()
     * @see #getDecimal()
     * @see #setDecimal(String)
     * @generated
     */
    void unsetDecimal();

    /**
     * Returns whether the value of the '{@link net.opengis.gml311.CoordinatesType#getDecimal <em>Decimal</em>}' attribute is set.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return whether the value of the '<em>Decimal</em>' attribute is set.
     * @see #unsetDecimal()
     * @see #getDecimal()
     * @see #setDecimal(String)
     * @generated
     */
    boolean isSetDecimal();

    /**
     * Returns the value of the '<em><b>Ts</b></em>' attribute.
     * The default value is <code>" "</code>.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Ts</em>' attribute isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Ts</em>' attribute.
     * @see #isSetTs()
     * @see #unsetTs()
     * @see #setTs(String)
     * @see net.opengis.gml311.Gml311Package#getCoordinatesType_Ts()
     * @model default=" " unsettable="true" dataType="org.eclipse.emf.ecore.xml.type.String"
     *        extendedMetaData="kind='attribute' name='ts'"
     * @generated
     */
    String getTs();

    /**
     * Sets the value of the '{@link net.opengis.gml311.CoordinatesType#getTs <em>Ts</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Ts</em>' attribute.
     * @see #isSetTs()
     * @see #unsetTs()
     * @see #getTs()
     * @generated
     */
    void setTs(String value);

    /**
     * Unsets the value of the '{@link net.opengis.gml311.CoordinatesType#getTs <em>Ts</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #isSetTs()
     * @see #getTs()
     * @see #setTs(String)
     * @generated
     */
    void unsetTs();

    /**
     * Returns whether the value of the '{@link net.opengis.gml311.CoordinatesType#getTs <em>Ts</em>}' attribute is set.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return whether the value of the '<em>Ts</em>' attribute is set.
     * @see #unsetTs()
     * @see #getTs()
     * @see #setTs(String)
     * @generated
     */
    boolean isSetTs();

} // CoordinatesType
