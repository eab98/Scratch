package scratch.model;


public class Repeat extends Instruction {
    private int loop;

    public Repeat(String name, InstructionType type, Value value) {
        super(name, type, value);
        loop = this.getValue().getValue();
    }

    @Override
    public void execute(Character character) {
        if (this.getType() == InstructionType.REPEAT) {
            Value val = this.getValue();
            if (val.getValue() == 0)
                val.setValue(loop);
            val.setValue(val.getValue() - 1);
        }
    }

    @Override
    public void setParameter(Value value) {
        super.setParameter(value);
        loop = this.getValue().getValue();
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
