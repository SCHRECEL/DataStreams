import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import java.nio.file.*;
import java.util.stream.Stream;

public class DataStreams extends JFrame {
    private JTextArea originalTextArea;
    private JTextArea filteredTextArea;
    private JTextField searchField;

    private Path filePath;

    public DataStreams() {
        setTitle("DataStreams");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        originalTextArea = new JTextArea();
        filteredTextArea = new JTextArea();
        searchField = new JTextField(20);

        JButton loadButton = new JButton("Load File");
        loadButton.addActionListener(new LoadButtonListener());

        JButton searchButton = new JButton("Search");
        searchButton.addActionListener(new SearchButtonListener());

        JButton quitButton = new JButton("Quit");
        quitButton.addActionListener(e -> System.exit(0));

        JPanel controlPanel = new JPanel();
        controlPanel.add(loadButton);
        controlPanel.add(searchField);
        controlPanel.add(searchButton);
        controlPanel.add(quitButton);

        JScrollPane originalScrollPane = new JScrollPane(originalTextArea);
        JScrollPane filteredScrollPane = new JScrollPane(filteredTextArea);

        JPanel textPanel = new JPanel(new GridLayout(1, 2));
        textPanel.add(originalScrollPane);
        textPanel.add(filteredScrollPane);

        getContentPane().setLayout(new BorderLayout());
        getContentPane().add(controlPanel, BorderLayout.NORTH);
        getContentPane().add(textPanel, BorderLayout.CENTER);
    }

    private class LoadButtonListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            JFileChooser fileChooser = new JFileChooser();
            int returnValue = fileChooser.showOpenDialog(DataStreams.this);
            if (returnValue == JFileChooser.APPROVE_OPTION) {
                filePath = fileChooser.getSelectedFile().toPath();
                try {
                    Stream<String> lines = Files.lines(filePath);
                    lines.forEach(line -> originalTextArea.append(line + "\n"));
                    lines.close();
                } catch (IOException ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(DataStreams.this, "Error loading file: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        }
    }

    private class SearchButtonListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            if (filePath != null) {
                filteredTextArea.setText("");
                String searchString = searchField.getText();
                try {
                    Stream<String> lines = Files.lines(filePath);
                    lines.filter(line -> line.contains(searchString))
                            .forEach(line -> filteredTextArea.append(line + "\n"));
                    lines.close();
                } catch (IOException ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(DataStreams.this, "Error searching file: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }
            } else {
                JOptionPane.showMessageDialog(DataStreams.this, "Load a file first", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new DataStreams().setVisible(true));
    }
}
