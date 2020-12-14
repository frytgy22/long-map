package de.comparus.opensource.longmap;

import org.junit.Assert;
import org.junit.Test;

public class LongMapImplTest {

    @Test
    public void shouldPutAllData() {
        //given
        LongMapImpl<String> longMap = new LongMapImpl<>();

        //when
        longMap.put(15465465, "1");
        longMap.put(3, "3");
        longMap.put(4567, "4");
        longMap.put(136565777777L, "13");
        longMap.put(5, "5");

        longMap.put(65346346546L, "6");
        longMap.put(756546, "7");
        longMap.put(8, "8");
        longMap.put(9, "9");
        longMap.put(1654656456465464560L, "10");

        //then
        Assert.assertEquals(10, longMap.size());

        Assert.assertEquals("1", longMap.get(15465465));
        Assert.assertEquals("3", longMap.get(3));
        Assert.assertEquals("4", longMap.get(4567));
        Assert.assertEquals("13", longMap.get(136565777777L));
        Assert.assertEquals("5", longMap.get(5));

        Assert.assertEquals("6", longMap.get(65346346546L));
        Assert.assertEquals("7", longMap.get(756546));
        Assert.assertEquals("8", longMap.get(8));
        Assert.assertEquals("9", longMap.get(9));
        Assert.assertEquals("10", longMap.get(1654656456465464560L));
    }

    @Test
    public void shouldOverwriteValue() {
        //given
        LongMapImpl<String> longMap = new LongMapImpl<>();

        //then
        longMap.put(19, "9");
        Assert.assertEquals(1, longMap.size());
        Assert.assertEquals("9", longMap.get(19));

        longMap.put(19, "154");
        Assert.assertEquals("154", longMap.get(19));
        Assert.assertEquals(1, longMap.size());
    }

    @Test
    public void shouldRemoveData() {
        //given
        LongMapImpl<String> longMap = new LongMapImpl<>();
        String number = "14353";
        String value = "value";

        //when
        String putNumber = longMap.put(1, number);
        String putValue = longMap.put(2, value);

        //then
        Assert.assertEquals(putNumber, number);
        Assert.assertEquals(putValue, value);
        Assert.assertEquals(2, longMap.size());

        String removedValue = longMap.remove(2);

        Assert.assertEquals(removedValue, value);
        Assert.assertNull(longMap.get(2));
        Assert.assertEquals(1, longMap.size());
    }

    @Test
    public void shouldCheckIsEmpty() {
        //given
        LongMapImpl<String> longMap = new LongMapImpl<>();

        //when
        longMap.put(15465465, "1");
        longMap.put(3, "3");
        longMap.put(4567, "4");

        //then
        Assert.assertEquals(3, longMap.size());
        Assert.assertFalse(longMap.isEmpty());

        longMap.clear();

        Assert.assertEquals(0, longMap.size());
        Assert.assertTrue(longMap.isEmpty());
    }

    @Test
    public void shouldFindKey() {
        //given
        LongMapImpl<String> longMap = new LongMapImpl<>();

        //when
        longMap.put(15465465, "1");
        longMap.put(3, "3");
        longMap.put(4567, "4");

        boolean containsKey = longMap.containsKey(15465465);
        boolean wrongKey = longMap.containsKey(12);

        //then
        Assert.assertTrue(containsKey);
        Assert.assertFalse(wrongKey);
    }

    @Test
    public void shouldFindValue() {
        //given
        LongMapImpl<String> longMap = new LongMapImpl<>();

        //when
        longMap.put(15465465, "1");
        longMap.put(3, "3");
        longMap.put(4567, "4");

        boolean containsValue = longMap.containsValue("3");
        boolean wrongValue = longMap.containsValue("wrong value");

        //then
        Assert.assertTrue(containsValue);
        Assert.assertFalse(wrongValue);
    }

    @Test
    public void shouldGetKeys() {
        //given
        LongMapImpl<String> longMap = new LongMapImpl<>();
        long[] expectedKeys = {15465465, 3, 4567};

        //when
        long[] emptyKeys = longMap.keys();
        Assert.assertArrayEquals(new long[0], emptyKeys);

        longMap.put(15465465, "1");
        longMap.put(3, "3");
        longMap.put(4567, "4");

        //then
        long[] fullKeys = longMap.keys();
        Assert.assertArrayEquals(expectedKeys, fullKeys);
    }

    @Test
    public void shouldGetValues() {
        //given
        LongMapImpl<String> longMap = new LongMapImpl<>();
        String[] expectedValues = {"1", "3", "4"};

        //when
        String[] emptyValues = longMap.values();
        Assert.assertNull(emptyValues);

        longMap.put(15465465, "1");
        longMap.put(3, "3");
        longMap.put(4567, "4");

        //then
        String[] fullValues = longMap.values();
        Assert.assertArrayEquals(expectedValues, fullValues);
    }
}

