package ru.urvanov.keystore.controller.domain;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class TreeItem implements Serializable {
    /**
     * 
     */
    private static final long serialVersionUID = -7555454118551049712L;
    private String id;
    private String text;
    private boolean leaf;
    private boolean expanded;
    private List<TreeItem> children = new ArrayList<TreeItem>();

    public String getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = String.valueOf(id);
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public boolean isLeaf() {
        return leaf;
    }

    public void setLeaf(boolean leaf) {
        this.leaf = leaf;
    }

    public boolean isExpanded() {
        return expanded;
    }

    public void setExpanded(boolean expanded) {
        this.expanded = expanded;
    }

    public List<TreeItem> getChildren() {
        return children;
    }

    public void setChildren(List<TreeItem> children) {
        this.children = children;
    }

    @Override
    public String toString() {
        return "TreeItem id=" + id + ", text=" + text;
    }
}
