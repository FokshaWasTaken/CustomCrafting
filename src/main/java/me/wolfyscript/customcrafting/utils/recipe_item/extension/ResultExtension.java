package me.wolfyscript.customcrafting.utils.recipe_item.extension;

import me.wolfyscript.utilities.libraries.com.fasterxml.jackson.annotation.*;
import me.wolfyscript.utilities.libraries.com.fasterxml.jackson.databind.annotation.JsonTypeIdResolver;
import me.wolfyscript.utilities.libraries.com.fasterxml.jackson.databind.annotation.JsonTypeResolver;
import me.wolfyscript.utilities.util.Keyed;
import me.wolfyscript.utilities.util.NamespacedKey;
import me.wolfyscript.utilities.util.json.jackson.CustomTypeIdResolver;
import me.wolfyscript.utilities.util.json.jackson.CustomTypeResolver;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@JsonTypeResolver(CustomTypeResolver.class)
@JsonTypeIdResolver(CustomTypeIdResolver.class)
@JsonTypeInfo(use = JsonTypeInfo.Id.CUSTOM, include = JsonTypeInfo.As.EXISTING_PROPERTY, property = "key")
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
@JsonPropertyOrder(value = {"key", "outerRadius", "innerRadius"})
public abstract class ResultExtension implements Keyed {

    protected @JsonIgnore
    Material icon = Material.CHAIN_COMMAND_BLOCK;
    protected @JsonIgnore
    String title = "&6&lExtension";
    protected @JsonIgnore
    List<String> description;

    @JsonProperty(value = "key")
    private final NamespacedKey namespacedKey;
    @JsonAlias(value = "outer_radius")
    protected Vector outerRadius = new Vector(0, 0, 0);
    @JsonAlias(value = "inner_radius")
    protected Vector innerRadius = new Vector(0, 0, 0);

    protected ResultExtension(ResultExtension extension) {
        this.namespacedKey = extension.namespacedKey;
        this.icon = extension.icon;
        this.title = extension.title;
        this.description = extension.description;
        this.outerRadius = extension.outerRadius;
        this.innerRadius = extension.innerRadius;
    }

    protected ResultExtension(NamespacedKey namespacedKey) {
        this.namespacedKey = namespacedKey;
        this.title = "&6&lExtension";
        this.description = Arrays.asList("&7" + namespacedKey.toString(), "");
    }

    protected ResultExtension(NamespacedKey namespacedKey, Material icon, String title, List<String> description) {
        this(namespacedKey);
        this.icon = icon;
        this.title = title;
        this.description = description;
    }

    /**
     * Called only when crafted in a workstation.
     * Not called if no block is involved!
     *
     * @param block  The block of the workstation.
     * @param player The player that might be involved in the crafting. Might be Null if the action didn't involve a player.
     */
    public abstract void onWorkstation(Block block, @Nullable Player player);

    /**
     * Called each time when the result was crafted on any location.
     * The player might be null.
     *
     * @param location The location of the inventory, the result was crafted in.
     * @param player   The player that crafted the item or null if it was crafted by a block.
     */
    public abstract void onLocation(Location location, @Nullable Player player);

    /**
     * Called only if a player is involved in the crafting of the result.
     *
     * @param player   The player that crafted the result.
     * @param location The location of the inventory, the result was crafted in.
     */
    public abstract void onPlayer(@NotNull Player player, Location location);

    /**
     * Called whenever the result is crafted in workstations or inventories without block.
     * <p>
     * Crafted for different workstations means:
     * - Furnaces: the ingredient is smelted and the result is put into the result slot.
     * - Other Recipes: the result is taken out of the inventory and the ingredients are consumed.
     *
     * @param location      The location of the inventory, the result was crafted in. (might be the location of the workstation block)
     * @param isWorkstation If it was crafted in a workstation.
     * @param player        The player that crafted the result.
     */
    public void onCraft(@NotNull Location location, boolean isWorkstation, @Nullable Player player) {
        if (isWorkstation) {
            onWorkstation(location.getBlock(), player);
        }
        onLocation(location, player);
        if (player != null) {
            onPlayer(player, location);
        }
    }

    public abstract ResultExtension clone();

    @Override
    public NamespacedKey getNamespacedKey() {
        return namespacedKey;
    }

    public Vector getInnerRadius() {
        return innerRadius.clone();
    }

    public void setInnerRadius(Vector innerRadius) {
        this.innerRadius = innerRadius;
    }

    public Vector getOuterRadius() {
        return outerRadius.clone();
    }

    public void setOuterRadius(Vector outerRadius) {
        this.outerRadius = outerRadius;
    }

    protected <E extends Entity> List<E> getEntitiesInRange(Class<E> entityType, Location location, Vector outerRadius, Vector innerRadius) {
        var world = location.getWorld();
        if (world != null) {
            List<E> outerEntities = world.getNearbyEntities(location, outerRadius.getX(), outerRadius.getZ(), outerRadius.getZ(), entityType::isInstance).stream().map(entityType::cast).collect(Collectors.toList());
            if (innerRadius.getX() != 0 || innerRadius.getY() != 0 || innerRadius.getZ() != 0) {
                List<E> innerEntities = world.getNearbyEntities(location, innerRadius.getX(), innerRadius.getZ(), innerRadius.getZ(), entityType::isInstance).stream().map(entityType::cast).collect(Collectors.toList());
                outerEntities.removeAll(innerEntities);
            }
            return outerEntities;
        }
        return new ArrayList<>();
    }

}
