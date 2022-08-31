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
 */
public class JedisDemo
{

    public static void main(String[] args) {

        //①新建客户端 ②连接服务端
        Jedis jedis = new Jedis("hadoop102", 6379);

        //密码授权认证
        //jedis.auth("123456");

        // ③发送命令
        String res = jedis.ping();

        // ④是读操作，返回结果
        System.out.println(res);

        //⑤关闭连接
        jedis.close();
        //你好啊~

    }


}
