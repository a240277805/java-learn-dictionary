package com.zmk.github.test.算法.树;

import java.util.Arrays;

public class HeapSortTest {

    /**
     * 堆排序
     * @param arr 待排序的数组
     */
    public static void sort(int [] arr){

        if(arr.length<=1)return;

        //首次调整大顶堆
        adjustHeap(arr,arr.length);

        for(int i=arr.length-1;i>=0;i--){
            swap(arr,i,0);
            adjustHeap(arr,i);
        }
    }

    /**
     * 调整大顶堆
     * @param arr 待排序数组
     * @param length 待调整的大顶堆的数组位数
     */
    public static void adjustHeap(int[] arr,int length){
        //从最后一个非子节点开始
        for(int i=length/2-1;i>=0;i--){
            swap3Point(arr,length,i);
        }
    }

    /**
     * (递归)将最大的放在二叉树三个节点的根节点
     *
     * 比较三个节点
     * 先比较两个子节点，找到最大的
     * 最大的节点和父节点比较，
     * 如果替换了，就讲那个子节点递归
     * @param arr
     * @param currentPoint
     */
    public static void swap3Point(int[] arr,int length,int currentPoint){

        //左子节点下标
        int leftPoint=2*currentPoint+1;
        //右子节点下标
        int rightPoint=2*currentPoint+2;
        //待替换的下标
        int swapPoint=leftPoint;
        //如果越界，就不用比较了
        if(leftPoint>length-1){
            return;
        }
        //(右子节点不越界则)判断他的两个子节点大小
        if(rightPoint<=length-1&&arr[leftPoint]<arr[rightPoint]){
            swapPoint=rightPoint;
        }

        //两个子节点中较大的和根节点比较，并替换，如替换，还要考虑被替换的子节点下的子节点
        if(arr[currentPoint]<arr[swapPoint]){
           swap(arr,swapPoint,currentPoint);
            swap3Point(arr,length,swapPoint);
        }

    }

    /**
     * 置换方法
     * @param arr
     * @param a
     * @param b
     */
    public static void swap(int [] arr,int a,int b){
        int temp =arr[a];
        arr[a]=arr[b];
        arr[b]=temp;
    }


    public static void main(String[] args) {
        int []arr = {9,8,7,36,5,334,3,2,23,44,123,55,62,77,1,32,33,2344,3123,52345,31231,3123,1};
//        int []arr = {9,8,7};
        sort(arr);
        System.out.println(Arrays.toString(arr));
    }
}
