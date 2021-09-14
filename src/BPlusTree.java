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
        root = new Node(totalNumberOfKeys, true, null);
    }

    public Node singleKeySearchNode(int target, boolean showNodeKey){
        Node tmpNode = root;

        while (!tmpNode.isLeaf()) {
            if (showNodeKey) tmpNode.showKeys();
            // 현재 노드가 가진 key 값에 target 값이 있는지 찾아본다
            int i = tmpNode.findIndexOfKeyInKeyArray(target);
            if (target == tmpNode.getKey(i)) tmpNode = tmpNode.getChildNode(i + 1);
            else tmpNode = tmpNode.getChildNode(i);
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

    public void insert(int inputKey, int inputValue){
        //중복 key는 들어오지 않는다
        //TODO: delete로 싹다 지워서 root가 null이 된 경우(예외처리)

        Node targetNode = singleKeySearchNode(inputKey, false);

        if(targetNode.getCurrentNumberOfKeys() < totalNumberOfKeys){
            targetNode.push_back(inputKey, inputValue);
        } else {
            //split
            int split_i = degree % 2 == 0 ? degree / 2 : (int) Math.ceil((double)degree / 2) - 1; //longTmpNode의 [split_i]번째를 올림

            /**
             * 1. (totalNumberOfKeys + 1)의 Node를 하나 생성, targetNode의 '값'만 복사해줌 (주소 X) ->virtualNode
             *      leftNode랑 .. 기타등등
             * 2. inputKey, inputValue 쌍을 넣어줌 push_back해줌
             * 3. rightChildNode(new로 동적할당) -> longTmpNode의 [split_i] 이후의 값을 대입
             * 4. targetNode의 rightNode가 rightChildNode를 가리키게 함 #targetNode가 root인지 확인!!(5와 예외처리)
             * 5. virutalNode의 keys[split_i]를 parent node에 internalNodeInsert()함
             */

            Node virtualNode = new Node(totalNumberOfKeys + 1, true, null);

            //복사 및 split
            for (int i = 0; i < totalNumberOfKeys; i++) {
                virtualNode.push_back(targetNode.getKey(i), targetNode.getValue(i));
            }
            virtualNode.push_back(inputKey, inputValue); //하나 남는 자리에 inputKey와 inputValue 넣어줌

            Node rightNode = new Node(totalNumberOfKeys, true, targetNode.getParent());

            for (int i = split_i; i < totalNumberOfKeys + 1; i++) {
                rightNode.push_back(virtualNode.getKey(i), virtualNode.getValue(i));
                if (i < totalNumberOfKeys){ targetNode.setKey(0, i); targetNode.setValue(0, i);}
            } targetNode.setCurrentNumberOfKeys(split_i);

            rightNode.setChildNode(targetNode.getChildNode(totalNumberOfKeys), totalNumberOfKeys);
            targetNode.setChildNode(rightNode, totalNumberOfKeys);
            //분할 완료

            //parent Node로 올려주기
            if(targetNode == root){
                //TODO: root인 경우 새로운 root를 파고 그 root의 좌우를 targetNode, rightNode로 설정해주고 각 노드의 부모를 새로운 root로 한다
                Node newRoot = new Node(totalNumberOfKeys, false, null);
                newRoot.push_back(virtualNode.getKey(split_i), 0);

                newRoot.setChildNode(targetNode, 0); newRoot.setChildNode(rightNode, 1);
                targetNode.setParent(newRoot); rightNode.setParent(newRoot);
                root = newRoot;
            } else {
                internalNodeInsert(targetNode.getParent(), rightNode, virtualNode.getKey(split_i));
            }
        }

    }

    //leafNode 위의 Node도 다차서 parent node를 또 쪼개야할때 재귀적으로 호출하는 함수
    public void internalNodeInsert(Node parentNode, Node childNode, int inputKey){

        if(parentNode.getCurrentNumberOfKeys() < totalNumberOfKeys){
            parentNode.push_back(inputKey, 0);
            parentNode.setChildNode(childNode, parentNode.findIndexOfKeyInKeyArray(inputKey) + 1); //TODO:순서 맞는지 (제정신일때) 확인!
            return;
        } else {
            //parent도 자리가 없다.. 쪼개줌
            int split_i = degree % 2 == 0 ? degree / 2 : (int) Math.ceil((double)degree / 2) - 1;

            //virtualNode에 기존 parentNode의 값 복사
            Node virtualNode = new Node(totalNumberOfKeys + 1, false, null);
            int index = parentNode.findIndexOfKeyInKeyArray(inputKey);
            for (int i = 0; i < totalNumberOfKeys; i++) {
                if(i == index){
                    virtualNode.setKey(inputKey, i);
                    virtualNode.setChildNode(null, i + 1);
                } else {
                    virtualNode.setKey(parentNode.getKey(i), i);
                    virtualNode.setChildNode(parentNode.getChildNode(i), i);
                }
            } virtualNode.setChildNode(parentNode.getChildNode(totalNumberOfKeys), totalNumberOfKeys);
            virtualNode.setCurrentNumberOfKeys(parentNode.getCurrentNumberOfKeys() + 1);

            Node rightNode = new Node(totalNumberOfKeys, false, parentNode.getParent()); //parent노드 오른쪽으로 쪼개질 노드(parent의 parent에 붙음)
            for (int i = split_i + 1; i < totalNumberOfKeys + 1; i++) {
                rightNode.push_back(virtualNode.getKey(i), virtualNode.getValue(i));
                if(i < totalNumberOfKeys) parentNode.setKey(0, i - 1);
            }
            parentNode.setCurrentNumberOfKeys(split_i);

            parentNode.setChildNode(childNode, parentNode.findIndexOfKeyInKeyArray(inputKey) + 1);

            //parent Node로 올려주기
            if(parentNode == root){
                //TODO: root인 경우 새로운 root를 파고 그 root의 좌우를 parentNode, rightNode로 설정해주고 각 노드의 부모를 새로운 root로 한다
                return;
            } else {
                internalNodeInsert(parentNode.getParent(), rightNode, virtualNode.getKey(split_i));
            }
        }

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
