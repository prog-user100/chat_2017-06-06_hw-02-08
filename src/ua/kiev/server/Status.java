package ua.kiev.server;

public enum Status {
    ON_LINE("on line"),
    OFF_LINE("off line");
    private String status;

    private Status(String status) {
        this.status = status;
    }

    public String print(){
        return status;
    }
}
