package ar.com.api.assetPlatform.model;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class AssetPlatform implements Serializable {
 
 @JsonProperty("id")
 private String id;

 @JsonProperty("chain_identifier") 
 private long chainIdentifier;

 @JsonProperty("name") 
 private String name;

 @JsonProperty("shortname") 
 private String shortname;

}