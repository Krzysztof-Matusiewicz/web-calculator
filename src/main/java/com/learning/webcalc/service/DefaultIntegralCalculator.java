package com.learning.webcalc.service;

import com.learning.webcalc.service.api.CalculationException;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.stream.IntStream;

import static java.util.stream.Collectors.toList;

@Component
public class DefaultIntegralCalculator implements com.learning.webcalc.service.api.IntegralCalculator
{

    public Double calculate(double lowerBound, double upperBound, int intervalCount, int threadCount) throws InterruptedException
    {
        validateIntervalCount(intervalCount);
        validateThreadCount(threadCount);

        final double boundSize = upperBound - lowerBound;
        final double intervalSize = boundSize / intervalCount;

        ExecutorService executor = Executors.newFixedThreadPool(threadCount);
        List<WorkerThread> workers = IntStream.range(0, intervalCount)
                .mapToObj(i -> new WorkerThread(lowerBound + i * intervalSize, intervalSize))
                .peek(executor::execute)
                .collect(toList());
        executor.shutdown();
        executor.awaitTermination(10, TimeUnit.SECONDS);
        return workers.stream().map(WorkerThread::getResult).mapToDouble(Optional::get).sum();
    }

    private void validateIntervalCount(int intervalCount)
    {
        if (intervalCount <= 0)
        {
            throw CalculationException.forIntervalCountNotGreaterThanZero(intervalCount);
        }
    }

    private void validateThreadCount(int threadCount)
    {
        if (threadCount <= 0)
        {
            throw CalculationException.forThreadCountNotGreaterThanZero(threadCount);
        }
    }

    private static class WorkerThread implements Runnable
    {

        private final double lowerBound;

        private final double interval;

        private Double result;

        public WorkerThread(double lowerBound, double interval)
        {
            this.lowerBound = lowerBound;
            this.interval = interval;
        }

        @Override
        public void run()
        {
            double upperBound = lowerBound + interval;
            result = Math.exp(upperBound) - Math.exp(lowerBound);
        }

        public Optional<Double> getResult()
        {
            return Optional.ofNullable(result);
        }

    }

}
