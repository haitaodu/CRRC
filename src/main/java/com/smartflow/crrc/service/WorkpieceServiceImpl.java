package com.smartflow.crrc.service;

import com.smartflow.crrc.dao.WorkpieceDao;
import com.smartflow.crrc.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class WorkpieceServiceImpl implements WorkpieceService {
    @Autowired
    WorkpieceDao workpieceDao;
    @Override
    public List<Workpiece> getWorkpieceListByPlantId(Integer plantId) {
        return workpieceDao.getWorkpieceListByPlantId(plantId);
    }

    @Override
    public Workpiece getWorkpiece(String workpieceid, String weldseamid) {
        return workpieceDao.getWorkpiece(workpieceid, weldseamid);
    }

    @Override
    public List<Current> getCurrent(String workpieceid, String weldseamid) {
        return workpieceDao.getCurrent(workpieceid, weldseamid);
    }

    @Override
    public List<Voltage> getVoltage(String workpieceid, String weldseamid) {
        return workpieceDao.getVoltage(workpieceid, weldseamid);
    }

    @Override
    public List<Sound> getSound(String workpieceid, String weldseamid) {
        return workpieceDao.getSound(workpieceid, weldseamid);
    }

    @Override
    public Image getImage(String workpieceid, String weldseamid) {
        return workpieceDao.getImage(workpieceid, weldseamid);
    }
}
