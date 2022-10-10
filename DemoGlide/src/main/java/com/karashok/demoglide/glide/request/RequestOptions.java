package com.karashok.demoglide.glide.request;

/**
 * @author KaraShokZ.
 * @des
 * @since 07-19-2022
 */
public class RequestOptions {

    private int errorId;
    private int placeHolderId;
    private int overrideHeight = -1;
    private int overrideWidth = -1;

    public RequestOptions placeHolder(int placeHolderId) {
        this.placeHolderId = placeHolderId;
        return this;
    }

    public RequestOptions error(int errorId) {
        this.errorId = errorId;
        return this;
    }

    public RequestOptions override(int width, int height) {
        this.overrideWidth = width;
        this.overrideHeight = height;
        return this;
    }

    public final int getErrorId() {
        return errorId;
    }

    public final int getPlaceHolderId() {
        return placeHolderId;
    }

    public final int getOverrideHeight() {
        return overrideHeight;
    }

    public final int getOverrideWidth() {
        return overrideWidth;
    }
}
