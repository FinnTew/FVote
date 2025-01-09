package com.fvote.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.*;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

@Component
public class RedisUtil {

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    /**
     * 设置键值对，并设置过期时间
     *
     * @param key     键
     * @param value   值
     * @param timeout 过期时间（秒）
     */
    public void set(String key, Object value, long timeout) {
        redisTemplate.opsForValue().set(key, value, timeout, TimeUnit.SECONDS);
    }

    /**
     * 设置键值对，无过期时间
     *
     * @param key   键
     * @param value 值
     */
    public void set(String key, Object value) {
        redisTemplate.opsForValue().set(key, value);
    }

    /**
     * 根据键获取值
     *
     * @param key 键
     * @return 值
     */
    public Object get(String key) {
        return redisTemplate.opsForValue().get(key);
    }

    /**
     * 删除键
     *
     * @param key 键
     * @return 是否删除成功
     */
    public boolean delete(String key) {
        return Boolean.TRUE.equals(redisTemplate.delete(key));
    }

    /**
     * 设置过期时间
     *
     * @param key     键
     * @param timeout 过期时间（秒）
     * @return 是否设置成功
     */
    public boolean expire(String key, long timeout) {
        return Boolean.TRUE.equals(redisTemplate.expire(key, timeout, TimeUnit.SECONDS));
    }

    /**
     * 获取键的过期时间
     *
     * @param key 键
     * @return 剩余过期时间（秒）; -1表示不过期; -2表示键不存在
     */
    public long getExpire(String key) {
        return redisTemplate.getExpire(key, TimeUnit.SECONDS);
    }

    /**
     * 判断键是否存在
     *
     * @param key 键
     * @return 是否存在
     */
    public boolean hasKey(String key) {
        return Boolean.TRUE.equals(redisTemplate.hasKey(key));
    }

    /**
     * 哈希：设置哈希值
     *
     * @param key   键
     * @param field 字段
     * @param value 值
     */
    public void hSet(String key, String field, Object value) {
        redisTemplate.opsForHash().put(key, field, value);
    }

    /**
     * 哈希：获取哈希值
     *
     * @param key   键
     * @param field 字段
     * @return 值
     */
    public Object hGet(String key, String field) {
        return redisTemplate.opsForHash().get(key, field);
    }

    /**
     * 哈希：获取整个哈希
     *
     * @param key 键
     * @return 哈希的键值对
     */
    public Map<Object, Object> hGetAll(String key) {
        return redisTemplate.opsForHash().entries(key);
    }

    /**
     * 哈希：删除哈希字段
     *
     * @param key    键
     * @param fields 字段
     */
    public void hDel(String key, Object... fields) {
        redisTemplate.opsForHash().delete(key, fields);
    }

    /**
     * 列表：左推
     *
     * @param key   键
     * @param value 值
     */
    public void lPush(String key, Object value) {
        redisTemplate.opsForList().leftPush(key, value);
    }

    /**
     * 列表：右推
     *
     * @param key   键
     * @param value 值
     */
    public void rPush(String key, Object value) {
        redisTemplate.opsForList().rightPush(key, value);
    }

    /**
     * 列表：弹出最左边的元素
     *
     * @param key 键
     * @return 弹出的值
     */
    public Object lPop(String key) {
        return redisTemplate.opsForList().leftPop(key);
    }

    /**
     * 列表：获取列表的所有元素
     *
     * @param key   键
     * @param start 起始索引
     * @param end   结束索引（-1表示获取所有）
     * @return 列表
     */
    public List<Object> lRange(String key, long start, long end) {
        return redisTemplate.opsForList().range(key, start, end);
    }

    /**
     * 集合：添加元素
     *
     * @param key    键
     * @param values 值
     */
    public void sAdd(String key, Object... values) {
        redisTemplate.opsForSet().add(key, values);
    }

    /**
     * 集合：获取集合中的所有元素
     *
     * @param key 键
     * @return 集合
     */
    public Set<Object> sMembers(String key) {
        return redisTemplate.opsForSet().members(key);
    }

    /**
     * 集合：移除集合中的指定元素
     *
     * @param key    键
     * @param values 值
     */
    public void sRemove(String key, Object... values) {
        redisTemplate.opsForSet().remove(key, values);
    }

    /**
     * 有序集合：添加元素
     *
     * @param key   键
     * @param value 值
     * @param score 分数
     */
    public void zAdd(String key, Object value, double score) {
        redisTemplate.opsForZSet().add(key, value, score);
    }

    /**
     * 有序集合：获取指定范围内的元素
     *
     * @param key   键
     * @param start 起始索引
     * @param end   结束索引
     * @return 元素集合
     */
    public Set<Object> zRange(String key, long start, long end) {
        return redisTemplate.opsForZSet().range(key, start, end);
    }

    /**
     * 有序集合：移除元素
     *
     * @param key    键
     * @param values 值
     */
    public void zRemove(String key, Object... values) {
        redisTemplate.opsForZSet().remove(key, values);
    }
}
