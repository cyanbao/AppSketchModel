package SketchType;

import javax.swing.tree.TreeNode;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Cyan on 2018/3/29.
 */
public class SketchManyTreeNode {
    private SketchTreeNode data;
    private List<SketchManyTreeNode> childList;

    /**
     * 构造函数
     *
     * @param data 树节点
     */
    public SketchManyTreeNode(SketchTreeNode data){
        this.data = data;
        this.childList = new ArrayList<SketchManyTreeNode>();
    }

    /**
     * 构造函数
     *
     * @param data
     * @param childList 子树集合
     */
    public SketchManyTreeNode(SketchTreeNode data,List<SketchManyTreeNode>childList){
        this.data = data;
        this.childList = childList;
    }

    public SketchTreeNode getData() {
        return data;
    }

    public void setData(SketchTreeNode data) {
        this.data = data;
    }

    public List<SketchManyTreeNode> getChildList() {
        return childList;
    }

    public void setChildList(List<SketchManyTreeNode> childList) {
        this.childList = childList;
    }
}
