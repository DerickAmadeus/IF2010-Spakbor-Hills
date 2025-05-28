package player;

import Items.*;
import java.util.*;

public class RecipeLoader {

    public static Recipe[] loadInitialRecipes() {
        Recipe[] recipes = new Recipe[11];

        // List umum untuk reusable data ikan
        ArrayList<String> allSeasons = new ArrayList<>(List.of("Spring", "Summer", "Fall", "Winter"));
        ArrayList<String> anyWeather = new ArrayList<>(List.of("Sunny", "Rainy"));
        ArrayList<String> anyLocation = new ArrayList<>(List.of("River", "Lake", "Ocean"));
        ArrayList<Integer> anytime = new ArrayList<>(List.of(0));
        ArrayList<Integer> endtime = new ArrayList<>(List.of(24));

        // Ikan biasa
        Item fish = new Fish("Any Fish", "Ikan biasa", 80, 50,
                allSeasons, anyWeather, anyLocation, "Common", anytime, endtime);

        // Ikan spesifik
        Item salmon = new Fish("Salmon", "Ikan Salmon", 150, 100,
                new ArrayList<>(List.of("Fall")),
                new ArrayList<>(List.of("Rainy")),
                new ArrayList<>(List.of("River")),
                "Regular",
                new ArrayList<>(List.of(6)),
                new ArrayList<>(List.of(20)));

        Item pufferfish = new Fish("Pufferfish", "Ikan buntal", 200, 120,
                new ArrayList<>(List.of("Summer")),
                new ArrayList<>(List.of("Sunny")),
                new ArrayList<>(List.of("Ocean")),
                "Regular",
                new ArrayList<>(List.of(12)),
                new ArrayList<>(List.of(16)));

        Item legendFish = new Fish("Legend", "Ikan legenda langka", 300, 200,
                new ArrayList<>(List.of("Winter")),
                new ArrayList<>(List.of("Snowy")),
                new ArrayList<>(List.of("Mountain Lake")),
                "Legendary",
                new ArrayList<>(List.of(6)),
                new ArrayList<>(List.of(8)));

        // Item tanaman
        Item wheat = new Crops("Wheat", "Tumbuhan biji-bijian", 40, 20, 3);
        Item potato = new Crops("Potato", "Umbi kentang", 30, 15, 3);
        Item grape = new Crops("Grape", "Buah anggur", 60, 30, 3);
        Item egg = new Misc("Egg", "Telur segar", 25, 15);
        Item pumpkin = new Crops("Pumpkin", "Labu", 80, 40, 3);
        Item cauliflower = new Crops("Cauliflower", "Kembang kol", 70, 35, 3);
        Item parsnip = new Crops("Parsnip", "Akar putih", 50, 25, 3);
        Item tomato = new Crops("Tomato", "Buah tomat", 60, 30, 3);
        Item hotPepper = new Crops("Hot Pepper", "Cabe pedas", 55, 28, 3);
        Item melon = new Crops("Melon", "Buah melon", 90, 45, 3);
        Item cranberry = new Crops("Cranberry", "Buah merah asam", 70, 35, 3);
        Item blueberry = new Crops("Blueberry", "Buah biru manis", 70, 35, 3);
        Item eggplant = new Misc("Eggplant", "Terong", 65, 32);

        // Daftar resep
        recipes[0] = makeRecipe("recipe_1", new Food("Fish n' Chips", "Kentang dan ikan goreng", 135, 150, 50), Map.of(fish, 2, wheat, 1, potato, 1));
        recipes[1] = makeRecipe("recipe_2", new Food("Baguette", "Roti Prancis panjang", 80, 100, 25), Map.of(wheat, 3));
        recipes[2] = makeRecipe("recipe_3", new Food("Sashimi", "Irisan salmon mentah", 275, 300, 70), Map.of(salmon, 3));
        recipes[3] = makeRecipe("recipe_4", new Food("Fugu", "Ikan buntal beracun, tapi aman", 135, 0, 50), Map.of(pufferfish, 1));
        recipes[4] = makeRecipe("recipe_5", new Food("Wine", "Anggur fermentasi", 90, 100, 20), Map.of(grape, 2));
        recipes[5] = makeRecipe("recipe_6", new Food("Pumpkin Pie", "Pai labu manis", 100, 120, 35), Map.of(egg, 1, wheat, 1, pumpkin, 1));
        recipes[6] = makeRecipe("recipe_7", new Food("Veggie Soup", "Sup sayuran sehat", 120, 140, 40), Map.of(cauliflower, 1, parsnip, 1, potato, 1, tomato, 1));
        recipes[7] = makeRecipe("recipe_8", new Food("Fish Stew", "Sup ikan gurih", 260, 280, 70), Map.of(fish, 2, hotPepper, 1, cauliflower, 2));
        recipes[8] = makeRecipe("recipe_9", new Food("Spakbor Salad", "Salad buah legendaris", 250, 0, 70), Map.of(melon, 1, cranberry, 1, blueberry, 1, tomato, 1));
        recipes[9] = makeRecipe("recipe_10", new Food("Fish Sandwich", "Sandwich isi ikan pedas", 180, 200, 50), Map.of(fish, 1, wheat, 2, tomato, 1, hotPepper, 1));
        recipes[10] = makeRecipe("recipe_11", new Food("The Legends of Spakbor", "Hidangan legenda dunia bawah", 2000, 0, 100), Map.of(legendFish, 1, potato, 2, parsnip, 1, tomato, 1, eggplant, 1));

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
