/*
 *       ____ _  _ ____ ___ ____ _  _ ____ ____ ____ ____ ___ _ _  _ ____
 *       |    |  | [__   |  |  | |\/| |    |__/ |__| |___  |  | |\ | | __
 *       |___ |__| ___]  |  |__| |  | |___ |  \ |  | |     |  | | \| |__]
 *
 *       CustomCrafting Recipe creation and management tool for Minecraft
 *                      Copyright (C) 2021  WolfyScript
 *
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package me.wolfyscript.customcrafting.gui.item_creator.tabs;

import com.wolfyscript.utilities.bukkit.BukkitNamespacedKey;
import com.wolfyscript.utilities.bukkit.WolfyUtilsBukkit;
import com.wolfyscript.utilities.bukkit.gui.GuiUpdate;
import com.wolfyscript.utilities.bukkit.world.inventory.ItemUtils;
import com.wolfyscript.utilities.bukkit.world.items.CustomItem;
import com.wolfyscript.utilities.common.gui.ButtonInteractionResult;
import me.wolfyscript.customcrafting.data.CCCache;
import me.wolfyscript.customcrafting.data.cache.items.Items;
import me.wolfyscript.customcrafting.gui.item_creator.MenuItemCreator;
import me.wolfyscript.customcrafting.utils.NamespacedKeyUtils;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class TabUnbreakable extends ItemCreatorTabVanilla {

    public static final String KEY = "unbreakable";

    public TabUnbreakable() {
        super(new BukkitNamespacedKey(NamespacedKeyUtils.NAMESPACE, KEY));
    }

    @Override
    public void register(MenuItemCreator creator, WolfyUtilsBukkit api) {
        creator.getButtonBuilder().toggle(KEY).stateFunction((holder, cache, slot) -> {
            CustomItem item = cache.getItems().getItem();
            return !ItemUtils.isAirOrNull(item) && item.getItemMeta().isUnbreakable();
        }).enabledState(state -> state.subKey("enabled").icon(Material.BEDROCK).action((holder, cache, btn, slot, details) -> {
            var items = cache.getItems();
            var itemMeta = items.getItem().getItemMeta();
            itemMeta.setUnbreakable(false);
            items.getItem().setItemMeta(itemMeta);
            return ButtonInteractionResult.cancel(true);
        })).disabledState(state -> state.subKey("disabled").icon(Material.GLASS).action((holder, cache, btn, slot, details) -> {
            var items = cache.getItems();
            var itemMeta = items.getItem().getItemMeta();
            itemMeta.setUnbreakable(true);
            items.getItem().setItemMeta(itemMeta);
            return ButtonInteractionResult.cancel(true);
        })).register();
    }

    @Override
    public String getOptionButton() {
        return KEY;
    }

    @Override
    public boolean shouldRender(GuiUpdate<CCCache> update, CCCache cache, Items items, CustomItem customItem, ItemStack item) {
        return super.shouldRender(update, cache, items, customItem, item) && !ItemUtils.isAirOrNull(item);
    }

    @Override
    public void render(GuiUpdate<CCCache> update, CCCache cache, Items items, CustomItem customItem, ItemStack item) {
        update.setButton(30, KEY + ".set");
        update.setButton(32, KEY + ".remove");
    }
}
