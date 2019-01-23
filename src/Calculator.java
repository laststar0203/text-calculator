import java.text.spi.NumberFormatProvider;
import java.util.ArrayList;
import java.util.EmptyStackException;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Stack;

public class Calculator {

	enum Operator {
		RESULT(0 , '='), SBRACKET(1, '['), BRACE(2, '{'),
		PARENTHESIS(3, '(') , ADD(4, '+'), MINUS(5, '-'), DIVIDE(6, '/'), MULTIPLY(7, '*'); 

		
		private final int priority;
		private final char operator;
		
		final static int[] arr = {'=' , '+', '-', '/', '*', '[', '{', '(' };
		

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
			case '=':
				return Operator.RESULT;
			}
			return null;
		}

		public static boolean isOperator(int text) {
			boolean isOperator = false;
			for (int i : arr)
				if (i == text)
					isOperator = true;
			return isOperator;
		}
		
	}

	String string;
	Stack<String> st_expression;
	Stack<Operator> st_operator;

	public Calculator() {
		this.st_expression = new Stack<>();
		this.st_operator = new Stack<>();
	}

	public int calculation(String expression) {

		makePostfixNotation(expression);
		System.out.println(st_expression);
		System.out.println(st_operator);
		
		return 0;
	}

	private void makePostfixNotation(String expression) {

		StringBuilder sb = new StringBuilder();
		boolean inBracket = false;
		
		try {
			for (int i = 0; i < expression.length(); i++) {
				char c = expression.charAt(i);

				if (c >= '0' && c <= '9' || c == '.')
					sb.append(c);
				else if (Operator.isOperator(c)) {
					st_expression.push(sb.toString());
					sb.delete(0, sb.length());
							
					Operator op = Operator.valueOf(c);
					if(op.getPriority() > 2) { inBracket = true;
						continue;
					}
					if(inBracket) {
						st_operator.push(op);
						inBracket = false;
					}
						
					
					if (!st_operator.isEmpty() && st_operator.peek().getPriority() >= op.getPriority()) {
						st_expression.push(st_operator.pop().getOperator()+"");
					}				
					st_operator.push(op);
					System.out.println(op + " : "+ i);
				}else
					throw new Exception("Invalid character found.");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
				
		}

	public static void main(String[] args) {
		Calculator c = new Calculator();
		c.calculation("12+2+(4+5=");
	}
	
}
