import island.IOEncapsulation.Polygon;

import java.util.List;

public class sort_test {
    public void sort_base_elevation() {


    }

    public static void main(String[] args) {
        int[] arr = new int[]{1,22,5,61,9};


        // One by one move boundary of unsorted sub-array
        for (int i = 0; i < arr.length; i++) {
            // Find the maximum element in unsorted array
            int min_idx = i;
            for (int j = i + 1; j < arr.length; j++) {
                if (arr[j]< arr[min_idx] ){
                    min_idx = j;
                }


                // Swap the found maximum element with the first element
                int temp = arr[min_idx];
                arr[min_idx] = arr[i];
                arr[i] = temp;


            }

        }
        for (int i = 0; i < arr.length; i++) {
            System.out.println(arr[i]);
        }
    }
}
