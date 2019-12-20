package com.thomax.letsgo.zoom.handling;

public class PrintGraph {
	public static void main(String[] args) {
//		Diamond example = new Diamond();
//		example.main(args);
	}
}


/**class1-打印菱形*/
class Diamond{
    public static void main(String[] args) {
        print(8); // 输出 8 行的菱形
    }
 
    public static void print(int size) {
        if (size % 2 == 0) {
            size++; // 计算菱形大小
        }
        //打印上半部分 (屏蔽掉上半部分代码即可打印倒三角形)
        for (int i = 0; i < size / 2 + 1; i++) {
            for (int j = size / 2 + 1; j > i + 1; j--) {
                System.out.print(" "); // 输出左上角位置的空白
            }
            for (int j = 0; j < 2 * i + 1; j++) {
                System.out.print("*"); // 输出菱形上半部边缘
            }
            System.out.println();
        }
        //打印下半部分 (屏蔽掉下半部分代码即可打印正三角形)
        for (int i = size / 2 + 1; i < size; i++) {
            for (int j = 0; j < i - size / 2; j++) {
                System.out.print(" "); // 输出菱形左下角空白
            }
            for (int j = 0; j < 2 * size - 1 - 2 * i; j++) {
                System.out.print("*"); // 输出菱形下半部边缘
            }
            System.out.println();
        }
    }
}
//    *
//   ***
//  *****
// *******
//*********
// *******
//  *****
//   ***
//    *


/**class2-打印平行四边形*/
class Parallelogram {
	public static void main(String[] args) {
		print(5);
	}
    public static void print(int size) {
        for (int i = 1; i <=size; i++) {
            //填充空格
            for (int j = 1; j <= size - i; j++) {
                System.out.print(" ");
            }
            //内层循环 每次打印一个*
            for (int k = 1; k <= size; k++) {
                System.out.print("*");
            }
            System.out.println();
        }
    }
}
//    *****
//   *****
//  *****
// *****
//*****


/**class3-九九乘法表*/
class MultiplicationTable {
    public static void main(String[] args) {
        for(int i=1;i<=9;i++) {
            for(int j=1;j<=i;j++) {
                System.out.print(j+"x"+i+"="+i*j+"\t");// \t 跳到下一个TAB位置
            }
            System.out.println();
        }
    }
}
//1x1=1	
//1x2=2	2x2=4	
//1x3=3	2x3=6	3x3=9	
//1x4=4	2x4=8	3x4=12	4x4=16	
//1x5=5	2x5=10	3x5=15	4x5=20	5x5=25	
//1x6=6	2x6=12	3x6=18	4x6=24	5x6=30	6x6=36	
//1x7=7	2x7=14	3x7=21	4x7=28	5x7=35	6x7=42	7x7=49	
//1x8=8	2x8=16	3x8=24	4x8=32	5x8=40	6x8=48	7x8=56	8x8=64	
//1x9=9	2x9=18	3x9=27	4x9=36	5x9=45	6x9=54	7x9=63	8x9=72	9x9=81






















