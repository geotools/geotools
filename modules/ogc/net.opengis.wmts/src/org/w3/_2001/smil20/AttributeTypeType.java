/**
 */
package org.w3._2001.smil20;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.eclipse.emf.common.util.Enumerator;

/**
 * <!-- begin-user-doc -->
 * A representation of the literals of the enumeration '<em><b>Attribute Type Type</b></em>',
 * and utility methods for working with them.
 * <!-- end-user-doc -->
 * @see org.w3._2001.smil20.Smil20Package#getAttributeTypeType()
 * @model extendedMetaData="name='attributeType_._type'"
 * @generated
 */
public enum AttributeTypeType implements Enumerator {
    /**
     * The '<em><b>XML</b></em>' literal object.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #XML_VALUE
     * @generated
     * @ordered
     */
    XML(0, "XML", "XML"),

    /**
     * The '<em><b>CSS</b></em>' literal object.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #CSS_VALUE
     * @generated
     * @ordered
     */
    CSS(1, "CSS", "CSS"),

    /**
     * The '<em><b>Auto</b></em>' literal object.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #AUTO_VALUE
     * @generated
     * @ordered
     */
    AUTO(2, "auto", "auto");

    /**
     * The '<em><b>XML</b></em>' literal value.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of '<em><b>XML</b></em>' literal object isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @see #XML
     * @model
     * @generated
     * @ordered
     */
    public static final int XML_VALUE = 0;

    /**
     * The '<em><b>CSS</b></em>' literal value.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of '<em><b>CSS</b></em>' literal object isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @see #CSS
     * @model
     * @generated
     * @ordered
     */
    public static final int CSS_VALUE = 1;

    /**
     * The '<em><b>Auto</b></em>' literal value.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of '<em><b>Auto</b></em>' literal object isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @see #AUTO
     * @model name="auto"
     * @generated
     * @ordered
     */
    public static final int AUTO_VALUE = 2;

    /**
     * An array of all the '<em><b>Attribute Type Type</b></em>' enumerators.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private static final AttributeTypeType[] VALUES_ARRAY =
        new AttributeTypeType[] {
            XML,
            CSS,
            AUTO,
        };

    /**
     * A public read-only list of all the '<em><b>Attribute Type Type</b></em>' enumerators.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public static final List<AttributeTypeType> VALUES = Collections.unmodifiableList(Arrays.asList(VALUES_ARRAY));

    /**
     * Returns the '<em><b>Attribute Type Type</b></em>' literal with the specified literal value.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param literal the literal.
     * @return the matching enumerator or <code>null</code>.
     * @generated
     */
    public static AttributeTypeType get(String literal) {
        for (int i = 0; i < VALUES_ARRAY.length; ++i) {
            AttributeTypeType result = VALUES_ARRAY[i];
            if (result.toString().equals(literal)) {
                return result;
            }
        }
        return null;
    }

    /**
     * Returns the '<em><b>Attribute Type Type</b></em>' literal with the specified name.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param name the name.
     * @return the matching enumerator or <code>null</code>.
     * @generated
     */
    public static AttributeTypeType getByName(String name) {
        for (int i = 0; i < VALUES_ARRAY.length; ++i) {
            AttributeTypeType result = VALUES_ARRAY[i];
            if (result.getName().equals(name)) {
                return result;
            }
        }
        return null;
    }

    /**
     * Returns the '<em><b>Attribute Type Type</b></em>' literal with the specified integer value.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the integer value.
     * @return the matching enumerator or <code>null</code>.
     * @generated
     */
    public static AttributeTypeType get(int value) {
        switch (value) {
            case XML_VALUE: return XML;
            case CSS_VALUE: return CSS;
            case AUTO_VALUE: return AUTO;
        }
        return null;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private final int value;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private final String name;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private final String literal;

    /**
     * Only this class can construct instances.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private AttributeTypeType(int value, String name, String literal) {
        this.value = value;
        this.name = name;
        this.literal = literal;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public int getValue() {
      return value;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public String getName() {
      return name;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public String getLiteral() {
      return literal;
    }

    /**
     * Returns the literal value of the enumerator, which is its string representation.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public String toString() {
        return literal;
    }
    
} //AttributeTypeType
