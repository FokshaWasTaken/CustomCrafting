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

package me.wolfyscript.customcrafting.gui.recipe_creator;

import com.wolfyscript.utilities.bukkit.gui.button.ButtonItemInput;
import com.wolfyscript.utilities.bukkit.gui.button.ButtonState;
import com.wolfyscript.utilities.bukkit.world.inventory.ItemUtils;
import com.wolfyscript.utilities.bukkit.world.items.CustomItem;
import me.wolfyscript.customcrafting.data.CCCache;
import me.wolfyscript.customcrafting.data.cache.items.ApplyItem;
import me.wolfyscript.customcrafting.recipes.items.Result;
import org.bukkit.Material;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;

class ButtonContainerItemResult extends ButtonItemInput<CCCache> {

    private static final ApplyItem APPLY_ITEM = (items, cache, customItem) -> cache.getRecipeCreatorCache().getRecipeCache().getResult().put(items.getVariantSlot(), CustomItem.getReferenceByItemStack(customItem.create()));

    ButtonContainerItemResult(int variantSlot) {
        super("variant_container_" + variantSlot, new ButtonState<>("", Material.AIR, (cache, guiHandler, player, inventory, slot, event) -> {
            if (event instanceof InventoryClickEvent clickEvent && clickEvent.getClick().equals(ClickType.SHIFT_RIGHT)) {
                if (!ItemUtils.isAirOrNull(inventory.getItem(slot))) {
                    cache.getItems().setVariant(variantSlot, CustomItem.getReferenceByItemStack(inventory.getItem(slot)));
                    cache.setApplyItem(APPLY_ITEM);
                    guiHandler.openWindow(ClusterRecipeCreator.ITEM_EDITOR);
                }
                return true;
            }
            return false;
        }, (cache, guiHandler, player, guiInventory, itemStack, i, event) -> {
            if (event instanceof InventoryClickEvent clickEvent && clickEvent.getClick().equals(ClickType.SHIFT_RIGHT)) {
                return;
            }
            cache.getRecipeCreatorCache().getRecipeCache().getResult().put(variantSlot, !ItemUtils.isAirOrNull(itemStack) ? CustomItem.getReferenceByItemStack(itemStack) : null);
        }, null, (hashMap, cache, guiHandler, player, inventory, itemStack, slot, help) -> {
            Result result = cache.getRecipeCreatorCache().getRecipeCache().getResult();
            return result != null ? result.getItemStack(variantSlot) : ItemUtils.AIR;
        }));
    }
}
