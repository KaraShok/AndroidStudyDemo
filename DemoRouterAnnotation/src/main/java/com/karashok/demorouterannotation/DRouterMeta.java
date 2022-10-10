package com.karashok.demorouterannotation;

import javax.lang.model.element.Element;

/**
 * @author KaraShokZ.
 * @des
 * @since 05-27-2022
 */
public class DRouterMeta {

    public enum Type {
        ACTIVITY,
        ISERVICE
    }

    public static DRouterMeta build(Type type, Class<?> destination, String path, String
            group) {
        return new DRouterMeta(type, null, destination, path, group);
    }

    private Type type;

    /**
     * 节点 (Activity)
     */
    private Element element;

    /**
     * 注解使用的类对象
     */
    private Class<?> destination;

    private String path;

    private String group;

    public DRouterMeta(Type type, Element element, Class<?> destination, Router route) {
        this.type = type;
        this.element = element;
        this.destination = destination;
        this.path = route.path();
        this.group = route.group();
    }

    public DRouterMeta(Type type, Element element, Class<?> destination, String path,
                       String group) {
        this.type = type;
        this.element = element;
        this.destination = destination;
        this.path = path;
        this.group = group;
    }

    public DRouterMeta() {
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public Element getElement() {
        return element;
    }

    public void setElement(Element element) {
        this.element = element;
    }

    public Class<?> getDestination() {
        return destination;
    }

    public void setDestination(Class<?> destination) {
        this.destination = destination;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }
}
