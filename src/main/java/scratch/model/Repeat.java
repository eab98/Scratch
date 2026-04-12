package scratch.model;


public class Repeat extends Instruction {

    public Repeat(String name, InstructionType type, Value value) {
        super(name, type, value);
    }

    @Override
    public void execute(Character character) {
        if (this.getType() == InstructionType.REPEAT) {
            Value val = this.getValue();
            val.setValue(val.getValue() - 1);
        }
    }

    @Override
    public Instruction newInstruction() {
        Value def = new Value(4);
        if (this.getType() == InstructionType.END_REPEAT)
            def.setValue(0);
        return new Repeat(this.getName(), this.getType(), def);
    }

    @Override
    public Instruction duplicate() {
        return new Repeat(this.getName(), this.getType(), this.getValue());
    }

    @Override
    public String toString() {
        return this.getName();
    }
}
