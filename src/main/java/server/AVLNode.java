package server;

public class AVLNode {
    int element;
    String[] data;
    AVLNode left;
    AVLNode right;
    int height;

    public AVLNode(int element, String[] data){
        this(element, data, null, null);
    }

    public AVLNode(int element, String[] data, AVLNode left, AVLNode right){
        this.element = element;
        this.data = data;
        this.left = left;
        this.right = right;
        this.height = 0;
    }
}
