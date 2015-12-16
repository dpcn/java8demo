/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.dataprocess.java8demo;

import java.util.Arrays;
import java.util.Collection;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.ForkJoinTask;
import static java.util.stream.Collectors.toList;
import java.util.stream.Stream;

/**
 * 简单函数、类操作，转为并行操作，异步执行
 * <p>
 * 并行执行在耗时比较多的单任务中可以提高性能</p>
 * <p>
 * 适用于顺序执行耗时比较多的流程，并不是每个流程都适合并行</p>
 * <p>
 * 并行执行在耗时比较多的单任务中可以提高性能</p>
 *
 * @author lfh
 */
public class Normal2Asyn {

    public static int testTimeUse(int si) throws InterruptedException {
        Thread.sleep(si * 1000);
        return si ; 
    }

    public static void main(String args[]) throws InterruptedException, ExecutionException {
        //任务执行时间10s 
        Collection<Integer> tarsks = Arrays.asList(10, 20);
        long start_time = System.nanoTime();
        //串行执行
        for (Integer si : tarsks) {
            Normal2Asyn.testTimeUse(si);
        }
        long end_time = System.nanoTime();
        System.out.println("串行耗时：" + (end_time - start_time) / 1000/1000 + "毫秒");

        start_time = System.nanoTime();
        //并行执行
        Collection<ForkJoinTask<Object>> st = tarsks.parallelStream().map((Integer si) -> {
            Callable<Object> ca = () -> Normal2Asyn.testTimeUse(si);
            return ForkJoinPool.commonPool().submit(ca);
        }).collect(toList()) ;
        
        for( ForkJoinTask<Object> ft : st  ) {
            ft.get();
        }

        end_time = System.nanoTime();
        System.out.println("并行耗时：" + (end_time - start_time) / 1000/1000 + "毫秒");

    }

}
