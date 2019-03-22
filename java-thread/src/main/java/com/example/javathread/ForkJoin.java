package com.example.javathread;

import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.ForkJoinTask;
import java.util.concurrent.RecursiveTask;

/**
 * @author jianganwei
 * @program study-v2
 * @description
 * @date 2019-03-22 14:06
 **/
public class ForkJoin {
    public static class SumTask extends RecursiveTask<Long> {

        private Long start;
        private Long end;
        private Long sum = 0L;

        public SumTask(Long start, Long end) {
            this.start = start;
            this.end = end;
        }

        @Override
        protected Long compute() {
            if ((end - start) < 10000) {
                for (Long i = start; i < end; i++) {
                    sum += i;
                }
            } else {
                long pos = (start + end) / 2;
                SumTask left = new SumTask(start, pos);
                SumTask right = new SumTask(pos, end);
                left.fork();
                long leftVaule = left.join();

                sum = right.invoke() + leftVaule;
            }
            return sum;
        }

    }

    public static void main(String[] args) {
        ForkJoinPool pool = new ForkJoinPool();

        ForkJoinTask<Long> task = pool.submit(new SumTask(3L, 2000000L));
        pool.shutdown();
        System.out.println(task.invoke());

    }
}
