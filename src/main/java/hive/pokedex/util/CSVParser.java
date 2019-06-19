package hive.pokedex.util;

import java.util.ArrayList;
import java.util.stream.Stream;

public class CSVParser {
  private final ArrayList<ArrayList<String>> currentObject;
  private final ArrayList<char[]> charObject;
  private final StringBuilder currentColumn;
  private final char columnSeparator;
  private final char quote;
  private ArrayList<String> currentLine;

  public CSVParser(final Stream<String> arr, final char columnSeparator, final char quote) {

    this.currentObject = new ArrayList<>();
    this.charObject = new ArrayList<>();

    this.currentLine = new ArrayList<>();
    this.currentColumn = new StringBuilder();

    this.columnSeparator = columnSeparator;
    this.quote = quote;

    for (Object row : arr.toArray()) {
      charObject.add(row.toString().toCharArray());
    }
  }

  public CSVParser parser() {

    for (int i = 0, size = charObject.size(); i < size; i++) {
      var insideQuotes = false;
      for (final char c : charObject.get(i)) {

        if (c == quote) {
          insideQuotes = !insideQuotes;
          continue;
        }

        if (insideQuotes) {
          currentColumn.append(c);
          continue;
        }

        if (c == columnSeparator) {
          currentLine.add(currentColumn.toString());
          currentColumn.setLength(0);
          continue;
        }

        currentColumn.append(c);
      }
      currentLine.add(currentColumn.toString());
      currentColumn.setLength(0);
      currentObject.add(currentLine);
      currentLine = new ArrayList<>();
    }

    return this;
  }

  public String[][] get() {

    final var obj = new String[currentObject.size()][];

    int i = 0;
    for (final var l : currentObject) {
      obj[i++] = l.toArray(new String[l.size()]);
    }

    return obj;
  }
}
