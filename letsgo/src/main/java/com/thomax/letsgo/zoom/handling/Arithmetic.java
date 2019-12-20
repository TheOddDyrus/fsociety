package com.thomax.letsgo.zoom.handling;

public class Arithmetic {
    public static void main(String[] args) {
        TowerOfHanoi aa = new TowerOfHanoi();
        aa.runTower();
    }
}

/**
 * class1-汉诺塔算法
 */
//三根柱子A B C 
class TowerOfHanoi {
    public void runTower() {
        int nDisks = 4;  //四个盘子编号1 2 3 4, 1号在最上面
        doTowers(nDisks, 'A', 'B', 'C');
    }

    public void doTowers(int topN, char from, char inter, char to) {
        if (topN == 1) {
            System.out.println("Disk 1 from " + from + " to " + to);
        } else {
            doTowers(topN - 1, from, to, inter);
            System.out.println("Disk " + topN + " from " + from + " to " + to);
            doTowers(topN - 1, inter, from, to);
        }
    }
}
//Disk 1 from A to B
//Disk 2 from A to C
//Disk 1 from B to C
//Disk 3 from A to B
//Disk 1 from C to A
//Disk 2 from C to B
//Disk 1 from A to B
//Disk 4 from A to C
//Disk 1 from B to C
//Disk 2 from B to A
//Disk 1 from C to A
//Disk 3 from B to C
//Disk 1 from A to B
//Disk 2 from A to C
//Disk 1 from B to C


/**
 * class2-斐波那契数列
 */
//斐波纳契数列以如下被以递归的方法定义：F(0)=0，F(1)=1, F(n)=F(n-1)+F(n-2)（n>=2，n∈N*）
class FibonacciSequence {
    public void runFibonacci() {
        for (int counter = 0; counter <= 10; counter++) {
            System.out.printf("F(%d) = %d\n", counter, fibonacci(counter));
        }
    }

    public long fibonacci(long number) {
        if ((number == 0) || (number == 1))
            return number;
        else
            return fibonacci(number - 1) + fibonacci(number - 2);
    }
}
//F(0) = 0
//F(1) = 1
//F(2) = 1
//F(3) = 2
//F(4) = 3
//F(5) = 5
//F(6) = 8
//F(7) = 13
//F(8) = 21
//F(9) = 34
//F(10) = 55


/**
 * class3-阶乘
 */
class Factorial {
    public void runFactorial() {
        int num = 10;
        for (int counter = 0; counter <= 10; counter++) {
            System.out.printf("%d! = %d\n", counter, factorial(counter));
        }
        System.out.printf("%d! = %d\n", num, factorial2(num));
    }

    public long factorial(long number) {
        if (number <= 1)
            return 1;
        else
            return number * factorial(number - 1);
    }

    public long factorial2(long number) {
        long returnNumber = 1;
        if (number <= 1)
            return 1;
        else
            while (number > 1) {  //超过一行的条件判断语句都要加{}方法体
                returnNumber *= number;
                number--;
            }
        return returnNumber;
    }
}
//0! = 1
//1! = 1
//2! = 2
//3! = 6
//4! = 24
//5! = 120
//6! = 720
//7! = 5040
//8! = 40320
//9! = 362880
//10! = 3628800
//10! = 3628800


/**
 * class4-利用标签嵌套循环扫描字符串是否存在
 */
class NestLoop {
    public static void main(String[] args) {
        String strSearch = "This is the string in which you have to search for a substring.";
        String substring = "substring";
        boolean found = false;
        int max = strSearch.length() - substring.length();
        testlbl:
        for (int i = 0; i <= max; i++) {
            int length = substring.length();
            int j = i;
            int k = 0;
            while (length-- != 0) {
                if (strSearch.charAt(j++) != substring.charAt(k++)) {
                    continue testlbl;
                }
            }
            found = true;
            break testlbl;
        }
        if (found) {
            System.out.println("发现子字符串。");
        } else {
            System.out.println("字符串中没有发现子字符串。");
        }

        System.out.println("substring中charAt(0)的字符串： " + substring.charAt(0));
    }
}









