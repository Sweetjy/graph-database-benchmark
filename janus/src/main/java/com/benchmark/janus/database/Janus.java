package com.benchmark.janus.database;


import com.galaxybase.benchmark.common.database.DataBase;
import com.galaxybase.benchmark.common.util.TestConfiguration;
import org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.GraphTraversalSource;
import org.janusgraph.core.JanusGraph;
import org.janusgraph.core.JanusGraphFactory;
import org.janusgraph.example.GraphOfTheGodsFactory;

public class Janus implements DataBase<GraphTraversalSource> {

    private static GraphTraversalSource ts;

    private static JanusGraph janusG;

    @Override
    public GraphTraversalSource getDataBase() {
        if (ts == null){
            initDataBase(TestConfiguration.instance());
        }
        return ts;

    }

    @Override
    public void initDataBase(TestConfiguration conf) {
        String propertyFile = conf.get("propertyFile");
        janusG = JanusGraphFactory.open(propertyFile);

//        GraphOfTheGodsFactory.load(janusG);
        ts = janusG.traversal();
    }

    @Override
    public void close() {
        try {
            ts.close();
            janusG.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
