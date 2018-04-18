package ControlIdentification;

/**
 * Created by Cyan on 2018/4/12.
 */
public class ControlsTreeNode {
    private int nodeId;
    private int parentId;
    private ControlsType controlsType;


    /**
     * 构造函数
     * @param nodeId 节点id
     */
    public ControlsTreeNode(int nodeId){
        this.nodeId = nodeId;
    }

    public int getNodeId() {
        return nodeId;
    }

    public void setNodeId(int nodeId) {
        this.nodeId = nodeId;
    }

    public int getParentId() {
        return parentId;
    }

    public void setParentId(int parentId) {
        this.parentId = parentId;
    }


    public ControlsType getControlsType() {
        return controlsType;
    }

    public void setControlsType(ControlsType controlsType) {
        this.controlsType = controlsType;
    }
}
