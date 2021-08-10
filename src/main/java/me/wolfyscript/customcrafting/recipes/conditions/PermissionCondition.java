package me.wolfyscript.customcrafting.recipes.conditions;

import me.wolfyscript.customcrafting.CustomCrafting;
import me.wolfyscript.customcrafting.recipes.CustomRecipeCooking;
import me.wolfyscript.customcrafting.recipes.ICustomRecipe;
import me.wolfyscript.customcrafting.utils.NamespacedKeyUtils;
import me.wolfyscript.utilities.api.inventory.gui.button.buttons.ChatInputButton;
import me.wolfyscript.utilities.util.NamespacedKey;
import org.bukkit.Material;

import java.util.List;

public class PermissionCondition extends Condition<PermissionCondition> {

    public static final NamespacedKey KEY = new NamespacedKey(NamespacedKeyUtils.NAMESPACE, "permission");

    private String permission = "customcrafting.craft.%namespace%.%recipe_name%";

    public PermissionCondition() {
        super(KEY);
        setAvailableOptions(Conditions.Option.EXACT);
    }

    @Override
    public boolean isApplicable(ICustomRecipe<?> recipe) {
        return ExperienceCondition.valid(recipe);
    }

    public void setPermission(String permission) {
        this.permission = permission;
    }

    public String getPermission() {
        return permission;
    }

    @Override
    public boolean check(ICustomRecipe<?> recipe, Conditions.Data data) {
        if (recipe instanceof CustomRecipeCooking && data.getPlayer() == null) {
            return true;
        }
        if (data.getPlayer() == null) return false;
        return CustomCrafting.inst().getApi().getPermissions().hasPermission(data.getPlayer(), permission.replace("%namespace%", recipe.getNamespacedKey().getNamespace()).replace("%recipe_name%", recipe.getNamespacedKey().getKey()));
    }

    public static class GUIComponent extends FunctionalGUIComponent<PermissionCondition> {

        public GUIComponent() {
            super(Material.REDSTONE, getLangKey(KEY.getKey(), "name"), List.of(getLangKey(KEY.getKey(), "description")),
                    (menu, wolfyUtilities) -> {
                        menu.registerButton(new ChatInputButton<>("conditions.permission", Material.REDSTONE, (hashMap, cache, guiHandler, player, guiInventory, itemStack, i, b) -> {
                            hashMap.put("%VALUE%", cache.getRecipe().getConditions().getByType(PermissionCondition.class).getPermission());
                            return itemStack;
                        }, (guiHandler, player, s, strings) -> {
                            guiHandler.getCustomCache().getRecipe().getConditions().getByType(PermissionCondition.class).setPermission(s.trim());
                            return false;
                        }));
                    },
                    (update, cache, condition, recipe) -> {
                    });
        }

        @Override
        public boolean shouldRender(ICustomRecipe<?> recipe) {
            return ExperienceCondition.valid(recipe);
        }
    }
}
