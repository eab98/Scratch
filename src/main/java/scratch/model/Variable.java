package scratch.model;

public class Variable extends Value{
    private String name;

    public Variable(String name) {
        super(0);
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        if(name.matches("^[a-zA-Z_][a-zA-Z0-9_]*$"))  this.name = name;
    }
}
