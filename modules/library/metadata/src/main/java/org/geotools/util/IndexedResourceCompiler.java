/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2001-2008, Open Source Geospatial Foundation (OSGeo)
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

import java.io.BufferedOutputStream;
import java.io.BufferedWriter;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.lang.reflect.Field;
import java.nio.charset.StandardCharsets;
import java.text.MessageFormat;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * Resource compiler. This class is run from the command line at compile time only. {@code IndexedResourceCompiler}
 * scans for {@code .properties} files and copies their content to {@code .utf} files using UTF8 encoding. It also
 * checks for key validity and checks values for {@link MessageFormat} compatibility. Finally, it creates a
 * {@code FooKeys.java} source file declaring resource keys as integer constants.
 *
 * <p>This class <strong>must</strong> be run from the maven root of Geotools project.
 *
 * <p>{@code IndexedResourceCompiler} and all {@code FooKeys} classes don't need to be included in the final JAR file.
 * They are used at compile time only and no other classes should keep reference to them.
 *
 * @since 2.4
 * @version $Id$
 * @author Martin Desruisseaux (IRD)
 */
public final class IndexedResourceCompiler implements Comparator<Object> {
    /**
     * The base directory for {@code "java"} {@code "resources"} sub-directories. The directory structure must be
     * consistent with Maven conventions.
     *
     * @see #sourceDirectory
     */
    private static final File SOURCE_DIRECTORY = new File("./src/main");

    /** The resources to process. */
    @SuppressWarnings({"unchecked", "PMD.UseShortArrayInitializer"})
    private static final Class<? extends IndexedResourceBundle>[] RESOURCES_TO_PROCESS = new Class[] {
        org.geotools.metadata.i18n.Descriptions.class,
        org.geotools.metadata.i18n.Vocabulary.class,
        org.geotools.metadata.i18n.Loggings.class
    };

    /** Extension for properties source files. Must be in the {@code ${sourceDirectory}/java} directory. */
    private static final String PROPERTIES_EXT = ".properties";

    /** Extension for resource target files. Will be be in the {@code ${sourceDirectory}/resources} directory. */
    private static final String RESOURCES_EXT = ".utf";

    /**
     * Prefix for argument count in resource key names. For example, a resource expecting one argument may have a key
     * name like "HELLO_$1".
     */
    private static final String ARGUMENT_COUNT_PREFIX = "_$";

    /** The maximal length of comment lines. */
    private static final int COMMENT_LENGTH = 92;

    /**
     * The base directory for {@code "java"} {@code "resources"} sub-directories. The directory structure must be
     * consistent with Maven conventions.
     */
    private final File sourceDirectory;

    /** Integer IDs allocated to resource keys. This map will be shared for all languages of a given resource bundle. */
    private final Map<Integer, String> allocatedIDs = new HashMap<>();

    /** Resource keys and their localized values. This map will be cleared for each language in a resource bundle. */
    private final Map<Object, Object> resources = new HashMap<>();

    /** The output stream for printing message. */
    private final PrintWriter out;

    /**
     * Constructs a new {@code IndexedResourceCompiler}. This method will immediately look for a {@code FooKeys.class}
     * file. If one is found, integer keys are loaded in order to reuse the same values.
     *
     * @param sourceDirectory The base directory for {@code "java"} {@code "resources"} sub-directories. The directory
     *     structure must be consistent with Maven conventions.
     * @param bundleClass The resource bundle base class (e.g. <code>
     *     {@linkplain org.geotools.metadata.i18n.Vocabulary}.class}</code>).
     * @param renumber {@code true} for renumbering all key values.
     * @param out The output stream for printing message.
     * @throws IOException if an input/output operation failed.
     */
    private IndexedResourceCompiler(
            final File sourceDirectory,
            final Class<? extends IndexedResourceBundle> bundleClass,
            final boolean renumber,
            final PrintWriter out)
            throws IOException {
        this.sourceDirectory = sourceDirectory;
        this.out = out;
        if (!renumber)
            try {
                final String classname = toKeyClass(bundleClass.getName());
                final Field[] fields = Class.forName(classname).getFields();
                out.print("Loading ");
                out.println(classname);
                /*
                 * Copies all fields into {@link #allocatedIDs} map.
                 */
                Field.setAccessible(fields, true);
                for (int i = fields.length; --i >= 0; ) {
                    final Field field = fields[i];
                    final String key = field.getName();
                    try {
                        final Object ID = field.get(null);
                        if (ID instanceof Integer) {
                            allocatedIDs.put((Integer) ID, key);
                        }
                    } catch (IllegalAccessException exception) {
                        final File source = new File(classname.replace('.', '/') + ".class");
                        warning(source, key, "Access denied", exception);
                    }
                }
            } catch (ClassNotFoundException exception) {
                /*
                 * 'VocabularyKeys.class' doesn't exist. This is okay (probably normal).
                 * We will create 'VocabularyKeys.java' later using automatic key values.
                 */
            }
    }

    /**
     * Returns the class name for the keys. For example if {@code bundleClass} is
     * {@code "org.geotools.metadata.i18n.Vocabulary"}, then this method returns
     * {@code "org.geotools.metadata.i18n.VocabularyKeys"}.
     */
    private static String toKeyClass(String bundleClass) {
        if (bundleClass.endsWith("s")) {
            bundleClass = bundleClass.substring(0, bundleClass.length() - 1);
        }
        return bundleClass + "Keys";
    }

    /** Load the specified property file. */
    private static Properties loadPropertyFile(final File file) throws IOException {
        try (InputStream input = new FileInputStream(file)) {
            final Properties properties = new Properties();
            properties.load(input);
            return properties;
        }
    }

    /**
     * Loads all properties from a {@code .properties} file. Resource keys are checked for naming conventions (i.e.
     * resources expecting some arguments must have a key name ending with {@code "_$n"} where {@code "n"} is the number
     * of arguments). This method transforms resource values into legal {@link MessageFormat} patterns when necessary.
     *
     * @param file The properties file to read.
     * @throws IOException if an input/output operation failed.
     */
    private void processPropertyFile(final File file) throws IOException {
        final Properties properties = loadPropertyFile(file);
        resources.clear();
        for (final Map.Entry<Object, Object> entry : properties.entrySet()) {
            final String key = (String) entry.getKey();
            final String value = (String) entry.getValue();
            /*
             * Checks key and value validity.
             */
            if (key.trim().length() == 0) {
                warning(file, key, "Empty key.", null);
                continue;
            }
            if (value.trim().length() == 0) {
                warning(file, key, "Empty value.", null);
                continue;
            }
            /*
             * Checks if the resource value is a legal MessageFormat pattern.
             */
            final MessageFormat message;
            try {
                message = new MessageFormat(toMessageFormatString(value));
            } catch (IllegalArgumentException exception) {
                warning(file, key, "Bad resource value", exception);
                continue;
            }
            /*
             * Checks if the expected arguments count (according to naming conventions)
             * matches the arguments count found in the MessageFormat pattern.
             */
            final int argumentCount;
            final int index = key.lastIndexOf(ARGUMENT_COUNT_PREFIX);
            if (index < 0) {
                argumentCount = 0;
                resources.put(key, value); // Text will not be formatted using MessageFormat.
            } else
                try {
                    String suffix = key.substring(index + ARGUMENT_COUNT_PREFIX.length());
                    argumentCount = Integer.parseInt(suffix);
                    resources.put(key, message.toPattern());
                } catch (NumberFormatException exception) {
                    warning(file, key, "Bad number in resource key", exception);
                    continue;
                }
            final int expected = message.getFormats().length;
            if (argumentCount != expected) {
                final String suffix = ARGUMENT_COUNT_PREFIX + expected;
                warning(file, key, "Key name should ends with \"" + suffix + "\".", null);
                continue;
            }
        }
        /*
         * Allocates an ID for each new key.
         */
        final String[] keys = resources.keySet().toArray(new String[resources.size()]);
        Arrays.sort(keys, this);
        int freeID = 0;
        for (final String key : keys) {
            if (!allocatedIDs.containsValue(key)) {
                Integer ID;
                do {
                    ID = freeID++;
                } while (allocatedIDs.containsKey(ID));
                allocatedIDs.put(ID, key);
            }
        }
    }

    /**
     * Write UTF file. Method {@link #processPropertyFile} should be invoked beforehand to {@code writeUTFFile}.
     *
     * @param file The destination file.
     * @throws IOException if an input/output operation failed.
     */
    private void writeUTFFile(final File file) throws IOException {
        final int count = allocatedIDs.isEmpty() ? 0 : Collections.max(allocatedIDs.keySet()) + 1;
        try (DataOutputStream out = new DataOutputStream(new BufferedOutputStream(new FileOutputStream(file)))) {
            out.writeInt(count);
            for (int i = 0; i < count; i++) {
                final String value = (String) resources.get(allocatedIDs.get(i));
                out.writeUTF((value != null) ? value : "");
            }
        }
    }

    /**
     * Changes a "normal" text string into a pattern compatible with {@link MessageFormat}. The main operation consists
     * of changing ' for '', except for '{' and '}' strings.
     */
    private static String toMessageFormatString(final String text) {
        int level = 0;
        int last = -1;
        final StringBuilder buffer = new StringBuilder(text);
        search:
        for (int i = 0; i < buffer.length(); i++) { // Length of 'buffer' will vary.
            switch (buffer.charAt(i)) {
                    /*
                     * Left and right braces take us up or down a level.  Quotes will only be doubled
                     * if we are at level 0.  If the brace is between quotes it will not be taken into
                     * account as it will have been skipped over during the previous pass through the
                     * loop.
                     */
                case '{':
                    level++;
                    last = i;
                    break;
                case '}':
                    level--;
                    last = i;
                    break;
                case '\'': {
                    /*
                     * If a brace ('{' or '}') is found between quotes, the entire block is
                     * ignored and we continue with the character following the closing quote.
                     */
                    if (i + 2 < buffer.length() && buffer.charAt(i + 2) == '\'') {
                        switch (buffer.charAt(i + 1)) {
                            case '{':
                                i += 2;
                                continue search;
                            case '}':
                                i += 2;
                                continue search;
                        }
                    }
                    if (level <= 0) {
                        /*
                         * If we weren't between braces, we must double the quotes.
                         */
                        buffer.insert(i++, '\'');
                        continue search;
                    }
                    /*
                     * If we find ourselves between braces, we don't normally need to double
                     * our quotes.  However, the format {0,choice,...} is an exception.
                     */
                    if (last >= 0 && buffer.charAt(last) == '{') {
                        int scan = last;
                        do if (scan >= i) continue search;
                        while (Character.isDigit(buffer.charAt(++scan)));
                        final String choice = ",choice,";
                        final int end = scan + choice.length();
                        if (end < buffer.length() && buffer.substring(scan, end).equalsIgnoreCase(choice)) {
                            buffer.insert(i++, '\'');
                            continue search;
                        }
                    }
                }
            }
        }
        return buffer.toString();
    }

    /**
     * Prints a message to the output stream.
     *
     * @param file File that produced the error, or {@code null} if none.
     * @param key Resource key that produced the error, or {@code null} if none.
     * @param message The message string.
     * @param exception An optional exception that is the cause of this warning.
     */
    private void warning(final File file, final String key, final String message, final Exception exception) {
        out.print("ERROR ");
        if (file != null) {
            String filename = file.getPath();
            if (filename.endsWith(PROPERTIES_EXT)) {
                filename = filename.substring(0, filename.length() - PROPERTIES_EXT.length());
            }
            out.print('(');
            out.print(filename);
            out.print(')');
        }
        out.print(": ");
        if (key != null) {
            out.print('"');
            out.print(key);
            out.print('"');
        }
        out.println();
        out.print(message);
        if (exception != null) {
            out.print(": ");
            out.print(exception.getLocalizedMessage());
        }
        out.println();
        out.println();
        out.flush();
    }

    /**
     * Creates a source file for resource keys.
     *
     * @param bundleClass The resource bundle base class (e.g. <code>
     *     {@linkplain org.geotools.metadata.i18n.Vocabulary}.class}</code>).
     * @throws IOException if an input/output operation failed.
     */
    private void writeJavaSource(final Class bundleClass) throws IOException {
        final String fullname = toKeyClass(bundleClass.getName());
        final int packageEnd = fullname.lastIndexOf('.');
        final String packageName = fullname.substring(0, packageEnd);
        final String classname = fullname.substring(packageEnd + 1);
        final File file = new File(sourceDirectory, "java/" + fullname.replace('.', '/') + ".java");
        if (!file.getParentFile().isDirectory()) {
            warning(file, null, "Parent directory not found.", null);
            return;
        }
        try (BufferedWriter out =
                new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file), StandardCharsets.UTF_8))) {
            out.write("/*\n"
                    + " *    GeoTools - The Open Source Java GIS Toolkit\n"
                    + " *    http://geotools.org\n"
                    + " *    \n"
                    + " *    (C) 2003-2008, Open Source Geospatial Foundation (OSGeo)\n"
                    + " *    \n"
                    + " *    This library is free software; you can redistribute it and/or\n"
                    + " *    modify it under the terms of the GNU Lesser General Public\n"
                    + " *    License as published by the Free Software Foundation;\n"
                    + " *    version 2.1 of the License.\n"
                    + " *    \n"
                    + " *    This library is distributed in the hope that it will be useful,\n"
                    + " *    but WITHOUT ANY WARRANTY; without even the implied warranty of\n"
                    + " *    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU\n"
                    + " *    Lesser General Public License for more details.\n"
                    + " *    \n"
                    + " *    THIS IS AN AUTOMATICALLY GENERATED FILE. DO NOT EDIT!\n"
                    + " *    Generated with: org.geotools.resources.IndexedResourceCompiler\n"
                    + " */\n");
            out.write("package ");
            out.write(packageName);
            out.write(";\n\n\n");
            out.write("/**\n"
                    + " * Resource keys. This class is used when compiling sources, but\n"
                    + " * no dependencies to {@code ResourceKeys} should appear in any\n"
                    + " * resulting class files.  Since Java compiler inlines final integer\n"
                    + " * values, using long identifiers will not bloat constant pools of\n"
                    + " * classes compiled against the interface, provided that no class\n"
                    + " * implements this interface.\n"
                    + " *\n"
                    + " * @see org.geotools.resources.IndexedResourceBundle\n"
                    + " * @see org.geotools.resources.IndexedResourceCompiler\n"
                    + " */\n");
            out.write("public final class ");
            out.write(classname);
            out.write(" {\n");
            out.write("    private ");
            out.write(classname);
            out.write("() {\n");
            out.write("    }\n");
            final Map.Entry[] entries = allocatedIDs.entrySet().toArray(new Map.Entry[allocatedIDs.size()]);
            Arrays.sort(entries, this);
            for (Map.Entry entry : entries) {
                out.write('\n');
                final String key = (String) entry.getValue();
                final String ID = entry.getKey().toString();
                String message = (String) resources.get(key);
                if (message != null) {
                    out.write("    /**\n");
                    while ((message = message.trim()).length() != 0) {
                        out.write("     * ");
                        int stop = message.length();
                        if (stop > COMMENT_LENGTH) {
                            stop = COMMENT_LENGTH;
                            while (stop > 20 && !Character.isWhitespace(message.charAt(stop))) {
                                stop--;
                            }
                        }
                        out.write(message.substring(0, stop).trim());
                        out.write('\n');
                        message = message.substring(stop);
                    }
                    out.write("     */\n");
                }
                out.write("    public static final int ");
                out.write(key);
                out.write(" = ");
                out.write(ID);
                out.write(";\n");
            }
            out.write("}\n");
        }
    }

    /**
     * Compares two resource keys. Object {@code o1} and {@code o2} are usually {@link String} objects representing
     * resource keys (for example, "{@code MISMATCHED_DIMENSION}"), but may also be {@link java.util.Map.Entry}.
     */
    @Override
    public int compare(Object o1, Object o2) {
        if (o1 instanceof Map.Entry) o1 = ((Map.Entry) o1).getValue();
        if (o2 instanceof Map.Entry) o2 = ((Map.Entry) o2).getValue();
        final String key1 = (String) o1;
        final String key2 = (String) o2;
        return key1.compareTo(key2);
    }

    /**
     * Scans the package for resources.
     *
     * @param sourceDirectory The base directory for {@code "java"} {@code "resources"} sub-directories. The directory
     *     structure must be consistent with Maven conventions.
     * @param bundleClass The resource bundle base class (e.g. <code>
     *     {@linkplain org.geotools.metadata.i18n.Vocabulary}.class}</code>).
     * @param renumber {@code true} for renumbering all key values.
     * @param out The output stream for printing message.
     * @throws IOException if an input/output operation failed.
     */
    private static void scanForResources(
            final File sourceDirectory,
            final Class<? extends IndexedResourceBundle> bundleClass,
            final boolean renumber,
            final PrintWriter out)
            throws IOException {
        final String fullname = bundleClass.getName();
        final int packageEnd = fullname.lastIndexOf('.');
        final String packageName = fullname.substring(0, packageEnd);
        final String classname = fullname.substring(packageEnd + 1);
        final String packageDir = packageName.replace('.', '/');
        final File srcDir = new File(sourceDirectory, "java/" + packageDir);
        final File utfDir = new File(sourceDirectory, "resources/" + packageDir);
        if (!srcDir.isDirectory()) {
            out.print('"');
            out.print(srcDir.getPath());
            out.println("\" is not a directory.");
            return;
        }
        if (!utfDir.isDirectory()) {
            out.print('"');
            out.print(utfDir.getPath());
            out.println("\" is not a directory.");
            return;
        }
        IndexedResourceCompiler compiler = null;
        final File[] content = srcDir.listFiles();
        File defaultLanguage = null;
        if (content != null) {
            for (final File file : content) {
                final String filename = file.getName();
                if (filename.startsWith(classname) && filename.endsWith(PROPERTIES_EXT)) {
                    if (compiler == null) {
                        compiler = new IndexedResourceCompiler(sourceDirectory, bundleClass, renumber, out);
                    }
                    compiler.processPropertyFile(file);
                    final String noExt = filename.substring(0, filename.length() - PROPERTIES_EXT.length());
                    final File utfFile = new File(utfDir, noExt + RESOURCES_EXT);
                    compiler.writeUTFFile(utfFile);
                    if (noExt.equals(classname)) {
                        defaultLanguage = file;
                    }
                }
            }
        }
        if (compiler != null) {
            if (defaultLanguage != null) {
                compiler.resources.clear();
                compiler.resources.putAll(loadPropertyFile(defaultLanguage));
            }
            compiler.writeJavaSource(bundleClass);
        }
    }

    /**
     * Run the resource compiler.
     *
     * @param args The command-line arguments.
     * @param sourceDirectory The base directory for {@code "java"} {@code "resources"} sub-directories. The directory
     *     structure must be consistent with Maven conventions.
     * @param resourcesToProcess The resource bundle base classes (e.g. <code>
     *     {@linkplain org.geotools.metadata.i18n.Vocabulary}.class}</code>).
     */
    @SuppressWarnings("PMD.CloseResource")
    public static void main(
            String[] args,
            final File sourceDirectory,
            final Class<? extends IndexedResourceBundle>[] resourcesToProcess) {
        final Arguments arguments = new Arguments(args);
        final boolean renumber = arguments.getFlag("-renumber");
        final PrintWriter out = arguments.out;
        arguments.getRemainingArguments(0);
        if (!sourceDirectory.isDirectory()) {
            out.print(sourceDirectory);
            out.println(" not found or is not a directory.");
            return;
        }
        for (Class<? extends IndexedResourceBundle> toProcess : resourcesToProcess) {
            try {
                scanForResources(sourceDirectory, toProcess, renumber, out);
            } catch (IOException exception) {
                out.println(exception.getLocalizedMessage());
            }
        }
        out.flush();
    }

    /** Run the compiler for GeoTools resources. */
    public static void main(final String... args) {
        main(args, SOURCE_DIRECTORY, RESOURCES_TO_PROCESS);
    }
}
