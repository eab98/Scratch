package scratch.viewmodel;

import scratch.model.Program;

import java.io.File;

public class MainViewModel {
    private final Program program;

    public MainViewModel(Program program) {
        this.program = program;
    }

    public void saveProgram(File file) {
        if (file != null) {
            // je dois encore le coder
        }
    }

    public void loadProgram(File file) {
        if (file != null) {
            // je dois encore le coder
        }
    }
}
