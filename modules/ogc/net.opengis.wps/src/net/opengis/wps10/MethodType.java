/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package net.opengis.wps10;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.eclipse.emf.common.util.AbstractEnumerator;

/**
 * <!-- begin-user-doc -->
 * A representation of the literals of the enumeration '<em><b>Method Type</b></em>',
 * and utility methods for working with them.
 * <!-- end-user-doc -->
 * @see net.opengis.wps10.Wps10Package#getMethodType()
 * @model extendedMetaData="name='method_._type'"
 * @generated
 */
public final class MethodType extends AbstractEnumerator {
    /**
     * The '<em><b>GET</b></em>' literal value.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of '<em><b>GET</b></em>' literal object isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @see #GET_LITERAL
     * @model
     * @generated
     * @ordered
     */
    public static final int GET = 0;

    /**
     * The '<em><b>POST</b></em>' literal value.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of '<em><b>POST</b></em>' literal object isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @see #POST_LITERAL
     * @model
     * @generated
     * @ordered
     */
    public static final int POST = 1;

    /**
     * The '<em><b>GET</b></em>' literal object.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #GET
     * @generated
     * @ordered
     */
    public static final MethodType GET_LITERAL = new MethodType(GET, "GET", "GET");

    /**
     * The '<em><b>POST</b></em>' literal object.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #POST
     * @generated
     * @ordered
     */
    public static final MethodType POST_LITERAL = new MethodType(POST, "POST", "POST");

    /**
     * An array of all the '<em><b>Method Type</b></em>' enumerators.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private static final MethodType[] VALUES_ARRAY =
        new MethodType[] {
            GET_LITERAL,
            POST_LITERAL,
        };

    /**
     * A public read-only list of all the '<em><b>Method Type</b></em>' enumerators.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public static final List VALUES = Collections.unmodifiableList(Arrays.asList(VALUES_ARRAY));

    /**
     * Returns the '<em><b>Method Type</b></em>' literal with the specified literal value.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public static MethodType get(String literal) {
        for (int i = 0; i < VALUES_ARRAY.length; ++i) {
            MethodType result = VALUES_ARRAY[i];
            if (result.toString().equals(literal)) {
                return result;
            }
        }
        return null;
    }

    /**
     * Returns the '<em><b>Method Type</b></em>' literal with the specified name.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public static MethodType getByName(String name) {
        for (int i = 0; i < VALUES_ARRAY.length; ++i) {
            MethodType result = VALUES_ARRAY[i];
            if (result.getName().equals(name)) {
                return result;
            }
        }
        return null;
    }

    /**
     * Returns the '<em><b>Method Type</b></em>' literal with the specified integer value.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public static MethodType get(int value) {
        switch (value) {
            case GET: return GET_LITERAL;
            case POST: return POST_LITERAL;
        }
        return null;
    }

    /**
     * Only this class can construct instances.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private MethodType(int value, String name, String literal) {
        super(value, name, literal);
    }

} //MethodType
