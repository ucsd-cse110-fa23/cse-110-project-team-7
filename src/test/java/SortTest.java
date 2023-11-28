import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
public class SortTest {
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

       recipe2 = new Recipe();
       recipe2.setTitle("[Beef and Rice]");
       recipe2.setIngredients("Beef and rice");
       recipe2.setInstructions("Instructions");


       recipe3 = new Recipe();
       recipe3.setTitle("[Zebra]");
       recipe3.setIngredients("Zebra");
       recipe3.setInstructions("Instructions");

    }

    @Test
    public void alphabeticalAZSortUsingSaveARecipeTest(){
        try{

            recipeList=saveRecipe.saveARecipe(recipeList, recipe);
            assertEquals(recipeList.get(0), recipe);
            // add on the top of the list 
            recipeList = saveRecipe.saveARecipe(recipeList, recipe2); 
            assertEquals(recipeList.get(0), recipe2);
            recipeList = saveRecipe.saveARecipe(recipeList, recipe3); 
            assertEquals(recipeList.get(0), recipe3);
            // [Beef and Rice, Egg and Rice, Zebra]
            // saveARecipe

            ArrayList<Recipe> expected = new ArrayList<Recipe>();
            expected.add(recipe2);
            expected.add(recipe);
            expected.add(recipe3);

            recipeList = Sort.alphabeticalAZSort(recipeList);
            assertEquals(recipeList, expected);




        }
        catch(Exception e){
            e.printStackTrace();
        }

    }

     @Test
    public void alphabeticalZASortUsingSaveARecipeTest(){
        try{

            recipeList=saveRecipe.saveARecipe(recipeList, recipe);
            assertEquals(recipeList.get(0), recipe);
            // add on the top of the list 
            recipeList = saveRecipe.saveARecipe(recipeList, recipe2); 
            assertEquals(recipeList.get(0), recipe2);
            recipeList = saveRecipe.saveARecipe(recipeList, recipe3); 
            assertEquals(recipeList.get(0), recipe3);
            // [Zebra, Beef and Rice, Egg and Rice ]
            // saveARecipe

            ArrayList<Recipe> expected = new ArrayList<Recipe>();
            expected.add(recipe3);
            expected.add(recipe);
            expected.add(recipe2);

            recipeList = Sort.alphabeticalZASort(recipeList);
            assertEquals(recipeList, expected);

        }
        catch(Exception e){
            e.printStackTrace();
        }

    }

    @Test
    public void oldestToNewestSortUsingSaveARecipeTest(){
        try{

            recipeList=saveRecipe.saveARecipe(recipeList, recipe);
            assertEquals(recipeList.get(0), recipe);
            // add on the top of the list 
            recipeList = saveRecipe.saveARecipe(recipeList, recipe2); 
            assertEquals(recipeList.get(0), recipe2);
            recipeList = saveRecipe.saveARecipe(recipeList, recipe3); 
            assertEquals(recipeList.get(0), recipe3);
            // [Zebra, Beef and Rice, Egg and Rice ]
            // saveARecipe

            ArrayList<Recipe> expected = new ArrayList<Recipe>();
            expected.add(recipe);
            expected.add(recipe2);
            expected.add(recipe3);

            recipeList = Sort.oldestToNewestSort(recipeList);
            assertEquals(recipeList, expected);

        }
        catch(Exception e){
            e.printStackTrace();
        }
    }


}