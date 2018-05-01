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
public class ControlsRecognition implements ControlsName{

    public List<ControlsTreeNode> CreateControlsTreeNodeList(List<SketchTreeNode> sketchTreeNodeList){
        if(sketchTreeNodeList==null||sketchTreeNodeList.size()<0)
            return null;

        List<ControlsTreeNode> controlsTreeNodeList = new ArrayList<ControlsTreeNode>();
        for(int i=0;i<sketchTreeNodeList.size();i++){
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
    public ControlsManyTreeNode DepthFirstSearch(ControlsManyTreeNode root){
        if(root==null)
            return null;

//        /*特殊情况，界面为空界面*/
//        if(JudgeIsLeafNode(root)){
//            root.getData().getControlsType().setLabel(View);
//            root.getData().getControlsType().setType(-1);
//            return root;
//        }

        Stack<ControlsManyTreeNode> stack = new Stack<ControlsManyTreeNode>();
        stack.push(root);
        while(!stack.isEmpty()){
            ControlsManyTreeNode treeNode = stack.pop();
            /*Todo:
            写判断函数
            */
            if(JudgeIsLeafNode(treeNode)){
                //true:改变，false：未改变
                boolean flag = ReplaceControlsTreeNode(root,treeNode);
                if(flag==true){
                    stack=new Stack<ControlsManyTreeNode>();
                    stack.push(root);
                }
            }
            else {
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


    public ControlsManyTreeNode FindParentNode(ControlsManyTreeNode treeNode){
        return treeNode;
    }
    public boolean ReplaceControlsTreeNode(ControlsManyTreeNode root,ControlsManyTreeNode treeNode){
        int type = treeNode.getData().getControlsType().getType();
        //发现叶子节点为 |（竖线）
        if(type==1){
            //找到父节点，符合要求修改父节点label为控件名

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
            //改对应节点的label为View
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


        return false;
    }

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
