import Coord from './Coord.js'

class NodeInfo {
    constructor() {
        this.inOpen = false
        this.inClose = false
        this.pathNode = false
        this.g = 0
        this.h = 0
    }

    isInOpen(){
        return this.inOpen
    }

    setInOpen(inOpen){
        this.inOpen = inOpen
    }

    isInClose(){
        return this.inClose
    }

    setInClose(inClose){
        this.inClose = inClose
    }

    isPathNode(){
        return this.pathNode
    }

    setPathNode(pathNode){
        this.pathNode = pathNode
    }

    setG(g){
        this.g = g
    }
    
    setH(h){
        this.h = h
    }

    getF(){
        return this.g + this.h
    }
}
export default NodeInfo