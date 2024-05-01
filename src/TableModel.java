import javax.swing.table.AbstractTableModel;

public class TableModel extends AbstractTableModel {
    private String[] columnNames ; // Noms des colonnes
    private Object[][] data; // Données du tableau

    public TableModel(String[] columnNames,Object[][] data) {
        this.columnNames = columnNames;
        this.data = data;

    }

    @Override
    public int getRowCount() {
        return data.length;
    }

    @Override
    public int getColumnCount() {
        return columnNames.length;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        return data[rowIndex][columnIndex];
    }

    @Override
    public String getColumnName(int column) {
        return columnNames[column];
    }

    public void setData(Object[][] newData) {
        this.data = newData;
        fireTableDataChanged();
    }
}

