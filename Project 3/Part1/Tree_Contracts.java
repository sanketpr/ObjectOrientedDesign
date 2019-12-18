// Assignment 3 Part 1

// Organization of the File:

// 1. Classes Tree_Test and DupTree_Test -- the tester classes
//
// 2. Classes AbsTree, Tree, and DupTree
//
// 3. Class AbsTree_Iterator

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Stack;


//***************** Contract Checking *********************

interface IContract {
	boolean check();
}

class Contract  {
	static void Invariant(boolean b) throws Exception {
		if (!b) throw new InvariantViolation();
	}
	static void Requires(boolean b) throws Exception {
		if (!b) throw new PreConditionViolation();
	}
	static void Ensures(boolean b) throws Exception {
		if (!b) throw new PostConditionViolation();
	}
}

class PreConditionViolation extends Exception {}

class PostConditionViolation extends Exception {}

class InvariantViolation extends Exception {}

/************************ Tree_Test *********************/
class Tree_Test {

	public static void main(String[] args) throws Exception {
		AbsTree tr = new Tree(100);
		tr.insert(50);
		tr.insert(125);
		tr.insert(150);
		tr.insert(20);
		tr.insert(75);
		tr.insert(20);
		tr.insert(90);
		tr.insert(50);
		tr.insert(125);
		tr.insert(150);
		tr.insert(75);
		tr.insert(90);

		System.out.println("Initial Tree: ");
		print(tr);

		tr.delete(50);
		tr.delete(125);
		tr.delete(150);
		tr.delete(75);
		tr.delete(90);
		tr.delete(20);

		System.out.println("Tree before last delete: ");
		print(tr);

		System.out.println("Attempt to delete last value must throw contract exception: ");
		tr.delete(100);

	}

	public static void print(AbsTree tr) throws Exception {
		AbsTree_Iterator it = new AbsTree_Iterator(tr);
		while (it.hasNext())
			System.out.print(it.next() + " ");
		System.out.println();
	}
}

class DupTree_Test {

	public static void main(String[] args) throws Exception {
		AbsTree tr = new DupTree(100);
		tr.insert(50);
		tr.insert(125);
		tr.insert(150);
		tr.insert(20);
		tr.insert(75);
		tr.insert(20);
		tr.insert(90);
		tr.insert(50);
		tr.insert(125);
		tr.insert(150);
		tr.insert(75);
		tr.insert(90);

		System.out.println("Initial DupTree: ");
		print(tr);

		tr.delete(50);
		tr.delete(125);
		tr.delete(150);
		tr.delete(75);
		tr.delete(90);
		tr.delete(20);
		tr.delete(100);

		System.out.println("DupTree after some delete's: ");
		print(tr);

		tr.delete(50);
		tr.delete(125);
		tr.delete(150);
		tr.delete(75);
		tr.delete(90);

		System.out.println("DupTree before last delete: ");
		print(tr);
		System.out.println("Attempt to delete last value: ");

		tr.delete(20);
	}

	public static void print(AbsTree tr) throws Exception {
		Tree_Test.print(tr);
	}
}

// ***************************************************************//

// @Invariant("ordered()")
abstract class AbsTree {
	public AbsTree(int n) throws Exception {
		value = n;
		left = null;
		right = null;
		Contract.Invariant(ordered());
	};

	// @Ensures("member(n)")
	public boolean insert(int n) throws Exception {
		Contract.Invariant(ordered());
		boolean b;

		if (value == n)
			b = count_duplicates();
		else if (value < n)
			if (right == null) {
				right = add_node(n);
				b = true;
			} else
				b = right.insert(n);
		else if (left == null) {
			left = add_node(n);
			b = true;
		} else
			b = left.insert(n);

		Contract.Ensures(member(n));
		Contract.Invariant(ordered());
		return b;
	}

	boolean ordered() {
		return (left == null || value > left.max().value && left.ordered())
				&& 
			(right == null || value < right.min().value && right.ordered());
	}

	boolean member(int n) {
		boolean rc = right !=null ? right.member(n) : false;
		boolean lc = left !=null ? left.member(n) : false;
		return  (value == n) || 
			(value < n && rc) ||
			(value > n && lc);
	}

	public AbsTree_Iterator iterator() throws Exception {
		return new AbsTree_Iterator(this) ;
	}

	// The pre-condition should apply to trees and duptrees and should
        // that the last value cannot be deleted from a tree or duptree
	// @Requires("__")

	public boolean delete(int n) throws Exception { // cannot delete last value in tree

		Contract.Invariant(ordered());

		// For any given last node in a tree, its count value should be 1
		// And since it is the only node its value should be both min() and max() in the tree.
		// If that is the case we don't want to delete the node and rather throw an exception
		Contract.Requires(!(get_count() == 1 && min().value == n && max().value == n));

		AbsTree t = find(n);
		boolean result = false;

		if (t == null) { // n is not in the tree
			result = false;
		}

		else if (t.get_count() > 1) {
			t.set_count(t.get_count() - 1);
			result = true;
		}

		else if (t.left == null && t.right == null) { // n is a leaf value
			if (t != this) {
				case1(t, this);
				result = true;
			} else
				result = false;
		}

		else if (t.left == null || t.right == null) { // t has one subtree only
			if (t != this) { // check whether t is the root of the tree
				case2(t, this);
				result = true;
			} else {
				if (t.right == null)
					case3(t, "left");
				else
					case3(t, "right");
				result = true;
			}
		}
		// t has two subtrees; go with smallest in right subtree of t
		else {
			case3(t, "right");
			result = true;
		}

		Contract.Invariant(ordered());
		return result;
	}

	protected void case1(AbsTree t, AbsTree root) { // remove the leaf
		if (t.value > root.value)
			if (root.right == t)
				root.right = null;
			else
				case1(t, root.right);
		else if (root.left == t)
			root.left = null;
		else
			case1(t, root.left);
	}

	protected void case2(AbsTree t, AbsTree root) { // remove internal node
		if (t.value > root.value)
			if (root.right == t)
				if (t.right == null)
					root.right = t.left;
				else
					root.right = t.right;
			else
				case2(t, root.right);
		else if (root.left == t)
			if (t.right == null)
				root.left = t.left;
			else
				root.left = t.right;
		else
			case2(t, root.left);
	}

	protected void case3(AbsTree t, String side) { // replace t.value and t.count
		if (side == "right") {
			AbsTree min_right_t = t.right.min();
			if (min_right_t.left == null && min_right_t.right == null)
				case1(min_right_t, this); // min_right_t is a leaf node
			else
				case2(min_right_t, this); // min_right_t is a non-leaf node
			t.value = min_right_t.value;
			t.set_count(min_right_t.get_count());
		} else {
			AbsTree max_left_t = t.left.max();
			if (max_left_t.left == null && max_left_t.right == null)
				case1(max_left_t, this); // max_left_t is a leaf node
			else
				case2(max_left_t, this); // max_left_t is a non-leaf node
			t.value = max_left_t.value;
			t.set_count(max_left_t.get_count());
		}
	}

	// @Requires(true)
	// @Ensures("____")
	protected AbsTree find(int n) throws Exception {

		AbsTree t, result;

		Contract.Invariant(ordered());

		if (value == n)
			t = this;
		else if (value < n)
			if (right == null)
				t = null;
			else
				t = right.find(n);
		else if (left == null)
			t = null;
		else
			t = left.find(n);

		result = t;
		Contract.Ensures(result == null || result.value == n);
		Contract.Invariant(ordered());
		return result;
	}

	public AbsTree min() {
		if (left != null)
			return left.min();
		else
			return this;
	}

	public AbsTree max() {
		if (right != null)
			return right.max();
		else
			return this;
	}

	protected abstract AbsTree add_node(int n) throws Exception;

	protected abstract boolean count_duplicates();

	protected abstract int get_count();

	protected abstract void set_count(int v);

	protected int value;
	protected AbsTree left;
	protected AbsTree right;
}

class Tree extends AbsTree {
	public Tree(int n) throws Exception {
		super(n);
	}

	// @Ensures("_____")
	public boolean delete(int n) throws Exception {
		boolean b = super.delete(n);
		Contract.Ensures(b && !member(n));
		return b;
	}

	protected AbsTree add_node(int n) throws Exception {
		return new Tree(n);
	}

	protected boolean count_duplicates() {
		return false;
	}

	protected int get_count() {
		return 1;
	}

	protected void set_count(int v) {
	}
}

class DupTree extends AbsTree {
	public DupTree(int n) throws Exception {
		super(n);
		count = 1;
		Contract.Invariant(ordered());
	};

	// @Ensures("___")
	public boolean insert(int n) throws Exception {

		Contract.Invariant(ordered());

		AbsTree t = find(n);

		if (t != null)
			old_n_count = t.get_count();
		else
			old_n_count = 0;

		super.insert(n);

		n_count = find(n).get_count();

		Contract.Ensures(n_count == old_n_count + 1 && member(n));
		Contract.Invariant(ordered());
		return true;
	}

	// @Ensures("___")
	public boolean delete(int n) throws Exception {

		Contract.Invariant(ordered());

		if (member(n))
			old_n_count = find(n).get_count();
		else
			old_n_count = 0;

		boolean b = super.delete(n);

		if (member(n))
			n_count = find(n).get_count();
		else
			n_count = 0;

		Contract.Ensures(((old_n_count > 0 && n_count == old_n_count - 1) || (old_n_count == 0 && n_count == 0)) && b);
		Contract.Invariant(ordered());
		return b;
	}

	protected AbsTree add_node(int n) throws Exception {
		return new DupTree(n);
	}

	protected boolean count_duplicates() {
		count++;
		return true;
	}

	protected int get_count() {
		return count;
	}

	protected void set_count(int v) {
		count = v;
	}

	protected int count;
	protected int n_count, old_n_count;
}

//**************************************************************************//

class AbsTree_Iterator {

	// @Requires("____")
	// @Ensures("_____")

	public AbsTree_Iterator(AbsTree root) throws Exception {

		Contract.Requires(root.ordered());

		stack_left_spine(root);

		Contract.Ensures(stack.peek() == root.min());
	}

	public boolean hasNext() {
		return !stack.isEmpty() || count > 0;
	}

	// @Requires("____")
	// @Ensures("____")

	public int next() throws Exception {
		int old_value = value;

		Contract.Requires(hasNext());

		if (count == 0) {
			AbsTree node = stack.pop();
			value = node.value;
			count = node.get_count();
			stack_left_spine(node.right);
		}
		count--;
		Contract.Ensures(old_value <= value);
		return value;
	}

	// @Requires("____")
	// @Ensures("_____")

	private void stack_left_spine(AbsTree node) throws Exception {

		Contract.Requires(true);

		int old_value = value;
		AbsTree old_node = node;

		if (node != null) {
			stack.push(node);
			while (node.left != null) {
				stack.push(node.left);
				node = node.left;
			}
		}

		AbsTree node2 = node; // use node2 instead of node

		Contract.Ensures(node2 == null || stack.peek().value == node2.min().value);
	}

	private Stack<AbsTree> stack = new Stack<AbsTree>();
	private int value;
	private int count = 0;
}