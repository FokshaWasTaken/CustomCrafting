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
import com.wolfyscript.utilities.bukkit.WolfyCoreBukkit;
import com.wolfyscript.utilities.bukkit.WolfyUtilsBukkit;
import com.wolfyscript.utilities.bukkit.compatibility.plugins.ItemsAdderIntegration;
import com.wolfyscript.utilities.bukkit.compatibility.plugins.itemsadder.CustomStack;
import com.wolfyscript.utilities.bukkit.compatibility.plugins.itemsadder.ItemsAdderRef;
import com.wolfyscript.utilities.bukkit.gui.GuiUpdate;
import com.wolfyscript.utilities.bukkit.world.inventory.PlayerHeadUtils;
import com.wolfyscript.utilities.bukkit.world.items.CustomItem;
import com.wolfyscript.utilities.common.gui.ButtonInteractionResult;
import me.wolfyscript.customcrafting.CustomCrafting;
import me.wolfyscript.customcrafting.configs.custom_data.EliteWorkbenchData;
import me.wolfyscript.customcrafting.configs.customitem.EliteCraftingTableSettings;
import me.wolfyscript.customcrafting.data.CCCache;
import me.wolfyscript.customcrafting.data.cache.items.Items;
import me.wolfyscript.customcrafting.gui.item_creator.ButtonOption;
import me.wolfyscript.customcrafting.gui.item_creator.MenuItemCreator;
import me.wolfyscript.customcrafting.utils.NamespacedKeyUtils;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class TabEliteCraftingTable extends ItemCreatorTab {

    public static final String KEY = "elite_workbench";

    public TabEliteCraftingTable() {
        super(new BukkitNamespacedKey(NamespacedKeyUtils.NAMESPACE, KEY));
    }

    private static EliteCraftingTableSettings getOrCreateSettings(Items items) {
        return items.getItem().computeDataIfAbsent(EliteCraftingTableSettings.class, id -> new EliteCraftingTableSettings());
    }

    private static void changeGridSize(Items items, int gridSize) {
        getOrCreateSettings(items).setGridSize(gridSize);
    }

    private static void setEnable(Items items, boolean enable) {
        getOrCreateSettings(items).setEnabled(enable);
    }

    private static void setAdvancedRecipes(Items items, boolean advRec) {
        getOrCreateSettings(items).setAdvancedRecipes(advRec);
    }

    @Override
    public void register(MenuItemCreator creator, WolfyUtilsBukkit api) {
        ButtonOption.register(creator.getButtonBuilder(), Material.CRAFTING_TABLE, this);
        creator.getButtonBuilder().dummy("elite_workbench.particles").state(state -> state.icon(Material.FIREWORK_ROCKET)).register();
        creator.getButtonBuilder().multiChoice("elite_workbench.grid_size")
                .stateFunction((holder, cache, btn, slot) ->
                        cache.getItems().getItem().getData(EliteCraftingTableSettings.class).map(settings -> settings.getGridSize() - 2)
                                // Get old elite crafting table settings
                                .orElse(((EliteWorkbenchData) cache.getItems().getItem().getCustomData(CustomCrafting.ELITE_CRAFTING_TABLE_DATA)).getGridSize() - 2))
                .addState(state -> state.subKey("size_2").icon(PlayerHeadUtils.getViaURL("9e95293acbcd4f55faf5947bfc5135038b275a7ab81087341b9ec6e453e839")).action((holder, cache, btn, slot, details) -> {
                    var items = cache.getItems();
                    changeGridSize(items, 3);
                    return ButtonInteractionResult.cancel(true);
                }))
                .addState(state -> state.subKey("size_3").icon(PlayerHeadUtils.getViaURL("9e95293acbcd4f55faf5947bfc5135038b275a7ab81087341b9ec6e453e839")).action((holder, cache, btn, slot, details) -> {
                    var items = cache.getItems();
                    changeGridSize(items, 4);
                    return ButtonInteractionResult.cancel(true);
                }))
                .addState(state -> state.subKey("size_4").icon(PlayerHeadUtils.getViaURL("cbfb41f866e7e8e593659986c9d6e88cd37677b3f7bd44253e5871e66d1d424")).action((holder, cache, btn, slot, details) -> {
                    var items = cache.getItems();
                    changeGridSize(items, 5);
                    return ButtonInteractionResult.cancel(true);
                }))
                // Deprecated states
                .addState(state -> state.subKey("size_5").icon(PlayerHeadUtils.getViaURL("14d844fee24d5f27ddb669438528d83b684d901b75a6889fe7488dfc4cf7a1c")).action((holder, cache, btn, slot, details) -> {
                    var items = cache.getItems();
                    changeGridSize(items, 6);
                    return ButtonInteractionResult.cancel(true);
                }))
                .addState(state -> state.subKey("size_6").icon(PlayerHeadUtils.getViaURL("faff2eb498e5c6a04484f0c9f785b448479ab213df95ec91176a308a12add70")).action((holder, cache, btn, slot, details) -> {
                    var items = cache.getItems();
                    changeGridSize(items, 2);
                    return ButtonInteractionResult.cancel(true);
                })).register();
        creator.getButtonBuilder().toggle("elite_workbench.toggle").stateFunction((holder, cache, slot) ->
                cache.getItems().getItem().getData(EliteCraftingTableSettings.class).map(EliteCraftingTableSettings::isEnabled)
                        // Get old elite crafting table settings
                        .orElse(((EliteWorkbenchData) cache.getItems().getItem().getCustomData(CustomCrafting.ELITE_CRAFTING_TABLE_DATA)).isEnabled())).enabledState(state -> state.subKey("elite_workbench.toggle.enabled").icon(Material.GREEN_CONCRETE).action((holder, cache, btn, slot, details) -> {
                    var items = cache.getItems();
                    setEnable(items, false);
                    return ButtonInteractionResult.cancel(true);
                })).disabledState(state -> state.subKey("elite_workbench.toggle.disabled").icon(Material.RED_CONCRETE).action((holder, cache, btn, slot, details) -> {
                    var items = cache.getItems();
                    setEnable(items, true);
                    return ButtonInteractionResult.cancel(true);
                })).register();
        creator.getButtonBuilder().toggle("elite_workbench.advanced_recipes").stateFunction((holder, cache, slot) ->
                cache.getItems().getItem().getData(EliteCraftingTableSettings.class).map(EliteCraftingTableSettings::isAdvancedRecipes)
                        // Get old elite crafting table settings
                        .orElse(((EliteWorkbenchData) cache.getItems().getItem().getCustomData(CustomCrafting.ELITE_CRAFTING_TABLE_DATA)).isAdvancedRecipes()))
                .enabledState(state -> state.subKey("elite_workbench.advanced_recipes.enabled").icon(Material.GREEN_CONCRETE).action((holder, cache, btn, slot, details) -> {
                    var items = cache.getItems();
                    setAdvancedRecipes(items, false);
                    return ButtonInteractionResult.cancel(true);
                })).disabledState(state -> state.subKey("elite_workbench.advanced_recipes.disabled").icon(Material.RED_CONCRETE).action((holder, cache, btn, slot, details) -> {
                    var items = cache.getItems();
                    setAdvancedRecipes(items, true);
                    return ButtonInteractionResult.cancel(true);
                })).register();
    }

    @Override
    public boolean shouldRender(GuiUpdate<CCCache> update, CCCache cache, Items items, CustomItem customItem, ItemStack item) {
        return item.getType().isBlock() || (customItem.getApiReference() instanceof ItemsAdderRef iaRef && update.getGuiHandler().getWolfyUtils().getCore().getCompatibilityManager().getPlugins().evaluateIfAvailable("ItemsAdder", ItemsAdderIntegration.class, ia -> ia.getStackInstance(iaRef.getItemID()).map(CustomStack::isBlock).orElse(false)));
    }

    @Override
    public void render(GuiUpdate<CCCache> update, CCCache cache, Items items, CustomItem customItem, ItemStack item) {
        update.setButton(28, "elite_workbench.particles");
        update.setButton(30, "elite_workbench.grid_size");
        update.setButton(32, "elite_workbench.toggle");
        update.setButton(34, "elite_workbench.advanced_recipes");
    }
}
