package com.meidusa.amoeba.aladdin.test;

import java.util.Arrays;

public class ArrayToString {
	public static void main(String args[]){
		int count=10;
		byte[] arr = new byte[count];
		for(int i=0;i<count;i++)
		{
			arr[i] |= (1 << (i & 7));
			System.out.print(arr[i]+" ");
		}
		System.out.println();
		System.out.println(Arrays.toString(arr));
//		int parameterCount = 12;
//        int nullCount = (parameterCount + 7) / 8;
//        byte[] nullBitsBuffer = new byte[nullCount];
//
//        for (int i = 0; i < parameterCount; i++) {
//            nullBitsBuffer[i] |= (1 << (i & 7));
//            System.out.print(nullBitsBuffer[i / 8]+" ");
//        }
//        System.out.println();
//        System.out.println(Arrays.toString(nullBitsBuffer));
	}
}
