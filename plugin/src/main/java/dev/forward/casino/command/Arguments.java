package dev.forward.casino.command;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Getter
@Setter
public class Arguments {
    private String[] args;

    public Arguments(String[] args) {
        this.args = args;
    }

    public boolean isEmpty() {
        if(args == null)
            return true;
        return args.length == 0;
    }
    public String get(int index) {
        if (args.length > index) {
            return args[index];
        }
        return "";
    }
    public int asInt(int index) {
        return Integer.parseInt(get(index));
    }
    public double asDouble(int index) {
        return Double.parseDouble(get(index));
    }
    public float asFloat(int index) {
        return Float.parseFloat(get(index));
    }
}