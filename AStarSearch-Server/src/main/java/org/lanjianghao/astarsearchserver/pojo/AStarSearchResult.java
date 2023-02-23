package org.lanjianghao.astarsearchserver.pojo;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class AStarSearchResult {
    //是否为双向搜索
    public boolean bidirectional;

    //是否找到路径
    public boolean isSuccess;

    //最终代价
    public double finalCost;

    //最终路径
    public List<List<Node>> path;

    //单步信息
    public List<AStarSingleStep[]> steps;

    public AStarSearchResult(){
        this(false);
    }

    public AStarSearchResult(boolean bidirectional){
        isSuccess = false;
        finalCost = -1.0;
        path = null;
        this.bidirectional = bidirectional;
        steps = new ArrayList<>();
    }

    public void addStep(AStarSingleStep step){
        steps.add(new AStarSingleStep[]{step});
    }

    public void addBidirectionalStep(AStarSingleStep[] step){
        steps.add(step);
    }

    public void setPath(List<Node> path) {
        this.path = new ArrayList<>(1);
        this.path.add(path);
    }

    public void setBidirectionalPath(List<List<Node>> path){
        this.path = path;
    }
}

