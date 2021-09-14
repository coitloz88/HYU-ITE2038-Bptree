public class BPlusTree {
    /**
     * degree: max degree(m-way)
     *
     * insert: 넣는거 시도하는데 성공적으로 넣어지면 1, 안되면 0을 반환하는 기본 함수로 일단 넣음 나머지 쪼개는거는 비쁠트리 차원에서
     */

    private final int degree;
    private final int totalNumberOfKeys;
    private Node root;

    public BPlusTree(int degree){
        this.degree = degree;
        this.totalNumberOfKeys = degree - 1;
        root = new Node(totalNumberOfKeys, true);
    }

    public Node singleKeySearchNode(int target, boolean showNodeKey){
        Node tmpNode = root;

        while (!tmpNode.isLeaf()) {
            if (showNodeKey) tmpNode.showKeys();
            // 현재 노드가 가진 key 값에 target 값이 있는지 찾아본다
            int i = tmpNode.findIndexOfKeyInKeyArray(target);
            if (i >= tmpNode.getCurrentNumberOfKeys()) tmpNode = tmpNode.getRightNode();
            else if (target == tmpNode.getKey(i)) tmpNode = tmpNode.getLeftNode(i + 1);
            else tmpNode = tmpNode.getLeftNode(i);
        }
        return tmpNode;
    }
    
    public String singleKeySearch(int target){
        /**
         * root부터 탐색 시작
         * 동일한 값이 있을 때 return
         * 사이값일 경우 그 사이 노드로 들어감
         *
         * 찾으면 해당하는 value 반환
         */
        Node tmpNode = singleKeySearchNode(target, true);

        //leaf 도달
        int i = tmpNode.findIndexOfKeyInKeyArray(target);

        if (i < tmpNode.getCurrentNumberOfKeys() && target == tmpNode.getKey(i))
            return String.valueOf(tmpNode.getValue(i));
        else return "NOT FOUND";
    }

    public String rangeSearch(){
        //TODO: rangeSearch 함수 구현
        return "NOT FOUND";
    }

    public Node findParentNode(Node topNode, Node childNode){
        Node parentNode;

        //해당하는 자식이 있으면
        for (int i = 0; i < topNode.getCurrentNumberOfKeys() + 1; i++) {
            if (i < topNode.getCurrentNumberOfKeys()){
                if(topNode.getLeftNode(i) == childNode){
                    parentNode = topNode;
                    return parentNode;
                } else {
                    parentNode = findParentNode(topNode.getLeftNode(i), childNode);
                }
            }
            else if (i == topNode.getCurrentNumberOfKeys()) {
                if(topNode.getRightNode() == childNode) {
                    parentNode = topNode;
                    return parentNode;
                } else {
                    parentNode = findParentNode(topNode.getRightNode(), childNode);
                }
            }
            if(parentNode != null) return parentNode;

        }
        return parentNode;
    }

    public void insert(int inputkey, int inputValue){
        //중복 key는 들어오지 않는다
        //TODO: delete로 싹다 지워서 root가 null이 된 경우(예외처리)

        /**
         * case1. keys가 다차지 않은 leafnode에 insert하는 경우
         * case2. keys가 다차서 부모 노드를 쪼개야 하는 경우
         *  2-1. leaf->parent(노드를 남겨두고 올려야함)
         *  2-2. childInternal->parentInternal(노드를 남겨두지 않고 올리면 됨)
         *      parentNode를 찾아주는 함수가 필요함!
         *      2-2-2. parentInternalNode가 root인 경우(root를 쪼개서 새로운 root가 만들어짐)
         *      2-2-3. parentInternalNode가 root가 아닌 경우
         */
    }

    public void internalInsert(Node internalNode, Node childNode, int inputKey){
        //leafNode 위의 Node도 다차서 또 쪼개야할때 재귀적으로 호출하는 함수
    }

    public void delete(){
        //TODO: delete 함수 구현
    }

    public void show(){
        for (int i = 0; i < root.getCurrentNumberOfKeys(); i++) {
            System.out.println("key: " + root.getKey(i) + ", value: " + root.getValue(i));
        }
    }
}
