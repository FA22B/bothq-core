package com.bothq.core.dto.combobox;

import com.bothq.core.dto.base.IConfigDTO;

public interface IComboBoxConfigDTO extends IConfigDTO {
    int getSelectedIndex();

    java.util.List<Object> getElements();

    void setSelectedIndex(int selectedIndex);

    void setElements(java.util.List<Object> elements);
}
