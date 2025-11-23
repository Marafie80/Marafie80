package com.undertale.entity;

public class NPC extends Entity {
    private DialogueTree dialogue;
    private String name;

    public NPC(String id, int x, int y, String name) {
        super(id, x, y);
        this.name = name;
    }

    @Override
    public void update(float dt) {
        // NPCs might have idle animations or patrol behavior
    }

    public void talk() {
        if (dialogue != null) {
            dialogue.reset();
            // This would trigger the UI to show the dialogue
        }
    }

    public void onInteract() {
        talk();
    }

    @Override
    public void onCollide(Entity e) {
        // NPCs might have specific collision behavior
        if (e instanceof Player) {
            // Could trigger dialogue or other interactions
        }
    }

    public DialogueTree getDialogue() {
        return dialogue;
    }

    public void setDialogue(DialogueTree dialogue) {
        this.dialogue = dialogue;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
