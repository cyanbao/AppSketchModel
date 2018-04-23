package ControlsLayout;

import SketchType.SketchManyTreeNode;

import java.util.ArrayList;
import java.util.Stack;


/**
 * Created by Cyan on 2018/4/18.
 */
public class ControlsRecognition {
    /*
    * 将元素树深度遍历，返回形成的控件树
    * */
    public ArrayList<Integer> DepthFirstSearch(SketchManyTreeNode root){
        ArrayList<Integer> list =  new ArrayList<>();
        if(root==null)
            return list;
        Stack<SketchManyTreeNode> stack = new Stack<SketchManyTreeNode>();
        stack.push(root);
        while(!stack.isEmpty()){
            SketchManyTreeNode tree = stack.pop();
            //将root的childList从右往左压
            int length = tree.getChildList().size();
            for(int i=length-1;i>=0;i--){
                stack.push(tree.getChildList().get(i));
            }
            list.add(tree.getData().getNodeId());
        }
        return list;
    }

}
