public class Recipe{ 

    public String mealType; 
    public String ingredients;
    public String title;
    public String instructions; 

    public Recipe(){
        this.title = "";
        this.mealType = ""; 
        this.ingredients = "";
        this.instructions = "";
    }

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
        String tmp = input.substring(0, 1).toUpperCase() + input.substring(1, input.length());
        this.mealType = tmp.replaceAll("[^a-zA-Z]+$", "");
    }

    public void setIngredients(String input){
        this.ingredients = input;
    }

    public void loadInstructions(String input) {
        this.instructions = input;
    }

    public void setTitle(String input){
        if(input.contains("[")){
            int start = input.indexOf("[");
            int end = input.indexOf("]");
            this.title = input.substring(start + 1, end).trim() + " - " + this.getMealType();
        }
        else{
            this.title = input.substring(7, input.indexOf(","));

        }
        
        
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

    
}