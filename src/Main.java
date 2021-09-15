import java.util.Random;
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
        int degree = 34; //child(가지) 개수
        BPlusTree bPlusTree = new BPlusTree(degree);

        int totalNumber = 100;

        boolean[] exist = new boolean[totalNumber];
        for (int i = 0; i < totalNumber; i++) {
            exist[i] = false;
        }

        Random rd = new Random();

        for (int i = 0; i < totalNumber; i++) {
            int num = rd.nextInt(totalNumber);
            while(exist[num]){
                num = rd.nextInt(totalNumber);
            }
            exist[num] = true;
            bPlusTree.insert(num, num * 100);
        }

        System.out.println();

        //System.out.println("\n# linked list 연결 확인 #");
        //bPlusTree.showAllLeafKeys();

        System.out.println("\n*\nsingle key search 시작!");
        int findNumber = rd.nextInt(totalNumber);
        System.out.println(bPlusTree.singleKeySearch(findNumber));

        System.out.println("\n*\nRange Search 시작!");
        System.out.println(bPlusTree.rangeSearch(0, 0));

        keyboard.close();

    }
}
