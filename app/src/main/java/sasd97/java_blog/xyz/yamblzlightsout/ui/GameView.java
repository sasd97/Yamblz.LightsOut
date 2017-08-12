package sasd97.java_blog.xyz.yamblzlightsout.ui;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.annotation.NonNull;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Display;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;

import xyz.javablog.common.matrixes.Matrix;
import xyz.javablog.common.sizes.Size;

public class GameView extends View {

    public interface OnGridItemClickListener {
        public void onItemClick(int x, int y);
    }

    private GestureDetector gestureDetector;
    private Bitmap bitmap;
    private Paint paint, bitmapPaint;
    private float canvasSize;
    private Size size;
    private int horizontalCountOfCells;
    private int verticalCountOfCells;
    private int cellX;
    private int cellY;
    private Matrix matrix;
    private int cellSize;
    private Paint paintActive = new Paint();
    private Paint paintPassive = new Paint();
    private Rect rectangle = new Rect();

    private OnGridItemClickListener listener;

    public void setListener(@NonNull OnGridItemClickListener listener) {
        this.listener = listener;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        int width = MeasureSpec.getSize(widthMeasureSpec);
        int height = MeasureSpec.getSize(heightMeasureSpec);

        canvasSize = Math.min(width, height);
    }

    public GameView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);

        gestureDetector = new GestureDetector(context, new MyGestureDetector());



        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        canvasSize = display.getWidth();


        cellSize = (int) (canvasSize / horizontalCountOfCells);

        bitmap = Bitmap.createBitmap((int) canvasSize, (int) canvasSize, Bitmap.Config.ARGB_8888);
        bitmapPaint = new Paint(Paint.DITHER_FLAG);

        paint = new Paint();
        paint.setAntiAlias(true);
        paint.setDither(true);
        paint.setColor(0xffff0505);
        paint.setStrokeWidth(5f);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeJoin(Paint.Join.ROUND);
        paint.setStrokeCap(Paint.Cap.ROUND);

    }


    public void setNewSize(Size size) {
        horizontalCountOfCells = size.getWidth();
        verticalCountOfCells = size.getHeight();
    }
    public void setMatrix(Matrix matrix) {
        this.matrix = matrix;
    }


    @Override
    protected void onDraw(Canvas canvas) {

        for (int x = 0; x < horizontalCountOfCells + 1; x++)
            canvas.drawLine((float) x * canvasSize / horizontalCountOfCells, 0, (float) x * canvasSize / horizontalCountOfCells, canvasSize, paint);
        for (int y = 0; y < verticalCountOfCells + 1; y++)
            canvas.drawLine(0, (float) y * canvasSize / verticalCountOfCells, canvasSize, (float) y * canvasSize / verticalCountOfCells, paint);


        cellSize = (int)(canvasSize / horizontalCountOfCells);

        paintPassive.setColor(Color.BLUE);

        int[][] ar = matrix.toRawArray();

        paintActive.setColor(Color.BLUE);
        paintPassive.setColor(Color.BLACK);

        for (int x1 = 0; x1 < ar.length; x1++) {
            for (int y1 = 0; y1 < ar.length; y1++) {
                rectangle.set(x1 * cellSize, y1 * cellSize, (x1+1) * (cellSize), (y1+1) * cellSize);
                if (ar[x1][y1] == 1) {

                    canvas.drawRect(rectangle, paintPassive);
                }else {
                    canvas.drawRect(rectangle,  paintActive);
                }
            }
        }



        canvas.drawBitmap(bitmap, 0, 0, bitmapPaint);
    }

    public void getTapPoint(){
        int x = -1;
        int y = -1;

        x = (int) Math.ceil(cellX / cellSize);
        y = (int) Math.ceil(cellY / cellSize);

        if (listener != null) listener.onItemClick(x, y);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        gestureDetector.onTouchEvent(event);
        return true;
    }

    public class MyGestureDetector extends GestureDetector.SimpleOnGestureListener {

        @Override
        public boolean onSingleTapConfirmed(MotionEvent event) {

            cellX = (int) ((event.getX() + getScrollX()));
            cellY = (int) ((event.getY() + getScrollY()));


            getTapPoint();
            return true;
        }
    }
}
