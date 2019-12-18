import static org.junit.jupiter.api.Assertions.*;

import java.util.Collections;
import java.util.Iterator;
import java.util.Random;
import java.util.TreeSet;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

class BST_Tree_Test {

	static Tree tr;
	static TreeSet<Integer> ts = new TreeSet<Integer>();
	static Random r = new Random();

	@BeforeAll
	public static void setup() {
		int rootVal = r.nextInt(25);
	 	tr = new Tree(rootVal);
	 	ts.add(rootVal);
	 	for (int i = 0; i < 26; i++) {
	 		int val = r.nextInt(24);
	 		ts.add(val);
	 		tr.insert(val);
	 	}
	 	
	 	System.out.println("Tree created in setup: ");
	 	tr.forEach(elem->System.out.print(elem+" "));
	 	
	 	System.out.println("\nTreeSet created in setup: ");
	 	ts.forEach(elem->System.out.print(elem+" "));
	 	System.out.println("\n---------------------------------------");
	}		 

	@AfterEach
	void check_invariant() {
		assertTrue(ordered(tr),"The BST property is not preserved");
		System.out.println("Tree invariant maintained");
		System.out.println("\n---------------------------------------");
	}
		
	@Test
	void test_insert() {
		System.out.println("Testing Tree insert ... ");
		
		System.out.println("TreeSet iterator and Comparing elements pair-wise ...");

		Iterator<Integer> treeIterator = tr.iterator();
		Iterator<Integer> setIterator = ts.iterator();
		
		assertTrue(treeIterator.hasNext() && setIterator.hasNext(), "(a)Insertion operation failed");
		while(treeIterator.hasNext() && setIterator.hasNext()) {
			assertTrue(treeIterator.hasNext() && setIterator.hasNext(), "(b)Insertion operation failed");
			assertTrue(treeIterator.next() == setIterator.next(), "(c)Insertion operation failed");
		}
		System.out.println("... Tree insert test passed");
	}
		
	public boolean ordered(Tree tr) {
		return (tr.left == null || (tr.value > tr.left.max().value && ordered(tr.left)))
				&& (tr.right == null || (tr.value < tr.right.min().value && ordered(tr.right)));
	}

}