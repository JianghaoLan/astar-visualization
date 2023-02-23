package org.lanjianghao.astarsearchserver.service;

import org.lanjianghao.astarsearchserver.algorithm.astar.AStarType;
import org.lanjianghao.astarsearchserver.pojo.AStarSearchResult;
import org.lanjianghao.astarsearchserver.pojo.MapInfo;

public interface AStarSearchService {
    AStarSearchResult search(MapInfo mapInfo);
    void setAStarType(AStarType aStarType);
    AStarType getAStarType();
}
