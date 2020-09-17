package com.smartflow.crrc.schedule;

import com.smartflow.crrc.dao.WorkpieceDao;
import com.smartflow.crrc.model.Current;
import com.smartflow.crrc.model.Image;
import com.smartflow.crrc.model.Sound;
import com.smartflow.crrc.model.Voltage;
import com.smartflow.crrc.service.WorkpieceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author ：tao
 * @date ：Created in 2020/9/15 20:03
 */
@Component
public class ScheduleForRedis {
    private final RedisTemplate<String,Object> redisTemplate;

    private final
    WorkpieceDao workpieceDao;

    @Autowired
    public ScheduleForRedis(RedisTemplate<String, Object> redisTemplate, WorkpieceDao workpieceDao) {
        this.redisTemplate = redisTemplate;
        this.workpieceDao = workpieceDao;
    }

    /**
     * 每天凌晨三点执行一次，用作向redis里添加数据，预热
     */
    @Scheduled(cron = "0 0 3 * * ? ")
    public void printSay() {
        List<Current> currents= workpieceDao.getCurrent("test1","test1");
        List<Sound> sounds=workpieceDao.getSound("test1","test1");
        List<Voltage> voltages=workpieceDao.getVoltage("test1","test1");
        Image images=workpieceDao.getImage("test1","test1");
        redisTemplate.opsForValue().set("test1current",currents);
        redisTemplate.opsForValue().set("test1sound",sounds);
        redisTemplate.opsForValue().set("test1voltage",voltages);
        redisTemplate.opsForValue().set("test1image",images);
    }
}
