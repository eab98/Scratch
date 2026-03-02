package scratch.viewmodel;

import javafx.collections.ObservableList;
import scratch.model.Instruction;
import scratch.model.Program;

public class ScratchViewModel {

    public ObservableList<Instruction> getInstru() {
        return Program.getCatalogue();
    }

}
