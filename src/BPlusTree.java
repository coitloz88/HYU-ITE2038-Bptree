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

    public Node findSuperParentNode(Node childLeafNode, int inputkey){
        /**
         * 주의. inputKey는 현재 B+tree 내부에 없는 새로운 key값임(findIndexOfKeyInKeyArray를 정상적으로 사용하기 위함)
         * 
         * <함수개요>
         *     삽입(혹은 삭제?)하려는 index와 해당 index가 있는 leafNode의 주소를 받는다.
         *     쪼갤 수 있는 가장 최하단의 부모 노드를 찾는다.
         *     - return값이 null일 경우: 
         *       1) 모든 노드가 꽉찬 경우, 쪼갤 수 있는 노드 없음, split::newNodeMode = true
         *       2) root를 처음으로 쪼개는 경우
         *     - return값이 root일 경우: root를 쪼개야함(구분해야하나?)
         *     - retur값이 internal node일 경우: split::newNodeMode = false, 평범하게 쪼개면 됨
         * </함수개요>
         */

        Node superParentNode = null;
        Node searchTmpNode = root;
        while (!searchTmpNode.isLeaf()) {
            if (searchTmpNode.getCurrentNumberOfKeys() < totalNumberOfKeys)
                superParentNode = searchTmpNode; //가장 마지막으로 key가 덜 찬 parent node를 찾는다

            int i = searchTmpNode.findIndexOfKeyInKeyArray(inputkey);

            if (i >= searchTmpNode.getCurrentNumberOfKeys()) searchTmpNode = searchTmpNode.getRightNode();
            else searchTmpNode = searchTmpNode.getLeftNode(i);
        }
        return superParentNode;
    };

    //split이 필요한 경우만 받음
    public Node split(Node superParentNode, int inputkey, int inputValue, boolean newNodeMode){
        /**
         * <함수인자>
         *     1. superParentNode: 추가 key(&value)가 삽입 가능하면서 가장 아래인 노드
         *     2. inputIndex: 추가하려는 index
         *     3. inputValue: 추가하려는 value(inputIndex와 쌍)
         *     4. newMode: 맨 처음 root를 쪼개거나(아직 다른 상황은 생각 못해봤는데)..할때 등 동적할당해야할 노드가 2개인 경우 true
         * </함수인자>
         *
         * <함수개요>
         *     +) 지금 insert에 구현되어있는.. superParentNode찾는것도 얘가하면 될듯? 재귀함수가 아니니까 << 이거는 findSuperParent함수로 찾아주자!
         *        삽입하고 싶은 가득찬 노드만 받으면 어디서부터 쪼개면 될지 차자작 계산해서 차자작 쪼개주는 함수!
         *
         *     split이 필요한 경우만 parent node를 받는다. 반복문으로 split이 필요한 경우를 전부 split한다.
         *     bottom-up으로 구현할 경우 parent node를 찾기 힘들기 때문에, 가장 마지막으로 추가 key를 끼울 수 있는 node를 함수 인자로 받아 그 노드의 자식부터 쪼갠다.
         *     # root가 leaf가 아니며 쪼개야하는 경우, root가 leaf이며 쪼개야하는 경우(node 2개 동적할당 필요)는 예외 처리를 해준다.
         *
         *     newMode == false일때
         *     1. parentNode를 superParentNode로 초기화
         *     2. inputIndex-value쌍을 끼워넣으려면 parentNode에서 몇번째에 있는 childNode로 가면 되는지 순서(orderInParent)를 찾는다.
         *     3. 해당하는 leftNode[orderInParent]를 childNode에 대입(포인터를 건네줌)
         *     4. childNode가 leaf인지 검사
         *      4-1.leaf가 아닐때
         *          1) inputIndex를 끼워넣으려면 child의 몇번째에 가면 되는지 미리 조사한다(orderInChild).
         *          2) rightChildNode를 동적할당해서 parent의 [orderInParent + 1]에 넣어준다.
         *          2) childNode의 [divide_i]번째에 해당하는 정보(key, leftNode 혹은 rightNode)를 parent로 push_back. 이때 value는 주지 않음
         *             이때, 끼워넣은 leftNode 혹은 rightNode...등은 직접 조정해야한다. push_back함수 참조
         *          3) (rightChild가 우선!) child를 뒤가 짤린 왼쪽용 childNode(뒤의 정보를 초기화)와 rightChildNode(new Node로 새롭게 동적할당)로 나눠준다.
         *          4) orderInChild가 divide_i보다 큰지 안큰지 구분, 맞는 child(left or right)로 찾아간다.
         *          5) loop를 돕시다. 찾아간 child를 parentNode에 담는다.
         *          6) 2.로 돌아감
         *      4-2. leaf일때
         *          1) inputIndex를 끼워넣으려면 child의 몇번째에 가면 되는지 미리 조사한다(orderInChild).
         *          2) childNode의 [divide_i]번째에 해당하는 정보(key, leftNode 혹은 rightNode)를 parent로 push_back. 이때 value는 주지 않음
         *             이때, 끼워넣은 leftNode 혹은 rightNode...등은 직접 조정해야한다. push_back함수 참조
         *          3) (rightChild가 우선!) child를 뒤가 짤린 왼쪽용 childNode(뒤의 정보를 초기화)와 rightChildNode(new Node로 새롭게 동적할당)로 나눠준다.
         *          4) orderInChild가 divide_i보다 큰지 안큰지 구분, 맞는 child(left or right)로 찾아간다.
         *          5) 해당 노드는 이제 여유가 생겼기 때문에 inputIndex-inputValue쌍을 push_back 해줍시다
         *
         *
         *     아.. 아니면 Node를 반환하는 함수로.. <<이걸로 가자! delete에도 써먹어야할 것 같으니
         *     parentNode 아래의 child(index를 포함할 가능성이 높은)Node를 쪼개서 parent에 올려주는 역할... 그러고 해당 child(index에 가까운)Node를 반환
         *     child가 leaf인지 검사해서 leaf일때까지 쪼개주는 함수로...? 그리고 그렇게 여유가 생긴 childNode를 반환하면 이제 여유있는 node에 push_back만 하면 되는상황
         *
         *     음 여유가 생긴 leaf노드를 반환하면 insert 함수에서 해당 leaf노드에 끼워넣을듯? 어차피 새로운 key-value는 leaf에만 들가니까
         * </함수개요>
         */
        if (newNodeMode) {
            return null; //TODO: newNodeMode작성
        } else {
            Node parentNode = superParentNode;
            Node childNode;

            int orderInParent = parentNode.findIndexOfKeyInKeyArray(inputkey);
            if (orderInParent >= parentNode.getCurrentNumberOfKeys()) childNode = parentNode.getRightNode();
            else childNode = parentNode.getLeftNode(orderInParent);
            int orderInChild = childNode.findIndexOfKeyInKeyArray(inputkey);
            int divide_i = degree % 2 == 0 ? degree / 2 : (int) (Math.ceil((double) degree / 2) - 1); //배열은 0부터 시작하므로 1더 빼줌, TODO: 안전하게 소수점 자르기            }

            while(!childNode.isLeaf()) {

            }
            /*
            loop를 나옴
            위에 있는 노드 좌좌작 다 쪼개서 parent는 non-leaf, child는 leaf인 경우(마지막)
             */
            
            return parentNode;        //삽입할 수 있는 node를 반환할때까지 loop를 돌면 while문안에 parentNode = childNode..어쩌고 하는게 마지막으로 한번더 실행이 되므로 parent 반환해야함
        }
    }

    public void insert(int inputkey, int inputValue){ //중복 key는 들어오지 않는다
        //TODO: delete로 싹다 지워서 root가 null이 된 경우
        /**
         * search?해서 알맞는 노드까지 감
         * 그 노드에 넣을 자리가 있으면 넣음
         * 넣을 자리가 없으면 쪼개기!
         */
        Node searchedLeafNode = singleKeySearchNode(inputkey, false);

        if(searchedLeafNode.getCurrentNumberOfKeys() < totalNumberOfKeys){
            searchedLeafNode.push_back(inputkey, inputValue);
        }

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

        else {
            Node superParentNode = findSuperParentNode(searchedLeafNode, inputkey);

            if (superParentNode == null) {
                //root도 쪼개야 하는 경우
                //1. root를 처음으로 쪼개는 경우
                //2. 그냥 나머지가 다 꽉차서 root를 쪼개야하는 경우
            } else if (superParentNode == root) {
                //root에 노드 하나 더 추가하는 경우(경우 나눌 필요 있나?)
            } else {
                //internal node가 superParentNode인 경우

                //TODO: split함수 구현
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
