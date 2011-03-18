package org.geotools.metadata;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Properties;
import java.util.ResourceBundle;

import org.geotools.metadata.iso.citation.CitationImpl;
import org.geotools.metadata.iso.citation.Citations;
import org.geotools.metadata.iso.citation.ResponsiblePartyImpl;
import org.geotools.metadata.iso.citation.TelephoneImpl;
import org.geotools.referencing.crs.DefaultGeographicCRS;
import org.geotools.text.Text;
import org.geotools.util.GrowableInternationalString;
import org.geotools.util.NumberRange;
import org.geotools.util.Range;
import org.geotools.util.ResourceInternationalString;
import org.geotools.util.SimpleInternationalString;
import org.geotools.util.Utilities;
import org.opengis.metadata.citation.Citation;
import org.opengis.metadata.citation.ResponsibleParty;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.opengis.util.InternationalString;

public class MetadataExamples {

public static void main(String args[]) {
    MetadataExamples examples = new MetadataExamples();
    
    examples.exampleRange();
    examples.referenceDocument(Citations.EPSG);
    examples.referenceDocument(Citations.OGC);
    examples.referenceDocument(Citations.ORACLE);
    examples.exampleCitation(examples);
    examples.telephone();
    examples.wkt();
}

private void exampleUtilities() {
    String string1 = null;
    String string2 = null;
    Object object = null;
    Object value = null;
    int seed = 0;
    
    // exampleUtilities start
    
    // equals provides a null safe equals check
    Utilities.equals(string1, string2); // null safe equals check
    Utilities.equals(null, "Hello"); // false
    Utilities.equals(null, null); // true!
    
    // deepEquals will check the contents of arrays
    Utilities.deepEquals(new double[] { 1.0 }, new double[] { 1.0 });
    
    // deepToString will print out objects and arrays
    Utilities.deepToString(new double[] { 1.0, Math.PI, Math.E });
    
    // when implementing your own object the following are handy
    Utilities.hash(value, seed);
    Utilities.equals(value, object);
    Utilities.ensureNonNull("parameter", object);
    // exampleUtilities end
}

private void exampleCitation(MetadataExamples examples) {
    CitationImpl citation = new CitationImpl();
    citation.setEditionDate(new Date()); // today
    
    Collection<ResponsibleParty> parties = Collections.singleton(ResponsiblePartyImpl.GEOTOOLS);
    citation.setCitedResponsibleParties(parties);
    
    referenceDocument(Citations.ORACLE);
}

private void referenceDocument(Citation citation) {
    System.out.println(citation.getTitle());
    System.out.println(citation.getTitle().toString(Locale.FRENCH));
    
    System.out.println(citation.getIdentifiers());
    System.out.println(citation.getAlternateTitles());
}

private void telephone() {
    TelephoneImpl phone = new TelephoneImpl();
    phone.setVoices(Collections.singleton("555-1234"));
    phone.setFacsimiles(Collections.singleton("555-2FAX"));
    System.out.println(phone);
}

private void wkt() {
    CoordinateReferenceSystem crs = DefaultGeographicCRS.WGS84;
    System.out.println(crs.toWKT());
}

private void exampleRange() {
    // exampleRange start
    // simple range of one value
    Range<Double> zero = new Range<Double>(Double.class, 0.0);
    
    System.out.println(zero); // [0.0, 0.0]
    
    // range include two values
    Range<Double> from1to5 = new Range<Double>(Double.class, 1.0, 5.0);
    System.out.println(from1to5); // [1.0, 5.0]
    
    // range from one value up to a limit
    Range<Double> from1upto10 = new Range<Double>(Double.class, 1.0, true, 10.0, false);
    System.out.println(from1upto10); // [1.0, 10.0)
    
    // range between two values
    Range<Double> between6and8 = new Range<Double>(Double.class, 6.0, false, 8.0, false);
    System.out.println(between6and8); // (6.0, 8.0)
    
    // range details
    from1to5.getMinValue();
    from1to5.isMinIncluded();
    from1to5.getMaxValue();
    from1to5.isMaxIncluded();
    
    // range operations
    from1upto10.subtract(between6and8); // returns two ranges
    from1upto10.subtract(from1to5); // returns one range
    
    from1to5.union(between6and8); // returns [1.0,8.0)
    
    // range check
    from1to5.contains(3.0); // true
    from1upto10.contains(between6and8); // true
    
    // exampleRange end
}

private void exampleRangeComparable() {
    // exampleRangeComparable start
    Range<Date> today = new Range<Date>(Date.class, new Date());
    // exampleRangeComparable end
}

private void exampleNumberRange() {
    // exampleNumberRange start
    
    NumberRange<Double> positive = NumberRange.create(0.0, Double.MAX_VALUE);
    NumberRange<Double> negative = NumberRange.create(0.0, false, Double.MIN_VALUE, true);
    
    NumberRange<Integer> decimal = NumberRange.create(1, 10);
    positive.contains(decimal);
    
    positive.contains((Number) 3.0); // test double
    positive.contains((Number) 3); // test integer
    
    // exampleNumberRange end
}

//
// Text
//

private void examleText() throws Exception {
    // exampleText start
    
    InternationalString greeting;
    
    // simple text place holder
    greeting = Text.text("hello world");
    
    // translated text for an internationalized application
    // useful as message.properties, message_fr.properties etc can be translated
    // outside of your application
    greeting = Text.text("greeting", "message.properties");
    
    // the next method is good for quickly doing things as a developer
    Map<String,String> translations = new HashMap<String,String>();
    translations.put("greeting", "Hello World");
    translations.put( "greeting_it", "ciao mondo");
    
    greeting = Text.text("greeting", translations );
    
    // you can actually use the same map to configure several international strings
    // (each string will only pick up the entries with a matching key)
    //
    // With that in mind we make a special effort to allow you to use properties
    Properties properties = new Properties();
    properties.load( new FileInputStream("message.properties") );
    
    InternationalString title = Text.text( "title", properties );
    InternationalString description = Text.text( "description", properties );
    
    // exampleText end
}

private void exampleResourceInternationalString() {
    // exampleResourceInternationalString start
    ResourceInternationalString greeting = new ResourceInternationalString("message.properties",
            "greeting");
    
    System.out.println(greeting); // will output best match to current Locale
    System.out.println(greeting.toString(Locale.CANADA_FRENCH)); // should output best match
    
    // Note the "messages.properties" above is used with Java ResourceBundle
    // In a manner similar to this
    ResourceBundle.getBundle("message.properties", Locale.getDefault()).getString("greeting");
    // exampleResourceInternationalString end
}

private void exampleSimpleInternationalStirng() {
    // exampleSimpleInternationalStirng start
    SimpleInternationalString greeting = new SimpleInternationalString("Hello World");
    
    System.out.println(greeting); // will output best match to current
    System.out.println(greeting.toString(Locale.FRENCH)); // but since there is only one...
    // exampleSimpleInternationalStirng end
}

private void exampleGrowableInternationalString() {
    // exampleGrowableInternationalString start
    GrowableInternationalString greeting = new GrowableInternationalString();
    
    // Make use of Locale when adding translations
    greeting.add(Locale.ENGLISH, "Hello World");
    greeting.add(Locale.FRENCH, "Bonjour tout le monde");
    greeting.add("fr", "greeting", "");
    
    System.out.println(greeting); // will output best match to current Locale
    System.out.println(greeting.toString(Locale.CANADA_FRENCH)); // should output best match
    
    // You can also use strings to avoid creating Locale objects right away
    // The method references a "key" which is handy when processing property files
    greeting.add("key_en", "key", "Hello World");
    greeting.add("key_fr", "key", "Bonjour tot le monde");
    
    // You can also quickly create an GrowableInternationalString and add to it later
    GrowableInternationalString cheerfulGreeting = new GrowableInternationalString("G'Day");
    cheerfulGreeting.add(Locale.UK, "Welcome");
    cheerfulGreeting.add(Locale.US, "Hello");
    // exampleGrowableInternationalString end
}
}
