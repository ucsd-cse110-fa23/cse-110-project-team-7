public class DeleteRecipe{

    public static void deleteTargetRecipe(RecipeList recipeList,Recipe recipe){
        recipeList.getRecipeList().remove(recipe);
        recipeList.saveRecipe();

    }
}