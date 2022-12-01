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

package me.wolfyscript.customcrafting.utils.cooking;

import com.wolfyscript.utilities.bukkit.BukkitNamespacedKey;
import com.wolfyscript.utilities.tuple.Pair;
import me.wolfyscript.customcrafting.CustomCrafting;
import me.wolfyscript.customcrafting.recipes.data.CookingRecipeData;
import me.wolfyscript.customcrafting.utils.NamespacedKeyUtils;
import org.bukkit.block.Block;
import org.bukkit.block.Furnace;
import org.bukkit.event.inventory.FurnaceSmeltEvent;

public class PaperSmeltAPIAdapter extends SmeltAPIAdapter {

    public PaperSmeltAPIAdapter(CustomCrafting customCrafting, CookingManager manager) {
        super(customCrafting, manager);
    }

    @Override
    public Pair<CookingRecipeData<?>, Boolean> process(FurnaceSmeltEvent event, Block block, Furnace furnace) {
        var recipe = event.getRecipe();
        if (recipe != null && recipe.getKey().getNamespace().equals(NamespacedKeyUtils.NAMESPACE)) {
            return processRecipe(event.getSource(), BukkitNamespacedKey.fromBukkit(recipe.getKey()), block);
        }
        return new Pair<>(null, false);
    }
}
