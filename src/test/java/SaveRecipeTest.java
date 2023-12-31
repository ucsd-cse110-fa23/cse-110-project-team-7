import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
public class SaveRecipeTest {

    private Recipe recipe;
    private Recipe recipe2;
    private ArrayList<Recipe> recipeList;

    @BeforeEach
    public void setUp(){

       recipe = new Recipe();
       recipeList = new ArrayList<>();
       recipe.setTitle("[Egg and Rice]");
       recipe.setIngredients("Egg, Rice");
       recipe.setInstructions("Instructions: ");

       recipe2 = new Recipe();
       recipe2.setTitle("[Beef and Rice]");
       recipe2.setIngredients("Beef and rice");
       recipe2.setInstructions("Instructions");

    }

    
    @Test
    public void testSavingCorrect(){
        try{

            recipeList=saveRecipe.saveARecipe(recipeList, recipe);
            assertEquals(recipeList.get(0), recipe);
            // add on the top of the list 
            recipeList = saveRecipe.saveARecipe(recipeList, recipe2); 
            assertEquals(recipeList.get(0), recipe2);

            // saveARecipe

        }
        catch(Exception e){
            e.printStackTrace();
        }
    }


    @Test
    public void testSavingMultipleRecipeSort(){
        try{

            recipeList=saveRecipe.saveARecipe(recipeList, recipe);
            assertEquals(recipeList.get(0), recipe);
            // add on the top of the list 
            recipeList = saveRecipe.saveARecipe(recipeList, recipe); 
            assertEquals(recipeList.get(0), recipe);
            assertEquals(recipeList.size(), 1);

            // saveARecipe

        }
        catch(Exception e){
            e.printStackTrace();
        }
    }

    @Test
    public void testRecipeExistsAndSaveRecipe(){
        try{
            recipeList=saveRecipe.saveARecipe(recipeList, recipe);
            assertEquals(recipeList.get(0), recipe);
            boolean result = recipe.recipeExists(recipeList);
            assertTrue(result);

            assertFalse(recipe2.recipeExists(recipeList));


        }
        catch(Exception e){
            e.printStackTrace();
        }
        


    }

    

}