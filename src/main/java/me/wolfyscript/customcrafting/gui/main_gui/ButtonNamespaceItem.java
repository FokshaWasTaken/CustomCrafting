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

package me.wolfyscript.customcrafting.gui.main_gui;

import me.wolfyscript.customcrafting.data.CCCache;
import me.wolfyscript.customcrafting.data.cache.items.ItemsButtonAction;
import com.wolfyscript.utilities.bukkit.gui.button.ButtonState;
import com.wolfyscript.utilities.bukkit.gui.button.ButtonAction;
import org.bukkit.Material;

class ButtonNamespaceItem extends ButtonAction<CCCache> {

    ButtonNamespaceItem(String namespace) {
        super("namespace_" + namespace, new ButtonState<>("namespace", Material.ENDER_CHEST, (ItemsButtonAction) (cache, items, guiHandler, player, inventory, i, event) -> {
            items.setListNamespace(namespace);
            return true;
        }, (values, cache, guiHandler, player, inventory, itemStack, slot, help) -> {
            values.put("%namespace%", namespace);
            return itemStack;
        }));
    }

}
