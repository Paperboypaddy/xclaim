package codes.wasabi.xclaim.platform.spigot;

import codes.wasabi.xclaim.platform.PlatformChatListener;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class SpigotPlatformChatListener implements PlatformChatListener {

    private final List<Consumer<Data>> callbacks = new ArrayList<>();

    @Override
    public void onChat(Consumer<Data> cb) {
        callbacks.add(cb);
    }

    @Override
    public void unregister() {
        HandlerList.unregisterAll(this);
        callbacks.clear();
    }

    @EventHandler
    public void onMessage(AsyncPlayerChatEvent event) {
        Data data = new Data(
                event.getPlayer(),
                PlainTextComponentSerializer.plainText().serialize(LegacyComponentSerializer.legacySection().deserialize(event.getMessage())),
                () -> event.setCancelled(true)
        );
        for (Consumer<Data> consumer : callbacks) consumer.accept(data);
    }

}
