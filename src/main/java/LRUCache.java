/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author salim
 */

/*
Design and implement a data structure for Least Recently Used (LRU) cache. It should support the following operations: get and put.

get(key) - Get the value (will always be positive) of the key if the key exists in the cache, otherwise return -1.
put(key, value) - Set or insert the value if the key is not already present. When the cache reached its capacity, it should invalidate the least recently used item before inserting a new item.

The cache is initialized with a positive capacity.

Follow up:
Could you do both operations in O(1) time complexity?

*/


// Logic -> HashMap ->  key: Number Value: Node of a doubly linkedlist

import java.util.*;


class Node{

    Node next;
    Node prev;
    int key;
    int val;
    
    Node(int key, int val){
        
     this.key = key;
     this.val = val;
         
    }
    
}

public class LRUCache {
 
    Map<Integer, Node> map;
    int capacity =  0;
    Node dummyHead;
    Node dummyTail;
    int numOfItems = 0;
    
    public LRUCache(int capacity){
    
        this.capacity = capacity;
        map = new HashMap<>();
        dummyHead = new Node(0, 0);
        dummyTail = new Node(0, 0);
        dummyHead.next = dummyTail;
        dummyTail.prev = dummyHead;
    }
    
    
    public int get(int key){
    
        if(map.containsKey(key)){
            
            Node node = map.get(key);
            shiftToHead(node, true);
            return node.val;
        }
        
        return -1;
    }
    
    public void put(int key, int value){
    
        if(map.containsKey(key)){
            
            // existing key - overwriting
            Node node = map.get(key);
            node.key = key;
            node.val = value;
            shiftToHead(node, true); // move this existing node to the head
                    
        }
        
        else{
             
            // new node completely
            
            if(numOfItems+1 > capacity){  // adding this new node reaches capacity
                
                Node tail_node_to_removed = dummyTail.prev; 
                map.remove(tail_node_to_removed.key); // remove the tail Node from the map
                changeTailNode(dummyTail.prev);
            }
            
            Node new_node = new Node(key, value);
            map.put(key, new_node); // add this newly created node to the hashmap
            shiftToHead(new_node, false);
            numOfItems++; // added a new node, so increase the count
            
        }
    }
    
    public void shiftToHead(Node currNode, boolean isExistingNode){
    
        // 1. if it is an existing node
        //    a) check for the existing node is the tail node 
        //          - If yes, change the tail node, 
        //          - If no, then break the link of the current node.
        //    b) shift the existing node to the head
        // 2. If not an existing node ie a new node, just shiift(add to head)
        
        
        if(isExistingNode){
            
            if(currNode == dummyTail.prev) // if existing node is tail node, then change the tail node
                changeTailNode(currNode);
            else{   // if not tail node, break the links of the surrounding current node
                
                Node prevNode = currNode.prev;
                currNode.next.prev = prevNode;
                prevNode.next = currNode.next;
                    
            }
                
        }
        
        // shift node to head. (this will be common for shifting both new node/ existing node)
        
        currNode.next = dummyHead.next;
        dummyHead.next.prev = currNode;
        dummyHead.next = currNode;
        currNode.prev = dummyHead;
        
    }
    
    
    public void changeTailNode(Node tailNode){
    
        
        // A -> tailNode -> dummyTail becomes A(tailNode) -> dummyTail
        
        dummyTail.prev = tailNode.prev;
        tailNode.prev.next = dummyTail;
        
    }
    
    public static void main(String[] args){
        
        LRUCache obj = new LRUCache(1);
        obj.put(2, 1);
        System.out.println("get value of hash key: 2: " + obj.get(2) + " result: " + (obj.get(2) == 1));
        obj.put(3, 2);
        System.out.println("get value of hash key: 2: " + obj.get(2) + " result: " + (obj.get(2) == -1));
        System.out.println("get value of hash key: 3: " + obj.get(3) + " result: " + (obj.get(3) == 2));
    }
}
