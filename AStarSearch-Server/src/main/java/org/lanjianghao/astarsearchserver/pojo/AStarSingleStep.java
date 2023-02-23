package org.lanjianghao.astarsearchserver.pojo;

import java.util.List;

public class AStarSingleStep {
    Node current;
    Node next;
    List<Node> neighbors;

    public void setCurrentNode(Node current) {
        this.current = current;
    }

    public void setNeighbors(List<Node> neighbors) {
        this.neighbors = neighbors;
    }

    public Node getCurrent() {
        return current;
    }

    public List<Node> getNeighbors() {
        return neighbors;
    }

    public Node getNext() {
        return next;
    }

    public void setNext(Node next) {
        this.next = next;
    }
}
