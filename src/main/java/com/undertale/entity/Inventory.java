package com.undertale.entity;

import java.util.ArrayList;
import java.util.List;

public class Inventory {
    private List<Item> items;
    private int maxSize;

    public Inventory(int maxSize) {
        this.maxSize = maxSize;
        this.items = new ArrayList<>();
    }

    public boolean addItem(Item item) {
        if (items.size() < maxSize) {
            items.add(item);
            return true;
        }
        return false;
    }

    public boolean removeItem(Item item) {
        return items.remove(item);
    }

    public Item getItem(int index) {
        if (index >= 0 && index < items.size()) {
            return items.get(index);
        }
        return null;
    }

    public List<Item> getItems() {
        return new ArrayList<>(items);
    }

    public int getSize() {
        return items.size();
    }

    public int getMaxSize() {
        return maxSize;
    }

    public boolean isFull() {
        return items.size() >= maxSize;
    }

    public boolean hasItem(String itemId) {
        for (Item item : items) {
            if (item.getId().equals(itemId)) {
                return true;
            }
        }
        return false;
    }
}
