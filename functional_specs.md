# Functional specifications

## Operation
1. App start
2. Init GUI
3. Init renderer
4. STATE -> App idle

#### Actions
* Idle
    * Change machine
        1. Promt selection
        2. Apply change
    * Change specs
        1. Check if specification fields filled correctly
            * Yes : Apply change
            * No : Mark wrong fileds
    * Start simulation
        1. Start PV diagram animation
        2. Start machine animation
        3. Start power calculator
        4. Start power rewrite
        5. STATE -> Running
* Running
    * Stop simulation
        1. Stop PV diagram animation
        2. Stop machine animation
        3. Stop power calculator
        4. Stop power rewrite
        5. STATE -> Idle

