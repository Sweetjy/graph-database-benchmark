package com.benchmark.load.sample;

import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;

/**
 * @author Sweetjy
 * @date 2021/12/31
 */
public class Sample {
    private String path = "../data";
    private Integer vertexNum = 1000000;   //图的顶点数
    private Integer sampleNum = 100;   //采样的数据个数
    private String filelabel;

    public Sample() {

    }

    public Sample(String path, String num, String sample) {
        this.path = path;
        this.vertexNum = Integer.parseInt(num);
        this.sampleNum = Integer.parseInt(sample);
        filelabel = String.valueOf((vertexNum/1000000));

    }

    public void knSample(){
        ArrayList<Integer> records = new ArrayList<>();   //记录生成的随机数，保证生成不重复的随机
        Random rd = new Random(1024);
        for(int i=0; i<sampleNum; i++) {
            int node = rd.nextInt(vertexNum);
            while (records.contains(node))
                node = rd.nextInt(vertexNum);
            records.add(node);
        }
        //保存采样文件
        String filename = path +"knn" + filelabel +".txt";
        try {
            FileWriter writer = new FileWriter(filename, true);
            for(int i=0; i<sampleNum; i++) {
                writer.write(records.get(i).toString() + "\n");
            }
            writer.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void shortSample(){
        ArrayList<Integer> begin = new ArrayList<>();
        ArrayList<Integer> end = new ArrayList<>();
        Random rd = new Random(0);
        Random rd1 = new Random(1);
        for(int i=0; i<sampleNum; i++) {
            int node1 = rd.nextInt(vertexNum);
            int node2 = rd1.nextInt(vertexNum);
            while (begin.contains(node1))
                node1 = rd.nextInt(vertexNum);
            begin.add(node1);
            while(end.contains(node2))
                node2 = rd.nextInt(vertexNum);
            end.add(node2);
        }
        //保存采样文件
        String filename = path + "shortpath" + filelabel + ".txt";
        try {
            FileWriter writer = new FileWriter(filename, true);
            for(int i=0; i<sampleNum; i++) {
                writer.write(begin.get(i).toString() + "," +end.get(i).toString() + "\n");
            }
            writer.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
