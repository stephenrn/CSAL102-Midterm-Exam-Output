import java.util.*;

// Represents a Moore machine, where outputs are associated with states
class MooreMachine {
    Set<String> states; // Set of all states in the Moore machine
    Set<String> inputAlphabet; // Set of all possible input symbols
    Map<String, Map<String, String>> transitionTable; // Maps: state -> (input -> nextState)
    Map<String, String> outputPerState; // Maps: state -> output symbol

    // Constructor to initialize all fields of the Moore machine
    public MooreMachine(Set<String> states, Set<String> inputAlphabet,
                        Map<String, Map<String, String>> transitionTable,
                        Map<String, String> outputPerState) {
        this.states = states;
        this.inputAlphabet = inputAlphabet;
        this.transitionTable = transitionTable;
        this.outputPerState = outputPerState;
    }
}

// Represents a Mealy machine, where outputs are associated with transitions
class MealyMachine {
    Set<String> states; // Set of all states in the Mealy machine
    Set<String> inputAlphabet; // Set of all possible input symbols
    Map<String, Map<String, String>> transitionTable; // Maps: state -> (input -> nextState)
    Map<String, Map<String, String>> outputPerTransition; // Maps: state -> (input -> output symbol)

    // Constructor to initialize all fields of the Mealy machine
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
        // Prepare a list of Moore machine test cases (hardcoded for demonstration)
        List<MooreMachine> testCases = getTestCases();

        // For each test case, print the Moore machine, convert it, and print the Mealy machine
        for (int i = 0; i < testCases.size(); i++) {
            System.out.println("=== Test Case " + (i + 1) + " ===");
            MooreMachine moore = testCases.get(i);
            printMooreMachine(moore); // Show the Moore machine details
            MealyMachine mealy = convertToMealy(moore); // Perform the conversion
            printMealyMachine(mealy); // Show the resulting Mealy machine
            System.out.println();
        }
    }

    // Converts a Moore machine to an equivalent Mealy machine
    // For each transition, the output is the output of the destination state in the Moore machine
    public static MealyMachine convertToMealy(MooreMachine moore) {
        Map<String, Map<String, String>> mealyTransitions = new HashMap<>(); // Will store transitions for Mealy
        Map<String, Map<String, String>> mealyOutputs = new HashMap<>(); // Will store outputs for each transition

        // For every state in the Moore machine
        for (String state : moore.states) {
            mealyTransitions.put(state, new HashMap<>());
            mealyOutputs.put(state, new HashMap<>());
            // For every possible input symbol
            for (String input : moore.inputAlphabet) {
                // Find the next state for this (state, input) pair
                String nextState = moore.transitionTable.get(state).get(input);
                // The output for this transition is the output of the destination state in the Moore machine
                String output = moore.outputPerState.get(nextState);
                // Store the transition and its output in the Mealy machine's tables
                mealyTransitions.get(state).put(input, nextState);
                mealyOutputs.get(state).put(input, output);
            }
        }
        // Return a new MealyMachine object with the constructed tables
        return new MealyMachine(moore.states, moore.inputAlphabet, mealyTransitions, mealyOutputs);
    }

    // Utility function to print the details of a Moore machine
    public static void printMooreMachine(MooreMachine moore) {
        System.out.println("Moore Machine:");
        System.out.println("States: " + moore.states);
        System.out.println("Input Alphabet: " + moore.inputAlphabet);
        System.out.println("Transition Table:");
        // Print each transition in the table
        for (String state : moore.states) {
            for (String input : moore.inputAlphabet) {
                String next = moore.transitionTable.get(state).get(input);
                System.out.println("  δ(" + state + ", " + input + ") = " + next);
            }
        }
        System.out.println("Output per State:");
        // Print the output associated with each state
        for (String state : moore.states) {
            System.out.println("  λ(" + state + ") = " + moore.outputPerState.get(state));
        }
    }

    // Utility function to print the details of a Mealy machine
    public static void printMealyMachine(MealyMachine mealy) {
        System.out.println("Equivalent Mealy Machine:");
        System.out.println("States: " + mealy.states);
        System.out.println("Input Alphabet: " + mealy.inputAlphabet);
        System.out.println("Transition Table and Output per Transition:");
        // Print each transition and its output
        for (String state : mealy.states) {
            for (String input : mealy.inputAlphabet) {
                String next = mealy.transitionTable.get(state).get(input);
                String out = mealy.outputPerTransition.get(state).get(input);
                System.out.println("  δ(" + state + ", " + input + ") = " + next +
                                   ", λ(" + state + ", " + input + ") = " + out);
            }
        }
    }

    // Returns a list of three Moore machine test cases of increasing complexity
    public static List<MooreMachine> getTestCases() {
        List<MooreMachine> cases = new ArrayList<>();

        // Test Case 1: Simple 2-state Moore machine
        Set<String> states1 = new HashSet<>(Arrays.asList("A", "B"));
        Set<String> input1 = new HashSet<>(Arrays.asList("0", "1"));
        Map<String, Map<String, String>> trans1 = new HashMap<>();
        // Define transitions for state A and B
        trans1.put("A", Map.of("0", "A", "1", "B"));
        trans1.put("B", Map.of("0", "A", "1", "B"));
        // Define outputs for each state
        Map<String, String> out1 = Map.of("A", "X", "B", "Y");
        cases.add(new MooreMachine(states1, input1, trans1, out1));

        // Test Case 2: 3-state Moore machine
        Set<String> states2 = new HashSet<>(Arrays.asList("S0", "S1", "S2"));
        Set<String> input2 = new HashSet<>(Arrays.asList("a", "b"));
        Map<String, Map<String, String>> trans2 = new HashMap<>();
        // Define transitions for S0, S1, S2
        trans2.put("S0", Map.of("a", "S1", "b", "S2"));
        trans2.put("S1", Map.of("a", "S0", "b", "S2"));
        trans2.put("S2", Map.of("a", "S2", "b", "S1"));
        // Define outputs for each state
        Map<String, String> out2 = Map.of("S0", "0", "S1", "1", "S2", "2");
        cases.add(new MooreMachine(states2, input2, trans2, out2));

        // Test Case 3: 4-state, more complex Moore machine
        Set<String> states3 = new HashSet<>(Arrays.asList("Q0", "Q1", "Q2", "Q3"));
        Set<String> input3 = new HashSet<>(Arrays.asList("x", "y"));
        Map<String, Map<String, String>> trans3 = new HashMap<>();
        // Define transitions for Q0, Q1, Q2, Q3
        trans3.put("Q0", Map.of("x", "Q1", "y", "Q2"));
        trans3.put("Q1", Map.of("x", "Q3", "y", "Q0"));
        trans3.put("Q2", Map.of("x", "Q2", "y", "Q3"));
        trans3.put("Q3", Map.of("x", "Q1", "y", "Q2"));
        // Define outputs for each state
        Map<String, String> out3 = Map.of("Q0", "A", "Q1", "B", "Q2", "C", "Q3", "D");
        cases.add(new MooreMachine(states3, input3, trans3, out3));

        // Return the list of test cases
        return cases;
    }
}
