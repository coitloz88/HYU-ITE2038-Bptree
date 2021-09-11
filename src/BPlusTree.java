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
        if(root.getCurrentNumberOfKeys() < degree-1){
            root.push_back(inputIndex);
        } else {
            //TODO: 쪼개는 insert 함수 구현
            /*
            1차 쪼개기 -> 노드 남기고 올림
            2차 쪼개기(loop)
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
