package com.zmk.github.test.算法.树;

import com.alibaba.fastjson.JSON;

public class maopaoTest {

    public static void bubleSour(int[] list) {
        int temp=0;// 用来交换的临时数
        for(int i=0;i<list.length-1;i++){
            for(int j=list.length-1;j>i;j--){
                // 比较相邻的元素，如果前面的数大于后面的数，则交换

                if(list[j-1]>list[j]){
                    temp=list[j-1];
                    list[j-1]=list[j];
                    list[j]=temp;
                }
                System.out.format("第 %d 趟：\t",i);
                System.out.println(JSON.toJSON(list));
            }
        }
    }
    public static void main(String[] args) {
        int [] arr=new int[5];
        arr[0]=12;
        arr[1]=21;
        arr[2]=33;
        arr[3]=34;
        arr[4]=9;

        bubleSour(arr);
    }
}
