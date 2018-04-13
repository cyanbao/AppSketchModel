package ControlIdentification;

/**
 * Created by Cyan on 2018/3/22.
 */

/*十五种基本控件
* Todo:按钮，文本框的设计
* ...
*
* */
public class ControlsType{
    private int x;
    private int y;
    private int height;
    private int width;
    private String label;
    private enum RelativeLayout_height{
        UP,MIDDLE,DOWN
    }
    private enum RelativeLayout_width{
        LEFT,MIDDLE,RIGHT
    }
    private RelativeLayout_height relativeLayout_height;
    private RelativeLayout_width relativeLayout_width;



//    public ControlIdentification(String label){
//
//    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }
}
