import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.util.ArrayList;

public class FileSorter {

    private final BufferedWriter fileWriter;
    private final ArrayList<BufferedReader> fileReaders;
    private final SortOrder sortOrder;
    private final DataType dataType;
    private final ArrayList<Item> currentItems;

    public FileSorter(BufferedWriter fileWriter, ArrayList<BufferedReader> fileReaders, SortOrder sortOrder, DataType dataType) {
        this.fileWriter = fileWriter;
        this.fileReaders = fileReaders;
        this.sortOrder = sortOrder;
        this.dataType = dataType;
        this.currentItems = new ArrayList<>();
    }

    public void sort() throws IOException {
        fillItems();
        if (this.currentItems.size() > 0) {
            while (this.fileReaders.size() > 0) {
                int minIndex = getMinIndex(this.currentItems);
                this.fileWriter.write(this.currentItems.get(minIndex).getString());
                this.fileWriter.newLine();
                Item readItem = readItem(minIndex);
                if (readItem != null) {
                    this.currentItems.set(minIndex, readItem);
                }
            }
        }
    }

    private void fillItems() throws IOException {
        for (int i = this.fileReaders.size() - 1; i >= 0; i--) {
            Item currentItem = readItem(i);
            if (currentItem != null) {
                this.currentItems.add(currentItem);
            }
        }
    }

    private Item readItem(int index) throws IOException {
        String currentItemString = this.fileReaders.get(index).readLine();
        if (currentItemString != null) {
            try {
                return new Item(this.dataType, currentItemString);
            }
            catch (Exception e) {
                System.err.println("Найдена недопустимая строка:" + currentItemString);
                return readItem(index);
            }
        } else {
            removeFile(this.fileReaders.get(index));
            return null;
        }
    }

    private int getMinIndex(ArrayList<Item> currentItems) {
        int min = 0;
        if (currentItems.size() == 1) {
            return min;
        }
        for (int i = 1; i < currentItems.size(); i++) {
            switch (this.sortOrder) {
                case ASCENDING:
                    if (currentItems.get(i).compareTo(currentItems.get(min)) < 0) {
                        min = i;
                    }
                    break;
                case DESCENDING:
                    if (currentItems.get(i).compareTo(currentItems.get(min)) > 0) {
                        min = i;
                    }
                    break;
            }
        }
        return min;
    }

    private void removeFile(BufferedReader inputFileReader) {
        try {
            inputFileReader.close();
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }
        int index = fileReaders.indexOf(inputFileReader);
        this.fileReaders.remove(index);
        if (this.currentItems.size() > 0) {
            this.currentItems.remove(index);
        }
    }

}
