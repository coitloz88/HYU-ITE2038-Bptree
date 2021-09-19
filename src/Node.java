import java.io.Serializable;

public class Node implements Serializable {
    /**
     * 0. leaf인지 아닌지
     * 1. m: # of keys
     * 2. p: an array of <key, left_child_node> pairs
     * 3. r: a pointer to the rightmost child node / a pointer to the right sibling node
     * <p>
     * boolean push_back(int index): index값을 하나 받으면 key배열에 더 넣을 수 있는지 확인해보고 되면 넣고 안되면 0을 반환하는 함수
     */

    private boolean leaf; //root혹은 internal일때는 false,leaf일때는 true
    private int currentNumberOfKeys; //배열 마지막 번호보다 1 크다.
    private int[] keys;
    private int[] values;
    private Node[] childNodes;
    private Node parent;

    public Node(int totalNumberOfKeys, boolean isLeaf, Node parent) {
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

    public int getKey(int i) {
        if (i >= currentNumberOfKeys) {
            System.out.println(" ** 초과한 key를 요청한 node **");
            showKeys();
            System.out.println(" ** 초과 보여주기 끝! **");
            System.err.println("getKey(): Ouf of bound");
        }

        return keys[i];
    }

    public void setKey(int key, int i) {
        if (i >= keys.length) System.err.println("setKey(): Ouf of bound");
        keys[i] = key;
    }

    public Node getChildNode(int i) {
        return childNodes[i];
    }

    public void setChildNode(Node leftNode, int i) {
        childNodes[i] = leftNode;
    }

    public int getValue(int i) {
        return values[i];
    }

    public void setValue(int value, int i) {
        values[i] = value;
    }

    public Node getParent() {
        return parent;
    }

    public void setParent(Node parent) {
        this.parent = parent;
    }

    //input이 삽입되면 되는 [index], 혹은 input이 위치한 [index] 반환
    public int findIndexOfKeyInKeyArray(int input) {
        //주의: search를 하는게 아니라 그냥 순서만 찾는 경우 음... 암튼 주의
        int index;
        for (index = 0; index < currentNumberOfKeys && input > keys[index]; index++) {
        } //input이 모든 key값보다 크면 keys 크기를 1 초과하는 값을 가짐
        return index;
    }

    public int findSmallIndexOfKeyInKeys(int input) {
        if (input <= keys[0]) return 0;
        int index;
        for (index = 0; index < currentNumberOfKeys && input >= keys[index]; index++) {
        }
        index--;
        return index;
    }

    public int findIndexOfChild(int input) {
        int index;
        for (index = 0; index < currentNumberOfKeys && input >= keys[index]; index++) {
        }
        return index;
    }

    //leaf에 삽입시 key-value 오름차순 정렬 삽입, parent삽입시..코드참고
    public void push(int key, int value) {

        if (currentNumberOfKeys >= keys.length) {
            System.err.println("push() 오류: 키 개수 초과");
            return;
        }

        int target_i = findIndexOfKeyInKeyArray(key); //[target_i]에 (index, value)가 들어간다. key를 오름차순으로 정렬하기 위함

        int[] tmpKeyArray = new int[currentNumberOfKeys - target_i];
        if (leaf) {
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
                tmpLeftNodesArray[i - target_i] = childNodes[i + 1];
            }
            ++currentNumberOfKeys;
            //target_i에 key가 삽입된다.
            //[target_i + 1]에는 새로운 노드가 연결된다(BPlusTree.java에서 처리)
            keys[target_i] = key;
            childNodes[target_i + 1] = null;
            for (int i = target_i + 1; i < currentNumberOfKeys; i++) {
                keys[i] = tmpKeyArray[i - target_i - 1];
                childNodes[i + 1] = tmpLeftNodesArray[i - target_i - 1];
            }

        }
    }

    public void push_out(int key) {
        if (currentNumberOfKeys == 0) {
            System.err.println("push_out() 오류: key가 없음");
        }

        int target_i = findIndexOfKeyInKeyArray(key); //[target_i]에 key가 위치한다. 이를 삭제해줄 예정

        if (leaf) {
            //leaf일때
            if (target_i == currentNumberOfKeys - 1) {
                keys[target_i] = 0;
                values[target_i] = 0;
            } else {
                for (int i = target_i; i < currentNumberOfKeys - 1; i++) {
                    keys[i] = keys[i + 1];
                    values[i] = values[i + 1];
                }
            }
        } else {
            //leaf가 아닐때(delete할때)
            int target_i_child = findIndexOfChild(key);
            if (currentNumberOfKeys > 1 && target_i_child >= currentNumberOfKeys) {
                keys[target_i] = 0;
                if (childNodes[target_i_child].isLeaf())
                    childNodes[target_i_child - 1].setChildNode(childNodes[target_i_child].getChildNode(0), 0);
                childNodes[target_i_child] = null;
            } else if (target_i_child == 0) { //0번째 child에서 값을 빼는 경우, 해당 deleteKey값과 child+1번째 node가 사라진다(sibling이 사라짐)
                if (childNodes[target_i_child].isLeaf())
                    childNodes[target_i_child].setChildNode(childNodes[target_i_child + 1].getChildNode(0), 0);
                for (int i = target_i; i < currentNumberOfKeys - 1; i++) {
                    keys[i] = keys[i + 1];
                }
                for (int i = target_i_child + 1; i < currentNumberOfKeys; i++) {
                    childNodes[i] = childNodes[i + 1];
                }
            } else { //1번째 이상의 child에서 값을 빼는 경우, child번째 node가 사라진다(mainDeleteNode가 사라짐)
                if (childNodes[target_i_child].isLeaf())
                    childNodes[target_i_child - 1].setChildNode(childNodes[target_i_child].getChildNode(0), 0);
                for (int i = target_i; i < currentNumberOfKeys - 1; i++) {
                    keys[i] = keys[i + 1];
                }
                for (int i = target_i_child; i < currentNumberOfKeys; i++) {
                    childNodes[i] = childNodes[i + 1];
                }
            }
        }
        --currentNumberOfKeys;

    }

    public void showKeys() {
        for (int i = 0; i < currentNumberOfKeys; i++) {
            if (i != 0) System.out.print(",");
            System.out.print(keys[i]);
        }
        System.out.println();
    }

}



