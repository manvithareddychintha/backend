package com.recursion.portfolioManager.models;

import lombok.Data;

import java.util.List;

@Data
public class TimeSeriesDaily {


    private List<TimeSeriesPerDay> timeSeriesPerDayList;
}
