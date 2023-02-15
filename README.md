# draft

This repository contains code that I use in my research. It is not linked to any article in particular.

## Packages

- **pull_model** contains the core classes to simulate gossip-like dynamics.
- **tests** contains the definition of some behaviors, and is used to launch simulations.
- **export** contains the code that prints the results of the simulations to a file. I use xml_reader.py (Python 3) to read this file and plot the results.

## Usage under Linux

- Download the repository
- Open a terminal and go to the **src** folder

### Running the simulation

- Compile with

		javac -d . tests/Main.java
		
- Run with

		java tests.Main
		
The program does not take any parameter.
To run a specific simulation, one has to change the source code manually and compile again.

### Plotting the results

- To plot the latest file, simply use

		python3 xml_reader.py
		
- To plot a specific generated xml file, use

		python3 xml_reader.py <filename>
		
- To save a png version of the figure, add *-s* or *--save* before the filename

		python3 xml_reader.py -s <filename>