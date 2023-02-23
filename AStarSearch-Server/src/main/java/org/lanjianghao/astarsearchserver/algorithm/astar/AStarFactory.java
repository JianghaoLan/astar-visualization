package org.lanjianghao.astarsearchserver.algorithm.astar;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class AStarFactory implements ApplicationContextAware {
    private static Map<AStarType, AStar> aStarBeanMap;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        Map<String, AStar> map = applicationContext.getBeansOfType(AStar.class);
        aStarBeanMap = new HashMap<>();
        map.forEach((key, value) -> aStarBeanMap.put(value.getCode(), value));
    }

    public AStar getAStar(AStarType type) {
        return aStarBeanMap.get(type);
    }
}
