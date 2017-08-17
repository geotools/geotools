/**
 */
package net.opengis.gml311;


/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Abstract Time Geometric Primitive Type</b></em>'.
 * <!-- end-user-doc -->
 *
 * <!-- begin-model-doc -->
 * The abstract supertype for temporal geometric primitives.
 *        A temporal geometry must be associated with a temporal reference system via URI. 
 *        The Gregorian calendar with UTC is the default reference system, following ISO 
 *        8601. Other reference systems in common use include the GPS calendar and the 
 *        Julian calendar.
 * <!-- end-model-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link net.opengis.gml311.AbstractTimeGeometricPrimitiveType#getFrame <em>Frame</em>}</li>
 * </ul>
 *
 * @see net.opengis.gml311.Gml311Package#getAbstractTimeGeometricPrimitiveType()
 * @model abstract="true"
 *        extendedMetaData="name='AbstractTimeGeometricPrimitiveType' kind='elementOnly'"
 * @generated
 */
public interface AbstractTimeGeometricPrimitiveType extends AbstractTimePrimitiveType {
    /**
     * Returns the value of the '<em><b>Frame</b></em>' attribute.
     * The default value is <code>"#ISO-8601"</code>.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Frame</em>' attribute isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @return the value of the '<em>Frame</em>' attribute.
     * @see #isSetFrame()
     * @see #unsetFrame()
     * @see #setFrame(String)
     * @see net.opengis.gml311.Gml311Package#getAbstractTimeGeometricPrimitiveType_Frame()
     * @model default="#ISO-8601" unsettable="true" dataType="org.eclipse.emf.ecore.xml.type.AnyURI"
     *        extendedMetaData="kind='attribute' name='frame'"
     * @generated
     */
    String getFrame();

    /**
     * Sets the value of the '{@link net.opengis.gml311.AbstractTimeGeometricPrimitiveType#getFrame <em>Frame</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Frame</em>' attribute.
     * @see #isSetFrame()
     * @see #unsetFrame()
     * @see #getFrame()
     * @generated
     */
    void setFrame(String value);

    /**
     * Unsets the value of the '{@link net.opengis.gml311.AbstractTimeGeometricPrimitiveType#getFrame <em>Frame</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #isSetFrame()
     * @see #getFrame()
     * @see #setFrame(String)
     * @generated
     */
    void unsetFrame();

    /**
     * Returns whether the value of the '{@link net.opengis.gml311.AbstractTimeGeometricPrimitiveType#getFrame <em>Frame</em>}' attribute is set.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return whether the value of the '<em>Frame</em>' attribute is set.
     * @see #unsetFrame()
     * @see #getFrame()
     * @see #setFrame(String)
     * @generated
     */
    boolean isSetFrame();

} // AbstractTimeGeometricPrimitiveType
