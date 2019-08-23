package org.proli.avproject.practice;

import org.junit.Test;

import static org.junit.Assert.*;

public class QuickSortTest {

    @Test
    public void quickSort() {

        int[] numbers = {32, 43, 23, 13, 5};
        QuickSort.quickSort(numbers, 0, 4);
        for (int number : numbers) {
            System.out.println(number);
        }

    }
}