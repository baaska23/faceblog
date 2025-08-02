package com.example.roadmapMyBlog.dsa;

import java.util.ArrayList;
import java.util.List;

public class Trie {
    private final TrieNode root = new TrieNode();
    
    public void insert(String word, int articleId) {
        TrieNode node = root;
        for (char ch : word.toLowerCase().toCharArray()) {
            node = node.children.computeIfAbsent(ch, c -> new TrieNode());
        }
        node.isEndOfWord = true;
        node.articleId = articleId;
    }
    
    public List<Integer> searchPrefix(String prefix) {
        TrieNode node = root;
        for (char ch : prefix.toLowerCase().toCharArray()) {
            node = node.children.get(ch);
            if (node == null) return new ArrayList<>();
        }
        List<Integer> result = new ArrayList<>();
        collect(node, result);
        return result;
    }
    
    private void collect(TrieNode node, List<Integer> result) {
        if (node.isEndOfWord) result.add(node.articleId);
        for (TrieNode child : node.children.values()) {
            collect(child, result);
        }
    }
}
