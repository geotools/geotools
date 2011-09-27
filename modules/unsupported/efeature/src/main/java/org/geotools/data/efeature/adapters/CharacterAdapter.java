package org.geotools.data.efeature.adapters;

import org.eclipse.emf.query.conditions.Condition;
import org.eclipse.emf.query.conditions.IDataTypeAdapter;

/**
 * An Adapter class to be used to extract from -adapt- the argument object to some {@link Character}
 * value that would later be used in <code>Condition</code> evaluation.
 * 
 * Clients can subclass it and provide their own implementation
 * 
 * @see {@link Condition}
 *
 * @source $URL$
 */
public abstract class CharacterAdapter implements IDataTypeAdapter<Character> {

    /**
     * The simplest {@link CharacterAdapter} implementation that represents the case when the
     * argument object to adapt is a {@link Character} object itself.
     */
    public static final CharacterAdapter DEFAULT = new CharacterAdapter() {

        @Override
        public Character toCharacter(Object object) {
            return (Character) object;
        }

    };

    /**
     * Extracts from/Adapts the argument object to a {@link Character}
     * 
     * @param object - the argument object to adapt to a {@link Character} by this adapter
     * @return the {@link Character} object representation of the argument object
     */
    public abstract Character toCharacter(Object object);

    @Override
    public Character adapt(Object value) {
        return toCharacter(value);
    }

}
