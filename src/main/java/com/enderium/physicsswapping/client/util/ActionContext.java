package com.enderium.physicsswapping.client.util;

public enum ActionContext {
    NONE(false),
    CLICK_TO(true,true),
    CLICK_FROM(false,true),
    RECIPE(false);

    public final boolean toInventory;
    public final boolean isClick;

    ActionContext(boolean toInventory, boolean click){
        this.toInventory=toInventory;
        this.isClick= click;
    }
    ActionContext(boolean toInventory){
        this.toInventory=toInventory;
        this.isClick=false;
    }

}
