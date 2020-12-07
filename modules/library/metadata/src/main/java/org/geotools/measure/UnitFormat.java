package org.geotools.measure;

import java.io.IOException;
import java.text.ParsePosition;

import javax.measure.Unit;
import javax.measure.format.MeasurementParseException;

/**
 * An interface that is similar to {@link javax.measure.format.UnitFormat} but elides mutating
 * methods.
 *
 * <p>It is used to protect global or shared UnitFormat instances from being changed inadvertently.
 */
public interface UnitFormat {

  Appendable format(Unit<?> unit, Appendable appendable) throws IOException;

  String format(Unit<?> unit);

  Unit<?> parse(CharSequence csq, ParsePosition pos) throws IllegalArgumentException, MeasurementParseException;

  Unit<?> parse(CharSequence csq) throws MeasurementParseException;

}
