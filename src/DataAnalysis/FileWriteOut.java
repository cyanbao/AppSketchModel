package DataAnalysis;

import ControlsType.ControlsManyTreeNode;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.File;
import java.io.IOException;
import java.util.Iterator;

/**
 * Created by Cyan on 2018/4/6.
 */
public class FileWriteOut {
    public static void FileWriteOutByJson(ControlsManyTreeNode root,String filename)throws IOException{
        String result = new String();
        result = toString(root,result);
        System.out.println(result);
        File file = new File(filename+"-controls.json");
        try {
            BufferedWriter bw = new BufferedWriter(new FileWriter(file));
            bw.write(result);
            bw.close();
        }catch (IOException el){
            //TODO Auto-geneated catch block
            el.printStackTrace();
        }
    }

    public static String toString(ControlsManyTreeNode root,String result){
        result += "{";
        result +="\"type\""+":"+"\""+root.getData().getControlsType().getLabel()+"\""+",";
        result +="\"x\""+":"+"\""+root.getData().getControlsType().getX()+"\""+",";
        result +="\"y\""+":"+"\""+root.getData().getControlsType().getY()+"\""+",";
        result +="\"width\""+":"+"\""+root.getData().getControlsType().getWidth()+"\""+",";
        result +="\"height\""+":"+"\""+root.getData().getControlsType().getHeight()+"\""+",";
        if(root.getChildList().size()>0){
            result+="\"children\""+":"+"[";
            for(int i=0;i<root.getChildList().size();i++){
                result = toString(root.getChildList().get(i),result);
                if(i<root.getChildList().size()&&
                        i<root.getChildList().size()-1)
                    result+=",";
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
