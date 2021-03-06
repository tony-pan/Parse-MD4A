package com.asha.md360player4android;

import android.content.ContentResolver;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.DrawableRes;
import android.util.Log;
import android.view.View;

import com.asha.vrlib.SharkLibrary;
import com.asha.vrlib.model.SharkRay;
import com.asha.vrlib.plugins.ISharkHotspot;
import com.asha.vrlib.texture.SharkBitmapTexture;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import static com.squareup.picasso.MemoryPolicy.NO_CACHE;
import static com.squareup.picasso.MemoryPolicy.NO_STORE;

/**
 * Created by hzqiujiadi on 16/4/5.
 * hzqiujiadi ashqalcn@gmail.com
 */
public class BitmapBasePlayActivity extends SharkBasePlayActivity {

    private static final String TAG = "BitmapBasePlayActivity";

    private Uri nextUri;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        findViewById(R.id.control_next).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                busy();
                nextUri = getDrawableUri(R.drawable.texture);
                getVRLibrary().notifyPlayerChanged();
            }
        });
    }

    private Target mTarget;// keep the reference for picasso.

    private void loadImage(Uri uri, final SharkBitmapTexture.Callback callback){
        mTarget = new Target() {
            @Override
            public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                // notify if size changed
                getVRLibrary().onTextureResize(bitmap.getWidth(),bitmap.getHeight());

                // texture
                callback.texture(bitmap);
                cancelBusy();
            }

            @Override
            public void onBitmapFailed(Drawable errorDrawable) {

            }

            @Override
            public void onPrepareLoad(Drawable placeHolderDrawable) {

            }
        };
        Picasso.with(getApplicationContext()).load(uri).resize(3072,2048).centerInside().memoryPolicy(NO_CACHE, NO_STORE).into(mTarget);
    }

    private Uri currentUri(){
        if (nextUri == null){
            return getUri();
        } else {
            return nextUri;
        }
    }

    @Override
    protected SharkLibrary createSharkLibrary() {
        return SharkLibrary.with(this)
                .displayMode(SharkLibrary.DISPLAY_MODE_NORMAL)
                .interactiveMode(SharkLibrary.INTERACTIVE_MODE_TOUCH)
                .asBitmap(new SharkLibrary.IBitmapProvider() {
                    @Override
                    public void onProvideBitmap(final SharkBitmapTexture.Callback callback) {
                        loadImage(currentUri(), callback);
                    }
                })
                .listenTouchPick(new SharkLibrary.ITouchPickListener() {
                    @Override
                    public void onHotspotHit(ISharkHotspot hitHotspot, SharkRay ray) {
                        Log.d(TAG,"Ray:" + ray + ", hitHotspot:" + hitHotspot);
                    }
                })
                .pinchEnabled(true)
                .projectionFactory(new CustomProjectionFactory())
                .build(R.id.gl_view);
    }

    private Uri getDrawableUri(@DrawableRes int resId){
        Resources resources = getResources();
        return Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE + "://" + resources.getResourcePackageName(resId) + '/' + resources.getResourceTypeName(resId) + '/' + resources.getResourceEntryName(resId) );
    }
}
