package ca.jfbconception.boot.logging;


public class Logger {

    private String name;

    private String level;

    public Logger() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    @Override
    public String toString() {
        return "LoggerDTO{" + "name='" + name + '\'' + ", level='" + level + '\'' + '}';
    }

}
