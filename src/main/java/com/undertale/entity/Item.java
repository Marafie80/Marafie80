package com.undertale.entity;

public class Item {
    private String id;
    private String name;
    private String description;
    private ItemType type;

    public enum ItemType {
        CONSUMABLE,
        KEY_ITEM,
        EQUIPMENT,
        MISC
    }

    public Item(String id, String name, String description, ItemType type) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.type = type;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public ItemType getType() {
        return type;
    }

    public void use(Player player) {
        // Override in specific item implementations
    }
}
