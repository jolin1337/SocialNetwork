/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package se.librate.SocialNetwork;

import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.RectF;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.Toast;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author jolin1337
 */
public class DesktopView extends SurfaceView implements View.OnTouchListener {
    List<Item> items = new ArrayList<Item>();
    MainOptionsArrow moa;
    
    RectF boundary = new RectF();
    RectF maxBoundary = new RectF();
    float panX = 0, panY = 0;
    float startPanX = 0, startPanY = 0;
    
    Item active;
    Paint paint = new Paint();
    private DesktopViewLoop desktopViewLoop;
    private final SurfaceHolder holder;
    private static final float boundaryMargin = 10;
    
    Matrix m = new Matrix();
    RectF destinationRect;
    Context ctx;
    
    private final SurfaceHolder.Callback callbackHolder = new SurfaceHolder.Callback() {
        @Override
        public void surfaceDestroyed(SurfaceHolder holder) {
            setOnTouchListener(null);
            boolean retry = true;
            desktopViewLoop.setRunning(false);
            while (retry && desktopViewLoop.isAlive()) {
                try {
                    desktopViewLoop.join();
                    retry = false;
                } catch (InterruptedException e) {
                }
            }
        }

        @Override
        public void surfaceCreated(SurfaceHolder holder) {
            destinationRect = new RectF(0,0,0,0);
            destinationRect.right = DesktopView.this.getWidth();
            destinationRect.bottom = DesktopView.this.getHeight();
            setOnTouchListener(DesktopView.this); 
            //setOnClickListener(DesktopView.this);
            //setOnDragListener(DesktopView.this);
            updateBoundaries();

            render();
            //desktopViewLoop.setRunning(true);
            //desktopViewLoop.start();
        }

        @Override
        public void surfaceChanged(SurfaceHolder holder, int format,
                      int width, int height) {
            destinationRect.right = width;
            destinationRect.bottom = height;
            render();
        }
    };
    
    public DesktopView(Context context) {
        super(context);
        moa = new MainOptionsArrow((EditText) findViewById(R.id.search_bar));
        ctx = context;
        desktopViewLoop = new DesktopViewLoop(this);
            
        items.add(new Contact(1, 20, 10));
        items.add(new Contact(2, 200, 300));
        items.add(new Contact("Bertil", 3, 10, 250));
        items.add(new Contact(4, 170, 400));
        holder = getHolder();
        holder.addCallback(callbackHolder);
        
    }
    public DesktopView(Context context, AttributeSet attrs) {
        super(context, attrs);
        moa = new MainOptionsArrow((EditText) findViewById(R.id.search_bar));
        ctx = context;
        desktopViewLoop = new DesktopViewLoop(this);
        
        items.add(new Contact(1, 20, 10));
        items.add(new Contact(2, 200, 300));
        items.add(new Contact("Bertil", 3, 10, 250));
        items.add(new Contact(4, 170, 400));
        holder = getHolder();
        holder.addCallback(callbackHolder);
    }
    public DesktopView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        moa = new MainOptionsArrow((EditText) findViewById(R.id.search_bar));
        ctx = context;
        desktopViewLoop = new DesktopViewLoop(this);
        
        items.add(new Contact(1, 20, 10));
        items.add(new Contact(2, 200, 300));
        items.add(new Contact("Bertil", 3, 10, 250));
        items.add(new Contact(4, 170, 400));
        holder = getHolder();
        holder.addCallback(callbackHolder);
    }
    
    @Override
    protected void onDraw(Canvas canvas) {
        paint.setAntiAlias(true);
        paint.setFilterBitmap(true);
        paint.setDither(true);
        paint.setColor(Color.parseColor("#111111"));
        paint.setStyle(Paint.Style.FILL);
        canvas.drawPaint(paint);
        
        float realPanX = panX;
        float realPanY = panY;
        boolean isOverview = moa.isOverview();
        // If circle was activated
        if(isOverview) {
            canvas.save();
            renderOverview(canvas);
            
            realPanX = 0;
            realPanY = 0;
        }
        paint.setStrokeWidth(1);
        paint.setColor(Color.RED);
        paint.setStyle(Paint.Style.STROKE);
        canvas.translate(realPanX, realPanY);
        // Draw the boundary of the items displayed on the canvas
        canvas.drawRect(boundary, paint);
        paint.setColor(Color.BLUE);
        canvas.drawRect(maxBoundary, paint);
        canvas.translate(-realPanX, -realPanY);
        
        renderItems(canvas, realPanX, realPanY, isOverview);
        // If circle was activated
        if(isOverview) {
            canvas.translate(-panX, -panY);
            moa.renderCircle(canvas);
            canvas.restore();
        }
        else
            moa.renderCircle(canvas);
        super.onDraw(canvas);
    }
    
    @Override
    public boolean onTouch(View v, MotionEvent e) {
        if(moa.isOverview()) {
            boolean res = moa.onTouch(v, e);
            if(res && e.getAction() == MotionEvent.ACTION_UP){
                performClick();
                
                // Compute the scale to choose (this works)
                float scaleX = (float) destinationRect.width() / (float) boundary.width();
                float scaleY = (float) destinationRect.height() / (float) boundary.height();
                float minScale = Math.min(scaleX, scaleY);// * 0.8f;

                // tx, ty should be the translation to take th  e image back to the screen center
                float tx = Math.max(0, minScale * boundary.width() / 2f + 0.5f * (destinationRect.width() - (minScale * boundary.width())));
                float ty = Math.max(0, minScale * boundary.height() / 2f + 0.5f * (destinationRect.height() - (minScale * boundary.height())));

                panX = -((e.getX() - tx)/minScale + boundary.width() / 2f + boundary.left) + getWidth()/2;
                panY = -((e.getY() - ty)/minScale + boundary.height() / 2f + boundary.top) + getHeight()/2;
            }
            render();
            return res;
        }
        else if(requestFocus());
        switch(e.getAction()) {
            case MotionEvent.ACTION_MOVE:
                moveEvent(e);
            case MotionEvent.ACTION_DOWN:
                downEvent(e);
                break;
            case MotionEvent.ACTION_CANCEL:
            case MotionEvent.ACTION_UP:
                upEvent(e);
                break;
        }
        boolean res = moa.onTouch(v, e);
        render();
        if(res) performClick();
        return true;
    }
    private void moveEvent(MotionEvent e) {
        if(active != null) {
            float newX = e.getX() - active.getWidth()/2 - panX;
            float newY = e.getY() - active.getHeight()/2 - panY;
            active.setPos(newX, newY);
            Item tmp = findItemExcept(active);
            if(tmp == null && maxBoundary.contains(active.getRect())) {
                active.applyPos();
            }

            updateBoundaries();
        }
        else {
            final float dx = e.getX() - startPanX;
            final float dy = e.getY() - startPanY;
            final float tmpPanX = panX + dx;
            final float tmpPanY = panY + dy;

            final float boundaryFactor = 0.1f;

            panX = tmpPanX;
            panY = tmpPanY;

            float dxFac = dx * boundaryFactor;
            float dyFac = dy * boundaryFactor;

            panX = -Math.max(-panX, maxBoundary.left - boundaryMargin - dxFac);
            panX = -Math.min(-panX, maxBoundary.right + boundaryMargin - dxFac - getWidth());
            panY = -Math.max(-panY, maxBoundary.top - boundaryMargin - dyFac);
            panY = -Math.min(-panY, maxBoundary.bottom + boundaryMargin - dyFac - getHeight());

            if(tmpPanX == panX)
                startPanX = e.getX();
            if(tmpPanY == panY)
                startPanY = e.getY();
        }
    }

    private void downEvent(MotionEvent e) {
        if(active != null) return;
        active = findItem(e.getX() - panX, e.getY() - panY);
        startPanX = e.getX();
        startPanY = e.getY();
    }

    private void upEvent(MotionEvent e) {
        if(active != null) {
            if(Math.abs(e.getX() - startPanX) < 3 && Math.abs(e.getY() - startPanY) < 3) {
                Intent in = new Intent(ctx, ChatActivity.class);
                ctx.startActivity(in);
                Toast.makeText(ctx, "Clicked a contact", Toast.LENGTH_LONG);
            }
            Item tmp = findItemExcept(active);
            if(tmp == null && maxBoundary.contains(active.getRect())) {
                active.applyPos();
            }
            else 
                active.resetPos();
            active = null;
        }

        panX = -Math.max(-panX, maxBoundary.left - boundaryMargin);
        panX = -Math.min(-panX, maxBoundary.right + boundaryMargin - getWidth());
        panY = -Math.max(-panY, maxBoundary.top - boundaryMargin);
        panY = -Math.min(-panY, maxBoundary.bottom + boundaryMargin - getHeight());
    }

    private Item findItem(float x, float y) {
        for(Item item : items) {
            if(item.isColliding(x, y))
                return item;
        }
        return null;
    }
    private Item findItemExcept(Item except) {
        for(Item item : items) {
            if(item != except && item.isColliding(except))
                return item;
        }
        return null;
    }

    private void updateBoundaries() {
        if(items.size() > 0) {
            Item item = items.get(0);
            maxBoundary.left = boundary.left = item.getX();
            maxBoundary.top = boundary.top = item.getY();
            maxBoundary.right = boundary.right = item.getX() + item.getWidth();
            maxBoundary.bottom = boundary.bottom = item.getY() + item.getHeight();
        }
        
        for(Item item : items) {
            float x = item.getX();
            float y = item.getY();
            float r = x + item.getWidth();
            float b = y + item.getHeight();
            if(boundary.left > x) {
                maxBoundary.left = x;
                boundary.left = x;
            }
            if(boundary.top > y) {
                maxBoundary.top = y;
                boundary.top = y;
            }
            
            if(boundary.right < r) {
                maxBoundary.right = r;
                boundary.right = r;
            }
            if(boundary.bottom < b) {
                maxBoundary.bottom = b;
                boundary.bottom = b;
            }
        }
        for(Item item : items) {
            float x = item.getX();
            float y = item.getY();
            float r = x + item.getWidth();
            float b = y + item.getHeight();
            if(boundary.left == maxBoundary.left || (boundary.left < x && x < maxBoundary.left))
                maxBoundary.left = x;
            if(boundary.top == maxBoundary.top || (boundary.top < y && y < maxBoundary.top))
                maxBoundary.top = y;
            
            if(boundary.right == maxBoundary.right || (boundary.right > r && r > maxBoundary.right))
                maxBoundary.right = r;
            if(boundary.bottom == maxBoundary.bottom || (boundary.bottom > b && b >maxBoundary.bottom))
                maxBoundary.bottom = b;
        }
        maxBoundary.left = maxBoundary.left - 2.0f*getWidth();
        maxBoundary.top = maxBoundary.top - 2.0f*getHeight();
        maxBoundary.right = maxBoundary.right + 2.0f*getWidth();
        maxBoundary.bottom = maxBoundary.bottom + 2.0f*getHeight();
        
        
        boolean updateConstraints = false;
        
        if(maxBoundary.left > boundary.left) {
            boundary.left = maxBoundary.left;
            updateConstraints = true;
        }
        if(maxBoundary.top > boundary.top) {
            boundary.top = maxBoundary.top;
            updateConstraints = true;
        }
        
        if(maxBoundary.right < boundary.right) {
            boundary.right = maxBoundary.right;
            updateConstraints = true;
        }
        if(maxBoundary.bottom < boundary.bottom) {
            boundary.bottom = maxBoundary.bottom;
            updateConstraints = true;
        }
        
        if(updateConstraints)
            updateItemContraints();
    }
    
    private void updateItemContraints() {
        for(Item item : items) {
            if(maxBoundary.left > item.getX())
                item.setX(maxBoundary.left);
            if(maxBoundary.top > item.getY())
                item.setY(maxBoundary.top);
            
            float rightPos = maxBoundary.right - item.getWidth();
            float bottomPos = maxBoundary.bottom - item.getHeight();
            if(rightPos < item.getX())
                item.setX(rightPos);
            if(bottomPos < item.getY())
                item.setY(bottomPos);
            
            item.applyPos();
        }
    }
    
    private void render() {
        
        Canvas canvas = null;
        try {
            canvas = getHolder().lockCanvas();
            synchronized (getHolder()) {
                onDraw(canvas);
            }
        } finally {
            if (canvas != null) {
                getHolder().unlockCanvasAndPost(canvas);
            }
        }
    }

    private void renderItems(Canvas canvas, float realPanX, float realPanY, boolean isOverview) {
        List<Double> arrowsToRender = new ArrayList<Double>();
        for (Item item : items) {
            float x = item.getX();
            float y = item.getY();
            float rx = item.getRealX();
            float ry = item.getRealY();
            item.setPos(x + realPanX, y + realPanY);
            item.setRealPos(rx + realPanX, ry + realPanY);
            
            // If the item is outside the screen and the item has news to tell
            if(item.hasNews() && !isOverview && (x + realPanX + item.getWidth() < 0 || x + realPanX > canvas.getWidth() ||
                    y + realPanY + item.getHeight() < 0 || y + realPanY > canvas.getHeight())) {
                arrowsToRender.add((Math.atan2((item.getY() + item.getHeight()/2 - getHeight()/2), (item.getX() + item.getWidth()/2 - getWidth()/2))));
            }
            else
                item.render(canvas);
            item.setPos(x, y);
            item.setRealPos(rx, ry);
        }
        Double []tmp = new Double[arrowsToRender.size()];
        tmp = arrowsToRender.toArray(tmp);
        moa.renderArrows(canvas, tmp);
    }

    private void renderOverview(Canvas canvas) {
        m.reset();
        // TODO: Animate minScale, tx and ty variables from 1, panX and panY respectively
        // Compute the scale to choose (this works)
        float scaleX = (float) destinationRect.width() / (float) boundary.width();
        float scaleY = (float) destinationRect.height() / (float) boundary.height();
        float minScale = Math.min(scaleX, scaleY);// * 0.8f;

        // tx, ty should be the translation to take the image back to the screen center
        float tx = Math.max(0, minScale * boundary.width() / 2f + 0.5f * (destinationRect.width() - (minScale * boundary.width())));
        float ty = Math.max(0, minScale * boundary.height() / 2f + 0.5f * (destinationRect.height() - (minScale * boundary.height())));

        // Middle of the image should be the scale pivot
        m.postTranslate(-boundary.left, -boundary.top);
        m.postTranslate(-boundary.width() / 2f, -boundary.height() / 2f);
        m.postScale(minScale, minScale);
        m.postTranslate(tx, ty);

        // Static method of doing the above code in this block of code
        //m.setRectToRect(boundary, destinationRect, Matrix.ScaleToFit.CENTER);
        canvas.concat(m);

    }

/*
    public boolean onDrag(View v, DragEvent event) {
        if(moa.isOverview()) {
//            boolean res = moa.onTouch(v, event);
//            v.invalidate();
            return false;
        }
        final int action = event.getAction();
        switch(action) {
//            case DragEvent.ACTION_DRAG_STARTED:
            case DragEvent.ACTION_DRAG_ENTERED:
                downEvent(event);
                render();
                v.invalidate();
                return true;
            case DragEvent.ACTION_DRAG_LOCATION:
                moveEvent(event);
                render();
                v.invalidate();
                return true;
            case DragEvent.ACTION_DRAG_EXITED:
                return false;
//            case DragEvent.ACTION_DROP:
            case DragEvent.ACTION_DRAG_ENDED:
                upEvent(event);
                render();
                v.invalidate();
                return true;
        }
        return false;
        
        
        
    }*/

    void setSearchBar(EditText et) {
        moa.et = et;
    }

}
