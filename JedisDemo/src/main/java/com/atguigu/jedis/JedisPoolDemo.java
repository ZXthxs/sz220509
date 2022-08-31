package com.atguigu.jedis;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

/**
 * Created by Smexy on 2022/8/31
 *
 *      ①新建客户端
 *      ②连接服务端
 *      ③发送命令
 *      ④是读操作，返回结果
 *      ⑤关闭连接
 */
public class JedisPoolDemo
{

    public static void main(String[] args) {

        //创建连接池
        JedisPool jedisPool = new JedisPool("hadoop102", 6379);


        //从池中借一个连接
        Jedis jedis = jedisPool.getResource();

        // ③发送命令
        String res = jedis.ping();

        // ④是读操作，返回结果
        System.out.println(res);

        //⑤如果连接是从池中中借的，那么此方法不是关闭连接，而是归还连接
        jedis.close();


    }


}
