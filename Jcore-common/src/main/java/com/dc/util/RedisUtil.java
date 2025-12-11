package com.dc.util;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.Cursor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ScanOptions;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

@Slf4j
@Component
@RequiredArgsConstructor
public class RedisUtil {

    private final RedisTemplate<String, Object> redisTemplate;

    /**
     * 存储数据
     */
    public void set(String key, Object value, long timeout) {
        redisTemplate.opsForValue().set(key, value, timeout, TimeUnit.SECONDS);
    }

    /**
     * 存储数据（默认过期时间）
     */
    public void set(String key, Object value) {
        redisTemplate.opsForValue().set(key, value);
    }

    public void setEx(String key, Object value, long timeout) {
        redisTemplate.opsForValue().set(key, value, timeout, TimeUnit.SECONDS);
    }

    /**
     * 获取数据
     */
    public Object get(String key) {
        return redisTemplate.opsForValue().get(key);
    }

    /**
     * 删除数据
     */
    public void delete(String key) {
        redisTemplate.delete(key);
    }

    /**
     * 设置过期时间
     */
    public void expire(String key, long timeout, TimeUnit unit) {
        redisTemplate.expire(key, timeout, unit);
    }

    /**
     * 判断是否存在
     */
    public boolean hasKey(String key) {
        return Boolean.TRUE.equals(redisTemplate.hasKey(key));
    }

    /**
     * 获取剩余过期时间（秒）
     */
    public Long getExpire(String key) {
        return redisTemplate.getExpire(key);
    }


    /**
     * 向 Hash 中添加键值对
     */
    public void hashPut(String key, String hashKey, String value) {
        redisTemplate.opsForHash().put(key, hashKey, value);
    }

    /**
     * 获取 Hash 中某个字段的值
     */
    public Object hashGet(String key, String hashKey) {
        return redisTemplate.opsForHash().get(key, hashKey);
    }

    public Map<Object, Object> hGetAll(String key) {
        return redisTemplate.opsForHash().entries(key);

    }

    /**
     * 删除 Hash 中的一个或多个字段
     */
    public void hashDelete(String key, String... hashKeys) {
        redisTemplate.opsForHash().delete(key, (Object[]) hashKeys);
    }

    /**
     * 判断 Hash 中是否存在指定字段
     */
    public boolean hashHasKey(String key, String hashKey) {
        return redisTemplate.opsForHash().hasKey(key, hashKey);
    }

    /**
     * 获取整个 Hash 的所有键值对
     */
    public Map<Object, Object> hashEntries(String key) {
        return redisTemplate.opsForHash().entries(key);
    }


    /**
     * 向 List 左侧推入元素（LPUSH）
     */
    public void listLeftPush(String key, Object value) {
        redisTemplate.opsForList().leftPush(key, value);
    }

    /**
     * 向 List 右侧推入元素（RPUSH）
     */
    public void listRightPush(String key, Object value) {
        redisTemplate.opsForList().rightPush(key, value);
    }

    /**
     * 从 List 左侧弹出元素（LPOP）
     */
    public Object listLeftPop(String key) {
        return redisTemplate.opsForList().leftPop(key);
    }

    /**
     * 从 List 右侧弹出元素（RPOP）
     */
    public Object listRightPop(String key) {
        return redisTemplate.opsForList().rightPop(key);
    }

    /**
     * 获取 List 中指定范围的元素（LRANGE）
     */
    public List<Object> listRange(String key, long start, long end) {
        return redisTemplate.opsForList().range(key, start, end);
    }

    /**
     * 获取 List 长度
     */
    public Long listSize(String key) {
        return redisTemplate.opsForList().size(key);
    }


    /**
     * 向 Set 添加元素
     */
    public void setAdd(String key, Object... values) {
        redisTemplate.opsForSet().add(key, values);
    }

    /**
     * 获取 Set 中的所有元素
     */
    public Set<Object> setMembers(String key) {
        return redisTemplate.opsForSet().members(key);
    }

    /**
     * 随机获取 Set 中的一个元素
     */
    public Object setRandomMember(String key) {
        return redisTemplate.opsForSet().randomMember(key);
    }

    /**
     * 删除 Set 中的一个或多个元素
     */
    public void setRemove(String key, Object... values) {
        redisTemplate.opsForSet().remove(key, values);
    }

    /**
     * 判断元素是否存在于 Set 中
     */
    public boolean setIsMember(String key, Object value) {
        return Boolean.TRUE.equals(redisTemplate.opsForSet().isMember(key, value));
    }


    /**
     * 向 ZSet 添加一个元素，并指定其分数（score）
     */
    public void zSetAdd(String key, Object value, double score) {
        redisTemplate.opsForZSet().add(key, value, score);
    }

    /**
     * 获取 ZSet 中指定分数范围内的元素（升序）
     */
    public Set<Object> zSetRangeByScore(String key, double min, double max) {
        return redisTemplate.opsForZSet().rangeByScore(key, min, max);
    }

    /**
     * 获取 ZSet 中指定排名范围内的元素（从低到高）
     */
    public Set<Object> zSetRange(String key, long start, long end) {
        return redisTemplate.opsForZSet().range(key, start, end);
    }

    /**
     * 获取 ZSet 中某个元素的排名（从0开始）
     */
    public Long zSetRank(String key, Object value) {
        return redisTemplate.opsForZSet().rank(key, value);
    }

    /**
     * 获取 ZSet 中元素的数量
     */
    public Long zSetCount(String key, double min, double max) {
        return redisTemplate.opsForZSet().count(key, min, max);
    }

    /**
     * 删除 ZSet 中一个或多个元素
     */
    public void zSetRemove(String key, Object... values) {
        redisTemplate.opsForZSet().remove(key, values);
    }


    public boolean exit(String key){

        Boolean exists = redisTemplate.hasKey(key);
        return  exists;
    }

    public Long incr(String key) {
        return redisTemplate.opsForValue().increment(key);
    }

    public Long getCounter(String key) {
        Object value = redisTemplate.opsForValue().get(key);
        if (value == null) return 0L;
        if (value instanceof Long) return (Long) value;
        return Long.parseLong(value.toString());
    }

    public Long incrBy(String key, long delta) {
        return redisTemplate.opsForValue().increment(key, delta);
    }
    public Long decr(String key) {
        return  redisTemplate.opsForValue().decrement(key);
    }


    public void removeNodeDataByScan(String setKey) {
        Set<Object> members = redisTemplate.opsForSet().members(setKey);
        if (members != null && !members.isEmpty()) {
            List<String> batch = new ArrayList<>();
            for (Object member : members) {
                batch.add(member.toString());
                if (batch.size() >= 1000) {
                    redisTemplate.delete(batch);
                    batch.clear();
                }
            }
            // 删除剩余未处理的
            if (!batch.isEmpty()) {
                redisTemplate.delete(batch);
            }
        }
        redisTemplate.delete(setKey); // 删除 Set 自身
    }


    /**
     * 从 dc_chat:entity_map 中清理所有 value 为 nodeName 的字段
     * @param nodeName 节点名，例如 "node1"
     */
    public void cleanEntitiesByNode(String keyPattern, String nodeName) {
        log.info("开始清理节点 [{}]，匹配键模式 [{}]", nodeName, keyPattern);
        int deleted = 0;
        try {
            // 使用 RedisTemplate 的 scan 方法替代直接使用连接
            ScanOptions options = ScanOptions.scanOptions()
                    .match(keyPattern)
                    .count(1000)
                    .build();

            try (Cursor<String> cursor = redisTemplate.scan(options)) {
                while (cursor.hasNext()) {
                    String key = cursor.next();
                    String value = (String) redisTemplate.opsForValue().get(key);
                    log.debug("扫描到 key: {}, value: {}", key, value);
                    if (nodeName.equals(value)) {
                        redisTemplate.delete(key);
                        deleted++;
                        log.info("删除 key: {}", key);
                    }
                }
            }
            log.info("清理完成，节点 [{}] 删除了 {} 个 key", nodeName, deleted);
        } catch (Exception e) {
            log.error("清理节点 [{}] 键时出错", nodeName, e);
        }
    }



}
