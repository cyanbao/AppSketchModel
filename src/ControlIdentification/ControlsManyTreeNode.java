package ControlIdentification;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Cyan on 2018/4/12.
 */
public class ControlsManyTreeNode {
    private ControlsTreeNode data;
    private List<ControlsManyTreeNode> childList;

    /**
     * 构造函数
     *
     * @param data 树节点
     */
    public ControlsManyTreeNode(ControlsTreeNode data){
        this.data = data;
        this.childList = new ArrayList< ControlsManyTreeNode>();
    }

    /**
     * 构造函数
     *
     * @param data
     * @param childList 子树集合
     */
    public ControlsManyTreeNode(ControlsTreeNode data,List<ControlsManyTreeNode>childList){
        this.data = data;
        this.childList = childList;
    }
    public ControlsTreeNode getData() {
        return data;
    }

    public void setData(ControlsTreeNode data) {
        this.data = data;
    }

    public List<ControlsManyTreeNode> getChildList() {
        return childList;
    }

    public void setChildList(List<ControlsManyTreeNode> childList) {
        this.childList = childList;
    }
}
