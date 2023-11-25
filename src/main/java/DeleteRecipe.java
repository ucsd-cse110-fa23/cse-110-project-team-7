import java.util.ArrayList;

public class DeleteRecipe{

    public static ArrayList<Recipe> deleteTargetRecipe(ArrayList<Recipe> recipeList, Recipe recipe){
        recipeList.remove(recipe);
        //recipeList.saveRecipe();
        // Update CSV file too somehow to remove list
        try{
            saveRecipe.saveToCSV(recipeList);
            saveRecipe.saveListToDatabase(recipeList);
        }catch(Exception e){
            System.err.println("Delete and Save failed");
            e.printStackTrace();
        }
        return recipeList;
    }
}