package ua.edu.ucu.stream;

import ua.edu.ucu.function.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.NoSuchElementException;

public class AsIntStream implements IntStream {
    private Iterator<Integer> array;
    private int size;


    private AsIntStream() {
        array = Collections.emptyIterator();
        size = 0;
    }

    private void setIterator(Iterator<Integer> inpArr, int inpSize) {
        this.array = inpArr;
        this.size = inpSize;
    }

    private Iterator<Integer> getIterator() {
        return array;
    }

    private void checkSize() throws IllegalArgumentException {
        if (size <= 0) {
            throw new IllegalArgumentException();
        }
    }

    public static IntStream of(int... values) {
        AsIntStream instance = new AsIntStream();
        ArrayList<Integer> newList = new ArrayList<>();
        for (int value : values) {
            newList.add(value);
        }
        instance.setIterator(newList.iterator(), values.length);
        return instance;
    }

    @Override
    public Double average() {
        checkSize();
        int sumA = 0;
        int count = 0;
        for (Iterator<Integer> it = array; it.hasNext(); ) {
            int value = it.next();
            sumA = sumA + value;
            count++;
        }
        return (double) sumA / (double) count;
    }

    @Override
    public Integer max() {
        checkSize();
        int maxi = Integer.MIN_VALUE;
        for (Iterator<Integer> it = array; it.hasNext(); ) {
            int value = it.next();
            if (value > maxi) {
                maxi = value;
            }
        }
        return maxi;
    }

    @Override
    public Integer min() {
        checkSize();
        int mini = Integer.MAX_VALUE;
        for (Iterator<Integer> it = array; it.hasNext(); ) {
            int value = it.next();
            if (value < mini) {
                mini = value;
            }
        }
        return mini;
    }

    @Override
    public long count() {
        checkSize();
        int count = 0;
        for (Iterator<Integer> it = array; it.hasNext(); ) {
            it.next();
            count++;
        }
        return count;
    }

    @Override
    public Integer sum() {
        checkSize();
        int sumS = 0;
        for (Iterator<Integer> it = array; it.hasNext(); ) {
            int value = it.next();
            sumS = sumS + value;
        }
        return sumS;
    }

    @Override
    public IntStream filter(IntPredicate predicate) {
        class FilterIterator implements Iterator<Integer> {
            private Integer value;
            private int nOfSuc;

            @Override
            public boolean hasNext() {
                while (array.hasNext()) {
                    value = array.next();
                    if (predicate.test(value)) {
                        return true;
                    }
                }
                if (nOfSuc == 0) {
                    throw new IllegalArgumentException();
                }
                return false;
            }


            @Override
            public Integer next() {
                nOfSuc++;
                if (value == null) {
                    throw new NoSuchElementException();
                }
                return value.intValue();
            }
        }

        AsIntStream toRes = new AsIntStream();
        toRes.setIterator(new

                FilterIterator(), size);
        return toRes;
    }

    @Override
    public void forEach(IntConsumer action) {
        for (Iterator<Integer> it = array; it.hasNext(); ) {
            Integer value;
            value = it.next();
            action.accept(value);
        }
    }

    @Override
    public IntStream map(IntUnaryOperator mapper) {

        class MapperIterator implements Iterator<Integer> {

            @Override
            public boolean hasNext() {
                return array.hasNext();
            }

            @Override
            public Integer next() {
                return mapper.apply(array.next());
            }
        }

        AsIntStream toRes = new AsIntStream();
        toRes.setIterator(new MapperIterator(), size);
        return toRes;
    }

    @Override
    public IntStream flatMap(IntToIntStreamFunction func) {

        class FlatMapIterator implements Iterator<Integer> {
            private Iterator<Integer> toRet = null;

            public boolean hasNext() {
                if (toRet != null && toRet.hasNext()) {
                    return true;
                }
                if (array.hasNext()) {
                    toRet = ((AsIntStream)
                            func.applyAsIntStream(
                                    array.next()))
                            .getIterator();
                    return true;
                }
                return false;
            }

            public Integer next() {
                return toRet.next();
            }
        }

        AsIntStream toRes = new AsIntStream();
        toRes.setIterator(new FlatMapIterator(), size);
        return toRes;
    }

    @Override
    public int reduce(int identity, IntBinaryOperator op) {
        int toRes = identity;
        for (Iterator<Integer> it = array; it.hasNext(); ) {
            int element = it.next();
            toRes = op.apply(toRes, element);
        }
        return toRes;
    }

    @Override
    public int[] toArray() {
        int[] toRes = new int[size];
        int i = 0;
        for (Iterator<Integer> it = array; it.hasNext(); ) {
            int value = it.next();
            toRes[i] = value;
            i++;
        }
        return toRes;
    }

}
