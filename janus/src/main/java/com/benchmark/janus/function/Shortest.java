package com.benchmark.janus.function;


import com.galaxybase.benchmark.common.test.AbstractTest;
import com.galaxybase.benchmark.common.util.Sample;
import org.apache.tinkerpop.gremlin.process.traversal.Path;
import org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.GraphTraversal;
import org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.GraphTraversalSource;
import org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.__;
import org.apache.tinkerpop.gremlin.structure.Vertex;
import org.apache.tinkerpop.gremlin.process.computer.traversal.step.map.ShortestPath;
import java.util.List;
import java.util.Map;

public class Shortest extends AbstractTest<GraphTraversalSource, String> {
    @Override
    public String onStartTest(GraphTraversalSource g, Object[] s) throws Exception {
        StringBuffer str = new StringBuffer();
        GraphTraversal<Vertex, Path> path = g.V().has("node", s[0]).repeat(__.outE().inV().simplePath()).emit().times(6).
                has("node", s[1]).limit(1).path();
//        GraphTraversalSource graph = g.withComputer();
//        GraphTraversal<Vertex, Path> path = graph.V().has("node", s[0]).shortestPath()
//                .with(ShortestPath.target, __.has("node", s[1]));
        str.append("num:").append(path.toList().size());
        while (path.hasNext()) {
            str.append(path.next());
        }
        return str.toString();
    }

    @Override
    public String resultHandle(String result) {
        return result;
    }

    @Override
    public Object[] getSample(Sample sample, int n) {
        return sample.get(n).split(",");
    }

    @Override
    public String getTestName() {
        return "ShortestPath";
    }
}
