import java.util.Scanner;

public class Main {
    public static void main(String[] args) {

        /**
         * csv파일에서 ','를 기준으로 index를 받아온다
         * 받아오면서 하나씩 B+tree에 넣어(insert)준다
         */
        Scanner keyboard = new Scanner(System.in);

        int degree = keyboard.nextInt();
        BPlusTree bPlusTree = new BPlusTree(degree);

    }
}
