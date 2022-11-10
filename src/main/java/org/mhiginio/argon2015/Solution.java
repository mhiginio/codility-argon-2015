package org.mhiginio.argon2015;

import java.util.LinkedList;

class Solution {
    private static final int SEASIDE = 0;

    private int[] days;
    private IndexInformation index;

    public int solution(int[] days) {
        this.days = days;
        computeIndex();
        int maxDays = 0;
        for (Integer cutPoint : index.cutPoints) {
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

    private void computeIndex() {
        index = new IndexInformation(days.length);
        indexFromBeginning();
        indexToEnd();
    }

    private void indexFromBeginning() {
        int lastWeather = days[0];
        for (int i = 0; i < days.length; i++) {
            int weather = days[i];
            if (weather == SEASIDE) {
                index.seasideMinusMountainFromBeginning[i] =
                        i - 1 < 0 ? 1 : index.seasideMinusMountainFromBeginning[i - 1] + 1;
            } else {
                index.seasideMinusMountainFromBeginning[i] =
                        i - 1 < 0 ? -1 : index.seasideMinusMountainFromBeginning[i - 1] - 1;
            }
            if (weather != lastWeather) {
                // If a solution splits a sequence of the same value between seaside / mountain periods
                // We can obtain another solution of the same length which doesn't split the sequence
                // So we don't need to check the points inside these chains. Example:
                // <things>111 |here start mountain period| 111<otherthings>
                // Then <things>|here start mountain period|111111<otherthings> is a solution as well.
                index.cutPoints.add(i);
                lastWeather = weather;
            }
        }
    }

    private void indexToEnd() {
        for (int i = days.length - 1; i >= 0; i--) {
            int weather = days[i];
            if (weather == SEASIDE) {
                index.seasideMinusMountainToEnd[i] =
                        i + 1 >= days.length ? 1 : index.seasideMinusMountainToEnd[i + 1] + 1;
            } else {
                index.seasideMinusMountainToEnd[i] =
                        i + 1 >= days.length ? -1 : index.seasideMinusMountainToEnd[i + 1] - 1;
            }
        }
    }

    private int maxSeasideDays(int cutPoint) {
        for (int i = 0; i < cutPoint; i++) {
            int difference = seasideMinusMountainsDaysBetween(i, cutPoint);
            if (difference < 0) {
                i = i - difference;
            } else if (difference > 0) {
                return cutPoint - i;
            }
        }
        return 0;
    }

    private int seasideMinusMountainsDaysBetween(int init, int cutPoint) {
        if (init == 0) {
            return index.seasideMinusMountainFromBeginning[cutPoint - 1];
        }
        return index.seasideMinusMountainFromBeginning[cutPoint - 1] -
                index.seasideMinusMountainFromBeginning[init - 1];
    }


    private int maxMountainDays(int cutPoint) {
        for (int i = days.length; i > cutPoint; i--) {
            int difference = mountainMinusSeasideDaysBetween(cutPoint, i);
            if (difference < 0) {
                i = i + difference;
            } else if (difference > 0) {
                return i - cutPoint;
            }
        }
        return 0;
    }

    private int mountainMinusSeasideDaysBetween(int cutPoint, int end) {
        if (end == days.length) {
            return -index.seasideMinusMountainToEnd[cutPoint];
        }
        return index.seasideMinusMountainToEnd[end] - index.seasideMinusMountainToEnd[cutPoint];
    }

    private static class IndexInformation {
        private final int[] seasideMinusMountainFromBeginning;
        private final int[] seasideMinusMountainToEnd;
        private final LinkedList<Integer> cutPoints = new LinkedList<>();

        public IndexInformation(int length) {
            seasideMinusMountainFromBeginning = new int[length];
            seasideMinusMountainToEnd = new int[length];
        }
    }
}
