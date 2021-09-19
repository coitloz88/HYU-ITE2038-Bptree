import java.io.*;
import java.util.Random;

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
/*
        if (args[2].equals("-c")) {
            //data file creation
            try {
                BPlusTree bPlusTree = new BPlusTree(Integer.parseInt(args[4]));
                ObjectOutputStream indexDAT = new ObjectOutputStream(new FileOutputStream(args[3]));
                indexDAT.writeObject(bPlusTree);
                indexDAT.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else if (args[2].equals("-i")) {
            //insert
            try {
                ObjectInputStream inputIndexDAT = new ObjectInputStream(new FileInputStream(args[3]));
                BPlusTree bPlusTree = (BPlusTree) inputIndexDAT.readObject();
                BufferedReader bufferInputCSV = new BufferedReader(new FileReader(args[4]));

                String line = null;

                while ((line = bufferInputCSV.readLine()) != null) {
                    String[] parsedData = line.split(",");
                    bPlusTree.insert(Integer.parseInt(parsedData[0]), Integer.parseInt(parsedData[1]));
                }

                ObjectOutputStream indexDAT = new ObjectOutputStream(new FileOutputStream(args[3]));
                indexDAT.writeObject(bPlusTree);
                indexDAT.close();
                inputIndexDAT.close();
                bufferInputCSV.close();

            } catch (IOException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }

        } else if (args[2].equals("-d")) {
            //deletion

            try {
                ObjectInputStream inputIndexDAT = new ObjectInputStream(new FileInputStream(args[3]));
                BPlusTree bPlusTree = (BPlusTree) inputIndexDAT.readObject();
                BufferedReader bufferInputCSV = new BufferedReader(new FileReader(args[4]));

                String line = null;

                while ((line = bufferInputCSV.readLine()) != null) {
                    bPlusTree.delete(Integer.parseInt(line));
                }

                ObjectOutputStream indexDAT = new ObjectOutputStream(new FileOutputStream(args[3]));
                indexDAT.writeObject(bPlusTree);
                indexDAT.close();
                inputIndexDAT.close();
                bufferInputCSV.close();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        } else if (args[2].equals("-s")) { //singleKeySearch

            try {
                ObjectInputStream indexDAT = new ObjectInputStream(new FileInputStream(args[3]));
                BPlusTree bPlusTree = (BPlusTree) indexDAT.readObject();
                bPlusTree.singleKeySearch(Integer.parseInt(args[4]));
                indexDAT.close();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }

        } else if (args[2].equals("-r")) {//range search

            try {
                ObjectInputStream indexDAT = new ObjectInputStream(new FileInputStream(args[3]));
                BPlusTree bPlusTree = (BPlusTree) indexDAT.readObject();
                bPlusTree.rangeSearch(Integer.parseInt(args[4]), Integer.parseInt(args[5]));
                indexDAT.close();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }


        }
*/
        System.out.println("Hello JAVA!");

/*
        try {
            BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter("input.csv"));
            int totalNumber = 1000;
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
                String tmp = (num*2) + "," + (num*200) + "\n";
                bufferedWriter.write(tmp);

            }
            bufferedWriter.flush();
            bufferedWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        //BPlusTree bPlusTree = new BPlusTree(5);

*/
    }
}
