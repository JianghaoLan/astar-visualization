package org.lanjianghao.astarsearchserver.pojo;

import java.util.Arrays;
import java.util.HashMap;

public class MapInfo {
    private int[][] labels;
    private int width;
    private int height;
    private Coord startCoord;
    private Coord endCoord;
    private HashMap<Integer, LabelInfo> labelInfos;

    public MapInfo() {}

    public MapInfo(int[][] labels, int width, int height, Coord startCoord, Coord endCoord,
                   HashMap<Integer, LabelInfo> labelInfos) {
        this.labels = labels;
        this.width = width;
        this.height = height;
        this.startCoord = startCoord;
        this.endCoord = endCoord;
        this.labelInfos = labelInfos;
    }

    private int getLabel(Coord coord){
        //System.out.println(coord);
        return labels[coord.y][coord.x];
    }

    public boolean isBarrier(Coord coord){
        return labelInfos.get(this.getLabel(coord)).isBarrier();
    }

    public double getTerrainCost(Coord coord){
        return labelInfos.get(this.getLabel(coord)).getTerrainCost();
    }

    public Coord getStartCoord() {
        return startCoord;
    }

    public void setStartCoord(Coord startCoord) {
        this.startCoord = startCoord;
    }

    public Coord getEndCoord() {
        return endCoord;
    }

    public void setEndCoord(Coord endCoord) {
        this.endCoord = endCoord;
    }

    public int[][] getLabels() {
        return labels;
    }

    public void setLabels(int[][] labels) {
        this.labels = labels;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public HashMap<Integer, LabelInfo> getLabelsInfo() {
        return labelInfos;
    }

    public void setLabelInfos(HashMap<Integer, LabelInfo> labelInfos) {
        this.labelInfos = labelInfos;
    }

    @Override
    public String toString() {
        return "MapInfo{" +
                "labels=" + Arrays.deepToString(labels) + '\n' +
                ", width=" + width +
                ", height=" + height +
                ", startCoord=" + startCoord +
                ", endCoord=" + endCoord +
                ", labelInfos=" + labelInfos +
                '}';
    }
}

class LabelInfo {
    //地形名字
    private String name;

    //是否为障碍物
    private boolean isBarrier;

    //地形代价
    private double terrainCost;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isBarrier() {
        return isBarrier;
    }

    public void setIsBarrier(boolean barrier) {
        isBarrier = barrier;
    }

    public double getTerrainCost() {
        return terrainCost;
    }

    public void setTerrainCost(double terrainCost) {
        this.terrainCost = terrainCost;
    }

    @Override
    public String toString() {
        return "LabelInfo{" +
                "name='" + name + '\'' +
                ", isBarrier=" + isBarrier +
                ", terrainCost=" + terrainCost +
                '}';
    }
}
