package com.atguigu.hbase;

import org.apache.commons.lang3.StringUtils;
import org.apache.hadoop.hbase.Cell;
import org.apache.hadoop.hbase.CellUtil;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.*;
import org.apache.hadoop.hbase.util.Bytes;

import java.io.IOException;

/**
 * Created by Smexy on 2022/9/3
 *
 *      ①创建一个客户端
 *      ②使用客户端连接服务端
 *              Connection对象维护连接hbase和zk的连接。
 *              Connection可以通过  ConnectionFactory创建。
 *
 *                  如何创建对象:
 *                          直接:   A a=new A()
 *                          使用设计模式:
 *                              建造者模式:   A a = new A.Builder().build()
 *                              工厂模式:  A a =  new AFactory().getInstance()
 *
 *              Connection的生命周期由调用者管理，谁创建，谁负责关闭。
 *              从Connection中可以获取Table,Admin
 *                      Table： 处理对一张表的 DML ，put,get,scan,delete
 *                      Admin:  负责对一张表的 DDL , 建库，建表，删表
 *
 *
 *              注意事项:
 *                      Connection的创建是重量级(费时，费力，费资源)，线程安全。一个App只需要创建一个Connection
 *                      Table和Admin恰恰相反，创建是轻量级(省时省力，不费劲)，线程不安全，每一个线程都自己的Table实例，不建议
 *                      池化或缓存。
 *
 *      ③准备命令，发送给服务端执行
 *
 *              Table :  代表要操作的那张表
 *
 *                  增|改:  Table.put(Put)
 *                            Put: 代表要插入的一个Cell
 *                   删:     Table.delete(Delete)
 *                              Delete: 一次删除操作(一行，一个cell，一列，列族)
 *
 *                   查单行:  Table.get(Get)
 *                          Get: 对单号的 查询
 *
 *                   查多行:   Table.getScanner(Scan)
 *                              Scan: 对多行的查询
 *      ④如果是读，需要接收服务端返回的结果
 *      ⑤关闭客户端
 *
 *    ------------------------------
 *    工具类:
 *          Bytes: 将自己定义的类型 和 byte[] 相互转换
 *                      Bytes.toBytes(T t): 类型转 byte[]
 *                      Bytes.toXxx(byte[] x): byte[] 转为指定类型
 *
 *
 *          CellUtil：  可以获取一个Cell中的指定的属性
 *                  CellUtil.cloneXxx() : 获取列族，列名，值，rowkey等
 *
 *    ---------------------------------
 *      客户端在读写时，如何找到你的regionserver?
 *
 *              先连接zk，读取meta表，查阅meta表，才知道读写的这行所在region的regionserver
 *
 */
public class HBaseUtil
{
    //为App创建一个单例Connection
    private static Connection connection;



    /*
                类在加载时，执行静态代码的内容，只运行一次
         */
    static {

        /*
                不要去new Configuration()!

                而应该调用空参的createConnection()，运行createConnection(HBaseConfiguration.create(), null, null)
                    HBaseConfiguration.create()可以帮你 new Configuration()，加入hbase的配置文件中的属性
         */
        try {
             connection = ConnectionFactory.createConnection();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public static void close() throws IOException {
        if (connection != null){
            connection.close();
        }
    }


    /*
        根据用户指定的表名，获取操作这张表的Table对象
     */
    public static Table getTable(String tableName) throws IOException {

        // isBlank()判断所传入字符串是否是 “”，null," 回车，空格 ，tab "
        if (StringUtils.isBlank(tableName)){
            throw new RuntimeException("表名非法!");
        }

        Table table = connection.getTable(TableName.valueOf(tableName));

        return table;

    }

    /*
        根据用户传入的参数，封装Put
            put '表名','rowkey'，‘cf:cn’,value
     */

    public static Put getPut(String rowkey,String cf,String cq,String value){

        Put put = new Put(Bytes.toBytes(rowkey));

        put.addColumn(Bytes.toBytes(cf),Bytes.toBytes(cq),Bytes.toBytes(value));

        return put;

    }

    /*
            用于遍历Result,一个Result代表一行查询的结果
     */
    public static void parseResult(Result result){

        //获取一行中所有的cell
        Cell[] cells = result.rawCells();

        for (Cell cell : cells) {

            String key = Bytes.toString(CellUtil.cloneRow(cell)) + ", " + Bytes.toString(CellUtil.cloneFamily(cell)) +":"
                + Bytes.toString(CellUtil.cloneQualifier(cell)) ;

            String value = Bytes.toString(CellUtil.cloneValue(cell));

            System.out.println(key +"--->" + value);

        }

    }
}
