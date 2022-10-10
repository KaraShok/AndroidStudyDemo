package com.karashok.imgoptimization;

import android.app.ActivityManager;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.util.LruCache;

import com.karashok.imgoptimization.disk.DiskLruCache;

import java.io.File;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.ref.Reference;
import java.lang.ref.ReferenceQueue;
import java.lang.ref.WeakReference;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

/**
 * @author KaraShokZ.
 * @des
 * @since 06-15-2022
 */
public class ImageCache {

    private static ImageCache instance;
    private static Set<WeakReference<Bitmap>> reuseablePool;

    private Context context;
    private LruCache<String, Bitmap> memoryCache;
    private DiskLruCache diskLruCache;

    private BitmapFactory.Options options = new BitmapFactory.Options();

    private ReferenceQueue<Bitmap> referenceQueue;
    private Thread clearReferenceQueue;
    private boolean shutDown;

    public static ImageCache getInstance() {
        if (instance == null) {
            synchronized (ImageCache.class) {
                if (instance == null) {
                    instance = new ImageCache();
                }
            }
        }
        return instance;
    }

    private ImageCache() {
    }

    private ReferenceQueue<Bitmap> getReferenceQueue() {
        if (referenceQueue == null) {
            referenceQueue = new ReferenceQueue<>();
            clearReferenceQueue = new Thread(new Runnable() {
                @Override
                public void run() {
                    while (!shutDown) {
                        try{
                            Reference<? extends Bitmap> reference = referenceQueue.remove();
                            Bitmap bitmap = reference.get();
                            if (null != bitmap && !bitmap.isRecycled()) {
                                bitmap.recycle();
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            });
            clearReferenceQueue.start();
        }
        return referenceQueue;
    }

    public void init(Context context, String dir) {
        this.context = context.getApplicationContext();
        reuseablePool = Collections.synchronizedSet(new HashSet<WeakReference<Bitmap>>());
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        // 获取程序最大可用内存 单位是M
        int memoryClass = am.getMemoryClass();

        memoryCache = new LruCache<String, Bitmap>(memoryClass / 8 *1024 * 1024) {

            @Override
            protected int sizeOf(String key, Bitmap value) {
                // 19之前, 必需同等大小，才能复用  inSampleSize=1
                if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) {
                    return value.getAllocationByteCount();
                }
                return value.getByteCount();
            }

            @Override
            protected void entryRemoved(boolean evicted, String key, Bitmap oldValue,
                                        Bitmap newValue) {
                if (oldValue.isMutable()) {
                    reuseablePool.add(new WeakReference<>(oldValue,referenceQueue));
                } else {
                    oldValue.recycle();
                }
            }
        };

        try{
            diskLruCache = DiskLruCache.open(new File(dir),BuildConfig.VERSION_CODE,1,10 * 1024 * 1024);
        } catch (Exception e) {
            e.printStackTrace();
        }

        getReferenceQueue();
    }

    public void putBitmapToMemory(String key, Bitmap bitmap) {
        memoryCache.put(key, bitmap);
    }

    public void putBitmapToDisk(String key, Bitmap bitmap) {
        DiskLruCache.Snapshot snapshot = null;
        OutputStream os = null;
        try{
            snapshot = diskLruCache.get(key);
            if (null == snapshot) {
                DiskLruCache.Editor editor = diskLruCache.edit(key);
                if (null != editor) {
                    os = editor.newOutputStream(0);
                    bitmap.compress(Bitmap.CompressFormat.JPEG,50,os);
                    editor.commit();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally{
            if (null != snapshot) {
                snapshot.close();
            }
            if (null != os) {
                try{
                    os.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }
    public Bitmap getReuseable(int w, int h, int inSampleSize) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.HONEYCOMB) {
            return null;
        }
        Bitmap reuseable = null;
        Iterator<WeakReference<Bitmap>> iterator = reuseablePool.iterator();
        while (iterator.hasNext()) {
            Bitmap bitmap = iterator.next().get();
            if (null != bitmap) {
                if (checkInBitmap(bitmap,w,h,inSampleSize)) {
                    reuseable = bitmap;
                    iterator.remove();
                    break;
                }
            } else {
                iterator.remove();
            }
        }
        return reuseable;
    }

    private boolean checkInBitmap(Bitmap bitmap, int w, int h, int inSampleSize) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) {
            return bitmap.getWidth() == w && bitmap.getHeight() == w && inSampleSize == 1;
        }
        if (inSampleSize > 1) {
            w /= inSampleSize;
            h /= inSampleSize;
        }
        int byteCount = w * h * getPixelsCount(bitmap.getConfig());
        return byteCount <= bitmap.getByteCount();
    }

    private int getPixelsCount(Bitmap.Config config) {
        if (Bitmap.Config.ARGB_8888 == config) {
            return 4;
        } else {
            return 2;
        }
    }

    public Bitmap getBitmapFromMemory(String key) {
        return memoryCache.get(key);
    }

    public Bitmap getBitmapFromDisk(String key, Bitmap reuseable) {
        DiskLruCache.Snapshot snapshot = null;
        Bitmap bitmap = null;
        InputStream is = null;
        try{
            snapshot = diskLruCache.get(key);
            if (snapshot == null) {
                return null;
            }
            is = snapshot.getInputStream(0);
            options.inMutable = true;
            options.inBitmap = reuseable;
            bitmap = BitmapFactory.decodeStream(is,null,options);
            if (bitmap != null) {
                memoryCache.put(key,bitmap);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally{
            if (null != snapshot) {
                snapshot.close();
            }
            if (is != null) {
                try{
                    is.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return bitmap;
    }
}
