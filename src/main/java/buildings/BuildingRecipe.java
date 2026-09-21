package buildings;

import items.ItemType;

public record BuildingRecipe(
        ItemType itemType,
        int work
) {
}
