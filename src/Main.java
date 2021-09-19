import java.io.*;

public class Main {

    static Node previousNode = null;

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

        if (args[0].equals("-c")) {
            try {
                FileWriter fw = new FileWriter(args[1]);
                fw.write(args[2]);
                fw.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else if (args[0].equals("-i")) {
            //insert
            BPlusTree bPlusTree = readTree(args[1]);
            try {
                BufferedReader bufferInputCSV = new BufferedReader(new FileReader(args[2]));
                String line = null;
                while ((line = bufferInputCSV.readLine()) != null) {
                    String[] parsedData = line.split(",");
                    bPlusTree.insert(Integer.parseInt(parsedData[0]), Integer.parseInt(parsedData[1]));
                }
                bPlusTree.saveTree(args[1]);
                bufferInputCSV.close();

            } catch (IOException e) {
                e.printStackTrace();
            }


        } else if (args[0].equals("-d")) {
            //deletion
            BPlusTree bPlusTree = readTree(args[1]);
            try {
                BufferedReader bufferDeleteCSV = new BufferedReader(new FileReader(args[2]));
                String line = null;

                while ((line = bufferDeleteCSV.readLine()) != null) {
                    bPlusTree.delete(Integer.parseInt(line));
                }
                bPlusTree.saveTree(args[1]);
                bufferDeleteCSV.close();

            } catch (IOException e) {
                e.printStackTrace();
            }


        } else if (args[0].equals("-s")) { //singleKeySearch
            BPlusTree bPlusTree = readTree(args[1]);
            bPlusTree.singleKeySearch(Integer.parseInt(args[2]));
        } else if (args[0].equals("-r")) {//range search
            BPlusTree bPlusTree = readTree(args[1]);
            bPlusTree.rangeSearch(Integer.parseInt(args[2]), Integer.parseInt(args[3]));
        } else {
            System.out.println("wrong input");
        }


/*        int degree = 5; //child(가지) 개수
        BPlusTree bPlusTree = new BPlusTree(degree, new Node(degree - 1, true, null));
        int totalNumber = 20;
        for (int i = 0; i < totalNumber; i++) {
            bPlusTree.insert(i, i * 100);
        }

        bPlusTree.saveTree("output.txt");
*/
/*
        BPlusTree bPlusTree = readTree("output.txt");
        bPlusTree.saveTree("output2.txt");
*/
    }

    public static BPlusTree readTree(String indexFile){
        BPlusTree bPlusTree = null;
        try {
            BufferedReader br = new BufferedReader(new FileReader(indexFile));
            int degree = Integer.parseInt(br.readLine());

            String line = br.readLine(); // root 정보를 읽음

            if(line == null){
                bPlusTree = new BPlusTree(degree, new Node(degree - 1, true, null));
                br.close();
                return bPlusTree;
            }
            else {
                String[] totalDatas = line.split(" / ");
                boolean isLeaf = totalDatas[0].equals("1");
                Node root = new Node(totalDatas.length - 1, isLeaf, null);
                if(isLeaf){
                    for (int i = 0; i < totalDatas.length - 1; i++) {
                        String[] keyValues = totalDatas[i + 1].split(" ");
                        root.setKey(Integer.parseInt(keyValues[0].replace(" ", "")), i);
                        root.setValue(Integer.parseInt(keyValues[1].replace(" ", "")), i);;
                    }
                } else {
                    for (int i = 0; i < totalDatas.length - 1; i++) {
                        root.setKey(Integer.parseInt(totalDatas[i + 1].replace(" ", "")),i);

                    }
                    root.setCurrentNumberOfKeys(totalDatas.length - 1);
                    for (int i = 0; i < totalDatas.length ; i++) {
                        readNode(br, root, i, degree - 1);
                    }
                }
                bPlusTree = new BPlusTree(degree, root);
            }
            br.close();
            
            //TODO: leaf 노드 연결?
            return bPlusTree;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return bPlusTree;
    }

    private static void readNode(BufferedReader br, Node parentNode, int indexInParentNode, int totalNumberOfKeys) {
        /**
         * node의 child와 parent를 연결한다.
         * 예를 들어 node가 leafnode의 parentNode라고 한다면
         */
        try {
            String line = br.readLine();
            String[] totalDatas = line.split(" / ");
            boolean isLeaf = totalDatas[0].equals("1");

            Node node = null;
            if (isLeaf) {
                Node childNode = new Node(totalNumberOfKeys, true, parentNode);
                for (int i = 0; i < totalDatas.length - 1; i++) {
                    String[] keyValues = totalDatas[i + 1].split(" ");
                    childNode.setKey(Integer.parseInt(keyValues[0].replace(" ", "")), i);
                    childNode.setValue(Integer.parseInt(keyValues[1].replace(" ", "")), i);
                }
                childNode.setCurrentNumberOfKeys(totalDatas.length - 1);
                parentNode.setChildNode(childNode, indexInParentNode);
                if(previousNode != null) previousNode.setChildNode(childNode, 0);
                previousNode = childNode;

            } else {
                node = new Node(totalNumberOfKeys, false, parentNode);
                for (int i = 0; i < totalDatas.length - 1; i++) {
                    node.setKey(Integer.parseInt(totalDatas[i + 1].replace(" ", "")), i);
                }
                node.setCurrentNumberOfKeys(totalDatas.length - 1);
                parentNode.setChildNode(node, indexInParentNode);
                for (int i = 0; i < totalDatas.length; i++) {
                    readNode(br, node, i, totalNumberOfKeys);
                }
            }


        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
