package com.karashok.demoglide.glide.load.model;

import com.karashok.demoglide.glide.load.model.ModelLoader;

import java.util.List;

/**
 * @author KaraShokZ.
 * @des
 * @since 07-17-2022
 */
public class MultiModelLoader<Model, Data> implements ModelLoader<Model, Data> {

    private final List<ModelLoader<Model, Data>> modelLoaders;

    public MultiModelLoader(List<ModelLoader<Model, Data>> modelLoaders) {
        this.modelLoaders = modelLoaders;
    }

    @Override
    public boolean handles(Model model) {
        for (ModelLoader<Model, Data> modelLoader : modelLoaders) {
            if (modelLoader.handles(model)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public LoadData<Data> buildData(Model model) {
        for (ModelLoader<Model, Data> modelLoader : modelLoaders) {
            if (modelLoader.handles(model)) {
                return modelLoader.buildData(model);
            }
        }
        return null;
    }
}
