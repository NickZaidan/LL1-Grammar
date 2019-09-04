//Name: Nicholas Zaidan


//Imports
import java.util.Stack;
import java.io.File;
import java.util.Scanner;
import java.util.ArrayList;

public class Parser{

	//Constants
	static final String ACCEPTED = "ACCEPTED";
	static final String REJECTED = "REJECTED";
	static final String INVALID = "ERROR_INVALID_SYMBOL";
	static final String[][] lookup = new String[][]{
		//10x17 Matrix. This is the hardcoded version of the grammar G' parse/lookup table
  	{"",  "a",   "b",   "c",   "d",   "0",   "1",   "2",   "3",  "(",   ")",   "+",   "-",   "*",  "print", "if", "$"},
  	{"L", "EN",  "EN",  "EN",  "EN",  "EN",  "EN",  "EN",  "EN",  "EN", "BAD", "BAD", "BAD", "BAD","BAD",   "BAD","BAD"},
  	{"N", "EN",  "EN",  "EN",  "EN",  "EN",  "EN",  "EN",  "EN",  "EN",  "",   "BAD", "BAD", "BAD","BAD",   "BAD", ""},
  	{"E", "V",   "V",   "V",   "V",   "T",   "T",   "T",   "T",   "(G)", "BAD", "BAD", "BAD", "BAD", "BAD", "(G)", "BAD"},
  	{"G", "BAD", "BAD", "BAD", "BAD", "BAD", "BAD", "BAD", "BAD", "BAD", "BAD", "F",   "F",   "F",  "F", "C", "BAD"},
		{"C", "BAD", "BAD", "BAD", "BAD", "BAD", "BAD", "BAD", "BAD", "BAD", "BAD", "BAD", "BAD", "BAD", "BAD", "ifEEI", "BAD"},
		{"I", "E", "E", "E", "E", "E", "E", "E", "E", "BAD", "", "BAD", "BAD", "BAD", "BAD", "BAD", "BAD", "BAD"},
		{"F", "BAD", "BAD", "BAD", "BAD", "BAD", "BAD", "BAD", "BAD", "BAD", "BAD", "+L",  "-L",  "*L",  "printL","BAD", "BAD"},
		{"V", "a",   "b",   "c",   "d",   "BAD", "BAD", "BAD", "BAD", "BAD", "BAD", "BAD", "BAD", "BAD", "BAD", "BAD", "BAD"},
		{"T", "BAD", "BAD", "BAD", "BAD", "0",   "1",   "2",   "3", "BAD", "BAD", "BAD", "BAD", "BAD", "BAD", "BAD", "BAD"}
	};

	//Class variables
	static Stack<String> inputStack = new Stack<String>(); //The stack of the inputs (put in in reverse order)
	static Stack<String> checkingStack = new Stack<String>(); //The stack for the parse table
	static boolean errorChecker = false; //The extension variable
	static boolean incorrectSymbol = false; //If a symbol is put in that is not a terminal
	static int errorVariable; //Index of the variable when using the extension
	static StringBuilder stringSoFar = new StringBuilder(); //The string for the input to track changes as it goes
	static StringBuilder grammarSoFar = new StringBuilder(); //The grammar changes throughout the lifecycle of the sentence



	//Extension A, Error Identification
	public static void errorIdentify(){
		System.out.print("Error: got " + inputStack.peek() + ", but expected {");
		printingResultOfRule();
		System.out.println("}.");
	}








	public static void printingResultOfRule(){
		ArrayList<String> list = new ArrayList<String>();
		for(int i = 1; i < lookup[0].length; i++){
			if(lookup[errorVariable][i] != "BAD"){
				if(!list.contains(lookup[errorVariable][i])){
					if(lookup[errorVariable][i].equals("")){
						list.add("epsilon");
					} else{
						list.add(lookup[errorVariable][i]);
					}
				}
			}
		}
		for(int i = 0; i < list.size(); i++){
			if(i == list.size() - 1){
					System.out.print(list.get(i));
			} else{
				System.out.print(list.get(i) + ", ");
			}
		}

	}


	//Making sure the input only has terminal characters. Done in the ugliest way possible for your reading pleasure
	public static boolean isValidInput(){
		String tmp = stringSoFar.toString();
		int x = 0;
		for(int i = 0; i < tmp.length(); i++){
			String c = "" + tmp.charAt(i);
			if(c.equals("(")){
				continue;
			}
			else if(c.equals(")")){
				continue;
			}
			else if(c.equals("a")){
				continue;
			}
			else if(c.equals("b")){
				continue;
			}
			else if(c.equals("c")){
				continue;
			}
			else if(c.equals("d")){
				continue;
			}
			else if(c.equals("0")){
				continue;
			}
			else if(c.equals("1")){
				continue;
			}
			else if(c.equals("2")){
				continue;
			}
			else if(c.equals("3")){
				continue;
			}
			else if(c.equals("+")){
				continue;
			}
			else if(c.equals("-")){
				continue;
			}
			else if(c.equals("*")){
				continue;
			}
			else if(c.equals("$")){
				continue;
			}

			else if(tmp.charAt(i) == 'p' && tmp.charAt(i + 1) == 'r' && tmp.charAt(i + 2) == 'i' && tmp.charAt(i + 3) == 'n' && tmp.charAt(i + 4) == 't'){
				i = i + 4;
				continue;
			}

			else if(tmp.charAt(i) == 'i' && tmp.charAt(i + 1) == 'f'){
				i = i + 1;
				continue;
			}

			else {
				incorrectSymbol = true;
				return false;
			}
		}
		return true;
	}

	//Main checking method
	public static boolean isValidString(){
		grammarSoFar.append("L");
		grammarSoFar.append("$");

		//First scan to see if any character is not a terminal
		if(!isValidInput()){
			return false;
		}

		//Loop until both stacks are empty
		while(!inputStack.isEmpty() && !checkingStack.isEmpty()){
			System.out.print(stringSoFar + " || " + grammarSoFar);
			System.out.println("");

			int variableIndex = findVaraibleIndex(checkingStack.peek()); //Find the variable index in relation to the lookup table
			int terminalIndex = findTerminalIndex(inputStack.peek()); //Find the terminal index in relation to the lookup table

			//If the characters are the same, remove both from stack and begin next iteration
			if(inputStack.peek().equals(checkingStack.peek())){
				if(inputStack.peek().equals("if")){
					stringSoFar.deleteCharAt(0);
					stringSoFar.deleteCharAt(0);
				}
				else if(inputStack.peek().equals("print")){
					stringSoFar.deleteCharAt(0);
					stringSoFar.deleteCharAt(0);
					stringSoFar.deleteCharAt(0);
					stringSoFar.deleteCharAt(0);
					stringSoFar.deleteCharAt(0);
				}

				else {
					stringSoFar.deleteCharAt(0);
				}
				if(checkingStack.peek().equals("if")){
					grammarSoFar.deleteCharAt(0);
					grammarSoFar.deleteCharAt(0);
				}
				else if(checkingStack.peek().equals("print")){
					grammarSoFar.deleteCharAt(0);
					grammarSoFar.deleteCharAt(0);
					grammarSoFar.deleteCharAt(0);
					grammarSoFar.deleteCharAt(0);
					grammarSoFar.deleteCharAt(0);
				}

				else {
					grammarSoFar.deleteCharAt(0);
				}

				inputStack.pop();
				checkingStack.pop();
				continue;
			}
			//Make sure the character was found on the parse table
			if(variableIndex != -1 && terminalIndex != -1){
				//Retrieve the string at the index to know what to put into the checking stack
				String replacement = lookup[variableIndex][terminalIndex];
				//System.out.println("Replacement is: " + replacement);

				//If BAD is found, then there is no rule in the parse table for the grammar, this rejecting import junit.framework.TestCase;
				if(replacement.equals("BAD")){
					errorVariable = variableIndex;
					return false;
				}

				//Remove the rule from the checking stack, and then put on the new rule found from the lookup table
				if(grammarSoFar.charAt(0) == 'i' && grammarSoFar.charAt(1) == 'f'){
					grammarSoFar.deleteCharAt(0);
					grammarSoFar.deleteCharAt(0);
				}
				else if(grammarSoFar.charAt(0) == 'p' && grammarSoFar.charAt(1) == 'r'  && grammarSoFar.charAt(2) == 'i'  && grammarSoFar.charAt(3) == 'n'  && grammarSoFar.charAt(4) == 't'){
					grammarSoFar.deleteCharAt(0);
					grammarSoFar.deleteCharAt(0);
					grammarSoFar.deleteCharAt(0);
					grammarSoFar.deleteCharAt(0);
					grammarSoFar.deleteCharAt(0);
				}
				else {
					grammarSoFar.deleteCharAt(0);

				}
				grammarSoFar.insert(0, replacement);
				checkingStack.pop();
				puttingCheckingStack(replacement);
			}

			//if either index is -1, then it was not found on the lookup and rejects the string
			else {
				return false;
			}
		}

		//If both stacks are empty, the sentence was accepted
		return true;
	}



	//Main method
	public static void main(String[] args) throws Exception{

		//If the arguments were inputted incorrectly
		if(!introductionCheckingMethod(args)){
			System.exit(1);
		}
		//Initialising the stacks with the start symbol
		inputStack.push("$");
		checkingStack.push("$");
		checkingStack.push("L");
		//Inputting the sentence into the stack

		puttingStack(sentenceConstruction(args[0]));

		//If the string in the file is accepted then accept, otherwise reject
		if(isValidString()){
			System.out.println(ACCEPTED);
		}
		else{

			//Extension checker
			if(errorChecker){
				errorIdentify();
			}
			else if(incorrectSymbol){
				System.out.println(INVALID);
			}
			System.out.println(REJECTED);
		}

	}







	//Parse Table Helper functions

	//Print the variables
	public static void printVariables(){
		for(int i = 1; i < lookup.length; i++){
			System.out.println(lookup[i][0]);
		}
	}

	//Print the terminals
	public static void printTerminals(){
		for(int i = 1; i < lookup[0].length; i++){
			System.out.print(lookup[0][i] + " ");
		}
		System.out.println("");
	}

	//Find the variable at its index, searches the leftmost column and returns it place in relation to i. If it doesn't exist, return -1
	public static int findVaraibleIndex(String variable){
		for(int i = 1; i < lookup.length; i++){
			if(variable.equals(lookup[i][0])){
				return i;
			}
		}
		return -1;
	}

	//Find the terminal at its index, searches the topmost row  and returns it place in relation to i. If it doesn't exist, return -1
	public static int findTerminalIndex(String terminal){
		for(int i = 1; i < lookup[0].length; i++){
			if(terminal.equals(lookup[0][i])){
				return i;
			}
		}
		return -1;
	}

	//ALL METHODS BELOW ARE JUST HELEPER STARTER FUNCTIONS, NOT RELEVANT TO THE ASSIGNMENT, MAINLY PASSING IN STRINGS AND INPUTTING VARIABLES

	//Print the lookup table
	public static void printLookUp(){
		for(int i = 1; i < lookup.length; i++){
			for(int j = 1; j < lookup[0].length; j++){
				System.out.print(lookup[i][j]);
			}
			System.out.println("");
		}
	}

	//Introductory method to check input
	public static boolean introductionCheckingMethod(String[] args){

		//If no arguments
		if(args.length < 1){
			System.out.println("Missing file or incorrect arguments");
			return false;
		}

		//Just a check of how many argument and if they are correct. Can definitely be written better
		if(args.length == 2 || args.length == 1){
			if(args.length == 2){
				if(args[1].equals("error")){
					errorChecker = true;
				}
			}
			else if (args.length > 2){
				System.out.println("Missing file or incorrect arguments");
				return false;
			}
		}
		else{
				System.out.println("Too many arguments");
				return false;
		}



		//If we make it this far, all arguments are valid
		return true;
	}


	//Combines the files text to a single string, and strips it of its whitespace
	public static String sentenceConstruction(String filename) throws Exception{
		String returnString = "";
		Scanner scanner = new Scanner(new File(filename));
		while(scanner.hasNext()){
			returnString = returnString + scanner.nextLine();
		}
		returnString = returnString.replaceAll("\\s", "");
		stringSoFar.append(returnString);
		stringSoFar.append("$");
		return returnString;
	}

	//Putting onto the checking stack by going backwards through the string to ensure correct order
	public static void puttingCheckingStack(String inputString){
		char[] characters = inputString.toCharArray();
		for(int i = characters.length - 1; i >= 0; i--){
			//If we are looking if, we have to put it on as a single string and not a character. So append it and then push it on and minusing the length from i.
			if(characters[i] == 'f' && characters[i-1] == 'i'){
				StringBuilder sb = new StringBuilder();
				sb.append(characters[i-1]);
				sb.append(characters[i]);
				i--;
				checkingStack.push(sb.toString());
				continue;
			}

			//If we are looking for print, we do the same as what we do for if
			else if(characters[i] == 't' && characters[i-1] == 'n' && characters[i-2] == 'i' && characters[i-3] == 'r' && characters[i-4] == 'p'){
				StringBuilder sb = new StringBuilder();
				sb.append(characters[i-4]);
				sb.append(characters[i-3]);
				sb.append(characters[i-2]);
				sb.append(characters[i-1]);
				sb.append(characters[i]);
				i = i - 4;
				checkingStack.push(sb.toString());
				continue;
			}
			else{
				checkingStack.push(String.valueOf(characters[i]));
			}


		}
	}

	//Putting onto the input stack
	public static void puttingStack(String inputString){
		char[] characters = inputString.toCharArray();
		for(int i = characters.length - 1; i >= 0; i--){
			//If we are looking if, we have to put it on as a single string and not a character. So append it and then push it on and minusing the length from i.
			if(characters[i] == 'f' && characters[i-1] == 'i'){
				StringBuilder sb = new StringBuilder();
				sb.append(characters[i-1]);
				sb.append(characters[i]);
				i--;
				inputStack.push(sb.toString());
				continue;
			}

			//If we are looking for print, we do the same as what we do for if
			else if(characters[i] == 't' && characters[i-1] == 'n' && characters[i-2] == 'i' && characters[i-3] == 'r' && characters[i-4] == 'p'){
				StringBuilder sb = new StringBuilder();
				sb.append(characters[i-4]);
				sb.append(characters[i-3]);
				sb.append(characters[i-2]);
				sb.append(characters[i-1]);
				sb.append(characters[i]);
				i = i - 4;
				inputStack.push(sb.toString());
				continue;
			}

			//Otherwise just push it on
			else {
				inputStack.push(String.valueOf(characters[i]));
			}
		}
	}

}
