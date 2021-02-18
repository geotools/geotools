package org.geotools.feature;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import org.junit.Test;
import org.opengis.feature.type.Name;

public class NameImplTest {

    @Test
    @SuppressWarnings("BanSerializableRead")
    public void testSerialize() throws Exception {
        NameImpl name = new NameImpl("hello", "world");

        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        ObjectOutputStream out = new ObjectOutputStream(buffer);

        out.writeObject(name);

        byte[] bytes = buffer.toByteArray();

        ByteArrayInputStream input = new ByteArrayInputStream(bytes);
        ObjectInputStream in = new ObjectInputStream(input);

        Name copy = (Name) in.readObject();

        assertNotSame(name, copy);
        assertEquals(name, copy);
    }

    @Test
    @SuppressWarnings("SelfComparison")
    public void testCompare() {
        NameImpl scoped1 = new NameImpl("hello", "world");
        NameImpl scoped2 = new NameImpl("hello", "fred");
        NameImpl fred = new NameImpl("world");

        assertEquals(0, scoped1.compareTo(scoped1));
        assertEquals(0, scoped2.compareTo(scoped2));
        assertEquals(0, fred.compareTo(fred));

        assertTrue(scoped1.compareTo(scoped2) > 0);
        assertTrue(scoped2.compareTo(scoped1) < 0);

        assertTrue(scoped2.compareTo(scoped1) < 0);
    }
}
