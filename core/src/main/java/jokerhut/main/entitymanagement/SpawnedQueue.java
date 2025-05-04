package jokerhut.main.entitymanagement;

import com.badlogic.ashley.core.Entity;

import java.util.*;

public class SpawnedQueue {

    private final Queue<Entity> units = new LinkedList<>();

    public Entity getNextOf(Entity e) {
        List<Entity> list = new ArrayList<>(units);
        Collections.reverse(list);
        int i = list.indexOf(e);
        if (i != -1 && i < list.size() - 1) return list.get(i + 1);
        return null;
    }

    public Entity getSecondLast() {
        if (units.size() < 2) return null;
        List<Entity> list = new ArrayList<>(units);
        return list.get(list.size() - 2);
    }



    public void enqueue(Entity unit) {
        units.add(unit);
    }

    public Entity peek() {
        return units.peek();
    }

    public Entity dequeue() {
        return units.poll();
    }

    public boolean isEmpty() {
        return units.isEmpty();
    }

    public int size() {
        return units.size();
    }

    public List<Entity> getAll() {
        return new ArrayList<>(units);
    }

}
