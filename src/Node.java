public class Node {
    /**
     * 0. leaf인지 아닌지 - 개념적으로는 구분되지만 코드 상으로는 구분 되지 않음
     * 1. m: # of keys
     * 2. p: an array of <key, left_child_node> pairs
     * 3. r: a pointer to the rightmost child node / a pointer to the right sibling node
     *
     * boolean push_back(int index): index값을 하나 받으면 key배열에 더 넣을 수 있는지 확인해보고 되면 넣고 안되면 0을 반환하는 함수
     */

    private int currentNumberOfKeys;
    private int[] keys;
    private Node[] leftNodes;
    private Node rightNode;

    public Node(int numberOfKey){
        currentNumberOfKeys = 0;
        keys = new int[numberOfKey - 1];
        leftNodes = new Node[numberOfKey - 1];
    }

    public void setCurrentNumberOfKeys(int currentNumberOfKeys) {
        this.currentNumberOfKeys = currentNumberOfKeys;
    }

    public int getCurrentNumberOfKeys() {
        return currentNumberOfKeys;
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
