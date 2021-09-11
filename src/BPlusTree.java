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
        root = new Node(degree);
    }
    
    public boolean search(int target){
        //TODO: search 함수 구현
        return false;
    }

    public void insert(int inputIndex){

        /**
         * search해서 알맞는 노드까지 감
         * 그 노드에 넣을 자리가 있으면 넣음
         * 넣을 자리가 없으면 쪼개기!
         */

        if(root.getCurrentNumberOfKeys() < degree-1){
            root.push_back(inputIndex);
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
             * 3. 새로운 root: 새로운 node 생성후 쪼개진 애를 key로 가짐
             */

        }
    }

    public void delete(){
        //TODO: delete 함수 구현
    }

    public void show(){
        root.showKeys();
    }
}
