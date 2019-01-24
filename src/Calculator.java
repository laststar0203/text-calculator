import java.text.spi.NumberFormatProvider;
import java.util.ArrayList;
import java.util.EmptyStackException;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Stack;
import java.util.Vector;

public class Calculator {

	public static void main(String[] args) {
		Calculator c = new Calculator();
		int value = c.calculation("{12+2-(4+5)}+2");
		System.out.println(value);
	}

	enum Operator {
		SBRACKET(1, '['), BRACE(2, '{'), PARENTHESIS(3, '('), ADD(4, '+'), MINUS(5, '-'), DIVIDE(6, '/'),
		MULTIPLY(7, '*');

		private final int priority;
		private final char operator;

		Operator(int priority, char operator) {
			this.priority = priority;
			this.operator = operator;
		}

		public int getPriority() {
			return priority;
		}

		public char getOperator() {
			return operator;
		}

		public static char getOppositionBracket(char text) {
			switch (text) {
			case '[':
				return ']';
			case '{':
				return '}';
			case '(':
				return ')';
			}
			return (Character) null;
		}

		public static Operator valueOf(char c) {
			switch (c) {
			case '+':
				return Operator.ADD;
			case '-':
				return Operator.MINUS;
			case '/':
				return Operator.DIVIDE;
			case '*':
				return Operator.MULTIPLY;
			case '[':
				return Operator.SBRACKET;
			case '{':
				return Operator.BRACE;
			case '(':
				return Operator.PARENTHESIS;
			}
			return null;
		}

		public static boolean isOperator(int text) {
			boolean isOperator = false;
			for (Operator i : Operator.values())
				if ((int) i.operator == text)
					isOperator = true;
			return isOperator;
		}

	}

	public Calculator() {

	}

	public int calculation(String expression) {
		int result;

		try {
			result = interpretExperssion(makePostfixNotation(expression));
		} catch (Exception e) {
			e.printStackTrace();
			result = 0;
		}

		return result;

	}

	private String makePostfixNotation(String expression) throws Exception {
		StringBuilder numberSb = new StringBuilder();
		StringBuilder expSb = new StringBuilder();

		Stack<Operator> st_operator = new Stack<>();

		char inBracket = 0;

		for (int i = 0; i < expression.length(); i++) {
			char c = expression.charAt(i);

			if (c >= '0' && c <= '9' || c == '.')
				numberSb.append(c);
			else if (Operator.isOperator(c)) {

				Operator op = Operator.valueOf(c);
				if (op.priority < 4) {

					inBracket = op.getOperator();
					st_operator.push(op);
				} else {
					expSb.append(numberSb.toString() + " ");

					numberSb.delete(0, numberSb.length());
					if (!st_operator.isEmpty() && st_operator.peek().priority >= op.priority) {
						expSb.append(Character.toString(st_operator.pop().getOperator()) + " ");
						st_operator.push(op);
					} else
						st_operator.push(op);
				}
			} else if (inBracket != 0 && c == Operator.getOppositionBracket(inBracket)) {
				expSb.append(numberSb.toString() + " ");
				numberSb.delete(0, numberSb.length());
				while (st_operator.peek().getOperator() != inBracket) {

					expSb.append(Character.toString(st_operator.pop().getOperator()) + " ");
				}
				inBracket = 0;
				st_operator.pop(); // exit bracket
			} else
				throw new Exception("Invalid character found. count :" + i);

		}
		while (!st_operator.isEmpty())
			expSb.append(Character.toString(st_operator.pop().getOperator()) + " ");

		return expSb.toString();
	}

	private int interpretExperssion(String expression) throws Exception {

		Stack<Integer> st_value = new Stack<>();
		Stack<String> st_expression = new Stack<>();

		String[] textToken = expression.split(" ");

		for (int i = textToken.length - 1; i >= 0; i--)
			st_expression.push(textToken[i]);

		while (!st_expression.isEmpty()) {
			String token = st_expression.pop();

			try {
				int number = Integer.parseInt(token);
				st_value.push(number);
			} catch (NumberFormatException nfe) {

				char operator = token.toCharArray()[0];

				int secondNum = st_value.pop();
				int firstNum = st_value.pop();

				st_value.push(operationCalculation(firstNum, operator, secondNum));

			}

		}

		int result = st_value.pop();
		if (!st_value.isEmpty())
			throw new Exception("Invalid expression");

		return result;

	}

	private int operationCalculation(int firstNum, char operator, int secondNum) {

		switch (operator) {
		case '+':
			return firstNum + secondNum;
		case '-':
			return firstNum - secondNum;
		case '*':
			return firstNum * secondNum;
		case '/':
			return firstNum / secondNum;
		}

		return 0;
	}

}
