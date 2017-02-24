package fiszki.common;
import java.util.ArrayList;
import java.util.Objects;

import javax.swing.table.AbstractTableModel;

public class SetTableModel extends AbstractTableModel {
	private static final long serialVersionUID = 2L;
	
	// kolumny
	public static final int FIRST_LANG = 0;
	public static final int SECOND_LANG = 1;
	public static final int KNOWN = 2;
	public static final int COL_COUNT = 3;
	
	private String name = "";
	private String[] columnNames = {"", "", ""};
	private String diskPath = "";
	private ArrayList<Object []> data = new ArrayList<Object []>();
	private boolean edited = true;

    public SetTableModel() {
    }
    
    public SetTableModel(String newName) {
    	name = newName;
    }
    
    public SetTableModel(SetTableModel srcSet) {
    	name = srcSet.name;
    	setColumnsNames(srcSet.columnNames);

    	data = new ArrayList<Object []>();
    	for (Object[] tmp : srcSet.data)
    		addData(tmp);
    }
    
    public void setName(String newName) {
    	name = newName;
    }
    
    public String getName() {
    	return name;
    }
    
    public ArrayList<Object[]> getData() {
    	return data;
    }

    public int getColumnCount() {
        return COL_COUNT;
    }

    public int getRowCount() {
        return data.size();
    }

    public void setColumnsNames(String[] newColumnNames) {
    	columnNames = new String[COL_COUNT];
    	System.arraycopy(newColumnNames, 0, columnNames, 0, COL_COUNT);
    }
    
    public String[] getColumnNames() {
    	String[] colNames = new String[COL_COUNT];
    	System.arraycopy(columnNames, 0, colNames, 0, COL_COUNT);
    	return colNames;
    }
    
    public String getColumnName(int col) {
        return columnNames[col];
    }

    public void addData(Object[] newData) {
    	Object[] toAdd = new Object[COL_COUNT];
    	System.arraycopy(newData, 0, toAdd, 0, COL_COUNT);
		data.add(toAdd);
    }
    
    public Object getValueAt(int row, int col) {
    	return data.get(row)[col];
    }

    public Class<?> getColumnClass(int c) {
        return getValueAt(0, c).getClass();
    }

    public void setValueAt(Object value, int row, int col) {
    	data.get(row)[col] = value;
        fireTableCellUpdated(row, col);
    }
    
    public String toString() {
    	return name;
    }
    
    public void remove(int index) {
    	data.remove(index);
    	fireTableRowsDeleted(index, index);
    }
    
    public Object[] getRow(int idx) {
    	return data.get(idx);
    }
    
    public int getItemIndex(Object[] item) {
    	for (int i = 0; i < data.size(); i++) {
    		if (Objects.equals((String)data.get(i)[FIRST_LANG], (String)item[FIRST_LANG]) &&
    				Objects.equals((String)data.get(i)[SECOND_LANG], (String)item[SECOND_LANG]) &&
    				Objects.equals((boolean)data.get(i)[KNOWN], (boolean)item[KNOWN]))
    			return i;
    	}
    	
    	return -1;
    }
    
    public void setDiskPath(String diskPath) {
    	this.diskPath = diskPath;
    }
    
    public String getDiskPath() {
    	return this.diskPath;
    }
    
    public boolean isEdited() {
    	return this.edited;
    }
    
    public void setEdited(boolean edited) {
    	this.edited = edited;
    }
}