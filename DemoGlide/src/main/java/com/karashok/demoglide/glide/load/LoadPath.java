package com.karashok.demoglide.glide.load;

import android.graphics.Bitmap;

import com.karashok.demoglide.glide.load.codec.ResourceDecoder;

import java.util.List;

/**
 * @author KaraShokZ.
 * @des
 * @since 07-17-2022
 */
public class LoadPath<Data> {

    private final Class<Data> dataClass;
    private final List<ResourceDecoder<Data>> decoders;

    public LoadPath(Class<Data> dataClass, List<ResourceDecoder<Data>> decoders) {
        this.dataClass = dataClass;
        this.decoders = decoders;
    }

    public Bitmap runLoad(Data data, int width, int height) {
        Bitmap result = null;
        for (ResourceDecoder<Data> decoder : decoders) {
            try{
                if (decoder.handles(data)) {
                    result = decoder.decode(data,width,height);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (result != null) {
                break;
            }
        }
        return result;
    }
}
