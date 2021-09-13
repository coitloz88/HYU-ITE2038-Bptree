import java.util.Scanner;

public class Main {
    public static void main(String[] args) {

        /**
         * csv파일에서 ','를 기준으로 index를 받아온다
         * 받아오면서 하나씩 B+tree에 넣어(insert)준다
         * 동작 하나를 할때마다 .csv파일에 다시 써줘야 할듯
         */
        Scanner keyboard = new Scanner(System.in);
        //int degree = keyboard.nextInt();
        int degree = 5; //child(가지) 개수
        BPlusTree bPlusTree = new BPlusTree(degree);
        bPlusTree.insert(3, 33);
        bPlusTree.insert(4, 44);
        bPlusTree.insert(2, 22);
        bPlusTree.insert(1, 11);

        System.out.println();
        System.out.println("Node split!");
        System.out.println();
        bPlusTree.insert(5, 55);
        //bPlusTree.insert(7, 77);
        //bPlusTree.insert(6, 66);
        //bPlusTree.insert(9, 99);
        bPlusTree.insert(8, 88);
        bPlusTree.insert(10, 110);

        bPlusTree.show();
        System.out.println();
        System.out.println("single key search 시작!");
        System.out.println(bPlusTree.singleKeySearch(8));

        keyboard.close();

    }
}
