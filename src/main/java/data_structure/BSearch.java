package data_structure;


import java.util.ArrayList;

/**
 * This class was taken from the Estructuras de Datos Jerárquicas presentation.
 */
public class BSearch {

    BSearchNode root;

    public BSearch(){
        root = null;
    }

    public void insert(String user, String password){
        root = insert(user, password, root);
    }

    private BSearchNode insert(String user, String password, BSearchNode T){
        if (T == null){
            return new BSearchNode(user, password);
        }
        if (user.compareTo(T.user)<0){
            T.left = insert(user, password, T.left);
        } else if (user.compareTo(T.user)>0) {
            T.right = insert(user, password, T.right);
        }
        return T;
    }

    public BSearchNode search(String user){
        return search(user, root);
    }

    public BSearchNode search(String user, BSearchNode T){
        if (T == null){
            return null;
        }
        if (user.compareTo(T.user)<0){
            return search(user, T.left);
        } else if (user.compareTo(T.user)>0) {
            return search(user, T.right);
        }
        return T;
    }

    public void update(String user, String password){
        update(user, password, root);
    }

    public void update(String user, String password, BSearchNode T){
        if (T == null){
            return;
        }
        if (user.compareTo(T.user)<0){
            update(user, password, T.left);
        } else if (user.compareTo(T.user)>0) {
            update(user, password, T.right);
        } else {
            T.password = password;
        }
    }

    public void delete(String user){
        delete(user, root);
    }

    public BSearchNode delete(String user, BSearchNode T){
        if (T == null){
            return null;
        }
        if (user.compareTo(T.user)<0){
            T.left = delete(user, T.left);
        } else if (user.compareTo(T.user)>0) {
            T.right = delete(user, T.right);
        } else {
            if (T.left == null){
                return T.right;
            } else if (T.right == null){
                return T.left;
            } else {
                T.user = findMin(T.right).user;
                T.right = delete(T.user, T.right);
            }
        }
        return T;
    }

    public BSearchNode findMin(BSearchNode T){
        if (T == null){
            return null;
        } else if (T.left == null){
            return T;
        } else {
            return findMin(T.left);
        }
    }

    public ArrayList<BSearchNode> getNodes(){
        ArrayList<BSearchNode> nodes = new ArrayList<BSearchNode>();
        getNodes(root, nodes);
        return nodes;
    }

    public void getNodes(BSearchNode T, ArrayList<BSearchNode> nodes){
        if (T == null){
            return;
        }
        getNodes(T.left, nodes);
        nodes.add(T);
        getNodes(T.right, nodes);
    }

    public void print(){
        print(root);
    }

    public void print(BSearchNode T){
        if (T == null){
            return;
        }
        print(T.left);
        System.out.println(T.user + " " + T.password);
        print(T.right);
    }

}
