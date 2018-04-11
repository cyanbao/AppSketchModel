package SketchType;

/**
 * Description: 定义草图类型，位置
 * Created by Cyan on 2018/3/22.
 */
/* 数字对应关系
* 1 -> 竖线
* 2 -> 圆
* 3 -> 三角
* 4 -> X
* 5 -> #
* 6 -> 矩形
* 7 -> 箭头
* 8 -> +
* 9 -> 尖号
* 10 -> A
* 11 -> B
* 12 -> F
* 13 -> 横线*/

public class SketchType {
    private int pos_x;
    private int pos_y;
    private int height;
    private int width;
    private int label;

    public SketchType(){};

    public SketchType(int x,int y,int height,int width,int label){
        this.pos_x = x;
        this.pos_y = y;
        this.height = height;
        this.width = width;
        this.label = label;
     }
    public int getPos_x() {
        return pos_x;
    }

    public void setPos_x(int pos_x) {
        this.pos_x = pos_x;
    }

    public int getPos_y() {
        return pos_y;
    }

    public void setPos_y(int pos_y) {
        this.pos_y = pos_y;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public int getLabel() {
        return label;
    }

    public void setLabel(int label) {
        this.label = label;
    }
}
