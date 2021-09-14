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
    private int[] values;
    private Node[] childNodes;
    private Node parent;

    public Node(int totalNumberOfKeys, boolean isLeaf, Node parent){
        leaf = isLeaf;

        currentNumberOfKeys = 0;

        keys = new int[totalNumberOfKeys];
        values = new int[totalNumberOfKeys];

        childNodes = new Node[totalNumberOfKeys + 1];
        for (int i = 0; i < totalNumberOfKeys + 1; i++) childNodes[i] = null;
        this.parent = parent;
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

    public int getKey(int i) { return keys[i]; }

    public void setKey(int key, int i) { keys[i] = key; }

    public Node getChildNode(int i) { return childNodes[i]; }

    public void setChildNode(Node leftNode, int i) { childNodes[i] = leftNode; }

    public int getValue(int i) { return values[i]; }

    public void setValue(int value, int i) { values[i] = value; }

    public Node getParent() { return parent; }

    public void setParent(Node parent) { this.parent = parent; }

    //input보다 작거나 같은 key index를 반환하는 함수
    public int findIndexOfKeyInKeyArray(int input){
        //주의: search를 하는게 아니라 그냥 순서만 찾는 경우 음... 암튼 주의
        int index;
        for (index = 0; index < currentNumberOfKeys && input > keys[index]; index++) { } //input이 모든 key값보다 크면 keys 크기를 1 초과하는 값을 가짐
        return index;
    }

    //leaf에 삽입시 key-value 오름차순 정렬 삽입, parent삽입시..코드참고
    public void push_back(int key, int value){

        if(currentNumberOfKeys >= keys.length){
            System.err.println("push_back() 오류: 키 개수 초과");
            return;
        }

        int target_i = findIndexOfKeyInKeyArray(key); //[target_i]에 (index, value)가 들어간다. key를 오름차순으로 정렬하기 위함

        int[] tmpKeyArray = new int[currentNumberOfKeys - target_i];
        if(leaf) {
            //leaf인 경우, keys와 values가 유효함. key와 value는 1:1로 대응됨
            int[] tmpValueArray = new int[currentNumberOfKeys - target_i];
            for (int i = target_i; i < currentNumberOfKeys; i++) {
                tmpKeyArray[i - target_i] = keys[i];
                tmpValueArray[i - target_i] = values[i];
            }
            keys[target_i] = key;
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
                tmpLeftNodesArray[i - target_i] = childNodes[i];
            }
            //target_i에 key가 삽입된다.
            //target_i + 1에 새로운 노드가 연결된다(BPlusTree.java에서 처리)
            ++currentNumberOfKeys;
            keys[target_i] = key;


            if(target_i >= keys.length - 2){
                //leftNodes 밀림 없음
                for (int i = target_i; i < currentNumberOfKeys; i++) {
                    tmpKeyArray[i - target_i] = keys[i];
                }
            } else {
                //leftNodes 밀림 있음
                childNodes[target_i] = null; //split 후 newRightChildNode가 들어올 곳
                keys[++target_i] = tmpKeyArray[0];
                keys[++target_i] = tmpKeyArray[1];

                for (int i = target_i; i < currentNumberOfKeys; i++) {
                    keys[i] = tmpKeyArray[i - target_i];
                    childNodes[i] = tmpLeftNodesArray[i - target_i];
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
