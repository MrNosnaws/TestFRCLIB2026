package frc.robot.lib.statistics;

import java.util.function.Function;

public class Regression {

    private final Function<Double, Double> function;

    public Regression(double[] xData, double[] yData, RegressionType type) {
        switch (type) {
            case LINEAR:
                function = linearRegression(xData, yData);
                break;
            case EXPONENTIAL:
                function = exponentialRegression(xData, yData);
                break;
            case POWER:
                function = powerRegression(xData, yData);
                break;
            case LOGARITHMIC:
                function = logarithmicRegression(xData, yData);
                break;
            default:
                throw new IllegalArgumentException("Unsupported regression type: " + type);
        }
    }

    private Function<Double, Double> linearRegression(double[] xData, double[] yData) {
        int n = xData.length;
        double sumX = 0, sumY = 0, sumXY = 0, sumX2 = 0;

        for (int i = 0; i < n; i++) {
            sumX += xData[i];
            sumY += yData[i];
            sumXY += xData[i] * yData[i];
            sumX2 += xData[i] * xData[i];
        }

        double slope = (n * sumXY - sumX * sumY) / (n * sumX2 - sumX * sumX);
        double intercept = (sumY - slope * sumX) / n;

        return x -> slope * x + intercept;
    }

    private Function<Double, Double> exponentialRegression(double[] xData, double[] yData) {
        int n = xData.length;
        double sumX = 0, sumLnY = 0, sumXLnY = 0, sumX2 = 0;

        for (int i = 0; i < n; i++) {
            sumX += xData[i];
            sumLnY += Math.log(yData[i]);
            sumXLnY += xData[i] * Math.log(yData[i]);
            sumX2 += xData[i] * xData[i];
        }

        double slope = (n * sumXLnY - sumX * sumLnY) / (n * sumX2 - sumX * sumX);
        double intercept = (sumLnY - slope * sumX) / n;

        return x -> Math.exp(intercept) * Math.exp(slope * x);
    }

    private Function<Double, Double> powerRegression(double[] xData, double[] yData) {
        int n = xData.length;
        double sumLnX = 0, sumLnY = 0, sumLnXLnY = 0, sumLnX2 = 0;

        for (int i = 0; i < n; i++) {
            sumLnX += Math.log(xData[i]);
            sumLnY += Math.log(yData[i]);
            sumLnXLnY += Math.log(xData[i]) * Math.log(yData[i]);
            sumLnX2 += Math.log(xData[i]) * Math.log(xData[i]);
        }

        double slope = (n * sumLnXLnY - sumLnX * sumLnY) / (n * sumLnX2 - sumLnX * sumLnX);
        double intercept = (sumLnY - slope * sumLnX) / n;

        return x -> Math.exp(intercept) * Math.pow(x, slope);
    }

    private Function<Double, Double> logarithmicRegression(double[] xData, double[] yData) {
        int n = xData.length;
        double sumLnX = 0, sumY = 0, sumLnXY = 0, sumLnX2 = 0;

        for (int i = 0; i < n; i++) {
            sumLnX += Math.log(xData[i]);
            sumY += yData[i];
            sumLnXY += Math.log(xData[i]) * yData[i];
            sumLnX2 += Math.log(xData[i]) * Math.log(xData[i]);
        }

        double slope = (n * sumLnXY - sumLnX * sumY) / (n * sumLnX2 - sumLnX * sumLnX);
        double intercept = (sumY - slope * sumLnX) / n;

        return x -> intercept + slope * Math.log(x);
    }

    public double predict(double x) {
        return function.apply(x);
    }

    public enum RegressionType {
        LINEAR,
        EXPONENTIAL,
        POWER,
        LOGARITHMIC
    }
}
