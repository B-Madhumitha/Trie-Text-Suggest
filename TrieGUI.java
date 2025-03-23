import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.List;
import java.util.ArrayList;

class TNode {
    boolean isEOW = false;
    TNode[] children = new TNode[26];
}

class Trie {
    TNode root = new TNode();
    
    void insertWord(String word) {
        TNode temp = root;
        for (char ch : word.toCharArray()) {
            int idx = ch - 'a';
            if (temp.children[idx] == null) {
                temp.children[idx] = new TNode();
            }
            temp = temp.children[idx];
        }
        temp.isEOW = true;
    }
    
    boolean hasWord(String word) {
        TNode temp = root;
        for (char ch : word.toCharArray()) {
            int idx = ch - 'a';
            if (temp.children[idx] == null) return false;
            temp = temp.children[idx];
        }
        return temp.isEOW;
    }
    
    List<String> getAllWordsWithPrefix(String prefix) {
        TNode temp = root;
        List<String> results = new ArrayList<>();
        for (char ch : prefix.toCharArray()) {
            int idx = ch - 'a';
            if (temp.children[idx] == null) return results;
            temp = temp.children[idx];
        }
        findWords(temp, new StringBuilder(prefix), results);
        return results;
    }
    
    private void findWords(TNode node, StringBuilder path, List<String> results) {
        if (node.isEOW) results.add(path.toString());
        for (int i = 0; i < 26; i++) {
            if (node.children[i] != null) {
                path.append((char) (i + 'a'));
                findWords(node.children[i], path, results);
                path.deleteCharAt(path.length() - 1);
            }
        }
    }
}

public class TrieGUI extends JFrame implements ActionListener {
    Trie trie = new Trie();
    JTextField inputField;
    JTextArea outputArea;
    JButton searchButton, addButton;

    public TrieGUI() {
        setTitle("Trie Auto-Completion System");
        setSize(400, 300);
        setLayout(new FlowLayout());
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        inputField = new JTextField(20);
        outputArea = new JTextArea(10, 30);
        outputArea.setEditable(false);
        searchButton = new JButton("Search");
        addButton = new JButton("Add Word");
        
        searchButton.addActionListener(this);
        addButton.addActionListener(this);
        
        add(new JLabel("Enter prefix/word:"));
        add(inputField);
        add(searchButton);
        add(addButton);
        add(new JScrollPane(outputArea));
        
        // Preload some words
        for (String word : new String[]{"apple", "banana", "mango", "nation", "national", "prime", "pretty"}) {
            trie.insertWord(word);
        }
    }

    public void actionPerformed(ActionEvent e) {
        String text = inputField.getText().toLowerCase();
        outputArea.setText("");
        if (e.getSource() == addButton) {
            trie.insertWord(text);
            outputArea.append("Word added: " + text + "\n");
        } else if (e.getSource() == searchButton) {
            List<String> words = trie.getAllWordsWithPrefix(text);
            if (words.isEmpty()) {
                outputArea.append("No words found with prefix: " + text);
            } else {
                outputArea.append("Suggestions: \n");
                for (String word : words) {
                    outputArea.append(word + "\n");
                }
            }
        }
    }
    
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new TrieGUI().setVisible(true));
    }
}