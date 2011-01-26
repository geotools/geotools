/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package net.opengis.wcs10;


/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Axis Description Type</b></em>'.
 * <!-- end-user-doc -->
 *
 * <!-- begin-model-doc -->
 * Description of a measured or observed quantity, and list of the “valid” quantity values (values for which measurements are available or “by which” aggregate values are available). The semantic is the URI of the quantity (for example observable or mathematical variable). The refSys attribute is a URI to a reference system, and the refSysLabel is the label used by client to refer the reference system.
 * <!-- end-model-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link net.opengis.wcs10.AxisDescriptionType#getValues <em>Values</em>}</li>
 *   <li>{@link net.opengis.wcs10.AxisDescriptionType#getRefSys <em>Ref Sys</em>}</li>
 *   <li>{@link net.opengis.wcs10.AxisDescriptionType#getRefSysLabel <em>Ref Sys Label</em>}</li>
 *   <li>{@link net.opengis.wcs10.AxisDescriptionType#getSemantic <em>Semantic</em>}</li>
 * </ul>
 * </p>
 *
 * @see net.opengis.wcs10.Wcs10Package#getAxisDescriptionType()
 * @model extendedMetaData="name='AxisDescriptionType' kind='elementOnly'"
 * @generated
 */
public interface AxisDescriptionType extends AbstractDescriptionType {
    /**
	 * Returns the value of the '<em><b>Values</b></em>' containment reference.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * The type and value constraints for the values of this axis.
	 * <!-- end-model-doc -->
	 * @return the value of the '<em>Values</em>' containment reference.
	 * @see #setValues(ValuesType)
	 * @see net.opengis.wcs10.Wcs10Package#getAxisDescriptionType_Values()
	 * @model containment="true" required="true"
	 *        extendedMetaData="kind='element' name='values' namespace='##targetNamespace'"
	 * @generated
	 */
    ValuesType getValues();

    /**
	 * Sets the value of the '{@link net.opengis.wcs10.AxisDescriptionType#getValues <em>Values</em>}' containment reference.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Values</em>' containment reference.
	 * @see #getValues()
	 * @generated
	 */
    void setValues(ValuesType value);

    /**
	 * Returns the value of the '<em><b>Ref Sys</b></em>' attribute.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * Pointer to the reference system in which values are expressed. This attribute shall be included either here or in RangeSetType.
	 * <!-- end-model-doc -->
	 * @return the value of the '<em>Ref Sys</em>' attribute.
	 * @see #setRefSys(String)
	 * @see net.opengis.wcs10.Wcs10Package#getAxisDescriptionType_RefSys()
	 * @model dataType="org.eclipse.emf.ecore.xml.type.AnyURI"
	 *        extendedMetaData="kind='attribute' name='refSys'"
	 * @generated
	 */
    String getRefSys();

    /**
	 * Sets the value of the '{@link net.opengis.wcs10.AxisDescriptionType#getRefSys <em>Ref Sys</em>}' attribute.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Ref Sys</em>' attribute.
	 * @see #getRefSys()
	 * @generated
	 */
    void setRefSys(String value);

    /**
	 * Returns the value of the '<em><b>Ref Sys Label</b></em>' attribute.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * Short human-readable label denoting the reference system, for human interface display. This attribute shall be included either here or in RangeSetType.
	 * <!-- end-model-doc -->
	 * @return the value of the '<em>Ref Sys Label</em>' attribute.
	 * @see #setRefSysLabel(String)
	 * @see net.opengis.wcs10.Wcs10Package#getAxisDescriptionType_RefSysLabel()
	 * @model dataType="org.eclipse.emf.ecore.xml.type.String"
	 *        extendedMetaData="kind='attribute' name='refSysLabel'"
	 * @generated
	 */
    String getRefSysLabel();

    /**
	 * Sets the value of the '{@link net.opengis.wcs10.AxisDescriptionType#getRefSysLabel <em>Ref Sys Label</em>}' attribute.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Ref Sys Label</em>' attribute.
	 * @see #getRefSysLabel()
	 * @generated
	 */
    void setRefSysLabel(String value);

    /**
	 * Returns the value of the '<em><b>Semantic</b></em>' attribute.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * Definition of the semantics or meaning of the values in the XML element it belongs to. The value of this "semantic" attribute can be a RDF Property or Class of a taxonomy or ontology.
	 * <!-- end-model-doc -->
	 * @return the value of the '<em>Semantic</em>' attribute.
	 * @see #setSemantic(String)
	 * @see net.opengis.wcs10.Wcs10Package#getAxisDescriptionType_Semantic()
	 * @model dataType="org.eclipse.emf.ecore.xml.type.AnyURI"
	 *        extendedMetaData="kind='attribute' name='semantic' namespace='##targetNamespace'"
	 * @generated
	 */
    String getSemantic();

    /**
	 * Sets the value of the '{@link net.opengis.wcs10.AxisDescriptionType#getSemantic <em>Semantic</em>}' attribute.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Semantic</em>' attribute.
	 * @see #getSemantic()
	 * @generated
	 */
    void setSemantic(String value);

} // AxisDescriptionType
