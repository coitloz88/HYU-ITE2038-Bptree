import java.util.Arrays;

public class Node{
    /**
     * 0. leaf인지 아닌지
     * 1. m: # of keys
     * 2. p: an array of <key, left_child_node> pairs
     * 3. r: a pointer to the rightmost child node / a pointer to the right sibling node
     *
     * boolean push_back(int index): index값을 하나 받으면 key배열에 더 넣을 수 있는지 확인해보고 되면 넣고 안되면 0을 반환하는 함수
     */

    private boolean leaf; //root혹은 internal일때는 false,leaf일때는 true
    private int currentNumberOfKeys;
    private int[] keys;
    private Node[] leftNodes;
    private Node rightNode;
    private int[] values;

    public Node(int degree, boolean isLeaf){
        leaf = isLeaf;
        currentNumberOfKeys = 0;
        keys = new int[degree];
        leftNodes = new Node[degree];
        values = new int[degree];
    }

    public boolean isLeaf() {
        return leaf;
    }

    public void setLeaf(boolean leaf) {
        this.leaf = leaf;
    }

    public int getCurrentNumberOfKeys() {
        return currentNumberOfKeys;
    }

    public void setCurrentNumberOfKeys(int currentNumberOfKeys) {
        this.currentNumberOfKeys = currentNumberOfKeys;
    }

    public int[] getKeys() {
        return keys;
    }

    public int getKey(int i) { return keys[i]; }

    public void setKey(int key, int i) { keys[i] = key; }

    public Node getLeftNode(int i) { return leftNodes[i]; }

    public void setLeftNode(Node leftNode, int i) { leftNodes[i] = leftNode; }

    public Node getRightNode() { return rightNode; }

    public void setRightNode(Node rightNode) { this.rightNode = rightNode; }

    public int getValue(int i) { return values[i]; }

    public void setValue(int value, int i) { values[i] = value; }

    //TODO: leftNode, rightNode에 포인터 건네주는 함수 구현

    public void push_back(int index){
        values[currentNumberOfKeys] = index * 11;
        keys[currentNumberOfKeys] = index;
        Arrays.sort(keys, 0, currentNumberOfKeys++);
    }

    public void showKeys(){
        for (int i = 0; i < currentNumberOfKeys; i++) {
            if(i != 0) System.out.print(",");
            System.out.print(keys[i]);
        }
        System.out.println();
    }

}
