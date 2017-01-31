//package com.example.android.graduationdemo;
//
//import android.app.Application;
//import android.graphics.drawable.Drawable;
//import android.net.Uri;
//import android.util.Log;
//import android.widget.ImageView;
//
//import com.mikepenz.materialdrawer.util.AbstractDrawerImageLoader;
//import com.mikepenz.materialdrawer.util.DrawerImageLoader;
//import com.squareup.picasso.Picasso;
//
///**
// * Created by Abshafi on 1/30/2017.
// */
//
//public class CustomApplication extends Application {
//    @Override
//    public void onCreate() {
//        super.onCreate();
//
//        //initialize and create the image loader logic
//        DrawerImageLoader.init(new AbstractDrawerImageLoader() {
//            @Override
//            public void set(ImageView imageView, Uri uri, Drawable placeholder) {
//                Picasso.with(imageView.getContext()).load(uri).placeholder(placeholder).into(imageView);
//                Log.e("CustomApplication",uri.toString());
//            }
//
//            @Override
//            public void cancel(ImageView imageView) {
//                Picasso.with(imageView.getContext()).cancelRequest(imageView);
//            }
//        });
//
//    }
//}
