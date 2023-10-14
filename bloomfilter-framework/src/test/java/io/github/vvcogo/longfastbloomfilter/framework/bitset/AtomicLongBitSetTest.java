package io.github.vvcogo.longfastbloomfilter.framework.bitset;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.concurrent.atomic.AtomicLongArray;

import static org.junit.jupiter.api.Assertions.*;

class AtomicLongBitSetTest {

    private static final long INITIAL_SIZE = 100L;
    private AtomicLongBitSet bitSet;

    @BeforeEach
    void setup() {
        this.bitSet = new AtomicLongBitSet(INITIAL_SIZE);
    }

    @Test
    void testConstructor() {
        assertEquals(INITIAL_SIZE, this.bitSet.getSize());
    }

    @Test
    void testSet() {
        this.bitSet.set(1L);
        this.bitSet.set(2L);
        this.bitSet.set(53L);
        assertTrue(this.bitSet.get(1L));
        assertTrue(this.bitSet.get(2L));
        assertTrue(this.bitSet.get(53L));
    }

    @Test
    void testGet() {
        assertFalse(this.bitSet.get(1L));
        assertFalse(this.bitSet.get(2L));
        assertFalse(this.bitSet.get(53L));
        this.bitSet.set(1L);
        assertTrue(this.bitSet.get(1L));
        assertFalse(this.bitSet.get(2L));
        assertFalse(this.bitSet.get(53L));
    }

    @Test
    void testEquals() {
        assertEquals(this.bitSet, this.bitSet);
        assertNotEquals(this.bitSet, null);
        AtomicLongBitSet other = new AtomicLongBitSet(INITIAL_SIZE);
        assertEquals(this.bitSet, other);
        other.set(1L);
        assertNotEquals(this.bitSet, other);
        this.bitSet.set(1L);
        assertEquals(this.bitSet, other);
    }

    @Test
    void testHashCode() {
        AtomicLongBitSet other = new AtomicLongBitSet(INITIAL_SIZE);
        assertEquals(this.bitSet.hashCode(), other.hashCode());
        other.set(1L);
        this.bitSet.set(1L);
        assertEquals(this.bitSet.hashCode(), other.hashCode());
    }

    @Test
    void testToString() {
        StringBuilder sb = new StringBuilder("[");
        for (long i = 0; i < INITIAL_SIZE; i++) {
            sb.append("0");
        }
        sb.append("]");
        String expected = sb.toString();
        assertEquals(this.bitSet.toString(), expected);
        this.bitSet.set(0L);
        assertNotEquals(this.bitSet.toString(), expected);
        expected = expected.replaceFirst("0", "1");
        assertEquals(expected, this.bitSet.toString());
    }

    @Test
    void testInitializeBitArray() throws NoSuchFieldException, IllegalAccessException {
        this.bitSet.initializeBitArray(1);
        Field field = this.bitSet.getClass().getDeclaredField("bits");
        field.setAccessible(true);
        AtomicLongArray[] bits = (AtomicLongArray[]) field.get(this.bitSet);
        assertEquals(1, bits.length);
        assertEquals(1, bits[0].length());
    }

    @Test
    void testAbstractSet() {
        this.bitSet.set(0, 0, 2);
        assertTrue(this.bitSet.get(2L));
        this.bitSet.set(0, 1, 2);
        assertTrue(this.bitSet.get(66L));
    }

    @Test
    void testAbstractGet() {
        this.bitSet.set(2L);
        assertTrue(this.bitSet.get(0, 0, 2));
        this.bitSet.set(66L);
        assertTrue(this.bitSet.get(0, 1, 2));
    }

    @Test
    void testGetArrayIndex() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Method method = AbstractLongBitSet.class.getDeclaredMethod("getArrayIndex", long.class);
        method.setAccessible(true);
        long index = Integer.MAX_VALUE * 64L;
        System.out.println(" > " + index);
        int actual = (int) method.invoke(this.bitSet, index);
        assertEquals(1, actual);
        actual = (int) method.invoke(this.bitSet,0L);
        assertEquals(0, actual);
        actual = (int) method.invoke(this.bitSet, Integer.MAX_VALUE * 64L * 2);
        assertEquals(2, actual);
    }

    @Test
    void testLongIndex() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Method method = AbstractLongBitSet.class.getDeclaredMethod("getLongIndex", long.class);
        method.setAccessible(true);
        int actual = (int) method.invoke(this.bitSet, 63L);
        assertEquals(0, actual);
        actual = (int) method.invoke(this.bitSet,Integer.MAX_VALUE * 64L);
        assertEquals(0, actual);
        actual = (int) method.invoke(this.bitSet, 64L);
        assertEquals(1, actual);
    }

    @Test
    void testGetBitIndex() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Method method = AbstractLongBitSet.class.getDeclaredMethod("getBitIndex", long.class);
        method.setAccessible(true);
        int actual = (int) method.invoke(this.bitSet, 64);
        assertEquals(0, actual);
        actual = (int) method.invoke(this.bitSet,65);
        assertEquals(1, actual);
        actual = (int) method.invoke(this.bitSet, 2);
        assertEquals(2, actual);
    }
}
