package server;

import java.util.Arrays;

public class AVLTree {
    private static final int ALLOWED_IMBALANCE = 1;
    private int height(AVLNode T){
        return T == null ? -1 : T.height;
    }
    public AVLNode insert(int x, String[] data, AVLNode T){
        if (T == null){
            return new AVLNode(x, data);
        }
        if (x<T.element){
            T.left = insert(x, data, T.left);
        } else if (x>T.element) {
            T.right = insert(x, data, T.right);
        }
        return balance(T);
    }
    private AVLNode balance(AVLNode T){
        if (T==null){
            return T;
        }
        if (height(T.left)-height(T.right)>ALLOWED_IMBALANCE){
            if (height(T.right.right)>=height(T.right.left)){
                T = rotateWithLeftChild(T);
            }
            else{
                T = doubleWithLeftChild(T);
            }
        }
        else{
            if(height(T.right)-height(T.left)>ALLOWED_IMBALANCE){
                if (height(T.right.right)>=height(T.right.left)){
                    T = rotateWithRightChild(T);
                }
                else{
                    T = doubleWithRightChild(T);
                }
            }
        }
        T.height = Math.max(height(T.left),height(T.right)+1);
        return T;
    }
    private AVLNode rotateWithLeftChild(AVLNode k2){
        AVLNode k1 = k2.left;
        k2.left = k1.right;
        k1.right = k2;
        k2.height = Math.max(height(k2.left),height(k2.right))+1;
        k1.height = Math.max(height(k1.left), k2.height)+1;
        return k1;
    }
    private AVLNode rotateWithRightChild(AVLNode k1){
        AVLNode k2 = k1.right;
        k1.right = k2.left;
        k2.left = k1;
        k1.height = Math.max(height(k1.left), height(k1.right)) + 1;
        k2.height = Math.max(height(k2.right), k1.height) + 1;
        return k2;
    }
    private AVLNode doubleWithLeftChild(AVLNode k3){
        k3.left = rotateWithRightChild(k3.left);
        return rotateWithLeftChild(k3);
    }
    private AVLNode doubleWithRightChild(AVLNode k3){
        k3.right = rotateWithLeftChild(k3.right);
        return rotateWithRightChild(k3);
    }
    public void inOrderTraversal(AVLNode root) {
        if (root != null) {
            inOrderTraversal(root.left);
            System.out.print(root.element + " ");
            inOrderTraversal(root.right);
        }
    }
}
