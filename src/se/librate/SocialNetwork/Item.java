/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package se.librate.SocialNetwork;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;

/**
 *
 * @author jolin1337
 */
public abstract class Item {
    private float x = 0;
    private float y = 0;
    private float rel_x = 0;
    private float rel_y = 0;
    
    private float width = 100;
    private float height = 100;
    private final RectF bound = new RectF(), relBound = new RectF();
    private int id;
    
    protected final Paint paint = new Paint();

    public Item(int id, float x, float y) {
        this.id = id;
        setPos(x,y);
        setRealPos(x, y);
    }
    public Item(int id, float x, float y, float width, float height) {
        this.id = id;
        this.width = width;
        this.height = height;
        setPos(x, y);
        setRealPos(x, y);
    }
    
    public Paint render(Canvas canvas) {
        
        paint.setStyle(Paint.Style.FILL);
        paint.setColor(Color.BLUE);
        canvas.drawRect(x, y, x + width, y + height, paint);
        return paint;
    }
    
    public boolean isColliding(Item item) {
        return getRect().intersects(item.getX(), item.getY(), item.getX()+item.getWidth(), item.getY()+item.getHeight());
//        return (item.getX() <= x + width && item.getX() + item.getWidth() >= x
//                && item.getY() <= y + height && item.getY() + item.getHeight() >= y);
    }
    public boolean isColliding(float x, float y) {
        return x > this.x && this.x + width > x && y > this.y && this.y + height > y;
    }
    
    
    public float getWidth() {
        return width;
    }

    public void setWidth(float width) {
        this.width = width;
        this.bound.right = this.bound.left + this.width;
    }

    public float getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
        this.bound.bottom = this.bound.top + this.height;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
    
    public float getX() {
        return x;
    }

    public void setX(float x) {
        this.x = x;
        this.bound.left = this.x;
        this.bound.right = this.bound.left + this.width;
    }

    public float getY() {
        return y;
    }

    public void setY(float y) {
        this.y = y;
        this.bound.top = this.y;
        this.bound.bottom = this.bound.top + this.height;
    }
    public float getRealX() {
        return rel_x;
    }
    public void setRealX(float x) {
        rel_x = x;
        relBound.left = x;
        relBound.right = relBound.left + this.width;
    }
    public float getRealY() {
        return rel_y;
    }
    public void setRealY(float y) {
        rel_y = y;
        relBound.top = y;
        relBound.bottom = relBound.top + this.height;
    }
    public final void setPos(float x, float y) {
        setX(x);
        setY(y);
    }
    public final void setRealPos(float x, float y) {
        setRealX(x);
        setRealY(y);
    }
    public void applyPos() {
        setRealX(x);
        setRealY(y);
    }
    public void resetPos() {
        setX(rel_x);
        setY(rel_y);
    }
    
    public RectF getRect() {
        return bound;
    }
    public RectF getRelRect() {
        return relBound;
    }
    
    public abstract boolean hasNews();
}
