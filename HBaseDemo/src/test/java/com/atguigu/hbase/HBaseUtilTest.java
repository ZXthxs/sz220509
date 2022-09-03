package com.atguigu.hbase;

import org.apache.hadoop.hbase.client.*;
import org.apache.hadoop.hbase.util.Bytes;
import org.junit.Test;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;

/**
 * Created by Smexy on 2022/9/3
 */
public class HBaseUtilTest
{

    @Test
    public void testPut() throws IOException {

        Table t1 = HBaseUtil.getTable("t1");

        Put put1 = HBaseUtil.getPut("110", "f1", "name", "tom");
        Put put2 = HBaseUtil.getPut("110", "f1", "age", "30");
        Put put3 = HBaseUtil.getPut("110", "f1", "gender", "male");

        List<Put> puts = Arrays.asList(put1, put2, put3);

        t1.put(puts);

        t1.close();

    }

    @Test
    public void testDelete() throws IOException {

        Table t1 = HBaseUtil.getTable("t1");

        Delete delete = new Delete(Bytes.toBytes("102"));

        //只删除某一行某一列的最新版本  为此列添加一个新的cell，ts=这列所有cell中最大的ts，type = delete
       // delete.addColumn(Bytes.toBytes("f1"),Bytes.toBytes("age"));

        //删除某一行某一列的所有版本  为此列添加一个新的cell，ts=最大ts，type = DeleteColumn
        //delete.addColumns(Bytes.toBytes("f1"),Bytes.toBytes("age"));

        //删除一行的一个列族  column=f1:, timestamp=最大, type=DeleteFamily
        //delete.addFamily(Bytes.toBytes("f1"));

        //默认删除一行，为这行的所有列族都进行删除
        t1.delete(delete);

        t1.close();

    }

    @Test
    public void testGet() throws IOException {

        Table t1 = HBaseUtil.getTable("t1");

        Get get = new Get(Bytes.toBytes("110"));

        Result result = t1.get(get);

        HBaseUtil.parseResult(result);

        t1.close();

    }

    @Test
    public void testScan() throws IOException {

        Table t1 = HBaseUtil.getTable("t1");

        Scan scan = new Scan();
        Scan scan1 = scan.withStartRow(Bytes.toBytes("103"))
            .setRaw(true)
            .withStopRow(Bytes.toBytes("106"));


        // 多个Result的集合
        ResultScanner scanner = t1.getScanner(scan1);

        for (Result result : scanner) {

            HBaseUtil.parseResult(result);
        }

        t1.close();

    }




}