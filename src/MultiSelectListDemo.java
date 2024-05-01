import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.util.HashSet;
import java.util.Set;

public class MultiSelectListDemo extends JPanel {
    private DefaultListModel<String> listModel;
    private JList<String> multiSelectList;
    /* classe de la multiselectbox.
    * nombre masimun d'item à selectionné passé en argument*/
    public MultiSelectListDemo(int maxSelectableItems) {
        setLayout(new BorderLayout());

        listModel = new DefaultListModel<>();
        multiSelectList = new JList<>(listModel);
        multiSelectList.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);

        multiSelectList.addListSelectionListener(new ListSelectionListener() {
            private Set<Integer> selectedIndices = new HashSet<>();

            @Override
            public void valueChanged(ListSelectionEvent e) {
                if (!e.getValueIsAdjusting()) {
                    JList<?> source = (JList<?>) e.getSource();
                    int[] selected = source.getSelectedIndices();

                    if (selected.length > maxSelectableItems) {
                        for (int i = 0; i < selected.length; i++) {
                            if (!selectedIndices.contains(selected[i])) {
                                source.removeSelectionInterval(selected[i], selected[i]);
                            }
                        }
                    } else {
                        for (int i : selected) {
                            selectedIndices.add(i);
                        }
                    }
                }
            }
        });

        JScrollPane scrollPane = new JScrollPane(multiSelectList);
        add(scrollPane, BorderLayout.CENTER);
    }

    public DefaultListModel<String> getListModel() {
        return listModel;
    }

    public JList<String> getMultiSelectList() {
        return multiSelectList;
    }
}
