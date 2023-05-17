package data_structure;


/**
 * This class was taken from Estructuras de Datos Jerárquicas presentation.
 */
public class BSearchNode {


    public String user;

    public String password;

    public BSearchNode left;

    public BSearchNode right;

    public BSearchNode(String user, String password){
        this.user = user;
        this.password = password;
        this.left = null;
        this.right = null;
    }


}
