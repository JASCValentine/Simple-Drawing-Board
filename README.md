# Simple Drawing Board
You are going to write a simple console-style drawing board program.

# Functions
The drawing board is very simple so only these functions are supported.

Function | Description
-------- | -----------
Create canvas | Create new empty canvas (filled with ` `) with given width and height
Draw line / rectangle | Draw the line with `x` which connects the given points.<br> If the points are diagonal, a rectangle instead of diagonal line should be drawn
Fill color | Fill the entire area connected to the given point with given character, also known as "bucket fill" in paint programs

# Output
- You should implement the `toString()` method to print the entire canvas surrounded by borders (`-` and `|`).
- There is no trailing newline

# Example
Empty canvas 4x2
```
------
|    |
|    |
------
```

Drawing 2 lines
`(0, 2)` to `(4, 2)` and `(2, 0)` to `(2, 4)`
```
-------
|  x  |
|  x  |
|xxxxx|
|  x  |
|  x  |
-------
```

Drawing a rectangle `(1, 1)` to `(5, 4)` and fill with `o`
```
---------
|       |
| xxxxx |
| xooox |
| xooox |
| xxxxx |
|       |
---------
```

# Remarks
- The coordinates are zero-based, therefore the origin is `(0, 0)`, which is at the top-left corner
- The coordinates are inclusive in `draw` method
- Throw `java.lang.IllegalArgumentException` for any invalid input
- If the area is already filled with different color, `fill` method should do nothing
