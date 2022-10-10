package com.karashok.demoglide.glide.load.codec;

import java.util.ArrayList;
import java.util.List;

/**
 * @author KaraShokZ.
 * @des
 * @since 07-16-2022
 */
public class ResourceDecoderRegistry {

    private final List<Entry<?>> entries = new ArrayList<>();

    public <Data> List<ResourceDecoder<Data>> getDecoders(Class<Data> dataClass) {
        List<ResourceDecoder<Data>> decoders = new ArrayList<>();
        for (Entry<?> entry : entries) {
            if (entry.handles(dataClass)) {
                decoders.add((ResourceDecoder<Data>) entry.decoder);
            }
        }
        return decoders;
    }

    public <T> void add(Class<T> dataClass, ResourceDecoder<T> decoder) {
        entries.add(new Entry<>(dataClass,decoder));
    }

    private static class Entry<T> {

        private final Class<T> dataClass;
        final ResourceDecoder<T> decoder;

        public Entry(Class<T> dataClass, ResourceDecoder<T> decoder) {
            this.dataClass = dataClass;
            this.decoder = decoder;
        }

        public boolean handles(Class<?> dataClass) {
            //判定此 Class 对象所表示的类或接口与指定的 Class 参数所表示的类或接口是否相同，或是否是其超类或超接口
            // A.isAssignableFrom(B) B和A是同一个类型 或者 B是A的子类
            return this.dataClass.isAssignableFrom(dataClass);
        }
    }
}
