package com.tngtech.demo.weather.lib;

public class CsvSplitter {
    public String[] split(String line) {
        return line.split(",(?=([^\"]*\"[^\"]*\")*[^\"]*$)", -1);
    }
}
