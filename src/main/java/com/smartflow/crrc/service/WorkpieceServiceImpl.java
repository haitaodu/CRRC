package com.smartflow.crrc.service;

import com.smartflow.crrc.dao.WorkpieceDao;
import com.smartflow.crrc.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author haita
 */
@Service
public class WorkpieceServiceImpl implements WorkpieceService {
    private final RedisTemplate<String,Object> redisTemplate;

    private static final String IMAGE="image";
    private static final String SOUNDE="sound";
    private static final String CURRENT="current";
    private static final String VOLTAGE="voltage";

    final
    WorkpieceDao workpieceDao;

    @Autowired
    public WorkpieceServiceImpl(WorkpieceDao workpieceDao, RedisTemplate<String, Object> redisTemplate) {
        this.workpieceDao = workpieceDao;
        this.redisTemplate = redisTemplate;
    }

    @Override
    public List<Workpiece> getWorkpieceListByPlantId(Integer plantId) {
        return workpieceDao.getWorkpieceListByPlantId(plantId);
    }

    @Override
    public Workpiece getWorkpiece(String workpieceid, String weldseamid) {
        return workpieceDao.getWorkpiece(workpieceid, weldseamid);
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<Current> getCurrent(String workpieceid, String weldseamid) {
        if (redisTemplate.opsForValue().get(workpieceid+CURRENT)!=null)
        {
            System.out.println("从redis获取current数据");
            return (List<Current>) redisTemplate.opsForValue().get(workpieceid+CURRENT);
        }
        else {
            List<Current> current=workpieceDao.getCurrent(workpieceid, weldseamid);
            redisTemplate.opsForValue().set(workpieceid+CURRENT,current);
            return current;
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<Voltage> getVoltage(String workpieceid, String weldseamid) {
        if (redisTemplate.opsForValue().get(workpieceid+VOLTAGE)!=null)
        {
            System.out.println("从redis获取voltage数据");
            return (List<Voltage>) redisTemplate.opsForValue().get(workpieceid+VOLTAGE);
        }
        else {
            List<Voltage> voltage=workpieceDao.getVoltage(workpieceid, weldseamid);
            redisTemplate.opsForValue().set(workpieceid+VOLTAGE,voltage);
            return voltage;
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<Sound> getSound(String workpieceid, String weldseamid) {
        if (redisTemplate.opsForValue().get(workpieceid+SOUNDE)!=null)
        {
            System.out.println("从redis获取sound数据");
            return (List<Sound>) redisTemplate.opsForValue().get(workpieceid+SOUNDE);
        }
        else {
            List<Sound> sound=workpieceDao.getSound(workpieceid, weldseamid);
            redisTemplate.opsForValue().set(workpieceid+IMAGE,sound);
            return sound;
        }
    }

    @Override
    public Image getImage(String workpieceid, String weldseamid) {
        if (redisTemplate.opsForValue().get(workpieceid+IMAGE)!=null)
        {
            System.out.println("从redis获取image数据");
           return (Image) redisTemplate.opsForValue().get(workpieceid+IMAGE);
        }
        else {
            Image image=workpieceDao.getImage(workpieceid, weldseamid);
            redisTemplate.opsForValue().set(workpieceid+IMAGE,image);
            return image;
        }
    }
}
