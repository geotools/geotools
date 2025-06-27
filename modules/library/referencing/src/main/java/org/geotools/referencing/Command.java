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
package org.geotools.referencing;

import java.io.IOException;
import java.io.PrintWriter;
import java.text.NumberFormat;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Locale;
import java.util.Set;
import org.geotools.api.metadata.Identifier;
import org.geotools.api.metadata.citation.Citation;
import org.geotools.api.referencing.AuthorityFactory;
import org.geotools.api.referencing.FactoryException;
import org.geotools.api.referencing.IdentifiedObject;
import org.geotools.api.referencing.NoSuchAuthorityCodeException;
import org.geotools.api.referencing.crs.CRSAuthorityFactory;
import org.geotools.api.referencing.crs.CoordinateReferenceSystem;
import org.geotools.api.referencing.operation.CoordinateOperation;
import org.geotools.api.referencing.operation.CoordinateOperationAuthorityFactory;
import org.geotools.api.referencing.operation.CoordinateOperationFactory;
import org.geotools.api.referencing.operation.OperationNotFoundException;
import org.geotools.api.util.InternationalString;
import org.geotools.metadata.i18n.Vocabulary;
import org.geotools.metadata.i18n.VocabularyKeys;
import org.geotools.referencing.datum.BursaWolfParameters;
import org.geotools.referencing.datum.DefaultGeodeticDatum;
import org.geotools.referencing.factory.AbstractAuthorityFactory;
import org.geotools.referencing.factory.FactoryDependencies;
import org.geotools.referencing.util.CRSUtilities;
import org.geotools.referencing.wkt.Parser;
import org.geotools.util.Arguments;
import org.geotools.util.TableWriter;
import org.geotools.util.factory.Hints;

/**
 * Implementation of the {@link CRS#main} method. Exists as a separated class in order to reduce the class loading for
 * applications that don't want to run this main method.
 *
 * @version $Id$
 * @author Martin Desruisseaux (IRD)
 */
final class Command {
    /** The hints for the factory to fetch. Null for now, but may be different in a future version. */
    private static final Hints HINTS = null;

    /** The authority factory. */
    private final AuthorityFactory factory;

    /** The object to use for formatting objects. */
    private final Parser formatter;

    /** Creates an instance of the specified authority. */
    private Command(final String authority) {
        factory = authority == null
                ? CRS.getAuthorityFactory(false)
                : ReferencingFactoryFinder.getCRSAuthorityFactory(authority, HINTS);
        formatter = new Parser();
    }

    /** The separator to put between WKT. */
    private static char[] getSeparator() {
        final char[] separator = new char[79];
        Arrays.fill(separator, '\u2500');
        return separator;
    }

    /** Prints usage. */
    private static void help(final PrintWriter out) {
        out.println("Display informations about CRS identified by authority codes.");
        out.println("Usage: java org.geotools.referencing.CRS [options] [codes]");
        out.println("Options:");
        out.println(" -authority=ARG : Uses the specified authority factory (default to all).");
        out.println(" -bursawolfs    : Lists Bursa-Wolf parameters for the specified CRS.");
        out.println(" -codes         : Lists all available CRS codes with their description.");
        out.println(" -colors        : Enable syntax coloring on ANSI X3.64 compatible terminal.");
        out.println(" -dependencies  : Lists authority factory dependencies as a tree.");
        out.println(" -factories     : Lists all availables CRS authority factories.");
        out.println(" -forcexy       : Force \"longitude first\" axis order.");
        out.println(" -help          : Prints this message.");
        out.println(" -locale=ARG    : Formats texts in the specified locale.");
        out.println(" -operations    : Prints all available coordinate operations between a pair of CRS.");
        out.println(" -transform     : Prints the preferred math transform between a pair of CRS.");
    }

    /** Prints all objects as WKT. This is the default behavior when no option is specified. */
    private void list(final PrintWriter out, final String... args) throws FactoryException {
        char[] separator = null;
        for (String arg : args) {
            if (separator == null) {
                separator = getSeparator();
            } else {
                out.println(separator);
            }
            out.println(formatter.format(factory.createObject(arg)));
            final String warning = formatter.getWarning();
            if (warning != null) {
                out.println();
                out.print(Vocabulary.format(VocabularyKeys.WARNING));
                out.print(": ");
                out.println(warning);
            }
        }
    }

    /** Lists all authority codes. */
    private void codes(final PrintWriter out) throws FactoryException {
        final Set<String> codes = factory.getAuthorityCodes(CoordinateReferenceSystem.class);
        final TableWriter table = new TableWriter(out);
        table.writeHorizontalSeparator();
        table.write(Vocabulary.format(VocabularyKeys.CODE));
        table.nextColumn();
        table.write(Vocabulary.format(VocabularyKeys.DESCRIPTION));
        table.writeHorizontalSeparator();
        for (final String code : codes) {
            table.write(code);
            table.nextColumn();
            try {
                final InternationalString description = factory.getDescriptionText(code);
                if (description != null) {
                    table.write(description.toString());
                }
            } catch (NoSuchAuthorityCodeException e) {
                table.write(e.getLocalizedMessage());
            }
            table.nextLine();
        }
        table.writeHorizontalSeparator();
        try {
            table.flush();
        } catch (IOException e) {
            // Should never happen, since we are backed by PrintWriter
            throw new AssertionError(e);
        }
    }

    /** Lists all CRS authority factories. */
    private static void factories(final PrintWriter out) {
        final Set<Citation> done = new HashSet<>();
        final TableWriter table = new TableWriter(out, TableWriter.SINGLE_VERTICAL_LINE);
        final TableWriter notes = new TableWriter(out, " ");
        int noteCount = 0;
        notes.setMultiLinesCells(true);
        table.setMultiLinesCells(true);
        table.writeHorizontalSeparator();
        table.write(Vocabulary.format(VocabularyKeys.AUTHORITY));
        table.nextColumn();
        table.write(Vocabulary.format(VocabularyKeys.DESCRIPTION));
        table.nextColumn();
        table.write(Vocabulary.format(VocabularyKeys.NOTE));
        table.writeHorizontalSeparator();
        for (AuthorityFactory factory : ReferencingFactoryFinder.getCRSAuthorityFactories(HINTS)) {
            final Citation authority = factory.getAuthority();
            final Iterator<? extends Identifier> identifiers =
                    authority.getIdentifiers().iterator();
            if (!identifiers.hasNext()) {
                // No identifier. Scan next authorities.
                continue;
            }
            if (!done.add(authority)) {
                // Already done. Scans next authorities.
                continue;
            }
            table.write(identifiers.next().getCode());
            table.nextColumn();
            table.write(authority.getTitle().toString().trim());
            if (factory instanceof AbstractAuthorityFactory) {
                String description;
                try {
                    description = ((AbstractAuthorityFactory) factory).getBackingStoreDescription();
                } catch (FactoryException e) {
                    description = e.getLocalizedMessage();
                }
                if (description != null) {
                    final String n = String.valueOf(++noteCount);
                    table.nextColumn();
                    table.write('(');
                    table.write(n);
                    table.write(')');
                    notes.write('(');
                    notes.write(n);
                    notes.write(')');
                    notes.nextColumn();
                    notes.write(description.trim());
                    notes.nextLine();
                }
            }
            table.nextLine();
        }
        table.writeHorizontalSeparator();
        try {
            table.flush();
            notes.flush();
        } catch (IOException e) {
            // Should never happen, since we are backed by PrintWriter.
            throw new AssertionError(e);
        }
    }

    /** Prints the bursa-wolfs parameters for the specified CRS. */
    private void bursaWolfs(final PrintWriter out, final String... args) throws FactoryException {
        final NumberFormat nf = NumberFormat.getNumberInstance();
        nf.setMinimumFractionDigits(3);
        nf.setMaximumFractionDigits(3);
        final TableWriter table = new TableWriter(out);
        table.writeHorizontalSeparator();
        final String[] titles = {Vocabulary.format(VocabularyKeys.TARGET), "dx", "dy", "dz", "ex", "ey", "ez", "ppm"};
        for (String title : titles) {
            table.write(title);
            table.nextColumn();
            table.setAlignment(TableWriter.ALIGN_CENTER);
        }
        table.writeHorizontalSeparator();
        for (String arg : args) {
            IdentifiedObject object = factory.createObject(arg);
            if (object instanceof CoordinateReferenceSystem) {
                object = CRSUtilities.getDatum((CoordinateReferenceSystem) object);
            }
            if (object instanceof DefaultGeodeticDatum) {
                final BursaWolfParameters[] params = ((DefaultGeodeticDatum) object).getBursaWolfParameters();
                for (final BursaWolfParameters p : params) {
                    table.setAlignment(TableWriter.ALIGN_LEFT);
                    table.write(p.targetDatum.getName().getCode());
                    table.nextColumn();
                    table.setAlignment(TableWriter.ALIGN_RIGHT);
                    double v;
                    for (int k = 0; k < 7; k++) {
                        switch (k) {
                            case 0:
                                v = p.dx;
                                break;
                            case 1:
                                v = p.dy;
                                break;
                            case 2:
                                v = p.dz;
                                break;
                            case 3:
                                v = p.ex;
                                break;
                            case 4:
                                v = p.ey;
                                break;
                            case 5:
                                v = p.ez;
                                break;
                            case 6:
                                v = p.ppm;
                                break;
                            default:
                                throw new AssertionError(k);
                        }
                        table.write(nf.format(v));
                        table.nextColumn();
                    }
                    table.nextLine();
                }
                table.writeHorizontalSeparator();
            }
        }
        try {
            table.flush();
        } catch (IOException e) {
            // Should never happen, since we are backed by PrintWriter
            throw new AssertionError(e);
        }
    }

    /** Prints the operations between every pairs of the specified authority code. */
    private void operations(final PrintWriter out, final String... args) throws FactoryException {
        if (!(factory instanceof CoordinateOperationAuthorityFactory)) {
            return;
        }
        final CoordinateOperationAuthorityFactory factory = (CoordinateOperationAuthorityFactory) this.factory;
        char[] separator = null;
        for (int i = 0; i < args.length; i++) {
            for (int j = i + 1; j < args.length; j++) {
                final Set<CoordinateOperation> op = factory.createFromCoordinateReferenceSystemCodes(args[i], args[j]);
                for (final CoordinateOperation operation : op) {
                    if (separator == null) {
                        separator = getSeparator();
                    } else {
                        out.println(separator);
                    }
                    out.println(formatter.format(operation));
                }
            }
        }
    }

    /** Prints the math transforms between every pairs of the specified authority code. */
    private void transform(final PrintWriter out, final String... args) throws FactoryException {
        if (!(factory instanceof CRSAuthorityFactory)) {
            return;
        }
        final CRSAuthorityFactory factory = (CRSAuthorityFactory) this.factory;
        final CoordinateOperationFactory opFactory = ReferencingFactoryFinder.getCoordinateOperationFactory(HINTS);
        char[] separator = null;
        for (int i = 0; i < args.length; i++) {
            final CoordinateReferenceSystem crs1 = factory.createCoordinateReferenceSystem(args[i]);
            for (int j = i + 1; j < args.length; j++) {
                final CoordinateReferenceSystem crs2 = factory.createCoordinateReferenceSystem(args[j]);
                final CoordinateOperation op;
                try {
                    op = opFactory.createOperation(crs1, crs2);
                } catch (OperationNotFoundException exception) {
                    out.println(exception.getLocalizedMessage());
                    continue;
                }
                if (separator == null) {
                    separator = getSeparator();
                } else {
                    out.println(separator);
                }
                out.println(formatter.format(op.getMathTransform()));
            }
        }
    }

    /** Prints all {@linkplain AuthorityFactory authority factory} dependencies as a tree. */
    private static void printAuthorityFactoryDependencies(final PrintWriter out, final boolean colors) {
        final FactoryDependencies printer = new FactoryDependencies(CRS.getAuthorityFactory(false));
        printer.setAttributeEnabled(true);
        printer.setColorEnabled(colors);
        printer.print(out);
        out.flush();
    }

    /** Dispose the factory. */
    private void dispose() throws FactoryException {
        if (factory instanceof AbstractAuthorityFactory) {
            ((AbstractAuthorityFactory) factory).dispose();
        }
    }

    /** Implementation of {@link CRS#main}. */
    @SuppressWarnings("PMD.CloseResource")
    public static void execute(String... args) {
        final Arguments arguments = new Arguments(args);
        final PrintWriter out = arguments.out;
        Locale.setDefault(arguments.locale);
        if (arguments.getFlag("-forcexy")) {
            Hints.putSystemDefault(Hints.FORCE_LONGITUDE_FIRST_AXIS_ORDER, Boolean.TRUE);
        }
        if (arguments.getFlag("-help")) {
            args = arguments.getRemainingArguments(0);
            help(out);
            return;
        }
        if (arguments.getFlag("-factories")) {
            args = arguments.getRemainingArguments(0);
            factories(out);
            return;
        }
        if (arguments.getFlag("-dependencies")) {
            final boolean colors = arguments.getFlag("-colors");
            args = arguments.getRemainingArguments(0);
            printAuthorityFactoryDependencies(out, colors);
            return;
        }
        final String authority = arguments.getOptionalString("-authority");
        final Command command = new Command(authority);
        command.formatter.setColorEnabled(arguments.getFlag("-colors"));
        try {
            if (arguments.getFlag("-codes")) {
                args = arguments.getRemainingArguments(0);
                command.codes(out);
            } else if (arguments.getFlag("-bursawolfs")) {
                args = arguments.getRemainingArguments(Integer.MAX_VALUE, '-');
                command.bursaWolfs(out, args);
            } else if (arguments.getFlag("-operations")) {
                args = arguments.getRemainingArguments(2, '-');
                command.operations(out, args);
            } else if (arguments.getFlag("-transform")) {
                args = arguments.getRemainingArguments(2, '-');
                command.transform(out, args);
            } else {
                args = arguments.getRemainingArguments(Integer.MAX_VALUE, '-');
                command.list(out, args);
            }
            out.flush();
            command.dispose();
        } catch (FactoryException exception) {
            out.flush();
            arguments.err.println(exception.getLocalizedMessage());
        } catch (Exception exception) {
            out.flush();
            java.util.logging.Logger.getGlobal().log(java.util.logging.Level.INFO, "", exception);
        }
    }
}
