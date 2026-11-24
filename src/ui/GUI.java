/**
 * Nathan Gloman
 * 5/28/26
 * GUI.java
 * The main user interface that replaced ConsoleUI with
 * a visual Swing window. It builds and manages everything
 * that user interacts with.
 *
 * This class was written by generative AI but uses my logic
 * from the console version.
 */
package ui;

import model.Meal;
import model.Recommendation;
import service.MealMatcher;
import service.PantryService;
import storage.PantryStorage;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Collections;
import java.util.List;
import java.util.ArrayList;
import java.util.Set;

public class GUI extends JFrame {

    /*
    Palette
     */
    private static final Color BG        = new Color(0x1A1A2E);
    private static final Color SURFACE   = new Color(0x16213E);
    private static final Color CARD      = new Color(0x0F3460);
    private static final Color ACCENT    = new Color(0xE94560);
    private static final Color ACCENT2   = new Color(0x53D8FB);
    private static final Color TEXT      = new Color(0xEEEEEE);
    private static final Color SUBTEXT   = new Color(0xAAAAAA);
    private static final Color BORDER    = new Color(0x2A2A4A);
    private static final Font  TITLE_F   = new Font("Serif", Font.BOLD, 22);
    private static final Font  LABEL_F   = new Font("SansSerif", Font.BOLD, 13);
    private static final Font  BODY_F    = new Font("SansSerif", Font.PLAIN, 13);
    private static final Font  MONO_F    = new Font("Monospaced", Font.PLAIN, 13);

    /*
    State
     */
    private final List<Meal>     meals;
    private final PantryService  pantryService;
    private final MealMatcher    matcher;
    private final PantryStorage  storage;

    /*
    Widgets
     */
    private DefaultListModel<String> pantryModel;
    private JList<String>            pantryList;
    private JTextField               ingredientField;
    private JPanel                   recPanel;

    public GUI(List<Meal> meals, Set<String> pantry) {
        this.meals         = meals;
        this.pantryService = new PantryService(pantry);
        this.matcher       = new MealMatcher();
        this.storage       = new PantryStorage();

        setTitle("Fridgorithm");
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        setSize(900, 640);
        setMinimumSize(new Dimension(720, 520));
        setLocationRelativeTo(null);
        getContentPane().setBackground(BG);

        // Save pantry on window close
        addWindowListener(new WindowAdapter() {
            @Override public void windowClosing(WindowEvent e) { quit(); }
        });

        buildUI();
        setVisible(true);
    }

    /*
    UI Construction
     */
    private void buildUI() {
        setLayout(new BorderLayout(0, 0));

        add(buildHeader(), BorderLayout.NORTH);

        JSplitPane split = new JSplitPane(
                JSplitPane.HORIZONTAL_SPLIT,
                buildPantryPanel(),
                buildRecsPanel());
        split.setDividerLocation(310);
        split.setDividerSize(6);
        split.setBackground(BG);
        split.setBorder(null);
        split.setContinuousLayout(true);
        add(split, BorderLayout.CENTER);

        add(buildFooter(), BorderLayout.SOUTH);
    }

    /** Dark gradient header bar */
    private JPanel buildHeader() {
        JPanel header = new JPanel(new BorderLayout()) {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                GradientPaint gp = new GradientPaint(0, 0, CARD, getWidth(), 0, SURFACE);
                g2.setPaint(gp);
                g2.fillRect(0, 0, getWidth(), getHeight());
                g2.dispose();
            }
        };
        header.setBorder(new EmptyBorder(16, 24, 16, 24));
        header.setOpaque(false);

        JLabel icon = new JLabel("🍽️");
        icon.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 30));
        header.add(icon, BorderLayout.WEST);

        JPanel titles = new JPanel(new GridLayout(2, 1, 0, 0));
        titles.setOpaque(false);
        titles.setBorder(new EmptyBorder(0, 12, 0, 0));

        JLabel title = new JLabel("Welcome to the Fridgorithm");
        title.setFont(TITLE_F);
        title.setForeground(TEXT);

        JLabel sub = new JLabel("Smart meal recommendations from your pantry");
        sub.setFont(BODY_F);
        sub.setForeground(SUBTEXT);

        titles.add(title);
        titles.add(sub);
        header.add(titles, BorderLayout.CENTER);

        JLabel mealCount = new JLabel(meals.size() + " meals loaded");
        mealCount.setFont(BODY_F);
        mealCount.setForeground(ACCENT2);
        header.add(mealCount, BorderLayout.EAST);

        return header;
    }

    /** Left panel: pantry list + add ingredient */
    private JPanel buildPantryPanel() {
        JPanel panel = new JPanel(new BorderLayout(0, 12));
        panel.setBackground(SURFACE);
        panel.setBorder(new EmptyBorder(16, 16, 16, 12));

        // Section label
        JLabel lbl = sectionLabel("My Pantry");
        panel.add(lbl, BorderLayout.NORTH);

        // List
        pantryModel = new DefaultListModel<>();
        refreshPantryModel();

        pantryList = new JList<>(pantryModel);
        pantryList.setBackground(CARD);
        pantryList.setForeground(TEXT);
        pantryList.setFont(MONO_F);
        pantryList.setSelectionBackground(ACCENT);
        pantryList.setSelectionForeground(Color.WHITE);
        pantryList.setFixedCellHeight(28);
        pantryList.setCellRenderer(new PantryCellRenderer());

        JScrollPane scroll = new JScrollPane(pantryList);
        scroll.setBorder(BorderFactory.createLineBorder(BORDER));
        scroll.setBackground(CARD);
        scroll.getViewport().setBackground(CARD);
        scroll.getVerticalScrollBar().setUI(new DarkScrollBarUI());
        panel.add(scroll, BorderLayout.CENTER);

        // Add ingredient area
        JPanel addArea = new JPanel(new BorderLayout(8, 0));
        addArea.setBackground(SURFACE);
        addArea.setBorder(new EmptyBorder(8, 0, 0, 0));

        ingredientField = new JTextField();
        ingredientField.setFont(BODY_F);
        ingredientField.setBackground(CARD);
        ingredientField.setForeground(TEXT);
        ingredientField.setCaretColor(ACCENT2);
        ingredientField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(BORDER),
                new EmptyBorder(6, 10, 6, 10)));
        ingredientField.putClientProperty("JTextField.placeholderText", "e.g. garlic");

        // Add on Enter
        ingredientField.addActionListener(e -> addIngredient());

        JButton addBtn = accentButton("Add", ACCENT);
        addBtn.addActionListener(e -> addIngredient());

        // Remove selected button
        JButton removeBtn = accentButton("Remove", new Color(0x3A3A5A));
        removeBtn.addActionListener(e -> removeIngredient());

        JPanel btnRow = new JPanel(new GridLayout(1, 2, 6, 0));
        btnRow.setOpaque(false);
        btnRow.add(addBtn);
        btnRow.add(removeBtn);

        addArea.add(ingredientField, BorderLayout.CENTER);
        addArea.add(btnRow, BorderLayout.SOUTH);

        // Wrap them in a south panel
        JPanel south = new JPanel(new BorderLayout(0, 6));
        south.setBackground(SURFACE);
        JLabel addLbl = new JLabel("Add ingredient:");
        addLbl.setFont(LABEL_F);
        addLbl.setForeground(SUBTEXT);
        south.add(addLbl, BorderLayout.NORTH);
        south.add(addArea, BorderLayout.CENTER);

        panel.add(south, BorderLayout.SOUTH);
        return panel;
    }

    /** Right panel: recommendations */
    private JPanel buildRecsPanel() {
        JPanel outer = new JPanel(new BorderLayout(0, 12));
        outer.setBackground(BG);
        outer.setBorder(new EmptyBorder(16, 12, 16, 16));

        // Header row
        JPanel topRow = new JPanel(new BorderLayout());
        topRow.setOpaque(false);
        topRow.add(sectionLabel("✨  Recommendations"), BorderLayout.WEST);

        JButton recBtn = accentButton("  Find Meals  ", ACCENT);
        recBtn.setFont(new Font("SansSerif", Font.BOLD, 13));
        recBtn.addActionListener(e -> recommend());
        topRow.add(recBtn, BorderLayout.EAST);
        outer.add(topRow, BorderLayout.NORTH);

        // Cards container (scrollable)
        recPanel = new JPanel();
        recPanel.setLayout(new BoxLayout(recPanel, BoxLayout.Y_AXIS));
        recPanel.setBackground(BG);

        // Placeholder hint
        showPlaceholder();

        JScrollPane scroll = new JScrollPane(recPanel);
        scroll.setBorder(null);
        scroll.setBackground(BG);
        scroll.getViewport().setBackground(BG);
        scroll.getVerticalScrollBar().setUI(new DarkScrollBarUI());
        outer.add(scroll, BorderLayout.CENTER);

        return outer;
    }

    /** Footer with quit button */
    private JPanel buildFooter() {
        JPanel footer = new JPanel(new FlowLayout(FlowLayout.RIGHT, 16, 8));
        footer.setBackground(SURFACE);
        footer.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, BORDER));

        JLabel hint = new JLabel("Pantry is auto-saved on exit");
        hint.setFont(BODY_F);
        hint.setForeground(SUBTEXT);
        footer.add(hint);

        JButton quitBtn = accentButton("Save & Quit", new Color(0x3A3A5A));
        quitBtn.addActionListener(e -> quit());
        footer.add(quitBtn);

        return footer;
    }

    // ─────────────────────────────────────────────────────────────────────────
    //  Actions
    // ─────────────────────────────────────────────────────────────────────────

    private void addIngredient() {
        String text = ingredientField.getText().trim();
        if (text.isEmpty()) return;

        // Support comma-separated entries
        String[] parts = text.split(",");
        for (String part : parts) {
            String ing = part.trim();
            if (!ing.isEmpty()) pantryService.addIngredient(ing);
        }

        ingredientField.setText("");
        refreshPantryModel();
    }

    private void removeIngredient() {
        String selected = pantryList.getSelectedValue();
        if (selected == null) return;
        pantryService.getPantry().remove(selected);
        refreshPantryModel();
    }

    private void recommend() {
        List<Recommendation> ranked = matcher.rankMeals(meals, pantryService.getPantry());

        recPanel.removeAll();

        if (ranked.isEmpty()) {
            showPlaceholder();
            return;
        }

        int top = Math.min(5, ranked.size());
        for (int i = 0; i < top; i++) {
            recPanel.add(buildRecCard(i + 1, ranked.get(i)));
            recPanel.add(Box.createVerticalStrut(10));
        }

        recPanel.revalidate();
        recPanel.repaint();
    }

    private void quit() {
        storage.savePantry(pantryService.getPantry());
        dispose();
        System.exit(0);
    }

    // ─────────────────────────────────────────────────────────────────────────
    //  Helpers
    // ─────────────────────────────────────────────────────────────────────────

    private void refreshPantryModel() {
        pantryModel.clear();
        List<String> sorted = new ArrayList<>(pantryService.getPantry());
        Collections.sort(sorted);
        for (String item : sorted) pantryModel.addElement(item);
    }

    private void showPlaceholder() {
        recPanel.removeAll();
        JLabel ph = new JLabel("<html><center>Click <b>Find Meals</b> to see<br>what you can cook!</center></html>");
        ph.setForeground(SUBTEXT);
        ph.setFont(BODY_F);
        ph.setAlignmentX(Component.CENTER_ALIGNMENT);
        ph.setBorder(new EmptyBorder(60, 0, 0, 0));
        recPanel.add(ph);
        recPanel.revalidate();
        recPanel.repaint();
    }

    /** Build a single recommendation card */
    private JPanel buildRecCard(int rank, Recommendation r) {
        double pct = r.getScore() * 100;
        Color barColor = pct >= 75 ? new Color(0x2ECC71)
                : pct >= 40 ? new Color(0xF39C12)
                : new Color(0xE74C3C);

        JPanel card = new JPanel(new BorderLayout(0, 8)) {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(CARD);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 12, 12);
                g2.setColor(barColor);
                g2.fillRoundRect(0, 0, 5, getHeight(), 4, 4);
                g2.dispose();
            }
        };
        card.setOpaque(false);
        card.setBorder(new EmptyBorder(14, 20, 14, 20));
        card.setMaximumSize(new Dimension(Integer.MAX_VALUE, 160));

        // Top row: rank + name + prep time + score
        JPanel topRow = new JPanel(new BorderLayout(10, 0));
        topRow.setOpaque(false);

        JLabel rankLbl = new JLabel("#" + rank);
        rankLbl.setFont(new Font("Serif", Font.BOLD, 20));
        rankLbl.setForeground(barColor);
        rankLbl.setPreferredSize(new Dimension(36, 24));
        topRow.add(rankLbl, BorderLayout.WEST);

        JLabel nameLbl = new JLabel(r.getMeal().getName());
        nameLbl.setFont(new Font("Serif", Font.BOLD, 17));
        nameLbl.setForeground(TEXT);
        topRow.add(nameLbl, BorderLayout.CENTER);

        // Prep time + score grouped on the right
        JPanel rightGroup = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        rightGroup.setOpaque(false);

        JLabel prepLbl = new JLabel("⏱ " + r.getMeal().getPrepTime() + " mins");
        prepLbl.setFont(BODY_F);
        prepLbl.setForeground(SUBTEXT);
        rightGroup.add(prepLbl);

        JLabel scoreLbl = new JLabel(Math.round(pct) + "%");
        scoreLbl.setFont(LABEL_F);
        scoreLbl.setForeground(barColor);
        rightGroup.add(scoreLbl);

        topRow.add(rightGroup, BorderLayout.EAST);
        card.add(topRow, BorderLayout.NORTH);

        // Progress bar
        JPanel barRow = new JPanel(new BorderLayout(0, 4)) {
            @Override protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                int w = getWidth();
                int h = 6;
                int y = (getHeight() - h) / 2;
                g2.setColor(BORDER);
                g2.fillRoundRect(0, y, w, h, h, h);
                int fill = (int)(w * r.getScore());
                if (fill > 0) {
                    g2.setColor(barColor);
                    g2.fillRoundRect(0, y, fill, h, h, h);
                }
                g2.dispose();
            }
        };
        barRow.setOpaque(false);
        barRow.setPreferredSize(new Dimension(0, 18));
        card.add(barRow, BorderLayout.CENTER);

        // Missing ingredients
        JPanel bottom = new JPanel(new BorderLayout());
        bottom.setOpaque(false);

        Set<String> missing = r.getMissingIngredients();
        String missingText = missing.isEmpty()
                ? "✅ You have everything!"
                : "🛒 Need: " + String.join(", ", missing);
        JLabel missingLbl = new JLabel("<html><body style='width:320px'>" + missingText + "</body></html>");
        missingLbl.setFont(BODY_F);
        missingLbl.setForeground(missing.isEmpty() ? new Color(0x2ECC71) : SUBTEXT);
        bottom.add(missingLbl, BorderLayout.CENTER);

        card.add(bottom, BorderLayout.SOUTH);

        return card;
    }

    private JLabel sectionLabel(String text) {
        JLabel lbl = new JLabel(text);
        lbl.setFont(LABEL_F);
        lbl.setForeground(ACCENT2);
        lbl.setBorder(new EmptyBorder(0, 0, 8, 0));
        return lbl;
    }

    private JButton accentButton(String text, Color bg) {
        JButton btn = new JButton(text) {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(getModel().isPressed() ? bg.darker() :
                        getModel().isRollover() ? bg.brighter() : bg);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 8, 8);
                g2.dispose();
                super.paintComponent(g);
            }
        };
        btn.setFont(LABEL_F);
        btn.setForeground(TEXT);
        btn.setBackground(bg);
        btn.setBorder(new EmptyBorder(8, 16, 8, 16));
        btn.setFocusPainted(false);
        btn.setContentAreaFilled(false);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        return btn;
    }

    // ─────────────────────────────────────────────────────────────────────────
    //  Custom cell renderer for pantry list
    // ─────────────────────────────────────────────────────────────────────────

    private static class PantryCellRenderer extends DefaultListCellRenderer {
        @Override
        public Component getListCellRendererComponent(
                JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
            JLabel lbl = (JLabel) super.getListCellRendererComponent(
                    list, value, index, isSelected, cellHasFocus);
            lbl.setText("  · " + value);
            lbl.setFont(MONO_F);
            lbl.setForeground(isSelected ? Color.WHITE : TEXT);
            lbl.setBackground(isSelected ? ACCENT : (index % 2 == 0 ? CARD : new Color(0x142850)));
            lbl.setBorder(new EmptyBorder(2, 6, 2, 6));
            return lbl;
        }
    }

    // ─────────────────────────────────────────────────────────────────────────
    //  Minimal dark scrollbar
    // ─────────────────────────────────────────────────────────────────────────

    private static class DarkScrollBarUI extends javax.swing.plaf.basic.BasicScrollBarUI {
        @Override protected void configureScrollBarColors() {
            thumbColor = new Color(0x3A3A6A);
            trackColor = SURFACE;
        }
        @Override protected JButton createDecreaseButton(int o) { return zeroButton(); }
        @Override protected JButton createIncreaseButton(int o) { return zeroButton(); }
        private JButton zeroButton() {
            JButton b = new JButton();
            b.setPreferredSize(new Dimension(0, 0));
            return b;
        }
    }
}