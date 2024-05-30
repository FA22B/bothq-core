package com.bothq.core.dto.put;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;


@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "type")
@JsonSubTypes({
        @JsonSubTypes.Type(value = CheckBoxConfigPutDTO.class, name = "checkbox"),
        @JsonSubTypes.Type(value = ComboBoxConfigPutDTO.class, name = "combobox"),
        @JsonSubTypes.Type(value = GroupConfigPutDTO.class, name = "group"),
        @JsonSubTypes.Type(value = RadioBoxConfigPutDTO.class, name = "radiobox"),
        @JsonSubTypes.Type(value = SliderConfigPutDTO.class, name = "slider"),
        @JsonSubTypes.Type(value = TextBoxConfigPutDTO.class, name = "textbox")
})
public interface IBaseConfigPutDTO<TValue, TComponent> {
    @JsonProperty("uniqueId")
    String getUniqueId();
    @JsonProperty("uniqueId")
    void setUniqueId(String uniqueId);

    TValue getValue();
    void setValue(TValue value);

    void setOn(TComponent component);
}
