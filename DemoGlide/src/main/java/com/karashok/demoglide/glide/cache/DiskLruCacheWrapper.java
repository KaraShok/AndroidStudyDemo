package com.karashok.demoglide.glide.cache;

import android.content.Context;

import com.karashok.demoglide.glide.GlideUtils;
import com.karashok.demoglide.glide.disklrucache.DiskLruCache;

import java.io.File;
import java.security.MessageDigest;

/**
 * @author KaraShokZ.
 * @des
 * @since 07-11-2022
 */
public class DiskLruCacheWrapper implements DiskCache {

    final static int DEFAULT_DISK_CACHE_SIZE = 250 * 1024 * 1024;
    final static String DEFAULT_DISK_CACHE_DIR = "image_manager_disk_cache";

    private MessageDigest messageDigest;
    private DiskLruCache diskLruCache;

    public DiskLruCacheWrapper(Context context) {
        this(new File(context.getCacheDir(),DEFAULT_DISK_CACHE_DIR),DEFAULT_DISK_CACHE_SIZE);
    }

    public DiskLruCacheWrapper(File directory, long maxSize) {
        try{
            messageDigest = MessageDigest.getInstance("SHA-256");

            //打开一个缓存目录，如果没有则首先创建它，
            // directory：指定数据缓存地址
            // appVersion：APP版本号，当版本号改变时，缓存数据会被清除
            // valueCount：同一个key可以对应多少文件
            // maxSize：最大可以缓存的数据量
            diskLruCache = DiskLruCache.open(directory,1,1,maxSize);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private String getKeyString(Key key) {
        key.updateDiskCacheKey(messageDigest);
        return GlideUtils.sha256BytesToHex(messageDigest.digest());
    }


    @Override
    public File get(Key key) {
        String k = getKeyString(key);
        File result = null;
        try{
            DiskLruCache.Value value = diskLruCache.get(k);
            if (value != null) {
                result  = value.getFile(0);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    @Override
    public void put(Key key, Writer writer) {
        String k = getKeyString(key);
        DiskLruCache.Editor edit = null;
        try{
            DiskLruCache.Value value = diskLruCache.get(k);
            if (value != null) {
                return;
            }
            edit = diskLruCache.edit(k);
            File file = edit.getFile(0);
            if (writer.write(file)) {
                edit.commit();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally{
            if (edit != null) {
                edit.abortUnlessCommitted();
            }
        }
    }

    @Override
    public void delete(Key key) {
        String k = getKeyString(key);
        try{
            diskLruCache.remove(k);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void clear() {
        try{
            diskLruCache.delete();
        } catch (Exception e) {
            e.printStackTrace();
        } finally{
            diskLruCache = null;
        }
    }
}
