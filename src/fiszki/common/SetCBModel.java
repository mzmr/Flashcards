package fiszki.common;
import java.util.ArrayList;

import javax.swing.AbstractListModel;
import javax.swing.ComboBoxModel;

public class SetCBModel extends AbstractListModel<SetTableModel> implements ComboBoxModel<SetTableModel> {
	private static final long serialVersionUID = 3L;
	
	private ArrayList<SetTableModel> data;
	public SetTableModel selection = null;
	
	public SetCBModel() {
		data = new ArrayList<SetTableModel>();
	}
	
	public int getSize() {
	    return data.size();
	}
	
	public boolean isEmpty() {
		return (data.size() == 0);
	}
	
	public void addData(SetTableModel newData) {
		data.add(new SetTableModel(newData));
	}
	
	public SetTableModel getElementAt(int idx) {
		return data.get(idx);
	}
	
	public void setSelectedItem(Object anItem) {
		selection = (SetTableModel) anItem;
	}
	
	public SetTableModel getSelectedItem() {
	    return selection;
	}
	
	public void remove(int index) {
		data.remove(index);
	}
	
	public String toString() {
		String str = "";
		
		for (SetTableModel el : data) {
			str += el.toString();
			str += "\n";
		}
		
		return str;
    }
}