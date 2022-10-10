package com.karashok.demoglide.glide.cache;

import com.karashok.demoglide.glide.cache.recycle.Resource;

/**
 * @author KaraShokZ.
 * @des
 * @since 07-03-2022
 */
public interface MemoryCache {

    void clearMemory();

    void trimMemory(int level);

    interface ResourceRemoveListener{
        void onResourceRemoved(Resource resource);
    }

    Resource put(Key key, Resource resource);

    void setResourceRemoveListener(ResourceRemoveListener listener);

    Resource removeResource(Key key);
}
