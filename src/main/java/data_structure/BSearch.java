package data_structure;


import java.util.ArrayList;

public class BSearch {

    BSearchNode root;

    public BSearch(){
        root = null;
    }

    /**
     * Insert a new node into the tree
     * @param user
     * @param password
     */

    
    public void insert(String user, String password){
        root = insert(user, password, root);
    }

    /**
     * Insert a new node into the tree
     * @param user
     * @param password
     * @param T
     * @return BSearchNode
     */
    
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
     /**
     * Search a node in the tree
     * @param user
     * @return BSearchNode
     */
     /**
     * Search a node in the tree
     * @param user
     * @return BSearchNode
     */
    
    public BSearchNode search(String user){
        return search(user, root);
    }

    /**
     * Search a node in the tree
     * @param user
     * @param T BSearchNode
     * @return BSearchNode
     */

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
    
    /**
     * Update a node in the tree
     * @param user
     * @param password
     */
    
    public void update(String user, String password){
        update(user, password, root);
    }

     /**
     * Update a node in the tree
     * @param user
     * @param password
     * @param T
     */

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
    /**
     * Delete a node in the tree
     * @param user
     */
    
    public void delete(String user){
        delete(user, root);
    }
    /**
     * Delete a node in the tree
     * @param user
     * @param T BSearchNode
     * @return BSearchNode
     */

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
    
    /**
     * Find the minimum node in the tree
     * @param T
     * @return BSearchNode
     */
    
    public BSearchNode findMin(BSearchNode T){
        if (T == null){
            return null;
        } else if (T.left == null){
            return T;
        } else {
            return findMin(T.left);
        }
    }
    
    /**
     * Get all the nodes in the tree
     * @return ArrayList<BSearchNode>
     */
    
    public ArrayList<BSearchNode> getNodes(){
        ArrayList<BSearchNode> nodes = new ArrayList<BSearchNode>();
        getNodes(root, nodes);
        return nodes;
    }
    
     /**
     * Get all the nodes in the tree
     * @param T
     * @param nodes
     */
    
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
