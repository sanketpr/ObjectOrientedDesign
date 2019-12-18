import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

class BST_DupTree_Test {

	static DupTree dtr;
	static List<Integer> al = new ArrayList<Integer>();
	static Random r = new Random();

	@BeforeAll
	public static void setup() {
		int rootVal = r.nextInt(25);
	 	dtr = new DupTree(rootVal);
	 	al.add(rootVal);
	 	for (int i = 0; i < 26; i++) {
	 		int val = r.nextInt(25);
	 		al.add(val);
	 		dtr.insert(val);
	 	}
	 	Collections.sort(al);
	 	
	 	System.out.println("DupTree created in setup: ");
	 	dtr.forEach(elem->System.out.print(elem+" "));
	 	
	 	System.out.println("\nSorted ArrayList created in setup: ");
	 	al.forEach(elem->System.out.print(elem+" "));
	 	
	 	System.out.println("\n---------------------------------------");
	}

	@AfterEach
	void check_invariant() {
		assertTrue(ordered(dtr),"The BST property is not preserved");
		System.out.println("DupTree invariant maintained");
		System.out.println("\n---------------------------------------");
	}
	
	@Test
	void test_insert() {
		System.out.println("Testing DupTree insert ... ");
		
		System.out.println("Creating ArrayList iterator and Comparing elements pair-wise ...");
		Iterator<Integer> treeIterator = dtr.iterator();
		Iterator<Integer> listIterator = al.iterator();
		
		assertTrue(treeIterator.hasNext() && listIterator.hasNext(), "(a)Insertion operation failed");
		while(treeIterator.hasNext() && listIterator.hasNext()) {
			assertTrue(treeIterator.hasNext() && listIterator.hasNext(), "(b)Insertion operation failed");
			assertTrue(treeIterator.next() == listIterator.next(), "(c)Insertion operation failed");
		}
		System.out.println("... DupTree insert test passed");
	}
	
	@Test
	void test_delete() {
		int r_int = r.nextInt(24);
		dtr.insert(r_int);
		int count = get_count(dtr, r_int);
		
		System.out.println("Testing DupTree delete: inserted value = "+r_int+" with count = "+count);

		dtr.delete(r_int);
		int new_count;
		if (count > 1) {
			new_count = get_count(dtr, r_int);
			assertEquals(count-1, new_count, "The delete operation didn't work. Count mismatch");
		} else {
			new_count = 0;
			Tree t = dtr.find(r_int);
			assertNull(t);
			assertEquals(count-1, new_count, "The delete operation didn't work. Count mismatch");
		}
		System.out.println("After DupTree delete: value = "+r_int+" with count = "+new_count);
		System.out.println("DupTree delete test passed");
	}		

	public int get_count(DupTree tr, int v) {
		Tree t = tr.find(v);
		return t != null ? t.get_count() : 0;
	}

	public boolean ordered(Tree tr) {
		return (tr.left == null || (tr.value > tr.left.max().value && ordered(tr.left)))
				&& (tr.right == null || (tr.value < tr.right.min().value && ordered(tr.right)));
	}
}