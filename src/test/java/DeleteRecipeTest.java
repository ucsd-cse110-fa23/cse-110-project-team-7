import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
public class DeleteRecipeTest {

    private Recipe recipe;
    private Recipe recipeTwo;
    private ArrayList<Recipe> recipeList;

    @BeforeEach
    public void setUp(){

       recipe = new Recipe();
       recipeList = new ArrayList<>();
       recipe.setTitle("[Egg and Rice]");
       recipe.setIngredients("Egg, Rice");
       recipe.setInstructions("Instructions: Cook");
       recipeTwo = new Recipe();
       recipeTwo.setTitle("[Steak]");
       recipeTwo.setIngredients("Steak, salt, pepper");
       recipeTwo.setInstructions("Instructions: Cook a lot");
    }

    
    @Test
    public void testDeleteCorrect(){
        try{
            recipeList.add(recipe);
            recipeList.add(recipeTwo);
            assertEquals(recipeList.size(), 2);
            DeleteRecipe.deleteTargetRecipe(recipeList, recipeTwo);
            assertEquals(recipeList.size(), 1);
            assertEquals(recipeList.get(0), recipe);
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }

    @Test
    public void testDeleteEmpty(){
        try{
            DeleteRecipe.deleteTargetRecipe(recipeList, recipeTwo);
            assertEquals(recipeList.size(), 0);
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }

}