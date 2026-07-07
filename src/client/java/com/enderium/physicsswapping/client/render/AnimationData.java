package com.enderium.physicsswapping.client.render;

import com.enderium.physicsswapping.client.config.data.DualFloatRange;
import com.enderium.physicsswapping.client.config.data.DualIntRange;

public record AnimationData(
        DualFloatRange duration,
        DualIntRange bounce,
        DualFloatRange yOffset,
        DualFloatRange angle,
        DualFloatRange itemScale
) {


    public void setFrom(AnimationData data) {
        duration.set(data.duration);
        bounce.set(data.bounce);
        yOffset.set(data.yOffset);
        angle.set(data.angle);
        itemScale.set(data.itemScale);
    }

}
