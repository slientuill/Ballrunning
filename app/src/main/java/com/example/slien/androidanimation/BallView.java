package com.example.slien.androidanimation;

/**
 * Created by slien on 2016/10/16.
 */
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import java.util.ArrayList;
/**
 * Created by slien on 2016/10/11.
 */
public class BallView extends SurfaceView implements SurfaceHolder.Callback {
    Bitmap[] bitmapArray =new Bitmap[6];
    Bitmap bmpBack;
    int green1=0xff00bb9c;
    int green2=0xff11cd6e;
    int blue1=0xff56abe4;
    int purple1=0xff9d55b8;
    int yellow1=0xfff4c600;
    int orange=0xffea8010;
    int red1=0xffeb4f38;
    int ballNumber =10; //小球数目
    int draw=0;
    int c[]= {Color.CYAN,Color.MAGENTA,Color.YELLOW,Color.RED,Color.BLUE,
            Color.DKGRAY,Color.GREEN,Color.BLACK,green1,green2,red1,orange,purple1,blue1,yellow1};
    ArrayList<Moveable> alMovable=new ArrayList<Moveable>(); //小球对象数组
    DrawThread dt; //后台屏幕绘制线程
    public  BallView(Context activity){
        super(activity);
        getHolder().addCallback(this);
        initBitmaps(getResources());
        initMoveables();
        dt=new DrawThread(this,getHolder());
    }
    public void initBitmaps(Resources r){
        bitmapArray[0]=BitmapFactory.decodeResource(r, R.drawable.ball_yellow);
        bitmapArray[1]=BitmapFactory.decodeResource(r, R.drawable.ball_blue);
        bitmapArray[2]=BitmapFactory.decodeResource(r, R.drawable.ball_black);
        bitmapArray[3]=BitmapFactory.decodeResource(r, R.drawable.ball_red);
        bitmapArray[4]=BitmapFactory.decodeResource(r, R.drawable.ball_pink);
        bitmapArray[5]=BitmapFactory.decodeResource(r, R.drawable.ball_brown);
        bmpBack=BitmapFactory.decodeResource(r, R.drawable.back);
    }
    public void initMoveables(){
        int MultC;
        int MultX;
        int MultY;
        int MultR;
        for(int i=0;i<15;i++){
            MultY=1+(int)(1700*Math.random());
            MultX=1+(int)(1100*Math.random());
            MultC=1+(int)(15*Math.random());
            Moveable m=new Moveable(MultX,MultY,40, null,c[MultC-1],false);
            alMovable.add(m);
        }
    }
    public int Create(){
        for(int i=0;i<alMovable.size();i++)
        {
            Moveable m=alMovable.get(i);
            if(m.r>150){
                Moveable newball=new Moveable(m.x,m.y, m.r*7/10, null,m.color,false);
                Moveable newball2=new Moveable(m.x,m.y, m.r*7/10, null,m.color,true);
                alMovable.add(newball);
                alMovable.add(newball2);
                alMovable.get(i).shutdown();
                alMovable.remove(i);
            }
            if(m.r>300){
                alMovable.get(i).startR=10;
            }
            //what??????????????????

        }
        return alMovable.size();
    }
    public void addBalls(){
        if(alMovable.size()<10){
            int MultC,MultX,MultY,MultR;
            int count=10-alMovable.size();
            for(int k=0;k<count;k++){
                MultY=1+(int)(1700*Math.random());
                MultX=1+(int)(1100*Math.random());
                MultC=1+(int)(15*Math.random());
                Moveable newball=new Moveable(MultX,MultY,40, null,c[MultC-1],false);
                alMovable.add(newball);
            }
        }
    }
    public void doDraw(Canvas canvas){ //绘制程序中需要的图片等信息
        canvas.drawBitmap(bmpBack, 0, 0,null);
        for(int i=0;i<alMovable.size();i++)//遍历绘制每个Movable对象
        {
            Moveable m=alMovable.get(i);
            if(m.r>0){
                m.drawSelf(canvas);
            }
            if(m.r<=0){
                alMovable.get(i).shutdown();
                alMovable.remove(i);
            }

            draw++;
        }
       // addBalls();
        Create();
        collision();
        for(int i=0;i<alMovable.size();i++){
            Paint p=new Paint();
            p.setColor(Color.BLUE);
            p.setTextSize(50);
            p.setAntiAlias(true);
            canvas.drawText(String.valueOf(alMovable.size())+","+draw+":"+String.valueOf(alMovable.get(i).r), 20, 100+i*50, p);
        }
    }
    public void collision(){
        int i,j;
        boolean anti=false;
        for(i=0;i<alMovable.size();i++){
            for(j=i+1;j<alMovable.size();j++){
                Moveable a1=alMovable.get(i);
                Moveable a2=alMovable.get(j);
                double dis=Math.sqrt((a1.x-a2.x)*(a1.x-a2.x)+(a1.y-a2.y)*(a1.y-a2.y));
                //double rou=a1.r+a2.r;
                if(dis<a1.r&&a1.r>2*a2.r){
                    Moveable newball=new Moveable(a1.x,a1.y, (float) Math.sqrt(a1.r*a1.r+a2.r*a2.r), null,a1.color,true);
                    alMovable.add(newball);
                    alMovable.get(i).bt.interrupt();
                    alMovable.get(j).bt.interrupt();
                    alMovable.remove(i);
                    alMovable.remove(j);
                }
                if(dis<a2.r&&a2.r>2*a1.r){
                    Moveable newball=new Moveable(a2.x,a2.y, (float) (Math.sqrt(a1.r*a1.r+a2.r*a2.r)), null,a2.color,false);
                    alMovable.add(newball);
                    alMovable.get(i).bt.interrupt();
                    alMovable.get(j).bt.interrupt();
                    alMovable.remove(i);
                    alMovable.remove(j);
                }
            }
        }
    }
    public void surfaceChanged(SurfaceHolder arg0, int arg1, int arg2, int arg3) {}
    public void surfaceCreated(SurfaceHolder arg0) {  if(!dt.isAlive())  dt.start();}
    public void surfaceDestroyed(SurfaceHolder arg0) {dt.flag=false;dt=null;  }
}