package DataAnalysis;

import SketchType.SketchType;
import SketchType.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by Cyan on 2018/3/22.
 *Description: 数据标准化：将数据转化为1920*1080 高宽比情况下的数据
 */


public class DataAnalysis implements StandardizedData{
    public static List<SketchType> DataConvert(List<SketchType> shapeList){
        List <SketchType> convertList = new ArrayList<SketchType>();
        Iterator<SketchType> it = shapeList.iterator();
        double rate_x = 0.0;
        double rate_y = 0.0;
        int start_x = 0;
        int start_y = 0;
        if(it.hasNext()){
            rate_x = (double)Standard_Width/(double)shapeList.get(0).getWidth();

            rate_y = (double)Standard_Height/(double)shapeList.get(0).getHeight();
            start_x = shapeList.get(0).getPos_x();
            start_y = shapeList.get(0).getPos_y();
            System.out.println("rate_x:"+rate_x+",rate_y:"+rate_y);
        }
        while(it.hasNext()){
            SketchType p = it.next();
            p.setPos_x((int)((double)(p.getPos_x()-start_x)*rate_x));
            p.setPos_y((int)((double)(p.getPos_y()-start_y)*rate_y));
            p.setHeight((int)((double)(p.getHeight())*rate_y));
            p.setWidth((int)((double)(p.getWidth())*rate_x));
            convertList.add(p);
        }
        convertList.get(0).setHeight(Standard_Height);
        convertList.get(0).setWidth(Standard_Width);
        return convertList;
    }

    /*判断元素嵌套关系*/
    public static boolean JudgeRelation(SketchType s1, SketchType s2){
        if(s2.getPos_x()>=s1.getPos_x()
                &&s2.getPos_x()<=(s1.getPos_x()+s1.getWidth())
                &&s2.getPos_y()>=s1.getPos_y()
                &&s2.getPos_y()<=(s1.getPos_y()+s1.getHeight())){
            return true;
        }
        return false;
    }

    /*为每个节点编号*/
    public static  List<SketchTreeNode> AddId( List<SketchType> convertList ){
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
        return sketchTreeNodeList;
    }

    /*设置每一个节点关系*/
    public static List<SketchTreeNode> JudgeNodeRelation(List<SketchTreeNode> sketchTreeNodeList) {
        for (int i = 0; i < sketchTreeNodeList.size(); i++) {
            if (i == 0) {
                sketchTreeNodeList.get(i).setParentId(-1);
            } else {
                int num = i;
                boolean flag = false;
                while (flag == false) {
                    flag = JudgeRelation(sketchTreeNodeList.get(--num).getSketchType(), sketchTreeNodeList.get(i).getSketchType());
                }
                sketchTreeNodeList.get(i).setParentId(num);
            }
        }
        return sketchTreeNodeList;
    }

}
