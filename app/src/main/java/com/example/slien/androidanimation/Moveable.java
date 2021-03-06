package com.example.slien.androidanimation;

/**
 * Created by slien on 2016/10/16.
 */

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
/**
 * Created by slien on 2016/10/11.
 */
public class Moveable {
    int startX=0;
    int startY=0;
    int x,y;//实时坐标
    float startVX=0f;//初始水平方向速度
    float startVY=0f;//竖直方向的速度
    float v_x=0f;
    float v_y=0f;
    float r;//半径
    float startR;
    float v_r=0f;
    double timeX;//x的运动时间
    double timeY;//Y的时间
    Bitmap bitmap=null;//图片？？？
    BallThread bt=null;//小球移动线程
    boolean bfall=false;//滑落测试
    boolean collision=false;
    int color;
    public Moveable(int x,int y,float r,Bitmap bitmap,int color,boolean poorson){
        this.startX=x;
        this.x=x;
        this.startY=y;
        this.y=y;
        this.startR=r;
        this.r=r;
        this.bitmap=bitmap;
        timeX=System.nanoTime();
        this.v_x=-80+(int)(80*Math.random());
        this.v_x*=8;
        this.v_y=-80+(int)(80*Math.random());
        this.v_y*=8;
        if(poorson){
            this.v_r=0-(1+(int)(8*Math.random()));
        }
        else{
            this.v_r=1+(int)(8*Math.random());
        }
        this.color=color;
        bt=new BallThread(this);
        bt.start();
    }
    public  void drawSelf(Canvas canvas){
        //canvas.drawBitmap(bitmap,x,y,null); 使用图片
        Paint paint=new Paint();//使用java graphic 画图
        paint.setColor(color);
        canvas.drawCircle(x,y,r,paint);
    }
    public void shutdown(){
        bt.interrupt();
    }
}