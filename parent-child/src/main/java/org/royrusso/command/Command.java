package org.royrusso.command;

import java.io.Serializable;

public class Command implements Serializable {

    private final String data;

    private final long id;

    private final int type;

    public Command(String data, long id, int type) {
        this.data = data;
        this.id = id;
        this.type = type;
    }

    public int getType() {
        return this.type;
    }

    public String getData() {
        return this.data;
    }

    @Override
    public String toString() {
        return "Command{" + "data='" + data + '\'' + '}';
    }
}
