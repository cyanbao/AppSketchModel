package ControlsLayout;

import ControlsType.*;
import SketchType.SketchManyTreeNode;
import SketchType.SketchTreeNode;

import javax.naming.ldap.Control;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;


/**
 * Created by Cyan on 2018/4/18.
 */
public class ControlsRecognition implements ControlsName {
    //创建控件原始树
    public List<ControlsTreeNode> CreateControlsTreeNodeList(List<SketchTreeNode> sketchTreeNodeList) {
        if (sketchTreeNodeList == null || sketchTreeNodeList.size() < 0)
            return null;

        List<ControlsTreeNode> controlsTreeNodeList = new ArrayList<ControlsTreeNode>();
        for (int i = 0; i < sketchTreeNodeList.size(); i++) {
            ControlsTreeNode controlsTreeNode = new ControlsTreeNode(
                    sketchTreeNodeList.get(i).getNodeId(),
                    sketchTreeNodeList.get(i).getParentId(),
                    new ControlsType(sketchTreeNodeList.get(i).getSketchType().getPos_x(),
                            sketchTreeNodeList.get(i).getSketchType().getPos_y(),
                            sketchTreeNodeList.get(i).getSketchType().getHeight(),
                            sketchTreeNodeList.get(i).getSketchType().getWidth(),
                            sketchTreeNodeList.get(i).getSketchType().getLabel())
            );
            controlsTreeNodeList.add(controlsTreeNode);
        }
        return controlsTreeNodeList;
    }


    /*
    * 将一棵初步的控件树，进行控件识别，输出
    * 第一步：深度遍历，根据元素建立第一棵控件树
    * 第二步:对控件树进行优化扫描，提取可能存在的View
    * 第三步：返回这一棵控件树
    * */
    public ControlsManyTreeNode DepthFirstSearch(ControlsManyTreeNode root,List<ControlsTreeNode> controlsList) {
        if (root == null)
            return null;

//        /*特殊情况，界面为空界面*/
//        if(JudgeIsLeafNode(root)){
//            root.getData().getControlsType().setLabel(View);
//            root.getData().getControlsType().setType(-1);
//            return root;
//        }

        Stack<ControlsManyTreeNode> stack = new Stack<ControlsManyTreeNode>();
        stack.push(root);
        JudgeRootChange judgeRootChange;

        while (!stack.isEmpty()) {
            ControlsManyTreeNode treeNode = stack.pop();

            if (JudgeIsLeafNode(treeNode)) {
                //true:改变，false：未改变
                judgeRootChange = JudgeControls(root, treeNode,controlsList);
                root = judgeRootChange.getRoot();
                if (judgeRootChange.isFlag() == true) {
                    //深度遍历，重新找到controlsList
                    //controlsList = DepthFirstSearch(root);
                    stack = new Stack<ControlsManyTreeNode>();
                    stack.push(root);
                }
            }
            else {
                if(treeNode.getData().getControlsType().getType()==6){
                    JudgeControls(root,treeNode,controlsList);
                }
                //将root的childList从右往左压
                int size = treeNode.getChildList().size();
                //将childList倒序排列
                for (int i = size - 1; i >= 0; i--) {
                    stack.push(treeNode.getChildList().get(i));
                }
            }


        }
        return root;
    }



    //DFS，返回list
    public List<ControlsTreeNode> DepthFirstSearch(ControlsManyTreeNode root){
        List<ControlsTreeNode> controlsList = new ArrayList<ControlsTreeNode>();
        Stack<ControlsManyTreeNode> stack = new Stack<ControlsManyTreeNode>();
        stack.push(root);
        while (!stack.isEmpty()) {
            ControlsManyTreeNode currentNode = stack.pop();
            controlsList.add(currentNode.getData());
            int size = currentNode.getChildList().size();
            //将childList倒序排列
            for (int i = size - 1; i >= 0; i--) {
                stack.push(currentNode.getChildList().get(i));
            }
        }
        return controlsList;
    }

    //在控件链表里寻找父节点
    public ControlsManyTreeNode FindParentNode(ControlsManyTreeNode root,ControlsManyTreeNode treeNode) {
        int parentId = treeNode.getData().getParentId();
        Stack<ControlsManyTreeNode> stack = new Stack<ControlsManyTreeNode>();
        stack.push(root);
        while (!stack.isEmpty()) {
            ControlsManyTreeNode currentNode = stack.pop();
            if(currentNode.getData().getNodeId()==parentId)
                return currentNode;
            int size = currentNode.getChildList().size();
            //将childList倒序排列
            for (int i = size - 1; i >= 0; i--) {
                stack.push(currentNode.getChildList().get(i));
            }
        }
        return null;
    }




    //判断控件方法
    public JudgeRootChange JudgeControls(ControlsManyTreeNode root,ControlsManyTreeNode treeNode,List<ControlsTreeNode> controlsList){
        boolean flag = false;
        int type = treeNode.getData().getControlsType().getType();
        ControlsManyTreeNode parentNode = FindParentNode(root,treeNode);

        //发现叶子节点为 |（竖线）
        if(type==1){
            List<ControlsManyTreeNode> childList = parentNode.getChildList();
            //TabHost，父节点的类型为方框，childList长度为3,均为竖线，修改父节点，并把子节点删去
            if(parentNode.getData().getControlsType().getLabel().equals(View)&&childList.size()==2){
                if(childList.get(0).getData().getControlsType().getType()==1
                        &&childList.get(1).getData().getControlsType().getType()==1){
                    flag=true;

                    //改变父节点
                    for(int i=0;i<controlsList.size();i++){
                        if(controlsList.get(i).getNodeId()==parentNode.getData().getNodeId()){
                            controlsList.get(i).getControlsType().setControlsTypeAndLabel(-1,TabHost);
                            break;
                        }
                    }
                    //删去子节点
                    for(int i=0;i<controlsList.size();i++){
                        if(controlsList.get(i).getParentId()==parentNode.getData().getNodeId()){
                            controlsList.remove(i);
                            i=i-1;
                        }
                    }
                }
            }
            //Navigation,父节点为View，子节点大小为4，分别为| — — —
            else if(parentNode.getData().getControlsType().getLabel().equals(View)&&childList.size()==4){
                if(childList.get(0).getData().getControlsType().getType()==1
                        &&childList.get(1).getData().getControlsType().getType()==13
                        &&childList.get(2).getData().getControlsType().getType()==13
                        &&childList.get(3).getData().getControlsType().getType()==13){
                    flag=true;

                    //改变父节点
                    for(int i=0;i<controlsList.size();i++){
                        if(controlsList.get(i).getNodeId()==parentNode.getData().getNodeId()){
                            controlsList.get(i).getControlsType().setControlsTypeAndLabel(-1,NavigationView);
                            break;
                        }
                    }
                    //删去子节点
                    for(int i=0;i<controlsList.size();i++){
                        if(controlsList.get(i).getParentId()==parentNode.getData().getNodeId()){
                            controlsList.remove(i);
                            i=i-1;
                        }
                    }
                }
            }
        }
        else if(type==2){

        }
        else if(type==3){

        }
        else if(type==4){

        }
        else if(type==5){//为#
            //找父节点有无额外的子节点
        }
        else if(type==6) {//为方框
            //改对应节点的type为view
            for(int i=0;i<controlsList.size();i++){
                if(controlsList.get(i).getNodeId()==treeNode.getData().getNodeId()){
                    controlsList.get(i).getControlsType().setControlsTypeAndLabel(-1,View);
                    break;
                }
            }
        }
        else if(type==7){//为下箭头
            //找父节点有无额外子节点
        }
        else if(type==8){

        }
        else if(type==9){

        }
        else if(type==10){

        }
        else if(type==11){

        }
        else if(type==12){

        }
        else if(type==13){

        }
        else if(type==-1){
            //已经识别为已经控件

        }
        else{
            System.out.println("No such sketch type:"+type);
        }

        ControlsManyNodeTree tree = new ControlsManyNodeTree();
        tree = tree.CreateTree(controlsList);
        root = tree.getRoot();
        return new JudgeRootChange(root,flag);
    }


    //判断是否为叶子节点
    public boolean JudgeIsLeafNode(ControlsManyTreeNode node){
        if(node.getChildList().size()==0||node.getChildList()==null)
            return true;
        return false;
    }



//    public static void main(String args[]){
//        ControlsManyTreeNode node = new ControlsManyTreeNode(new ControlsTreeNode(2));
//        System.out.println(node.getData().getNodeId());
//        ControlsRecognition controlsRecognition = new ControlsRecognition();
//        controlsRecognition.ChangeNode(node);
//        System.out.println(node.getData().getNodeId());
//    }
//
//    public void ChangeNode(ControlsManyTreeNode node){
//        node .getData().setNodeId(1);
//    }
}
