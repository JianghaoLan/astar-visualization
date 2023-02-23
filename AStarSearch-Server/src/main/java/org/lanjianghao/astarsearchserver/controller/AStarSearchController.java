package org.lanjianghao.astarsearchserver.controller;

import org.lanjianghao.astarsearchserver.algorithm.astar.AStarType;
import org.lanjianghao.astarsearchserver.pojo.AStarSearchResult;
import org.lanjianghao.astarsearchserver.pojo.MapInfo;
import org.lanjianghao.astarsearchserver.service.AStarSearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin(origins = "*", allowCredentials = "true", allowedHeaders = "*")
public class AStarSearchController {

    @Autowired
    AStarSearchService aStarSearchService;

    @RequestMapping("/hello")
    public String handle01(){
        return "Hello, Spring Boot 2!";
    }

    @RequestMapping("/astar/bidirectional/search")
    public AStarSearchResult bidirectionalAStarSearchHandle(@RequestBody MapInfo mapInfo){
        aStarSearchService.setAStarType(AStarType.BIDIRECTIONAL);
        return aStarSearchService.search(mapInfo);
    }


    @RequestMapping({"/astar/search", "/astar/unidirectional/search"})
    public AStarSearchResult aStarSearchHandle(@RequestBody MapInfo mapInfo){
        aStarSearchService.setAStarType(AStarType.UNIDIRECTIONAL);
        return aStarSearchService.search(mapInfo);
    }
}
