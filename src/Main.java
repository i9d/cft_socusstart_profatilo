import java.io.*;
import java.util.ArrayList;

public class Main {

    public static SortOrder sortOrder = SortOrder.ASCENDING;
    public static DataType dataType;
    public static ArrayList<String> fileNameList = new ArrayList<>();

    public static void main(String[] args) {
        try {
            parseArguments(args);
        } catch (IllegalArgumentException e) {
            System.err.println(e.getMessage());
            System.exit(1);
        }

        try (FileWriter outputFileWriter = new FileWriter(fileNameList.get(0))) {
            BufferedWriter outputBufferedWriter = new BufferedWriter(outputFileWriter);
            fileNameList.remove(0);
            ArrayList<BufferedReader> inputBufferedReaders = new ArrayList<>();
            for (String fileName : fileNameList) {
                try {
                    inputBufferedReaders.add(new BufferedReader(new FileReader(fileName)));
                } catch (FileNotFoundException e) {
                    System.err.println(e.getMessage());
                }

            }
            FileSorter fileSorter = new FileSorter(outputBufferedWriter, inputBufferedReaders, sortOrder, dataType);
            fileSorter.sort();
            outputBufferedWriter.close();
            for (BufferedReader inputBufferedReader : inputBufferedReaders) {
                inputBufferedReader.close();
            }
        } catch (IOException e) {
            System.err.println(e.getMessage());
            System.exit(1);
        }
    }

    public static void parseArguments(String[] args) {
        if (args.length < 3) {
            throw new IllegalArgumentException("Заданы не все необходимые параметры");
        }
        for (String arg : args) {
            switch (arg) {
                case "-a" -> sortOrder = SortOrder.ASCENDING;
                case "-d" -> sortOrder = SortOrder.DESCENDING;
                case "-s" -> dataType = DataType.STRING;
                case "-i" -> dataType = DataType.INTEGER;
                default -> fileNameList.add(arg);
            }
        }
        if (!validateDataType()) {
            throw new IllegalArgumentException("Не задан параметр типа данных.");
        }
        if (!validateFileCount()) {
            throw new IllegalArgumentException("Неверно заданы параметры файлов.");
        }
    }

    public static boolean validateDataType() {
        return dataType != null;
    }

    public static boolean validateFileCount() {
        return fileNameList.size() > 1;
    }
}