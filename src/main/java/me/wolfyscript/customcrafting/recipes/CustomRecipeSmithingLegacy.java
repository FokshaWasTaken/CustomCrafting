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

package me.wolfyscript.customcrafting.recipes;

import com.google.common.base.Preconditions;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import me.wolfyscript.customcrafting.CustomCrafting;
import me.wolfyscript.customcrafting.data.CCCache;
import me.wolfyscript.customcrafting.gui.recipebook.ButtonContainerIngredient;
import me.wolfyscript.customcrafting.gui.recipebook.ClusterRecipeBook;
import me.wolfyscript.customcrafting.recipes.conditions.Conditions;
import me.wolfyscript.customcrafting.recipes.data.IngredientData;
import me.wolfyscript.customcrafting.recipes.data.SmithingDataLegacy;
import me.wolfyscript.customcrafting.recipes.items.Ingredient;
import me.wolfyscript.customcrafting.recipes.items.Result;
import me.wolfyscript.customcrafting.recipes.items.target.MergeAdapter;
import me.wolfyscript.customcrafting.recipes.items.target.adapters.DamageMergeAdapter;
import me.wolfyscript.customcrafting.recipes.items.target.adapters.EnchantMergeAdapter;
import me.wolfyscript.customcrafting.utils.ItemLoader;
import me.wolfyscript.lib.com.fasterxml.jackson.annotation.JacksonInject;
import me.wolfyscript.lib.com.fasterxml.jackson.annotation.JsonCreator;
import me.wolfyscript.lib.com.fasterxml.jackson.annotation.JsonIgnore;
import me.wolfyscript.lib.com.fasterxml.jackson.annotation.JsonProperty;
import me.wolfyscript.lib.com.fasterxml.jackson.core.JsonGenerator;
import me.wolfyscript.lib.com.fasterxml.jackson.databind.JsonNode;
import me.wolfyscript.lib.com.fasterxml.jackson.databind.SerializerProvider;
import me.wolfyscript.utilities.api.inventory.custom_items.CustomItem;
import me.wolfyscript.utilities.api.inventory.gui.GuiCluster;
import me.wolfyscript.utilities.api.inventory.gui.GuiHandler;
import me.wolfyscript.utilities.api.inventory.gui.GuiUpdate;
import me.wolfyscript.utilities.api.inventory.gui.GuiWindow;
import me.wolfyscript.utilities.util.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

@Deprecated
public class CustomRecipeSmithingLegacy extends CustomRecipe<CustomRecipeSmithingLegacy> {

    private static final String KEY_BASE = "base";
    private static final String KEY_ADDITION = "addition";

    private Ingredient base;
    private Ingredient addition;

    private boolean preserveEnchants;
    private boolean preserveDamage;
    private boolean onlyChangeMaterial; //Only changes the material of the item. Useful to make vanilla style recipes.

    @JsonIgnore
    private List<MergeAdapter> internalMergeAdapters = new ArrayList<>(2);

    public CustomRecipeSmithingLegacy(NamespacedKey namespacedKey, JsonNode node) {
        super(namespacedKey, node);
        this.type = RecipeType.SMITHING;
        setBase(ItemLoader.loadIngredient(node.path(KEY_BASE)));
        setAddition(ItemLoader.loadIngredient(node.path(KEY_ADDITION)));
        preserveEnchants = node.path("preserve_enchants").asBoolean(true);
        preserveDamage = node.path("preserveDamage").asBoolean(true);
        onlyChangeMaterial = node.path("onlyChangeMaterial").asBoolean(false);
    }

    @JsonCreator
    public CustomRecipeSmithingLegacy(@JsonProperty("key") @JacksonInject("key") NamespacedKey key, @JacksonInject("customcrafting") CustomCrafting customCrafting) {
        super(key, customCrafting, RecipeType.SMITHING);
        this.base = new Ingredient();
        this.addition = new Ingredient();
        this.result = new Result();
        this.preserveEnchants = true;
        this.preserveDamage = true;
        this.onlyChangeMaterial = false;
    }

    @Deprecated
    public CustomRecipeSmithingLegacy(NamespacedKey key) {
        this(key, CustomCrafting.inst());
    }

    private CustomRecipeSmithingLegacy(CustomRecipeSmithingLegacy customRecipeSmithing) {
        super(customRecipeSmithing);
        this.result = customRecipeSmithing.getResult();
        this.base = customRecipeSmithing.getBase();
        this.addition = customRecipeSmithing.getAddition();
        this.preserveEnchants = customRecipeSmithing.isPreserveEnchants();
        this.preserveDamage = customRecipeSmithing.isPreserveDamage();
        this.onlyChangeMaterial = customRecipeSmithing.isOnlyChangeMaterial();
    }

    @JsonIgnore
    public List<MergeAdapter> getInternalMergeAdapters() {
        return Collections.unmodifiableList(internalMergeAdapters);
    }

    public SmithingDataLegacy check(Player player, InventoryView view, ItemStack base, ItemStack addition) {
        if (!checkConditions(Conditions.Data.of(player, view))) return null;

        IngredientData baseData = null;
        IngredientData additionData = null;

        Optional<CustomItem> baseCustom = getBase().check(base, isCheckNBT());
        if (baseCustom.isPresent()) {
            baseData = new IngredientData(1, 1, getBase(), baseCustom.get(), base);
        } else if (!getBase().isAllowEmpty()) return null;

        Optional<CustomItem> additionCustom = getAddition().check(addition, isCheckNBT());
        if (additionCustom.isPresent()) {
            additionData = new IngredientData(1, 1, getAddition(), additionCustom.get(), base);
        } else if (!getAddition().isAllowEmpty()) return null;

        return new SmithingDataLegacy(this, new IngredientData[]{ baseData, additionData});
    }

    @Override
    public Ingredient getIngredient(int slot) {
        return slot == 0 ? getBase() : getAddition();
    }

    public Ingredient getAddition() {
        return addition;
    }

    public void setAddition(@NotNull Ingredient addition) {
        Preconditions.checkArgument(!addition.isEmpty(), "Invalid Addition! Recipe must have non-air addition!");
        this.addition = addition;
    }

    public Ingredient getBase() {
        return base;
    }

    public void setBase(@NotNull Ingredient base) {
        Preconditions.checkArgument(!base.isEmpty(), "Invalid Base ingredient! Recipe must have non-air base ingredient!");
        this.base = base;
    }

    public boolean isPreserveEnchants() {
        return preserveEnchants;
    }

    public void setPreserveEnchants(boolean preserveEnchants) {
        this.preserveEnchants = preserveEnchants;
        if (!onlyChangeMaterial && preserveEnchants) {
            internalMergeAdapters.add(new EnchantMergeAdapter());
        }
    }

    public boolean isPreserveDamage() {
        return preserveDamage;
    }

    public void setPreserveDamage(boolean preserveDamage) {
        this.preserveDamage = preserveDamage;
        if (!onlyChangeMaterial && preserveEnchants) {
            internalMergeAdapters.add(new DamageMergeAdapter());
        }
    }

    public boolean isOnlyChangeMaterial() {
        return onlyChangeMaterial;
    }

    public void setOnlyChangeMaterial(boolean onlyChangeMaterial) {
        this.onlyChangeMaterial = onlyChangeMaterial;
        if (onlyChangeMaterial) {
            internalMergeAdapters.clear();
        }
    }

    @Override
    public CustomRecipeSmithingLegacy clone() {
        return new CustomRecipeSmithingLegacy(this);
    }

    @Override
    public void prepareMenu(GuiHandler<CCCache> guiHandler, GuiCluster<CCCache> cluster) {
        ((ButtonContainerIngredient) cluster.getButton(ButtonContainerIngredient.key(10))).setVariants(guiHandler, getBase());
        ((ButtonContainerIngredient) cluster.getButton(ButtonContainerIngredient.key(13))).setVariants(guiHandler, getAddition());
        ((ButtonContainerIngredient) cluster.getButton(ButtonContainerIngredient.key(23))).setVariants(guiHandler, this.getResult());
    }

    @Override
    public void renderMenu(GuiWindow<CCCache> guiWindow, GuiUpdate<CCCache> event) {
        var cluster = guiWindow.getCluster();
        event.setButton(19, ButtonContainerIngredient.key(cluster, 10));
        event.setButton(21, ButtonContainerIngredient.key(cluster, 13));
        event.setButton(23, new NamespacedKey(ClusterRecipeBook.KEY, "smithing"));
        event.setButton(25, ButtonContainerIngredient.key(cluster, 23));
    }

    @Override
    public void writeToJson(JsonGenerator gen, SerializerProvider serializerProvider) throws IOException {
        super.writeToJson(gen, serializerProvider);
        gen.writeBooleanField("preserve_enchants", preserveEnchants);
        gen.writeBooleanField("preserveDamage", preserveDamage);
        gen.writeBooleanField("onlyChangeMaterial", onlyChangeMaterial);
        gen.writeObjectField(KEY_RESULT, result);
        gen.writeObjectField(KEY_BASE, base);
        gen.writeObjectField(KEY_ADDITION, addition);
    }
}
