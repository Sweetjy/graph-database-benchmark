package com.benchmark.load.update;

import com.benchmark.load.janusload.Loader;
import org.apache.thrift.TException;
import org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.GraphTraversal;
import org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.GraphTraversalSource;
import org.apache.tinkerpop.gremlin.structure.Vertex;
import org.janusgraph.core.JanusGraph;
import org.janusgraph.core.JanusGraphFactory;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;

/**
 * @author Sweetjy
 * @date 2022/2/15
 */
public class Update {

    public static JanusGraph graph = null;
    public static GraphTraversalSource g = null;


    //    String properties = "E:\\IntelliJ IDEA\\workspace\\janusgraph\\janusgraph-dist\\src\\assembly\\cfilter\\conf\\" +
//            "janusgraph-rocksdb-lucene.properties";
    String properties = "../conf/janusgraph-rocksdb-lucene.properties";

    int num = 100000;

    public Update(String properties, String num){
        this.properties = properties;
        this.num = Integer.parseInt(num);
    }

    public void updateProperty(){
        long startTime=System.nanoTime();

        for (int nodeid = 0; nodeid < num; nodeid++) {
            String update = UUID.randomUUID().toString()
                    .replaceAll("-", "");
            try {
                GraphTraversal<Vertex, Vertex> vertex = g.V().has("node", nodeid)
                        .outV().property("node", update);
            }catch(Exception e){
                continue;
            }
            //System.out.println(nodeid);

        }
        long runtime = (System.nanoTime()-startTime) /1000 /1000;
        System.out.printf("time(ms)： %d\n", runtime);
    }

    public void updateMain(){
        graph = JanusGraphFactory.open(properties);
        g = graph.traversal();
        updateProperty();
        graph.close();
    }

}
