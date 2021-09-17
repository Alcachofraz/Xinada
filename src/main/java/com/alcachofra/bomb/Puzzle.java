package com.alcachofra.bomb;

import com.alcachofra.main.Language;
import com.alcachofra.main.Xinada;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.event.Listener;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.util.Objects;
import java.util.Random;

public class Puzzle implements Listener {
    private final Inventory inventory;
    private final int size;
    private boolean solved = false;
    private BukkitTask task;

    public Puzzle(String title, int rows) {
        inventory = Bukkit.createInventory(null, rows*9, title);
        size = rows*9;

        Random rand = new Random();

        int n;
        Material color;

        for (int i = 0; i < rows*9; i++) {
            n = rand.nextInt(9);
            color = (n>7) ? Material.LIME_STAINED_GLASS_PANE : Material.RED_STAINED_GLASS_PANE;
            ItemStack item = new ItemStack(color, 1);
            ItemMeta itemMeta = item.getItemMeta();
            if (itemMeta != null) {
                itemMeta.setDisplayName(Language.getRoleString("135"));
                item.setItemMeta(itemMeta);
            }
            inventory.setItem(i, item);
        }
    }

    public void start() {
        int rate = Xinada.getPlugin().getConfig().getInt("game.terroristFuseRate");
        Random rand = new Random();
        task = new BukkitRunnable() {
            public void run() {
                Objects.requireNonNull(getInventory().getItem(rand.nextInt(size))).setType(Material.RED_STAINED_GLASS_PANE);
            }
        }.runTaskTimer(Xinada.getPlugin(), rate, rate); // 20 ticks = 1 second
    }

    public void stop() {
        task.cancel();
    }

    public Inventory getInventory() {
        return inventory;
    }

    public int getSize() {
        return size;
    }

    public void setSolved(boolean solved) {
        this.solved = solved;
    }

    public boolean isSolved() {
        if (solved) return true;
        for (ItemStack item : inventory.getContents()) {
            if (item.getType() == Material.RED_STAINED_GLASS_PANE) return false;
        }
        return (solved = true);
    }
}
