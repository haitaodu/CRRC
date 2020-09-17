package com.smartflow.crrc;

import com.smartflow.crrc.dao.WorkpieceDao;
import com.smartflow.crrc.model.Current;
import com.smartflow.crrc.model.Image;
import com.smartflow.crrc.model.Sound;
import com.smartflow.crrc.model.Voltage;
import com.smartflow.crrc.service.WorkpieceService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

/**
 * @author ：tao
 * @date ：Created in 2020/9/13 23:41
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class get {
    @Autowired
    private RedisTemplate<String,Object> redisTemplate;

    @Autowired
    WorkpieceService workpieceService;

    @Autowired
    WorkpieceDao workpieceDao;
    @Test
    public void set(){
        List<Current> currents= workpieceDao.getCurrent("test1","test1");
        List<Sound> sounds=workpieceDao.getSound("test1","test1");
        List<Voltage> voltages=workpieceDao.getVoltage("test1","test1");
        Image images=workpieceDao.getImage("test1","test1");
        redisTemplate.opsForValue().set("test1current",currents);
        redisTemplate.opsForValue().set("test1sound",sounds);
        redisTemplate.opsForValue().set("test1voltage",voltages);
        redisTemplate.opsForValue().set("test1image",images);
    }

    @Test
    public void image()
    {
        Image image=workpieceService.getImage("test1","test1");
    }

    @Test
    public void get()
    {
        System.out.println(redisTemplate.opsForValue().get("test1current"));
        System.out.println(redisTemplate.opsForValue().get("test1sound"));
        System.out.println(redisTemplate.opsForValue().get("test1voltage"));
        System.out.println(redisTemplate.opsForValue().get("test1image"));
    }
}
