package com.codeforge.judge.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Table(name = "programming_language")
public class ProgrammingLanguage extends BaseEntity {

    @Column(nullable = false, unique = true, length = 50)
    private String name;

    @Column(length = 32)
    private String version;

    @Column(name = "compile_command", length = 255)
    private String compileCommand;

    @Column(name = "run_command", length = 255)
    private String runCommand;

    @Column(name = "enable_submission")
    private boolean enableSubmission = true;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getCompileCommand() {
        return compileCommand;
    }

    public void setCompileCommand(String compileCommand) {
        this.compileCommand = compileCommand;
    }

    public String getRunCommand() {
        return runCommand;
    }

    public void setRunCommand(String runCommand) {
        this.runCommand = runCommand;
    }

    public boolean isEnableSubmission() {
        return enableSubmission;
    }

    public void setEnableSubmission(boolean enableSubmission) {
        this.enableSubmission = enableSubmission;
    }
}

