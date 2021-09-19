import java.io.*;

public class BPlusTree{

    private final int degree;
    private final int totalNumberOfKeys;
    private Node root;
    private final int divide_i;
    private final int minNumberOfKeys;

    public BPlusTree(int degree, Node root) {
        this.degree = degree;
        this.totalNumberOfKeys = degree - 1;
        this.divide_i = degree % 2 == 0 ? degree / 2 : (int) Math.ceil((double) degree / 2) - 1;
        this.minNumberOfKeys = (int) Math.ceil((double) degree / 2) - 1;
        this.root = root;
    }

    private Node singleKeySearchNode(int target, boolean showNodeKey) {
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

    private Node singleKeySearchInternalNode(int target) {
        Node tmpNode = root;
        while (!tmpNode.isLeaf()) {
            // 현재 노드가 가진 key 값에 target 값이 있는지 찾아본다
            int i = tmpNode.findIndexOfKeyInKeyArray(target);
            if (i < tmpNode.getCurrentNumberOfKeys() && target == tmpNode.getKey(i))
                return tmpNode;
            else tmpNode = tmpNode.getChildNode(i);

        }
        return tmpNode;
    }

    public void singleKeySearch(int target) {

        if (root.getCurrentNumberOfKeys() == 0) {
            System.out.println("root is empty");
            return;
        }
        Node tmpNode = singleKeySearchNode(target, true);

        //leaf 도달
        int i = tmpNode.findIndexOfKeyInKeyArray(target);

        if (i < tmpNode.getCurrentNumberOfKeys() && target == tmpNode.getKey(i)) {
            System.out.println(tmpNode.getValue(i));
        } else {
            System.out.println("NOT FOUND");
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
            } else {
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
    private void internalNodeInsert(Node parentNode, Node childNode, int inputKey) {
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

    public void delete(int inputDeleteKey) {

        Node searchLeafNode = singleKeySearchNode(inputDeleteKey, false);
        int target_i = searchLeafNode.findIndexOfKeyInKeyArray(inputDeleteKey);
        target_i = target_i >= searchLeafNode.getCurrentNumberOfKeys() ? target_i - 1 : target_i;
        //예외 처리1: 트리가 빈 경우
        if (searchLeafNode.getCurrentNumberOfKeys() == 0) {
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

        if (searchLeafNode.getCurrentNumberOfKeys() > minNumberOfKeys) {
            //key가 많은 경우
            if (inputDeleteKey == searchLeafNode.getKey(0)) {

                searchLeafNode.push_out(inputDeleteKey);
                Node internalNode = singleKeySearchInternalNode(inputDeleteKey);
                if (!internalNode.isLeaf()) { //최솟값지우는경우 예외처리, leaf의 [0]은 지우고, internalNode자리에는 leaf의 [1]번째 key를 넣어주면됨!([0]부터 지웠다면 [0]
                    int indexInInternalNode = internalNode.findIndexOfKeyInKeyArray(inputDeleteKey);
                    indexInInternalNode = indexInInternalNode >= internalNode.getCurrentNumberOfKeys() ? indexInInternalNode - 1 : indexInInternalNode;
                    internalNode.setKey(searchLeafNode.getKey(0), indexInInternalNode);
                }
            } else{

                searchLeafNode.push_out(inputDeleteKey);
            }
        } else {
            //key가 적은 경우
            //sibling node에서 빌려올 수 있는지 확인

            if (!borrowFromSiblingNode(searchLeafNode, inputDeleteKey)) { //빌릴수 있으면 조건문 함수내에서 빌린거 정리싹다함
                //빌릴 수 없으면 merge
                merge(searchLeafNode, inputDeleteKey);
            }
        }

    }

    //mainNode에는 key가 divide_i만큼 있음(그래서 원래는 더이상 못뺌)
    private boolean borrowFromSiblingNode(Node mainNode, int deleteKey) {
        //빌릴 수 있으면 빌려서 연산 다끝내고 true 반환, 못빌리면 false 반환함
        boolean isZero = mainNode.findIndexOfKeyInKeyArray(deleteKey) == 0;
        int indexOfChildInParentNode = mainNode.getParent().findIndexOfChild(deleteKey);

        if (indexOfChildInParentNode > 0 && mainNode.getParent().getChildNode(indexOfChildInParentNode - 1).getCurrentNumberOfKeys() > minNumberOfKeys) { //left sibling이 key를 빌려줄 수 있음
            Node siblingNode = mainNode.getParent().getChildNode(indexOfChildInParentNode - 1);
            if (isZero) {
                Node internalNode = singleKeySearchInternalNode(deleteKey);
                internalNode.setKey(siblingNode.getKey(siblingNode.getCurrentNumberOfKeys() - 1), internalNode.findIndexOfKeyInKeyArray(deleteKey));
            }

            mainNode.push_out(deleteKey);
            mainNode.push(siblingNode.getKey(siblingNode.getCurrentNumberOfKeys() - 1), siblingNode.getValue(siblingNode.getCurrentNumberOfKeys() - 1));
            siblingNode.push_out(siblingNode.getKey(siblingNode.getCurrentNumberOfKeys() - 1));

            mainNode.getParent().setKey(mainNode.getKey(0), indexOfChildInParentNode - 1);
        } else if (indexOfChildInParentNode < mainNode.getParent().getCurrentNumberOfKeys() && mainNode.getParent().getChildNode(indexOfChildInParentNode + 1).getCurrentNumberOfKeys() > minNumberOfKeys) { //right sibiling 존재, 빌려올 수 있음

            Node siblingNode = mainNode.getParent().getChildNode(indexOfChildInParentNode + 1);

            if (isZero) {
                Node internalNode = singleKeySearchInternalNode(deleteKey);
                if (degree < 4)
                    internalNode.setKey(siblingNode.getKey(0), internalNode.findIndexOfKeyInKeyArray(deleteKey));
                else internalNode.setKey(mainNode.getKey(1), internalNode.findIndexOfKeyInKeyArray(deleteKey));
            }

            mainNode.push_out(deleteKey);
            mainNode.push(siblingNode.getKey(0), siblingNode.getValue(0));
            siblingNode.push_out(siblingNode.getKey(0)); // leaf라서 ㄱㅊ

            mainNode.getParent().setKey(siblingNode.getKey(0), indexOfChildInParentNode);
        } else return false;

        return true;
    }

    //borrow가 안되는 경우 실행됨
    private void merge(Node mainDeleteNode, int deleteKey) {
        //merge될때, 왼쪽 기준이 살아남음
        boolean isZero = mainDeleteNode.findIndexOfKeyInKeyArray(deleteKey) == 0;
        int indexOfChildInParentNode = mainDeleteNode.getParent().findIndexOfChild(deleteKey);
        Node siblingNode;

        //예외 처리: degree가 3이라서 key 빼면 node가 실종되는 경우
        if (degree <= 4) {
            if (indexOfChildInParentNode == 0) {
                //오른쪽에 merge: sibling이 사라짐
                siblingNode = mainDeleteNode.getParent().getChildNode(indexOfChildInParentNode + 1);
                Node internalNode = singleKeySearchInternalNode(deleteKey);
                if (!internalNode.isLeaf()) {
                    internalNode.setKey(siblingNode.getKey(0), internalNode.findIndexOfKeyInKeyArray(deleteKey));
                }
                mainDeleteNode.push_out(deleteKey);
                for (int i = 0; i < siblingNode.getCurrentNumberOfKeys(); i++)
                    mainDeleteNode.push(siblingNode.getKey(i), siblingNode.getValue(i));
                mainDeleteNode.getParent().push_out(siblingNode.getKey(0));
            } else {
                //왼쪽에 merge: mainDeleteNode가 사라짐
                siblingNode = mainDeleteNode.getParent().getChildNode(indexOfChildInParentNode - 1);
                mainDeleteNode.push_out(deleteKey);
                for (int i = 0; i < mainDeleteNode.getCurrentNumberOfKeys(); i++)
                    siblingNode.push(mainDeleteNode.getKey(i), mainDeleteNode.getValue(i));
                mainDeleteNode.getParent().push_out(deleteKey);
            }
        }

        //degree >= 4인 경우
        else {
            if (indexOfChildInParentNode == 0) {
                //오른쪽에 merge: sibling이 사라짐
                siblingNode = mainDeleteNode.getParent().getChildNode(indexOfChildInParentNode + 1);
                if (isZero) {
                    //internal Node에 해당하는 deleteKey순서의 값 변경
                    Node internalNode = singleKeySearchInternalNode(deleteKey);
                    //예외처리: 최솟값을 지우는 경우 - 필요없나?
                    if (!internalNode.isLeaf()) {
                        internalNode.setKey(mainDeleteNode.getKey(1), internalNode.findIndexOfKeyInKeyArray(deleteKey));
                    }
                }
                mainDeleteNode.push_out(deleteKey);
                for (int i = 0; i < siblingNode.getCurrentNumberOfKeys(); i++)
                    mainDeleteNode.push(siblingNode.getKey(i), siblingNode.getValue(i));
                mainDeleteNode.getParent().push_out(siblingNode.getKey(0));
            } else {
                //왼쪽에 merge
                siblingNode = mainDeleteNode.getParent().getChildNode(indexOfChildInParentNode - 1);
                if (isZero) {
                    //최솟값이 아닌 deleteKey, internal Node에서 해당하는 deleteKey 순서의 값 변경
                    Node internalNode = singleKeySearchInternalNode(deleteKey);
                    internalNode.setKey(mainDeleteNode.getKey(1), internalNode.findIndexOfKeyInKeyArray(deleteKey));
                }
                mainDeleteNode.push_out(deleteKey);
                for (int i = 0; i < mainDeleteNode.getCurrentNumberOfKeys(); i++)
                    siblingNode.push(mainDeleteNode.getKey(i), mainDeleteNode.getValue(i));
                mainDeleteNode.getParent().push_out(mainDeleteNode.getKey(0));

            }
        }
        if(mainDeleteNode.getParent() == root){
            if(root.getCurrentNumberOfKeys() < 1) root = indexOfChildInParentNode == 0 ? mainDeleteNode : siblingNode;
            return;
        }
        if (mainDeleteNode.getParent().getCurrentNumberOfKeys() < minNumberOfKeys) {
            int minKey = indexOfChildInParentNode == 0 ? mainDeleteNode.getKey(0) : siblingNode.getKey(0);
            redistribute(mainDeleteNode.getParent(), minKey);
        }
    }

    public void internalMerge(Node mainDeleteNode, int leastkey) {
        int indexOfChildInParentNode = mainDeleteNode.getParent().findIndexOfChild(leastkey);

        Node siblingNode;

        //degree >= 4인 경우
        if (indexOfChildInParentNode == 0) {
            //오른쪽에 merge: sibling이 사라짐
            siblingNode = mainDeleteNode.getParent().getChildNode(1);
            mainDeleteNode.push(mainDeleteNode.getParent().getKey(0), 0);
            mainDeleteNode.getParent().push_out(mainDeleteNode.getParent().getKey(0));
            for (int i = 0; i < siblingNode.getCurrentNumberOfKeys(); i++) {
                mainDeleteNode.setKey(siblingNode.getKey(i), mainDeleteNode.getCurrentNumberOfKeys());
                mainDeleteNode.setChildNode(siblingNode.getChildNode(i), mainDeleteNode.getCurrentNumberOfKeys());
                mainDeleteNode.getChildNode(mainDeleteNode.getCurrentNumberOfKeys()).setParent(mainDeleteNode);
                mainDeleteNode.setCurrentNumberOfKeys(mainDeleteNode.getCurrentNumberOfKeys() + 1);
            }
            mainDeleteNode.setChildNode(siblingNode.getChildNode(siblingNode.getCurrentNumberOfKeys()), mainDeleteNode.getCurrentNumberOfKeys());
            mainDeleteNode.getChildNode(mainDeleteNode.getCurrentNumberOfKeys()).setParent(mainDeleteNode);
        } else {
            //왼쪽에 merge
            siblingNode = mainDeleteNode.getParent().getChildNode(indexOfChildInParentNode - 1);
            siblingNode.push(siblingNode.getParent().getKey(indexOfChildInParentNode - 1), 0);
            mainDeleteNode.getParent().push_out(mainDeleteNode.getParent().getKey(indexOfChildInParentNode - 1));
            for (int i = 0; i < mainDeleteNode.getCurrentNumberOfKeys(); i++) {
                siblingNode.setKey(mainDeleteNode.getKey(i), siblingNode.getCurrentNumberOfKeys());
                siblingNode.setChildNode(mainDeleteNode.getChildNode(i), siblingNode.getCurrentNumberOfKeys());
                siblingNode.getChildNode(siblingNode.getCurrentNumberOfKeys()).setParent(siblingNode);
                siblingNode.setCurrentNumberOfKeys(siblingNode.getCurrentNumberOfKeys() + 1);
            }
            siblingNode.setChildNode(mainDeleteNode.getChildNode(mainDeleteNode.getCurrentNumberOfKeys()), siblingNode.getCurrentNumberOfKeys());
            siblingNode.getChildNode(siblingNode.getCurrentNumberOfKeys()).setParent(siblingNode);

        }

        if (mainDeleteNode.getParent() == root) {
            if (root.getCurrentNumberOfKeys() < 1) root = indexOfChildInParentNode == 0 ? mainDeleteNode : siblingNode;
            return;
        }

        if (mainDeleteNode.getParent().getCurrentNumberOfKeys() < minNumberOfKeys) {
            int minKey = indexOfChildInParentNode == 0 ? mainDeleteNode.getKey(0) : siblingNode.getKey(0);
            redistribute(mainDeleteNode.getParent(), minKey);
        }
    }

    private void redistribute(Node mainNode, int deleteKey) {
        deleteKey = mainNode.getCurrentNumberOfKeys() == 0 ? deleteKey : mainNode.getKey(0);
        Node parentNode = mainNode.getParent();
        int indexInParentNode = parentNode.findIndexOfChild(deleteKey);

        // => parentNode 밑은 정리 완료된 채로 왔음!

        if (indexInParentNode > 0 && parentNode.getChildNode(indexInParentNode - 1).getCurrentNumberOfKeys() > minNumberOfKeys) {
            Node siblingNode = parentNode.getChildNode(indexInParentNode - 1);
            int indexInParentKeyArrays = parentNode.findSmallIndexOfKeyInKeys(deleteKey);
            indexInParentKeyArrays = indexInParentKeyArrays >= parentNode.getCurrentNumberOfKeys() ? indexInParentKeyArrays - 1 : indexInParentKeyArrays;

            mainNode.push(parentNode.getKey(indexInParentKeyArrays), 0);
            mainNode.setChildNode(mainNode.getChildNode(0), 1);
            Node moveNode = siblingNode.getChildNode(siblingNode.getCurrentNumberOfKeys());
            moveNode.setParent(mainNode);
            mainNode.setChildNode(moveNode, 0);

            parentNode.setKey(siblingNode.getKey(siblingNode.getCurrentNumberOfKeys() - 1), indexInParentKeyArrays);

            siblingNode.setChildNode(null, siblingNode.getCurrentNumberOfKeys());
            siblingNode.setKey(0, siblingNode.getCurrentNumberOfKeys() - 1);
            siblingNode.setCurrentNumberOfKeys(siblingNode.getCurrentNumberOfKeys() - 1);

        } else if (indexInParentNode < parentNode.getCurrentNumberOfKeys() && parentNode.getChildNode(indexInParentNode + 1).getCurrentNumberOfKeys() > minNumberOfKeys) {

            Node siblingNode = parentNode.getChildNode(indexInParentNode + 1);

            int indexInParentKeyArrays = parentNode.findIndexOfKeyInKeyArray(deleteKey);
            indexInParentKeyArrays = indexInParentKeyArrays >= parentNode.getCurrentNumberOfKeys() ? indexInParentKeyArrays - 1 : indexInParentKeyArrays;


            mainNode.push(parentNode.getKey(indexInParentKeyArrays), 0);
            parentNode.setKey(siblingNode.getKey(0), indexInParentKeyArrays);
            mainNode.setChildNode(siblingNode.getChildNode(0), mainNode.getCurrentNumberOfKeys());
            siblingNode.getChildNode(0).setParent(mainNode);

            for (int i = 0; i < siblingNode.getCurrentNumberOfKeys() - 1; i++) {
                siblingNode.setKey(siblingNode.getKey(i + 1), i);
                siblingNode.setChildNode(siblingNode.getChildNode(i + 1), i);
            } siblingNode.setChildNode(siblingNode.getChildNode(siblingNode.getCurrentNumberOfKeys()), siblingNode.getCurrentNumberOfKeys() - 1);
            siblingNode.setChildNode(null, siblingNode.getCurrentNumberOfKeys());
            siblingNode.setCurrentNumberOfKeys(siblingNode.getCurrentNumberOfKeys() - 1);

        }
        //빌릴수없는경우..
        else {
            internalMerge(mainNode, deleteKey); //이거 좌우만 지정해주면 되는거아닌가?

        }


    }

    public void saveTree(String indexFile){
        try {
            FileWriter fw = new FileWriter(indexFile);
            fw.write(degree + "\n");

            saveNode(fw, root);

            fw.flush();
            fw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void saveNode(FileWriter fw, Node node){ //트리 순회
        try {

            if(node.isLeaf()){
                //leaf node 저장
                fw.write(1 + " / ");
                for (int i = 0; i < node.getCurrentNumberOfKeys(); i++) {
                    fw.write(node.getKey(i) + " " + node.getValue(i) + " / ");
                }
                fw.write("\n");
            } else {
                //leaf가 아닌 노드 저장
                fw.write(0 + " / ");
                for (int i = 0; i < node.getCurrentNumberOfKeys(); i++) {
                    fw.write(node.getKey(i) + " / ");
                }
                fw.write("\n");
                for (int i = 0; i <= node.getCurrentNumberOfKeys(); i++) {
                    saveNode(fw, node.getChildNode(i));
                }
                //fw.write("#\n");

            }


        } catch (IOException e) {
            e.printStackTrace();
        }
    }



}



