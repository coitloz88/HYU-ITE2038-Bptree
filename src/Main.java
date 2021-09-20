import java.io.*;

public class Main {

    static Node previousNode = null;

    public static void main(String[] args) {
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
                        root.setValue(Integer.parseInt(keyValues[1].replace(" ", "")), i);
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

            return bPlusTree;
        } catch (IOException e) {
            e.printStackTrace();
        }

        return bPlusTree;
    }

    private static void readNode(BufferedReader br, Node parentNode, int indexInParentNode, int totalNumberOfKeys) {
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
