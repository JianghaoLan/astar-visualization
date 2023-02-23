package org.lanjianghao.astarsearchserver.algorithm.astar.impl;

import org.lanjianghao.astarsearchserver.algorithm.astar.AStar;
import org.lanjianghao.astarsearchserver.algorithm.astar.AStarType;
import org.lanjianghao.astarsearchserver.pojo.*;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class AStarImpl implements AStar {

    OpenList openList;
    CloseList closeList;

    public AStarSearchResult search(MapInfo mapInfo) {
        if (mapInfo == null) return null;

        createOpenCloseList(mapInfo.getHeight(), mapInfo.getWidth());
        openList.add(new Node(mapInfo.getStartCoord(), null, 0.0, getH(mapInfo.getStartCoord(), mapInfo.getEndCoord())));
        AStarSearchResult aStarSearchResult = new AStarSearchResult();

        while(!openList.isEmpty()){
            AStarSingleStep stepInfo = new AStarSingleStep();
            boolean success = step(mapInfo, stepInfo);
            aStarSearchResult.addStep(stepInfo);

            if(success){
                aStarSearchResult.isSuccess = true;
                aStarSearchResult.finalCost = getFinalCost(closeList.get(mapInfo.getEndCoord()));
                aStarSearchResult.setPath(getPath(mapInfo));
                return aStarSearchResult;
            }
        }
        //未找到终点
        return aStarSearchResult;
    }

    /**
     * 执行一步搜索
     * @param mapInfo mapInfo
     * @param stepInfo 将该步信息记录在此对象上
     * @return 是否成功
     */
    boolean step(MapInfo mapInfo, AStarSingleStep stepInfo){
        Node current = openList.poll();
        closeList.add(current);
        stepInfo.setCurrentNode(current);
        if(current.getCoord().equals(mapInfo.getEndCoord())){
            //成功找到终点
            stepInfo.setNeighbors(new ArrayList<Node>(0));
            return true;
        }
        List<Node> neighborNodes = extendNeighbors(mapInfo, current);
        stepInfo.setNeighbors(neighborNodes);
        stepInfo.setNext(openList.peek());
        if(openList.peek().getCoord().equals(current.getCoord())){
            System.out.println("Error!");
        }
        return false;
    }

    static class NeighborInfo {
        Coord coord;
        double moveCost;
        NeighborInfo(Coord coord, double cost) { this.coord = coord; this.moveCost = cost; }
    }

    List<Node> extendNeighbors(MapInfo mapInfo, Node current) {
        List<Node> extended = new ArrayList<>();
        for(NeighborInfo neighborInfo: getNeighborInfos(current.getCoord())) {
            Coord neighbor = neighborInfo.coord;
            if (!isCoordOnMap(mapInfo, neighbor)) continue;
            if (mapInfo.isBarrier(neighbor)) continue;
            if (closeList.contains(neighbor)) continue;
            //G=父节点G+移动代价+地形代价
            double G = current.getG() + neighborInfo.moveCost + mapInfo.getTerrainCost(neighbor);
            Node node = extendNeighbor(mapInfo, current, neighborInfo.coord, G);
            if(node != null) extended.add(node);
        }
        return extended;
    }

    private ArrayList<NeighborInfo> getNeighborInfos(Coord coord){
        ArrayList<NeighborInfo> neighbors = new ArrayList<>(8);

        neighbors.add(new NeighborInfo(new Coord(coord.x - 1, coord.y - 1), OBLIQUE_MOVE_COST));
        neighbors.add(new NeighborInfo(new Coord(coord.x - 1, coord.y + 1), OBLIQUE_MOVE_COST));
        neighbors.add(new NeighborInfo(new Coord(coord.x + 1, coord.y - 1), OBLIQUE_MOVE_COST));
        neighbors.add(new NeighborInfo(new Coord(coord.x + 1, coord.y + 1), OBLIQUE_MOVE_COST));
        neighbors.add(new NeighborInfo(new Coord(coord.x - 1, coord.y), DIRECT_MOVE_COST));
        neighbors.add(new NeighborInfo(new Coord(coord.x + 1, coord.y), DIRECT_MOVE_COST));
        neighbors.add(new NeighborInfo(new Coord(coord.x, coord.y - 1), DIRECT_MOVE_COST));
        neighbors.add(new NeighborInfo(new Coord(coord.x, coord.y + 1), DIRECT_MOVE_COST));
        return neighbors;
    }

    private Node extendNeighbor(MapInfo mapInfo, Node current, Coord neighbor, double g) {
        if (openList.contains(neighbor)){
            Node neighborNodeInOpen = openList.get(neighbor);
            if(g < neighborNodeInOpen.getG()){
                openList.remove(neighbor);
                Node node = new Node(neighbor, current.getCoord(), g, getH(neighbor, mapInfo.getEndCoord()));
                openList.add(node);
                return node;
            }
            return null;
        }
        else {
            Node node = new Node(neighbor, current.getCoord(), g, getH(neighbor, mapInfo.getEndCoord()));
            openList.add(node);
            return node;
        }
    }

    static double getH (Coord coord1, Coord coord2){
        int xDiff = Math.abs(coord1.x - coord2.x);
        int yDiff = Math.abs(coord1.y - coord2.y);
//        return Math.sqrt(Math.pow(xDiff, 2) + Math.pow(yDiff, 2));
        return OBLIQUE_MOVE_COST * Math.min(xDiff, yDiff) +
                DIRECT_MOVE_COST * (Math.max(xDiff, yDiff) - Math.min(xDiff, yDiff));
    }

    private boolean isCoordOnMap(MapInfo mapInfo, Coord coord){
        return coord.x >= 0 && coord.x < mapInfo.getWidth()
                && coord.y >= 0 && coord.y < mapInfo.getHeight();
    }

    private List<Node> getPath(MapInfo mapInfo){
        List<Node> path = new ArrayList<>();

        Coord coord = mapInfo.getEndCoord();
        Node node = closeList.get(coord);
        path.add(node);
        while (!node.getCoord().equals(mapInfo.getStartCoord())){
            node = closeList.getParent(node);
            path.add(node);
        }
        Collections.reverse(path);
        return path;
    }

    private static double getFinalCost(Node endNode){
        return endNode.getG();
    }

    void createOpenCloseList(int height, int width){
        openList = new OpenList(height, width);
        closeList = new CloseList(height, width);
    }

    @Override
    public AStarType getCode() {
        return AStarType.UNIDIRECTIONAL;
    }
}

class OpenList{
    Queue<Node> openQueue; // 优先队列(升序)
    Node[][] openMap;

    public OpenList(int height, int width) {
        this.openQueue = new PriorityQueue<>();
        this.openMap = new Node[height][width];
    }

    void add(Node node) {
        if (this.contains(node.getCoord())){
            System.out.println("[OpenList.add()] Node in this coord is already in openList!");
            return;
        }
        openQueue.add(node);
        this.openMap[node.getCoord().y][node.getCoord().x] = node;
    }

    Node poll(){
        cleanQueueHead();

        Node node = openQueue.poll();
        assert(openMap[node.getCoord().y][node.getCoord().x] != null);
        openMap[node.getCoord().y][node.getCoord().x] = null;
        return node;
    }

    Node peek(){
        cleanQueueHead();
        return openQueue.peek();
    }

    Node get(Coord coord){
        return openMap[coord.y][coord.x];
    }

    boolean contains(Coord coord){
        return this.openMap[coord.y][coord.x] != null;
    }

    void remove(Coord coord){
        //在openQueue上remove的复杂度为O(n)，因此仅在openMap上作标记，poll()或isEmpty()时会在openMap上检查是否存在
        assert(openMap[coord.y][coord.x] != null);
        openMap[coord.y][coord.x] = null;
//        cleanQueueHead();
    }

    boolean isEmpty(){
        //除掉队列头实际已经被删除的元素
        cleanQueueHead();
        return this.openQueue.isEmpty();
    }

    void cleanQueueHead(){
        //除掉队列头实际已经被删除的元素
        while(!openQueue.isEmpty() && !contains(openQueue.peek().getCoord())){
            openQueue.remove();
        }
    }
}

class CloseList{
    Node[][] closeMap;

    CloseList(int height, int width){
        closeMap = new Node[height][width];
    }

    boolean contains(Coord coord){
        return closeMap[coord.y][coord.x] != null;
    }

    void add(Node node){
        Coord coord = node.getCoord();
        closeMap[coord.y][coord.x] = node;
    }

    Node get(Coord coord){
        return closeMap[coord.y][coord.x];
    }

    Node getParent(Node node) {
        Coord parentCoord = node.getParent();
        return closeMap[parentCoord.y][parentCoord.x];
    }
}