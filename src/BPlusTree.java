public class BPlusTree {
    /**
     * degree: max degree(m-way)
     * <p>
     * insert: 넣는거 시도하는데 성공적으로 넣어지면 1, 안되면 0을 반환하는 기본 함수로 일단 넣음 나머지 쪼개는거는 비쁠트리 차원에서
     */

    private final int degree;
    private final int totalNumberOfKeys;
    private Node root;
    private final int divide_i;

    public BPlusTree(int degree) {
        this.degree = degree;
        this.totalNumberOfKeys = degree - 1;
        this.divide_i = degree % 2 == 0 ? degree / 2 : (int) Math.ceil((double) degree / 2) - 1;
        root = new Node(totalNumberOfKeys, true, null);
    }

    public Node singleKeySearchNode(int target, boolean showNodeKey) {
        Node tmpNode = root;
        while (!tmpNode.isLeaf()) {
            if (showNodeKey) tmpNode.showKeys();

            // 현재 노드가 가진 key 값에 target 값이 있는지 찾아본다
            int i = tmpNode.findIndexOfKeyInKeyArray(target);
            if (i < tmpNode.getCurrentNumberOfKeys() && target == tmpNode.getKey(i))
                tmpNode = tmpNode.getChildNode(i + 1);
            else tmpNode = tmpNode.getChildNode(i);

        }
        return tmpNode;
    }

    public Node singleKeySearchInternalNode(int target, boolean showNodeKey) {
        Node tmpNode = root;
        while (!tmpNode.isLeaf()) {
            if (showNodeKey) tmpNode.showKeys();

            // 현재 노드가 가진 key 값에 target 값이 있는지 찾아본다
            int i = tmpNode.findIndexOfKeyInKeyArray(target);
            if (i < tmpNode.getCurrentNumberOfKeys() && target == tmpNode.getKey(i))
                return tmpNode;
            else tmpNode = tmpNode.getChildNode(i);

        }
        return tmpNode;
    }

    public void singleKeySearch(int target) {
        /**
         * root부터 탐색 시작
         * 동일한 값이 있을 때 return
         * 사이값일 경우 그 사이 노드로 들어감
         *
         * 찾으면 해당하는 value 반환
         */
        if (root.getCurrentNumberOfKeys() == 0){
            System.out.println("root is empty");
            return;
        }
        Node tmpNode = singleKeySearchNode(target, true);

        //leaf 도달
        int i = tmpNode.findIndexOfKeyInKeyArray(target);

        if (i < tmpNode.getCurrentNumberOfKeys() && target == tmpNode.getKey(i)){
            System.out.println(tmpNode.getValue(i));
        }
        else{
            System.out.println("NOT FOUND");
            return;
        }
    }

    public void rangeSearch(int startTarget, int endTarget) {
        Node startNode = singleKeySearchNode(startTarget, false);
        Node endNode = singleKeySearchNode(endTarget, false);
        int startNode_i = startNode.findIndexOfKeyInKeyArray(startTarget);

        if (startNode_i >= startNode.getCurrentNumberOfKeys()) {
            if (startNode == endNode) {
                System.out.println("NOT FOUND");
                return;
            }
            else {
                startNode = startNode.getChildNode(0);
                startNode_i = 0;
            }
        } else if (startNode.getKey(startNode_i) > endTarget) {
            System.out.println("NOT FOUND");
            return;
        }

        while (startNode != endNode) {
            for (int i = startNode_i; i < startNode.getCurrentNumberOfKeys(); i++) {
                System.out.println(startNode.getKey(i) + "," + startNode.getValue(i));
            }
            startNode_i = 0;
            startNode = startNode.getChildNode(0);
        }

        for (int i = startNode_i; i < startNode.getCurrentNumberOfKeys() && startNode.getKey(i) <= endTarget; i++) {
            System.out.println(startNode.getKey(i) + "," + startNode.getValue(i));
        }
    }

    public void insert(int inputKey, int inputValue) {
        //중복 key는 들어오지 않는다
        Node targetNode = singleKeySearchNode(inputKey, false); //해당하는 key가 들어갈 leaf Node를 찾아준다

        if (targetNode.getCurrentNumberOfKeys() < totalNumberOfKeys) {
            targetNode.push(inputKey, inputValue);
        } else {
            //split
            /**
             * 1. (totalNumberOfKeys + 1)의 Node를 하나 생성, targetNode의 '값'만 복사해줌 (주소 X) ->virtualNode
             *      leftNode랑 .. 기타등등
             * 2. inputKey, inputValue 쌍을 넣어줌 push_back해줌
             * 3. rightChildNode(new로 동적할당) -> longTmpNode의 [divide_i] 이후의 값을 대입
             * 4. targetNode의 rightNode가 rightChildNode를 가리키게 함 #targetNode가 root인지 확인!!(5와 예외처리)
             * 5. virutalNode의 keys[divide_i]를 parent node에 internalNodeInsert()함
             */

            Node virtualNode = new Node(totalNumberOfKeys + 1, true, null);

            //복사 및 split
            for (int i = 0; i < totalNumberOfKeys; i++) {
                virtualNode.push(targetNode.getKey(i), targetNode.getValue(i));
                targetNode.setKey(0, i);
                targetNode.setValue(0, i);
            }
            virtualNode.push(inputKey, inputValue); //하나 남는 자리에 inputKey와 inputValue 넣어줌
            virtualNode.setChildNode(targetNode.getChildNode(0), 0);
            targetNode.setCurrentNumberOfKeys(0);

            for (int i = 0; i < divide_i; i++) {
                targetNode.push(virtualNode.getKey(i), virtualNode.getValue(i));
            }

            Node rightNode = new Node(totalNumberOfKeys, true, targetNode.getParent());

            for (int i = divide_i; i < totalNumberOfKeys + 1; i++) {
                rightNode.push(virtualNode.getKey(i), virtualNode.getValue(i));
            }
            rightNode.setChildNode(virtualNode.getChildNode(0), 0);
            targetNode.setChildNode(rightNode, 0);
            //분할 완료

            //parent Node로 올려주기
            if (targetNode == root) {
                Node newRoot = new Node(totalNumberOfKeys, false, null);
                newRoot.push(virtualNode.getKey(divide_i), 0);

                newRoot.setChildNode(targetNode, 0);
                newRoot.setChildNode(rightNode, 1);
                targetNode.setParent(newRoot);
                rightNode.setParent(newRoot);
                root = newRoot;
            } else {
                internalNodeInsert(targetNode.getParent(), rightNode, virtualNode.getKey(divide_i));
            }
        }
    }

    //leafNode 위의 Node도 다차서 parent node를 또 쪼개야할때 재귀적으로 호출하는 함수
    public void internalNodeInsert(Node parentNode, Node childNode, int inputKey) {
        if (parentNode.getCurrentNumberOfKeys() < totalNumberOfKeys) {
            parentNode.push(inputKey, 0);

            int num = parentNode.findIndexOfKeyInKeyArray(inputKey);
            num = num >= parentNode.getCurrentNumberOfKeys() ? num - 1 : num;
            parentNode.setChildNode(childNode, num + 1);

            return;
        } else {
            //parent도 자리가 없다.. 쪼개줌
            int split_i = degree % 2 == 0 ? degree / 2 : (int) Math.ceil((double) degree / 2) - 1;

            //virtualNode에 기존 parentNode의 값 복사
            Node virtualNode = new Node(totalNumberOfKeys + 1, false, null);
            for (int i = 0; i < totalNumberOfKeys; i++) {
                virtualNode.setKey(parentNode.getKey(i), i);
                virtualNode.setChildNode(parentNode.getChildNode(i), i);
                parentNode.setKey(0, i);
                parentNode.setChildNode(null, i); //삽입한 parentNode는 초기화 해준다.
            }
            virtualNode.setChildNode(parentNode.getChildNode(totalNumberOfKeys), totalNumberOfKeys);
            virtualNode.setCurrentNumberOfKeys(parentNode.getCurrentNumberOfKeys());
            virtualNode.push(inputKey, 0);
            int num = virtualNode.findIndexOfKeyInKeyArray(inputKey);
            num = num >= virtualNode.getCurrentNumberOfKeys() ? num - 1 : num;

            virtualNode.setChildNode(childNode, num + 1); //넣어야할 곳(push_back에서 자리를 만들어줌)에 삽입해준다.
            parentNode.setCurrentNumberOfKeys(0);

            Node rightNode = new Node(totalNumberOfKeys, false, parentNode.getParent()); //parent노드 오른쪽으로 쪼개질 노드(parent의 parent에 붙음)
            for (int i = 0; i < split_i; i++) {
                parentNode.setKey(virtualNode.getKey(i), i);
                parentNode.setChildNode(virtualNode.getChildNode(i), i);
            }
            parentNode.setChildNode(virtualNode.getChildNode(split_i), split_i);
            parentNode.setCurrentNumberOfKeys(split_i);

            for (int i = split_i + 1; i < totalNumberOfKeys + 1; i++) {
                //rightNode에 virtualNode[split_i + 1] 부터 key랑 node 복사(leaf가 아님!)
                //parentNode의 [split_i]부터 key랑 node 초기화
                rightNode.setKey(virtualNode.getKey(i), i - split_i - 1);
                rightNode.setChildNode(virtualNode.getChildNode(i), i - split_i - 1);
            }
            rightNode.setChildNode(virtualNode.getChildNode(totalNumberOfKeys + 1), totalNumberOfKeys - split_i);
            rightNode.setCurrentNumberOfKeys(totalNumberOfKeys - split_i);

            for (int i = 0; i <= rightNode.getCurrentNumberOfKeys(); i++) {
                if (rightNode.getChildNode(i) != null) rightNode.getChildNode(i).setParent(rightNode);
            }
            //parent Node로 올려주기
            if (parentNode == root) {
                Node newRoot = new Node(totalNumberOfKeys, false, null);
                newRoot.push(virtualNode.getKey(split_i), 0);

                newRoot.setChildNode(parentNode, 0);
                newRoot.setChildNode(rightNode, 1);
                parentNode.setParent(newRoot);
                rightNode.setParent(newRoot);
                root = newRoot;
                return;
            } else {
                internalNodeInsert(parentNode.getParent(), rightNode, virtualNode.getKey(split_i));
            }
        }

    }

    public void delete(int inputDeleteKey){
        /**
         * 1. B+ Tree index에서 key 값을 검색하여 데이터의 위치를 찾아 데이터베이스에서 데이터를 삭제한다.
         * 2. B+ Tree index에서 삭제한 데이터의 (pointer, key) 쌍을 삭제한다.
         * 3-1. 삭제 후 entry가 얼마 남지 않았고, 형제 노드와 merge가 가능한 경우 merge하여 하나의 노드로 만든다.
         * 3-2. 삭제 후 entry가 얼마 남지 않았지만, 형제 노드와 merge가 불가능한 경우 redistribute한다.
         * 4. parent 노드에서도 (pointer, key) 쌍을 삭제한 뒤 3-1과 3-2를 반복한다.
         */
        Node searchLeafNode = singleKeySearchNode(inputDeleteKey, false);
        int target_i = searchLeafNode.findIndexOfKeyInKeyArray(inputDeleteKey);

        //예외 처리1: 트리가 빈 경우
        if(searchLeafNode.getCurrentNumberOfKeys() == 0) {
            System.out.println("The tree is empty.");
            return;
        }
        
        //예외 처리2: 트리에 해당하는 노드가 없는 경우
        if (inputDeleteKey != searchLeafNode.getKey(target_i) || target_i >= searchLeafNode.getCurrentNumberOfKeys()) {
            System.out.println("There is no " + inputDeleteKey + " in the b+tree.");
            return;
        }

        //예외 처리3: 찾은 leaf node가 root인 경우(root만 있는 경우) -> divide_i와의 비교가 필요없기때문에 함수 종료
        if (searchLeafNode == root) { 
            searchLeafNode.push_out(inputDeleteKey);
            return;
        }

        if(searchLeafNode.getCurrentNumberOfKeys() > divide_i){
            //key가 많은 경우
            if (inputDeleteKey == searchLeafNode.getKey(0)) {
                searchLeafNode.push_out(inputDeleteKey);
                Node internalNode = singleKeySearchInternalNode(inputDeleteKey, false);
                if(!internalNode.isLeaf()) { //최솟값지우는경우 예외처리, leaf의 [0]은 지우고, internalNode자리에는 leaf의 [1]번째 key를 넣어주면됨!([0]부터 지웠다면 [0]
                    int indexInInternalNode = internalNode.findIndexOfKeyInKeyArray(inputDeleteKey);
                    indexInInternalNode = indexInInternalNode >= internalNode.getCurrentNumberOfKeys() ? indexInInternalNode - 1 : indexInInternalNode;
                    internalNode.setKey(searchLeafNode.getKey(0), indexInInternalNode);
                }
            }
            else searchLeafNode.push_out(inputDeleteKey);
        } else {
            //key가 적은 경우
            //sibling node에서 빌려올 수 있는지 확인
            if(!borrowFromSiblingNode(searchLeafNode, inputDeleteKey)){ //빌릴수 있으면 조건문 함수내에서 빌린거 정리싹다함
                //빌릴 수 없으면 merge
                merge(searchLeafNode, inputDeleteKey);
            }
        }

    }

    public boolean borrowFromSiblingNode(Node mainNode, int deleteKey){
        //빌릴 수 있으면 빌려서 연산 다끝내고 true 반환, 못빌리면 false 반환함
        boolean isZero = mainNode.findIndexOfKeyInKeyArray(deleteKey) == 0;
        int indexInParentNode = mainNode.getParent().findIndexOfKeyInKeyArray(deleteKey);
        if (indexInParentNode > 0 && mainNode.getParent().getChildNode(indexInParentNode - 1).getCurrentNumberOfKeys() > divide_i) { //left sibling이 key를 빌려줄 수 있음
            Node siblingNode = mainNode.getParent().getChildNode(indexInParentNode - 1);

            mainNode.push_out(deleteKey);
            mainNode.push(siblingNode.getKey(siblingNode.getCurrentNumberOfKeys() - 1), siblingNode.getValue(siblingNode.getCurrentNumberOfKeys() - 1));
            siblingNode.push_out(siblingNode.getKey(siblingNode.getCurrentNumberOfKeys() - 1));

            indexInParentNode = indexInParentNode >= mainNode.getParent().getCurrentNumberOfKeys() ? indexInParentNode - 1 : indexInParentNode;
            mainNode.getParent().setKey(mainNode.getKey(0), indexInParentNode);

            if(isZero){
                Node internalNode = singleKeySearchInternalNode(deleteKey, false);
                internalNode.setKey(mainNode.getKey(0), internalNode.findIndexOfKeyInKeyArray(deleteKey));
            }

        } else if (indexInParentNode < mainNode.getParent().getCurrentNumberOfKeys() && mainNode.getParent().getChildNode(indexInParentNode + 1).getCurrentNumberOfKeys() > divide_i) { //right sibiling 존재, 빌려올 수 있음
            Node siblingNode = mainNode.getParent().getChildNode(indexInParentNode + 1);

            mainNode.push_out(deleteKey);
            mainNode.push(siblingNode.getKey(0), siblingNode.getValue(0));
            siblingNode.push_out(siblingNode.getKey(0));
            indexInParentNode = indexInParentNode >= mainNode.getParent().getCurrentNumberOfKeys() ? indexInParentNode - 1 : indexInParentNode;
            mainNode.getParent().setKey(siblingNode.getKey(0), indexInParentNode);

            if(isZero){
                Node internalNode = singleKeySearchInternalNode(deleteKey, false);
                internalNode.setKey(mainNode.getKey(0), internalNode.findIndexOfKeyInKeyArray(deleteKey));
            }

        } else return false;
        return true;
    }

    public void merge(Node mainDeleteNode, int deleteKey){
        //TODO:merge 구현
        /**
         * 아.. 잠시만 와다다 정리해봄
         * 남은 key가 0인 경우(즉 mainDeleteNode의 currentNumberOfKeys가 1인 경우)는 바로 redistribute로 넘겨줌! 아래 과정 처리안됨
         *
         *  1. merge가 가능한 경우: 부모 노드가 divide_i 초과
         *   merge가 가능한데 [0]을 삭제하는 경우, internalNode에 해당하는 값을 [0]을 지운 노드의 [1]번째로 set해준다 (push_out으로 꺼낸 다음인 경우 [0])
         *   - 기본적으로 왼쪽 노드에 merge함
         *      siblingNode = getParent.childNode[indexInParentNode - 1]
         *      (1) isZero인 경우, mainDeleteNode의 [1]번째 값을 internal Node에 알맞은 위치에(본래 deleteKey가 위치해야했던 곳) set해줌 -> 이 internal이 root여도 상관없는게 지우는게 아니라 값을 바꿔치기 하는거임
         *      (2) mainDeleteNode에서 deleteKey를 push_out해줌
         *      (3) 왼쪽 siblingNode에 있던 애들을 전부 push_out하고 mainDeleteNode로 push
         *      (4) parentNode의 keys에서 mainDeleteNode의 keys[currentNumberOfKeys - 1]값을 push_out
         *
         *   - 오른쪽으로 merge하는 경우(왼쪽에 형제 노드가 더 없는 경우!! 예외 처리처럼 해줌)
         *      siblingNode = getParent.childNode[indexInParentNode + 1]
         *      (1) isZero인 경우 mainDeleteNode의 [1]번째 값을 internal Node에 알맞은 위치에(본래 deleteKey가 위치해야했던 곳) set해줌
         *      (1) mainDeleteNode에서 deleteKey를 push_out해줌
         *      (2) 빼고나서 mainDeleteNode에 key가 없으면(0개면) getParent.childNode[indexInParentNode + 1]에 mainDeleteKey에 남은 값 push하는거 관둠(해야되면 반복문으로 끝까지 push)
         *      (3) parentNode의 keys에서 siblingNode의 [0]번째 key값을 push_out해줌
         *      
         *  2. merge 안되면 부모 노드가 부모의 sibling에서 빌려올수있는지 확인 //TODO
         */
        boolean isZero = mainDeleteNode.findIndexOfKeyInKeyArray(deleteKey) == 0;
        int indexInParentNode = mainDeleteNode.getParent().findIndexOfKeyInKeyArray(deleteKey); //TODO: parent가 null인 경우 예외 처리

        if(mainDeleteNode.getParent().getCurrentNumberOfKeys() > divide_i){
            if(indexInParentNode == 0){
                //오른쪽 형제 노드에 merge
                Node siblingNode = mainDeleteNode.getParent().getChildNode(indexInParentNode + 1);
                if (isZero) {
                    Node internalNode = singleKeySearchInternalNode(deleteKey, false);
                    internalNode.setKey(mainDeleteNode.getKey(1), internalNode.findIndexOfKeyInKeyArray(deleteKey));
                }
                mainDeleteNode.push_out(deleteKey);
                for(int i = 0; i < mainDeleteNode.getCurrentNumberOfKeys(); i++){
                    siblingNode.push(mainDeleteNode.getKey(i), mainDeleteNode.getValue(i));
                }
                siblingNode.getParent().push_out(siblingNode.getKey(0));

            } else {
                //왼쪽 형제 노드에 merge
                Node siblingNode = mainDeleteNode.getParent().getChildNode(indexInParentNode - 1);
                if(isZero){
                    Node internalNode = singleKeySearchInternalNode(deleteKey, false);
                    internalNode.setKey(mainDeleteNode.getKey(1) , internalNode.findIndexOfKeyInKeyArray(deleteKey));
                }
                mainDeleteNode.push_out(deleteKey);

                for(int i = 0; i < siblingNode.getCurrentNumberOfKeys(); i++){
                    mainDeleteNode.push(siblingNode.getKey(i), siblingNode.getValue(i));
                }
                mainDeleteNode.getParent().push_out(mainDeleteNode.getKey(mainDeleteNode.getCurrentNumberOfKeys()) - 1);
            }
        } else {
            //TODO: merge 안되는 경우
            // merge도 boolean으로 넣어서 처리? 아니면 redistribute로 바로 넘겨줌?
        }



    }

    public void redistribute(){
        //TODO:redistribute 구현
    }

    public void showAllLeafKeys() {
        Node tmpNode = singleKeySearchNode(0, false);
        while (tmpNode != null) {
            tmpNode.showKeys();
            tmpNode = tmpNode.getChildNode(0);
        }
    }

}



