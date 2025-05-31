package player;

import Items.*;
import java.util.*;

public class RecipeLoader {

    public static Recipe[] loadInitialRecipes() {
        Recipe[] recipes = new Recipe[11];

        ArrayList<String> allSeasons = new ArrayList<>(List.of("Spring", "Summer", "Fall", "Winter"));
        ArrayList<String> anyWeather = new ArrayList<>(List.of("Sunny", "Rainy"));
        ArrayList<String> anyLocation = new ArrayList<>(List.of("River", "Lake", "Ocean"));
        ArrayList<Integer> anytime = new ArrayList<>(List.of(0));
        ArrayList<Integer> endtime = new ArrayList<>(List.of(24));

        Item fish = new Fish("Any Fish", 80, 50,
                allSeasons, anyWeather, anyLocation, "Common", anytime, endtime);

        Item salmon = new Fish("Salmon", 150, 100,
                new ArrayList<>(List.of("Fall")),
                new ArrayList<>(List.of("Rainy")),
                new ArrayList<>(List.of("River")),
                "Regular",
                new ArrayList<>(List.of(6)),
                new ArrayList<>(List.of(20)));

        Item pufferfish = new Fish("Pufferfish", 200, 120,
                new ArrayList<>(List.of("Summer")),
                new ArrayList<>(List.of("Sunny")),
                new ArrayList<>(List.of("Ocean")),
                "Regular",
                new ArrayList<>(List.of(12)),
                new ArrayList<>(List.of(16)));

        Item legendFish = new Fish("Legend", 300, 200,
                new ArrayList<>(List.of("Winter")),
                new ArrayList<>(List.of("Snowy")),
                new ArrayList<>(List.of("Mountain Lake")),
                "Legendary",
                new ArrayList<>(List.of(6)),
                new ArrayList<>(List.of(8)));

        Item wheat = new Crops("Wheat", 40, 20, 3);
        Item potato = new Crops("Potato", 30, 15, 3);
        Item grape = new Crops("Grape", 60, 30, 3);
        Item egg = new Misc("Egg", "Telur segar", 25, 15);
        Item pumpkin = new Crops("Pumpkin", 80, 40, 3);
        Item cauliflower = new Crops("Cauliflower", 70, 35, 3);
        Item parsnip = new Crops("Parsnip", 50, 25, 3);
        Item tomato = new Crops("Tomato", 60, 30, 3);
        Item hotPepper = new Crops("Hot Pepper", 55, 28, 3);
        Item melon = new Crops("Melon", 90, 45, 3);
        Item cranberry = new Crops("Cranberry", 70, 35, 3);
        Item blueberry = new Crops("Blueberry", 70, 35, 3);
        Item eggplant = new Misc("Eggplant", "Terong", 65, 32);

        recipes[0] = makeRecipe("recipe_1", new Food("Fish n' Chips", 135, 150, 50), Map.of(fish, 2, wheat, 1, potato, 1));
        recipes[1] = makeRecipe("recipe_2", new Food("Baguette", 80, 100, 25), Map.of(wheat, 3));
        recipes[2] = makeRecipe("recipe_3", new Food("Sashimi", 275, 300, 70), Map.of(salmon, 3));
        recipes[3] = makeRecipe("recipe_4", new Food("Fugu", 135, 0, 50), Map.of(pufferfish, 1));
        recipes[4] = makeRecipe("recipe_5", new Food("Wine", 90, 100, 20), Map.of(grape, 2));
        recipes[5] = makeRecipe("recipe_6", new Food("Pumpkin Pie", 100, 120, 35), Map.of(egg, 1, wheat, 1, pumpkin, 1));
        recipes[6] = makeRecipe("recipe_7", new Food("Veggie Soup", 120, 140, 40), Map.of(cauliflower, 1, parsnip, 1, potato, 1, tomato, 1));
        recipes[7] = makeRecipe("recipe_8", new Food("Fish Stew", 260, 280, 70), Map.of(fish, 2, hotPepper, 1, cauliflower, 2));
        recipes[8] = makeRecipe("recipe_9", new Food("Spakbor Salad", 250, 0, 70), Map.of(melon, 1, cranberry, 1, blueberry, 1, tomato, 1));
        recipes[9] = makeRecipe("recipe_10", new Food("Fish Sandwich", 180, 200, 50), Map.of(fish, 1, wheat, 2, tomato, 1, hotPepper, 1));
        recipes[10] = makeRecipe("recipe_11", new Food("The Legends of Spakbor", 2000, 0, 100), Map.of(legendFish, 1, potato, 2, parsnip, 1, tomato, 1, eggplant, 1));

        recipes[1].setUnlockInfo(true);
        recipes[4].setUnlockInfo(true);
        recipes[5].setUnlockInfo(true);
        recipes[8].setUnlockInfo(true);

        return recipes;
    }

    private static Recipe makeRecipe(String id, Food name, Map<Item, Integer> ingredients) {
        Recipe r = new Recipe(id, name);
        for (Map.Entry<Item, Integer> e : ingredients.entrySet()) {
            r.getIngredients().put(e.getKey(), e.getValue());
        }
        return r;
    }
}
