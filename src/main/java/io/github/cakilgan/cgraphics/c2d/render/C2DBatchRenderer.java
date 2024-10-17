package io.github.cakilgan.cgraphics.c2d.render;

import io.github.cakilgan.cgraphics.c2d.render.sprite.C2DSprite;
import io.github.cakilgan.engine.system.CESceneComponent;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class C2DBatchRenderer implements CESceneComponent {
    private final int MAX_BATCH_SIZE = 1000;
    private List<C2DRenderBatch> batches;

    public List<C2DRenderBatch> getBatches() {
        return batches;
    }

    public C2DBatchRenderer() {
        this.batches = new ArrayList<>();
    }


    public void add(C2DSprite sprite) {
        boolean added = false;
        for (C2DRenderBatch batch : batches) {
            if (Arrays.asList(batch.getSprites()).contains(sprite)){
                return;
            }
            if (batch.hasRoom()&&batch.getZ_i()==sprite.getZPos()) {
                C2DTexture texture = sprite.getTexture();
                if (texture == null || (batch.haveTexture(texture) || batch.canAddSprite())){
                    batch.addSprite(sprite);
                    added = true;
                    break;
                }
            }
        }
        if (!added) {
            C2DRenderBatch newBatch = new C2DRenderBatch(MAX_BATCH_SIZE,sprite.getZPos());
            newBatch.start();
            batches.add(newBatch);
            newBatch.addSprite(sprite);
            Collections.sort(batches);
        }
    }

    public void render() {
        for (C2DRenderBatch batch : batches) {
            batch.render();
        }
    }


    @Override
    public void init() {

    }

    @Override
    public void update(double dt) {
        render();
    }

    @Override
    public void dispose() {

    }
}
