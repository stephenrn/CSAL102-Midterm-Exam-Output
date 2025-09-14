# CSAL102-Midterm-Exam-Output

# Moore Machine to Mealy Machine Converter

## Approach and Theory

A **Moore machine** associates outputs with states, while a **Mealy machine** associates outputs with transitions. To convert a Moore machine to an equivalent Mealy machine:
- For each transition from state `S` to state `T` on input `a`, the Mealy output is the output associated with state `T` in the Moore machine.

## Algorithm

1. For each state and input in the Moore machine:
    - Find the destination state for the transition.
    - Assign the output of the destination state to the corresponding Mealy transition.

## Testing

The program includes three test cases:
1. A simple 2-state Moore machine.
2. A 3-state Moore machine with more transitions.
3. A 4-state Moore machine with more complex transitions.

## How to Run

Compile and run the Java program:
```sh
javac src/MooreToMealyConverter.java
java -cp src MooreToMealyConverter
```

## AI Assistance

Parts of this project (Java code and documentation) were AI-assisted using GitHub Copilot. All AI-generated code was reviewed, tested, and adjusted for correctness and clarity by the project authors.