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
    private int currentNumberOfKeys; //배열 마지막 번호보다 1 크다.
    private int[] keys;
    private Node[] leftNodes;
    private Node rightNode;
    private int[] values;

    public Node(int degree, boolean isLeaf){
        leaf = isLeaf;
        currentNumberOfKeys = 0;
        keys = new int[degree];
        leftNodes = new Node[degree];
        for (int i = 0; i < degree; i++) leftNodes[i] = null;
        values = new int[degree];
        rightNode = null;
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

    public void push_back(int index, int value){
        if(currentNumberOfKeys >= keys.length){
            System.err.println("push_back() 오류: 키 개수 초과");
            return;
        }

        int target_i; //[target_i]에 (index, value)가 들어간다.
        for(target_i = 0; target_i < currentNumberOfKeys && index > keys[target_i]; target_i++){} //key를 오름차순으로 정렬하기 위함

        int[] tmpKeyArray = new int[currentNumberOfKeys - target_i];
        if(leaf) {
            //leaf인 경우, keys와 values가 유효함. key와 value는 1:1로 대응됨
            int[] tmpValueArray = new int[currentNumberOfKeys - target_i];
            for (int i = target_i; i < currentNumberOfKeys; i++) {
                tmpKeyArray[i - target_i] = keys[i];
                tmpValueArray[i - target_i] = values[i];
            }
            keys[target_i] = index;
            values[target_i] = value;
            ++currentNumberOfKeys;
            for (int i = target_i + 1; i < currentNumberOfKeys; i++) {
                keys[i] = tmpKeyArray[i - target_i - 1];
                values[i] = tmpValueArray[i - target_i - 1];
            }
        } else {
            //leaf가 아닌 경우, keys와 leftnodes가 유효함. keys와 leftnodes는 1:1로 대응되지 않음(leftnodes는 듬성듬성 존재)
            //insert나 delete할때 노드를 쪼개고 부모에 넣는 경우를 위해 사용
            //leftnode[-]가 null인 경우 null로, 주소를 가지는 경우는 주소를 저장해줌

            //쪼개기 할때, rightChild가 새로 생긴다고 가정
            
            Node[] tmpLeftNodesArray = new Node[currentNumberOfKeys - target_i];
            for (int i = target_i; i < currentNumberOfKeys; i++) {
                tmpKeyArray[i - target_i] = keys[i];
                tmpLeftNodesArray[i - target_i] = leftNodes[i];
            }
            //target_i에 key가 삽입된다.
            //target_i + 1에 새로운 노드가 연결된다(BPlusTree.java에서 처리)
            ++currentNumberOfKeys;
            keys[target_i] = index;


            if(target_i >= keys.length - 2){
                //leftNodes 밀림 없음
                for (int i = target_i; i < currentNumberOfKeys; i++) {
                    tmpKeyArray[i - target_i] = keys[i];
                }
            } else {
                //leftNodes 밀림 있음
                leftNodes[target_i] = null; //split 후 newRightChildNode가 들어올 곳
                keys[++target_i] = tmpKeyArray[0];
                keys[++target_i] = tmpKeyArray[1];

                for (int i = target_i; i < currentNumberOfKeys; i++) {
                    keys[i] = tmpKeyArray[i - target_i];
                    leftNodes[i] = tmpLeftNodesArray[i - target_i];
                }
            }

        }

    }

    public void showKeys(){
        for (int i = 0; i < currentNumberOfKeys; i++) {
            if(i != 0) System.out.print(",");
            System.out.print(keys[i]);
        }
        System.out.println();
    }

}
