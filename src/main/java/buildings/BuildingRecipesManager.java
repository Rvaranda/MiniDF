package buildings;

import items.ItemType;
import java.util.Map;

public class BuildingRecipesManager {
    private static final Map<String, BuildingRecipe> recipes = Map.ofEntries(
            Map.entry("wall", new BuildingRecipe(ItemType.STONE, 60)),
            Map.entry("door", new BuildingRecipe(ItemType.WOOD, 30))
    );

    public static BuildingRecipe getRecipe(String buildingString) {
        return recipes.get(buildingString);
    }
}
