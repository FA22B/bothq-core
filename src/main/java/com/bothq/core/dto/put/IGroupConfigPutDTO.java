package com.bothq.core.dto.put;

public interface IGroupConfigPutDTO {
    IBaseConfigPutDTO<?, ?>[] getValue();
    void setValue(IBaseConfigPutDTO<?, ?>[] value);
}
