package com.smartflow.crrc.controller;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Future;
import java.util.stream.Collectors;

import com.smartflow.crrc.dto.OnePassMeasurementRecordDTO;
import com.smartflow.crrc.dto.RealTimeDataOutputDTO;
import com.smartflow.crrc.model.*;
import com.smartflow.crrc.service.WorkpieceService;
import com.smartflow.crrc.util.GzipUtil;
import lombok.extern.log4j.Log4j2;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@Log4j2
@RestController
@RequestMapping("/RealTimeData")
public class RealTimeDataController extends BaseController{

    final
    WorkpieceService workpieceService;

    @Resource(name = "defaultThreadPool")
    private ThreadPoolTaskExecutor poolTaskExecutor;
    //电压电流的坐标值是需要标定使用的(固定系数)  电流乘219电压乘21
    private static final BigDecimal CurrentCoefficient = new BigDecimal(219);//电流系数
    private static final BigDecimal VoltageCoefficient = new BigDecimal(21);//电压系数

    @Autowired
    public RealTimeDataController(WorkpieceService workpieceService) {
        this.workpieceService = workpieceService;
    }

    @CrossOrigin(origins="*",maxAge=3600)
    @GetMapping(value = "/GetRealTimeData/{id}")
    public Map<String,Object> getRealTimeData(@PathVariable Integer id){
        try{
            long startTime=System.currentTimeMillis();
            List<Workpiece> workpieceList = workpieceService.getWorkpieceListByPlantId(id);
            if (workpieceList.size() == 0)
            {
                return this.setJson(200, "Success", GetNoData());
            }
            Workpiece workpiece = workpieceList.get(workpieceList.size()-1);
            RealTimeDataOutputDTO data = new RealTimeDataOutputDTO();
            String workpieceid = workpiece.getWorkpieceid();
            String weldseamid = workpiece.getWeldseamid();
            List<Current> currentList = workpieceService.getCurrent(workpieceid, weldseamid);

            List<Voltage> voltageList = workpieceService.getVoltage(workpieceid, weldseamid);
            List<Sound> soundList = workpieceService.getSound(workpieceid, weldseamid);
            Image image = workpieceService.getImage(workpieceid, weldseamid);

            List<String> pass_noList = workpieceList.stream().filter(w -> !w.getWorkpieceid().equals(workpieceid)).map(w -> w.getWeldseamid()).collect(Collectors.toList());

            workpiece = workpieceService.getWorkpiece(workpieceid, weldseamid);
            data = GetUnitMeasurementHistoryByPartSerialAndPassNo(workpiece,
                    pass_noList, currentList,
                    voltageList,
                    soundList, image);
            data.SerialNumbers = pass_noList;
            long endTime=System.currentTimeMillis();
            System.out.println(endTime-startTime);
            //GZIP压缩数据
            String compressData = GzipUtil.compress(JSONObject.fromObject(data).toString());
            return this.setJson(200, "Success", compressData);
        }catch(Exception e){
            e.printStackTrace();
            log.error(e);
            return this.setJson(0, "Error:"+e.getMessage(), -1);
        }
    }

    public RealTimeDataOutputDTO GetUnitMeasurementHistoryByPartSerialAndPassNo
            (Workpiece workpiece, List<String> pass_noList,
             List<Current> currentList, List<Voltage> voltageList, List<Sound> soundList, Image image)
    {
        RealTimeDataOutputDTO data = new RealTimeDataOutputDTO();
        if (CollectionUtils.isEmpty(currentList))
        {
            return GetNoData();
        }
        Current current = currentList.get(currentList.size()-1);
        OnePassMeasurementRecordDTO onePassNoData = new OnePassMeasurementRecordDTO();
        onePassNoData.Part_Serial = workpiece.getWorkpieceid();
        onePassNoData.Part_Number = workpiece.getWorkpiecename();
        onePassNoData.End_Time = current.getCtime();
        onePassNoData.Pass_No = workpiece.getWeldseamid();
        onePassNoData.Start_Time = current.getLtime();

        StringBuffer totalCurrent = new StringBuffer();
        StringBuffer totalVoltage = new StringBuffer();
        StringBuffer totalSound = new StringBuffer();
        List<String> currentTimePoints = new ArrayList<>();
        List<String> voltageTimePoints = new ArrayList<>();
        List<String> soundTimePoints = new ArrayList<>();

        int currentListSize = currentList.size();
        SimpleDateFormat sdf = new SimpleDateFormat("hh:mm:ss");
            for (int i = 0; i < currentListSize; i++)
            {
                CountDownLatch countDownLatch = new CountDownLatch(currentListSize);
                int finalI = i;
            poolTaskExecutor.submit(() -> {

                    int currentLength = currentList.get(finalI).getCurrent().split("\\|").length - 1;
                    for (int j = 0; j < currentLength; j++)
                    {
                        currentTimePoints.add(sdf.format(currentList.get(finalI).getLtime()));
                        voltageTimePoints.add(sdf.format(voltageList.get(finalI).getLtime()));
                    }
                    totalCurrent.append(currentList.get(finalI).getCurrent());
                    totalVoltage.append(voltageList.get(finalI).getVoltage());
                countDownLatch.countDown();
                });
                //int currentLength = currentList.get(i).getCurrent().split("\\|").length - 1;
                //for (int j = 0; j < currentLength; j++)
                //{
                //    currentTimePoints.add(sdf.format(currentList.get(i).getLtime()));
                //    voltageTimePoints.add(sdf.format(voltageList.get(i).getLtime()));
                //}
                //totalCurrent.append(currentList.get(i).getCurrent());
                //totalVoltage.append(voltageList.get(i).getVoltage());
            }
        String[] dataCurrent;
        dataCurrent = totalCurrent.toString().split("\\|");
        List<BigDecimal> dataCurrentDecimal = new ArrayList<>();
        for (String s : dataCurrent)
        {
            if (StringUtils.isEmpty(s))
            {
                dataCurrentDecimal.add(new BigDecimal(0));
            }
            else
            {
                dataCurrentDecimal.add(new BigDecimal(s).multiply(CurrentCoefficient));
            }
        }
        onePassNoData.Current_TimePoint = currentTimePoints;
        onePassNoData.Data_Current = dataCurrentDecimal;

            String[] dataVoltage = totalVoltage.toString().split("\\|");
            List<BigDecimal> dataVoltageDecimal = new ArrayList<>();
            for (String s : dataVoltage)
            {
                if (StringUtils.isEmpty(s))
                {
                    dataVoltageDecimal.add(new BigDecimal(0));//
                }
                else
                {
                    dataVoltageDecimal.add(new BigDecimal(s).multiply(VoltageCoefficient));
                }
            }
            onePassNoData.Voltage_TimePoint = voltageTimePoints;
            onePassNoData.Data_Voltage = dataVoltageDecimal;
        List<BigDecimal> dataSoundDecimal = new ArrayList<>();
        if (!CollectionUtils.isEmpty(soundList))
        {
            int soundListSize = soundList.size();
            for (Sound sound : soundList)
            {
                int soundSplitLength = sound.getSound().split("\\|").length - 1;
                CountDownLatch countDownLatch=new CountDownLatch(soundSplitLength);
                poolTaskExecutor.submit(() -> {
                for (int j = 0; j < soundSplitLength; j++)
                {
                    soundTimePoints.add(sdf.format(sound.getLtime()));
                }
                totalSound.append(sound.getSound());
                    countDownLatch.countDown();
            });
            }
            String[] dataSound = totalSound.substring(0, totalSound.length() - 1).split("\\|");
            for (String s : dataSound)
            {
                if (StringUtils.isEmpty(s))
                {
                    dataSoundDecimal.add(new BigDecimal(0));
                }
                else
                {
                    dataSoundDecimal.add(new BigDecimal(s));
                }
            }

        }
        onePassNoData.Sound_TimePoint = soundTimePoints;
        onePassNoData.Data_Sound = dataSoundDecimal;

        if (image != null) {
            onePassNoData.ImagePath = "data:image/png;base64," + image.getImage();
        }
        onePassNoData.setPart_Serial(workpiece.getWorkpieceid());
        BigDecimal currentMaxValue = Collections.max(onePassNoData.getData_Current());
        BigDecimal voltageMaxValue = Collections.max(onePassNoData.getData_Voltage());
        data.setCurrentMax(currentMaxValue);
        data.setVoltageMax(voltageMaxValue);
        data.setOnePassMeasurementRecord(onePassNoData);
        return data;
    }


    public RealTimeDataOutputDTO GetNoData()
    {
        RealTimeDataOutputDTO realTimeDataOutputDTO = new RealTimeDataOutputDTO();
        realTimeDataOutputDTO.setCurrentMax(new BigDecimal(0));
        realTimeDataOutputDTO.setVoltageMax(new BigDecimal(0));
        OnePassMeasurementRecordDTO onePassMeasurementRecordDTO = new OnePassMeasurementRecordDTO();
        List<String> xAxisList = new ArrayList<>();
        onePassMeasurementRecordDTO.setCurrent_TimePoint(xAxisList);
        onePassMeasurementRecordDTO.setVoltage_TimePoint(xAxisList);
        onePassMeasurementRecordDTO.setSound_TimePoint(xAxisList);
        List<BigDecimal> yAxisList = new ArrayList<>();
        onePassMeasurementRecordDTO.setData_Current(yAxisList);
        onePassMeasurementRecordDTO.setData_Voltage(yAxisList);
        onePassMeasurementRecordDTO.setData_Sound(yAxisList);
        Date date = new Date();
        onePassMeasurementRecordDTO.setEnd_Time(date);
        onePassMeasurementRecordDTO.setStart_Time(date);

        realTimeDataOutputDTO.setPass_Nos(xAxisList);
        realTimeDataOutputDTO.setSerialNumbers(xAxisList);
        realTimeDataOutputDTO.setOnePassMeasurementRecord(onePassMeasurementRecordDTO);
        return realTimeDataOutputDTO;
    }

    private OnePassMeasurementRecordDTO splitCurrent(List<Current> currentList,List<Voltage> voltages,
                                                     OnePassMeasurementRecordDTO onePassMeasurementRecordDTO) {
    return new OnePassMeasurementRecordDTO();
    }
}
