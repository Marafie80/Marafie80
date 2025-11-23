package com.hospital.repository.impl;

import com.hospital.model.InventoryItem;
import com.hospital.repository.CrudRepository;

import java.util.*;
import java.util.stream.Collectors;

public class InMemoryInventoryRepository implements CrudRepository<InventoryItem, String> {
    private final Map<String, InventoryItem> db = new HashMap<>();

    @Override
    public InventoryItem save(InventoryItem item) {
        if (item.getId() == null || item.getId().isEmpty()) {
            item.setId(UUID.randomUUID().toString());
        }
        db.put(item.getId(), item);
        return item;
    }

    @Override
    public Optional<InventoryItem> findById(String id) {
        return Optional.ofNullable(db.get(id));
    }

    @Override
    public List<InventoryItem> findAll() {
        return new ArrayList<>(db.values());
    }

    @Override
    public void deleteById(String id) {
        db.remove(id);
    }

    public List<InventoryItem> findLowStock() {
        return db.values().stream()
                .filter(InventoryItem::needsReorder)
                .collect(Collectors.toList());
    }
}
