import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import org.junit.runners.JUnit4;

import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.Queue;
import java.util.Random;

public class SolutionTest {
  @Test
  public void drawLines() {
    Canvas c = new Canvas(5, 5);
    c.draw(0, 2, 4, 2);
    c.draw(2, 0, 2, 4);
    assertEquals("-------\n|  x  |\n|  x  |\n|xxxxx|\n|  x  |\n|  x  |\n-------", c.toString());
  }
    
  @Test
  public void drawRectangle() {
    Canvas c = new Canvas(7, 7);
    c.draw(1, 1, 5, 4);
    assertEquals("---------\n|       |\n| xxxxx |\n| x   x |\n| x   x |\n| xxxxx |\n|       |\n|       |\n---------", c.toString());
  }
  
  @Test
  public void fill() {
    Canvas c = new Canvas(7, 7);
    c.draw(1, 1, 5, 4);
    c.fill(3, 3, 'o');
    assertEquals("---------\n|       |\n| xxxxx |\n| xooox |\n| xooox |\n| xxxxx |\n|       |\n|       |\n---------", c.toString());
  }
    
  @Test
  public void mixedDrawing() {
    Canvas c = new Canvas(20, 4);
    c.draw(0, 1, 5, 1);
    c.draw(5, 2, 5, 3);
    c.draw(13, 0, 17, 2);
    c.fill(9, 2, 'o');
    assertEquals("----------------------\n|oooooooooooooxxxxxoo|\n|xxxxxxooooooox   xoo|\n|     xoooooooxxxxxoo|\n|     xoooooooooooooo|\n----------------------", c.toString());
  }
  
  private Random rnd = new Random();

  @Test(expected = IllegalArgumentException.class)
  public void invalidCanvasWidth() {
    new Canvas(-1 - rnd.nextInt(128), 1 + rnd.nextInt(128));
  }
  
  @Test(expected = IllegalArgumentException.class)
  public void invalidCanvasHeight() {
    new Canvas(1 + rnd.nextInt(128), -1 - rnd.nextInt(128));
  }
  
  @Test
  public void outOfBounds() {
    for (int i = 0; i < 128; i++) {
      try {
        int width = 1 + rnd.nextInt(128);
        int height = 1 + rnd.nextInt(128);
        Canvas c = new Canvas(width, height);
        int x, y;
        if (rnd.nextBoolean()) {
          // x go out of bounds
          x = Math.round(width * (1 + rnd.nextFloat()));
          y = Math.round(height * rnd.nextFloat());
        } else {
          // y go out of bounds
          x = Math.round(width * rnd.nextFloat());
          y = Math.round(height * (1 + rnd.nextFloat()));
        }
        
        if (rnd.nextBoolean()) {
          c.draw(x, y, x, y);
        } else {
          c.fill(x, y, 'o');
        }
        fail("Expected exception: java.lang.IllegalArgumentException width = " + width + ", height = " + height + ", x = " + x + ", y = " + y);
      } catch (IllegalArgumentException ex) {
        continue;
      }
    }
  }
  
  @Test
  public void randomMixedDrawing() {
    for (int i = 0; i < 128; i++) {
      int width = 32 + rnd.nextInt(32);
      int height = 32 + rnd.nextInt(32);
      Canvas c = new Canvas(width, height);
      CanvasSolution sol = new CanvasSolution(width, height);
      for (int j = 0; j < 16; j++) {
        int x1 = rnd.nextInt(width);
        int y1 = rnd.nextInt(height);
        int x2 = rnd.nextInt(width);
        int y2 = rnd.nextInt(height);
        c.draw(x1, y1, x2, y2);
        sol.draw(x1, y1, x2, y2);
      }
      
      for (char j = 'a'; j <= 'z'; j++) {
        if (j == 'x') continue;
        int x = rnd.nextInt(width);
        int y = rnd.nextInt(height);
        c.fill(x, y, j);
        sol.fill(x, y, j);
      }
      
      assertEquals(sol.toString(), c.toString());
    }
  }
}

class CanvasSolution {
  private static final char CH_EMPTY = ' ';
  private static final char CH_LINE = 'x';
  
  private char[][] canvas;
  
  public CanvasSolution(int width, int height) {
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
