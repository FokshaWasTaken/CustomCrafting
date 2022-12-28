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

import com.wolfyscript.utilities.bukkit.gui.GuiCluster;
import com.wolfyscript.utilities.bukkit.gui.GuiUpdate;
import com.wolfyscript.utilities.bukkit.world.inventory.PlayerHeadUtils;
import com.wolfyscript.utilities.common.gui.ButtonInteractionResult;
import me.wolfyscript.customcrafting.CustomCrafting;
import me.wolfyscript.customcrafting.data.CCCache;
import org.bukkit.Material;

public class RecipeCreatorCraftingElite extends RecipeCreator {

    public static final String KEY = "elite_crafting";

    private static final String SETTINGS = "settings";

    public RecipeCreatorCraftingElite(GuiCluster<CCCache> cluster, CustomCrafting customCrafting) {
        super(cluster, KEY, 54, customCrafting);
    }

    @Override
    public void onInit() {
        super.onInit();
        var btnB = getButtonBuilder();
        for (int i = 0; i < 37; i++) {
            ButtonRecipeIngredient.register(getButtonBuilder(), i);
        }
        ButtonRecipeResult.register(getButtonBuilder());
        btnB.action(SETTINGS).state(s -> s.icon(Material.REDSTONE).action((holder, cache, btn, slot, details) -> {
            holder.getGuiHandler().openWindow(RecipeCreatorCraftingEliteSettings.KEY);
            return ButtonInteractionResult.cancel(true);
        })).register();
        btnB.toggle(ClusterRecipeCreator.SHAPELESS).stateFunction((holder, cache, slot) -> cache.getRecipeCreatorCache().getEliteCraftingCache().isShapeless()).enabledState(s -> s.cluster(getCluster()).subKey("enabled").icon(PlayerHeadUtils.getViaURL("f21d93da43863cb3759afefa9f7cc5c81f34d920ca97b7283b462f8b197f813")).action((holder, cache, btn, slot, details) -> {
            cache.getRecipeCreatorCache().getEliteCraftingCache().setShapeless(false);
            return ButtonInteractionResult.cancel(true);
        })).disabledState(s -> s.cluster(getCluster()).subKey("disabled").icon(PlayerHeadUtils.getViaURL("1aae7e8222ddbee19d184b97e79067814b6ba3142a3bdcce8b93099a312")).action((holder, cache, btn, slot, details) -> {
            cache.getRecipeCreatorCache().getEliteCraftingCache().setShapeless(true);
            return ButtonInteractionResult.cancel(true);
        })).register();
        btnB.toggle(ClusterRecipeCreator.MIRROR_HORIZONTAL).stateFunction((holder, cache, slot) -> cache.getRecipeCreatorCache().getEliteCraftingCache().isMirrorHorizontal()).enabledState(s -> s.cluster(getCluster()).subKey("enabled").icon(PlayerHeadUtils.getViaURL("956a3618459e43b287b22b7e235ec699594546c6fcd6dc84bfca4cf30ab9311")).action((holder, cache, btn, slot, details) -> {
            cache.getRecipeCreatorCache().getEliteCraftingCache().setMirrorHorizontal(false);
            return ButtonInteractionResult.cancel(true);
        })).disabledState(s -> s.cluster(getCluster()).subKey("disabled").icon(PlayerHeadUtils.getViaURL("956a3618459e43b287b22b7e235ec699594546c6fcd6dc84bfca4cf30ab9311")).action((holder, cache, btn, slot, details) -> {
            cache.getRecipeCreatorCache().getEliteCraftingCache().setMirrorHorizontal(true);
            return ButtonInteractionResult.cancel(true);
        })).register();
        btnB.toggle(ClusterRecipeCreator.MIRROR_VERTICAL).stateFunction((holder, cache, slot) -> cache.getRecipeCreatorCache().getEliteCraftingCache().isMirrorVertical()).enabledState(s -> s.cluster(getCluster()).subKey("enabled").icon(PlayerHeadUtils.getViaURL("882faf9a584c4d676d730b23f8942bb997fa3dad46d4f65e288c39eb471ce7")).action((holder, cache, btn, slot, details) -> {
            cache.getRecipeCreatorCache().getEliteCraftingCache().setMirrorVertical(false);
            return ButtonInteractionResult.cancel(true);
        })).disabledState(s -> s.cluster(getCluster()).subKey("disabled").icon(PlayerHeadUtils.getViaURL("882faf9a584c4d676d730b23f8942bb997fa3dad46d4f65e288c39eb471ce7")).action((holder, cache, btn, slot, details) -> {
            cache.getRecipeCreatorCache().getEliteCraftingCache().setMirrorVertical(true);
            return ButtonInteractionResult.cancel(true);
        })).register();
        btnB.toggle(ClusterRecipeCreator.MIRROR_ROTATION).stateFunction((holder, cache, slot) -> cache.getRecipeCreatorCache().getEliteCraftingCache().isMirrorRotation()).enabledState(s -> s.cluster(getCluster()).subKey("enabled").icon(PlayerHeadUtils.getViaURL("e887cc388c8dcfcf1ba8aa5c3c102dce9cf7b1b63e786b34d4f1c3796d3e9d61")).action((holder, cache, btn, slot, details) -> {
            cache.getRecipeCreatorCache().getEliteCraftingCache().setMirrorRotation(false);
            return ButtonInteractionResult.cancel(true);
        })).disabledState(s -> s.cluster(getCluster()).subKey("disabled").icon(PlayerHeadUtils.getViaURL("e887cc388c8dcfcf1ba8aa5c3c102dce9cf7b1b63e786b34d4f1c3796d3e9d61")).action((holder, cache, btn, slot, details) -> {
            cache.getRecipeCreatorCache().getEliteCraftingCache().setMirrorRotation(true);
            return ButtonInteractionResult.cancel(true);
        })).register();
    }

    @Override
    public void onUpdateAsync(GuiUpdate<CCCache> update) {
        super.onUpdateAsync(update);
        update.setButton(6, BACK);
        CCCache cache = update.getGuiHandler().getCustomCache();
        var cacheCraftingElite = cache.getRecipeCreatorCache().getEliteCraftingCache();

        if (!cacheCraftingElite.isShapeless()) {
            if (cacheCraftingElite.isMirrorHorizontal() && cacheCraftingElite.isMirrorVertical()) {
                update.setButton(33, ClusterRecipeCreator.MIRROR_ROTATION);
            }
            update.setButton(42, ClusterRecipeCreator.MIRROR_HORIZONTAL);
            update.setButton(51, ClusterRecipeCreator.MIRROR_VERTICAL);
        }

        int slot;
        for (int i = 0; i < 36; i++) {
            slot = i + (i / 6) * 3;
            update.setButton(slot, "recipe.ingredient_" + i);
        }
        update.setButton(25, "recipe.result");
        update.setButton(24, ClusterRecipeCreator.SHAPELESS);
        update.setButton(44, SETTINGS);

        if (cacheCraftingElite.isSaved()) {
            update.setButton(52, ClusterRecipeCreator.SAVE);
        }
        update.setButton(53, ClusterRecipeCreator.SAVE_AS);
    }

}
