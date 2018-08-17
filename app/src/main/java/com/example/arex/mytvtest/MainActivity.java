package com.example.arex.mytvtest;

import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;

public class MainActivity extends FragmentActivity {
    private MainViewPager mViewPagerFragment;
    private static final String TAG = "key";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FragmentManager fm = getSupportFragmentManager();
        mViewPagerFragment = new MainViewPager();
        fm.beginTransaction().add(R.id.frag_container, mViewPagerFragment).commit();
    }

    public void setViewPagerPage(int pageIndex) {
        mViewPagerFragment.setPage(pageIndex);
    }

    public void setViewPagerNext() {
        mViewPagerFragment.setNextPage();
    }

    public void setViewPagerPrev() {
        mViewPagerFragment.setPrevPage();
    }

    @Override
//    public boolean onKeyDown(int keyCode, KeyEvent event) {
    public boolean dispatchKeyEvent(KeyEvent event) {
        if (event.getAction() == KeyEvent.ACTION_DOWN) {
            int keyCode = event.getKeyCode();
            Log.d(TAG, String.valueOf(keyCode));
            switch (keyCode) {

                case KeyEvent.KEYCODE_ENTER:     //确定键enter
                    Log.d(TAG, "center--->");
                    break;
                case KeyEvent.KEYCODE_DPAD_CENTER:
                    Log.d(TAG, "center--->");
                    break;
                case KeyEvent.KEYCODE_BACK:    //返回键
                    Log.d(TAG, "back--->");
                    return true;   //这里由于break会退出，所以我们自己要处理掉 不返回上一层
                case KeyEvent.KEYCODE_SETTINGS: //设置键
                    Log.d(TAG, "setting--->");
                    break;
                case KeyEvent.KEYCODE_DPAD_DOWN:   //向下键

                    /*    实际开发中有时候会触发两次，所以要判断一下按下时触发 ，松开按键时不触发
                     *    exp:KeyEvent.ACTION_UP
                     */
                    if (event.getAction() == KeyEvent.ACTION_DOWN) {

                        Log.d(TAG, "down--->");
                    }
                    break;
                case KeyEvent.KEYCODE_DPAD_UP:   //向上键
                    Log.d(TAG, "up--->");
                    break;
                case KeyEvent.KEYCODE_DPAD_LEFT: //向左键
                    setViewPagerPrev();
                    Log.d(TAG, "left--->");
                    break;
                case KeyEvent.KEYCODE_DPAD_RIGHT:  //向右键
                    setViewPagerNext();
                    Log.d(TAG, "right--->");
                    break;
                default:
                    break;
            }
        }
        return super.dispatchKeyEvent(event);
    }

//    @Override
//    public boolean dispatchKeyEvent(KeyEvent event) {
//        Log.d(TAG, event.toString());
////        if (event.getAction() == KeyEvent.ACTION_DOWN) {
////            // keydown logic
////            return true;
////        }
//        return false;
//    }
}
