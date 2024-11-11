package org.techArk.responsePOJO;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class GetSingleRepositoryResponse {
    @JsonProperty(value = "full_name")
    public String fullName;
    @JsonProperty(value = "default_branch")
    public String defaultBranch;
    @JsonProperty(value = "message")
    public String message;
    @JsonProperty(value = "visibility")
    public String visibility;
}
