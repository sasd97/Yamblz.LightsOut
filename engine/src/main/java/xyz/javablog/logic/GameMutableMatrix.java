package xyz.javablog.logic;

import xyz.javablog.common.cells.Cell;
import xyz.javablog.common.cells.PrimitiveCell;
import xyz.javablog.common.matrixes.Matrix;
import xyz.javablog.common.matrixes.MutableMatrix;
import xyz.javablog.common.points.Point;
import xyz.javablog.common.sizes.Size;

public class GameMutableMatrix implements MutableMatrix {

    private int width;
    private int height;

    private int[][] matrix;

    public GameMutableMatrix(Size size) {
        this.width = size.getWidth();
        this.height = size.getHeight();
        this.matrix = new int[width][height];
    }

    public GameMutableMatrix(Size size,
                             Matrix matrix) {
        this.width = size.getWidth();
        this.height = size.getHeight();
        this.matrix = matrix.toRawArray().clone();
    }

    @Override
    public void performClick(Point point) {
        int[][] possibleClickArea = PointHandler.getNeighbours(point.getX(), point.getY());

        for(int i = 0; i < PointHandler.AREAS_COUNT; i++) {
           int[] possibleClick = possibleClickArea[i];
           int x = possibleClick[0];
           int y = possibleClick[1];

           if (!PointHandler.isValidPoint(width, height, x, y)) continue;
           matrix[x][y] = PointHandler.switchArea(matrix[x][y]);
        }
    }

    @Override
    public Cell getCell(Point point) {
        int type = matrix[point.getX()][point.getY()];
        return new PrimitiveCell(type, point);
    }

    @Override
    public int[][] toRawArray() {
        return matrix;
    }

    @Override
    public boolean isSolved() {
        return MatrixHandler.isNull(matrix);
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        for(int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                builder.append(matrix[i][j]);
            }
            builder.append("\n");
        }
        return builder.toString();
    }
}
