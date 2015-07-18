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
import android.widget.EditText;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author jolin1337
 */
public class MainOptionsArrow {
    public static final double LIMIT = 12 * Math.PI / 180;
    
    private final Paint paint = new Paint();
    private boolean exp = false;
    private float width, height;
    EditText et;
    public MainOptionsArrow(EditText et) {
        this.et = et;
    }
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
    public void renderArrow(Canvas canvas, float posX, float posY, float w, float h) {
        paint.setAntiAlias(true);
        paint.setFilterBitmap(true);
        paint.setDither(true);
        paint.setColor(Color.WHITE);
        paint.setStyle(Paint.Style.FILL_AND_STROKE);
        paint.setStrokeWidth(2);
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
    public void renderArrow(Canvas canvas, double angle) {
        paint.setAntiAlias(true);
        paint.setFilterBitmap(true);
        paint.setDither(true);
        paint.setColor(Color.WHITE);
        paint.setStyle(Paint.Style.FILL_AND_STROKE);
        paint.setStrokeWidth(2);
        
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
    void renderArrows(Canvas canvas, Double[] arrowsToRender) {
        if(arrowsToRender.length == 0) return;
        for(int i = 0; i < arrowsToRender.length; i++) {
            if(arrowsToRender[i] == null) continue;
            for(int j = 0; j < arrowsToRender.length; j++) {
                if(arrowsToRender[j] == null) continue;
                if(i != j && Math.abs(arrowsToRender[i] - arrowsToRender[j]) < LIMIT) {
                    arrowsToRender[i] = (arrowsToRender[i] + arrowsToRender[j])/2;
                    arrowsToRender[j] = null;
                }
            }
        }
        for (Double arrowAngle : arrowsToRender) {
            if (arrowAngle != null) {
                renderArrow(canvas, arrowAngle);
            }
        }
    }

    private boolean moved = false;
    private boolean touched = false;
    boolean onTouch(View v, MotionEvent e) {
        switch(e.getAction()) {
            case MotionEvent.ACTION_CANCEL:
            case MotionEvent.ACTION_UP:
                if(touched && !moved) {
                    toggleOverview();
                    touched = false;
                    moved = false;
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
                if(xpos*xpos + ypos*ypos < 50 * 50 || isOverview()) {
                    touched = true;
                }
                return true;
                
        }
        return false;
    }
    
    boolean isOverview() {
        return exp;
    }
    void showOverview() {
        exp = true;
        if(et != null) 
            et.setVisibility(View.GONE);
    }
    void hideOverview() {
        exp = false;
        if(et != null) 
            et.setVisibility(View.VISIBLE);
    }
    void toggleOverview() {
        exp = !exp;
        if(et != null) {
            if(exp)
                et.setVisibility(View.GONE);
            else
                et.setVisibility(View.VISIBLE);
        }
    }

}
