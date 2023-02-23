package org.lanjianghao.astarsearchserver.service.impl;

import org.lanjianghao.astarsearchserver.algorithm.astar.AStar;
import org.lanjianghao.astarsearchserver.algorithm.astar.AStarFactory;
import org.lanjianghao.astarsearchserver.algorithm.astar.AStarType;
import org.lanjianghao.astarsearchserver.algorithm.astar.impl.AStarImpl;
import org.lanjianghao.astarsearchserver.algorithm.astar.impl.BidirectionalAStarImpl;
import org.lanjianghao.astarsearchserver.pojo.*;
import org.lanjianghao.astarsearchserver.service.AStarSearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AStarSearchServiceImpl implements AStarSearchService {

    AStarType aStarType = AStarType.UNIDIRECTIONAL;

    @Autowired
    AStarFactory aStarFactory;

    @Override
    public AStarSearchResult search(MapInfo mapInfo) {
        AStar aStar = aStarFactory.getAStar(this.aStarType);
        return aStar.search(mapInfo);
    }

    @Override
    public AStarType getAStarType() {
        return aStarType;
    }

    @Override
    public void setAStarType(AStarType aStarType) {
        this.aStarType = aStarType;
    }
}
