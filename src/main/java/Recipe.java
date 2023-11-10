import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.Writer;

public class Recipe{ 

    public String mealType; 
    public String ingredients;
    public String title;
    public String instructions; 


    public String getMealType(){
        return this.mealType;
    }

    public String getTitle(){
        return this.title;

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

    public void setTitle(String input){
        int start = input.indexOf("[");
        int end = input.indexOf("]");
        this.title = input.substring(start + 1, end).trim();
        
    }

    public void setInstructions(String input){
        int start = input.indexOf("]");
        this.instructions = input.substring(start + 1, input.length());
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

    public Recipe(){
        this.mealType = ""; 
        this.ingredients = "";
        this.instructions = "";
    }
    public void saveToFile(Recipe recipe){
        try{
            FileWriter fw = new FileWriter("./recipeName.csv", false);
            Writer writer = new BufferedWriter(fw);
            writer.write(recipe.getTitle()+ ";" + '\n');
            System.out.println("Save___" + recipe.getTitle());
            writer.write(recipe.getInstructions() + ";" + '\n');
            System.out.println("Save___" + recipe.getInstructions());
            writer.flush();
            writer.close();
            fw.close();
        }
        catch(Exception exception){
            System.out.println("Not save to file");
        }
        //System.out.println(123);
    }
}
