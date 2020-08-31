package com.smartflow.crrc.dto;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;
@Data
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY, getterVisibility = JsonAutoDetect.Visibility.NONE)
public class RealTimeDataOutputDTO {
    public String Part_Serial;
    public List<String> SerialNumbers;

    public BigDecimal CurrentMax;
    public BigDecimal VoltageMax;
    public BigDecimal SoundMax;

    public List<String> Pass_Nos;
    public OnePassMeasurementRecordDTO onePassMeasurementRecord;
}
