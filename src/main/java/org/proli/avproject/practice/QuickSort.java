package org.proli.avproject.practice;


/**
 * 快速排序
 */
public class QuickSort {

    public static void main(String[] args) {

        int[] numbers = {32, 43, 23, 13, 5};

        quickSort(numbers, 0, 4);

        for (int number : numbers) {
            System.out.println(number);
        }
    }

    public static void quickSort(int[] numbers, int start, int end) {
        if (start < end) {
            // 选定的基准值（第一个数值作为基准值）
            int base = numbers[start];
            int temp; // 记录临时中间值
            int i = start, j = end;
            do {
                while ((numbers[i] < base) && (i < end)) {
                    i++;
                }
                while ((numbers[j] > base) && (j > start)) {
                    j--;
                }
                if (i <= j) {
                    temp = numbers[i];
                    numbers[i] = numbers[j];
                    numbers[j] = temp;
                    i++;
                    j--;
                }
            } while (i <= j);
            if (start < j) {
                quickSort(numbers, start, j);
            }
            if (end > i) {
                quickSort(numbers, i, end);
            }
        }
    }
}
