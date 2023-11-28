import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;



public class FilterTest {
    private Recipe recipe;
    private Recipe recipe2;
    private Recipe recipe3;

    private ArrayList<Recipe> recipeList;

    @BeforeEach
    public void setUp(){

       recipe = new Recipe();
       recipeList = new ArrayList<>();
       recipe.setTitle("[Egg and Rice]");
       recipe.setIngredients("Egg, Rice");
       recipe.setInstructions("Instructions: ");
       recipe.setMealType("Breakfast");

       recipe2 = new Recipe();
       recipe2.setTitle("[Beef and Rice]");
       recipe2.setIngredients("Beef and rice");
       recipe2.setInstructions("Instructions");
       recipe2.setMealType("Lunch");


       recipe3 = new Recipe();
       recipe3.setTitle("[Zebra]");
       recipe3.setIngredients("Zebra");
       recipe3.setInstructions("Instructions");
       recipe3.setMealType("Dinner");
    }

    @Test
    public void filterTest(){
        try{

            recipeList = saveRecipe.saveARecipe(recipeList, recipe);
            assertEquals(recipeList.get(0), recipe);
            // add on the top of the list 
            recipeList = saveRecipe.saveARecipe(recipeList, recipe2); 
            assertEquals(recipeList.get(0), recipe2);
            recipeList = saveRecipe.saveARecipe(recipeList, recipe3); 
            assertEquals(recipeList.get(0), recipe3);
            // [Beef and Rice, Egg and Rice, Zebra]
            // saveARecipe
            ArrayList<Recipe> expected = new ArrayList<Recipe>();
            // add breakfast
            
            expected.add(recipe);
            ArrayList<Recipe> breakfast = Filter.filterRecipes(recipeList, "Breakfast");
            assertEquals(breakfast, expected);

            // add lunch
            expected = new ArrayList<Recipe>();
            expected.add(recipe2);

            ArrayList<Recipe> lunch = Filter.filterRecipes(recipeList, "Lunch");
            assertEquals(lunch, expected);

            // add dinner
            expected = new ArrayList<Recipe>();
            expected.add(recipe3);

            ArrayList<Recipe> dinner = Filter.filterRecipes(recipeList, "Dinner");
            assertEquals(dinner, expected);

            // add all
            expected = new ArrayList<Recipe>();
            expected.add(recipe3);
            expected.add(recipe2);
            expected.add(recipe);

            ArrayList<Recipe> all = Filter.filterRecipes(recipeList, "all");
            assertEquals(all, expected);

        }
        catch(Exception e){
            e.printStackTrace();
        }

    }

    


}