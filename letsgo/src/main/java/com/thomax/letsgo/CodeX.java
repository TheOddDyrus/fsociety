package com.thomax.letsgo;

import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Stack;
import java.util.TreeMap;

/**
 * Good luck have fun!!
 *
 * @author thomax
 */
public class CodeX {

	public static void main(String[] args) {

	}

}


class MinStack {

	private final Stack<Integer> stack;

	private final TreeMap<Integer, Integer> minMap;

	/** initialize your data structure here. */
	public MinStack() {
		stack = new Stack<>();
		minMap = new TreeMap<>();
	}

	public void push(int x) {
		stack.push(x);

		Integer total = minMap.get(x);
		if (total == null) {
			total = 1;
		} else {
			total++;
		}
		minMap.put(x, total);
	}

	public void pop() {
		Integer pop = stack.pop();

		Integer total = minMap.get(pop);
		if (total == 1) {
			minMap.remove(pop);
		} else {
			minMap.put(pop, --total);
		}
	}

	public int top() {
		return stack.peek();
	}

	public int min() {
		return minMap.firstKey();
	}
}

/**
 * Your MinStack object will be instantiated and called as such:
 * MinStack obj = new MinStack();
 * obj.push(x);
 * obj.pop();
 * int param_3 = obj.top();
 * int param_4 = obj.min();
 */