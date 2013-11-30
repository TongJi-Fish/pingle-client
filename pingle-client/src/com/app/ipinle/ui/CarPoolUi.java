package com.app.ipinle.ui;

import java.util.ArrayList;

import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.app.ipinle.base.BaseUi;
import com.example.ipingle.R;

public class CarPoolUi extends BaseUi {

	 private ViewPager viewPager;  
	 private ArrayList<View> pageViews;  
	 private ImageView imageView;  
	 private ImageView[] imageViews; 
	 // 包裹滑动图片LinearLayout
	 private ViewGroup main;
	 // 包裹小圆点的LinearLayout
	 private ViewGroup group;
	 
	@Override
	protected void onCreate(Bundle savedInstanceState){
		//setContentView(R.layout.ui_carpool);
		super.onCreate(savedInstanceState);

        LayoutInflater inflater = getLayoutInflater();  
        View v1 = inflater.inflate(R.layout.item01, null);
        View v2 = inflater.inflate(R.layout.item02, null);
        View v3 = inflater.inflate(R.layout.item03, null);
        
        pageViews = new ArrayList<View>();  
        pageViews.add(v1);  
        pageViews.add(v2);  
        pageViews.add(v3);  
        
        imageViews = new ImageView[pageViews.size()];  
        main = (ViewGroup)inflater.inflate(R.layout.ui_carpool, null);  
        
        group = (ViewGroup)main.findViewById(R.id.viewGroup);  
        viewPager = (ViewPager)main.findViewById(R.id.guidePages);
        
        for (int i = 0; i < pageViews.size(); i++) {  
            imageView = new ImageView(CarPoolUi.this);  
            imageView.setLayoutParams(new LayoutParams(20,20));  
            imageView.setPadding(20, 0, 20, 0);  
            imageViews[i] = imageView;  
            
            if (i == 0) {  
                //默认选中第一张图片
                imageViews[i].setBackgroundResource(R.drawable.page_indicator_focused);  
            } else {  
                imageViews[i].setBackgroundResource(R.drawable.page_indicator);  
            }  
            
            group.addView(imageViews[i]);  
        }  
	}
    
    // 事件监听器
    private class TextViewOnClickListener implements OnClickListener{
    	@Override
    	public void onClick(View v) {
    		// TODO Auto-generated method stub
    		Toast.makeText(CarPoolUi.this, ((TextView) v).getText(), Toast.LENGTH_LONG).show();
    	}
    }
    
    // 指引页面数据适配器
    private class GuidePageAdapter extends PagerAdapter {  
  	  
        @Override  
        public int getCount() {  
            return pageViews.size();  
        }  
  
        @Override  
        public boolean isViewFromObject(View arg0, Object arg1) {  
            return arg0 == arg1;  
        }  
  
        @Override  
        public int getItemPosition(Object object) {  
            // TODO Auto-generated method stub  
            return super.getItemPosition(object);  
        }  
  
        @Override  
        public void destroyItem(View arg0, int arg1, Object arg2) {  
            // TODO Auto-generated method stub  
            ((ViewPager) arg0).removeView(pageViews.get(arg1));  
        }  
  
        @Override  
        public Object instantiateItem(View arg0, int arg1) {  
            // TODO Auto-generated method stub  
            ((ViewPager) arg0).addView(pageViews.get(arg1));  
            return pageViews.get(arg1);  
        }  
  
        @Override  
        public void restoreState(Parcelable arg0, ClassLoader arg1) {  
            // TODO Auto-generated method stub  
  
        }  
  
        @Override  
        public Parcelable saveState() {  
            // TODO Auto-generated method stub  
            return null;  
        }  
  
        @Override  
        public void startUpdate(View arg0) {  
            // TODO Auto-generated method stub  
  
        }  
  
        @Override  
        public void finishUpdate(View arg0) {  
            // TODO Auto-generated method stub  
  
        }  
    } 
    
    // 指引页面更改事件监听器
    private class GuidePageChangeListener implements OnPageChangeListener {  
    	  
        @Override  
        public void onPageScrollStateChanged(int arg0) {  
            // TODO Auto-generated method stub  
  
        }  
  
        @Override  
        public void onPageScrolled(int arg0, float arg1, int arg2) {  
            // TODO Auto-generated method stub  
  
        }  
  
        @Override  
        public void onPageSelected(int arg0) {  
            for (int i = 0; i < imageViews.length; i++) {  
                imageViews[arg0].setBackgroundResource(R.drawable.page_indicator_focused);
                
                if (arg0 != i) {  
                    imageViews[i].setBackgroundResource(R.drawable.page_indicator);  
                }  
            }
        }  
    }  
}
