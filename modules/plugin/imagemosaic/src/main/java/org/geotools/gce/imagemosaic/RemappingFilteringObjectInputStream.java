/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2025, Open Source Geospatial Foundation (OSGeo)
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

package org.geotools.gce.imagemosaic;

import java.io.IOException;
import java.io.InputStream;
import java.io.InvalidClassException;
import java.io.ObjectInputFilter;
import java.io.ObjectInputStream;
import java.io.ObjectStreamClass;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Predicate;
import java.util.regex.Pattern;

public final class RemappingFilteringObjectInputStream extends ObjectInputStream {
    private final Map<String, String> remap; // exact class name -> new name
    private final List<PackageRule> pkgRules; // ordered prefix remaps
    private final Predicate<String> validator;
    private final ClassLoader loader;

    private static final class PackageRule {
        final String oldPrefix; // e.g., "old.pkg.images"
        final String newPrefix; // e.g., "new.pkg.images"

        PackageRule(String o, String n) {
            this.oldPrefix = o;
            this.newPrefix = n;
        }
    }

    private RemappingFilteringObjectInputStream(
            InputStream in,
            Map<String, String> remap,
            List<PackageRule> pkgRules,
            Predicate<String> validator,
            ObjectInputFilter jep290Filter)
            throws IOException {
        super(in);
        this.remap = remap.isEmpty() ? Map.of() : new HashMap<>(remap);
        this.pkgRules = List.copyOf(pkgRules);
        this.validator = (validator != null) ? validator : name -> true;
        this.loader = Thread.currentThread().getContextClassLoader();
        if (jep290Filter != null) {
            setObjectInputFilter(info -> {
                ObjectInputFilter.Status s = jep290Filter.checkInput(info);
                return (s == ObjectInputFilter.Status.UNDECIDED) ? ObjectInputFilter.Status.UNDECIDED : s;
            });
        }
    }

    private void requireValid(String original) throws InvalidClassException {
        if (!validator.test(original)) throw new InvalidClassException("Rejected by validation: " + original);
    }

    private Class<?> load(String fqcn) throws ClassNotFoundException {
        try {
            return Class.forName(fqcn, false, loader);
        } catch (ClassNotFoundException e) {
            return Class.forName(fqcn, false, ClassLoader.getSystemClassLoader());
        }
    }

    /* ---------- name mapping (exact > package; array-aware) ---------- */

    private String mapNonArray(String name) {
        // exact class remap first
        String exact = remap.get(name);
        if (exact != null) return exact;

        // package prefix remap (ordered; first match wins)
        for (PackageRule r : pkgRules) {
            String op = r.oldPrefix;
            if (name.equals(op) || name.startsWith(op + ".")) {
                return r.newPrefix + name.substring(op.length());
            }
        }
        return name;
    }

    private String mapAny(String original) {
        if (!original.startsWith("[")) {
            // regular or inner class (inners have $ in the name)
            return mapNonArray(original);
        }

        // Array types: JVM descriptors like "[I", "[[Z", "[Lpkg.Clz;"
        // Primitive arrays pass through unchanged
        if (original.matches("^\\[+[BCDFIJSZV]$")) return original;

        // Object array: peel the "[L ... ;"
        // Count leading '[' to preserve dims
        int i = 0;
        while (i < original.length() && original.charAt(i) == '[') i++;
        // now expect 'L...;'
        String elem = original.substring(i + 1, original.endsWith(";") ? original.length() - 1 : original.length());
        String mappedElem = mapNonArray(elem);
        StringBuilder sb = new StringBuilder(i + 2 + mappedElem.length());
        for (int k = 0; k < i; k++) sb.append('[');
        sb.append('L').append(mappedElem).append(';');
        return sb.toString();
    }

    @Override
    protected Class<?> resolveClass(ObjectStreamClass desc) throws IOException, ClassNotFoundException {
        String original = desc.getName(); // binary name (e.g., a.b.Outer$Inner)
        requireValid(original); // validate pre-remap
        String mapped = mapAny(original); // exact or package map (array-aware)
        return load(mapped);
    }

    @Override
    @SuppressWarnings("deprecation")
    protected Class<?> resolveProxyClass(String[] interfaces) throws IOException, ClassNotFoundException {
        Class<?>[] ifaces = new Class<?>[interfaces.length];
        for (int i = 0; i < interfaces.length; i++) {
            String original = interfaces[i];
            requireValid(original); // validate pre-remap
            String mapped = mapAny(original); // handle package rules for interfaces, too
            ifaces[i] = load(mapped);
        }
        try {
            return java.lang.reflect.Proxy.getProxyClass(loader, ifaces);
        } catch (IllegalArgumentException e) {
            return java.lang.reflect.Proxy.getProxyClass(ClassLoader.getSystemClassLoader(), ifaces);
        }
    }

    // helper: did mapping change? (non-array only)
    private boolean isRemappedNonArray(String name) {
        if (name.startsWith("[")) return false;
        if (remap.containsKey(name)) return true;
        for (PackageRule r : pkgRules) {
            String op = r.oldPrefix;
            if (name.equals(op) || name.startsWith(op + ".")) return true;
        }
        return false;
    }

    @Override
    protected ObjectStreamClass readClassDescriptor() throws IOException, ClassNotFoundException {
        ObjectStreamClass streamDesc = super.readClassDescriptor();
        String original = streamDesc.getName(); // binary name from the stream

        // Always validate the ORIGINAL name first
        requireValid(original);

        // If enabled and this class will be remapped, replace descriptor with LOCAL one
        if (isRemappedNonArray(original)) {
            String mapped = mapAny(original); // apply exact or package mapping
            try {
                Class<?> target = load(mapped);
                ObjectStreamClass localDesc = ObjectStreamClass.lookup(target);
                if (localDesc != null) {
                    return localDesc; // SUID now matches local class
                }
            } catch (ClassNotFoundException ignore) {
                // fall through to use the stream's descriptor
            }
        }
        return streamDesc;
    }

    /* -------------------------- Builder -------------------------- */

    public static Builder builder() {
        return new Builder();
    }

    public static final class Builder {
        private InputStream in;
        private final Set<String> allowedNames = new HashSet<>();
        private final List<java.util.regex.Pattern> allowedPatterns = new ArrayList<>();
        private final Set<String> allowedPrimitives =
                new HashSet<>(Set.of("boolean", "byte", "short", "char", "int", "long", "float", "double", "void"));
        private final Map<String, String> remap = new HashMap<>();
        private final List<PackageRule> pkgRules = new ArrayList<>();
        private ObjectInputFilter jep290Filter;

        public Builder input(InputStream in) {
            this.in = in;
            return this;
        }

        // Validation (same as before)
        public Builder accept(String... names) {
            if (names == null || names.length == 0) return this;

            Collections.addAll(this.allowedNames, names);
            return this;
        }

        public Builder accept(Class<?>... classes) {
            if (classes == null || classes.length == 0) return this;

            for (Class<?> c : classes) {
                if (c.isPrimitive()) allowedPrimitives.add(c.getName());
                allowedNames.add(c.getName());
            }
            return this;
        }

        public Builder accept(Pattern pattern) {
            if (pattern == null) return this;

            this.allowedPatterns.add(pattern);
            return this;
        }

        public Builder acceptPattern(String... regexes) {
            if (regexes == null || regexes.length == 0) return this;

            for (String r : regexes) allowedPatterns.add(java.util.regex.Pattern.compile(r));
            return this;
        }

        public Builder acceptPrimitives(String... prims) {
            Collections.addAll(this.allowedPrimitives, prims);
            return this;
        }

        // Remapping
        public Builder remap(String oldName, String newName) {
            remap.put(oldName, newName);
            return this;
        }
        /** Remap an entire package (and its subpackages/classes). Use binary package names, e.g., "old.pkg.images". */
        public Builder remapPackage(String oldPkg, String newPkg) {
            pkgRules.add(new PackageRule(oldPkg, newPkg));
            return this;
        }

        public Builder objectInputFilter(ObjectInputFilter f) {
            this.jep290Filter = f;
            return this;
        }

        private Predicate<String> buildValidator() {
            Predicate<String> p = allowedNames::contains;
            p = p.or(allowedPrimitives::contains);
            if (!allowedPatterns.isEmpty()) {
                p = p.or(name -> {
                    for (java.util.regex.Pattern pat : allowedPatterns)
                        if (pat.matcher(name).matches()) return true;
                    return false;
                });
            }
            // Arrays accepted if element type is accepted (by exact/prims/pattern)
            p = p.or(name -> {
                if (!name.startsWith("[")) return false;
                if (name.matches("^\\[+[BCDFIJSZV]$")) return true; // primitive arrays
                if (name.matches("^\\[+L.+;+$")) {
                    String elem = name.replaceFirst("^\\[+L", "").replaceAll(";+$", "");
                    if (allowedNames.contains(elem) || allowedPrimitives.contains(elem)) return true;
                    for (java.util.regex.Pattern pat : allowedPatterns)
                        if (pat.matcher(elem).matches()) return true;
                }
                return false;
            });
            return p;
        }

        public RemappingFilteringObjectInputStream build() throws IOException {
            if (in == null) throw new IllegalStateException("input(InputStream) is required");
            return new RemappingFilteringObjectInputStream(in, remap, pkgRules, buildValidator(), jep290Filter);
        }
    }
}
