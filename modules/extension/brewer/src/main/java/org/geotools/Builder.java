package org.geotools;

/**
 * Builder interface used to impose consistency on Builder implementations.
 * 
 * @param <T>
 *            class of object under construction
 *
 *
 * @source $URL$
 */
public interface Builder<T> {
    /**
     * Configure the Builder to produce <code>null</code>.
     * <p>
     * This method allows a Builder to be used as a placeholder; in its
     * unset state the build() method will produce <code>null</code>. If
     * any of the builder methods are used the builder will produce a
     * result.
     * 
     * @return Builder configured to build <code>null</code>
     */
    Builder<T> unset();
    /**
     * Configure the Builder to produce a default result.
     * @return Builder configured to produce a default result.
     */
    Builder<T> reset();
    /**
     * Configure the Builder to produce a copy of the provided original.
     * @param origional Original, if null this will behave the same as unset()
     * @return Builder configured to produce the provided original
     */
    Builder<T> reset( T original );    

    /**
     * Created object, may be null if unset()
     * @return Created object may be null if unset()
     */
    T build();
}
