package com.undertale.entity;

import java.util.HashMap;
import java.util.Map;

public class DialogueTree {
    private Map<String, DialogueNode> nodes;
    private String currentNodeId;
    private String rootNodeId;

    public DialogueTree(String rootNodeId) {
        this.nodes = new HashMap<>();
        this.rootNodeId = rootNodeId;
        this.currentNodeId = rootNodeId;
    }

    public void addNode(String nodeId, DialogueNode node) {
        nodes.put(nodeId, node);
    }

    public DialogueNode getCurrentNode() {
        return nodes.get(currentNodeId);
    }

    public void selectChoice(int choiceIndex) {
        DialogueNode current = getCurrentNode();
        if (current != null && choiceIndex >= 0 && choiceIndex < current.getChoices().size()) {
            String nextNodeId = current.getNextNodeIds().get(choiceIndex);
            if (nextNodeId != null) {
                currentNodeId = nextNodeId;
            }
        }
    }

    public void reset() {
        currentNodeId = rootNodeId;
    }

    public boolean isAtEnd() {
        DialogueNode current = getCurrentNode();
        return current == null || current.getChoices().isEmpty();
    }

    public static class DialogueNode {
        private String text;
        private java.util.List<String> choices;
        private java.util.List<String> nextNodeIds;

        public DialogueNode(String text) {
            this.text = text;
            this.choices = new java.util.ArrayList<>();
            this.nextNodeIds = new java.util.ArrayList<>();
        }

        public void addChoice(String choice, String nextNodeId) {
            choices.add(choice);
            nextNodeIds.add(nextNodeId);
        }

        public String getText() {
            return text;
        }

        public java.util.List<String> getChoices() {
            return choices;
        }

        public java.util.List<String> getNextNodeIds() {
            return nextNodeIds;
        }
    }
}
