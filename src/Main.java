import ControlsLayout.ControlsRecognition;
import ControlsType.ControlsManyNodeTree;
import ControlsType.ControlsManyTreeNode;
import ControlsType.ControlsTreeNode;
import DataAnalysis.DataAnalysis;
import DataAnalysis.FileReadIn;
import DataAnalysis.FileWriteOut;
import SketchType.SketchType;
import SketchType.SketchTreeNode;
import SketchType.SketchManyNodeTree;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Scanner;

/**
 * Created by Cyan on 2018/3/29.
 */
public class Main {
    public static void main(String []args)throws IOException{
        Scanner input = new Scanner(System.in);
        System.out.print("Please input filename:");
        String inputString = input.next();
        String filename = "C:/Users/Cyan/Desktop/AppSketchModel/resources/ShowExample/"+inputString;
        FileReadIn.ReadFileByLines(filename);
        Iterator<SketchType> it = FileReadIn.shapeList.iterator();
        filename = filename.substring(0,filename.lastIndexOf("."));

        /*1920*1080屏幕分辨率转换转换结果*/
        List<SketchType> convertList = DataAnalysis.DataConvert(FileReadIn.shapeList);

        /*建立元素树*/
        //为每一个节点编号
        List<SketchTreeNode> sketchTreeNodeList =DataAnalysis.AddId(convertList);

        //判断包含关系
        sketchTreeNodeList = DataAnalysis.JudgeNodeRelation(sketchTreeNodeList);
//        for(int i = 0;i<sketchTreeNodeList.size();i++){
//            if(i==0){
//                sketchTreeNodeList.get(i).setParentId(-1);
//            }
//            else{
//                int num = i;
//                boolean flag = false;
//                while(flag==false) {
//                    flag = DataAnalysis.JudgeRelation(sketchTreeNodeList.get(--num).getSketchType(), sketchTreeNodeList.get(i).getSketchType());
//                    //System.out.println("flag:"+flag);版本，有待测试
//                }
//                sketchTreeNodeList.get(i).setParentId(num);
//            }
//        }

        /*创建多叉元素树*/
        SketchManyNodeTree sketchManyNodeTree = new SketchManyNodeTree();
        sketchManyNodeTree = sketchManyNodeTree.CreateTree(sketchTreeNodeList);

        System.out.println("PreOrder Result："+sketchManyNodeTree.iteratorTree(sketchManyNodeTree.getRoot()));

        /*创建多叉控件树*/
        ControlsRecognition controlsRecognition = new ControlsRecognition();
        List<ControlsTreeNode> controlsTreeNodeList = new ArrayList<>();
        controlsTreeNodeList = controlsRecognition.CreateControlsTreeNodeList(sketchTreeNodeList);
        ControlsManyNodeTree controlsManyNodeTree = new ControlsManyNodeTree();
        controlsManyNodeTree = controlsManyNodeTree.CreateTree(controlsTreeNodeList);

        /*先序输出*/
        System.out.println("ControlsTree PreOrder Result:" +controlsManyNodeTree.iteratorTree(controlsManyNodeTree.getRoot()));

        ControlsManyTreeNode root = controlsRecognition.DepthFirstSearch(controlsManyNodeTree.getRoot(),controlsTreeNodeList);

        //输出控件树
        FileWriteOut.FileWriteOutByJson(root,filename);

        //输出预测视图后的界面
        root = controlsRecognition.PredictView(root);
        FileWriteOut.FileWriteOutByJson(root,filename+"viewPredict");

        System.out.println("Output Successfully!");
    }
}
