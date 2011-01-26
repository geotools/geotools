/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2007-2008, Open Source Geospatial Foundation (OSGeo)
 *
 *    This library is free software; you can redistribute it and/or
 *    modify it under the terms of the GNU Lesser General Public
 *    License as published by the Free Software Foundation;
 *    version 2.1 of the License.
 *
 *    This library is distributed in the hope that it will be useful,
 *    but WITHOUT ANY WARRANTY; without even the implied warranty of
 *    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 *    Lesser General Public License for more details.
 */
package org.geotools.util;

import java.util.Set;

import org.geotools.factory.FactoryRegistryException;
import org.geotools.factory.GeoTools;
import org.geotools.factory.Hints;
import org.geotools.metadata.iso.citation.Citations;
import org.opengis.metadata.citation.Citation;
import org.opengis.util.GenericName;


/**
 * This is facade around several constructs used by GeoTools for internal caching.
 * <p>
 * This class provides the following services:
 * <ul>
 *   <li>Access to an implementation of "weak", "all" and "none" implementations of {@link ObjectCache}.</li>
 *   <li>The ability to turn a "code" into a good "key" for use with an ObjectCache.</li>
 *   <li>A Pair data object (think of C STRUCT) for use as a key when storing a value against two objects.</li>
 * </ul>
 *
 * @since 2.5
 * @author Jody Garnett
 * @author Cory Horner
 *
 * @source $URL$
 */
public final class ObjectCaches {
    /**
     * Do not allow instantiation of this class.
     */
    private ObjectCaches() {
    }

    /**
     * A pair of Codes for {@link ObjectCache) to work with.
     * Please be advised that this is a data object:
     *
     * <ul>
     *   <li>equals - is dependent on both source and target being equal.</li>
     *   <li>hashcode - is dependent on the hashCode of source and target.</li>
     * </ul>
     *
     * A Pair is considered ordered:
     *
     * <blockquote><pre>
     * Pair pair1 = new Pair("a","b");
     * Pair pair2 = new Pair("b","a");
     *
     * System.out.println( pair1.equals( pair2 ) ); // prints false
     * </pre></blockquote>
     *
     * {@link #createFromCoordinateReferenceSystemCodes}.
     */
    private static final class Pair {
        private final String source, target;

        public Pair(String source, String target) {
            this.source = source;
            this.target = target;
        }

        public int hashCode() {
            int code = 0;
            if (source!=null) code  = source.hashCode();
            if (target!=null) code += target.hashCode()*37;
            return code;
        }

        public boolean equals(final Object other) {
            if (other instanceof Pair) {
                final Pair that = (Pair) other;
                return Utilities.equals(this.source, that.source) &&
                       Utilities.equals(this.target, that.target);
            }
            return false;
        }

        public String toString() {
            return source + " \u21E8 " + target;
        }
    }

    /**
     * Create a two level cache, operates as a level1 cache that is willing to
     * obtain values from a (usually shared) level2 cache.
     * <p>
     * This functionality is used to tie two ObjectCache implementations together
     * (allowing them to collaborate while focusing on different use cases).
     * The real world example of chaining is in {@link AbstractFindableAuthorityFactory} in which:
     * <ul>
     * <li>create uses: chain( cache, findCache )
     * <li>find uses: chain( findCache, cache )
     * </ul>
     * In this manner the find operation does not upset normal cache. It will not create any
     * objects already present in the cache.
     *
     * @param level1
     * @param level2
     * @return ObjectCache
     */
    public static ObjectCache chain( final ObjectCache level1, final ObjectCache level2 ){
        if ( level1 == level2 ) {
            return level1;
        }
        if( level1 == null ) return level2;
        if( level2 == null ) return level1;
        return new ObjectCache(){
            public void clear() {
                level1.clear();
            }
            public Object get( Object key ) {
                Object value = level1.get( key );
                if( value == null ){
                    Object check = level2.get( key );
                    if( check != null ) {
                        try {
                            level1.writeLock(key);
                            value = level1.peek(key);
                            if( value == null ){
                                level1.put(key, check );
                                value = check;
                            }
                        }
                        finally {
                            level1.writeUnLock(key);
                        }
                    }
                }
                return value;
            }

            public Object peek( Object key ) {
                return level1.peek(key);
            }

            public void put( Object key, Object object ) {
                level1.put(key, object );
            }

            public void writeLock( Object key ) {
                level1.writeLock(key);
            }

            public void writeUnLock( Object key ) {
                level1.writeLock(key);
            }
            
            public Set<Object> getKeys(){
            	return level1.getKeys();
            }
            
            public void remove(Object key){
            	level1.remove(key);
            }
        };
    }
    /**
     * Utility method used to produce cache based on provide Hint
     */
    public static ObjectCache create( Hints hints )
            throws FactoryRegistryException {
        if( hints == null ) hints = GeoTools.getDefaultHints();
        String policy = (String) hints.get(Hints.CACHE_POLICY);
        int limit = Hints.CACHE_LIMIT.toValue(hints);
        return create( policy, limit );
    }
    /**
     * Utility method used to produce an ObjectCache.
     *
     * @param policy One of "weak", "all", "none", "soft"
     * @param size Used to indicate requested size, exact use depends on policy
     * @return A new ObjectCache
     * @see Hints.BUFFER_POLICY
     */
    public static ObjectCache create( String policy, int size ){
        if ("weak".equalsIgnoreCase(policy)) {
            return new WeakObjectCache(0);
        } else if ("all".equalsIgnoreCase(policy)) {
            return new DefaultObjectCache(size);
        } else if ("none".equalsIgnoreCase(policy)) {
            return NullObjectCache.INSTANCE;
        } else if ("fixed".equalsIgnoreCase(policy)) {
            return new FixedSizeObjectCache(size);
        } else if ("soft".equals(policy)){
        	return new SoftObjectCache(size);
        } else {
            return new DefaultObjectCache(size);
        }
    }

    /**
     * Produce a good key based on the privided citaiton and code.
     * You can think of the citation as being "here" and the code being the "what".
     *
     * @param code Code
     * @return A good key for use with ObjectCache
     */
    public static String toKey( Citation citation, String code ){
        code = code.trim();
        final GenericName name = NameFactory.create(code);
        final GenericName scope = name.scope().name();
        if (scope == null) {
            return code;
        }
        if (citation != null && Citations.identifierMatches( citation, scope.toString())) {
            return name.tip().toString().trim();
        }
        return code;
    }

    /**
     * Produce a good key based on a pair of codes.
     *
     * @param code1
     * @param code2
     * @return A object to use as a key
     */
    public static Object toKey( Citation citation, String code1, String code2 ){
        String key1 = toKey( citation, code1 );
        String key2 = toKey( citation, code2 );

        return new Pair( key1, key2 );
    }
}
