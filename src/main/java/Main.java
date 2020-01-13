import java.util.Arrays;
import java.util.Scanner;

public class Main {
  static String[][] field;
  static boolean isWinnerFound = false;
  static boolean isX = true;

  public static void main(String[] args) {
    Scanner scanner = new Scanner(System.in);

    field = fillField("_________");

    printField(field);

    while (true) {
      String[] coordinatesNotValidated = scanCoordinates(scanner);
      int[] coordinates;
      String sign;

      if (isX) {
        sign = "X";
        isX = false;
      } else {
        sign = "O";
        isX = true;
      }

      if (isInputValid(coordinatesNotValidated)) {
        coordinates = getCoordinatesFromStringArray(coordinatesNotValidated);
      } else {
        continue;
      }

      if (isCoordinatesValid(coordinates[0], coordinates[1])) {
        coordinates = convertToCommonTwoDimArray(coordinates);

        if (isAvailable(coordinates[0], coordinates[1])) {

          placeSymbol(coordinates[0], coordinates[1], sign);

        } else {
          continue;
        }

      } else {
        continue;
      }

      whoWon(field);
      if (!isWinnerFound) {
        printField(field);
        continue;
      }

      printField(field);
      System.out.println(whoWon(field));

      break;
    }

    scanner.close();
  }

  private static void placeSymbol(int x, int y, String symbol) {
    field[x][y] = symbol;
  }

  private static int[] convertToCommonTwoDimArray(int[] input) {

    int[][][] transformationMatrix = new int[3][3][2];

    for (int i = 0; i < 3; i++) {
      for (int j = 2; j >= 0; j--) {
        transformationMatrix[i][j] = new int[] {Math.abs(2 - j), i};
      }
    }

    return transformationMatrix[input[0] - 1][input[1] - 1];
  }

  private static int[] getCoordinatesFromStringArray(String[] coordinates) {
    int[] result = new int[coordinates.length];
    for (int i = 0; i < coordinates.length; i++) {
      result[i] = Integer.parseInt(coordinates[i]);
    }

    return result;
  }

  private static boolean isInputValid(String[] coordinates) {
    try {
      for (int i = 0; i < 2; i++) {
        Integer.parseInt(coordinates[i]);
      }
    } catch (NumberFormatException nfe) {
      System.out.println("Invalid input format");
      return false;
    }
    return true;
  }

  private static String[] scanCoordinates(Scanner scanner) {
    System.out.print("Enter the coordinates:");
    String[] result = new String[2];
    result[0] = scanner.next();
    result[1] = scanner.next();

    return result;
  }

  private static boolean isAvailable(int x, int y) {
    if (field[x][y].equals("_")) {
      return true;
    } else {
      System.out.println("This cell is occupied! Choose another one!");
      return false;
    }
  }

  private static boolean isCoordinatesValid(int x, int y) {
    if (!(x >= 4 || x <= 0 || y >= 4 || y <= 0)) {
      return true;
    } else {
      System.out.println("Coordinates should be from 1 to 3!");
      return false;
    }
  }

  private static void play(String[][] field) {
    if (isGameImpossible(field)) {
      System.out.println("Impossible");
    } else {
      System.out.println(whoWon(field));
    }
  }

  private static String whoWon(String[][] twoDimArray) {
    if (isGivenSymbolWon(twoDimArray, "X")) {
      isWinnerFound = true;
      return "X wins";
    }
    if (isGivenSymbolWon(twoDimArray, "O")) {
      isWinnerFound = true;
      return "O wins";
    }
    if (!isGivenSymbolWon(twoDimArray, "X") && !isGivenSymbolWon(twoDimArray, "O")) {
      if (!isGameFinished()) return "Game not finished";
    }
    return "Draw";
  }

  private static boolean isGameFinished() {
    return isWinnerFound;
  }

  private static boolean isGameImpossible(String[][] twoDimArray) {
    int unitsDifference =
        Math.abs(
            countSymbolInTwoDimArray(twoDimArray, "X")
                - countSymbolInTwoDimArray(twoDimArray, "O"));

    return unitsDifference >= 2 || isTwoWinners(twoDimArray, "X", "O");
  }

  private static boolean isTwoWinners(String[][] twoDimArray, String symbol1, String symbol2) {
    if (isGivenSymbolWon(twoDimArray, symbol1) && isGivenSymbolWon(twoDimArray, symbol2)) {
      return true;
    }
    return false;
  }

  private static boolean isGivenSymbolWon(String[][] twoDimArray, String symbol) {

    return checkRows(twoDimArray, symbol)
        || checkColumns(twoDimArray, symbol)
        || checkDiagonals(twoDimArray, symbol);
  }

  private static boolean checkDiagonals(String[][] twoDimArray, String symbol) {
    int mainDiagonalCount = 0;
    int sideDiagonal = 0;

    for (int i = 0; i < twoDimArray.length; i++) {
      if (symbol.equals(twoDimArray[i][i])) {
        mainDiagonalCount++;
      }
    }

    for (int i = 0, j = twoDimArray.length - 1; i < twoDimArray.length & j >= 0; i++, j--) {
      if (symbol.equals(twoDimArray[i][j])) {
        sideDiagonal++;
      }
    }

    return (mainDiagonalCount == 3) || (sideDiagonal == 3);
  }

  private static boolean checkColumns(String[][] twoDimArray, String symbol) {

    for (int i = 0; i < twoDimArray.length; i++) {
      String[] tmp = new String[twoDimArray.length];

      for (int j = 0; j < twoDimArray[0].length; j++) {
        tmp[j] = twoDimArray[j][i];
      }

      if (countSymbolInArray(tmp, symbol) == 3) {
        return true;
      }
    }

    return false;
  }

  private static boolean checkRows(String[][] twoDimArray, String symbol) {
    for (int i = 0; i < twoDimArray[0].length; i++) {
      if (countSymbolInArray(twoDimArray[i], symbol) == 3) {
        return true;
      }
    }
    return false;
  }

  private static int countSymbolInTwoDimArray(String[][] twoDimArray, String symbol) {
    int result = 0;

    for (String[] strings : twoDimArray) {
      result += countSymbolInArray(strings, symbol);
    }

    return result;
  }

  private static int countSymbolInArray(String[] arr, String symbol) {
    int count = 0;
    for (String s : arr) {
      if (symbol.equals(s)) {
        count++;
      }
    }
    return count;
  }

  private static void printField(String[][] twoDimArray) {
    System.out.println("---------");
    for (String[] strings : twoDimArray) {
      System.out.println("| " + printArray(strings) + "|");
    }

    System.out.println("---------");
  }

  private static String printArray(String[] arr) {
    StringBuilder stringBuilder = new StringBuilder();
    for (String s : arr) {
      stringBuilder.append(s).append(" ");
    }
    return stringBuilder.toString();
  }

  private static String[][] fillField(String input) {
    String[][] result = new String[3][3];
    String[] splittedLine = input.split("");

    for (int i = 0, j = 0; i < 9; i += 3, j++) {
      result[j] = Arrays.copyOfRange(splittedLine, i, i + 3);
    }

    return result;
  }
}
