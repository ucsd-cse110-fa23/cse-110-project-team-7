public class DeleteRecipe{

    public static void deleteTargetRecipe(RecipeList recipeList, Recipe recipe){
        recipeList.getRecipeList().remove(recipe);
        recipeList.saveRecipe();
        // Update CSV file too somehow to remove list
        try{
            saveRecipe.saveToCSV(recipeList);
        }catch(Exception e){
            System.err.println("Delete and Save failed");
            e.printStackTrace();
        }
        
    }
}