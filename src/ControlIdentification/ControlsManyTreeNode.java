package ControlIdentification;

import java.util.List;

/**
 * Created by Cyan on 2018/4/12.
 */
public class ControlsManyTreeNode {
    private ControlsTypeTreeNode data;
    private List<ControlsTypeTreeNode> childList;

    public ControlsTypeTreeNode getData() {
        return data;
    }

    public void setData(ControlsTypeTreeNode data) {
        this.data = data;
    }

    public List<ControlsTypeTreeNode> getChildList() {
        return childList;
    }

    public void setChildList(List<ControlsTypeTreeNode> childList) {
        this.childList = childList;
    }
}
