import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Random;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        /**
         * command line argument 예시
         * 1. data file creation: program -c index_file b
         *  java bptree -c index. dat 8
         * 2. insertion: program -i index_file data_file
         *  java bptree -i index. dat input.csv
         * 3. deletion: program -d index_file data_file
         *  java bptree -d index. dat delete.csv
         * 4. single key search: program -s index_file key
         *  java bptree -s index. dat 125
         * 5. program -r index_file start_key end_key
         *  java bptree -r index. dat 100 200
         */
/*
        //TODO: command line argument 구현
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
            try {
                FileReader fileReader_indexDAT = new FileReader(args[3]);
                FileReader fileReader_inputCSV = new FileReader(args[4]);
                //TODO: index . dat에 있던 tree를 복사, input.csv를 파싱해서 insert
                fileReader_indexDAT.close();
                fileReader_inputCSV.close();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else if(args[2].equals("-d")){
            //deletion
        } else if(args[2].equals("-s")){
            try {
                FileReader fileReader_indexDAT = new FileReader(args[3]);

                fileReader_indexDAT.close();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            //single key search
        } else if(args[2].equals("-r")){
            try {
                FileReader fileReader_indexDAT = new FileReader(args[3]);

                fileReader_indexDAT.close();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            //range search
        }
*/
        Scanner keyboard = new Scanner(System.in);
        //int degree = keyboard.nextInt();
        int degree = 5; //child(가지) 개수
        BPlusTree bPlusTree = new BPlusTree(degree);

 /*       int totalNumber = 30;

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
            bPlusTree.insert(num * 2, num * 2 * 100);
            System.out.println("bPlusTree.insert(" + (num*2) + ");");
        }
*/
        bPlusTree.insert(14,1400);
        bPlusTree.insert(46, 4600);
        bPlusTree.insert(42, 4200);
        bPlusTree.insert(10, 1000);
        bPlusTree.insert(16, 1600);
        bPlusTree.insert(8, 800);
        bPlusTree.insert(40, 4000);
        bPlusTree.insert(44, 4400);
        bPlusTree.insert(50, 5000);
        bPlusTree.insert(24, 2400);
        bPlusTree.insert(52, 5200);
        bPlusTree.insert(26, 2600);
        bPlusTree.insert(20, 2000);
        bPlusTree.insert(6, 600);
        bPlusTree.insert(12, 1200);
        bPlusTree.insert(56, 5600);
        bPlusTree.insert(54, 5400);
        bPlusTree.insert(4, 400);
        bPlusTree.insert(30, 300);
        bPlusTree.insert(22, 2200);
        bPlusTree.insert(36, 3600);
        bPlusTree.insert(58, 5800);
        bPlusTree.insert(32, 3200);
        bPlusTree.insert(2, 200);
        bPlusTree.insert(18, 1800);
        bPlusTree.insert(0,0);
        bPlusTree.insert(34,3400);
        bPlusTree.insert(28,2800);
        bPlusTree.insert(38,3800);
        bPlusTree.insert(48,4800);
        bPlusTree.insert(41,4100);
        bPlusTree.insert(60,6000);
/*        for (int i = totalNumber + 1; i <= totalNumber * 2; i++) {
            bPlusTree.insert(i, i * 100);
        }
*/

        System.out.println("insert 종료\n");
        System.out.println("\n# linked list 연결 확인 #");
        bPlusTree.showAllLeafKeys();
        //System.out.println();
/*

        System.out.println("\n*\nsingle key search 시작!");
        int findNumber = rd.nextInt(totalNumber);
        bPlusTree.singleKeySearch(findNumber);

        System.out.println("\n*\nRange Search 시작!");
        bPlusTree.rangeSearch(-1,1);
*/

        int inputNum = 0;

        while(inputNum != 100){
            System.out.print("\n지울 키를 입력하세요(1~20), 100입력시 종료: ");
            inputNum = keyboard.nextInt();
            bPlusTree.delete(inputNum);
            bPlusTree.showAllLeafKeys();
        }

        System.out.print("\n*** search 할 key 입력(1000입력시 종료): ");
        inputNum = keyboard.nextInt();
        inputNum = keyboard.nextInt();

            bPlusTree.singleKeySearch(inputNum);


        System.out.println("\n# linked list 연결 확인 #");
        bPlusTree.showAllLeafKeys();

        keyboard.close();

    }
}
