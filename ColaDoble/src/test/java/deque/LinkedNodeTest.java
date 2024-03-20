package deque;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class LinkedNodeTest {

    @Test
    public void LinkedNode_NoSiblings_GetPreviousNextReturnNull(){
        String item = "FirstNode";

        LinkedNode<String> leaf = new LinkedNode<>(item,null,null);

        assertNull(leaf.getPrevious());
        assertNull(leaf.getNext());
    }

    @Test
    public void GetItem_StringNode_ReturnsStringItem(){
        String item = "FirstNode";
        LinkedNode<String> node = new LinkedNode<>(item,null,null);

        String nodeItem = node.getItem();

        assertEquals(item,nodeItem);
    }
}
