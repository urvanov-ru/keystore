package ru.urvanov.keystore.controller.domain;

public class TreeResponse extends SimpleResponse {

    /**
     * 
     */
    private static final long serialVersionUID = 4982033837752189727L;

    private TreeItem children = new TreeItem();

    private Long totalRecords;

    public TreeItem getChildren() {
        return children;
    }

    public void setChildren(TreeItem children) {
        this.children = children;
    }

    public Long getTotalRecords() {
        return totalRecords;
    }

    public void setTotalRecords(Long totalRecords) {
        this.totalRecords = totalRecords;
    }

}
