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

    private void setIterator(Iterator<Integer> array, int size) {
        this.array = array;
        this.size = size;
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
        return (double) (sumA / count);
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
            private int ind = 0;
            private int nOfSuc = 0;
            private Integer preNext;
            private boolean proceed = false;

            @Override
            public boolean hasNext() {
                boolean mainCond = ind < size;
                if (!mainCond) {
                    return false;
                }
                if (proceed) {
                    preNext = preGet();
                    if (preNext == null) {
                        return false;
                    }

                }
                return true;
            }

            private Integer preGet() {
                try {
                    return array.next();
                } catch (NoSuchElementException exp) {
                    return null;
                }
            }

            @Override
            public Integer next() {
//                System.out.print(ind);
//                System.out.print(" Filter\n");
                proceed = true;
                if (hasNext()) {
                    ind++;
                    int value = preNext;
                    if (predicate.test(value)) {
                        proceed = false;
                        nOfSuc++;
                        return value;
                    } else {
                        proceed = false;
                        return next();
                    }
                }
                proceed = false;
                throw new IllegalArgumentException();
            }
        }

        AsIntStream toRes = new AsIntStream();
        toRes.setIterator(new FilterIterator(), size);
        return toRes;

//        AsIntStream toRes = new AsIntStream();
//        for (int value : array) {
//            if (predicate.test(value)) {
//                toRes.array.add(value);
//            }
//        }
//        return toRes;

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
                int value = array.next();
//                System.out.println(value);
                return mapper.apply(value);
            }
        }

        AsIntStream toRes = new AsIntStream();
        toRes.setIterator(new MapperIterator(), size);
        return toRes;


//        AsIntStream toRes = new AsIntStream();
//        array.forEach(
//                x -> {
//                    toRes.array.add(mapper.apply(x));
//                });
//        return toRes;

    }

    @Override
    public IntStream flatMap(IntToIntStreamFunction func) {

        ArrayList<Integer> cache = new ArrayList<>();
        ArrayList<Integer> curStream = new ArrayList<>();
        while (array.hasNext()) {
            curStream.add(array.next());
        }
        curStream.forEach(
                x -> {
                    AsIntStream asIntStream = (AsIntStream) func.applyAsIntStream(x);
                    IntConsumer action = cache::add;
                    asIntStream.forEach(action);
                });
        int[] intArray = new int[cache.size()];
        int value = 0;
        while (value < cache.size()) {
            intArray[value] = cache.get(value);
            value++;
        }
        return AsIntStream.of(intArray);

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
