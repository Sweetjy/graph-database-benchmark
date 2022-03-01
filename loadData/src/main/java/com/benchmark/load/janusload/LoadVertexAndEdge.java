package com.benchmark.load.janusload;

import org.janusgraph.core.JanusGraph;
import org.janusgraph.core.JanusGraphEdge;
import org.janusgraph.core.JanusGraphTransaction;
import org.janusgraph.core.JanusGraphVertex;

import java.util.ArrayList;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Sweetjy
 * @date 2021/12/30
 */
public class LoadVertexAndEdge {

    private final Map<Integer, Long> vertexes = new ConcurrentHashMap<>();

    private int VERTEX_NUM = 10000;
    private int ratio = 10; //平均一个点有几条边

    private int commitBatch = 5000;

    public LoadVertexAndEdge(int commitBatch, String num, String ratio){
        this.commitBatch = commitBatch;
        this.ratio = Integer.parseInt(ratio);
        VERTEX_NUM = Integer.parseInt(num);
    }

    /**
     * 添加点
     * @param JanusG
     */
    public synchronized void addVertex(JanusGraph JanusG) {
        JanusGraphTransaction tx = JanusG.newTransaction();

        for (int nodeid = 0; nodeid<VERTEX_NUM; nodeid++){
            JanusGraphVertex srcVertex = tx.addVertex("MyNode");
            srcVertex.property("node", String.valueOf(nodeid));
//            System.out.printf("node = %d, id = %d\n", nodeid, srcVertex.longId());
            vertexes.put(nodeid, (Long) srcVertex.id());   //保存顶点的信息

            if (nodeid % commitBatch == 0) {
                tx.commit();
                tx = JanusG.newTransaction();
            }
        }
        tx.commit();
        notifyAll();
    }


    /**
     * 添加边
     * @param JanusG
     */
    public synchronized void addEdges(JanusGraph JanusG) {
        try {
            wait();

            JanusGraphTransaction tx = JanusG.newTransaction();
            long startTime=System.nanoTime();

            for (int nodeid = 0; nodeid < VERTEX_NUM; nodeid++) {
                ArrayList<Integer> records = new ArrayList<>();   //记录生成的随机数，保证生成不重复的随机数
                records.add(nodeid);
                Random rd = new Random(nodeid);
                for (int i = 0; i < ratio; i++) {
                    JanusGraphVertex srcVertex = tx.getVertex(vertexes.get(nodeid));
                    int tgt = rd.nextInt(VERTEX_NUM);
                    while (records.contains(tgt))
                        tgt = rd.nextInt(VERTEX_NUM);
                    records.add(tgt);
                    JanusGraphVertex dstVertex = tx.getVertex(vertexes.get(tgt));
                    if(srcVertex == null || dstVertex == null)
                        continue;
                    JanusGraphEdge edge = srcVertex.addEdge("MyEdge", dstVertex);

//                    System.out.println(nodeid + "->" + tgt);
                }
                tx.commit();
                tx = JanusG.newTransaction();
            }
            tx.commit();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

}
