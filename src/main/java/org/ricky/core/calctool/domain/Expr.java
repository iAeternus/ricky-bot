package org.ricky.core.calctool.domain;

import lombok.Getter;
import org.ricky.common.exception.MyException;

import java.util.*;
import java.util.stream.Collectors;

import static org.ricky.core.common.constants.ErrorMsgConstants.*;
import static org.ricky.common.exception.ErrorCodeEnum.*;

/**
 * @author Ricky
 * @version 1.0
 * @date 2025/3/21
 * @className Expr
 * @desc
 */
public class Expr {

    /**
     * Token集合
     */
    private final List<Token> tokens = new ArrayList<>();

    /**
     * 表达式是否合法
     */
    @Getter
    private final boolean valid;

    /**
     * Token类型枚举
     */
    private enum TokenType {
        NUMBER, OPERATOR, LEFT_PAREN, RIGHT_PAREN, UNARY_OP
    }

    private static class Token {
        final TokenType type;
        final String opValue;
        final double numValue;
        private static final Map<String, OpProperty> OP_MAP = new HashMap<>();

        static {
            OP_MAP.put("+", new OpProperty(2, false));
            OP_MAP.put("-", new OpProperty(2, false));
            OP_MAP.put("*", new OpProperty(3, false));
            OP_MAP.put("/", new OpProperty(3, false));
            OP_MAP.put("%", new OpProperty(3, false));
            OP_MAP.put("^", new OpProperty(4, true));
            OP_MAP.put("u-", new OpProperty(5, true));
        }

        Token(TokenType type, String opValue, double numValue) {
            this.type = type;
            this.opValue = opValue;
            this.numValue = numValue;
        }

        OpProperty getOpProperty() {
            return OP_MAP.get(opValue);
        }

        @Override
        public String toString() {
            return type == TokenType.NUMBER ? String.valueOf(numValue) : opValue;
        }
    }

    /**
     * 操作符属性
     */
    private static class OpProperty {

        /**
         * 优先级
         */
        final int precedence;

        /**
         * 是否右结合
         */
        final boolean rightAssoc;

        OpProperty(int precedence, boolean rightAssoc) {
            this.precedence = precedence;
            this.rightAssoc = rightAssoc;
        }
    }

    public Expr(String expr) {
        try {
            String filtered = expr.replaceAll("\\s+", "");
            tokenize(filtered);
            valid = checkBrackets();
        } catch (Exception e) {
            throw new MyException(TOKENIZATION_ERROR, TOKENIZATION_ERROR_MSG, Map.of("message", e.getMessage()));
        }
    }

    public double eval() {
        if (!valid) throw new MyException(INVALID_EXPRESSION, INVALID_EXPRESSION_MSG);
        return evalPostfix(in2post());
    }

    /**
     * 核心分词方法
     *
     * @param expr 表达式字符串
     */
    private void tokenize(String expr) {
        StringBuilder numStr = new StringBuilder();
        for (int i = 0; i < expr.length(); i++) {
            char c = expr.charAt(i);

            if (Character.isDigit(c) || c == '.') {
                numStr.append(c);
            } else {
                handleNumber(numStr);
                if (isUnaryNegation(c, i, expr)) {
                    tokens.add(new Token(TokenType.UNARY_OP, "u-", 0));
                } else if (c == '(') {
                    tokens.add(new Token(TokenType.LEFT_PAREN, "(", 0));
                } else if (c == ')') {
                    tokens.add(new Token(TokenType.RIGHT_PAREN, ")", 0));
                } else if (isOperator(c)) {
                    tokens.add(new Token(TokenType.OPERATOR, String.valueOf(c), 0));
                } else {
                    throw new MyException(INVALID_CHARACTER, INVALID_CHARACTER_MSG, Map.of("ch", c));
                }
            }
        }
        handleNumber(numStr);
    }

    private void handleNumber(StringBuilder numStr) {
        if (numStr.isEmpty()) return;

        String num = numStr.toString();
        validateNumber(num);
        tokens.add(new Token(TokenType.NUMBER, "", Double.parseDouble(num)));
        numStr.setLength(0);
    }

    private boolean isUnaryNegation(char c, int index, String expr) {
        if (c != '-') return false;
        return index == 0 ||
                expr.charAt(index - 1) == '(' ||
                isOperator(expr.charAt(index - 1));
    }

    private boolean isOperator(char c) {
        return "+-*/%^".indexOf(c) != -1;
    }

    private void validateNumber(String num) {
        if (num.startsWith("-")) num = num.substring(1);
        int dotCount = 0;
        for (int i = 0; i < num.length(); i++) {
            char c = num.charAt(i);
            if (c == '.') {
                if (++dotCount > 1) {
                    throw new MyException(INVALID_NUMBER, INVALID_NUMBER_MSG, Map.of("num", num));
                }
                if (i == 0 || i == num.length() - 1) {
                    throw new MyException(INVALID_NUMBER, INVALID_NUMBER_MSG, Map.of("num", num));
                }
            }
        }
    }

    /**
     * 判断括号是否匹配
     *
     * @return true=匹配 false=不匹配
     */
    private boolean checkBrackets() {
        Deque<Token> stack = new ArrayDeque<>();
        for (Token t : tokens) {
            if (t.type == TokenType.LEFT_PAREN) {
                stack.push(t);
            } else if (t.type == TokenType.RIGHT_PAREN) {
                if (stack.isEmpty() || stack.peek().type != TokenType.LEFT_PAREN) {
                    return false;
                }
                stack.pop();
            }
        }
        return stack.isEmpty();
    }

    /**
     * 中缀表达式转后缀表达式
     */
    private List<Token> in2post() {
        List<Token> output = new ArrayList<>();
        Deque<Token> opStack = new ArrayDeque<>();

        for (Token token : tokens) {
            switch (token.type) {
                case NUMBER:
                    output.add(token);
                    break;
                case LEFT_PAREN:
                    opStack.push(token);
                    break;
                case RIGHT_PAREN:
                    while (!opStack.isEmpty() && opStack.peek().type != TokenType.LEFT_PAREN) {
                        output.add(opStack.pop());
                    }
                    if (opStack.isEmpty()) {
                        throw new MyException(MISMATCHED_PARENTHESES, MISMATCHED_PARENTHESES_MSG);
                    }
                    opStack.pop();
                    break;
                case OPERATOR:
                case UNARY_OP:
                    while (!opStack.isEmpty() &&
                            opStack.peek().type != TokenType.LEFT_PAREN &&
                            shouldPop(token, opStack.peek())) {
                        output.add(opStack.pop());
                    }
                    opStack.push(token);
                    break;
                default:
                    throw new MyException(UNEXPECTED_TOKEN, UNEXPECTED_TOKEN_MSG);
            }
        }

        while (!opStack.isEmpty()) {
            Token t = opStack.pop();
            if (t.type == TokenType.LEFT_PAREN) {
                throw new MyException(MISMATCHED_PARENTHESES, MISMATCHED_PARENTHESES_MSG);
            }
            output.add(t);
        }

        return output;
    }

    private boolean shouldPop(Token current, Token stackTop) {
        OpProperty currProp = current.getOpProperty();
        OpProperty stackProp = stackTop.getOpProperty();

        if (currProp == null || stackProp == null) {
            return false;
        }

        if (currProp.rightAssoc) {
            return currProp.precedence < stackProp.precedence;
        } else {
            return currProp.precedence <= stackProp.precedence;
        }
    }

    /**
     * 计算后缀表达式
     */
    private double evalPostfix(List<Token> postfix) {
        Deque<Double> stack = new ArrayDeque<>();
        for (Token token : postfix) {
            switch (token.type) {
                case NUMBER:
                    stack.push(token.numValue);
                    break;
                case OPERATOR:
                    if (stack.size() < 2) {
                        throw new MyException(INSUFFICIENT_OPERANDS, INSUFFICIENT_OPERANDS_MSG);
                    }
                    double b = stack.pop();
                    double a = stack.pop();
                    stack.push(applyOperator(a, b, token.opValue));
                    break;
                case UNARY_OP:
                    if (stack.isEmpty()) {
                        throw new MyException(INSUFFICIENT_OPERANDS, INSUFFICIENT_OPERANDS_MSG);
                    }
                    stack.push(-stack.pop());
                    break;
                default:
                    throw new MyException(UNEXPECTED_TOKEN, UNEXPECTED_TOKEN_MSG);
            }
        }
        if (stack.size() != 1) {
            throw new MyException(MALFORMED_EXPRESSION, MALFORMED_EXPRESSION_MSG);
        }
        return stack.pop();
    }

    private double applyOperator(double a, double b, String op) {
        return switch (op) {
            case "+" -> a + b;
            case "-" -> a - b;
            case "*" -> a * b;
            case "/" -> {
                if (b == 0) throw new MyException(DIVIDED_BY_ZERO, DIVIDED_BY_ZERO_MSG);
                yield a / b;
            }
            case "%" -> a % b;
            case "^" -> Math.pow(a, b);
            default -> throw new MyException(UNKNOWN_OPERATOR, UNKNOWN_OPERATOR_MSG, Map.of("op", op));
        };
    }

    @Override
    public String toString() {
        return tokens.stream()
                .map(Token::toString)
                .collect(Collectors.joining(" "));
    }

}
