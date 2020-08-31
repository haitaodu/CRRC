package com.smartflow.crrc.service;

import com.smartflow.crrc.model.*;

import java.util.List;

public interface WorkpieceService {
    /**
     * 根据工位id获取工件
     * @param plantId
     * @return
     */
    public List<Workpiece> getWorkpieceListByPlantId(Integer plantId);

    /**
     * 根据工件号和焊缝号获取工件
     * @param workpieceid
     * @param weldseamid
     * @return
     */
    public Workpiece getWorkpiece(String workpieceid, String weldseamid);

    /**
     * 根据工件号和焊缝号获取电流
     * @param workpieceid
     * @param weldseamid
     * @return
     */
    public List<Current> getCurrent(String workpieceid, String weldseamid);
    /**
     * 根据工件号和焊缝号获取电压
     * @param workpieceid
     * @param weldseamid
     * @return
     */
    public List<Voltage> getVoltage(String workpieceid, String weldseamid);
    /**
     * 根据工件号和焊缝号获取声音
     * @param workpieceid
     * @param weldseamid
     * @return
     */
    public List<Sound> getSound(String workpieceid, String weldseamid);
    /**
     * 根据工件号和焊缝号获取图像
     * @param workpieceid
     * @param weldseamid
     * @return
     */
    public Image getImage(String workpieceid, String weldseamid);
}
