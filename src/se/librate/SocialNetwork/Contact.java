/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package se.librate.SocialNetwork;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.RectF;

/**
 *
 * @author jolin1337
 */
public class Contact extends Item {
    static public final float width = 100;
    static public final float height = 100;
    static public Bitmap defaultProfileImage;

    private String name;
    public Contact(int id, float x, float y) {
        super(id, x, y, Contact.width, Contact.height);
        name = "Unkown";
    }
    public Contact(String name, int id, float x, float y) {
        super(id, x, y, Contact.width, Contact.height);
        this.name = name;
    }
    
    @Override
    public Paint render(Canvas canvas) {
        paint.setAntiAlias(true);
        paint.setFilterBitmap(true);
        paint.setDither(true);
        paint.setColor(Color.parseColor("#AAAAAA"));
        
        float startX = getX();
        float startY = getY();
        
        if(getRealX() != startX || getRealY() != startY ) {
            float rx = getRealX();
            float ry = getRealY();
            RectF boundary = new RectF(rx, ry, 
                            rx + Contact.width, ry + Contact.height
                    );
            canvas.drawRect(boundary, paint);
        }
        
        float rad = 5;
        canvas.save();
        RectF boundary = new RectF(startX, startY, 
                        startX + Contact.width, startY + 3*Contact.height/4
                );
        Path path = new Path();
        path.addRoundRect(
                boundary, 
                rad, rad, Path.Direction.CCW);
        canvas.clipPath(path);
        if(defaultProfileImage == null) {
            canvas.drawCircle(getX() + Contact.width/2, getY() + Contact.height/4, Contact.width, paint);
        }
        else {
            int imgWidth = defaultProfileImage.getWidth();
            int imgHeight = defaultProfileImage.getHeight();
            if(imgWidth < imgHeight) {
                float ratio = (float)imgWidth / (float)imgHeight;
                float boundaryHeight = boundary.bottom - boundary.top;
                float boundaryOrigWidth = boundary.right - boundary.left;
                float boundaryWidth = boundaryHeight * ratio;
                float offs = (boundaryWidth - boundaryOrigWidth)/2;
                boundary.right = boundary.left + boundaryWidth - offs;
                boundary.left -= offs;
            }
            else {
                float ratio = (float)imgHeight / (float)imgWidth;
                float boundaryWidth = boundary.right - boundary.left;
                float boundaryOrigHeight = boundary.bottom - boundary.top;
                float boundaryHeight = boundaryWidth * ratio;
                float offs = (boundaryHeight - boundaryOrigHeight)/2;
                boundary.bottom = boundary.top + boundaryHeight - offs;
                boundary.top -= offs;
            }
            //canvas.drawCircle(getX() + Contact.width/2, getY() + Contact.height/4, Contact.width, paint);
            canvas.drawBitmap(defaultProfileImage, new Rect(
                    0, 0, imgWidth, imgHeight
            ), boundary, paint);
        }
        //canvas.clipPath(null);
        canvas.restore();
        paint.setColor(Color.BLACK);
        float fontSize = 20, textWidth;
        do {
            paint.setTextSize(fontSize);
            textWidth = paint.measureText(name);
            fontSize--;
        } while(textWidth > Contact.width);
        canvas.drawText(name, startX - textWidth/2 + Contact.width/2, startY + Contact.height-5, paint);
        
        return paint;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    
    public boolean hasNews() {
        return true;
    }
}
