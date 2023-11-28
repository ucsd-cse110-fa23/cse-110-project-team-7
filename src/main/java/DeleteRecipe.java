import java.util.ArrayList;

public class DeleteRecipe{

    public static ArrayList<Recipe> deleteTargetRecipe(ArrayList<Recipe> recipeList, Recipe recipe){
        recipeList.remove(recipe);
        return recipeList;
    }
}