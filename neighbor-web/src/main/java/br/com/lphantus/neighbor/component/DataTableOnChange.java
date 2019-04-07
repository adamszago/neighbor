package br.com.lphantus.neighbor.component;

import org.primefaces.component.datatable.DataTable;

public class DataTableOnChange extends DataTable {

	@Override
	public String getFilterEvent() {
		return "change";
	}

}
