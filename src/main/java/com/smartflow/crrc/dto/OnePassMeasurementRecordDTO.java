package com.smartflow.crrc.dto;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
@Data
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY, getterVisibility = JsonAutoDetect.Visibility.NONE)
public class OnePassMeasurementRecordDTO {
    public String Part_Serial;
    public String Part_Number;
    public String Pass_No;

    public List<BigDecimal> Data_Voltage;

    public List<String> Voltage_TimePoint;

    public List<BigDecimal> Data_Current;

    public List<String> Current_TimePoint;

    public List<BigDecimal> Data_Sound;

    public List<String> Sound_TimePoint;

    public String ImagePath;

    public Date Start_Time;

    public Date End_Time;
}
