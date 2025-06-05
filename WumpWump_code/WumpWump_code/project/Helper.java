package project;

import java.util.HashMap;
import java.util.function.BiPredicate;

public class Helper {
    public static final String[] DIRECTION_TO_NAME = new String[] {"NE", "N", "NW", "SW", "S", "SE"};
    public static final HashMap<String, Integer> NAME_TO_DIRECTION = new HashMap<>() {{
        put("NE", 0);
        put("N", 1);
        put("NW", 2);
        put("SW", 3);
        put("S", 4);
        put("SE", 5);
    }};

    public static int clamp(int val, int min, int max) {
        if (val < min) return min;
        if (val > max) return max;
        return  val;
    }

    public static boolean probabilityTest(float probability) { return GameManager.rand.nextFloat() <= probability; }

    public static void sleep(int millis) {
        try { Thread.sleep(millis); }
        catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public static <T> void quickSort(T[] arr, int low, int high, BiPredicate<Integer, Integer> lessThanPivot) {
        if (low < high) {
            int pi = partition(arr, low, high, lessThanPivot);

            quickSort(arr, low, pi - 1, lessThanPivot);
            quickSort(arr, pi + 1, high, lessThanPivot);
        }
    }

    private static <T> void swap(T[] arr, int i, int j) {
        var temp = arr[i];
        arr[i] = arr[j];
        arr[j] = temp;
    }

    private static <T> int partition(T[] arr, int low, int high, BiPredicate<Integer, Integer> lessThanPivot) {
        int i = low - 1;
        for (int j = low; j <= high - 1; j++) {
            if (lessThanPivot.test(j, high)) {
                i++;
                swap(arr, i, j);
            }
        }

        swap(arr, i + 1, high);
        return i + 1;
    }
}
