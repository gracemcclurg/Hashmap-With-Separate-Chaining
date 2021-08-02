package hashMapChaining;

import java.util.*;
import java.util.ArrayList;

class Entry<K, V> {
   K key;
   V value;

   Entry<K, V> next;

   Entry<K, V> before;
   Entry<K, V> after;

   public Entry(K key, V value) {
      this.key = key;
      this.value = value;
   }
}

public class HashMap<K, V> {
   private int numOfEntries;
   private int numBuckets;
   private ArrayList<Entry<K, V>> entries;
   private Entry<K, V> front, tail;

   public HashMap() {
      // create an array of size 8
      entries = new ArrayList<>();
      numBuckets = 8;
      numOfEntries = 0;

      // Create empty chains 
      for (int i = 0; i < numBuckets; i++)
         entries.add(null);

   }

   //get bucket index based on key's hashcode
   public int getIndex(K key) {
      int hashCo = key.hashCode();
      return Math.abs(hashCo % numBuckets);
   }

   //when given value, find corresponding key
   public Object findKey(Object o) {
      //check all buckets
      for (int i = 0; i < numBuckets; i++) {
         Entry<K, V> head = entries.get(i);
         //check all values that exist in the bucket for matches
         while (head != null) {
            if (o == head.value)
               return head.key;
            head = head.next;
         }
      }
      return null;
   }

   //when given key, find corresponding value
   public Object findValue(Object o) {
      //find bucket index
      int hashCo = o.hashCode();
      int index = hashCo % numBuckets;
      //go to corresponding bucket
      Entry<K, V> head = entries.get(index);
      //check all values that exist in the bucket for matches
      while (head != null) {
         if (head.key.equals(o))
            return head.value;
         head = head.next;
      }
      return null;
   }

   public Object get(Object o) {
      if (o instanceof Integer) {
         return findKey(o);
      } else {
         return findValue(o);
      }
   }

   //insert entry
   public V put(K key, V value) {
      int bucketIndex = getIndex(key);
      //get corresponding bucket
      Entry<K, V> start = entries.get(bucketIndex);
      //check if key is already present
      while (start != null) {
         if (start.key.equals(key)) {
            //if key is there, replace value
            V oldVal = start.value;
            start.value = value;
            return oldVal;
         }
         start = start.next;
      }
      //update position in bucket
      start = entries.get(bucketIndex);

      Entry<K, V> newNode = new Entry<K, V>(key, value);
      //update connections between buckets
      //if first entry
      if (start == null && front == null) {
         tail = newNode;
         front = newNode;
      //update before and after connections if entries in other buckets exist
      } else {
         newNode.before = tail;
         tail.after = newNode;
         tail = newNode;
      }
      //start moves up the chain and newNode is new start
      newNode.next = start;
      //new head
      entries.set(bucketIndex, newNode);
      numOfEntries++;

      //if load factor >=0.8, double size
      if (numOfEntries / numBuckets >= 0.8) {
         doubleSize();
      }

      return value;

   }

   //double the size once load factor>0.8
   public void doubleSize() {
      ArrayList<Entry<K, V>> temp = entries;
      entries = new ArrayList<>();
      numBuckets = 2 * numBuckets;
      /*numOfEntries is set back to 0 so that
      when the entries are added back in (and thus the count++),
      there is no double counting*/
      numOfEntries = 0;
      for (int i = 0; i < numBuckets; i++)
         entries.add(null);
      //copy over each bucket
      for (Entry<K, V> head : temp) {
         while (head != null) {
            put(head.key, head.value);
            head = head.next;
         }

      }

   }

   //remove the entry corresponding to the given key
   public V remove(K key) {
      //find corresponding bucket key
      int index = getIndex(key);
      //we want to save previous so we can update the next link
      Entry<K, V> prev = null;
      Entry<K, V> head = entries.get(index);
      //go through bucket until key is found
      while (head != null) {
         if (head.key.equals(key))
            break;
         prev = head;
         head = head.next;
      }
      //entry not found
      if (head == null)
         return null;

      //if key removed was not head
      if (prev != null) {
         prev.next = head.next;
         //if head, make new head the proceeding entry in the bucket
      } else {
         entries.set(index, head.next);
      }
      numOfEntries--;

      return head.value;

   }

   @SuppressWarnings("unchecked")
   //return all keys in map
   public Set<K> keySet() {
      K arr[] = (K[]) new Object[numOfEntries];
      int arrIndex = 0;
      //iterate through all buckets
      for (int i = 0; i < numBuckets; i++) {
         Entry<K, V> head = entries.get(i);
         //iterate through all entries in bucket
         while (head != null) {
            arr[arrIndex] = head.key;
            head = head.next;
            arrIndex++;
         }
      }
      //convert array to set
      Set<K> set = new HashSet<>(Arrays.asList(arr));
      return (set);
   }

   @SuppressWarnings("unchecked")
   //return all entries
   public Set<Entry<K, V>> entrySet() {
      Entry<K, V> arr[] = (Entry<K, V>[]) new Entry[numOfEntries];
      int arrIndex = 0;
      //iterate through all buckets
      for (int i = 0; i < numBuckets; i++) {
         Entry<K, V> head = entries.get(i);
         //iterate through all entries in bucket
         while (head != null) {
            arr[arrIndex] = head;
            head = head.next;
            arrIndex++;
         }
      }
      //convert array to set
      Set<Entry<K, V>> set = new HashSet<>(Arrays.asList(arr));
      return (set);
   }

   public String entrySetToString(Set<Entry<K, V>> entrySet) {
      String setStr = "|";
      //iterate through set
      for (Entry<K, V> entry : entrySet) {
         setStr = setStr + (String) entry.key + "=" + entry.value + "|";
      }
      return setStr;

   }

   //return how many entries exist
   public int size() {
      return numOfEntries;
   }

   // create the set based on the backing of the list
   private final class KeySet extends AbstractSet<K> {
      public Iterator<K> iterator() {
         return new Iterator<K>() {
            Entry<K, V> current = front;

            public boolean hasNext() {
               return current != null;
            }

            public K next() {
               if (current != null) {
                  return current.key;
               } else {
                  // throw an excpetion
                  throw new NoSuchElementException();
               }
            }

            public void remove() {
               if (current == null) {
                  throw new IllegalStateException("no current value");
               } else {
                  //erase connections to entry
                  current.before.after = current.after;
                  current.after.before = current.before;
               }

            }
         };
      }

      public int size() {
         return numOfEntries;
      }
   }
}
