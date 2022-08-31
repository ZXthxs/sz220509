package com.atguigu.jedis;

import redis.clients.jedis.Jedis;

/**
 * Created by Smexy on 2022/8/31
 *
 *      ①新建客户端
 *      ②连接服务端
 *      ③发送命令
 *      ④是读操作，返回结果
 *      ⑤关闭连接
 *
 *
 *      查询一个string类型的key-value时，key不存在，返回null!
 *      查询一个set类型的key-value时，key不存在，返回[](空Set)！
 */
public class JedisExampleDemo
{

    public static void main(String[] args) {

        //①新建客户端 ②连接服务端
        Jedis jedis = new Jedis("hadoop102", 6379);

       //写string
        jedis.set("hahaha","xixixi");

        System.out.println(jedis.get("hahaha1"));

        //写set
        jedis.sadd("myset","a","b","c");

        System.out.println(jedis.smembers("myset1"));

        //⑤关闭连接
        jedis.close();


    }


}
