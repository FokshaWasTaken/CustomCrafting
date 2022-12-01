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

package me.wolfyscript.customcrafting;

import com.google.common.base.Preconditions;
import com.wolfyscript.utilities.Keyed;
import com.wolfyscript.utilities.NamespacedKey;
import java.util.Objects;
import me.wolfyscript.customcrafting.recipes.conditions.Condition;
import me.wolfyscript.customcrafting.recipes.items.extension.ResultExtension;
import me.wolfyscript.customcrafting.recipes.items.target.MergeAdapter;
import me.wolfyscript.utilities.util.ClassRegistry;
import org.jetbrains.annotations.Nullable;

@Deprecated
public interface CCClassRegistry<T extends Keyed> extends ClassRegistry<T> {

    RecipeConditionsRegistry RECIPE_CONDITIONS = new RecipeConditionsRegistry();
    SimpleClassRegistry<MergeAdapter> RESULT_MERGE_ADAPTERS = new WrapperClassRegistry<>(() -> CustomCrafting.inst().getRegistries().getRecipeMergeAdapters());
    SimpleClassRegistry<ResultExtension> RESULT_EXTENSIONS = new WrapperClassRegistry<>(() -> CustomCrafting.inst().getRegistries().getRecipeResultExtensions());

    class RecipeConditionsRegistry extends WrapperClassRegistry<Condition<?>> {

        public RecipeConditionsRegistry() {
            super(() -> CustomCrafting.inst().getRegistries().getRecipeConditions());
        }

        /**
         * Registers the {@link Condition} and it's optional {@link Condition.AbstractGUIComponent} for the GUI settings.
         *
         * @param condition The {@link Condition} to register.
         * @param component An optional {@link Condition.AbstractGUIComponent}, to edit settings inside the GUI.
         * @param <C>       The type of the {@link Condition}
         */
        public <C extends Condition<C>> void register(NamespacedKey key, Class<C> condition, @Nullable Condition.AbstractGUIComponent<C> component) {
            Preconditions.checkArgument(condition != null, "Condition must not be null!");
            Condition.registerGUIComponent(Objects.requireNonNull(key, "Invalid NamespacedKey! Key cannot be null!"), component);
            super.register(key, condition);
        }

        /**
         * Registers a {@link Condition} into the {@link CCClassRegistry#RECIPE_CONDITIONS} without an additional GUI component.
         *
         * @param condition The {@link Condition} to register.
         */
        @Override
        public void register(Condition<?> condition) {
            this.register(condition.getNamespacedKey(), condition.getClass(), null);
        }

        @Override
        public void register(NamespacedKey key, Class<? extends Condition<?>> value) {
            Condition.registerGUIComponent(Objects.requireNonNull(key, "Invalid NamespacedKey! Key cannot be null!"), null);
            super.register(key, value);
        }

        @Override
        public void register(NamespacedKey key, Condition<?> value) {
            this.register(key, value.getClass(), null);
        }
    }

}
