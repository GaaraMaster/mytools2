package com.coderstory.mytools;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;



public class about extends AppCompatActivity implements GestureDetector.OnGestureListener,View.OnTouchListener {
    @Override
    public boolean onTouch(View v, MotionEvent event) {
        return  this.detector.onTouchEvent(event);
    }

    GestureDetector detector = null;
    private Context mContext=null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        ActivityCollector.addActivity(this);
        detector = new GestureDetector(this,this);
        View v=findViewById(R.id.action_about);
        v.setOnTouchListener(this);
        mContext=about.this;
    }

    //销毁activity的时候出发的事件
    @Override
    protected void onDestroy() {
       super.onDestroy();
        ActivityCollector.removeActivity(this);
    }

    //打开我的博客
    public  void opneUrl(View view){
      //  Toast.makeText(this, "",Toast.LENGTH_LONG).show();
        Intent intent=new Intent( Intent.ACTION_VIEW);
        intent.setData(Uri.parse( "http://blog.coderstory.cn"));
        startActivity(intent);
    }

    @Override
    public boolean onDown(MotionEvent e) {
     //   Toast.makeText(this,"1",Toast.LENGTH_LONG).show();
        return true;
    }

    @Override
    public void onShowPress(MotionEvent e) {
      //  Toast.makeText(this,"2",Toast.LENGTH_LONG).show();
    }

    @Override
    public boolean onSingleTapUp(MotionEvent e) {
     //   Toast.makeText(this,"3",Toast.LENGTH_LONG).show();
        return true;
    }

    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
     //   Toast.makeText(this,"4",Toast.LENGTH_LONG).show();
        return true;
    }

    @Override
    public void onLongPress(MotionEvent e) {
      //  Toast.makeText(this,"5",Toast.LENGTH_LONG).show();
    }

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        if (e1.getX() - e2.getX() <100
                && Math.abs(velocityX) > 100) {
            this.finish();
        }
        return true;
    }


}

