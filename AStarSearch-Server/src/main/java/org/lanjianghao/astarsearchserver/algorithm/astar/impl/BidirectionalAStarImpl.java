package org.lanjianghao.astarsearchserver.algorithm.astar.impl;

import org.lanjianghao.astarsearchserver.algorithm.astar.AStar;
import org.lanjianghao.astarsearchserver.algorithm.astar.AStarType;
import org.lanjianghao.astarsearchserver.pojo.*;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Component
public class BidirectionalAStarImpl implements AStar {
    private AStarImpl forwardAStar;
    private AStarImpl backwardAStar;

    public AStarSearchResult search(MapInfo mapInfo){
        if (mapInfo == null) return null;

        forwardAStar = new AStarImpl();
        backwardAStar = new AStarImpl();
        forwardAStar.createOpenCloseList(mapInfo.getHeight(), mapInfo.getWidth());
        backwardAStar.createOpenCloseList(mapInfo.getHeight(), mapInfo.getWidth());
        forwardAStar.openList.add(new Node(mapInfo.getStartCoord(),
                null, 0.0, AStarImpl.getH(mapInfo.getStartCoord(), mapInfo.getEndCoord())));
        backwardAStar.openList.add(new Node(mapInfo.getEndCoord(),
                null, 0.0, AStarImpl.getH(mapInfo.getEndCoord(), mapInfo.getStartCoord())));
        MapInfo backwardMapInfo = getBackwardMapInfo(mapInfo);
        
        AStarSearchResult aStarSearchResult = new AStarSearchResult(true);

        while(!forwardAStar.openList.isEmpty() && !forwardAStar.openList.isEmpty()){
            AStarSingleStep[] stepInfo = new AStarSingleStep[]{new AStarSingleStep(), new AStarSingleStep()};
            Coord meetCoord = step(forwardAStar, backwardAStar, mapInfo, stepInfo[0]);
            if(meetCoord != null){
                aStarSearchResult.addBidirectionalStep(stepInfo);
                aStarSearchResult.isSuccess = true;
                aStarSearchResult.finalCost = getFinalCost(meetCoord);
                aStarSearchResult.setBidirectionalPath(getPath(mapInfo, meetCoord, forwardAStar));
                return aStarSearchResult;
            }

            meetCoord = step(backwardAStar, forwardAStar, backwardMapInfo, stepInfo[1]);
            aStarSearchResult.addBidirectionalStep(stepInfo);
            if(meetCoord != null){
                aStarSearchResult.isSuccess = true;
                aStarSearchResult.finalCost = getFinalCost(meetCoord);
                aStarSearchResult.setBidirectionalPath(getPath(backwardMapInfo, meetCoord, backwardAStar));
                return aStarSearchResult;
            }
        }
        //未找到终点
        return aStarSearchResult;
    }

    /**
     * 执行一步搜索
     * @param currentAStar 当前AStar
     * @param anotherAStar 对面AStar
     * @param mapInfo
     * @param stepInfo 将该步信息记录在此对象上
     * @return 成功找到解则返回相遇坐标，否则返回null
     */
    private Coord step(AStarImpl currentAStar, AStarImpl anotherAStar, MapInfo mapInfo, AStarSingleStep stepInfo){
        Node current = currentAStar.openList.poll();
        currentAStar.closeList.add(current);
        stepInfo.setCurrentNode(current);

        //如果当前结点在对面的close表中，成功找到解
        if(anotherAStar.closeList.contains(current.getCoord())){
            stepInfo.setNeighbors(new ArrayList<Node>(0));
            return current.getCoord();
        }

        List<Node> neighborNodes = currentAStar.extendNeighbors(mapInfo, current);
        stepInfo.setNeighbors(neighborNodes);
        stepInfo.setNext(currentAStar.openList.peek());
        return null;
    }

    private List<List<Node>> getPath(MapInfo mapInfo, Coord meetCoord, AStarImpl whoMetTheOtherFirst){
        List<Node> pathFromStart = new ArrayList<>();
        List<Node> pathFromEnd = new ArrayList<>();

        Node node = forwardAStar.closeList.get(meetCoord);
        if(whoMetTheOtherFirst == forwardAStar) pathFromStart.add(node);
        while (!node.getCoord().equals(mapInfo.getStartCoord())){
            node = forwardAStar.closeList.getParent(node);
            pathFromStart.add(node);
        }

        node = backwardAStar.closeList.get(meetCoord);
        if(whoMetTheOtherFirst == backwardAStar) pathFromStart.add(node);
        pathFromEnd.add(node);
        while (!node.getCoord().equals(mapInfo.getEndCoord())){
            node = backwardAStar.closeList.getParent(node);
            pathFromEnd.add(node);
        }
        Collections.reverse(pathFromStart);

        List<List<Node>> path = new ArrayList<>(2);
        path.add(pathFromStart);
        path.add(pathFromEnd);
        return path;
    }
    
    private static MapInfo getBackwardMapInfo(MapInfo mapInfo){
        return new MapInfo(mapInfo.getLabels(), mapInfo.getWidth(), mapInfo.getHeight(),
                mapInfo.getEndCoord(), mapInfo.getStartCoord(), mapInfo.getLabelsInfo());
    }

    @Override
    public AStarType getCode() {
        return AStarType.BIDIRECTIONAL;
    }

    private double getFinalCost(Coord meetCoord) {
        Node forwardNode = forwardAStar.closeList.get(meetCoord);
        Node backwardNode = backwardAStar.closeList.get(meetCoord);
        return forwardNode.getG() + backwardNode.getG();
    }
}
