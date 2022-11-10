package org.mhiginio.argon2015;

import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;

class SolutionTest {
    private final Solution solution = new Solution();


    @Test
    void test_11010011_should_return_7() {
        assertEquals(7, solution.solution(new int[]{1, 1, 0, 1, 0, 0, 1, 1}));
    }

    @Test
    void test_1100_should_return_0() {
        assertEquals(0, solution.solution(new int[]{1, 1, 0, 0}));
    }

    @Test
    void test_01_should_return_2() {
        assertEquals(2, solution.solution(new int[]{0, 1}));
    }

    @Test
    void test_big1_should_return_0() {
        int[] bigOne = new int[100000];
        Arrays.fill(bigOne, 1);
        assertEquals(0, solution.solution(bigOne));
    }

    @Test
    void test_big01_should_return_size() {
        int SIZE = 100000;
        assertEquals(0, SIZE % 2, "SIZE must be pair!");
        int[] big = new int[SIZE];
        for (int i = 1; i < big.length; i = i + 2) {
            big[i] = 1;
        }
        assertEquals(SIZE, solution.solution(big));
    }

    @Test
    void test_1000100000_should_return_5() {
        int[] test = new int[]{1, 0, 0, 0, 1, 0, 0, 0, 0, 0};
        assertEquals(5, solution.solution(test));
    }

//       @Test
//    void one_hundred_random_tests() {
//        for (int test = 0; test < 1000; test++) {
//            int SIZE = 10000;
//            int[] random = new int[SIZE];
//            for (int i = 0; i < random.length; i++) {
//                random[i] = Math.random() > 0.5 ? 1 : 0;
//            }
//            Collector<CharSequence, ?, String> joining = Collectors.joining(",", "int[] test = new int[]{", "};");
//            System.out.println(Arrays.stream(random).mapToObj(Integer::toString).collect(joining));
//            solution.solution(random);
//        }
//    }
}
