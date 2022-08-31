package com.atguigu.jedis;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

/**
 * Created by Smexy on 2022/8/31
 *
 *      ①新建客户端
 *      ②连接服务端
 *      ③发送命令
 *      ④是读操作，返回结果
 *      ⑤关闭连接
 */
public class JedisPoolCustomDemo
{

    public static void main(String[] args) {

        //自定义池子的配置
        JedisPoolConfig jedisPoolConfig = new JedisPoolConfig();

        //池子的最大容量
        jedisPoolConfig.setMaxTotal(20);
        //池中中最大最小的空闲连接数
        jedisPoolConfig.setMaxIdle(20);
        jedisPoolConfig.setMinIdle(10);
        //借连接时，先测一下，好使了，再借出去
        jedisPoolConfig.setTestOnBorrow(true);
        //还连接时，先测一下，好使了，再还到池子中
        jedisPoolConfig.setTestOnReturn(true);
        //当连接耗尽时，又有人来借连接，是否直接报错，还是让它等一等
        jedisPoolConfig.setBlockWhenExhausted(true);
        //最多等多久，就报错
        jedisPoolConfig.setMaxWaitMillis(10000);

        //创建连接池
        JedisPool jedisPool = new JedisPool(jedisPoolConfig,"hadoop102", 6379);


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
