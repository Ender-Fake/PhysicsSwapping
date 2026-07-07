package com.enderium.physicsswapping.client.render;

public class RenderContext {

    private static final ThreadLocal<RenderContext> CONTEXT = ThreadLocal.withInitial(RenderContext::new);

    public ItemAnimation last;

    public ItemAnimation getLast() {
        return last;
    }

    public void setLast(ItemAnimation animation) {
        last = animation;
    }

    public void clear() {
        last = null;
    }


    public static RenderContext get() {
        return CONTEXT.get();
    }

    public static ItemAnimation last() {
        return get().getLast();
    }

    public static void set(ItemAnimation animation) {
        get().setLast(animation);
    }

    public static void clearValues() {
        get().clear();
    }

}
