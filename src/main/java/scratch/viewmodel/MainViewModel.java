package scratch.viewmodel;

import scratch.model.Program;

import java.io.File;

public class MainViewModel {
    private final Program program;
    private final ScratchViewModel scratchViewModel;

    public MainViewModel(Program program) {
        this.program = program;
        this.scratchViewModel = new ScratchViewModel(program);
    }

    public ScratchViewModel getScratchViewModel() {
        return scratchViewModel;
    }

    public void saveProgram(File file) {
        if (file != null) {
            program.saveToFile(file);
        }
    }

    public void loadProgram(File file) {
        if (file != null) {
            program.loadFromFile(file);
        }
    }
}
