package com.sky.test;

import org.junit.jupiter.api.Test;
import org.springframework.data.redis.connection.DataType;
import org.springframework.data.redis.core.*;

import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

//@SpringBootTest  // with autowired below comment them to save server startup time
public class SpringDataRedisTest {


    //    @Autowired
    private RedisTemplate redisTemplate;

    @Test
    public void testRedisTemplate() {
        System.out.println(redisTemplate);
        ValueOperations valueOperations = redisTemplate.opsForValue();
        HashOperations hashOperations = redisTemplate.opsForHash();
        ListOperations listOperations = redisTemplate.opsForList();
        SetOperations setOperations = redisTemplate.opsForSet();
        ZSetOperations zSetOperations = redisTemplate.opsForZSet();
    }


    @Test
    public void testString() {
        redisTemplate.opsForValue().set("city", "Melbourne");
        Object city = (String) redisTemplate.opsForValue().get("city");
        System.out.println(city);

        redisTemplate.opsForValue().set("code", "1234", 1, TimeUnit.MINUTES);
        redisTemplate.opsForValue().setIfAbsent("lock", "1");
        redisTemplate.opsForValue().setIfAbsent("lock", "2");
    }

    @Test
    public void testHash() {
        HashOperations hashOperations = redisTemplate.opsForHash();
        hashOperations.put("100", "name", "Tom");
        hashOperations.put("100", "age", "20");
        String name = (String) hashOperations.get("100", "name");
        System.out.println(name);

        Set keys = hashOperations.keys("100");
        System.out.println(keys);

        List values = hashOperations.values("100");
        System.out.println(values);

        hashOperations.delete("100","name");

    }

    @Test
    public void testList() {
        ListOperations listOperations = redisTemplate.opsForList();

        listOperations.leftPushAll("mylist","a","b","c");
        listOperations.leftPush("mylist","d");

        List range = listOperations.range("mylist", 0, -1);
        System.out.println(range);

        listOperations.rightPop("mylist");

        Long size = listOperations.size("mylist");
        System.out.println(size);
    }

    @Test
    public void testSet() {
        SetOperations setOperations = redisTemplate.opsForSet();
        setOperations.add("set1", "a", "b", "c");
        setOperations.add("set2", "a", "b", "x", "y", "z");

        Set member = setOperations.members("set1");
        System.out.println(member);

        Long size = setOperations.size("set1");
        System.out.println(size);

        Set intersect = setOperations.intersect("set1", "set2");
        System.out.println(intersect);

        Set union = setOperations.union("set1", "set2");
        System.out.println(union);

        setOperations.remove("set1", "a", "b");
    }

    @Test
    public void testZSet() {
        ZSetOperations zSetOperations = redisTemplate.opsForZSet();
        zSetOperations.add("zset1", "a", 10);
        zSetOperations.add("zset1", "b", 12);
        zSetOperations.add("zset1", "c", 6);

        Set range = zSetOperations.range("zset1", 0, -1);
        System.out.println(range);

        zSetOperations.incrementScore("zset1", "c", 2);

        zSetOperations.remove("zset1", "a", "b");
    }

    @Test
    public void testGeneral() {
        Set keys = redisTemplate.keys("*");
        System.out.println(keys);

        Boolean name = redisTemplate.hasKey("name");
        Boolean set1 = redisTemplate.hasKey("set1");

        for (Object key : keys) {
            DataType type = redisTemplate.type(key);
            System.out.println(type.name());
        }

        redisTemplate.delete("mylist");
    }
}
