package hashMapChaining;

import hashMapChaining.HashMap;


public class HashMapTest {
   public static void main(String[] args) {
      HashMap<String, Integer>map = new HashMap<>(); 
      
        map.put("Jenny",10); 
        map.put("Annie",2); 
        map.put("Lizzie",4);
        map.put("Sally",6);
        //update value of Jenny
        map.put("Jenny",13);
         map.put("Katzumi",7);
         map.put("Ian",9);
        
        System.out.println(map.keySet());
        //expect 6
        System.out.println(map.size()); 
        //expect 6
         System.out.println(map.remove("Jenny")); 
         //expect 5
         System.out.println(map.size());
         //expect Lizzie
         System.out.println(map.get((Object)4)); 
         //expect 2
         System.out.println(map.get((Object)"Annie")); 
        System.out.println(map.keySet()); 
        System.out.println(map.entrySetToString(map.entrySet())); 
       
   }
}
