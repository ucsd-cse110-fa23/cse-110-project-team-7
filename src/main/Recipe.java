package main;
public class Recipe{ 

    public String mealType; 
    public String ingredients;
    public String instructions; 


    public String getMealType(){
        return this.mealType;
    }

    public String getIngredients(){
        return this.ingredients;
    }

    public void setMealType(String input){
        this.mealType = input;
    }

    public void setIngredients(String input){
        this.ingredients = input;
    }

    public String getInstructions(){
        return this.instructions;
    }

    public boolean checkMealType(String input){
        String result = input.replaceAll("[^a-zA-Z]+$", "").toLowerCase();

        if(result.equals("breakfast") || result.equals("lunch") ||
                     result.equals("dinner") ){
                        return true;
            }
        return false; 

    }

    public void setInstructions(String input){
        this.instructions = input;
    }

    public Recipe(){
        this.mealType = ""; 
        this.ingredients = "";
        this.instructions = "";
    }
}