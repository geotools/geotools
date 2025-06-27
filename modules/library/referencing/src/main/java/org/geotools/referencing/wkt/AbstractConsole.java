/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2004-2008, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.referencing.wkt;

import java.io.IOException;
import java.io.LineNumberReader;
import java.io.PrintWriter;
import java.io.Writer;
import java.text.Format;
import java.text.ParseException;
import org.geotools.api.referencing.FactoryException;
import org.geotools.api.referencing.crs.CoordinateReferenceSystem;
import org.geotools.api.referencing.operation.MathTransform;
import org.geotools.util.Arguments;
import org.geotools.util.Classes;
import org.geotools.util.logging.Logging;

/**
 * Base class for application performing operations on WKT objects from the command line. Instructions are usually read
 * from the {@linkplain System#in standard input stream} and results sent to the {@linkplain System#out standard output
 * stream}, but those streams can be redirected. The set of allowed instructions depends on the subclass used.
 *
 * @since 2.1
 * @version $Id$
 * @author Martin Desruisseaux (IRD)
 */
public abstract class AbstractConsole implements Runnable {
    /** The input stream, usually the {@linkplain System#in standard one}. */
    protected final LineNumberReader in;

    /** The output stream, usually the {@linkplain System#out standard one}. */
    protected final Writer out;

    /** The error stream, usually the {@linkplain System#err standard one}. */
    protected final PrintWriter err;

    /** The line separator, usually the system default. */
    protected final String lineSeparator;

    /** The WKT parser, usually a {@link Preprocessor} object. */
    protected final Format parser;

    /** The command-line prompt. */
    private String prompt = "crs>";

    /** The last line read, or {@code null} if none. */
    private transient String line;

    /** Set to {@code true} if {@link #stop()} was invoked. */
    private transient volatile boolean stop;

    /**
     * Creates a new console instance using {@linkplain System#in standard input stream}, {@linkplain System#out
     * standard output stream}, {@linkplain System#err error output stream} and the system default line separator.
     *
     * @param parser The WKT parser, usually a {@link Preprocessor} object.
     */
    public AbstractConsole(final Format parser) {
        this(parser, new LineNumberReader(Arguments.getReader(System.in)));
    }

    /**
     * Creates a new console instance using the specified input stream.
     *
     * @param parser The WKT parser, usually a {@link Preprocessor} object.
     * @param in The input stream.
     */
    public AbstractConsole(final Format parser, final LineNumberReader in) {
        this(
                parser,
                in,
                Arguments.getWriter(System.out),
                new PrintWriter(Arguments.getWriter(System.err), true),
                System.getProperty("line.separator", "\n"));
    }

    /**
     * Creates a new console instance using the specified streams and line separator.
     *
     * @param parser The WKT parser, usually a {@link Preprocessor} object.
     * @param in The input stream.
     * @param out The output stream.
     * @param err The error stream.
     * @param lineSeparator The line separator.
     */
    public AbstractConsole(
            final Format parser,
            final LineNumberReader in,
            final Writer out,
            final PrintWriter err,
            final String lineSeparator) {
        this.parser = parser;
        this.in = in;
        this.out = out;
        this.err = err;
        this.lineSeparator = lineSeparator;
    }

    /**
     * Parses the specified text. The default implementation delegates the work to the {@linkplain #parser}.
     *
     * @param text The text, as a name, a WKT to parse, or an authority code.
     * @param type The expected type for the object to be parsed (usually a <code>
     *     {@linkplain CoordinateReferenceSystem}.class</code> or <code>
     *     {@linkplain MathTransform}.class</code>).
     * @return The object.
     * @throws ParseException if parsing the specified WKT failed.
     * @throws FactoryException if the object is not of the expected type.
     */
    public Object parseObject(final String text, final Class type) throws ParseException, FactoryException {
        if (parser instanceof Preprocessor) {
            final Preprocessor parser = (Preprocessor) this.parser;
            parser.offset = line != null ? Math.max(0, line.indexOf(text)) : 0;
            return parser.parseObject(text, type);
        } else {
            return parser.parseObject(text);
        }
    }

    /**
     * Adds a predefined Well Know Text (WKT). The {@code value} argument given to this method can contains itself other
     * definitions specified in some previous calls to this method. This method do nothing if the {@linkplain #parser}
     * is not an instance of {@link Preprocessor}.
     *
     * @param name The name for the definition to be added.
     * @param value The Well Know Text (WKT) represented by the name.
     * @throws IllegalArgumentException if the name is invalid.
     * @throws ParseException if the WKT can't be parsed.
     */
    public void addDefinition(final String name, final String value) throws ParseException {
        if (parser instanceof Preprocessor) {
            ((Preprocessor) parser).addDefinition(name, value);
        }
    }

    /**
     * Load all definitions from the specified stream. Definitions are key-value pairs in the form {@code name = wkt}
     * (without the {@code SET} keyword). The result is the same than invoking the {@code SET} instruction for each line
     * in the specified stream. This method is used for loading predefined objects like the database used by
     * {@link org.geotools.referencing.factory.PropertyAuthorityFactory}.
     *
     * @param in The input stream.
     * @throws IOException if an input operation failed.
     * @throws ParseException if a well know text (WKT) can't be parsed.
     */
    public void loadDefinitions(final LineNumberReader in) throws IOException, ParseException {
        while ((line = readLine(in)) != null) {
            String name = line, value = null;
            final int i = line.indexOf('=');
            if (i >= 0) {
                name = line.substring(0, i).trim();
                value = line.substring(i + 1).trim();
            }
            addDefinition(name, value);
        }
    }

    /**
     * Prints to the {@linkplain #out output stream} a table of all definitions. The content of this table is inferred
     * from the values given to the {@link #addDefinition} method. This method print nothing if the {@linkplain #parser}
     * is not an instance of {@link Preprocessor}.
     *
     * @throws IOException if an error occured while writting to the output stream.
     */
    public void printDefinitions() throws IOException {
        if (parser instanceof Preprocessor) {
            ((Preprocessor) parser).printDefinitions(out);
        }
    }

    /** Returns the command-line prompt, or {@code null} if there is none. */
    public String getPrompt() {
        return prompt;
    }

    /** Set the command-line prompt, or {@code null} for none. */
    public void setPrompt(final String prompt) {
        this.prompt = prompt;
    }

    /**
     * Read the next line from the specified input stream. Empty lines and comment lines are skipped. If there is no
     * more line to read, then this method returns {@code null}.
     *
     * @param in The input stream to read from.
     * @return The next non-empty and non-commented line, or {@code null} if none.
     * @throws IOException if the reading failed.
     */
    private static String readLine(final LineNumberReader in) throws IOException {
        String line;
        while ((line = in.readLine()) != null) {
            line = line.trim();
            if (line.length() == 0) {
                // Ignore empty lines.
                continue;
            }
            if (line.startsWith("//")) {
                // Ignore comment lines.
                continue;
            }
            break;
        }
        return line;
    }

    /**
     * Process instructions from the {@linkplain #in input stream} specified at construction time. All lines are read
     * until the end of stream ({@code [Ctrl-Z]} for input from the keyboard), or until {@link #stop()} is invoked.
     * Non-empty and non-comment lines are given to the {@link #execute} method. Errors are catched and printed to the
     * {@linkplain #err error stream}.
     */
    @Override
    public void run() {
        try {
            while (!stop) {
                if (prompt != null) {
                    out.write(prompt);
                }
                out.flush();
                line = readLine(in);
                if (line == null) {
                    break;
                }
                try {
                    execute(line);
                } catch (Exception exception) {
                    reportError(exception);
                }
            }
            out.flush();
            stop = false;
        } catch (IOException exception) {
            reportError(exception);
        }
    }

    /**
     * Executes all instructions (like {@link #run}), but stop at the first error.
     *
     * @throws Exception if an instruction failed.
     */
    public void executeAll() throws Exception {
        while ((line = readLine(in)) != null) {
            try {
                execute(line);
                out.flush();
            } catch (Exception e) {
                reportError(e);
                throw e;
            }
        }
    }

    /**
     * Execute the specified instruction.
     *
     * @param instruction The instruction to execute.
     * @throws Exception if the instruction failed.
     */
    protected abstract void execute(String instruction) throws Exception;

    /**
     * Stops the {@link #run} method. This method can been invoked from any thread. If a line is in process, it will be
     * finished before the {@link #run} method stops.
     */
    public void stop() {
        this.stop = true;
    }

    /**
     * Print an exception message to the {@linkplain System#err standard error stream}. The error message includes the
     * line number, and the column where the failure occured in the exception is an instance of {@link ParseException}.
     *
     * @param exception The exception to report.
     * @todo Localize
     */
    protected void reportError(final Exception exception) {
        try {
            out.flush();
        } catch (IOException ignore) {
            Logging.unexpectedException(AbstractConsole.class, "reportError", ignore);
        }
        err.print(Classes.getShortClassName(exception));
        err.print(" at line ");
        err.print(in.getLineNumber());
        Throwable cause = exception;
        while (true) {
            String message = cause.getLocalizedMessage();
            if (message != null) {
                err.print(": ");
                err.print(message);
            }
            err.println();
            cause = cause.getCause();
            if (cause == null) {
                break;
            }
            err.print("Caused by ");
            err.print(Classes.getShortClassName(cause));
        }
        err.println("Type 'stacktrace' for stack trace information.");
        if (line != null && exception instanceof ParseException) {
            AbstractParser.reportError(err, line, ((ParseException) exception).getErrorOffset());
        }
        err.println();
    }
}
