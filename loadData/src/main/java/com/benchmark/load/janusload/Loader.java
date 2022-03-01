package com.benchmark.load.janusload;//package org.janusgraph.mytest.csvload;

import com.benchmark.load.janusload.LoadVertexAndEdge;
import org.janusgraph.core.*;
import org.janusgraph.graphdb.database.management.ManagementSystem;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;

public class Loader{

    private final int THREAD_NUM = 4;
    private ExecutorService pool = Executors.newFixedThreadPool(THREAD_NUM, new MyThreadFactory());

    private int commitBatch = 5000;

//    String properties = "E:\\IntelliJ IDEA\\workspace\\janusgraph\\janusgraph-dist\\src\\assembly\\cfilter\\conf\\" +
//            "janusgraph-rocksdb-lucene.properties";
    String properties = "../conf/janusgraph-rocksdb-lucene.properties";

    String num = "100000";
    String ratio = "10";

    public Loader(String properties, String num, String ratio){
        this.properties = properties;
        this.num = num;
        this.ratio = ratio;
    }

    public void loadMain(){
        System.out.println("load: " + properties);
        JanusGraph JanusG = JanusGraphFactory.open(properties);

//        创建图结构
        ManagementSystem mgmt = (ManagementSystem) JanusG.openManagement();
        mgmt.makeEdgeLabel("MyEdge").make();
        mgmt.makeVertexLabel("MyNode").make();
        PropertyKey id_key = mgmt.makePropertyKey("node").dataType(String.class).make();
        mgmt.buildIndex("byId", JanusGraphVertex.class).addKey(id_key).unique().buildCompositeIndex();
        mgmt.commit();

        long startTime=System.nanoTime();
        LoadVertexAndEdge load = new LoadVertexAndEdge(commitBatch, num, ratio);

        pool.execute(()->load.addEdges(JanusG));
        pool.execute(()->load.addVertex(JanusG));
        pool.shutdown();
        try {
            pool.awaitTermination(1440, TimeUnit.MINUTES);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        long runtime = (System.nanoTime()-startTime) / 1000 /1000 /1000;
        double throughput = 1000.0 * Integer.parseInt(num)/runtime;
        System.out.printf("time(sec)： %d, throughput(ops/sec)：%.2f\n", runtime,throughput);

        JanusG.close();
    }


    /** this class define name for threads
     */
    public class MyThreadFactory implements ThreadFactory {
        private int counter = 0;
        public Thread newThread(Runnable r) {
            return new Thread(r, "" + counter++);
        }
    }
}