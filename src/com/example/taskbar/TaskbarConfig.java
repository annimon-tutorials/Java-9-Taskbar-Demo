package com.example.taskbar;

import java.awt.Image;
import java.awt.PopupMenu;
import java.awt.Taskbar;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.Optional;
import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.SpinnerNumberModel;

public final class TaskbarConfig extends JFrame {

    private static final Image DEFAULT_IMAGE = new BufferedImage(32, 32, BufferedImage.TYPE_INT_RGB);

    private final Taskbar taskbar;

    public TaskbarConfig(Taskbar taskbar) {
        super("Taskbar example");
        this.taskbar = taskbar;

        final BoxLayout boxLayout = new BoxLayout(getContentPane(), BoxLayout.PAGE_AXIS);
        setLayout(boxLayout);

        if (taskbar.isSupported(Taskbar.Feature.ICON_BADGE_TEXT)) {
            addIconBadgeTextConfig();
        }
        if (taskbar.isSupported(Taskbar.Feature.ICON_BADGE_NUMBER)) {
            addIconBadgeNumberConfig();
        }
        if (taskbar.isSupported(Taskbar.Feature.ICON_BADGE_IMAGE_WINDOW)) {
            addIconBadgeImageWindowConfig();
        }
        if (taskbar.isSupported(Taskbar.Feature.ICON_IMAGE)) {
            addIconImageConfig();
        }
        if (taskbar.isSupported(Taskbar.Feature.MENU)) {
            addMenuConfig();
        }
        if (taskbar.isSupported(Taskbar.Feature.PROGRESS_STATE_WINDOW)) {
            addProgressStateWindowConfig();
        }
        if (taskbar.isSupported(Taskbar.Feature.PROGRESS_VALUE)) {
            addProgressValueConfig();
        }
        if (taskbar.isSupported(Taskbar.Feature.PROGRESS_VALUE_WINDOW)) {
            addProgressValueWindowConfig();
        }
        if (taskbar.isSupported(Taskbar.Feature.USER_ATTENTION)) {
            addUserAttentionConfig();
        }
        if (taskbar.isSupported(Taskbar.Feature.USER_ATTENTION_WINDOW)) {
            addUserAttentionWindowConfig();
        }
        pack();
        setDefaultCloseOperation(EXIT_ON_CLOSE);
    }

    private void addIconBadgeTextConfig() {
        final JTextField textField;
        textField = new JTextField(10);
        textField.addActionListener(e -> {
            taskbar.setIconBadge(textField.getText());
        });
        add(wrapTitled(textField, "Icon badge text"));
    }

    private void addIconBadgeNumberConfig() {
        final JSpinner spinner;
        spinner = new JSpinner(new SpinnerNumberModel(5, 1, 10, 1));
        spinner.addChangeListener(e -> {
            taskbar.setIconBadge(String.valueOf(spinner.getValue()));
        });
        add(wrapTitled(spinner, "Icon badge number"));
    }

    private void addIconBadgeImageWindowConfig() {
        final JButton button;
        button = new JButton("Icon badge image window");
        button.addActionListener(e -> {
            Image badge = loadImage("/res/cloud-check.png").orElse(DEFAULT_IMAGE);
            taskbar.setWindowIconBadge(this, badge);
        });
        add(wrap(button));
    }

    private void addIconImageConfig() {
        final JButton button;
        button = new JButton("Icon image");
        button.addActionListener(e -> {
            Image icon = loadImage("/res/cloud-check.png").orElse(DEFAULT_IMAGE);
            taskbar.setIconImage(icon);
        });
        add(wrap(button));
    }

    private void addMenuConfig() {
        final JButton button;
        button = new JButton("Menu");
        button.addActionListener(e -> {
            final PopupMenu popup = new PopupMenu();
            popup.add("Item 1");
            popup.add("Item 2");
            popup.add("Item 3");
            taskbar.setMenu(popup);
        });
        add(wrap(button));
    }

    private void addProgressStateWindowConfig() {
        final JComboBox<Taskbar.State> comboBox;
        comboBox = new JComboBox<>(Taskbar.State.values());
        comboBox.addActionListener(e -> {
            taskbar.setWindowProgressState(this, comboBox.getItemAt(comboBox.getSelectedIndex()));
        });
        add(wrapTitled(comboBox, "Progress state window"));
    }

    private void addProgressValueConfig() {
        final JSlider slider;
        slider = new JSlider(JSlider.HORIZONTAL, 0, 100, 0);
        slider.addChangeListener(e -> {
            taskbar.setProgressValue(slider.getValue());
        });
        add(wrapTitled(slider, "Progress value"));
    }

    private void addProgressValueWindowConfig() {
        final JSlider slider;
        slider = new JSlider(JSlider.HORIZONTAL, 0, 100, 0);
        slider.addChangeListener(e -> {
            taskbar.setWindowProgressValue(this, slider.getValue());
        });
        add(wrapTitled(slider, "Progress value window"));
    }

    private void addUserAttentionConfig() {
        final JCheckBox enabled, critical;
        enabled = new JCheckBox("Enabled");
        critical = new JCheckBox("Critical");
        final ActionListener listener = e -> {
            taskbar.requestUserAttention(enabled.isSelected(), critical.isSelected());
        };
        enabled.addActionListener(listener);
        critical.addActionListener(listener);

        final JPanel panel = new JPanel();
        final BoxLayout boxLayout = new BoxLayout(panel, BoxLayout.LINE_AXIS);
        panel.setLayout(boxLayout);
        panel.add(enabled);
        panel.add(critical);
        panel.setBorder(BorderFactory.createTitledBorder("User attention"));
        add(panel);
    }

    private void addUserAttentionWindowConfig() {
        final JButton button;
        button = new JButton("User attention window");
        button.addActionListener(e -> {
            taskbar.requestWindowUserAttention(this);
        });
        add(wrap(button));
    }


    private JPanel wrap(JComponent component) {
        return wrapTitled(component, null);
    }

    private JPanel wrapTitled(JComponent component, String title) {
        final JPanel wrapper = new JPanel();
        wrapper.add(component);
        if (title != null) {
            wrapper.setBorder(BorderFactory.createTitledBorder(title));
        }
        return wrapper;
    }

    private Optional<Image> loadImage(String url) {
        try (InputStream is = getClass().getResourceAsStream(url)) {
            return Optional.ofNullable(ImageIO.read(is));
        } catch (IOException ex) {
            return Optional.empty();
        }
    }
}