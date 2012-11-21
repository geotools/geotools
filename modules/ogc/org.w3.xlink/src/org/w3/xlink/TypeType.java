/**
 */
package org.w3.xlink;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.eclipse.emf.common.util.AbstractEnumerator;

/**
 * <!-- begin-user-doc -->
 * A representation of the literals of the enumeration '<em><b>Type Type</b></em>',
 * and utility methods for working with them.
 * <!-- end-user-doc -->
 * @see org.w3.xlink.XlinkPackage#getTypeType()
 * @model extendedMetaData="name='typeType'"
 * @generated
 */
public final class TypeType extends AbstractEnumerator {
    /**
     * The '<em><b>Simple</b></em>' literal value.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of '<em><b>Simple</b></em>' literal object isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @see #SIMPLE_LITERAL
     * @model name="simple"
     * @generated
     * @ordered
     */
    public static final int SIMPLE = 0;

    /**
     * The '<em><b>Extended</b></em>' literal value.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of '<em><b>Extended</b></em>' literal object isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @see #EXTENDED_LITERAL
     * @model name="extended"
     * @generated
     * @ordered
     */
    public static final int EXTENDED = 1;

    /**
     * The '<em><b>Title</b></em>' literal value.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of '<em><b>Title</b></em>' literal object isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @see #TITLE_LITERAL
     * @model name="title"
     * @generated
     * @ordered
     */
    public static final int TITLE = 2;

    /**
     * The '<em><b>Resource</b></em>' literal value.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of '<em><b>Resource</b></em>' literal object isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @see #RESOURCE_LITERAL
     * @model name="resource"
     * @generated
     * @ordered
     */
    public static final int RESOURCE = 3;

    /**
     * The '<em><b>Locator</b></em>' literal value.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of '<em><b>Locator</b></em>' literal object isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @see #LOCATOR_LITERAL
     * @model name="locator"
     * @generated
     * @ordered
     */
    public static final int LOCATOR = 4;

    /**
     * The '<em><b>Arc</b></em>' literal value.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of '<em><b>Arc</b></em>' literal object isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @see #ARC_LITERAL
     * @model name="arc"
     * @generated
     * @ordered
     */
    public static final int ARC = 5;

    /**
     * The '<em><b>Simple</b></em>' literal object.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #SIMPLE
     * @generated
     * @ordered
     */
    public static final TypeType SIMPLE_LITERAL = new TypeType(SIMPLE, "simple", "simple");

    /**
     * The '<em><b>Extended</b></em>' literal object.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #EXTENDED
     * @generated
     * @ordered
     */
    public static final TypeType EXTENDED_LITERAL = new TypeType(EXTENDED, "extended", "extended");

    /**
     * The '<em><b>Title</b></em>' literal object.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #TITLE
     * @generated
     * @ordered
     */
    public static final TypeType TITLE_LITERAL = new TypeType(TITLE, "title", "title");

    /**
     * The '<em><b>Resource</b></em>' literal object.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #RESOURCE
     * @generated
     * @ordered
     */
    public static final TypeType RESOURCE_LITERAL = new TypeType(RESOURCE, "resource", "resource");

    /**
     * The '<em><b>Locator</b></em>' literal object.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #LOCATOR
     * @generated
     * @ordered
     */
    public static final TypeType LOCATOR_LITERAL = new TypeType(LOCATOR, "locator", "locator");

    /**
     * The '<em><b>Arc</b></em>' literal object.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #ARC
     * @generated
     * @ordered
     */
    public static final TypeType ARC_LITERAL = new TypeType(ARC, "arc", "arc");

    /**
     * An array of all the '<em><b>Type Type</b></em>' enumerators.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private static final TypeType[] VALUES_ARRAY =
        new TypeType[] {
            SIMPLE_LITERAL,
            EXTENDED_LITERAL,
            TITLE_LITERAL,
            RESOURCE_LITERAL,
            LOCATOR_LITERAL,
            ARC_LITERAL,
        };

    /**
     * A public read-only list of all the '<em><b>Type Type</b></em>' enumerators.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public static final List VALUES = Collections.unmodifiableList(Arrays.asList(VALUES_ARRAY));

    /**
     * Returns the '<em><b>Type Type</b></em>' literal with the specified literal value.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public static TypeType get(String literal) {
        for (int i = 0; i < VALUES_ARRAY.length; ++i) {
            TypeType result = VALUES_ARRAY[i];
            if (result.toString().equals(literal)) {
                return result;
            }
        }
        return null;
    }

    /**
     * Returns the '<em><b>Type Type</b></em>' literal with the specified name.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public static TypeType getByName(String name) {
        for (int i = 0; i < VALUES_ARRAY.length; ++i) {
            TypeType result = VALUES_ARRAY[i];
            if (result.getName().equals(name)) {
                return result;
            }
        }
        return null;
    }

    /**
     * Returns the '<em><b>Type Type</b></em>' literal with the specified integer value.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public static TypeType get(int value) {
        switch (value) {
            case SIMPLE: return SIMPLE_LITERAL;
            case EXTENDED: return EXTENDED_LITERAL;
            case TITLE: return TITLE_LITERAL;
            case RESOURCE: return RESOURCE_LITERAL;
            case LOCATOR: return LOCATOR_LITERAL;
            case ARC: return ARC_LITERAL;
        }
        return null;
    }

    /**
     * Only this class can construct instances.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private TypeType(int value, String name, String literal) {
        super(value, name, literal);
    }

} //TypeType
