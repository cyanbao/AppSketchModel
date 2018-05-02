package ControlsLayout;

import ControlsType.ControlsManyTreeNode;

/**
 * Created by Cyan on 2018/5/3.
 */
public class JudgeRootChange {
    private ControlsManyTreeNode root;
    private boolean flag;

    public JudgeRootChange(ControlsManyTreeNode root,boolean flag){
        this.root = root;
        this.flag=flag;
    }

    public JudgeRootChange(){}

    public ControlsManyTreeNode getRoot() {
        return root;
    }

    public void setRoot(ControlsManyTreeNode root) {
        this.root = root;
    }

    public boolean isFlag() {
        return flag;
    }

    public void setFlag(boolean flag) {
        this.flag = flag;
    }
}
