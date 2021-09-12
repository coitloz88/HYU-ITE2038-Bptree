public class BPlusTree {
    /**
     * degree: max degree(m-way)
     *
     * insert: 넣는거 시도하는데 성공적으로 넣어지면 1, 안되면 0을 반환하는 기본 함수로 일단 넣음 나머지 쪼개는거는 비쁠트리 차원에서
     */

    private int degree;
    private Node root;

    public BPlusTree(int degree){
        this.degree = degree;
        root = new Node(degree, true);
    }

    public Node singleKeySearchPreviousNode(int target){
        Node tmpNode = root;

        while (!tmpNode.isLeaf()) {
            // 현재 노드가 가진 key 값에 target 값이 있는지 찾아본다
            int[] tmpKeys = tmpNode.getKeys();
            int i;
            for (i = 0; i < tmpNode.getCurrentNumberOfKeys() - 1 && target > tmpKeys[i]; i++) {
                // 현재 노드가 가진 key의 값의 어느 범위에 들어가는지 확인해본다(i가 그만큼 증가)
            }

            if (target == tmpKeys[i]) {
                if (i >= tmpNode.getCurrentNumberOfKeys()) tmpNode = tmpNode.getRightNode();
                else tmpNode = tmpNode.getLeftNode(i + 1);
            } else tmpNode = tmpNode.getLeftNode(i);
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
            Node tmpNode = singleKeySearchPreviousNode(target);
            
            //leaf 도달
            int[] tmpKeys = tmpNode.getKeys();
            int i;
            for (i = 0; i < tmpNode.getCurrentNumberOfKeys() - 1 && target > tmpKeys[i]; i++) { }
            if(target == tmpKeys[i]) return String.valueOf(tmpNode.getValue(i));
            else return "NOT FOUND";
    }

    public void insert(int inputIndex){ //중복 key는 들어오지 않는다

        /**
         * search?해서 알맞는 노드까지 감
         * 그 노드에 넣을 자리가 있으면 넣음
         * 넣을 자리가 없으면 쪼개기!
         */
        Node tmpNode = singleKeySearchPreviousNode(inputIndex);

        if(tmpNode.getCurrentNumberOfKeys() < degree-1){
            tmpNode.push_back(inputIndex);
        } else {
            //TODO: 쪼개는 insert 함수 구현
            /*
            1차 쪼개기 -> 노드 남기고 올림
            2차 쪼개기(loop)
             */

            //linked list 생성시 올라간 애는 오른쪽으로 쪼개지는 노드에 붙음
            /**
             * 쪼개지는 애: 0부터 시작한다고 가정, Up(M / 2) - 1
             * 1. 왼쪽으로 쪼개지는 노드: 쪼개지는 arrayIndex 미만의 key를 나눠가짐(기존 노드에서 current Index 수정)
             *      rightNode를 오른쪽으로 쪼개지는 Node 주소로 설정
             * 2. 오른쪽으로 쪼개지는 노드: 쪼개지는 arrayIndex 이상의 key를 나눠가짐
             *
             * 3. ??
             */
            int[] tmpKeys = tmpNode.getKeys();
            int i;
            for (i = 0; i < tmpNode.getCurrentNumberOfKeys() - 1 && inputIndex > tmpKeys[i]; i++) { /*inputIndex는 tmpKey[i]보다는 크고 tmpKey[i+1]보다는 작음 */ }




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
