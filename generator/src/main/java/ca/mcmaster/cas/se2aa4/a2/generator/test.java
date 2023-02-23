package ca.mcmaster.cas.se2aa4.a2.generator;

public class test {
     static int idx2D_to1D(int[] arr){
        int idx = 0;
        if(arr[0]>1){
            idx = arr[0]*50000+arr[1];
            return idx;
        }else {
            idx = arr[1];
            return idx;
        }
    }
     static int[] idx1D_to2D(int n){
        int raw=0;
        int column=0;
        int[] index_2D = new int[2];

        if(n<=50000){
            column = n;
            index_2D[0] =raw;
            index_2D[1] = column;
            return index_2D;
        }else{
            raw = n/50000;
            column = n%50000;
            index_2D[0] = raw;
            index_2D[1] = column;
            return index_2D;
        }
    }
    public static void main(String[] args) {
        for (int n:
             idx1D_to2D(62000)) {
            System.out.println(n);
        }
        int[] test =  new int[]{0,40};
        System.out.println(idx2D_to1D(test));
    }

}
