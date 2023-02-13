package fr.altarik.toolbox.task;

import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.minecraft.server.MinecraftServer;

public class ServerTickListener {

    private final Runnable task;

    public ServerTickListener(Runnable syncTask) {
        this.task = syncTask;
        ServerTickEvents.START_SERVER_TICK.register(this::onServerTick);
    }

    private void onServerTick(MinecraftServer minecraftServer) {
        task.run();
    }

}
