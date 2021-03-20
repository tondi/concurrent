package com.agh;

public class Main {
    public static double x(int n) {
        if(n == 0) return 0.01;

        double prev = x(n - 1);
        return prev + 3.0 * prev * (1 - prev);
    }

    public static float xFloat(int n) {
        if(n == 0) return 0.01f;

        float prev = xFloat(n - 1);
        return prev + 3.0f * prev * (1 - prev);
    }

    public static double xTwo(int n) {
        if(n == 0) return 0.01;

        double prev = xTwo(n - 1);
        return prev + 4.0 * prev - 3.0 * prev * prev;
    }

    public static float xTwoFloat(int n) {
        if(n == 0) return 0.01f;

        float prev = xTwoFloat(n - 1);
        return prev + 4.0f * prev - 3.0f * prev * prev;
    }

    public static void main(String[] args) {
        System.out.println("double: " + x(10) + " float: " + xFloat(10));
        System.out.println(xTwo(10) + " " + xTwoFloat(10));

        double a = 0.1;
        while(a + 1 > 1) {
            a = a / 2;
        }
        System.out.println("Epsilon: " + a);
    }
}

// 1.
// x(10)
// 0.722914301179573 0.7229306

// 2.
// -4.150000545482118E27 -4.1499575E27

//3. 8.881784197001253E-17

