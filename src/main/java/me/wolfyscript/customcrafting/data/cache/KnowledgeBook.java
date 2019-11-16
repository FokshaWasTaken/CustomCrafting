package me.wolfyscript.customcrafting.data.cache;

import me.wolfyscript.customcrafting.gui.Setting;
import me.wolfyscript.customcrafting.recipes.types.CustomRecipe;
import me.wolfyscript.utilities.api.custom_items.CustomItem;
import org.bukkit.Bukkit;

import java.util.ArrayList;
import java.util.HashMap;

public class KnowledgeBook {

    private int page;
    private Setting setting;
    private WorkbenchFilter workbenchFilter;
    private CustomItem result;
    private CustomRecipe customRecipe;
    private int timerTask;
    private HashMap<Integer, Integer> timerTimings;

    public KnowledgeBook() {
        this.page = 0;
        this.setting = Setting.MAIN_MENU;
        this.customRecipe = null;
        this.timerTask = -1;
        this.timerTimings = new HashMap<>();
        workbenchFilter = WorkbenchFilter.ALL;
    }

    public HashMap<Integer, Integer> getTimerTimings() {
        return timerTimings;
    }

    public void setTimerTimings(HashMap<Integer, Integer> timerTimings) {
        this.timerTimings = timerTimings;
    }

    public void setTimerTask(int task) {
        this.timerTask = task;
    }

    public int getTimerTask() {
        return timerTask;
    }

    public void stopTimerTask() {
        if (timerTask != -1) {
            Bukkit.getScheduler().cancelTask(timerTask);
            timerTask = -1;
            timerTimings = new HashMap<>();
        }
    }

    public CustomRecipe getCustomRecipe() {
        return customRecipe;
    }

    public void setCustomRecipe(CustomRecipe customRecipe) {
        this.customRecipe = customRecipe;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public Setting getSetting() {
        return setting;
    }

    public void setSetting(Setting setting) {
        this.setting = setting;
    }

    public HashMap<Character, ArrayList<CustomItem>> getIngredients() {
        return new HashMap<>();
    }

    public CustomItem getResult() {
        return result;
    }

    public void setResult(CustomItem result) {
        this.result = result;
    }

    public WorkbenchFilter getWorkbenchFilter() {
        return workbenchFilter;
    }

    public void setWorkbenchFilter(WorkbenchFilter workbenchFilter) {
        this.workbenchFilter = workbenchFilter;
    }

    public enum WorkbenchFilter {
        ALL,
        ADVANCED,
        NORMAL;

        public static WorkbenchFilter next(WorkbenchFilter filter) {
            switch (filter) {
                case ALL:
                    return ADVANCED;
                case ADVANCED:
                    return NORMAL;
                case NORMAL:
                    return ALL;
            }
            return filter;
        }
    }
}
