package org.example.entities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import java.util.Date;
import java.util.List;
import java.util.Map;

@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
@JsonIgnoreProperties(ignoreUnknown = true)
public class Train {


    private String trainNo;
    private String trainId;
    private List<List<Integer>> seats;
    private Map<String, String> stationTime;
    private List<String> stations;

    public Train(String trainNo,String trainId,List<List<Integer>> seats,Map<String,String> stationTime,List<String> stations)
    {
        this.trainNo=trainNo;
        this.trainId=trainId;
        this.seats=seats;
        this.stations=stations;
        this.stationTime=stationTime;
    }

    public Train() {

    }

    public String getTrainNo() {
        return trainNo;
    }

    public void setTrainNo(String trainNo) {
        this.trainNo = trainNo;
    }

    public String getTrainId() {
        return trainId;
    }

    public void setTrainId(String trainId) {
        this.trainId = trainId;
    }

    public List<List<Integer>> getSeats() {
        return seats;
    }

    public void setSeats(List<List<Integer>> seats) {
        this.seats = seats;
    }

    public Map<String, String> getStationTime() {
        return stationTime;
    }

    public void setStationTime(Map<String, String> stationTime) {
        this.stationTime = stationTime;
    }

    public List<String> getStations() {
        return stations;
    }

    public void setStations(List<String> stations) {

        this.stations =stations;
}
   public String gettraininfo()
   {
      return String.format("train id is %s train no: %s",trainId,trainNo);
   }
}
