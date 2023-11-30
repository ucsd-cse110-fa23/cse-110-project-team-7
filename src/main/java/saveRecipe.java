import java.io.IOException;
import java.util.ArrayList;
public class saveRecipe {
   public static ArrayList<Recipe> saveARecipe(ArrayList<Recipe> recipeList, Recipe recipe) throws IOException {
        if(!recipeList.contains(recipe)){
            recipeList.add(0, recipe);
        }
        return recipeList; 
   
   }
}