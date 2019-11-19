package handler;

import optimize.Optimizer;

import java.util.List;
import java.util.Map;

public class OptimizerHandler {
    public Map<Integer, String> handleRequest(Map<String, List<Object>> input) {
        return new Optimizer(input).optimize();
    }
}
