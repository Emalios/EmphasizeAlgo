import javax.print.DocFlavor;
import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class EmphasizeAlgo {

    public static List<String> WORDS_TO_EMPHASIZE = Arrays.asList("Pour", "allant", "de", "à", "faire", "Tant que", "retourne", "Début", "Fin");

    public static void main(String[] args) {
        File file = new File(getPath());
        if(file.isFile()) {
            System.out.println("File to be emphasize : " + file.getName());
            emphasizeFile(file);
        } else {
            makeTree(file);
            emphasizeFolder(file);
        }
    }

    private static void emphasizeFolder(File file) {
        Arrays.stream(file.listFiles())
                .filter(File::isFile)
                .forEach(EmphasizeAlgo::emphasizeFile);
    }

    private static void emphasizeFile(File file) {
        System.out.println("Emphasize file : " + file.getName());
        List<List<String>> emphasizedLines = new ArrayList<>();
        try {
            FileReader fileReader = new FileReader(file);
            BufferedReader bufferedReader = new BufferedReader(fileReader);
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                List<String> words = Arrays.asList(line.split(" "));
                emphasizedLines.add(emphasizeLine(words));
            }
            replaceOldFile(file, emphasizedLines);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void replaceOldFile(File oldFile, List<List<String>> emphasizedLines) {
        File emphasizedFile = new File(oldFile.getAbsolutePath());
        try {
            FileWriter fileWriter = new FileWriter(emphasizedFile);
            for (List<String> emphasizedLine : emphasizedLines) {
                StringBuilder line = new StringBuilder();
                for (String word : emphasizedLine) {
                    line.append(word).append(" ");
                }
                fileWriter.write(line.toString());
                fileWriter.write("\n");
            }
            fileWriter.close();
            oldFile.delete();
            emphasizedFile.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static List<String> emphasizeLine(List<String> line) {
        for (int i = 0; i < line.size(); i++) {
            String word = line.get(i);
            if(WORDS_TO_EMPHASIZE.contains(word)) {
                line.set(i, "__"+word+"__");
            }
        }
        return line;
    }

    private static void makeTree(File file) {
        Arrays.stream(file.listFiles())
                .filter(File::isFile)
                .forEach(file1 -> {
                    System.out.println(file1.getName());
                });
    }

    private static String getPath() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Give a directory where emphasize algo files :");
        String path = scanner.nextLine();
        File file = new File(path);
        if(!file.exists()) {
            System.out.println("Error, path is wrong !");
            return getPath();
        }
        if(file.listFiles() == null) {
            System.out.println("Folder don't contains any files.");
            return getPath();
        }
        System.out.println("Path set to : " + path);
        return path;
    }

}
