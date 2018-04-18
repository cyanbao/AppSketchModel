package ControlIdentification;

import java.util.List;

/**
 * Created by Cyan on 2018/4/12.
 */
public class ControlsManyNodeTree {
    private ControlsManyTreeNode root;

    public ControlsManyTreeNode getRoot() {
        return root;
    }

    public void setRoot(ControlsManyTreeNode root) {
        this.root = root;
    }


    /**
     * 构造函数
     */
    public ControlsManyNodeTree(ControlsTreeNode ControlsTreeNode){
        root = new ControlsManyTreeNode(ControlsTreeNode);
    }

    /**
     * 构造函数，设置节点id
     */
    public ControlsManyNodeTree(){
        root = new ControlsManyTreeNode(new ControlsTreeNode(0));
    }

    /**
     * 生成一颗多叉树(控件树)，根节点为root
     *
     * @param  controlsTreeNodes 生成多叉树的节点集合
     * @return controlsManyNodeTree
     */
    public ControlsManyNodeTree CreateTree(List<ControlsTreeNode> controlsTreeNodes) {
        if (controlsTreeNodes == null || controlsTreeNodes.size() < 0) {
            return null;
        }
        ControlsManyNodeTree controlsManyNodeTree = new ControlsManyNodeTree(controlsTreeNodes.get(0));
        //将所有的节点添加到多叉树中
        for(ControlsTreeNode controlsTreeNode:controlsTreeNodes) {
            if (controlsTreeNode.getParentId()==0) {
                //向跟节点添加一个节点
                controlsManyNodeTree.getRoot().getChildList().add(new ControlsManyTreeNode(controlsTreeNode));
               //System.out.println("child:"+sketchTreeNode.getNodeId()+"root:"+root.getData().getNodeId());
            } else {
                AddChild(controlsManyNodeTree.getRoot(), controlsTreeNode);
            }
        }

        return controlsManyNodeTree;
    }

    /**
     * 像指定多叉树节点添加子节点
     *
     * @param controlsManyTreeNode 多叉树节点
     * @param  child 节点
     * @return
     */
    public void AddChild(ControlsManyTreeNode controlsManyTreeNode,ControlsTreeNode child){
        for(ControlsManyTreeNode item:controlsManyTreeNode.getChildList()){
            if(item.getData().getNodeId()==(child.getParentId())){
                //找到对应的父亲
                item.getChildList().add(new ControlsManyTreeNode(child));
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
     * @param controlsManyTreeNode 多叉树节点
     * @return
     */
    public String iteratorTree(ControlsManyTreeNode controlsManyTreeNode) {
        StringBuilder buffer = new StringBuilder();
        buffer.append("\n");
        if(controlsManyTreeNode.getData().getNodeId()==0){
            buffer.append(String.valueOf(controlsManyTreeNode.getData().getNodeId())+","+"\n");
        }
        //buffer.append("0,\n");
        if(controlsManyTreeNode !=null){
            for(ControlsManyTreeNode index: controlsManyTreeNode.getChildList()){
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
}

