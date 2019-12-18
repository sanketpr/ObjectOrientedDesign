// Assignment 1 Part 1: Starter Code

class Tree_Test {

	public static void main(String[] args) {
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

		tr.delete(20);
		tr.delete(20);
		tr.delete(20);
		tr.delete(150);
		tr.delete(100);
		tr.delete(150);
		tr.delete(125);
		tr.delete(125);
		tr.delete(50);
		tr.delete(50);
		tr.delete(50);
		tr.delete(75);
		tr.delete(90);
		tr.delete(75);
		tr.delete(90);
	}
}

class DupTree_Test {

	public static void main(String[] args) {
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

		tr.delete(20);
		tr.delete(20);
		tr.delete(20);
		tr.delete(150);
		tr.delete(100);
		tr.delete(150);
		tr.delete(125);
		tr.delete(125);
		tr.delete(50);
		tr.delete(50);
		tr.delete(50);
		tr.delete(75);
		tr.delete(90);
		tr.delete(75);
		tr.delete(90);
	}
}

abstract class AbsTree {
	public AbsTree(int n) {
		value = n;
		left = null;
		right = null;
	}

	public void insert(int n) {
		if (value == n)
			count_duplicates();
		else if (value < n)
			if (right == null) {
				right = add_node(n);
			} else
				right.insert(n);
		else if (left == null) {
			left = add_node(n);
		} else
			left.insert(n);
	}

	public void delete(int n) {  
		AbsTree t = find(n);

		if (t == null) { // n is not in the tree
			System.out.println("Unable to delete " + n + " -- not in the tree!");
			return;
		}

		int c = t.get_count();
		if (c > 1) {
			t.set_count(c-1);
			return;
		}

		if (t.left == null && t.right == null) { // n is a leaf value
			if (t != this)
				case1(t);
			else
				System.out.println("Unable to delete " + n + " -- tree will become empty!");
			return;
		}
		if (t.left == null || t.right == null) { // t has one subtree only
			if (t != this) { // check whether t is the root of the tree
				case2(t);
				return;
			} else {
				if (t.right == null)
					case3L(t);
				else
					case3R(t);
				return;
			}
		}
		// t has two subtrees; go with smallest in right subtree of t
		case3R(t);
	}

	protected void case1(AbsTree t) { // remove the leaf
		AbsTree parentNode = t.parent;

		if (t.value > parentNode.value) {
			parentNode.right = null;
		} else {
			parentNode.left = null;
		}
		t.parent = null;
	}

	protected void case2(AbsTree t) { // remove internal node
		AbsTree parentNode = t.parent;
		AbsTree childNode;
		if (t.left == null) {
			childNode = t.right;
			t.right = null;
		} else {
			childNode = t.left;
			t.left = null;
		}

		if (t.value > parentNode.value) {
			parentNode.right = null;
			parentNode.right = childNode;
		} else {
			parentNode.left = null;
			parentNode.left = childNode;
		}

		childNode.parent = parentNode;
		t.parent = null;
	}

	protected void case3L(AbsTree t) { // replace t.value and t.count
		AbsTree maxNode = t.left.max();

		t.value = maxNode.value;
		t.set_count(maxNode.get_count());
		AbsTree parentNode = maxNode.parent;
		if (parentNode.left != null && parentNode.left.value == maxNode.value) {
			maxNode.parent.left = null;
		} else {
			maxNode.parent.right = null;
		}
		maxNode.parent = null;
	}

	protected void case3R(AbsTree t) { // replace t.value
		AbsTree minNode = t.left != null ? t.right.min() : t.min();
		
		t.value = minNode.value;
		t.set_count(minNode.get_count());
		AbsTree parentNode = minNode.parent;
		if (parentNode.left != null && parentNode.left.value == minNode.value) {
			minNode.parent.left = null;
		} else {
			minNode.parent.right = null;
		}
		minNode.parent = null;
	}

	public AbsTree find(int n) {
		if (value == n) {
			return this;
		} else if (value < n) {
			return right == null ? null : right.find(n);
		} else if (value > n) {
			return left == null ? null : left.find(n);
		} 

		return null;
	}

	public AbsTree min() {
		if (left == null) {
			return this;
		} else {
			return left.min();
		}
	}

	public AbsTree max() {
		if (right == null) {
			return this;
		} else {
			return right.max();
		}
	}

	protected int value;
	protected AbsTree left;
	protected AbsTree right;
	protected AbsTree parent;

	protected abstract AbsTree add_node(int n);
	protected abstract void count_duplicates();
	protected abstract int get_count();
	protected abstract void set_count(int v);
}

class Tree extends AbsTree {
	public Tree(int n) {
		super(n);
	}

	protected AbsTree add_node(int n) {
		AbsTree newNode = new Tree(n);
		newNode.parent = this;
		return newNode;
	}

	protected void count_duplicates() {
		;
	}

	protected int get_count() {
		return 1;
	}

	protected void set_count(int v) {
		// no-op
	}
}

class DupTree extends AbsTree {
	public DupTree(int n) {
		super(n);
		count = 1;
	};

	protected AbsTree add_node(int n) {
		AbsTree newNode = new Tree(n);
		newNode.parent = this;
		return newNode;
	}

	protected void count_duplicates() {
		count++;
	}

	protected int get_count() {
		return count;
	}

	protected void set_count(int v) {
		this.count = v;
	}

	protected int count;
}
