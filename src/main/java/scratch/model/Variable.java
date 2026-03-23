package scratch.model;

public class Variable {
    private String name;
    private Integer value;


    public Variable(String name) {
        this.name = name;
        this.value = 0;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        if(name.matches("^[a-zA-Z_][a-zA-Z0-9_]*$"))  this.name = name;
    }
}
