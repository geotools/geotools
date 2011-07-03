package org.geotools.data.efeature.adapters;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import org.eclipse.emf.query.conditions.Condition;
import org.eclipse.emf.query.conditions.IDataTypeAdapter;

/**
 * An Adapter class to be used to extract from -adapt- the argument object to some {@link Date}
 * value that would later be used in <code>Condition</code> evaluation.
 * 
 * Clients can subclass it and provide their own implementation
 * 
 * @see {@link Condition}
 */
public abstract class DateAdapter implements IDataTypeAdapter<Date> {

    /**
     * The simplest {@link DateAdapter} implementation that represents the case when the argument
     * object to adapt is a {@link Date} object itself.
     */
    public static final DateAdapter DEFAULT = new DateAdapter() {
        @Override
        public Date toDate(Object object) {
            if (object instanceof Date) {
                return (Date) object;
            }
            String s = (object != null ? object.toString() : null);
            if (!(s == null || s.length() == 0)) {
                for (SimpleDateFormat it : FORMATS) {
                    try {
                        return it.parse(s);
                    } catch (ParseException e) {
                        // Ignore and try next date parser
                    }
                }
            }
            // All parsers failed
            return null;
        }

    };

    /** Set of legal date formats */
    public static final Set<SimpleDateFormat> FORMATS = createDefaultFormats();

    /**
     * Extracts from/Adapts the argument object to a {@link Date}
     * 
     * @param object - the argument object to adapt to a {@link Date} by this adapter
     * @return the {@link Date} object representation of the argument object
     */
    public abstract Date toDate(Object object);

    @Override
    public Date adapt(Object value) {
        return toDate(value);
    }

    public static Set<SimpleDateFormat> createDefaultFormats() {
        Set<SimpleDateFormat> formats = new HashSet<SimpleDateFormat>();
        formats.add(new SimpleDateFormat());
        return formats;
    }

}
