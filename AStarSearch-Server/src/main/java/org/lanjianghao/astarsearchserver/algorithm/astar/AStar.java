package org.lanjianghao.astarsearchserver.algorithm.astar;

import org.lanjianghao.astarsearchserver.pojo.AStarSearchResult;
import org.lanjianghao.astarsearchserver.pojo.MapInfo;

public interface AStar {
    static double DIRECT_MOVE_COST = 1.0;
    static double OBLIQUE_MOVE_COST = Math.sqrt(2.0);

    public AStarSearchResult search(MapInfo mapInfo);
    public AStarType getCode();
}
