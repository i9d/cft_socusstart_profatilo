public class Item implements Comparable<Item> {
    private Integer integer;
    private final DataType dataType;
    private final String string;

    public Item(DataType dataType, String string) {
        this.dataType = dataType;
        this.string = string;
        if (string.contains(" ")) {
            throw new IllegalArgumentException();
        }
        if (dataType == DataType.INTEGER) {
            this.integer = Integer.parseInt(string);
        }
    }

    public String getString() {
        return string;
    }

    @Override
    public int compareTo(Item item) {
        if (this.dataType == DataType.INTEGER && item.dataType == DataType.INTEGER) {
            return this.integer - item.integer;
        }
        return this.string.compareTo(item.string);
    }
}
