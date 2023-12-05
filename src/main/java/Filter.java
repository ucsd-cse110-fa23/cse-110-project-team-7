import java.util.ArrayList;

public class Filter {
    public static ArrayList<Recipe> filterRecipes(ArrayList<Recipe> recipes, String mealType){
        if(mealType.toLowerCase().equals("all")){
            return recipes;
        }
        ArrayList<Recipe> filteredRecipes = new ArrayList<>();
        for(Recipe recipe: recipes){
            String m = recipe.getMealType();
            if(m.toLowerCase().equals(mealType.toLowerCase())){
                filteredRecipes.add(recipe);
            }
        }
        return filteredRecipes;
    }
}