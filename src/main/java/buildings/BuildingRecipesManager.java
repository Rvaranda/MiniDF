package buildings;

import items.ItemType;
import java.util.Map;

public class BuildingRecipesManager {
    private static final Map<BuildingType, BuildingRecipe> recipes = Map.ofEntries(
            Map.entry(BuildingType.WALL, new BuildingRecipe(BuildingType.WALL, ItemType.STONE, 60)),
            Map.entry(BuildingType.DOOR, new BuildingRecipe(BuildingType.DOOR, ItemType.WOOD, 30))
    );

    public static BuildingRecipe getRecipe(BuildingType type) {
        return recipes.get(type);
    }
}
