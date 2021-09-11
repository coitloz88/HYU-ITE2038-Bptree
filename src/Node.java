public class Node {
    /**
     * 0. leaf인지 아닌지 - 개념적으로는 구분되지만 코드 상으로는 구분 되지 않음
     * 1. m: # of keys
     * 2. p: an array of <key, left_child_node> pairs
     * 3. r: a pointer to the rightmost child node / a pointer to the right sibling node
     *
     * boolean push_back(int index): index값을 하나 받으면 key배열에 더 넣을 수 있는지 확인해보고 되면 넣고 안되면 0을 반환하는 함수
     */

    private int totalNumberOfKeys;
    private int currentNumberOfKeys;
    private int[] keys;
    private Node[] leftNodes;
    private Node rightNode;

    public Node(int numberOfKey){
        totalNumberOfKeys = numberOfKey;
        currentNumberOfKeys = 0;
        keys = new int[numberOfKey];
        leftNodes = new Node[numberOfKey];
    }

    public int getTotalNumberOfKeys(){
        return totalNumberOfKeys;
    }

    public boolean push_back(int index){
        if(currentNumberOfKeys < totalNumberOfKeys){
            keys[currentNumberOfKeys++] = index;
            return true;
        }
        return false;
    }
}
