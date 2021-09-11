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

    public Node(int numberOfKey){
        leaf = false;
        currentNumberOfKeys = 0;
        keys = new int[numberOfKey - 1];
        leftNodes = new Node[numberOfKey - 1];
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

    public void setKeys(int[] keys) {
        this.keys = keys;
    }

    public Node getLeftNode(int i) {
        return leftNodes[i];
    }

    public void setLeftNodes(Node leftNode, int i) {
        leftNodes[i] = leftNode;
    }

    public Node getRightNode() {
        return rightNode;
    }

    public void setRightNode(Node rightNode) {
        this.rightNode = rightNode;
    }

    //TODO: leftNode, rightNode에 포인터 건네주는 함수 구현

    public void push_back(int index){
        keys[currentNumberOfKeys++] = index;
        //TODO: 오름차순 정렬
    }

    public void showKeys(){
        for (int key:keys) {
            System.out.print(key + " ");
        }
        System.out.println();
    }

}
