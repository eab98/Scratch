package scratch.model;

public class PenAction extends Instruction {

    public PenAction(String name, InstructionType type) {
        super(name, type, null);
    }

    @Override
    public Instruction newInstruction() {
        return new PenAction(this.getName(), this.getType());
    }

    public boolean changePenStatus(boolean status) {
        if (status && this.getType() == InstructionType.PEN_DOWN || !status && this.getType() == InstructionType.PEN_UP)
            return status;
        return !status;
    }

    @Override
    public Instruction duplicate() {
        return new PenAction(this.getName(), this.getType());
    }
}
