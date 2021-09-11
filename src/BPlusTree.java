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
        totalNumberOfKeys = degree - 1;
        root = new Node(degree, true);
    }

    public Node singleKeySearchNode(int target, boolean showNodeKey){
        Node tmpNode = root;

        while (!tmpNode.isLeaf()) {
            if (showNodeKey) tmpNode.showKeys();
            // 현재 노드가 가진 key 값에 target 값이 있는지 찾아본다
            int[] tmpKeys = tmpNode.getKeys();
            int i;
            for (i = 0; i < tmpNode.getCurrentNumberOfKeys() && target > tmpKeys[i]; i++) {
                // 현재 노드가 가진 key의 값의 어느 범위에 들어가는지 확인해본다(i가 그만큼 증가)
            }

            if (i >= tmpNode.getCurrentNumberOfKeys()) tmpNode = tmpNode.getRightNode();
            else if (target == tmpKeys[i]) tmpNode = tmpNode.getLeftNode(i + 1);
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
            int[] tmpKeys = tmpNode.getKeys();
            int i = 0;
            while(i < tmpNode.getCurrentNumberOfKeys() - 1 && target > tmpKeys[i]){ i++; }

            if(target == tmpKeys[i]) return String.valueOf(tmpNode.getValue(i));
            else return "NOT FOUND";
    }

    public Node split(Node superParentNode, int inputIndex){

        /**
         * parentNode: key가 덜 찬 node이자 쪼갠 childNode에서 올라올 key를 받는 node
         * 이거 반복해야하지 않나?
         */
        Node parentNode = superParentNode;

        Node childNode;
       
        //child node 찾기
        int[] parentNodeKeys = parentNode.getKeys();
        int orderInParent;
        for (orderInParent = 0; orderInParent < parentNode.getCurrentNumberOfKeys() && inputIndex > parentNodeKeys[orderInParent]; orderInParent++) {
            // 현재 노드가 가진 key의 값의 어느 범위에 들어가는지 확인해본다(i가 그만큼 증가)
        }
        if (orderInParent >= parentNode.getCurrentNumberOfKeys()) childNode = parentNode.getRightNode();
        else childNode = parentNode.getLeftNode(orderInParent);
        
        //child node 자르기
        int divide_i = (int) (Math.ceil((double)degree / 2) - 1); //배열은 0부터 시작하므로 1더 빼줌, TODO: 안전하게 소수점 자르기

        if(childNode.isLeaf()){
            //childNode가 leaf인 경우, Right child는 new Node(~~)로 동적 할당해준 뒤 값 수정할 것!
            Node rightChildNode = new Node(degree,true);
            /**
             * 1. childNode에 있던 값을 임시 Node에 '값만 복사해서' 저장
             * 2. childNode 2개로 쪼개기(기존 childNode, rightChildNode)
             * <RightChildNode에 대해>
             *     1. leaf = true;
             *     2. currentNumberofNodes = degree - divide_i
             *     3. keys: childNode keys의 [divide_i]이상
             *     4. leftNodes: -
             *     5. rightNodes: childNode의 원래 rightNode
             *     6. value: childNode values의 [divide_i]이상
             *     </RightChildNode에 대해>
             * <ChildNode에 대해>
             *     1. leaf = true;
             *     2. currentNumberofNodes = divide_i
             *     3. keys: 본래 key의 [divide_i] 미만
             *     4. leftNodes: -
             *     5. rightNodes: RightChildNode
             *     6. value: childNode values의 [divide_i]미만
             *     </ChildNode에 대해>
             */
/*
            //key값 복사
            int[] tmpKeys = new int[childNode.getCurrentNumberOfKeys()];
            for (int i = 0; i < childNode.getCurrentNumberOfKeys(); i++) { tmpKeys[i] = childNode.getKey(i); }

            //value값 복사
            int[] tmpValues = new int[childNode.getCurrentNumberOfKeys()];
            for (int i = 0; i < childNode.getCurrentNumberOfKeys(); i++) { tmpValues[i] = childNode.getValue(i); }
*/
            //rightChildNode 값 조정
            rightChildNode.setCurrentNumberOfKeys(degree - divide_i + 1);
            for (int i = divide_i; i < totalNumberOfKeys; i++) {
                rightChildNode.setKey(childNode.getKey(i), i - divide_i);
                rightChildNode.setValue(childNode.getValue(i), i - divide_i);
            }
            rightChildNode.setRightNode(childNode.getRightNode());

            childNode.setCurrentNumberOfKeys(divide_i);
            for (int i = divide_i; i < totalNumberOfKeys; i++) {
                childNode.setKey(0, i);
                childNode.setValue(0, i);
            }
            childNode.setCurrentNumberOfKeys(divide_i - 1);
            childNode.setRightNode(rightChildNode);

            /**
             * 부모 노드의 orderInParent를 기준으로
             */

        } else {
            //childNode가 leaf가 아닌 경우, Right child는 new Node(~~)로 동적 할당해준 뒤 값 수정할 것!
            Node rightChildNode = new Node(degree,false);
            /**
             * 1. childNode를 2개로 쪼개기(기존 childNode, rightChildNode)
             * <RightChildNode에 대해>
             *     1. leaf = false;
             *     2. currentNumberofNodes = degree - divide_i
             *     3. keys: childNode keys의 [divide_i]이상
             *     4. leftNodes: childNode leftNodes의 [divide_i]이상
             *     5. rightNodes: childNode의 원래 rightNode
             *     6. leaf가 아니므로 value는 없음
             *     </RightChildNode에 대해>
             * <ChildNode에 대해>
             *     1. leaf = false;
             *     2. currentNumberofNodes = divide_i
             *     3. keys: 본래 key의 [divide_i] 미만
             *     4. leftNodes: 본래 leftNodes의 [divide_i] 미만
             *     5. rightNodes: RightChildNode
             *     6. leaf가 아니므로 value는 없음
             *     </ChildNode에 대해>
             */

        }
        
        return superParentNode; //굳이 return 할 필요 있나?
    }

    public void insert(int inputIndex){ //중복 key는 들어오지 않는다

        /**
         * search?해서 알맞는 노드까지 감
         * 그 노드에 넣을 자리가 있으면 넣음
         * 넣을 자리가 없으면 쪼개기!
         */
        Node searchedLeafNode = singleKeySearchNode(inputIndex, false);

        if(searchedLeafNode.getCurrentNumberOfKeys() < degree-1){
            searchedLeafNode.push_back(inputIndex);
        } else {
            //TODO: 쪼개는 insert 함수 구현

            //linked list 생성시 올라간 애는 오른쪽으로 쪼개지는 노드에 붙음
            /**
             * <기본 개념>
             * 쪼개지는 애: 0부터 시작한다고 가정, Up(M / 2) - 1
             *     1. 왼쪽으로 쪼개지는 노드: 쪼개지는 arrayIndex 미만의 key를 나눠가짐(기존 노드에서 current Index 수정)
             *      rightNode를 오른쪽으로 쪼개지는 Node 주소로 설정
             *     2. 오른쪽으로 쪼개지는 노드: 쪼개지는 arrayIndex 이상의 key를 나눠가짐
             *
             *     3. ??
             *     이 경우, 1차 쪼개기->노드 남기고 올림 / 2차 쪼개기->안남기고 올림(loop)
             * </기본>
             *
             * <구현개요>
             *     - SuperParentNode: 본래 inputindex를 다시 찾는 과정에서, key가 꽉 차지 않은 node를 찾음, 해당 node를 기준으로 쪼개기 시작
             *     어... 이거 안되네 생각해보니ㅜ 가장 마지막으로 덜찬 노드를 찾아야하는데 가장 마지막인지 아닌지 구분 불가
             *     아!! 일단 inputIndex search를 다시 하면서, SuperParentNode에 key가 꽉차지 않은 node를 계속 복사해줌. 그럼 마지막으로 꽉차지 않은 node발견><
             *     근데 그럴려면 search 함수를 쓰는게 아니라 insert함수 내에 search 과정을 다시 써야할듯!
             *     - 앗...근데 이렇게 superParentNode를 찾아도 해당 node에서 어디로 가야할지 모르니까ㅜ 안되네...
             * </구현개요>
             */

            Node superParentNode = null;
            Node searchTmpNode = root;
            while (!searchTmpNode.isLeaf()) {
                if(searchTmpNode.getCurrentNumberOfKeys() < degree - 1) superParentNode = searchTmpNode; //가장 마지막으로 key가 덜 찬 parent node를 찾는다

                int[] tmpKeys = searchTmpNode.getKeys();
                int i;
                for (i = 0; i < searchTmpNode.getCurrentNumberOfKeys() - 1 && inputIndex > tmpKeys[i]; i++) {
                    // 현재 노드가 가진 key의 값의 어느 범위에 들어가는지 확인해본다(i가 그만큼 증가)
                }


                if (i >= searchTmpNode.getCurrentNumberOfKeys()) searchTmpNode = searchTmpNode.getRightNode();
                else searchTmpNode = searchTmpNode.getLeftNode(i);
            }

            if(superParentNode == null){
                //root도 쪼개야 하는 경우
            } /*else if (superParentNode == root){
                //root에 노드 하나 더 추가하는 경우(경우 나눌 필요 있나?)
            } */else {
                //internal node가 superParentNode인 경우

                //TODO: split함수 구현
            }


        }
    }

    public void delete(){
        //TODO: delete 함수 구현
    }

    public void show(){
        int[] keys = root.getKeys();
        for (int key:keys
             ) {
            System.out.print(key + " ");
        }
        System.out.println();
    }
}
