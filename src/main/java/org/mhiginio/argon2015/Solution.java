package org.mhiginio.argon2015;

import java.util.stream.IntStream;

class Solution {
    private static final int SEASIDE = 0;
    private static final int MOUNTAIN = 1;
    private int[] days;
    private IndexInformation index;

    public int solution(int[] days) {
        this.days = days;
        index = computeIndex();
        int maxDays = 0;
        for (int cutPoint = 1; cutPoint < days.length; cutPoint++) {
            if (index.freqFromBeginning[cutPoint][SEASIDE] == 0 || index.freqFromBeginning[cutPoint][MOUNTAIN] == 0) {
                continue;
            }
            int maxSeaside = maxSeasideDays(cutPoint);
            if (maxSeaside == 0) {
                continue;
            }
            int maxMountain = maxMountainDays(cutPoint);
            if (maxMountain == 0) {
                continue;
            }
            int vacationDays = maxSeaside + maxMountain;
            if (vacationDays > maxDays) {
                maxDays = vacationDays;
            }
        }
        return maxDays;
    }

    private IndexInformation computeIndex() {
        IndexInformation result = new IndexInformation(days.length);
        result.freqFromBeginning = iterate(IntStream.range(0, days.length));
        result.freqToEnd = iterate(IntStream.range(0, days.length).map(i -> days.length - i - 1));
        return result;
    }

    private int[][] iterate(IntStream range) {
        int[] counters = new int[2];
        int[][] result = new int[days.length][2];

        range.forEach(i -> {
            int weather = days[i];
            counters[weather]++;
            int[] copy = new int[2];
            System.arraycopy(counters, 0, copy, 0, 2);
            result[i] = copy;
        });
        return result;
    }

    private int maxSeasideDays(int cutPoint) {
        for (int i = 0; i < cutPoint; i++) {
            int difference = seasideMinusMountainsDaysBetween(i, cutPoint);
            if (difference < 0) {
                i = i - difference - 1;
            } else if (difference > 0) {
                return cutPoint - i;
            }
        }
        return 0;
    }

    private int seasideMinusMountainsDaysBetween(int init, int cutPoint) {
        if (init == 0) {
            return index.freqFromBeginning[cutPoint - 1][SEASIDE] -
                    index.freqFromBeginning[cutPoint - 1][MOUNTAIN];
        }
        return index.freqFromBeginning[cutPoint - 1][SEASIDE] - index.freqFromBeginning[init - 1][SEASIDE] -
                (index.freqFromBeginning[cutPoint - 1][MOUNTAIN] - index.freqFromBeginning[init - 1][MOUNTAIN]);
    }


    private int maxMountainDays(int cutPoint) {
        for (int i = days.length; i >cutPoint; i--) {
            int difference = mountainMinusSeasideDaysBetween(cutPoint, i);
            if (difference < 0) {
                i = i + difference + 1;
            } else if (difference > 0) {
                return i - cutPoint;
            }
        }
        return 0;
    }

    private int mountainMinusSeasideDaysBetween(int cutPoint, int end) {
        if (end == days.length) {
            return index.freqToEnd[cutPoint][MOUNTAIN] -
                    index.freqToEnd[cutPoint][SEASIDE];
        }
        return index.freqToEnd[cutPoint][MOUNTAIN] - index.freqToEnd[end][MOUNTAIN] -
                (index.freqToEnd[cutPoint][SEASIDE] - index.freqToEnd[end][SEASIDE]);
    }

    private static class IndexInformation {
        private int[][] freqFromBeginning;
        private int[][] freqToEnd;

        public IndexInformation(int length) {
            freqFromBeginning = new int[length][2];
            freqToEnd = new int[length][2];
        }
    }
}
