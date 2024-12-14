package utils;

import java.util.List;

public class StatsUtil {
    public static double getExpectedMeanRoll(int sides){
        return (sides+1)/2.0;
    }
    public static double getMean(int[] rolls, int sides){
        if(rolls.length == 0){
            return getExpectedMeanRoll(sides);
        }
        int sum = 0;
        for (int roll : rolls) sum += roll;
        return (sum*1.0d)/rolls.length;
    }
    public static double getSum(int[] rolls, int sides){
        if(rolls.length == 0){
            return getExpectedMeanRoll(sides)*rolls.length;
        }
        int sum = 0;
        for (int roll : rolls) sum += roll;
        return sum*1.0d;
    }
    public static double getLuck(int[] rolls, int sides){
        double sum = getSum(rolls,sides);
        double expectedTotal = getExpectedMeanRoll(sides)*rolls.length;
        double expectedVariance = getVariance(sides) * rolls.length;
        double standardDeviation = Math.sqrt(expectedVariance);
        return (sum-expectedTotal)/standardDeviation;
    }
    public static double getVariance(int sides){
        return (sides*sides-1)/12.0;
    }
    public static double getStdDev(int sides){
        return Math.sqrt(getVariance(sides));
    }
}
