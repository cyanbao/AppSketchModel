package SketchType;

/**
 * Created by Cyan on 2018/3/29.
 */
public class SketchTreeNode {
    private int nodeId;
    private int parentId;
    private SketchType sketchType;
   /**
    * 构造函数
    * @param nodeId 节点id
    */
   public SketchTreeNode(int nodeId){
       this.nodeId = nodeId;
   }

   /**
     * 构造函数
     * @param nodeId 节点Id
     * @param parentId 父节点Id
     */
    public SketchTreeNode(int nodeId,int parentId){
        this.nodeId = nodeId;
        this.parentId = parentId;
    }

    /**
     * 构造函数
     * @param nodeId 节点Id
     * @param sketchType 元素整体
     */
    public SketchTreeNode(int nodeId,SketchType sketchType){
        this.nodeId = nodeId;
        this.sketchType = sketchType;
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

    public SketchType getSketchType() {
        return sketchType;
    }

    public void setSketchType(SketchType sketchType) {
        this.sketchType = sketchType;
    }
}
