package buildings;

import items.ItemType;

public record BuildingRecipe(
        BuildingType type,
        ItemType itemType,
        int work
) {
}
