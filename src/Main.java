import ControlsLayout.ControlsRecognition;
import ControlsType.ControlsManyNodeTree;
import ControlsType.ControlsManyTreeNode;
import ControlsType.ControlsTreeNode;
import DataAnalysis.DataAnalysis;
import DataAnalysis.FileReadIn;
import SketchType.SketchType;
import SketchType.SketchTreeNode;
import SketchType.SketchManyNodeTree;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by Cyan on 2018/3/29.
 */
public class Main {
    public static void main(String []args){
        FileReadIn.ReadFileByLines("resources/test-navigationview.txt");
        Iterator<SketchType> it = FileReadIn.shapeList.iterator();
        int line = 0;
        while(it.hasNext()){
            SketchType p = it.next();
            System.out.println(line+":"+p.getPos_x()+","+p.getPos_y()+","+p.getHeight()+","+p.getWidth()+","+p.getLabel());
            line++;
        }
        /*转换结果*/
        List<SketchType> convertList = DataAnalysis.DataConvert(FileReadIn.shapeList);
        Iterator<SketchType> it_c = convertList.iterator();
        line = 0;
        while(it_c.hasNext()){
            SketchType p = it_c.next();
            System.out.println(line+":"+p.getPos_x()+","+p.getPos_y()+","+p.getHeight()+","+p.getWidth()+","+p.getLabel());
            line++;
        }

        /*建立元素树*/
        List<SketchTreeNode> sketchTreeNodeList = new ArrayList<>();
        Iterator<SketchType> it_t = convertList.iterator();
        //给每一个元素编号
        int id = 0;
        while(it_t.hasNext()){
            SketchType p = it_t.next();
            SketchTreeNode sketchTreeNode = new SketchTreeNode(id,p);
            sketchTreeNodeList.add(sketchTreeNode);
            id++;
        }
        //判断包含关系
        for(int i = 0;i<sketchTreeNodeList.size();i++){
            if(i==0){
                sketchTreeNodeList.get(i).setParentId(-1);
            }
            else{
                int num = i;
                boolean flag = false;
                while(flag==false) {
                    flag = DataAnalysis.JudgeRelation(sketchTreeNodeList.get(--num).getSketchType(), sketchTreeNodeList.get(i).getSketchType());
                    //System.out.println("flag:"+flag);
                }
                sketchTreeNodeList.get(i).setParentId(num);
            }
        }
      /*  for(int i=0;i<sketchTreeNodeList.size();i++){
            System.out.println("relate"+sketchTreeNodeList.get(i).getNodeId()+","+sketchTreeNodeList.get(i).getParentId());
        }*/

        /*创建多叉元素树*/
        SketchManyNodeTree sketchManyNodeTree = new SketchManyNodeTree();
        sketchManyNodeTree = sketchManyNodeTree.CreateTree(sketchTreeNodeList);
        //System.out.println("null sketchManyTreeNode");
        System.out.println("PreOrder Result："+sketchManyNodeTree.iteratorTree(sketchManyNodeTree.getRoot()));

//        ControlsRecognition controlsRecognition = new ControlsRecognition();
//        List<Integer> list = controlsRecognition.DepthFirstSearch(sketchManyNodeTree.getRoot());
//        System.out.println("DFS Result: ");
//        for(int i=0;i<list.size();i++){
//            System.out.print(list.get(i)+",");
//        }

        /*创建多叉控件树*/
        ControlsRecognition controlsRecognition = new ControlsRecognition();
        List<ControlsTreeNode> controlsTreeNodeList = new ArrayList<>();
        controlsTreeNodeList = controlsRecognition.CreateControlsTreeNodeList(sketchTreeNodeList);
        ControlsManyNodeTree controlsManyNodeTree = new ControlsManyNodeTree();
        controlsManyNodeTree = controlsManyNodeTree.CreateTree(controlsTreeNodeList);

        System.out.println("ControlsTree PreOrder Result:" +controlsManyNodeTree.iteratorTree(controlsManyNodeTree.getRoot()));

        ControlsManyTreeNode root = controlsRecognition.DepthFirstSearch(controlsManyNodeTree.getRoot(),controlsTreeNodeList);

        System.out.println("ControlsTree PreOrder Result: Finished!");





    }
}
