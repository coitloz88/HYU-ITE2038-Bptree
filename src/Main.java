import java.io.FileWriter;
import java.io.IOException;
import java.util.Random;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        /**
         * command line argument 예시
         * 1. data file creation: program -c index_file b
         *  java bptree -c index.dat 8
         * 2. insertion: program -i index_file data_file
         *  java bptree -i index.dat input.csv
         * 3. deletion: program -d index_file data_file
         *  java bptree -d index.dat delete.csv
         * 4. single key search: program -s index_file key
         *  java bptree -s index.dat 125
         * 5. program -r index_file start_key end_key
         *  java bptree -r index.dat 100 200
         */

        if(args[2].equals("-c")){
            //data file creation
            try {
                FileWriter fileWriter_indexDAT = new FileWriter(args[3]);
                //TODO: b+tree를 생성해서 filewriter에 쓰기
                fileWriter_indexDAT.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else if(args[2].equals("-i")){
            //insert
        } else if(args[2].equals("-d")){
            //deletion
        } else if(args[2].equals("-s")){
            //single key search
        } else if(args[2].equals("-r")){
            //range search
        }

        /**
         * csv파일에서 ','를 기준으로 index를 받아온다
         * 받아오면서 하나씩 B+tree에 넣어(insert)준다
         * 동작 하나를 할때마다 .csv파일에 다시 써줘야 할듯
         */
        Scanner keyboard = new Scanner(System.in);
        //int degree = keyboard.nextInt();
        int degree = 20; //child(가지) 개수
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
        System.out.println(bPlusTree.rangeSearch(50, 54));

        keyboard.close();

    }
}
