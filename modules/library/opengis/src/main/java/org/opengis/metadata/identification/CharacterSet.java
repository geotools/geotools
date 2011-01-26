/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2011, Open Source Geospatial Foundation (OSGeo)
 *    (C) 2004-2005, Open Geospatial Consortium Inc.
 *    
 *    All Rights Reserved. http://www.opengis.org/legal/
 */
package org.opengis.metadata.identification;

import java.nio.charset.Charset;
import java.nio.charset.UnsupportedCharsetException;
import java.util.List;
import java.util.ArrayList;

import org.opengis.util.CodeList;
import org.opengis.annotation.UML;

import static org.opengis.annotation.Specification.ISO_19115;
import static org.opengis.annotation.Obligation.CONDITIONAL;


/**
 * Name of the character coding standard used for the resource.
 *
 * @version <A HREF="http://www.opengeospatial.org/standards/as#01-111">ISO 19115</A>
 * @author  Ely Conn (Leica Geosystems Geospatial Imaging, LLC)
 * @since   GeoAPI 2.1
 */
@UML(identifier="MD_CharacterSetCode", specification=ISO_19115)
public final class CharacterSet extends CodeList<CharacterSet> {
    /**
     * Serial number for compatibility with different versions.
     */
    private static final long serialVersionUID = -4726629268456735927L;

    /**
     * List of all enumerations of this type.
     * Must be declared before any enum declaration.
     */
    private static final List<CharacterSet> VALUES = new ArrayList<CharacterSet>(29);

    /**
     * 16-bit fixed size Universal Character Set, based on ISO/IEC 10646.
     */
    @UML(identifier="ucs2", obligation=CONDITIONAL, specification=ISO_19115)
    public static final CharacterSet UCS_2 = new CharacterSet("UCS_2", "UCS-2");

    /**
     * 32-bit fixed size Universal Character Set, based on ISO/IEC 10646.
     */
    @UML(identifier="ucs4", obligation=CONDITIONAL, specification=ISO_19115)
    public static final CharacterSet UCS_4 = new CharacterSet("UCS_4", "UCS-4");

    /**
     * 7-bit variable size UCS Transfer Format, based on ISO/IEC 10646.
     */
    @UML(identifier="utf7", obligation=CONDITIONAL, specification=ISO_19115)
    public static final CharacterSet UTF_7 = new CharacterSet("UTF_7", "UTF-7");

    /**
     * 8-bit variable size UCS Transfer Format, based on ISO/IEC 10646.
     */
    @UML(identifier="utf8", obligation=CONDITIONAL, specification=ISO_19115)
    public static final CharacterSet UTF_8 = new CharacterSet("UTF_8", "UTF-8");

    /**
     * 16-bit variable size UCS Transfer Format, based on ISO/IEC 10646.
     */
    @UML(identifier="utf16", obligation=CONDITIONAL, specification=ISO_19115)
    public static final CharacterSet UTF_16 = new CharacterSet("UTF_16", "UTF-16");

    /**
     * ISO/IEC 8859-1, Information technology.
     * 8-bit single-byte coded graphic character sets - Part 1: Latin alphabet No. 1.
     */
    @UML(identifier="8859part1", obligation=CONDITIONAL, specification=ISO_19115)
    public static final CharacterSet ISO_8859_1 = new CharacterSet("ISO_8859_1", "ISO-8859-1");

    /**
     * ISO/IEC 8859-2, Information technology.
     * 8-bit single-byte coded graphic character sets - Part 2: Latin alphabet No. 2.
     */
    @UML(identifier="8859part2", obligation=CONDITIONAL, specification=ISO_19115)
    public static final CharacterSet ISO_8859_2 = new CharacterSet("ISO_8859_2", "ISO-8859-2");

    /**
     * ISO/IEC 8859-3, Information technology.
     * 8-bit single-byte coded graphic character sets - Part 3: Latin alphabet No. 3.
     */
    @UML(identifier="8859part3", obligation=CONDITIONAL, specification=ISO_19115)
    public static final CharacterSet ISO_8859_3 = new CharacterSet("ISO_8859_3", "ISO-8859-3");

    /**
     * ISO/IEC 8859-4, Information technology.
     * 8-bit single-byte coded graphic character sets - Part 4: Latin alphabet No. 4.
     */
    @UML(identifier="8859part4", obligation=CONDITIONAL, specification=ISO_19115)
    public static final CharacterSet ISO_8859_4 = new CharacterSet("ISO_8859_4", "ISO-8859-4");

    /**
     * ISO/IEC 8859-5, Information technology.
     * 8-bit single-byte coded graphic character sets - Part 5: Latin/Cyrillic alphabet.
     */
    @UML(identifier="8859part5", obligation=CONDITIONAL, specification=ISO_19115)
    public static final CharacterSet ISO_8859_5 = new CharacterSet("ISO_8859_5", "ISO-8859-5");

    /**
     * ISO/IEC 8859-6, Information technology.
     * 8-bit single-byte coded graphic character sets - Part 6: Latin/Arabic alphabet.
     */
    @UML(identifier="8859part6", obligation=CONDITIONAL, specification=ISO_19115)
    public static final CharacterSet ISO_8859_6 = new CharacterSet("ISO_8859_6", "ISO-8859-6");

    /**
     * ISO/IEC 8859-7, Information technology.
     * 8-bit single-byte coded graphic character sets - Part 7: Latin/Greek alphabet.
     */
    @UML(identifier="8859part7", obligation=CONDITIONAL, specification=ISO_19115)
    public static final CharacterSet ISO_8859_7 = new CharacterSet("ISO_8859_7", "ISO-8859-7");

    /**
     * ISO/IEC 8859-8, Information technology.
     * 8-bit single-byte coded graphic character sets - Part 8: Latin/Hebrew alphabet.
     */
    @UML(identifier="8859part8", obligation=CONDITIONAL, specification=ISO_19115)
    public static final CharacterSet ISO_8859_8 = new CharacterSet("ISO_8859_8", "ISO-8859-8");

    /**
     * ISO/IEC 8859-9, Information technology.
     * 8-bit single-byte coded graphic character sets - Part 9: Latin alphabet No. 5.
     */
    @UML(identifier="8859part9", obligation=CONDITIONAL, specification=ISO_19115)
    public static final CharacterSet ISO_8859_9 = new CharacterSet("ISO_8859_9", "ISO-8859-9");

    /**
     * ISO/IEC 8859-10, Information technology.
     * 8-bit single-byte coded graphic character sets - Part 10: Latin alphabet No. 6.
     */
    @UML(identifier="8859part10", obligation=CONDITIONAL, specification=ISO_19115)
    public static final CharacterSet ISO_8859_10 = new CharacterSet("ISO_8859_10", "ISO-8859-10");

    /**
     * ISO/IEC 8859-11, Information technology.
     * 8-bit single-byte coded graphic character sets - Part 11: Latin/Thai alphabet.
     */
    @UML(identifier="8859part11", obligation=CONDITIONAL, specification=ISO_19115)
    public static final CharacterSet ISO_8859_11 = new CharacterSet("ISO_8859_11", "ISO-8859-11");

    /**
     * A future ISO/IEC 8-bit single-byte coded graphic character set.
     */
    @UML(identifier="8859part12", obligation=CONDITIONAL, specification=ISO_19115)
    public static final CharacterSet ISO_8859_12 = new CharacterSet("ISO_8859_12", "ISO-8859-12");

    /**
     * ISO/IEC 8859-13, Information technology.
     * 8-bit single-byte coded graphic character sets - Part 13: Latin alphabet No. 7.
     */
    @UML(identifier="8859part13", obligation=CONDITIONAL, specification=ISO_19115)
    public static final CharacterSet ISO_8859_13 = new CharacterSet("ISO_8859_13", "ISO-8859-13");

    /**
     * ISO/IEC 8859-14, Information technology.
     * 8-bit single-byte coded graphic character sets - Part 14: Latin alphabet No. 8 (Celtic).
     */
    @UML(identifier="8859part14", obligation=CONDITIONAL, specification=ISO_19115)
    public static final CharacterSet ISO_8859_14 = new CharacterSet("ISO_8859_14", "ISO-8859-14");

    /**
     * ISO/IEC 8859-15, Information technology.
     * 8-bit single-byte coded graphic character sets - Part 15: Latin alphabet No. 9.
     */
    @UML(identifier="8859part15", obligation=CONDITIONAL, specification=ISO_19115)
    public static final CharacterSet ISO_8859_15 = new CharacterSet("ISO_8859_15", "ISO-8859-15");

    /**
     * ISO/IEC 8859-16, Information technology.
     * 8-bit single-byte coded graphic character sets - Part 16: Latin alphabet No. 10.
     */
    @UML(identifier="8859part16", obligation=CONDITIONAL, specification=ISO_19115)
    public static final CharacterSet ISO_8859_16 = new CharacterSet("ISO_8859_16", "ISO-8859-16");

    /**
     * Japanese code set used for electronic transmission.
     */
    @UML(identifier="jis", obligation=CONDITIONAL, specification=ISO_19115)
    public static final CharacterSet JIS = new CharacterSet("JIS", "JIS_X0201");

    /**
     * Japanese code set used on MS-DOS based machines.
     */
    @UML(identifier="shiftJIS", obligation=CONDITIONAL, specification=ISO_19115)
    public static final CharacterSet SHIFT_JIS = new CharacterSet("SHIFT_JIS", "Shift_JIS");

    /**
     * Japanese code set used on UNIX based machines.
     */
    @UML(identifier="eucJP", obligation=CONDITIONAL, specification=ISO_19115)
    public static final CharacterSet EUC_JP = new CharacterSet("EUC_JP", "EUC-JP");

    /**
     * United States ASCII code set (ISO 646 US).
     */
    @UML(identifier="usAscii", obligation=CONDITIONAL, specification=ISO_19115)
    public static final CharacterSet US_ASCII = new CharacterSet("US_ASCII", "US-ASCII");

    /**
     * IBM mainframe code set.
     */
    @UML(identifier="ebcdic", obligation=CONDITIONAL, specification=ISO_19115)
    public static final CharacterSet EBCDIC = new CharacterSet("EBCDIC", null);

    /**
     * Korean code set.
     */
    @UML(identifier="eucKR", obligation=CONDITIONAL, specification=ISO_19115)
    public static final CharacterSet EUC_KR = new CharacterSet("EUC_KR", "EUC-KR");

    /**
     * Traditional Chinese code set used in Taiwan, Hong Kong, and other areas.
     */
    @UML(identifier="big5", obligation=CONDITIONAL, specification=ISO_19115)
    public static final CharacterSet BIG_5 = new CharacterSet("BIG_5", "Big5");

    /**
     * Simplified Chinese code set.
     */
    @UML(identifier="GB2312", obligation=CONDITIONAL, specification=ISO_19115)
    public static final CharacterSet GB2312 = new CharacterSet("GB2312", "GB2312");

    /**
     * The Java {@link Charset} name (never {@code null}).
     */
    private final String charset;

    /**
     * Constructs an enum with the given name. The new enum is
     * automatically added to the list returned by {@link #values}.
     *
     * @param name The enum name. This name must not be in use by an other enum of this type.
     * @param charset The Java {@link Charset} name, or {@code null} if none.
     */
    private CharacterSet(final String name, final String charset) {
        super(name, VALUES);
        this.charset = (charset != null) ? charset : name;
    }

    /**
     * Constructs an enum with identical name and charset.
     * This is needed for {@link CodeList#valueOf} reflection.
     */
    private CharacterSet(final String name) {
        this(name, name);
    }

    /**
     * Converts the Character Set to a java Charset, if it can.
     *
     * @return The Java Charset.
     * @throws UnsupportedCharsetException If no support for the charset is available.
     *
     * @see <A HREF="http://java.sun.com/javase/6/docs/technotes/guides/intl/encoding.doc.html">Supported encodings</A>
     */
    public Charset toCharset() throws UnsupportedCharsetException {
        return Charset.forName(charset);
    }

    /**
     * Returns the list of {@code CharacterSet}s.
     *
     * @return The list of codes declared in the current JVM.
     */
    public static CharacterSet[] values() {
        synchronized (VALUES) {
            return VALUES.toArray(new CharacterSet[VALUES.size()]);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean matches(final String name) {
        if (super.matches(name)) {
            return true;
        }
        return (name != null) && name.equalsIgnoreCase(charset);
    }

    /**
     * Returns the list of enumerations of the same kind than this enum.
     */
    public CharacterSet[] family() {
        return values();
    }

    /**
     * Returns the character set that matches the given string, or returns a
     * new one if none match it.
     *
     * @param code The name of the code to fetch or to create.
     * @return A code matching the given name.
     */
    public static CharacterSet valueOf(String code) {
        return valueOf(CharacterSet.class, code);
    }
}
