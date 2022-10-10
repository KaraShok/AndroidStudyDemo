package com.karashok.demoglide.glide;

import com.karashok.demoglide.glide.cache.Key;
import com.karashok.demoglide.glide.load.LoadPath;
import com.karashok.demoglide.glide.load.codec.ResourceDecoder;
import com.karashok.demoglide.glide.load.codec.ResourceDecoderRegistry;
import com.karashok.demoglide.glide.load.model.ModelLoader;
import com.karashok.demoglide.glide.load.model.ModelLoaderRegistry;

import java.util.ArrayList;
import java.util.List;

/**
 * @author KaraShokZ.
 * @des
 * @since 07-21-2022
 */
public class Registry {

    private final ModelLoaderRegistry modelLoaderRegistry = new ModelLoaderRegistry();
    private final ResourceDecoderRegistry resourceDecoderRegistry = new ResourceDecoderRegistry();

    public <Model,Data> Registry add(Class<Model> source, Class<Data> data,
                                     ModelLoader.ModelLoaderaFactory<Model,Data> factory) {
        modelLoaderRegistry.add(source,data,factory);
        return this;
    }

    public <Model> List<ModelLoader<Model,?>> getModelLoaders(Model model) {
        Class<Model> modelClass = (Class<Model>) model.getClass();
        List<ModelLoader<Model, ?>> modelLoaders = modelLoaderRegistry.getModelLoaders(modelClass);
        return modelLoaders;
    }

    public List<ModelLoader.LoadData<?>> getLoadDatas(Object model) {
        List<ModelLoader.LoadData<?>> loadData = new ArrayList<>();
        List<ModelLoader<Object,?>> modelLoaders = getModelLoaders(model);
        for (ModelLoader<Object, ?> modelLoader : modelLoaders) {
            ModelLoader.LoadData<?> current = modelLoader.buildData(model);
            if (current != null) {
                loadData.add(current);
            }
        }
        return loadData;
    }

    public List<Key> getKeys(Object model) {
        List<Key> keys = new ArrayList<>();
        List<ModelLoader.LoadData<?>> loadDatas = getLoadDatas(model);
        for (ModelLoader.LoadData<?> loadData : loadDatas) {
            keys.add(loadData.key);
        }
        return keys;
    }

    public <T> void register(Class<T> dataClass, ResourceDecoder<T> decoder) {
        resourceDecoderRegistry.add(dataClass,decoder);
    }

    public boolean hasLoadPath(Class<?> dataClass) {
        return getLoadPath(dataClass) != null;
    }

    public <Data> LoadPath<Data> getLoadPath(Class<Data> dataClass) {
        List<ResourceDecoder<Data>> decoders = resourceDecoderRegistry.getDecoders(dataClass);
        return new LoadPath<>(dataClass,decoders);
    }
}
