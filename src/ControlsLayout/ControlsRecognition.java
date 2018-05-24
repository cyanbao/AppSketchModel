package ControlsLayout;

import ControlsType.*;
import SketchType.SketchManyTreeNode;
import SketchType.SketchTreeNode;
import javafx.geometry.Pos;

import javax.naming.ldap.Control;
import javax.swing.text.Position;
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
            List<ControlsManyTreeNode> childList = parentNode.getChildList();
            //VideoView
            if(parentNode.getData().getControlsType().getLabel().equals(View)&&childList.size()==2) {
                if (childList.get(0).getData().getControlsType().getType() == 2
                        && childList.get(1).getData().getControlsType().getType() == 3) {
                    flag = true;

                    //改变父节点
                    for (int i = 0; i < controlsList.size(); i++) {
                        if (controlsList.get(i).getNodeId() == parentNode.getData().getNodeId()) {
                            controlsList.get(i).getControlsType().setControlsTypeAndLabel(-1, VideoView);
                            break;
                        }
                    }
                    //删去子节点
                    for (int i = 0; i < controlsList.size(); i++) {
                        if (controlsList.get(i).getParentId() == parentNode.getData().getNodeId()) {
                            controlsList.remove(i);
                            i = i - 1;
                        }
                    }
                }
                //SeekBar
                else if(childList.get(0).getData().getControlsType().getType() == 2
                        && childList.get(1).getData().getControlsType().getType() == 7) {
                    flag = true;
                    //改变父节点
                    for (int i = 0; i < controlsList.size(); i++) {
                        if (controlsList.get(i).getNodeId() == parentNode.getData().getNodeId()) {
                            controlsList.get(i).getControlsType().setControlsTypeAndLabel(-1, SeekBar);
                            break;
                        }
                    }
                    //删去子节点
                    for (int i = 0; i < controlsList.size(); i++) {
                        if (controlsList.get(i).getParentId() == parentNode.getData().getNodeId()) {
                            controlsList.remove(i);
                            i = i - 1;
                        }
                    }

                }
            }
            else if(parentNode.getData().getControlsType().getLabel().equals(View)&&childList.size()==3) {
                //Switch
                if (childList.get(0).getData().getControlsType().getType() == 2
                        && childList.get(1).getData().getControlsType().getType() == 7
                        && childList.get(2).getData().getControlsType().getType() == 4) {
                    flag = true;

                    //改变父节点
                    for (int i = 0; i < controlsList.size(); i++) {
                        if (controlsList.get(i).getNodeId() == parentNode.getData().getNodeId()) {
                            controlsList.get(i).getControlsType().setControlsTypeAndLabel(-1, Switch);
                            break;
                        }
                    }
                    //删去子节点
                    for (int i = 0; i < controlsList.size(); i++) {
                        if (controlsList.get(i).getParentId() == parentNode.getData().getNodeId()) {
                            controlsList.remove(i);
                            i = i - 1;
                        }
                    }
                }
            }
        }
        else if(type==3){
            List<ControlsManyTreeNode> childList = parentNode.getChildList();
            //ImageView
            if(parentNode.getData().getControlsType().getLabel().equals(View)&&childList.size()==1){
                if(childList.get(0).getData().getControlsType().getType()==3){
                    flag=true;

                    //改变父节点
                    for(int i=0;i<controlsList.size();i++){
                        if(controlsList.get(i).getNodeId()==parentNode.getData().getNodeId()){
                            controlsList.get(i).getControlsType().setControlsTypeAndLabel(-1,ImageView);
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
            //AlertDialog
            else if(parentNode.getData().getControlsType().getLabel().equals(View)&&childList.size()==2){
                if(childList.get(0).getData().getControlsType().getType()==3
                        &&childList.get(1).getData().getControlsType().getType()==4){
                    flag=true;

                    //改变父节点
                    for(int i=0;i<controlsList.size();i++){
                        if(controlsList.get(i).getNodeId()==parentNode.getData().getNodeId()){
                            controlsList.get(i).getControlsType().setControlsTypeAndLabel(-1,AlertDialog);
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
            //Menu
            else if(parentNode.getData().getControlsType().getLabel().equals(View)&&childList.size()==3){
                if(childList.get(0).getData().getControlsType().getType()==3
                        &&childList.get(1).getData().getControlsType().getType()==3
                        &&childList.get(2).getData().getControlsType().getType()==3){
                    flag=true;

                    //改变父节点
                    for(int i=0;i<controlsList.size();i++){
                        if(controlsList.get(i).getNodeId()==parentNode.getData().getNodeId()){
                            controlsList.get(i).getControlsType().setControlsTypeAndLabel(-1,Menu);
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
        else if(type==4){
            List<ControlsManyTreeNode> childList = parentNode.getChildList();
            //TextView
            if(parentNode.getData().getControlsType().getLabel().equals(View)&&childList.size()==3){
                if(childList.get(0).getData().getControlsType().getType()==4
                        &&childList.get(1).getData().getControlsType().getType()==4
                        &&childList.get(2).getData().getControlsType().getType()==4){
                    flag=true;

                    //改变父节点
                    for(int i=0;i<controlsList.size();i++){
                        if(controlsList.get(i).getNodeId()==parentNode.getData().getNodeId()){
                            controlsList.get(i).getControlsType().setControlsTypeAndLabel(-1,TextView);
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
                //EditTextView
                else if(childList.get(0).getData().getControlsType().getType()==4
                        &&childList.get(1).getData().getControlsType().getType()==4
                        &&childList.get(2).getData().getControlsType().getType()==1){
                    flag=true;

                    //改变父节点
                    for(int i=0;i<controlsList.size();i++){
                        if(controlsList.get(i).getNodeId()==parentNode.getData().getNodeId()){
                            controlsList.get(i).getControlsType().setControlsTypeAndLabel(-1,EditTextView);
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
            //TextUrl
            else if(parentNode.getData().getControlsType().getLabel().equals(View)&&childList.size()==5){
                if(childList.get(0).getData().getControlsType().getType()==4
                        &&childList.get(1).getData().getControlsType().getType()==4
                        &&childList.get(2).getData().getControlsType().getType()==4
                        &&childList.get(3).getData().getControlsType().getType()==7
                        &&childList.get(4).getData().getControlsType().getType()==2){
                    flag=true;

                    //改变父节点
                    for(int i=0;i<controlsList.size();i++){
                        if(controlsList.get(i).getNodeId()==parentNode.getData().getNodeId()){
                            controlsList.get(i).getControlsType().setControlsTypeAndLabel(-1,TextUrl);
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
        else if(type==5){//为#
            List<ControlsManyTreeNode> childList = parentNode.getChildList();
            //Button
            if(parentNode.getData().getControlsType().getLabel().equals(View)&&childList.size()==1) {
                if (childList.get(0).getData().getControlsType().getType() == 5) {
                    flag = true;

                    //改变父节点
                    for (int i = 0; i < controlsList.size(); i++) {
                        if (controlsList.get(i).getNodeId() == parentNode.getData().getNodeId()) {
                            controlsList.get(i).getControlsType().setControlsTypeAndLabel(-1, Button);
                            break;
                        }
                    }
                    //删去子节点
                    for (int i = 0; i < controlsList.size(); i++) {
                        if (controlsList.get(i).getParentId() == parentNode.getData().getNodeId()) {
                            controlsList.remove(i);
                            i = i - 1;
                        }
                    }
                }
            }
            else if(parentNode.getData().getControlsType().getLabel().equals(View)&&childList.size()==3) {
                //TabBar
                if (childList.get(0).getData().getControlsType().getType() == 5
                        &&childList.get(1).getData().getControlsType().getType() == 5
                        &&childList.get(2).getData().getControlsType().getType() == 5) {
                    flag = true;

                    //改变父节点
                    for (int i = 0; i < controlsList.size(); i++) {
                        if (controlsList.get(i).getNodeId() == parentNode.getData().getNodeId()) {
                            controlsList.get(i).getControlsType().setControlsTypeAndLabel(-1, TabBar);
                            break;
                        }
                    }
                    //删去子节点
                    for (int i = 0; i < controlsList.size(); i++) {
                        if (controlsList.get(i).getParentId() == parentNode.getData().getNodeId()) {
                            controlsList.remove(i);
                            i = i - 1;
                        }
                    }
                }
                //CompoundButton
                else if (childList.get(0).getData().getControlsType().getType() == 5
                        &&childList.get(1).getData().getControlsType().getType() == 2
                        &&childList.get(2).getData().getControlsType().getType() == 2) {
                    flag = true;

                    //改变父节点A
                    for (int i = 0; i < controlsList.size(); i++) {
                        if (controlsList.get(i).getNodeId() == parentNode.getData().getNodeId()) {
                            controlsList.get(i).getControlsType().setControlsTypeAndLabel(-1, CompoundButton);
                            break;
                        }
                    }
                    //删去子节点
                    for (int i = 0; i < controlsList.size(); i++) {
                        if (controlsList.get(i).getParentId() == parentNode.getData().getNodeId()) {
                            controlsList.remove(i);
                            i = i - 1;
                        }
                    }
                }
                //CheckBox
                else if (childList.get(0).getData().getControlsType().getType() == 5
                        &&childList.get(1).getData().getControlsType().getType() == 5
                        &&childList.get(2).getData().getControlsType().getType() == 2) {
                    flag = true;

                    //改变父节点
                    for (int i = 0; i < controlsList.size(); i++) {
                        if (controlsList.get(i).getNodeId() == parentNode.getData().getNodeId()) {
                            controlsList.get(i).getControlsType().setControlsTypeAndLabel(-1, CheckBox);
                            break;
                        }
                    }
                    //删去子节点
                    for (int i = 0; i < controlsList.size(); i++) {
                        if (controlsList.get(i).getParentId() == parentNode.getData().getNodeId()) {
                            controlsList.remove(i);
                            i = i - 1;
                        }
                    }
                }
            }
        }
        else if(type==6) {//为方框
            //改对应节点的type为view
            for(int i=0;i<controlsList.size();i++){
                if(controlsList.get(i).getNodeId()==treeNode.getData().getNodeId()){
                    controlsList.get(i).getControlsType().setControlsTypeAndLabel(-1,View);
                    break;
                }
            }
            //不为root
            if(treeNode.getData().getParentId()!=-1){
                List<ControlsManyTreeNode> childList = parentNode.getChildList();
                boolean isLeafFlag = false;
                for(int i=0;i<childList.size();i++){
                    isLeafFlag = JudgeIsLeafNode(childList.get(i));
                    if(isLeafFlag == false)break;
                }
                if(childList.size()==4&&isLeafFlag==true){
                    if(childList.get(1).getData().getControlsType().getType()==6
                            &&childList.get(2).getData().getControlsType().getType()==6
                            &&childList.get(3).getData().getControlsType().getType()==6){
                        flag = true;

                        //改变父节点
                        for (int i = 0; i < controlsList.size(); i++) {
                            if (controlsList.get(i).getNodeId() == parentNode.getData().getNodeId()) {
                                controlsList.get(i).getControlsType().setControlsTypeAndLabel(-1, Grid);
                                break;
                            }
                        }
                        //删去子节点
                        for (int i = 0; i < controlsList.size(); i++) {
                            if (controlsList.get(i).getParentId() == parentNode.getData().getNodeId()) {
                                controlsList.remove(i);
                                i = i - 1;
                            }
                        }
                    }
                }
                else if(childList.size()==2&&isLeafFlag==true){
                    if(childList.get(1).getData().getControlsType().getType()==6){
                        flag = true;

                        //改变父节点
                        for (int i = 0; i < controlsList.size(); i++) {
                            if (controlsList.get(i).getNodeId() == parentNode.getData().getNodeId()) {
                                controlsList.get(i).getControlsType().setControlsTypeAndLabel(-1, Card);
                                break;
                            }
                        }
                        //删去子节点
                        for (int i = 0; i < controlsList.size(); i++) {
                            if (controlsList.get(i).getParentId() == parentNode.getData().getNodeId()) {
                                controlsList.remove(i);
                                i = i - 1;
                            }
                        }
                    }
                }
            }

        }
        else if(type==14){//为下箭头
            //Overflow
            List<ControlsManyTreeNode> childList = parentNode.getChildList();
            if(parentNode.getData().getControlsType().getLabel().equals(View)&&childList.size()==1) {
                if (childList.get(0).getData().getControlsType().getType() == 14) {
                    flag = true;

                    //改变父节点
                    for (int i = 0; i < controlsList.size(); i++) {
                        if (controlsList.get(i).getNodeId() == parentNode.getData().getNodeId()) {
                            controlsList.get(i).getControlsType().setControlsTypeAndLabel(-1, Overflow);
                            break;
                        }
                    }
                    //删去子节点
                    for (int i = 0; i < controlsList.size(); i++) {
                        if (controlsList.get(i).getParentId() == parentNode.getData().getNodeId()) {
                            controlsList.remove(i);
                            i = i - 1;
                        }
                    }
                }
            }
            //List
            else if(parentNode.getData().getControlsType().getLabel().equals(View)&&childList.size()==2) {
                if (childList.get(0).getData().getControlsType().getLabel().equals(View)
                        &&childList.get(1).getData().getControlsType().getType()==14) {
                    flag = true;

                    //改变父节点
                    for (int i = 0; i < controlsList.size(); i++) {
                        if (controlsList.get(i).getNodeId() == parentNode.getData().getNodeId()) {
                            controlsList.get(i).getControlsType().setControlsTypeAndLabel(-1, ListView);
                            break;
                        }
                    }

                    //list内层结构,删除子节点
                    if(JudgeIsLeafNode(childList.get(0))==true){
                        for (int i = 0; i < controlsList.size(); i++) {
                            if (controlsList.get(i).getParentId() == parentNode.getData().getNodeId()) {
                                controlsList.remove(i);
                                i = i - 1;
                            }
                        }
                    }
                    else{//list内含内层结构，删掉第二个子节点
                            for (int i = 0; i < controlsList.size(); i++) {
                                if (controlsList.get(i).getNodeId() == childList.get(1).getData().getNodeId()) {
                                    controlsList.remove(i);
                                    i = i - 1;
                                }
                            }
                    }
                }
            }
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
            //FloatActionButton
            List<ControlsManyTreeNode> childList = parentNode.getChildList();
            if(parentNode.getData().getControlsType().getLabel().equals(View)&&childList.size()==1) {
                if (childList.get(0).getData().getControlsType().getType() == 12) {
                    flag = true;

                    //改变父节点
                    for (int i = 0; i < controlsList.size(); i++) {
                        if (controlsList.get(i).getNodeId() == parentNode.getData().getNodeId()) {
                            controlsList.get(i).getControlsType().setControlsTypeAndLabel(-1, FloatActionButton);
                            break;
                        }
                    }
                    //删去子节点
                    for (int i = 0; i < controlsList.size(); i++) {
                        if (controlsList.get(i).getParentId() == parentNode.getData().getNodeId()) {
                            controlsList.remove(i);
                            i = i - 1;
                        }
                    }
                }
            }

        }
        else if(type==13){

        }
        else if(type==-1){
            //已经识别为已经控件
            List<ControlsManyTreeNode> childList = parentNode.getChildList();
            if(parentNode.getData().getControlsType().getLabel().equals(View)&&childList.size()==5) {
                if (childList.get(0).getData().getControlsType().getType()==-1
                        &&childList.get(1).getData().getControlsType().getType()==7
                        &&childList.get(2).getData().getControlsType().getType()==-1
                        &&childList.get(3).getData().getControlsType().getType()==7
                        &&childList.get(4).getData().getControlsType().getType()==-1) {
                    //ContentDisplay,内容轮显示/
                    if(childList.get(0).getData().getControlsType().getLabel().equals(ImageView)
                            &&childList.get(2).getData().getControlsType().getLabel().equals(ImageView)
                            &&childList.get(4).getData().getControlsType().getLabel().equals(ImageView)) {
                        flag = true;
                        for (int i = 0; i < controlsList.size(); i++) {
                            if (controlsList.get(i).getNodeId() == parentNode.getData().getNodeId()) {
                                controlsList.get(i).getControlsType().setControlsTypeAndLabel(-1, ContentDisplay);
                                break;
                            }
                        }
                        //删去子节点
                        for (int i = 0; i < controlsList.size(); i++) {
                            if (controlsList.get(i).getParentId() == parentNode.getData().getNodeId()) {
                                controlsList.remove(i);
                                i = i - 1;
                            }
                        }
                    }
                    else if(childList.get(0).getData().getControlsType().getLabel().equals(Button)
                            &&childList.get(2).getData().getControlsType().getLabel().equals(Button)
                            &&childList.get(4).getData().getControlsType().getLabel().equals(Button)) {
                        flag = true;
                        for (int i = 0; i < controlsList.size(); i++) {
                            if (controlsList.get(i).getNodeId() == parentNode.getData().getNodeId()) {
                                controlsList.get(i).getControlsType().setControlsTypeAndLabel(-1, Lane);
                                break;
                            }
                        }
                        //删去子节点
                        for (int i = 0; i < controlsList.size(); i++) {
                            if (controlsList.get(i).getParentId() == parentNode.getData().getNodeId()) {
                                controlsList.remove(i);
                                i = i - 1;
                            }
                        }
                    }
                }
            }
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


    public ControlsManyTreeNode PredictView(ControlsManyTreeNode root){
        List<ControlsTreeNode> controlsList = DepthFirstSearch(root);
        if (root == null)
            return null;
        Stack<ControlsManyTreeNode> stack = new Stack<ControlsManyTreeNode>();
        stack.push(root);

        JudgeRootChange judgeRootChange;

        while (!stack.isEmpty()) {
            ControlsManyTreeNode treeNode = stack.pop();
            judgeRootChange = PredictView(root, treeNode, controlsList);
            root = judgeRootChange.getRoot();
            if (judgeRootChange.isFlag() == true) {
                stack = new Stack<ControlsManyTreeNode>();
                stack.push(root);
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

    //预测可能存在的视图
    public JudgeRootChange PredictView(ControlsManyTreeNode root,ControlsManyTreeNode treeNode,List<ControlsTreeNode> controlsList){
        boolean flag = false;
        List<ControlsManyTreeNode> childList = treeNode.getChildList();
        int count = 0;
        //判断如果一个View中所有控件在同一水平线，若在则不需要重新生成View
        for(int i=0;i<childList.size()-1;i++){
            boolean isHorizontal = true;
            ControlsType control1 = childList.get(i).getData().getControlsType();
            ControlsType control2 = childList.get(i+1).getData().getControlsType();
            int h_center1 = control1.getY()+control1.getHeight()/2;
            int h_center2 = control2.getY()+control2.getHeight()/2;
            if(h_center1-h_center2>(-24)&&h_center1-h_center2<24){
                count++;
            }
            else
                isHorizontal = false;
            if(isHorizontal==false)break;
        }
        if(count==childList.size()-1)
            return new JudgeRootChange(root,flag);

        for(int i=0;i<childList.size()-1;i++){
            ControlsManyTreeNode controls1 = childList.get(i);
            ControlsManyTreeNode controls2 = childList.get(i+1);
            ControlsType control1 = controls1.getData().getControlsType();
            ControlsType control2 = controls2.getData().getControlsType();
            int h_center1 = control1.getY()+control1.getHeight()/2;
            int h_center2 = control2.getY()+control2.getHeight()/2;
            if(h_center1-h_center2>(-24)&&h_center1-h_center2<24){
                //两个控件位置水平相似建立一个预测的视图
                //建立一个新视图并且判断有无其余控件覆盖
                //找出最大nodeId
                int maxId = 0;
                for(int j=0;j<controlsList.size();j++){
                    if(controlsList.get(j).getNodeId()>maxId)
                        maxId = controlsList.get(j).getNodeId();
                }
                ControlsTreeNode newView = new ControlsTreeNode(maxId+1);

                int x=0,y=0,height=0,width=0,type=0;
                if(control1.getX()<control2.getX())
                    x = control1.getX();
                else
                    x = control2.getX();
                if(control1.getY()<control2.getY())
                    y = control1.getY();
                else
                    y = control2.getY();
                if((control1.getY()+control1.getHeight())<(control2.getY()+control2.getHeight()))
                    height = control2.getY()+ control2.getHeight()-y;
                else
                    height = control1.getY()+control1.getHeight()-y;
                if(control1.getX()+control1.getWidth()<control2.getX()+control2.getWidth())
                    width = control2.getX()+control2.getWidth() - x;
                else
                    width = control1.getX()+control1.getWidth() - x;

                ControlsType controlsType = new ControlsType(x,y,height,width,View,type);
                newView.setControlsType(controlsType);

                Boolean isIn = false;
                for(int k=0;k<childList.size();k++){
                    if(k!=i&&k!=i+1){
                        ControlsType control3 = childList.get(k).getData().getControlsType();
                        ControlsType control4 = newView.getControlsType();
                        if(control3.getX()+control3.getWidth()>control4.getX()
                                &&control4.getX()+control4.getWidth()>control3.getX()
                                &&control3.getY()+control3.getHeight()>control4.getY()
                                &&control4.getY()+control4.getHeight()>control3.getY()) {
                            isIn = true;
                            break;
                        }
                        else
                            isIn = false;
                    }
                }
                //如果不重叠
                if(isIn==false){
                    //判断这个视图是否为新建视图
                    //如果是，把第二个控件放入该视图底下,并且扩充视图大小
                    if(treeNode.getData().getControlsType().getType()==0){
                        for(int p = 0;p<controlsList.size();p++){
                            if(controlsList.get(p).getNodeId()==childList.get(i+1).getData().getNodeId()) {
                                controlsList.get(p).setParentId(treeNode.getData().getNodeId());
                                newView.setParentId(treeNode.getData().getNodeId());
                                controlsList.get(i).setControlsType(newView.getControlsType());
                            }
                        }
                    }
                    //如果不是，新建一个视图层
                    else {
                        newView.setParentId(treeNode.getData().getNodeId());
                        int pos = 0;
                        for (int p = 0; p < controlsList.size(); p++) {
                            if (controlsList.get(p).getNodeId() == childList.get(i).getData().getNodeId()
                                     ||controlsList.get(p).getNodeId() == childList.get(i + 1).getData().getNodeId()) {
                                controlsList.get(p).setParentId(newView.getNodeId());
                                pos = p;
                            }

                        }
                        controlsList.add(pos-1, newView);
                    }
                    flag =true;
                    ControlsManyNodeTree tree = new ControlsManyNodeTree();
                    tree = tree.CreateTree(controlsList);
                    root = tree.getRoot();
                }
            }
        }
        return new JudgeRootChange(root,flag);
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
