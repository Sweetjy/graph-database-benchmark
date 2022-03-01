package com.benchmark.load;

import com.benchmark.load.janusload.Loader;
import com.benchmark.load.sample.Sample;

import com.benchmark.load.update.Update;
import org.apache.log4j.*;

/**
 * @author Sweetjy
 * @date 2021/12/31
 */
public class application {
    //关掉janusgraph的日志
//    private static String properties = "E:\\IntelliJ IDEA\\workspace\\janusgraph-benchmark\\conf\\janusgraph-rocksdb-lucene.properties";
    private static String properties = "../conf/janusgraph-rocksdb-lucene.properties";
    private static String num = "10000";
    private static String ratio = "10";
    private static String dataPath = "../data";
    private static String sampleNum = "100";

    private static Logger log = Logger.getLogger(application.class);

    public static void main(String[] args) {
        String properties = args[0];
        String num = args[1];
        String ratio = args[2];
        String dataPath = args[3];
        String sampleNum = args[4];
        //关闭日志
        log.setLevel(Level.OFF);
        System.out.println("properties file is "+properties
                + ", vertex num is "+num
                + ", edge ratio is "+ratio
                + ", sample data save in "+dataPath
                + ", sample data num is "+ sampleNum
        );

        //加载数据到图
        Loader loader = new Loader(properties, num, ratio);
//        long startTime=System.nanoTime();
//        loader.loadMain();
//        long runtime = (System.nanoTime()-startTime) / 1000 /1000 /1000;
//        double throughput = 1000.0 * Integer.parseInt(num)/runtime;
//        System.out.printf("time(sec)： %d, throughput(ops/sec)：%.2f\n", runtime,throughput);

//        //生成采样文件
//        System.out.println("create sample data..........");
//        Sample sp = new Sample(dataPath, num, sampleNum);
//        sp.knSample();
//        sp.shortSample();
//        System.out.println("end!");
        Update update = new Update(properties, num);
        update.updateMain();

    }

}
