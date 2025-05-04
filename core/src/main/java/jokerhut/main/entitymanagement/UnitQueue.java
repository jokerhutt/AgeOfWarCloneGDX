package jokerhut.main.entitymanagement;

import com.badlogic.ashley.core.Entity;
import jokerhut.main.ecs.ECSEngine;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class UnitQueue {

    private final Queue<String> units = new LinkedList<>();

    public void enqueue(String unit) {
        units.add(unit);
    }

    public String peek() {
        return units.peek();
    }

    public String dequeue() {
        return units.poll();
    }

    public boolean isEmpty() {
        return units.isEmpty();
    }

    public int size() {
        return units.size();
    }

    public List<String> getAll() {
        return new ArrayList<>(units);
    }

}
