import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.Queue;

class Canvas {
  private static final char CH_EMPTY = ' ';
  private static final char CH_LINE = 'x';
  
  private char[][] canvas;
  
  public Canvas(int width, int height) {
    if (width < 1 || height < 1) {
      throw new IllegalArgumentException();
    }
    
    canvas = new char[height][width];
    for (char[] line : canvas) {
      Arrays.fill(line, CH_EMPTY);
    }
  }
  
  public void draw(int fromX, int fromY, int toX, int toY) {
    if (!checkBounds(fromX, fromY) || !checkBounds(toX, toY)) {
      throw new IllegalArgumentException();
    }
  
    verticalLine(fromX, Math.min(fromY, toY), Math.max(fromY, toY));
    horizontalLine(fromY, Math.min(fromX, toX), Math.max(fromX, toX));
    verticalLine(toX, Math.min(fromY, toY), Math.max(fromY, toY));
    horizontalLine(toY, Math.min(fromX, toX), Math.max(fromX, toX));
  }
  
  private void horizontalLine(int y, int from, int to) {
    Arrays.fill(canvas[y], from, to + 1, CH_LINE);
  }
  
  private void verticalLine(int x, int from, int to) {
    for (int i = from; i <= to; i++) {
      canvas[i][x] = CH_LINE;
    }
  }
  
  public void fill(int x, int y, char ch) {
    if (!checkBounds(x, y)) {
      throw new IllegalArgumentException();
    }
    
    // Flood fill algorithm
    if (canvas[y][x] != CH_EMPTY) {
      return;
    }
    
    Queue<Point> q = new ArrayDeque<>();
    q.add(new Point(x, y));
    
    while (!q.isEmpty()) {
      Point p = q.remove();
      int west = p.x;
      int east = p.x;
      
      while (west > 0 && canvas[p.y][west - 1] == CH_EMPTY) {
        west--;
      }
      while (east < canvas[p.y].length - 1 && canvas[p.y][east + 1] == CH_EMPTY) {
        east++;
      }
      
      for (int i = west; i <= east; i++) {
        canvas[p.y][i] = ch;
        if (p.y > 0 && canvas[p.y - 1][i] == CH_EMPTY) {
          q.add(new Point(i, p.y - 1));
        }
        if (p.y < canvas.length - 1 && canvas[p.y + 1][i] == CH_EMPTY) {
          q.add(new Point(i, p.y + 1));
        }
      }
    }
  }
  
  private boolean checkBounds(int x, int y) {
    return x >= 0 && x < canvas[0].length && y >= 0 && y < canvas.length;
  }
  
  @Override
  public String toString() {
    char[] border = new char[canvas[0].length + 2];
    Arrays.fill(border, '-');
    
    StringBuilder sb = new StringBuilder();
    sb.append(border).append('\n');
    for (char[] line : canvas) {
      sb.append('|').append(line).append('|').append('\n');
    }
    sb.append(border);
    return sb.toString();
  }
  
  private static class Point {
    public int x;
    public int y;
    
    public Point(int x, int y) {
      this.x = x;
      this.y = y;
    }
  }
}
