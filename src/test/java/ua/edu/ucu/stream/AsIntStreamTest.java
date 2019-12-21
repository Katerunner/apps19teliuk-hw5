package ua.edu.ucu.stream;

import org.junit.Before;
import org.junit.Test;

import java.util.NoSuchElementException;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

public class AsIntStreamTest {
    private IntStream intStream1;
    private IntStream intStream2;
    private IntStream intStream3;
    private int[] intArr1 = {-2, -1, 0, 1, 2, 3, 100};
    private int[] intArr2 = {};
    private int[] intArr3 = {10, 11, 13};

    @Before
    public void init() {
        intStream1 = AsIntStream.of(intArr1);
        intStream2 = AsIntStream.of(intArr2);
        intStream3 = AsIntStream.of(intArr3);
    }

    @Test
    public void testOf() {
        AsIntStream.of(intArr1);
        AsIntStream.of(intArr2);
        AsIntStream.of(intArr3);
    }

    @Test
    public void testAverage() {
        Double actual = intStream1.average();
        assertEquals(14.71428, (double) actual, 0.0001);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testException() {
        Double actual = intStream2.average();
        assertEquals(14.0, (double) actual, 0.0001);
    }

    @Test
    public void testMax() {
        int actual1 = intStream1.max();
        int actual2 = intStream3.max();
        int actual3 = AsIntStream.of(1, 2, 0, 2).max();

        assertEquals(100, actual1);
        assertEquals(13, actual2);
        assertEquals(2, actual3);
    }

    @Test
    public void testMin() {
        int actual1 = intStream1.min();
        int actual2 = intStream3.min();
        assertEquals(-2, actual1);
        assertEquals(10, actual2);
    }

    @Test
    public void testCount() {
        long actual1 = intStream1.count();
        long actual2 = intStream3.count();
        assertEquals(7, actual1);
        assertEquals(3, actual2);
    }

    @Test
    public void testSum() {
        int actual1 = intStream1.sum();
        int actual2 = intStream3.sum();
        assertEquals(103, actual1);
        assertEquals(34, actual2);
    }

    @Test
    public void testFilter() {
        int actual1 = intStream1.filter(x -> x > 10).sum();
        int actual2 = intStream3.filter(x -> x > 12).sum();
        assertEquals(100, actual1);
        assertEquals(13, actual2);
    }



    @Test(expected = IllegalArgumentException.class)
    public void testFilterExceptionNoElements() {
        intStream2.filter(x -> x > 13).sum();
    }

    @Test
    public void testForEach() {
        StringBuilder str = new StringBuilder();
        intStream1.forEach(str::append);
        assertEquals("-2-10123100", str.toString());
    }

    @Test
    public void testMap() {
        int actual1 = intStream1.map(x -> x * 2).sum();
        int actual2 = intStream3.map(x -> x * x).sum();
        assertEquals(206, actual1);
        assertEquals(390, actual2);
    }

    @Test
    public void testFlatMap() {
        int actual1 = intStream1.flatMap(x -> AsIntStream.of(x - 56, x, x * 2)).sum();
        int actual2 = intStream3.flatMap(x -> AsIntStream.of(x - 1, x, x * x)).sum();
        assertEquals(20, actual1);
        assertEquals(455, actual2);
    }

    @Test
    public void testReduce() {
        int actual1 = intStream1.reduce(0, (sum, x) -> sum += x);
        int actual2 = intStream3.reduce(0, (sum, x) -> sum -= x);
        int actual3 = AsIntStream.of(1, 2, 0, 2)
                .filter(x -> x > 1)
                .map(x -> x * x)
                .flatMap(x -> AsIntStream.of(x - 1, x, x + 1))
                .reduce(0, (sum, x) -> sum += x);
        int actual4 = AsIntStream.of(1, 2, 0, 2)
                .map(x -> x * x)
                .filter(x -> x > 1)
                .filter(x -> x > 1)
                .map(x -> x * 2)
                .reduce(0, (sum, x) -> sum += x);
        assertEquals(103, actual1);
        assertEquals(-34, actual2);
        assertEquals(24, actual3);
        assertEquals(16, actual4);
    }

    @Test
    public void testToArray() {
        int[] actual1 = intStream1.toArray();
        int[] actual2 = intStream2.toArray();
        int[] actual3 = intStream3.toArray();
        assertArrayEquals(new int[]{-2, -1, 0, 1, 2, 3, 100}, actual1);
        assertArrayEquals(new int[]{}, actual2);
        assertArrayEquals(new int[]{10, 11, 13}, actual3);
    }
}