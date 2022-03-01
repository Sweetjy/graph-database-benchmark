# JanusGraph 基准测试指导文档

本文详细说明了如何在 `JanusGraph 0.5.2` 上完成图数据库基准测试项。
### 1 添加jar包

如果测试自定义的存储引擎的janusgraph，则需要先打包：将rocksdb模块的jar包```janusgraph-rocksdb-0.5.2.jar```添加到本地maven仓库中
```shell
    mvn install:install-file -Dfile=janusgraph-rocksdb-0.5.2.jar -DgroupId=org.janusgraph -DartifactId=janusgraph-rocksdb -Dversion=0.5.2 -Dpackaging=jar
```
在loadData和janus子项目中的pom文件中添加依赖项
```
        <dependency>
            <groupId>org.janusgraph</groupId>
            <artifactId>janusgraph-core</artifactId>
            <version>0.5.2</version>
        </dependency>
<!--存储-->
        <dependency>
            <groupId>org.janusgraph</groupId>
            <artifactId>janusgraph-rocksdb</artifactId>
            <version>0.5.2</version>
        </dependency>
        <dependency>
            <groupId>org.rocksdb</groupId>
            <artifactId>rocksdbjni</artifactId>
            <version>6.2.2</version>
        </dependency>
<!--索引-->
        <dependency>
            <groupId>org.janusgraph</groupId>
            <artifactId>janusgraph-lucene</artifactId>
            <version>0.5.2</version>
        </dependency> 
        <dependency>
            <groupId>org.janusgraph</groupId>
            <artifactId>janusgraph-es</artifactId>
            <version>0.5.2</version>
        </dependency>
```

### 2 打包测试项目

根目录运行 `mvn package`，获得 jar 包：`benchmark-janus-1-jar-with-dependencies.jar` 和 `loadData-1-jar-with-dependencies.jar`

```shell
mvn package
```
loadData的jar包用于导入测试数据，生成采样数据
benchmark的jar包用于测试，包括k-neighbor和short path测试

### 3 载入数据

进入loadData文件夹，执行./run.sh，可以指定参数:

     1.参数1是配置文件位置
     2.参数2是顶点数
     3.参数3与参数2的乘积是边数
     4.参数4是测试采样点文件的存储位置
     5.参数5是测试采样点的个数
```shell
    ./run.sh ../conf/janusgraph-rocksdb-lucene.properties 10000 10 ../janus/data 100
```

#### 4 查询测试

1. 进入janus文件夹，该文件夹包括K跳顶点测试和最短路径测试，测试结果包括响应时间等。
2. 修改 `application.yaml` 文件中properties文件的地址、上一步生成的采样文件的地址
3. 使用`java -jar target/benchmark-janus-1-jar-with-dependencies.jar` 运行测试程序，运行结束后当前目录下会生成四个日志文件，为本次测试的结果。

#### 5 配置文件介绍

测试项的自动化执行需要依赖配置文件 `application.yaml` ，该配置文件主要实现以下配置：

* 指定图数据库类型和图名称
* 指定测试样本文件路径
* 设置超时时间
* 设置测试项、测试样本数量、测试项循环次数

每个图数据库模块实现了共同框架之后都应按需要编辑配置文件，以便测试程序正确地连接图数据库，读取测试样本，执行测试项。以下是测试 Galaxybase 图数据库的配置样例：

```yaml
# （必须）图数据库类型
graphType: janusgraph
# （必须）图名称
graphName: graph-test
# （必须）默认样本文件路径（与测试程序所在目录的相对路径），如果测试项中没有指定样本文件，则默认读取本样本文件
sample: ../data/knn.txt
# （非必须）测试中需要多个样本文件，可在此处指定样本路径到样本名称映射，后续测试项上通过样本名称指定样本。
samples:
  # （可添加多条）样本名称: 样本所在路径
  sp: ../data/shortpath.txt
# （必须）默认超时时间，测试项也支持独立指定超时时间，单位：s，超时后将强制关闭当前测试任务
timeout: 3600
# （非必须，默认值为true）测试任务超时后是否停止超时任务所在的测试项，直接开始下一个测试项的测试
timeoutStop: false
# （非必须，默认值为true）遇到错误后是否停止超时任务所在的测试项，直接开始下一个测试项的测试
errorStop: false
# （必须）数据库连接实现类的包名（全限定名）
dataBaseClass:  com.benchmark.janus.database.Janus
# （非必须）各个数据库测试实现的自定义参数区，根据不同数据库将有所不同，请参照各个测试模块的README说明文档
parameters:
    propertyFile: /home/ljy/janusgraph-benchmark/conf/janusgraph-berkeleyje-lucene.properties
# (必须)测试项列表（有序）
test:
  # Galaxybase K-hop neighbor 1度查询配置示例
  # （必须）KNeighbor1实现类的包名（全限定名）
  - testClass: com.benchmark.janus.function.KNeighbor1
    # （非必须，默认值为1）当前测试项的测试任务数
    count: 300
    # （非必须，默认值为1）当前测试项的循环测试次数，次数越多测试结果越稳定
    loop: 3
    # （非必须，默认值为删除配置的默认超时时间的值）超时时间，超时后将强制关闭当前测试任务
    timeout: 180
......

  #Shortest Path算法测试
  # （必须）ShortestPath实现类的包名（全限定名）
  - testClass: com.benchmark.janus.function.Shortest
    # （非必须，默认值为1）当前测试项的测试任务数
    count: 100
    # （非必须，默认值为1）当前测试项的循环测试次数，次数越多测试结果越稳定
    loop: 3
    # （非必须，默认值为上述`sample`配置的默认样本）指定当前测试项采用的测试样本名称
    sample: sp
```
