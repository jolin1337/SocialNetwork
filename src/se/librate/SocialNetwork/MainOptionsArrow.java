/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package se.librate.SocialNetwork;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.view.MotionEvent;
import android.view.View;

/**
 *
 * @author jolin1337
 */
public class MainOptionsArrow {
    Paint paint = new Paint();
    private boolean exp = false;
    private float width, height;
    public void renderCircle(Canvas canvas) {
        paint.setAntiAlias(true);
        paint.setFilterBitmap(true);
        paint.setDither(true);
        width = canvas.getWidth();
        height = canvas.getHeight();
        if(exp || isOverview())
            paint.setColor(Color.parseColor("#AAEE1111"));
        else
            paint.setColor(Color.parseColor("#AA11EE11"));
        canvas.drawCircle(width/2, height/2, 50, paint);
    }
    public void renderArrow(Canvas canvas, Item item) {
        paint.setAntiAlias(true);
        paint.setFilterBitmap(true);
        paint.setDither(true);
        paint.setColor(Color.WHITE);
        paint.setStyle(Paint.Style.FILL_AND_STROKE);
        paint.setStrokeWidth(2);
        float posX = item.getX();
        float posY = item.getY();
        float w = item.getWidth();
        float h = item.getHeight();
        double angle = Math.atan2((posY + h/2 - height/2), (posX + w/2 - width/2));
        
        canvas.save();
        canvas.translate(width/2, height/2);
        canvas.rotate((float)(angle * 180 / Math.PI));
        Path path = new Path();
        path.moveTo(50, 0);
        path.lineTo(70, 0);
        path.lineTo(70, 5);
        path.lineTo(75, 0);
        path.lineTo(70, -5);
        path.lineTo(70, 0);
        canvas.drawPath(path, paint);
        canvas.restore();
    }

    private boolean moved = false;
    private boolean touched = false;
    boolean onTouch(View v, MotionEvent e) {
        switch(e.getAction()) {
            case MotionEvent.ACTION_CANCEL:
            case MotionEvent.ACTION_UP:
                if(touched && !moved) {
                    toggleOverview();
                    return true;
                }
                touched = false;
                moved = false;
                break;
                
            case MotionEvent.ACTION_MOVE:
                moved = true;
                break;
                
            case MotionEvent.ACTION_DOWN:
                float xpos = e.getX() - width/2;
                float ypos = e.getY() - height/2;
                if(xpos*xpos + ypos*ypos < 50 * 50) {
                    touched = true;
                    return true;
                }
                break;
                
        }
        return false;
    }
    
    boolean isOverview() {
        return exp;
    }
    void showOverview() {
        exp = true;
    }
    void hideOverview() {
        exp = false;
    }
    void toggleOverview() {
        exp = !exp;
    }
}
