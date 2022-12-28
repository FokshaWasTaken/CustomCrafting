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

package me.wolfyscript.customcrafting.gui.potion_creator;

import com.wolfyscript.utilities.bukkit.BukkitNamespacedKey;
import com.wolfyscript.utilities.bukkit.gui.GuiCluster;
import com.wolfyscript.utilities.bukkit.gui.GuiHandler;
import com.wolfyscript.utilities.bukkit.gui.GuiUpdate;
import com.wolfyscript.utilities.bukkit.gui.button.ButtonAction;
import com.wolfyscript.utilities.bukkit.gui.button.ButtonChatInput;
import com.wolfyscript.utilities.bukkit.gui.button.ButtonDummy;
import com.wolfyscript.utilities.bukkit.gui.button.ButtonState;
import com.wolfyscript.utilities.bukkit.gui.button.ButtonToggle;
import com.wolfyscript.utilities.bukkit.gui.callback.CallbackButtonRender;
import com.wolfyscript.utilities.bukkit.world.inventory.PlayerHeadUtils;
import com.wolfyscript.utilities.common.gui.ButtonInteractionResult;
import java.util.Locale;
import me.wolfyscript.customcrafting.CustomCrafting;
import me.wolfyscript.customcrafting.data.CCCache;
import me.wolfyscript.customcrafting.data.cache.potions.PotionEffects;
import me.wolfyscript.customcrafting.gui.CCWindow;
import me.wolfyscript.customcrafting.gui.main_gui.ClusterMain;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import org.apache.commons.lang.StringUtils;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.potion.PotionEffect;

public class MenuPotionCreator extends CCWindow {

    public MenuPotionCreator(GuiCluster<CCCache> cluster, CustomCrafting customCrafting) {
        super(cluster, ClusterPotionCreator.POTION_CREATOR.getKey(), 54, customCrafting);
    }

    @Override
    public void onInit() {
        var btbBuilder = getButtonBuilder();

        btbBuilder.action("back").state(state -> state.key(ClusterMain.BACK).icon(PlayerHeadUtils.getViaURL("864f779a8e3ffa231143fa69b96b14ee35c16d669e19c75fd1a7da4bf306c")).action((holder, cache, btn, slot, details) -> {
            if (cache.getPotionEffectCache().isRecipePotionEffect()) {
                holder.getGuiHandler().openCluster("recipe_creator");
            } else {
                holder.getGuiHandler().openCluster("item_creator");
            }
            return ButtonInteractionResult.cancel(true);
        })).register();
        btbBuilder.action("cancel").state(state -> state.icon(Material.BARRIER).action((holder, cache, btn, slot, details) -> {
            if (cache.getPotionEffectCache().isRecipePotionEffect()) {
                holder.getGuiHandler().openCluster("recipe_creator");
            } else {
                holder.getGuiHandler().openCluster("item_creator");
            }
            return ButtonInteractionResult.cancel(true);
        })).register();
        btbBuilder.action("apply").state(state -> state.icon(Material.LIME_CONCRETE).action((holder, cache, btn, slot, details) -> {
            PotionEffects potionEffectCache = cache.getPotionEffectCache();
            potionEffectCache.applyPotionEffect(cache);
            if (potionEffectCache.isRecipePotionEffect()) {
                holder.getGuiHandler().openCluster("recipe_creator");
            } else {
                holder.getGuiHandler().openCluster("item_creator");
            }
            return ButtonInteractionResult.cancel(true);
        })).register();
        btbBuilder.dummy("preview").state(state -> state.icon(Material.POTION).render((holder, cache, btn, slot, itemStack) -> {
            PotionEffects potionEffectCache = holder.getGuiHandler().getCustomCache().getPotionEffectCache();
            ItemStack stack = new ItemStack(Material.POTION);
            PotionMeta itemMeta = (PotionMeta) stack.getItemMeta();
            itemMeta.setDisplayName("§7 - - - - - ");
            if (potionEffectCache.getType() != null) {
                itemMeta.addCustomEffect(new PotionEffect(potionEffectCache.getType(), potionEffectCache.getDuration(), potionEffectCache.getAmplifier(), potionEffectCache.isAmbient(), potionEffectCache.isParticles(), potionEffectCache.isIcon()), true);
            }
            stack.setItemMeta(itemMeta);
            return CallbackButtonRender.Result.of(stack);
        })).register();
        btbBuilder.action("potion_effect_type").state(state -> state.icon(Material.BOOKSHELF).action((holder, cache, btn, slot, details) -> {
            PotionEffects potionEffectCache = cache.getPotionEffectCache();
            potionEffectCache.setApplyPotionEffectType((cache1, potionEffect) -> potionEffectCache.setType(potionEffect));
            potionEffectCache.setOpenedFrom("potion_creator", "potion_creator");
            holder.getGuiHandler().openWindow("potion_effect_type_selection");
            return ButtonInteractionResult.cancel(true);
        }).render((holder, cache, btn, slot, itemStack) -> {
            PotionEffects potionEffectCache = holder.getGuiHandler().getCustomCache().getPotionEffectCache();
            return CallbackButtonRender.Result.of(Placeholder.parsed("effect_type", potionEffectCache.getType() != null ? StringUtils.capitalize(potionEffectCache.getType().getName().replace("_", " ").toLowerCase(Locale.ROOT)) : "<red>none"));
        })).register();

        btbBuilder.chatInput("duration").state(state -> state.icon(Material.CLOCK).render((holder, cache, btn, slot, itemStack) -> CallbackButtonRender.Result.of(Placeholder.parsed("duration", String.valueOf(holder.getGuiHandler().getCustomCache().getPotionEffectCache().getDuration()))))).inputAction((guiHandler, player, s, args) -> {
            try {
                holder.getGuiHandler().getCustomCache().getPotionEffectCache().setDuration(Integer.parseInt(args[0]));
                return false;
            } catch (NumberFormatException e) {
                api.getChat().sendKey(player, new BukkitNamespacedKey("item_creator", "main_menu"), "potion.error_number");
            }
            return true;
        }).register();

        btbBuilder.chatInput("amplifier").state(state -> state.icon(Material.IRON_SWORD).render((holder, cache, btn, slot, itemStack) -> CallbackButtonRender.Result.of(Placeholder.parsed("amplifier", String.valueOf(holder.getGuiHandler().getCustomCache().getPotionEffectCache().getAmplifier()))))).inputAction((guiHandler, player, s, args) -> {
            try {
                holder.getGuiHandler().getCustomCache().getPotionEffectCache().setAmplifier(Integer.parseInt(args[0]));
                return false;
            } catch (NumberFormatException e) {
                api.getChat().sendKey(player, new BukkitNamespacedKey("item_creator", "main_menu"), "potion.error_number");
            }
            return true;
        }).register();
        btbBuilder.toggle("ambient").enabledState(state -> state.subKey("enabled").icon(Material.BLAZE_POWDER).action((holder, cache, btn, slot, details) -> {
            cache.getPotionEffectCache().setAmbient(false);
            return ButtonInteractionResult.cancel(true);
        })).disabledState(state -> state.subKey("disabled").icon(Material.BLAZE_POWDER).action((holder, cache, btn, slot, details) -> {
            cache.getPotionEffectCache().setAmbient(true);
            return ButtonInteractionResult.cancel(true);
        })).register();
        btbBuilder.toggle("particles").enabledState(state -> state.subKey("enabled").icon(Material.FIREWORK_ROCKET).action((holder, cache, btn, slot, details) -> {
            cache.getPotionEffectCache().setParticles(false);
            return ButtonInteractionResult.cancel(true);
        })).disabledState(state -> state.subKey("disabled").icon(Material.FIREWORK_ROCKET).action((holder, cache, btn, slot, details) -> {
            cache.getPotionEffectCache().setParticles(true);
            return ButtonInteractionResult.cancel(true);
        })).register();
        btbBuilder.toggle("icon").enabledState(state -> state.subKey("enabled").icon(Material.ITEM_FRAME).action((holder, cache, btn, slot, details) -> {
            cache.getPotionEffectCache().setIcon(false);
            return ButtonInteractionResult.cancel(true);
        })).disabledState(state -> state.subKey("disabled").icon(Material.ITEM_FRAME).action((holder, cache, btn, slot, details) -> {
            cache.getPotionEffectCache().setIcon(true);
            return ButtonInteractionResult.cancel(true);
        })).register();

    }

    @Override
    public void onUpdateAsync(GuiUpdate<CCCache> update) {
        super.onUpdateAsync(update);
        GuiHandler<CCCache> guiHandler = update.getGuiHandler();
        PotionEffects potionEffectCache = guiHandler.getCustomCache().getPotionEffectCache();
        update.setButton(0, "back");
        update.setButton(11, "apply");
        update.setButton(13, "preview");
        update.setButton(15, "cancel");

        update.setButton(28, "potion_effect_type");
        update.setButton(30, "duration");
        update.setButton(32, "amplifier");
        ((ButtonToggle<CCCache>) getButton("ambient")).setState(guiHandler, potionEffectCache.isAmbient());
        ((ButtonToggle<CCCache>) getButton("particles")).setState(guiHandler, potionEffectCache.isParticles());
        ((ButtonToggle<CCCache>) getButton("icon")).setState(guiHandler, potionEffectCache.isIcon());
        update.setButton(34, "ambient");
        update.setButton(38, "particles");
        update.setButton(42, "icon");
    }
}
