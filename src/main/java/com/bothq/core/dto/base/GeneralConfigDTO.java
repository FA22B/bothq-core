package com.bothq.core.dto.base;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@AllArgsConstructor
@ToString
@EqualsAndHashCode
public class GeneralConfigDTO implements IConfigDTO {
    @Getter(onMethod_ = {@JsonProperty("type")})
    @Setter(onMethod_ = {@JsonProperty("type")})
    private String type;
    @Getter(onMethod_ = {@JsonProperty("uniqueId")})
    @Setter(onMethod_ = {@JsonProperty("uniqueId")})
    private String uniqueID;

    @Getter(onMethod_ = {@JsonProperty("displayName")})
    @Setter(onMethod_ = {@JsonProperty("displayName")})
    private String displayName;
}
