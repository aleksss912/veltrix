package com.example.dripclient.modules;

import com.example.dripclient.Category;
import com.example.dripclient.Module;
import com.example.dripclient.Setting;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.util.math.Box;

/** Powieksza hitbox innych graczy po stronie klienta i (opcjonalnie) pokazuje hitboxy. */
public class HitboxModule extends Module {
    private final Setting.Num size = add(new Setting.Num("Powiększenie", 0.3, 0.1, 1.0, 0.05));
    private final Setting.Bool show = add(new Setting.Bool("Pokaż hitboxy", true));

    private boolean savedRender = false;

    public HitboxModule() {
        super("Hitbox", Category.COMBAT);
    }

    @Override
    public void onEnable() {
        savedRender = MinecraftClient.getInstance().getEntityRenderDispatcher().shouldRenderHitboxes();
    }

    @Override
    public void onTick(MinecraftClient mc) {
        if (mc.world == null) return;
        double s = size.value;
        for (AbstractClientPlayerEntity p : mc.world.getPlayers()) {
            if (p == mc.player) continue;
            Box base = p.getDimensions(p.getPose()).getBoxAt(p.getPos());
            p.setBoundingBox(base.expand(s, s, s));
        }
        mc.getEntityRenderDispatcher().setRenderHitboxes(show.value);
    }

    @Override
    public void onDisable() {
        MinecraftClient mc = MinecraftClient.getInstance();
        mc.getEntityRenderDispatcher().setRenderHitboxes(savedRender);
        if (mc.world == null) return;
        for (AbstractClientPlayerEntity p : mc.world.getPlayers()) {
            p.setBoundingBox(p.getDimensions(p.getPose()).getBoxAt(p.getPos()));
        }
    }
}
