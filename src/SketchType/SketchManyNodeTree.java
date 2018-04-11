package SketchType;

import java.util.List;

/**
 * Created by Cyan on 2018/3/30.
 */
public class SketchManyNodeTree {
    private SketchManyTreeNode root;

    /**
      * 构造函数
      */
    public SketchManyNodeTree(SketchTreeNode sketchTreeNode){
        root = new SketchManyTreeNode(sketchTreeNode);
    }

    /**
     * 构造函数
     */
    public SketchManyNodeTree(){
        root = new SketchManyTreeNode(new SketchTreeNode(0));
    }

    /**
     * 生成一颗多叉树，根节点为root
     *
     * @param  sketchTreeNodes 生成多叉树的节点集合
     * @return SketchManyNodeTree
     */
    public SketchManyNodeTree CreateTree(List<SketchTreeNode> sketchTreeNodes){
        if(sketchTreeNodes == null|| sketchTreeNodes.size()<0)
            return null;
        SketchManyNodeTree sketchManyNodeTree = new SketchManyNodeTree(sketchTreeNodes.get(0));
        //将所有节点添加到多叉树种
        for(SketchTreeNode sketchTreeNode:sketchTreeNodes) {
            if (sketchTreeNode.getParentId()==0) {
                //向跟节点添加一个节点
                sketchManyNodeTree.getRoot().getChildList().add(new SketchManyTreeNode(sketchTreeNode));
                //System.out.println("child:"+sketchTreeNode.getNodeId()+"root:"+root.getData().getNodeId());

            } else {
                AddChild(sketchManyNodeTree.getRoot(), sketchTreeNode);
            }
        }

        return sketchManyNodeTree;

    }

    /**
     * 像指定多叉树节点添加子节点
     *
     * @param sketchManyTreeNode 多叉树节点
     * @param  child 节点
     * @return
     */
    public void AddChild(SketchManyTreeNode sketchManyTreeNode,SketchTreeNode child){
        for(SketchManyTreeNode item:sketchManyTreeNode.getChildList()){
            if(item.getData().getNodeId()==(child.getParentId())){
                //找到对应的父亲
                item.getChildList().add(new SketchManyTreeNode(child));
                break;
            }
            else{
                if(item.getChildList()!=null&&item.getChildList().size()>0){
                    AddChild(item,child);
                }

            }
        }
    }

    /**
     * 遍历多叉树
     *
     * @param sketchManyTreeNode 多叉树节点
     * @return
     */
    public String iteratorTree(SketchManyTreeNode sketchManyTreeNode) {
        StringBuilder buffer = new StringBuilder();
        buffer.append("\n");
        if(sketchManyTreeNode.getData().getNodeId()==0){
            buffer.append(String.valueOf(sketchManyTreeNode.getData().getNodeId())+","+"\n");
        }
        //buffer.append("0,\n");
        if(sketchManyTreeNode !=null){
            for(SketchManyTreeNode index: sketchManyTreeNode.getChildList()){
               // System.out.println("order:"+index.getData().getNodeId());
                buffer.append(String.valueOf(index.getData().getNodeId())+",");
                if(index.getChildList()!=null&&index.getChildList().size()>0){
                    buffer.append(iteratorTree(index));
                }
            }
        }
        buffer.append("\n");
     //   System.out.println("here sketchManyTreeNode"+buffer);
        return buffer.toString();
    }

    public SketchManyTreeNode getRoot() {
        return root;
    }

    public void setRoot(SketchManyTreeNode root) {
        this.root = root;
    }
}
