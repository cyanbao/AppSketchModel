package DataAnalysis;

import SketchType.SketchType;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by Cyan on 2018/3/22.
 */
public class FileReadIn {
    public static List<SketchType> shapeList = new ArrayList<>();
    public static void ReadFileByLines(String fileName){
        File file = new File(fileName);
        BufferedReader reader = null;
        try {
            //System.out.println("以行为单位读取文件内容，一次读一整行：");
            reader = new BufferedReader(new FileReader(file));
            String tempString = null;
            int line = 0;
            // 一次读入一行，直到读入null为文件结束
            while (((tempString = reader.readLine()) != null)&&tempString!="") {
                // 显示行号
//                System.out.println("line " + line + ": " + tempString);
//                line++;
                /*读取文件中的数据存储与节点内*/
                String []sArray = tempString.split("\t");
                //System.out.println(sArray[0]+sArray[1]+sArray[2]+sArray[3]+sArray[4]+"test");
                SketchType shapeNode =  new SketchType();
                try {
                    shapeNode = new SketchType(
                            Integer.valueOf(sArray[1]), Integer.valueOf(sArray[0]), Integer.valueOf(sArray[2]),
                            Integer.valueOf(sArray[3]), Integer.valueOf(sArray[4]));
                }
                catch (NumberFormatException e){
                    e.printStackTrace();
                }
                shapeList.add(shapeNode);
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e1) {
                }
            }
        }
    }


    //test
    public static void main(String agrs[]){
        ReadFileByLines("resources/test.txt");
        Iterator<SketchType> it = shapeList.iterator();
        int line = 0;
         while(it.hasNext()){
             SketchType p = it.next();
             System.out.println(line+":"+p.getPos_x()+","+p.getPos_y()+","+p.getHeight()+","+p.getWidth()+","+p.getLabel());
             line++;
         }
    }
}
