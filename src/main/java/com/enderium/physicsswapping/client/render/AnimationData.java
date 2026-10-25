package com.enderium.physicsswapping.client.render;

import com.enderium.physicsswapping.client.config.data.DualFloatRange;
import com.enderium.physicsswapping.client.config.data.DualIntRange;
import com.enderium.physicsswapping.client.config.data.ref.BooleanRef;
import com.enderium.physicsswapping.client.config.data.ref.FloatRef;

public record AnimationData(
        DualFloatRange duration,
        DualIntRange bounce,
        DualFloatRange yOffset,
        DualFloatRange angle,
        DualFloatRange itemScale,
        BooleanRef squashStretch,
        FloatRef squashStretchX,
        FloatRef squashStretchY
) {


    public void setFrom(AnimationData data) {
        duration.set(data.duration);
        bounce.set(data.bounce);
        yOffset.set(data.yOffset);
        angle.set(data.angle);
        itemScale.set(data.itemScale);
        squashStretch.value = data.squashStretch.value;
        squashStretchX.value = data.squashStretchX.value;
        squashStretchY.value = data.squashStretchY.value;
    }

}
