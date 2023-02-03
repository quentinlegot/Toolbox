package fr.altarik.toolbox.task.syncTasks;

import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.minecraft.server.MinecraftServer;

public class ServerTickListener {

    private final PeriodicSyncTask task;

    public ServerTickListener(PeriodicSyncTask syncTask) {
        this.task = syncTask;
        ServerTickEvents.START_SERVER_TICK.register(this::onServerTick);
    }

    private void onServerTick(MinecraftServer minecraftServer) {

    }

}
