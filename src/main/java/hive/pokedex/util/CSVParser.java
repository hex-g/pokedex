package hive.pokedex.util;

import java.util.ArrayList;
import java.util.stream.Stream;

public class CSVParser {

  private final ArrayList<ArrayList<String>> currentObject;
  private final ArrayList<char[]> charObject;

  private ArrayList<String> currentLine;
  private final StringBuilder currentColumn;

  private final char columnSeparator;
  private final char quote;

  public CSVParser(Stream<String> arr, char cs, char q) {
    currentObject = new ArrayList<>();
    charObject = new ArrayList<>();

    currentLine = new ArrayList<>();
    currentColumn = new StringBuilder();

    columnSeparator = cs;
    quote = q;

    for (Object row : arr.toArray()) {
      charObject.add(row.toString().toCharArray());
    }
  }

  public CSVParser parser() {
    for (int i = 0, size = charObject.size(); i < size; i++) {
      var insideQuotes = false;
      for (char c : charObject.get(i)) {

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
    var obj = new String[currentObject.size()][];

    int i = 0;
    for (var l : currentObject) {
      obj[i++] = l.toArray(new String[l.size()]);
    }

    return obj;
  }

}
