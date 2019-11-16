package me.wolfyscript.customcrafting.handlers;

import me.wolfyscript.customcrafting.gui.crafting.CraftingWindow3;
import me.wolfyscript.customcrafting.gui.crafting.CraftingWindow4;
import me.wolfyscript.customcrafting.gui.crafting.CraftingWindow5;
import me.wolfyscript.customcrafting.gui.crafting.CraftingWindow6;
import me.wolfyscript.customcrafting.gui.item_creator.ItemCreator;
import me.wolfyscript.customcrafting.gui.main_gui.*;
import me.wolfyscript.customcrafting.gui.recipe_creator.ConditionsMenu;
import me.wolfyscript.customcrafting.gui.recipe_creator.VariantMenu;
import me.wolfyscript.customcrafting.gui.recipe_creator.recipe_creators.*;
import me.wolfyscript.customcrafting.gui.recipebook.RecipeBook;
import me.wolfyscript.utilities.api.WolfyUtilities;
import me.wolfyscript.utilities.api.inventory.GuiCluster;
import me.wolfyscript.utilities.api.inventory.InventoryAPI;
import me.wolfyscript.utilities.api.inventory.button.ButtonState;
import me.wolfyscript.utilities.api.inventory.button.buttons.ActionButton;
import me.wolfyscript.utilities.api.inventory.button.buttons.DummyButton;
import me.wolfyscript.utilities.api.inventory.button.buttons.ToggleButton;
import me.wolfyscript.utilities.api.utils.chat.ClickData;
import me.wolfyscript.utilities.api.utils.chat.ClickEvent;
import org.bukkit.Material;

public class InventoryHandler {

    private WolfyUtilities api;
    private InventoryAPI invAPI;

    public InventoryHandler(WolfyUtilities api) {
        this.api = api;
        this.invAPI = api.getInventoryAPI();
    }

    public void init() {
        api.sendConsoleMessage("$msg.startup.inventories$");
        registerInvs();
        invAPI.registerButton("none", new DummyButton("glass_gray", new ButtonState("none", "background", Material.GRAY_STAINED_GLASS_PANE, null)));
        invAPI.registerButton("none", new DummyButton("glass_black", new ButtonState("none", "background", Material.BLACK_STAINED_GLASS_PANE, null)));
        invAPI.registerButton("none", new DummyButton("glass_red", new ButtonState("none", "background", Material.RED_STAINED_GLASS_PANE, null)));
        invAPI.registerButton("none", new DummyButton("glass_white", new ButtonState("none", "background", Material.WHITE_STAINED_GLASS_PANE, null)));
        invAPI.registerButton("none", new DummyButton("glass_green", new ButtonState("none", "background", Material.GREEN_STAINED_GLASS_PANE, null)));
        invAPI.registerButton("none", new DummyButton("glass_purple", new ButtonState("none", "background", Material.PURPLE_STAINED_GLASS_PANE, null)));
        invAPI.registerButton("none", new DummyButton("glass_pink", new ButtonState("none", "background", Material.PINK_STAINED_GLASS_PANE, null)));
        invAPI.registerButton("none", new ToggleButton("gui_help", true, new ButtonState("gui_help_off", WolfyUtilities.getCustomHead("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvOGVlZjc4ZWRkNDdhNzI1ZmJmOGMyN2JiNmE3N2Q3ZTE1ZThlYmFjZDY1Yzc3ODgxZWM5ZWJmNzY4NmY3YzgifX19"), (guiHandler, player, inventory, i, inventoryClickEvent) -> {
            guiHandler.setHelpEnabled(true);
            return true;
        }), new ButtonState("gui_help_on", WolfyUtilities.getCustomHead("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvOGVlZjc4ZWRkNDdhNzI1ZmJmOGMyN2JiNmE3N2Q3ZTE1ZThlYmFjZDY1Yzc3ODgxZWM5ZWJmNzY4NmY3YzgifX19"), (guiHandler, player, inventory, i, inventoryClickEvent) -> {
            guiHandler.setHelpEnabled(false);
            return true;
        })));
        invAPI.registerButton("none", new ActionButton("back", new ButtonState("none", "back", WolfyUtilities.getCustomHead("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvODY0Zjc3OWE4ZTNmZmEyMzExNDNmYTY5Yjk2YjE0ZWUzNWMxNmQ2NjllMTljNzVmZDFhN2RhNGJmMzA2YyJ9fX0="), (guiHandler, player, inventory, i, inventoryClickEvent) -> {
            guiHandler.openPreviousInv();
            return true;
        })));
    }

    private void registerInvs() {
        //Main Cluster
        GuiCluster mainCluster = invAPI.getOrRegisterGuiCluster("none");
        mainCluster.registerGuiWindow(new MainMenu(invAPI));
        mainCluster.registerGuiWindow(new ItemEditor(invAPI));
        mainCluster.registerGuiWindow(new RecipeEditor(invAPI));
        mainCluster.registerGuiWindow(new RecipesList(invAPI));
        mainCluster.registerGuiWindow(new Settings(invAPI));
        mainCluster.setMainmenu("main_menu");

        mainCluster.registerButton(new ActionButton("patreon", new ButtonState("main_menu", "patreon", WolfyUtilities.getSkullViaURL("5693b66a595f78af3f51f4efa4c13375b1b958e6f4c507a47c4fe565cc275"), (guiHandler, player, inventory, i, inventoryClickEvent) -> {
            api.sendActionMessage(player, new ClickData("&7[&3Click here to go to Patreon&7]", null, new ClickEvent(ClickEvent.Action.OPEN_URL, "https://www.patreon.com/wolfyscript")));
            return true;
        })), invAPI.getWolfyUtilities());
        mainCluster.registerButton(new ActionButton("instagram", new ButtonState("main_menu", "instagram", WolfyUtilities.getSkullViaURL("ac88d6163fabe7c5e62450eb37a074e2e2c88611c998536dbd8429faa0819453"), (guiHandler, player, inventory, i, inventoryClickEvent) -> {
            api.sendActionMessage(player, new ClickData("&7[&3Click here to go to Instagram&7]", null, new ClickEvent(ClickEvent.Action.OPEN_URL, "https://www.instagram.com/wolfyscript/")));
            return true;
        })), invAPI.getWolfyUtilities());
        mainCluster.registerButton(new ActionButton("youtube", new ButtonState("main_menu", "youtube", WolfyUtilities.getSkullViaURL("b4353fd0f86314353876586075b9bdf0c484aab0331b872df11bd564fcb029ed"), (guiHandler, player, inventory, i, inventoryClickEvent) -> {
            api.sendActionMessage(player, new ClickData("&7[&3Click here to go to YouTube&7]", null, new ClickEvent(ClickEvent.Action.OPEN_URL, "https://www.youtube.com/channel/UCTlqRLm4PxZuAI4nVN4X74g")));
            return true;
        })), invAPI.getWolfyUtilities());
        mainCluster.registerButton(new ActionButton("discord", new ButtonState("main_menu", "discord", WolfyUtilities.getSkullViaURL("4d42337be0bdca2128097f1c5bb1109e5c633c17926af5fb6fc20000011aeb53"), (guiHandler, player, inventory, i, inventoryClickEvent) -> {
            api.sendActionMessage(player, new ClickData("&7[&3Click here to join Discord&7]", null, new ClickEvent(ClickEvent.Action.OPEN_URL, "https://discord.gg/2jcN4vF")));
            return true;
        })), invAPI.getWolfyUtilities());

        GuiCluster recipeCreator = invAPI.getOrRegisterGuiCluster("recipe_creator");
        recipeCreator.registerGuiWindow(new AnvilCreator(invAPI));
        recipeCreator.registerGuiWindow(new CookingCreator(invAPI));
        recipeCreator.registerGuiWindow(new CauldronCreator(invAPI));
        recipeCreator.registerGuiWindow(new StonecutterCreator(invAPI));
        recipeCreator.registerGuiWindow(new WorkbenchCreator(invAPI));
        recipeCreator.registerGuiWindow(new EliteWorkbenchCreator(invAPI));
        recipeCreator.registerGuiWindow(new ConditionsMenu(invAPI));
        recipeCreator.registerGuiWindow(new VariantMenu(invAPI));

        recipeCreator.registerButton(new ActionButton("conditions", new ButtonState("conditions", Material.CYAN_CONCRETE_POWDER, (guiHandler, player, inventory, i, inventoryClickEvent) -> {
            guiHandler.changeToInv("conditions");
            return true;
        })), invAPI.getWolfyUtilities());

        GuiCluster recipeBook = invAPI.getOrRegisterGuiCluster("recipe_book");
        recipeBook.registerGuiWindow(new RecipeBook(invAPI));
        recipeBook.registerGuiWindow(new me.wolfyscript.customcrafting.gui.recipebook.MainMenu(invAPI));
        recipeBook.setMainmenu("main_menu");

        GuiCluster craftingCluster = new GuiCluster();
        invAPI.registerCustomGuiCluster("crafting", craftingCluster);
        craftingCluster.registerGuiWindow(new CraftingWindow3(invAPI));
        craftingCluster.registerGuiWindow(new CraftingWindow4(invAPI));
        craftingCluster.registerGuiWindow(new CraftingWindow5(invAPI));
        craftingCluster.registerGuiWindow(new CraftingWindow6(invAPI));
        craftingCluster.setMainmenu("crafting_3");
        craftingCluster.registerButton(new ActionButton("knowledge_book", new ButtonState("crafting", "knowledge_book", Material.KNOWLEDGE_BOOK, (guiHandler, player, inventory, i, inventoryClickEvent) -> {

            return true;
        })), invAPI.getWolfyUtilities());

        GuiCluster itemCreator = invAPI.getOrRegisterGuiCluster("item_creator");
        itemCreator.registerGuiWindow(new ItemCreator(invAPI));

    }


}
