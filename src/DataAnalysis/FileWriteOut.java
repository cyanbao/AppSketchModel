package DataAnalysis;

import ControlsType.ControlsManyTreeNode;

import java.util.Iterator;

/**
 * Created by Cyan on 2018/4/6.
 */
public class FileWriteOut {
    public static void FileWriteOutByJson(ControlsManyTreeNode root){
        String result = new String();
        System.out.println(toString(root,result));
    }

    public static String toString(ControlsManyTreeNode root,String result){
        result += "{";
        result +="\"type\""+":"+"\""+root.getData().getControlsType().getLabel()+"\"";
        result+=",";
        if(root.getChildList().size()>0){
            result+="\"children\""+":"+"[";
            for(int i=0;i<root.getChildList().size();i++){
                result = toString(root.getChildList().get(i),result);
            }
            result+="]";
        }
        else{
            result+="\"children\""+":"+"null";
        }
        result += "}";
        return result;
    }
}
