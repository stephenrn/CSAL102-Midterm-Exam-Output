import java.util.*;

class MooreMachine {
    Set<String> states;
    Set<String> inputAlphabet;
    Map<String, Map<String, String>> transitionTable; // state -> input -> nextState
    Map<String, String> outputPerState; // state -> output

    public MooreMachine(Set<String> states, Set<String> inputAlphabet,
                        Map<String, Map<String, String>> transitionTable,
                        Map<String, String> outputPerState) {
        this.states = states;
        this.inputAlphabet = inputAlphabet;
        this.transitionTable = transitionTable;
        this.outputPerState = outputPerState;
    }
}

class MealyMachine {
    Set<String> states;
    Set<String> inputAlphabet;
    Map<String, Map<String, String>> transitionTable; // state -> input -> nextState
    Map<String, Map<String, String>> outputPerTransition; // state -> input -> output

    public MealyMachine(Set<String> states, Set<String> inputAlphabet,
                        Map<String, Map<String, String>> transitionTable,
                        Map<String, Map<String, String>> outputPerTransition) {
        this.states = states;
        this.inputAlphabet = inputAlphabet;
        this.transitionTable = transitionTable;
        this.outputPerTransition = outputPerTransition;
    }
}

public class MooreToMealyConverter {
    public static void main(String[] args) {
        // Example: Read Moore machine from hardcoded test cases
        List<MooreMachine> testCases = getTestCases();

        for (int i = 0; i < testCases.size(); i++) {
            System.out.println("=== Test Case " + (i + 1) + " ===");
            MooreMachine moore = testCases.get(i);
            printMooreMachine(moore);
            MealyMachine mealy = convertToMealy(moore);
            printMealyMachine(mealy);
            System.out.println();
        }
    }

    // Conversion algorithm
    public static MealyMachine convertToMealy(MooreMachine moore) {
        Map<String, Map<String, String>> mealyTransitions = new HashMap<>();
        Map<String, Map<String, String>> mealyOutputs = new HashMap<>();

        for (String state : moore.states) {
            mealyTransitions.put(state, new HashMap<>());
            mealyOutputs.put(state, new HashMap<>());
            for (String input : moore.inputAlphabet) {
                String nextState = moore.transitionTable.get(state).get(input);
                String output = moore.outputPerState.get(nextState);
                mealyTransitions.get(state).put(input, nextState);
                mealyOutputs.get(state).put(input, output);
            }
        }
        return new MealyMachine(moore.states, moore.inputAlphabet, mealyTransitions, mealyOutputs);
    }

    // Utility: Print Moore machine
    public static void printMooreMachine(MooreMachine moore) {
        System.out.println("Moore Machine:");
        System.out.println("States: " + moore.states);
        System.out.println("Input Alphabet: " + moore.inputAlphabet);
        System.out.println("Transition Table:");
        for (String state : moore.states) {
            for (String input : moore.inputAlphabet) {
                String next = moore.transitionTable.get(state).get(input);
                System.out.println("  δ(" + state + ", " + input + ") = " + next);
            }
        }
        System.out.println("Output per State:");
        for (String state : moore.states) {
            System.out.println("  λ(" + state + ") = " + moore.outputPerState.get(state));
        }
    }

    // Utility: Print Mealy machine
    public static void printMealyMachine(MealyMachine mealy) {
        System.out.println("Equivalent Mealy Machine:");
        System.out.println("States: " + mealy.states);
        System.out.println("Input Alphabet: " + mealy.inputAlphabet);
        System.out.println("Transition Table and Output per Transition:");
        for (String state : mealy.states) {
            for (String input : mealy.inputAlphabet) {
                String next = mealy.transitionTable.get(state).get(input);
                String out = mealy.outputPerTransition.get(state).get(input);
                System.out.println("  δ(" + state + ", " + input + ") = " + next +
                                   ", λ(" + state + ", " + input + ") = " + out);
            }
        }
    }

    // Three test cases of increasing complexity
    public static List<MooreMachine> getTestCases() {
        List<MooreMachine> cases = new ArrayList<>();

        // Test Case 1: Simple 2-state Moore machine
        Set<String> states1 = new HashSet<>(Arrays.asList("A", "B"));
        Set<String> input1 = new HashSet<>(Arrays.asList("0", "1"));
        Map<String, Map<String, String>> trans1 = new HashMap<>();
        trans1.put("A", Map.of("0", "A", "1", "B"));
        trans1.put("B", Map.of("0", "A", "1", "B"));
        Map<String, String> out1 = Map.of("A", "X", "B", "Y");
        cases.add(new MooreMachine(states1, input1, trans1, out1));

        // Test Case 2: 3-state Moore machine
        Set<String> states2 = new HashSet<>(Arrays.asList("S0", "S1", "S2"));
        Set<String> input2 = new HashSet<>(Arrays.asList("a", "b"));
        Map<String, Map<String, String>> trans2 = new HashMap<>();
        trans2.put("S0", Map.of("a", "S1", "b", "S2"));
        trans2.put("S1", Map.of("a", "S0", "b", "S2"));
        trans2.put("S2", Map.of("a", "S2", "b", "S1"));
        Map<String, String> out2 = Map.of("S0", "0", "S1", "1", "S2", "2");
        cases.add(new MooreMachine(states2, input2, trans2, out2));

        // Test Case 3: 4-state, more complex
        Set<String> states3 = new HashSet<>(Arrays.asList("Q0", "Q1", "Q2", "Q3"));
        Set<String> input3 = new HashSet<>(Arrays.asList("x", "y"));
        Map<String, Map<String, String>> trans3 = new HashMap<>();
        trans3.put("Q0", Map.of("x", "Q1", "y", "Q2"));
        trans3.put("Q1", Map.of("x", "Q3", "y", "Q0"));
        trans3.put("Q2", Map.of("x", "Q2", "y", "Q3"));
        trans3.put("Q3", Map.of("x", "Q1", "y", "Q2"));
        Map<String, String> out3 = Map.of("Q0", "A", "Q1", "B", "Q2", "C", "Q3", "D");
        cases.add(new MooreMachine(states3, input3, trans3, out3));

        return cases;
    }
}
