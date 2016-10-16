package com.example.slien.androidanimation;

/**
 * Created by slien on 2016/10/16.
 */
public class BallThread extends Thread{
    Moveable father;
    boolean flag=false;//线程执行标识
    int sleepSpan=50;//休眠时间
    double current;//当前时间
    int MultX=1+(int)(2*Math.random());
    int MultY=1+(int)(2*Math.random());
    public BallThread(Moveable father){
        this.father=father;
        this.flag=true;
    }
    public void run () {
        while (flag) {
            current = System.nanoTime();
            double timeSpanX = ((current - father.timeX) / 1000 / 1000 / 1000); // 获取水平方向走过的时间
            father.x = (int) (father.startX + MultX*father.v_x * timeSpanX);
            father.r=(int)(father.startR+father.v_r*timeSpanX);
            if (father.bfall) {
                double timeSpanY = ((current - father.timeY) / 1000 / 1000 / 1000);
                father.y=(int)(father.startY+MultY*father.v_y*timeSpanY);
                if (father.y + father.r>= 1700 && father.v_y > 0||father.y  <= 0 && father.v_y <0) {
                    father.v_y = 0 - father.v_y;
                    father.startR=father.r;
                    father.startX = father.x;
                    father.timeX = System.nanoTime();
                    father.startY = father.y;
                    father.timeY = System.nanoTime();
                    father.startVY = father.v_y;
                }
                if (father.x + father.r>= 1100&& father.v_x > 0||father.x  <= 0&& father.v_x < 0) {
                    father.v_x = 0 - father.v_x;
                    father.startX = father.x;
                    father.timeX = System.nanoTime();
                    father.startY = father.y;
                    father.timeY = System.nanoTime();
                    father.startVY = father.v_y;
                    father.startR=father.r;
                }

            }
            else if (father.x>= 0) {// 通过X坐标判断球是否移出了挡板
                father.timeY = System.nanoTime();
                father.bfall = true; // 确定下落
            }
            try {
                Thread.sleep(sleepSpan);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}